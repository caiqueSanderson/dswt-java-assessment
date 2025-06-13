package org.example.stage1;
import io.javalin.Javalin;
import org.example.stage1.model.Task;
import org.example.stage1.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class App {
    public static void main(String[] args){
        TaskRepository repository = new TaskRepository();
        Javalin app = Javalin.create().start(7000);

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

        app.get("/tarefas", context -> context.json(repository.getAll()));

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
    }

    record StatusResponse(String status, String timestamp){}
    record GreetingResponse(String message) {}
}
