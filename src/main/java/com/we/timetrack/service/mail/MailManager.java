package com.we.timetrack.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailManager {

	private final static Logger logger = LoggerFactory.getLogger(MailManager.class);
	
	@Autowired
	private MailSender mailSender;
	@Autowired
	private SimpleMailMessage templateMessage;
	
	public void setMailSender(MailSender mailSender){
		this.mailSender = mailSender;
	}
	
	public void setTemplateMessage(SimpleMailMessage templateMessage){
		this.templateMessage = templateMessage;
	}
	
	public void send(String address, String text){
		logger.debug("Send email to {}", address);
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setTo(address);
		msg.setText(text);
		try {
			mailSender.send(msg);
		} catch(MailException ex){
			logger.warn("An error occured while sending email", ex);
		}
	}
}
