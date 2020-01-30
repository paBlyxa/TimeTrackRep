package com.we.timetrack.service.report;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Writer {

	private static Logger logger = LoggerFactory.getLogger(Writer.class);
	
	/**
	 * Writes the report to the output stream
	 */
	public static void write(HttpServletResponse response, HSSFSheet worksheet){
		
		logger.debug("Writing report to the sream");
		try {
			// Retrieve the output stream
			ServletOutputStream outputStream = response.getOutputStream();
			// Write to the output stream
			worksheet.getWorkbook().write(outputStream);
			// Flush the stream
			outputStream.flush();
		} catch (Exception e) {
			logger.error("Unable to write report to the output stream");
		}
	}
}
