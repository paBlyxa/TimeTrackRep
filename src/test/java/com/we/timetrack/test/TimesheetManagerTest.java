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
import java.util.List;
import java.util.UUID;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class TimesheetManagerTest{

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
			System.out.println(">>>>1 Сотрудник: " + employeeId + " Количество часов: " + timesheet.getCountTime());
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
			System.out.println(">>>>2 Сотрудник: " + employeeId + " Количество часов: " + timesh.getCountTime());
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
			System.out.println(">>>>>3 Проект: " + project.getProjectId() + " Количество часов: " + timesh.getCountTime());
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
			System.out.println("List number " + ++countList + " Дата: " + timeshs.getDate());
			for(Timesheet timesh: timeshs.getTimesheets())
				System.out.println(">>>> Дата " + timesh.getDateTask());
		}
	}*/
}
