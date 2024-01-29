package com.OrangeHRM.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDataUtils {

	private static File xmlFile = null;

	/**
	 * To set the path of the xml file to be parsed (place the xml files under
	 * /src/main/resources/testdata/data folder)
	 * 
	 * @param fileName
	 * @return String - file's canonical path
	 * @throws Exception
	 */
	private static String setFilePath(String fileName) throws Exception {
		String fileDirectory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "testdata" + File.separator + "data" + File.separator;
		xmlFile = new File(fileDirectory, fileName);
		if (!xmlFile.exists()) {
			throw new Exception("File does not exist : " + xmlFile.getCanonicalPath());
		}
		return xmlFile.getCanonicalPath();
	}

	/**
	 * To get the Document element of the xml file
	 * 
	 * @param fileName
	 * @return Document - document element
	 * @throws Exception
	 */
	private static Document getDocumentElement(String fileName) throws Exception {
	    setFilePath(fileName);

	    // Disable access to external entities
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	    dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

	    try (FileInputStream fileInputStream = new FileInputStream(xmlFile)) {
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        return dBuilder.parse(fileInputStream);
	    }
	}



	/**
	 * To fetch data using attributeName and value by xpath
	 * 
	 * @param fileName
	 * @param path     - Path in the form of
	 *                 attributeName1=value1|attributeName2=value2|... pair till the
	 *                 last node where the data is present
	 * @return HashMap<String, String> - key = tagname, value = text within the tag
	 * @throws Exception
	 */
	public static HashMap<String, String> fetchDataUsingXpath(String fileName, String path) throws Exception {
		String xpathAtttr[] = path.split("\\|");
		String formedXpath = "";
		for (String selector : xpathAtttr) {
			if (selector.split("=").length != 2) {
				Log.message("Ignoring invalid selector " + selector);
				continue;
			}
			formedXpath += ("//*[@" + selector.split("=")[0] + "='" + selector.split("=")[1] + "']");
		}
		return fetchDataByCustomXpath(fileName, formedXpath + "//child::node()");
	}

	/**
	 * To fetch data using custom xpath
	 * 
	 * @param fileName
	 * @param xpathSelector
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public static HashMap<String, String> fetchDataByCustomXpath(String fileName, String xpathSelector)
			throws Exception {
		HashMap<String, String> xmlData = new HashMap<String, String>();
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr = xpath.compile(xpathSelector);
		NodeList nList = (NodeList) expr.evaluate(getDocumentElement(fileName), XPathConstants.NODESET);

		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE && nList.item(i).hasChildNodes()) {
				xmlData.put(nList.item(i).getNodeName(), nList.item(i).getChildNodes().item(0).getNodeValue());
			}
		}
		return xmlData;
	}

	/**
	 * To fetch all data in the xml
	 * 
	 * @param fileName
	 * @return HashMap<String, String> - return the node value of all the nodes
	 * @throws Exception
	 */
	public static HashMap<String, String> fetchData(String fileName) throws Exception {
		HashMap<String, String> xmlData = new HashMap<String, String>();
		Document doc = getDocumentElement(fileName);
		NodeList nList = doc.getElementsByTagName("*");
		int count = 0;
		for (int i = 0; i < nList.getLength(); i++) {
			for (int j = 0; j < nList.item(i).getChildNodes().getLength(); j++) {
				if (nList.item(i).getChildNodes().item(j).getNodeType() == 3
						&& !nList.item(i).getChildNodes().item(j).getTextContent().contains("  ")) {
					if (xmlData.containsKey(nList.item(i).getNodeName() + count)) {
						count++;
					}
					if (!nList.item(i).getChildNodes().item(j).getTextContent().replaceAll("\\s+", "").equals("")) {
						xmlData.put(nList.item(i).getNodeName() + count,
								nList.item(i).getChildNodes().item(j).getTextContent());
					}
				}
			}
		}
		return xmlData;
	}

	/**
	 * To get tag value by tag name
	 * 
	 * @param fileName
	 * @param tagName
	 * @return String - return tag value
	 * @throws Exception
	 */
	public static ArrayList<String> getTagValues(String fileName, String tagName) throws Exception {
		ArrayList<String> tagValue = new ArrayList<String>();
		String tag = null;
		int count = 0;
		HashMap<String, String> xmlData = fetchData(fileName);
		for (int i = 0; i < xmlData.size(); i++) {
			tag = tagName + i;
			if (xmlData.get(tag) != null) {
				tagValue.add(count, xmlData.get(tag));
				count++;
			}
		}
		return tagValue;
	}

	/**
	 * To get tag name by tag value
	 * 
	 * @param xmlData
	 * @param tagValue
	 * @return String - return tag name
	 */
	public static String getTagName(HashMap<String, String> xmlData, String tagValue) {
		for (Map.Entry<String, String> entry : xmlData.entrySet()) {
			if (entry.getValue().equals(tagValue)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * To get attribute values by passing attribute name
	 * 
	 * @param fileName
	 * @param attrName
	 * @return ArrayList<String>
	 * @throws Exception
	 */
	public static ArrayList<String> getAttributeValuesByAtttributeName(String fileName, String attrName)
			throws Exception {
		ArrayList<String> returnData = new ArrayList<>();
		Document doc = getDocumentElement(fileName);
		NodeList nList = doc.getElementsByTagName("*");
		for (int i = 0; i < nList.getLength(); i++) {
			for (int j = 0; j < nList.item(i).getChildNodes().getLength(); j++) {
				if (nList.item(i).getChildNodes().item(j).hasAttributes()) {
					Attr attr = (Attr) nList.item(i).getChildNodes().item(j).getAttributes().getNamedItem(attrName);
					if (attr != null) {
						returnData.add(attr.getValue());
					}
				}
			}
		}
		return returnData;
	}

}
