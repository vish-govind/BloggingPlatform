# Blogging Platform

This is a robust blogging platform built with Spring Boot.
The application allows users to create and manage blogs and comments.

It supports both traditional JWT-based authentication as well as OAuth2 login. 
Admin users have additional privileges such as deleting any blogs or comments with inappropriate content and notifying users via email.

## Features
- **Blog Management:**  
  - Create, retrieve, update, and delete blogs.
  - Retrieve all blogs or a specific blog by ID.
  - Mapping of blog entities to DTOs for data transfer.

- **Comment Management:**  
  - Add, retrieve, update, and delete comments for a blog.
  - Retrieve all comments associated with a blog.
  
- **User Authentication & Authorization:**  
  - Traditional authentication using JWT.
  - OAuth2 integration for social login.
  - Secure endpoints with role-based access control (USER and ADMIN).
  
- **Caching:**  
  - Redis caching is integrated for blogs, comments, and OAuth2 users.
  - Configured with a TTL (time-to-live) of 180 minutes to improve performance.

- **Global Exception Handling:**  
  - Consistent error responses through a global exception handler (`@RestControllerAdvice`).

- **Email Notification:**  
  - Notifies users via email when an admin deletes a blog or comment due to inappropriate content.

- **Mapper Classes:**  
  - Dedicated mapper classes (e.g., `BlogMapper`, `CommentMapper`) convert entities to DTOs to streamline data transfer.

## Architecture

The application follows a layered architecture that includes:

- **Controllers:** REST controllers handling HTTP requests (e.g., `BlogController`, `CommentController`, `AuthController`, `AdminController`, `OAuth2Controller`).
- **Services:** Business logic encapsulated in service classes (e.g., `BlogServiceImpl`, `CommentServiceImpl`, `UserService`, `EmailService`).
- **Repositories:** Data persistence managed via Spring's `JdbcTemplate` with custom repository implementations.
- **Security:** Configured with Spring Security, JWT, and OAuth2 integration.
- **Caching:** Managed by Redis with a custom cache configuration.
- **Exception Handling:** Global exception handling via `GlobalExceptionHandler` to provide consistent error messages.
- **DTO Mapping:** Mapper classes (like `BlogMapper`) that convert entity objects to Data Transfer Objects (DTOs).

## Tech Stack

- **Backend Framework:** Spring Boot
- **Database:** MySQL (or any JDBC-supported database)
- **ORM/Data Access:** Spring JdbcTemplate
- **Security:** Spring Security, JWT, OAuth2
- **Caching:** Redis
- **Email:** JavaMailSender
- **Logging:** SLF4J with Logback

## Setup and Installation

- **Clone the Repository:**

   git clone https://github.com/vish-govind/BloggingPlatform.git

   cd blogging-platform

- **Update your application.yml with your actual database configuration:**

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yourdatabase             
    username: yourusername
    password: yourpassword

	  
- **Build the Project:** 
          mvn clean install

- **Run the Application:**
**Using the Maven Spring Boot plugin:**
mvn spring-boot:run
Or **execute the generated JAR file:**
java -jar target/blogging-platform.jar


# API Endpoints

- **Authentication**
POST /auth/login – Login with identifier (email or username) 
and password to receive a JWT token.

POST /auth/register – Register a new user.

GET /auth/hello – Test endpoint.

GET /auth/oauth2/token – Retrieve an OAuth2 token for authenticated OAuth2 users.

- **Blog Management**

GET /blogs – Retrieve all blogs.

GET /blogs/{id} – Retrieve a blog by its ID.

POST /blogs – Create a new blog (requires authentication).

PUT /blogs/{id} – Update an existing blog (only the author can update).

DELETE /blogs/{id} – Delete a blog (author or admin).

- **Comment Management**

POST /comments/{blogId} – Add a comment to a blog.

GET /comments/{blogId} – Retrieve all comments for a blog.

PUT /comments/{commentId} – Update a comment (only the comment author can update).

DELETE /comments/{commentId} – Delete a comment (author or admin).

- **Admin Endpoints**
DELETE /admin/blogs/{id} – Admin deletes a blog and notifies the blog author.

DELETE /admin/comments/{id} – Admin deletes a comment and notifies the comment author.

# Security
- **Authentication**:
JWT-based authentication is used for standard login, and OAuth2 is available for social logins. Tokens must be included in the Authorization header for secured endpoints.

- **Authorization**:
Endpoints are secured with role-based access (e.g., @PreAuthorize("hasRole('ADMIN')") for admin actions).

- **Security Configuration**:
The SecurityConfig class sets up the security filter chain, OAuth2 user service, and password encoder.

- **Caching**
Redis is configured as the caching provider with a TTL of 180 minutes. The CacheConfig class initializes the cache manager and sets the caching policies.

- **Exception Handling**
A global exception handler (GlobalExceptionHandler) is implemented using @RestControllerAdvice to handle common exceptions like EntityNotFoundException and generic exceptions, providing consistent error responses.

- **DTO Mapping**
Mapper classes (such as BlogMapper and CommentMapper) are used to convert entity objects to DTOs. This improves data transfer efficiency and decouples the internal data structure from API responses.

