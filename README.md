# Assessment - Desenvolvimento de Serviços Web e Testes com Java

## 📑 Descrição Geral
Este projeto consiste na criação de uma API REST utilizando o framework **Javalin**, com implementação de testes unitários com **JUnit** e consumo de API via **HttpURLConnection**, conforme as etapas do assessment da disciplina **Desenvolvimento de Serviços Web e Testes com Java**.

---

## 🚀 Etapas do Projeto

### ✅ Etapa 1 - API REST com Javalin
- Executar a classe:  
  `org.example.stage1.Main`
- Porta utilizada:  
  `7000`

### ✅ Etapa 2 - Testes Automatizados com JUnit
- Executar os testes localizados em:  
  `src/test/java/org/example/stage2`

### ✅ Etapa 3 - Cliente HTTP com HttpURLConnection
- Executar a classe:  
  `org.example.stage3.TaskApiClient`

---

## Endpoints da API

| Method | Endpoint                  | Description                                           |
|--------|---------------------------|-------------------------------------------------------|
| GET    | `/`                       | Returns a list of all available API endpoints.        |
| GET    | `/hello`                  | Retorna uma mensagem de teste simples.                |
| GET    | `/status`                 | Verifica o status da API com timestamp atual.         |
| POST   | `/echo`                   | Recebe uma string no corpo e retorna essa string.     |
| GET    | `/saudacao/{nome}`        | Retorna uma saudação personalizada.                   |
| POST   | `/tarefas`                | Cria uma nova tarefa.                                 |
| GET    | `/tarefas`                | Lista todas as tarefas. Se vazio, informa que não há. |
| GET    | `/tarefas/{id}`           | Busca uma tarefa específica pelo seu ID.              |
| PUT    | `/tarefas/{id}`           | Atualiza uma tarefa específica pelo ID.               |
| PATCH  | `/tarefas/{id}/concluida` | Marca uma tarefa como concluída.                      |
| DELETE | `/tarefas/{id}`           | Remove uma tarefa específica pelo ID.                 |

--- 

## Exemplo de requisições

### Criar uma tarefa

`POST /tarefas`

Request body:
```
{
  "title": "Estudar Java",
  "description": "Fazer os exercícios de Javalin"
}
```

Resposta:

```
{
  "id": "uuid-gerado",
  "title": "Estudar Java",
  "description": "Fazer os exercícios de Javalin",
  "completed": false,
  "creationDate": "2025-06-15T16:40:00"
}
```

### Buscar todas as tarefas

`GET /tarefas`

Se houver tarefas:
```
[
  {
    "id": "uuid",
    "title": "Estudar Java",
    "description": "Fazer os exercícios de Javalin",
    "completed": false,
    "creationDate": "2025-06-15T16:40:00"
  }
]
```
Se não houver tarefas:

```
{
  "message": "Nenhuma tarefa cadastrada.",
  "tarefas": []
}
```