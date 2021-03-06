package com.we.timetrack.service.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateRange {

	public final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	private LocalDate begin;
	private LocalDate end;
	
	public DateRange(){
		
	}
	
	public DateRange(LocalDate begin, LocalDate end){
		this.begin = begin;
		this.end = end;
	}
	
	public LocalDate getBegin() {
		return begin;
	}
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		if (begin != null && end != null){
			return begin.format(dateFormat) + " - " + end.format(dateFormat);
		} else {
			return null;
		}
	}
	
}
