package com.we.timetrack.db.hibernate;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.CalendarRepository;
import com.we.timetrack.model.Day;
import com.we.timetrack.model.DayStatus;

/**
 * Manages database operations for WorkCalendar table.
 * 
 * @author pablo
 */
@Repository(value = "hibernateCalendarRepository")
@Transactional
public class HibernateCalendarRepository implements CalendarRepository {

	private SessionFactory sessionFactory;

	@Inject
	public HibernateCalendarRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Return day with mathichg date.
	 */
	@Override
	public Day getDay(LocalDate date) {

		return (Day) currentSession().createCriteria(Day.class).add(Restrictions.eq("dateDay", date)).uniqueResult();
	}

	/**
	 * Return list of all days.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getDays() {

		return currentSession().createCriteria(Day.class).list();
	}

	/**
	 * Return list of all days later than beginDate and early then endDate.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getDays(LocalDate beginDate, LocalDate endDate) {
		return currentSession().createCriteria(Day.class).add(Restrictions.between("dateDay", beginDate, endDate))
				.list();
	}

	/**
	 * Return list of weekend's days.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getWeekends() {
		return currentSession().createCriteria(Day.class).add(Restrictions.eq("status", DayStatus.Weekend)).list();
	}

	/**
	 * Return list of weekend's days later than beginDate and early then
	 * endDate.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getWeekends(LocalDate beginDate, LocalDate endDate) {

		return currentSession().createCriteria(Day.class).add(Restrictions.eq("status", DayStatus.Weekend))
				.add(Restrictions.between("dateDay", beginDate, endDate)).list();
	}

	/**
	 * Return list of weekend's days in year.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getWeekends(int year) {
		// TODO
		return currentSession().createCriteria(Day.class).add(Restrictions.eq("status", DayStatus.Weekend)).list();
	}

	/**
	 * Return list of short days.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getShortDays() {
		return currentSession().createCriteria(Day.class).add(Restrictions.eq("status", DayStatus.Short)).list();
	}

	/**
	 * Return list of short days later than beginDate and early then endDate.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getShortDays(LocalDate beginDate, LocalDate endDate) {
		return currentSession().createCriteria(Day.class).add(Restrictions.eq("status", DayStatus.Short))
				.add(Restrictions.between("dateDay", beginDate, endDate)).list();
	}

	/**
	 * Return list of short days in year.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Day> getShortDays(int year) {
		// TODO
		return currentSession().createCriteria(Day.class).add(Restrictions.eq("status", DayStatus.Short)).list();
	}

	/**
	 * Saves a Day's object.
	 */
	@Override
	public void saveDay(Day day) {

		currentSession().saveOrUpdate(day);
	}

	/**
	 * Saves a list of Day's objects.
	 */
	@Override
	public void saveDays(List<Day> days) {

		for (Day day : days) {
			Day dayTemp = (Day) currentSession().createCriteria(Day.class)
					.add(Restrictions.eq("dateDay", day.getDateDay())).uniqueResult();
			if (dayTemp != null) {
				dayTemp.setDateDay(day.getDateDay());
				dayTemp.setStatus(day.getStatus());
				currentSession().saveOrUpdate(dayTemp);
			} else {
				currentSession().saveOrUpdate(day);
			}
		}

	}

	/**
	 * Remove a Day's object.
	 */
	@Override
	public void remove(Day day) {
		currentSession().delete(day);
	}

}
