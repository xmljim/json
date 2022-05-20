package io.github.xmljim.json.jsonpath.test.function.node;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.node.NodeTypeFunction;
import io.github.xmljim.json.jsonpath.util.Settings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTypeFunctionTest {

    @Test
    void testNodeTypeFunction() {
        List<Context> testContext = new ArrayList<>();

        testContext.add(Context.createSimpleContext(42));
        testContext.add(Context.createSimpleContext(3.14));
        testContext.add(Context.createSimpleContext("A String"));
        testContext.add(Context.defaultContext());
        testContext.add(Context.createSimpleContext(true));

        NodeTypeFunction fx = new NodeTypeFunction(new Settings());
        List<Context> results = fx.apply(testContext.stream()).toList();
        assertStreamEquals(testContext.stream(), results.stream());
    }


    static void assertStreamEquals(Stream<Context> s1, Stream<Context> s2) {
        Iterator<Context> iter1 = s1.iterator();
        Iterator<Context> iter2 = s2.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            assertEquals(iter1.next().type().name().toLowerCase(Locale.ROOT), iter2.next().get().asJsonValue().get());
        }

        assert !iter1.hasNext() && !iter2.hasNext();
    }

}