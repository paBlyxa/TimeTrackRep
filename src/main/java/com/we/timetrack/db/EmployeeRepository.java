package com.we.timetrack.db;

import java.util.List;

import com.we.timetrack.model.Employee;

/**
 * Repository interface with operations for {@link Employee} persistence.
 * @author pablo
 */
public interface EmployeeRepository {
	
	/**
     * Gets employee record with matching employeeId
     */
	public Employee getEmployee(int employeeId);
	
	/**
	 * Gets employee record with matching username
	 */
	public Employee getEmployee(String username);
	
	/**
	 * Gets employee list
	 */
	public List<Employee> getEmployees();
	
	/**
     * Saves a Employee object
     */
	public void saveEmployee(Employee employee);
	
	/**
	 * Remove a Employee object
	 */
	public void removeEmployee(Employee employee);
	
	/**
	 * Get employee record with matching username
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	//public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
