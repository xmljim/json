package io.github.xmljim.json.factory.merge;

import io.github.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.github.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.github.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;

/**
 * Builds the Merge Configuration and applies to a new instance of a MergeProcessor
 */
public interface MergeBuilder {

    /**
     * Set the {@link ArrayConflictStrategy} to apply to JSONArray values
     *
     * @param arrayConflictStrategy the strategy
     * @return The current MergeBuilder
     */
    MergeBuilder setArrayConflictStrategy(ArrayConflictStrategy arrayConflictStrategy);

    /**
     * Set the {@link ObjectConflictStrategy} to apply to JSONObject values
     *
     * @param objectConflictStrategy the strategy
     * @return the current MergeBuilder
     */
    MergeBuilder setObjectConflictStrategy(ObjectConflictStrategy objectConflictStrategy);

    /**
     * Set the {@link MergeResultStrategy} for returning the merged JsonNode
     *
     * @param mergeResultStrategy the strategy
     * @return the current MergeBuilder
     */
    MergeBuilder setMergeResultStrategy(MergeResultStrategy mergeResultStrategy);

    /**
     * Set the key value to append to a JsonObject key for Append conflict strategies (i.e., two keys with the same name)
     *
     * <p>
     * For example, if two JsonObjects have the same key, but non-equivalent values, and an Append conflict strategy
     * is applied, then primary value might retain the original key value, and the secondary value is renamed with
     * {@code [key] + [appendKey]}
     * </p>
     *
     * @param appendKey the key value to append to a given key.
     * @return the current MergeBuilder
     */
    MergeBuilder setMergeAppendKey(String appendKey);

    /**
     * Build a new MergeProcessor using the configuration settings
     *
     * @return a new MergeProcessor instance.
     */
    MergeProcessor build();
}
