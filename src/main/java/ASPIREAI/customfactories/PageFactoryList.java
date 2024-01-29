package ASPIREAI.customfactories;

import java.util.ArrayList;
import java.util.List;

public class PageFactoryList {

	public static List<String> pageFactoryKey = new ArrayList<String>();
	public static List<String> pageFactoryValue = new ArrayList<String>();

	public static List<String> getPageFatoryKey() {
		return pageFactoryKey;
	}

	public static List<String> getPageFactoryValue() {
		return pageFactoryValue;
	}

	public static void flushPageFactoryList() {
		pageFactoryKey.clear();
		pageFactoryValue.clear();
	}

}
