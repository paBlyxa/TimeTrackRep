package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.model.Task;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles({"dataSourceProduction", "DBEmployees"})
public class TaskRepositoryTest {

	@Autowired
	private TaskRepository taskRepository;
	
	@Test
	public void test() {
		List<Task> tasks = taskRepository.getTasks();
		assertNotNull(tasks);
		tasks.forEach(task -> {
			System.out.println(task.getTaskId() + ": " + task.getName());
		});
	}

	
}
