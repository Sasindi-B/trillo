# InnoveSync API Documentation

**Base URL:** `http://localhost:8080`

**Note:** All endpoints support both `/api/...` and `/...` paths (e.g., `/api/auth/login` and `/auth/login` both work)

---

## 1. Authentication

### `POST /api/auth/login` or `POST /auth/login`

User login (traveller or business owner)

**Request Body:**

```json
{
  "email": "string (required - can also use 'username' field)",
  "password": "string (required)"
}
```

**Response (200):**

```json
{
  "message": "Logged in",
  "token": "jwt-token-string",
  "user": {
    "id": "uuid-or-email",
    "email": "user@example.com",
    "role": "traveller" | "business" | "companyadmin",
    "name": "Display Name"
  }
}
```

**Error Responses:**

- `400 Bad Request`: Validation error (missing email/password)
- `401 Unauthorized`: Invalid credentials

**Example:**

```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "traveller@innovecorp.com",
  "password": "Traveller#2025"
}
```

---

## 2. Travellers

### `POST /api/travellers/signup` or `POST /travellers/signup`

Create a new traveller account

**Request Body:**

```json
{
  "fullName": "string (required)",
  "email": "string (required, valid email)",
  "password": "string (required, min 8 characters)",
  "confirmPassword": "string (required, must match password)",
  "city": "string (required)",
  "interests": "string (optional, comma-separated)"
}
```

**Response (201):**

```json
{
  "id": "uuid",
  "fullName": "string",
  "email": "string",
  "city": "string",
  "interests": ["string"]
}
```

**Error Responses:**

- `400 Bad Request`: Validation error or email already registered

---

### `GET /api/travellers/{idOrEmail}/settings` or `GET /travellers/{idOrEmail}/settings`

Get traveller settings

**Response (200):**

```json
{
  "emailAlertsEnabled": true,
  "smsAlertsEnabled": false,
  "pushNotificationsEnabled": true,
  "digestCadence": "daily",
  "profileVisibility": "public",
  "dataSharingEnabled": true,
  "autoSaveTripsEnabled": true,
  "language": "en",
  "currency": "USD",
  "timezone": "UTC",
  "travelVibes": ["adventure", "relaxation"]
}
```

---

### `PUT /api/travellers/{idOrEmail}/settings` or `PUT /travellers/{idOrEmail}/settings`

Update traveller settings

**Request Body:** Same structure as GET response

**Response (200):** Same as GET response

---

## 3. Business Owners

### `POST /api/business-owners/signup` or `POST /business-owners/signup`

Create a new business owner account

**Request Body:**

```json
{
  "businessName": "string (required)",
  "email": "string (required, valid email)",
  "password": "string (required, min 8 characters)",
  "confirmPassword": "string (required, must match password)",
  "location": "string (required)",
  "category": "string (optional)"
}
```

**Response (201):**

```json
{
  "id": "uuid",
  "businessName": "string",
  "email": "string",
  "location": "string",
  "category": "string"
}
```

---

### `GET /api/business-owners/{idOrEmail}/profile` or `GET /business-owners/{idOrEmail}/profile`

Get business owner profile

**Response (200):**

```json
{
  "profileId": "uuid",
  "ownerId": "uuid",
  "businessName": "string",
  "email": "string",
  "location": "string",
  "category": "string",
  "description": "string",
  "googleMapsUrl": "string",
  "imageUrls": ["string"],
  "imageCount": 0
}
```

---

### `PUT /api/business-owners/{idOrEmail}/profile` or `PUT /business-owners/{idOrEmail}/profile`

Update business owner profile (includes location, description, images, Google Maps URL)

**Request Body:**

```json
{
  "description": "string (optional, max 500 chars)",
  "location": "string (optional, max 200 chars)",
  "googleMapsUrl": "string (optional, max 2048 chars)",
  "imageUrls": ["string"] (optional, max 6 images)
}
```

**Response (200):** Same as GET profile response

**Note:** The `location` field in the request updates the business owner's location in the database, which will be visible to travellers when they search.

---

### `POST /api/business-owners/{idOrEmail}/profile/images` or `POST /business-owners/{idOrEmail}/profile/images`

Add images to business profile

**Request Body:**

```json
{
  "imageUrls": ["string"] (required, max 6 total images)
}
```

**Response (200):** Same as GET profile response

---

### `POST /api/business-owners/{idOrEmail}/profile/images/upload` or `POST /business-owners/{idOrEmail}/profile/images/upload`

Upload image files to business profile

**Request:** `multipart/form-data` with `files` field (array of files)

**Response (200):** Same as GET profile response

---

### `DELETE /api/business-owners/{idOrEmail}/profile/images?url={imageUrl}` or `DELETE /business-owners/{idOrEmail}/profile/images?url={imageUrl}`

Remove an image from business profile

**Response (200):** Same as GET profile response

---

### `GET /api/business-owners/{idOrEmail}/settings` or `GET /business-owners/{idOrEmail}/settings`

Get business owner settings

**Response (200):** Settings object (same structure as traveller settings)

---

### `PUT /api/business-owners/{idOrEmail}/settings` or `PUT /business-owners/{idOrEmail}/settings`

Update business owner settings

**Request Body:** Settings object

**Response (200):** Updated settings object

---

## 4. Hotspots (Hotel/Business Search)

### `GET /api/hotspots` or `GET /hotspots`

Search and list hotels/businesses (includes updated profile data from business owners)

**Query Parameters:**

- `category` (optional): Filter by category
- `search` (optional): Search in business name, category, location, and profile description
- `page` (optional, default: 0): Page number (0-indexed)
- `size` (optional, default: 10): Items per page

**Response (200):**

```json
{
  "items": [
    {
      "id": "string",
      "businessName": "string",
      "category": "string",
      "location": "string",
      "previewImage": "string (first image from profile, or empty)",
      "businessOwnerId": "string",
      "createdAt": "ISO timestamp"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10
}
```

**Note:**

- Search includes profile descriptions updated by business owners
- Preview images come from business owner profiles
- Location reflects updates made by business owners

**Example:**

```bash
GET /api/hotspots?search=beach&category=Hotels&page=0&size=10
```

---

### `GET /api/hotspots/{businessId}` or `GET /hotspots/{businessId}`

Get detailed information about a specific business/hotel

**Response (200):**

```json
{
  "businessId": "string",
  "businessName": "string",
  "category": "string",
  "location": "string (updated by business owner)",
  "description": "string (from business owner profile)",
  "googleMapsUrl": "string (from business owner profile)",
  "images": ["string"] (up to 6 images from business owner profile),
  "contactEmail": "string",
  "contactName": "string",
  "freeTimeSlots": []
}
```

**Note:** This endpoint returns all profile data updated by business owners, including:

- Updated location
- Description
- Google Maps URL
- Images (up to 6)

---

## 5. Error Responses

All endpoints may return the following error responses:

### `400 Bad Request`

```json
{
  "timestamp": "ISO timestamp",
  "status": 400,
  "error": "Validation error" | "Bad request",
  "message": "Error description"
}
```

### `401 Unauthorized`

```json
{
  "timestamp": "ISO timestamp",
  "status": 401,
  "error": "Invalid credentials",
  "message": "Error description"
}
```

### `404 Not Found`

```json
{
  "timestamp": "ISO timestamp",
  "status": 404,
  "error": "Not found",
  "message": "Resource not found"
}
```

### `503 Service Unavailable`

```json
{
  "timestamp": "ISO timestamp",
  "status": 503,
  "error": "Database error",
  "message": "Database connection error. Please ensure MongoDB is running on localhost:27017"
}
```

---

## 6. Authentication

Most endpoints (except login, signup, hotspots GET, and settings) require authentication.

**Authentication Header:**

```
Authorization: Bearer {token}
```

The token is returned in the login response.

---

## 7. CORS

CORS is enabled for:

- `http://localhost:5173`
- `http://127.0.0.1:5173`

All endpoints support OPTIONS requests for CORS preflight.

---

## 8. Built-in Test Accounts

For testing without signup:

1. **Traveller:**

   - Email: `traveller@innovecorp.com`
   - Password: `Traveller#2025`

2. **Business Owner:**

   - Email: `owner@innovecorp.com`
   - Password: `Owner#2025`

3. **Admin:**
   - Email: `admin@innovecorp.com`
   - Password: `Admin#2025`

---

## 9. Important Notes

1. **Profile Updates:** When business owners update their profile (description, images, location, Google Maps URL), these changes are immediately visible to travellers when they search for hotels.

2. **Search Functionality:** The search endpoint searches across:

   - Business name
   - Category
   - Location
   - Profile description (updated by business owners)

3. **Image URLs:** Image URLs in profile responses may be:

   - External URLs (if provided by business owner)
   - Internal URLs like `/api/business-owners/{id}/profile/images/{fileId}` (for uploaded images)

4. **Location Updates:** Business owners can update their location via the profile endpoint, and this updated location will be shown to travellers.

5. **Database:** MongoDB must be running on `localhost:27017` for signup and profile features to work. Login with built-in accounts works without MongoDB.
