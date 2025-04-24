package com.example.crud.service;

import com.example.crud.model.Todo;
import com.example.crud.repository.TodoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<Todo> findAll() {
        return repo.findAll();
    }

    public Todo findById(Integer id) {
        return repo.findById(id)
                   .orElseThrow(() -> new RuntimeException("Todo not found"));
    }

    public Todo create(Todo todo) {
        return repo.save(todo);
    }

    public Todo update(Integer id, Todo dto) {
        Todo existing = findById(id);
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setCompleted(dto.isCompleted());
        return repo.save(existing);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
