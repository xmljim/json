package io.xmljim.json.factory.merge;

import io.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;

public interface MergeConfig {

    ArrayConflictStrategy getArrayConflictStrategy();

    ObjectConflictStrategy getObjectConflictStrategy();

    MergeResultStrategy getMergeResultStrategy();

    String getMergeAppendKey();
}
