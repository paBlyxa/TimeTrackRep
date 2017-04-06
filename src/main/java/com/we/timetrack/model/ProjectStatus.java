package com.we.timetrack.model;

public enum ProjectStatus {
	Active("В работе"),
	Completed("Завершен");
	
	ProjectStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	private String name;
}
