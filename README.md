# E-Commerce Microservices Platform

A scalable, cloud-native e-commerce backend built with **Spring Boot** and **microservices architecture**, designed to handle user authentication, product management, order processing, and payment integration. This project demonstrates enterprise-grade skills in Java, microservices, event-driven architecture, and cloud deployment, tailored for high-demand tech roles.

## Project Overview
This project simulates a modern e-commerce platform with four core microservices:
- **User Service**: Manages user registration, authentication, and profiles using JWT and Spring Security.
- **Product Service**: Handles product catalog with CRUD operations and pagination, stored in MongoDB.
- **Order Service**: Processes orders with inventory validation, using REST and Kafka for async communication.
- **Payment Service**: Simulates payment processing, integrated via Kafka for event-driven updates.

The system leverages **Spring Cloud** for service discovery (Eureka) and routing (API Gateway), **Kafka** for event-driven communication, and **Docker** for containerization. It is deployed on **AWS EC2** (free tier) to showcase cloud proficiency.

## High-Level Design (HLD)
The architecture follows a microservices pattern, ensuring scalability, fault tolerance, and loose coupling. Key components:
- **API Gateway**: Routes client requests to appropriate services.
- **Eureka Server**: Enables dynamic service discovery.
- **Kafka**: Facilitates async communication (e.g., order placement triggers payment processing).
- **Databases**: MySQL for relational data (users, orders), MongoDB for flexible product data.

### Architecture Diagram
## mermaid
graph TD
    A[Client (UI/App)] --> B[API Gateway]
    B --> C[Eureka Server]
    B --> D[User Service (8081)]
    B --> E[Product Service (8082)]
    B --> F[Order Service (8083)]
    B --> G[Payment Service (8084)]
    F -->|Kafka: order-placed| G
    G -->|Kafka: payment-confirmed| F
    D --> H[MySQL]
    F --> H
    E --> I[MongoDB]


    ### Challenges Faced
- **Maven Wrapper Missing**: Lacked `mvnw.cmd` for builds. Generated using `mvn -N wrapper:wrapper` after installing Maven 3.9.11.
- **Eureka Server Failure**: `DataSourceAutoConfiguration` error due to default package. Moved `EurekaServerApplication.java` to correct package and excluded unnecessary auto-configuration.
- **Dependency Vulnerabilities**: Addressed CVEs by updating `spring-cloud-dependencies` to 2023.0.3.
- **Deprecated Spring Security APIs**: Updated `httpBasic()` and password encoding to modern APIs (`httpBasic(withDefaults())`, `BCryptPasswordEncoder`).
- **Actuator Endpoint Issues**: Fixed 404 and empty `/actuator/info` by enabling Actuator and build metadata.
-  **Challenge**: Database selection for microservices.
- **Solution**: Used MySQL for structured user/order data, MongoDB for flexible product data.
- **Challenge**: Centralized basic authentication.
- **Solution**: Moved basic auth to `api-gateway` using Spring Security, securing all service endpoints.
- **Challenge**: Setting up auth-service for database-backed authentication.
- **Solution**: Created `auth-service` with MySQL and Eureka integration.
