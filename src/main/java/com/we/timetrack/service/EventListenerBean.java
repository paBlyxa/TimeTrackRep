package com.we.timetrack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.we.timetrack.service.schedule.UpdateAllEmployeesTask;

@Component
@Profile("LdapAndDBEmployees")
public class EventListenerBean {

	private final static Logger logger = LoggerFactory.getLogger(EventListenerBean.class);
	public static int counter = 0;
	
	@Autowired
	private UpdateAllEmployeesTask updateAllEmployeesTask;
	@Autowired
	private TaskExecutor taskExecutor;
	
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Application event {}, counter={}",event.getApplicationContext().getId(), counter++);
		//employeeRepository.updateAll();
		if (!event.getApplicationContext().getDisplayName().contains("dispatcher")) {
			taskExecutor.execute(updateAllEmployeesTask);
		}
	}
}
