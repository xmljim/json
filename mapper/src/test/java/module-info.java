module io.xmljim.json.mapper.test {
    requires transitive io.github.xmljim.json.elementfactory;
    requires transitive io.github.xmljim.json.mapper;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    opens io.xmljim.json.mapper.test;
    opens io.xmljim.json.mapper.test.testclasses;
}