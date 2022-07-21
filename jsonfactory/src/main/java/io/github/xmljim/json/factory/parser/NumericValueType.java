package io.github.xmljim.json.factory.parser;


/**
 * Applies a number value to a numeric string
 */
@FunctionalInterface
public interface NumericValueType {
    Number apply(String numericString);
}
