package com.we.timetrack.db;

import java.util.List;

import com.we.timetrack.model.EmployeeProperty;

public interface EmployeePropertyRepository {

	/**
	 * Get Employee's list who need remember.
	 */
	public List<EmployeeProperty> getEmployeesInListRemember();
	
	/**
	 * Save list of employees, who need remember.
	 */
	public void saveList(List<EmployeeProperty> list);
	
	/**
	 * Get list with matches property name.
	 */
	public List<EmployeeProperty> get(String propertyName);

	/**
	 * Save property.
	 */
	public void save(EmployeeProperty property);
}
