package io.github.xmljim.json.factory.merge;

import io.github.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.github.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.github.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;

/**
 * Merge configuration
 */
public interface MergeConfig {

    /**
     * Return the Array Conflict Strategy
     *
     * @return the Array Conflict Strategy
     */
    ArrayConflictStrategy getArrayConflictStrategy();

    /**
     * Return the Object Conflict Strategy
     *
     * @return the Object Conflict Strategy
     */
    ObjectConflictStrategy getObjectConflictStrategy();

    /**
     * Return the MergeResultStrategy
     *
     * @return the MergeResultStrategy
     */
    MergeResultStrategy getMergeResultStrategy();

    /**
     * Return the key string to append to conflicted object keys in an Append strategy
     *
     * @return the key string to append
     */
    String getMergeAppendKey();
}
