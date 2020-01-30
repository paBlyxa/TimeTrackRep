package com.we.timetrack.service.model;

import java.util.Comparator;

public class TimesheetComparator implements Comparator<TimesheetView> {

	@Override
	public int compare(TimesheetView t1, TimesheetView t2) {
		
		int compareProjectName = t1.getProject().compareTo(t2.getProject());
		if (compareProjectName != 0){
			return compareProjectName;
		}
		
		int compareTaskName = t1.getTask().compareTo(t2.getTask());
		if (compareTaskName != 0){
			return compareTaskName;
		}
		
		return (t1.getId() - t2.getId());
	}

}
