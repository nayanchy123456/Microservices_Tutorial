# 🧩 Quiz Microservices Application

A Spring Boot microservices project implementing a Quiz application with service discovery, API gateway routing, and inter-service communication via OpenFeign.

---

## 📐 Architecture

```
Client
  │
  ▼
API Gateway (port 8765)          ← Single entry point for all requests
  │              │
  ▼              ▼
Quiz Service   Question Service
(port 8080)    (port 8081)
  │              │
  └──────────────┘
         │
         ▼
   MySQL Database
   (quizService)
         │
         ▼
  Eureka Service Registry (port 8761)
  (all services register here)
```

All four services register with the Eureka Service Registry. The API Gateway uses Eureka to resolve `lb://quiz-service` and `lb://question-service` load-balanced URIs. The Quiz Service communicates with the Question Service internally via OpenFeign (also resolved through Eureka).

---

## 🗂️ Project Structure

```
Microservices_Tutorial/
├── service_registry/       # Eureka Server — service discovery
├── api_gateway/            # Spring Cloud Gateway — routes all traffic
├── quiz_service/           # Manages quizzes
└── question_service/       # Manages questions
```

---

## 🛠️ Tech Stack

| Layer              | Technology                              |
|--------------------|-----------------------------------------|
| Language           | Java 21                                 |
| Framework          | Spring Boot 4.0.5                       |
| Service Discovery  | Spring Cloud Netflix Eureka             |
| API Gateway        | Spring Cloud Gateway Server WebMVC      |
| Inter-service Comm | Spring Cloud OpenFeign                  |
| Load Balancing     | Spring Cloud LoadBalancer               |
| ORM                | Spring Data JPA / Hibernate             |
| Database           | MySQL                                   |
| Build Tool         | Maven                                   |
| Spring Cloud BOM   | 2025.1.1                                |

---

## ⚙️ Services Overview

### 1. Service Registry — `service_registry`
- Runs a Netflix Eureka Server
- All other services register with it on startup
- **Port:** `8761`
- **Dashboard:** `http://localhost:8761`

### 2. API Gateway — `api_gateway`
- Single entry point for all client requests
- Routes `/quiz/**` → Quiz Service
- Routes `/question/**` → Question Service
- Uses `lb://` URIs for client-side load balancing via Eureka
- **Port:** `8765`

### 3. Quiz Service — `quiz_service`
- Manages quiz entities (create, list, get by ID)
- Fetches associated questions from Question Service via Feign Client
- **Port:** `8080`

### 4. Question Service — `question_service`
- Manages question entities (create, list, get by ID, get by quiz ID)
- **Port:** `8081`

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL running locally on port `3306`

### Database Setup

Create the database before starting any service:

```sql
CREATE DATABASE quizService;
```

### Default DB credentials (update in each service's `application.properties` if needed)

```
username: root
password: root
```

---

## ▶️ Running the Services

> **Important:** Start services in this exact order. Each service depends on the one before it.

**Step 1 — Start the Eureka Service Registry**
```bash
cd service_registry
mvn spring-boot:run
```
Wait until you see the Eureka dashboard at `http://localhost:8761`.

**Step 2 — Start the Question Service**
```bash
cd question_service
mvn spring-boot:run
```

**Step 3 — Start the Quiz Service**
```bash
cd quiz_service
mvn spring-boot:run
```

**Step 4 — Start the API Gateway**
```bash
cd api_gateway
mvn spring-boot:run
```

---

## 🔌 API Endpoints

All requests go through the **API Gateway at port `8765`**.

### Question Service

| Method | Endpoint                        | Description                        |
|--------|---------------------------------|------------------------------------|
| POST   | `/question`                     | Create a new question              |
| GET    | `/question`                     | Get all questions                  |
| GET    | `/question/{questionId}`        | Get a question by ID               |
| GET    | `/question/quiz/{quizId}`       | Get all questions for a given quiz |

**Example — Create a question:**
```bash
curl -X POST http://localhost:8765/question \
  -H "Content-Type: application/json" \
  -d '{"question": "What is Java?", "quizId": 1}'
```

### Quiz Service

| Method | Endpoint        | Description              |
|--------|-----------------|--------------------------|
| POST   | `/quiz`         | Create a new quiz        |
| GET    | `/quiz`         | Get all quizzes          |
| GET    | `/quiz/{id}`    | Get a quiz by ID (with questions) |

**Example — Create a quiz:**
```bash
curl -X POST http://localhost:8765/quiz \
  -H "Content-Type: application/json" \
  -d '{"title": "Java Basics"}'
```

**Example — Get quiz with questions:**
```bash
curl http://localhost:8765/quiz/1
```

---

## 🔍 Verifying Services in Eureka

Once all services are running, open `http://localhost:8761` in your browser. You should see all three services registered:

- `QUIZ-SERVICE`
- `QUESTION-SERVICE`
- `API-GATEWAY`

---

## 📋 Configuration Reference

### `service_registry/application.properties`
```properties
spring.application.name=service-registry
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

### `api_gateway/application.properties`
```properties
spring.application.name=api-gateway
server.port=8765
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
spring.cloud.gateway.server.webmvc.discovery.locator.enabled=true
spring.cloud.gateway.server.webmvc.discovery.locator.lower-case-service-id=true
spring.cloud.gateway.server.webmvc.routes[0].id=quiz-service
spring.cloud.gateway.server.webmvc.routes[0].uri=lb://quiz-service
spring.cloud.gateway.server.webmvc.routes[0].predicates[0]=Path=/quiz/**
spring.cloud.gateway.server.webmvc.routes[1].id=question-service
spring.cloud.gateway.server.webmvc.routes[1].uri=lb://question-service
spring.cloud.gateway.server.webmvc.routes[1].predicates[0]=Path=/question/**
```


## 📦 Dependencies (Key)

```xml
<!-- Service Registry -->
spring-cloud-starter-netflix-eureka-server

<!-- API Gateway -->
spring-cloud-starter-gateway-server-webmvc
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-loadbalancer

<!-- Quiz & Question Services -->
spring-boot-starter-data-jpa
spring-boot-starter-webmvc
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-openfeign   <!-- quiz-service only -->
mysql-connector-j
lombok
```

---

## 📝 License

This project is for educational purposes.
