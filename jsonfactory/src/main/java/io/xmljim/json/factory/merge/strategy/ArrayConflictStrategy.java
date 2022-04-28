package io.xmljim.json.factory.merge.strategy;

import io.xmljim.json.model.JsonArray;

/**
 * Functional interface for handling JsonArray conflicts
 */
@FunctionalInterface
public interface ArrayConflictStrategy extends ConflictStrategy<JsonArray, Integer> {
}
