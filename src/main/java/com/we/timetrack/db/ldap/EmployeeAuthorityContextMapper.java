package com.we.timetrack.db.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Component;

import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Privilege;
import com.we.timetrack.model.Role;

@Component
public class EmployeeAuthorityContextMapper extends EmployeeContextMapper {

	private final static Logger logger = LoggerFactory.getLogger(EmployeeAuthorityContextMapper.class);

	private final static String GROUPS_ATTRIBUTE = "memberOf";

	@Override
	public Employee mapFromContext(Object ctx) throws NamingException {
		DirContextAdapter context = (DirContextAdapter) ctx;
		Employee employee = super.mapFromContext(ctx);
		employee.setRoles(loadUserAuthorities(context));
		return employee;
	}

	private Collection<Role> loadUserAuthorities(DirContextOperations ctx) {

		List<Role> roles = new ArrayList<>();
		String[] groups = ctx.getStringAttributes(GROUPS_ATTRIBUTE);

		logger.debug("'{}' attribute values: {}", GROUPS_ATTRIBUTE, Arrays.asList(groups));

		for (String group : groups) {

			String str = LdapUtils.getStringValue(LdapUtils.newLdapName(group), "CN");
			Role role = new Role();
			role.setName(str);
			Privilege privilege = new Privilege();
			privilege.setName(str);
			List<Privilege> privileges = new ArrayList<>();
			privileges.add(privilege);
			role.setPrivileges(privileges);
			roles.add(role);

		}

		return roles;
	}
}
