package io.xmljim.json.parser.event;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.factory.parser.event.JsonAssembler;
import io.xmljim.json.model.JsonNode;
import io.xmljim.json.service.ServiceManager;

import java.util.function.Supplier;

abstract class AbstractJsonAssembler extends AbstractAssembler<JsonNode> implements JsonAssembler {
    protected JsonNode result;
    private volatile boolean documentComplete;
    private final ElementFactory elementFactory;

    public AbstractJsonAssembler() {
        this.elementFactory = ServiceManager.getProvider(ElementFactory.class);
    }

    public ElementFactory getElementFactory() {
        return elementFactory;
    }

    @Override
    public void documentEnd() {
        documentComplete = true;
    }

    @Override
    public Supplier<JsonNode> getResult() {
        while (!documentComplete) {
            Thread.onSpinWait();
        }

        return () -> result;
    }
}
