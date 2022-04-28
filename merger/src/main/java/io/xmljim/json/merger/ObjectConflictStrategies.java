package io.xmljim.json.merger;

import io.xmljim.json.factory.merge.MergeProcessor;
import io.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.model.JsonValue;

public enum ObjectConflictStrategies implements ObjectConflictStrategy {
    ACCEPT_PRIMARY {
        @Override
        public void apply(JsonObject context, String propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor) {
            if (primaryValue.isEquivalent(secondaryValue)) {
                context.put(propertyValue, primaryValue);
            } else {
                if (primaryValue.type().equals(secondaryValue.type())) {
                    if (primaryValue.type().isPrimitive()) {
                        context.put(propertyValue, primaryValue);
                    } else if (primaryValue.type().isObject()) {
                        context.put(propertyValue, processor.merge((JsonObject) primaryValue.get(), (JsonObject) secondaryValue.get()));
                    } else {
                        context.put(propertyValue, processor.merge((JsonArray) primaryValue.get(), (JsonArray) secondaryValue.get()));
                    }
                } else {
                    context.put(propertyValue, primaryValue);
                }
            }
        }
    },

    ACCEPT_SECONDARY {
        @Override
        public void apply(JsonObject context, String propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor) {
            if (primaryValue.isEquivalent(secondaryValue)) {
                if (primaryValue.type().isPrimitive()) {
                    context.put(propertyValue, secondaryValue);
                } else if (primaryValue.type().isObject()) {
                    context.put(propertyValue, processor.merge((JsonObject) primaryValue.get(), (JsonObject) secondaryValue.get()));
                } else {
                    context.put(propertyValue, processor.merge((JsonArray) primaryValue.get(), (JsonArray) secondaryValue.get()));
                }
            } else {
                context.put(propertyValue, secondaryValue);
            }
        }
    },

    APPEND {
        @Override
        public void apply(JsonObject context, String propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor) {
            if (primaryValue.isEquivalent(secondaryValue)) {
                context.put(propertyValue, primaryValue);
            } else {
                if (primaryValue.type().equals(secondaryValue.type())) {
                    if (primaryValue.type().isPrimitive()) {
                        String key = propertyValue + processor.getConfiguration().getMergeAppendKey();
                        context.put(key, secondaryValue);
                        context.put(propertyValue, primaryValue);
                    } else if (primaryValue.type().isObject()) {
                        context.put(propertyValue, processor.merge((JsonObject) primaryValue.get(), (JsonObject) secondaryValue.get()));
                    } else {
                        context.put(propertyValue, processor.merge((JsonArray) primaryValue.get(), (JsonArray) secondaryValue.get()));
                    }
                } else {
                    String key = propertyValue + processor.getConfiguration().getMergeAppendKey();
                    context.put(key, secondaryValue);
                    context.put(propertyValue, primaryValue);
                }
            }
        }
    }
}
