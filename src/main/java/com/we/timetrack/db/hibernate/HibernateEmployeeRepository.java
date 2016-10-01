package com.we.timetrack.db.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Employee;

/**
 * Manages database operations for Employee table.
 * @author pablo
 */
@Repository
@Transactional
public class HibernateEmployeeRepository implements EmployeeRepository {

	private SessionFactory sessionFactory;
	
	@Inject
	public HibernateEmployeeRepository(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
     * Gets employee record with matching employeeId
     */
	public Employee getEmployee(int employeeId){
		
		Employee employee = null;

		employee = (Employee)currentSession().createQuery("from Employee where employeeId = :employeeId")
				.setParameter("employeeId", employeeId)
				.uniqueResult();

		return employee;
	}
	
	/**
	 * Gets employee record with matching username
	 */
	public Employee getEmployee(String username){
		
		Employee employee = null;
		
		employee = (Employee)currentSession().createQuery("from Employee where username = :username")
					.setParameter("username", username)
					.uniqueResult();

		return employee;
	}
	
	/**
	 * Gets employee list
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> getEmployees(){
		
		List<Employee> employees = null;
		
		employees = (List<Employee>)currentSession().createCriteria(Employee.class)
					.list();
		
		return employees;
	}
	
    /**
     * Saves a Employee object
     */
	public void saveEmployee(Employee employee){
		
		currentSession().saveOrUpdate(employee);
	}
	
	/**
	 * Remove a Employee object
	 */
	public void removeEmployee(Employee employee){
		
		currentSession().delete(employee);
	}
	
	/**
	 * Get employee record with matching username
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	/*public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = getEmployee(username);
		if (employee != null) {
			User user = new User(employee);
			return user;
		}
		throw new UsernameNotFoundException(
				"User '" + username + "' not found.");
	}*/
}
