package com.we.timetrack.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Timesheet;

@Service
public class EmployeeManager {
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	
	//Статистика по задачам
	private Map<String, Float> summaryByTasks;
	//Статистика по проектам
	private Map<String, Float> summaryByProjects;
	
	/**
	 * Создание статистики
	 * @param employee
	 */
	@Transactional(readOnly=true)
	public void makeEmployeeSummary(Employee employee){
		
		//Считываем все записи по сотрдунику
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(employee);
		
		Map<String, Float> resultByTasks = new HashMap<String, Float>();
		Map<Project, Float> resultByProjects = new HashMap<Project, Float>();
		
		//Собираем статистику по задачам и проектам
		for (Timesheet timesheet : timesheets){
			//Статистика по задачам
			if (resultByTasks.containsKey(timesheet.getTask().getName())){
				resultByTasks.put(timesheet.getTask().getName(), resultByTasks.get(timesheet.getTask().getName()) + timesheet.getCountTime());
			}
			else {
				resultByTasks.put(timesheet.getTask().getName(), timesheet.getCountTime());
			}
			//Статистика по проектам
			if (resultByProjects.containsKey(timesheet.getProject())){
				resultByProjects.put(timesheet.getProject(), resultByProjects.get(timesheet.getProject()) + timesheet.getCountTime());
			}
			else {
				resultByProjects.put(timesheet.getProject(), timesheet.getCountTime());
			}
		}
		//Сохраняем статистику по задачам
		summaryByTasks = resultByTasks;
		//Сохраняем статистику по проектам
		summaryByProjects = new HashMap<String, Float>();
		for (Map.Entry<Project, Float> entry : resultByProjects.entrySet()) {
			summaryByProjects.put(entry.getKey().getName(), entry.getValue());
		}
	}
	
	public Map<String, Float> getSummaryByTasks() {
		return summaryByTasks;
	}
	
	public Map<String, Float> getSummaryByProjects() {
		return summaryByProjects;
	}
}
