module io.github.xmljim.json.api {
    
    requires transitive io.github.xmljim.json.elementfactory;
    requires transitive io.github.xmljim.json.parser;
    requires transitive io.github.xmljim.json.merger;
    requires transitive io.github.xmljim.json.mapper;
    requires transitive io.github.xmljim.json.jsonpath;

    exports io.github.xmljim.json.api;
}