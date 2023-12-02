
package ma._1337.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface HtmlInput {

    String type() default "text";

    String name() default "";

    String placeholder() default "";
}
