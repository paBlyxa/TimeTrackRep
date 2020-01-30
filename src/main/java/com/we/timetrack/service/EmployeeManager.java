package com.we.timetrack.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.we.timetrack.db.CalendarRepository;
import com.we.timetrack.db.EmployeePropertyRepository;
import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.PrivilegeRepository;
import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.RoleRepository;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Day;
import com.we.timetrack.model.DayStatus;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.EmployeeProperty;
import com.we.timetrack.model.Privilege;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Role;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.EmployeeProperty.PROPERTIES;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.EmployeePropertyForm;

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
	private EmployeeRepository employeeRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PrivilegeRepository privilegeRepository;
	@Autowired
	private EmployeePropertyRepository employeePropertyRepository;
	@Autowired
	private CalendarRepository calendarRepository;

	/**
	 * Get employee by employeeId
	 */
	public Employee getEmployee(UUID employeeId) {
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
	public Map<String, Float> getEmployeeSummary(DateRange period, int type, boolean all, List<Integer> items) {
		Employee employee = getCurrentEmployee();
		if (!all && items == null) {
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
	public Map<String, Float> getEmployeeSummary(UUID employeeId, DateRange period, int type, boolean all,
			List<Integer> items) {
		Employee employee = employeeRepository.getEmployee(employeeId);
		if (!all && items == null) {
			return new HashMap<String, Float>();
		}
		return getEmployeeSummary(employee, period, type, items);
	}

	/**
	 * Get current employee (user)
	 * 
	 * @return
	 */
	public Employee getCurrentEmployee() {
		return (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Создание статистики для сотрудника employeeId
	 */
	private Map<String, Float> getEmployeeSummary(Employee employee, DateRange period, int type, List<Integer> items) {
		switch (type) {
		case 1:
			return timesheetRepository.getEmployeeSummaryByProjects(employee.getEmployeeId(), period.getBegin(),
					period.getEnd(), createTaskList(items));
		case 2:
			return timesheetRepository.getEmployeeSummaryByTasks(employee.getEmployeeId(), period.getBegin(),
					period.getEnd(), createProjectList(items));
		case 3:
			return timesheetRepository.getEmployeeSummaryByTime(employee.getEmployeeId(), period.getBegin(),
					period.getEnd());
		}
		return new HashMap<>();
	}

	/**
	 * Получение списка подчиненных сотрудников. Если пользователь имеет роль "Timex
	 * статисты" список содержит всех сотрудников.
	 * 
	 * @param model Список сохраняется в модель.
	 */
	public void getEmployeeList(Model model) {

		Employee employee = getCurrentEmployee();

		List<Employee> employeeList;

		if (employee.getAuthorities().contains(new SimpleGrantedAuthority("statistic"))) {
			logger.debug("Employee [{}] has statistic authority", employee.getShortName());
			employeeList = employeeRepository.getEmployees();
		} else {
			employeeList = employeeRepository.getDirectReports(employee);
		}

		model.addAttribute("employeeList", employeeList);

	}

	public Map<String, Integer> getItems(DateRange period, Integer type) {
		switch (type) {
		case 1:
			return getTasks(period);
		case 2:
			return getProjects(period);
		}
		return new HashMap<>();
	}

	private Map<String, Integer> getProjects(DateRange period) {
		List<Project> projects;
		if (period == null) {
			projects = projectRepository.getProjects();
		} else {
			projects = projectRepository.getProjects();
		}
		Map<String, Integer> result = new LinkedHashMap<>();
		for (Project proj : projects) {
			result.put(proj.getName(), proj.getProjectId());
		}
		return result;
	}

	private Map<String, Integer> getTasks(DateRange period) {
		List<Task> tasks;
		if (period == null) {
			tasks = taskRepository.getTasks();
		} else {
			tasks = taskRepository.getTasks();
		}
		Map<String, Integer> result = new LinkedHashMap<>();
		for (Task task : tasks) {
			result.put(task.getName(), task.getTaskId());
		}
		return result;
	}

	private List<Task> createTaskList(List<Integer> listTaskId) {
		if (listTaskId == null) {
			return null;
		}
		List<Task> tasks = new ArrayList<Task>();
		for (Integer taskId : listTaskId) {
			Task task = new Task();
			task.setTaskId(taskId);
			tasks.add(task);
		}
		return tasks;
	}

	private List<Project> createProjectList(List<Integer> listProjectId) {
		if (listProjectId == null) {
			return null;
		}
		List<Project> projects = new ArrayList<Project>();
		for (Integer projectId : listProjectId) {
			Project project = new Project();
			project.setProjectId(projectId);
			projects.add(project);
		}
		return projects;
	}

	/**
	 * Get employees, who has empty days.
	 * 
	 * @param dateRange
	 * @return
	 */
	public Map<Employee, List<LocalDate>> getNotReadyEmployees(DateRange dateRange) {
		// 1 Get all active employees
		List<Employee> employees = employeeRepository.getActiveEmployees();
		// 2 Get weekends
		List<Day> weekends = calendarRepository.getDays(dateRange.getBegin(), dateRange.getEnd());
		// 3 Create result map
		Map<Employee, List<LocalDate>> result = new HashMap<>();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// 4 Foreach employee 
		for (Employee employee : employees) {
			// 4.1 Get summary by time
			Map<String, Float> summary = timesheetRepository.getEmployeeSummaryByTime(employee.getEmployeeId(), dateRange.getBegin(),
					dateRange.getEnd());
			// 4.2 Create list result for employee
			List<LocalDate> list = new ArrayList<>();
			// 4.3 Foreach day check weekends and summary hours
			for (LocalDate date = dateRange.getBegin(); !date.isAfter(dateRange.getEnd()); date = date.plusDays(1)){
				Day newDay = new Day();
				newDay.setDateDay(date);
				int index = weekends.indexOf(newDay);
				if (index != -1) {
					//This is weekend or short day
					newDay.setStatus(weekends.get(index).getStatus());
					if (newDay.getStatus().equals(DayStatus.Weekend)) {
						// Weekends not to check
						continue;
					}
				} else {
					newDay.setStatus(DayStatus.Work);
				}
				Float countTime = summary.get(newDay.getDateDay().format(dateFormat));
				if (countTime == null || countTime < newDay.getStatus().getWorkingHours()) {
					list.add(date);
				}
			}
			// 4.4 Put all in result
			result.put(employee, list);
			
		}
		return result;
	}

	/**
	 * Get list of Employee with initialized properties.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<EmployeePropertyForm> getEmployeesWithProperties() {
		// Get all empoyees
		List<Employee> employees = employeeRepository.getEmployees();
		List<EmployeePropertyForm> result = new ArrayList<>();
		// Initialize properties and managers
		for (Employee employee : employees) {
			Hibernate.initialize(employee.getProperties());
			Hibernate.initialize(employee.getRoles());
			result.add(new EmployeePropertyForm(employee));
		}
		return result;
	}
	
	/**
	 * Get list of Roles.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		roleRepository.getRoles().forEach(role -> roles.add(role.getName()));
		return roles;
	}	
	
	/**
	 * Save roles for employee
	 * @param employeeForm
	 */
	@Transactional
	public void saveRoles(EmployeePropertyForm employeeForm) {
		Employee employee = employeeRepository.getEmployee(employeeForm.getEmployeeId());
		Iterator<Role> iterator = employee.getRoles().iterator();
		while (iterator.hasNext()) {
			Role role = iterator.next();
			boolean find = false;
			Iterator<String> iterator2 = employeeForm.getRoles().iterator();
			while (iterator2.hasNext()) {
				String str = iterator2.next();
				if (role.getName().equals(str)) {
					find = true;
					iterator2.remove();
					continue;
				}
			}
			if (!find) {
				iterator.remove();
			}
		}
		List<Role> allRoles = roleRepository.getRoles();
		for (String str :  employeeForm.getRoles()) {
			for (Role role : allRoles) {
				if (str.equals(role.getName())) {
					employee.getRoles().add(role);
					continue;
				}
			}
		}
	}
	
	/**
	 * Get list of Privileges.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> getPrivileges() {
		List<String> privileges = new ArrayList<>();
		privilegeRepository.getPrivileges().forEach(privilege -> privileges.add(privilege.getName()));
		return privileges;
	}

	/**
	 * Save new privilege
	 * @param name
	 */
	@Transactional
	public void addPrivilege(String name) {
		Privilege privilege = new Privilege();
		privilege.setName(name);
		privilegeRepository.savePrivilege(privilege);
	}
	
	/**
	 * Delete privileges
	 * @param privileges
	 */
	@Transactional
	public List<String> deletePrivilege(String[] privilegesToDelete) {
		List<Privilege> privileges = privilegeRepository.getPrivileges();
		for (String str : privilegesToDelete) {
			boolean find = false;
			for (Privilege privilege : privileges) {
				if (privilege.getName().equals(str)) {
					find = true;
					privilegeRepository.deletePrivilege(privilege);
					continue;
				}
			}
			if (!find) {
				logger.warn("Privilege \"{}\" doesn't exist, can't delete", str);
			}
		}
		return getPrivileges();
	}
	
	/**
	 * Save new role
	 * @param name
	 */
	@Transactional
	public void addRole(String name) {
		Role role = new Role();
		role.setName(name);
		roleRepository.saveRole(role);
	}
	
	/**
	 * Delete roles
	 * @param rolesToDelete
	 */
	@Transactional
	public List<String> deleteRoles(String[] rolesToDelete) {
		List<Role> roles = roleRepository.getRoles();
		for (String str : rolesToDelete) {
			boolean find = false;
			for (Role role : roles) {
				if (role.getName().equals(str)) {
					find = true;
					roleRepository.deleteRole(role);
					continue;
				}
			}
			if (!find) {
				logger.warn("Role \"{}\" doesn't exist, can't delete", str);
			}
		}
		return getRoles();
	}
	
	/**
	 * Get list of Privileges for role.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> getPrivileges(String roleName) {
		Role role = roleRepository.getRole(roleName);
		List<String> privileges = new ArrayList<>();
		if (role == null) {
			logger.error("Role \"{}\" doesn't exist", roleName);
			return privileges;
		}
		for (Privilege privilege : role.getPrivileges()) {
			privileges.add(privilege.getName());
		}
		return privileges;
	}

	/**
	 * Save privileges for role
	 * @param roleName
	 * @param privilegeNames
	 */
	@Transactional
	public String saveRolePrivileges(String roleName, String[] privilegeNames) {
		Role role = roleRepository.getRole(roleName);
		if (role == null) {
			logger.error("Role \"{}\" doesn't exist", roleName);
			return roleName + " doesn't exist";
		}
		Iterator<Privilege> iterator = role.getPrivileges().iterator();  
		while (iterator.hasNext()) {
			Privilege privilege = iterator.next();
			boolean find = false;
			for (int i = 0; i < privilegeNames.length; i++) {
				if (privilegeNames[i].equals(privilege.getName())){
					find = true;
					privilegeNames[i] = "";
					continue;
				}
			}
			if (!find) {
				iterator.remove();
			}
		}
		
		List<Privilege> allPrivileges = privilegeRepository.getPrivileges();
		for (String str : privilegeNames) {
			if (str.length() > 0) {
				for (Privilege p : allPrivileges) {
					if (str.equals(p.getName())) {
						role.getPrivileges().add(p);
					}
					continue;
				}
			}
		}
		return "success";
	}

	/**
	 * Get property from employee
	 * @param employee
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getProperty(Employee employee, PROPERTIES property) {
		employee = employeeRepository.getEmployee(employee.getEmployeeId());
		for (EmployeeProperty prop : employee.getProperties()) {
			if (prop.getName().equals(property.name())){
				return prop.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Save property for employee
	 * @param employeeId
	 * @param propertyName
	 * @param propertyValue
	 */
	@Transactional
	public void saveProperty(UUID employeeId, String propertyName, String propertyValue) {
			
		Employee employee = employeeRepository.getEmployee(employeeId);
		
		for (EmployeeProperty prop : employee.getProperties()) {
			if (prop.getName().equals(propertyName)) {
				prop.setValue(propertyValue);
				return;
			}
		}
		EmployeeProperty property = new EmployeeProperty();
		property.setName(propertyName);
		property.setValue(propertyValue);
		employeePropertyRepository.save(property);
		employee.getProperties().add(property);
	}
}
