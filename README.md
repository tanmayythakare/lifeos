# LifeOS — Simple Task & Note Management System

LifeOS is a ditto clone of SecondBrain, built with **Spring Boot 3.2 (Java 17)** and **Angular 14**, completely stripped of AI components and deployment pipelines.

## Stack
- **Backend**: Java 17, Spring Boot 3.2, Spring Security (JWT), Spring Data JPA, PostgreSQL
- **Frontend**: Angular 14, Lucide Icons, CSS Design System (Indigo `#6366f1` Dark & Light Mode)
- **Containerization**: Docker & Docker Compose (Node-only static server for frontend, no Nginx)

## Quickstart with Docker Compose

```bash
docker-compose up --build
```

- **Frontend**: `http://localhost:4000`
- **Backend API**: `http://localhost:8080`
- **Swagger Docs**: `http://localhost:8080/swagger-ui/index.html`

## Manual Local Setup

### 1. Database
Ensure PostgreSQL is running on `localhost:5432` with database `lifeos_db`, username `lifeos_user`, password `lifeos_password`.

### 2. Backend
```bash
cd backend
mvn clean spring-boot:run
```

### 3. Frontend
```bash
cd frontend
npm install
npm start
```
App will open at `http://localhost:4000`.
