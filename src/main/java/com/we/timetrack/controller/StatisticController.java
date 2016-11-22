package com.we.timetrack.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.we.timetrack.controller.binder.DateRangeEditor;
import com.we.timetrack.service.EmployeeManager;
import com.we.timetrack.service.model.DateRange;

@Controller
@RequestMapping("/stat")
public class StatisticController {
	
	@Autowired
	private EmployeeManager employeeManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getSummary(@RequestParam(value="statPeriod", required=false) DateRange period, Model model){
		
		if (period == null){
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));
		}
		//«апрос на сводную информацию по проектам пользовател€ с соответствующим employeeId	
		employeeManager.getEmployeeSummary(period, model);
		
		return "employeeStat";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
