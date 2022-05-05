import io.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.xmljim.json.jsonpath.JsonPathFactoryImpl;

module io.xmljim.json.jsonpath {
    requires transitive io.xmljim.jsonparser;

    exports io.xmljim.json.jsonpath;
    provides JsonPathFactory with JsonPathFactoryImpl;
}