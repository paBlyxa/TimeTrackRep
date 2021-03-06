package com.we.timetrack.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jboss.logging.Logger;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.we.timetrack.controller.binder.DateRangeEditor;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.ProjectStatus;
import com.we.timetrack.model.Task;
import com.we.timetrack.service.ProjectManager;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.Message;

@Controller
@RequestMapping("/projects")
public class ProjectController {

	private static Logger logger = Logger.getLogger(ProjectController.class);

	@Autowired
	private ProjectManager projectManager;

	@RequestMapping(method = RequestMethod.GET)
	public String projects(Model model) {

		projectManager.getMyProjects(model);
		return "projects";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newProject(Model model) {

		projectManager.getProjects(model);
		return "newProject";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String saveProject(Project projectForm, Model model) {

		projectManager.saveProject(projectForm);
		return "redirect:/projects/new";
	}

	@RequestMapping(value = "/{id}/modify", method = RequestMethod.GET)
	public String showProject(@PathVariable("id") int id, Model model) {
		Project project = projectManager.getProjectWithLeaders(id);
		Map<String, String> employeeList = projectManager.getEmployees();

		model.addAttribute("statusList", ProjectStatus.values());
		model.addAttribute("project", project);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("tasks", projectManager.getActiveTasks());
		return "modifyProject";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteProject(@RequestParam(value = "projectId") int projectId, Model model, RedirectAttributes redirectAttributes) {

		try {
			projectManager.deleteProject(projectId);
		} catch(DataIntegrityViolationException e) {
			logger.error("Can't delete project", e);
			Message message = new Message();
			message.setName("Ошибка");
			message.setText("Невозможно удалить проект (projectId = " + projectId + ")");
			message.setType("ERROR");
			message.setInfo(e.getMostSpecificCause().getMessage());
			redirectAttributes.addFlashAttribute("message", message);
		}
		return "redirect:/projects/new";
	}

	@RequestMapping(value = "/project", method = RequestMethod.GET)
	public String getProjectStat(@RequestParam(value = "id") int projectId,
			@RequestParam(value = "period", required = false) DateRange period,
			@RequestParam(value = "type", required = false) Integer type, Model model) {

		if (period == null) {
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));
		}
		model.addAttribute("statPeriod", period);
		model.addAttribute(projectManager.getProject(projectId));
		return "project";
	}

	@RequestMapping("stat/getData")
	public @ResponseBody Map<String, Float> getData(@RequestParam(value = "id") int projectId,
			@RequestParam(value = "statPeriod", required = false) DateRange period,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "itemsAll", required = false) Boolean all,
			@RequestParam(value = "items[]", required = false) List<String> items) {

		logger.debug("Paramaters: projectId " + projectId + ", period: " + period + ", parameter type: " + type
				+ ", all: " + all + ", items: " + items);
		if (period == null) {
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));

		}
		Map<String, Float> data = projectManager.getProjectSummary(projectId, period.getBegin(), period.getEnd(), type,
				all, items);
		return data;
	}

	@RequestMapping("stat/getItems")
	public @ResponseBody Map<String, String> getItems(@RequestParam(value = "id") Integer projectId,
			@RequestParam(value = "statPeriod", required = false) DateRange period,
			@RequestParam(value = "type", required = false) Integer type) {
		return projectManager.getItems(projectId, period, type);
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Set.class, "projectLeaders", new CustomCollectionEditor(Set.class) {
			@Override
			protected Object convertElement(Object element) {
				UUID id = null;
				if (element instanceof String && !((String) element).equals("")) {
					// From the JSP 'element' will be a String
					try {
						id = UUID.fromString((String) element);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else if (element instanceof UUID) {
					// From the database 'element' will be a Integer
					id = (UUID) element;
				}
				if (id != null) {
					return id;
				} else
					return null;
			}
		});
		binder.registerCustomEditor(Set.class, "tasks", new CustomCollectionEditor(Set.class) {
			@Override
			protected Object convertElement(Object element) {
				Task task = null;
				if (element instanceof String && !((String) element).equals("")) {
					// From the JSP 'element' will be a String
					try {
						int taskId = Integer.parseInt((String) element);
						task = new Task();
						task.setTaskId(taskId);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else if (element instanceof UUID) {
					// From the database 'element' will be a Task
					task = (Task) element;
				}
				return task;
			}
		});
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
