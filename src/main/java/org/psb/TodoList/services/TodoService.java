package org.psb.TodoList.services;

import java.util.List;
import java.util.Optional;

import org.psb.TodoList.ToDoListException;
import org.psb.TodoList.models.ToDo;
import org.psb.TodoList.repositories.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service will utilize plain jdbc calls. It will not utilize ORM or JPA.
 * 
 * @author Igor Artimenko
 */
@Service
public class TodoService {

    @Autowired
    TodoListRepository todoListRepository;

    /**
     * @return a List of All todos regardless of status
     */
    public List<ToDo> getAllTodos() {
        return todoListRepository.getAllTodos();
    }

    /**
     * Retrieve full information about specific ToDo by it's id
     * 
     * @param id
     * @return
     */
    public ToDo getTodoById(Integer id) {        
        Optional<ToDo> todoItemOptional = todoListRepository.findTodoById(id);
        return todoItemOptional.orElseThrow(() -> new ToDoListException("User was not found") );        
    }

    /**
     * @param todoData is a Data Transfer Object (DTO). It contains all information
     *                 except id
     * @return 1 if the resource was created. 0 if it was not
     */
    public ToDo createNewTodo(ToDo todoData) {        
        ToDo todoInserted = todoListRepository.createNewTodo(todoData);
        return todoInserted;        
    }

    /**
     * Update information about the object
     * 
     * @param id       Identifies specific todo from the database
     * @param todoData
     * @return 1 if the resource was modified. 0 if it was not
     */
    public ToDo updateTodo(Integer id, ToDo todoData) {        
        return todoListRepository.updateTodo(id, todoData);        
    }

    /**
     * @param id of the resource to be deleted from the db
     * @return 1 if the resource was deleted. 0 if it was not
     */
    public int deleteTodo(Integer id) {
        return todoListRepository.deleteTodo(id);
    }

    public ToDo create(ToDo toCreate) {
        return null;
    }

}
