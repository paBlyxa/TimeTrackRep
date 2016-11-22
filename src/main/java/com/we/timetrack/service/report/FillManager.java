package com.we.timetrack.service.report;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import com.we.timetrack.model.Timesheet;
import com.we.timetrack.service.model.TimesheetDay;

public class FillManager {

	/**
	 * Fills the report with content
	 */
	public static void fillReport(HSSFSheet worksheet, int startRowIndex, int startColIndex, List<TimesheetDay> datasource){
		//Row offset
		startRowIndex += 2;
		
		// Create cell style for the body
		HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
		bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
		bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyCellStyle.setWrapText(true);
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy");
		
		// Create body
		int rowIndex = startRowIndex;
		for(TimesheetDay timesheetDay : datasource){
			// Create a new row
			HSSFRow row = worksheet.createRow((short) ++rowIndex);
			
			// Retrieve the date value
			HSSFCell cell1 = row.createCell(startColIndex + 0);
			cell1.setCellValue(timesheetDay.getDate().format(dateFormat));
			cell1.setCellStyle(bodyCellStyle);
			
			boolean firstRow = true;
			for(Timesheet timesheet : timesheetDay.getTimesheets()){
				if (!firstRow){
					row = worksheet.createRow((short) ++rowIndex);
				}
				
				// Retrieve the project's name value
				HSSFCell cell2 = row.createCell(startColIndex + 1);
				cell2.setCellValue(timesheet.getProject().getName());
				cell2.setCellStyle(bodyCellStyle);
				
				// Retrieve the task's name value
				HSSFCell cell3 = row.createCell(startColIndex + 2);
				cell3.setCellValue(timesheet.getTask().getName());
				cell3.setCellStyle(bodyCellStyle);
				
				// Retrieve the count hours value
				HSSFCell cell4 = row.createCell(startColIndex + 3);
				cell4.setCellValue(timesheet.getCountTime());
				cell4.setCellStyle(bodyCellStyle);
				
				if (firstRow){
					// Retrieve the count hours by day value
					HSSFCell cell5 = row.createCell(startColIndex + 4);
					cell5.setCellValue(timesheetDay.getHours());
					cell5.setCellStyle(bodyCellStyle);	
					firstRow = false;
				}
				
				// Retrieve the comment value
				HSSFCell cell6 = row.createCell(startColIndex + 5);
				cell6.setCellValue(timesheet.getComment());
				cell6.setCellStyle(bodyCellStyle);			
				
			}
			if (timesheetDay.getTimesheets().size() > 1){
				worksheet.addMergedRegion(new CellRangeAddress(rowIndex - timesheetDay.getTimesheets().size() + 1, rowIndex,
						startColIndex, startColIndex));
				worksheet.addMergedRegion(new CellRangeAddress(rowIndex - timesheetDay.getTimesheets().size() + 1, rowIndex,
						startColIndex + 4, startColIndex + 4));
			}
			
		}
	}
}
