package io.xmljim.json.factory.merge;

import io.xmljim.json.service.JsonService;

public interface MergeFactory extends JsonService {

    MergeBuilder newMergeBuilder();

    default MergeProcessor newMergeProcessor() {
        return newMergeBuilder().build();
    }
}
