package com.we.timetrack.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.we.timetrack.model.Employee;

public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2392325862517336326L;
	
	private Employee employee;

	public User(){
		
	}
	
	public User(Employee employee){
		this.employee = employee;
	}
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (employee != null){
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			return authorities;
		}
		return null;
	}

	@Override
	public String getPassword() {
		if (employee != null){
			return employee.getPassword();
		}
		return null;
	}

	@Override
	public String getUsername() {
		if (employee != null){
			return employee.getUsername();
		}
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
