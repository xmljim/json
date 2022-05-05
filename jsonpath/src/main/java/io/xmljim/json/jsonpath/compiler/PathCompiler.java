package io.xmljim.json.jsonpath.compiler;

import io.xmljim.json.jsonpath.variables.Global;
import io.xmljim.json.jsonpath.filter.Filter;
import io.xmljim.json.jsonpath.filter.FilterFactory;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.filter.FilterType;

class PathCompiler extends Compiler<FilterStream> {
    private final FilterStream sequence = new FilterStream();
    private boolean predicatePath = false;

    public PathCompiler(PathExpression expression, Global global) {
        this(expression, false, global);
    }

    public PathCompiler(PathExpression expression, boolean isPredicatePath, Global global) {
        super(expression, global);
        this.predicatePath = isPredicatePath;
    }

    @Override
    public FilterStream compile() {
        processExpression();
        return sequence;
    }

    @Override
    public void handleCharacter(Character character) {
        switch (character) {
            case ROOT -> handleRoot();
            case CURRENT -> handleCurrent();
            case DOT -> handleDot();
            case FILTER_START -> handleFilterStart();
            case FILTER_END -> handleFilterEnd();
            case LEFT_PAREN -> handleLeftParen();
            case RIGHT_PAREN -> handleRightParen();
            case QUOTE -> handleQuote();
            case REGEX_CONTAINER -> handleRegexContainer();
            case WILDCARD -> handleWildcard();
            default -> handleFilterContent();
        }
    }

    @Override
    public void applyToken() {
        sequence.add(FilterFactory.newOperator(getTokenString(), getGlobal()));
        clearToken();
    }

    private void handleRoot() {
        appendCurrentToToken();

        if (!isEncased()) {
            applyToken();
        }
    }

    private void handleCurrent() {
        if (predicatePath) {
            appendCurrentToToken();
            applyToken();
        } else {
            if (lastEnclosure() != LEFT_PAREN) {
                throw new JsonPathExpressionException(expression(), "Invalid context. Current ('@') can only be applied as a Filter predicate");
            }

            appendCurrentToToken();
        }
    }

    private void handleDot() {
        if (isEncased()) {
            //just append, it's encased in a bracket, regex, paren or quote
            //we'll handle on the second predicate compiler pass
            appendCurrentToToken();
        } else if (isEncased() && isQuotedOrRegex() && predicatePath) {
            //predicate path dot is enclosed by quote or regex container, just append
            appendCurrentToToken();
        } else {
            if (isStartOfExpression()) {
                //shouldn't be the start of the expression
                throw new JsonPathExpressionException(expression(), "Invalid syntax: Expression cannot start with dot separator");
            } else if (isOrphaned()) {
                //invalid syntax - ending dot without accessor or filter
                throw new JsonPathExpressionException(expression(), "Invalid syntax: Orphaned dot separator");
            } else if (isRecursiveCandidate()) {
                //catch redundant dot if last sequence type is recursive
                if (getLastFilterType() == FilterType.RECURSIVE) {
                    throw new JsonPathExpressionException(expression(), "Invalid dot separator. Last sequence is recursive");
                }

                //append to buffer
                appendCurrentToToken();

                //check if this is a 'naked' recursion (not surrounded by brackets)
                //in the case that a recursion filter is surrounded by brackets, it will
                //be handled with the handleFilterEnd method
                if (getTokenString().equals("..")) {
                    applyToken();
                }
            } else if (tokenHasContent()) {
                //has token data for the previous filter
                applyToken();
                if (isNextCharacter(DOT)) {
                    appendCurrentToToken();
                }

            }

            //default behavior is just to ignore and move on
        }
    }

    private void handleFilterStart() {
        if (isStartOfExpression() && !predicatePath) {
            //shouldn't be the start of the expression
            throw new JsonPathExpressionException(expression(), "Invalid syntax: Expression cannot start with a filter");
        }

        if (!hasNext()) {
            throw new JsonPathExpressionException(expression(), "Expression cannot end with " + current());
        }

        if (isEnclosuresEmpty() && tokenHasContent()) {
            applyToken();
        }

        pushEnclosure(current());
        appendCurrentToToken();
    }

    private void handleFilterEnd() {
        if (isTokenEmpty()) {
            //can't start a new filter with an end bracket
            throw new JsonPathExpressionException(expression(), "Expression cannot start with a filter terminator");
        }

        Character lastEnclosure = lastEnclosure();
        if (isQuotedOrRegex()) {
            appendCurrentToToken();
        } else if (lastEnclosure == FILTER_START) {
            popEnclosure();
            appendCurrentToToken();

            if (isEnclosuresEmpty()) {
                applyToken();
            }
        } else {
            throw new JsonPathExpressionException(expression(), "Unbalanced terminating filter enclosure");
        }
    }

    private void handleFilterContent() {
        if (shouldBeSubscripted()) {
            throw new JsonPathExpressionException(expression(), "Invalid array accesor. Should be subscripted");
        }
        appendCurrentToToken();
    }

    private void handleLeftParen() {
        if (!isQuotedOrRegex()) {
            pushEnclosure(current());
        }
        appendCurrentToToken();
    }

    private void handleRightParen() {
        //TODO: handle function logic
        if (lastEnclosure() == LEFT_PAREN) {
            popEnclosure();
            appendCurrentToToken();
        } else if (!isQuotedOrRegex()) {
            throw new JsonPathExpressionException(expression(), "Unbalanced parenthesis");
        } else {
            appendCurrentToToken();
        }
    }

    private void handleQuote() {
        if (lastEnclosure() != null && lastEnclosure() == QUOTE) {
            popEnclosure();
        } else {
            pushEnclosure();
        }
        appendCurrentToToken();
    }

    private void handleWildcard() {
        if (isEncased()) {
            appendCurrentToToken();
        } else {
            if (isTokenEmpty()) {
                appendCurrentToToken();
                applyToken();
            }
        }
    }

    private void handleRegexContainer() {
        if (lastEnclosure() == LEFT_PAREN && lastNonWhitespaceCharacter() == REGEX_OP) {
            pushEnclosure();
        } else if (lastEnclosure() == REGEX_CONTAINER) {
            popEnclosure();
        }
        appendCurrentToToken();
    }


    private boolean isRecursiveCandidate() {
        return ((isTokenEmpty() && peek() == DOT) || tokenMatches("\\[?\\."))
            && (current() == DOT);
    }

    private Filter getLastSequence() {
        return sequence.peekLast();
    }

    private FilterType getLastFilterType() {
        return getLastSequence() == null ? FilterType.INVALID : getLastSequence().getOperatorType();
    }

    private boolean shouldBeSubscripted() {
        return (Character.isDigit(current())
            && isTokenEmpty()
            && !isEncased()
        );
    }
}
