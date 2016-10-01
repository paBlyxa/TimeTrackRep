package com.we.timetrack.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.TimesheetDay;
import com.we.timetrack.service.TimesheetForm;
import com.we.timetrack.service.TimesheetManager;
import com.we.timetrack.service.User;
/**
 * Controller for the Timesheet List screen.
 * @author fakadey
 */
@Controller							// Declared to be a controller
@RequestMapping("/timesheet")
public class TimesheetListController  {
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	private ProjectRepository projectManager;
	@Autowired
	private TaskRepository taskManager;
	@Autowired
	private TimesheetManager timesheetManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String timesheets(@RequestParam(value="week", defaultValue="0") int week, Model model) {
		
		//������� ������������������ ������������
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//��������� ���� ���������� ������ (�����������)
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.add(Calendar.WEEK_OF_YEAR, -week);
        Date dateTask = calendar.getTime();
        
        //������ � ���� �� ������ �� ���� ������
		List<TimesheetDay> timesheetsByDays = timesheetManager.getTimesheetsByDays(user.getEmployee(), dateTask);

		//������ ���� �������� ��� ������
		List<Project> projects = projectManager.getProjects();
		
		//������ ���� ����� ��� ������
		List<Task> tasks = taskManager.getTasks();
		
		//������� ������ ���������� ����� �� ������
		float countTime = 0;
		for (TimesheetDay timesheetDay: timesheetsByDays){
			countTime += timesheetDay.getHours();
		}
		
		//���� ��� ������ redirect, �� � ������ ��� ���� �����, ��������� �� � ������
		if (model.asMap().get("timesheetForm") != null){
			model.addAttribute(model.asMap().get("timesheetForm"));
		}
		else{
			TimesheetForm timesheetForm;
			timesheetForm = new TimesheetForm();
			model.addAttribute(timesheetForm);
		}
		
		//���������� ���������� ��� ����������� �� ��������
		model.addAttribute("timesheetsByDays", timesheetsByDays);
		model.addAttribute("projectList", projects);
		model.addAttribute("taskList", tasks);
		model.addAttribute("countTime", countTime);
				
		return "timesheet";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveTimesheet(TimesheetForm timesheetForm, RedirectAttributes redirectAttributes){
		
		if (timesheetForm != null){
			
			//��������� ����� ������
			Timesheet timesheet = new Timesheet();
			
			//������� ����������������� ������������
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						
			//��������� ��� �������� � ����� ������
			timesheet.setEmployee(user.getEmployee());
			timesheet.setComment(timesheetForm.getComment());
			timesheet.setCountTime(timesheetForm.getCountTime());
			timesheet.setDateTask(timesheetForm.getDateTask());
			timesheet.setProject(projectManager.getProject(Integer.parseInt(timesheetForm.getProjectId())));
			timesheet.setTask(taskManager.getTask(Integer.parseInt(timesheetForm.getTaskId())));
			
			//��������� ������ � ���� ������
			timesheetRepository.saveTimesheet(timesheet);
			
			//��������� �������� �����, ��� ����������� �� ��������
			redirectAttributes.addFlashAttribute("timesheetForm", timesheetForm);
		}
		return "redirect:/timesheet";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteProject(@RequestParam(value="timesheetId") int timesheetId, Model model){
		
		//�������� ������ � ��������������� id
		Timesheet timesheet = new Timesheet();
		timesheet.setId(timesheetId);
		timesheetRepository.deleteTimesheet(timesheet);
		
		return "redirect:/timesheet";
	}
	
	@InitBinder     
	public void initBinder(WebDataBinder binder){
	     binder.registerCustomEditor(Date.class,     
	                         new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));   
	}
}
