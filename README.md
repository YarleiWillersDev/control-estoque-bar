# API Gerenciamento de Estoque Bar 💻


<p> <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"> </a> <a href="#"> <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"> </a> <a href="#"> <img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"> </a> <a href="#"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> </a> <a href="#"> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"> </a> </p>

<p style="text-align: justify;">
    Este é um projeto backend que foi desenvolvido buscando resolver um problema encontrado no cotidiano do bar do meu avô, o gerenciamento do estoque dos produtos. Sendo assim, usei meus conhecimentos em desenvolvimento de APIs para criar um gerenciador de estoque para o bar do meu avô e acabar com o problema que o mesmo vinha enfrentando.
</p>

<p style="text-align: justify;">
    A API gerencia o estoque dos produtos comercializados no bar buscando um melhor controle e entrada e saída dos produtos. Sendo assim, é possível realizar a consulta de produto, categorias, movimentções e usuários, e também a criação dos mesmos citados anteriormente.
</p>

<p style="text-align: justify;">
    A aplicação foi desenvolvida utilizando Java 21 junto do Spring Boot 3.5.8 em um projeto Maven. O banco de dados utilizado foi o Oracle FREE, junto do JPA para efetuar os comandos SQL da aplicação. Também é importante ressaltar que o versionamento do banco de dados é gerenciado pelo Flyway através de migrations, e a API foi totalmente documentada a partir do swagger. A aplicação também foi empacotada em um container docker onde sendo inicializado, já sobe o banco de dados juntamente da aplicação.
</p>

<br>

## 🚀 Como Rodar o Projeto
1 - Copie `.env.example` para `.env` e preencha as variáveis:

```
APP_USER=
APP_USER_PASSWORD=
ORACLE_PASSWORD=
```

2 - Suba os containers:

```
docker compose up -d --build
```

3 - Veja os logs:

```
docker compose log -f api
```

4 - Acesse a API:

```
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html
```
---
<br>

## 📦 Estrutura do Projeto

Segue abaixo a estrutura de pacotes contidos no meu projeto:

```
Projeto
├── src/
|
│ ├── main/
| | |
│ │ ├── java/... # Código fonte (controllers, services, entities, DTO, Repositories)
| | |
│ │ └── resources/
| | |
│ │ ├── db/migration/ # Migrations e seeds do Flyway
| | |
│ │ └── application.properties # Configuração do Spring
|
├── dockerfile # Construção da imagem da API
|
├── docker-compose.yml # Orquestração dos containers
|
├── .env.example # Exemplo de variáveis de ambiente
|
├── README.md # Esta documentação
|
└── pom.xml # Dependências Maven
```
---
<br>

## 📚 Endpoints API

A API disponibiliza os seguintes endpoints:

Categoria Endpoints
```
POST    /categoria                 - Cria uma nova categoria
PUT     /categoria/{id}            - Atualiza as informações de uma categoria
GET     /categoria/{id}            - Busca categoria pelo ID
GET     /categoria/buscar          - Busca categoria pelo nome
GET     /categoria                 - Lista todas as categoria cadastradas
```

Movimentação de Estoque Endpoints

```
POST    /movimentacoes             - Cria uma nova movimentação
PATCH   /movimentacoes/{id}        - Atualiza a quantidade de uma movimentação
GET     /movimentacoes/{id}        - Busca movimentação pelo ID
GET     /movimentacoes             - Lista todas as movimentacoes cadastradas
GET     /movimentacoes/produtoId   - Busca movimentação pelo ID do Produto
```

Produto Endpoints

```
POST    /produtos                  - Cria um novo produto
PUT     /produtos/{id}             - Atualiza as informações de um produto
PATCH   /produtos/{id}/status      - Atualiza o status de um produto
GET     /produtos/{id}             - Busca produto pelo ID
GET     /produtos/buscar           - Busca produto pelo nome
GET     /produtos/categoriaId      - Buscar produto pela categoria
GET     /produtos                  - Lista todos os produtos cadastrados
```

Usuario Endpoints

```
POST    /usuarios                  - Cria um novo usuário
PUT     /usuarios/{id}             - Atualiza as informações de um usuário
DELETE  /usuarios/{id}             - Deleta um usuário do sistema
GET     /usuarios/buscar           - Busca um usuário pelo nome
GET     /usuarios                  - Lista todos os usuários da API
```

Todos os endpoints de criação ou atualização de produto, usuário e categoria necessitam de role de gerente para efetuar a operações, o restante, possui permissão para role de vendedor e para pessoa autenticadas que não possuem role.

```
ROLES

ROLE_GERENTE -> Permite efetuar qualquer alteração no sistema
ROLE_VENDEDOR -> Permite apenas consultas, registros e atualização de movimentações
```
---
<br>

## 🔒 Segurança

A API implementa autenticação e autorização seguindo boas práticas do ecossistema Spring, protegendo endpoints sensíveis e garantindo o uso seguro em produção.

Autenticação via JWT

- O login é feito pelo endpoint de autenticação.

- Após login, um token JWT é retornado; ele deve ser usado no header Authorization: Bearer SEU_TOKEN para acessar rotas protegidas.

- Exemplo:

        Authorization: Bearer SEU_TOKEN_JWT

Controle de acesso

- Endpoints de produtos, categorias, movimentações e usuários são protegidos (autenticados).

- Permissões diferenciadas podem ser configuradas por role caso o sistema utilize múltiplos papéis (ex: ADMIN, USUARIO).

Senhas e credenciais

- Senhas de usuários são armazenadas com hash (ex: BCrypt) e nunca em texto plano.

- Usuários/credenciais sensíveis (APP_USER, APP_USER_PASSWORD, ORACLE_PASSWORD) não devem ser commitadas, apenas informadas via .env fora do repositório.

CORS

- O backend permite o acesso apenas de origens autorizadas, prevenindo ataques cross-origin indevidos.

Logs e auditoria

- Operações sensíveis (login, movimentação de estoque, cadastro de usuário) são logadas para auditoria e detecção de acessos indevidos.

Observações
- Nunca exponha suas variáveis de ambiente reais.

- Se possível, rode a API sempre com HTTPS em produção.

- O sistema permite a extensão de regras de autorização facilmente via configuração do Spring Security.

---
<br>

## 🧪 Testes
A aplicação possui testes automatizados para garantir o funcionamento das principais regras de negócio, endpoints e integrações.

- Unitários: Cobrem serviços, validações e lógica interna usando JUnit.

- Integração: Testes que simulam chamadas reais aos endpoints, usando Spring Boot Test e banco em memória (H2) ou containers (Testcontainers).

Como rodar os testes
Para rodar todos os testes:

        mvn test
Ou para rodar testes específicos:

        mvn -Dtest=NomeDoTeste test

- Os resultados aparecem no console e também em /target/surefire-reports/.

- Recomenda-se executar os testes antes de cada commit/deploy para garantir estabilidade.

Dicas avançadas
- Você pode evoluir para testes de integração que sobem containers Oracle reais usando Testcontainers.

- Cobertura pode ser medida via plugins como JaCoCo (opcional).

---





