package io.xmljim.json.jsonpath.variables;

import io.xmljim.json.jsonpath.function.JsonPathFunction;
import io.xmljim.json.jsonpath.function.aggregate.AvgFunction;
import io.xmljim.json.jsonpath.function.aggregate.CountFunction;
import io.xmljim.json.jsonpath.function.aggregate.SumFunction;
import io.xmljim.json.jsonpath.function.node.DistinctValuesFunction;
import io.xmljim.json.jsonpath.function.node.IsArrayFunction;
import io.xmljim.json.jsonpath.function.node.IsBooleanFunction;
import io.xmljim.json.jsonpath.function.node.IsNullFunction;
import io.xmljim.json.jsonpath.function.node.IsNumericFunction;
import io.xmljim.json.jsonpath.function.node.IsObjectFunction;
import io.xmljim.json.jsonpath.function.node.IsPrimitiveFunction;
import io.xmljim.json.jsonpath.function.node.IsStringFunction;
import io.xmljim.json.jsonpath.function.node.KeysFunction;
import io.xmljim.json.jsonpath.function.node.LengthFunction;
import io.xmljim.json.jsonpath.function.node.NodeTypeFunction;
import io.xmljim.json.jsonpath.function.node.ValuesFunction;
import io.xmljim.json.jsonpath.function.predicate.CountIfFunction;
import io.xmljim.json.jsonpath.function.predicate.SumIfFunction;
import io.xmljim.json.jsonpath.function.string.ConcatFunction;
import io.xmljim.json.jsonpath.function.string.EndsWithFunction;
import io.xmljim.json.jsonpath.function.string.LowerCaseFunction;
import io.xmljim.json.jsonpath.function.string.ReplaceFunction;
import io.xmljim.json.jsonpath.function.string.SplitFunction;
import io.xmljim.json.jsonpath.function.string.StartsWithFunction;
import io.xmljim.json.jsonpath.function.string.SubstringFunction;
import io.xmljim.json.jsonpath.function.string.TrimFunction;
import io.xmljim.json.jsonpath.function.string.UpperCaseFunction;

import java.util.Arrays;

public enum BuiltIns {
    TYPE("type", "Returns the node argType for each element in context",
        "path", "node", NodeTypeFunction.class),
    IS_PRIMITIVE("is-primitive", "returns if the node argType for each context element is a primitive argType",
        "path", "node", IsPrimitiveFunction.class),
    IS_NUMERIC("is-numeric", "Evaluates whether the context element is a numeric value",
        "path", "node", IsNumericFunction.class),
    IS_STRING("is-string", "returns true if the node argType is a string value",
        "path", "node", IsStringFunction.class),
    IS_BOOLEAN("is-boolean", "returns true if the node argType is a boolean value",
        "path", "node", IsBooleanFunction.class),
    IS_ARRAY("is-array", "return true if the node argType is an array",
        "path", "node", IsArrayFunction.class),
    IS_OBJECT("is-object", "returns true if the node argType in context is an object",
        "path", "node", IsObjectFunction.class),
    IS_NULL("is-null", "returns true if the node argType value is null",
        "path", "node", IsNullFunction.class),
    AVERAGE("average", "returns the average of all numeric values in context",
        "path", "aggregate", AvgFunction.class),
    SUM("sum", "returns the sum of all numeric values in context",
        "path", "aggregate", SumFunction.class),
    COUNT("count", "returns a count of all values in context",
        "path", "aggregate", CountFunction.class),
    SUBSTRING("substring", "returns a substring for string elements in context",
        "path", "string", SubstringFunction.class),
    UPPERCASE("uppercase", "converts all string elements in context to uppercase",
        "path", "string", UpperCaseFunction.class),
    LENGTH("length", "returns the lenghth/size of an array, object or string node; returns -1 for all other node types",
        "path", "node", LengthFunction.class),
    KEYS("keys", "returns keys for JsonObject instances; all other types return an emtpy array",
        "path", "node", KeysFunction.class),
    VALUES("values", "returns an array of values for each context element",
        "path", "node", ValuesFunction.class),
    DISTINCT_VALUES("distinct-values", "returns an array of distinct values for each context element",
        "path", "node", DistinctValuesFunction.class),
    CONCAT("concat", "Concatenates a string from a set of expressions",
        "path", "string", ConcatFunction.class),
    COUNT_IF("count-if", "returns a count of values based on a predicate",
        "predicate", "aggregate", CountIfFunction.class),
    SUM_IF("sum-if", "returns a sum of values based on a predicate",
        "predicate", "aggregate", SumIfFunction.class),
    LOWERCASE("lowercase", "converts all string values in context to lowercase",
        "path", "string", LowerCaseFunction.class),
    TRIM("trim", "removes all surrounding whitespace from all string values in context",
        "path", "string", TrimFunction.class),
    STARTS_WITH("starts-with", "evaluates a string in context for matching the start characters",
        "path", "string", StartsWithFunction.class),
    ENDS_WITH("ends-with", "evaluates a string in context for matching the end characters",
        "path", "string", EndsWithFunction.class),
    REPLACE("replace", "replaces string match values with replacement string for all context elements",
        "path", "string", ReplaceFunction.class),
    SPLIT("split", "splits a string into an array of strings from a match expression",
        "path", "string", SplitFunction.class),
    UNDEFINED("UNDEFINED", "", "", "", null),
    ;
    private final String functionName;
    private final Class<? extends JsonPathFunction> functionClass;

    private final String category;
    private final String classification;

    private final String description;

    BuiltIns(String functionName, String description, String category, String classification, Class<? extends JsonPathFunction> functionClass) {
        this.functionName = functionName;
        this.description = description;
        this.category = category;
        this.classification = classification;
        this.functionClass = functionClass;
    }

    public String functionName() {
        return functionName;
    }

    public String category() {
        return category;
    }

    public String classification() {
        return classification;
    }

    public String description() {
        return description;
    }

    public Class<? extends JsonPathFunction> functionClass() {
        return functionClass;
    }

    public static boolean isBuiltIn(String functionName, Class<? extends JsonPathFunction> functionClass) {
        return Arrays.stream(values()).anyMatch(v -> v.functionName().equals(functionName) && v.functionClass().equals(functionClass));
    }
}
