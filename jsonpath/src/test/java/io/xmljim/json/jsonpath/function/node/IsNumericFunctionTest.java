package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsNumericFunctionTest {

    @Test
    void testIsNumeric() {
        List<Context> testContext = new ArrayList<>();

        testContext.add(Context.createSimpleContext(42));
        testContext.add(Context.createSimpleContext(3.14));
        testContext.add(Context.createSimpleContext("A String"));
        testContext.add(Context.defaultContext());

        IsNumericFunction fx = new IsNumericFunction();
        List<Context> results = fx.apply(testContext.stream()).toList();

        assertStreamEquals(testContext.stream(), results.stream());
    }

    static void assertStreamEquals(Stream<Context> s1, Stream<Context> s2) {
        Iterator<Context> iter1 = s1.iterator();
        Iterator<Context> iter2 = s2.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            assertEquals(iter1.next().type().isNumeric(), iter2.next().get().asJsonValue().get());
        }

        assert !iter1.hasNext() && !iter2.hasNext();
    }
}