package com.we.timetrack.db.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.model.EmployeeComparator;
import com.we.timetrack.util.UuidUtils;


import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository(value = "ldapEmployeeRepository")
public class LdapEmployeeRepository implements EmployeeRepository {

	
	private static Logger logger = LoggerFactory.getLogger(LdapEmployeeRepository.class);

	private final static String BASE = "dc=we,dc=ru";
	private final static String MAIN_GROUP = "cn=Timex пользователи,ou=Группы,dc=we,dc=ru";

	// Attributes
	private final static String NAME_ATTRIBUTE = "givenName";
	private final static String SURNAME_ATTRIBUTE = "sn";
	private final static String USERNAME_ATTRIBUTE = "sAMAccountName";
	private final static String DEPARTMENT_ATTRIBUTE = "department";
	private final static String GUID_ATTRIBUTE = "objectguid";
	private final static String MANAGER_ATTRIBUTE = "manager";
	private final static String EMAIL_ATTRIBUTE = "mail";
	private final static String TITLE_ATTRIBUTE = "title";
	private final static String GROUPS_ATTRIBUTE = "memberOf";
	@SuppressWarnings("unused")
	private final static String[] RETURN_ATTRIBUTES = { NAME_ATTRIBUTE, SURNAME_ATTRIBUTE, USERNAME_ATTRIBUTE,
			EMAIL_ATTRIBUTE, DEPARTMENT_ATTRIBUTE, GUID_ATTRIBUTE, MANAGER_ATTRIBUTE, TITLE_ATTRIBUTE,
			GROUPS_ATTRIBUTE };

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
				getContextMapper());
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
		LdapQuery query = query().base(BASE).filter("(&(objectClass=person)(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + "))");
		
		List<Employee> employeeList = getLdapTemplate().search(query, getContextMapper());
		employeeList.sort(new EmployeeComparator());
		return employeeList;
	}
	
	/**
	 * Get employees from ActiveDirectory with matching Group
	 * 
	 * @return list of employees
	 */
	@Override
	public List<Employee> getEmployees(String group) {
		LdapQuery query = query().base(BASE).filter("(&(objectClass=person)(memberOf:1.2.840.113556.1.4.1941:=" + MAIN_GROUP + ")(memberOf:1.2.840.113556.1.4.1941:=" + group + "))");
		
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
		LdapContextSource contextSource = new LdapContextSource();
		Map<String, Object> props = new HashMap<>();
		props.put("java.naming.ldap.attributes.binary", "objectGUID");
		contextSource.setUrl("ldap://we.ru");
		contextSource.setUserDn("addrbook@we.ru");
		contextSource.setPassword("123addrbook321");
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

	private class EmployeeContextMapper implements ContextMapper<Employee> {

		public Employee mapFromContext(Object ctx) {

			// Read attributes from active directory
			DirContextAdapter context = (DirContextAdapter) ctx;
			Employee employee = new Employee();
			employee.setName(context.getStringAttribute(NAME_ATTRIBUTE));
			employee.setSurname(context.getStringAttribute(SURNAME_ATTRIBUTE));
			employee.setUsername(context.getStringAttribute(USERNAME_ATTRIBUTE));
			employee.setDepartment(context.getStringAttribute(DEPARTMENT_ATTRIBUTE));
			// employee.setChief(context.getStringAttribute(MANAGER_ATTRIBUTE));
			byte[] guid = (byte[]) context.getObjectAttribute(GUID_ATTRIBUTE);
			employee.setEmployeeId(UuidUtils.asUuid(guid));
			employee.setMail(context.getStringAttribute(EMAIL_ATTRIBUTE));
			employee.setPost(context.getStringAttribute(TITLE_ATTRIBUTE));
			employee.setAuthorities(loadUserAuthorities(context));
			return employee;
		}

		private Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations ctx) {
			String[] groups = ctx.getStringAttributes(GROUPS_ATTRIBUTE);

			if (groups == null) {
				logger.debug("No values for '" + GROUPS_ATTRIBUTE + "' attribute.");

				return AuthorityUtils.NO_AUTHORITIES;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("'" + GROUPS_ATTRIBUTE + "' attribute values: " + Arrays.asList(groups));
			}

			ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(groups.length);

			for (String group : groups) {
				authorities
						.add(new SimpleGrantedAuthority(LdapUtils.getStringValue(LdapUtils.newLdapName(group), "CN")));
			}

			return authorities;
		}

	}

}
