package io.github.xmljim.json.factory.serializer;

import io.github.xmljim.json.model.JsonElement;

public interface ElementContext {
    JsonElement getElement();

    String getPath();

    boolean matches(ElementContext nodeContext);

    default boolean doesNotMatch(ElementContext nodeContext) {
        return !matches(nodeContext);
    }
}
