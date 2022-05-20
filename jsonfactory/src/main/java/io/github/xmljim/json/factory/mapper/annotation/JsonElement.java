package io.github.xmljim.json.factory.mapper.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonElement {
    /**
     * Specifies the JSON key to use. Only required if the field name is different than the JSON element's key.
     *
     * @return the JSON key mapped to this field. If empty, the Field name will be used, otherwise the key value will be used.
     */
    String key() default "";

    /**
     * Specifies a setter method to use to apply the JSON value to the Class value.
     * Only required if the JSONValue should not be set directly to the Field
     * By default, the value is empty indicating that the value should be applied to the Field's set() method.
     * If the setterMethod value is set, then the JSONValue is applied to the method.
     * <p><strong>NOTE:</strong> The method must be public and only accept one parameter, or it will throw a JSONConversionException
     *
     * @return the methodName, or an empty String (default).</p>
     */
    String setterMethod() default "";

    /**
     * Specifies a getter method to use to retrieve a Class value to be set in a JSON value.
     * Only required if the value should not be retrieved directly from the field.
     * <p><strong>NOTE: <strong>The method must be public and contain no parameters with a return parameter (can't be
     * void), or a JSONConversionException will be thrown</p>
     *
     * @return the getter method name, or an empty string (default)
     */
    String getterMethod() default "";

    boolean ignore() default false;
}
