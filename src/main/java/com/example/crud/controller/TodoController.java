package com.example.crud.controller;

import com.example.crud.model.Todo;
import com.example.crud.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Todo> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Todo get(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        Todo saved = service.create(todo);
        return ResponseEntity
            .created(null)   // in prod, build URI: URI.create("/api/todos/" + saved.getId())
            .body(saved);
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable Integer id, @RequestBody Todo todo) {
        return service.update(id, todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
