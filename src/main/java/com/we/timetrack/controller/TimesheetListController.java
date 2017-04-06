package com.we.timetrack.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.we.timetrack.service.TimesheetFormValidator;
import com.we.timetrack.service.TimesheetManager;
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
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(timesheetFormValidator);
	}
	
	@Inject
	public TimesheetListController(TimesheetManager timesheetManager,
			TimesheetFormValidator timesheetFormValidator){
		this.timesheetManager = timesheetManager;
		this.timesheetFormValidator = timesheetFormValidator;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String timesheets(@RequestParam(value="week", defaultValue="0") int week,
			Model model) {
        
        timesheetManager.getTimesheetsByDays(week, model);
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
			timesheetManager.getTimesheetsByDays(week, model);
			return "timesheet";
			
		}
		timesheetManager.saveTimesheet(timesheetForm);
		redirectAttributes.addFlashAttribute("timesheetForm", timesheetForm);
		return "redirect:/timesheet?week=" + week;
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
	
	@RequestMapping(value="/modifyComment", method=RequestMethod.POST)
	public String modifyComment(
			@RequestParam(value="week", defaultValue="0") int week,
			@RequestParam(value="timesheetId") int timesheetId,
			@RequestParam(value="comment") String comment,
			Model model){
		
		timesheetManager.modifyTimesheet(timesheetId, comment);
		return "redirect:/timesheet?week=" + week;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteTimesheet(
			@RequestParam(value="week", defaultValue="0") int week,
			@RequestParam(value="timesheetId") int timesheetId,
			Model model){
		
		timesheetManager.deleteTimesheet(timesheetId, model);
		return "redirect:/timesheet?week=" + week;
	}
}
