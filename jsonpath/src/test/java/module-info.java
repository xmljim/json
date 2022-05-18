module io.xmljim.json.jsonpathtest {
    //transitive dependencies on implementation modules for testing
    requires transitive io.xmljim.json.mapper;
    requires transitive io.xmljim.json.elementfactory;
    requires transitive io.xmljim.jsonparser;
    //transitive dependency on "main"
    requires transitive io.xmljim.json.jsonpath;

    //These dependencies are needed to run tests
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;

    //exposes test classes to Jupiter tests
    opens io.xmljim.json.jsonpathtest;
    opens io.xmljim.json.jsonpathtest.predicate;
    opens io.xmljim.json.jsonpathtest.compiler;
    opens io.xmljim.json.jsonpathtest.function;
    opens io.xmljim.json.jsonpathtest.function.predicate;
    opens io.xmljim.json.jsonpathtest.predicate.expression;
    opens io.xmljim.json.jsonpathtest.function.node;
    opens io.xmljim.json.jsonpathtest.filter;
}