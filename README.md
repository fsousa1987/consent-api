# 🛡️ Consent API

API REST para gestão de consentimentos de usuários, desenvolvida como parte de um desafio técnico.  
Permite criar, listar, atualizar e revogar consentimentos, com histórico de alterações e integração externa.

---

## ⚙️ Pré-requisitos

- Java 21
- Docker e Docker Compose
- (Opcional) MongoDB local para rodar sem Docker
- Git

---

## 🚀 Tecnologias
<p align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java" width="50"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" alt="Spring Boot" width="50"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mongodb/mongodb-original.svg" alt="MongoDB" width="50"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" alt="Docker" width="50"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" alt="GitHub" width="50"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" alt="Maven" width="50"/>
</p>

- Java 21 
- Spring Boot 3.4
- MongoDB
- Testcontainers (para testes de integração)
- JUnit 5 + Mockito
- Swagger/OpenAPI
- Docker + Docker Compose
- Prometheus (métricas)
- MapStruct + Lombok
- GitHub Actions (CI/CD)

---

## 📦 Como rodar o projeto

### 💻 Localmente com Mongo via Docker

```bash
# subir o MongoDB localmente
docker compose up mongo
```

```bash
# rodar a aplicação na IDE ou via terminal
./mvnw spring-boot:run
```

### 🐳 Tudo com Docker Compose (app + banco)

```bash
docker compose up --build
```

A aplicação será acessível em:

```
http://localhost:8099
```

---

## 🧪 Testes

### 🔬 Rodar testes unitários e de integração:

```bash
./mvnw clean verify
```

Os testes de integração usam **Testcontainers**, sem necessidade de banco local.

---

## 🔄 Build do projeto

```bash
./mvnw clean package
```

---

## ✅ CI com GitHub Actions

O projeto possui integração contínua (CI) configurada via **GitHub Actions**.  
A cada `push` ou `pull request` na branch `main`, os testes são executados automaticamente.  
Veja em: [Aba Actions](https://github.com/fsousa1987/consent-api/actions)

---

## 📂 Endpoints disponíveis

- `POST /consents` – Criar consentimento
- `GET /consents` – Listar todos
- `GET /consents/{id}` – Buscar por ID
- `PUT /consents/{id}` – Atualizar consentimento
- `DELETE /consents/{id}` – Revogar/excluir
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

## 🔍 Acesso às interfaces web

Durante a execução via Docker Compose, as seguintes interfaces estarão disponíveis:

- 🔧 **Swagger/OpenAPI**: documentação da API\
  [http://localhost:8099/swagger-ui.html](http://localhost:8099/swagger-ui.html)

- 📈 **Prometheus UI**: visualização e consulta de métricas\
  [http://localhost:9090](http://localhost:9090)

- 📊 **Mongo Express**: interface web para visualizar dados da base MongoDB\
  [http://localhost:8081](http://localhost:8081)\
  **Login:** `admin` / `admin123`

---

## 🧾 Histórico de alterações

A cada `PUT` ou `DELETE`, um registro de rastreabilidade é criado na coleção `consent_history`, contendo:

- ID do consentimento
- CPF
- Status anterior
- Tipo de operação (UPDATED ou REVOKED)
- Timestamp

---

## 🌐 Integração externa (GitHub)

Quando o campo `additionalInfo` não for informado, é feita uma chamada para:

```
https://api.github.com/users/{username}
```

O campo `bio` do usuário é utilizado como valor padrão.

---

## 🧼 Qualidade e boas práticas

- ✅ Código limpo e organizado
- ✅ Arquitetura baseada em princípios da Clean Architecture
- ✅ Separação clara de responsabilidades (controller, service, domain, repository, config, etc.)
- ✅ Cobertura de testes com JUnit 5, Mockito e Testcontainers
- ✅ Commits semânticos
- ✅ Documentação automatizada com Swagger
- ✅ Pipeline CI no GitHub Actions

---

## 🧑‍💻 Autor

Francisco Edglei de Sousa  
[GitHub: @fsousa1987](https://github.com/fsousa1987)
