package io.xmljim.json.merger;

import io.xmljim.json.factory.merge.MergeConfig;
import io.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;

class MergeConfigImpl implements MergeConfig {
    private ArrayConflictStrategy arrayConflictStrategy = ArrayConflictStrategies.APPEND;
    private ObjectConflictStrategy objectConflictStrategy = ObjectConflictStrategies.ACCEPT_PRIMARY;
    private MergeResultStrategy mergeResultStrategy = MergeResultStrategy.NEW_INSTANCE;
    private String mergeAppendKey = "_append";

    @Override
    public ArrayConflictStrategy getArrayConflictStrategy() {
        return arrayConflictStrategy;
    }

    protected void setArrayConflictStrategy(ArrayConflictStrategy strategy) {
        this.arrayConflictStrategy = strategy;
    }

    @Override
    public ObjectConflictStrategy getObjectConflictStrategy() {
        return objectConflictStrategy;
    }

    protected void setObjectConflictStrategy(ObjectConflictStrategy strategy) {
        this.objectConflictStrategy = strategy;
    }

    @Override
    public MergeResultStrategy getMergeResultStrategy() {
        return mergeResultStrategy;
    }

    protected void setMergeResultStrategy(MergeResultStrategy strategy) {
        this.mergeResultStrategy = strategy;
    }

    @Override
    public String getMergeAppendKey() {
        return mergeAppendKey;
    }

    protected void setMergeAppendKey(String appendKey) {
        this.mergeAppendKey = appendKey;
    }
}
