package com.we.timetrack.service.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.we.timetrack.model.Employee;
import com.we.timetrack.model.EmployeeProperty;
import com.we.timetrack.model.Role;

public class EmployeePropertyForm extends EmployeeProperty {

	private UUID employeeId;
	private String viewName;
	private List<String> roles;
	private Collection<EmployeeProperty> properties;
	private ScheduleType scheduleType;
	private boolean autoSave;
	private boolean active;
	
	public EmployeePropertyForm() {
		
	}
	
	public EmployeePropertyForm(Employee employee) {
		this.employeeId = employee.getEmployeeId();
		this.viewName = employee.getShortName();
		this.active = employee.isActive();
		this.roles = new ArrayList<String>();
		for (Role role : employee.getRoles()) {
			roles.add(role.getName());
		}
		this.properties = employee.getProperties();
		this.scheduleType = ScheduleType.OFF;
		for (EmployeeProperty prop : this.properties) {
			if (prop.getName().equals(PROPERTIES.remember.name())) {
				this.scheduleType = ScheduleType.valueOf(prop.getValue());
			}
			if (prop.getName().equals(PROPERTIES.autoSave.name())) {
				this.autoSave = Boolean.parseBoolean(prop.getValue());
			}
		}
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public UUID getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(UUID employeeId) {
		this.employeeId = employeeId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Collection<EmployeeProperty> getProperties() {
		return properties;
	}

	public void setProperties(Collection<EmployeeProperty> properties) {
		this.properties = properties;
	}

	public ScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

	public boolean isAutoSave() {
		return autoSave;
	}

	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
