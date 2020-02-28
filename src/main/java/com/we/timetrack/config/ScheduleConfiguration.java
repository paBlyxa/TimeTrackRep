package com.we.timetrack.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.we.timetrack.service.AutoWiringSpringBeanJobFacotry;
import com.we.timetrack.service.model.ScheduleType;
import com.we.timetrack.service.schedule.RememberJob;
import com.we.timetrack.service.schedule.UpdateAllEmployeesTask;

@Configuration
@PropertySource({ "classpath:timex.properties" })
public class ScheduleConfiguration {
	
	private final static Logger logger = LoggerFactory.getLogger(ScheduleConfiguration.class);

	@Autowired
	private Environment env;
	@Autowired
	private ApplicationContext applicationContext;
	
	@PostConstruct
	public void init() {
		logger.info("Create schedule configuration");
	}
	
	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutoWiringSpringBeanJobFacotry jobFactory = new AutoWiringSpringBeanJobFacotry();
		logger.debug("Configuring job factory");
		
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}
	
	@Bean
	@Profile("!test")
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setJobFactory(springBeanJobFactory());
		factory.setTriggers(triggerEveryWeekBean().getObject(), triggerEveryMonthBean().getObject(),
				triggerEveryQuarterBean().getObject(), triggerUpdateAllEmployeesTask().getObject());
		return factory;
	}
	
	@Bean
	@Profile("!test")
	public CronTriggerFactoryBean triggerUpdateAllEmployeesTask() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(jobDetailFactoryBeanUpdate().getObject());
		stFactory.setStartDelay(3000);
		stFactory.setName("triggerEveryMonday");
		stFactory.setGroup("myGroup");
		stFactory.setCronExpression("0 0 21 ? * 2");// Fire at 21:00 every Monday [0 0 21 ? * MON]
		return stFactory;
	}

	@Bean
	@Profile("!test")
	public CronTriggerFactoryBean triggerEveryWeekBean() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(jobDetailFactoryBeanWeek().getObject());
		stFactory.setStartDelay(3000);
		stFactory.setName("triggerEveryWeek");
		stFactory.setGroup("myGroup");
		stFactory.setCronExpression("0 0 6 ? * 2");// Fire at 6:00 every Monday [0 0 6 ? * MON]
		return stFactory;
	}

	@Bean
	@Profile("!test")
	public CronTriggerFactoryBean triggerEveryMonthBean() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(jobDetailFactoryBeanMonth().getObject());
		stFactory.setStartDelay(3000);
		stFactory.setName("triggerEveryMonth");
		stFactory.setGroup("myGroup");
		stFactory.setCronExpression("0 0 7 ? * 2#1");// Fire at 7:00 on the first Monday of month [0 0 7 ? * 2#1]
		return stFactory;
	}

	@Bean
	@Profile("!test")
	public CronTriggerFactoryBean triggerEveryQuarterBean() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(jobDetailFactoryBeanQuarter().getObject());
		stFactory.setStartDelay(3000);
		stFactory.setName("triggerEveryQuarter");
		stFactory.setGroup("myGroup");
		stFactory.setCronExpression("0 30 6 ? 1,4,7,10 2#1");// Fire at 6:30 on the first Monday of Quarter [0 30 6 ?
																// 1,4,7,10 2#1]
		return stFactory;
	}

	@Bean
	public JobDetailFactoryBean jobDetailFactoryBeanUpdate() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(UpdateAllEmployeesTask.class);
		factory.setGroup("myGroup");
		factory.setName("UpdateAllEmployeesTask");
		return factory;
	}
	
	@Bean
	public JobDetailFactoryBean jobDetailFactoryBeanWeek() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(RememberJob.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleType", ScheduleType.WEEK);
		factory.setJobDataAsMap(map);
		factory.setGroup("myGroup");
		factory.setName("RememberJobEveryWeek");
		return factory;
	}

	@Bean
	public JobDetailFactoryBean jobDetailFactoryBeanMonth() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(RememberJob.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleType", ScheduleType.MONTH);
		factory.setJobDataAsMap(map);
		factory.setGroup("myGroup");
		factory.setName("RememberJobEveryMonth");
		return factory;
	}

	@Bean
	public JobDetailFactoryBean jobDetailFactoryBeanQuarter() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(RememberJob.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleType", ScheduleType.QUARTER);
		factory.setJobDataAsMap(map);
		factory.setGroup("myGroup");
		factory.setName("RememberJobEveryQuarter");
		return factory;
	}

	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getRequiredProperty("mail.host"));
		mailSender.setPort(env.getRequiredProperty("mail.port", Integer.class));
		mailSender.setUsername(env.getRequiredProperty("mail.user"));
		mailSender.setPassword(env.getRequiredProperty("mail.password"));
		return mailSender;
	}

	@Bean
	public SimpleMailMessage templateMessage() {
		SimpleMailMessage template = new SimpleMailMessage();
		template.setFrom(env.getProperty("mail.from"));
		template.setSubject(env.getProperty("mail.subject"));
		return template;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setDaemon(true);
		taskExecutor.setMaxPoolSize(3);
		return taskExecutor;
	}
}
