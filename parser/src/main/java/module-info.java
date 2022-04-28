import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.parser.ParserFactoryImpl;

module io.xmljim.jsonparser {
    requires transitive io.xmljim.json.elementfactory;
    requires io.xmljim.json.mapper;
    exports io.xmljim.json.parser;

    provides ParserFactory with ParserFactoryImpl;
}