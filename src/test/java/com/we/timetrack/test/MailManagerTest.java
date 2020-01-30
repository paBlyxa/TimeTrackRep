package com.we.timetrack.test;


import org.junit.Test;

import com.we.timetrack.config.ScheduleConfiguration;
import com.we.timetrack.service.mail.MailManager;

public class MailManagerTest {

	@Test
	public void test() {
		MailManager mm = new MailManager();
		ScheduleConfiguration conf = new ScheduleConfiguration();
		mm.setMailSender(conf.mailSender());
		mm.setTemplateMessage(conf.templateMessage());
		//mm.send();
	}

}
