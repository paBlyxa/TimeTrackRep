package com.we.timetrack.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.DateRange;

@Service
public class EmployeeManager {
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	@Qualifier("ldapEmployeeRepository")
	private EmployeeRepository employeeRepository;
	
	/**
	 * Создание статистики для текущего пользователя
	 */
	public void getEmployeeSummary(DateRange period, Model model){
		//Текущий сотрудник
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		getEmployeeSummary(employee, period, model);
	}
	
	/**
	 * Отображение параметром текущего пользователя
	 * @param model
	 */
	public void getAccount(Model model){
		//Текущий сотрудник
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("employee", employee);
	}
	
	/**
	 * Создание статистики для сотрудника employeeId
	 */
	public void getEmployeeSummary(UUID employeeId, DateRange period, Model model){
		Employee employee = employeeRepository.getEmployee(employeeId);
		getEmployeeSummary(employee, period, model);
	}
	
	/**
	 * Создание статистики
	 */
	private void getEmployeeSummary(Employee employee, DateRange period, Model model){
		
		List<Timesheet> timesheets;
		
		if (period == null){
			//Считываем все записи по сотрдунику
			timesheets = timesheetRepository.getTimesheets(employee.getEmployeeId());
		} else {
			timesheets = timesheetRepository.getTimesheets(employee.getEmployeeId(), period.getBegin(), period.getEnd());
			model.addAttribute("statPeriod", period);
		}
		
		Map<String, Float> resultByTasks = new HashMap<String, Float>();
		Map<String, Float> resultByProjects = new HashMap<String, Float>();
		
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
			if (resultByProjects.containsKey(timesheet.getProject().getName())){
				resultByProjects.put(timesheet.getProject().getName(), resultByProjects.get(timesheet.getProject().getName()) + timesheet.getCountTime());
			}
			else {
				resultByProjects.put(timesheet.getProject().getName(), timesheet.getCountTime());
			}
		}
		//Сохраняем статистику по задачам
		model.addAttribute("summaryByTasks", resultByTasks);
		//Сохраняем статистику по проектам
		model.addAttribute("summaryByProjects", resultByProjects);
		model.addAttribute("employee", employee);
		
	}

	public void getEmployeeList(Model model){
		
		//Текущий сотрудник
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Employee> employeeList;
		
		if (employee.getAuthorities().contains("admin")) {
			employeeList = employeeRepository.getEmployees();
		} else {
			employeeList = employeeRepository.getDirectReports(employee);
		}
		
		model.addAttribute("employeeList", employeeList);
		
	}
}
