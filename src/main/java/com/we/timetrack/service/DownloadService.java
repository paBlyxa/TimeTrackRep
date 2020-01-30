package com.we.timetrack.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we.timetrack.model.Employee;
import com.we.timetrack.service.model.DateRange;
import com.we.timetrack.service.model.Message;
import com.we.timetrack.service.model.TimesheetDay;
import com.we.timetrack.service.report.FillManager;
import com.we.timetrack.service.report.Layouter;
import com.we.timetrack.service.report.Writer;

@Service
public class DownloadService {

	private static Logger logger = LoggerFactory.getLogger(DownloadService.class);

	@Autowired
	private TimesheetManager timesheetManager;
	@Autowired
	private SMBService smbService;
	
	public Message exportXLS(Employee employee, DateRange period) {
		logger.debug("Export Excel report for employee {}", employee.getShortName());
		Message message = new Message();
		try {
			HSSFSheet worksheet = createXLS(employee, period);
			// 6. Set the response properties
			String parentFolder = employee.getShortName();
			if (parentFolder.endsWith(".")) {
				parentFolder = parentFolder.substring(0, parentFolder.length() - 1);
			}
			String fileName = parentFolder + "/" + period.getBegin().getYear() + "/" 
					+ employee.getUsername() + "_" + period.getBegin() + "_" + period.getEnd() + ".xls";
			
			OutputStream outputStream = smbService.getOutputStream(fileName);
			// Write to the output stream
			worksheet.getWorkbook().write(outputStream);
			// Flush the stream
			outputStream.flush();
			// Return message			
			message.setName("Экспорт");
			message.setText("Файл успешно сохранен");
			message.setType("OK");
			message.setInfo(fileName);
		}catch(IOException e) {
			logger.warn("IOException in exportXLS: " + e.getMessage(), e);
			message.setName("Ошибка");
			message.setText("Не возможно экспортировать файл");
			message.setType("ERROR");
			message.setInfo(e.getMessage());
		}
		return message;
		
	}
	
	
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

		try {
			HSSFSheet worksheet = createXLS(employee, period);
			// 6. Set the response properties
			String fileName = employee.getUsername() + "_" + period.getBegin() + "_" + period.getEnd() + ".xls";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			// String user_agent = request.getHeader("user-agent");
			// boolean isInternetExplorer = (user_agent.indexOf("MSIE") > -1);
			// if (isInternetExplorer) {
			// response.setHeader("Content-disposition", "attachment;
			// filename=\"" + URLEncoder.encode(filename, "utf-8") + "\"");
			// } else {
			// response.setHeader("Content-disposition", "attachment;
			// filename=\"" + MimeUtility.encodeWord(filename) + "\"");
			// }
			// Make sure to set the correct content type
			response.setContentType("application/vnd.ms-excel");

			// 7. Write to the outut stream
			Writer.write(response, worksheet);
		} catch (IOException e) {
			logger.warn("IOException in downloadService: " + e.getMessage());
		}
	}

	public HSSFSheet createXLS(Employee employee, DateRange period) throws IOException {
		// 1. Create new workbook
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {

			// 2. Create new worksheet
			HSSFSheet worksheet = workbook.createSheet("POI Worksheet");

			// 3. Define starting indices for rows and columns
			int startRowIndex = 0;
			int startColIndex = 0;

			// 4. Build layout
			// Build title, date, column headers
			String[] headers = {"Дата", "Проект", "Задача", "Часы", "Часы", "Переработки", "Опоздания","Комментарий"};
			int[] columnWidths = {5000,5000,5000,3000,3000,3000,3000,10000};
			Layouter.buildReport(worksheet, "Сотрудник: " + employee.getSurname() + " " + employee.getName(), startRowIndex, startColIndex, headers, columnWidths);

			// 5. Fill report
			FillManager.fillReport(worksheet, startRowIndex, startColIndex,
						getDatasource(employee.getEmployeeId(), period));
			return worksheet;
		} catch(IOException e) {
			logger.warn("IOException in createExport: " + e.getMessage());
			throw e;
		}
	}
	
	public void downloadSummary(HttpServletResponse response, Map<String, String[]> summaryData, DateRange period) {
		logger.debug("Downloading Excel summary statistic");
		
		// 1. Create new workbook
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {

			// 2. Create new worksheet
			HSSFSheet worksheet = workbook.createSheet("POI Worksheet");

			// 3. Define starting indices for rows and columns
			int startRowIndex = 0;
			int startColIndex = 0;

			// 4. Build layout
			// Build title, date, column headers
			int[] columnWidths = new int[summaryData.get(StatisticService.COLUMN_NAMES).length];
			columnWidths[0] = 6000;
			for (int i = 1; i < columnWidths.length; i++){
				columnWidths[i] = 5000;
			}
			// Count working hours
			String hours = summaryData.get("итого")[summaryData.get("итого").length - 2];
			String about = "Рабочие часы за период c " + period.getBegin() + " по " + period.getEnd() + ": " + hours;
			Layouter.buildReport(worksheet, about, startRowIndex, startColIndex, summaryData.get(StatisticService.COLUMN_NAMES), columnWidths);

			// 5. Fill report
			FillManager.fillStatistic(worksheet, startRowIndex, startColIndex,
					summaryData);

			// 6. Set the response properties
			String fileName = "summary" + "_" + period.getBegin() + "_" + period.getEnd() + ".xls";
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			// String user_agent = request.getHeader("user-agent");
			// boolean isInternetExplorer = (user_agent.indexOf("MSIE") > -1);
			// if (isInternetExplorer) {
			// response.setHeader("Content-disposition", "attachment;
			// filename=\"" + URLEncoder.encode(filename, "utf-8") + "\"");
			// } else {
			// response.setHeader("Content-disposition", "attachment;
			// filename=\"" + MimeUtility.encodeWord(filename) + "\"");
			// }
			// Make sure to set the correct content type
			response.setContentType("application/vnd.ms-excel");

			// 7. Write to the outut stream
			Writer.write(response, worksheet);
		} catch (IOException e) {
			logger.warn("IOException in downloadService: " + e.getMessage());
		}
	}

	private List<TimesheetDay> getDatasource(UUID employeeId, DateRange period) {
		return timesheetManager.getTimesheetsByDays(employeeId, period);
	}
}
