package org.psb.TodoList.controllers;

import java.util.List;

import org.psb.TodoList.models.ToDo;
import org.psb.TodoList.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodosController {

    @Autowired
    TodoService todoService;

    // Retrieve all todos
    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public @ResponseBody List<ToDo> getAllTodos() {
	return todoService.getAllTodos();
    }

    // Retrieve a single ToDo
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.GET)
    public @ResponseBody ToDo getTodoById( @PathVariable Integer id) {
	return todoService.getTodoById(id);
    }

    // create a todo
    @RequestMapping(value = "/todos", method = RequestMethod.POST)
    public @ResponseBody String createTodoById(@RequestBody ToDo todoData) {
	
	int rowCreated = todoService.createNewTodo(todoData);
	
	String response = (rowCreated > 0 ) ? "Resource was succesfully created": "Failure to create a Resource";
	
	return response;
    }

    // update a todo
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.PATCH)
    public @ResponseBody int patchTodoById(@PathVariable Integer id, @RequestBody ToDo updatedTodoData) {
	return todoService.updateTodo( id, updatedTodoData);
    }

    // delete specific todo resource, identified by id
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.DELETE)
    public @ResponseBody int deleteTodoById(@PathVariable Integer id) {
	return todoService.deleteTodo(id);
    }

}
