# Movin - Backend

Backend do projeto Movin, desenvolvido em Java com JDBC e MySQL.

A ideia foi manter uma estrutura simples, sem frameworks, fazendo a comunicação com o banco de dados diretamente pelo Java.

Tecnologias utilizadas

- Java 21
- Maven
- MySQL
- JDBC
- Gson
- HttpServer do Java

Não foi utilizado Spring, Hibernate, JPA ou outro framework de backend.

---

## Estrutura do backend

O backend foi separado da seguinte forma:

```text
backend/
├── pom.xml
└── src/main/java/org/movin/
    ├── Main.java
    ├── config/
    │   └── ConnectionFactory.java
    ├── model/
    │   ├── Treino.java
    │   └── Exercicio.java
    ├── dao/
    │   ├── TreinoDAO.java
    │   └── ExercicioDAO.java
    └── controller/
        ├── TreinoController.java
        └── ExercicioController.java
```

---

## Como o backend funciona

O backend segue uma estrutura simples:

```text
Frontend
   ↓
Controller
   ↓
DAO
   ↓
MySQL
```

E para retornar os dados:

```text
MySQL
   ↓
DAO
   ↓
Controller
   ↓
JSON
   ↓
Frontend
```

Cada parte possui uma responsabilidade diferente.

---

## Model

As classes da pasta `model` representam os dados utilizados pelo sistema.

Atualmente existem:

```text
Treino.java
Exercicio.java
```

Um treino possui informações como:

```text
id
titulo
```

Um exercício possui:

```text
id
nome
series
repeticoes
kg
```

Esses objetos são usados para transportar os dados entre as partes do backend.

---

## DAO

A pasta `dao` contém as classes responsáveis pela comunicação direta com o banco de dados.

Existem:

```text
TreinoDAO.java
ExercicioDAO.java
```

É nessa parte que ficam os comandos SQL do sistema, como:

```sql
INSERT
SELECT
UPDATE
DELETE
```

Também são utilizados:

```text
Connection
PreparedStatement
ResultSet
```

O DAO recebe os dados, executa a operação necessária no MySQL e devolve o resultado para o Controller.

Por exemplo:

```text
TreinoController
       ↓
TreinoDAO
       ↓
MySQL
```

---

## Controller

Os Controllers recebem as requisições HTTP enviadas pelo frontend.

Existem:

```text
TreinoController.java
ExercicioController.java
```

Eles recebem requisições como:

```text
GET
POST
PUT
DELETE
```

O Controller lê os dados recebidos, chama o DAO necessário e depois devolve uma resposta para o frontend.

Os dados enviados e recebidos pela API utilizam JSON.

O Gson é utilizado para trabalhar com esses dados.

Exemplo:

```json
{
    "nome": "Supino",
    "series": 3,
    "repeticoes": 10,
    "kg": 50
}
```

---

## ConnectionFactory

A classe:

```text
ConnectionFactory.java
```

é responsável por abrir a conexão entre o Java e o MySQL.

Ela utiliza o JDBC através do MySQL Connector.

A configuração fica semelhante a:

```java
private static final String URL = "jdbc:mysql://localhost:3306/movin";
private static final String USUARIO = "root";
private static final String SENHA = "SUA_SENHA";
```

Cada pessoa que executar o projeto deve colocar a senha do MySQL instalado na própria máquina.

Por segurança, não é recomendado colocar a senha pessoal do MySQL no repositório público.

---

## Main

A classe:

```text
Main.java
```

é o ponto inicial do backend.

Ela cria o servidor HTTP, registra os Controllers e inicia o sistema na porta:

```text
3000
```

Quando o backend está funcionando, a API fica disponível em:

```text
http://localhost:3000/api
```

Ao iniciar corretamente, o console deverá mostrar:

```text
Servidor iniciado na porta 3000.
```

---

# Banco de dados

O projeto utiliza MySQL.

Primeiro deve ser criado o banco:

```sql
CREATE DATABASE movin;
USE movin;
```

Depois, as tabelas:

```sql
CREATE TABLE treino (
    id_treino INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL
);

CREATE TABLE exercicio (
    id_exercicio INT AUTO_INCREMENT PRIMARY KEY,
    id_treino INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    series INT NOT NULL,
    repeticoes INT NOT NULL,
    carga_kg DECIMAL(6,2) NOT NULL,
    concluido BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_exercicio_treino
        FOREIGN KEY (id_treino)
        REFERENCES treino(id_treino)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT chk_series CHECK (series > 0),
    CONSTRAINT chk_repeticoes CHECK (repeticoes > 0),
    CONSTRAINT chk_carga CHECK (carga_kg >= 0)
);
```

A relação entre as tabelas é:

```text
Treino 1 : N Exercícios
```

Ou seja, um treino pode possuir vários exercícios.

O campo:

```text
id_treino
```

da tabela `exercicio` é uma chave estrangeira ligada à tabela `treino`.

Também foi utilizado:

```sql
ON DELETE CASCADE
```

Com isso, ao excluir um treino, os exercícios relacionados a ele também são excluídos automaticamente.

---

# Dependências

O projeto utiliza Maven para controlar as dependências.

As dependências ficam no arquivo:

```text
pom.xml
```

## MySQL Connector

Responsável pela conexão do Java com o MySQL.

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>9.7.0</version>
</dependency>
```

## Gson

Utilizado para trabalhar com JSON.

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.11.0</version>
</dependency>
```

Ao abrir o projeto como Maven no IntelliJ, essas dependências devem ser baixadas automaticamente.

---

# O que é necessário para executar

Antes de rodar o backend, é necessário ter instalado:

```text
Java 21
MySQL
Maven
```

Uma IDE como IntelliJ IDEA também pode ser utilizada para facilitar a execução.

---

# Como executar o projeto

## 1. Iniciar o MySQL

O MySQL precisa estar rodando na máquina.

---

## 2. Criar o banco

Execute o script SQL informado anteriormente para criar:

```text
Banco: movin
Tabela: treino
Tabela: exercicio
```

---

## 3. Configurar a conexão

Abra:

```text
src/main/java/org/movin/config/ConnectionFactory.java
```

Configure:

```java
private static final String URL = "jdbc:mysql://localhost:3306/movin";
private static final String USUARIO = "root";
private static final String SENHA = "SUA_SENHA";
```

Troque:

```text
SUA_SENHA
```

pela senha do MySQL da máquina.

---

## 4. Abrir o backend

Abra a pasta:

```text
backend
```

no IntelliJ.

O projeto deve ser reconhecido como um projeto Maven por causa do arquivo:

```text
pom.xml
```

Caso as dependências ainda não tenham sido baixadas, atualize/recarregue o Maven.


Rodar no VS Code:

Instale a extensão Extension Pack for Java.

Abra a pasta backend, espere o Maven carregar as dependências, abra Main.java e clique em Run.

Quando aparecer:
Servidor iniciado na porta 3000.


a API estará rodando em:
http://localhost:3000/api/treinos
---

## 5. Executar

Execute:

```text
Main.java
```

Se estiver tudo correto, será mostrado:

```text
Servidor iniciado na porta 3000.
```

---

## 6. Testar

Com o servidor rodando, é possível acessar:

```text
http://localhost:3000/api/treinos
```

Se não existir nenhum treino cadastrado, a resposta será:

```json
[]
```

Se existirem treinos, será retornada uma lista em JSON.

---

# Rotas da API

A URL base utilizada é:

```text
http://localhost:3000/api
```

## Treinos

```text
GET    /api/treinos
POST   /api/treinos
PUT    /api/treinos/{id}
DELETE /api/treinos/{id}
```

## Exercícios

```text
GET    /api/treinos/{id}/exercicios
POST   /api/treinos/{id}/exercicios

PUT    /api/exercicios/{id}
DELETE /api/exercicios/{id}
```

---

# GET /api/treinos

Retorna os treinos cadastrados.

Exemplo:

```text
GET http://localhost:3000/api/treinos
```

Resposta:

```json
[
    {
        "id": 1,
        "titulo": "Treino A",
        "totalExercicios": 3
    }
]
```

---

# POST /api/treinos

Cria um novo treino.

Também é possível criar os exercícios junto com o treino.

Exemplo:

```text
POST http://localhost:3000/api/treinos
```

JSON:

```json
{
    "titulo": "Treino de Peito",
    "exercicios": [
        {
            "nome": "Supino",
            "series": 3,
            "repeticoes": 10,
            "kg": 50
        },
        {
            "nome": "Crucifixo",
            "series": 3,
            "repeticoes": 12,
            "kg": 20
        }
    ]
}
```

Resposta:

```json
{
    "id": 1,
    "titulo": "Treino de Peito",
    "totalExercicios": 2
}
```

O treino é criado primeiro.

Depois o ID gerado pelo MySQL é utilizado para relacionar os exercícios ao treino.

---

# PUT /api/treinos/{id}

Atualiza o título de um treino.

Exemplo:

```text
PUT http://localhost:3000/api/treinos/1
```

JSON:

```json
{
    "titulo": "Treino A - Peito"
}
```

---

# DELETE /api/treinos/{id}

Exclui um treino.

Exemplo:

```text
DELETE http://localhost:3000/api/treinos/1
```

Resposta:

```json
{
    "success": true
}
```

Como existe `ON DELETE CASCADE` no banco, os exercícios ligados ao treino também são removidos.

---

# GET /api/treinos/{id}/exercicios

Retorna todos os exercícios de um treino.

Exemplo:

```text
GET http://localhost:3000/api/treinos/1/exercicios
```

Resposta:

```json
[
    {
        "id": 1,
        "nome": "Supino",
        "series": 3,
        "repeticoes": 10,
        "kg": 50
    },
    {
        "id": 2,
        "nome": "Crucifixo",
        "series": 3,
        "repeticoes": 12,
        "kg": 20
    }
]
```

---

# POST /api/treinos/{id}/exercicios

Adiciona um exercício em um treino que já existe.

Exemplo:

```text
POST http://localhost:3000/api/treinos/1/exercicios
```

JSON:

```json
{
    "nome": "Supino Inclinado",
    "series": 4,
    "repeticoes": 10,
    "kg": 40
}
```

---

# PUT /api/exercicios/{id}

Atualiza um exercício.

Exemplo:

```text
PUT http://localhost:3000/api/exercicios/1
```

JSON:

```json
{
    "nome": "Supino Inclinado",
    "series": 4,
    "repeticoes": 8,
    "kg": 60
}
```

---

# DELETE /api/exercicios/{id}

Exclui um exercício.

Exemplo:

```text
DELETE http://localhost:3000/api/exercicios/1
```

Resposta:

```json
{
    "success": true
}
```

---

# Integração com o frontend

O frontend se comunica com o backend pela API.

No frontend, a URL base utilizada é:

```javascript
const BASE_URL = 'http://localhost:3000/api';
```

O arquivo:

```text
api.js
```

fica responsável pelas chamadas HTTP.

Exemplo:

```javascript
const res = await fetch(`${BASE_URL}/treinos`);
```

O backend possui configuração de CORS para permitir que o frontend seja executado em uma porta diferente.

Por exemplo:

```text
Frontend:
http://localhost:5500

Backend:
http://localhost:3000
```

Isso permite utilizar o Live Server para rodar o frontend enquanto o Java continua rodando separadamente.

---

# Fluxo de uma requisição

Um exemplo de criação de treino funciona assim:

```text
Usuário clica em salvar
        ↓
Frontend cria o JSON
        ↓
POST /api/treinos
        ↓
TreinoController
        ↓
TreinoDAO
        ↓
INSERT no MySQL
        ↓
MySQL gera o ID do treino
        ↓
ExercicioDAO salva os exercícios
        ↓
Controller monta a resposta JSON
        ↓
Frontend recebe a resposta
```

Em uma listagem:

```text
Frontend
   ↓
GET /api/treinos
   ↓
TreinoController
   ↓
TreinoDAO
   ↓
SELECT no MySQL
   ↓
Controller transforma em JSON
   ↓
Frontend
```

---

# Operações CRUD

O projeto possui as quatro operações principais de banco de dados.

## Create

Criação de treino e exercícios.

```sql
INSERT
```

## Read

Consulta de treinos e exercícios.

```sql
SELECT
```

## Update

Alteração de treinos e exercícios.

```sql
UPDATE
```

## Delete

Exclusão de treinos e exercícios.

```sql
DELETE
```

---

# Validações

O banco também possui algumas restrições para evitar dados inválidos.

Séries:

```sql
CHECK (series > 0)
```

Repetições:

```sql
CHECK (repeticoes > 0)
```

Carga:

```sql
CHECK (carga_kg >= 0)
```

Além dessas validações do banco, o backend também valida os dados recebidos antes de executar algumas operações.

---

# Observações importantes

O backend foi mantido simples propositalmente.

Não foram utilizados:

```text
Spring
Spring Boot
Hibernate
JPA
ORM
```

A conexão com o MySQL é feita diretamente usando JDBC.

Os comandos SQL também são escritos diretamente nos DAOs.

A organização principal utilizada foi:

```text
Model
  ↓
DAO
  ↓
Controller
```

O `Main.java` fica responsável apenas por iniciar o servidor e registrar as rotas.

O objetivo dessa estrutura é deixar claro o funcionamento da comunicação entre Java, API e banco de dados, além de facilitar a visualização das operações CRUD.

---

# Resumo

Para executar o backend:

```text
1. Instalar Java 21 e MySQL
2. Criar o banco movin
3. Criar as tabelas treino e exercicio
4. Configurar usuário e senha em ConnectionFactory.java
5. Abrir o projeto backend como Maven
6. Executar Main.java
7. Acessar http://localhost:3000/api/treinos
```

Com o backend iniciado, o frontend pode acessar normalmente as rotas da API através da porta `3000`.

---

# Movin - Frontend

Frontend do projeto Movin, desenvolvido com HTML, CSS e JavaScript puro.

A ideia foi manter a mesma simplicidade do backend, sem frameworks ou bibliotecas externas, fazendo a comunicação com a API diretamente pelo JavaScript.

Tecnologias utilizadas

- HTML5
- CSS3
- JavaScript (vanilla)

Não foi utilizado React, Vue, Angular ou outro framework de frontend.

---

## Estrutura do frontend

O frontend foi separado da seguinte forma:

```text
FrontEnd/
├── index.html
├── src/
│   └── NovoTreino.html
├── style/
│   ├── index.css
│   ├── navbar.css
│   ├── cards.css
│   ├── dialog.css
│   └── plano.css
├── Js/
│   ├── api.js
│   ├── Cards.js
│   ├── dialog.js
│   └── novoTreino.js
└── Img/
    └── logo movin.png
```

---

## Como o frontend funciona

O frontend segue uma estrutura simples:

```text
Usuário
   ↓
HTML (página)
   ↓
JavaScript
   ↓
api.js (fetch)
   ↓
Backend (API REST)
```

E para exibir os dados:

```text
Backend
   ↓
api.js
   ↓
JavaScript
   ↓
DOM atualizado
   ↓
Usuário vê os dados
```

Cada arquivo JavaScript possui uma responsabilidade diferente.

---

## Páginas

O projeto possui duas páginas principais.

### index.html

Página inicial do sistema.

Exibe os treinos cadastrados no banco de dados em formato de cards.

Ao clicar em um card, é aberto um dialog com os exercícios daquele treino.

### src/NovoTreino.html

Página para criação de um novo treino.

O usuário preenche o título, seleciona o dia da semana e adiciona os exercícios com nome, séries, repetições e kg.

Ao concluir, os dados são enviados para a API e o treino é salvo no banco.

---

## Arquivos JavaScript

### api.js

Camada de comunicação com o backend.

Contém todas as funções de requisição HTTP:

```text
getTreinos()
createTreino()
updateTreino()
deleteTreino()

getExercicios()
createExercicio()
updateExercicio()
deleteExercicio()
```

Todas as funções utilizam `fetch` com `async/await`.

A URL base da API fica definida neste arquivo:

```javascript
const BASE_URL = 'http://localhost:3000/api';
```

### Cards.js

Responsável por carregar e renderizar os cards de treinos na tela inicial.

Ao carregar a página, exibe um skeleton de loading enquanto busca os dados.

```text
Página carrega
      ↓
Skeleton aparece (3 cards de carregamento)
      ↓
getTreinos() busca os dados
      ↓
Skeletons são removidos
      ↓
Cards reais são inseridos na tela
```

Cada card exibe:

```text
Título do treino
Quantidade de exercícios
Botão para abrir o dialog
```

### dialog.js

Controla o dialog de exercícios que abre ao clicar em um card.

Funcionalidades:

```text
Abrir dialog ao clicar no card
Exibir loading enquanto busca exercícios
Carregar exercícios do banco via getExercicios()
Fechar dialog com botão, clique fora ou tecla Escape
Modo de edição com botão Editar / Salvar
```

No modo de edição é possível:

```text
Alterar nome, séries e repetições dos exercícios
Adicionar novos exercícios
Excluir exercícios existentes
```

O campo de kg é sempre editável, sem precisar entrar no modo de edição.

Ao salvar, as operações enviadas para a API são:

```text
updateExercicio() - para exercícios existentes alterados
createExercicio() - para novos exercícios adicionados
deleteExercicio() - para exercícios excluídos
```

O reload da página ao fechar o dialog só acontece se houve alguma mudança real, como:

```text
Exercício adicionado
Exercício excluído
Exercício atualizado
Kg alterado
```

### novoTreino.js

Controla a lógica da página de criação de treino.

Funcionalidades:

```text
Seleção de dia da semana
Adição de linhas de exercícios
Remoção de linhas de exercícios
Validação dos campos obrigatórios
Envio do treino para a API
```

A validação impede o envio se:

```text
O título não for preenchido
Não houver pelo menos um exercício com nome
Séries ou repetições estiverem em branco
```

As mensagens de validação são personalizadas em português:

```text
"Informe o número de séries"
"Informe o número de repetições"
"O valor mínimo é 1"
```

Enquanto o treino está sendo salvo, o botão exibe:

```text
Salvando...
```

---

## Layout e responsividade

A página inicial exibe os cards em um grid com 3 colunas.

Em telas menores, o grid se adapta automaticamente:

```text
Acima de 860px  → 3 colunas
Entre 560-860px → 2 colunas
Abaixo de 560px → 1 coluna
```

Os arquivos de estilo são separados por responsabilidade:

```text
index.css  → layout geral e grid de cards
cards.css  → estilo dos cards e skeleton de loading
dialog.css → estilo do dialog de exercícios
navbar.css → barra de navegação
plano.css  → página de novo treino
```

---

## Loading

O frontend possui feedback visual de carregamento em três momentos.

### Carregamento dos cards

Ao abrir a página, três cards skeleton aparecem com animação shimmer enquanto os treinos são buscados no banco.

### Carregamento dos exercícios

Ao abrir um card, um spinner aparece dentro do dialog enquanto os exercícios são carregados da API.

### Salvando treino

Ao clicar em Concluir na página de novo treino, o botão é desabilitado e exibe:

```text
Salvando...
```

---

## Como executar o frontend

Para rodar o frontend localmente, é necessário ter o backend rodando primeiro.

Com o backend ativo, abra o arquivo:

```text
FrontEnd/index.html
```

Utilizando o Live Server do VS Code ou qualquer servidor local.

Se estiver usando o Live Server, o frontend ficará disponível em:

```text
http://localhost:5500
```

O backend precisa estar rodando em:

```text
http://localhost:3000
```

Para ajustar a URL da API, altere o arquivo:

```text
FrontEnd/Js/api.js
```

A variável:

```javascript
const BASE_URL = 'http://localhost:3000/api';
```

deve apontar para o endereço onde o backend está rodando.

---

## Resumo

Para executar o frontend:

```text
1. Ter o backend rodando na porta 3000
2. Abrir FrontEnd/index.html com Live Server
3. Acessar http://localhost:5500
```

Com o backend e o frontend rodando ao mesmo tempo, o sistema estará funcionando completamente.