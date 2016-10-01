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
public class ProjectManager {
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	
	//Статистика проекта по задачам
	private Map<String, Float> summaryByTasks;
	//Статистика проекта по сотрудникам
	private Map<String, Float> summaryByEmployees;
	
	/**
	 * Создание статистики проекта
	 * @param project
	 */
	@Transactional(readOnly=true)
	public void makeProjectSummary(Project project){
		
		//Считываем все записи по проекту
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(project);
		
		Map<String, Float> resultByTasks = new HashMap<String, Float>();
		Map<Employee, Float> resultByEmployees = new HashMap<Employee, Float>();
		
		//Собираем статистику по задачам и сотрудникам
		for (Timesheet timesheet : timesheets){
			//Статистика по задачам
			if (resultByTasks.containsKey(timesheet.getTask().getName())){
				resultByTasks.put(timesheet.getTask().getName(), resultByTasks.get(timesheet.getTask().getName()) + timesheet.getCountTime());
			}
			else {
				resultByTasks.put(timesheet.getTask().getName(), timesheet.getCountTime());
			}
			//Статистика по сотрудникам
			if (resultByEmployees.containsKey(timesheet.getEmployee())){
				resultByEmployees.put(timesheet.getEmployee(), resultByEmployees.get(timesheet.getEmployee()) + timesheet.getCountTime());
			}
			else {
				resultByEmployees.put(timesheet.getEmployee(), timesheet.getCountTime());
			}
		}
		//Сохраняем статистику по задачам
		summaryByTasks = resultByTasks;
		//Сохраняем статистику по сотрдуникам (только Фамилия, имя, и количество часов)
		summaryByEmployees = new HashMap<String, Float>();
		for (Map.Entry<Employee, Float> entry : resultByEmployees.entrySet()) {
			summaryByEmployees.put(entry.getKey().getSurname() + " " + entry.getKey().getName(), entry.getValue());
		}
	}
	
	public Map<String, Float> getSummaryByTasks() {
		return summaryByTasks;
	}
	
	public Map<String, Float> getSummaryByEmployees() {
		return summaryByEmployees;
	}
}
