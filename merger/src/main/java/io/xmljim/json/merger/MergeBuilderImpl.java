package io.xmljim.json.merger;

import io.xmljim.json.factory.merge.MergeBuilder;
import io.xmljim.json.factory.merge.MergeProcessor;
import io.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;

class MergeBuilderImpl implements MergeBuilder {
    private final MergeConfigImpl config = new MergeConfigImpl();

    @Override
    public MergeBuilder setArrayConflictStrategy(ArrayConflictStrategy arrayConflictStrategy) {
        config.setArrayConflictStrategy(arrayConflictStrategy);
        return this;
    }

    @Override
    public MergeBuilder setObjectConflictStrategy(ObjectConflictStrategy objectConflictStrategy) {
        config.setObjectConflictStrategy(objectConflictStrategy);
        return this;
    }

    @Override
    public MergeBuilder setMergeResultStrategy(MergeResultStrategy mergeResultStrategy) {
        config.setMergeResultStrategy(mergeResultStrategy);
        return this;
    }

    @Override
    public MergeBuilder setMergeAppendKey(String appendKey) {
        config.setMergeAppendKey(appendKey);
        return this;
    }

    @Override
    public MergeProcessor build() {
        return new MergeProcessorImpl(config);
    }
}
