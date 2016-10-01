package com.we.timetrack.db.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.model.Task;

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

		tasks = currentSession().createQuery("from Task")
					.list();
		
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
