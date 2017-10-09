package com.we.timetrack.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Vacation;

public class VacationForm {

	private UUID employeeId;
	private String shortName;
	private List<Vacation> vacationList;
	
	public VacationForm(Employee employee){
		employeeId = employee.getEmployeeId();
		shortName = employee.getShortName();
		vacationList = new ArrayList<Vacation>();
	}

	public UUID getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(UUID employeeId) {
		this.employeeId = employeeId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<Vacation> getVacationList() {
		return vacationList;
	}

	public void setVacationList(List<Vacation> vacationList) {
		this.vacationList = vacationList;
	}
	
	public void addVacation(Vacation vacation){
		vacationList.add(vacation);
	}
}
