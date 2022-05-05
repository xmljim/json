package io.xmljim.json.jsonpath.function.info;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionDefinition {
    String name();


    String description() default "";

    ArgumentDefinition[] args() default {};
}
