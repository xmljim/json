module io.xmljim.jsonparser.test {
    requires transitive io.xmljim.jsonparser;
    requires transitive io.xmljim.json.elementfactory;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    opens io.xmljim.json.parsertest;
}