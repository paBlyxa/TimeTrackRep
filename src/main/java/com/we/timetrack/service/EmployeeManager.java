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
	
	//���������� �� �������
	private Map<String, Float> summaryByTasks;
	//���������� �� ��������
	private Map<String, Float> summaryByProjects;
	
	/**
	 * �������� ����������
	 * @param employee
	 */
	@Transactional(readOnly=true)
	public void makeEmployeeSummary(Employee employee){
		
		//��������� ��� ������ �� ����������
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(employee);
		
		Map<String, Float> resultByTasks = new HashMap<String, Float>();
		Map<Project, Float> resultByProjects = new HashMap<Project, Float>();
		
		//�������� ���������� �� ������� � ��������
		for (Timesheet timesheet : timesheets){
			//���������� �� �������
			if (resultByTasks.containsKey(timesheet.getTask().getName())){
				resultByTasks.put(timesheet.getTask().getName(), resultByTasks.get(timesheet.getTask().getName()) + timesheet.getCountTime());
			}
			else {
				resultByTasks.put(timesheet.getTask().getName(), timesheet.getCountTime());
			}
			//���������� �� ��������
			if (resultByProjects.containsKey(timesheet.getProject())){
				resultByProjects.put(timesheet.getProject(), resultByProjects.get(timesheet.getProject()) + timesheet.getCountTime());
			}
			else {
				resultByProjects.put(timesheet.getProject(), timesheet.getCountTime());
			}
		}
		//��������� ���������� �� �������
		summaryByTasks = resultByTasks;
		//��������� ���������� �� ��������
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
