package com.OrangeHRM.utils;

import static java.util.Objects.nonNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * Data Utils consists finding Row and Column count,Reading excel headers,
 * Retrieving testdata id and Fetching testdata values
 */
public class TestDataUtils {
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.

	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	private static final String CREDENTIALS_FILE_PATH = "./././client_secret.json";

	public static HashMap<String, String> getTestData(String workBookName, String workSheet, String testCaseId) {
		try {
			if (configProperty.getProperty("readFromGoogleSheet").equalsIgnoreCase("true")) {
				Log.event("Fetching test data from Google sheets");
				//return readTestDataFromGoogleSheet(testCaseId, workSheet);
				return readTestDataFromDownloadsFolder(workBookName, workSheet, testCaseId);
			} else {
				Log.event("Fetching test data from excel sheet");
				return readTestData(workBookName, workSheet, testCaseId);
			}
		}catch (IOException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}			return null;
		}
	}

	public static HashMap<String, String> readTestData(String workBookName, String workSheet, String testCaseId)
			throws IOException {
		if ((workBookName.split("\\.")[workBookName.split("\\.").length - 1]).equals("xlsx")) {
			XSSFWorkbook wb = new XSSFWorkbook(
					TestDataUtils.class.getResourceAsStream("/testdata/data/" + workBookName));
			XSSFSheet sheet = wb.getSheet(workSheet);
			XSSFRow headerRow = sheet.getRow(0);
			HashMap<String, Integer> headers = new HashMap<>();
			for (int c = 0; c < headerRow.getPhysicalNumberOfCells(); c++) {
				XSSFCell cell = headerRow.getCell(c);
				headers.put(cell.toString(), c);
			}
			if (!headers.containsKey("TC_ID"))
				throw new RuntimeException("no Tc_Id column found");

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (nonNull(row)) {
					XSSFCell idCell = row.getCell(headers.get("TC_ID"));
					if (StringUtils.equalsIgnoreCase(idCell.toString(), testCaseId)) {
						HashMap<String, String> testData = new HashMap<>();
						for (String key : headers.keySet()) {
							testData.put(key, Objects.toString(row.getCell(headers.get(key)), "").trim());
						}
						wb.close();
						return testData;
					}
				}
			}
		} else {
			POIFSFileSystem fs = new POIFSFileSystem(
					TestDataUtils.class.getResourceAsStream("/testdata/data/" + workBookName));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheet(workSheet);
			HSSFRow headerRow = sheet.getRow(0);
			HashMap<String, Integer> headers = new HashMap<>();
			for (int c = 0; c < headerRow.getPhysicalNumberOfCells(); c++) {
				HSSFCell cell = headerRow.getCell(c);
				headers.put(cell.toString(), c);
			}
			if (!headers.containsKey("TC_ID"))
				throw new RuntimeException("no Tc_Id column found");

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				HSSFRow row = sheet.getRow(i);
				if (nonNull(row)) {
					HSSFCell idCell = row.getCell(headers.get("TC_ID"));
					if (StringUtils.equalsIgnoreCase(idCell.toString(), testCaseId)) {
						HashMap<String, String> testData = new HashMap<>();
						for (String key : headers.keySet()) {
							testData.put(key, Objects.toString(row.getCell(headers.get(key)), "").trim());
						}
						return testData;
					}
				}
			}
		}
		throw new RuntimeException(String.format("no data found for %s", testCaseId));
	}

	public static HashMap<String, String> readTestDataFromGoogleSheet(String testCaseId, String sheetName)
			throws IOException, GeneralSecurityException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		final String spreadsheetId = configProperty.getProperty("sheetId");
		final String range = sheetName + "!" + configProperty.getProperty("range");
		Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
		List<List<Object>> values = response.getValues();
		List<Object> headers = values.get(0);
		values.remove(0);
		HashMap<String, String> testData = new HashMap<>();
		if (values == null || values.isEmpty()) {
			throw new RuntimeException(String.format("no data found for %s", testCaseId));
		} else {
			for (int i = 0; i < values.size(); i++) {
				for (int j = 0; j < values.get(i).size(); j++) {
					if (testCaseId.equals(values.get(i).get(0))) {
						testData.put(String.valueOf(headers.get(j)), String.valueOf(values.get(i).get(j)));
					}
				}
			}
		}
		return testData;
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.

		InputStream in = SheetsQuickstart.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
				.setAccessType("offline").build();

		in.close();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	public static HashMap<String, String> readTestDataFromDownloadsFolder(String workBookName, String workSheet, String testCaseId)
			throws IOException {
		String downloadPath = System.getProperty("user.home") + "/";
		if ((workBookName.split("\\.")[workBookName.split("\\.").length - 1]).equals("xlsx")) {
			XSSFWorkbook wb = new XSSFWorkbook(
					new FileInputStream(downloadPath+workBookName));
			XSSFSheet sheet = wb.getSheet(workSheet);
			XSSFRow headerRow = sheet.getRow(0);
			HashMap<String, Integer> headers = new HashMap<>();
			for (int c = 0; c < headerRow.getPhysicalNumberOfCells(); c++) {
				XSSFCell cell = headerRow.getCell(c);
				headers.put(cell.toString(), c);
			}
			if (!headers.containsKey("TC_ID"))
				throw new RuntimeException("no Tc_Id column found");

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (nonNull(row)) {
					XSSFCell idCell = row.getCell(headers.get("TC_ID"));
					if (StringUtils.equalsIgnoreCase(idCell.toString(), testCaseId)) {
						HashMap<String, String> testData = new HashMap<>();
						for (String key : headers.keySet()) {
							testData.put(key, Objects.toString(row.getCell(headers.get(key)), "").trim());
						}
						wb.close();
						return testData;
					}
				}
			}
		} else {
			POIFSFileSystem fs = new POIFSFileSystem(
					TestDataUtils.class.getResourceAsStream("/testdata/data/" + workBookName));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheet(workSheet);
			HSSFRow headerRow = sheet.getRow(0);
			HashMap<String, Integer> headers = new HashMap<>();
			for (int c = 0; c < headerRow.getPhysicalNumberOfCells(); c++) {
				HSSFCell cell = headerRow.getCell(c);
				headers.put(cell.toString(), c);
			}
			if (!headers.containsKey("TC_ID"))
				throw new RuntimeException("no Tc_Id column found");

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				HSSFRow row = sheet.getRow(i);
				if (nonNull(row)) {
					HSSFCell idCell = row.getCell(headers.get("TC_ID"));
					if (StringUtils.equalsIgnoreCase(idCell.toString(), testCaseId)) {
						HashMap<String, String> testData = new HashMap<>();
						for (String key : headers.keySet()) {
							testData.put(key, Objects.toString(row.getCell(headers.get(key)), "").trim());
						}
						return testData;
					}
				}
			}
		}
		throw new RuntimeException(String.format("no data found for %s", testCaseId));
	}

}