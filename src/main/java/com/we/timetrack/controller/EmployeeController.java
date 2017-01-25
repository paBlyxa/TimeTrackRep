package com.we.timetrack.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.we.timetrack.controller.binder.DateRangeEditor;
import com.we.timetrack.service.DownloadService;
import com.we.timetrack.service.EmployeeManager;
import com.we.timetrack.service.model.DateRange;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	private EmployeeManager employeeManager;
	
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
		
		if (period == null){
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
	public void getXLS(@RequestParam(value="id") String employeeId, @RequestParam(value="period") DateRange period,
			HttpServletResponse response, Model model) throws ClassNotFoundException {
		logger.debug("Received request to download report as an XLS");
		
		// Delegate to downloadService
		downloadService.downloadXLS(response, UUID.fromString(employeeId), period);
	}
	
	@RequestMapping("/stat/getData")
	public @ResponseBody Map<String, Float> getData(
			@RequestParam(value="employeeId", required=true) String employeeId,
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
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
