import io.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.jsonpath.JsonPathFactoryImpl;

module io.xmljim.json.jsonpath {
    //transitive dependencies
    requires transitive io.xmljim.jsonfactory;

    // limit access to jsonfactory module: Require consumers to go through ServiceManager;
    // can't access public classes directly
    // however - important that packages are available to testing module at compile time.
    opens io.xmljim.json.jsonpath to io.xmljim.jsonfactory;
    exports io.xmljim.json.jsonpath to io.xmljim.json.jsonpathtest;
    opens io.xmljim.json.jsonpath.function to io.xmljim.jsonfactory;
    exports io.xmljim.json.jsonpath.function to io.xmljim.json.jsonpathtest;

    //exports for testing only - not intended for public consumption
    exports io.xmljim.json.jsonpath.function.predicate to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.function.node to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.function.aggregate to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.function.info to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.function.document to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.function.string to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.util to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.context to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.filter to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.compiler to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.predicate to io.xmljim.json.jsonpathtest;
    exports io.xmljim.json.jsonpath.predicate.expression to io.xmljim.json.jsonpathtest;

    //Ensure that we have at least one implementation of these services on the classpath...
    uses ElementFactory;
    uses ParserFactory;
    uses MapperFactory;

    //"register" a service provider
    provides JsonPathFactory with JsonPathFactoryImpl;
}