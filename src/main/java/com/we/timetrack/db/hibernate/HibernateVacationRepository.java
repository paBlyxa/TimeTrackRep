package com.we.timetrack.db.hibernate;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.VacationRepository;
import com.we.timetrack.model.Vacation;

/**
 * Manages database operations for Vacation table.
 * @author pablo
 */
@Repository
@Transactional
public class HibernateVacationRepository implements VacationRepository {

	private SessionFactory sessionFactory;
	
	@Inject
	public HibernateVacationRepository(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * Return list of all vacation's records
	 * @return List<Vacation>
	 */
	@SuppressWarnings("unchecked")
	public List<Vacation> getVacations() {
		
		List<Vacation> vacationList = null;
		
		vacationList = currentSession().createCriteria(Vacation.class)
				.list();
		
		return vacationList;
	}
	
	/**
	 * Return list of all vacation's records with matching
	 * employeeId.
	 * @return List<Vacation>
	 */
	@SuppressWarnings("unchecked")
	public List<Vacation> getVacations(UUID employeeId) {
		
		List<Vacation> vacationList = null;
		
		vacationList = currentSession().createCriteria(Vacation.class)
				.add(Restrictions.eq("employeeId", employeeId))
				.list();
		
		return vacationList;
	}
	
	/**
	 * Return list of all vacation's records later than beginDate and
	 * early than endDate.
	 * @return List<Vacation>
	 */
	@SuppressWarnings("unchecked")
	public List<Vacation> getVacations(LocalDate beginDate, LocalDate endDate) {
		
		List<Vacation> vacationList = null;
		
		vacationList = currentSession().createCriteria(Vacation.class)
				.add(Restrictions.between("startDate", beginDate, endDate))
				.list();
		
		return vacationList;
	}
	
	/**
	 * Save a Vacation object.
	 * @param vacation
	 */
	public void saveVacation(Vacation vacation){
		
		currentSession().saveOrUpdate(vacation);
	}
	
	/**
	 * Save a vacation's list.
	 * @param vacationList
	 */
	public void saveVacations(List<Vacation> vacationList){
		
		for (Vacation vacation : vacationList){
			currentSession().saveOrUpdate(vacation);
		}
	}

	/**
	 * Delete a vacation from vacationList.
	 * @param vacationList
	 */
	@Override
	public void deleteVacations(List<Vacation> vacationList) {
		
		for (Vacation vacation : vacationList){
			currentSession().delete(vacation);
		}
		
	}

}
