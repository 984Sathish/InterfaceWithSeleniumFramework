package ASPIREAI.customfactories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ASPIREAI.customfactories.IFindBy;

/**
 * Used to mark a field on a Page Object to indicate that lookup should use a
 * series of @FindBy tags in a chain as described in
 * {@link org.openqa.selenium.support.pagefactory.ByChained}
 *
 * It can be used on a types as well, but will not be processed by default.
 *
 * Eg:
 *
 * <pre class="code">
 * &#64;FindBys({&#64;FindBy(id = "foo"),
 *           &#64;FindBy(className = "bar")})
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface IFindBys {
	IFindBy[] value();
}
