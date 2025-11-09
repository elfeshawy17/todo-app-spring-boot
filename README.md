# 📝 Todo Application

A modern, secure RESTful Todo application built with Spring Boot that allows users to manage their tasks efficiently. This application provides a complete CRUD API for both users and tasks with JWT-based authentication, role-based authorization, proper validation, error handling, and clean architecture.

🌐 **Project URL:** [https://roadmap.sh/projects/todo-list-api](https://roadmap.sh/projects/todo-list-api)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

## 🚀 Features

- 🔐 **Authentication & Authorization**: JWT-based authentication with role-based access control
- 🔑 **Token Management**: Access and refresh token implementation
- 👥 **User Registration & Login**: Secure user registration and login system
- ✅ **User Management**: Create, read, update, and delete users (Admin only)
- 📋 **Task Management**: Full CRUD operations for tasks (User & Admin)
- 🛡️ **Security**: Password encryption, secure endpoints, and role-based permissions
- ✨ **Data Validation**: Comprehensive input validation with custom error messages
- 🎯 **Exception Handling**: Global exception handling with meaningful error responses
- 🏗️ **Clean Architecture**: Well-structured codebase with separation of concerns
- 📖 **API Documentation**: RESTful API design with consistent response format
- 🔄 **Data Mapping**: Automatic entity-DTO mapping using MapStruct
- 🧪 **Unit Testing**: Comprehensive JUnit 5 tests with Mockito for mocking dependencies

## 🏗️ Architecture

```
src/main/java/org/mytodoapp/todo/
├── 📁 security/
│   ├── 📁 auth/          # Authentication components
│   │   ├── 📁 controller/    # Auth REST controllers
│   │   ├── 📁 dto/           # Auth DTOs
│   │   ├── 📁 mapper/        # Auth mappers
│   │   └── 📁 service/       # Auth business logic
│   ├── 📁 config/        # Security configuration
│   ├── 📁 filter/        # JWT authentication filter
│   ├── 📁 model/         # Custom user details
│   └── 📁 util/          # JWT utilities
├── 📁 shared/
│   ├── 📁 dto/           # Shared DTOs (ApiResponse)
│   ├── 📁 exception/     # Custom exceptions & global handler
│   └── 📁 util/          # Utility classes (ResponseBuilder)
├── 📁 user/
│   ├── 📁 controller/    # REST controllers
│   ├── 📁 dto/           # Data Transfer Objects
│   ├── 📁 entity/        # JPA entities
│   ├── 📁 mapper/        # MapStruct mappers
│   ├── 📁 repo/          # JPA repositories
│   └── 📁 service/       # Business logic
└── 📁 task/
    ├── 📁 controller/    # REST controllers
    ├── 📁 dto/           # Data Transfer Objects
    ├── 📁 entity/        # JPA entities
    ├── 📁 mapper/        # MapStruct mappers
    ├── 📁 repo/          # JPA repositories
    └── 📁 service/       # Business logic
```

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.5.5
- **Security**: Spring Security 6 with JWT
- **Language**: Java 21
- **Database**: JPA/Hibernate compatible databases
- **Authentication**: JWT (JSON Web Tokens)
- **Password Encryption**: BCrypt
- **Mapping**: MapStruct for entity-DTO mapping
- **Validation**: Bean Validation (JSR 303)
- **Build Tool**: Maven
- **Architecture**: Layered Architecture (Controller → Service → Repository)
- **Testing**:  
  - **Unit Testing**: JUnit 5  
  - **Mocking**: Mockito for isolating service and repository layers

## 🔐 Security Features

### Authentication
- **JWT-based Authentication**: Stateless authentication using JSON Web Tokens
- **Access & Refresh Tokens**: Dual token system for enhanced security
- **Password Encryption**: BCrypt hashing with strength 10
- **Secure Registration**: Email uniqueness validation and password confirmation

### Authorization
- **Role-based Access Control**: ADMIN and USER roles with different permissions
- **Method-level Security**: `@PreAuthorize` annotations for fine-grained access control
- **Protected Endpoints**: Secure API endpoints based on user roles

### Token Management
- **Access Token**: Short-lived token for API access
- **Refresh Token**: Long-lived token for obtaining new access tokens
- **Token Validation**: Comprehensive token validation and expiration handling

## 📊 Database Schema

```mermaid
erDiagram
    USERS {
        BIGINT id PK "Auto-generated"
        VARCHAR username "Username (unique)"
        VARCHAR email "Email (unique)"
        VARCHAR password "Encrypted password"
        ENUM role "USER or ADMIN"
        BOOLEAN enabled "Account status"
        BOOLEAN account_non_expired "Account expiration"
        BOOLEAN account_non_locked "Account lock status"
        BOOLEAN credentials_non_expired "Credentials expiration"
    }
    
    TASKS {
        BIGINT id PK "Auto-generated"
        VARCHAR title "Task title"
        TEXT description "Task description"
        BIGINT user_id FK "Reference to user"
    }
    
    USERS ||--o{ TASKS : "has many"
```

## 🔧 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.5.5+
- Your preferred database (H2, MySQL, PostgreSQL, etc.)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/todo-app.git
   cd todo-app
   ```

2. **Configure database connection**
   ```properties
   # application.properties
   spring.datasource.url=jdbc:your-database-url
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   spring.jpa.hibernate.ddl-auto=update
   
   # JWT Configuration
   jwt.secret=your-jwt-secret-key-here-make-it-long-and-secure
   jwt.expiration=3600000
   jwt.refresh-expiration=86400000
   ```

3. **Build the project**
   ```bash
   mvn clean compile
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📡 API Documentation

### 🔐 Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `POST` | `/api/auth/register` | Register a new user | Public |
| `POST` | `/api/auth/login` | Login user | Public |
| `POST` | `/api/auth/refresh` | Refresh access token | Public |

### 👤 User Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `POST` | `/api/users` | Create a new user | Admin |
| `GET` | `/api/users` | Get all users | Admin |
| `GET` | `/api/users/{userId}` | Get user by ID | Admin |
| `PUT` | `/api/users/{userId}` | Update user | Admin |
| `DELETE` | `/api/users/{userId}` | Delete user | Admin |

### ✅ Task Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `POST` | `/api/users/{userId}/tasks` | Create a task for user | User/Admin |
| `GET` | `/api/users/{userId}/tasks` | Get all tasks for user | User/Admin |
| `GET` | `/api/users/{userId}/tasks/{taskId}` | Get specific task | User/Admin |
| `PUT` | `/api/users/{userId}/tasks/{taskId}` | Update task | User/Admin |
| `DELETE` | `/api/users/{userId}/tasks/{taskId}` | Delete task | User/Admin |

### 📝 Request/Response Examples

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "johndoe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
}
```

#### Authentication Success Response
```json
{
    "status": 200,
    "message": "Ok",
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "tokenType": "Bearer",
        "expiresIn": 3600000,
        "email": "john.doe@example.com",
        "role": "USER"
    },
    "errors": null,
    "timestamp": "16-09-2025 10:30:45"
}
```

#### Refresh Token
```http
POST /api/auth/refresh
Content-Type: application/json

{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Authenticated Request Example
```http
GET /api/users/1/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Create Task (Authenticated)
```http
POST /api/users/1/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
    "title": "Complete project documentation",
    "description": "Write comprehensive README and API documentation"
}
```

#### Validation Error Response
```json
{
    "status": 400,
    "message": "Validation Failed",
    "data": null,
    "errors": [
        "password: Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character",
        "email: Invalid email format"
    ],
    "timestamp": "16-09-2025 10:35:22"
}
```

## 🧪 Testing

The application includes comprehensive unit tests to ensure code quality and reliability.

### Test Coverage

- **Authentication Service Tests**: Complete test coverage for registration, login, and token refresh functionality
- **User Service Tests**: Full CRUD operation testing with validation and error handling
- **Task Service Tests**: Comprehensive tests for task management operations

### Test Structure

```
src/test/java/org/mytodoapp/todo/
├── 📁 security/
│   └── 📁 auth/
│       ├── 📁 service/impl/
│       │   └── AuthServiceImplTest.java
│       └── 📁 util/
│           └── AuthTestDataFactory.java
├── 📁 user/
│   ├── 📁 service/impl/
│   │   └── UserServiceImplTest.java
│   └── 📁 util/
│       └── UserTestDataFactory.java
└── 📁 task/
    ├── 📁 service/impl/
    │   └── TaskServiceImplTest.java
    └── 📁 util/
        └── TaskTestDataFactory.java
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=AuthServiceImplTest

# Run tests in a specific package
mvn test -Dtest="org.mytodoapp.todo.security.auth.**"
```

### Test Features

- **Mockito Integration**: Using `@Mock` and `@InjectMocks` for clean dependency injection
- **Test Data Factories**: Reusable factory classes for creating test data
- **Comprehensive Scenarios**: Testing success cases, validation failures, and error handling
- **Assertions**: Thorough verification of expected outcomes and method interactions

### Key Test Scenarios

#### Authentication Tests
- ✅ Successful user registration
- ✅ Registration with duplicate email
- ✅ Password mismatch validation
- ✅ Successful login with valid credentials
- ✅ Login failure with invalid credentials
- ✅ Token refresh with valid refresh token
- ✅ Token refresh with invalid/expired token

#### User Service Tests
- ✅ Create user with valid data
- ✅ Prevent duplicate email/username
- ✅ Update user information
- ✅ Delete existing user
- ✅ Find user by ID
- ✅ Retrieve all users
- ✅ Handle non-existent user scenarios

#### Task Service Tests
- ✅ Add task to existing user
- ✅ Update task information
- ✅ Delete task
- ✅ Find task by user and task ID
- ✅ Retrieve all tasks for a user
- ✅ Handle non-existent user/task scenarios

## 🚀 Usage Flow

1. **Register** a new user account with `/api/auth/register`
2. **Login** with credentials to receive access and refresh tokens via `/api/auth/login`
3. **Use access token** in Authorization header for protected API calls
4. **Refresh token** when access token expires using `/api/auth/refresh`
5. **Access resources** based on your role (USER can manage own tasks, ADMIN can manage all users)

## 🤝 Contribution

Contributions, issues, and feature requests are welcome! Please feel free to check the issues page and submit pull requests.
