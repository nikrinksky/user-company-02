# User and Company Management System

Микросервисная система для управления компаниями и пользователями с REST API, построенная на Spring Cloud.

## 📦 Состав системы
user-company-02/

├── company-service/ # Сервис управления компаниями

├── user-service/ # Сервис управления пользователями

├── api-gateway/ # API Gateway (Spring Cloud Gateway)

├── service-discovery/ # Сервер Eureka для регистрации сервисов

├── config-service/ # Сервер запускающий файлы конфигурации

└── docker-compose.yml # Конфигурация Docker

## 🚀 Запуск системы

### Требования
- Java 17
- Docker и Docker Compose
- Maven 3.8+

### 1. Запуск инфраструктуры
```bash
docker-compose up -d postgres redis

2. Сборка и запуск сервисов
mvn clean package
java -jar service-discovery/target/*.jar
java -jar company-service/target/*.jar
java -jar user-service/target/*.jar
java -jar api-gateway/target/*.jar
java -jar config-gateway/target/*.jar


Или через Docker:
docker-compose up --build

🌐 API Endpoints
Company Service (8082):http://localhost:8082
или через api-gateway (8089): http://localhost:8089
GET /api/companies/{id} - Получить компанию с сотрудниками
GET /api/companies/with-users - Получить все компании с сотрудниками
GET /api/companies - Получить все компании (без сотрудников)
GET /api/companies/not-user/{id} - Получить компанию без сотрудников
POST /api/companies - Создать компанию
PUT /api/companies/{id} - Обновить компанию
DELETE /api/companies/{id} - Удалить компанию

User Service (8081):http://localhost:8081
или через api-gateway (8089): http://localhost:8089
GET /api/users/company/{companyId} -  Получить всех пользователей одной компании с данными о компании
GET /api/users/company - Получить всех пользователей с данными компаниях
GET /api/users/{id} - Получить пользователя только с id компании
GET /api/users - Получить всех пользователей только с id компании
PUT /api/users/{id} - Обновить пользователя
POST /api/users - Создать пользователя
DELETE /api/users/{id} - Удалить пользователя 
🛠 Технологии
Backend:
Spring Boot 3.2
Spring Cloud (Gateway, Eureka, Config)
Spring Data JPA
Feign Client

Базы данных:
PostgreSQL (основная БД)

Инфраструктура:
Docker
Maven

🔧 Конфигурация
Основные настройки в config-service:
# Пример конфигурации company-service
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/company_db
    username: postgres
    password: postgres
user:
  service:
    url: http://user-service:8081

🐛 Отладка
Проверить состояние сервисов:
curl http://localhost:8761/eureka/apps
Логи в реальном времени:
docker-compose logs -f company-service

📝 Особенности реализации
Межсервисное взаимодействие:
CompanyService → UserService через Feign Client
Circuit Breaker с Fallback

DTO-модели:
public record CompanyResponseDTO(
    Long id,
    String name,
    Double budget,
    List<UserDTO> employees
) {}

📈 Дальнейшее развитие
Добавить аутентификацию (JWT)
Реализовать кеширование (Redis)
Настроить мониторинг (Prometheus + Grafana)
Добавить Swagger-документацию
