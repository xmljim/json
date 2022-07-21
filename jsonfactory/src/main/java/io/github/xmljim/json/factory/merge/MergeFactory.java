package io.github.xmljim.json.factory.merge;

import io.github.xmljim.json.service.JsonService;

/**
 * Factory/Service for Json Merging
 */
public interface MergeFactory extends JsonService {

    /**
     * Create a new MergeBuilder for configuring a Merge
     *
     * @return a new MergeBuilder
     */
    MergeBuilder newMergeBuilder();

    /**
     * Create a default MergeProcessor
     *
     * @return a new MergeProcess with default configuration
     */
    default MergeProcessor newMergeProcessor() {
        return newMergeBuilder().build();
    }
}
