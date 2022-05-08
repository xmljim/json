package io.xmljim.json.jsonpath.function.info;

import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.function.JsonPathFunction;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;

import java.util.Arrays;

public record FunctionInfo(String name, String description, String category, String classification,
                           Class<? extends JsonPathFunction> functionClass,
                           ArgumentInfo... arguments) {
    public static FunctionInfo fromBuiltIn(BuiltIns builtin, ArgumentInfo... arguments) {
        return new FunctionInfo(builtin.functionName(), builtin.description(), builtin.category(), builtin.classification(), builtin.functionClass(), arguments);
    }

    public static FunctionInfo fromFunctionDefinition(FunctionDefinition functionDefinition, Class<? extends JsonPathFunction> fxClass, ArgumentInfo... arguments) {
        if (functionDefinition.builtIn() != BuiltIns.UNDEFINED) {
            return FunctionInfo.fromBuiltIn(functionDefinition.builtIn(),
                Arrays.stream(functionDefinition.args())
                    .map(ArgumentInfo::fromArgumentDefinition)
                    .toArray(ArgumentInfo[]::new));
        }

        return new FunctionInfo(functionDefinition.name(), functionDefinition.description(), functionDefinition.category(), functionDefinition.classification(), fxClass,
            Arrays.stream(functionDefinition.args())
                .map(ArgumentInfo::fromArgumentDefinition)
                .toArray(ArgumentInfo[]::new));
    }

    @SuppressWarnings("unchecked")
    public static FunctionInfo fromJson(JsonObject functionRecord) {
        try {
            JsonArray array = functionRecord.get("arguments");
            return new FunctionInfo(
                String.valueOf(functionRecord.get("name")),
                String.valueOf(functionRecord.get("description")),
                String.valueOf(functionRecord.get("category")),
                String.valueOf(functionRecord.get("classification")),
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

    public boolean isBuiltIn() {
        return BuiltIns.isBuiltIn(name(), functionClass());
    }


}
