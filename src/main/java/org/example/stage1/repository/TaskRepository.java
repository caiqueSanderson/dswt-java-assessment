package org.example.stage1.repository;

import org.example.stage1.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    public void add(Task task) {
        tasks.add(task);
    }

    public List<Task> getAll() {
        return tasks;
    }

    public Optional<Task> findById(UUID id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public boolean update(UUID id, String title, String description, boolean completed) {
        return findById(id).map(task -> {
            task.setTitle(title);
            task.setDescription(description);
            task.setCompleted(completed);
            return true;
        }).orElse(false);

    }

    public boolean markAsCompleted(UUID id) {
        return findById(id).map(task -> {
            task.setCompleted(true);
            return true;
        }).orElse(false);
    }

    public boolean remove(UUID id) {
        return tasks.removeIf(task -> task.getId().equals(id));
    }
}
