package org.psb.TodoList;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.psb.TodoList.models.ToDo;
import org.psb.TodoList.repositories.TodoListRepository;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.config.name=application-test"
)
class ToDoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoListRepository repository;

    private String baseUrl() {
        return "http://localhost:" + port + "/todos";
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testCreateReadUpdateDelete() {
        
        // --- CREATE
        ToDo newToDo = new ToDo( null, "Write tests", "Integration test for CRUD" );
        ResponseEntity<ToDo> createResponse = restTemplate.postForEntity(
            URI.create(baseUrl()),
            newToDo,
            ToDo.class
        );
        
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ToDo created = createResponse.getBody();
        assert created != null;
        assertThat(created.getId()).isPositive();
        assertThat(created.getDescription()).isEqualTo("Write tests");

        Integer id = created.getId();

        // --- READ ALL
        ResponseEntity<ToDo[]> listResponse = restTemplate.getForEntity(
            baseUrl(), ToDo[].class
        );
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ToDo[] ToDos = listResponse.getBody();
        assertThat(ToDos).hasSize(1);

        // --- READ ONE
        ResponseEntity<ToDo> getResponse = restTemplate.getForEntity(
            baseUrl() + "/" + id, ToDo.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getDescription())
            .isEqualTo("Integration test for CRUD");

        // --- UPDATE
        ToDo updated = new ToDo( id, "Write more tests", "Include edge cases" );
        HttpEntity<ToDo> updateReq = new HttpEntity<>(updated);
        ResponseEntity<ToDo> updateResponse = restTemplate.exchange(
            baseUrl() + "/" + id,
            HttpMethod.PUT,
            updateReq,
            ToDo.class
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        // assertThat(updateResponse.getBody().getCompletionStatus()).isTrue();
        assertThat(updateResponse.getBody().getDescription())
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
        List<ToDo> remaining = repository.getAllTodos();
        assertThat(remaining).isEmpty();
    }
    
}