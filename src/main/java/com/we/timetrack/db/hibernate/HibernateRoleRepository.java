package com.we.timetrack.db.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.RoleRepository;
import com.we.timetrack.model.Role;

@Repository
@Transactional
public class HibernateRoleRepository implements RoleRepository {

	private SessionFactory sessionFactory;
	
	@Inject
	public HibernateRoleRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Role getRole(int roleId) {
		
		Role role = null;
		
		role = (Role) currentSession().createCriteria(Role.class)
		.add(Restrictions.idEq(roleId))
		.uniqueResult();
		
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRoles() {
		
		List<Role> roles = null;
		
		roles = currentSession().createCriteria(Role.class).list();
		
		return roles;
	}

	@Override
	public void saveRole(Role role) {
		
		currentSession().saveOrUpdate(role);
	}

	@Override
	public void deleteRole(Role role) {
		
		currentSession().delete(role);
	}

	@Override
	public Role getRole(String name) {
		
		Role role = null;
		
		role = (Role) currentSession().createCriteria(Role.class)
		.add(Restrictions.eq("name", name))
		.uniqueResult();
		
		return role;
	}

}
