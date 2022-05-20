package io.github.xmljim.json.factory.parser;

public class JsonEventParserException extends JsonParserException {
    private int line = -1;
    private int column = -1;

    public JsonEventParserException() {
    }

    public JsonEventParserException(int lineNumber, int column, String message) {
        super(message + " [at line: " + lineNumber + "; col: " + column + "]");
        this.line = line;
        this.column = column;
    }

    public JsonEventParserException(final String message) {
        super(message);
    }

    public JsonEventParserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonEventParserException(final Throwable cause) {
        super(cause);
    }

    public JsonEventParserException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public long getLineNumber() {
        return line;
    }

    public long getColumn() {
        return column;
    }
}
