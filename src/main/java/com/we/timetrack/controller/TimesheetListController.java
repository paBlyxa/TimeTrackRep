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
		
//		 final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
//         Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
//         System.out.println("currentUser: " + currentUser.getUsername());
//         for (GrantedAuthority grantedAuthority : authorities) {
//             System.out.println(grantedAuthority);
//         }
        
        timesheetManager.getTimesheetsByDays(week, model);
				
		return "timesheet";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveTimesheet(TimesheetForm timesheetForm, RedirectAttributes redirectAttributes){
		
		timesheetManager.saveTimesheet(timesheetForm, redirectAttributes);
		return "redirect:/timesheet";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modifyTimesheet(@RequestParam(value="timesheetId") int timesheetId, @RequestParam(value="countTime") float countTime, Model model){
		
		timesheetManager.modifyTimesheet(timesheetId, countTime);
		
		return "redirect:/timesheet";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteTimesheet(@RequestParam(value="timesheetId") int timesheetId, Model model){
		
		timesheetManager.deleteTimesheet(timesheetId, model);
		
		return "redirect:/timesheet";
	}
	
	/*@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(Set.class, "dates", new CustomCollectionEditor(Set.class){
			@Override
			protected Object convertElement(Object element){
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date date = null;
				if (element instanceof String && !((String)element).equals("")){
					//From the JSP 'element' will be a String
					try {
						date = dateFormat.parse((String)element);
					}
					catch (ParseException ex) {
						throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
					}
				}
				else if(element instanceof Date) {
					//From the database 'element' will be a Integer
					date = (Date)element;
				}
				return date;
			}
		});
		binder.registerCustomEditor(Date.class,     
                new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"), true, 10));   
	}*/
	
}
