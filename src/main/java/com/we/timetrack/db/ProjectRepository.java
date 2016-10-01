package com.we.timetrack.db;

import java.util.List;

import com.we.timetrack.model.Project;

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
     * Saves a Project object.
     */
	public void saveProject(Project project);
	
	 /**
     * Deletes Project record 
     */
	public void deleteProject(Project project);
}
