package com.we.timetrack.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.we.timetrack.controller.binder.DateRangeEditor;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.EmployeeProperty;
import com.we.timetrack.model.EmployeeProperty.PROPERTIES;
import com.we.timetrack.service.DownloadService;
import com.we.timetrack.service.EmployeeManager;
import com.we.timetrack.service.TimesheetManager;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.Message;
import com.we.timetrack.service.model.ScheduleType;
import com.we.timetrack.service.model.TimesheetDay;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeManager employeeManager;

	@Autowired
	private TimesheetManager timesheetManager;
	
	@Autowired
	private DownloadService downloadService;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String getEmployeeList(Model model){
		
		employeeManager.getEmployeeList(model);
		
		return "employees";
	}
		
	@RequestMapping(value="/account", method=RequestMethod.GET)
	public String getAccount(Model model){
		Employee employee = employeeManager.getCurrentEmployee();
		model.addAttribute(employee);
		model.addAttribute("rememberList", ScheduleType.values());
		String remember = employeeManager.getProperty(employee, PROPERTIES.remember);
		if (remember == null) remember = ScheduleType.OFF.name();
		model.addAttribute("remember", remember);
		String autoSave = employeeManager.getProperty(employee, PROPERTIES.autoSave);
		if (autoSave == null) autoSave = "false";
		model.addAttribute("autoSave", Boolean.valueOf(autoSave));
		return "account";
	}
	
	@RequestMapping(value="/mystat", method=RequestMethod.GET)
	public String getSummary(Model model){
		
		DateRange period = new DateRange();
		period.setEnd(LocalDate.now());
		period.setBegin(LocalDate.now().withDayOfMonth(1));
		
		model.addAttribute("statPeriod", period);
		model.addAttribute("employee", employeeManager.getCurrentEmployee());
		return "employeeStat";
	}
	
	@RequestMapping(value="/stat", method=RequestMethod.GET)
	public String getEmployeeStat(
			@RequestParam(value="id") String employeeId,
			@RequestParam(value="statPeriod", required=false) DateRange period,
			@RequestParam(value="type", required=false) Integer type,
			Model model){
		
		if (period == null || period.getBegin() == null || period.getEnd() == null){
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));
		}
		model.addAttribute("statPeriod", period);
		model.addAttribute("employee", employeeManager.getEmployee(UUID.fromString(employeeId)));
		
		return "employeeStat";
		
	}
	
	/**
	 * Export the report as an Excel format.
	 */
	@RequestMapping(value = "/xls", method = RequestMethod.GET)
	public String getXLS(@RequestParam(value="id") String employeeId, @RequestParam(value="period", required=false) DateRange period,
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws ClassNotFoundException {
		logger.debug("Received request to download report as an XLS");
		
		if (period == null || period.getBegin() == null || period.getEnd() == null){
			int offsetMonth = LocalDate.now().getMonthValue() % 3;
			if (offsetMonth == 0){
				offsetMonth = 2;
			} else {
				offsetMonth += 2;
			}
			LocalDate dateEarly = LocalDate.now().withDayOfMonth(1).minusMonths(offsetMonth);
			period = new DateRange();
			period.setBegin(dateEarly);
			period.setEnd(dateEarly.plusMonths(3).minusDays(1));
		}
		// Delegate to downloadService
		Message message = downloadService.exportXLS(employeeManager.getCurrentEmployee(), period);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/timesheet";
	}
	
	/**
	 * Download the report as an Excel format.
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam(value="id") String employeeId, @RequestParam(value="period", required=false) DateRange period,
			HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws ClassNotFoundException {
		logger.debug("Received request to download report as an XLS");
		
		if (period == null || period.getBegin() == null || period.getEnd() == null){
			int offsetMonth = LocalDate.now().getMonthValue() % 3;
			if (offsetMonth == 0){
				offsetMonth = 2;
			} else {
				offsetMonth += 2;
			}
			LocalDate dateEarly = LocalDate.now().withDayOfMonth(1).minusMonths(offsetMonth);
			period = new DateRange();
			period.setBegin(dateEarly);
			period.setEnd(dateEarly.plusMonths(3).minusDays(1));
		}
		Employee employee;
		if (employeeId == null || employeeId.isEmpty()) {
			employee = employeeManager.getCurrentEmployee();
		} else {
			employee = employeeManager.getEmployee(UUID.fromString(employeeId));
		}
		// Delegate to downloadService
		downloadService.downloadXLS(response, employee, period);
		
	}
	
	@RequestMapping("/stat/getData")
	public @ResponseBody Map<String, Float> getData(
			@RequestParam(value="id", required=true) String employeeId,
			@RequestParam(value="statPeriod", required=false) DateRange period,
			@RequestParam(value="type", required=false) Integer type,
			@RequestParam(value="itemsAll", required=false) Boolean all,
			@RequestParam(value="items[]", required=false) List<Integer> items){
		
		logger.debug("Paramaters: period: " + period + ", parameter type: " + type
				+ ", all: " + all + ", items: " + items);
		if (period == null){
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));
			
		}
		Map<String, Float> data = employeeManager.getEmployeeSummary(UUID.fromString(employeeId), period, type, all, items);
		return data;
	}
	
	@RequestMapping("/stat/getItems")
	public @ResponseBody Map<String, Integer> getItems(
			@RequestParam(value="statPeriod", required=false) DateRange period,
			@RequestParam(value="type", required=false) Integer type){
		return employeeManager.getItems(period, type);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String showTimesheet(
			@PathVariable("id") UUID id,
			@RequestParam(value="week", defaultValue="0") int week,
			Model model) {
		List<TimesheetDay> timesheetsByDays = timesheetManager.getTimesheetsByDays(id, week);
		// Count all hours in week
		float countTime = 0;
		float countOverTime = 0;
		for (TimesheetDay timesheetDay : timesheetsByDays) {
			countTime += timesheetDay.getHours();
			countOverTime += (timesheetDay.getHours() - timesheetDay.getDay().getStatus().getWorkingHours());
		}
				
		model.addAttribute("timesheetsByDays", timesheetsByDays);
		model.addAttribute("countTime", countTime);
		model.addAttribute("countOverTime", countOverTime);
		Employee employee = employeeManager.getEmployee(id);
		model.addAttribute("employee", employee);
		return "timesheetEmployee";
	}
	
	@RequestMapping(value="/{id}/getTimesheets")
	public @ResponseBody List<TimesheetDay> getTimesheets(@PathVariable("id") UUID id,
			@RequestParam(value="week", defaultValue="0") int week) {
		return timesheetManager.getTimesheetsByDays(id, week);
	}

	@RequestMapping(value = "/saveProperty", method = RequestMethod.POST)
	public @ResponseBody String saveProperty(@RequestBody EmployeeProperty employeeProperty) {
				
		logger.info("Get request save property {} for employee \"{}\"", employeeProperty.getName(), employeeManager.getCurrentEmployee().getShortName());
		
		employeeManager.saveProperty(employeeManager.getCurrentEmployee().getEmployeeId(), employeeProperty.getName(), employeeProperty.getValue());
		return "{\"msg\":\"success\"}";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
