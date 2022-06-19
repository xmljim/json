package io.github.xmljim.json.factory.mapper.annotation;

import io.github.xmljim.json.factory.mapper.ValueConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that is assigned to a field, method or class that
 * assigns ValueConverters to and from Json and a class instance
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface ConvertValue {

    ConverterArg[] toJsonArgs() default {};

    ConverterArg[] toValueArgs() default {};

    Class<? extends ValueConverter<?>> toJson();

    Class<? extends ValueConverter<?>> toValue();
}
