# Membership API Documentation

## Overview

The Membership API allows Business Owners to view, purchase, and manage membership packages (Silver and Gold). Only authenticated Business Owners can access these endpoints.

**Base URL:** `http://localhost:8080`

---

## 1. View Available Packages

### `GET /api/memberships/packages`

Get list of available membership packages with prices and features.

**Authentication:** Required (Business Owner role)

**Response (200):**

```json
[
  {
    "type": "SILVER",
    "displayName": "Silver Membership",
    "price": 29.99,
    "features": [
      "Limited Monthly Analytics",
      "Upload up to X Images",
      "Limited Promotion Visibility",
      "Basic Customer Engagement Features"
    ]
  },
  {
    "type": "GOLD",
    "displayName": "Gold Membership",
    "price": 79.99,
    "features": [
      "Unlimited Analytics",
      "Unlimited Images",
      "Priority Promotion Visibility",
      "Featured Listing on Category Pages",
      "Advanced Analytics Dashboard",
      "Direct Messaging Priority",
      "Basic Customer Engagement Features"
    ]
  }
]
```

**Error Responses:**

- `401 Unauthorized`: Not authenticated
- `403 Forbidden`: Not a Business Owner

**Example:**

```bash
GET /api/memberships/packages
Authorization: Bearer {token}
```

---

## 2. Purchase Membership

### `POST /api/memberships/purchase`

Purchase or upgrade a membership package.

**Authentication:** Required (Business Owner role)

**Request Body:**

```json
{
  "businessOwnerId": "string (required)",
  "packageType": "SILVER" | "GOLD" (required)
}
```

**Response (200):**

```json
{
  "id": "membership-id",
  "businessOwnerId": "business-owner-id",
  "packageType": "GOLD",
  "displayName": "Gold Membership",
  "features": [
    "Unlimited Analytics",
    "Unlimited Images",
    "Priority Promotion Visibility",
    "Featured Listing on Category Pages",
    "Advanced Analytics Dashboard",
    "Direct Messaging Priority",
    "Basic Customer Engagement Features"
  ],
  "startDate": "2024-01-15T10:00:00Z",
  "expiryDate": "2024-02-14T10:00:00Z",
  "isActive": true,
  "createdAt": "2024-01-15T10:00:00Z"
}
```

**Business Logic:**

- **New Membership:** Creates a new 30-day membership
- **Same Package:** Extends existing membership by 30 days from current expiry date
- **Upgrade (Silver → Gold):** Upgrades immediately, starts new 30-day period
- **Downgrade:** Not allowed - must wait for current membership to expire

**Error Responses:**

- `400 Bad Request`:
  - Business owner not found
  - Cannot downgrade membership
  - Validation errors
- `401 Unauthorized`: Not authenticated
- `403 Forbidden`: Not a Business Owner

**Example:**

```bash
POST /api/memberships/purchase
Authorization: Bearer {token}
Content-Type: application/json

{
  "businessOwnerId": "507f1f77bcf86cd799439011",
  "packageType": "GOLD"
}
```

---

## 3. Get Current Membership

### `GET /api/memberships/{businessOwnerId}/current`

Get the current active membership for a business owner.

**Authentication:** Required (Business Owner role)

**Path Parameters:**

- `businessOwnerId` (required): The business owner's ID

**Response (200):**

```json
{
  "id": "membership-id",
  "businessOwnerId": "business-owner-id",
  "packageType": "GOLD",
  "displayName": "Gold Membership",
  "features": [
    "Unlimited Analytics",
    "Unlimited Images",
    "Priority Promotion Visibility",
    "Featured Listing on Category Pages",
    "Advanced Analytics Dashboard",
    "Direct Messaging Priority",
    "Basic Customer Engagement Features"
  ],
  "startDate": "2024-01-15T10:00:00Z",
  "expiryDate": "2024-02-14T10:00:00Z",
  "isActive": true,
  "createdAt": "2024-01-15T10:00:00Z"
}
```

**Response (404):** No active membership found

**Error Responses:**

- `401 Unauthorized`: Not authenticated
- `403 Forbidden`: Not a Business Owner

**Example:**

```bash
GET /api/memberships/507f1f77bcf86cd799439011/current
Authorization: Bearer {token}
```

---

## 4. Admin: List All Memberships

### `GET /api/memberships/admin/all`

Get all active memberships (Admin only).

**Authentication:** Required (Company Admin role)

**Response (200):**

```json
[
  {
    "id": "membership-id-1",
    "businessOwnerId": "business-owner-id-1",
    "packageType": "GOLD",
    "displayName": "Gold Membership",
    "features": [...],
    "startDate": "2024-01-15T10:00:00Z",
    "expiryDate": "2024-02-14T10:00:00Z",
    "isActive": true,
    "createdAt": "2024-01-15T10:00:00Z"
  },
  {
    "id": "membership-id-2",
    "businessOwnerId": "business-owner-id-2",
    "packageType": "SILVER",
    "displayName": "Silver Membership",
    "features": [...],
    "startDate": "2024-01-10T10:00:00Z",
    "expiryDate": "2024-02-09T10:00:00Z",
    "isActive": true,
    "createdAt": "2024-01-10T10:00:00Z"
  }
]
```

**Error Responses:**

- `401 Unauthorized`: Not authenticated
- `403 Forbidden`: Not a Company Admin

**Example:**

```bash
GET /api/memberships/admin/all
Authorization: Bearer {admin-token}
```

---

## Membership Packages

### Silver Membership

- **Price:** $29.99
- **Duration:** 30 days
- **Features:**
  - Limited Monthly Analytics
  - Upload up to X Images
  - Limited Promotion Visibility
  - Basic Customer Engagement Features

### Gold Membership

- **Price:** $79.99
- **Duration:** 30 days
- **Features:**
  - Unlimited Analytics
  - Unlimited Images
  - Priority Promotion Visibility
  - Featured Listing on Category Pages
  - Advanced Analytics Dashboard
  - Direct Messaging Priority
  - Basic Customer Engagement Features

---

## Membership Business Logic

### Purchase Scenarios

1. **New Membership:**

   - Creates membership with 30-day validity
   - Starts immediately

2. **Renewal (Same Package):**

   - Extends expiry date by 30 days
   - If already expired, starts new 30-day period
   - If still active, extends from current expiry date

3. **Upgrade (Silver → Gold):**

   - Upgrades immediately
   - Starts new 30-day period from upgrade date
   - Previous Silver membership is replaced

4. **Downgrade (Gold → Silver):**
   - Not allowed
   - Must wait for current membership to expire
   - Returns 400 Bad Request error

### Membership Status

- **Active:** `isActive = true` AND `expiryDate > now()`
- **Expired:** `expiryDate <= now()` (automatically inactive)
- **Inactive:** `isActive = false`

---

## Error Responses

### `400 Bad Request`

```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 400,
  "error": "Bad request",
  "message": "Business owner not found: {id}"
}
```

### `401 Unauthorized`

```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 401,
  "error": "Invalid credentials",
  "message": "Authentication required"
}
```

### `403 Forbidden`

```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Only business owners can access membership features"
}
```

### `404 Not Found`

```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 404,
  "error": "Not found",
  "message": "No active membership found"
}
```

---

## Security

- All endpoints require authentication via JWT token
- Business Owner endpoints require `ROLE_BUSINESS_OWNER`
- Admin endpoints require `ROLE_COMPANY_ADMIN`
- Travellers cannot access any membership endpoints

**Authorization Header:**

```
Authorization: Bearer {jwt-token}
```

---

## Database Schema

### Membership Collection

```javascript
{
  "_id": ObjectId("..."),
  "businessOwnerId": "string (indexed)",
  "packageType": "SILVER" | "GOLD",
  "features": ["FEATURE_NAME", ...],
  "startDate": ISODate("..."),
  "expiryDate": ISODate("..."),
  "isActive": true,
  "createdAt": ISODate("..."),
  "updatedAt": ISODate("...")
}
```

---

## Example Usage Flow

### 1. Business Owner Views Packages

```bash
GET /api/memberships/packages
Authorization: Bearer {token}
```

### 2. Business Owner Purchases Gold Membership

```bash
POST /api/memberships/purchase
Authorization: Bearer {token}
Content-Type: application/json

{
  "businessOwnerId": "507f1f77bcf86cd799439011",
  "packageType": "GOLD"
}
```

### 3. Business Owner Checks Current Membership

```bash
GET /api/memberships/507f1f77bcf86cd799439011/current
Authorization: Bearer {token}
```

### 4. Business Owner Renews (Same Package)

```bash
POST /api/memberships/purchase
Authorization: Bearer {token}
Content-Type: application/json

{
  "businessOwnerId": "507f1f77bcf86cd799439011",
  "packageType": "GOLD"
}
```

→ Extends expiry date by 30 days

### 5. Business Owner Upgrades Silver to Gold

```bash
POST /api/memberships/purchase
Authorization: Bearer {token}
Content-Type: application/json

{
  "businessOwnerId": "507f1f77bcf86cd799439011",
  "packageType": "GOLD"
}
```

→ Upgrades immediately, starts new 30-day period

---

## Notes

1. **Membership Duration:** Default is 30 days from purchase/upgrade date
2. **Automatic Expiry:** Memberships automatically become inactive when `expiryDate` passes
3. **Feature Access:** Features are determined by the package type and are included in the response
4. **No Payment Integration:** This API handles membership logic only; payment processing should be integrated separately
5. **MongoDB Required:** All membership operations require MongoDB to be running
