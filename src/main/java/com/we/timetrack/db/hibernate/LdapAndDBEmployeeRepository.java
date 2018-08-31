package com.we.timetrack.db.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.ldap.EmployeeContextMapper;
import com.we.timetrack.db.ldap.LdapEmployeeRepository;
import com.we.timetrack.model.Employee;

/**
 * Manages database operations for Employee table.
 * Additional use ldap repository.
 * @author pablo
 */
@Repository(value="ldapAndDBEmployeeRepository")
@Profile("LdapAndDBEmployees")
@Transactional
public class LdapAndDBEmployeeRepository extends HibernateEmployeeRepository {

	@Inject
	public LdapAndDBEmployeeRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
		this.ldapEmployeeRepository = new LdapEmployeeRepository();
		this.ldapEmployeeRepository.setContextMapper(new EmployeeContextMapper());
	}

	private LdapEmployeeRepository ldapEmployeeRepository;
		
	public void init() {
		updateAll();
	}
	
	/**
	 * Gets employee record with matching username
	 */
	@Override
	public Employee getEmployee(String username){
		
		Employee employee = super.getEmployee(username);
		
		// If no employee in db, try to load from ldap
		if (employee == null) {
			employee = ldapEmployeeRepository.getEmployee(username);
			saveOrUpdate(employee);
		}
		Hibernate.initialize(employee.getRoles());
		return employee;
	}
	
	/**
	 * Get all employees from Ldap server, and save it to database.
	 */
	public void updateAll() {
		List<Employee> listEmployeeFromLdap = ldapEmployeeRepository.getEmployees();
		List<Employee> listEmployeeFromDB = getEmployees();
		if (listEmployeeFromLdap != null && !listEmployeeFromLdap.isEmpty()) {
			listEmployeeFromLdap.forEach(employee -> {
				if (listEmployeeFromDB.contains(employee)) {
					saveOrUpdate(merge(employee, listEmployeeFromDB.get(listEmployeeFromDB.indexOf(employee))));
				} else {
					saveOrUpdate(employee);
				}
			});
		}
	}
	
	/**
	 * Compare two employees, and if first has new values, update second.
	 * @param employeeLdap
	 * @param employeeDB
	 * @return
	 */
	private Employee merge(Employee employeeLdap, Employee employeeDB) {
		if (employeeLdap.getDepartment() != null && !employeeLdap.getDepartment().equals(employeeDB.getDepartment())) {
			employeeDB.setDepartment(employeeLdap.getDepartment());
		}
		if (employeeLdap.getMail() != null && !employeeLdap.getMail().equals(employeeDB.getMail())) {
			employeeDB.setMail(employeeLdap.getMail());
		}
		if (employeeLdap.getName() != null && !employeeLdap.getName().equals(employeeDB.getName())) {
			employeeDB.setName(employeeLdap.getName());
		}
		if (employeeLdap.getPost() != null && !employeeLdap.getPost().equals(employeeDB.getPost())) {
			employeeDB.setPost(employeeLdap.getPost());
		}
		if (employeeLdap.getSurname() != null && !employeeLdap.getSurname().equals(employeeDB.getSurname())) {
			employeeDB.setSurname(employeeLdap.getSurname());
		}
		if (employeeLdap.getUsername() != null && !employeeLdap.getUsername().equals(employeeDB.getUsername())) {
			employeeDB.setUsername(employeeLdap.getUsername());
		}
		return employeeDB;
	}
}
