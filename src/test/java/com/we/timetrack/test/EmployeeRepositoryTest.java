package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.util.Collection;
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
import com.we.timetrack.model.Task;
import com.we.timetrack.model.TaskStatus;
import com.we.timetrack.config.DataConfig;
import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.hibernate.HibernateTaskRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
@WebAppConfiguration
@ActiveProfiles({"dataSourceProduction", "LdapAndDBEmployees", "test"})
public class EmployeeRepositoryTest {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Test
	@Transactional
	public void testGetEmployeeById() {
		assertNotNull(employeeRepository);
		List<Employee> employees = employeeRepository.getEmployees();
		UUID employeeId = employees.get(0).getEmployeeId();
		Employee employee = employeeRepository.getEmployee(employeeId);
		assertNotNull(employee);
		assertTrue(employee.getEmployeeId() == employeeId);
		System.out.println("Сотрудник: id = " + employee.getEmployeeId() +
				" Фамилия = " + employee.getSurname());
	}
	
	@Test
	@Transactional
	public void testGetAllEmployees() {
		assertNotNull(employeeRepository);
		List<Employee> employees = employeeRepository.getEmployees();
		printResult(employees);
	}
	

	@Test
	@Transactional
	public void testUpdateAll() {
		assertNotNull(employeeRepository);
		employeeRepository.updateAll();
	}
	
	@Test
	@Transactional
	public void testGetByGroup() {
		assertNotNull(employeeRepository);
		List<Employee> employees = employeeRepository.getEmployees("ОПИК");
		printResult(employees);
	}


	@Test
	@Transactional
	public void testGetTasks() {
		assertNotNull(employeeRepository);
		List<Employee> employees = employeeRepository.getEmployees();
		HibernateTaskRepository taskRepository = new HibernateTaskRepository(sessionFactory);
		System.out.println("Seach tasks for department " + employees.get(0).getDepartment().getName());
		List<Task> tasks = taskRepository.getFreeTasks(TaskStatus.Active, employees.get(0).getDepartment());
		System.out.println(">>>>>>>>");
		System.out.println(">>>>>>>>");
		tasks.forEach(task -> {
			System.out.println(task.getName());
		});
	}
	
	@Test
	@Transactional
	public void testGetSubordinates() {
		List<Employee> employees = employeeRepository.getEmployees();
		for (Employee employee : employees) {
			System.out.println(employee.getShortName() + " has subordinates: ");
			printResult(employee.getSubordinates());
		}
		System.out.println();
	}
	
	private void printResult(Collection<Employee> employees) {
		assertNotNull(employees);
		System.out.println(">>>>>>>>>Количество сотрдуников: " + employees.size());
		for(Employee employee: employees){
			assertNotNull(employee);
			printEmployee(employee);
		}
	}

	private void printEmployee(Employee employee) {
		if (employee != null)
			System.out.println(">>>>>>>>Сотрудник: " + employee.getEmployeeId() + " " + employee.getSurname() + " " + employee.getName());
		else
			System.out.println("null");
	}
}
