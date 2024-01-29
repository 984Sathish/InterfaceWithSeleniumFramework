package ASPIREAI.customfactories;

import java.lang.reflect.Field;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import com.OrangeHRM.utils.Log;

public class CustomAnnotation extends CustomAbstractAnnotations {

	private Field field;
	public String temp, elementName, locator;
	public static By by;
	public static String propertiesFileName, configFilePath;

	public CustomAnnotation(Field field)  {
		propertiesFileName = field.getDeclaringClass().getSimpleName();
		PageFactoryList.pageFactoryKey.add((temp = field.getName()).toString().trim());
		// System.out.println(field.getName()+" = ");
		try {
			field.setAccessible(true);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());			}
		}
		this.field = field;
	}

	/*
	 * @see org.openqa.selenium.support.pagefactory.AbstractAnnotations#buildBy()
	 */

	@Override
	public By buildBy() {

		IFindBy ifindBy = field.getAnnotation(IFindBy.class);
		if (ifindBy != null) {
			by = buildByFromIFindBy(ifindBy, field);
		}

		IFindAll ifindAll = field.getAnnotation(IFindAll.class);
		if (by == null && ifindAll != null) {
			by = buildBysFromIFindByOneOf(ifindAll, field);
		}

		FindBys findBys = field.getAnnotation(FindBys.class);
		if (findBys != null) {
			by = buildByFromFindBys(findBys);
		}

		FindAll findAll = field.getAnnotation(FindAll.class);
		if (by == null && findAll != null) {
			by = buildBysFromFindByOneOf(findAll);
		}

		FindBy findBy = field.getAnnotation(FindBy.class);
		if (findBy != null) {
			by = buildByFromFindBy(findBy);

		}

		if (by == null) {
			by = buildByFromDefault();
		}

		if (by == null) {
			throw new IllegalArgumentException("Cannot determine how to locate element " + field);
		}

		return by;
	}

	@Override
	public boolean isLookupCached() {
		return (field.getAnnotation(CacheLookup.class) != null);
	}

	protected Field getField() {
		return field;
	}

	protected By buildByFromDefault() {
		return new ByIdOrName(field.getName());
	}

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
