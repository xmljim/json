package io.xmljim.json.parsertest;

import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.service.ServiceManager;
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
