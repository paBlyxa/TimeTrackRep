package com.we.timetrack.service;

import java.util.Properties;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Employee;

@Service
public class ActiveDirectoryUserService implements UserDetailsService, AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryUserService.class);
	private static final String ldapUrl = "ldap://we.ru";
	private static final String userSuffix = "@we.ru";

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee loadUserByUsername(String username) throws UsernameNotFoundException {

		Employee employee = employeeRepository.getEmployee(username);
		if (employee != null) {
			return employee;
		} else {
			UsernameNotFoundException ex = new UsernameNotFoundException("User '" + username + "' not found.");
			logger.warn("Could not login", ex);
			throw ex;
		}

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String name = authentication.getName();
		final String password = authentication.getCredentials().toString();
		logger.debug("Performing login into AD with credentials '{}'/'{}'", name, password.replaceAll(".", "*"));

		DirContext ctx = null;
		try {
			ctx = getDirContext(name + userSuffix, password);
			logger.debug("User '{}' has been successfully logged on", name);
			final Employee employee = loadUserByUsername(name);
			return new UsernamePasswordAuthenticationToken(employee, password, employee.getAuthorities());
		} catch (CommunicationException e) {
			logger.warn("Could not connect to '" + ldapUrl + "'", e);
			throw new AuthenticationServiceException("Could not connect to '" + ldapUrl + "'.");
		} catch (NamingException ex) {
			logger.warn("Could not login into '" + ldapUrl + "'", ex);
			throw new BadCredentialsException("Invalid username or password.");
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException ex) {
					logger.warn("Could not close DirContext", ex);
				}
			}
		}

	}

	private DirContext getDirContext(String username, String password) throws NamingException {
		final Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.SECURITY_AUTHENTICATION, "simple");
		props.put(Context.SECURITY_PRINCIPAL, username);
		props.put(Context.SECURITY_CREDENTIALS, password);
		props.put(Context.PROVIDER_URL, ldapUrl);

		return new InitialDirContext(props);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
