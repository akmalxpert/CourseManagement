# Course Management System 🎓

A comprehensive course management application designed for educational institutions (schools, universities) to efficiently manage courses, students, teachers, and academic groups. This project demonstrates modern enterprise Java development practices with a multi-module architecture.

> **Note**: This is a portfolio project developed to showcase software engineering skills and does not represent any actual service or company.

## 📱 DEMO
![DEMO.gif](DEMO.gif)

## 🚀 Features

### Core Functionality
- **School Management**: Create and manage multiple educational institutions
- **Course Management**: Define courses with codes, descriptions, and school associations
- **Student Management**: Register students, assign to groups, and enroll in courses
- **Teacher Management**: Manage faculty with positions, contact info, and course assignments
- **Group Management**: Organize students by academic level and faculty
- **Academic Level Tracking**: Monitor student progression through different levels

### Technical Features
- **RESTful API**: Complete REST API with proper HTTP methods and status codes
- **Database Migrations**: Flyway-based schema versioning and data seeding
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Containerization**: Docker and Docker Compose for easy deployment
- **Web Interface**: Thymeleaf-based web UI for administration
- **Input Validation**: Comprehensive validation layer for data integrity
- **Exception Handling**: Global exception handling with proper error responses

## 🏗️ Architecture

This project follows a **multi-module Maven architecture** with clear separation of concerns:

```
CourseManagement/
├── web/                 # Presentation Layer
│   ├── controller/      # Controllers (API & View)
│   │   ├── api/        # REST API endpoints
│   │   └── view/       # Web page controllers
│   ├── swagger/        # API documentation config
│   └── templates/      # Thymeleaf templates
├── service/             # Business Logic Layer
│   ├── service/        # Business services
│   ├── validations/    # Input validation
│   ├── mapper/         # DTO transformations
│   ├── exception/      # Custom exceptions
│   └── utils/          # Utility classes
└── dao/                 # Data Access Layer
    ├── entity/         # JPA entities
    ├── repository/     # Data repositories
    ├── dtos/          # Data transfer objects
    └── enums/         # Domain enumerations
```

## 🛠️ Technology Stack

### Backend
- **Java 11** - Programming language
- **Spring Boot 2.7.0** - Application framework
- **Spring Data JPA** - Data persistence
- **Spring Web** - REST API development
- **Hibernate** - ORM framework
- **Maven** - Dependency management and build tool

### Database
- **PostgreSQL** - Primary database
- **Flyway** - Database migration tool
- **UUID** - Primary key strategy for better scalability

### Frontend
- **Thymeleaf** - Server-side templating engine
- **Bootstrap 5.3** - Latest CSS framework for responsive UI
- **HTML5/CSS3** - Modern web markup and styling
- **JavaScript ES6+** - Client-side functionality and validation
- **Font Awesome 6.4** - Professional icon library
- **Google Fonts (Inter)** - Modern typography

#### Static Resources Structure
```
web/src/main/resources/static/
├── css/
│   ├── main.css          # Core styles and component definitions
│   └── components.css    # Specialized component styles and utilities
└── js/
    ├── main.js          # Core JavaScript functionality and utilities
    └── table-search.js  # Enhanced table search and filtering
```

### Documentation & API
- **Swagger/OpenAPI 3** - API documentation
- **SpringFox (v3.0.0)** - Swagger integration with Spring Boot

### Testing
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integration testing

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

### Development Tools
- **Lombok** - Reduces boilerplate code
- **Log4j2 (v2.18.0)** - Advanced logging framework with YAML configuration
- **Jackson (v2.13.3)** - JSON processing and YAML configuration support

## 📋 Prerequisites

Before running this project, make sure you have the following installed:

- **Docker** (version 20.0+)
- **Docker Compose** (version 1.29+)
- **Java 11** (for local development)
- **Maven 3.6+** (for local development)

## 🚀 Quick Start

### Using Docker (Recommended)

1. **Clone the repository**
   ```bash
   git clone https://github.com/akmalxpert/CourseManagement.git
   cd CourseManagement
   ```

2. **Start the application**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - **Web Interface**: http://localhost:8081
   - **API Documentation**: http://localhost:8081/swagger-ui/
   - **API Base URL**: http://localhost:8081/api

4. **Stop the application**
   ```bash
   docker-compose down
   ```

### Local Development Setup

1. **Prerequisites**
   - Ensure PostgreSQL is running locally on port 5435 (or configure your preferred port)
   - Create database: `course_app_db`

2. **Clone and build**
   ```bash
   git clone https://github.com/akmalxpert/CourseManagement.git
   cd CourseManagement
   mvn clean install
   ```

3. **Run the application**
   ```bash
   cd web
   mvn spring-boot:run
   ```

4. **Access the application**
   - Application: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui/

## 📊 Database Schema

The application uses a well-designed relational database schema with UUID primary keys:

### Core Entities
- **School**: Educational institutions with address, name, phone number, and postal code
- **Course**: Academic courses with code, description, name, and school association
- **Group**: Student groups organized by level, faculty, and school
- **Student**: Enrolled students with full name, group assignment, and academic level
- **Teacher**: Faculty members with email, office phone, and school association

### Database Migrations
The schema is managed through Flyway migrations:
- **V1.1**: Initial main tables (school, course, groups)
- **V1.2**: Secondary tables (student, teacher, junction tables)
- **V1.3**: Sample data insertion for development and testing

### Key Relationships
- Schools have multiple Courses and Groups (one-to-many)
- Students belong to Groups and can enroll in multiple Courses (many-to-many via student_courses)
- Teachers can teach multiple Courses (many-to-many via teacher_courses)
- Teachers have multiple positions (one-to-many via teacher_positions)
- All entities use UUID primary keys for better scalability and security

## 🔌 API Endpoints

The application provides REST APIs for managing all core entities. All endpoints follow RESTful conventions and return JSON responses with a standard `ResponseData` format.

### API Overview
- **Base URL**: `/api`
- **Response Format**: All endpoints return `ResponseData<T>` with data, message, and status code
- **Authentication**: Currently open (no authentication required)
- **Content-Type**: `application/json` for request/response bodies

### Available Endpoints
- **Schools**: `/api/school` - Complete CRUD operations for educational institutions
- **Courses**: `/api/course` - Course management with school associations and filtering
- **Students**: `/api/student` - Student registration, group assignment, and level tracking
- **Teachers**: `/api/teacher` - Faculty management with position and course assignments  
- **Groups**: `/api/group` - Academic group organization with faculty and level filtering

### Key Features
- **Flexible Filtering**: Query courses by school or group, students by group or level
- **Relationship Management**: Proper handling of entity relationships and constraints
- **Validation**: Comprehensive input validation for all create/update operations
- **Error Handling**: Standardized error responses with appropriate HTTP status codes

For detailed API documentation with request/response examples, visit the Swagger UI at `/swagger-ui/` when the application is running.

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run tests for specific module
mvn test -pl service
mvn test -pl dao
mvn test -pl web
```

### Test Coverage
- **Unit Tests**: Service layer business logic (CourseServiceImpl, StudentServiceImpl, TeacherServiceImpl, SchoolServiceImpl, GroupServiceImpl)
- **Validation Tests**: Input validation and error scenarios (CourseValidation, StudentValidation, TeacherValidation, SchoolValidation, GroupValidation)
- **Parameterized Tests**: JUnit 5 parameterized tests for comprehensive validation testing
- **Mock-based Testing**: Mockito integration for isolated unit testing
- **Test Organization**: Structured test packages mirroring main source structure

## 📁 Project Structure

```
CourseManagement/
├── web/src/main/
│   ├── java/uz/exadel/
│   │   ├── controller/          # Controllers
│   │   │   ├── api/            # REST API endpoints
│   │   │   └── view/           # Thymeleaf view controllers
│   │   ├── swagger/            # Swagger configuration
│   │   └── ControllerApplication.java
│   └── resources/
│       ├── db/migration/       # Flyway migration scripts
│       ├── templates/          # Thymeleaf templates
│       ├── application.yml     # Application configuration
│       └── log4j2.yml         # Logging configuration
├── service/src/main/java/uz/exadel/
│   ├── service/               # Business services & implementations
│   ├── validations/           # Input validation services
│   ├── mapper/                # Entity-DTO mappers
│   ├── exception/             # Custom exceptions & global handler
│   └── utils/                 # Utility classes
├── dao/src/main/java/uz/exadel/
│   ├── entity/                # JPA entities
│   ├── repository/            # Spring Data repositories
│   ├── dtos/                  # Data transfer objects
│   └── enums/                 # Domain enumerations
└── */src/test/                # Test classes for each module
```

## 🔧 Configuration

### Environment Variables

**🔐 Security First**: This application uses environment variables for all sensitive configuration.

1. **Copy the example environment file**:
   ```bash
   cp env.example .env
   ```

2. **Configure your credentials** in `.env`:
   ```env
   # Database Configuration
   DB_HOST=localhost
   DB_PORT=5435
   DB_NAME=course_app_db
   DB_USERNAME=postgres
   DB_PASSWORD=your_secure_password_here

   # Application Configuration
   SERVER_PORT=8080
   LOG_LEVEL=INFO

   # Docker Database Configuration (for docker-compose)
   POSTGRES_USER=postgres
   POSTGRES_PASSWORD=your_secure_password_here
   POSTGRES_DB=course_app_db
   DOCKER_DB_PORT=5435
   DOCKER_APP_PORT=8081
   ```

3. **Important**: Never commit `.env` files to version control. The `.gitignore` file already excludes them.

For error handling and logging information, see [ERROR_HANDLING.md](ERROR_HANDLING.md).

### Application Properties
Key configuration options in `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5435}/${DB_NAME:course_app_db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:This28isdb}
  
  sql:
    init:
      mode: always

logging:
  level:
    org.springframework.web: ${LOG_LEVEL:info}
    uz.exadel: ${LOG_LEVEL:info}
```

## 🚀 Deployment

### Docker Deployment
The application is containerized and can be deployed using Docker:

   ```bash
   # Build and deploy
   docker-compose up -d

   # View logs
   docker-compose logs -f course-app

   # Stop services
   docker-compose down
   ```

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 📞 Contact

**Akmaljon Samandarov**
- GitHub: [@akmalxpert](https://github.com/akmalxpert)
- Email: akmaljonsamandarov@gmail.com

---

**© 2025 Akmaljon Samandarov. All rights reserved.**

*This project was developed as a portfolio demonstration of enterprise Java development skills and best practices.*