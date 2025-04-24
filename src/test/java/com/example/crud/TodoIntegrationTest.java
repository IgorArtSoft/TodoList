package com.example.crud;

import com.example.crud.model.Todo;
import com.example.crud.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = CrudApplication.class,                     // ← specify your main class
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.config.name=application-test"
      )
class TodoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository repository;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/todos";
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testCreateReadUpdateDelete() {
        // --- CREATE
        Todo newTodo = new Todo("Write tests", "Integration test for CRUD", false);
        ResponseEntity<Todo> createResponse = restTemplate.postForEntity(
            URI.create(baseUrl()),
            newTodo,
            Todo.class
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Todo created = createResponse.getBody();
        assert created != null;
        assertThat(created.getId()).isPositive();
        assertThat(created.getTitle()).isEqualTo("Write tests");

        Long id = created.getId();

        // --- READ ALL
        ResponseEntity<Todo[]> listResponse = restTemplate.getForEntity(
            baseUrl(), Todo[].class
        );
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Todo[] todos = listResponse.getBody();
        assertThat(todos).hasSize(1);

        // --- READ ONE
        ResponseEntity<Todo> getResponse = restTemplate.getForEntity(
            baseUrl() + "/" + id, Todo.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getDescription())
            .isEqualTo("Integration test for CRUD");

        // --- UPDATE
        Todo updated = new Todo("Write more tests", "Include edge cases", true);
        HttpEntity<Todo> updateReq = new HttpEntity<>(updated);
        ResponseEntity<Todo> updateResponse = restTemplate.exchange(
            baseUrl() + "/" + id,
            HttpMethod.PUT,
            updateReq,
            Todo.class
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().isCompleted()).isTrue();
        assertThat(updateResponse.getBody().getTitle())
            .isEqualTo("Write more tests");

        // --- DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            baseUrl() + "/" + id,
            HttpMethod.DELETE,
            null,
            Void.class
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        // verify gone
        List<Todo> remaining = repository.findAll();
        assertThat(remaining).isEmpty();
    }
}
