package org.example.stage1;
import io.javalin.Javalin;
import org.example.stage1.model.Task;
import org.example.stage1.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    }

    record StatusResponse(String status, String timestamp){}
    record GreetingResponse(String message) {}
}
