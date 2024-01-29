package com.OrangeHRM.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONParsarUtils {

	/**
	 * Used to read from JSON file
	 * @param filename - JSON file name
	 * @return json content in string format
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static String readFileFromJsonFile(String filename) throws URISyntaxException, IOException {
	    StringWriter writer = new StringWriter();
	    IOUtils.copy(JSONParsarUtils.class.getResourceAsStream("/mockjson/" +  filename), writer, StandardCharsets.UTF_8);
	    return writer.toString();  
	}

    public static JsonObject getJSONData(String data) {
        return JsonParser.parseString(data).getAsJsonObject();
    }

    public static JsonObject getJSONData(File data) throws Exception {
        return new Gson().fromJson(new BufferedReader(new FileReader(data)), JsonObject.class);
    }

    public static JsonObject getJsonObject(JsonObject jsonData, String key) {
        return jsonData.getAsJsonObject(key);
    }

    public static JsonObject getJsonObject(JsonArray jsonArray, int index) {
        return jsonArray.get(index).getAsJsonObject();
    }

    public static JsonArray getJsonArray(JsonObject jsonData, String key) {
        return jsonData.getAsJsonArray(key);
    }

    public static String getValue(JsonObject jsonData, String key) {
        return jsonData.get(key).getAsString();
    }

    public static List<String> getSearchTitles(String json) {
    	List<String> titles = new ArrayList<String>();
    	JsonArray contentList = JSONParsarUtils.getJsonArray(JSONParsarUtils.getJsonObject(JSONParsarUtils.getJsonObject(JSONParsarUtils.getJSONData(json), "data"), "getContentById"), "contentItems");
    	for(int i = 0; i < contentList.size(); i++) {
    		titles.add(i, JSONParsarUtils.getValue(JSONParsarUtils.getJsonObject(contentList, i), "title"));
    	}
    	return titles;
    }
    
    public static List<String> getSearchContent(String json) {
    	List<String> titles = new ArrayList<String>();
    	JsonArray contentList = JSONParsarUtils.getJsonArray(JSONParsarUtils.getJsonObject(JSONParsarUtils.getJsonObject(JSONParsarUtils.getJSONData(json), "data"), "getSearchResult"), "contents");
    	for(int i = 0; i < contentList.size(); i++) {
    		titles.add(i, JSONParsarUtils.getValue(JSONParsarUtils.getJsonObject(contentList, i), "title"));
    	}
    	return titles;
    }
}
