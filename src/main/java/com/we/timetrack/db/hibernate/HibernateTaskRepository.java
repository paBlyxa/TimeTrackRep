package com.we.timetrack.db.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.model.Department;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.TaskStatus;

@Repository
@Transactional
public class HibernateTaskRepository implements TaskRepository {
	
	private SessionFactory sessionFactory;
	
	@Inject
	public HibernateTaskRepository(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
     * Gets task record with matching taskId
     */
	public Task getTask(int taskId){
		
		Task task = null;
		
		task = (Task)currentSession().createQuery("from Task where taskId = :taskId")
					.setParameter("taskId", taskId)
					.uniqueResult();
		return task;
	}
	
	 /**
     * Returns list of all task database records.
     */
	@SuppressWarnings("unchecked")
	public List<Task> getTasks(){
		
		List<Task> tasks = null;
		
		tasks = currentSession().createQuery("select distinct t from Task t LEFT JOIN FETCH t.departments ORDER BY t.name")
					.list();
		
		return tasks;
	}
	
	 /**
     * Returns list of all task database records
     * with matching TaskStatus.
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getTasks(TaskStatus status) {
		
		List<Task> tasks = null;

		tasks = currentSession().createQuery("from Task t WHERE t.status =:status ORDER BY t.name")
				.setParameter("status", status)
					.list();
		
		return tasks;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Returns list of task database records
	 * with matching TaskStatus, Department, and free of projects.
	 */
	public List<Task> getFreeTasks(TaskStatus status, Department department){
		
		List<Task> tasks = null;
		
		if (department != null) {
			tasks = currentSession().createQuery("from Task t "
					+ "WHERE (size(t.departments) = 0 "
					+ "or :dep in elements(t.departments)) "
					+ "and t.status = :status "
					+ "and size(t.projects) = 0 "
					+ "ORDER BY t.name")
					.setParameter("status", status)
					.setParameter("dep", department)
						.list();
		} else {

			tasks = currentSession().createQuery("from Task t WHERE t.status =:status ORDER BY t.name")
					.setParameter("status", status)
						.list();
		}
		return tasks;
	}
	
	 /**
     * Saves a Task object.
     */
	public void saveTask(Task task){
		
		currentSession().saveOrUpdate(task);
	}
	
    /**
     * Deletes Task record 
     */
	public void deleteTask(Task task){

		currentSession().delete(task);
	}

}
