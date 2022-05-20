package io.github.xmljim.json.jsonpath.function.info;

import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.util.DataType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ArgumentDefinition {

    String name();

    ArgumentScope scope();

    Class<? extends Argument<?, ?>> argType();

    DataType valueType() default DataType.ANY;

    String description();
}
