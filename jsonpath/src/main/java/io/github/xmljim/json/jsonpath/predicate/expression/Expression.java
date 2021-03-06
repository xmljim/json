package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.DataType;

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

    default DataType getContextType(Context inputContext) {
        return getContextTypeAt(inputContext, 0);
    }

    default DataType getContextTypeAt(Context inputContext, int index) {
        return getContextAt(inputContext, index).map(Context::type).orElse(DataType.UNDEFINED);
    }

    @SuppressWarnings("unchecked")
    default <T> T get(Context context) {
        return (T) getValue(context).orElse(null);
    }

    @SuppressWarnings("unchecked")
    default <T> T get(Context context, int index) {
        return (T) getValueAt(context, index).orElse(null);
    }

    /**
     * The expression argType
     *
     * @return the expression argType
     */
    DataType type();

    String getExpression();
}
