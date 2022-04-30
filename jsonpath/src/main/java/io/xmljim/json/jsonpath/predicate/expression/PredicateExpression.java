package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.NodeType;

import java.util.Optional;

/**
 * An expression value in a predicate
 */
public interface PredicateExpression {
    int size(Context inputContext);

    default boolean isEmpty(Context inputContext) {
        return size(inputContext) == 0;
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<T> getValue(Context inputContext) {
        Optional<Context> context = getContext(inputContext);
        return context.flatMap(value -> (Optional<T>) Optional.ofNullable(value.get().asJsonValue().get()));
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<T> getValueAt(Context inputContext, int index) {
        Optional<Context> context = getContextAt(inputContext, index);
        return context.flatMap(value -> (Optional<T>) Optional.of(value));
    }

    default Optional<Context> getContext(Context inputContext) {
        return isEmpty(inputContext) ? Optional.empty() : getContextAt(inputContext, 0);
    }

    Optional<Context> getContextAt(Context inputContext, int index);

    default NodeType getContextType(Context inputContext) {
        return getContextTypeAt(inputContext, 0);
    }

    default NodeType getContextTypeAt(Context inputContext, int index) {
        return getContextAt(inputContext, index).map(Context::type).orElse(NodeType.UNDEFINED);
    }

    /**
     * The expression type
     *
     * @return the expression type
     */
    ExpressionType type();

    String getExpression();
}
