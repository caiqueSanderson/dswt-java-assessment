package org.example.stage2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AppTest {
    private static final String URL = "http://localhost:7000";

    @Test
    void helloEndpoint_shouldReturn200AndMessage() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/hello"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Hello, Javalin!", response.body());
    }

    static String createdTaskId;

    @Test
    void createTask_shouldReturn201() throws Exception {
        String json = """
                {
                  "title": "Teste",
                  "description": "Criando tarefa para teste"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tarefas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String body = response.body();
        createdTaskId = body.split("\"id\":\"")[1].split("\"")[0];
        assertNotNull(createdTaskId);
    }

    @Test
    void getTaskById_shouldReturnCreatedTask() throws Exception {
        assumeTrue(createdTaskId != null, "Tarefa deve ser criada primeiro");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tarefas/" + createdTaskId))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"title\":\"Teste\""));
        assertTrue(response.body().contains("\"description\":\"Criando tarefa para teste\""));
    }

    @Test
    void getAllTasks_shouldReturnNonEmptyArray() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tarefas"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().startsWith("["));
        assertTrue(response.body().contains("\"title\""));
    }
}
