# Troubleshooting: Updated Hotspots Not Showing

## Issue

When business owners update their profiles (description, images, location, Google Maps URL), travellers are not seeing these updates when searching for hotels.

## Backend Implementation Status ✅

The backend code is correctly implemented to:

1. ✅ Fetch fresh data from MongoDB on every request (no caching)
2. ✅ Include profile data (images, description) in search results
3. ✅ Include updated location from BusinessOwner entity
4. ✅ Search includes profile descriptions

## Troubleshooting Steps

### 1. Verify MongoDB is Running

**Check if MongoDB is running:**

```powershell
netstat -an | findstr ":27017"
```

If nothing is returned, MongoDB is not running. Start it:

```powershell
# Option 1: If installed as Windows service
net start MongoDB

# Option 2: If using Docker
docker start mongodb
# or
docker run -d -p 27017:27017 --name mongodb mongo

# Option 3: If installed locally
mongod --dbpath "C:\data\db"
```

### 2. Verify Data is Being Saved

**Test the profile update endpoint:**

```bash
PUT /api/business-owners/{email}/profile
Content-Type: application/json

{
  "description": "Test description",
  "location": "Updated Location",
  "googleMapsUrl": "https://maps.google.com/...",
  "imageUrls": ["https://example.com/image.jpg"]
}
```

**Check the response** - it should return the updated profile data.

### 3. Verify Data is Being Retrieved

**Test the hotspot search endpoint:**

```bash
GET /api/hotspots?search=test
```

**Check the response:**

- `items[].previewImage` should show the first image from the profile
- `items[].location` should show the updated location
- Search should find businesses by description keywords

**Test the business details endpoint:**

```bash
GET /api/hotspots/{businessId}
```

**Check the response:**

- `description` should show the updated description
- `googleMapsUrl` should show the updated URL
- `images` should show all profile images
- `location` should show the updated location

### 4. Frontend Caching Issues

The backend returns fresh data on every request, but the frontend might be caching responses.

**Solutions:**

1. **Add cache-busting query parameter:**

   ```javascript
   fetch(`/api/hotspots?search=${search}&_t=${Date.now()}`);
   ```

2. **Set proper cache headers in frontend:**

   ```javascript
   fetch("/api/hotspots", {
     headers: {
       "Cache-Control": "no-cache",
       Pragma: "no-cache",
     },
   });
   ```

3. **Force refresh after profile updates:**
   - After a business owner updates their profile, clear any cached hotspot data
   - Refetch hotspot data when travellers navigate to the search page

### 5. Verify Database Collections

**Check MongoDB collections:**

```javascript
// Connect to MongoDB
use Traveller

// Check BusinessOwner collection
db.BusinessOwner.find().pretty()

// Check BusinessOwnerProfile collection
db.BusinessOwnerProfile.find().pretty()
```

**Verify:**

- BusinessOwner documents have updated `location` field
- BusinessOwnerProfile documents exist with `ownerId` matching BusinessOwner `_id`
- BusinessOwnerProfile has `description`, `googleMapsUrl`, and `imageUrls` fields populated

### 6. Check Application Logs

**Look for errors in Spring Boot logs:**

- MongoDB connection errors
- Repository query errors
- Null pointer exceptions

**Common errors:**

- `Connection refused` - MongoDB not running
- `Collection not found` - Collections don't exist (first signup will create them)
- `Index error` - MongoDB index issues

### 7. Test the Complete Flow

**Step-by-step test:**

1. **Business Owner Updates Profile:**

   ```bash
   PUT /api/business-owners/owner@innovecorp.com/profile
   {
     "description": "Beautiful beachfront hotel",
     "location": "Beach Road, Mirissa",
     "googleMapsUrl": "https://maps.google.com/...",
     "imageUrls": ["https://example.com/beach.jpg"]
   }
   ```

2. **Verify Update Succeeded:**

   ```bash
   GET /api/business-owners/owner@innovecorp.com/profile
   ```

   Should return the updated data.

3. **Traveller Searches:**

   ```bash
   GET /api/hotspots?search=beach
   ```

   Should find the business and show:

   - Updated location: "Beach Road, Mirissa"
   - Preview image: "https://example.com/beach.jpg"
   - Search should match "beautiful beachfront hotel" in description

4. **Traveller Views Details:**
   ```bash
   GET /api/hotspots/{businessId}
   ```
   Should show:
   - Description: "Beautiful beachfront hotel"
   - Location: "Beach Road, Mirissa"
   - Google Maps URL
   - All images

## Expected Behavior

### When Business Owner Updates Profile:

1. **Location Update:**

   - Updates `BusinessOwner.location` field
   - Immediately visible in `/api/hotspots` list
   - Immediately visible in `/api/hotspots/{id}` details

2. **Description Update:**

   - Updates `BusinessOwnerProfile.description` field
   - Searchable via `/api/hotspots?search=...`
   - Visible in `/api/hotspots/{id}` details

3. **Image Updates:**

   - Updates `BusinessOwnerProfile.imageUrls` array
   - First image shows as `previewImage` in `/api/hotspots` list
   - All images show in `/api/hotspots/{id}` details

4. **Google Maps URL Update:**
   - Updates `BusinessOwnerProfile.googleMapsUrl` field
   - Visible in `/api/hotspots/{id}` details

## Code Verification

The backend code correctly:

- ✅ Uses `BusinessOwnerRepository` (not the old `AppBusinessOwnerRepository`)
- ✅ Uses `BusinessOwnerProfileRepository` (not the old `BusinessProfileRepository`)
- ✅ Fetches profiles for each owner in `toHotspotResponse()`
- ✅ Includes profile data in `getBusinessDetails()`
- ✅ Searches profile descriptions in `filterSearch()`
- ✅ Updates location in `BusinessOwnerProfileService.upsertProfile()`

## If Issues Persist

1. **Restart Spring Boot Application:**

   ```powershell
   # Stop current instance (Ctrl+C)
   # Restart:
   cd c:\Users\User\trillo\innovesync
   .\mvnw spring-boot:run
   ```

2. **Clear Browser Cache:**

   - Hard refresh: `Ctrl+Shift+R` (Windows) or `Cmd+Shift+R` (Mac)
   - Clear browser cache
   - Use incognito/private mode to test

3. **Check Network Tab:**

   - Open browser DevTools → Network tab
   - Verify API calls are being made
   - Check response data matches expected format
   - Verify no 304 (Not Modified) responses

4. **Verify API Endpoints:**
   - Test endpoints directly with Postman or curl
   - Compare responses before and after profile updates
   - Verify data is actually changing in the database

## Quick Test Script

```bash
# 1. Update profile
curl -X PUT http://localhost:8080/api/business-owners/owner@innovecorp.com/profile \
  -H "Content-Type: application/json" \
  -d '{"description": "Test update", "location": "New Location"}'

# 2. Verify update
curl http://localhost:8080/api/business-owners/owner@innovecorp.com/profile

# 3. Search hotspots
curl "http://localhost:8080/api/hotspots?search=test"

# 4. Get details
curl http://localhost:8080/api/hotspots/{businessId}
```

Replace `{businessId}` with the actual ID from step 2.
