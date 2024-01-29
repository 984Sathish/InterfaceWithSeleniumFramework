package ASPIREAI.customfactories;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;	
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import com.google.common.base.Preconditions;

/**
 * Abstract class to work with fields in Page Objects. Provides methods to
 * process {@link org.openqa.selenium.support.FindBy},
 * {@link org.openqa.selenium.support.FindBys} and
 * {@link org.openqa.selenium.support.FindAll} annotations.
 */
/**
 * @author lakshmi.nagandla
 *
 */
public abstract class CustomAbstractAnnotations {

	private Field field;
	public String temp, elementName, locator;
	public static By by;
	public static String propertiesFileName, configFilePath;

	/**
	 * Defines how to transform given object (field, class, etc) into
	 * {@link org.openqa.selenium.By} class used by webdriver to locate elements.
	 *
	 * @return By object
	 */
	public abstract By buildBy();

	/**
	 * Defines whether or not given element should be returned from cache on further
	 * calls.
	 *
	 * @return boolean if lookup cached
	 */

	protected By buildByFromFindBys(FindBys findBys) {
		assertValidFindBys(findBys);

		FindBy[] findByArray = findBys.value();
		By[] byArray = new By[findByArray.length];
		for (int i = 0; i < findByArray.length; i++) {
			byArray[i] = buildByFromFindBy(findByArray[i]);
		}
		return new ByChained(byArray);
	}

	/**
	 * @param findBys
	 * @return
	 */
	protected By buildBysFromFindByOneOf(FindAll findBys) {
		assertValidFindAll(findBys);

		FindBy[] findByArray = findBys.value();
		By[] byArray = new By[findByArray.length];
		for (int i = 0; i < findByArray.length; i++) {
			byArray[i] = buildByFromFindBy(findByArray[i]);
		}

		return new ByAll(byArray);
	}

	protected By buildBysFromIFindByOneOf(IFindAll ifindBys, Field field) {

		IFindBy[] findByArray = ifindBys.value();
		By[] byArray = new By[findByArray.length];
		for (int i = 0; i < findByArray.length; i++) {
			byArray[i] = buildByFromIFindBy(findByArray[i], field);
		}

		return new ByAll(byArray);
	}

	/**
	 * @param findBy
	 * @return
	 */
	protected By buildByFromFindBy(FindBy findBy) {
		assertValidFindBy(findBy);

		By ans = buildByFromShortFindBy(findBy);
		if (ans == null) {
			ans = buildByFromLongFindBy(findBy);
		}

		return ans;
	}

	/**
	 * @param ifindBy
	 * @param field
	 * @return
	 */
	protected By buildByFromIFindBy(IFindBy ifindBy, Field field) {

		if ((ifindBy.init()) != (Init.LOCAL_INSTANCE) && ifindBy.AI()) {
			By ans = buildByFromShortIFindBy(ifindBy, field);
			if (ans == null) {
				ans = buildByFromLongIFindBy(ifindBy, field);
			}
			return ans;
		} else {
			By ans = buildByFromShortFindBy(ifindBy);
			if (ans == null) {
				ans = buildByFromLongFindBy(ifindBy);
			}
			return ans;

		}

	}

	/**
	 * @param ifindBy
	 * @param field
	 * @return
	 */
	protected By buildByFromLongIFindBy(IFindBy ifindBy, Field field) {
		try {
		   Preconditions.checkArgument(ifindBy != null, "Failed to locate the annotation @IFindBy");
		   elementName = field.getName();
		   propertiesFileName = field.getDeclaringClass().getSimpleName();
		   String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
		     + File.separator + "resources" + File.separator + "properties" + File.separator;
		   configFilePath = basePath + propertiesFileName + ".properties";

		   Preconditions.checkArgument(isNotNullAndEmpty(elementName), "Element name is not found.");
		   Preconditions.checkArgument(isNotNullAndEmpty(propertiesFileName), "Locator file name is missing.");
		   Preconditions.checkArgument(isNotNullAndEmpty(propertiesFileName), "Locators File name not provided");

		   File file = new File(configFilePath);
		   Preconditions.checkArgument(file.exists(), "Unable to locate " + propertiesFileName);
		   
		   try (FileInputStream fileInput = new FileInputStream(file)) {
	            Properties prop = new Properties();

	            prop.load(fileInput);
	            locator = prop.getProperty(elementName);

	            if (locator.equals("")) {
	                return buildByFromLongFindBy(ifindBy);
	            }
	        }

	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    }


		  // Preconditions.checkArgument(isNotNullAndEmpty(locator), "Locator cannot be
		  // null (or) empty.");

		  How how = ifindBy.how();

		  switch (how) {
		  case ID:
		   return By.cssSelector(locator);

		  case NAME:
		   return By.cssSelector(locator);

		  case CLASS_NAME:
		   return By.cssSelector(locator);

		  case CSS:
		   return By.cssSelector(locator);

		  case TAG_NAME:
		   return By.cssSelector(locator);

		  case LINK_TEXT:
		   return By.cssSelector(locator);

		  case PARTIAL_LINK_TEXT:
		   return By.cssSelector(locator);

		  case XPATH:
		   return By.cssSelector(locator);

		  default:
		   // Note that this shouldn't happen (eg, the above matches all
		   // possible values for the How enum)
		   throw new IllegalArgumentException("Cannot determine how to locate element ");
		  }

		 }

	protected By buildByFromLongFindBy(IFindBy findBy) {

		How how = findBy.how();
		String using = findBy.using(), parent = findBy.parent(), child = findBy.child();
		if (using.equals(""))
			using = parent + " " + child;

		switch (how) {
		case CLASS_NAME:
			return By.className(using);

		case CSS:
			return By.cssSelector(using);

		case ID:
		case UNSET:
			return By.id(using);

		case ID_OR_NAME:
			return new ByIdOrName(using);

		case LINK_TEXT:
			return By.linkText(using);

		case NAME:
			return By.name(using);

		case PARTIAL_LINK_TEXT:
			return By.partialLinkText(using);

		case TAG_NAME:
			return By.tagName(using);

		case XPATH:
			return By.xpath(using);

		default:
			// Note that this shouldn't happen (eg, the above matches all
			// possible values for the How enum)
			throw new IllegalArgumentException("Cannot determine how to locate element ");
		}
	}

	/**
	 * @param findBy
	 * @return
	 */
	protected By buildByFromLongFindBy(FindBy findBy) {
		How how = findBy.how();
		String using = findBy.using();

		switch (how) {
		case CLASS_NAME:
			return By.className(using);

		case CSS:
			return By.cssSelector(using);

		case ID:
		case UNSET:
			return By.id(using);

		case ID_OR_NAME:
			return new ByIdOrName(using);

		case LINK_TEXT:
			return By.linkText(using);

		case NAME:
			return By.name(using);

		case PARTIAL_LINK_TEXT:
			return By.partialLinkText(using);

		case TAG_NAME:
			return By.tagName(using);

		case XPATH:
			return By.xpath(using);

		default:
			// Note that this shouldn't happen (eg, the above matches all
			// possible values for the How enum)
			throw new IllegalArgumentException("Cannot determine how to locate element ");
		}
	}

	/**
	 * @param ifindBy
	 * @param field
	 * @return
	 */
	protected By buildByFromShortIFindBy(IFindBy ifindBy, Field field) {
	    if (ifindBy == null) {
	        System.out.println("The parameter 'ifindBy' is null.");
	        return null;
	    }

	    try (FileInputStream fileInput = new FileInputStream(new File(configFilePath))) {
	        Properties prop = new Properties();
	        Preconditions.checkArgument(ifindBy != null, "Failed to locate the annotation @IFindBy");
	        elementName = field.getName();
	        propertiesFileName = field.getDeclaringClass().getSimpleName();
	        String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
	                + File.separator + "resources" + File.separator + "properties" + File.separator;
	        configFilePath = basePath + propertiesFileName + ".properties";

	        Preconditions.checkArgument(isNotNullAndEmpty(elementName), "Element name is not found.");
	        Preconditions.checkArgument(isNotNullAndEmpty(propertiesFileName), "Locator file name is missing.");
	        Preconditions.checkArgument(isNotNullAndEmpty(propertiesFileName), "Locators File name not provided");

	        File file = new File(configFilePath);
	        Preconditions.checkArgument(file.exists(), "Unable to locate " + propertiesFileName);

	        prop.load(fileInput);
	        locator = prop.getProperty(elementName);

	        if (locator == null) {
	            String key = elementName;
	            WriteProperties.storePropertyKey(key, propertiesFileName);

	            // Reopen the file with a new FileInputStream
	            try (FileInputStream fileInput2 = new FileInputStream(file)) {
	                prop.load(fileInput2);
	                locator = prop.getProperty(elementName);
	            }
	        }

	        if (locator.equals("")) {
	            By ans = buildByFromShortFindBy(ifindBy);
	            return ans;
	        }
	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }

		// Preconditions.checkArgument(isNotNullAndEmpty(locator), "Locator cannot be
		// null (or) empty.");

		if (!"".equals(ifindBy.className()))
			return By.cssSelector(locator);

		if (!"".equals(ifindBy.css()))
			return By.cssSelector(locator);

		if (!"".equals(ifindBy.id()))
			return By.cssSelector(locator);

		if (!"".equals(ifindBy.linkText()))
			return By.cssSelector(locator);

		if (!"".equals(ifindBy.name()))
			return By.cssSelector(locator);

		if (!"".equals(ifindBy.partialLinkText()))
			return By.cssSelector(locator);

		if (!"".equals(ifindBy.tagName()))
			return By.cssSelector(locator);

		if (!"".equals(ifindBy.xpath()))
			return By.cssSelector(locator);

		// Fall through
		return null;
	}

	/**
	 * @param findBy
	 * @return
	 */
	protected By buildByFromShortFindBy(IFindBy findBy) {
		if (!"".equals(findBy.className()))
			return By.className(findBy.className());

		if (!"".equals(findBy.css()))
			return By.cssSelector(findBy.css());

		if (!"".equals(findBy.id()))
			return By.id(findBy.id());

		if (!"".equals(findBy.linkText()))
			return By.linkText(findBy.linkText());

		if (!"".equals(findBy.name()))
			return By.name(findBy.name());

		if (!"".equals(findBy.partialLinkText()))
			return By.partialLinkText(findBy.partialLinkText());

		if (!"".equals(findBy.tagName()))
			return By.tagName(findBy.tagName());

		if (!"".equals(findBy.xpath()))
			return By.xpath(findBy.xpath());

		// Fall through
		return null;
	}

	/**
	 * @param findBy
	 * @return
	 */
	protected By buildByFromShortFindBy(FindBy findBy) {
		if (!"".equals(findBy.className()))
			return By.className(findBy.className());

		if (!"".equals(findBy.css()))
			return By.cssSelector(findBy.css());

		if (!"".equals(findBy.id()))
			return By.id(findBy.id());

		if (!"".equals(findBy.linkText()))
			return By.linkText(findBy.linkText());

		if (!"".equals(findBy.name()))
			return By.name(findBy.name());

		if (!"".equals(findBy.partialLinkText()))
			return By.partialLinkText(findBy.partialLinkText());

		if (!"".equals(findBy.tagName()))
			return By.tagName(findBy.tagName());

		if (!"".equals(findBy.xpath()))
			return By.xpath(findBy.xpath());

		// Fall through
		return null;
	}

	/**
	 * @param findBys
	 */
	private void assertValidFindBys(FindBys findBys) {
		for (FindBy findBy : findBys.value()) {
			assertValidFindBy(findBy);
		}
	}

	/**
	 * @param findBys
	 */
	private void assertValidFindAll(FindAll findBys) {
		for (FindBy findBy : findBys.value()) {
			assertValidFindBy(findBy);
		}
	}

	/**
	 * @param findBy
	 */
	private void assertValidFindBy(FindBy findBy) {
		if (findBy.how() != null) {
			if (findBy.using() == null) {
				throw new IllegalArgumentException("If you set the 'how' property, you must also set 'using'");
			}
		}

		Set<String> finders = new HashSet<>();
		if (!"".equals(findBy.using()))
			finders.add("how: " + findBy.using());
		if (!"".equals(findBy.className()))
			finders.add("class name:" + findBy.className());
		if (!"".equals(findBy.css()))
			finders.add("css:" + findBy.css());
		if (!"".equals(findBy.id()))
			finders.add("id: " + findBy.id());
		if (!"".equals(findBy.linkText()))
			finders.add("link text: " + findBy.linkText());
		if (!"".equals(findBy.name()))
			finders.add("name: " + findBy.name());
		if (!"".equals(findBy.partialLinkText()))
			finders.add("partial link text: " + findBy.partialLinkText());
		if (!"".equals(findBy.tagName()))
			finders.add("tag name: " + findBy.tagName());
		if (!"".equals(findBy.xpath()))
			finders.add("xpath: " + findBy.xpath());

		// A zero count is okay: it means to look by name or id.
		if (finders.size() > 1) {
			throw new IllegalArgumentException(
					String.format("You must specify at most one location strategy. Number found: %d (%s)",
							finders.size(), finders.toString()));
		}
	}

	/**
	 * @return
	 */
	public boolean isLookupCached() {
		return (field.getAnnotation(CacheLookup.class) != null);
	}

	/**
	 * @return
	 */
	protected Field getField() {
		return field;
	}

	/**
	 * @param arg
	 * @return
	 */
	private boolean isNotNullAndEmpty(String arg) {
		return ((arg != null) && (!arg.trim().isEmpty()));
	}

	/**
	 * @return
	 */
	protected By buildByFromDefault() {
		return new ByIdOrName(field.getName());
	}

	/**
	 *
	 */
	protected void assertValidAnnotations() {
		FindBys findBys = field.getAnnotation(FindBys.class);
		FindAll findAll = field.getAnnotation(FindAll.class);
		FindBy findBy = field.getAnnotation(FindBy.class);
		if (findBys != null && findBy != null) {
			throw new IllegalArgumentException(
					"If you use a '@FindBys' annotation, " + "you must not also use a '@FindBy' annotation");
		}
		if (findAll != null && findBy != null) {
			throw new IllegalArgumentException(
					"If you use a '@FindAll' annotation, " + "you must not also use a '@FindBy' annotation");
		}
		if (findAll != null && findBys != null) {
			throw new IllegalArgumentException(
					"If you use a '@FindAll' annotation, " + "you must not also use a '@FindBys' annotation");
		}
	}

}