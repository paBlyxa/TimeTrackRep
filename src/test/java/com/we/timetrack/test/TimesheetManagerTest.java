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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.*;
import com.we.timetrack.service.TimesheetDay;
import com.we.timetrack.service.TimesheetManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class TimesheetManagerTest{

	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	private TimesheetManager timesheetManager;
	
	@Test
	@Transactional
	public void testGetByEmployeeId(){
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		assertNotNull(timesheetList);
		assertTrue(timesheetList.size() > 0);
		
		Employee employee = timesheetList.get(1).getEmployee();
		timesheetList = timesheetRepository.getTimesheets(employee);
		assertNotNull(timesheetList);
		
		for (Timesheet timesheet : timesheetList){
			assertEquals(employee.getEmployeeId(), timesheet.getEmployee().getEmployeeId());
			System.out.println(">>>>1 Сотрудник: " + employee.getEmployeeId() + " Количество часов: " + timesheet.getCountTime());
		}
	}
	
	@Test
	@Transactional
	public void testGetByEmployeeIdAndPeriod() {
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		if (timesheetList == null || timesheetList.size() < 1) return;
		
		Timesheet timesheet = (Timesheet) timesheetList.get(0);
        Employee employee = timesheet.getEmployee();
        Date dateTask = timesheet.getDateTask();
        List<Timesheet> timesheets = timesheetRepository.getTimesheets(employee,dateTask);
        assertNotNull(timesheets);
		
		for (Timesheet timesh : timesheets){
			assertEquals(employee.getEmployeeId(), timesh.getEmployee().getEmployeeId());
			System.out.println(">>>>2 Сотрудник: " + employee.getEmployeeId() + " Количество часов: " + timesh.getCountTime());
		}
	}
	
	@Test
	@Transactional
	public void testGetByProjectId(){
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		if (timesheetList == null || timesheetList.size() < 1) return;
		
		Timesheet timesheet = (Timesheet) timesheetList.get(0);
		Project project = timesheet.getProject();
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(project);
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
		
        GregorianCalendar gc = new GregorianCalendar(2016, Calendar.APRIL, 29);
        Date dateTask = gc.getTime();
        
        Timesheet timesheet = (Timesheet) timesheetList.get(0);
        Employee employee = timesheet.getEmployee();
        Project project = timesheet.getProject();
        Task task = timesheet.getTask();
        
        List<Timesheet> timesheets = timesheetRepository.getTimesheets(employee, dateTask);
        
        //boolean newRecord = false;
        int size =  timesheets.size();
        if (timesheets == null || size < 1){
            timesheet = new Timesheet();
            timesheet.setEmployee(employee);
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

        List<Timesheet> timesheetsInDb = timesheetRepository.getTimesheets(employee, dateTask);
        
        assertNotNull(timesheetsInDb);
        assertTrue(timesheetsInDb.size() == (size + 1));
        
        timesheetRepository.deleteTimesheet(timesheet);
        timesheetsInDb = timesheetRepository.getTimesheets(employee, dateTask);
        
        assertTrue(timesheetsInDb.size() == size);
	}
	
	@Test
	@Transactional
	public void testGetTimesheetsByDays(){
		
		List<Timesheet> timesheetList = timesheetRepository.getTimesheets();
		if (timesheetList == null || timesheetList.size() < 1) return;	
		
		Timesheet timesheet = (Timesheet) timesheetList.get(1);
        Employee employee = timesheet.getEmployee();
        
        GregorianCalendar gc = new GregorianCalendar();
        
        Date dateTask = gc.getTime();
        List<TimesheetDay> timesheetsByDays = timesheetManager.getTimesheetsByDays(employee,dateTask);
        assertNotNull(timesheetsByDays);
		
        int countList = 0;
		for (TimesheetDay timeshs : timesheetsByDays){
			System.out.println("List number " + ++countList + " Дата: " + timeshs.getDate());
			for(Timesheet timesh: timeshs.getTimesheets())
				System.out.println(">>>> Дата " + timesh.getDateTask());
		}
	}
}
