package org.psb.TodoList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.psb.TodoList.controllers.TodosController;
import org.psb.TodoList.models.ToDo;
import org.psb.TodoList.repositories.TodoListRepository;
import org.psb.TodoList.services.TodoService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// @WebMvcTest(TodosController.class)
class TodoListApplicationTests {

    private static final int NON_EXISTENT_ID = 99;
    
    @Mock private TodoListRepository repo;
    @InjectMocks private TodoService svc;

    private ToDo existing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existing = new ToDo( 1, "Description 1", "Initial" );
    }

    @Test
    void findAll_delegatesToRepo() {
        svc.getAllTodos();
        verify(repo).getAllTodos();
    }

    @Test
    void findById_whenExists_returnsToDo() {
        when(repo.findTodoById(1)).thenReturn(Optional.of(existing));
        ToDo p = svc.getTodoById(1);
        assertEquals( "Description 1", p.getDescription() );
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when( repo.findTodoById(NON_EXISTENT_ID)).thenReturn(Optional.empty() );
        ToDoListException ex = assertThrows( ToDoListException.class, () -> svc.getTodoById(NON_EXISTENT_ID) );
        assertTrue( ex.getMessage().contains( "not found" ) );
    }

//    @Test
//    void create_whenNew_savesAndReturns() {
//        ToDo toCreate = new ToDo( null, "Pencil", "Graphite");
//        when(repo.existsByDescription("Pencil")).thenReturn(false);
//        when(repo.save(toCreate)).thenAnswer(i -> { toCreate.setId(2);
//            return toCreate;
//        });
//
//        ToDo created = svc.createNewTodo( toCreate);
//        assertNotNull(created.getId());
//        assertEquals("Pencil", created.getDescription());
//    }
//
//    @Test
//    void create_whenDuplicate_throwsConflict() {
//        when(repo.existsByDescription(existing.getDescription())).thenReturn(true);
//        assertThrows( ToDoListException.class, () -> svc.createNewTodo( existing ) );
//    }

    @Test
    void update_whenExists_updatesFields() {
        ToDo update = new ToDo( null, "Description 1", "Initial" );
        when( repo.findTodoById(1) ).thenReturn(Optional.of(existing));
        when( repo.save( any() ) ).thenAnswer(i -> i.getArgument(0));

        ToDo result = svc.updateTodo(1, update);
        assertEquals("Initial", result.getDescription());
    }
    
}
