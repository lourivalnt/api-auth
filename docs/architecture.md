## Arquitetura

- Spring Boot 3
- PostgreSQL
- Stateless JWT Authentication
- Access + Refresh Token
- Camadas:
  - Controller
  - Service
  - Repository
  - Security
  - Exception

Fluxo:
Client → Controller → Service → Repository → DB