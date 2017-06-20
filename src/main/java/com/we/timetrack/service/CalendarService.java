package com.we.timetrack.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.we.timetrack.db.CalendarRepository;
import com.we.timetrack.model.Day;
import com.we.timetrack.model.DayStatus;
import com.we.timetrack.util.CSVUtils;

@Service
public class CalendarService {
	
	private static Logger logger = LoggerFactory.getLogger(CalendarService.class);
	
	@Autowired
	private CalendarRepository calendarRepository;
	
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
		List<Day> dayList = getShortAndWeekends(file);
		
		logger.debug("Try to save weekends and short days in database");
		calendarRepository.saveDays(dayList);
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
}
