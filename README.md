# Farm Collector

## Overview
**Farm Collector** is a Spring Boot-based project designed to handle high-throughput requests for farm data collection and processing. It provides efficient request handling with a focus on logging and tracing via correlation IDs for request tracking, utilizing a highly concurrent setup to support robust performance under load.

## Table of Contents
1. [Project Structure](#project-structure)
2. [Setup and Installation](#setup-and-installation)
3. [Configuration](#configuration)
4. [Logging and Monitoring](#logging-and-monitoring)
5. [Running the Application](#running-the-application)
6. [Testing](#testing)
7. [Troubleshooting](#troubleshooting)
8. [Contributing](#contributing)
9. [License](#license)

## Project Structure
This project uses the **Spring Boot** framework, structured as follows:

- `src/main/java/com/kehinde/farmproject`: Contains all main source files for the project.
    - `controller`: REST controllers for handling incoming API requests.
    - `service`: Business logic implementation layer.
    - `repository`: Database interaction using Spring Data JPA.
    - `config`: Configuration files, including custom filters for logging and tracing.
    - `LoggingFilter.java`: Implements a filter to log each request with a unique correlation ID.
    - `FarmProjectApplication.java`: Main application class.

- `src/test/java/com/kehinde/farmproject`: Contains unit and integration tests.

## Setup and Installation
### Prerequisites
Ensure the following software is installed:
- Java 17 or higher
- Maven 3.6 or higher
- MySQL (optional if using an in-memory database for testing)

### Clone the Repository
```bash
git clone https://github.com/Kehinde-O/farm_collector.git
```

## Build and Run
### Build the project:
```mvn clean install```

## Run the application:
```mvn spring-boot:run```

## Configuration

Application Properties
The project’s configuration files are located in src/main/resources/application.properties. Update the following properties for your environment:

# Server configuration
server.port=8080

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/farmcollector_db
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password

# JPA/Hibernate settings
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
Logging and Monitoring

### Correlation ID Logging
Each incoming request is assigned a unique correlation ID for tracking. The LoggingFilter class intercepts requests and responses, logging details with this ID to allow easy tracing across the application. The logs are structured as follows:

### Incoming Request: Logged with the HTTP method, URI, and correlation ID.
Outgoing Response: Logged with the correlation ID and HTTP status.
MDC (Mapped Diagnostic Context)
The application uses MDC to store the correlation ID, ensuring each request can be uniquely identified across different logs and threads. See LoggingFilter.java for implementation details.

## Sample Log Format
INFO [pool-5-thread-7] c.k.f.g.f.config.LoggingFilter: Incoming Request: [36b68161-a7c3-4f03-8899-4880730d14cc] GET /api/v1/farm/data
INFO [pool-5-thread-7] c.k.f.g.f.config.LoggingFilter: Outgoing Response: [36b68161-a7c3-4f03-8899-4880730d14cc] Status 200
Running the Application

Start the Server: Run the application as explained in the Build and Run section.
Access API Endpoints: The application’s main API endpoints include:
GET /api/v1/farm/data: Fetch farm data.
POST /api/v1/farm/collect: Submit farm data for collection.
For a complete list of endpoints and payload details, refer to the swagger-ui documentation at http://localhost:8080/swagger-ui.

## Testing

Unit and integration tests are located in src/test/java/com/kehinde/farm_collector. To run all tests:

mvn test
Running a Single Test

To run a specific test method:

mvn -Dtest=YourTestClass#yourTestMethod test
