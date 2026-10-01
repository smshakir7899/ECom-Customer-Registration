# ECom Customer Registration Service

A Spring Boot REST API for customer registration with input validation, duplicate email/mobile detection, BCrypt password hashing, JPA persistence, and centralized exception handling.

## Overview

This project implements the Customer Registration module for an e-commerce application.

The API allows a customer to register using:

- Name
- Email
- Mobile number
- Password

The application validates incoming requests, prevents duplicate customer information, securely hashes passwords using BCrypt, and stores customer details in MySQL.

## Features

- Customer registration REST API
- Automatic customer ID generation
- Request validation using Jakarta Bean Validation
- Email format validation
- Indian mobile number validation
- Password validation
- Duplicate email detection
- Duplicate mobile number detection
- BCrypt password hashing
- MySQL database integration
- Spring Data JPA
- Automatic `createdAt` and `updatedAt` timestamps
- Centralized exception handling
- Proper HTTP status codes

## Tech Stack

| Technology | Usage |
|---|---|
| Java 17+ | Programming language |
| Spring Boot | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| MySQL | Relational database |
| BCrypt | Password hashing |
| Jakarta Bean Validation | Request validation |
| Maven | Build and dependency management |
| Postman | API testing |
| Git & GitHub | Version control |

## Project Structure

```text
src/main/java/com/flipkart/customer
│
├── config
│   └── PasswordEncoderConfig.java
│
├── controller
│   └── CustomerController.java
│
├── entity
│   └── CustomerEntity.java
│
├── exception
│   ├── DuplicateEmailException.java
│   ├── DuplicateMobileException.java
│   └── GlobalExceptionHandler.java
│
├── repository
│   └── CustomerRepository.java
│
├── request
│   └── CustomerRequest.java
│
├── response
│   ├── CustomerResponse.java
│   └── ValidationErrorResponse.java
│
└── service
    └── CustomerService.java
```

## API Endpoint

### Register Customer

**POST**

```text
/api/v1/customers/register
```

### Request

```json
{
    "name": "Shakir",
    "email": "shakir@gmail.com",
    "mobile": "9876543211",
    "password": "Shakir@123"
}
```

### Successful Response

**HTTP 201 Created**

```json
{
    "customerId": 1,
    "name": "Shakir",
    "email": "shakir@gmail.com",
    "mobile": "9876543211",
    "createdAt": "2026-10-01T18:37:48.6451315"
}
```

The password is intentionally not included in the response.

## Validation

The registration request validates:

### Name

- Must not be blank
- Maximum 100 characters

### Email

- Must not be blank
- Must be in a valid email format
- Maximum 100 characters

### Mobile

- Must not be blank
- Must contain a valid 10-digit Indian mobile number

### Password

- Must not be blank
- Minimum 8 characters
- Must contain at least one letter
- Must contain at least one number

## Error Handling

The application uses `@RestControllerAdvice` for centralized exception handling.

### Duplicate Email

**HTTP 409 Conflict**

```json
{
    "status": 409,
    "error": "Conflict",
    "message": "Email already registered"
}
```

### Duplicate Mobile

**HTTP 409 Conflict**

```json
{
    "status": 409,
    "error": "Conflict",
    "message": "Mobile number already registered"
}
```

### Validation Failure

**HTTP 400 Bad Request**

```json
{
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "errors": {
        "email": "Invalid email format",
        "mobile": "Invalid Indian mobile number",
        "password": "Password must contain at least 8 characters"
    }
}
```

## Password Security

Passwords are never stored in plain text.

The raw password received from the API is encoded using BCrypt before being persisted.

```text
Raw Password
     ↓
PasswordEncoder
     ↓
BCrypt Hash
     ↓
CustomerEntity
     ↓
MySQL
```

Example database value:

```text
$2a$10$.................................................
```

## Database

The application uses MySQL with Spring Data JPA.

Create the database:

```sql
CREATE DATABASE flipkart_customer;
```

The customer table is mapped from `CustomerEntity`.

Expected table structure:

```text
customers
├── customer_id
├── name
├── email
├── mobile
├── password
├── created_at
└── updated_at
```

Email and mobile are configured as unique fields.

## Configuration

Configure the MySQL connection in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flipkart_customer
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Do not commit real database passwords or other sensitive credentials to GitHub.

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/smshakir7899/ECom-Customer-Registration.git
```

### 2. Open the project

Open the project in IntelliJ IDEA, Eclipse, or another Java IDE.

### 3. Configure MySQL

Create the database and update the database credentials in `application.properties`.

### 4. Build the project

```bash
mvn clean install
```

### 5. Run the application

```bash
mvn spring-boot:run
```

Or run the Spring Boot main class directly from your IDE.

### 6. Test the API

Use Postman:

```text
POST http://localhost:8080/api/v1/customers/register
```

Set:

```text
Content-Type: application/json
```

and send the registration JSON shown above.

## HTTP Status Codes

| Status | Meaning |
|---|---|
| 201 Created | Customer successfully registered |
| 400 Bad Request | Request validation failed |
| 409 Conflict | Email or mobile already exists |
| 500 Internal Server Error | Unexpected server-side error |

## Request Processing Flow

```text
Client / Postman
       │
       ▼
CustomerController
       │
       ▼
CustomerRequest
       │
    @Valid
       │
       ├──────── Invalid ────────► 400 Bad Request
       │
       ▼
CustomerService
       │
       ├── Duplicate Email ──────► 409 Conflict
       │
       ├── Duplicate Mobile ─────► 409 Conflict
       │
       ▼
BCrypt Password Encoding
       │
       ▼
CustomerRepository
       │
       ▼
MySQL
       │
       ▼
CustomerResponse
       │
       ▼
201 Created
```

## Database Entity Mapping

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
Database
```

Responsibilities:

- **Controller** — Handles HTTP requests and responses
- **Request DTO** — Represents and validates client input
- **Service** — Contains customer registration business logic
- **Repository** — Handles database operations
- **Entity** — Maps Java objects to the database table
- **Response DTO** — Controls the data returned to the client
- **Exception Handler** — Converts application exceptions into HTTP responses
- **Configuration** — Provides reusable Spring beans such as `PasswordEncoder`

## Testing

The registration API has been manually tested using Postman for:

- Successful registration
- Duplicate email
- Duplicate mobile
- Invalid email
- Invalid mobile number
- Missing required fields
- Invalid password

Automated unit and controller tests can be added as the test suite is expanded.

## Future Enhancements

Potential extensions for the customer module include:

- Customer login
- JWT-based authentication
- Password reset
- Customer profile management
- Email/mobile verification
- Pagination and customer search
- Integration testing
- Automated CI/CD pipeline

## Author

**Shakir**

GitHub:  
https://github.com/smshakir7899

## Repository

https://github.com/smshakir7899/ECom-Customer-Registration
