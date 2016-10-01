package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.util.List;

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
		EmployeeRepository employeeManager = new HibernateEmployeeRepository(sessionFactory);
		Employee employee = employeeManager.getEmployee(3);
		assertNotNull(employee);
		assertTrue(employee.getEmployeeId() == 3);
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
	
	@Test
	@Transactional
	public void testSaveEmployee(){
		EmployeeRepository employeeRepository = new HibernateEmployeeRepository(sessionFactory);
		int countEmployees = employeeRepository.getEmployees().size();
		System.out.println(">>>>>>>>Количество записей сотрудников до сохранения нового: " + countEmployees);
		Employee newEmployee = new Employee();
		newEmployee.setName("Петр");
		newEmployee.setSurname("Иванов");
		newEmployee.setUsername("p.ivanov");
		newEmployee.setPassword("123ivanov");
		newEmployee.setMail("p.ivanov@west-e.ru");
		newEmployee.setDepartment("ОПиК");
		newEmployee.setPost("Инженер");
		employeeRepository.saveEmployee(newEmployee);
		countEmployees = employeeRepository.getEmployees().size();
		System.out.println(">>>>>>>>Количество записей сотрудников после сохранения нового: " + countEmployees);
		Employee employee = employeeRepository.getEmployee("p.ivanov");
		assertTrue(employee.equals(newEmployee));
		employeeRepository.removeEmployee(newEmployee);
		countEmployees = employeeRepository.getEmployees().size();
		System.out.println(">>>>>>>>Количество записей сотрудников после удаления нового: " + countEmployees);
	}

}
