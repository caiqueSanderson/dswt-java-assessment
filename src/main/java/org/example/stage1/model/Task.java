package org.example.stage1.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime creationDate;

    public Task(String titulo, String descricao) {
        this.id = UUID.randomUUID();
        this.title = titulo;
        this.description = descricao;
        this.completed = false;
        this.creationDate = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCreationDate() { return creationDate; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
