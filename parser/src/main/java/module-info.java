import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.parser.ParserFactoryImpl;

module io.github.xmljim.json.parser {
    requires transitive io.github.xmljim.json.factory;
    opens io.github.xmljim.json.parser;
    provides ParserFactory with ParserFactoryImpl;

    uses ElementFactory;
}