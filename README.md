Student Management System
A Spring Boot-based backend for managing students, courses, modules, and grades. Designed with JWT-secured admin access, this system allows full CRUD operations and modular course management.

Features:
Admin login via JWT

Student registration with auto-generated student number & email

Course and module management

Grade assignment and modification per module

Student search by name or course

Auto timestamps for creation and modifications

Comprehensive unit test coverage for StudentService and AdminService

Graceful error handling with custom exceptions

Secure endpoints using Spring Security and JWT filter

Tech Stack

.Backend: Spring Boot, Spring Security, JWT, Hibernate, JPA

.Database: MySQL

.Build Tool: Maven

.Testing: JUnit 5, Mockito

📂 Project Structure

src/

├── controller/       # REST API controllers

├── model/            # JPA entities: Student, Course, Module, Grade, Admin

├── repository/       # Spring Data JPA interfaces

├── services/         # Business logic

├── exception/        # Custom exceptions

└── utilities/        # JWT Auth filter

|__config/            #config

|_test/              # Unit tests for services

resources/

|__application.properties # database connection and settings

Admin Authentication

Login endpoint: /api/auth/login

example: Login Endpoint:

POST /api/auth/login

Request Body:

json

{

  "email": "admin@example.com",
  
  "password": "yourPassword"
  
}

Response:
On success, returns a JWT token:

json

{

  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  
}

How to Use the Token

Include the JWT in the Authorization header of all protected requests:

Add this token in Authorization: Bearer <token> header for all protected routes

example "Bearer yJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

Without the token or with an invalid/expired token, access to protected routes will be denie

API Highlights

Student

POST /api/students/create – Create student

GET /api/students – Get all students

GET /api/students/{studentNumber} – Get by student number

PUT /api/students/{studentNumber}/grades/{moduleName}?grade=85 – Update grade

Course & Module

GET /api/students/courses – All courses

GET /api/students/modules – All modules

Sample Student Creation

POST /api/students/create?courseName=Computer Science

{

  "firstName": "John",
  
  "lastName": "Doe",
  
  "idNumber": "6689675664567",
  
  "yearOfEnrollment": 2025
  
}


Author:
Built with passion for learning and growth by Mikateko Mashila
