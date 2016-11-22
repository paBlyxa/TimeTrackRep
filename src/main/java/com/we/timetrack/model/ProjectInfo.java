package com.we.timetrack.model;

import java.util.HashSet;
import java.util.Set;

public class ProjectInfo extends Project{

	public ProjectInfo(Project project){
		this.setComment(project.getComment());
		this.setName(project.getName());
		this.setProjectId(project.getProjectId());
	}
	
	private Set<Employee> managers = new HashSet<>();

	public Set<Employee> getManagers() {
		return managers;
	}

	public void setManagers(Set<Employee> managers) {
		this.managers = managers;
	}

}
