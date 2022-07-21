module io.github.xmljim.json.jsonpath.test {
    //transitive dependencies on implementation modules for testing
    requires transitive io.github.xmljim.json.mapper;
    requires transitive io.github.xmljim.json.elementfactory;
    requires transitive io.github.xmljim.json.parser;
    //transitive dependency on "main"
    requires transitive io.github.xmljim.json.jsonpath;

    //These dependencies are needed to run tests
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;

    //exposes test classes to Jupiter tests
    opens io.github.xmljim.json.jsonpath.test;
    opens io.github.xmljim.json.jsonpath.test.predicate;
    opens io.github.xmljim.json.jsonpath.test.compiler;
    opens io.github.xmljim.json.jsonpath.test.function;
    opens io.github.xmljim.json.jsonpath.test.function.predicate;
    opens io.github.xmljim.json.jsonpath.test.predicate.expression;
    opens io.github.xmljim.json.jsonpath.test.function.node;
    opens io.github.xmljim.json.jsonpath.test.filter;
    opens io.github.xmljim.json.jsonpath.test.util;
}