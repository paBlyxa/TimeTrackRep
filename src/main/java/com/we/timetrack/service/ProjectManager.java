package com.we.timetrack.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Department;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.ProjectInfo;
import com.we.timetrack.model.ProjectStatus;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.TaskStatus;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.EmployeeComparator;

@Service
public class ProjectManager {

	private static Logger logger = LoggerFactory.getLogger(ProjectManager.class);

	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private SessionFactory sessionFactory;

	private Map<UUID, Employee> employeeMap;

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Get project with matching projectId
	 */
	@Transactional
	public Project getProject(int projectId) {
		Project project = projectRepository.getProject(projectId);
		return project;
	}

	/**
	 * Get project with matching projectId and initialized project's leaders
	 */
	@Transactional
	public Project getProjectWithLeaders(int projectId) {
		Project project = projectRepository.getProject(projectId);
		Hibernate.initialize(project.getProjectLeaders());
		Hibernate.initialize(project.getTasks());
		return project;
	}

	/**
	 * Get task
	 * 
	 * @param model
	 */
	@Transactional
	public void getTask(Model model, int taskId) {
		Task task = getTask(taskId);
		Hibernate.initialize(task.getDepartments());
		model.addAttribute("taskStatusList", TaskStatus.values());
		model.addAttribute("taskForm", task);
		model.addAttribute("departmentList", getDepartments());
	}

	/**
	 * Get task
	 * 
	 * @param model
	 */
	@Transactional
	public Task getTask(int taskId) {
		Task task = taskRepository.getTask(taskId);
		Hibernate.initialize(task.getProjects());
		logger.debug("tasks.getProjects: {}", task.getProjects().toArray());
		return task;
	}

	/**
	 * Get all active projects
	 */
	@Transactional(readOnly = true)
	public void getActiveProjects(Model model) {
		List<Project> projects = projectRepository.getProjects(ProjectStatus.Active);
		model.addAttribute("projects", projects);
	}

	/**
	 * Get all projects
	 */
	@Transactional(readOnly = true)
	public void getProjects(Model model) {

		List<Project> projects = projectRepository.getProjects();

		Project projectForm = new Project();

		List<ProjectInfo> projectInfoList = getProjectInfoList(projects);

		Map<String, String> employeeList = getEmployees();

		model.addAttribute("statusList", ProjectStatus.values());
		model.addAttribute("projectForm", projectForm);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("projectList", projectInfoList);
	}

	/**
	 * Get all employees
	 */
	public Map<String, String> getEmployees() {
		List<Employee> employees = employeeRepository.getEmployees();
		getEmployeeMap(employees);
		Map<String, String> employeeList = new LinkedHashMap<String, String>();
		for (Employee employee : employees) {
			employeeList.put(employee.getEmployeeId().toString(), employee.getSurname() + " " + employee.getName());
		}
		return employeeList;
	}

	/**
	 * Save project
	 * 
	 * @param project
	 */
	@Transactional(readOnly = false)
	public void saveProject(Project project) {

		logger.debug("Save project: " + project.getName() + " " + project.getComment());
		projectRepository.saveProject(project);
	}

	@Transactional(readOnly = false)
	public void deleteProject(int projectId) {

		logger.debug("Delete project: " + projectId);
		Project project = new Project();
		project.setProjectId(projectId);
		projectRepository.deleteProject(project);
	}

	/**
	 * Get project, where projectLeader is current user
	 */
	@Transactional(readOnly = true)
	public void getMyProjects(Model model) {

		// Get current employee
		Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<Project> projects;

		if (employee.getAuthorities().contains(new SimpleGrantedAuthority("statistic"))) {
			logger.debug("Employee [{}] has statistic authority", employee.getShortName());
			projects = projectRepository.getProjects();
		} else {
			projects = projectRepository.getProjects(employee.getEmployeeId());
		}

		List<ProjectInfo> projectInfoList = getProjectInfoList(projects);

		model.addAttribute("projectList", projectInfoList);
	}

	/**
	 * Get project, where projectLeader is employee with matching employeeId
	 */
	@Transactional(readOnly = true)
	public List<ProjectInfo> getMyProjects(UUID employeeId) {

		List<Project> projects = projectRepository.getProjects(employeeId);

		List<ProjectInfo> projectInfoList = getProjectInfoList(projects);

		return projectInfoList;
	}

	/**
	 * Get project summary statistic
	 * 
	 * @param project
	 */
	@Transactional(readOnly = true)
	public void getProjectSummary(int projectId, DateRange period, Model model) {

		Project project = projectRepository.getProject(projectId);

		List<Timesheet> timesheets;
		if (period == null) {
			// Get all project's timesheets
			timesheets = timesheetRepository.getTimesheets(projectId);
		} else {
			// Get project's timesheets for period
			timesheets = timesheetRepository.getTimesheets(projectId, period.getBegin(), period.getEnd());

			model.addAttribute("period", period);
		}

		Map<String, Float> resultByTasks = new HashMap<String, Float>();
		Map<UUID, Float> resultByEmployees = new HashMap<UUID, Float>();

		// Make statistic
		for (Timesheet timesheet : timesheets) {
			// Statistic by tasks
			if (resultByTasks.containsKey(timesheet.getTask().getName())) {
				resultByTasks.put(timesheet.getTask().getName(),
						resultByTasks.get(timesheet.getTask().getName()) + timesheet.getCountTime());
			} else {
				resultByTasks.put(timesheet.getTask().getName(), timesheet.getCountTime());
			}
			// Statistic by employees
			if (resultByEmployees.containsKey(timesheet.getEmployeeId())) {
				resultByEmployees.put(timesheet.getEmployeeId(),
						resultByEmployees.get(timesheet.getEmployeeId()) + timesheet.getCountTime());
			} else {
				resultByEmployees.put(timesheet.getEmployeeId(), timesheet.getCountTime());
			}
		}
		// Add attribute to model
		model.addAttribute("projectSummaryByTasks", resultByTasks);
		// Get employee's names
		Map<String, Float> summaryByEmployees = new HashMap<String, Float>();
		for (Map.Entry<UUID, Float> entry : resultByEmployees.entrySet()) {
			Employee employee = employeeRepository.getEmployee(entry.getKey());
			summaryByEmployees.put(employee.getSurname() + " " + employee.getName(), entry.getValue());
		}
		model.addAttribute("projectSummaryByEmployees", summaryByEmployees);
		model.addAttribute(project);
	}

	@Transactional
	/**
	 * Get project summary statistic
	 * 
	 * @param project
	 */
	public Map<String, Float> getProjectSummary(int projectId, LocalDate beginDate, LocalDate endDate, int type,
			boolean all, List<String> items) {
		if (!all && items == null) {
			return new HashMap<String, Float>();
		}
		Criteria criteria = currentSession().createCriteria(Timesheet.class);
		switch (type) {
		case 1:
			criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("employeeId"))
					.add(Projections.sum("countTime")));
			if (items != null) {
				List<Integer> tasks = new ArrayList<>();
				for (String item : items) {
					tasks.add(Integer.parseInt(item));
				}
				criteria.add(Restrictions.in("task.taskId", tasks));
			}
			break;
		case 2:
			criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("task"))
					.add(Projections.sum("countTime")));
			if (items != null) {
				List<UUID> employees = new ArrayList<>();
				for (String item : items) {
					employees.add(UUID.fromString(item));
				}
				criteria.add(Restrictions.in("employeeId", employees));
			}
			break;
		case 3:
			criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("dateTask"))
					.add(Projections.sum("countTime")));
			criteria.addOrder(Order.asc("dateTask"));
			break;
		default:
			return new HashMap<>();
		}
		if (projectId != 0) {
			criteria.add(Restrictions.eq("project.projectId", projectId));
		}
		if (beginDate != null) {
			criteria.add(Restrictions.ge("dateTask", beginDate));
		}
		if (endDate != null) {
			criteria.add(Restrictions.le("dateTask", endDate));
		}
		@SuppressWarnings("unchecked")
		List<Object[]> result = criteria.list();
		return resultToMap(result);
	}

	@Transactional
	public Map<String, String> getItems(int projectId, DateRange period, int type) {
		switch (type) {
		case 2:
			return getEmployees(projectId, period);
		case 1:
			return getTasks(projectId, period);
		}
		return new HashMap<>();
	}

	private Map<String, String> getEmployees(int projectId, DateRange period) {
		Query query = currentSession()
				.createQuery("select distinct t.employeeId as employee " + "from Timesheet t "
						+ "where t.project.projectId = :projectId AND "
						+ "t.dateTask >= :dateBegin AND t.dateTask <= :dateEnd")
				.setParameter("projectId", projectId).setParameter("dateBegin", period.getBegin())
				.setParameter("dateEnd", period.getEnd());

		@SuppressWarnings("unchecked")
		List<UUID> employeeUUIDs = query.list();
		List<Employee> employeeList = new ArrayList<>();
		Map<String, String> result = new LinkedHashMap<>();
		for (UUID employeeId : employeeUUIDs) {
			Employee employee = employeeRepository.getEmployee(employeeId);
			employeeList.add(employee);
		}
		employeeList.sort(new EmployeeComparator());
		for (Employee employee : employeeList) {
			result.put(employee.getSurname() + " " + employee.getName(), employee.getEmployeeId().toString());
		}
		return result;
	}

	private Map<String, String> getTasks(int projectId, DateRange period) {
		Query query = currentSession()
				.createQuery("select distinct s.task from Timesheet s " + "where s.project.projectId = :projectId AND "
						+ "s.dateTask >= :dateBegin AND s.dateTask <= :dateEnd")
				.setParameter("projectId", projectId).setParameter("dateBegin", period.getBegin())
				.setParameter("dateEnd", period.getEnd());

		@SuppressWarnings("unchecked")
		List<Task> tasks = query.list();
		tasks.sort(new Comparator<Task>() {
			public int compare(Task task1, Task task2) {
				return task1.getName().compareTo(task2.getName());
			}
		});
		Map<String, String> result = new LinkedHashMap<>();
		for (Task task : tasks) {
			result.put(task.getName(), Integer.toString(task.getTaskId()));
		}
		return result;
	}

	private List<ProjectInfo> getProjectInfoList(List<Project> projects) {
		if (employeeMap == null || employeeMap.isEmpty()) {
			employeeMap = employeeRepository.getEmployeeMap();
		}
		List<ProjectInfo> projectInfoList = new ArrayList<>();
		// Convert all items of Project.class to ProjectInfo.class
		for (Project prj : projects) {
			ProjectInfo projInfo = new ProjectInfo(prj);
			Set<Employee> managers = new HashSet<Employee>();
			for (UUID employeeId : prj.getProjectLeaders()) {
				managers.add(employeeMap.get(employeeId));
			}
			projInfo.setManagers(managers);
			projectInfoList.add(projInfo);
		}
		return projectInfoList;
	}

	private Map<String, Float> resultToMap(List<Object[]> arrayList) {
		Map<String, Float> result = new LinkedHashMap<>();
		for (Object[] array : arrayList) {
			if (array.length != 2) {
				return null;
			}
			String name = "";
			if (array[0] instanceof Project) {
				name = ((Project) array[0]).getName();
			}
			if (array[0] instanceof Task) {
				name = ((Task) array[0]).getName();
			}
			if (array[0] instanceof UUID) {
				Employee employee = employeeRepository.getEmployee((UUID) array[0]);
				if (employee != null) {
					name = employee.getSurname() + " " + employee.getName();
				} else {
					logger.error("No employee with matching employeeId = [{}]", array[0]);
					name = "Unknown: " + array[0];
				}
			}
			if (array[0] instanceof LocalDate) {
				name = ((LocalDate) array[0]).toString();
			}
			result.put(name, (float) (double) array[1]);
		}
		return result;
	}

	/**
	 * Get map of uuid, employee
	 */
	private void getEmployeeMap(List<Employee> employeeList) {
		employeeMap = new HashMap<UUID, Employee>();
		for (Employee employee : employeeList) {
			employeeMap.put(employee.getEmployeeId(), employee);
		}

	}

	/**
	 * Save task with binding projects
	 * 
	 * @param taskForm
	 */
	@Transactional
	public void saveTask(Task taskForm) {

		if (taskForm.getTaskId() != null) {
			Task task = taskRepository.getTask(taskForm.getTaskId());
			Hibernate.initialize(task.getProjects());
			Hibernate.initialize(task.getDepartments());
			task.setComment(taskForm.getComment());
			task.setName(taskForm.getName());
			task.setStatus(taskForm.getStatus());
			task.getDepartments().clear();
			if (taskForm.getDepartments() != null) {
				taskForm.getDepartments().forEach(dep -> {
					task.getDepartments().add(dep);
				});
			}

			List<Project> oldPrjs = new ArrayList<>();
			for (Project prj1 : task.getProjects()) {
				boolean find = false;
				if (taskForm.getProjects() != null) {
					Iterator<Project> iterator = taskForm.getProjects().iterator();
					while (iterator.hasNext()) {
						Project prj2 = iterator.next();
						if (prj1.equals(prj2)) {
							find = true;
							iterator.remove();
							continue;
						}
					}
				}
				if (!find)
					oldPrjs.add(prj1);
			}
			if (taskForm.getProjects() != null) {
				for (Project prj : taskForm.getProjects()) {
					prj = getProject(prj.getProjectId());
					task.getProjects().add(prj);
					prj.getTasks();
					prj.getTasks().add(taskForm);
				}
			}
			for (Project prj : oldPrjs) {
				task.getProjects().remove(prj);
				prj.getTasks().remove(task);
			}
		} else {
			taskRepository.saveTask(taskForm);
		}
	}

	/**
	 * Get list of all active tasks
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Task> getActiveTasks() {

		return taskRepository.getTasks(TaskStatus.Active);
	}

	/**
	 * Get list of all departments
	 */
	@Transactional(readOnly = true)
	public List<Department> getDepartments() {
		return employeeRepository.getDepartments();
	}
}
