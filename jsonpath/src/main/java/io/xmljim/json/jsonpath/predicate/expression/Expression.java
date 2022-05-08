package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.NodeType;

import java.util.List;
import java.util.Optional;

/**
 * An expression value in a predicate or a function argument
 */
public interface Expression {
    int size(Context inputContext);

    default boolean isEmpty(Context inputContext) {
        return size(inputContext) == 0;
    }

    default <T> Optional<T> getValue(Context inputContext) {
        Optional<Context> context = getContext(inputContext);
        return context.flatMap(value -> Optional.ofNullable(value.value()));
    }

    List<Context> values(Context inputContext);


    default <T> Optional<T> getValueAt(Context inputContext, int index) {
        Optional<Context> context = getContextAt(inputContext, index);
        return context.flatMap(value -> Optional.ofNullable(value.value()));
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
