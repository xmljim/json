package io.xmljim.json.jsonpathtest.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.FilterPredicate;
import io.xmljim.json.jsonpath.predicate.PredicateOperator;
import io.xmljim.json.jsonpath.util.Settings;
import io.xmljim.json.jsonpathtest.JsonPathTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompiledPredicateTests extends JsonPathTestBase {

    @Test
    void testEqualPredicateTrue() {
        Settings settings = new Settings();
        settings.setVariable("pi", 3.14);

        String expression = "3.14 == {pi}";
        Predicate<Context> predicate = getPredicateContext(expression, settings);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleOrJoin() {
        Settings settings = new Settings();
        settings.setVariable("firstName", "Jim");
        settings.setVariable("lastName", "Earley");

        String expression = "'James' == {firstName} || 'Earley' == {lastName}";
        Predicate<Context> predicate = getPredicateContext(expression, settings);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleAndJoin() {
        Settings settings = new Settings();
        settings.setVariable("firstName", "Jim");
        settings.setVariable("lastName", "Earley");

        String expression = "'Jim' == {firstName} && 'Earley' == {lastName}";
        Predicate<Context> predicate = getPredicateContext(expression, settings);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleAndJoinFalse() {
        Settings settings = new Settings();
        settings.setVariable("firstName", "Jim");
        settings.setVariable("lastName", "Earley");

        String expression = "'John' == {firstName} && 'Earley' == {lastName}";
        Predicate<Context> predicate = getPredicateContext(expression, settings);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testComplexJoinAnd() {
        Settings settings = new Settings();
        settings.setVariable("firstName", "Jim");
        settings.setVariable("lastName", "Earley");
        settings.setVariable("gender", "M");
        settings.setVariable("age", 42);

        String expression = "'Jim' == {firstName} && 'Earley' == {lastName} && ('F' == {gender} || {age} < 50)";
        Predicate<Context> predicate = getPredicateContext(expression, settings);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    public void isNotNullPredicate() {
        String expression = "@.foo.bar";
        Predicate<Context> predicate = getPredicateContext(expression);
        assertNotNull(predicate);
        Assertions.assertEquals(PredicateOperator.IS_NOT_NULL, ((FilterPredicate) predicate).operator());
    }
}
