# Mini Payroll & Attendance System

A full-stack SaaS MVP for managing employees, attendance, leave, and payroll with role-based access control.

## Live URLs

| Service | URL |
|---|---|
| Frontend | https://payroll-frontend-tau.vercel.app |
| Backend API | https://payroll-backend-1tid.onrender.com |
| Database | PostgreSQL on Render |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 17 (Standalone Components) |
| Backend | Java 17 + Spring Boot 3.2 |
| Database | PostgreSQL 18 |
| ORM | Spring Data JPA + Hibernate |
| Auth | JWT (JSON Web Tokens) |
| Extra Feature | Role-Based Access Control (ADMIN / EMPLOYEE) |

---

## Architecture

```
Angular Frontend (Vercel)
        |
        | HTTPS/JSON
        v
Spring Boot REST API (Render)
        |
        | JPA/Hibernate
        v
PostgreSQL Database (Render)
```

The frontend is a Single Page Application (SPA) deployed on Vercel. It communicates with the Spring Boot REST API over HTTPS. The API connects to a hosted PostgreSQL database on Render. JWT tokens are used for stateless authentication — no sessions are stored on the server.

---

## Database Schema

### employees

| Column | Type | Notes |
|---|---|---|
| id | UUID (PK) | Auto-generated |
| name | VARCHAR(255) | Required |
| role | VARCHAR(20) | WFH, OFFICE, ONSITE |
| salary_type | VARCHAR(20) | MONTHLY, DAILY |
| salary_amount | DECIMAL(10,2) | Required, must be > 0 |
| system_role | VARCHAR(20) | ADMIN, EMPLOYEE |
| username | VARCHAR(100) | Unique, used for login |
| password | VARCHAR(255) | BCrypt hashed |
| created_at | TIMESTAMP | Auto-set |

### attendance

| Column | Type | Notes |
|---|---|---|
| id | UUID (PK) | Auto-generated |
| employee_id | UUID (FK) | References employees.id |
| date | DATE | Required |
| status | VARCHAR(10) | PRESENT, ABSENT |
| created_at | TIMESTAMP | Auto-set |

Constraint: UNIQUE (employee_id, date) — one record per employee per day.

### leaves

| Column | Type | Notes |
|---|---|---|
| id | UUID (PK) | Auto-generated |
| employee_id | UUID (FK) | References employees.id |
| start_date | DATE | Required |
| end_date | DATE | Required |
| status | VARCHAR(10) | PENDING, APPROVED, REJECTED |
| reason | TEXT | Optional |
| created_at | TIMESTAMP | Auto-set |

Constraint: end_date >= start_date.

---

## Entity Relationships

```
employees 1 ──── * attendance
employees 1 ──── * leaves
```

Payroll has no persistent table — it is computed on demand from attendance records and employee salary data.

---

## API Endpoints

Base URL: `https://payroll-backend-1tid.onrender.com/api`

### Authentication

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /auth/login | Public | Login and get JWT token |

Request:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response:
```json
{
  "token": "eyJhbGci...",
  "employeeId": "uuid",
  "systemRole": "ADMIN"
}
```

### Employees

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /employees | ADMIN | Create a new employee |
| GET | /employees | ADMIN | Get all employees |

### Attendance

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /attendance | ADMIN | Mark attendance |
| GET | /attendance/employee/{id} | ADMIN or own | Get attendance by employee |

### Leave

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /leaves | Authenticated | Apply for leave |
| PUT | /leaves/{id}/approve | ADMIN | Approve a leave request |
| PUT | /leaves/{id}/reject | ADMIN | Reject a leave request |
| GET | /leaves | ADMIN | Get all leave requests |
| GET | /leaves/employee/{id} | ADMIN or own | Get leaves by employee |

### Payroll

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | /payroll/employee/{id}?month=4&year=2026 | ADMIN or own | Calculate payroll |

Payroll response:
```json
{
  "employeeId": "uuid",
  "employeeName": "Alice Johnson",
  "month": 4,
  "year": 2026,
  "salaryType": "MONTHLY",
  "salaryAmount": 45000.00,
  "presentDays": 20,
  "approvedLeaveDays": 2,
  "payableDays": 22,
  "totalSalary": 33000.00
}
```

---

## Payroll Calculation Logic

### Monthly Salary
```
Daily Rate = Monthly Salary / 30
Total Salary = Daily Rate × Payable Days
```

### Daily Salary
```
Total Salary = Daily Wage × Payable Days
```

### Payable Days
```
Payable Days = Present Days + Approved Leave Days
```

---

## Extra Feature — Role-Based Access Control

This project implements role-based access as the extra feature.

### Roles
- **ADMIN** — Full access to all features
- **EMPLOYEE** — Can only access their own data

### Access Rules

| Feature | ADMIN | EMPLOYEE |
|---|---|---|
| Create employee | ✅ | ❌ |
| View all employees | ✅ | ❌ |
| Mark attendance | ✅ | ❌ |
| View attendance | ✅ Any | ✅ Own only |
| Apply leave | ✅ | ✅ |
| Approve/reject leave | ✅ | ❌ |
| View payroll | ✅ Any | ✅ Own only |

Role enforcement happens at two levels:
1. **Backend** — Spring Security checks JWT role on every request
2. **Frontend** — Route guards and dual-mode components show different views per role

---

## Assumptions

1. **Payroll is computed on demand** — no payroll history table is stored. Each API call recalculates from attendance data.
2. **Approved leave counts as paid days** — `payableDays = presentDays + approvedLeaveDays`. This is documented and applied consistently.
3. **Monthly salary uses 30-day divisor** — regardless of actual days in the month, the formula divides by 30.
4. **One attendance record per employee per day** — enforced by a unique database constraint.
5. **Passwords are BCrypt hashed** — plain text passwords are never stored.
6. **JWT tokens expire after 24 hours** — no refresh token mechanism.
7. **Admin user is auto-seeded** — on first startup, an admin user (`admin` / `admin123`) is created if no admin exists.

---

## Local Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 18+
- PostgreSQL 14+

### Backend Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE payroll_db;
```

2. Set environment variables:
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/payroll_db
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=yourpassword
export JWT_SECRET=your-secret-key-minimum-32-characters-long
```

3. Run the backend:
```bash
cd mini-payroll-attendance
mvn spring-boot:run
```

API available at: `http://localhost:8081`

Tables are auto-created by Hibernate on first startup. Admin user is auto-seeded.

### Frontend Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Run the frontend:
```bash
npm start
```

App available at: `http://localhost:4200`

### Default Login Credentials

| Username | Password | Role |
|---|---|---|
| admin | admin123 | ADMIN |

---

## Project Structure

```
.
├── mini-payroll-attendance/          # Spring Boot backend
│   ├── src/main/java/com/payroll/
│   │   ├── config/                   # Security, DataSeeder
│   │   ├── controller/               # REST controllers
│   │   ├── dto/                      # Request/Response DTOs
│   │   ├── exception/                # Global exception handling
│   │   ├── model/                    # JPA entities
│   │   ├── repository/               # Spring Data repositories
│   │   ├── security/                 # JWT filter and utility
│   │   └── service/                  # Business logic
│   ├── src/main/resources/
│   │   └── application.yml           # Configuration
│   ├── Dockerfile                    # Docker build for Render
│   └── pom.xml
│
└── frontend/                         # Angular 17 frontend
    ├── src/app/
    │   ├── components/               # Login, Employee, Attendance, Leave, Payroll
    │   ├── guards/                   # Auth and role guards
    │   ├── interceptors/             # JWT interceptor
    │   ├── models/                   # TypeScript interfaces
    │   └── services/                 # HTTP services
    └── src/environments/             # Dev and prod API URLs
```

---

## Architecture Decisions

| Decision | Reason |
|---|---|
| Stateless JWT auth | No server-side session storage needed, scales easily |
| Payroll computed on demand | Simpler than storing snapshots, always reflects current data |
| Role-based access as extra feature | Adds real business value, demonstrates security implementation |
| Hibernate ddl-auto: update | Auto-creates tables on first deploy, no manual SQL needed |
| BCrypt password hashing | Industry standard, resistant to rainbow table attacks |
| BehaviorSubject + async pipe | Ensures Angular change detection works reliably with HTTP responses |
| Docker for Render deployment | Render has no native Java support, Docker gives full control |

---

## GitHub Repository

https://github.com/anusreeraveendrantp-ui/payrollAttendenceSystem
