package io.xmljim.json.jsonpathtest.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.node.IsPrimitiveFunction;
import io.xmljim.json.jsonpath.util.Settings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsPrimitiveFunctionTest {

    @Test
    void testIsPrimitiveFunction() {
        List<Context> testContext = new ArrayList<>();

        testContext.add(Context.createSimpleContext(42));
        testContext.add(Context.createSimpleContext(3.14));
        testContext.add(Context.createSimpleContext("A String"));
        testContext.add(Context.defaultContext());
        testContext.add(Context.createSimpleContext(true));

        IsPrimitiveFunction fx = new IsPrimitiveFunction(new Settings());
        List<Context> results = fx.apply(testContext.stream()).toList();
        assertStreamEquals(testContext.stream(), results.stream());
    }


    static void assertStreamEquals(Stream<Context> s1, Stream<Context> s2) {
        Iterator<Context> iter1 = s1.iterator();
        Iterator<Context> iter2 = s2.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            assertEquals(iter1.next().type().isPrimitive(), iter2.next().get().asJsonValue().get());
        }

        assert !iter1.hasNext() && !iter2.hasNext();
    }
}