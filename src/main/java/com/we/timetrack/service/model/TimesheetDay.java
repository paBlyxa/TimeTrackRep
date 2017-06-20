package com.we.timetrack.service.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.we.timetrack.model.Day;
import com.we.timetrack.model.DayStatus;
import com.we.timetrack.model.Timesheet;

public class TimesheetDay {

	private static Logger logger = LoggerFactory.getLogger(TimesheetDay.class);
	
	private Day day;
	private float hours;
	
	private SortedSet<Timesheet> timesheets;

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public SortedSet<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(SortedSet<Timesheet> timesheets) {
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
	public static List<TimesheetDay> getTimesheetsByDays(List<Timesheet> timesheets, LocalDate beginDate, LocalDate endDate, List<Day> weekends){
		
		Map<LocalDate, SortedSet<Timesheet>> timesheetsByDays = new HashMap<>();
		
		// Create map by days with empty lists
		for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(1)){
			timesheetsByDays.put(date, new TreeSet<Timesheet>(new TimesheetComparator()));
		}
				
		// Fill map
		for(Timesheet timesheet : timesheets){
			SortedSet<Timesheet> timesheetList = timesheetsByDays.get(timesheet.getDateTask());
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
			Day newDay = new Day();
			newDay.setDateDay(date);
			int index = weekends.indexOf(newDay);
			if (index != -1){
				//This is weekend or short day
				newDay.setStatus(weekends.get(index).getStatus());
			} else {
				newDay.setStatus(DayStatus.Work);
			}
			logger.debug("Date {} has status {}", newDay.getDateDay(), newDay.getStatus());
			timesheetDay.setDay(newDay);
			SortedSet<Timesheet> tss = timesheetsByDays.get(date);
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
