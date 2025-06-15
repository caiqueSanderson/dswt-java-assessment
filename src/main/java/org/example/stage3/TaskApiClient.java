package org.example.stage3;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TaskApiClient {
    private static final String BASE_URL = "http://localhost:7000";

    public static void main(String[] args) throws IOException {
        String createdId = createTask();
        listTasks();
        getTaskById(createdId);
        getStatus();
    }

    public static String createTask() throws IOException {
        String json = """
                {
                  "title": "Tarefa de consumo",
                  "description": "Criada via cliente HttpURLConnection"
                }
                """;

        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/json");
        conn.setDoInput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("POST /tarefas => HTTP " + responseCode);
        System.out.println("Resposta:\n" + response);
        conn.disconnect();

        return response.contains("\"id\"") ? response.split("\"id\":\"")[1].split("\"")[0] : null;
    }

    public static void listTasks() throws IOException {
        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("GET /tarefas => HTTP " + responseCode);
        System.out.println("Resposta: \n" + response);
        conn.disconnect();
    }

    public static void getTaskById(String id) throws IOException{
        if (id == null){
            System.out.println("ID da tarefa não encontrado");
            return;
        }

        URL url = new URL(BASE_URL + "/tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("GET /tarefas/" + id + " => HTTP " + responseCode);
        System.out.println("Resposta:\n" + response);
        conn.disconnect();
    }

    public static void getStatus() throws IOException {
        URL url = new URL(BASE_URL + "/status");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("GET /status => HTTP " + responseCode);
        System.out.println("Resposta:\n" + response);
        conn.disconnect();
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        try (InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                response.append(line).append("\n");
            return response.toString();
        }
    }
}
