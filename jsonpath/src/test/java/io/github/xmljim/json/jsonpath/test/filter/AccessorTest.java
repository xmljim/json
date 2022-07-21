package io.github.xmljim.json.jsonpath.test.filter;

import io.github.xmljim.json.jsonpath.context.Accessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessorTest {

    @Test
    void testRoot() {
        Accessor accessor = Accessor.root();
        assertNotNull(accessor);
        assertEquals("$", accessor.get());
    }

    @Test
    void testOfString() {
        Accessor accessor = Accessor.of("foo");
        assertNotNull(accessor);
        assertTrue(accessor.isString());
        assertEquals("foo", accessor.get());
    }

    @Test
    void testOfInteger() {
        Accessor accessor = Accessor.of(1);
        assertNotNull(accessor);
        assertFalse(accessor.isString());
        assertEquals((Integer) 1, accessor.get());
    }

    @Test
    void testParseString() {
        Accessor accessor = Accessor.parse("foo");
        assertNotNull(accessor);
        assertTrue(accessor.isString());
        assertEquals("foo", accessor.get());

        accessor = Accessor.parse("['foo']");
        assertNotNull(accessor);
        assertTrue(accessor.isString());
        assertEquals("foo", accessor.get());
    }

    @Test
    void testParseInteger() {
        Accessor accessor = Accessor.parse("[1]");
        assertNotNull(accessor);
        assertFalse(accessor.isString());
        assertEquals((Integer) 1, accessor.get());

        accessor = Accessor.parse("[-1]");
        assertNotNull(accessor);
        assertFalse(accessor.isString());
        assertEquals((Integer) (-1), accessor.get());
    }
}