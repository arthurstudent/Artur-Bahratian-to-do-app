# To-Do Application

This is a To-Do application written in Java using Spring components, including Spring Cloud. Below are instructions for setting up and running the application, as well as additional details about its functionality.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [Configuration Server](#configuration-server)
    - [Discovery Server](#discovery-server)
    - [Other Services](#other-services)
- [API Testing](#api-testing)
    - [Gateway Service](#gateway-service)
    - [Direct Access to Services](#direct-access-to-services)
- [Service Dependencies](#service-dependencies)
    - [User Service](#user-service)
    - [Task Service](#task-service)
    - [Task Service and User Service](#task-service-and-user-service)
    - [Task Service and Other Services](#task-service-and-other-services)
    - [Config Service](#config-service)
- [Folder Structure](#folder-structure)
- [Configuration Details](#configuration-details)
- [Notes](#notes)

## Prerequisites
Ensure you have the following installed:
- Java 17 or later
- Maven
- Postman (optional, for testing API endpoints)

## Getting Started
To run the application, the services must be started in a specific order:

### Configuration Server
This service provides configuration for other microservices.

### Discovery Server
Acts as a registry for other services.

### Other Services
Once the Config and Discovery servers are running, start the remaining services in any order.

## API Testing
In the `collection` folder, you will find a JSON file that can be imported into Postman. This collection contains pre-configured API requests for testing the application.

### Gateway Service
All API requests in the Postman collection are routed through the **Gateway Service**. This service acts as a central point for managing requests to different microservices.

### Direct Access to Services
Although the Gateway Service is recommended, it is also possible to interact with individual services directly without using the gateway.

## Service Dependencies

### User Service
- The **User Service** is responsible for:
    - Registering new users
    - Logging in users
    - Issuing JWT tokens

### Task Service
- The **Task Service** is responsible for managing tasks.
- Without a valid JWT token issued by the **User Service**, operations in the **Task Service** will be blocked.
- The **Task Service** supports CRUD operations for tasks and associated files.
- Users can upload files, and these files will be linked to the corresponding tasks.

### Task Service and User Service
- The **Task Service** works closely with the **User Service**.
- If the **User Service** is not running, the **Task Service** will not be able to validate tokens issued by the User Service. This may impact the application's functionality.

### Task Service and Other Services
The **Task Service** is designed to integrate seamlessly with other services, ensuring data consistency and proper functionality across the application.

### Config Service
The **Config Service** contains all configs and should be run first, so other service can get their configs 

## Folder Structure
- **collection**: Contains the Postman collection JSON file for testing API endpoints.

## Configuration Details
- In the **Configuration Server**, locate the configuration file for the **Task Service** and set your local path to the `temp` folder.
- This project uses two H2 databases that are accessible via the `/h2-console` endpoints of the Task and User services.
- All necessary settings are contained in configuration files stored in the **Configuration Server**.
- The `.env` file is not used because all information in this project is public and does not include any sensitive data.

## Usage Instructions

- First, register a new user using the User Service.

- Then, log in with the registered user's credentials.

- Upon successful login, a JWT token will be returned in the response.

- Use this token to authenticate and execute further API requests.

## Notes
- Use the Gateway Service for a unified entry point to the application.
- Ensure all dependent services are running to avoid issues with token validation and data synchronization.

