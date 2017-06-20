package com.we.timetrack.test.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.we.timetrack.model.Day;
import com.we.timetrack.service.CalendarService;
import com.we.timetrack.util.CSVUtils;

public class CSVUtilTest {

	@Test
	public void test_no_quote() {

		String line = "10,AU,Australia";
		List<String> result = CSVUtils.parseLine(line);

		assertNotNull(result);
		assertTrue(result.size() == 3);
		assertTrue(result.get(0).equals("10"));
		assertTrue(result.get(1).equals("AU"));
		assertTrue(result.get(2).equals("Australia"));
	}

	@Test
	public void test_no_quote_but_double_quotes_in_column() {

		String line = "10,AU,Aus\"\"tralia";
		;
		List<String> result = CSVUtils.parseLine(line);

		assertNotNull(result);
		assertTrue(result.size() == 3);
		assertTrue(result.get(0).equals("10"));
		assertTrue(result.get(1).equals("AU"));
		System.out.println(result.get(2));
		assertTrue(result.get(2).equals("Aus\"tralia"));
	}

	@Test
	public void test_double_quotes() {

		String line = "\"10\",\"AU\",\"Australia\"";
		List<String> result = CSVUtils.parseLine(line);

		assertNotNull(result);
		assertTrue(result.size() == 3);
		assertTrue(result.get(0).equals("10"));
		assertTrue(result.get(1).equals("AU"));
		assertTrue(result.get(2).equals("Australia"));
	}
	
	@Test
	public void test_double_quotes_and_double_quotes_in_column() {

		String line = "\"10\",\"AU\",\"Aus\"\"tralia\"";
		List<String> result = CSVUtils.parseLine(line);

		assertNotNull(result);
		assertTrue(result.size() == 3);
		assertTrue(result.get(0).equals("10"));
		assertTrue(result.get(1).equals("AU"));
		assertTrue(result.get(2).equals("Aus\"tralia"));
	}
	
	@Test
	public void test_double_quotes_and_comma_in_column() {

		String line = "\"10\",\"AU\",\"Aus,tralia\"";
		List<String> result = CSVUtils.parseLine(line);

		assertNotNull(result);
		assertTrue(result.size() == 3);
		assertTrue(result.get(0).equals("10"));
		assertTrue(result.get(1).equals("AU"));
		assertTrue(result.get(2).equals("Aus,tralia"));
	}
	
	@Test
	public void test_csv_file() {
		
		String csvFilePath = "F:\\data-20161107T1038-structure-20161107T1038.csv";
		
		List<List<String>> result = CSVUtils.parseCSVFile(csvFilePath);
		
		assertNotNull(result);
		
		for (List<String> line : result){
			for (String item: line){
				System.out.print(item + ",   ");
			}
			System.out.println();
		}
	}
	
	@Test
	public void importCSV() {
		
		String csvFilePath = "F:\\data-20161107T1038-structure-20161107T1038.csv";
		
		CalendarService service = new CalendarService();
		
		List<Day> dayList = service.getShortAndWeekends(csvFilePath);
		
		assertNotNull(dayList);
		for (Day day : dayList){
			System.out.println(day.toString());
		}
	}
	

}
