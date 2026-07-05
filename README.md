# FitnessApp- Microservices

This repository contains a full-stack fitness application built as a set of Spring Boot microservices with a React frontend.

## Project Overview

The system is organized into independent services that work together to provide a modern fitness experience:

- User management and authentication through `userservice`
- Activity and fitness tracking through `activityservice`
- AI-powered features through `aiservice`
- API routing and centralized access through `gateway`
- Service discovery through `eureka`
- Centralized configuration through `configserver`
- Asynchronous messaging through Apache Kafka
- A modern web client in `FitnessFrontend/FitnessApp`

## Architecture

The application follows a microservices architecture:

- Frontend: React + Vite + Material UI
- Backend: Java 21 + Spring Boot + Spring Cloud
- Service discovery: Eureka
- Configuration management: Spring Cloud Config
- API gateway: Spring Cloud Gateway
- Message broker: Apache Kafka
- Data layer: PostgreSQL for user-related services

## Repository Structure

- `userservice/` - User registration, authentication, and profile management
- `activityservice/` - Activity and workout-related operations
- `aiservice/` - AI-based fitness features
- `gateway/` - API gateway for routing requests to backend services
- `configserver/` - Central configuration server
- `eureka/` - Service registry and discovery
- `FitnessFrontend/FitnessApp/` - React frontend application

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Cloud Config
- Spring Security
- Eureka Discovery Client
- Apache Kafka
- Maven

### Frontend
- React 19
- Vite
- Material UI
- Redux Toolkit
- Axios

## Getting Started

### Prerequisites
- JDK 21 or newer
- Maven
- Node.js and npm
- Docker and Docker Compose

### Run the backend services
From each backend service directory, run:

```bash
./mvnw spring-boot:run
```

A typical startup order is:
1. `configserver`
2. `eureka`
3. `userservice`
4. `activityservice`
5. `aiservice`
6. `gateway`

### Run Kafka with Docker
Start Kafka and Zookeeper using Docker Compose:

```yaml
version: "3.8"
services:
  zookeeper:
    image: bitnami/zookeeper:3.9
    container_name: zookeeper
    environment:
      ALLOW_ANONYMOUS_LOGIN: yes
    ports:
      - "2181:2181"

  kafka:
    image: bitnami/kafka:3.7
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_LISTENERS: PLAINTEXT://:9092
      KAFKA_CFG_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      ALLOW_PLAINTEXT_LISTENER: yes
    depends_on:
      - zookeeper
```

Run:

```bash
docker compose up -d
```

### Run the frontend
From the frontend folder:

```bash
cd FitnessFrontend/FitnessApp
npm install
npm run dev
```

## Notes

This project is intended as a microservices-based fitness platform and can be extended with additional services such as payments, notifications, or recommendations.
