package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.JsonPathTestBase;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.variables.Variables;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompiledPredicateTests extends JsonPathTestBase {

    @Test
    void testEqualPredicateTrue() {
        Variables variables = new Variables();
        variables.setVariable("pi", 3.14);

        String expression = "3.14 == {pi}";
        Predicate<Context> predicate = getPredicateContext(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleOrJoin() {
        Variables variables = new Variables();
        variables.setVariable("firstName", "Jim");
        variables.setVariable("lastName", "Earley");

        String expression = "'James' == {firstName} || 'Earley' == {lastName}";
        Predicate<Context> predicate = getPredicateContext(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleAndJoin() {
        Variables variables = new Variables();
        variables.setVariable("firstName", "Jim");
        variables.setVariable("lastName", "Earley");

        String expression = "'Jim' == {firstName} && 'Earley' == {lastName}";
        Predicate<Context> predicate = getPredicateContext(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleAndJoinFalse() {
        Variables variables = new Variables();
        variables.setVariable("firstName", "Jim");
        variables.setVariable("lastName", "Earley");

        String expression = "'John' == {firstName} && 'Earley' == {lastName}";
        Predicate<Context> predicate = getPredicateContext(expression, variables);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testComplexJoinAnd() {
        Variables variables = new Variables();
        variables.setVariable("firstName", "Jim");
        variables.setVariable("lastName", "Earley");
        variables.setVariable("gender", "M");
        variables.setVariable("age", 42);

        String expression = "'Jim' == {firstName} && 'Earley' == {lastName} && ('F' == {gender} || {age} < 50)";
        Predicate<Context> predicate = getPredicateContext(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    public void isNotNullPredicate() {
        String expression = "@.foo.bar";
        Predicate<Context> predicate = getPredicateContext(expression);
        assertNotNull(predicate);
        assertEquals(PredicateOperator.IS_NOT_NULL, ((FilterPredicate) predicate).operator());
    }
}
