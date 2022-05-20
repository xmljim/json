package io.github.xmljim.json.factory.merge;

import io.github.xmljim.json.factory.merge.strategy.ConflictStrategy;
import io.github.xmljim.json.model.JsonNode;

/**
 * A merge operation is the workhorse of the merge process. This handle the merging of
 * JSON nodes
 *
 * @param <T> The JsonNode type (JsonArray or JsonObject)
 * @param <G> The property accessor type, e.g., an integer for JsonArray items, and string for JsonObject keys
 */
@FunctionalInterface
public interface MergeOperation<T extends JsonNode, G> {
    /**
     * Merge two JsonNode instances of the same type
     *
     * @param primary          the primary node
     * @param secondary        the secondary node
     * @param conflictStrategy the underlying conflict strategy to apply
     * @param processor        the {@link MergeProcessor}
     * @return The merged node
     */
    T merge(T primary, T secondary, ConflictStrategy<T, G> conflictStrategy, MergeProcessor processor);
}
