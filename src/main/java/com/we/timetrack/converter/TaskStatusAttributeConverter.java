package com.we.timetrack.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.we.timetrack.model.TaskStatus;

@Converter
public class TaskStatusAttributeConverter implements AttributeConverter<TaskStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TaskStatus attribute) {
		switch (attribute) {
		case Active:
			return new Integer(1);
		case NotActive:
			return new Integer(2);
		default:
			throw new IllegalArgumentException("Unknown " + attribute);
		}
	}

	@Override
	public TaskStatus convertToEntityAttribute(Integer dbData) {
		switch (dbData){
		case 1:
			return TaskStatus.Active;
		case 2:
			return TaskStatus.NotActive;
			default:
				throw new IllegalArgumentException("Unknown " + dbData);
		}
	}

}
