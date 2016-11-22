package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.TimesheetDay;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class TimesheetDayTest {
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	
	@Test
	@Transactional
	public void test() {
		List<Timesheet> timesheetsAll = timesheetRepository.getTimesheets();
		UUID employeeId = timesheetsAll.get(0).getEmployeeId();
		Employee employee = new Employee(); 
		employee.setEmployeeId(employeeId);
        List<Timesheet> timesheets = timesheetRepository.getTimesheets(employeeId);
        LocalDate beginDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now().plusMonths(1);
        List<TimesheetDay> timesheetsByDays = TimesheetDay.getTimesheetsByDays(timesheets, beginDate, endDate);
        assertNotNull(timesheetsByDays);
        System.out.println("Результаты:");
        for (TimesheetDay tsd : timesheetsByDays){
        	assertNotNull(tsd);
        	System.out.println(tsd.getDate() + " " + tsd.getTimesheets().size() + " " + tsd.getHours());
        }
	}

}
