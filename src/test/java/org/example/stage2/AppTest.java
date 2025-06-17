package org.example.stage2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AppTest {
    private static final String URL = "http://localhost:7000";

    private String buildTaskJson(String title, String description) throws Exception {
        return """
                {
                    "title": "%s",
                    "description": "%s"
                }
                """.formatted(title, description);
    }

    private String extractIdFromResponse(String responseBody) {
        return responseBody.split("\"id\":\"")[1].split("\"")[0];
    }

    @Test
    @DisplayName("[GET] /hello -> Deve retornar 200 e mensagem 'Hello, Javalin!'")
    void helloEndpoint_shouldReturn200AndMessage() throws Exception {
        System.out.println("Testando /hello endpoint...");
        // ARRANGE
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/hello"))
                .GET()
                .build();

        // ACT
        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        // ASSERT
        assertEquals(200, response.statusCode(), "Status Code inválido");
        assertEquals("Hello, Javalin!", response.body(), "A mensagem retornada não é a esperada");
    }

    static String createdTaskId;

    @Test
    @DisplayName("[POST] /tarefas -> Deve criar uma tarefa com sucesso")
    void createTask_shouldReturn201() throws Exception {
        String json = buildTaskJson("Teste Create","Criando tarefa para teste");

        System.out.println("Criando tarefa...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tarefas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Resposta criação: " + response.body());

        assertEquals(201, response.statusCode(), "Erro: tarefa não foi criada corretamente.");

        String body = response.body();
        createdTaskId = extractIdFromResponse(body);
        assertNotNull(createdTaskId, "ID da tarefa não deve ser nulo.");
        System.out.println("Tarefa criada com ID: " + createdTaskId);
    }

    @Test
    @DisplayName("🔍 [GET] /tarefas/{id} -> Deve buscar uma tarefa específica")
    void getTaskById_shouldReturnCreatedTask() throws Exception {
        createTask_shouldReturn201();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tarefas/" + createdTaskId))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Resposta busca por ID: " + createdTaskId);

        assertEquals(200, response.statusCode(), "Status code inválido.");
        assertTrue(response.body().contains("\"title\":\"Teste Create\""),
            "Error: O título da tarefa não corresponde.");
        assertTrue(response.body().contains("\"description\":\"Criando tarefa para teste\""),
                "Error: A descrição da tarefa não corresponde.");
    }

    @Test
    @DisplayName("[GET] /tarefas -> Deve listar tarefas existentes")
    void getAllTasks_shouldReturnNonEmptyArray() throws Exception {
        createTask_shouldReturn201();

        System.out.println("Buscando todas as tarefas...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tarefas"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Resposta da listagem: " + response.body());

        assertEquals(200, response.statusCode(),
                "Status code inválido na listagem.");
        assertTrue(response.body().startsWith("["),
                "A resposta da listagem não parece ser um array JSON.");
        assertTrue(response.body().contains("\"title\""),
                "Nenhuma tarefa encontrada na resposta.");

        System.out.println("Listagem funcionando corretamente.");
    }
}
