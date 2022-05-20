package io.xmljim.json.parsertest;

import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {


    @Test
    void testA() {
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParserBuilder().setUseStrict(false).build();

        try (InputStream inputStream = getClass().getResourceAsStream("/books.json")) {
            JsonObject books = parser.parse(InputData.of(inputStream));
            assertNotNull(books);
            System.out.println(parser.getStatistics());
        } catch (IOException ioe) {
            fail(ioe);
        }
    }

    @Test
    public void testB() {

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParserBuilder().setUseStrict(false).build();

        try (InputStream inputStream = getClass().getResourceAsStream("/AllSets.json")) {
            JsonObject allSets = parser.parse(InputData.of(inputStream));
            assertNotNull(allSets);
            System.out.println(parser.getStatistics());
        } catch (IOException ioe) {
            fail(ioe);
        }
    }
}