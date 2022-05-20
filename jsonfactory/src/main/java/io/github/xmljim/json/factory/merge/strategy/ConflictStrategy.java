package io.github.xmljim.json.factory.merge.strategy;

import io.github.xmljim.json.factory.merge.MergeOperation;
import io.github.xmljim.json.factory.merge.MergeProcessor;
import io.github.xmljim.json.model.JsonValue;

/**
 * Functional interface for all Conflict Strategies.  Conflicts occur when a pair of values have the same
 * accessor property, but their values are not equivalent. The underlying strategy provides the
 * logic for handling this during the {@link MergeOperation}
 *
 * @param <T> The underlying JsonNode type for the context
 * @param <G> The accessor property type;
 */
@FunctionalInterface
public interface ConflictStrategy<T, G> {
    /**
     * Apply a conflict strategy to a pair of merging nodes and resolve to the context
     *
     * @param context        The merge context node
     * @param propertyValue  the property accessor (int for a JsonArray index, String for JsonObject key)
     * @param primaryValue   the primary value to merge
     * @param secondaryValue the secondary value to merge
     * @param processor      the {@link MergeProcessor}
     */
    void apply(T context, G propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor);
}
