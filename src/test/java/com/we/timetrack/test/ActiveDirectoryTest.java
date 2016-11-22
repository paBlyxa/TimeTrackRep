package com.we.timetrack.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.we.timetrack.db.ldap.LdapEmployeeRepository;
import com.we.timetrack.model.Employee;

public class ActiveDirectoryTest {

	@Test
	public void getAll() {
		// TODO Auto-generated method stub
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		List<Employee> employeeList = employeeRepository.getEmployees();
		for (Employee employee : employeeList){
			System.out.println("Сотрудник: " + employee.getSurname() + " " + employee.getName() + " " 
					+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
		}
	}
	
	@Test
	public void getDirectReports() {
		String dn = "CN=Павел Факадей,OU=Проектирования и конструирования,DC=we,DC=ru";
		about(dn);
		
		dn = "CN=Антон Иванов,OU=Проектирования и конструирования,DC=we,DC=ru";
		about(dn);
	}
	
	@Test
	public void getByGUID() {
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		Employee employee = employeeRepository.getEmployee(UUID.fromString("12a24625-9f35-4d33-f7af-0ad444f9e5ee"));
		System.out.println("getByGUID" + " " + employee);
	}
	
	@Test
	public void getByDN() {
		String dn = "CN=Павел Факадей,OU=Проектирования и конструирования,DC=we,DC=ru";
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		Employee employee = employeeRepository.get(dn);
		assertNotNull(employee);
		System.out.println("getByDN Сотрудник: " + employee.getEmployeeId() + " " + employee.getSurname() + " " + employee.getName() + " " 
				+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
	}
	
	@Test
	public void getByUsername() {
		String username = "fakadey";
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		Employee employee = employeeRepository.getEmployee(username);
		assertNotNull(employee);
		System.out.println("getByUsername Сотрудник: " + employee.getEmployeeId() + " " + employee.getSurname() + " " + employee.getName() + " " 
				+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
	}
	
	private static void about(String dn){
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		Employee employee = employeeRepository.get(dn);
		if (employee != null) {
			System.out.println("Найден сотрудник: " + employee.getSurname() + " " + employee.getName());
			if (employee.getAuthorities() != AuthorityUtils.NO_AUTHORITIES){
				System.out.print("Authority: " );
				for (GrantedAuthority auth : employee.getAuthorities()){
					System.out.print(auth.getAuthority() + " ");
				}
				System.out.println();
			}
		}
		else {
			System.out.println(dn + " не существует");
		}
		
		List<Employee> directReports = employeeRepository.getDirectReports(employee);
		System.out.println("Подчиненные:");
		for (Employee directReport : directReports){
			System.out.println(directReport.getSurname() + " " + directReport.getName());
		}
	}

}
