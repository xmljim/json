package io.xmljim.json.parsertest;

import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.JsonEventParserException;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {


    @Test
    void testA() {
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParserBuilder().setUseStrict(false).build();

        try (InputStream inputStream = getClass().getResourceAsStream("/books.json")) {
            JsonObject books = parser.parse(InputData.of(inputStream));
            assertNotNull(books);
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
            //System.out.println(parser.getStatistics());
        } catch (IOException ioe) {
            fail(ioe);
        }
    }

    @Test
    void testMissingValueError() {
        String json = """
            {
                "foo": "bar",
                "boo": 
            }
            """;
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParser();
        JsonEventParserException e = assertThrows(JsonEventParserException.class, () -> parser.parse(InputData.of(json)));
        assertTrue(e.getMessage().startsWith("Object entity missing value"));
    }

    @Test
    void testInvalidDelimiterError() {
        String json = """
            {
                "foo": "bar",
                "boo", "baz" 
            }
            """;
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParser();
        JsonEventParserException e = assertThrows(JsonEventParserException.class, () -> parser.parse(InputData.of(json)));
        assertTrue(e.getMessage().contains("Unexpected delimiter:"));
    }

    @Test
    void testExtraClosureError() {
        String json = """
            {
                "test": [
                    "a", "b", "c"
                ]]
            }
            """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParser();
        JsonEventParserException e = assertThrows(JsonEventParserException.class, () -> parser.parse(InputData.of(json)));
        assertTrue(e.getMessage().contains("Expected an object closure ('}'), but read array closure ']'"));
    }

    @Test
    void testMissingEnclosureError() {
        String json = """
            {
                "store": {
                    "book": [
                        {
                            "category": "reference",
                            "author": "Nigel Rees",
                            "title": "Sayings of the Century",
                            "price": 8.95
                        },
                        {
                            "category": "fiction",
                            "author": "Evelyn Waugh",
                            "title": "Sword of Honour",
                            "price": 12.99
                        },
                        {
                            "category": "fiction",
                            "author": "Herman Melville",
                            "title": "Moby Dick",
                            "isbn": "0-553-21311-3",
                            "price": 8.99
                        },
                        {
                            "category": "fiction",
                            "author": "J. R. R. Tolkien",
                            "title": "The Lord of the Rings",
                            "isbn": "0-395-19395-8",
                            "price": 22.99
                        }
                    ],
                    "bicycle": {
                        "color": "red",
                        "price": 19.95
                    }
                },
                "expensive": 10
                        
            """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParser();
        JsonEventParserException e = assertThrows(JsonEventParserException.class, () -> parser.parse(InputData.of(json)));
        assertTrue(e.getMessage().contains("Map container missing closing character '}'"));
    }


}