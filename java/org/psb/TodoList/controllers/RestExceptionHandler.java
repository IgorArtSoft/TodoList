package org.psb.TodoList.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class RestExceptionHandler {
    
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Map<String,String>> notFound(ResourceNotFoundException ex) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                           .body(Map.of("error", ex.getMessage()));
//    }
//
//    @ExceptionHandler(ResourceConflictException.class)
//    public ResponseEntity<Map<String,String>> conflict(ResourceConflictException ex) {
//      return ResponseEntity.status(HttpStatus.CONFLICT)
//                           .body(Map.of("error", ex.getMessage()));
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String,String>> validationError(MethodArgumentNotValidException ex) {
//      String msg = ex.getBindingResult()
//                     .getFieldErrors()
//                     .stream()
//                     .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
//                     .collect(Collectors.joining("; "));
//      return ResponseEntity.badRequest().body(Map.of("error", msg));
//    }
    
}
