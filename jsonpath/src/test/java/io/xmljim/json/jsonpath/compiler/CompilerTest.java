package io.xmljim.json.jsonpath.compiler;

import io.xmljim.json.jsonpath.JsonPathTestBase;
import io.xmljim.json.jsonpath.filter.Filter;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.filter.FilterType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Compiler Tests")
class CompilerTest extends JsonPathTestBase {

    @Test
    @DisplayName("Given an expression with a root select and bracketed property key, then return a sequence" +
        "containing a RootPathOperator and a ChildPathOperator")
    void testChildPathWithBrackets() {
        String expression = "$['foo']";
        FilterStream sequence = getFilterStream(expression);
        assertNotNull(sequence);
        assertEquals(2, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.CHILD, sequence.pop().getOperatorType());
    }

    @Test
    @DisplayName("Given an expression with a root selector and 'dot' property key, then return a sequence" +
        "containing a RootPathOperator and a ChildPathOperator")
    void testChildPathWithDot() {
        String expression = "$.foo";
        FilterStream sequence = getFilterStream(expression);
        assertNotNull(sequence);
        assertEquals(2, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.CHILD, sequence.pop().getOperatorType());
    }

    @Test
    void testChildPathWithIndex() {
        String expression = "$[1]";
        FilterStream sequence = getFilterStream(expression);
        assertNotNull(sequence);
        assertEquals(2, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.CHILD, sequence.pop().getOperatorType());
    }

    @Test
    void testWildcardAsSelector() {
        String expression = "*";
        FilterStream sequence = getFilterStream(expression);
        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals(FilterType.WILDCARD, sequence.pop().getOperatorType());
    }

    @Test
    void testRecursionFilter() {
        String expression = "$..foo";
        FilterStream sequence = getFilterStream(expression);
        assertNotNull(sequence);
        assertEquals(3, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.RECURSIVE, sequence.pop().getOperatorType());
        assertEquals(FilterType.CHILD, sequence.pop().getOperatorType());
    }

    @Test
    void testPredicateFilter() {
        String expression = "$[?(@.foo == $.bar)]";
        FilterStream sequence = getFilterStream(expression);
        assertNotNull(sequence);
        assertEquals(2, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.PREDICATE, sequence.pop().getOperatorType());
    }

    @Test
    void testUnionFilter() {
        String expression = "$[1,3, 5]"; //intentionally leaving a space
        FilterStream stream = getFilterStream(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.UNION, stream.pop().getOperatorType());
    }

    @Test
    void testSliceFilter() {
        String expression = "$[1:-1]"; //intentionally leaving a space
        FilterStream stream = getFilterStream(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());

        expression = "$[0:2]";
        stream = getFilterStream(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());

        expression = "$[:-1]";
        stream = getFilterStream(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());

        expression = "$[-1:]";
        stream = getFilterStream(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());
    }

    @Test
    void testFunctionFilter() {
        String expression = "$.type()";
        FilterStream stream = getFilterStream(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.FUNCTION, stream.pop().getOperatorType());
    }

    @Test
    void testWithPredicateFunction() {
        String expression = "$.*[?(count-if(@.assignments*, @.doc_id == 19 && @.doc_status == 'PROCESSED') >= 2)]";
        FilterStream stream = getFilterStream(expression);
        assertEquals(3, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.WILDCARD, stream.pop().getOperatorType());
        Filter lastFilter = stream.pop();
        assertEquals(FilterType.PREDICATE, lastFilter.getOperatorType());

        assertNotNull(stream);
    }


}