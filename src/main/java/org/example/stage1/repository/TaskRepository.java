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
}
