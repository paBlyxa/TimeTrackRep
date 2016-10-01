package com.we.timetrack.db.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
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
	public List<Timesheet> getTimesheets(Employee employee){
		
		List<Timesheet> timesheets = null;
		
		timesheets = currentSession().createQuery("from Timesheet where employeeId = :employeeId")
					.setParameter("employeeId", employee.getEmployeeId())
					.list();
		
		return timesheets;
	}
	
    /**
     * Returns list of all timesheet database records with matching
     * employeeId and later task than dateTask.
     */
	@SuppressWarnings("unchecked")
	public List<Timesheet> getTimesheets(Employee employee, Date dateTask){
		
		List<Timesheet> timesheets = null;

		timesheets = currentSession().createQuery("from Timesheet where employeeId = :employeeId  and dateTask >= :dateTask")
					.setParameter("employeeId", employee.getEmployeeId())
					.setParameter("dateTask", dateTask)
					.list();
		
		return timesheets;
	}
	
	/**
	 * Returns list of all timesheet database records with matching
	 * projectId.
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> getTimesheets(Project project){
		
		List<Timesheet> timesheets = null;

		timesheets = currentSession().createQuery("from Timesheet where projectId = :projectId")
					.setParameter("projectId", project.getProjectId())
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
	public List<Map> getEmployeeSummary(Employee employee){
		
		List<Map> result = null;
		
		Query query = currentSession().createQuery("select p.name as project, t.name as task, SUM(s.countTime) as sumHours"
					+ 			" from Project p, Task t, Timesheet s"
					+ 			" where p.projectId = s.project.projectId AND t.taskId = s.task.taskId"
					+ 			" AND s.employee.employeeId = :employeeId "
					+ 			" GROUP BY p.name, t.name")
					.setParameter("employeeId", employee.getEmployeeId());
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			
		result = query.list();

		return result;
	}
}
