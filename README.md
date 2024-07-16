# Notes API

This is a simple Notes API built with Spring Boot that allows users to create, read, update, and delete notes. The API
also supports authentication and note sharing features.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- PostgreSQL

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/jazzcowboy616/notesapi.git
   cd notesapi

2. **Create the database:**

```sql
CREATE
DATABASE notedb
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
```

3. **Configure the database:**

```yaml
spring:
   datasource:
      driver-class-name: org.postgresql.Driver
      url: jdbc:postgresql://localhost:5432/notedb
      username: username
      password: password
```

Details explaining the choice of framework/db/ any 3rd party tools.
instructions on how to run your code and run the tests.
Any necessary setup files or scripts to run your code locally or in a test environment.

Authentication Endpoints
POST /api/auth/signup: create a new user account.x
POST /api/auth/login: log in to an existing user account and receive an access token.x

Note Endpoints
GET /api/notes: get a list of all notes for the authenticated user.x
GET /api/notes/:id: get a note by ID for the authenticated user.x
POST /api/notes: create a new note for the authenticated user.x
PUT /api/notes/:id: update an existing note by ID for the authenticated user.x
DELETE /api/notes/:id: delete a note by ID for the authenticated user.x
POST /api/notes/:id/share: share a note with another user for the authenticated user.x
GET /api/search?q=:query: search for notes based on keywords for the authenticated user.x