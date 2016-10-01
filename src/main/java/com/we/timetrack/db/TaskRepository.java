package com.we.timetrack.db;

import java.util.List;

import com.we.timetrack.model.Task;

public interface TaskRepository {

	/**
     * Gets task record with matching taskId
     */
	public Task getTask(int taskId);
	
	/**
     * Returns list of all task database records.
     */
	public List<Task> getTasks();
	
	/**
     * Saves a Task object.
     */
	public void saveTask(Task task);
	
	/**
     * Deletes Task record 
     */
	public void deleteTask(Task task);
}
