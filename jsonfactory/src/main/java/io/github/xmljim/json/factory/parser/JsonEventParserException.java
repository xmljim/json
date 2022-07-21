package io.github.xmljim.json.factory.parser;

/**
 * An event parser exception
 */
public class JsonEventParserException extends JsonParserException {
    private int line = -1;
    private int column = -1;

    /**
     * Constructor
     */
    public JsonEventParserException() {
    }


    /**
     * Constructor
     *
     * @param lineNumber line number
     * @param column     column
     * @param message    message
     */
    public JsonEventParserException(int lineNumber, int column, String message) {
        super(message + " [at line: " + lineNumber + "; col: " + column + "]");
        this.line = lineNumber;
        this.column = column;
    }

    /**
     * Constructor
     *
     * @param message message
     */
    public JsonEventParserException(final String message) {
        super(message);
    }


    /**
     * Constructor
     *
     * @param cause the underlying exception
     */
    public JsonEventParserException(final Throwable cause) {
        super(cause);
    }

    public long getLineNumber() {
        return line;
    }

    public long getColumn() {
        return column;
    }
}
