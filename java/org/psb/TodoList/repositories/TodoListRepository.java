package org.psb.TodoList.repositories;

import java.util.List;
import java.util.Optional;

import org.psb.TodoList.models.ToDo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TodoListRepository {

    private final JdbcTemplate jdbc;

    private final static String FIND_SPECIFIC_TODO = "SELECT id, description, completionStatus FROM todos WHERE id = ?";
    private static final RowMapper<ToDo> USER_ROW_MAPPER = ( rs, rowNum) -> new ToDo( rs.getInt("id"), rs.getString("description"), rs.getString("completionStatus") );
    
    public TodoListRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ToDo> getAllTodos() {
	return jdbc.query( "SELECT id, description, completionStatus FROM todos", (rs, rowNum) -> new ToDo( rs.getInt( "id" ), rs.getString( "description" ), rs.getString("completionStatus") ) );
    }

    public Optional<ToDo> getTodoById(Integer id) {
	
        return this.jdbc.query( FIND_SPECIFIC_TODO, USER_ROW_MAPPER, id).stream().findFirst();
	
	 /* I avoided using deprecated methods like queryForObject
	    return jdbc.queryForObject( FIND_SPECIFIC_TODO, new Object[]{id},
		( rs, rowNum) -> new ToDo( rs.getInt("id"), rs.getString("description"), rs.getString("completionStatus") ) );
	*/
    }
      
    /**
     * @param todoData
     * @return It should return 1 if new ToDo was created successfully
     */
    public int insertNewTodo(ToDo todoData) {
	// id field will be AUTOINCREMENTed
	return jdbc.update("INSERT INTO todos ( description, completionStatus) VALUES (?,?)", todoData.getDescription(), todoData.getCompletionStatus() );
    }

    public int updateTodo( Integer id, ToDo todoData) {
	System.out.printf( "\033[0;32m" );
	System.out.printf( "Description: %-30s CompletionStatus: %-30s  Id: %-30d\n", todoData.getDescription(), todoData.getCompletionStatus(), id  );
	System.out.printf( "\u001B[0m" );

	return jdbc.update("UPDATE todos SET description = ?, completionStatus = ? WHERE id = ?", todoData.getDescription(), todoData.getCompletionStatus(), id );
    }

    public int deleteTodo(Integer id) {
	return jdbc.update( "DELETE FROM todos WHERE id = ?", id );
    }
}
