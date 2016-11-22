package com.we.timetrack.db;

import java.util.List;
import java.util.UUID;

import com.we.timetrack.model.Employee;

/**
 * Repository interface with operations for {@link Employee} persistence.
 * @author pablo
 */
public interface EmployeeRepository {
	
	/**
     * Gets employee record with matching employeeId
     */
	public Employee getEmployee(UUID employeeId);
	
	/**
	 * Gets employee record with matching username
	 */
	public Employee getEmployee(String username);
	
	/**
	 * Gets employee list
	 */
	public List<Employee> getEmployees();
	
	
	/**
	 * Get employee record with matching username
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	//public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	/**
	 * Gets direct reports of employee
	 * @param employee
	 * @return List<Employee> - direct reports
	 */
	public List<Employee> getDirectReports(Employee employee);
}
