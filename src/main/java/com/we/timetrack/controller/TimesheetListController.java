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
		
		//Текущий зарегистрированный пользователь
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//Последний день предыдущей недели (воскресение)
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.add(Calendar.WEEK_OF_YEAR, -week);
        Date dateTask = calendar.getTime();
        
        //Запрос в базу на записи по дням недели
		List<TimesheetDay> timesheetsByDays = timesheetManager.getTimesheetsByDays(user.getEmployee(), dateTask);

		//Запрос всех проектов для списка
		List<Project> projects = projectManager.getProjects();
		
		//Запрос всех задач для списка
		List<Task> tasks = taskManager.getTasks();
		
		//Подсчет общего количества часов за неделю
		float countTime = 0;
		for (TimesheetDay timesheetDay: timesheetsByDays){
			countTime += timesheetDay.getHours();
		}
		
		//Если был вызван redirect, то в модели уже есть форма, сохраняем ее в модель
		if (model.asMap().get("timesheetForm") != null){
			model.addAttribute(model.asMap().get("timesheetForm"));
		}
		else{
			TimesheetForm timesheetForm;
			timesheetForm = new TimesheetForm();
			model.addAttribute(timesheetForm);
		}
		
		//Добавление параметров для отображения на странице
		model.addAttribute("timesheetsByDays", timesheetsByDays);
		model.addAttribute("projectList", projects);
		model.addAttribute("taskList", tasks);
		model.addAttribute("countTime", countTime);
				
		return "timesheet";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveTimesheet(TimesheetForm timesheetForm, RedirectAttributes redirectAttributes){
		
		if (timesheetForm != null){
			
			//Сохраняем новую запись
			Timesheet timesheet = new Timesheet();
			
			//Текущий зарегистированный пользователь
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						
			//Сохраняем все значения в новую запись
			timesheet.setEmployee(user.getEmployee());
			timesheet.setComment(timesheetForm.getComment());
			timesheet.setCountTime(timesheetForm.getCountTime());
			timesheet.setDateTask(timesheetForm.getDateTask());
			timesheet.setProject(projectManager.getProject(Integer.parseInt(timesheetForm.getProjectId())));
			timesheet.setTask(taskManager.getTask(Integer.parseInt(timesheetForm.getTaskId())));
			
			//Сохраняем запись в базу данных
			timesheetRepository.saveTimesheet(timesheet);
			
			//Добавляем параметр формы, для отображения на странице
			redirectAttributes.addFlashAttribute("timesheetForm", timesheetForm);
		}
		return "redirect:/timesheet";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteProject(@RequestParam(value="timesheetId") int timesheetId, Model model){
		
		//Удаление записи с соответствующим id
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
