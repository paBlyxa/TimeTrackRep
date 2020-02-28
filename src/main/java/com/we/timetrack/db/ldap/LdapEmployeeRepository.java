package com.we.timetrack.db.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Repository;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Department;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.model.EmployeeComparator;
import com.we.timetrack.util.UuidUtils;


import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository(value = "ldapEmployeeRepository")
@Profile("LdapEmployees")
public class LdapEmployeeRepository implements EmployeeRepository {

	
	private final static Logger logger = LoggerFactory.getLogger(LdapEmployeeRepository.class);

	private final static String BASE = "dc=we,dc=ru";
	private final static String MAIN_GROUP = "cn=Timex пользователи,ou=Группы,dc=we,dc=ru";
	private final static String FIRED_GROUP = "cn=Бывшие сотрудники,ou=Группы,dc=we,dc=ru";

	@Value("${ldap.user}")
	private String ldapUser;
	@Value("${ldap.password}")
	private String ldapPassword;
	@Value("${ldap.domain}")
	private String ldapDomain;
	
	/**
	 * Get employee with matching employeeId from ActiveDirectory
	 * 
	 * @param employeeId - employee's UUID
	 * @return Employee if exist, otherwise null
	 */
	@Override
	public Employee getEmployee(UUID employeeId) {
		List<Employee> employees = getLdapTemplate().search(
				query().base(BASE).filter("(&(objectClass=person)(objectGUID="
						+ UuidUtils.convertToByteString(employeeId) + ")" + "(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + "))"),
				getContextMapper());
		if (employees.isEmpty()){
			logger.warn("No employee with matching employeeId = [{}]", employeeId);
		}
		return employees.isEmpty() ? null : employees.get(0);
	}

	/**
	 * Get employee with matching username from ActiveDirectory
	 * 
	 * @param username - employee's username
	 * @return Employee if exist, otherwise null
	 */
	@Override
	public Employee getEmployee(String username) {
		List<Employee> employees = getLdapTemplate().search(
				query().base(BASE).filter(
						"(&(objectClass=person)(sAMAccountName=" + username + ")" + "(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + "))"),
				getContextMapperWithAuth());
		if (employees.isEmpty()){
			logger.warn("No employee with matching username = [{}]", username);
		}
		return employees.isEmpty() ? null : employees.get(0);
	}
	

	/**
	 * Get employee with matching username from ActiveDirectory and 
	 * custom contextMapper
	 * 
	 * @param username - employee's username
	 * @return Employee if exist, otherwise null
	 */
	public <T> T getEmployee(String username, ContextMapper<T> contextMapper) {
		List<T> employees = getLdapTemplate().search(
				query().base(BASE).filter(
						"(&(objectClass=person)(sAMAccountName=" + username + ")" + "(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + "))"),
				contextMapper);
		if (employees.isEmpty()){
			logger.warn("No employee with matching username = [{}]", username);
		}
		return employees.isEmpty() ? null : employees.get(0);
	}
	
	/**
	 * Get employees from ActiveDirectory
	 * 
	 * @return list of employees
	 */
	@Override
	public List<Employee> getEmployees() {
		List<Employee> employeeList = getEmployees(getContextMapper()); 
		employeeList.sort(new EmployeeComparator());
		return employeeList;
	}
	
	/**
	 * Get any models from ActiveDirectory
	 * @param contextMapper - custom ContextMapper
	 * @return
	 */
	public <T> List<T> getEmployees(ContextMapper<T> contextMapper){
		LdapQuery query = query().base(BASE).filter("(&(objectClass=person)(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + "))");
		List<T> employeeList = getLdapTemplate().search(query, contextMapper);
		return employeeList;
	}
	
	
	/**
	 * Get employees from ActiveDirectory with matching Group
	 * 
	 * @return list of employees
	 */
	@Override
	public List<Employee> getEmployees(String group) {
		String name = "cn=" + group + ",ou=" + group + ","+ BASE;
		LdapQuery query = query().base(BASE).filter("(&(objectClass=person)(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + ")(memberOf:1.2.840.113556.1.4.1941:=" + name + "))");
		
		List<Employee> employeeList = getLdapTemplate().search(query, getContextMapper());
		employeeList.sort(new EmployeeComparator());
		return employeeList;
	}

	/**
	 * Get all active employees.
	 * 
	 * @return list of employees
	 */
	@Override
	public List<Employee> getActiveEmployees() {
LdapQuery query = query().base(BASE).filter("(&(objectClass=person)(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + ")(!(memberOf:1.2.840.113556.1.4.1941:=" + FIRED_GROUP + ")))");
		
		List<Employee> employeeList = getLdapTemplate().search(query, getContextMapper());
		employeeList.sort(new EmployeeComparator());
		return employeeList;
	}
	
	
	/**
	 * Get employee by dn from ActiveDirectory
	 * 
	 * @param dn
	 * @return Employee if exist, otherwise null
	 */	
	public Employee get(String dn) {
		return getLdapTemplate().lookup(dn, getContextMapper());
	}

	/**
	 * Get subordinate employees from ActiveDirectory
	 * 
	 * @param employee
	 * @return list of employees
	 */
	public List<Employee> getDirectReports(Employee employee) {

		List<Employee> directReports = new ArrayList<>();
		List<Employee> checked = new ArrayList<>();
		getDirectReports(employee, directReports, checked);
		directReports.sort(new EmployeeComparator());
		return directReports;
	}

	private void getDirectReports(Employee employee, List<Employee> directReports, List<Employee> checked) {

		if (!checked.contains(employee)) {
			DirContextOperations context = getLdapTemplate()
					.searchForContext(query().base(BASE).filter("(&(objectClass=person)(sAMAccountName="
							+ employee.getUsername() + ")" + "(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + "))"));
			String[] strDirectReports = context.getStringAttributes("directReports");
			checked.add(employee);
			if (strDirectReports != null) {
				for (String directReport : strDirectReports) {
					Employee empl = get(directReport);
					getDirectReports(empl, directReports, checked);
					if (!directReports.contains(empl)) {
						directReports.add(empl);
					}
				}
			}
		}
	}

	private LdapTemplate getLdapTemplate() {
		logger.debug("Create LdapTemplate with user={}@{}", ldapUser, ldapDomain);
		LdapContextSource contextSource = new LdapContextSource();
		Map<String, Object> props = new HashMap<>();
		props.put("java.naming.ldap.attributes.binary", "objectGUID");
		contextSource.setUrl("ldap://" + ldapDomain);
		contextSource.setUserDn(ldapUser + "@" + ldapDomain);
		contextSource.setPassword(ldapPassword);
		contextSource.setReferral("follow");
		contextSource.setBaseEnvironmentProperties(props);
		try {
			contextSource.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LdapTemplate ldapTemplate = new LdapTemplate();
		ldapTemplate.setContextSource(contextSource);
		return ldapTemplate;
	}

	private ContextMapper<Employee> getContextMapper() {
		return new EmployeeContextMapper();
	}
	
	private ContextMapper<Employee> getContextMapperWithAuth(){
		return new EmployeeAuthorityContextMapper();
	}
	
	/**
	 * Get map of uuid, employee
	 */
	public Map<UUID, Employee> getEmployeeMap() {
		List<Employee> employees = getEmployees();
		Map<UUID, Employee> employeeMap = new HashMap<UUID, Employee>();
		for (Employee employee : employees) {
			employeeMap.put(employee.getEmployeeId(), employee);
		}
		return employeeMap;
	}

	@Override
	public List<Department> getDepartments() {
		
		return new ArrayList<>();
	}

	public void setLdapUser(String ldapUser) {
		this.ldapUser = ldapUser;
	}

	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	public void setLdapDomain(String ldapDomain) {
		this.ldapDomain = ldapDomain;
	}

	
}
