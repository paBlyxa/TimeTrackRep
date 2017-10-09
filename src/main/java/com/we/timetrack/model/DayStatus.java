package com.we.timetrack.model;

public enum DayStatus {
	Work(8.0f),Short(7.0f),Weekend(0.0f);
	
	private final float workingHours;
	
	private DayStatus(float workingHours){
		this.workingHours = workingHours;
	}
	
	public float getWorkingHours(){
		return workingHours;
	}
	
	public boolean isWeekend(){
		return workingHours == 0.0f;
	}
}
