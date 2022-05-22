package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.JsonEventParserException;
import io.github.xmljim.json.factory.parser.ParserSettings;
import io.github.xmljim.json.factory.parser.Statistics;
import io.github.xmljim.json.parser.util.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
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

    private void notifyStringTokenStart() {
        setTokenState(TOKEN_STATE_STRING);
        fireStringStartEvent(lineNumber, column);
    }

    private void notifyStringTokenEnd(ByteBuffer tokenValue) {
        resetToken();
        fireStringEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyBooleanTokenStart() {
        setTokenState(TOKEN_STATE_BOOLEAN);
        fireBooleanStartEvent(lineNumber, column);
    }

    private void notifyBooleanTokenEnd(ByteBuffer tokenValue) {
        validateBoolean(tokenValue);
        resetToken();
        fireBooleanEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyNumberTokenStart() {
        setTokenState(TOKEN_STATE_NUMBER);
        fireNumberStartEvent(lineNumber, column);
    }

    private void notifyNumberTokenEnd(ByteBuffer tokenValue) {
        resetToken();
        fireNumberEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyNullTokenStart() {
        setTokenState(TOKEN_STATE_NULL);
        fireNullStartEvent(lineNumber, column);
    }

    private void notifyNullTokenEnd(ByteBuffer tokenValue) {
        validateNull(tokenValue);
        resetToken();
        fireNullEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyEntityEnd() {
        resetToken();
        fireEntityEndEvent(lineNumber, column);
    }

    private void notifyKeyEnd() {
        resetToken();
        fireKeyEndEvent(lineNumber, column);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumn() {
        return column;
    }

    private void processBlock(ByteBuffer block) {
        if (block != null) {
            while (block.hasRemaining()) {
                final byte b = block.get();
                handleByte(b);
            }
        } else {
            fireDocumentEndEvent(lineNumber, column);
        }
    }

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

    private boolean isWhitespace(byte b) {
        return hasByte(b, whitespaceRange);
    }

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

    private boolean isNumeric(byte byt) {
        return hasByte(byt, numberRange) && (!inToken() || inNumberToken());
    }

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

    private boolean isBoolean(byte byt) {
        // final boolean isBool = (hasByte(byt, booleanRange) && ((!inToken() && ((byt == t) || (byt == f)) )|| inBooleanToken()));
        return hasByte(byt, booleanRange) && (!inToken() && (byt == ByteConstants.t || byt == ByteConstants.f) || inBooleanToken());
    }

    private void handleBoolean(byte byt) {
        if (!inToken()) {
            notifyBooleanTokenStart();
        }

        appendToken(byt);

    }

    private boolean isNull(byte byt) {
        return hasByte(byt, nullRange) && (!inToken() && byt == ByteConstants.n || inNullToken());
    }

    private void handleNull(byte byt) {
        if (!inToken()) {
            notifyNullTokenStart();
        }

        appendToken(byt);
    }

    private boolean isDelimiter(byte byt) {
        return hasByte(byt, delimiterRange) && !inStringToken();
    }

    private void handleDelimiter(byte byt) {
        notifyEndToken();

        if (byt == ByteConstants.COMMA) {
            notifyEntityEnd();
        } else if (byt == ByteConstants.COLON) {
            notifyKeyEnd();
        }

        incrementColumn();
    }

    private boolean isContainer(byte byt) {
        return hasByte(byt, containerRange);
    }

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
                }
                case ByteConstants.END_ARRAY -> fireArrayEndEvent(lineNumber, column);

                case ByteConstants.START_MAP -> {
                    if (!documentStarted) {
                        fireDocumentStartEvent(ByteBuffer.allocate(1).put(byt), lineNumber, column);
                        documentStarted = true;
                        resetToken();
                    } else {
                        fireMapStartEvent(lineNumber, column);
                    }
                }
                case ByteConstants.END_MAP -> fireMapEndEvent(lineNumber, column);
            }

            incrementColumn();

        }
    }

    private boolean isSpecialCharacter(byte byt) {
        return inStringToken() && escapeRange.contains(byt);
    }

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

    private void notifyEndToken() {
        switch (tokenState) {
            case TOKEN_STATE_BOOLEAN -> notifyBooleanTokenEnd(token.toByteBuffer());
            case TOKEN_STATE_NULL -> notifyNullTokenEnd(token.toByteBuffer());
            case TOKEN_STATE_NUMBER -> notifyNumberTokenEnd(token.toByteBuffer());
            case TOKEN_STATE_STRING -> notifyStringTokenEnd(token.toByteBuffer());
        }

        resetToken();
    }

    private void resetToken() {
        token.clear();
        setTokenState(TOKEN_STATE_EMPTY);
    }

    private boolean hasByte(byte byt, ByteRange range) {
        return range.contains(byt);
    }

    private boolean hasByte(byte byt, ByteBuffer buffer) {
        return ByteRange.startWith(buffer.array()).contains(byt);
    }

    private void appendToken(byte byt) {
        token.add(byt);
        incrementColumn();
    }

    private void incrementColumn() {
        column++;
    }

    private void incrementLine() {
        lineNumber++;
        column = 0;
    }

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

    private InputStream getStream() {
        return bis;
    }

    private boolean inStringToken() {
        return tokenState == TOKEN_STATE_STRING;
    }

    private boolean inBooleanToken() {
        return tokenState == TOKEN_STATE_BOOLEAN;
    }

    private boolean inNumberToken() {
        return tokenState == TOKEN_STATE_NUMBER;
    }

    private boolean inNullToken() {
        return tokenState == TOKEN_STATE_NULL;
    }

    private boolean inToken() {
        return tokenState != TOKEN_STATE_EMPTY;
    }

    private void setTokenState(int tokenState) {
        this.tokenState = tokenState;
    }

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

    private void validateNull(ByteBuffer nullData) {
        if (!nullSequence.matches(nullData.array())) {
            throwError("Unexpected null value: expected " + nullSequence
                + " but found " + ByteSequence.withStartingSequence(nullData.array()));
        }
    }

    private void throwError(String message) {
        throw new JsonEventParserException(getLineNumber(), getColumn(), message);
    }
}
