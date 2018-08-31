package com.we.timetrack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.we.timetrack.db.EmployeeRepository;

@Component
@Profile("LdapAndDBEmployees")
public class EventListenerBean {

	private final static Logger logger = LoggerFactory.getLogger(EventListenerBean.class);
	public static int counter = 0;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Application event {}", counter);
		employeeRepository.updateAll();
	}
}
