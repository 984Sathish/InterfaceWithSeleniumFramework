package com.OrangeHRM.utils;

import java.io.File;
import java.io.FileInputStream;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Data Utils consists finding Row and Column count,Reading excel headers,
 * Retrieving testdata id and Fetching testdata values
 */
public class DataUtils {
	private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);

	/**
	 * findRowColumnCount method to get total no of row and column count in a excel
	 * work sheet
	 * 
	 * @param sheet          name
	 * @param rowColumnCount as Hashtable
	 * @return Hashtable (returns row count and column count)
	 */

	public static Hashtable<String, Integer> findRowColumnCount(Sheet sheet,
			Hashtable<String, Integer> rowColumnCount) {

		Row row = null;
		int rows;
		rows = sheet.getPhysicalNumberOfRows();
		int cols = 0;
		int tmp = 0;
		int counter = 0;
		String temp = null;

		for (int i = 0; i < 10 || i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				temp = convertCellToString(row.getCell(0));
				if (!temp.isEmpty()) {
					counter++;
				}
				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				if (tmp > cols) {
					cols = tmp;
				}
			}
		}

		rowColumnCount.put("RowCount", counter);
		rowColumnCount.put("ColumnCount", cols);

		return rowColumnCount;
	}
	

	/**
	 * readExcelHeaders method read the excel headers column wise sheet
	 * 
	 * @param sheet          name
	 * @param excelHeaders   (Hashtable)
	 * @param rowColumnCount (Hashtable)
	 * @return excelHeaders (returns Header column values)
	 */
	public static Hashtable<String, Integer> readExcelHeaders(Sheet sheet, Hashtable<String, Integer> excelHeaders,
			Hashtable<String, Integer> rowColumnCount) {

		Row row = null;
		Cell cell = null;
		for (int r = 0; r < rowColumnCount.get("RowCount"); r++) {
			row = sheet.getRow(r);

			if (row != null) {
				for (int c = 0; c < rowColumnCount.get("ColumnCount"); c++) {
					cell = row.getCell(c);
					if (cell != null) {
						excelHeaders.put(cell.toString(), c);
					}
				}
				break;
			}
		}
		return excelHeaders;
	}
	

	/**
	 * convertHSSFCellToString method to convert the HSSFCell value to its
	 * equivalent string value
	 * 
	 * @param cell value
	 * @return String cellValue
	 */
	public static String convertCellToString(Cell cell) {
		String cellValue = null;
		if (cell != null) {
			cellValue = cell.toString();
			cellValue = cellValue.trim();
		} else {
			cellValue = "";
		}
		return cellValue;
	}

	/**
	 * To overriding the config sheet name to get test data
	 * 
	 * @param testCaseId    from test case
	 * @param testClassName test name
	 * @return
	 */
	public static HashMap<String, String> testDatabyID(String testCaseId, String testClassName) {
		String configSheetName = "Config";
		return testDatabyID(testCaseId, testClassName, configSheetName);
	}

	/**
	 * Map to the test data sheet based on Config declaration
	 * 
	 * @param testCaseId    from test case
	 * @param testClassName test name
	 * @return data cell value
	 */
	public static HashMap<String, String> testDatabyID(String testCaseId, String testClassName,
			String configSheetName) {
		String filePath = "";
		String sheetName = "";
		String fileName = "";
		Row row = null;
		Cell cell = null;
		Workbook wb = null;
		Sheet sheet = null;
		HashMap<String, String> data = new HashMap<String, String>();
		Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();

		try {

			String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "testdata" + File.separator;
			logger.info("data utils base path: " + basePath);
			String configFilePath = basePath + "Config-TD.xls";

			FileInputStream fs = new FileInputStream(new File(configFilePath));
			String extension = FilenameUtils.getExtension(configFilePath);
			System.out.println(extension);
			if (extension.equalsIgnoreCase("xlsx")) {
				wb = new XSSFWorkbook(fs);
				sheet = wb.getSheet(configSheetName);
			} else if (extension.equalsIgnoreCase("xls")) {
				wb = new HSSFWorkbook(fs);
				sheet = wb.getSheet(configSheetName);
			}
			// Function call to find excel header fields
			Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();
			excelrRowColumnCount = findRowColumnCount(sheet, excelrRowColumnCount);
			excelHeaders = readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);

			// Get test data set
			for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
				row = sheet.getRow(r);
				if (row != null) {
					Cell tempCell = sheet.getRow(r).getCell(0);
					if (tempCell != null) {
						String testClass = convertCellToString(row.getCell(0));

						if (testClass.equalsIgnoreCase(testClassName)) {
							cell = sheet.getRow(r).getCell(1);
							if (cell != null) {
								fileName = convertCellToString(row.getCell(1));
							}
							cell = sheet.getRow(r).getCell(2);
							if (cell != null) {
								sheetName = convertCellToString(row.getCell(2));
							}
							break;
						}
					}
				}
			}
			filePath = basePath + fileName;
			data = getTestData(filePath, fileName, sheetName, testCaseId);
			return data;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}finally {
	        // Close Workbook in the finally block
	        if (wb != null) {
	            try {
	                wb.close();
	            } catch (IOException e) {
	                logger.error("Error closing Workbook: " + e.getMessage(), e);
	            }
	        }
	    }
	    throw new RuntimeException("Error: DataUtils couldn't load the data");
	}

	/**
	 * Fetch the test data for a test case based on test case ID
	 * 
	 * @param filePath   test data xls location
	 * @param workBook   name
	 * @param sheetName  name
	 * @param testCaseId test id
	 * @return testData data
	 * @throws IOException
	 */
	public static HashMap<String, String> getTestData(String filePath, String workBook, String sheetName,
	        String testCaseId) throws IOException {
	    try (FileInputStream fs = new FileInputStream(new File(filePath));
	         Workbook wb = getWorkbook(fs, filePath)) {

	        Sheet sheet = wb.getSheet(sheetName);

	        Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<>();
	        excelrRowColumnCount = findRowColumnCount(sheet, excelrRowColumnCount);

	        // function call to find excel header fields
	        Hashtable<String, Integer> excelHeaders = new Hashtable<>();
	        excelHeaders = readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);
	        HashMap<String, String> data = null;
	        ArrayList<String> header = new ArrayList<>();
	        ArrayList<String> matcher = null;
	        HashMap<String, String> matcherList = new HashMap<>();
	        int idcounter = 1;

	        // Get all header
	        Row row = sheet.getRow(0);
	        if (row != null) {
	            for (int c = 0; c < excelrRowColumnCount.get("ColumnCount"); c++) {
	                Cell cell = sheet.getRow(0).getCell(c);
	                if (cell != null) {
	                    String temp = convertCellToString(row.getCell(c));
	                    header.add(temp);
	                }
	            }
	        }

	        // Get test data set
	        for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
	            row = sheet.getRow(r);
	            if (row != null) {
	                Cell tempCell = sheet.getRow(r).getCell(0);
	                if (tempCell != null) {
	                    String tcID = convertCellToString(row.getCell(0));
	                    if (tcID.equalsIgnoreCase(testCaseId)) {
	                        if (idcounter > 1) {
	                            Assert.fail("Please check the '" + workBook + "' file's '" + sheetName
	                                    + "' sheet, its mapped with more than one id: " + testCaseId);
	                        }
	                        data = new HashMap<>();
	                        matcher = new ArrayList<>();
	                        matcher.add(tcID);
	                        for (int c = 1; c < excelrRowColumnCount.get("ColumnCount"); c++) {
	                            Cell cell = sheet.getRow(r).getCell(c);
	                            String temp = convertCellToString(row.getCell(c));
	                            matcher.add(temp);
	                        }
	                        // Add all the test data to a Map
	                        for (int i = 0; i < matcher.size(); i++) {
	                            data.put(header.get(i), matcher.get(i));
	                        }
	                        matcherList.putAll(data);
	                        idcounter++;
	                    }
	                }
	            }
	        }

	        return matcherList;
	    }
	}

	private static Workbook getWorkbook(FileInputStream fs, String filePath) throws IOException {
	    String extension = FilenameUtils.getExtension(filePath);
	    if (extension.equalsIgnoreCase("xlsx")) {
	        return new XSSFWorkbook(fs);
	    } else if (extension.equalsIgnoreCase("xls")) {
	        return new HSSFWorkbook(fs);
	    } else {
	        throw new IllegalArgumentException("Unsupported file extension: " + extension);
	    }
	}


	/**
	 * To read the entire data present in the specified workbook and sheet
	 * 
	 * @param fileName  - File name of the workbook under
	 *                  src/main/resources/testdata
	 * @param sheetName - Name of the sheet where content to be read
	 * @return ArrayList<HashMap<String, String>> - data of the entire sheet
	 *         specified
	 * @throws Exception
	 */
		public static ArrayList<HashMap<String, String>> getEntireDataFromSheet(String fileName, int sheetNumber)
		        throws Exception {
	
		    Row row = null;
		    Cell cell = null;
		    Workbook wb = null;
		    Sheet sheet = null;
	
		    ArrayList<HashMap<String, String>> allData = new ArrayList<>();
	
		    try (FileInputStream fs = new FileInputStream(new File(fileName))) {
		        String extension = FilenameUtils.getExtension(fileName);
		        System.out.println(extension);
		        if (extension.equalsIgnoreCase("xlsx")) {
		            wb = new XSSFWorkbook(fs);
		        } else if (extension.equalsIgnoreCase("xls")) {
		            wb = new HSSFWorkbook(fs);
		        }
	
		        if (wb != null) {
		            sheet = wb.getSheetAt(sheetNumber);
		        } else {
		            throw new Exception("Workbook is null. Unable to read data.");
		        }
	
		        // Ensure the sheet is not null before further processing
		        if (sheet != null) {
		            Hashtable<String, Integer> excelrRowColumnCount = findRowColumnCount(sheet, new Hashtable<>());
		            int rowCount = excelrRowColumnCount.get("RowCount");
		            int columnCount = excelrRowColumnCount.get("ColumnCount");
	
		            // To get the header values
		            row = sheet.getRow(0);
		            ArrayList<String> header = new ArrayList<>();
		            if (row != null) {
		                for (int k = 0; k < excelrRowColumnCount.get("ColumnCount"); k++) {
		                    cell = row.getCell(k);
		                    header.add(convertCellToString(cell));
		                }
		            } else {
		                throw new Exception("Header row is empty. Please fill the first row in sheet "
		                        + sheet.getSheetName() + " of workbook " + fileName);
		            }
	
		            // Rest of the code...
		        } else {
		            throw new Exception("Sheet is null. Unable to read data.");
		        }
		    } catch (IOException e) {
		        // Handle or log the exception
		    	try {
					Log.exception(e);
				} catch (Exception logException) {
					// TODO Auto-generated catch block
					System.err.println("Exception: " + e.getMessage());	
			}
		    	throw new Exception("Error reading data from the sheet: " + e.getMessage());
		    } finally {
		        // Close resources in the finally block
		        if (wb != null) {
		            wb.close();
		        }
		    }
	
		    return allData;
		}


	/**
	 * Read data from Excel file extensions(both xls, xlsx), returns all cells in
	 * the row starting from where the testCaseId was found
	 *
	 * @param testCaseID    Data which needs to be found; could be a test case ID or
	 *                      any other unique value that can be found on the sheet
	 * @param testSheetName workbook file name that contains the test data
	 * @param testSheetName workbook file name that contains the test data
	 * @return String array that contains the required user data, that is found
	 *         first for the test case ID if there are any duplicate TC IDs
	 * @throws IOException
	 * @throws InvalidFormatException
	 */

		public static String[] getTestData(String testCaseID, String testSheetName, int sheetNumber)
		        throws InvalidFormatException {
		    Workbook wb = null;
		    Sheet sheet = null;
		    String[] data = new String[100]; // all cells in the row starting from where the testCaseId was found
		    String fileName = "/testdata/" + testSheetName;

		    try (FileInputStream fs = new FileInputStream(new File(fileName))) {
		        String extension = FilenameUtils.getExtension(fileName);
		        System.out.println(extension);

		        if (extension.equalsIgnoreCase("xlsx")) {
		            wb = new XSSFWorkbook(fs);
		        } else if (extension.equalsIgnoreCase("xls")) {
		            wb = new HSSFWorkbook(fs);
		        }

		        if (wb != null) {
		            sheet = wb.getSheetAt(sheetNumber);
		        } else {
		            Log.fail("Workbook is null. Unable to read data.");
		            return data;
		        }

		        int row_number = 0; // not used but can be used if required for anything
		        int cell_number = 0; // not used but can be used if required for anything
		        boolean found = false;
		        int iterator = 0;

		        for (Row row : sheet) {
		            for (Cell cell : row) {
		                if (cell != null) {
		                    if (cell.toString().equals(testCaseID)) {
		                        found = true;
		                    } else {
		                        if (found) {
		                            data[iterator] = cell.toString().trim();
		                            iterator = iterator + 1;
		                        }
		                    }
		                }

		                cell_number = cell_number + 1;
		            }

		            if (found) {
		                break;
		            }

		            cell_number = 0;
		            iterator = 0;
		            row_number = row_number + 1;
		        }
		    } catch (IOException e) {
		        Log.fail("File - " + fileName + " not found");
		    } finally {
		        // Close resources in the finally block
		        if (wb != null) {
		            try {
		                wb.close();
		            } catch (IOException e) {
		            	try {
		    				Log.exception(e);
		    			} catch (Exception logException) {
		    				// TODO Auto-generated catch block
		    				System.err.println("Exception: " + e.getMessage());	
		    		}		            }
		        }
		    }

		    return data;
		}
}