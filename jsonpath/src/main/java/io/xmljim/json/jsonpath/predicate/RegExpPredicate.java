package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegExpPredicate extends StringFilterPredicate {
    private final Pattern pattern;

    public RegExpPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.REGEX_MATCH);

        Pattern pattern = Pattern.compile(Expression.REGEX_PATTERN);
        Matcher matcher = pattern.matcher(getValue(rightSide, Context.defaultContext()));
        if (matcher.matches()) { //need to fire this method to invoke the match and retrieve the groups
            String regexExpression = matcher.group("pattern");
            String flagExpression = matcher.group("flags");
            AtomicInteger flags = new AtomicInteger(0);

            if (flagExpression != null && !"".equals(flagExpression)) {
                flagExpression.chars().forEach(c -> {
                    switch (c) {
                        case 'i' -> flags.updateAndGet(v -> v | Pattern.CANON_EQ);
                        case 'x' -> flags.updateAndGet(v -> v | Pattern.COMMENTS);
                        case 'm' -> flags.updateAndGet(v -> v | Pattern.MULTILINE);
                        case 'u' -> flags.updateAndGet(v -> v | Pattern.UNICODE_CASE);
                        case 's' -> flags.updateAndGet(v -> v | Pattern.DOTALL);
                        case 'd' -> flags.updateAndGet(v -> v | Pattern.UNIX_LINES);
                    }
                });
            }
            //this is a silly IntelliJ warning. Intrinsically I've handled the int value above by or-ing the
            //values
            this.pattern = Pattern.compile(regexExpression, flags.get());
        } else {
            throw new JsonPathExpressionException(getValue(leftSide, Context.defaultContext()), 0, "Invalid RegExp Expression");
        }
    }


    @Override
    public boolean test(Context context) {
        Matcher matcher = pattern.matcher(getValue(leftSide(), context));
        return matcher.matches();
    }
}
