package com.we.timetrack.db;

import java.time.LocalDate;
import java.util.List;

import com.we.timetrack.model.Day;

public interface CalendarRepository {

	/**
	 * Return day with mathichg date.
	 * @param date
	 * @return
	 */
	public Day getDay(LocalDate date); 
	
	/**
	 * Return list of all days.
	 * @return list of days
	 */
	public List<Day> getDays();
	
	/**
	 * Return list of all days later than beginDate and early then endDate.
	 * @param beginDate
	 * @param endDate
	 * @return list of days
	 */
	public List<Day> getDays(LocalDate beginDate, LocalDate endDate);
	
	/**
	 * Return list of weekend's days.
	 * @return
	 */
	public List<Day> getWeekends();
	
	/**
	 * Return list of weekend's days later than beginDate and early then endDate.
	 * @return
	 */
	public List<Day> getWeekends(LocalDate beginDate, LocalDate endDate);	
	
	/**
	 * Return list of weekend's days in year.
	 * @param year - year of query
	 * @return
	 */
	public List<Day> getWeekends(int year);	
	
	/**
	 * Return list of short days.
	 * @return
	 */
	public List<Day> getShortDays();
	
	/**
	 * Return list of short days later than beginDate and early then endDate.
	 * @return
	 */
	public List<Day> getShortDays(LocalDate beginDate, LocalDate endDate);	
	
	/**
	 * Return list of short days in year.
	 * @param year - year of query
	 * @return
	 */
	public List<Day> getShortDays(int year);	
	
	/**
	 * Saves a Day's object.
	 * @param day
	 */
	public void saveDay(Day day);

	/**
	 * Saves a list of Day's objects.
	 */
	public void saveDays(List<Day> days);
	
	/**
	 * Remove a Day's object.
	 */
	public void remove(Day day);
}
