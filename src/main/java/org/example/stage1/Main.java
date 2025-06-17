package org.example.stage1;
import io.javalin.Javalin;
import org.example.stage1.model.Task;
import org.example.stage1.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Main {
    public static void main(String[] args){
        TaskRepository repository = new TaskRepository();
        Javalin app = Javalin.create().start(7000);

        app.get("/", context -> {
            context.json(new Object() {
                public final String message = "Bem-vindo à Task API! Lista de endpoints disponíveis:";
                public final Endpoint[] endpoints = {
                        new Endpoint("GET", "/hello", "Retorna uma mensagem simples de teste."),
                        new Endpoint("GET", "/status", "Verifica se a API está online, com timestamp atual."),
                        new Endpoint("POST", "/echo", "Recebe uma string no corpo e retorna essa mesma string."),
                        new Endpoint("GET", "/saudacao/{name}", "Retorna uma saudação personalizada com o nome informado."),
                        new Endpoint("POST", "/tarefas", "Cria uma nova tarefa."),
                        new Endpoint("GET", "/tarefas", "Lista todas as tarefas. Se não houver, retorna mensagem informando."),
                        new Endpoint("GET", "/tarefas/{id}", "Busca uma tarefa específica pelo ID."),
                        new Endpoint("PUT", "/tarefas/{id}", "Atualiza uma tarefa específica pelo ID."),
                        new Endpoint("PATCH", "/tarefas/{id}/concluida", "Marca uma tarefa como concluída."),
                        new Endpoint("DELETE", "/tarefas/{id}", "Remove uma tarefa específica pelo ID.")
                };
            });
        });


        app.get("/hello", context -> {
            context.contentType("text/plain; charset=UTF-8");
            context.result("Hello, Javalin!");
        });

        app.get("/status", context -> {
            String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            context.json(new StatusResponse("ok", now));
        });

        app.post("/echo", context -> {
            String message = context.body();
            context.result("Mensagem: " + message);
        });

        app.get("/saudacao/{name}", context -> {
            String name = context.pathParam("name");
            context.json(new GreetingResponse("Olá, " + name + "!"));
        });

        app.post("/tarefas", context -> {
            Task task = context.bodyAsClass(Task.class);
            Task newTask = new Task(task.getTitle(), task.getDescription());
            repository.add(newTask);
            context.status(201).json(newTask);
        });

        app.get("/tarefas", context -> {
            var tasks = repository.getAll();

            if(tasks.isEmpty()){
                context.status(200).json(new ListEmptyResponse(
                        "Nenhuma tarefa cadastrada",
                        tasks
                ));
            }else {
                context.json(tasks);
            }
        });

        app.get("tarefas/{id}", context -> {
            try{
                UUID id = UUID.fromString(context.pathParam("id"));
                repository.findById(id)
                        .ifPresentOrElse(
                                task -> context.json(task),
                                () -> context.status(404).result("Tarefa não encontrada")
                        );
            }catch (IllegalArgumentException e){
                context.status(400).result("ID inválido");
            }
        });

        app.put("/tarefas/{id}",context -> {
           try {
                UUID id = UUID.fromString(context.pathParam("id"));
                Task updatedTask = context.bodyAsClass(Task.class);
                boolean updated = repository.update(id, updatedTask.getTitle(), updatedTask.getDescription(), updatedTask.isCompleted());

                if(updated){
                    context.status(204);
                } else {
                    context.status(404).result("Tarefa não encontrada");
                }
           } catch (IllegalArgumentException e){
                context.status(400).result("ID inválido");
           }
        });

        app.patch("/tarefas/{id}/concluida", context -> {
            try{
                UUID id = UUID.fromString(context.pathParam("id"));
                boolean updated = repository.markAsCompleted(id);

                if(updated) {
                    context.status(204);
                } else {
                    context.status(404).result("Tarefa não encontrada");
                }
            } catch (IllegalArgumentException e){
                context.status(400).result("ID inválido");
            }
        });

        app.delete("/tarefas/{id}", context -> {
            try {
                UUID id = UUID.fromString(context.pathParam("id"));
                boolean removed = repository.remove(id);

                if (removed) {
                    context.status(204);
                } else {
                    context.status(404).result("Tarefa não encontrada");
                }
            } catch (IllegalArgumentException e) {
                context.status(400).result("ID inválido")  ;
            }
        });
    }

    record Endpoint(String method, String path, String description) {}
    record StatusResponse(String status, String timestamp){}
    record GreetingResponse(String message) {}
    record ListEmptyResponse(String message, Object tasks) {}
}
