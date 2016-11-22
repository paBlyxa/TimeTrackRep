package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.ProjectInfo;
import com.we.timetrack.service.ProjectManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("production")
public class ProjectManagerTest {

	@Autowired
	private ProjectManager projectManager;

	@Test
	public void getMyProjects() {
		List<ProjectInfo> projects = projectManager.getMyProjects(UUID.fromString("12a24625-9f35-4d33-f7af-0ad444f9e5ee"));
		assertNotNull(projects);
		for (Project project : projects){
			assertNotNull(project);
			System.out.println(">>>>>>> " + project.getName());
		}
	}
}
