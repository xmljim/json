import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.factory.test.TestServiceClass;

module io.github.xmljim.json.factory.test {

    requires transitive io.github.xmljim.json.factory;

    opens io.github.xmljim.json.factory.test;

    //These dependencies are needed to run tests
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;

    provides ParserFactory with TestServiceClass;
}