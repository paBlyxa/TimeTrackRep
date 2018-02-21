package com.we.timetrack.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Project;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.DateRange;

@Service
public class StatisticService {

	private final static Logger logger = LoggerFactory.getLogger(StatisticService.class);
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	@Qualifier("ldapEmployeeRepository")
	private EmployeeRepository employeeRepository;
	@Autowired
	private CalendarService calendarService;
	
	/**
	 * Get projects summary statistic
	 * 
	 * @param project
	 */
	@Transactional(readOnly = true)
	public Map<String, String[]> getProjectsSummary(DateRange period) {
		if (period != null){
			Employee curEmployee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			logger.debug("Start get statistic for {}", curEmployee.getShortName());
			List<Timesheet> timesheets;
			if (curEmployee.getAuthorities().contains(new SimpleGrantedAuthority("Timex статисты"))) {
				logger.debug("Employee [{}] has Timex статисты authority", curEmployee.getShortName());
				timesheets = timesheetRepository.getTimesheets(period.getBegin(), period.getEnd());
			} else {
				List<Employee> employeeList = employeeRepository.getDirectReports(curEmployee);
				logger.debug("Employee [{}] has direct reports count of {}", curEmployee.getShortName(), employeeList.size());
				employeeList.add(curEmployee);
				timesheets = new ArrayList<Timesheet>();
				for (Employee emp : employeeList){
					timesheets.addAll(timesheetRepository.getTimesheets(emp.getEmployeeId(), period.getBegin(), period.getEnd()));
				}
			}
			logger.debug("Start get timesheets, size = {}", timesheets.size());
			Map<UUID, Map<Integer, Float>> summary = new HashMap<>();
			List<Project> projectList = new ArrayList<>();
			// Проходим по всем записям и формируем общую статистику
			for (Timesheet timesheet : timesheets){
				Map<Integer, Float> map = summary.get(timesheet.getEmployeeId());
				if (map == null){
					map = new HashMap<Integer, Float>();
					summary.put(timesheet.getEmployeeId(), map);
				}
				Float countByProject = map.get(timesheet.getProject().getProjectId());
				if (countByProject == null){
					map.put(timesheet.getProject().getProjectId(), timesheet.getCountTime());
				} else {
					countByProject += timesheet.getCountTime();
					map.put(timesheet.getProject().getProjectId(), countByProject);
				}
				// Сохраняем в список все проекты, в которых были задействованы сотрудники
				if (!projectList.contains(timesheet.getProject())){
					projectList.add(timesheet.getProject());
				}
			}
			// Count working hours
			float workingHours = calendarService.getWorkingHours(period);
			// Фомируем конечный результат
			Map<String, String[]> result = new TreeMap<>();
			// Итого по проектам
			float[] projectsSummary = new float[projectList.size() + 1];
			Map<UUID, Employee> employeeMap = employeeRepository.getEmployeeMap();
			for (UUID uuid : summary.keySet()){
				String[] employeeSummary = new String[projectList.size() + 2];
				float count = 0;
				for (int i = 0; i < projectList.size(); i++){
					Float projectCount = summary.get(uuid).get(projectList.get(i).getProjectId());
					if (projectCount != null){
						employeeSummary[i] = String.format("%.1f", projectCount);
						count += projectCount;
						projectsSummary[i] += projectCount;
					} else {
						employeeSummary[i] = "0,0";
					}
				}
				employeeSummary[projectList.size() + 1] = String.format("%.1f", count);
				employeeSummary[projectList.size() ] = String.format("%.1f", (count - workingHours));
				Employee employee = employeeMap.get(uuid);
				result.put(employee.getShortName(), employeeSummary);
			}
			String[] prSummary = new String[projectList.size() + 2];
			float countAllPr = 0;
			for (int i = 0; i < projectsSummary.length; i++){
				prSummary[i] = String.format("%.1f", projectsSummary[i]);
				countAllPr += projectsSummary[i];
			}
			prSummary[projectList.size() + 1] = String.format("%.1f", countAllPr);
			prSummary[projectList.size()] = String.format("%.1f", workingHours);
			// Итого по проектам
			result.put("итого", prSummary);
			// Заглавия для столбцов
			String[] columnName = new String[projectList.size() + 3];
			columnName[0] = "Сотрудники";
			for (int i = 0; i < projectList.size(); i++){
				columnName[i + 1] = projectList.get(i).getName();
			}
			columnName[projectList.size() + 1] = "Переработки";
			columnName[projectList.size() + 2] = "Итого";
			result.put("&&&", columnName);
			logger.debug("Finish get statistic");
			return result;
		}
		return null;
	}
}
