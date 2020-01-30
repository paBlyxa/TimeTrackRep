package com.we.timetrack.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.DataConfig;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.service.ProjectManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
@WebAppConfiguration
@ActiveProfiles({"dataSourceProduction", "LdapAndDBEmployees"})
public class ProjectTaskSaveTest {

	@Autowired
	private ProjectManager projectManager;
	
	@Test
	public void test() {
		Project prj = projectManager.getProject(25);
		System.out.println(prj.getName());
		
		Task task = projectManager.getTask(2);
		print(task);
		int count = task.getProjects().size();
		
		task.getProjects().add(prj);
		projectManager.saveTask(task);
		
		task = projectManager.getTask(2);
		print(task);
		assertTrue(count < task.getProjects().size());
		
	}

	private void print(Task task) {
		System.out.print(task.getName() + ": ");
		for (Project p : task.getProjects()) {
			System.out.print(p.getName() + ", ");
		}
		System.out.println();
	}

}
