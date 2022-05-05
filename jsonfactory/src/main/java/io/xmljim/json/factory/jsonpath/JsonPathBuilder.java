package io.xmljim.json.factory.jsonpath;

public interface JsonPathBuilder {

    JsonPath build();

    <T> JsonPathBuilder setVariable(String name, T value);

    <T> JsonPathBuilder setProperty(String name, T value);
}
