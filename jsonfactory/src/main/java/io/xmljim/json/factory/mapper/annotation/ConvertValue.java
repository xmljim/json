package io.xmljim.json.factory.mapper.annotation;

import io.xmljim.json.factory.mapper.ValueConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface ConvertValue {

    ConverterArg[] toJsonArgs() default {};

    ConverterArg[] toValueArgs() default {};

    Class<? extends ValueConverter<?>> toJson();

    Class<? extends ValueConverter<?>> toValue();
}
