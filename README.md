# Notes API

This is a simple Notes API built with Spring Boot that allows users to create, read, update, and delete notes. The API
also supports authentication and note sharing features.

## Project Overview

You have been asked to build a secure and scalable RESTful API that allows users to create, read, update, and delete
notes. The application should also allow users to share their notes with other users and search for notes based on
keywords.

## Technical Requirements

- Implement a RESTful API using a framework of your choice (e.g. Express, DRF, Spring).
- Use a database of your choice to store the data (preferably MongoDB or PostgreSQL).
- Use any authentication protocol and implement a simple rate limiting and request throttling to handle high traffic.
- Implement search functionality to enable users to search for notes based on keywords. ( preferably text indexing for
  high performance )
- Write unit tests and integration tests your API endpoints using a testing framework of your choice.

## Get Started

See the [guide](https://github.com/jazzcowboy616/notesapi/blob/master/QuickStarted.md) on getting started with the Note
API.

## Test

**Linux:**
   ```bash
   cd ${application.home}
   ./mvn test
   ```

**Windows:**

   ```bash
   cd ${application.home}
   mvnw.cmd test
   ```

You can also test the API using a tool like Postman or curl.

## APIs

The Note API provides the following endpoints:
```
Authentication Endpoints
POST /api/auth/signup               create a new user account.
POST /api/auth/login                log in to an existing user account and receive an access token.

Note Endpoints
GET /api/notes                      get a list of all notes for the authenticated user.
GET /api/notes/{id}                 get a note by ID for the authenticated user.
POST /api/notes                     create a new note for the authenticated user.
PUT /api/notes/{id}                 update an existing note by ID for the authenticated user.
DELETE /api/notes/{id}              delete a note by ID for the authenticated user.
POST /api/notes/{id}/share          share a note with another user for the authenticated user.
GET /api/search?search={query}      search for notes based on keywords for the authenticated user.
```

## Why Spring Boot

- Ease of Setup and Configuration

  Considering that we only have 48 hours to build this API, I need a strong and maturial framework that can quickly set
  up a new application. The Auto-configuration feature of Spring Boot meets my needs and save a lot of time. With
  minimal configuration, you can quickly start developing application.

- Embedded Server

  Spring Boot comes with an embedded server (Tomcat, Jetty, or Undertow) which eliminates the need for a separate server
  setup, making development and deployment straightforward. Here, I used Tomcat as the embedded server which is the
  default choice and can completely satisfy the requirements of this project.

- Dependency Management

  Spring Boot’s dependency management via Maven or Gradle ensures that you have the right versions of libraries and
  frameworks, reducing compatibility issues. You can easily deploy the application with only a few commands.

- Integration with Spring Ecosystem

  Seamless integration with other Spring projects like Spring Security, Spring Data, and Spring MVC, which are essential
  for building secure, data-driven applications. Spring community provides a lot of matural projects and plugins for
  easy development and good support. Even in later maintenance, it could save the cost of upgrading and refactoring. In
  this project, I leveraged various plugins to support the features of the API:
   - JPA for database access
   - Spring Security for authentication
   - Jsonwebtoken for token generation and validation
   - Google guava for rate limiting

## Why PostgreSQL

- Text indexing Support

  One of the major features of this note api application is the ability to search for notes based on keywords.
  PostgreSQL supports text indexing, which can help easily support the requirement and easily implement search
  functionality.

- ACID Compliance

  PostgreSQL is ACID-compliant, ensuring reliable transactions, data integrity, and fault tolerance, which are critical
  for any application dealing with user data.

- Rich SQL Support

  It supports advanced SQL features such as complex queries, joins, views, and stored procedures.

- Extensibility

  PostgreSQL is highly extensible. You can add custom functions using different programming languages such as PL/pgSQL,
  PL/Python, etc.

- Strong Community and Documentation

  PostgreSQL has a robust community and extensive documentation, making it easier to find support and resources.

- Performance and Scalability

  PostgreSQL is known for its performance and scalability. It can handle large datasets and high concurrency, which is
  essential for growing applications.

## Why Google Guava

- Simplicity and Ease of Use

  Guava provides a simple and intuitive API for rate limiting through the RateLimiter class. It's easy to integrate into
  code without extensive configuration.

- Efficient Rate Limiting

  Guava's RateLimiter uses a smooth, efficient algorithm (based on the token bucket algorithm) to ensure a consistent
  rate of requests over time, preventing bursts that could overwhelm your system.

- Flexibility

  Guava allows for dynamic rate adjustments and supports both fixed rate limiting and bursty traffic patterns, making it
  adaptable to various use cases.

- Proven and Reliable

  Guava is a well-established, widely used library in the Java ecosystem, providing robust and reliable utilities. Its
  rate limiting features have been battle-tested in many production systems.

- Thread Safety

  The RateLimiter class is designed to be thread-safe, ensuring that it can be used concurrently by multiple threads
  without additional synchronization.
