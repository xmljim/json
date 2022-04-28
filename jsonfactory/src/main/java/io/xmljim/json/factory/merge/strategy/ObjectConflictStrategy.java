package io.xmljim.json.factory.merge.strategy;

import io.xmljim.json.model.JsonObject;

/**
 * Functional interface for JsonObjects
 */
@FunctionalInterface
public interface ObjectConflictStrategy extends ConflictStrategy<JsonObject, String> {
}
