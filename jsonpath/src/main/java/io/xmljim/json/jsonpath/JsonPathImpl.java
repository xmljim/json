package io.xmljim.json.jsonpath;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.factory.jsonpath.ResultType;
import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.factory.parser.InputData;
import io.xmljim.json.factory.parser.Parser;
import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.variables.Global;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonNode;
import io.xmljim.json.service.ServiceManager;

import java.util.stream.Stream;

class JsonPathImpl implements JsonPath {

    private final Global globals;
    private ElementFactory elementFactory;
    private Parser parser;

    public JsonPathImpl(Global globals) {
        this.globals = globals;
    }

    @Override
    public JsonArray select(JsonNode jsonNode, String expression, ResultType resultType) {
        JsonArray array = getElementFactory().newArray();
        getResultStream(jsonNode, getFilterStream(expression))
            .forEach(context -> array.add(resultType == ResultType.VALUE ? context.get() : context.path()));
        return array;
    }

    @Override
    public JsonArray select(InputData inputData, String expression, ResultType resultType) {
        JsonNode jsonNode = getParser().parse(inputData);
        return select(jsonNode, expression, resultType);
    }

    private FilterStream getFilterStream(String expression) {
        return Compiler.newPathCompiler(expression, globals).compile();
    }

    private Stream<Context> getResultStream(JsonNode jsonNode, FilterStream filterStream) {
        return filterStream.filter(jsonNode);
    }

    private ElementFactory getElementFactory() {
        if (elementFactory == null) {
            elementFactory = ServiceManager.getProvider(ElementFactory.class);
        }

        return elementFactory;
    }

    private Parser getParser() {
        if (parser == null) {
            parser = ServiceManager.getProvider(ParserFactory.class).newParser();
        }

        return parser;
    }
}
