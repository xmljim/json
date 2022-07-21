package io.xmljim.json.parsertest.util;

import io.github.xmljim.json.parser.util.ByteConstants;
import io.github.xmljim.json.parser.util.ByteRange;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ByteRangeTest {
    @Test
    void empty() {
        ByteRange byteRange = ByteRange.empty();
        assertEquals("", byteRange.toString());
    }

    @Test
    void fromString() {
        ByteRange byteRange = ByteRange.fromString("TEST");
        assertTrue(byteRange.contains(ByteConstants.T));
        assertTrue(byteRange.contains(ByteConstants.E));
        assertTrue(byteRange.contains(ByteConstants.S));
        assertFalse(byteRange.contains(ByteConstants.t));
        System.out.println(byteRange);
    }

    @Test
    void startWithSingleByte() {
        ByteRange byteRange = ByteRange.startWith(ByteConstants.A);
        assertEquals("A", byteRange.toString());
    }

    @Test
    void startWithByteArray() {
        ByteRange byteRange = ByteRange.startWith("JSON".getBytes());
        assertEquals(0, byteRange.getMatchingByteIndexes(new byte[]{ByteConstants.J})[0]);
        assertTrue(byteRange.contains(ByteConstants.S, ByteConstants.O, ByteConstants.N));
    }

    @Test
    void testStartWithRangeStartEnd() {
        ByteRange byteRange = ByteRange.startWith(ByteConstants.a, ByteConstants.z);
        assertTrue(byteRange.contains(ByteConstants.n));
        assertEquals(1, byteRange.getMatchingByteIndexes(new byte[]{ByteConstants.B, ByteConstants.b})[0]);
    }

    @Test
    void andAddFrom() {
        ByteRange byteRange = ByteRange.startWith(ByteConstants.a, ByteConstants.z)
            .andAddFrom(ByteConstants.A, ByteConstants.Z);
        assertTrue(byteRange.contains(ByteConstants.a, ByteConstants.A, ByteConstants.D, ByteConstants.g));
    }

    @Test
    void andAdd() {
        ByteRange byteRange = ByteRange.startWith(ByteConstants.ZERO, ByteConstants.NINE).andAdd(ByteConstants.A);
        assertTrue(byteRange.contains(ByteConstants.A));
    }

    @Test
    void testAndAddByteArray() {
        ByteRange byteRange = ByteRange.fromString("ABC").andAdd("abc".getBytes());
        assertEquals("ABCabc", byteRange.toString());
    }

    @Test
    void testContainsVarArgsBytes() {
        ByteRange byteRange = ByteRange.fromString("TEST");
        assertTrue(byteRange.contains(ByteConstants.T, ByteConstants.S));
    }

    @Test
    void containsAll() {
        ByteRange byteRange = ByteRange.fromString("STRAIGHT");
        assertTrue(byteRange.containsAll("STAR".getBytes()));
    }

    @Test
    void containsSome() {
        ByteRange byteRange = ByteRange.fromString("baseball");
        assertTrue(byteRange.containsSome("bat".getBytes()));
    }

    @Test
    void containsCount() {
        ByteRange byteRange = ByteRange.fromString("baseball");
        assertEquals(2, byteRange.containsCount("bat".getBytes()));
    }

    @Test
    void getMatchingByteIndexes() {
        ByteRange byteRange = ByteRange.fromString("baseball");
        int[] values = byteRange.getMatchingByteIndexes("bat".getBytes());
        List<Integer> actual = new ArrayList<>();
        Arrays.stream(values).forEach(actual::add);
        assertEquals(List.of(0, 1), actual);
    }

    @Test
    void getMatchingByteValues() {
        ByteRange byteRange = ByteRange.fromString("baseball");
        byte[] matching = byteRange.getMatchingByteValues("bat".getBytes());

        List<Byte> actual = new ArrayList<>();
        for (byte b : matching) {
            actual.add(b);
        }

        assertEquals(List.of(ByteConstants.b, ByteConstants.a), actual);
    }

    @Test
    void testToString() {
        ByteRange byteRange = ByteRange.fromString("STAR");
        //remember that the bytes are sorted internally
        assertEquals("ARST", byteRange.toString());
    }
}
