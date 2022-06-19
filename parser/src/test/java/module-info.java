module io.xmljim.jsonparser.test {
    requires transitive io.github.xmljim.json.parser;
    requires transitive io.github.xmljim.json.elementfactory;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    opens io.xmljim.json.parsertest;
    opens io.xmljim.json.parsertest.util;
}