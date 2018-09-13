package com.we.timetrack.db.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.model.EmployeeComparator;

/**
 * Manages database operations for Employee table.
 * @author pablo
 */
@Repository(value="hibernateEmployeeRepository")
@Profile("DBEmployees")
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
		employees.sort(new EmployeeComparator());
		return employees;
	}

	@Override
	public List<Employee> getDirectReports(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getEmployees(String group) {
		List<Employee> employees = null;
		
		employees = (List<Employee>)currentSession().createCriteria(Employee.class)
					.add(Restrictions.ilike("department", group))
					.add(Restrictions.eq("active", Boolean.TRUE))
					.list();
		employees.sort(new EmployeeComparator());
		return employees;
	}
	
	/**
	 * Get map of uuid, employee
	 */
	public Map<UUID, Employee> getEmployeeMap() {
		List<Employee> employees = getEmployees();
		Map<UUID, Employee> employeeMap = new HashMap<UUID, Employee>();
		for (Employee employee : employees) {
			employeeMap.put(employee.getEmployeeId(), employee);
		}
		return employeeMap;
	}
	
	/**
	 * Either save(Object) or update(Object) the given instance,
	 * depending upon resolution of the unsaved-value checks
	 * (see the manual for discussion of unsaved-value checking). 
	 */
	public void saveOrUpdate(Employee employee) {
		currentSession().saveOrUpdate(employee);
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
