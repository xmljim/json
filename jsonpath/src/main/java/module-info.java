import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.jsonpath.JsonPathFactoryImpl;

module io.github.xmljim.json.jsonpath {
    //transitive dependencies
    requires transitive io.github.xmljim.json.factory;

    // limit access to jsonfactory module: Require consumers to go through ServiceManager;
    // can't access public classes directly
    // however - important that packages are available to testing module at compile time.
    opens io.github.xmljim.json.jsonpath to io.github.xmljim.json.factory;

    //export function package globally so that custom functions can be created;
    exports io.github.xmljim.json.jsonpath.function;

    //exports for testing only - not intended for public consumption
    exports io.github.xmljim.json.jsonpath to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.function.predicate to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.function.node to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.function.aggregate to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.function.info to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.function.document to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.function.string to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.util to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.context to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.filter to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.compiler to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.predicate to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.predicate.expression to io.github.xmljim.json.jsonpath.test;
    exports io.github.xmljim.json.jsonpath.function.date to io.github.xmljim.json.jsonpath.test;

    //Ensure that we have at least one implementation of these services on the classpath...
    uses ElementFactory;
    uses ParserFactory;
    uses MapperFactory;

    //"register" a service provider
    provides JsonPathFactory with JsonPathFactoryImpl;
}