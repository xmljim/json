package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.FilterStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class VariableExpression extends AbstractExpression {
    private final String key;
    private final String path;
    private final PredicateExpression variableExpression;
    private FilterStream filterStream;

    public VariableExpression(String expression, Global global) {
        super(expression, global);
        Pattern pattern = Pattern.compile(Expression.VARIABLE_PATTERN);
        Matcher matcher = pattern.matcher(expression);
        if (matcher.matches()) {
            this.key = matcher.group("key");
            this.path = matcher.group("path");
            this.variableExpression = global.getVariable(key);
            if (path != null) {
                this.filterStream = Compiler.newPathCompiler(path, global).compile();
            }
        } else {
            throw new JsonPathExpressionException(expression, 0, "Invalid Variable Expression");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(Context inputContext) {
        //TODO: this could be a big problem. Reevaluate
        return (T) get(inputContext).get().asJsonValue().get();
    }

    @Override
    public Context get(Context inputContext) {
        PredicateExpression predicateExpression = getGlobal().getVariable(key);
        if (variableExpression.type() == ExpressionType.NODE && filterStream != null) {
            Stream<Context> resultStream = filterStream.filter(Stream.of(variableExpression.get(Context.defaultContext())));
            return resultStream.findFirst().orElse(Context.defaultContext());
        } else {
            return variableExpression.get(Context.defaultContext());
        }
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.VARIABLE;
    }
}
