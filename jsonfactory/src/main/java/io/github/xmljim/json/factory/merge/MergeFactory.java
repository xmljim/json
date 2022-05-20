package io.github.xmljim.json.factory.merge;

import io.github.xmljim.json.service.JsonService;

public interface MergeFactory extends JsonService {

    MergeBuilder newMergeBuilder();

    default MergeProcessor newMergeProcessor() {
        return newMergeBuilder().build();
    }
}
