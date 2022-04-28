package io.xmljim.json.factory.merge;


import io.xmljim.json.model.JsonNode;

/**
 * Orchestration interface for merging
 */
public interface MergeProcessor {
    /**
     * The merge configuration for this process
     *
     * @return the merge configuration
     */
    MergeConfig getConfiguration();

    /**
     * Merge the primary node with the secondary node
     *
     * @param primary   the primary node
     * @param secondary the secondary node
     * @param <T>       The JsonNode subinterface (JsonObject or JsonArray)
     * @return The merged JsonNode
     */
    <T extends JsonNode> T merge(T primary, T secondary);
}
