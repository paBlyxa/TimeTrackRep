package com.we.timetrack.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.service.model.DateRange;

@Service
public class EmployeeManager {
	
	private final static Logger logger = LoggerFactory.getLogger(EmployeeManager.class);
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	@Qualifier("ldapEmployeeRepository")
	private EmployeeRepository employeeRepository;
	
	/**
	 * Get employee by employeeId
	 */
	public Employee getEmployee(UUID employeeId){
		return employeeRepository.getEmployee(employeeId);
	}
	
	/**
	 * Создание статистики для текущего пользователя
	 */
	public Map<String, Float> getEmployeeSummary(DateRange period, int type) {
		return getEmployeeSummary(period, type, true, null);
	}
			
	/**
	 * Создание статистики для текущего пользователя
	 */
	public Map<String, Float> getEmployeeSummary(DateRange period, int type, boolean all, List<Integer> items){
		Employee employee = getCurrentEmployee();
		if (!all && items == null){
			return new HashMap<String, Float>();
		}
		return getEmployeeSummary(employee, period, type, items);
	}
	
	/**
	 * Создание статистики для сотрудника employeeId
	 */
	public Map<String, Float> getEmployeeSummary(UUID employeeId, DateRange period, int type) {
		return getEmployeeSummary(employeeId, period, type, true, null);
	}
			
	/**
	 * Создание статистики для сотрудника employeeId
	 */
	public Map<String, Float> getEmployeeSummary(UUID employeeId, DateRange period, int type, boolean all, List<Integer> items){
		Employee employee = employeeRepository.getEmployee(employeeId);
		if (!all && items == null){
			return new HashMap<String, Float>();
		}
		return getEmployeeSummary(employee, period, type, items);
	}
	
	/**
	 * Get current employee (user)
	 * @return
	 */
	public Employee getCurrentEmployee(){
		return (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/**
	 * Создание статистики для сотрудника employeeId
	 */
	private Map<String, Float> getEmployeeSummary(Employee employee, DateRange period, int type, List<Integer> items){
		switch(type){
		case 1:
			return timesheetRepository.getEmployeeSummaryByProjects(
					employee.getEmployeeId(), period.getBegin(), period.getEnd(), createTaskList(items));
		case 2:
			return timesheetRepository.getEmployeeSummaryByTasks(
					employee.getEmployeeId(), period.getBegin(), period.getEnd(), createProjectList(items));
		case 3:
			return timesheetRepository.getEmployeeSummaryByTime(employee.getEmployeeId(), period.getBegin(), period.getEnd());
		}
		return new HashMap<>();
	}
	
	/**
	 * Получение списка подчиненных сотрудников.
	 * Если пользователь имеет роль "Timex статисты" список
	 * содержит всех сотрудников.
	 * 
	 * @param model
	 * 			Список сохраняется в модель.
	 */
	public void getEmployeeList(Model model){
		
		Employee employee = getCurrentEmployee();
		
		List<Employee> employeeList;
		
		if (employee.getAuthorities().contains(new SimpleGrantedAuthority("Timex статисты"))) {
			logger.debug("Employee [{}] has Timex статисты authority", employee.getShortName());
			employeeList = employeeRepository.getEmployees();
		} else {
			employeeList = employeeRepository.getDirectReports(employee);
		}
		
		model.addAttribute("employeeList", employeeList);
		
	}
	
	public Map<String, Integer> getItems(DateRange period, Integer type){
		
		//Employee employee = getCurrentEmployee();
		
		switch(type){
			case 1: 
				return getTasks(period);
			case 2:
				return getProjects(period);
		}
		return new HashMap<>();
	}
	
	private Map<String, Integer> getProjects(DateRange period){
		List<Project> projects;
		if (period == null){
			projects = projectRepository.getProjects();
		} else {
			projects = projectRepository.getProjects();
		}
		Map<String, Integer> result = new LinkedHashMap<>();
		for (Project proj : projects){
			result.put(proj.getName(), proj.getProjectId());
		}
		return result;
	}
	
	private Map<String, Integer> getTasks(DateRange period){
		List<Task> tasks;
		if (period == null){
			tasks = taskRepository.getTasks();
		} else {
			tasks = taskRepository.getTasks();
		}
		Map<String, Integer> result = new LinkedHashMap<>();
		for (Task task : tasks){
			result.put(task.getName(), task.getTaskId());
		}
		return result;
	}
	
	private List<Task> createTaskList(List<Integer> listTaskId){
		if (listTaskId == null) {
			return null;
		}
		List<Task> tasks = new ArrayList<Task>();
		for (Integer taskId : listTaskId){
			Task task = new Task();
			task.setTaskId(taskId);
			tasks.add(task);
		}
		return tasks;
	}
	
	private List<Project> createProjectList(List<Integer> listProjectId){
		if (listProjectId == null) {
			return null;
		}
		List<Project> projects = new ArrayList<Project>();
		for (Integer projectId : listProjectId){
			Project project = new Project();
			project.setProjectId(projectId);
			projects.add(project);
		}
		return projects;
	}
	
	/**
	 * Создание статистики
	 */
/*	private Map<String, Float> getEmployeeSummaryOld(Employee employee, DateRange period, int type, List<Integer> items){
		
		List<Timesheet> timesheets;
		
		if (period == null){
			//Считываем все записи по сотрдунику
			timesheets = timesheetRepository.getTimesheets(employee.getEmployeeId());
		} else {
			timesheets = timesheetRepository.getTimesheets(employee.getEmployeeId(), period.getBegin(), period.getEnd(), items);
			
		}
		
		Map<String, Float> result = new HashMap<String, Float>();
		
		//Собираем статистику
		for (Timesheet timesheet : timesheets){
			String key = null;
			switch (type){
				case 1: 
					key = timesheet.getProject().getName();
					break;
				case 2:
					key = timesheet.getTask().getName();
					break;
			}
			if (result.containsKey(key)){
				result.put(key, result.get(key) + timesheet.getCountTime());
			}
			else {
				result.put(key, timesheet.getCountTime());
			}

		}
		return result;
	}
*/
}
