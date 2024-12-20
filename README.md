# **Email API Gateway Microservice**

## **Overview**

The **Email API Gateway** microservice is the centralized entry point for managing and routing HTTP requests across various microservices within the system. It uses **Spring Cloud Gateway** for building efficient, scalable, and secure API gateways that streamline request handling while applying cross-cutting concerns like authentication and routing.

### **Purpose**

The primary goal of this microservice is to simplify communication between clients and backend services by providing:

1. **Centralized Routing:** Directs incoming requests to the appropriate downstream microservices based on predefined URI paths.
2. **Security Layer:** Applies authentication filters using JSON Web Tokens (JWT) to validate user access to protected routes.
3. **Load Distribution:** Balances requests across backend services to improve scalability and performance.
4. **Enhanced Monitoring:** Offers health checks and metrics for all connected routes to ensure the system's availability and reliability.

### **Key Responsibilities**

- **Authentication and Authorization:**

  Routes marked as "secured" are protected with JWT authentication. Requests without valid tokens are rejected with an appropriate error response (HTTP 401).

- **Dynamic Routing:**

  Routes requests to the correct microservice based on the path prefixes. For example:

    - Requests with the prefix **`/auth/**`** are routed to the **Authentication Service**.
    - Requests with **`/user/**`** are routed to the **Users Service**.
- **Flexible Configuration:**

  The gateway is highly configurable via the **`application.yaml`** file, allowing easy modifications to routes, filters, and server settings.

- **Scalability:**

  As new microservices are introduced, they can easily be integrated into the system by adding their routes and configurations to the gateway without changes to the client applications.


---

## **Example Workflow**

1. **Client Request:**

   A client application sends a request to the gateway, such as **`/user/profile`**.

2. **Routing:**
    - The gateway evaluates the request path (**`/user/**`**) and forwards it to the **Users Service**.
    - If the route is secured, the **AuthenticationFilter** checks for a valid JWT token in the request header.
3. **Response:**
    - If authentication is successful, the gateway forwards the request to the destination microservice and returns its response to the client.
    - If authentication fails or the token is missing/expired, the gateway sends a **`401 Unauthorized`** response.

### **Example of Secured Route Handling**

1. **Request Headers:**

    ```
    Authorization: Bearer <JWT_TOKEN>
    ```

2. **JWT Validation Flow:**
    - **AuthenticationFilter** extracts the token from the **`Authorization`** header.
    - The token is passed to the **`JwtUtils`** service for validation and to extract user information.
    - If valid, the gateway attaches the user's ID to the request headers and forwards it to the downstream service.
3. **Failure Case:**

   If the token is invalid, expired, or missing, the request is terminated with an appropriate error response.


---

## **System Integration**

The Email API Gateway interacts with the following microservices:

| **Service** | **Base URI** | **Secured** | **Role** |
| --- | --- | --- | --- |
| Authentication Service | **`http://localhost:8081`** | Yes | Manages user registration, login, and JWT generation. |
| Users Service | **`http://localhost:8084`** | Yes | Handles user-related data retrieval and operations. |
| Letters Service | **`http://localhost:8083`** | Yes | Manages the creation, validation, and updates of letters. |
| Packages Service | **`http://localhost:8082`** | Yes | Handles package operations, including dimension validation. |

---

### **Why Use a Gateway?**

1. **Unified Access:** Clients interact with a single endpoint instead of managing multiple service URLs.
2. **Scalability:** Decouples the client-side from the backend services, allowing independent scaling and updates.
3. **Security:** Centralizes authentication and authorization logic, reducing the risk of inconsistencies.
4. **Efficiency:** Reduces round-trip times by routing and filtering requests at the gateway level.

This gateway acts as the backbone of your microservices architecture, ensuring efficient, secure, and seamless communication between the client applications and backend services

## **Features**

- **Routing and Filtering:** Routes requests to microservices like Authentication, Users, Letters, and Packages based on predefined paths.
- **JWT Authentication:** Ensures that secure routes are only accessible with a valid JWT token.
- **Health Monitoring:** Supports endpoint health checks with detailed status.
- **Centralized Gateway:** Serves as a single entry point for the entire system, improving security and scalability.

---

## **Table of Contents**

1. [Technology Stack](https://chatgpt.com/c/6736297a-b448-8011-919c-786085c83bb4#technology-stack)
2. [Installation and Setup](https://chatgpt.com/c/6736297a-b448-8011-919c-786085c83bb4#installation-and-setup)
3. [Configuration](https://chatgpt.com/c/6736297a-b448-8011-919c-786085c83bb4#configuration)
4. [Endpoints](https://chatgpt.com/c/6736297a-b448-8011-919c-786085c83bb4#endpoints)
5. [Folder Structure](https://chatgpt.com/c/6736297a-b448-8011-919c-786085c83bb4#folder-structure)

---

## **Technology Stack**

- **Framework:** Spring Boot (Spring Cloud Gateway)
- **Language:** Java 17
- **JWT Library:** JJWT
- **Build Tool:** Maven

---

## **Installation and Setup**

### **Prerequisites**

- Java 17 or later
- Maven 3.8+
- Spring Boot
- A running instance of the dependent microservices:
    - Authentication Service (port: **`8081`**)
    - Users Service (port: **`8084`**)
    - Letters Service (port: **`8083`**)
    - Packages Service (port: **`8082`**)

### **Steps**

1. Clone the repository.
2. Navigate to the project directory.
3. Run the following Maven command to install dependencies:

    ```bash
    mvn clean install
    ```

4. Start the application:

    ```bash
    mvn spring-boot:run
    ```


---

## **Configuration**

### **`application.yaml`**

Below is the primary configuration used by the Email API Gateway:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-route
          uri: http://localhost:8081
          predicates:
            - Path=/auth/**
        - id: users-route
          uri: http://localhost:8084
          predicates:
            - Path=/user/**
          filters:
            - AuthenticationFilter
        - id: letters-route
          uri: http://localhost:8083
          predicates:
            - Path=/letter/**
          filters:
            - AuthenticationFilter
        - id: packages-route
          uri: http://localhost:8082
          predicates:
            - Path=/package/**
          filters:
            - AuthenticationFilter
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
    gateway:
      enabled: true
server:
  port: 8080
```

> [!Note]
> You must have the related microservices along with this service since they all work together. Please find them below this line

## Related Microservices

The system consists of multiple microservices that work together to provide comprehensive functionality. Below is a list of all the microservices in the system, with links to their respective repositories:

- [**users-service-api**](https://github.com/juansebstt/users-service-api): Handles user management, including registration, profile updates, and account data.
- [**email-kafka-microservice**](https://github.com/juansebstt/email-kafka-microservice): Manages asynchronous email event processing using Kafka for reliable messaging.
- [**notifications-microservice-api**](https://github.com/juansebstt/notifications-microservice-api): Sends notifications based on triggered events from other services.
- [**email-authentication-service-api**](https://github.com/juansebstt/email-authentication-service-api): Manages email-based authentication and verification processes.
- [**email-api-gateway**](https://github.com/juansebstt/email-api-gateway): Serves as the entry point for routing requests to various microservices.
- [**letter-service-api**](https://github.com/juansebstt/letter-service-api): Manages letters, including creation, storage, and retrieval.
- [**packages-service-api**](https://github.com/juansebstt/packages-service-api): Manages package-related operations, including tracking and status updates.


### **Key Configuration Details**

- **Routes:** Defines how requests are mapped to downstream services.
- **Filters:** Applies **`AuthenticationFilter`** for secured routes.
- **Server Port:** The gateway listens on port **`8080`**.

---

## **Endpoints**

| **Route** | **Downstream URI** | **Secured** |
| --- | --- | --- |
| **`/auth/**`** | **`http://localhost:8081`** | Yes |
| **`/user/**`** | **`http://localhost:8084`** | Yes |
| **`/letter/**`** | **`http://localhost:8083`** | Yes |
| **`/package/**`** | **`http://localhost:8082`** | Yes |

---

## **Folder Structure**

```markdown
com.emailapigateway
├── service
│   ├── AuthenticationFilter.java
│   ├── JwtUtils.java
│   └── RouteValidator.java
└── resources
    └── application.yaml
```

- **`AuthenticationFilter`**: Implements token-based validation for secured routes.
- **`JwtUtils`**: Utility class for parsing and validating JWT tokens.
- **`RouteValidator`**: Validates whether a route is secured or open.
- **`application.yaml`**: Contains configuration for routing, security, and server settings.

---

## **Security**

The **AuthenticationFilter** ensures that all requests to secured routes include a valid JWT token. The token is verified using the **`JwtUtils`** service. Unauthorized requests receive a **`401 Unauthorized`** response.

### **Open Routes**

- **`/auth`**
- **`/swagger-ui.html`**
- **`/v3/api-docs`**