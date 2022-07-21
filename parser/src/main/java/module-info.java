import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.parser.ParserFactoryImpl;

module io.github.xmljim.json.parser {
    requires transitive io.github.xmljim.json.factory;
    exports io.github.xmljim.json.parser to io.xmljim.jsonparser.test;
    exports io.github.xmljim.json.parser.event to io.xmljim.jsonparser.test;
    exports io.github.xmljim.json.parser.util to io.xmljim.jsonparser.test;
    opens io.github.xmljim.json.parser to io.github.xmljim.json.factory;
    provides ParserFactory with ParserFactoryImpl;
    //exports io.github.xmljim.json.parser.event to io.github.xmljim.json.mapper;

    uses ElementFactory;
}