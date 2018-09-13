package com.we.timetrack.db.ldapHibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.hibernate.HibernateEmployeeRepository;
import com.we.timetrack.db.ldap.EmployeeContextMapper;
import com.we.timetrack.db.ldap.LdapEmployeeRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.model.EmployeeComparator;

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
	 * Gets direct reports of employee
	 * @param employee
	 * @return List<Employee> - direct reports
	 */
	@Override
	public List<Employee> getDirectReports(Employee employee) {
		Employee emp = getEmployee(employee.getEmployeeId());
		Hibernate.initialize(emp.getSubordinates());
		List<Employee> employees = new ArrayList<Employee>(emp.getSubordinates()); 
		employees.sort(new EmployeeComparator());
		return employees;
	}
	
	/**
	 * Get all employees from Ldap server, and save it to database.
	 */
	public void updateAll() {
		List<EmployeeWrap> listEmployeeFromLdap = ldapEmployeeRepository.getEmployees(new EmployeeWrapContextMapper());
		List<Employee> listEmployeeFromDB = getEmployees();
		if (listEmployeeFromLdap != null && !listEmployeeFromLdap.isEmpty()) {
			Map<String, EmployeeWrap> map = new HashMap<>();
			listEmployeeFromLdap.forEach(employee -> {
				map.put(employee.dn, employee);
				if (listEmployeeFromDB.contains(employee.employee)) {
					Employee employeeDB = listEmployeeFromDB.get(listEmployeeFromDB.indexOf(employee.employee)); 
					employee.employee = merge(employee, employeeDB);
				} else {
					listEmployeeFromDB.add(employee.employee);
				}
			});
			listEmployeeFromLdap.forEach(employeeWrap -> {
				if (employeeWrap.manager != null && map.containsKey(employeeWrap.manager)) {
					if (!map.get(employeeWrap.manager).employee.equals(employeeWrap.employee.getManager())) {
						employeeWrap.employee.setManager(map.get(employeeWrap.manager).employee);
					}
				} else {
					if (employeeWrap.employee.getManager() != null) {
						employeeWrap.employee.setManager(null);
					}
				}
			});
			listEmployeeFromDB.forEach(employee -> saveOrUpdate(employee));
		}
	}
	
	/**
	 * Compare two employees, and if first has new values, update second.
	 * @param employeeLdap
	 * @param employeeDB
	 * @return
	 */
	private Employee merge(EmployeeWrap employeeLdap, Employee employeeDB) {
		if (employeeLdap.employee.getDepartment() != null && !employeeLdap.employee.getDepartment().equals(employeeDB.getDepartment())) {
			employeeDB.setDepartment(employeeLdap.employee.getDepartment());
		}
		if (employeeLdap.employee.getMail() != null && !employeeLdap.employee.getMail().equals(employeeDB.getMail())) {
			employeeDB.setMail(employeeLdap.employee.getMail());
		}
		if (employeeLdap.employee.getName() != null && !employeeLdap.employee.getName().equals(employeeDB.getName())) {
			employeeDB.setName(employeeLdap.employee.getName());
		}
		if (employeeLdap.employee.getPost() != null && !employeeLdap.employee.getPost().equals(employeeDB.getPost())) {
			employeeDB.setPost(employeeLdap.employee.getPost());
		}
		if (employeeLdap.employee.getSurname() != null && !employeeLdap.employee.getSurname().equals(employeeDB.getSurname())) {
			employeeDB.setSurname(employeeLdap.employee.getSurname());
		}
		if (employeeLdap.employee.getUsername() != null && !employeeLdap.employee.getUsername().equals(employeeDB.getUsername())) {
			employeeDB.setUsername(employeeLdap.employee.getUsername());
		}
		if (employeeLdap.employee.getManager() != null && !employeeLdap.employee.getManager().equals(employeeDB.getManager())) {
			employeeDB.setManager(employeeLdap.employee.getManager());
		}
		if (employeeLdap.employee.isActive() != null && !employeeLdap.employee.isActive().equals(employeeDB.isActive())) {
			employeeDB.setActive(employeeLdap.employee.isActive());
		}
		return employeeDB;
	}
	
	private class EmployeeWrap {
		private Employee employee;
		private String manager;
		private String dn;
	}
	
	private class EmployeeWrapContextMapper implements ContextMapper<EmployeeWrap>{

		private final static String MANAGER_ATTRIBUTE = "manager";
		private final static String DN_ATTRIBUTE = "distinguishedName";
		private final ContextMapper<Employee> contextMapper = new EmployeeContextMapper();
		
		@Override
		public EmployeeWrap mapFromContext(Object ctx) throws NamingException {
			DirContextAdapter context = (DirContextAdapter) ctx;
			EmployeeWrap employee = new EmployeeWrap();
			employee.employee = contextMapper.mapFromContext(ctx);
			employee.manager = context.getStringAttribute(MANAGER_ATTRIBUTE);
			employee.dn = context.getStringAttribute(DN_ATTRIBUTE);
			employee.employee.setActive(!employee.dn.contains("OU=Уволенные"));
			return employee;
		}
		
	}
}
