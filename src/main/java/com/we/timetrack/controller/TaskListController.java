package com.we.timetrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.model.Task;

@Controller
@RequestMapping("/tasks")
public class TaskListController {

	@Autowired
	private TaskRepository taskManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String tasks(Model model){
		
		List<Task> tasks = taskManager.getTasks();
		
		Task taskForm = new Task();
		
		model.addAttribute("taskForm", taskForm);
		model.addAttribute(tasks);
		
		return "tasks";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveProject(Task taskForm, Model model){
		
		//System.out.println("Project name = " + projectForm.getName() + ", comment = " + projectForm.getComment());
		taskManager.saveTask(taskForm);
		
		return "redirect:/tasks";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteTask(@RequestParam(value="taskId") int taskId, Model model){
		
		//System.out.println("Project name = " + projectForm.getName() + ", comment = " + projectForm.getComment());
		Task task = new Task();
		task.setTaskId(taskId);
		taskManager.deleteTask(task);
		
		return "redirect:/tasks";
	}
}
