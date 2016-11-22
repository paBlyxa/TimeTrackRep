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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.we.timetrack.db.ProjectRepository;
import com.we.timetrack.db.TaskRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Task;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.TimesheetDay;
import com.we.timetrack.service.model.TimesheetForm;

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
	 * Returns a database Timesheets records with matching EmployeeId
	 * and later than (dateTask - week) ordered by days
	 * @param week - number of week, where 0 - current week, 1 - week before, -1 - week later
	 * @param model - model where info will be save
	 */
	@Transactional(readOnly=true)
	public void getTimesheetsByDays(int week, Model model){
		
		//Последний день предыдущей недели (воскресение)	
		LocalDate dateEarly = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1)
				.minusWeeks(week);
		
		//Текущий сотрудник
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(employee.getEmployeeId(), dateEarly, dateEarly.plusDays(6));
		
		logger.debug("Loaded timesheets count: " + timesheets.size() + " for employee " + employee.getUsername() + ", dateEarly: " + dateEarly);

		List<TimesheetDay> timesheetsByDays = TimesheetDay.getTimesheetsByDays(timesheets, dateEarly, dateEarly.plusDays(6));
		
		//Запрос всех проектов для списка
		List<Project> projects = projectRepository.getProjects();
				
		//Запрос всех задач для списка
		List<Task> tasks = taskRepository.getTasks();
				
		//Подсчет общего количества часов за неделю
		float countTime = 0;
		for (TimesheetDay timesheetDay: timesheetsByDays){
			countTime += timesheetDay.getHours();
		}
				
		//Если был вызван redirect, то в модели уже есть форма
		if (model.asMap().get("timesheetForm") == null){
			TimesheetForm timesheetForm;
			timesheetForm = new TimesheetForm();
			model.addAttribute(timesheetForm);
		}
				
		//Добавление параметров для отображения на странице
		model.addAttribute("timesheetsByDays", timesheetsByDays);
		model.addAttribute("projectList", projects);
		model.addAttribute("taskList", tasks);
		model.addAttribute("countTime", countTime);
	}
	
	/**
	 * Save new timesheet from current user
	 * @param form - form with params for new timesheet
	 * @param redirectAttributes - attributes for save form params in redirect
	 */
	public void saveTimesheet(TimesheetForm form, RedirectAttributes redirectAttributes){
		if (form != null){
				
			//Текущий зарегистированный пользователь
			Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			List<Timesheet> timesheets = new ArrayList<Timesheet>();
			
			String[] dates = form.getDates().split(", ");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			//Сохраняем новую запись
			for(String dateStr : dates){
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
			
			//Сохраняем запись в базу данных
			timesheetRepository.saveTimesheets(timesheets);
			
			//Добавляем параметр формы, для отображения на странице
			redirectAttributes.addFlashAttribute("timesheetForm", form);
		}
	}
	
	/**
	 * Modify countTime in timesheet with matching timesheetId
	 * @param timesheetId
	 * @param countTime
	 */
	public void modifyTimesheet(int timesheetId, float countTime){
		
		Timesheet timesheet = timesheetRepository.getTimesheet(timesheetId, false);
		timesheet.setCountTime(countTime);
		timesheetRepository.saveTimesheet(timesheet);
	}
	
	/**
	 * Delete choosen timesheet
	 * @param timesheetId - timesheet's id to delete
	 * @param model - not use
	 */
	public void deleteTimesheet(int timesheetId, Model model){
		//Удаление записи с соответствующим id
		Timesheet timesheet = new Timesheet();
		timesheet.setId(timesheetId);
		timesheetRepository.deleteTimesheet(timesheet);
	}
}
