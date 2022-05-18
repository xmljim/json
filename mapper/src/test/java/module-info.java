module io.xmljim.json.mapper.test {
    requires transitive io.xmljim.json.elementfactory;
    requires transitive io.xmljim.json.mapper;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    opens io.xmljim.json.mappertest;
    opens io.xmljim.json.mappertest.testclasses;
}