package io.xmljim.json.jsonpath.compiler;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.PredicateFactory;
import io.xmljim.json.jsonpath.predicate.PredicateOperator;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.function.Predicate;

class PredicateCompiler extends Compiler<Predicate<Context>> {
    private Predicate<Context> predicate;

    private PredicateToken predicateToken = PredicateToken.START;
    private PredicateExpression left;
    private PredicateExpression right;
    private PredicateOperator operator;
    private boolean negate;
    private boolean nestedPredicate;  //TODO: handle "nested" predicate for left/right expressions
    private PredicateJoin predicateJoin = PredicateJoin.NONE;

    public PredicateCompiler(PathExpression expression, Global global) {
        super(expression, global);
    }

    @Override
    public Predicate<Context> compile() {
        processExpression();
        return predicate;
    }

    @Override
    public void handleCharacter(Character character) {
        if (predicateToken == PredicateToken.START) {
            predicateToken = PredicateToken.LEFT;
        }

        switch (character) {
            case SPACE -> handleSpace();
            case NOT -> handleNot();
            case EQ -> handleEquals();
            case GT -> handleGreaterThan();
            case LT -> handleLessThan();
            case REGEX_OP -> handleRegexOperator();
            case REGEX_CONTAINER -> handleRegexContainer();
            case QUOTE -> handleQuote();
            case QUESTION -> handleQuestion();
            case AND -> handleAnd();
            case OR -> handleOr();
            case LEFT_PAREN -> handleLeftParen();
            case RIGHT_PAREN -> handleRightParen();
            case LEFT_BRACE -> handleLeftBrace();
            case RIGHT_BRACE -> handleRightBrace();
            default -> handleContent();
        }
    }

    @Override
    public void applyToken() {

        if (predicateJoin != PredicateJoin.NONE) {
            appendJoin();
        } else {
            switch (predicateToken) {
                case LEFT, RIGHT -> createExpression();
                case OPERATOR -> setOperator();
            }
            promoteToken();
            clearToken();
        }

        if (!hasNext() && predicateToken == PredicateToken.OPERATOR) {
            predicate = PredicateFactory.create(left, right, PredicateOperator.IS_NOT_NULL, negate);
        }
    }

    private void promoteToken() {
        predicateToken = switch (predicateToken) {
            case LEFT -> PredicateToken.OPERATOR;
            case OPERATOR -> PredicateToken.RIGHT;
            case RIGHT -> PredicateToken.END;
            default -> PredicateToken.START;
        };
    }

    private void handleContent() {
        appendCurrentToToken();
    }

    private void handleSpace() {
        if (isEncased()) {
            appendCurrentToToken();
        } else if (predicateJoin != PredicateJoin.NONE) {
            appendCurrentToToken();
        } else if (predicateToken == PredicateToken.LEFT) {
            applyToken();
            //no need to append, just transition to next token (operator)
        } else if (predicateToken == PredicateToken.OPERATOR && tokenHasContent()) {
            applyToken();
        }

        //TODO: Consider cases where spaces are invalid
    }

    private void handleNot() {
        if (isEncased()) {
            appendCurrentToToken();
        } else if (predicateJoin != PredicateJoin.NONE) {
            appendCurrentToToken();
        } else if (predicateToken != PredicateToken.OPERATOR && isStartOfExpression()) {
            negate = !negate;
        } else if (predicateToken == PredicateToken.LEFT && tokenHasContent()) {
            applyToken(); //build the expression, which also clears the token buffer
            appendCurrentToToken(); //append to the token buffer
        }
    }

    private void handleEquals() {
        if (isEncased()) {
            appendCurrentToToken();
        } else if (predicateJoin != PredicateJoin.NONE) {
            appendCurrentToToken();
        } else if (predicateToken == PredicateToken.LEFT && tokenHasContent()) {
            applyToken();
            appendCurrentToToken();
        } else if (predicateToken == PredicateToken.OPERATOR && (isLastCharacter(EQ) || isLastCharacter(LT) || isLastCharacter(GT))) {
            appendCurrentToToken();
            applyToken();
        } else if (predicateToken == PredicateToken.OPERATOR && isTokenEmpty()) {
            appendCurrentToToken();
        } else {
            throw new JsonPathExpressionException(expression(), "Unexpected operator: " + current());
        }
    }

    private void handleGreaterThan() {
        handleExpressionOperator();
    }

    private void handleLessThan() {
        handleExpressionOperator();
    }

    private void handleRegexOperator() {
        if (isEncased()) {
            appendCurrentToToken();
        } else if (lastCharacter() == EQ && predicateToken == PredicateToken.OPERATOR) {
            appendCurrentToToken();
            applyToken();
        } else {
            throw new JsonPathExpressionException(expression(), "Unexpected operator: " + current());
        }
    }

    private void handleRegexContainer() {
        if (lastNonWhitespaceCharacter() == REGEX_OP && predicateToken == PredicateToken.RIGHT && isTokenEmpty()) {
            pushEnclosure();
            appendCurrentToToken();
        } else if (isLastEnclosure(REGEX_CONTAINER)) {
            popEnclosure();
            appendCurrentToToken();
        } else if (isQuoted()) {
            appendCurrentToToken();
        } else {
            throw new JsonPathExpressionException(expression(), "Unexpected operator: " + current());
        }
    }

    private void handleQuote() {
        if (predicateJoin != PredicateJoin.NONE) {
            appendCurrentToToken();
        } else if (isQuoted()) {
            popEnclosure();
            appendCurrentToToken();

        } else if (isRegex()) {
            appendCurrentToToken();
        } else if (isFilter()) {
            pushEnclosure();
            appendCurrentToToken();
        } else if (isLastCharacter(ESCAPE)) {
            appendCurrentToToken();
        } else if (predicateToken != PredicateToken.OPERATOR && isTokenEmpty()) {
            pushEnclosure();
            appendCurrentToToken();
        } else if (isLastCharacter(FILTER_START)) {
            pushEnclosure();
            appendCurrentToToken();
        } else if (isLastCharacter(FILTER_END)) {
            popEnclosure();
            appendCurrentToToken();
        }
    }

    private void handleQuestion() {
        if (isQuoted() || isRegex()) {
            appendCurrentToToken();
        } else if (isFilter() && peek() == LEFT_PAREN && predicateToken != PredicateToken.OPERATOR) {
            //start of a new predicate
            appendCurrentToToken();
        } else {
            throw new JsonPathExpressionException(expression(), "Unexpected operator: " + current());
        }
    }

    private void handleLeftParen() {
        if (!isQuotedOrRegex()) {
            pushEnclosure();
        }
        appendCurrentToToken();
    }

    private void handleRightParen() {
        //TODO: handle function logic
        if (lastEnclosure() == LEFT_PAREN) {
            popEnclosure();
            appendCurrentToToken();

            if (predicateJoin != PredicateJoin.NONE && isEnclosuresEmpty()) {
                appendJoin();
            } else if (predicateJoin == PredicateJoin.NONE && isEnclosuresEmpty()) {
                //function?
                applyToken();
            }
        } else if (!isQuotedOrRegex()) {
            throw new JsonPathExpressionException(expression(), "Unbalanced parenthesis");
        } else {
            appendCurrentToToken();
        }
    }

    private void handleLeftBrace() {
        if (predicateJoin != PredicateJoin.NONE) {
            appendCurrentToToken();
        } else if (isQuotedOrRegex()) {
            appendCurrentToToken();
        } else if ((lastEnclosure() != null && lastEnclosure() == LEFT_PAREN)
            && (predicateToken == PredicateToken.LEFT || predicateToken == PredicateToken.RIGHT)) {
            pushEnclosure();
            appendCurrentToToken();
        } else if (predicateJoin == PredicateJoin.NONE && isTokenEmpty()) {
            pushEnclosure();
            appendCurrentToToken();

        } else {
            //invalid token?
            throw new JsonPathExpressionException(expression(), "invalid left brace");
        }
    }

    private void handleRightBrace() {
        if (predicateJoin != PredicateJoin.NONE) {
            appendCurrentToToken();
        } else if (isQuotedOrRegex()) {
            appendCurrentToToken();
        } else if (isLastEnclosure(LEFT_BRACE)) {
            popEnclosure();
            appendCurrentToToken();
            applyToken();
        } else {
            //invalid token
            throw new JsonPathExpressionException(expression(), "Invalid right brace");
        }
    }

    private void handleAnd() {
        if (isQuotedOrRegex()) {
            appendCurrentToToken();
        } else {
            if (predicateJoin == PredicateJoin.NONE) {
                if (predicateToken == PredicateToken.END && (!isLastCharacter(AND)) && (isNextCharacter(AND))) {
                    predicateJoin = PredicateJoin.AND;
                    predicateToken = PredicateToken.START;
                } else if (predicateJoin == PredicateJoin.AND && !isNextCharacter(AND)) {
                    //nothing to do
                } else {
                    //invalid
                    throw new JsonPathExpressionException(expression(), "Invalid logical operator (|)");
                }
            } else { //already in a join
                if (tokenHasContent() && isEnclosuresEmpty()) {
                    appendJoin();
                    predicateJoin = PredicateJoin.AND;
                    predicateToken = PredicateToken.START;
                } else if (isLastEnclosure(LEFT_PAREN)) {
                    appendCurrentToToken();
                }
            }


        }
    }

    private void handleOr() {
        if (isQuotedOrRegex()) {
            appendCurrentToToken();
        } else {
            if (predicateJoin == PredicateJoin.NONE) {
                if (predicateToken == PredicateToken.END && (!isLastCharacter(OR)) && (isNextCharacter(OR))) {
                    predicateJoin = PredicateJoin.OR;
                    predicateToken = PredicateToken.START;
                } else if (predicateJoin == PredicateJoin.OR && !isNextCharacter(OR)) {
                    //nothing to do
                } else {
                    //invalid
                    throw new JsonPathExpressionException(expression(), "Invalid logical operator (|)");
                }
            } else { //already in a join
                if (tokenHasContent() && isEnclosuresEmpty()) {
                    appendJoin();
                    predicateJoin = PredicateJoin.OR;
                    predicateToken = PredicateToken.START;
                } else if (isLastEnclosure(LEFT_PAREN)) {
                    appendCurrentToToken();
                }
            }


        }
    }


    private void handleExpressionOperator() {
        if (isEncased()) {
            appendCurrentToToken();
        } else if (predicateJoin != PredicateJoin.NONE) {
            appendCurrentToToken();
        } else if (predicateToken == PredicateToken.LEFT && tokenHasContent()) {
            applyToken();
            appendCurrentToToken();
        } else if (predicateToken == PredicateToken.OPERATOR && isTokenEmpty()) {
            appendCurrentToToken();
        } else {
            throw new JsonPathExpressionException(expression(), "Unexpected operator: " + current());
        }
    }

    private void createExpression() {
        PredicateExpression expression = Expression.create(getTokenString(), getGlobal());

        if (predicateToken == PredicateToken.LEFT) {
            left = expression;
            //predicateToken = PredicateToken.OPERATOR;
        } else {
            right = expression;
            predicate = PredicateFactory.create(left, right, operator, negate);
        }
    }

    private void setOperator() {
        operator = PredicateOperator.find(getTokenString());
    }

    private void appendJoin() {
        if (predicate != null) {
            PredicateCompiler compiler = new PredicateCompiler(new PathExpression(unwrapToken()), getGlobal());
            Predicate<Context> joinedPredicate = compiler.compile();
            if (this.predicateJoin == PredicateJoin.AND) {
                predicate = predicate.and(joinedPredicate);
            } else {
                predicate = predicate.or(joinedPredicate);
            }
            clearToken();
            predicateJoin = PredicateJoin.NONE;
        } else {
            throw new JsonPathExpressionException(expression(), "Appending join: unexpected join. Primary predicate not created");
        }
    }

    private String unwrapToken() {
        String stripped = getTokenString().strip().strip();
        if (getTokenString().strip().startsWith("(")) {
            return stripped.substring(1, stripped.length() - 1);
        }
        return stripped;
    }

    private enum PredicateToken {
        START,
        LEFT,
        RIGHT,
        OPERATOR,
        END
    }

    private enum PredicateJoin {
        AND,
        OR,
        NONE
    }
}
