package com.OrangeHRM.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FileUtils consists copy/move a file from source to destination location
 */
public class FileUtils {

	/**
	 * Copy a file from one location to another
	 * 
	 * @param f1 - Source file
	 * @param f2 - Destination File
	 * @throws IOException
	 */
	public static void copyFile(File f1, File f2) throws IOException {
	    try (InputStream in = new FileInputStream(f1); OutputStream out = new FileOutputStream(f2)) {

	        // For Overwrite the file.
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	    }
	}

	/***
	 * Method move the file from source location, rename it and place it in the
	 * destination location and deletes the old file from the destination location
	 * if any
	 * 
	 * @param oldFile
	 * @param newFile
	 * @throws IOException
	 */
	
	public static void moveFile(String oldFile, String newFile) throws IOException {
		File oldfile = new File(oldFile);
		File newfile = new File(newFile);
		copyFile(oldfile, newfile);
		boolean isDeleted =oldfile.delete();
		Log.softAssertThat(isDeleted, "Successfully deleted the old file: " +oldFile, "Failed to delete the old file:" +oldFile);
		}

	/**
	 * Method reads content from file and returns the content available in the file
	 * 
	 * @param FileName - Source file
	 * @return text - content of file
	 * @throws Exception
	 */
	public static String readFile(String fileName) throws Exception {
	    String text = "";
	    try (FileInputStream textFile = new FileInputStream(System.getProperty("user.dir") + fileName)) {

	        int i = 0;
	        while ((i = textFile.read()) != -1) {
	            text = text + (char) i;
	        }
	    } catch (Exception e) {
	        Log.exception(e);
	        Log.message("Something went wrong while reading a file");
	    }
	    return text;
	}


}
