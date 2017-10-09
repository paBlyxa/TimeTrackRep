package com.we.timetrack.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.we.timetrack.controller.binder.DateRangeEditor;
import com.we.timetrack.model.Day;
import com.we.timetrack.model.Vacation;
import com.we.timetrack.service.CalendarService;
import com.we.timetrack.service.model.DateRange;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

	private final static Logger logger = LoggerFactory.getLogger(CalendarController.class);

	@Autowired
	private CalendarService calendarService;

	@RequestMapping(method = RequestMethod.GET)
	public String getCalendar(Model model) {
		return "calendar";
	}
	
	@RequestMapping(value = "/vacation", method = RequestMethod.GET)
	public String getVacation() {
		return "vacation";
	}
	
	@RequestMapping("/getDays")
	public @ResponseBody List<List<Day>> getItems(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month) {
		List<List<Day>> daysList = calendarService.getDays(year);
		return daysList;
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			logger.warn("File to be uploaded is empty");
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:/calendar";
		}

		calendarService.saveCalendar(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded '" + file.getOriginalFilename() + "'");

		return "redirect:/calendar";
	}
	
	@RequestMapping("vacation/getData")
	public @ResponseBody Object[] getVacationList(@RequestParam(value="period", required=false) DateRange period){
		if (period == null) {
			period = new DateRange();
			period.setEnd(LocalDate.now().plusMonths(2));
			period.setBegin(LocalDate.now());
		}
		return calendarService.getVacations(period).toArray();
	}
	
	@RequestMapping(value="vacation/saveData", method = RequestMethod.POST)
	public @ResponseBody Object[] saveVacation(@RequestBody List<Vacation> vacationList){
		return calendarService.saveVacations(vacationList).toArray();
	}	 
	
	@RequestMapping(value="vacation/deleteData", method = RequestMethod.POST)
	public @ResponseBody String deleteVacation(@RequestBody List<Vacation> vacationList){
		return calendarService.deleteVacations(vacationList);
	}	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(DateRange.class, new DateRangeEditor());
	}
	
}
