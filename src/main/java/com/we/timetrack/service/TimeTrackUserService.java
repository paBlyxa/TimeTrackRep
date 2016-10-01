package com.we.timetrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Employee;

@Service
public class TimeTrackUserService implements UserDetailsService {
	
	@Autowired
	private EmployeeRepository manager;
	
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = manager.getEmployee(username);
		if (employee != null) {
			User user = new User(employee);
			return user;
		}
		throw new UsernameNotFoundException(
				"User '" + username + "' not found.");
	}

}
