package com.we.timetrack.service.report;

import java.time.DayOfWeek;
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
		startRowIndex += 3;
		
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
					float hours = timesheetDay.getHours();
					HSSFCell cell5 = row.createCell(startColIndex + 4);
					cell5.setCellValue(hours);
					cell5.setCellStyle(bodyCellStyle);	
					if (timesheetDay.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || timesheetDay.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
						// Hours over norm
						HSSFCell cell6 = row.createCell(startColIndex + 5);
						cell6.setCellValue(hours);
						cell6.setCellStyle(bodyCellStyle);	
						// Hours late norm
						HSSFCell cell7 = row.createCell(startColIndex + 6);
						cell7.setCellValue(0);
						cell7.setCellStyle(bodyCellStyle);	
						firstRow = false;
					} else {
						// Hours over norm
						HSSFCell cell6 = row.createCell(startColIndex + 5);
						cell6.setCellValue(hours > 8 ? (hours- 8): 0);
						cell6.setCellStyle(bodyCellStyle);	
						// Hours late norm
						HSSFCell cell7 = row.createCell(startColIndex + 6);
						cell7.setCellValue(hours < 8 ? (8 - hours): 0);
						cell7.setCellStyle(bodyCellStyle);	
						firstRow = false;
					}
				}
				
				// Retrieve the comment value
				HSSFCell cell8 = row.createCell(startColIndex + 7);
				cell8.setCellValue(timesheet.getComment());
				cell8.setCellStyle(bodyCellStyle);			
				
			}
			if (timesheetDay.getTimesheets().size() > 1){
				worksheet.addMergedRegion(new CellRangeAddress(rowIndex - timesheetDay.getTimesheets().size() + 1, rowIndex,
						startColIndex, startColIndex));
				worksheet.addMergedRegion(new CellRangeAddress(rowIndex - timesheetDay.getTimesheets().size() + 1, rowIndex,
						startColIndex + 4, startColIndex + 4));
				worksheet.addMergedRegion(new CellRangeAddress(rowIndex - timesheetDay.getTimesheets().size() + 1, rowIndex,
						startColIndex + 5, startColIndex + 5));
				worksheet.addMergedRegion(new CellRangeAddress(rowIndex - timesheetDay.getTimesheets().size() + 1, rowIndex,
						startColIndex + 6, startColIndex + 6));
			}
			
		}
	}
}
