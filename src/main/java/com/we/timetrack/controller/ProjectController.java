package com.we.timetrack.controller;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.we.timetrack.controller.binder.DateRangeEditor;
import com.we.timetrack.model.Project;
import com.we.timetrack.service.ProjectManager;
import com.we.timetrack.service.model.DateRange;

@Controller
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private ProjectManager projectManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String projects(Model model){
		
		projectManager.getMyProjects(model);
		return "projects";
	}
	
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String newProject(Model model){
		
		projectManager.getProjects(model);
		return "newProject";
	}
	
	@RequestMapping(value="/new", method=RequestMethod.POST)
	public String saveProject(Project projectForm, Model model){
		
		projectManager.saveProject(projectForm);
		return "redirect:/projects/new";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteProject(@RequestParam(value="projectId") int projectId, Model model){
		
		projectManager.deleteProject(projectId);
		return "redirect:/projects";
	}
	
	@RequestMapping(value="/project", method=RequestMethod.GET)
	public String getProject(@RequestParam(value="id") int projectId, @RequestParam(value="period", required=false) DateRange period, Model model){
		
		if (period == null){
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));
		}
		projectManager.getProjectSummary(projectId, period, model);
		return "project";
	}	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(Set.class, "projectLeaders", new CustomCollectionEditor(Set.class){
			@Override
			protected Object convertElement(Object element){
				UUID id = null;
				if (element instanceof String && !((String)element).equals("")){
					//From the JSP 'element' will be a String
					try{
						id = UUID.fromString((String)element);
					}
					catch(NumberFormatException e){
						e.printStackTrace();
					}
				}
				else if(element instanceof UUID) {
					//From the database 'element' will be a Integer
					id = (UUID)element;
				}
				if (id != null){
					return id;
				}
				else return null;
			}
		});
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
