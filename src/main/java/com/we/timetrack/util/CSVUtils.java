package com.we.timetrack.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class CSVUtils {

	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '\"';

	/**
	 * Parse csv file, and return list of string's arrays
	 * 
	 * @param file
	 *            - string name of file
	 * @return List of String[]
	 */
	public static List<List<String>> parseCSVFile(String file) {

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			return parceCSVFile(br);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<List<String>>();
	}

	public static List<List<String>> parseCSVFile(MultipartFile file) {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			return parceCSVFile(br);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<List<String>>();

	}

	private static List<List<String>> parceCSVFile(BufferedReader br) throws IOException {

		List<List<String>> result = new ArrayList<>();

		String line;

		while ((line = br.readLine()) != null) {

			List<String> datas = parseLine(line);
			result.add(datas);

		}

		return result;
	}

	public static List<String> parseLine(String csvLine) {
		return parseLine(csvLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String csvLine, char separator) {
		return parseLine(csvLine, separator, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String csvLine, char separator, char quote) {

		List<String> result = new ArrayList<>();
		;

		// if empty return
		if (csvLine == null || csvLine.isEmpty()) {
			return result;
		}

		if (quote == ' ') {
			quote = DEFAULT_QUOTE;
		}

		if (separator == ' ') {
			separator = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = csvLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == quote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}
				}
			} else {
				if (ch == quote) {

					inQuotes = true;
					
					// double quotes in column will hit this
					if (startCollectChar) {
						curVal.append('"');
					}
				} else if (ch == separator) {
					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;
				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break
					break;
				} else {
					curVal.append(ch);
					startCollectChar = true;
				}
			}
		}

		result.add(curVal.toString());
		return result;
	}
}
