package io.xmljim.json.jsonpath.function.info;

import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.util.DataType;

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
