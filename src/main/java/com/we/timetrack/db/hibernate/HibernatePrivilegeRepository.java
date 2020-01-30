package com.we.timetrack.db.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.PrivilegeRepository;
import com.we.timetrack.model.Privilege;

@Repository
@Transactional
public class HibernatePrivilegeRepository implements PrivilegeRepository {

	private SessionFactory sessionFactory;
	
	@Inject
	public HibernatePrivilegeRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Privilege getPrivilege(int privilegeId) {
		
		Privilege privilege = null;
		
		privilege = (Privilege) currentSession().createCriteria(Privilege.class)
				.add(Restrictions.idEq(privilegeId))
				.uniqueResult();
		
		return privilege;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Privilege> getPrivileges() {

		List<Privilege> privileges = null;
		
		privileges = currentSession().createCriteria(Privilege.class).list();
		
		return privileges;
	}

	@Override
	public void savePrivilege(Privilege privilege) {
		
		currentSession().saveOrUpdate(privilege);
	}

	@Override
	public void deletePrivilege(Privilege privilege) {
		
		currentSession().delete(privilege);
	}

}
