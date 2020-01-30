package com.we.timetrack.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles({"dataSourceProduction", "LdapAndDBEmployees"})
public class TimesheetRepositoryTest{

	private final static UUID EMPLOYEE_ID = UUID.fromString("12a24625-9f35-4d33-f7af-0ad444f9e5ee");
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	
	@Test
	@Transactional
	public void testGetByEmployeeId(){
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		assertNotNull(timesheetList);
		assertTrue(timesheetList.size() > 0);
		
		UUID employeeId = timesheetList.get(1).getEmployeeId();
		timesheetList = timesheetRepository.getTimesheets(employeeId);
		assertNotNull(timesheetList);
		
		for (Timesheet timesheet : timesheetList){
			assertEquals(employeeId, timesheet.getEmployeeId());
			System.out.println(">>>>1 EmployeeId: " + employeeId + " countTime: " + timesheet.getCountTime());
		}
	}
	
	@Test
	@Transactional
	public void testGetByEmployeeIdAndPeriod() {
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		if (timesheetList == null || timesheetList.size() < 1) return;
		
		Timesheet timesheet = (Timesheet) timesheetList.get(0);
		UUID employeeId = timesheet.getEmployeeId();
        LocalDate dateTask = timesheet.getDateTask();
        List<Timesheet> timesheets = timesheetRepository.getTimesheets(employeeId,dateTask);
        assertNotNull(timesheets);
		
		for (Timesheet timesh : timesheets){
			assertEquals(employeeId, timesh.getEmployeeId());
			System.out.println(">>>>2   EmployeeId: " + employeeId + " countTime: " + timesh.getCountTime());
		}
	}
	
	@Test
	@Transactional
	public void testGetByProjectId(){
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		if (timesheetList == null || timesheetList.size() < 1) return;
		
		Timesheet timesheet = (Timesheet) timesheetList.get(0);
		Project project = timesheet.getProject();
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(project.getProjectId());
		assertNotNull(timesheets);
		
		for (Timesheet timesh : timesheets){
			assertEquals(project.getProjectId(), timesh.getProject().getProjectId());
			System.out.println(">>>>>3  ProjectId: " + project.getProjectId() + " countTime: " + timesh.getCountTime());
		}
	}

	@Test
	@Transactional
	public void testSaveSingle(){
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		if (timesheetList == null || timesheetList.size() < 1) return;		
		
        LocalDate dateTask = LocalDate.of(2016, 9, 2);
        
        Timesheet timesheet = (Timesheet) timesheetList.get(0);
        UUID employeeId = timesheet.getEmployeeId();
        Project project = timesheet.getProject();
        Task task = timesheet.getTask();
        
        List<Timesheet> timesheets = timesheetRepository.getTimesheets(employeeId, dateTask);
        
        //boolean newRecord = false;
        int size =  timesheets.size();
        if (timesheets == null || size < 1){
            timesheet = new Timesheet();
            timesheet.setEmployeeId(employeeId);
            timesheet.setDateTask(dateTask);
            //newRecord = true;
        }
        else{
            System.out.println("4Timesheet record found: "
                    + timesheet.getId());
        }
        
        timesheet.setCountTime(7);
        timesheet.setProject(project);
        timesheet.setTask(task);
        timesheetRepository.saveTimesheet(timesheet);

        List<Timesheet> timesheetsInDb = timesheetRepository.getTimesheets(employeeId, dateTask);
        
        assertNotNull(timesheetsInDb);
        assertTrue(timesheetsInDb.size() == (size + 1));
        
        timesheetRepository.deleteTimesheet(timesheet);
        timesheetsInDb = timesheetRepository.getTimesheets(employeeId, dateTask);
        
        assertTrue(timesheetsInDb.size() == size);
	}
	
/*	@Test
	@Transactional
	public void testGetTimesheetsByDays(){
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		if (timesheetList == null || timesheetList.size() < 1) return;	
		
		Timesheet timesheet = (Timesheet) timesheetList.get(1);
        Employee employee = timesheet.getEmployee();
        
        timesheetManager.getTimesheetsByDays(1, model);
        assertNotNull(timesheetsByDays);
		
        int countList = 0;
		for (TimesheetDay timeshs : timesheetsByDays){
			System.out.println("List number " + ++countList + " date: " + timeshs.getDate());
			for(Timesheet timesh: timeshs.getTimesheets())
				System.out.println(">>>> date " + timesh.getDateTask());
		}
	}*/
	
	@Test
	@Transactional
	public void testGetTimesheets() {
		LocalDate endDate = LocalDate.now();
		LocalDate beginDate = endDate.minusMonths(12);
		List<Integer> projects = new ArrayList<>();
		projects.add(1);
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets(EMPLOYEE_ID, beginDate, endDate, projects);
		print(timesheetList);
	}
	
	@Test
	@Transactional
	public void testGetEmployeeSummaryByProjects() {
		LocalDate endDate = LocalDate.now();
		LocalDate beginDate = endDate.minusMonths(12);
		List<Task> tasks = new ArrayList<>();
		Task task = new Task();
		task.setTaskId(2);
		tasks.add(task);
		Map<String, Float> result = timesheetRepository.getEmployeeSummaryByProjects(
				EMPLOYEE_ID, beginDate, endDate, tasks);
		result.forEach((str, count) -> {System.out.println(">>>>" + str + ": " + count);});
	}
	
	@Test
	@Transactional
	public void testGetEmployeeSummaryByTime() {
		LocalDate endDate = LocalDate.now();
		LocalDate beginDate = endDate.minusMonths(1);
		
		Map<String, Float> result = timesheetRepository.getEmployeeSummaryByTime(
				EMPLOYEE_ID, beginDate, endDate);
		result.forEach((str, count) -> {System.out.println(">>>>" + str + ": " + count);});
	}
	
	private static void print(Timesheet timesheet){
		System.out.println("Id = " + timesheet.getId() + " Проект: " + timesheet.getProject().getName() +
				" Задача: " + timesheet.getTask().getName() + " Сотрудник: " + timesheet.getEmployeeId());
	}
	
	private static void print(List<Timesheet> timesheetList){
		for (Timesheet timesheet : timesheetList){
			print(timesheet);
		}
	}
}
