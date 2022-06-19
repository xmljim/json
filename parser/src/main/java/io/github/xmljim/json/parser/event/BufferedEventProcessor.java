package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.JsonEventParserException;
import io.github.xmljim.json.factory.parser.ParserSettings;
import io.github.xmljim.json.factory.parser.Statistics;
import io.github.xmljim.json.factory.parser.event.EventType;
import io.github.xmljim.json.parser.util.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static io.github.xmljim.json.parser.event.ActionConstants.*;

class BufferedEventProcessor extends BaseEventProcessor {

    /**
     * Number byte range (0-9, e, E, +, -)
     */
    private final ByteRange numberRange = ByteRange.startWith(ByteConstants.ZERO, ByteConstants.NINE).andAdd(ByteConstants.e).andAdd(ByteConstants.E).andAdd(ByteConstants.PLUS).andAdd(ByteConstants.MINUS);
    /**
     * Whitespace byte values: {space},(\t),(\r),(\n)
     */
    private final ByteRange whitespaceRange = ByteRange.startWith(ByteConstants.TAB).andAdd(ByteConstants.SPACE).andAdd(ByteConstants.CR).andAdd(ByteConstants.LF);

    /**
     * Boolean character byte values. Both share an 'e' byte.
     */
    private final ByteRange booleanRange = ByteRange.fromString("trufalse"); //intentional -only care about unique chars
    private final ByteRange delimiterRange = ByteRange.startWith(ByteConstants.COMMA).andAdd(ByteConstants.COLON);
    private final ByteRange containerRange = ByteRange.startWith(new byte[]{ByteConstants.START_ARRAY, ByteConstants.START_MAP, ByteConstants.END_ARRAY, ByteConstants.END_MAP});
    private final ByteRange escapeRange = ByteRange.startWith(new byte[]{ByteConstants.QUOTE, ByteConstants.BACKSLASH, ByteConstants.SOLIDUS, ByteConstants.b, ByteConstants.f, ByteConstants.n, ByteConstants.r, ByteConstants.t});
    private final ByteRange nullRange = ByteRange.fromString("nul");

    private final ByteSequence nullSequence = ByteSequence.fromString("null");
    private final ByteSequence trueSequence = ByteSequence.fromString("true");
    private final ByteSequence falseSequence = ByteSequence.fromString("false");

    private BufferedInputStream bis;
    private final ResizableByteBuffer token = new ResizableByteBuffer();

    private int tokenState;
    private int lineNumber;
    private int column;
    private boolean documentStarted = false;
    private boolean escapeFlag = false;
    private long byteCount;

    private final Deque<Byte> expectedDelimiters = new ArrayDeque<>();
    private final Deque<MarkerInfo> containerStack = new ArrayDeque<>();

    private MarkerInfo lastMarker;

    private final Timer<BufferedEventProcessor> processorTimer = new Timer<>();

    public BufferedEventProcessor(ParserSettings settings) {
        super(settings);
    }

    @Override
    public void process(InputStream inputStream) {
        processorTimer.start(this);
        incrementLine();
        incrementColumn();
        try {
            if (inputStream instanceof BufferedInputStream) {
                bis = (BufferedInputStream) inputStream;
            } else {
                bis = new BufferedInputStream(inputStream);
            }

            ByteBuffer block = readBlock();

            while (block != null) {
                processBlock(block);
                block = readBlock();
            }

            //fireDocumentEndEvent(lineNumber, column);
            //inputStream.close();
        } catch (final IOException e) {
            throw new JsonEventParserException(e);
        }

        checkDocumentEnd();
        fireDocumentEndEvent(lineNumber, column);
        processorTimer.stop(this);

    }

    @Override
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();

        if (getSettings().enableStatistics()) {
            double sendTime = (double) getSendTime() / 1_000_000_000;
            double processorTime = processorTimer.get(TimeUnit.SECONDS);
            double rawProcessorTime = processorTime - sendTime;
            double bytesPerSecond = (byteCount / rawProcessorTime);//processorTimer.get(TimeUnit.SECONDS));
            statistics.setParsingTime(rawProcessorTime);
            statistics.setEventSendTime(sendTime);
            statistics.setProcessingTime(processorTime);
            statistics.setBytesProcessed(byteCount);
            statistics.setBytesPerSecond(bytesPerSecond);
        }

        return statistics;
    }

    /**
     * Change state for start of a string token and fire event
     */
    private void notifyStringTokenStart() {
        setTokenState(TOKEN_STATE_STRING);
        fireStringStartEvent(lineNumber, column);
    }

    /**
     * Change state for end of string token and fire event
     *
     * @param tokenValue The token data to pass to event
     */
    private void notifyStringTokenEnd(ByteBuffer tokenValue) {
        resetToken();
        fireStringEndEvent(tokenValue, lineNumber, column);
    }

    /**
     * Change state for start of boolean token and fire event
     */
    private void notifyBooleanTokenStart() {
        setTokenState(TOKEN_STATE_BOOLEAN);
        fireBooleanStartEvent(lineNumber, column);
    }

    /**
     * Change state for end of boolean token and fire event
     *
     * @param tokenValue the boolean token value to pass to event
     */
    private void notifyBooleanTokenEnd(ByteBuffer tokenValue) {
        validateBoolean(tokenValue);
        resetToken();
        fireBooleanEndEvent(tokenValue, lineNumber, column);
    }

    /**
     * Change state for number start token and fire event
     */
    private void notifyNumberTokenStart() {
        setTokenState(TOKEN_STATE_NUMBER);
        fireNumberStartEvent(lineNumber, column);
    }

    /**
     * Change state for end of number token and fire event
     *
     * @param tokenValue the number token value to pass to event
     */
    private void notifyNumberTokenEnd(ByteBuffer tokenValue) {
        resetToken();
        fireNumberEndEvent(tokenValue, lineNumber, column);
    }

    /**
     * Change state for start of null token and fire event;
     */
    private void notifyNullTokenStart() {
        setTokenState(TOKEN_STATE_NULL);
        fireNullStartEvent(lineNumber, column);
    }

    /**
     * Change state for end of null token and fire event
     *
     * @param tokenValue the null token value (should be equivalent of 'null' ByteSequence)
     */
    private void notifyNullTokenEnd(ByteBuffer tokenValue) {
        validateNull(tokenValue);
        resetToken();
        fireNullEndEvent(tokenValue, lineNumber, column);
    }

    /**
     * Change state for end of entity element (object or array) and fire event
     */
    private void notifyEntityEnd() {
        resetToken();
        fireEntityEndEvent(lineNumber, column);
    }

    /**
     * Change state for end of object key and fire event
     */
    private void notifyKeyEnd() {
        resetToken();
        fireKeyEndEvent(lineNumber, column);
    }

    /**
     * Get current line number
     *
     * @return the line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Get current column number
     *
     * @return the column number
     */
    public int getColumn() {
        return column;
    }

    /**
     * Process a block of data
     *
     * @param block the block of data
     */
    private void processBlock(ByteBuffer block) {
        if (block != null) {
            while (block.hasRemaining()) {
                final byte b = block.get();
                handleByte(b);
            }
        } else {
            checkDocumentEnd();
            fireDocumentEndEvent(lineNumber, column);
        }
    }

    /**
     * Process a byte. Evaluate current state and call specific handle method
     *
     * @param b the byte
     */
    private void handleByte(byte b) {
        byteCount++;

        if (isWhitespace(b)) {
            handleWhitespace(b);

        } else if (isNumeric(b)) {
            handleNumeric(b);

        } else if (isBoolean(b)) {
            handleBoolean(b);

        } else if (isNull(b)) {
            handleNull(b);

        } else if (isDelimiter(b)) {
            handleDelimiter(b);

        } else if (isContainer(b)) {
            handleContainer(b);

        } else {
            handleString(b);
        }


    }

    /**
     * Return whether current byte is a whitespace value
     *
     * @param b the current byte
     * @return {@code true} if the byte is a known whitespace value; false otherwise
     */
    private boolean isWhitespace(byte b) {
        return hasByte(b, whitespaceRange);
    }

    /**
     * Handle a whitespace byte
     *
     * @param b the whitespace byte
     */
    private void handleWhitespace(byte b) {
        switch (b) {
            case ByteConstants.SPACE -> {
                if (inStringToken()) {
                    appendToken(b);
                } else if (inBooleanToken() || inNumberToken() || inNullToken()) {
                    notifyEndToken();
                }
                incrementColumn();
            }
            case ByteConstants.LF -> {
                if (inToken() && !inStringToken()) {
                    notifyEndToken();
                }
                incrementLine();
            }
            case ByteConstants.TAB | ByteConstants.CR -> incrementColumn();

            //no-op, extraneous
            //CR will be followed by LF, so line increment will be dealt with there.
        }
    }

    /**
     * Evaluates whether the current byte is a numeric value
     *
     * @param byt the current byte
     * @return {@code true} if the current byte is a known numeric value; false otherwise
     */
    private boolean isNumeric(byte byt) {
        return hasByte(byt, numberRange) && (!inToken() || inNumberToken());
    }

    /**
     * Handle the current byte as a numeric character value
     *
     * @param byt the current byte
     */
    private void handleNumeric(byte byt) {
        if (!inToken()) {
            notifyNumberTokenStart();
            appendToken(byt);
            return;
        }

        if (inNumberToken()) {
            if (ByteRange.startWith(ByteConstants.e).andAdd(ByteConstants.E).contains(byt) && hasByte(ByteConstants.b, token.toByteBuffer())) {
                throwError("Number exception, cannot have two exponent symbols in the same number");
            }

            if (byt == ByteConstants.DECIMAL && hasByte(byt, token.toByteBuffer())) {
                throwError("Number exception: cannot have two decimal symbols in the same number");
            }

            appendToken(byt);
        }
    }

    /**
     * Evaluates if the current byte should be handled as a boolean value
     *
     * @param byt the current byte
     * @return {@code true} if the current byte should be handled as a boolean value; false otherwise
     */
    private boolean isBoolean(byte byt) {
        // final boolean isBool = (hasByte(byt, booleanRange) && ((!inToken() && ((byt == t) || (byt == f)) )|| inBooleanToken()));
        return hasByte(byt, booleanRange) && (!inToken() && (byt == ByteConstants.t || byt == ByteConstants.f) || inBooleanToken());
    }

    /**
     * Handle the current byte as a boolean value
     *
     * @param byt the current byte
     */
    private void handleBoolean(byte byt) {
        if (!inToken()) {
            notifyBooleanTokenStart();
        }

        appendToken(byt);

    }

    /**
     * Evaluate if the current by should be handled as a null value
     *
     * @param byt the current byte
     * @return {@code true} if the current byte should be handled as null value; false otherwise
     */
    private boolean isNull(byte byt) {
        return hasByte(byt, nullRange) && (!inToken() && byt == ByteConstants.n || inNullToken());
    }

    /**
     * Handle the current byte as a null value token
     *
     * @param byt the current byte
     */
    private void handleNull(byte byt) {
        if (!inToken()) {
            notifyNullTokenStart();
        }

        appendToken(byt);
    }

    /**
     * Evaluate if the current byte is a delimiter (comma or colon) and should be handled as a delimiter
     *
     * @param byt the current byte
     * @return {@code true} if the current byte should be handled as a delimiter; false otherwise;
     */
    private boolean isDelimiter(byte byt) {
        return hasByte(byt, delimiterRange) && !inStringToken();
    }

    /**
     * Handle delimiter between key/value pairs and entity elements
     *
     * @param byt the current byte
     */
    private void handleDelimiter(byte byt) {
        //mark the delimiter
        lastMarker = new MarkerInfo(byt, getLineNumber(), getColumn() + 1);
        //if not the next expected delimiter, throw error

        byte expectedDelimiter = getExpectedDelimiter();
        if (byt != expectedDelimiter) {
            throwError("Unexpected delimiter: '" + (char) byt + "'");
        }

        notifyEndToken();

        if (byt == ByteConstants.COMMA) {
            notifyEntityEnd();
        } else if (byt == ByteConstants.COLON) {
            notifyKeyEnd();
        }

        pushNextExpectedDelimiter(expectedDelimiter);

        incrementColumn();
    }

    /**
     * Evaluate if the current byte should be handled as a container marker
     *
     * @param byt the current byte
     * @return {@code true} if the byte should be handled as a container marker; false otherwise
     */
    private boolean isContainer(byte byt) {
        return hasByte(byt, containerRange) && !inStringToken();
    }

    /**
     * Handle the current byte as a container marker
     *
     * @param byt the current byte
     */
    private void handleContainer(byte byt) {

        if (inStringToken()) {
            appendToken(byt);
        } else {
            if (inNumberToken() || inBooleanToken() || inNullToken()) {
                notifyEndToken();
            }

            switch (byt) {
                case ByteConstants.START_ARRAY -> {
                    if (!documentStarted) {
                        fireDocumentStartEvent(ByteBuffer.allocate(1).put(byt), lineNumber, column);
                        documentStarted = true;
                        resetToken();
                    } else {
                        fireArrayStartEvent(lineNumber, column);
                    }
                    //push container to stack
                    setContainer(byt);
                }
                case ByteConstants.END_ARRAY -> {
                    MarkerInfo markerInfo = containerStack.peek();
                    byte expectedContainer = getContainer();
                    byte expectedDelimiter = getExpectedDelimiter();

                    if (expectedContainer == ByteConstants.START_ARRAY && expectedDelimiter == ByteConstants.COMMA) {
                        fireArrayEndEvent(lineNumber, column);
                    } else {
                        if (expectedContainer != ByteConstants.START_ARRAY) {
                            throwError("Expected an object closure ('}'), but read array closure ']' [" + markerInfo + "]");
                        } else {
                            throwError("Expected last delimiter to be a comma, but read key delimiter ':'");
                        }
                    }
                }
                case ByteConstants.START_MAP -> {
                    if (!documentStarted) {
                        fireDocumentStartEvent(ByteBuffer.allocate(1).put(byt), lineNumber, column);
                        documentStarted = true;
                        resetToken();
                    } else {
                        fireMapStartEvent(lineNumber, column);
                    }
                    setContainer(byt);
                }
                case ByteConstants.END_MAP -> {
                    MarkerInfo markerInfo = containerStack.peek();
                    byte expectedContainer = getContainer();
                    byte expectedDelimiter = getExpectedDelimiter();

                    if (expectedContainer == ByteConstants.START_MAP && expectedDelimiter == ByteConstants.COMMA
                        && getLastEvent().getEventType() != EventType.KEY_END) {
                        fireMapEndEvent(lineNumber, column);
                    } else {
                        if (getLastEvent().getEventType() == EventType.KEY_END || getLastEvent().getEventType() == EventType.STRING_END) {
                            throwError("Object entity missing value. Last valid event: " + getLastEvent());
                        } else if (expectedContainer != ByteConstants.START_MAP) {
                            throwError("Expected an array closure (']'), but read object closure '}' [" + markerInfo + "]");
                        } else {
                            throwError("Extra comma delimiter: [" + lastMarker + "]");
                        }
                    }
                }
            }

            incrementColumn();

        }
    }

    /**
     * Evaluate if the current byte should be handled as a special character (i.e., backslash, quote, or start of boolean or null value)
     *
     * @param byt the current byte
     * @return {@code true} if the current byte should be handled as a special character; false otherwise;
     */
    private boolean isSpecialCharacter(byte byt) {
        return inStringToken() && escapeRange.contains(byt);
    }

    /**
     * Handle special character
     *
     * @param byt the current character
     */
    private void handleSpecialCharacters(byte byt) {

        if (escapeFlag) {
            appendToken(byt);
            escapeFlag = false;
        } else if (byt == ByteConstants.BACKSLASH) {
            appendToken(byt);
            escapeFlag = true;
        } else if (byt == ByteConstants.QUOTE) {
            incrementColumn();
            notifyStringTokenEnd(token.toByteBuffer());
        } else if (byt == ByteConstants.SOLIDUS) {
            if (getSettings().useStrict()) {
                throwError("Per ECMA-404 specification, solidus ('/') must be escaped");
            } else {
                appendToken(byt);
            }
        } else {
            appendToken(byt);
        }
    }


    /**
     * Handle string value
     *
     * @param byt the current byte to processed as a string
     */
    private void handleString(byte byt) {
        if (!inToken()) {
            if (byt == ByteConstants.QUOTE) {
                notifyStringTokenStart();
                incrementColumn();
                return;
            } else {
                throwError("Unexpected character found outside of String value " + (char) byt);
            }
        }

        if (isSpecialCharacter(byt) || escapeFlag) {
            handleSpecialCharacters(byt);
        } else {
            appendToken(byt);
        }

    }

    /**
     * Fire event at the end of token processing and reset state
     */
    private void notifyEndToken() {
        switch (tokenState) {
            case TOKEN_STATE_BOOLEAN -> notifyBooleanTokenEnd(token.toByteBuffer());
            case TOKEN_STATE_NULL -> notifyNullTokenEnd(token.toByteBuffer());
            case TOKEN_STATE_NUMBER -> notifyNumberTokenEnd(token.toByteBuffer());
            case TOKEN_STATE_STRING -> notifyStringTokenEnd(token.toByteBuffer());
        }

        resetToken();
    }

    /**
     * Reset token state
     */
    private void resetToken() {
        token.clear();
        setTokenState(TOKEN_STATE_EMPTY);
    }

    /**
     * Evaluate a byte against a defined {@link ByteRange}
     *
     * @param byt   the current byte
     * @param range the defined ByteRange
     * @return {@code true} if the range contains this byte; false otherwise
     */
    private boolean hasByte(byte byt, ByteRange range) {
        return range.contains(byt);
    }

    /**
     * Syntactic sugar for {@link #hasByte(byte, ByteRange)}. Just wraps the ByteBuffer in a ByteRange
     *
     * @param byt    the current byte
     * @param buffer the current ByteBuffer
     * @return {@code true} if the range contains this byte; false otherwise
     */
    private boolean hasByte(byte byt, ByteBuffer buffer) {
        return ByteRange.startWith(buffer.array()).contains(byt);
    }

    /**
     * Append a byte to the current token
     *
     * @param byt the current byte to append
     */
    private void appendToken(byte byt) {
        token.add(byt);
        incrementColumn();
    }

    /**
     * Increment the column cursor
     */
    private void incrementColumn() {
        column++;
    }

    /**
     * Increment the line cursor
     */
    private void incrementLine() {
        lineNumber++;
        column = 0;
    }

    /**
     * Read a block of data
     *
     * @return a ByteBuffer containing a block of data
     * @throws IOException thrown if an error occurs
     */
    private ByteBuffer readBlock() throws IOException {
        final byte[] byteBuffer = new byte[getSettings().getBlockSizeBytes()];
        final int readResult = getStream().read(byteBuffer);

        ByteBuffer buffer = null;

        if (readResult != -1) {
            //read the byte array into a ByteBuffer for better handling of the array
            //and only allocate what was read
            buffer = ByteBuffer.wrap(byteBuffer, 0, readResult);
        }

        return buffer;
    }

    /**
     * Return the underlying inputstream holding the JSON data
     *
     * @return the underlying inputStream;
     */
    private InputStream getStream() {
        return bis;
    }

    /**
     * Evaluate if the current state is processing a string
     *
     * @return {@code true} if the current state is processing a string
     */
    private boolean inStringToken() {
        return tokenState == TOKEN_STATE_STRING;
    }

    /**
     * Evaluate if the current state is processing a boolean
     *
     * @return {@code true} if the current state is processing a boolean
     */
    private boolean inBooleanToken() {
        return tokenState == TOKEN_STATE_BOOLEAN;
    }

    /**
     * Evaluate if the current state is processing a number
     *
     * @return {@code true} if the current state is processing a number
     */
    private boolean inNumberToken() {
        return tokenState == TOKEN_STATE_NUMBER;
    }

    /**
     * Evaluate if the current state is processing a null
     *
     * @return {@code true} if the current state is processing a null
     */
    private boolean inNullToken() {
        return tokenState == TOKEN_STATE_NULL;
    }

    /**
     * Evaluate if the current state is processing anything
     *
     * @return {@code true} if the current state is processing anything
     */
    private boolean inToken() {
        return tokenState != TOKEN_STATE_EMPTY;
    }

    /**
     * Apply a token state
     *
     * @param tokenState the token state to apply
     */
    private void setTokenState(int tokenState) {
        this.tokenState = tokenState;
    }

    /**
     * Validate if the current byte buffer is a boolean token
     *
     * @param booleanData the byte array containing the boolean data
     */
    private void validateBoolean(ByteBuffer booleanData) {
        ByteSequence sequenceToUse = null;
        if (booleanData.array()[0] == ByteConstants.t) {
            sequenceToUse = trueSequence;
        } else if (booleanData.array()[0] == ByteConstants.f) {
            sequenceToUse = falseSequence;
        }

        if (sequenceToUse == null) {
            throwError("Unexpected boolean value: expecting either " + trueSequence
                + " or " + falseSequence + ", but found " + ByteSequence.withStartingSequence(booleanData.array()));
        }

        final boolean result = sequenceToUse.matches(booleanData.array());

        if (!result) {
            throwError("Unexpected boolean value: expected " + sequenceToUse +
                " but found " + ByteSequence.withStartingSequence(booleanData.array()));
        }
    }

    /**
     * Validate if the current byte buffer is a null token
     *
     * @param nullData the byte array containing the null data
     */
    private void validateNull(ByteBuffer nullData) {
        if (!nullSequence.matches(nullData.array())) {
            throwError("Unexpected null value: expected " + nullSequence
                + " but found " + ByteSequence.withStartingSequence(nullData.array()));
        }
    }

    /**
     * Throw an error
     *
     * @param message the error message
     */
    private void throwError(String message) {
        throw new JsonEventParserException(getLineNumber(), getColumn(), message);
    }

    /**
     * pop and return the last pushed delimiter byte value.
     *
     * @return the last pushed delimiter byte value.
     */
    private byte getExpectedDelimiter() {
        try {
            return expectedDelimiters.pop();
        } catch (NoSuchElementException e) {
            String message = "Expecting to find a delimiter, but not found";
            throwError(message);

        }

        return 0;
    }

    /**
     * push new delimiter to stack if the container stack is not empty and evaluate the last delimiter
     * based on the container.
     *
     * @param lastDelimiter the last delimiter popped.
     */
    private void pushNextExpectedDelimiter(byte lastDelimiter) {
        if (containerNotEmpty() && peekContainer() == ByteConstants.START_ARRAY) {
            setExpectedDelimiter(ByteConstants.COMMA);
        } else if (containerNotEmpty() && peekContainer() == ByteConstants.START_MAP) {
            if (lastDelimiter == ByteConstants.COLON) {
                setExpectedDelimiter(ByteConstants.COMMA);
            } else if (lastDelimiter == ByteConstants.NULL || lastDelimiter == ByteConstants.COMMA) {
                setExpectedDelimiter(ByteConstants.COLON);
            } else {
                throwError("Unexpected delimiter: " + (char) lastDelimiter);
            }
        }
    }

    /**
     * Push a delimiter to stack
     *
     * @param delimiter the delimiter
     */
    private void setExpectedDelimiter(byte delimiter) {
        if (delimiter == ByteConstants.COLON || delimiter == ByteConstants.COMMA) {
            expectedDelimiters.push(delimiter);
        } else {
            throwError("Unexpected delimiter: " + (char) delimiter);
        }
    }

    /**
     * Evaluate if container stack is not empty
     *
     * @return {@code true} if not empty
     */
    private boolean containerNotEmpty() {
        return !containerStack.isEmpty();

    }

    /**
     * Peek and return the last container
     *
     * @return the last container without popping stack
     */
    private byte peekContainer() {
        if (containerStack.isEmpty()) {
            throwError("Unexpected empty entity container");
        }
        return containerStack.peek().containerByte();
    }

    /**
     * Pop and return the last container
     *
     * @return the last container
     */
    private byte getContainer() {
        try {
            return containerStack.pop().containerByte();
        } catch (NoSuchElementException e) {
            throwError("Missing entity container");
        }

        return 0;
    }

    /**
     * Push a new container marker to stack
     *
     * @param containerByte the container byte
     */
    private void setContainer(byte containerByte) {
        if (containerByte == ByteConstants.START_ARRAY || containerByte == ByteConstants.START_MAP) {
            containerStack.push(new MarkerInfo(containerByte, getLineNumber(), getColumn()));
            //we'll set the next delimiter based on the container
            pushNextExpectedDelimiter(ByteConstants.NULL);
        } else {
            throwError("Unexpected container " + (char) containerByte);
        }
    }

    /**
     * Last check after processing all bytes to ensure that there are no "dangling" containers
     */
    private void checkDocumentEnd() {
        if (!containerStack.isEmpty()) {
            MarkerInfo info = containerStack.peek();
            if (info.isArray()) {
                throw new JsonEventParserException(info.row(), info.column(), "Array container missing closing character ']'");
            } else {
                throw new JsonEventParserException(info.row(), info.column(), "Map container missing closing character '}'");
            }
        }
    }

    /**
     * Record class holding marker data
     *
     * @param containerByte the marker byte
     * @param row           the line/row
     * @param column        the column
     */
    private record MarkerInfo(byte containerByte, int row, int column) {
        /**
         * Return true if the marker byte is a map
         *
         * @return true if map byte; false otherwise
         */
        public boolean isMap() {
            return containerByte() == ByteConstants.START_MAP;
        }

        /**
         * Evaluate if byte is an array marker
         *
         * @return true if an array marker
         */
        public boolean isArray() {
            return containerByte() == ByteConstants.START_ARRAY;
        }

        /**
         * Return record as a string
         *
         * @return record as a string
         */
        public String toString() {
            return "Marker [char='" + (char) containerByte() + "'; row=" + row() + "; column=" + column() + "]";
        }
    }
}
