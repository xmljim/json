package io.github.xmljim.json.jsonpath.test;

import io.github.xmljim.json.factory.jsonpath.JsonPath;
import io.github.xmljim.json.factory.jsonpath.JsonPathBuilder;
import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.jsonpath.compiler.Compiler;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.filter.FilterStream;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.util.Settings;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonNode;
import io.github.xmljim.json.service.ServiceManager;

import java.util.function.Predicate;

public class JsonPathTestBase {

    public static JsonNode loadData(String classResource) throws Exception {
        try (InputData data = InputData.of(JsonPathTestBase.class.getResourceAsStream(classResource))) {
            ParserFactory parserFactory = ServiceManager.getProvider(ParserFactory.class);
            Parser parser = parserFactory.newParserBuilder().setUseStrict(false).build();
            return parser.parse(data);
        }
    }

    public static JsonPath getJsonPath() {
        return getJsonPathFactory().newJsonPath();
    }

    public static JsonPathBuilder getJsonPathBuilder() {
        return getJsonPathFactory().newJsonPathBuilder();
    }

    public static JsonPathFactory getJsonPathFactory() {
        return ServiceManager.getProvider(JsonPathFactory.class);
    }

    public static JsonArray selectForTest(String classResource, String expression) throws Exception {
        JsonNode node = loadData(classResource);
        return getJsonPath().select(node, expression);
    }

    public Compiler<FilterStream> newPathCompiler(String expression, Global global) {
        return Compiler.newPathCompiler(expression, global);
    }

    public Compiler<FilterStream> newPathCompiler(String expression) {
        return newPathCompiler(expression, new Settings());
    }

    public Compiler<Predicate<Context>> newPredicateCompiler(String expression, Global global) {
        return Compiler.newPredicateCompiler(expression, global);
    }

    public Compiler<Predicate<Context>> newPredicateCompiler(String expression) {
        return Compiler.newPredicateCompiler(expression, new Settings());
    }

    public FilterStream getFilterStream(String expression, Global global) {
        return newPathCompiler(expression, global).compile();
    }

    public FilterStream getFilterStream(String expression) {
        return getFilterStream(expression, new Settings());
    }

    public Predicate<Context> getPredicateContext(String expression, Global global) {
        return newPredicateCompiler(expression, global).compile();
    }

    public Predicate<Context> getPredicateContext(String expression) {
        return getPredicateContext(expression, new Settings());
    }
}
