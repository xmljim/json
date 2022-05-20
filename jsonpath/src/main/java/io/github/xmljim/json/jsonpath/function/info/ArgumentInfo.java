package io.github.xmljim.json.jsonpath.function.info;

import io.github.xmljim.json.jsonpath.JsonPathException;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.model.JsonObject;

public record ArgumentInfo(String name, ArgumentScope scope, String description, Class<? extends Argument<?, ?>> type) {

    public static ArgumentInfo fromArgumentDefinition(ArgumentDefinition definition) {
        return new ArgumentInfo(definition.name(), definition.scope(), definition.description(), definition.argType());
    }

    @SuppressWarnings("unchecked")
    public static ArgumentInfo fromJson(JsonObject record) {
        try {
            return new ArgumentInfo(String.valueOf(record.get("name")),
                ArgumentScope.valueOf(String.valueOf(record.get("scope"))),
                String.valueOf(record.get("description")),
                (Class<? extends Argument<?, ?>>) Class.forName(String.valueOf(record.get("typeClass")))
            );
        } catch (ClassNotFoundException e) {
            throw new JsonPathException("Argument argType not found: " + e.getMessage());
        }

    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        switch (scope()) {
            case REQUIRED -> builder.append(name());
            case OPTIONAL -> builder.append("[").append(name()).append("]");
            case VARARGS -> builder.append("[").append(name()).append("...").append("]");
        }

        return builder.toString();
    }
}
