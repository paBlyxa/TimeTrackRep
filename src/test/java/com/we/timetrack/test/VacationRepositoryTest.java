package com.we.timetrack.test;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.db.VacationRepository;
import com.we.timetrack.model.Vacation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("dataSourceProduction")
public class VacationRepositoryTest {

	@Autowired
	private VacationRepository vacationRepository;
	
	@Test
	public void testSave() {

		Vacation myVacation = new Vacation();
		myVacation.setEmployeeId(UUID.fromString("12a24625-9f35-4d33-f7af-0ad444f9e5ee"));
		myVacation.setStartDate(LocalDate.now());
		myVacation.setDuration(7);
		myVacation.setChangeDate(LocalDate.now());
		
		vacationRepository.saveVacation(myVacation);
		
	}
	
	@Test
	public void testGetAll() {
		
		List<Vacation> vacationList = vacationRepository.getVacations();
		for (Vacation vacation : vacationList){
			System.out.println(vacation.toString());
		}
	}

}
