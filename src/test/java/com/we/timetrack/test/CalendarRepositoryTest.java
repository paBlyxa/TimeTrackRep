package com.we.timetrack.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.db.CalendarRepository;
import com.we.timetrack.model.Day;
import com.we.timetrack.model.DayStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("dataSourceProduction")
public class CalendarRepositoryTest {

	@Autowired
	private CalendarRepository calendarRepository;
	
	
	@Test
	@Transactional
	public void testSaveDay() {
		
		LocalDate date = LocalDate.of(2017, 06, 12);
		Day newDay = new Day();
		newDay.setDateDay(date);
		newDay.setStatus(DayStatus.Weekend);
		calendarRepository.saveDay(newDay);
		List<Day> days = calendarRepository.getDays();
		assertNotNull(days);
		System.out.println(">>>>>>>> Count days: " + days.size());
		for (Day day : days){
			System.out.println(">>>>>>>>>> " + day.toString());
		}
	}
	

}
