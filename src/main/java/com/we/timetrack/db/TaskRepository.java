package com.we.timetrack.db;

import java.util.List;

import com.we.timetrack.model.Department;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.TaskStatus;

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
     * Returns list of all task database records
     * with matching TaskStatus.
     */
	public List<Task> getTasks(TaskStatus status);
	
	/**
     * Saves a Task object.
     */
	public void saveTask(Task task);
	
	/**
     * Deletes Task record 
     */
	public void deleteTask(Task task);

	
	/**
	 * Returns list of task database records
	 * with matching TaskStatus, Department, and free of projects.
	 */
	public List<Task> getFreeTasks(TaskStatus status, Department department);
}
