package io.xmljim.json.jsonpath.function.info;

import io.xmljim.json.jsonpath.function.Argument;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ArgumentDefinition {

    String name();

    ArgumentScope scope();

    Class<? extends Argument<?, ?>> type();

    String description();
}
