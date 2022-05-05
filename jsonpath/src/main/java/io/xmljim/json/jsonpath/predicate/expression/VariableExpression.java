package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.variables.Global;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class VariableExpression extends AbstractExpression {
    private final String key;
    private final String path;
    private final Expression variableExpression;
    private FilterStream filterStream;

    public VariableExpression(String expression, Global global) {
        super(expression, global);
        Pattern pattern = Pattern.compile(ExpressionFactory.VARIABLE_PATTERN);
        Matcher matcher = pattern.matcher(expression);
        if (matcher.matches()) {
            this.key = matcher.group("key");
            this.path = matcher.group("path");
            this.variableExpression = global.getVariable(key);
            if (path != null) {
                this.filterStream = Compiler.newPathCompiler(path, global).compile();
            }

            if (variableExpression.type().isPrimitive()) {
                set(variableExpression.getContext(Context.defaultContext()));
            }

        } else {
            throw new JsonPathExpressionException(expression, 0, "Invalid Variable Expression");
        }
    }

    @Override
    public Optional<Context> getContext(Context inputContext) {
        return getContextAt(inputContext, 0);
    }


    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        Expression predicateExpression = getGlobal().getVariable(key);
        if (variableExpression.type() == ExpressionType.NODE && filterStream != null) {
            Context nodeContext = variableExpression.getContext(Context.defaultContext()).orElse(null);
            if (nodeContext != null) {
                set(filterStream.filter(Stream.of(nodeContext)));
            } else {
                return Optional.empty();
            }
        } else {
            return variableExpression.getContextAt(Context.defaultContext(), index);
        }

        return index < values().size() ? Optional.of(values().get(index)) : Optional.empty();
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.VARIABLE;
    }
}
