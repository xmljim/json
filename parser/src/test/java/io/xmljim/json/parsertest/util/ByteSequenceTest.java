package io.xmljim.json.parsertest.util;

import io.github.xmljim.json.parser.util.ByteConstants;
import io.github.xmljim.json.parser.util.ByteSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ByteSequenceTest {

    @Test
    void emptySequence() {
        ByteSequence sequence = ByteSequence.emptySequence();
        assertEquals("", sequence.toString());
    }

    @Test
    void fromString() {
        ByteSequence sequence = ByteSequence.fromString("T");
        assertTrue(sequence.matches(ByteConstants.T));

        ByteSequence sequence2 = ByteSequence.fromString("Testing");
        assertTrue(sequence2.matches("Testing".getBytes()));

        assertTrue(sequence2.matches("Test".getBytes()));
    }

    @Test
    void startsWith() {
        ByteSequence sequence = ByteSequence.startsWith(ByteConstants.GREATERTHAN);
        assertEquals("[>]", sequence.toString());
    }

    @Test
    void withStartingSequence() {

    }

    @Test
    void testStartsWith() {
    }

    @Test
    void startsWithRange() {
    }

    @Test
    void startsWithRangeFrom() {
    }

    @Test
    void startsWithAnyOf() {
    }

    @Test
    void followedBy() {
    }

    @Test
    void followedByAnyOf() {
    }

    @Test
    void followedByRangeFrom() {
    }

    @Test
    void followedBySequenceOf() {
    }

    @Test
    void followedByRange() {
    }

    @Test
    void matches() {
    }

    @Test
    void testMatches() {
    }
}