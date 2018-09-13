package com.we.timetrack.db.ldap;

import javax.naming.NamingException;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.stereotype.Component;

import com.we.timetrack.model.Employee;
import com.we.timetrack.util.UuidUtils;

@Component
public class EmployeeContextMapper implements ContextMapper<Employee> {

	// Attributes
	private final static String NAME_ATTRIBUTE = "givenName";
	private final static String SURNAME_ATTRIBUTE = "sn";
	private final static String USERNAME_ATTRIBUTE = "sAMAccountName";
	private final static String DEPARTMENT_ATTRIBUTE = "department";
	private final static String GUID_ATTRIBUTE = "objectguid";
	private final static String EMAIL_ATTRIBUTE = "mail";
	private final static String TITLE_ATTRIBUTE = "title";
	
	@Override
	public Employee mapFromContext(Object ctx) throws NamingException {
		// Read attributes from active directory
		DirContextAdapter context = (DirContextAdapter) ctx;
		Employee employee = new Employee();
		employee.setName(context.getStringAttribute(NAME_ATTRIBUTE));
		employee.setSurname(context.getStringAttribute(SURNAME_ATTRIBUTE));
		employee.setUsername(context.getStringAttribute(USERNAME_ATTRIBUTE));
		employee.setDepartment(context.getStringAttribute(DEPARTMENT_ATTRIBUTE));
		byte[] guid = (byte[]) context.getObjectAttribute(GUID_ATTRIBUTE);
		employee.setEmployeeId(UuidUtils.asUuid(guid));
		employee.setMail(context.getStringAttribute(EMAIL_ATTRIBUTE));
		employee.setPost(context.getStringAttribute(TITLE_ATTRIBUTE));
		return employee;
	}

}
