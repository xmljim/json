package io.github.xmljim.json.jsonpath.function;

import io.github.xmljim.json.jsonpath.JsonPathException;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.function.info.FunctionInfo;
import io.github.xmljim.json.model.JsonObject;

import java.util.*;
import java.util.stream.IntStream;

public class FunctionRegistry {

    private final Map<String, FunctionInfo> registry = new HashMap<>();

    public void registerFunction(FunctionInfo functionInfo) {
        registry.put(functionInfo.name(), functionInfo);
    }

    public void registerFunction(Class<? extends JsonPathFunction> functionClass) {
        FunctionInfo functionInfo = getFunctionInfo(functionClass)
            .orElseThrow(() -> new JsonPathException("Cannot register function. No function information supplied"));
        registerFunction(functionInfo);
    }

    public void registerFunction(JsonObject functionRecord) {
        registerFunction(FunctionInfo.fromJson(functionRecord));
    }

    public boolean containsFunction(String functionName) {
        return registry.containsKey(functionName);
    }

    public Optional<FunctionInfo> getFunctionInfo(String name) {
        return Optional.ofNullable(registry.get(name));
    }

    public Optional<FunctionInfo> getFunctionInfo(Class<? extends JsonPathFunction> function) {

        if (function.isAnnotationPresent(FunctionDefinition.class)) {
            FunctionDefinition functionDefinition = function.getAnnotation(FunctionDefinition.class);
            return Optional.of(FunctionInfo.fromFunctionDefinition(functionDefinition, function));
        }

        return Optional.empty();
    }

    public List<FunctionInfo> getRegisteredFunctions() {
        return registry.values().stream().toList();
    }

    public String printFunctionList() {
        StringBuilder builder = new StringBuilder();

        int max = getRegisteredFunctions().stream().mapToInt(fi -> fi.toString().length()).max().orElse(0);
        int padding = max + 4;

        getRegisteredFunctions()
            .stream().sorted(Comparator.comparing(FunctionInfo::toString))
            .forEach(fi -> {
                String signature = fi.toString();
                int length = signature.length();
                int paddingSize = padding - length;
                builder.append(signature).append(pad(paddingSize)).append(fi.description()).append(System.lineSeparator());
            });

        return builder.toString();
    }

    private String pad(int len) {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, len).forEach(i -> builder.append(" "));
        return builder.toString();
    }


}
