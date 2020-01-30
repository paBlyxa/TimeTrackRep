package com.we.timetrack.controller;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.model.Department;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.TaskStatus;
import com.we.timetrack.service.ProjectManager;
import com.we.timetrack.service.model.Message;

@Controller
@RequestMapping("/tasks")
public class TaskListController {

	private static Logger logger = LoggerFactory.getLogger(TaskListController.class);
	
	@Autowired
	private TaskRepository taskManager;
	@Autowired
	private ProjectManager projectManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String tasks(Model model){
		
		List<Task> tasks = taskManager.getTasks();
		
		Task taskForm = new Task();
		
		model.addAttribute("taskStatusList", TaskStatus.values());
		model.addAttribute("departmentList", projectManager.getDepartments());
		model.addAttribute("taskForm", taskForm);
		model.addAttribute(tasks);
		
		return "tasks";
	}
	
	@RequestMapping(value = "/new", method=RequestMethod.POST)
	public String saveTask(Task taskForm, Model model){
		
		logger.debug("Start saving task {}", taskForm.getName());
		projectManager.saveTask(taskForm);
		logger.debug("Finish saving task {}", taskForm.getName());
		return "redirect:/tasks";
	}
	
	@RequestMapping(value="/{id}/modify", method=RequestMethod.GET)
	public String showTask(@PathVariable("id") int id, Model model) {
		projectManager.getActiveProjects(model);
		projectManager.getTask(model, id);
		return "modifyTask";
		
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteTask(@RequestParam(value="taskId") int taskId, Model model, RedirectAttributes redirectAttributes){
		
		try {
			Task task = new Task();
			task.setTaskId(taskId);
			taskManager.deleteTask(task);
		} catch(DataIntegrityViolationException e) {
			logger.error("Can't delete task", e);
			Message message = new Message();
			message.setName("Ошибка");
			message.setText("Невозможно удалить задачу (taskId = " + taskId + ")");
			message.setType("ERROR");
			message.setInfo(e.getMostSpecificCause().getMessage());
			redirectAttributes.addFlashAttribute("message", message);
		}
		
		return "redirect:/tasks";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Collection.class, "projects", new CustomCollectionEditor(Collection.class) {
			@Override
			protected Object convertElement(Object element) {
				Project prj = null;
				if (element instanceof String && !((String) element).equals("")) {
					// From the JSP 'element' will be a String
					try {
						int projectId = Integer.parseInt((String)element);
						prj = new Project();
						prj.setProjectId(projectId);
						return prj;
					} catch (NumberFormatException e) {
						logger.error("Can't convert " + element, e);
					}
					
				} else if (element instanceof Project) {
					// From the database 'element' will be a Project
					prj = (Project) element;
					logger.debug("Convert project {}",prj.getName());
				}
				return prj;
			}
		});
		binder.registerCustomEditor(Collection.class, "departments", new CustomCollectionEditor(Collection.class) {
			@Override
			protected Object convertElement(Object element) {
				Department dep = null;
				if (element instanceof String && !((String) element).equals("")) {
					// From the JSP 'element' will be a String
					try {
						int departmentId = Integer.parseInt((String)element);
						dep = new Department();
						dep.setDepartmentId(departmentId);
						return dep;
					} catch (NumberFormatException e) {
						logger.error("Can't convert " + element, e);
					}
					
				} else if (element instanceof Department) {
					// From the database 'element' will be a Department
					dep = (Department) element;
					logger.debug("Convert Department {}",dep.getName());
				}
				return dep;
			}
		});
	}
}
