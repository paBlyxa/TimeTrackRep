package com.we.timetrack.test;

import static org.junit.Assert.*;

//import java.util.List;

import org.junit.Test;
//import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
//import org.springframework.web.servlet.ModelAndView;

import com.we.timetrack.controller.TimesheetListController;
import com.we.timetrack.model.Employee;
//import com.we.timetrack.model.Timesheet;
//import com.we.timetrack.model.TimesheetManager;

public class TimesheetListControllerTest {
	
	private final int EMPLOYEE_ID = 2;
	
	@Test
	//@SuppressWarnings("unchecked")
	public void test() throws Exception{

		// Модель реального запроса HTTP
		//MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("GET",
		//		"/timesheets.htm");
		
		// Создаем ряд объектов проверки зависимости и внедряем их,
		// как это автоматические будет делать среда Spring
		Employee employee = new Employee();
		employee.setEmployeeId(EMPLOYEE_ID);
		
		// внедряем объекты, как это обычно делает Spring
		TimesheetListController timesheetListController = new TimesheetListController();
		//TimesheetManager timesheetManager = new TimesheetManager();
		//timesheetListController.setTimesheetManager(timesheetManager);
		
		// излекаем список
		Model model = null;
		String modelAndView = timesheetListController.timesheets(0, model);
		
		assertNotNull(modelAndView);
		//assertNotNull(modelAndView.getModel());
		
		//List<Timesheet> timesheets = (List<Timesheet>) modelAndView.getModel()
		//		.get(TimesheetListController.MAP_KEY);
		//assertNotNull(timesheets);
		
/*		Timesheet timesheet;
		for (int i = 0; i < timesheets.size(); i++){
			timesheet = timesheets.get(i);
			assertEquals(EMPLOYEE_ID, timesheet.getEmployee().getEmployeeId());
			System.out.println(timesheet.getId() + " passed!");
		}*/
		
	}

}
