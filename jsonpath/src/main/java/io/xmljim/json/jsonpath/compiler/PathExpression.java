package io.xmljim.json.jsonpath.compiler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

class PathExpression implements Iterator<Character> {
    private final String expression;
    private final AtomicInteger position = new AtomicInteger(-1);
    private Character lastChar = null;
    private Character lastNonWhitespaceChar = null;
    private final LinkedList<Character> expressionList = new LinkedList<>();
    private Character current = null;

    public PathExpression(String expression) {
        this.expression = expression;
        expression.chars().forEach(c -> {
            expressionList.add((char) c);
        });
    }


    @Override
    public boolean hasNext() {
        return expressionList.iterator().hasNext();
    }

    @Override
    public Character next() {
        if (expressionList.peek() != null) {
            position.getAndIncrement();
            lastNonWhitespaceChar = ((lastChar != null || current != null) && !Character.isWhitespace(current)) ? current : lastChar;
            lastChar = current;
            current = expressionList.pop();
            return current;
        }

        return null;
    }

    public int position() {
        return position.get();
    }

    public Character current() {
        return current;
    }

    public Character peek() {
        return expressionList.peek();
    }

    public Character last() {
        return lastChar;
    }

    public Character lastNonWhitespaceChar() {
        return lastNonWhitespaceChar;
    }

    public String getExpression() {
        return expression;
    }

    public String toString() {
        return "[" + position.get() + ", " + current + "]: " + expression;
    }
}
