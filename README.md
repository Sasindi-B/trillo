# Thrillo – Backend 🌍🚀

Thrillo is a full-stack travel and experience booking platform that connects **travellers** with **business owners** offering unique stays, events, and activities.  
This repository contains the **backend application**, built using **Java Spring Boot** and exposing secure REST APIs consumed by the Thrillo frontend.

🌍 Live API: [https://thrillo.lk](https://thrillo.lk)

---

## 🚀 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA / MongoDB**
- **Maven**
- **RESTful APIs**
- **Docker** (optional)
- **Render / Railway / Cloud deployment**

---

## 🎯 Core Features

### Authentication & Authorization
- Role-based authentication:
  - **TRAVELLER**
  - **BUSINESS_OWNER**
- JWT-based security
- Password hashing with BCrypt

### Business Owner
- Create and manage business profile
- Upload up to **6 business images**
- Add Google Maps location link
- Manage availability / free times
- Receive booking requests via notifications
- Accept or decline bookings

### Traveller
- View registered businesses via **Hotspots**
- Browse business details and image galleries
- Request bookings using calendar-based dates
- Receive booking & payment notifications
- Proceed to payment after confirmation

### Booking & Notifications
- Booking lifecycle: **PENDING → ACCEPTED / DECLINED**
- Notification system for both travellers and business owners
- Payment preparation after booking acceptance

---

## 📂 Project Structure

src/main/java/com/thrillo/
├── config/ # Security & application configuration
├── auth/ # Authentication & JWT logic
├── controllers/ # REST controllers
├── services/ # Business logic
├── repositories/ # Database repositories
├── dtos/ # Data Transfer Objects
├── entities/ # Database entities
├── utils/ # Helper utilities
└── ThrilloApplication.java


---

## ⚙️ Configuration

### Application Properties
`application.properties` or `application.yml`

```properties
server.port=8080
spring.datasource.url=jdbc:...
spring.datasource.username=...
spring.datasource.password=...
jwt.secret=your_secret_key


🛠️ Local Development
1️⃣ Prerequisites

Java 17+

Maven

Database (MongoDB / PostgreSQL / MySQL)

2️⃣ Build the project
mvn clean install

3️⃣ Run the application
mvn spring-boot:run


Backend runs at:

http://localhost:8080

🔗 API Endpoints (Sample)
Authentication
POST /api/auth/register
POST /api/auth/login

Hotspots
GET /api/hotspots
GET /api/hotspots/{businessId}

Business Profile
POST /api/business/profile
PUT /api/business/profile
POST /api/business/profile/images

Bookings
POST /api/bookings
GET /api/bookings/traveller/{id}
GET /api/bookings/business/{id}
PUT /api/bookings/{id}/accept
PUT /api/bookings/{id}/decline

Notifications
GET /api/notifications/{userId}
PUT /api/notifications/{id}/read

🔐 Security

JWT-based authentication

Role-based route protection

Secure password storage

CORS configuration for frontend integration

🌐 Deployment

Backend deployed on cloud hosting (Render / Railway / EC2)

Maven build:

mvn clean install -DskipTests


Run command:

java -jar target/*.jar

🧪 Testing

API testing using Postman

Manual integration testing with frontend

Validation & exception handling implemented

📌 Future Enhancements

WebSocket-based real-time notifications

Reviews & ratings

Admin dashboard

Advanced availability scheduling

Payment gateway integration (Stripe / PayHere)

🌍 Live API: [https://thrillo.onrender.com](https://thrillo.onrender.com)
🌍 Website: [https://thrillo.lk](https://thrillo.lk)
