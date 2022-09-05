package io.github.xmljim.json.factory.serializer;

import io.github.xmljim.json.model.NodeType;

public interface SerializationContext {

    NodeType getNodeType();

    String getPath();

    <T> T getAccessor();

    SerializationContext getParent();
}
