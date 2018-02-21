package com.we.timetrack.controller;

import java.time.LocalDate;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.we.timetrack.service.StatisticService;
import com.we.timetrack.service.model.DateRange;

@Controller
@RequestMapping("/stat")
public class StatController {

	private final static Logger logger = LoggerFactory.getLogger(StatController.class);
	
	@Autowired
	private StatisticService statisticService;

	@Autowired
	private DownloadService downloadService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String getSummary(Model model) {
		
		DateRange period = new DateRange();
		period.setEnd(LocalDate.now());
		period.setBegin(LocalDate.now().withDayOfMonth(1));
		
		model.addAttribute("statPeriod", period);
		
		return "stat";
	}
	
	@RequestMapping("/getData")
	public @ResponseBody Map<String, String[]> getData(@RequestParam(value = "statPeriod", required = false) DateRange period) {

		logger.debug("Paramaters:  period: " + period);
		if (period == null) {
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));

		}
		return statisticService.getProjectsSummary(period);
	}
	
	/**
	 * Downloads summary statistic as an Excel document.
	 * 
	 */
	@RequestMapping(value = "/xls", method = RequestMethod.GET)
	public void getXLS(
			@RequestParam(value = "statPeriod", required = true) DateRange period,
			HttpServletResponse response){
		logger.debug("Received request to download summary statistic as an XLS, period: {}", period);
		
		if (period == null) {
			period = new DateRange();
			period.setEnd(LocalDate.now());
			period.setBegin(LocalDate.now().withDayOfMonth(1));

		}
		// Delegate to downloadService
		downloadService.downloadSummary(response, statisticService.getProjectsSummary(period), period);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
}
