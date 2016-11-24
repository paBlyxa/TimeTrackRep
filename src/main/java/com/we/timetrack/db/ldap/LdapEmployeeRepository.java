package com.we.timetrack.db.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.we.timetrack.util.UuidUtils;

import org.jboss.logging.Logger;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository(value="ldapEmployeeRepository")
public class LdapEmployeeRepository implements EmployeeRepository {
	
	private static Logger logger = Logger.getLogger("db");

	@Override
	public Employee getEmployee(UUID employeeId) {
		List<Employee> employees = getLdapTemplate().search(query()
				.base("OU=Проектирования и конструирования,dc=we,dc=ru")
				.filter("(&(objectClass=person)(objectGUID=" + UuidUtils.convertToByteString(employeeId) + "))"),
				getContextMapper());
		return employees.isEmpty() ? null : employees.get(0);
	}

	@Override
	public Employee getEmployee(String username) {
		List<Employee> employees = getLdapTemplate().search(query()
				.base("OU=Проектирования и конструирования,dc=we,dc=ru")
				.where("sAMAccountName").is(username),
				getContextMapper());
		return employees.get(0);
	}

	@Override
	public List<Employee> getEmployees() {
		LdapQuery query = query()
				.base("OU=Проектирования и конструирования,dc=we,dc=ru")
				//.attributes("givenName", "sn", "sAMAccountName", "department", "mail", "title", "memberOf")
				.where("objectclass").is("person");
		
		return getLdapTemplate().search(query, getContextMapper());
	}
	
	public Employee get(String dn){
		return getLdapTemplate().lookup(dn,getContextMapper());
	}
	
	public List<Employee> getDirectReports(Employee employee){
		
		List<Employee> directReports = new ArrayList<>();
		List<Employee> checked = new ArrayList<>();
		getDirectReports(employee, directReports, checked);
		return directReports;
	}
	
	private void getDirectReports(Employee employee, List<Employee> directReports, List<Employee> checked){
		
		if (!checked.contains(employee)){
			DirContextOperations context = getLdapTemplate().searchForContext(query()
					.base("OU=Проектирования и конструирования,dc=we,dc=ru")
					.where("sAMAccountName").is(employee.getUsername()));
			String[] strDirectReports = context.getStringAttributes("directReports");
			checked.add(employee);
			if (strDirectReports != null) {
				for (String directReport : strDirectReports){
					Employee empl = get(directReport);
					getDirectReports(empl, directReports, checked);
					if (!directReports.contains(empl)){
						directReports.add(empl);
					}
				}
			}
		}
	}
	
	
	private LdapTemplate getLdapTemplate() {
		LdapContextSource contextSource = new LdapContextSource();
		Map<String, Object> props = new HashMap<>();
		props.put("java.naming.ldap.attributes.binary","objectGUID");
		contextSource.setUrl("ldap://wemaster1:389");
		contextSource.setUserDn("fakadey@we.ru");
		contextSource.setPassword("niceDay!");
		contextSource.setBaseEnvironmentProperties(props);
		try {
			contextSource.afterPropertiesSet();
		}
		catch(Exception e){
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
			DirContextAdapter context = (DirContextAdapter)ctx;
			Employee employee = new Employee();
			employee.setName(context.getStringAttribute("givenName"));
			employee.setSurname(context.getStringAttribute("sn"));
			employee.setUsername(context.getStringAttribute("sAMAccountName"));
			employee.setDepartment(context.getStringAttribute("department"));
			//employee.setChief(context.getStringAttribute("manager"));
			byte[] guid = (byte[]) context.getObjectAttribute("objectguid");
			employee.setEmployeeId(UuidUtils.asUuid(guid));
			employee.setMail(context.getStringAttribute("mail"));
			employee.setPost(context.getStringAttribute("title"));
			employee.setAuthorities(loadUserAuthorities(context));
			return employee;
		}
		
		private Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations ctx) {
			String[] groups = ctx.getStringAttributes("memberOf");
			
			if (groups == null) {
				logger.debug("No values for 'memberOf' attribute.");
			
				return AuthorityUtils.NO_AUTHORITIES;
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("'memberOf' attribute values: " + Arrays.asList(groups));
			}
			
			ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(groups.length);
			
			for (String group : groups) {
				authorities.add(new SimpleGrantedAuthority(LdapUtils.getStringValue(LdapUtils.newLdapName(group), "CN")));
			}
			
			return authorities;
		}
	
	}
	  
}
