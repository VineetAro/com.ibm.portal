package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	// Constant for file Operations
	private static final String TESTDATA_PATH = "src/test/resources/testdata/";
	
	
	// Load workbook from Excel file
	public static Workbook loadWorkbook(String filename) {

		Workbook workbook = null;
		
		// Create FileInputStream with TESTDATA_PATH + filename
		try {

			FileInputStream fis = new FileInputStream(TESTDATA_PATH + filename);

			// TODO: Create XSSFWorkbook(stream)
			workbook = new XSSFWorkbook(fis);

		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + TESTDATA_PATH + filename);
			e.printStackTrace();
		} catch (IOException e) {
			
			// XSSFWorkbook constructor throws IOException
			e.printStackTrace();
		}

		// TODO: Return workbook
		return workbook;
	}

	
	
	// Read all rows from specific sheet
	public static List<Map<String, String>> readExcelData(String filename, String sheetName) {

		List<Map<String, String>> lst = new ArrayList<>();
		Workbook workbook = loadWorkbook(filename);
		Sheet sheet = workbook.getSheet(sheetName);

		if (sheet == null)
			return lst; // Safety check

		Row header = sheet.getRow(0);
		DataFormatter formatter = new DataFormatter(); // Better for handling numbers/dates

		// Loop through data rows
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row currentRow = sheet.getRow(i);
			if (currentRow == null) {
				continue;
			}

			Map<String, String> newMap = new HashMap<String, String>();
			boolean isRowCompletelyEmpty = true;
			
			for (int j = 0; j < header.getLastCellNum(); j++) {
				String columnName = header.getCell(j).getStringCellValue();
				Cell cell = currentRow.getCell(j);

				// DataFormatter handles nulls and numeric types automatically
				String cellValue = formatter.formatCellValue(cell);
				// If even one cell has visible characters that aren't our skip signal (-1)
				// then this row contains legitimate data to execute
				if (!cellValue.isEmpty()) {
					isRowCompletelyEmpty = false;
				}

				newMap.put(columnName, cellValue);
			}
			lst.add(newMap);
		}

		return lst;
	}

	// Read specific cell value
	public static String readCellValue(String filename, String sheetName, int row, int col) {

		// Load workbook
		Workbook workbook = loadWorkbook(filename);

		// Get sheet

		Sheet sheet = workbook.getSheet(sheetName);

		// Get cell value
		Cell cell = sheet.getRow(row).getCell(col);

		DataFormatter formatter = new DataFormatter(); // Better for handling numbers/dates

		// Return as string
		return formatter.formatCellValue(cell);
	}

	// Write data to Excel (for reporting results)
	public static void writeCellValue(String filename, String sheetName, int row, int col, String value) {
		// Load workbook
		Workbook workbook = loadWorkbook(filename);
		// TODO: Write cell
		Sheet sheet = workbook.getSheet(sheetName);

		if (sheet == null)
			sheet = workbook.createSheet(sheetName);
		// Get or create row
		Row rowObj = sheet.getRow(row);
		if (rowObj == null)
			rowObj = sheet.createRow(row);
		// Get or create cell
		Cell cell = rowObj.getCell(col);
		if (cell == null)
			cell = rowObj.createCell(col);
		// Set the value
		cell.setCellValue(value);
		// Save file
		try (FileOutputStream fos = new FileOutputStream(TESTDATA_PATH + filename)) {
			workbook.write(fos);
			workbook.close();

		} catch (IOException e) {
			System.err.println("Could not save the file. Ensure it is not open in Excel.");
			e.printStackTrace();
		}
	}
	private Map<String, String> parseExcelAssertColumn(String rawAssertStr) {
		Map<String, String> checklistMap = new HashMap<>();
		
		if (rawAssertStr == null || rawAssertStr.trim().isEmpty() || "-1".equals(rawAssertStr)) {
			return checklistMap;
		}
		
		// Clean out surrounding data debris like extra quotes or curly brackets if present
		String sanitizedStr = rawAssertStr.replace("{", "").replace("}", "").replace("\"", "").trim();
		
		// Split fields by semicolon or comma boundaries cleanly
		String[] pairs = sanitizedStr.split(";|\\,");
		for (String pair : pairs) {
			if (pair.contains("=")) {
				String[] splitPair = pair.split("=", 2);
				String labelKey = splitPair[0].trim();
				String expectedVal = splitPair[1].trim();
				
				if (!labelKey.isEmpty()) {
					checklistMap.put(labelKey, expectedVal);
				}
			}
		}
		return checklistMap;
	}

}

