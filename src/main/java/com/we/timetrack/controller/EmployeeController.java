package com.we.timetrack.controller;

import java.time.LocalDate;
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
	
		employeeManager.getAccount(model);
		
		return "account";
	}
	
	@RequestMapping(value="/employee", method=RequestMethod.GET)
	public String getEmployeeStat(@RequestParam(value="id") String employeeId,
				@RequestParam(value="statPeriod", required=false) DateRange period,
				Model model){
		
		if (period == null){
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));
		}
		employeeManager.getEmployeeSummary(UUID.fromString(employeeId), period, model);
		
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
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
