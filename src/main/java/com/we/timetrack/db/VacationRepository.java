package com.we.timetrack.db;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.we.timetrack.model.Vacation;

public interface VacationRepository {

	/**
	 * Return list of all vacation's records
	 * @return List<Vacation>
	 */
	public List<Vacation> getVacations();
	
	/**
	 * Return list of all vacation's records with matching
	 * employeeId.
	 * @return List<Vacation>
	 */
	public List<Vacation> getVacations(UUID employeeId);
	
	/**
	 * Return list of all vacation's records later than beginDate and
	 * early than endDate.
	 * @return List<Vacation>
	 */
	public List<Vacation> getVacations(LocalDate beginDate, LocalDate endDate);
	
	/**
	 * Save a Vacation object.
	 * @param vacation
	 */
	public void saveVacation(Vacation vacation);
	
	/**
	 * Save a vacation's list.
	 * @param vacationList
	 */
	public void saveVacations(List<Vacation> vacationList);
	
	/**
	 * Delete a vacation's list.
	 * @param vacationList
	 */
	public void deleteVacations(List<Vacation> vacationList);
}
