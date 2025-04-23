package org.psb.TodoList.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.psb.TodoList.models.ToDo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

@Repository
public class TodoListRepository {

    private final JdbcTemplate jdbc;

    private final static String FIND_TODO_BY_ID = "SELECT id, description, completionStatus FROM todos WHERE id = ?";
    private final static String FIND_TODO_BY_DESCRIPTION = "SELECT id, description, completionStatus FROM todos WHERE description like %?%";
    private static final RowMapper<ToDo> USER_ROW_MAPPER = ( rs, rowNum) -> new ToDo( rs.getInt("id"), rs.getString("description"), rs.getString("completionStatus") );
    
    public TodoListRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ToDo> getAllTodos() {
        
	return jdbc.query( "SELECT id, description, completionStatus FROM todos", (rs, rowNum) -> new ToDo( rs.getInt( "id" ), rs.getString( "description" ), rs.getString("completionStatus") ) );
    }

    public Optional<ToDo> findTodoById(Integer id) {
	
        return this.jdbc.query( FIND_TODO_BY_ID, USER_ROW_MAPPER, id).stream().findFirst();
	
	 /* Avoid using deprecated methods like queryForObject
	    return jdbc.queryForObject( FIND_SPECIFIC_TODO, new Object[]{id},
		( rs, rowNum) -> new ToDo( rs.getInt("id"), rs.getString("description"), rs.getString("completionStatus") ) );
	*/
    }
      
    /**
     * @param todoData
     * @return It should return 1 if new ToDo was created successfully
     */
    public ToDo createNewTodo(ToDo todoData) {
        
        // id field will be AUTOINCREMENTed
        // int newRowsCreated = jdbc.update("INSERT INTO todos ( description, completionStatus) VALUES (?,?) RETURNING *", todoData.getDescription(), todoData.getCompletionStatus() );
        String sql =
                "INSERT INTO todos ( description, completionStatus) " +
                "VALUES(?, ?) " +
                "RETURNING id, description, completionStatus";

      return jdbc.queryForObject(
          sql,
          (rs, rowNum) -> {
              ToDo p = new ToDo();
              p.setId( rs.getInt( "id"));
              p.setDescription(  rs.getString("description"));
              p.setCompletionStatus( rs.getString("completionStatus"));
              return p;
          },
          todoData.getDescription(),
          todoData.getCompletionStatus()
      );
        
    }

    public ToDo updateTodo(Integer id, ToDo todoData) {
	System.out.printf( "\033[0;32m" );
	System.out.printf( "Description: %-30s CompletionStatus: %-30s  Id: %-30d\n", todoData.getDescription(), todoData.getCompletionStatus(), id  );
	System.out.printf( "\u001B[0m" );

        Optional<ToDo> retrieveUpdatedRow = Optional.empty();

        int rowUpdated = jdbc.update("UPDATE todos SET description = ?, completionStatus = ? WHERE id = ?",
                todoData.getDescription(), todoData.getCompletionStatus(), id);
        if (rowUpdated > 0) {
            retrieveUpdatedRow = this.jdbc.query(FIND_TODO_BY_ID, USER_ROW_MAPPER, id).stream().findFirst();
        }
        return retrieveUpdatedRow.get();
    }

    public int deleteTodo(Integer id) {
        return jdbc.update( "DELETE FROM todos WHERE id = ?", id );
    }

    public Optional<ToDo> existsByDescription(String description) {
        return this.jdbc.query( FIND_TODO_BY_DESCRIPTION, USER_ROW_MAPPER, description ).stream().findFirst();
    }

    public Object save(ToDo toCreate) {
        return null;
    }

    public void deleteAll() {
        jdbc.update( "DELETE FROM todos");
    }
    
}
