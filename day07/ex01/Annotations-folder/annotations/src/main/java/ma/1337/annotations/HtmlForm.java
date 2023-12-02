
package ma._1337.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface HtmlForm {
    String fileName()

    default "index.html";

    String action()

    default "/";

    String method() default "get";
}
