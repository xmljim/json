package io.github.xmljim.json.jsonpath;

import io.github.xmljim.json.factory.jsonpath.JsonPathBuilder;
import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.service.JsonServiceProvider;

@JsonServiceProvider(service = JsonPathFactory.class, isNative = true, version = "1.0.1")
public class JsonPathFactoryImpl implements JsonPathFactory {
    @Override
    public JsonPathBuilder newJsonPathBuilder() {
        return new JsonPathBuilderImpl();
    }

}
