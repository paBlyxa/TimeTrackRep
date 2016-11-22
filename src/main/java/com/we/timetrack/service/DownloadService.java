package com.we.timetrack.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.we.timetrack.db.EmployeeRepository;
import com.we.timetrack.db.TimesheetRepository;
import com.we.timetrack.model.Employee;
import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.TimesheetDay;
import com.we.timetrack.service.report.FillManager;
import com.we.timetrack.service.report.Layouter;
import com.we.timetrack.service.report.Writer;

@Service
public class DownloadService {

	private static Logger logger = Logger.getLogger("service");
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	@Qualifier("ldapEmployeeRepository")
	private EmployeeRepository employeeRepository;
	
	/**
	  * Processes the download for Excel format.
	  * It does the following steps:
	  * <pre>1. Create new workbook
	  * 2. Create new worksheet
	  * 3. Define starting indices for rows and columns
	  * 4. Build layout 
	  * 5. Fill report
	  * 6. Set the HttpServletResponse properties
	  * 7. Write to the output stream
	  * </pre>
	  */
	public void downloadXLS(HttpServletResponse response, UUID employeeId, DateRange period){
		logger.debug("Downloading Excel report");
		
		Employee employee = employeeRepository.getEmployee(employeeId);
		
		//1. Create new workbook
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
		
			//2. Create new worksheet
			HSSFSheet worksheet = workbook.createSheet("POI Worksheet");
			
			//3. Define starting indices for rows and columns
			int startRowIndex = 0;
			int startColIndex = 0;
			
			//4. Build layout
			//Build title, date, column headers
			Layouter.buildReport(worksheet, startRowIndex, startColIndex);
			
			//5. Fill report
			FillManager.fillReport(worksheet, startRowIndex, startColIndex, getDatasource(employee.getEmployeeId(), period));
			
			//6. Set the response properties
			String fileName = "Report.xls";
			response.setHeader("Content-Disposition",  "inline; filename=" + fileName);
			//Make sure to set the correct content type
			response.setContentType("application/vnd.ms-excel");
			
			//7. Write to the outut stream
			Writer.write(response, worksheet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("IOException in downloadService: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private List<TimesheetDay> getDatasource(UUID employeeId, DateRange period){
		List<Timesheet> timesheets = timesheetRepository.getTimesheets(employeeId, period.getBegin(), period.getEnd());
		return TimesheetDay.getTimesheetsByDays(timesheets, period.getBegin(), period.getEnd());
	}
}
