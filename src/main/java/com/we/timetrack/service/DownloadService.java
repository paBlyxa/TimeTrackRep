package com.we.timetrack.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we.timetrack.model.Employee;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.TimesheetDay;
import com.we.timetrack.service.report.FillManager;
import com.we.timetrack.service.report.Layouter;
import com.we.timetrack.service.report.Writer;

@Service
public class DownloadService {

	private static Logger logger = Logger.getLogger("service");

	@Autowired
	private TimesheetManager timesheetManager;

	/**
	 * Processes the download for Excel format. It does the following steps:
	 * 
	 * <pre>
	 * 1. Create new workbook
	 * 2. Create new worksheet
	 * 3. Define starting indices for rows and columns
	 * 4. Build layout 
	 * 5. Fill report
	 * 6. Set the HttpServletResponse properties
	 * 7. Write to the output stream
	 * </pre>
	 */
	public void downloadXLS(HttpServletResponse response, Employee employee, DateRange period) {
		logger.debug("Downloading Excel report");

		// 1. Create new workbook
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {

			// 2. Create new worksheet
			HSSFSheet worksheet = workbook.createSheet("POI Worksheet");

			// 3. Define starting indices for rows and columns
			int startRowIndex = 0;
			int startColIndex = 0;

			// 4. Build layout
			// Build title, date, column headers
			Layouter.buildReport(worksheet, employee, startRowIndex, startColIndex);

			// 5. Fill report
			FillManager.fillReport(worksheet, startRowIndex, startColIndex,
					getDatasource(employee.getEmployeeId(), period));

			// 6. Set the response properties
			String fileName = employee.getUsername() + "_" + period.getBegin() + "_" + period.getEnd() + ".xls";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
//			String user_agent = request.getHeader("user-agent");
//			boolean isInternetExplorer = (user_agent.indexOf("MSIE") > -1);
//			if (isInternetExplorer) {
//			    response.setHeader("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(filename, "utf-8") + "\"");
//			} else {
//			    response.setHeader("Content-disposition", "attachment; filename=\"" + MimeUtility.encodeWord(filename) + "\"");
//			}
			// Make sure to set the correct content type
			response.setContentType("application/vnd.ms-excel");

			// 7. Write to the outut stream
			Writer.write(response, worksheet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("IOException in downloadService: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private List<TimesheetDay> getDatasource(UUID employeeId, DateRange period) {
		return timesheetManager.getTimesheetsByDays(employeeId, period);
	}
}
