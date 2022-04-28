package io.xmljim.json.jsonpath.compiler;

public class JsonPathExpressionException extends RuntimeException {

    private final String expression;
    private final int position;
    private final String message;

    public JsonPathExpressionException(PathExpression expression, String message) {
        this(expression.getExpression(), expression.position(), message);
    }

    public JsonPathExpressionException(String expression, int position, String message) {
        super(message + ": [expression=" + expression + "; position=" + position + "]");

        this.expression = expression;
        this.position = position;
        this.message = message;
    }

    public String getExpression() {
        return expression;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
