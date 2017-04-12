package com.we.timetrack.service.model;

import java.util.Comparator;

import com.we.timetrack.model.Timesheet;

public class TimesheetComparator implements Comparator<Timesheet> {

	@Override
	public int compare(Timesheet t1, Timesheet t2) {
		
		int compareProjectName = t1.getProject().getName().compareTo(t2.getProject().getName());
		if (compareProjectName != 0){
			return compareProjectName;
		}
		
		int compareTaskName = t1.getTask().getName().compareTo(t2.getTask().getName());
		if (compareTaskName != 0){
			return compareTaskName;
		}
		
		return (t1.getId() - t2.getId());
	}

}
