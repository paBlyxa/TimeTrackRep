package com.we.timetrack.db;

import java.util.List;
import java.util.Map;
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
	 * Get employees from ActiveDirectory with matching Group
	 * 
	 * @return list of employees
	 */
	public List<Employee> getEmployees(String group);
	
	/**
	 * Gets employee record with matching username
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

	/**
	 * Get map of uuid, employee
	 */
	public Map<UUID, Employee> getEmployeeMap();

}
