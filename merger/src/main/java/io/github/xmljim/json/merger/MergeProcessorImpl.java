package io.github.xmljim.json.merger;

import io.github.xmljim.json.factory.merge.MergeConfig;
import io.github.xmljim.json.factory.merge.MergeProcessor;
import io.github.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonNode;
import io.github.xmljim.json.model.JsonObject;

class MergeProcessorImpl implements MergeProcessor {
    private final MergeConfig mergeConfig;

    protected MergeProcessorImpl(MergeConfig mergeConfig) {
        this.mergeConfig = mergeConfig;
    }

    @Override
    public MergeConfig getConfiguration() {
        return mergeConfig;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonNode> T merge(T primary, T secondary) {

        T result = null;

        if (primary.type().isObject()) {
            result = (T) MergeOperations.objectMerge.merge((JsonObject) primary, (JsonObject) secondary,
                getConfiguration().getObjectConflictStrategy(), this);
        } else {
            result = (T) MergeOperations.arrayMerge.merge((JsonArray) primary, (JsonArray) secondary,
                getConfiguration().getArrayConflictStrategy(), this);
        }

        if (getConfiguration().getMergeResultStrategy() == MergeResultStrategy.NEW_INSTANCE) {
            primary = result;
            return primary;
        } else {
            return result;
        }
    }
}
