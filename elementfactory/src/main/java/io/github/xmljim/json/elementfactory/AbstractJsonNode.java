package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonNode;
import io.github.xmljim.json.model.NodeType;

abstract class AbstractJsonNode extends AbstractJsonElement implements Comparable<JsonNode> {
    private final ElementFactory elementFactory;

    public AbstractJsonNode(ElementFactory elementFactory, NodeType nodeType, JsonElement parent) {
        super(nodeType, parent);
        this.elementFactory = elementFactory;
    }

    public abstract void clear();

    public abstract int size();

    public boolean isEmpty() {
        return size() == 0;
    }

    public ElementFactory getElementFactory() {
        return elementFactory;
    }
}
