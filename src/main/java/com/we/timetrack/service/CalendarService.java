package com.we.timetrack.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.we.timetrack.db.CalendarRepository;
import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.VacationRepository;
import com.we.timetrack.model.Day;
import com.we.timetrack.model.DayStatus;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Vacation;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.VacationForm;
import com.we.timetrack.util.CSVUtils;

@Service
public class CalendarService {
	
	private static Logger logger = LoggerFactory.getLogger(CalendarService.class);
	
	@Autowired
	private CalendarRepository calendarRepository;
	@Autowired
	private VacationRepository vacationRepository;
	@Autowired
	@Qualifier("ldapEmployeeRepository")
	private EmployeeRepository employeeRepository;
	
	/**
	 * Return weekends and short days in this year
	 * @param year
	 * @return
	 */
	public List<List<Day>> getDays(int year){
		LocalDate dateBegin = LocalDate.of(year, 1, 1);
		LocalDate dateEnd = LocalDate.of(year, 12, 31);
		List<Day> days = calendarRepository.getDays(dateBegin, dateEnd);
		List<List<Day>> daysList = new ArrayList<>();
		for (int i = 0; i < 12; i++){
			daysList.add(new ArrayList<Day>());
		}
		for (Day day : days){
			daysList.get(day.getDateDay().getMonthValue() - 1).add(day);
		}
		return daysList;
	}
	
	
	/**
	 * Extract from csv file a day's list, which consist of weekends and short days,
	 * and save to database.
	 * @param file
	 */
	public void saveCalendar(MultipartFile file){
		
		logger.debug("Try to extract weekends and short days");
		List<Day> newDayList = getShortAndWeekends(file);
		
		List<Day> oldDayList = calendarRepository.getDays();
		
		logger.debug("Try to clear weekends and short days in database");
		for (Day oldDay : oldDayList){
			Day d = getDayFromList(newDayList, oldDay);
			if (d != null){
				if (oldDay.getStatus() != d.getStatus()){
					logger.debug("Day {} have another status", d.getDateDay());
					oldDay.setStatus(d.getStatus());
					calendarRepository.saveDay(oldDay);
				}
				logger.debug("Remove day {} from list", d.getDateDay());
				newDayList.remove(d);
			} else {
				logger.debug("Remove day {} from database", oldDay.getDateDay());
				calendarRepository.remove(oldDay);
			}
		}
		
		logger.debug("Try to save weekends and short days in database");
		calendarRepository.saveDays(newDayList);
	}
	
	/**
	 * Extract from csv file a day's list, which consist of weekends and short days.
	 * @param file - MultipartFile
	 * @return
	 */
	public List<Day> getShortAndWeekends(MultipartFile file){
		
		List<List<String>> result = CSVUtils.parseCSVFile(file);
		
		return getShortAndWeekends(result);
	}
	
	/**
	 * Extract from csv file a day's list, which consist of weekends and short days.
	 * @param file - string name of file
	 * @return
	 */
	public List<Day> getShortAndWeekends(String file){
		
		List<List<String>> result = CSVUtils.parseCSVFile(file);
		
		return getShortAndWeekends(result);
	}	
	
	/**
	 * Extract from csv file a day's list, which consist of weekends and short days.
	 * @param result
	 * @return
	 */
	private List<Day> getShortAndWeekends(List<List<String>> result){
		
		List<Day> dayList = new ArrayList<>();
		
		for (List<String> line : result){
			if (line.get(0).matches("^-?\\d+$")){
				int year = Integer.parseInt(line.get(0));
				for (int i = 1; i <=12; i++){
					String[] days = line.get(i).split(",");
					for (int j = 0; j < days.length; j++){
						Day day = new Day();
						if (days[j].endsWith("*")){
							System.out.println(days[j]);
							day.setStatus(DayStatus.Short);
						} else {
							day.setStatus(DayStatus.Weekend);
						}
						int dayNumber = parseInt(days[j]);
						day.setDateDay(LocalDate.of(year, i, dayNumber));
						dayList.add(day);
					}
				}
			}
		}
		
		return dayList;
	}
	
	public List<VacationForm> getVacations(DateRange period){
		
		List<Vacation> vacationList = vacationRepository.getVacations(period.getBegin().minusMonths(1), period.getEnd());
		
		List<VacationForm> result = new ArrayList<VacationForm>();
		
		String group = "cn=ОПИК,ou=ОПИК,dc=we,dc=ru";
		List<Employee> employeeList = employeeRepository.getEmployees(group);
		
		for(Employee employee : employeeList){
			VacationForm vacationForm = new VacationForm(employee);
			result.add(vacationForm);
		}
		
		for (Vacation vacation : vacationList){
			logger.info(vacation.toString());
			for (VacationForm vacationForm : result){
				if (vacationForm.getEmployeeId().compareTo(vacation.getEmployeeId()) == 0){
					vacationForm.addVacation(vacation);
					break;
				}
			}
		}
		
		return result;
		
	}
	
	/**
	 * Save or update vacation's objects from vacationList
	 * @param vacationList
	 * @return
	 */
	public List<VacationForm> saveVacations(List<Vacation> vacationList){
		List<VacationForm> result = new ArrayList<VacationForm>();
		
		for (Vacation vacation : vacationList){
			logger.debug("Save vacation: {}", vacation.toString());
			vacation.setChangeDate(LocalDate.now());
			vacationRepository.saveVacation(vacation);
			
			boolean vacationFormExist = false;
			for (VacationForm vacationForm : result){
				if (vacationForm.getEmployeeId().compareTo(vacation.getEmployeeId()) == 0){
					vacationForm.addVacation(vacation);
					vacationFormExist = true;
					break;
				}
			}
			if (!vacationFormExist){
				VacationForm vacationForm = new VacationForm(employeeRepository.getEmployee(vacation.getEmployeeId()));
				vacationForm.addVacation(vacation);
				result.add(vacationForm);
			}
		}
		logger.debug("Save success");
		return result;
	}
	
	/**
	 * Delete vacation's objects from vacationList
	 * @param vacationList
	 * @return
	 */
	public String deleteVacations(List<Vacation> vacationList){
			vacationRepository.deleteVacations(vacationList);
		return "Success";
	}
	
	/**
	 * Parse integer from string. Don't throws exceptions.
	 * @param str
	 * @return
	 */
	private int parseInt(String str){
		char[] chars = str.toCharArray();
		int i = 0;
		int num = 0;
		while (i < chars.length && (chars[i] < '0' || chars[i] > '9')){
			i++;
		}
		while (i < chars.length && chars[i] >= '0' && chars[i] <= '9'){
			num = num * 10 + (chars[i] - '0');
			i++;
		}
		return num;
	}
	
	/**
	 * Look for day in list with the same date
	 */
	private Day getDayFromList(List<Day> dayList, Day day){
		for (Day d : dayList){
			if (d.getDateDay().compareTo(day.getDateDay()) == 0){
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Count working hours for period of days
	 */
	public float getWorkingHours(DateRange period){
		//Working hours
		List<Day> listDays = calendarRepository.getDays(period.getBegin(), period.getEnd());
		Map<LocalDate, Day> mapDays = new HashMap<>();
		for (Day day : listDays){
			mapDays.put(day.getDateDay(), day);
		}
		LocalDate date = period.getBegin();
		float hours = 0;
		for (;!date.isAfter(period.getEnd()); date = date.plusDays(1)){
			logger.debug("Date: {}", date);
			if (mapDays.get(date) != null){
				hours += mapDays.get(date).getStatus().getWorkingHours();
			} else {
				hours += 8;
			}
		}
		return hours;
	}
}
