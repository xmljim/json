package io.github.xmljim.json.factory.merge;

import io.github.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.github.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.github.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;

public interface MergeConfig {

    ArrayConflictStrategy getArrayConflictStrategy();

    ObjectConflictStrategy getObjectConflictStrategy();

    MergeResultStrategy getMergeResultStrategy();

    String getMergeAppendKey();
}
