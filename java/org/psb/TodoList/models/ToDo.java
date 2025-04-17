package org.psb.TodoList.models;

public class ToDo {
    
    Integer id;
    String description;
    String completionStatus;
    
    public ToDo(){}
    
    public ToDo(Integer id, String description, String completionStatus){
	this.id = id;
	this.description = description;
	this.completionStatus = completionStatus;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCompletionStatus() {
        return completionStatus;
    }
    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }
    
}
