package com.we.timetrack.db.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.EmployeePropertyRepository;
import com.we.timetrack.model.EmployeeProperty;

/**
 * Manages database operations for EmployeeProperty table.
 * @author pablo
 */
@Repository(value="hibernateEmployeePropertyRepository")
@Transactional
public class HibernateEmployeePropertyRepository implements EmployeePropertyRepository {

	private SessionFactory sessionFactory;
	
	@Inject
	public HibernateEmployeePropertyRepository(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * Get Employee's list who need remember
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeProperty> getEmployeesInListRemember() {
		List<EmployeeProperty> employeeProperties = null;
		
		employeeProperties = (List<EmployeeProperty>)currentSession().createCriteria(EmployeeProperty.class)
				.add(Restrictions.and(Restrictions.eq("name", "remember"), Restrictions.ge("value", 1))).list();
		
		return employeeProperties;
	}

	/**
	 * Save property.
	 */
	@Override
	public void save(EmployeeProperty property) {
		
		currentSession().saveOrUpdate(property);		
	}

	/**
	 * Save list of properties.
	 */
	@Override
	public void saveList(List<EmployeeProperty> list) {
		
		list.forEach(property -> currentSession().saveOrUpdate(property));		
	}

	/**
	 * Get list with matches property name.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmployeeProperty> get(String propertyName) {
		
		List<EmployeeProperty> employeeProperties = null;
		
		employeeProperties = (List<EmployeeProperty>)currentSession().createCriteria(EmployeeProperty.class)
				.add(Restrictions.eq("name", propertyName)).list();
		
		return employeeProperties;
	}

}
