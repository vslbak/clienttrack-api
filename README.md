# ClientTrack API

A clean Spring Boot backend powering the ClientTrack CRM demo under Codebshift.

## Tech Stack
- Java 21
- Spring Boot 3
- PostgreSQL
- Liquibase
- MapStruct
- Docker
- PDF generation

## Features
- Clients / deals / activities CRUD
- Deal stages + pipeline metrics
- Proposal PDF generator and previewer
- Dashboard metrics

## Run locally
```
docker compose up -d
./mvnw spring-boot:run
```

API base URL: `http://localhost:8080/api`

## ENV
```
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/clienttrack
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

## Docker build
```
docker build -t clienttrack-api .
docker compose up -d
```