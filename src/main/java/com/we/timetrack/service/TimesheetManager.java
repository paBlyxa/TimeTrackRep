package com.we.timetrack.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Timesheet;

@Service
public class TimesheetManager {

	@Autowired
	private TimesheetRepository timesheetRepository;
	
	/**
	 * Returns a database Timesheets records with matching EmployeeId
	 * and later than (dateTask - week) ordered by days
	 */
	@Transactional(readOnly=true)
	public List<TimesheetDay> getTimesheetsByDays(Employee employee, Date dateTask){
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dateTask);
		gc.add(GregorianCalendar.DAY_OF_YEAR, -6);
		
		Date dateEarly = gc.getTime();
		
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(employee, dateEarly);
		//System.out.println(">>>>>>>Timesheets.size = " + timesheets.size());

		GregorianCalendar date = new GregorianCalendar();
		List<TimesheetDay> timesheetsByDays = new ArrayList<TimesheetDay>();
		for(int i = 0; i < 7; i++){

			List<Timesheet> timesheetsByDay = new ArrayList<Timesheet>();
			float countHours = 0;
			
			for (Timesheet timesheet: timesheets){
				date.setTime(timesheet.getDateTask());
				if ((gc.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) &&
						(gc.get(Calendar.YEAR) == date.get(Calendar.YEAR))){
						timesheetsByDay.add(timesheet);
						countHours += timesheet.getCountTime();
						//timesheets.remove(timesheet);
				}
			}
			
			if (timesheetsByDay.size() < 1){
				timesheetsByDay.add(new Timesheet());
			}
			
			TimesheetDay timesheetDay = new TimesheetDay();
			timesheetDay.setDate(gc.getTime());
			timesheetDay.setTimesheets(timesheetsByDay);
			timesheetDay.setHours(countHours);
			timesheetsByDays.add(timesheetDay);
			gc.add(GregorianCalendar.DAY_OF_YEAR, 1);
		}
		
		return timesheetsByDays;
	}
}
