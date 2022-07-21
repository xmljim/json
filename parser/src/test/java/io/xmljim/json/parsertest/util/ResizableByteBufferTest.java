package io.xmljim.json.parsertest.util;

import io.github.xmljim.json.parser.util.ByteConstants;
import io.github.xmljim.json.parser.util.ResizableByteBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResizableByteBufferTest {

    @Test
    void testResizableBuffer() {
        String testString = "This is a test";
        byte[] testBytes = testString.getBytes();
        ResizableByteBuffer rbf = new ResizableByteBuffer(testBytes);
        assertEquals(testBytes.length, rbf.size());

        byte first = rbf.first();
        assertEquals(ByteConstants.T, first, "Should be 'T'");
        assertEquals(testBytes.length, rbf.size(), "Size and length should match");

        assertEquals(0, rbf.getPosition(), "Position should be 0");
        assertEquals(testBytes.length, rbf.getRemaining(), "Remaining value should be equal to length");

        byte getFirst = rbf.get();

        assertEquals(ByteConstants.T, getFirst, "Firrst byte retrieved should be 'T'");
        assertEquals(1, rbf.getPosition(), "Cursor should have incremented");
        assertEquals(testBytes.length - 1, rbf.getRemaining(), "Get remaining should be decremented");

        assertEquals(ByteConstants.h, rbf.peek(), "Peek should return 'h'");
        rbf.get();
        assertEquals(ByteConstants.T, rbf.previous(), "Should return 'T'");


    }

}