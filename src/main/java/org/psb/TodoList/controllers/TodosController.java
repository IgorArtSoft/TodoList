package org.psb.TodoList.controllers;

import java.util.List;

import org.psb.TodoList.models.ToDo;
import org.psb.TodoList.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodosController {
   
    @Autowired
    TodoService todoService;

    // Retrieve all todos
    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public ResponseEntity<List<ToDo>> getAllTodos() {
        
        // Prevent exposing internal implementation of RESTful API to potential hackers
        try {
            return ResponseEntity.status(HttpStatus.OK).body( todoService.getAllTodos() );          
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( null );
        }

    }

    // Retrieve a single todo
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.GET)
    public ResponseEntity<ToDo> getTodoById(@PathVariable Integer id) {
        
        try {
            ToDo todo = todoService.getTodoById(id);
            if( todo != null ) {
                return ResponseEntity.status(HttpStatus.OK).body( todo );                
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body( null );                
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( null );
        }
   
    }

    // Create new todo
    @RequestMapping(value = "/todos", method = RequestMethod.POST)
    public ResponseEntity<String> createTodoById(@RequestBody ToDo todoData) {
       
        try {
            ToDo rowsCreated = todoService.createNewTodo(todoData);
            if( rowsCreated != null ) {
                return ResponseEntity.status(HttpStatus.CREATED).body( "Resource was created" );                
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Resource was NOT created" );                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( null );
        }
    }

    // Update a todo by id
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> patchTodoById(@PathVariable Integer id, @RequestBody ToDo updatedTodoData) {
        
        try {
            
            ToDo rowsUpdated = todoService.updateTodo(id, updatedTodoData);
            
            if( rowsUpdated != null ) {
                return ResponseEntity.status(HttpStatus.OK).body( "Resource was updated for id" + id );                
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Resource was NOT updated for id " + id );                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( null );
        }       
        
    }
    

    // Delete specific todo resource, identified by id
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTodoById(@PathVariable Integer id) {
                
        try {
            
            int rowsUpdated = todoService.deleteTodo(id);;
            
            if( rowsUpdated == 1 ) {
                return ResponseEntity.status(HttpStatus.OK).body( "Resource was deleted for id" + id );                
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( "Resource was NOT deleted for id " + id );                
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( null );            
        }
        
    }

}
