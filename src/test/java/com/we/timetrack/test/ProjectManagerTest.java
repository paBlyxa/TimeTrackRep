package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.model.Project;
import com.we.timetrack.service.ProjectManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class ProjectManagerTest {

	@Autowired
	private ProjectManager projectManager;
	
	@Test
	public void test() {
		Project project = new Project();
		project.setProjectId(3);
		projectManager.makeProjectSummary(project);
		Map<String, Float> projectSummary = projectManager.getSummaryByTasks();
		assertNotNull(projectSummary);
		for (Map.Entry<String, Float> entry : projectSummary.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

}
