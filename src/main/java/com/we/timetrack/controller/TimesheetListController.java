package com.we.timetrack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

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

	@Inject
	public TimesheetListController(TimesheetManager timesheetManager){
		this.timesheetManager = timesheetManager;
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
			TimesheetForm timesheetForm,
			RedirectAttributes redirectAttributes){
		
		timesheetManager.saveTimesheet(timesheetForm);
		redirectAttributes.addFlashAttribute("timesheetForm", timesheetForm);
		return "redirect:/timesheet?week=" + week;
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modifyTimesheet(
			@RequestParam(value="week", defaultValue="0") int week,
			@RequestParam(value="timesheetId") int timesheetId,
			@RequestParam(value="countTime") float countTime,
			Model model){
		
		timesheetManager.modifyTimesheet(timesheetId, countTime);
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
