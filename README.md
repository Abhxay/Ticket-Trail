# 🎟 TicketTrail – Full-Stack Ticket & Request Management System

**Focus:** Backend Architecture, Security, and Role-Based Access Control  
**Tech Stack:** Spring Boot (Backend) | React + TailwindCSS (Frontend) | JWT Authentication

---

## 📌 Overview

**TicketTrail** is a full-stack ticket/request management system designed to demonstrate secure, scalable backend engineering and role-based access control.

- **Backend (Spring Boot):** JWT authentication, role-based authorization, robust request management  
- **Frontend (React + TailwindCSS):** Clean, responsive UI to interact with backend APIs

---

## 🚀 Key Backend Features

### **User Roles & Permissions**
- **ROLE_USER** – Submit and track personal requests  
- **ROLE_ADMIN** – Manage requests & view statistics  
- **ROLE_SUPER_ADMIN** – Full system control (users, roles, requests, settings)

### **Authentication & Security**
- JWT-based stateless authentication  
- Password hashing with Spring Security’s `PasswordEncoder`  

**Pre-configured Demo Accounts:**

| Role         | Username    | Password   |
|--------------|-------------|-----------|
| Super Admin  | Superadmin  | superadmin |
| Admin        | Admin_1     | admin123   |
| User         | user        | user123    |

---

### **Request Management**
- CRUD operations for requests  
- Role-based restrictions for approvals and updates  
- Request statuses: **Raised**, **Done**  
- Filtering by role and ownership  

---

### **Statistics & Reporting**
- Aggregated metrics: total, completed, and raised requests  
- 7-day trend data for request volume  
- Role-secured endpoints for admin-only analytics  

---

### **Security Highlights**
- URL-level Spring Security rules  
- Method-level restrictions with `@PreAuthorize`  
- Clear separation between authentication, authorization, and business logic  

---

## 🌐 API Highlights

| Method | Endpoint                               | Description                           | Access           |
|--------|----------------------------------------|---------------------------------------|------------------|
| POST   | `/auth/login`                          | Authenticate & get JWT                | All              |
| POST   | `/auth/register`                       | Create new user (ROLE_USER default)   | All              |
| GET    | `/requests`                            | View requests (filtered by role)      | Authenticated    |
| PUT    | `/requests/{id}/done`                  | Mark request as done                   | Admin/Superadmin |
| GET    | `/admin/users`                         | List all users                         | Superadmin       |
| POST   | `/admin/manage/{id}/add-admin`         | Promote user to Admin                  | Superadmin       |
| GET    | `/admin/statistics`                    | Get request analytics                  | Admin/Superadmin |

---

## 🎨 Frontend Overview
- Role-aware routing with `PrivateRoute`  
- Modern responsive UI via **TailwindCSS**  
- **Axios** automatically attaches JWT to API calls  

**Pages include:**
- Login / Register  
- Dashboard (submit & track requests)  
- Account settings  
- Admin management panel  
- Admin statistics (charts)  

---

## 🛠 Setup Instructions

### Backend
**Prerequisites:** Java 11+, Maven  

```bash
mvn spring-boot:run
Configure application.properties with your database credentials

Default roles and demo users are auto-seeded on startup

Frontend
Prerequisites: Node.js, npm


npm install
npm start
Runs at: http://localhost:3000
```

🔍 Why This Project

Real-world security patterns with JWT & Spring Security

Demonstrates REST API best practices

Role hierarchy with fine-grained permissions

Maintainable, extensible backend design

Data aggregation for analytics

📈 Future Enhancements

Advanced analytics & filtering options

Event-driven notifications for request updates

Automated backend test suite

Horizontal scaling support

📬 Contact
GitHub: Abhxay
LinkedIn:https://www.linkedin.com/in/abhay-thakur23/
