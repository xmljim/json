package io.xmljim.json.jsonpath.compiler;

import io.xmljim.json.jsonpath.variables.Variables;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.filter.FilterType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Compiler Tests")
class CompilerTest {

    @Test
    @DisplayName("Given an expression with a root select and bracketed property key, then return a sequence" +
        "containing a RootPathOperator and a ChildPathOperator")
    void testChildPathWithBrackets() {
        String expression = "$['foo']";
        FilterStream sequence = compileExpression(expression);
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
        FilterStream sequence = compileExpression(expression);
        assertNotNull(sequence);
        assertEquals(2, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.CHILD, sequence.pop().getOperatorType());
    }

    @Test
    void testChildPathWithIndex() {
        String expression = "$[1]";
        FilterStream sequence = compileExpression(expression);
        assertNotNull(sequence);
        assertEquals(2, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.CHILD, sequence.pop().getOperatorType());
    }

    @Test
    void testWildcardAsSelector() {
        String expression = "*";
        FilterStream sequence = compileExpression(expression);
        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals(FilterType.WILDCARD, sequence.pop().getOperatorType());
    }

    @Test
    void testRecursionFilter() {
        String expression = "$..foo";
        FilterStream sequence = compileExpression(expression);
        assertNotNull(sequence);
        assertEquals(3, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.RECURSIVE, sequence.pop().getOperatorType());
        assertEquals(FilterType.CHILD, sequence.pop().getOperatorType());
    }

    @Test
    void testPredicateFilter() {
        String expression = "$[?(@.foo == $.bar)]";
        FilterStream sequence = compileExpression(expression);
        assertNotNull(sequence);
        assertEquals(2, sequence.size());
        assertEquals(FilterType.ROOT, sequence.pop().getOperatorType());
        assertEquals(FilterType.PREDICATE, sequence.pop().getOperatorType());
    }

    @Test
    void testUnionFilter() {
        String expression = "$[1,3, 5]"; //intentionally leaving a space
        FilterStream stream = compileExpression(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.UNION, stream.pop().getOperatorType());
    }

    @Test
    void testSliceFilter() {
        String expression = "$[1:-1]"; //intentionally leaving a space
        FilterStream stream = compileExpression(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());

        expression = "$[0:2]";
        stream = compileExpression(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());

        expression = "$[:-1]";
        stream = compileExpression(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());

        expression = "$[-1:]";
        stream = compileExpression(expression);
        assertNotNull(stream);
        assertEquals(2, stream.size());
        assertEquals(FilterType.ROOT, stream.pop().getOperatorType());
        assertEquals(FilterType.SLICE, stream.pop().getOperatorType());
    }

    private FilterStream compileExpression(String expression) {
        Compiler<FilterStream> compiler = Compiler.newPathCompiler(expression, new Variables());
        return compiler.compile();
    }


}