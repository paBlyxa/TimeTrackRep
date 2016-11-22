package com.we.timetrack.service;

import java.util.Properties;
import java.util.logging.Level;

import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
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
	
	private static final Logger logger = Logger.getLogger(ActiveDirectoryUserService.class.getName());
	private static final String ldapUrl = "ldap://wemaster1:3268";
	private static final String userSuffix = "@we.ru";
	
	@Autowired
	@Qualifier("ldapEmployeeRepository")
	private EmployeeRepository employeeRepository;
	
	public Employee loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Employee employee = employeeRepository.getEmployee(username);
		if (employee != null) {
			return employee;
		}
		else {
			UsernameNotFoundException ex = new UsernameNotFoundException("User '" + username + "' not found.");	
			logger.log(Level.SEVERE, "Could not login", ex);
			throw ex;
		}
		
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String name = authentication.getName();
		final String password = authentication.getCredentials().toString();
		logger.log(Level.FINE, "Performing logon into AD with credentials '" + name + "'/'" + password.replaceAll(".", "*") + "'");
		
		DirContext ctx = null;
		try {
			ctx = getDirContext(name + userSuffix, password);
			logger.log(Level.FINE, "User '" + name + "' has been successfully logged on");
			final Employee employee = loadUserByUsername(name);
			return new UsernamePasswordAuthenticationToken(employee, password, employee.getAuthorities());
		} catch (NamingException ex) {
			logger.log(Level.SEVERE, "Could not login into '" + ldapUrl + "'", ex);
			throw new BadCredentialsException(ex.getMessage());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException ex) {
					logger.log(Level.WARNING, "Could not close DirContext", ex);
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
