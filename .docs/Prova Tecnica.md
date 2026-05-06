Prova Técnica – Java 21, Spring Boot, MongoDB e Redis

📌 Objetivo

Avaliar a capacidade do candidato de construir uma API REST com CRUD completo utilizando boas práticas de engenharia de
software, arquitetura limpa, Java 21 e o ecossistema Spring Boot com MongoDB e Redis.

O prazo de entrega é de uma semana, se precisar de mais tempo, sinaliza pra gente.

🛠️ Stack Obrigatória

Tecnologia Versão recomendada

Java 21

Spring Boot 3.x (última estável)

Spring Data MongoDB via Spring Boot

Spring Data Redis via Spring Boot

Maven 3.9+

Lombok via Spring Boot

ModelMapper 3.x

SpringDoc OpenAPI 2.x

JUnit 5 via Spring Boot

Mockito via Spring Boot

Testcontainers última estável

📦 Contexto do Desafio

Você foi contratado para desenvolver o microsserviço de gerenciamento de uma Biblioteca de Livros.

O sistema deve permitir o cadastro, consulta, atualização e remoção de livros, com suporte a cache Redis para otimização
de leitura e persistência no MongoDB.

🗂️ Domínio

Entidade: Livro

Campo Tipo Obrigatório Descrição

id String - Gerado automaticamente pelo MongoDB

titulo String ✅ Título do livro

autor String ✅ Nome do autor

isbn String ✅ Código ISBN único do livro

anoPublicacao Integer ✅ Ano de publicação (entre 1000 e atual)

genero String ✅ Gênero literário (valor de enum)

disponivel Boolean ✅ Indica se o livro está disponível

dataInclusao LocalDateTime - Preenchida automaticamente na criação

dataAtualizacao LocalDateTime - Preenchida automaticamente na atualização

Enum: GeneroPojo

FICCAO_CIENTIFICA,

FANTASIA,

ROMANCE,

TERROR,

BIOGRAFIA,

HISTORIA,

TECNOLOGIA,

INFANTIL

🔌 Endpoints Exigidos

POST /livros

Cria um novo livro

Retorna 201 Created com o livro criado

Validações:

titulo, autor, isbn, genero não podem ser nulos ou vazios

anoPublicacao deve ser maior que 1000 e menor ou igual ao ano atual

isbn deve ser único no banco

genero deve ser um valor válido do enum GeneroPojo

GET /livros/{id}

Busca um livro por ID

Retorna 200 OK com os dados do livro

Deve utilizar cache Redis com TTL de 10 minutos

Lança exceção de negócio se não encontrado

GET /livros

Lista todos os livros com paginação obrigatória

Parâmetros de query:

pagina (default: 0)

tamanho (default: 10)

genero (opcional, filtro por gênero)

Retorna 200 OK com lista paginada

PUT /livros/{id}

Atualiza um livro existente

As mesmas validações da criação se aplicam

Ao atualizar, deve invalidar o cache do livro no Redis

Retorna 200 OK com o livro atualizado

DELETE /livros/{id}

Remove um livro por ID

Deve invalidar o cache do livro no Redis após exclusão

Retorna 204 No Content

🏗️ Arquitetura Exigida

Seguir o padrão Clean Architecture simplificado:

Controller → Service → Repository → MongoDB

↕

Redis (Cache)

Regras de Camadas:

Controller: apenas orquestra requisições HTTP, sem regras de negócio

Service: contém todas as regras de negócio, validações e integração com cache

Repository: apenas acesso ao MongoDB via Spring Data

DTOs: usar record do Java 21 para entrada e saída de dados onde fizer sentido

Entidade: anotada com @Document do Spring Data MongoDB

💾 Cache Redis

Usar Spring Cache com Redis como provider

Chave de cache padrão: biblioteca:livro:{id}

TTL: 10 minutos

Cache deve ser invalidado nas operações de atualização e exclusão

Nunca armazenar dados críticos apenas no cache

✅ Validações

Validar os dados de entrada no DTO (usando jakarta.validation ou validação manual no Service)

Lançar exceções de negócio com mensagens descritivas usando uma exceção customizada: NegocioException

Tratar erros globalmente via @RestControllerAdvice

Exemplo de retorno de erro padronizado:

{

"codigo": "LIVRO_NAO_ENCONTRADO",

"mensagem": "Livro com id '123' não encontrado.",

"timestamp": "2025-04-27T14:30:00"

}

📝 Documentação

Documentar todos os endpoints com SpringDoc OpenAPI (@Operation, @ApiResponse, @Tag)

Swagger UI deve estar acessível em: http://localhost:8080/swagger-ui.html

🧪 Testes

Obrigatórios:

Testes de unidade para a camada Service:

Cenários de sucesso

Cenários de erro (livro não encontrado, ISBN duplicado, etc.)

Mockar repositórios com Mockito

Testes de integração para a camada Controller:

Usar Testcontainers para MongoDB e Redis reais

Testar os endpoints POST, GET, PUT, DELETE

Testar cenários de erro (400, 404)

Cobertura mínima esperada: 80%

esse foi o que a gente mandou?

ele disse que mandou outro porque tava desformatado kkk

o que tu botou no grupo foi o que a gente enviou
Olá, tudo bem?!
Segue nosso desafio técnico para a vaga.
Prova Técnica – Java 21, Spring Boot, MongoDB e Redis
📌 Objetivo Avaliar a capacidade do candidato de construir uma API REST com CRUD completo utilizando boas práticas de
engenharia de software, arquitetura limpa, Java 21 e o ecossistema Spring Boot com MongoDB e Redis.
🛠️ Stack Obrigatória Tecnologia Versão recomendada Java 21 Spring Boot 3.x (última estável) Spring Data MongoDB via
Spring Boot Spring Data Redis via Spring Boot Maven 3.9+ Lombok via Spring Boot ModelMapper 3.x SpringDoc OpenAPI 2.x
JUnit 5 via Spring Boot Mockito via Spring Boot Testcontainers última estável

📦 Contexto do Desafio Você foi contratado para desenvolver o microsserviço de gerenciamento de uma Biblioteca de Livros.
O sistema deve permitir o cadastro, consulta, atualização e remoção de livros, com suporte a cache Redis para otimização
de leitura e persistência no MongoDB.

🗂️ Domínio Entidade: Livro Campo Tipo Obrigatório Descrição id String - Gerado automaticamente pelo MongoDB titulo
String
✅ Título do livro autor String
✅ Nome do autor isbn String
✅ Código ISBN único do livro anoPublicacao Integer
✅ Ano de publicação (entre 1000 e atual) genero String
✅ Gênero literário (valor de enum) disponivel Boolean
✅ Indica se o livro está disponível dataInclusao LocalDateTime - Preenchida automaticamente na criação dataAtualizacao
LocalDateTime - Preenchida automaticamente na atualização Enum: GeneroPojo FICCAO_CIENTIFICA, FANTASIA, ROMANCE, TERROR,
BIOGRAFIA, HISTORIA, TECNOLOGIA, INFANTIL

🔌 Endpoints Exigidos POST /livros Cria um novo livro Retorna 201 Created com o livro criado Validações: titulo, autor,
isbn, genero não podem ser nulos ou vazios anoPublicacao deve ser maior que 1000 e menor ou igual ao ano atual isbn deve
ser único no banco genero deve ser um valor válido do enum GeneroPojo GET /livros/{id} Busca um livro por ID Retorna 200
OK com os dados do livro Deve utilizar cache Redis com TTL de 10 minutos Lança exceção de negócio se não encontrado GET
/livros Lista todos os livros com paginação obrigatória Parâmetros de query: pagina (default: 0) tamanho (default: 10)
genero (opcional, filtro por gênero) Retorna 200 OK com lista paginada PUT /livros/{id} Atualiza um livro existente As
mesmas validações da criação se aplicam Ao atualizar, deve invalidar o cache do livro no Redis Retorna 200 OK com o
livro atualizado DELETE /livros/{id} Remove um livro por ID Deve invalidar o cache do livro no Redis após exclusão
Retorna 204 No Content
🏗️ Arquitetura Exigida Seguir o padrão Clean Architecture simplificado: Controller → Service → Repository → MongoDB ↕
Redis (Cache) Regras de Camadas: Controller: apenas orquestra requisições HTTP, sem regras de negócio Service: contém
todas as regras de negócio, validações e integração com cache Repository: apenas acesso ao MongoDB via Spring Data DTOs:
usar record do Java 21 para entrada e saída de dados onde fizer sentido Entidade: anotada com @Document do Spring Data
MongoDB 💾 Cache Redis Usar Spring Cache com Redis como provider Chave de cache padrão: biblioteca:livro:{id} TTL: 10
minutos Cache deve ser invalidado nas operações de atualização e exclusão Nunca armazenar dados críticos apenas no cache

✅ Validações Validar os dados de entrada no DTO (usando jakarta.validation ou validação manual no Service) Lançar
exceções de negócio com mensagens descritivas usando uma exceção customizada: NegocioException Tratar erros globalmente
via @RestControllerAdvice Exemplo de retorno de erro padronizado: { "codigo": "LIVRO_NAO_ENCONTRADO", "mensagem": "Livro
com id '123' não encontrado.", "timestamp": "2025-04-27T14:30:00" }

📝 Documentação Documentar todos os endpoints com SpringDoc OpenAPI (@Operation, @ApiResponse, @Tag) Swagger UI deve
estar acessível em: http://localhost:8080/swagger-ui.html

🧪 Testes Obrigatórios: Testes de unidade para a camada Service: Cenários de sucesso Cenários de erro (livro não
encontrado, ISBN duplicado, etc.) Mockar repositórios com Mockito Testes de integração para a camada Controller: Usar
Testcontainers para MongoDB e Redis reais Testar os endpoints POST, GET, PUT, DELETE Testar cenários de erro (400, 404)
Cobertura mínima esperada: 80%