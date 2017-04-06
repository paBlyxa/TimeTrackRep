package com.we.timetrack.service.model;

import java.util.Comparator;

import com.we.timetrack.model.Employee;

public class EmployeeComparator implements Comparator<Employee> {

	public int compare(Employee employee1, Employee employee2){
		
		int compareSurname = employee1.getSurname().compareTo(employee2.getSurname());
		if (compareSurname != 0){
			return compareSurname;
		}
		
		return employee1.getName().compareTo(employee2.getName());
		
	}
}
