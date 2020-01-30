package com.we.timetrack.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.TimesheetFormValidator;
import com.we.timetrack.service.TimesheetManager;
import com.we.timetrack.service.model.TimesheetDay;
import com.we.timetrack.service.model.TimesheetForm;

/**
 * Controller for the Timesheet List screen.
 * @author fakadey
 */
@Controller							// Declared to be a controller
@RequestMapping("/timesheet")
public class TimesheetListController  {

	private TimesheetManager timesheetManager;
	private TimesheetFormValidator timesheetFormValidator;
	
	//Set a form validator
	@InitBinder("timesheetForm")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(timesheetFormValidator);
	}
	
	@Inject
	public TimesheetListController(TimesheetManager timesheetManager,
			TimesheetFormValidator timesheetFormValidator){
		this.timesheetManager = timesheetManager;
		this.timesheetFormValidator = timesheetFormValidator;
	}
	
	private void getTimesheets(int week, Model model) {
		// if call redirect, model's already had timesheetform
		if (model.asMap().get("timesheetForm") == null) {
			TimesheetForm timesheetForm;
			timesheetForm = new TimesheetForm();
			model.addAttribute(timesheetForm);
		}
		
  /*      List<TimesheetDay> timesheetsByDays = timesheetManager.getTimesheetsByDays(week);
		// Count all hours in week
		float countTime = 0;
		float countOverTime = 0;
		for (TimesheetDay timesheetDay : timesheetsByDays) {
			countTime += timesheetDay.getHours();
			countOverTime += (timesheetDay.getHours() - timesheetDay.getDay().getStatus().getWorkingHours());
		}
				
		model.addAttribute("timesheetsByDays", timesheetsByDays);
		model.addAttribute("countTime", countTime);
		model.addAttribute("countOverTime", countOverTime);*/
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String timesheets(@RequestParam(value="week", defaultValue="0") int week,
			Model model) {
        
		getTimesheets(week, model);
		return "timesheet";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveTimesheet(
			@RequestParam(value="week", defaultValue="0") int week,
			@Validated TimesheetForm timesheetForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes){
		
		if (result.hasErrors()) {
			getTimesheets(week, model);
			return "timesheet";
			
		}
		timesheetManager.saveTimesheet(timesheetForm);
		redirectAttributes.addFlashAttribute("timesheetForm", timesheetForm);
		return "redirect:/timesheet?week=" + week;
	}
	
	@RequestMapping(value="/newTimesheet", method=RequestMethod.POST)
	public @ResponseBody String newTimesheet(@RequestBody TimesheetForm timesheetForm){

		timesheetManager.saveTimesheet(timesheetForm);
		return "{\"msg\":\"success\"}";
	}
	
	@RequestMapping(value="/modifyCountTime", method=RequestMethod.POST)
	public String modifycountTime(
			@RequestParam(value="week", defaultValue="0") int week,
			@RequestParam(value="timesheetId") int timesheetId,
			@RequestParam(value="countTime") float countTime,
			Model model){
		
		timesheetManager.modifyTimesheet(timesheetId, countTime);
		return "redirect:/timesheet?week=" + week;
	}
	
	@RequestMapping(value="/modifyTime", method=RequestMethod.POST)
	public @ResponseBody String modifycountTime(@RequestBody Timesheet timesheet){
		timesheetManager.modifyTimesheet(timesheet.getId(), timesheet.getCountTime());
		return "{\"msg\":\"success\"}";
	}
	
	@RequestMapping(value="/modifyComment", method=RequestMethod.POST)
	public String modifyComment(
			@RequestParam(value="week", defaultValue="0") int week,
			@RequestParam(value="timesheetId") int timesheetId,
			@RequestParam(value="comment") String comment,
			Model model){
		
		timesheetManager.modifyTimesheet(timesheetId, comment);
		return "redirect:/timesheet?week=" + week;
	}
	
	@RequestMapping(value="/modifyTimesheetComment", method=RequestMethod.POST)
	public @ResponseBody String modifyComment(@RequestBody Timesheet timesheet){
		timesheetManager.modifyTimesheet(timesheet.getId(), timesheet.getComment());
		return "{\"msg\":\"success\"}";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteTimesheet(
			@RequestParam(value="week", defaultValue="0") int week,
			@RequestParam(value="timesheetId") int timesheetId,
			Model model){
		
		timesheetManager.deleteTimesheet(timesheetId);
		return "redirect:/timesheet?week=" + week;
	}
	
	@RequestMapping(value="/deleteTimesheet", method=RequestMethod.POST)
	public @ResponseBody String deleteTimesheet(@RequestBody int timesheetId){
		timesheetManager.deleteTimesheet(timesheetId);
		return "{\"msg\":\"success\"}";
	}
	
	@RequestMapping(value="/getProjects")
	public @ResponseBody List<Project> getProjects() {
		return timesheetManager.getProjects();
	}
	
	@RequestMapping(value="/getTasks")
	public @ResponseBody List<Task> getTasks() {
		return timesheetManager.getTasks();
	}
	
	@RequestMapping(value="/getTimesheets")
	public @ResponseBody List<TimesheetDay> getTimesheets(@RequestParam(value="week", defaultValue="0") int week) {
		return timesheetManager.getTimesheetsByDays(week);
	}
}
