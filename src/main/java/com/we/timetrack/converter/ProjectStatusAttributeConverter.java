package com.we.timetrack.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.we.timetrack.model.ProjectStatus;

@Converter
public class ProjectStatusAttributeConverter implements AttributeConverter<ProjectStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ProjectStatus attribute) {
		switch (attribute) {
		case Active:
			return new Integer(1);
		case Completed:
			return new Integer(2);
		default:
			throw new IllegalArgumentException("Unknown" + attribute);
		}
	}

	@Override
	public ProjectStatus convertToEntityAttribute(Integer dbData) {
		switch (dbData){
		case 1:
			return ProjectStatus.Active;
		case 2:
			return ProjectStatus.Completed;
			default:
				throw new IllegalArgumentException("Unknown" + dbData);
		}
	}

}
