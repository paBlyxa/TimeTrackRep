package com.we.timetrack.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.TimesheetDay;
import com.we.timetrack.service.model.TimesheetForm;

/**
 * @author fakadey
 *
 */
@Service
public class TimesheetManager {

	private static Logger logger = Logger.getLogger("service");

	@Autowired
	private TimesheetRepository timesheetRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;

	/**
	 * Returns a database Timesheets records with matching EmployeeId and later
	 * than (dateTask - week) ordered by days
	 * 
	 * @param week
	 *            - number of week, where 0 - current week, 1 - week before, -1
	 *            - week later
	 * @param model
	 *            - model where info will be save
	 */
	@Transactional(readOnly = true)
	public void getTimesheetsByDays(int week, Model model) {

		// Last day in last week (Sunday)
		LocalDate dateEarly = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).minusWeeks(week);

		// Current user
		Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<Timesheet> timesheets = timesheetRepository.getTimesheets(employee.getEmployeeId(), dateEarly,
				dateEarly.plusDays(6));

		logger.debug("Loaded timesheets count: " + timesheets.size() + " for employee " + employee.getUsername()
				+ ", dateEarly: " + dateEarly);

		List<TimesheetDay> timesheetsByDays = TimesheetDay.getTimesheetsByDays(timesheets, dateEarly,
				dateEarly.plusDays(6));

		List<Project> projects = projectRepository.getProjects();

		List<Task> tasks = taskRepository.getTasks();

		// Count all hours in week
		float countTime = 0;
		for (TimesheetDay timesheetDay : timesheetsByDays) {
			countTime += timesheetDay.getHours();
		}

		// if call redirect, model's already had timesheetform
		if (model.asMap().get("timesheetForm") == null) {
			TimesheetForm timesheetForm;
			timesheetForm = new TimesheetForm();
			model.addAttribute(timesheetForm);
		}

		model.addAttribute("timesheetsByDays", timesheetsByDays);
		model.addAttribute("projectList", projects);
		model.addAttribute("taskList", tasks);
		model.addAttribute("countTime", countTime);
	}

	/**
	 * Save new timesheet from current user
	 * 
	 * @param form
	 *            - form with params for new timesheet
	 * @param redirectAttributes
	 *            - attributes for save form params in redirect
	 */
	public void saveTimesheet(TimesheetForm form) {
		if (form != null) {

			// Current user
			Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			List<Timesheet> timesheets = new ArrayList<Timesheet>();

			String[] dates = form.getDates().split(", ");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			// Save new timesheet
			for (String dateStr : dates) {
				LocalDate date = LocalDate.parse(dateStr, dateFormat);
				Timesheet timesheet = new Timesheet();
				timesheet.setEmployeeId(employee.getEmployeeId());
				timesheet.setComment(form.getComment());
				timesheet.setCountTime(form.getCountTime());
				timesheet.setDateTask(date);

				Project project = new Project();
				project.setProjectId(Integer.parseInt(form.getProjectId()));
				timesheet.setProject(project);
				Task task = new Task();
				task.setTaskId(Integer.parseInt(form.getTaskId()));
				timesheet.setTask(task);
				timesheets.add(timesheet);
			}

			timesheetRepository.saveTimesheets(timesheets);
		}
	}

	/**
	 * Modify countTime in timesheet with matching timesheetId
	 * 
	 * @param timesheetId
	 * @param countTime
	 */
	public void modifyTimesheet(int timesheetId, float countTime) {

		Timesheet timesheet = timesheetRepository.getTimesheet(timesheetId, false);
		timesheet.setCountTime(countTime);
		timesheetRepository.saveTimesheet(timesheet);
	}

	/**
	 * Delete choosen timesheet
	 * 
	 * @param timesheetId
	 *            - timesheet's id to delete
	 * @param model
	 *            - not use
	 */
	public void deleteTimesheet(int timesheetId, Model model) {

		Timesheet timesheet = new Timesheet();
		timesheet.setId(timesheetId);
		timesheetRepository.deleteTimesheet(timesheet);
	}
}
