import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.parser.ParserFactoryImpl;

module io.xmljim.jsonparser {
    requires transitive io.xmljim.jsonfactory;
    opens io.xmljim.json.parser;
    provides ParserFactory with ParserFactoryImpl;

    uses ElementFactory;
}