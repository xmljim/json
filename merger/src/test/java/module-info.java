module io.github.xmljim.json.merger.test {
    // "Parent" project
    requires transitive io.github.xmljim.json.merger;

    // "Component" (sibling) project dependencies for testing
    requires transitive io.github.xmljim.json.parser;
    requires transitive io.github.xmljim.json.elementfactory;

    //These dependencies are needed to run tests
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;

    opens io.github.xmljim.json.merger.test;
}