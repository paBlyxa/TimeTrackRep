package com.we.timetrack.controller.binder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

import com.we.timetrack.service.model.DateRange;

public class DateRangeEditor extends PropertyEditorSupport{

	@Override
	public void setAsText(String strDateRange){
		
		DateRange dateRange = new DateRange();
		if (!(strDateRange).equals("")){
			String[] strDates = strDateRange.split(" - ");
			dateRange.setBegin(LocalDate.parse(strDates[0], DateRange.dateFormat));
			if (strDates.length > 1) {
				dateRange.setEnd(LocalDate.parse(strDates[1], DateRange.dateFormat));
			} else {
				dateRange.setEnd(dateRange.getBegin());
			}
			
		}
		this.setValue(dateRange);
	}
}
