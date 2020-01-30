package com.we.timetrack.service.schedule;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.CommunicationException;
import org.springframework.stereotype.Component;

import com.we.timetrack.db.EmployeeRepository;

@Component
public class UpdateAllEmployeesTask implements Runnable, Job {

	private final static Logger logger = LoggerFactory.getLogger(UpdateAllEmployeesTask.class);

	@Autowired
	private EmployeeRepository employeeRepository;


	@Override
	public void run() {
		go();
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		go();
	}

	private void go() {
		logger.info("Execute update all employees");
		try {
			employeeRepository.updateAll();
		} catch (CommunicationException e) {
			logger.error("Couldn't connect to server", e);
		}
	}
}
