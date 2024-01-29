package ASPIREAI.customfactories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.openqa.selenium.support.How;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface IFindBy {

	String elementName() default "";

	How how() default How.UNSET;

	String using() default "";

	String id() default "";

	String name() default "";

	String className() default "";

	String css() default "";

	String tagName() default "";

	String linkText() default "";

	String partialLinkText() default "";

	String xpath() default "";

	String parent() default "";

	String child() default "";

	Init init() default Init.AI_INSTANCE;

	boolean AI() default true;

}
