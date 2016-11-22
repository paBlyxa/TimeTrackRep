package com.we.timetrack.db.hibernate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Timesheet;

/**
 * Manages database operations for Timesheet table.
 * @author pablo
 */
@Repository
@Transactional
public class HibernateTimesheetRepository implements TimesheetRepository {

	private SessionFactory sessionFactory;
	
	@Inject
	public HibernateTimesheetRepository(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
    /**
     * Returns list of all timesheet database records with matching 
     * employeeId.
     */
	@SuppressWarnings("unchecked")
	public List<Timesheet> getTimesheets(UUID employeeId){
		
		List<Timesheet> timesheets = null;
		
		timesheets = currentSession().createCriteria(Timesheet.class)
				.add(Restrictions.eq("employeeId", employeeId))
					.list();
		
		return timesheets;
	}
	
    /**
     * Returns list of all timesheet database records with matching
     * employeeId and later task than dateTask.
     */
	@SuppressWarnings("unchecked")
	public List<Timesheet> getTimesheets(UUID employeeId, LocalDate dateTask){
		
		List<Timesheet> timesheets = null;

		timesheets = currentSession().createCriteria(Timesheet.class)
				.add(Restrictions.eq("employeeId", employeeId))
				.add(Restrictions.ge("dateTask", dateTask))
				.list();
		
		return timesheets;
	}
	
	/**
     * Returns list of all timesheet database records with matching
     * employeeId and later task than beginDate and early then endDate.
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Timesheet> getTimesheets(UUID employeeId, LocalDate beginDate, LocalDate endDate) {

		return currentSession().createCriteria(Timesheet.class)
				.add(Restrictions.eq("employeeId", employeeId))
				.add(Restrictions.between("dateTask", beginDate, endDate))
				.list();
	}
	
	/**
	 * Returns list of all timesheet database records with matching
	 * projectId.
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> getTimesheets(int projectId){
		
		List<Timesheet> timesheets = null;

		timesheets = currentSession().createQuery("from Timesheet where projectid = :projectId")
					.setParameter("projectId", projectId)
					.list();

		return timesheets;
	}

	/**
	 * Returns list of all timesheet database records with matching
	 * projectId  and later task than beginDate and early then endDate.
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> getTimesheets(int projectId, LocalDate beginDate, LocalDate endDate){
		
		List<Timesheet> timesheets = null;

		timesheets = currentSession().createCriteria(Timesheet.class)
				.add(Restrictions.eq("project.projectId", projectId))
				.add(Restrictions.between("dateTask", beginDate, endDate))
				.list();

		return timesheets;
	}
	
	/**
	 * Returns list of all timesheet database records.
	 * For test only.
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> getTimesheets(){
		
		List<Timesheet> timesheets = null;
		
		timesheets = currentSession().createQuery("from Timesheet")
					.list();
			
		return timesheets;
	}
	
    /**
     * Saves a Timesheet object.
     */
	public void saveTimesheet(Timesheet timesheet){
		
		currentSession().saveOrUpdate(timesheet);
	}

	/**
     * Saves a list of timesheet's objects.
     */
	public void saveTimesheets(List<Timesheet> timesheets) {

		for (Timesheet timesheet : timesheets){
			currentSession().saveOrUpdate(timesheet);
		}
	}
	
    /**
     * Deletes Timesheet record 
     */
	public void deleteTimesheet(Timesheet timesheet){
		
		currentSession().delete(timesheet);
	}
	
    /**
     * Returns a database Timesheet record with matching timesheetId
     * and demonstrates how a record can be locked using Hibernate.
     */
	public Timesheet getTimesheet(int timesheetId, boolean doLock){
		
		Timesheet timesheet = null;
		
		if (doLock){
			timesheet = currentSession().get(Timesheet.class, timesheetId, LockMode.PESSIMISTIC_WRITE);
		}
		else{
			timesheet = currentSession().get(Timesheet.class, timesheetId);
		}

		return timesheet;
	}
	
	/**
	 * Returns a summary hours group by projects and task
	 * with mathcing employeeId
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getEmployeeSummary(UUID employeeId){
		
		List<Map> result = null;
		
		Query query = currentSession().createQuery("select p.name as project, t.name as task, SUM(s.countTime) as sumHours"
					+ 			" from Project p, Task t, Timesheet s"
					+ 			" where p.projectId = s.project.projectId AND t.taskId = s.task.taskId"
					+ 			" AND s.employee.employeeid = :employeeId "
					+ 			" GROUP BY p.name, t.name")
					.setParameter("employeeId", employeeId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			
		result = query.list();

		return result;
	}
}
