package com.we.timetrack.db;

import java.util.List;
import java.util.UUID;

import com.we.timetrack.model.Project;
import com.we.timetrack.model.ProjectStatus;

public interface ProjectRepository {

	/**
     * Gets project record with matching projectId
     */
	public Project getProject(int projectId);
	
	/**
     * Returns list of all project database records.
     */
	public List<Project> getProjects();
	
	/**
     * Returns list of project database records
     * with matching ProjectStatus.
     */
	public List<Project> getProjects(ProjectStatus status);	
	
	/**
     * Saves a Project object.
     */
	public void saveProject(Project project);
	
	 /**
     * Deletes Project record 
     */
	public void deleteProject(Project project);
	
	/**
	 * Returns list of all project databse records
	 * with matching employee as leader
	 */
	public List<Project> getProjects(UUID employeeId);
}
