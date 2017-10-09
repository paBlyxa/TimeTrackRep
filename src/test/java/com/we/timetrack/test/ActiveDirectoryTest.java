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
			System.out.println("Employee: " + employee.getSurname() + " " + employee.getName() + " " 
					+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
		}
	}
	
	@Test
	public void getDirectReports() {
		String dn = "CN=Павел Факадей,OU=Проектирования и конструирования,DC=we,DC=ru";
		about(dn);
		
		dn = "CN=Павел Факадей,OU=Проектирования и конструирования,DC=we,DC=ru";
		about(dn);
	}
	
	@Test
	public void getByGUID() {
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		//12a24625-9f35-4d33-f7af-0ad444f9e5ee
		//9e2fca30-878b-4a16-1da7-5ee301a03bde
		Employee employee = employeeRepository.getEmployee(UUID.fromString("0763a058-4c6d-43a4-8183-835faddbb8fd"));
		System.out.println("getByGuid: " + employee.getEmployeeId() + " " + employee.getSurname() + " " + employee.getName() + " " 
				+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
	}
	
	@Test
	public void getByDN() {
		String dn = "CN=Павел Факадей,OU=Проектирования и конструирования,DC=we,DC=ru";
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		Employee employee = employeeRepository.get(dn);
		assertNotNull(employee);
		System.out.println("getByDN Employee: " + employee.getEmployeeId() + " " + employee.getSurname() + " " + employee.getName() + " " 
				+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
	}
	
	@Test
	public void getByUsername() {
		String username = "fakadey";
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		Employee employee = employeeRepository.getEmployee(username);
		assertNotNull(employee);
		System.out.println("getByUsername Employee: " + employee.getEmployeeId() + " " + employee.getSurname() + " " + employee.getName() + " " 
				+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
	}

	@Test
	public void getByGroup() {
		String group = "cn=ОПИК,ou=ОПИК,dc=we,dc=ru";
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		List<Employee> employeeList = employeeRepository.getEmployees(group);
		for (Employee employee : employeeList){
			System.out.println("Employee: " + employee.getSurname() + " " + employee.getName() + " " 
					+ employee.getUsername() + " " + employee.getPost() + " " + employee.getDepartment());
		}
	}
	
	private static void about(String dn){
		LdapEmployeeRepository employeeRepository = new LdapEmployeeRepository();
		Employee employee = employeeRepository.get(dn);
		if (employee != null) {
			System.out.println("Employee: " + employee.getSurname() + " " + employee.getName());
			if (employee.getAuthorities() != AuthorityUtils.NO_AUTHORITIES){
				System.out.print("Authority: " );
				for (GrantedAuthority auth : employee.getAuthorities()){
					System.out.print(auth.getAuthority() + " ");
				}
				System.out.println();
			}
		}
		else {
			System.out.println(dn + " not exist");
		}
		
		List<Employee> directReports = employeeRepository.getDirectReports(employee);
		System.out.println("directReport:");
		for (Employee directReport : directReports){
			System.out.println(directReport.getSurname() + " " + directReport.getName());
		}
	}

}
