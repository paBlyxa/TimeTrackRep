package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.EmployeeManager;
import com.we.timetrack.service.model.DateRange;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles({"dataSourceProduction", "DBEmployees"})
public class EmployeeManagerTest {

	@Autowired
	private EmployeeManager employeeManager;
	
	@Test
	public void testNoReadyEmployees() {
		DateRange dateRange = new DateRange(LocalDate.now().minusWeeks(1), LocalDate.now().minusDays(1));
		Map<Employee, List<LocalDate>> map = employeeManager.getNotReadyEmployees(dateRange);
		assertNotNull(map);
		for (Map.Entry<Employee, List<LocalDate>> entry : map.entrySet()) {
			System.out.println(entry.getKey().getShortName() + ":");
			for (LocalDate date : entry.getValue()) {
				System.out.print(date.format(DateRange.dateFormat) + ", ");
			}
			System.out.println();
		}
	}

}
