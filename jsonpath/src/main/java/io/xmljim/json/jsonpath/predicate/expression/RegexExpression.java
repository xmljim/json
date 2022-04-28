package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

import java.util.regex.Pattern;

public class RegexExpression extends AbstractExpression {
    private Pattern pattern;

    public RegexExpression(String expression, Global global) {
        super(expression, global);

        //TODO: apply to predicate, not expression
        /*
        Pattern pattern = Pattern.compile(Expression.REGEX_PATTERN);
        Matcher matcher = pattern.matcher(expression);
        String regexExpression = matcher.group("pattern");
        String flagExpression = matcher.group("flags");

        AtomicInteger flags = new AtomicInteger();
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

        pattern = Pattern.compile(regexExpression, flags.get());
        */

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(Context inputContext) {
        return (T) get(inputContext).get().asJsonValue().get();
    }

    @Override
    public Context get(Context inputContext) {
        return Context.createSimpleContext(getExpression());
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.REGEX;
    }
}
