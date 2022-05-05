package io.xmljim.json.jsonpath.function.info;

import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.function.JsonPathFunction;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;

import java.util.Arrays;

public record FunctionInfo(String name, String description, Class<? extends JsonPathFunction> functionClass,
                           ArgumentInfo... arguments) {
    public static FunctionInfo fromFunctionDefinition(FunctionDefinition functionDefinition, Class<? extends JsonPathFunction> fxClass) {
        return new FunctionInfo(functionDefinition.name(), functionDefinition.description(), fxClass,
            Arrays.stream(functionDefinition.args())
                .map(ArgumentInfo::fromArgumentDefinition)
                .toArray(ArgumentInfo[]::new));
    }

    @SuppressWarnings("unchecked")
    public static FunctionInfo fromJson(JsonObject functionRecord) {
        try {
            JsonArray array = functionRecord.get("arguments");
            return new FunctionInfo(String.valueOf(functionRecord.get("name")),
                String.valueOf(functionRecord.get("description")),
                (Class<? extends JsonPathFunction>) Class.forName(String.valueOf(functionRecord.get("functionClass"))),
                array.jsonValues()
                    .map(JsonElement::asJsonObject)
                    .map(ArgumentInfo::fromJson)
                    .toArray(ArgumentInfo[]::new)
            );

        } catch (ClassNotFoundException e) {
            throw new JsonPathException("No class found for function: " + e.getMessage());
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name()).append("(");
        builder.append(String.join(", ", Arrays.stream(arguments()).map(ArgumentInfo::toString).toArray(String[]::new)));
        builder.append(")");
        return builder.toString();
    }


}
