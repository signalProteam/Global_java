    # gs-java

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/gs-java-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - Oracle ([guide](https://quarkus.io/guides/datasource)): Connect to the Oracle database via JDBC
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

# API de Apoio a Desastres

![Java](https://img.shields.io/badge/Java-17-blue)
![Quarkus](https://img.shields.io/badge/Quarkus-3.x-blueviolet)
![Database](https://img.shields.io/badge/Database-Oracle_SQL-red)
![License](https://img.shields.io/badge/License-MIT-green)

Este projeto é uma API RESTful desenvolvida como parte de um trabalho de faculdade (FIAP), com o objetivo de fornecer um backend para uma aplicação de apoio a pessoas em situações de pós-desastre natural no Brasil, com um foco inicial em enchentes.

A API permite o cadastro de usuários e o registro de incidentes (pedidos de ajuda) a partir de um CEP, que é enriquecido com dados de geolocalização para facilitar a ação de equipes de resgate. Conta também com uma interface de chatbot para apoio empático.

## ✨ Funcionalidades Principais

- **Gestão de Usuários:** Cadastro e login de usuários no sistema.
- **Criação de Incidentes (Pedidos de Ajuda):** Permite registrar um pedido de ajuda de forma simples, utilizando apenas o CEP.
- **Enriquecimento de Dados:** Consulta automática de uma API externa para obter o endereço e geolocalização a partir do CEP.
- **Gestão de Incidentes:** Permite a listagem, busca e resolução de incidentes registrados.
- **Chatbot Empático:** Endpoint para interação com um chatbot de apoio emocional.
- **Documentação da API:** Geração automática da documentação com OpenAPI (Swagger UI).

## 🛠️ Tecnologias Utilizadas

- **Framework:** [Quarkus](https://quarkus.io/)
- **Linguagem:** Java 17
- **Persistência:** JDBC (Java Database Connectivity)
- **Banco de Dados:** Oracle SQL
- **Documentação:** OpenAPI (via anotações)
- **APIs Externas:** [BrasilAPI](https://brasilapi.com.br/) para consulta de CEP.
- **Build Tool:** Maven

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura em camadas para promover a organização e separação de responsabilidades:

```
src/main/java/fiap/tds/
├── clients/      # Clientes para APIs externas (BrasilAPI, etc.)
├── dtos/         # Data Transfer Objects (DTOs) para a API
├── exceptions/   # Exceções customizadas e Exception Mappers
├── models/       # POJOs que representam os dados (ex: HelpRequest)
├── repositories/ # DAOs (Data Access Objects) com a lógica JDBC
├── resources/    # Endpoints REST (JAX-RS)
└── services/     # Camada com a lógica de negócio
```

## 🚀 Documentação Interativa (Swagger UI)

Este projeto utiliza a extensão `quarkus-smallrye-openapi` para gerar uma documentação interativa da API. Após iniciar a aplicação, você pode acessar e testar todos os endpoints através do seu navegador no seguinte endereço:

- **URL:** [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)

## 📡 API Endpoints

Aqui estão os principais endpoints disponíveis na API, agrupados por funcionalidade.

---

### Gestão de Usuários

#### 1. Cadastrar Novo Usuário

- **Método:** `POST`
- **Endpoint:** `/usuarios`
- **Descrição:** Cadastra um novo usuário no sistema.
- **Corpo da Requisição (JSON):**
  ```json
  {
    "username": "novo_usuario",
    "password": "uma_senha_forte"
  }
  ```
- **Resposta de Sucesso (201 CREATED):**
  ```json
  "Usuário cadastrado com sucesso!"
  ```

#### 2. Realizar Login

- **Método:** `POST`
- **Endpoint:** `/login`
- **Descrição:** Autentica um usuário com base em suas credenciais.
- **Corpo da Requisição (JSON):**
  ```json
  {
    "username": "novo_usuario",
    "password": "uma_senha_forte"
  }
  ```
- **Resposta de Sucesso (200 OK):**
  ```json
  "Login realizado com sucesso!"
  ```
- **Resposta de Erro (401 UNAUTHORIZED):** Ocorre se as credenciais forem inválidas.

---

### Gestão de Incidentes (Pedidos de Ajuda)

#### 1. Criar um Incidente

- **Método:** `POST`
- **Endpoint:** `/solicitar-ajuda`
- **Descrição:** Cria um novo pedido de ajuda baseado em um CEP.
- **Corpo da Requisição (JSON):**
  ```json
  {
    "cep": "01311-000",
    "notes": "Preciso de água e abrigo para 2 adultos.",
    "contactInfo": "Maria - (11) 98765-4321"
  }
  ```
- **Resposta de Sucesso (201 CREATED):** Retorna o objeto completo do incidente criado.

#### 2. Listar Todos os Incidentes

- **Método:** `GET`
- **Endpoint:** `/incidentes`
- **Descrição:** Retorna uma lista de todos os incidentes registrados.
- **Resposta de Sucesso (200 OK):** Retorna um array de `HelpRequestResponseDTO`.
  ```json
  [
    {
      "id": 1,
      "cep": "01311000",
      "notes": "Preciso de água e abrigo para 2 adultos.",
      "contactInfo": "Maria - (11) 98765-4321",
      "latitude": -23.5613,
      "longitude": -46.6565,
      "status": "PENDENTE"
    }
  ]
  ```

#### 3. Buscar um Incidente por ID

- **Método:** `GET`
- **Endpoint:** `/incidentes/{id}`
- **Descrição:** Recupera os detalhes de um incidente específico.
- **Resposta de Sucesso (200 OK):** Retorna o objeto completo do incidente.

#### 4. Resolver um Incidente

- **Método:** `PUT`
- **Endpoint:** `/incidentes/{id}`
- **Descrição:** Atualiza o status de um incidente para "resolvido" (ou lógica similar).
- **Resposta de Sucesso (200 OK):**
  ```json
  "Incidente resolvido com sucesso!"
  ```

---

### Chatbot

#### 1. Interagir com o Chatbot

- **Método:** `POST`
- **Endpoint:** `/chat`
- **Descrição:** Envia uma mensagem do usuário para o chatbot e recebe uma resposta de apoio.
- **Corpo da Requisição (JSON):**
  ```json
  {
    "message": "Estou me sentindo muito ansioso com tudo isso."
  }
  ```
- **Resposta de Sucesso (201 CREATED):**
  ```json
  {
    "reply": "Entendo perfeitamente que você se sinta assim. É uma situação muito difícil, mas lembre-se de que você não está sozinho. Respire fundo. Estamos aqui para ajudar."
  }
  ```

---

## ⚙️ Configuração e Instalação

### Pré-requisitos

- Java JDK 17 (ou superior)
- Apache Maven 3.8 (ou superior)
- Acesso a um banco de dados Oracle

### Configuração

1. Clone o repositório.
2. Crie o esquema de tabelas no seu banco de dados Oracle.
3. Configure o arquivo `src/main/resources/application.properties` com suas credenciais:

   ```properties
   # Oracle Datasource
   quarkus.datasource.db-kind=oracle
   quarkus.datasource.username=SEU_USUARIO
   quarkus.datasource.password=SUA_SENHA
   quarkus.datasource.jdbc.url=jdbc:oracle:thin:@//localhost:1521/SEU_SERVICE_NAME

   # URL da API de CEP
   brasilapi.cep.v2.url=[https://brasilapi.com.br/api/cep/v2](https://brasilapi.com.br/api/cep/v2)
   quarkus.rest-client."fiap.tds.clients.BrasilCepClient".url=${brasilapi.cep.v2.url}
   ```

## ▶️ Executando o Projeto

Para rodar a aplicação em modo de desenvolvimento (com hot reload):

```bash
./mvnw quarkus:dev
```

A API estará disponível em `http://localhost:8080`.

Para compilar e empacotar a aplicação:

```bash
./mvnw clean package
```

## 👨‍💻 Autor

- [Lucas José Lima](https://github.com/Lucasjlima)
