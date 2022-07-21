module io.github.xmljim.json.factory {
    requires transitive io.github.xmljim.json.model;

    exports io.github.xmljim.json.factory.mapper;
    exports io.github.xmljim.json.factory.mapper.annotation;
    exports io.github.xmljim.json.factory.model;
    exports io.github.xmljim.json.factory.parser;
    exports io.github.xmljim.json.factory.parser.event;
    exports io.github.xmljim.json.factory.merge;
    exports io.github.xmljim.json.factory.merge.strategy;
    exports io.github.xmljim.json.service;
    exports io.github.xmljim.json.service.exception;
    exports io.github.xmljim.json.factory.jsonpath;
    exports io.github.xmljim.json.factory.config;
    exports io.github.xmljim.json.factory.mapper.parser;
}