package com.we.timetrack.db.hibernate;

import java.util.List;
import java.util.UUID;

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
@Repository(value="hibernateEmployeeRepository")
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
	public Employee getEmployee(UUID employeeId){
		
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

	@Override
	public List<Employee> getDirectReports(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> getEmployees(String group) {
		// TODO Auto-generated method stub
		return null;
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
