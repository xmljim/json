package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SliceFilter extends AbstractFilter {
    private final int start;
    private final int end;

    public SliceFilter(String expression, Global global) {
        super(expression, FilterType.SLICE, global);
        Pattern pattern = Pattern.compile("(?<start>\\-?\\d+)?:(?<end>\\-?\\d+)?");
        Matcher matcher = pattern.matcher(expression);
        if (matcher.matches()) {
            String startString = matcher.group("start");
            String endString = matcher.group("end");

            if (startString == null || "".equals(startString)) {
                this.start = 0;
            } else {
                this.start = Integer.parseInt(startString.strip());
            }

            if (endString == null || "".equals(endString)) {
                this.end = -1; //the tail
            } else {
                this.end = Integer.parseInt(endString.strip());
            }


        } else {
            throw new JsonPathExpressionException(expression, 0, "Invalid Slice Expression");
        }
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return inputs.flatMap(this::apply);
    }

    @Override
    public Stream<Context> apply(Context input) {
        if (input.canTraverse()) {
            int begin = start < 0 ? (input.childLength() + start) : start;
            int finish = end < 0 ? (input.childLength() + end) + 1 : end;


            return IntStream.range(begin, finish).mapToObj(index -> getValue(input, Accessor.of(index))).filter(Objects::nonNull);
        }

        return Stream.empty();
    }

    private Context getValue(Context input, Accessor accessor) {
        Context newContext = null;
        if (input.type().isArray()) {
            JsonArray array = input.get().asJsonArray();
            int index = accessor.get();
            newContext = array.value(index).map(val -> Context.create(val, input, accessor)).orElse(null);
        } else if (input.type().isObject()) {
            JsonObject object = input.get().asJsonObject();
            String key = accessor.get();
            newContext = object.value(key).map(val -> Context.create(val, input, accessor)).orElse(null);
        }

        return newContext;
    }
}
