package com.we.timetrack.db.ldapHibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.naming.NamingException;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.hibernate.HibernateEmployeeRepository;
import com.we.timetrack.db.ldap.EmployeeContextMapper;
import com.we.timetrack.db.ldap.LdapEmployeeRepository;
import com.we.timetrack.model.Department;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.model.EmployeeComparator;

/**
 * Manages database operations for Employee table. Additional use ldap
 * repository.
 * 
 * @author pablo
 */
@Repository(value = "ldapAndDBEmployeeRepository")
@Profile("LdapAndDBEmployees")
@Transactional
public class LdapAndDBEmployeeRepository extends HibernateEmployeeRepository {

	private final static Logger logger = LoggerFactory.getLogger(LdapAndDBEmployeeRepository.class);

	@Autowired
	private TaskExecutor taskExecutor;

	@Value("${ldap.user}")
	private String ldapUser;
	@Value("${ldap.password}")
	private String ldapPassword;
	@Value("${ldap.domain}")
	private String ldapDomain;
	
	@Inject
	public LdapAndDBEmployeeRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	private LdapEmployeeRepository ldapEmployeeRepository;

	@PostConstruct
	public void init() {
		this.ldapEmployeeRepository = new LdapEmployeeRepository();
		this.ldapEmployeeRepository.setLdapDomain(ldapDomain);
		this.ldapEmployeeRepository.setLdapUser(ldapUser);
		this.ldapEmployeeRepository.setLdapPassword(ldapPassword);
	}

	/**
	 * Gets employee record with matching username
	 */
	@Override
	public Employee getEmployee(String username) {

		Employee employeeDB = super.getEmployee(username);
		if (employeeDB != null) {
			taskExecutor.execute(new UpdateEmployeeTask(employeeDB, this));
			Hibernate.initialize(employeeDB.getRoles());
			return employeeDB;
		} else {
			EmployeeWrap employeeLdap = ldapEmployeeRepository.getEmployee(username, new EmployeeWrapContextMapper());
			if (employeeLdap != null) {
				saveOrUpdate(employeeLdap.employee);
				Hibernate.initialize(employeeLdap.employee.getRoles());
				return employeeLdap.employee;
			}
		}
		return null;
	}

	/**
	 * Gets direct reports of employee
	 * 
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
		logger.info("Update all employees from LDAP server");

		List<EmployeeWrap> listEmployeeFromLdap = ldapEmployeeRepository.getEmployees(new EmployeeWrapContextMapper());
		List<Employee> listEmployeeFromDB = getEmployees();
		List<Employee> newEmployees = new ArrayList<>();
		Map<String, Department> departmentMap = getDepartmentMap();
		if (listEmployeeFromLdap != null && !listEmployeeFromLdap.isEmpty()) {
			Map<String, EmployeeWrap> map = new HashMap<>();
			listEmployeeFromLdap.forEach(employee -> {
				map.put(employee.dn, employee);
				if (listEmployeeFromDB.contains(employee.employee)) {
					Employee employeeDB = listEmployeeFromDB.get(listEmployeeFromDB.indexOf(employee.employee));
					employee.employee = merge(employee, employeeDB);
				} else {
					logger.info("Find new employee: {}", employee.employee);
					newEmployees.add(employee.employee);
				}
				if (employee.employee.getDepartment() != null
						&& employee.employee.getDepartment().getDepartmentId() == null) {
					Department department = departmentMap.get(employee.employee.getDepartment().getName());
					if (department != null) {
						employee.employee.setDepartment(department);
					} else {
						departmentMap.put(employee.employee.getDepartment().getName(),
								employee.employee.getDepartment());
					}
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
			newEmployees.forEach(employee -> saveOrUpdate(employee));
		}

	}

	public void updateEmployee(Employee employee) {
		logger.info("Update employee '{}' from AD", employee.getShortName());
		// Get employee from AD
		EmployeeWrap employeeLdap = ldapEmployeeRepository.getEmployee(employee.getUsername(),
				new EmployeeWrapContextMapper());
		// Merge employees
		merge(employeeLdap, employee);
		// Check deparment
		if (employee.getDepartment().getDepartmentId() == null) {
			Department department = getDeparment(employee.getDepartment().getName());
			if (department != null) {
				employee.setDepartment(department);
			}
		}
	}
	
	/**
	 * Compare two employees, and if first has new values, update second.
	 * 
	 * @param employeeLdap
	 * @param employeeDB
	 * @return
	 */
	private Employee merge(EmployeeWrap employeeLdap, Employee employeeDB) {
		if (employeeLdap.employee.getDepartment() != null
				&& !employeeLdap.employee.getDepartment().equals(employeeDB.getDepartment())) {
			logger.debug("Find difference in 'department' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.getDepartment(), employeeLdap.employee.getDepartment());
			employeeDB.setDepartment(employeeLdap.employee.getDepartment());
		}
		if (employeeLdap.employee.getMail() != null && !employeeLdap.employee.getMail().equals(employeeDB.getMail())) {
			logger.debug("Find difference in 'mail' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.getMail(), employeeLdap.employee.getMail());
			employeeDB.setMail(employeeLdap.employee.getMail());
		}
		if (employeeLdap.employee.getName() != null && !employeeLdap.employee.getName().equals(employeeDB.getName())) {
			logger.debug("Find difference in 'name' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.getName(), employeeLdap.employee.getName());
			employeeDB.setName(employeeLdap.employee.getName());
		}
		if (employeeLdap.employee.getPost() != null && !employeeLdap.employee.getPost().equals(employeeDB.getPost())) {
			logger.debug("Find difference in 'post' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.getPost(), employeeLdap.employee.getPost());
			employeeDB.setPost(employeeLdap.employee.getPost());
		}
		if (employeeLdap.employee.getSurname() != null
				&& !employeeLdap.employee.getSurname().equals(employeeDB.getSurname())) {
			logger.debug("Find difference in 'surname' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.getSurname(), employeeLdap.employee.getSurname());
			employeeDB.setSurname(employeeLdap.employee.getSurname());
		}
		if (employeeLdap.employee.getUsername() != null
				&& !employeeLdap.employee.getUsername().equals(employeeDB.getUsername())) {
			logger.debug("Find difference in 'username' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.getUsername(), employeeLdap.employee.getUsername());
			employeeDB.setUsername(employeeLdap.employee.getUsername());
		}
		if (employeeLdap.employee.getManager() != null
				&& !employeeLdap.employee.getManager().equals(employeeDB.getManager())) {
			logger.debug("Find difference in 'manager' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.getManager(), employeeLdap.employee.getManager());
			employeeDB.setManager(employeeLdap.employee.getManager());
		}
		if (employeeLdap.employee.isActive() != null
				&& !employeeLdap.employee.isActive().equals(employeeDB.isActive())) {
			logger.debug("Find difference in 'active' for {}, old: {}, new: {}", employeeLdap.employee.getShortName(), 
					employeeDB.isActive(), employeeLdap.employee.isActive());
			employeeDB.setActive(employeeLdap.employee.isActive());
		}
		return employeeDB;
	}

	private class EmployeeWrap {
		private Employee employee;
		private String manager;
		private String dn;
	}

	private class EmployeeWrapContextMapper implements ContextMapper<EmployeeWrap> {

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

	private class UpdateEmployeeTask implements Runnable {

		private final Employee employee;
		private final LdapAndDBEmployeeRepository employeeRepository;
		
		public UpdateEmployeeTask(Employee employee, LdapAndDBEmployeeRepository employeeRepository) {
			this.employee = employee;
			this.employeeRepository = employeeRepository;
		}

		@Override
		public void run() {
			employeeRepository.updateEmployee(employee);
		}

	}
}
