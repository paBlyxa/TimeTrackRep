package com.we.timetrack.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.RootConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class TimeTrackUserServiceTest {

	@Autowired
	private UserDetailsService service;
	
	@Test
	public void testLoadUserByUsername() {
		UserDetails user = service.loadUserByUsername("p.fakadey");
		System.out.println(">>>>>>>>>>>>>Username: " + user.getUsername() + ", password: " + user.getPassword());
		assertNotNull(user);
	}

}
