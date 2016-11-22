package com.we.timetrack.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.ProjectInfo;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.DateRange;

@Service
public class ProjectManager {
	
	private static Logger logger = Logger.getLogger(ProjectManager.class);
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	@Qualifier("ldapEmployeeRepository")
	private EmployeeRepository employeeRepository;
	
	/**
	 * ���� ������ ��� �������� ��������
	 */
	@Transactional(readOnly=true)
	public void getProjects(Model model){
		
		List<Project> projects = projectRepository.getProjects();
		List<Employee> employees = employeeRepository.getEmployees();
		Map<String, String> employeeList = new HashMap<String, String>();
		for (Employee employee : employees){
			employeeList.put(employee.getEmployeeId().toString(), employee.getSurname() + " " + employee.getName());
		}
		Project projectForm = new Project();
		
		List<ProjectInfo> projectInfoList = getProjectInfoList(projects);
		
		model.addAttribute("projectForm", projectForm);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("projectList", projectInfoList);
	}
	
	/**
	 * Save project
	 * @param project
	 */
	@Transactional(readOnly=false)
	public void saveProject(Project project){
		
		logger.debug("Save project: " + project.getName() + " " + project.getComment());
		projectRepository.saveProject(project);
	}
	
	@Transactional(readOnly=false)
	public void deleteProject(int projectId){
		
		logger.debug("Delete project: " + projectId);
		Project project = new Project();
		project.setProjectId(projectId);
		projectRepository.deleteProject(project);
	}
	
	/**
	 * ���� ������ ��� �������� ��� �������
	 */
	@Transactional(readOnly=true)
	public void getMyProjects(Model model){
		
		//������� ���������
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<? extends GrantedAuthority> auths = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		
		for (GrantedAuthority auth : auths){
			System.out.println(auth.getAuthority());
		}
		List<Project> projects = projectRepository.getProjects(employee.getEmployeeId());
		
		List<ProjectInfo> projectInfoList = getProjectInfoList(projects);

		model.addAttribute("projectList", projectInfoList);
	}
	
	/**
	 * ���� ������ ��� �������� ��� �������
	 */
	@Transactional(readOnly=true)
	public List<ProjectInfo> getMyProjects(UUID employeeId){
		
		List<Project> projects = projectRepository.getProjects(employeeId);
		
		List<ProjectInfo> projectInfoList = getProjectInfoList(projects);
		
		return projectInfoList;
	}	
	
	/**
	 * �������� ���������� �������
	 * @param project
	 */
	@Transactional(readOnly=true)
	public void getProjectSummary(int projectId, DateRange period, Model model){
		
		Project project = projectRepository.getProject(projectId);
		
		List<Timesheet> timesheets;
		if (period == null){
			//��������� ��� ������ �� �������
			timesheets = timesheetRepository.getTimesheets(projectId);
		} else {
			//��������� ������ �� ������� �� �����
			timesheets = timesheetRepository.getTimesheets(projectId, period.getBegin(), period.getEnd());

			model.addAttribute("period", period);
		}
		
		Map<String, Float> resultByTasks = new HashMap<String, Float>();
		Map<UUID, Float> resultByEmployees = new HashMap<UUID, Float>();
		
		//�������� ���������� �� ������� � �����������
		for (Timesheet timesheet : timesheets){
			//���������� �� �������
			if (resultByTasks.containsKey(timesheet.getTask().getName())){
				resultByTasks.put(timesheet.getTask().getName(), resultByTasks.get(timesheet.getTask().getName()) + timesheet.getCountTime());
			}
			else {
				resultByTasks.put(timesheet.getTask().getName(), timesheet.getCountTime());
			}
			//���������� �� �����������
			if (resultByEmployees.containsKey(timesheet.getEmployeeId())){
				resultByEmployees.put(timesheet.getEmployeeId(), resultByEmployees.get(timesheet.getEmployeeId()) + timesheet.getCountTime());
			}
			else {
				resultByEmployees.put(timesheet.getEmployeeId(), timesheet.getCountTime());
			}
		}
		//��������� ���������� �� �������
		model.addAttribute("projectSummaryByTasks", resultByTasks);
		//��������� ���������� �� ����������� (������ �������, ���, � ���������� �����)
		Map<String, Float> summaryByEmployees = new HashMap<String, Float>();
		for (Map.Entry<UUID, Float> entry : resultByEmployees.entrySet()) {
			Employee employee = employeeRepository.getEmployee(entry.getKey());
			summaryByEmployees.put(employee.getSurname() + " " + employee.getName(), entry.getValue());
		}
		model.addAttribute("projectSummaryByEmployees", summaryByEmployees);
		model.addAttribute(project);	
	}
	
	
	private List<ProjectInfo> getProjectInfoList(List<Project> projects){
		List<ProjectInfo> projectInfoList = new ArrayList<>();
		//������������� ������� ����������� �� ��������
		for(Project prj : projects){
			ProjectInfo projInfo = new ProjectInfo(prj);
			Set<Employee> managers = new HashSet<Employee>();
			for (UUID employeeId : prj.getProjectLeaders()){
				managers.add(employeeRepository.getEmployee(employeeId));
			}
			projInfo.setManagers(managers);
			projectInfoList.add(projInfo);
		}
		return projectInfoList;
	}

}
