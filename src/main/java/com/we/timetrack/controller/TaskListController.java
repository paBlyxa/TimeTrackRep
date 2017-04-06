package com.we.timetrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.TaskStatus;

@Controller
@RequestMapping("/tasks")
public class TaskListController {

	@Autowired
	private TaskRepository taskManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String tasks(Model model){
		
		List<Task> tasks = taskManager.getTasks();
		
		Task taskForm = new Task();
		
		model.addAttribute("taskStatusList", TaskStatus.values());
		model.addAttribute("taskForm", taskForm);
		model.addAttribute(tasks);
		
		return "tasks";
	}
	
	@RequestMapping(value = "/new", method=RequestMethod.POST)
	public String saveProject(Task taskForm, Model model){
		
		taskManager.saveTask(taskForm);
		
		return "redirect:/tasks";
	}
	
	@RequestMapping(value="/{id}/modify", method=RequestMethod.GET)
	public String showTask(@PathVariable("id") int id, Model model) {
		Task task = taskManager.getTask(id);

		model.addAttribute("taskStatusList", TaskStatus.values());
		model.addAttribute("taskForm", task);
		return "modifyTask";
		
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteTask(@RequestParam(value="taskId") int taskId, Model model){
		
		Task task = new Task();
		task.setTaskId(taskId);
		taskManager.deleteTask(task);
		
		return "redirect:/tasks";
	}
}
