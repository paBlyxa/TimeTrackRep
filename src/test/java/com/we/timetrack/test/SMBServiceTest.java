package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.service.SMBService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles({"dataSourceProduction", "DBEmployees"})
public class SMBServiceTest {

	@Autowired
	private SMBService smbService;
	
	@Test
	public void test() throws IOException {
		assertNotNull(smbService);
		String fileName = "Test/2019/testFile.txt";
		System.out.println(smbService.getPath());
		smbService.getOutputStream(fileName).write("test".getBytes());
	}

}
