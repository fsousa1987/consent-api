```markdown
# 🛡️ Consent API

API REST para gestão de consentimentos de usuários, desenvolvida como parte de um desafio técnico.  
Permite criar, listar, atualizar e revogar consentimentos, com histórico de alterações e integração externa.

---

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.4
- MongoDB
- Testcontainers
- JUnit 5 + Mockito
- Swagger/OpenAPI
- Docker + Docker Compose
- Prometheus (métricas)
- MapStruct + Lombok

---

## 📦 Como rodar o projeto

### 🧪 Executar localmente (com Mongo já instalado)

```bash
./mvnw spring-boot:run
```

### 🐳 Executar com Docker Compose

```bash
docker-compose up -d
```

---

## 🧪 Testes

```bash
./mvnw test
```

Testes de integração usam **Testcontainers**, então nenhum Mongo local é necessário.

---

## 📂 Endpoints disponíveis

- `POST /consents` – Criar consentimento
- `GET /consents` – Listar todos
- `GET /consents/{id}` – Buscar por ID
- `PUT /consents/{id}` – Atualizar consentimento
- `DELETE /consents/{id}` – Revogar/excluir consentimento
- `GET /consents/paged?page=0&size=10` – Lista paginada

---

## 📄 Exemplo de requisição (POST)

```json
POST /consents
Content-Type: application/json

{
  "cpf": "123.456.789-00",
  "status": "ACTIVE",
  "expirationDateTime": "2026-12-31T23:59:59",
  "additionalInfo": "Termo de aceite digital"
}
```

---

## 📚 Swagger

Acesse:

```
http://localhost:8099/swagger-ui.html
```

---

## 📈 Métricas Prometheus

Ativo via Spring Boot Actuator:

```
GET /actuator/prometheus
```

---

## 🔁 Histórico de alterações

A cada `PUT` ou `DELETE`, é criado um documento de rastreabilidade na coleção `consent_history`.

---

## 🌐 Integração externa

Quando o campo `additionalInfo` não for informado, é feita uma chamada para:

```
https://api.github.com/users/{username}
```

O campo `bio` é utilizado como valor padrão.

---

## ✅ Feito com foco em:

- Código limpo e organizado
- Separação de responsabilidades
- Boas práticas REST
- Cobertura de testes
- Documentação e métricas
- Commits semânticos

---

## 🧑‍💻 Autor

Francisco Edglei de Sousa  
[GitHub: @fsousa1987](https://github.com/fsousa1987)