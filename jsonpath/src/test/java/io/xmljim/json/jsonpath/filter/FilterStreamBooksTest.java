package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.JsonPathTestBase;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.variables.Variables;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FilterStream Tests - books.json")
public class FilterStreamBooksTest extends JsonPathTestBase {

    @Test
    @DisplayName("Get all book authors (dot notation): $.store.book.*.author")
    void testGetAllBookAuthorsDotNotation() throws Exception {
        String expr = "$.store.book.*.author";
        List<Context> results = getResults(expr);
        assertEquals(4, results.size());
        assertEquals("Nigel Rees", results.get(0).get().asJsonValue().get());
        assertEquals("Evelyn Waugh", results.get(1).get().asJsonValue().get());
        assertEquals("Herman Melville", results.get(2).get().asJsonValue().get());
        assertEquals("J. R. R. Tolkien", results.get(3).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get all book authors (bracket notation): $['store']['book'][*]['author']")
    void testGetAllBookAuthorsBracketNotation() throws Exception {
        String expr = "$['store']['book'][*]['author']";
        List<Context> results = getResults(expr);
        assertEquals(4, results.size());
        assertEquals("Nigel Rees", results.get(0).get().asJsonValue().get());
        assertEquals("Evelyn Waugh", results.get(1).get().asJsonValue().get());
        assertEquals("Herman Melville", results.get(2).get().asJsonValue().get());
        assertEquals("J. R. R. Tolkien", results.get(3).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get all book authors (recursive): $['store'][..]['author']")
    void testGetAllBookAuthorsRecursive() throws Exception {
        String expr = "$['store'][..]['author']";
        List<Context> results = getResults(expr);
        assertEquals(4, results.size());
        assertEquals("Nigel Rees", results.get(0).get().asJsonValue().get());
        assertEquals("Evelyn Waugh", results.get(1).get().asJsonValue().get());
        assertEquals("Herman Melville", results.get(2).get().asJsonValue().get());
        assertEquals("J. R. R. Tolkien", results.get(3).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get the price of all items in the store: $.store..price")
    void testGetThePriceOfAllStoreItems() throws Exception {
        String expr = "$.store..price";
        List<Context> results = getResults(expr);
        assertEquals(5, results.size());
        assertEquals(8.95, results.get(0).get().asJsonValue().get());
        assertEquals(12.99, results.get(1).get().asJsonValue().get());
        assertEquals(8.99, results.get(2).get().asJsonValue().get());
        assertEquals(22.99, results.get(3).get().asJsonValue().get());
        assertEquals(19.95, results.get(4).get().asJsonValue().get());
    }

    @Test
    @DisplayName("Get a book at a specific index: $..book[2]")
    void testGetBookAtIndex() throws Exception {
        String expr = "$..book[2]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(1, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
    }


    @Test
    @DisplayName("Get the second to last book: $..book[-2]")
    void testGetBookAtIndexFromTail() throws Exception {
        String expr = "$..book[-2]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(1, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the first two books (union): $..book[0,1]")
    void testGetBooksFirstTwoBooksUnion() throws Exception {
        String expr = "$..book[0,1]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Sword of Honour", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the first two books (slice): $..book[:2]")
    void testGetBooksFirstTwoBooksSlice() throws Exception {
        String expr = "$..book[:2]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Sword of Honour", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the middle two books (slice): $..book[1:3]")
    void testGetBooksMiddleTwoBooksSlice() throws Exception {
        String expr = "$..book[1:3]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sword of Honour", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get the last two books (slice): $..book[2:]")
    void testGetBookAtIndexFromTailSlice() throws Exception {
        String expr = "$..book[2:]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
        assertEquals("The Lord of the Rings", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books with an ISBN number: $..book[?(@.isbn)]")
    void testGetBooksWithIsNotNullPredicate() throws Exception {
        String expr = "$..book[?(@.isbn)]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Moby Dick", results.get(0).get().asJsonObject().get("title"));
        assertEquals("The Lord of the Rings", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books without an ISBN number: $..book[?(!@.isbn)]")
    void testGetBooksWithIsNotNullPredicateNegated() throws Exception {
        String expr = "$..book[?(!@.isbn)]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Sword of Honour", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books cheaper than 10: $..book[?(@.price < 10)]")
    void testGetBooksLessThanConstant() throws Exception {
        String expr = "$..book[?(@.price < 10)]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books that are not 'expensive': $..book[?(@.price <= $['expensive'])]")
    void testGetBooksLessEqualsThanDocumentValue() throws Exception {
        String expr = "$..book[?(@.price <= $['expensive'])]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get all books that are not 'expensive' (variable): $..book[?(@.price <= {expensive})]")
    void testGetBooksLessEqualsThanVariableValue() throws Exception {
        Variables variables = new Variables();
        variables.setVariable("expensive", 10);

        JsonObject object = loadData("/books.json").asJsonObject();
        String expr = "$..book[?(@.price <= {expensive})]";
        FilterStream filterStream = getFilterStream(expr, variables);
        List<Context> results = filterStream.filter(object).toList();
        System.out.println(results);
        assertEquals(2, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
        assertEquals("Moby Dick", results.get(1).get().asJsonObject().get("title"));
    }

    @Test
    @DisplayName("Get authors data type: $..book.*.author.type()")
    void testGetAuthorsDataType() throws Exception {

        String expr = "$..book.*.author.type()";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(4, results.size());
        results.forEach(context -> {
            assertEquals("string", context.value());
        });
    }

    @Test
    @DisplayName("Get the sum of all book prices: $..book.*.price.sum()")
    void testGetSumOfAllBookPrices() throws Exception {
        JsonObject object = loadData("/books.json").asJsonObject();
        JsonObject store = object.get("store");
        JsonArray book = store.get("book");
        double testValue = book.jsonValues().map(JsonElement::asJsonObject).mapToDouble(bk -> bk.get("price")).sum();

        String expr = "$..book.*.price.sum()";
        List<Context> results = getResults(expr);
        assertEquals(testValue, results.get(0).value());
        System.out.println(results);
    }

    @Test
    @DisplayName("Get books by author using Regexp: $..book[?(@.author =~ /.*REES/i)]")
    void testBookByAuthorRegexp() throws Exception {

        String expr = "$..book[?(@.author =~ /.*REES/i)]";
        List<Context> results = getResults(expr);
        System.out.println(results);
        assertEquals(1, results.size());
        assertEquals("Sayings of the Century", results.get(0).get().asJsonObject().get("title"));
    }

    private List<Context> getResults(String expr) throws Exception {
        JsonObject object = loadData("/books.json").asJsonObject();
        FilterStream filterStream = getFilterStream(expr);
        return filterStream.filter(object).toList();
    }

}
