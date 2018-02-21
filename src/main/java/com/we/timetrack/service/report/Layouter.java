package com.we.timetrack.service.report;

import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

public class Layouter {

	public final static String TITLE = "Учет рабочего времени";
	public final static String HEADER = "Отчет сгенерирован в ";
	
	/**
	 * Builds the report layout
	 */
	public static void buildReport(HSSFSheet worksheet, String aboutHeader, int startRowIndex, int startColIndex, String[] headers, int[] columnWidths){
		// Set column widths
		for (int i = 0; i < columnWidths.length; i++){
			worksheet.setColumnWidth(i, columnWidths[i]);
		}
		
		// Build the title and date headers
		buildTitle(worksheet, aboutHeader, startRowIndex, startColIndex);
		// Build the column headers
		buildHeaders(worksheet, startRowIndex, startColIndex, headers);
	}
	
	/**
	 * Builds the report title and the date header
	 */
	public static void buildTitle(HSSFSheet worksheet, String aboutHeader, int startRowIndex, int startColIndex){
		// Create font style for the report title
		Font fontTitle = worksheet.getWorkbook().createFont();
		fontTitle.setBold(true);
		fontTitle.setFontHeight((short) 280);
		
		// Create cell style for the report title
		HSSFCellStyle cellStyleTitle = worksheet.getWorkbook().createCellStyle();
		cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
		cellStyleTitle.setWrapText(true);
		cellStyleTitle.setFont(fontTitle);
		
		// Create report title
		HSSFRow rowTitle = worksheet.createRow((short) startRowIndex);
		rowTitle.setHeight((short) 500);
		HSSFCell cellTitle = rowTitle.createCell(startRowIndex);
		cellTitle.setCellValue(TITLE);
		cellTitle.setCellStyle(cellStyleTitle);
		
		// Create merged region for the report title
		worksheet.addMergedRegion(new CellRangeAddress(0,0,0,7));
		
		// Create about header
		HSSFRow aboutTitle = worksheet.createRow((short) startRowIndex + 1);
		HSSFCell cellAbout = aboutTitle.createCell(startColIndex);
		cellAbout.setCellValue(aboutHeader);
		
		// Create fate header
		HSSFRow dateTitle = worksheet.createRow((short) startRowIndex + 2);
		HSSFCell cellDate = dateTitle.createCell(startColIndex);
		cellDate.setCellValue(HEADER + new Date());
	}
	
	/**
	 * Builds the column headers
	 */
	public static void buildHeaders(HSSFSheet worksheet, int startRowIndex, int startColIndex, String[] headers){
		// Create font style for the headers
		Font font = worksheet.getWorkbook().createFont();
		font.setBold(true);
		
		// Create cell style for the headers
		HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
		headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		headerCellStyle.setFillPattern(FillPatternType.FINE_DOTS);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerCellStyle.setWrapText(true);
		headerCellStyle.setFont(font);
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		
		// Create the column headers
		HSSFRow rowHeader = worksheet.createRow((short) startRowIndex + 3);
		rowHeader.setHeight((short) 500);
		
		int index = 0;
		for (String header : headers){
			HSSFCell cell = rowHeader.createCell(startColIndex + index);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
			index++;
		}
		
		/*// Date column
		HSSFCell cell1 = rowHeader.createCell(startColIndex + 0);
		cell1.setCellValue(HEADER_DATE_COLUMN);
		cell1.setCellStyle(headerCellStyle);
		
		// Project column
		HSSFCell cell2 = rowHeader.createCell(startColIndex + 1);
		cell2.setCellValue(HEADER_PROJECT_COLUMN);
		cell2.setCellStyle(headerCellStyle);
		
		// Task column
		HSSFCell cell3 = rowHeader.createCell(startColIndex + 2);
		cell3.setCellValue(HEADER_TASK_COLUMN);
		cell3.setCellStyle(headerCellStyle);
		
		// Hours per task column
		HSSFCell cell4 = rowHeader.createCell(startColIndex + 3);
		cell4.setCellValue(HEADER_HOURS_PER_TASK_COLUMN);
		cell4.setCellStyle(headerCellStyle);
		
		// Hours per day clolumn
		HSSFCell cell5 = rowHeader.createCell(startColIndex + 4);
		cell5.setCellValue(HEADER_HOURS_PER_DAY_COLUMN);
		cell5.setCellStyle(headerCellStyle);

		// Hours over norm column
		HSSFCell cell6 = rowHeader.createCell(startColIndex + 5);
		cell6.setCellValue(HEADER_OVER_NORM_COLUMN);
		cell6.setCellStyle(headerCellStyle);

		// Hours late column
		HSSFCell cell7 = rowHeader.createCell(startColIndex + 6);
		cell7.setCellValue(HEADER_LATE_COLUMN);
		cell7.setCellStyle(headerCellStyle);
		
		// Comment column
		HSSFCell cell8 = rowHeader.createCell(startColIndex + 7);
		cell8.setCellValue(HEADER_COMMENT_COLUMN);
		cell8.setCellStyle(headerCellStyle);*/
	}
}
