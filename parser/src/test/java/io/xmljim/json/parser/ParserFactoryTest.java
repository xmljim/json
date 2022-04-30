package io.xmljim.json.parser;

import io.xmljim.json.factory.parser.Parser;
import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Parser Factory Test")
public class ParserFactoryTest {

    @Test
    void testCreateParserFactory() {
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        assertNotNull(factory);

        Parser parser = factory.newParser();
        assertNotNull(parser);

    }

}