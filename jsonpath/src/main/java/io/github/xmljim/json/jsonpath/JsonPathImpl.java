package io.github.xmljim.json.jsonpath;

import io.github.xmljim.json.factory.jsonpath.JsonPath;
import io.github.xmljim.json.factory.jsonpath.JsonPathError;
import io.github.xmljim.json.factory.jsonpath.ResultType;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.jsonpath.compiler.Compiler;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.filter.FilterStream;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonNode;
import io.github.xmljim.json.service.ServiceManager;

import java.util.Collections;
import java.util.List;
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

    @Override
    public List<JsonPathError> getErrors() {
        //TODO: integrate with global
        return Collections.emptyList();
    }
}
