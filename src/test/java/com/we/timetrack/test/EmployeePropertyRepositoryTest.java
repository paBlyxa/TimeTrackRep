package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.DataConfig;
import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Employee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
@WebAppConfiguration
@ActiveProfiles({"dataSourceProduction", "LdapAndDBEmployees"})
public class EmployeePropertyRepositoryTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Test
	public void test() {
		Employee employee = employeeRepository.getEmployee("ivanov");
		assertNotNull(employee);
		assertNotNull(employee.getProperties());
	}
	
	@Test
	public void testAll() {
		List<Employee> employees = employeeRepository.getEmployees();
		assertNotNull(employees);
		assertTrue(employees.size() > 0);
		for (Employee e : employees) {
			System.out.println(e.getShortName());
		}
	}

}
