package io.xmljim.json.jsonpath.compiler;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.variables.Global;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

/**
 * Base class for JsonPath expression compilation
 *
 * @param <T> The return result argType from the compilation
 */
public abstract class Compiler<T> {
    protected static final char ROOT = '$';
    protected static final char CURRENT = '@';
    protected static final char DOT = '.';
    protected static final char FILTER_START = '[';
    protected static final char FILTER_END = ']';
    protected static final char WILDCARD = '*';
    protected static final char QUESTION = '?';
    protected static final char LEFT_PAREN = '(';
    protected static final char RIGHT_PAREN = ')';
    protected static final char REGEX_OP = '~';
    protected static final char REGEX_CONTAINER = '/';
    protected static final char QUOTE = 39;
    protected static final char SPACE = ' ';
    protected static final char LT = '<';
    protected static final char GT = '>';
    protected static final char EQ = '=';
    protected static final char NOT = '!';
    protected static final char LEFT_BRACE = '{';
    protected static final char RIGHT_BRACE = '}';
    protected static final char ESCAPE = '\\';
    protected static final char AND = '&';
    protected static final char OR = '|';

    private final PathExpression expression;
    private final Token token = new Token();
    private final Deque<Character> enclosures = new ArrayDeque<>();
    private final Global global;

    protected Compiler(PathExpression expression, Global global) {
        this.expression = expression;
        this.global = global;
    }

    public static Compiler<FilterStream> newPathCompiler(String expression, Global global) {
        return newPathCompiler(expression, false, global);
    }

    public static Compiler<FilterStream> newPathCompiler(String expression, boolean isPredicatePath, Global global) {
        return new PathCompiler(new PathExpression(expression), isPredicatePath, global);
    }

    public static Compiler<Predicate<Context>> newPredicateCompiler(String expression, Global global) {
        return new PredicateCompiler(new PathExpression(expression), global);
    }

    public PathExpression expression() {
        return expression;
    }

    public Global getGlobal() {
        return global;
    }

    public abstract T compile();

    public void processExpression() {
        while (hasNext()) {
            handleCharacter(next());
        }

        if (tokenHasContent()) {
            applyToken();
        }
    }

    public Character current() {
        return expression().current();
    }

    public boolean hasNext() {
        return expression.hasNext();
    }

    public Character next() {
        return expression.next();
    }

    public Character peek() {
        return expression.peek();
    }

    public boolean isNextCharacter(Character c) {
        return (peek() != null && peek() == c);
    }

    public Character lastCharacter() {
        return expression.last();
    }

    public boolean isLastCharacter(Character c) {
        return (lastCharacter() != null && lastCharacter() == c);
    }

    public Character lastNonWhitespaceCharacter() {
        return expression.lastNonWhitespaceChar();
    }

    public boolean isStartOfExpression() {
        return isTokenEmpty() && lastCharacter() == null;
    }

    public boolean isOrphaned() {
        return isTokenEmpty() && !hasNext();
    }

    public void appendCurrentToToken() {
        token.append(current());
    }

    public void clearToken() {
        token.clear();
    }

    public String getTokenString() {
        return token.toString();
    }

    protected boolean isTokenEmpty() {
        return token.isEmpty();
    }

    public boolean tokenMatches(String regex) {
        return token.matches(regex);
    }

    public int tokenLength() {
        return token.length();
    }

    public boolean tokenHasContent() {
        return token.hasContent();
    }

    public boolean isEncased() {
        return enclosures.size() > 0;
    }

    public boolean isQuoted() {
        return lastEnclosure() != null && (lastEnclosure() == QUOTE && !isEscaped());
    }

    public boolean isEscaped() {
        return lastCharacter() != null && lastCharacter() == ESCAPE;
    }

    public boolean isRegex() {
        return lastEnclosure() != null && lastEnclosure() == REGEX_CONTAINER;
    }

    public boolean isFunctionCandidate() {
        return getTokenString().matches("[a-z-]+\\(.*");
    }

    public boolean isQuotedOrRegex() {
        return lastEnclosure() != null && (lastEnclosure() == QUOTE || lastEnclosure() == REGEX_CONTAINER);
    }

    public boolean isFilter() {
        return lastEnclosure() != null && lastEnclosure() == FILTER_START;
    }

    public void pushEnclosure(Character character) {
        enclosures.push(character);
    }

    public void pushEnclosure() {
        pushEnclosure(current());
    }

    public Character lastEnclosure() {
        return enclosures.peek();
    }

    public boolean isLastEnclosure(Character c) {
        return lastEnclosure() != null && lastEnclosure() == c;
    }

    public void popEnclosure() {
        enclosures.pop();
    }

    public boolean isEnclosuresEmpty() {
        return enclosures.isEmpty();
    }

    public abstract void handleCharacter(Character character);

    public abstract void applyToken();

    public String toString() {
        return expression().toString();
    }
}
