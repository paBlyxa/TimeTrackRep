package com.we.timetrack.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.model.Project;
import com.we.timetrack.service.ProjectManager;

@Controller
@RequestMapping("/projects")
public class ProjectListController {

	@Autowired
	private ProjectRepository projectRepository;
	//@Autowired
	//private TimesheetRepository timesheetRepository;
	@Autowired
	private ProjectManager projectManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String projects(Model model){
		
		List<Project> projects = projectRepository.getProjects();
		
		Project projectForm = new Project();
		
		model.addAttribute("projectForm", projectForm);
		model.addAttribute(projects);
		
		return "projects";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveProject(Project projectForm, Model model){
		
		//System.out.println("Project name = " + projectForm.getName() + ", comment = " + projectForm.getComment());
		projectRepository.saveProject(projectForm);
		
		return "redirect:/projects";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteProject(@RequestParam(value="projectId") int projectId, Model model){
		
		//System.out.println("Project name = " + projectForm.getName() + ", comment = " + projectForm.getComment());
		Project project = new Project();
		project.setProjectId(projectId);
		projectRepository.deleteProject(project);
		
		return "redirect:/projects";
	}
	
	@RequestMapping(value="/project", method=RequestMethod.GET)
	public String getProject(@RequestParam(value="id") int projectId, Model model){
		
		Project project = projectRepository.getProject(projectId);
		project.setProjectId(projectId);
		projectManager.makeProjectSummary(project);
		Map<String, Float> projectSummaryByTasks = projectManager.getSummaryByTasks();
		Map<String, Float> projectSummaryByEmployees = projectManager.getSummaryByEmployees();
		model.addAttribute(project);
		model.addAttribute("projectSummaryByTasks", projectSummaryByTasks);
		model.addAttribute("projectSummaryByEmployees", projectSummaryByEmployees);
		return "project";
	}
}
