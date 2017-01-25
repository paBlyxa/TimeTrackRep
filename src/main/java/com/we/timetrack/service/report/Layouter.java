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
	public final static String HEADER_DATE_COLUMN = "Дата";
	public final static String HEADER_PROJECT_COLUMN = "Проект";
	public final static String HEADER_TASK_COLUMN = "Задача";
	public final static String HEADER_HOURS_PER_TASK_COLUMN = "Часы";
	public final static String HEADER_HOURS_PER_DAY_COLUMN = "Часы";
	public final static String HEADER_COMMENT_COLUMN = "Комментарий";
	
	/**
	 * Builds the report layout
	 */
	public static void buildReport(HSSFSheet worksheet, int startRowIndex, int startColIndex){
		// Set column widths
		worksheet.setColumnWidth(0, 5000);
		worksheet.setColumnWidth(1, 5000);
		worksheet.setColumnWidth(2, 5000);
		worksheet.setColumnWidth(3, 5000);
		worksheet.setColumnWidth(4, 5000);
		worksheet.setColumnWidth(5, 5000);
		
		// Build the title and date headers
		buildTitle(worksheet, startRowIndex, startColIndex);
		// Build the column headers
		buildHeaders(worksheet, startRowIndex, startColIndex);
	}
	
	/**
	 * Builds the report title and the date header
	 */
	public static void buildTitle(HSSFSheet worksheet, int startRowIndex, int startColIndex){
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
		worksheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
		
		//Create fate header
		HSSFRow dateTitle = worksheet.createRow((short) startRowIndex + 1);
		HSSFCell cellDate = dateTitle.createCell(startColIndex);
		cellDate.setCellValue(HEADER + new Date());
	}
	
	/**
	 * Builds the column headers
	 */
	public static void buildHeaders(HSSFSheet worksheet, int startRowIndex, int startColIndex){
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
		HSSFRow rowHeader = worksheet.createRow((short) startRowIndex + 2);
		rowHeader.setHeight((short) 500);
		
		// Date column
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
		
		// Comment column
		HSSFCell cell6 = rowHeader.createCell(startColIndex + 5);
		cell6.setCellValue(HEADER_COMMENT_COLUMN);
		cell6.setCellStyle(headerCellStyle);
	}
}
