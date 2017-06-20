package com.we.timetrack.converter;

import javax.persistence.AttributeConverter;

import com.we.timetrack.model.DayStatus;

public class DayStatusAttributeConverter implements AttributeConverter<DayStatus, Integer>{

	@Override
	public Integer convertToDatabaseColumn(DayStatus attribute) {
		switch(attribute){
		case Work:
			return new Integer(0);
		case Short:
			return new Integer(1);
		case Weekend:
			return new Integer(2);
		default:
			throw new IllegalArgumentException("Unknown DayStatys " + attribute);
		}
	}

	@Override
	public DayStatus convertToEntityAttribute(Integer dbData) {
		switch(dbData){
		case 0:
			return DayStatus.Work;
		case 1:
			return DayStatus.Short;
		case 2:
			return DayStatus.Weekend;
		default:
			throw new IllegalArgumentException("Unknown code of DayStatus: " + dbData);
		}
	}

}
