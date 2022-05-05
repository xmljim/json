package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.variables.Global;
import io.xmljim.json.jsonpath.variables.Variables;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompiledPredicateTests {

    @Test
    void testEqualPredicateTrue() {
        Variables variables = new Variables();
        variables.setVariable("pi", 3.14);

        String expression = "3.14 == {pi}";
        Predicate<Context> predicate = compile(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleOrJoin() {
        Variables variables = new Variables();
        variables.setVariable("firstName", "Jim");
        variables.setVariable("lastName", "Earley");

        String expression = "'James' == {firstName} || 'Earley' == {lastName}";
        Predicate<Context> predicate = compile(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleAndJoin() {
        Variables variables = new Variables();
        variables.setVariable("firstName", "Jim");
        variables.setVariable("lastName", "Earley");

        String expression = "'Jim' == {firstName} && 'Earley' == {lastName}";
        Predicate<Context> predicate = compile(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testSimpleAndJoinFalse() {
        Variables variables = new Variables();
        variables.setVariable("firstName", "Jim");
        variables.setVariable("lastName", "Earley");

        String expression = "'John' == {firstName} && 'Earley' == {lastName}";
        Predicate<Context> predicate = compile(expression, variables);
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
        Predicate<Context> predicate = compile(expression, variables);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    public void isNotNullPredicate() {
        String expression = "@.foo.bar";
        Predicate<Context> predicate = compile(expression);
        assertNotNull(predicate);
        assertEquals(PredicateOperator.IS_NOT_NULL, ((FilterPredicate) predicate).operator());
    }

    private Predicate<Context> compile(String expression) {
        return compile(expression, new Variables());
    }

    private Predicate<Context> compile(String expression, Global global) {
        return Compiler.newPredicateCompiler(expression, global).compile();
    }
}
