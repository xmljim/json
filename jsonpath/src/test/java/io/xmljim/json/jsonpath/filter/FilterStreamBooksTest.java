package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.factory.parser.InputData;
import io.xmljim.json.factory.parser.Parser;
import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.jsonpath.Variables;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FilterStream Tests - books.json")
public class FilterStreamBooksTest {

    @Test
    @DisplayName("Get all book authors (dot notation): $.store.book.*.author")
    void testGetAllBookAuthorsDotNotation() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$.store.book.*.author";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        assertEquals(4, results.size());
        assertEquals("Nigel Rees", results.get(0).get().asJsonValue().get());
        assertEquals("Evelyn Waugh", results.get(1).get().asJsonValue().get());
        assertEquals("Herman Melville", results.get(2).get().asJsonValue().get());
        assertEquals("J. R. R. Tolkien", results.get(3).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get all book authors (bracket notation): $['store']['book'][*]['author']")
    void testGetAllBookAuthorsBracketNotation() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$['store']['book'][*]['author']";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        assertEquals(4, results.size());
        assertEquals("Nigel Rees", results.get(0).get().asJsonValue().get());
        assertEquals("Evelyn Waugh", results.get(1).get().asJsonValue().get());
        assertEquals("Herman Melville", results.get(2).get().asJsonValue().get());
        assertEquals("J. R. R. Tolkien", results.get(3).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get all book authors (recursive): $['store'][..]['author']")
    void testGetAllBookAuthorsRecursive() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$['store'][..]['author']";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        assertEquals(4, results.size());
        assertEquals("Nigel Rees", results.get(0).get().asJsonValue().get());
        assertEquals("Evelyn Waugh", results.get(1).get().asJsonValue().get());
        assertEquals("Herman Melville", results.get(2).get().asJsonValue().get());
        assertEquals("J. R. R. Tolkien", results.get(3).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get the price of all items in the store: $.store..price")
    void testGetThePriceOfAllStoreItems() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$.store..price";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        assertEquals(5, results.size());
        assertEquals(8.95, results.get(0).get().asJsonValue().get());
        assertEquals(12.99, results.get(1).get().asJsonValue().get());
        assertEquals(8.99, results.get(2).get().asJsonValue().get());
        assertEquals(22.99, results.get(3).get().asJsonValue().get());
        assertEquals(19.95, results.get(4).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get a book at a specific index: $..book[2]")
    void testGetBookAtIndex() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[2]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(1, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
    }


    @Test
    @DisplayName("Get the second to last book: $..book[-2]")
    void testGetBookAtIndexFromTail() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[-2]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(1, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the first two books (union): $..book[0,1]")
    void testGetBooksFirstTwoBooksUnion() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[0,1]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Sword of Honour", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the first two books (slice): $..book[:2]")
    void testGetBooksFirstTwoBooksSlice() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[:2]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Sword of Honour", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the middle two books (slice): $..book[1:3]")
    void testGetBooksMiddleTwoBooksSlice() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[1:3]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sword of Honour", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the last two books (slice): $..book[2:]")
    void testGetBookAtIndexFromTailSlice() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[2:]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
        assertEquals("The Lord of the Rings", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books with an ISBN number: $..book[?(@.isbn)]")
    void testGetBooksWithIsNotNullPredicate() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[?(@.isbn)]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
        assertEquals("The Lord of the Rings", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books without an ISBN number: $..book[?(!@.isbn)]")
    void testGetBooksWithIsNotNullPredicateNegated() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[?(!@.isbn)]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Sword of Honour", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books cheaper than 10: $..book[?(@.price < 10)]")
    void testGetBooksLessThanConstant() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[?(@.price < 10)]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books that are not 'expensive': $..book[?(@.price <= $['expensive'])]")
    void testGetBooksLessEqualsThanDocumentValue() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[?(@.price <= $['expensive'])]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books that are not 'expensive' (variable): $..book[?(@.price <= {expensive})]")
    void testGetBooksLessEqualsThanVariableValue() throws IOException {
        Variables variables = new Variables();
        variables.setVariable("expensive", 10);

        JsonObject object = loadBooks();
        String expr = "$..book[?(@.price <= {expensive})]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, variables).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get books by author using Regexp: $..book[?(@.author =~ /.*REES/i)]")
    void testBookByAuthorRegexp() throws IOException {
        JsonObject object = loadBooks();
        String expr = "$..book[?(@.author =~ /.*REES/i)]";
        FilterStream filterStream = Compiler.newPathCompiler(expr, new Variables()).compile();
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(1, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
    }

    private JsonObject loadBooks() throws IOException {
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        Parser parser = factory.newParser();
        try (InputStream inputStream = getClass().getResourceAsStream("/books.json")) {
            return parser.parse(InputData.of(inputStream));
        }
    }
}
