package io.xmljim.json.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JsonServiceProvider {
    String version();
    boolean isNative() default false;
    int priority() default 0;
    Class<? extends JsonService> service();
}
