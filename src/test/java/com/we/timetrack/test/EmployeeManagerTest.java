package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.model.Employee;

import com.we.timetrack.config.DataConfig;
import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.hibernate.HibernateEmployeeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class EmployeeManagerTest {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	@Transactional
	public void testGetEmployeeById() {
		EmployeeRepository employeeRepository = new HibernateEmployeeRepository(sessionFactory);
		List<Employee> employees = employeeRepository.getEmployees();
		UUID employeeId = employees.get(0).getEmployeeId();
		Employee employee = employeeRepository.getEmployee(employeeId);
		assertNotNull(employee);
		assertTrue(employee.getEmployeeId() == employeeId);
		System.out.println("Сотрдуник: id = " + employee.getEmployeeId() +
				" Фамилия = " + employee.getSurname());
	}
	
	@Test
	@Transactional
	public void testGetAllEmployees() {
		EmployeeRepository employeeRepository = new HibernateEmployeeRepository(sessionFactory);
		List<Employee> employees = employeeRepository.getEmployees();
		assertNotNull(employees);
		System.out.println(">>>>>>>>>Количество записей сотрудников: " + employees.size());
		for(Employee employee: employees){
			assertNotNull(employee);
			System.out.println(">>>>>>>>Сотрудник: " + employee.getEmployeeId() + " " + employee.getSurname() + " " + employee.getName());
		}
	}

}
