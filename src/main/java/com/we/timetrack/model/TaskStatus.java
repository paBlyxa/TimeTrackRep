package com.we.timetrack.model;

public enum TaskStatus {
	Active("Активная"),
	NotActive("Неактивная");
	
	TaskStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	private String name;
}
