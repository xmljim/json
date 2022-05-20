package io.github.xmljim.json.merger;

import io.github.xmljim.json.factory.merge.MergeProcessor;
import io.github.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;

public enum ArrayConflictStrategies implements ArrayConflictStrategy {
    INSERT_BEFORE {
        @Override
        public void apply(JsonArray context, Integer propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor) {
            if (primaryValue.isEquivalent(secondaryValue)) {
                context.add(primaryValue);
            } else {
                if (primaryValue.type().equals(secondaryValue.type())) {
                    if (primaryValue.type().isPrimitive()) {
                        context.add(secondaryValue);
                        context.add(primaryValue);
                    } else if (primaryValue.type().isObject()) {
                        JsonObject mergedObject = processor.merge((JsonObject) primaryValue.get(), (JsonObject) secondaryValue.get());
                        context.add(mergedObject);
                    } else {
                        JsonArray mergedArray = processor.merge((JsonArray) primaryValue.get(), (JsonArray) secondaryValue.get());
                        context.add(mergedArray);
                    }
                } else {
                    context.add(primaryValue);
                    context.add(secondaryValue);
                }
            }
        }
    },

    INSERT_AFTER {
        @Override
        public void apply(JsonArray context, Integer propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor) {
            if (primaryValue.isEquivalent(secondaryValue)) {
                context.add(primaryValue);
            } else {
                if (primaryValue.type().equals(secondaryValue.type())) {
                    if (primaryValue.type().isPrimitive()) {
                        context.add(primaryValue);
                        context.add(secondaryValue);
                    } else if (primaryValue.type().isObject()) {
                        context.add(processor.merge((JsonObject) primaryValue.get(), (JsonObject) secondaryValue.get()));
                    } else {
                        context.add(processor.merge((JsonArray) primaryValue.get(), (JsonArray) secondaryValue.get()));
                    }
                } else {
                    context.add(primaryValue);
                    context.add(secondaryValue);
                }
            }
        }
    },

    APPEND {
        @Override
        public void apply(JsonArray context, Integer propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor) {
            if (primaryValue.isEquivalent(secondaryValue)) {
                context.add(primaryValue);
            } else {
                if (primaryValue.type().equals(secondaryValue.type())) {
                    if (primaryValue.type().isPrimitive()) {
                        if (propertyValue == 0 || propertyValue == context.size()) {
                            context.add(primaryValue);
                            context.add(secondaryValue);
                        } else {
                            context.insert(propertyValue, primaryValue);
                            context.add(secondaryValue);
                        }
                    } else if (primaryValue.type().isObject()) {
                        context.add(processor.merge((JsonObject) primaryValue.get(), (JsonObject) secondaryValue.get()));
                    } else {
                        context.add(processor.merge((JsonArray) primaryValue.get(), (JsonArray) secondaryValue.get()));
                    }
                } else if (propertyValue == 0 || propertyValue == context.size()) {
                    context.add(primaryValue);
                    context.add(secondaryValue);
                } else {
                    context.insert(propertyValue, primaryValue);
                    context.add(secondaryValue);
                }
            }
        }
    },

    DEDUPLICATE {
        @Override
        public void apply(JsonArray context, Integer propertyValue, JsonValue<?> primaryValue, JsonValue<?> secondaryValue, MergeProcessor processor) {
            if (primaryValue.isEquivalent(secondaryValue)) {
                context.add(primaryValue);
            } else {
                if (primaryValue.type().equals(secondaryValue.type())) {
                    if (primaryValue.type().isPrimitive()) {
                        optAppend(context, propertyValue, primaryValue, true);
                        optAppend(context, propertyValue, secondaryValue, false);
                    } else if (primaryValue.type().isObject()) {
                        context.add(processor.merge((JsonObject) primaryValue.get(), (JsonObject) secondaryValue.get()));
                    } else {
                        context.add(processor.merge((JsonArray) primaryValue.get(), (JsonArray) secondaryValue.get()));
                    }
                } else {
                    optAppend(context, propertyValue, primaryValue, true);
                    optAppend(context, propertyValue, secondaryValue, false);
                }
            }
        }

        private void optAppend(JsonArray context, int index, JsonValue<?> value, boolean isPrimary) {
            boolean match = context.jsonValues().anyMatch(value::isEquivalent);

            if (!match) {
                context.add(value);
            }
        }
    }

}
