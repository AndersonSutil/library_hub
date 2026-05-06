# Library Hub API

Uma API RESTful para gerenciamento de acervo de biblioteca, desenvolvida com o objetivo de demonstrar boas práticas de
engenharia de software, utilizando **Java 21**, **Spring Boot 3.4.13**, **MongoDB** e **Redis**.

O projeto foi construído seguindo rigorosamente os princípios de **Clean Architecture (Arquitetura Hexagonal)**, **DDD (
Domain-Driven Design)** e **TDD (Test-Driven Development)**, assegurando alta manutenibilidade, testabilidade e
desacoplamento.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21 *(Nota: o projeto utiliza funcionalidades modernas como Records e possui compatibilidade total garantida para Java 25)*
- **Framework Principal:** Spring Boot 3.4.13
- **Banco de Dados:** MongoDB (Persistência Principal)
- **Cache de Dados:** Redis via Spring Cache
- **Testes Unitários:** JUnit 5 e Mockito
- **Testes de Integração:** Testcontainers (MongoDB e Redis)
- **Documentação da API:** SpringDoc OpenAPI (Swagger UI)
- **Métricas de Qualidade:** JaCoCo (Cobertura de Testes exigida > 80%)
- **Build Tool:** Maven Wrapper (`mvnw`)

---

## 🏛️ Visão de Arquitetura

O projeto adota o padrão **Ports and Adapters (Arquitetura Hexagonal)** visando o desacoplamento das regras de negócio
em relação aos frameworks e infraestrutura externa.

A estrutura de pacotes é organizada da seguinte forma:

```text
src/main/java/com/sicredi/libraryhub/
├── domain/            # Core da aplicação. Entidades, Enums e regras de negócio sem dependências externas.
├── application/       # Casos de uso e orquestração. DTOs, Serviços (Ports de entrada), Exceções e Mapeamentos.
├── infrastructure/    # Adapters de saída. Implementações de Repositórios, Configurações do Spring (MongoDB, Redis, OpenAPI).
└── presentation/      # Adapters de entrada. Controllers REST (Endpoints da API) e Handlers Globais (GlobalExceptionHandler).
```

### Princípios Aplicados

1. **SOLID:** Alto nível de coesão, Injeção de dependências e segregação de interfaces.
2. **DDD (Domain-Driven Design):** As regras pertencem ao domínio; exceções e validações são tratadas contextualmente (
   `NegocioException`).
3. **Fail-Fast:** A API falha rapidamente e informa de forma legível erros de formato e violações de negócio (ex: *ISBN
   duplicado*).

---

## 🧪 Como Executar e Testar

### Pré-requisitos

Para rodar a aplicação localmente de forma integral, você precisará de:

- **Java 21** ou superior instalado (configurado no `JAVA_HOME`).
- **MongoDB** rodando na porta padrão (`27017`).
- **Redis** rodando na porta padrão (`6379`).
- **Docker** em execução (necessário apenas para os Testes de Integração com *Testcontainers*).

### 1. Rodando os Testes e Gerando o Relatório de Cobertura

O projeto usa JaCoCo para validar que a cobertura é maior do que 80%. O comando `verify` executará tanto os testes
unitários (`Mockito`) quanto os testes de integração (`Testcontainers`).

```bash
# Executa a limpeza, testes e gera o jar
./mvnw clean verify
```

Após o sucesso do build, abra o arquivo `target/site/jacoco/index.html` em seu navegador para visualizar o relatório
completo de cobertura de código.

*(Nota: Se o Docker não estiver rodando na sua máquina local, os testes de integração irão falhar. Você pode pular a
etapa de integração adicionando `-DskipITs`)*.

### 2. Rodando a Aplicação Localmente (Via Docker Compose)

Para facilitar a execução e evitar a necessidade de instalar e configurar o MongoDB e o Redis manualmente, o projeto já
vem configurado com **Docker Compose**. Ele irá subir os bancos de dados e a própria aplicação de uma vez só!

Na raiz do projeto (onde está o arquivo `docker-compose.yml`), execute:

```bash
docker compose up -d --build
```

Esse comando irá:

1. Baixar as imagens do MongoDB e Redis.
2. Compilar a aplicação Java via Dockerfile (Multi-stage build).
3. Subir todos os containers na mesma rede.

A API estará disponível em `http://localhost:8080`.

*(Caso queira rodar apenas as dependências de banco de dados e subir o Java pela sua IDE/Terminal, basta
executar `docker compose up -d mongo redis` e depois `./mvnw spring-boot:run`)*.

### 3. Testando via Swagger (OpenAPI)

Com a aplicação em execução, acesse a interface visual do Swagger para validar e testar os endpoints através do seu
navegador:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### 4. Testando via Postman

Na raiz do projeto, encontra-se o arquivo `Library_Hub_Postman_Collection.json`.

1. Abra o Postman.
2. Clique em **Import**.
3. Selecione o arquivo e faça o teste das requisições (POST, GET, PUT, PATCH, DELETE) diretamente na collection configurada.

---

## 📌 Principais Funcionalidades (Endpoints)

- `POST /livros` - Cadastra um novo livro na biblioteca (com validação de ISBN único).
- `GET /livros` - Lista os livros com paginação (`?pagina=0&tamanho=10`) suportado por cacheamento no Redis.
- `GET /livros/{id}` - Busca os detalhes de um único livro (utiliza cache Redis).
- `PUT /livros/{id}` - Atualiza um livro existente (substituição total).
- `PATCH /livros/{id}` - Atualização parcial de um livro (ex: apenas o título ou disponibilidade).
- `DELETE /livros/{id}` - Remove um livro do catálogo de forma definitiva.

---

## ✅ Considerações Finais
Este projeto é uma demonstração técnica de arquitetura e consistência para um sistema de gerenciamento de biblioteca. Em um ambiente produtivo de larga escala, as seguintes evoluções seriam aplicadas:

- **Utilização de Java 25**: Em novos serviços, a adoção do Java 25 permitiria explorar o máximo de performance de **Virtual Threads (Project Loom)** para lidar com milhares de requisições simultâneas de busca no acervo e **Scoped Values**, otimizando o uso de recursos.
- **Mensageria Distribuída (Kafka)**: Substituiríamos chamadas síncronas por Apache Kafka para gerenciar eventos de domínio, como notificações de novos livros, alertas de devolução ou integração com sistemas de histórico e recomendação, garantindo uma arquitetura resiliente e orientada a eventos.
- **Lock Distribuído (Redis/Redlock)**: Em cenários de alta concorrência, o uso do Redis para Distributed Locking garantiria a consistência em operações críticas, como a reserva de um exemplar único, impedindo que dois usuários reservem o mesmo livro no exato milissegundo.
- **Ecossistema Spring Cloud**: Utilizaríamos o Spring Cloud Config e Gateway para gerenciar a topologia dos serviços e resiliência (Circuit Breakers) em uma arquitetura de microsserviços.
- **Observabilidade 360°**:
    - **Logs e Traces**: Integração com ELK Stack ou CloudWatch, utilizando OpenTelemetry para rastreabilidade de ponta a ponta (Distributed Tracing).
    - **Métricas**: Exposição de métricas via Prometheus (ex: tempo médio de busca, taxa de sucesso de persistência) e visualização no Grafana.
    - **Alertas**: Configuração de alertas para monitorar latência no Redis, saúde do MongoDB e picos de demanda no acervo.
