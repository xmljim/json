package io.github.xmljim.json.factory.merge.strategy;

import io.github.xmljim.json.model.JsonArray;

/**
 * Functional interface for handling JsonArray conflicts
 */
@FunctionalInterface
public interface ArrayConflictStrategy extends ConflictStrategy<JsonArray, Integer> {
}
