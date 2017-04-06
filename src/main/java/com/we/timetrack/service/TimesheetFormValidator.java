package com.we.timetrack.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.we.timetrack.service.model.TimesheetForm;

@Component
public class TimesheetFormValidator implements Validator {

	private static Logger logger = Logger.getLogger(TimesheetFormValidator.class);
	
	@Override
	public boolean supports(Class<?> arg0) {
		return TimesheetForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		TimesheetForm timesheetForm = (TimesheetForm) target;
		
		if (timesheetForm.getDates() == null || timesheetForm.getDates().length() < 8){
			logger.warn("Dates is null or length < 8");
			errors.rejectValue("dates", "NotEmpty.timesheetForm.dates");
		} else {
			try {
				String[] dates = timesheetForm.getDates().split(", ");
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				// Try parse dates
				for (String dateStr : dates) {
					LocalDate.parse(dateStr, dateFormat);
				}
			} catch (Exception e) {
				errors.rejectValue("dates", "Valid.timesheetForm.dates");
				logger.warn("Error in parsing date from timesheetForm", e);
			}
		}

	}

}
