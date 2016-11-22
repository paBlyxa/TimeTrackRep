package com.we.timetrack.service.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import com.we.timetrack.model.Timesheet;

public class TimesheetDay {

	private static Logger logger = Logger.getLogger(TimesheetDay.class);
	
	private LocalDate date;
	private float hours;
	
	private List<Timesheet> timesheets;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(List<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	public float getHours() {
		return hours;
	}

	public void setHours(float hours) {
		this.hours = hours;
	}
	
	/**
	 * Group timesheets by day
	 * @param timesheets
	 * @return
	 */
	public static List<TimesheetDay> getTimesheetsByDays(List<Timesheet> timesheets, LocalDate beginDate, LocalDate endDate){
		
		Map<LocalDate, List<Timesheet>> timesheetsByDays = new HashMap<>();
		
		// Create map by days with empty lists
		for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(1)){
			timesheetsByDays.put(date, new ArrayList<Timesheet>());
		}
				
		// Fill map
		for(Timesheet timesheet : timesheets){
			List<Timesheet> timesheetList = timesheetsByDays.get(timesheet.getDateTask());
			if (timesheetList != null) {
				timesheetList.add(timesheet);
			} else {
				logger.error("Timesheet's date [" + timesheet.getDateTask() + "] is out of range: [" + beginDate + " - " + endDate + "]");
			}
		}
		
		// Convert to list of timesheetDay
		List<TimesheetDay> result = new ArrayList<>();
		for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(1)){
			TimesheetDay timesheetDay = new TimesheetDay();
			timesheetDay.setDate(date);
			List<Timesheet> tss = timesheetsByDays.get(date);
			float countHours = 0;
			// Sum hours
			for (Timesheet ts : tss){
				countHours += ts.getCountTime();
			}
			timesheetDay.setTimesheets(tss);
			timesheetDay.setHours(countHours);
			result.add(timesheetDay);
		}
		
		return result;
	}
	
}
