package io.github.xmljim.json.factory.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the target class used for mapping to and from Json and Java
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
public @interface ConvertClass {
    /**
     * The target class
     *
     * @return the target class
     */
    Class<?> target();
}
