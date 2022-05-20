package io.github.xmljim.json.factory.merge.strategy;

import io.github.xmljim.json.model.JsonObject;

/**
 * Functional interface for JsonObjects
 */
@FunctionalInterface
public interface ObjectConflictStrategy extends ConflictStrategy<JsonObject, String> {
}
