# Art Academy LMS - API Documentation

Complete guide for the subscription-based class management system.

---

## Overview

The LMS operates on a **monthly subscription model** with an **8-class limit per month**:
- Students enroll in the art academy
- Admin approves enrollments and creates monthly subscriptions
- Admin creates class sessions (runs 4 days/week)
- Admin marks attendance - system flags students exceeding 8 sessions

---

## Authentication

All endpoints require JWT authentication via Bearer token.

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@artacademy.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "...",
  "tokenType": "Bearer"
}
```

Use the token in subsequent requests:
```http
Authorization: Bearer eyJ...
```

---

## Complete Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  1. ENROLLMENT  │ ──▶ │ 2. SUBSCRIPTION │ ──▶ │   3. SESSIONS   │
│  Student Signs  │     │  Admin Creates  │     │  Admin Creates  │
│      Up         │     │ Monthly (8 max) │     │  Class Dates    │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                                                        ▼
                                               ┌─────────────────┐
                                               │  4. ATTENDANCE  │
                                               │ Track + Flag    │
                                               │  Over-Limit     │
                                               └─────────────────┘
```

---

## 1. Enrollment Management

### Student Enrolls (Student Role)
```http
POST /api/enrollments
Authorization: Bearer {student_token}
Content-Type: application/json

{
  "classId": "artclass-oil-painting",
  "notes": "Interested in evening batches"
}
```

### Get Pending Enrollments (Admin)
```http
GET /api/admin/enrollments/pending?page=0&size=20
Authorization: Bearer {admin_token}
```

**Response:**
```json
{
  "content": [
    {
      "id": "enr-abc123",
      "userId": "user-xyz",
      "studentName": "John Doe",
      "studentEmail": "john@example.com",
      "classId": "artclass-oil-painting",
      "className": "Oil Painting Basics",
      "status": "PENDING",
      "enrollmentDate": "2026-01-24T10:00:00Z"
    }
  ],
  "totalElements": 1
}
```

### Approve Enrollment (Admin)
```http
POST /api/admin/enrollments/{enrollmentId}/approve?adminNotes=Welcome
Authorization: Bearer {admin_token}
```

### Reject Enrollment (Admin)
```http
POST /api/admin/enrollments/{enrollmentId}/reject?adminNotes=Already enrolled
Authorization: Bearer {admin_token}
```

---

## 2. Subscription Management

After approving enrollment, create a monthly subscription for the student.

### Create Subscription (Admin)
```http
POST /api/subscriptions
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "studentId": "user-xyz",
  "subscriptionMonth": 1,
  "subscriptionYear": 2026,
  "allowedSessions": 8,
  "amountPaid": 2000.00,
  "paymentId": "pay_xyz123",
  "notes": "January 2026 subscription"
}
```

**Response:**
```json
{
  "id": "sub-abc123",
  "studentId": "user-xyz",
  "studentName": "John Doe",
  "studentEmail": "john@example.com",
  "subscriptionMonth": 1,
  "subscriptionYear": 2026,
  "startDate": "2026-01-01",
  "endDate": "2026-01-31",
  "allowedSessions": 8,
  "attendedSessions": 0,
  "remainingSessions": 8,
  "isOverLimit": false,
  "status": "ACTIVE"
}
```

### Get Active Subscriptions (Admin)
```http
GET /api/subscriptions/active?page=0&size=20
Authorization: Bearer {admin_token}
```

### Get Subscriptions by Month (Admin)
```http
GET /api/subscriptions/month/2026/1
Authorization: Bearer {admin_token}
```

### Get Over-Limit Students (Admin)
```http
GET /api/subscriptions/over-limit
Authorization: Bearer {admin_token}
```

**Response:**
```json
[
  {
    "studentName": "Jane Smith",
    "studentEmail": "jane@example.com",
    "allowedSessions": 8,
    "attendedSessions": 9,
    "remainingSessions": -1,
    "isOverLimit": true
  }
]
```

### Renew Subscription (Admin)
```http
POST /api/subscriptions/student/{studentId}/renew
Authorization: Bearer {admin_token}
```
*Creates subscription for next month automatically.*

### Cancel Subscription (Admin)
```http
POST /api/subscriptions/{subscriptionId}/cancel
Authorization: Bearer {admin_token}
```

---

## 3. Session Management

Admin creates class sessions for each day classes run.

### Create Session (Admin)
```http
POST /api/sessions
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "sessionDate": "2026-01-27",
  "startTime": "10:00",
  "endTime": "12:00",
  "topic": "Oil Painting - Color Theory",
  "description": "Learn about color mixing and palette preparation",
  "meetingLink": "https://meet.example.com/session-1",
  "meetingPassword": "art123"
}
```

**Response:**
```json
{
  "id": "sess-abc123",
  "sessionDate": "2026-01-27",
  "startTime": "10:00",
  "endTime": "12:00",
  "topic": "Oil Painting - Color Theory",
  "status": "SCHEDULED",
  "attendanceTaken": false
}
```

### Get All Sessions (Admin)
```http
GET /api/sessions?page=0&size=20
Authorization: Bearer {admin_token}
```

### Get Sessions by Date Range
```http
GET /api/sessions/range?startDate=2026-01-01&endDate=2026-01-31
Authorization: Bearer {admin_token}
```

### Get Upcoming Sessions
```http
GET /api/sessions/upcoming?page=0&size=10
Authorization: Bearer {admin_token}
```

### Update Session Status (Admin)
```http
PATCH /api/sessions/{sessionId}/status?status=COMPLETED
Authorization: Bearer {admin_token}
```

**Status Values:** `SCHEDULED`, `ONGOING`, `COMPLETED`, `CANCELLED`

---

## 4. Attendance Tracking

Mark attendance for each session. System automatically:
- Counts sessions per student per month
- Flags students exceeding 8-session limit
- Updates subscription `attendedSessions` count

### Mark Attendance (Admin)
```http
POST /api/attendance
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "sessionId": "sess-abc123",
  "attendanceRecords": [
    {
      "studentId": "user-xyz",
      "isPresent": true,
      "remarks": "On time"
    },
    {
      "studentId": "user-abc",
      "isPresent": true,
      "remarks": "Arrived 10 mins late"
    },
    {
      "studentId": "user-def",
      "isPresent": false,
      "remarks": "Did not attend"
    }
  ]
}
```

**Response (includes over-limit flags):**
```json
{
  "id": "sess-abc123",
  "sessionDate": "2026-01-27",
  "topic": "Oil Painting - Color Theory",
  "attendanceTaken": true,
  "presentCount": 2,
  "absentCount": 1,
  "totalStudents": 3,
  "attendanceRecords": [
    {
      "studentId": "user-xyz",
      "studentName": "John Doe",
      "isPresent": true,
      "sessionCountThisMonth": 5,
      "isOverLimit": false
    },
    {
      "studentId": "user-abc",
      "studentName": "Jane Smith",
      "isPresent": true,
      "sessionCountThisMonth": 9,
      "isOverLimit": true,
      "remarks": "⚠️ Exceeded 8-session limit"
    }
  ]
}
```

> **⚠️ Important:** When `isOverLimit: true`, the admin should note that this student has exceeded their monthly quota. The system allows marking attendance but flags it for admin awareness.

### Get Attendance for Session (Admin)
```http
GET /api/attendance/session/{sessionId}
Authorization: Bearer {admin_token}
```

### Get Over-Limit Attendance Records (Admin)
```http
GET /api/attendance/over-limit?month=1&year=2026
Authorization: Bearer {admin_token}
```

### Get Student's Monthly Attendance (Admin)
```http
GET /api/attendance/student/{studentId}/monthly?month=1&year=2026
Authorization: Bearer {admin_token}
```

### Student Views Own Attendance
```http
GET /api/attendance/my-attendance?page=0&size=20
Authorization: Bearer {student_token}
```

---

## Complete Workflow Example

### Step 1: Student Enrolls
```bash
curl -X POST /api/enrollments \
  -H "Authorization: Bearer {student_token}" \
  -d '{"classId": "art-class-001"}'
```

### Step 2: Admin Approves
```bash
curl -X POST /api/admin/enrollments/{id}/approve \
  -H "Authorization: Bearer {admin_token}"
```

### Step 3: Admin Creates Subscription
```bash
curl -X POST /api/subscriptions \
  -H "Authorization: Bearer {admin_token}" \
  -d '{
    "studentId": "user-xyz",
    "subscriptionMonth": 1,
    "subscriptionYear": 2026,
    "allowedSessions": 8,
    "amountPaid": 2000
  }'
```

### Step 4: Admin Creates Sessions
```bash
curl -X POST /api/sessions \
  -H "Authorization: Bearer {admin_token}" \
  -d '{
    "sessionDate": "2026-01-27",
    "startTime": "10:00",
    "endTime": "12:00",
    "topic": "Day 1 - Introduction"
  }'
```

### Step 5: Admin Marks Attendance
```bash
curl -X POST /api/attendance \
  -H "Authorization: Bearer {admin_token}" \
  -d '{
    "sessionId": "sess-001",
    "attendanceRecords": [
      {"studentId": "user-xyz", "isPresent": true}
    ]
  }'
```

---

## Status Enums

### EnrollmentStatus
| Value | Description |
|-------|-------------|
| `PENDING` | Awaiting admin approval |
| `APPROVED` | Approved, can create subscription |
| `REJECTED` | Enrollment rejected |
| `IN_PROGRESS` | Student actively attending |
| `COMPLETED` | Course completed |
| `CANCELLED` | Student cancelled |

### SubscriptionStatus
| Value | Description |
|-------|-------------|
| `ACTIVE` | Current month subscription |
| `EXPIRED` | Month ended, subscription expired |
| `CANCELLED` | Admin cancelled subscription |
| `RENEWED` | Renewed for next month |

### SessionStatus
| Value | Description |
|-------|-------------|
| `SCHEDULED` | Upcoming session |
| `ONGOING` | Session in progress |
| `COMPLETED` | Session finished |
| `CANCELLED` | Session cancelled |

---

## Error Responses

```json
{
  "timestamp": "2026-01-24T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Subscription already exists for this month",
  "path": "/api/subscriptions"
}
```

| Status | Meaning |
|--------|---------|
| 400 | Bad request / validation error |
| 401 | Unauthorized - invalid or missing token |
| 403 | Forbidden - insufficient permissions |
| 404 | Resource not found |
| 409 | Conflict - duplicate resource |

---

## Base URL

```
Production: https://api.artacademy.com
Development: http://localhost:8081
```

## Swagger UI

Interactive API documentation available at:
```
http://localhost:8081/swagger-ui.html
```
