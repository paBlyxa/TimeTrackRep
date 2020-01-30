package com.we.timetrack.service.model;

public enum ScheduleType {

	OFF("Отключены"), WEEK("Неделя"), MONTH("Месяц"), QUARTER("Квартал");
	
	private ScheduleType(String text) {
		this.text = text;
	}
	
	private final String text;
	
	public String getText() {
		return text;
	}
	
}
