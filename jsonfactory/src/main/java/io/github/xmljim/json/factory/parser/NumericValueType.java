package io.github.xmljim.json.factory.parser;

@FunctionalInterface
public interface NumericValueType {
    Number apply(String numericString);
}
