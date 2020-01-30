package com.we.timetrack.db.hibernate;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.ProjectStatus;

@Repository
@Transactional
public class HibernateProjectRepository implements ProjectRepository {
	
	private SessionFactory sessionFactory;
	
	@Inject
	public HibernateProjectRepository(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
     * Gets project record with matching projectId
     */
	public Project getProject(int projectId){
		
		Project project = null;

		project = (Project)currentSession().createQuery("from Project where projectId = :projectId")
					.setParameter("projectId", projectId)
					.uniqueResult();
		return project;
	}
	
	 /**
     * Returns list of all project database records.
     */
	@SuppressWarnings("unchecked")
	public List<Project> getProjects(){
		
		List<Project> projects = null;

		projects = currentSession().createQuery("from Project p ORDER BY p.name")
					.list();
		
		return projects;
	}
	
	 /**
     * Returns list of project database records
     * with matching ProjectStatus.
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjects(ProjectStatus status) {
		
		List<Project> projects = null;

		projects = currentSession().createQuery("from Project p WHERE p.status =:status ORDER BY p.name")
				.setParameter("status", status)
					.list();
		
		return projects;
	}
	
    /**
     * Saves a Project object.
     */
	public void saveProject(Project project){
		
		currentSession().saveOrUpdate(project);
	}
	
    /**
     * Deletes Project record 
     */
	public void deleteProject(Project project){
		
		currentSession().delete(project);
	}
	
	/**
	 * Returns list of all project databse records
	 * with matching employee as leader
	 */
	@SuppressWarnings("unchecked")
	public List<Project> getProjects(UUID employeeId){
		List<Project> projects = currentSession().createCriteria(Project.class)
				.createAlias("projectLeaders", "p")
				.add(Restrictions.eq("p.elements", employeeId))
				.addOrder(Order.asc("name"))
				.list();
		return projects;
	}

	/**
	 * Returns list of project database records
     * with matching ProjectStatus and initialized tasks
     * by fetch join query
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjectsFJ(ProjectStatus status) {
		List<Project> projects = null;

		projects = currentSession().createQuery("from Project p JOIN FETCH p.tasks WHERE p.status =:status ORDER BY p.name")
				.setParameter("status", status)
				.list();
		
		return projects;
	}
}
