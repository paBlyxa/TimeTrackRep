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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.we.timetrack.controller.binder.DateRangeEditor;
import com.we.timetrack.model.Employee;
import com.we.timetrack.service.DownloadService;
import com.we.timetrack.service.EmployeeManager;
import com.we.timetrack.service.TimesheetManager;
import com.we.timetrack.service.model.DateRange;

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
	
		model.addAttribute(employeeManager.getCurrentEmployee());
		
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
	 * Downloads the report as an Excel format.
	 */
	@RequestMapping(value = "/xls", method = RequestMethod.GET)
	public void getXLS(@RequestParam(value="id") String employeeId, @RequestParam(value="period", required=false) DateRange period,
			@RequestParam(value="week", required=false) Integer week,
			HttpServletResponse response, Model model) throws ClassNotFoundException {
		logger.debug("Received request to download report as an XLS");
		
		if (period == null || period.getBegin() == null || period.getEnd() == null){
			if (week == null){
				week = 0;
			}
			LocalDate dateEarly = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).minusWeeks(week);
			period = new DateRange();
			period.setBegin(dateEarly);
			period.setEnd(dateEarly.plusDays(6));
		}
		// Delegate to downloadService
		downloadService.downloadXLS(response, employeeManager.getCurrentEmployee(), period);
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
		timesheetManager.getTimesheetsByDays(id, week, model);
		Employee employee = employeeManager.getEmployee(id);
		model.addAttribute("employee", employee);
		return "timesheetEmployee";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
