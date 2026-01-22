# 🔐 Auth API – Spring Boot + JWT + PostgreSQL

API de autenticação e gerenciamento de usuários construída com **Spring Boot**, seguindo padrões **REST**, **JWT**, **RFC 7807 (Problem Details)** e boas práticas de arquitetura utilizadas em ambientes corporativos.

---

## 🚀 Visão Geral

Esta API fornece:

- Autenticação JWT (Access Token + Refresh Token)
- Cadastro e consulta de usuários
- Controle de acesso por **roles (USER / ADMIN)**
- Padronização de erros com **Problem Details (RFC 7807)**
- Documentação automática com **OpenAPI / Swagger**
- Persistência em **PostgreSQL**
- Testes de integração com **MockMvc + Testcontainers**

Projeto ideal para:
- Portfólio profissional
- Base de Auth Server
- Evolução para OAuth2 / SSO

---

## 🧱 Stack Tecnológica

- **Java 21**
- **Spring Boot 3.x**
- Spring Security
- JWT (Access + Refresh)
- Spring Data JPA
- PostgreSQL
- Bean Validation
- MapStruct
- OpenAPI / Swagger
- Testcontainers
- MockMvc

---

## 🔐 Segurança

- Autenticação stateless via JWT
- Access Token curto + Refresh Token
- Controle de acesso por role
- Endpoints protegidos com `@PreAuthorize`
- Filtro JWT customizado

### Roles

| Role | Descrição |
|-----|----------|
| USER | Usuário padrão |
| ADMIN | Acesso administrativo |

---

## 🔁 Fluxo de Autenticação

```text
[Login] → Access Token + Refresh Token
   ↓
[Requisições protegidas]
   ↓
[Access expirou]
   ↓
[POST /auth/refresh]
```

---

## 📌 Endpoints Principais

### 🔓 Públicos

| Método | Endpoint | Descrição |
|------|---------|----------|
| POST | /auth/login | Autenticação |
| POST | /auth/refresh | Renovar access token |
| POST | /users | Criar usuário |

### 🔐 Protegidos

| Método | Endpoint | Role |
|------|---------|------|
| GET | /users/me | USER |
| GET | /users/{id} | ADMIN |

---

## 🧪 Testes

A aplicação utiliza **testes de unitários reais**:

---

## 📄 Padrão de Erros – RFC 7807

Todos os erros seguem o formato **Problem Details**:

```json
{
  "type": "https://api.auth.com/errors/validation",
  "title": "Validation error",
  "status": 400,
  "detail": "Erro de validação nos campos da requisição",
  "instance": "/users",
  "timestamp": "2026-01-22T12:00:00Z"
}
```

---

## 📚 Documentação (Swagger)

- Swagger UI disponível em:

```
http://localhost:8080/swagger-ui.html
```

- OpenAPI documentado com:
  - Headers (Location)
  - Security Schemes
  - Responses detalhadas
  - Roles explícitas

---

## ⚙️ Como Executar

### Pré-requisitos

- Java 21+
- Docker (para PostgreSQL)

### Subir aplicação

```bash
./mvnw spring-boot:run
```
---

## 👨‍💻 Autor

Projeto desenvolvido com foco em **arquitetura, segurança e boas práticas corporativas**.

---
