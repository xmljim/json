package io.xmljim.json.jsonpath.function.info;

import io.xmljim.json.jsonpath.variables.BuiltIns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionDefinition {
    String name() default "UNDEFINED";

    String description() default "";

    String category() default "";

    String classification() default "";

    BuiltIns builtIn() default BuiltIns.UNDEFINED;

    ArgumentDefinition[] args() default {};
}
