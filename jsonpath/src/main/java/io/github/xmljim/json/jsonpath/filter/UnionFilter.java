package io.github.xmljim.json.jsonpath.filter;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Stream;

class UnionFilter extends AbstractFilter {
    private final List<Accessor> accessors = new ArrayList<>();

    public UnionFilter(String expression, Global global) {
        super(expression, FilterType.UNION, global);

        StringTokenizer tokenizer = new StringTokenizer(expression, ",");
        while (tokenizer.hasMoreElements()) {
            accessors.add(Accessor.parse(tokenizer.nextToken().strip()));
        }
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return inputs.flatMap(this::apply);
    }

    @Override
    public Stream<Context> apply(Context input) {

        if (input.canTraverse()) {
            return accessors.stream().map(accessor -> getValue(input, accessor)).filter(Objects::nonNull);
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
