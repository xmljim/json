package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.Variables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("OperatorFactory Tests")
class OperatorFactoryTest {

    @Test
    @DisplayName("When filter equals '$' return RootPathOperator")
    void testRootFilter() {
        Filter operator = createDefaultFilter("$");
        assertNotNull(operator);
        assertEquals(FilterType.ROOT, operator.getOperatorType());
    }

    @Test
    @DisplayName("When filter equals '@' return CurrentPathOperator")
    void testCurrentFilter() {
        Filter operator = createDefaultFilter("@");
        assertNotNull(operator);
        assertEquals(FilterType.CURRENT, operator.getOperatorType());
    }

    @Test
    @DisplayName("When filter is either a numeric index or string key, then return a ChildPathOperator")
    void testChildFilter() {
        Filter operator = createDefaultFilter("[1]");
        assertNotNull(operator);
        assertEquals(FilterType.CHILD, operator.getOperatorType());

        operator = createDefaultFilter("[-1]");
        assertNotNull(operator);
        assertEquals(FilterType.CHILD, operator.getOperatorType());

        operator = createDefaultFilter("_key");
        assertNotNull(operator);
        assertEquals(FilterType.CHILD, operator.getOperatorType());
    }

    @Test
    @DisplayName("When filter is a wildcard then return a WildcardOperator")
    void testWildcardFilter() {
        Filter operator = createDefaultFilter("*");
        assertNotNull(operator);
        assertEquals(FilterType.WILDCARD, operator.getOperatorType());
    }

    @Test
    @DisplayName("When filter is a recursion filter then return RecursionOperator")
    void testRecursionFilter() {
        Filter operator = createDefaultFilter("..");
        assertNotNull(operator);
        assertEquals(FilterType.RECURSIVE, operator.getOperatorType());
    }

    @Test
    @DisplayName("When filter includes predicate then return a PredicateFilter")
    void testPredicateFilter() {

        Filter filter = createDefaultFilter("[?(@.foo == $.bar)]");
        assertNotNull(filter);
        assertEquals(FilterType.PREDICATE, filter.getOperatorType());
    }

    @Test
    @DisplayName("When filter includes comma-separated indices, create a UnionFilter")
    void testUnionFilter() {
        Filter filter = createDefaultFilter("[1,2, 3]");
        assertNotNull(filter);
        assertEquals(FilterType.UNION, filter.getOperatorType());
    }

    @Test
    void testSliceFilter() {
        Filter filter = createDefaultFilter("[1:-1]");
        assertNotNull(filter);
        assertEquals(FilterType.SLICE, filter.getOperatorType());
        
    }

    private Filter createDefaultFilter(String expression) {
        return FilterFactory.newOperator(expression, new Variables());
    }
}