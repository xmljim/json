package io.github.xmljim.json.factory.merge.strategy;

/**
 * Specifies the output of a merge operation
 */
public enum MergeResultStrategy {
    /**
     * Merge directly to the primary instance.
     * <strong>WARNING</strong>: The original primary data will be lost. You've been warned, use this if you
     * know what you're doing.
     */
    MERGE_PRIMARY,
    /**
     * Return a JSONNode instance containing the merge between the primary and secondary JSON instance
     */
    NEW_INSTANCE
}
