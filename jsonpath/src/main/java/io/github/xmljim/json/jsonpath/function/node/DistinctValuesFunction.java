package io.github.xmljim.json.jsonpath.function.node;

import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.DISTINCT_VALUES)
public class DistinctValuesFunction extends AbstractJsonPathFunction {
    public DistinctValuesFunction(Global global) {
        super(BuiltIns.DISTINCT_VALUES, Collections.emptyList(), global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        /*
        JsonArray array = ServiceManager.getProvider(ElementFactory.class).newArray();

        contextStream.forEach(context -> {

            switch (context.type()) {
                case ARRAY -> vals.addAll((Collection<?>) context.array().jsonValues().distinct());
                case OBJECT -> vals.addAll((Collection<?>) context.object().values().distinct());
                default -> vals.add(context.value());
            }
        });
        */


        Set<Context> vals = new HashSet<>();
        contextStream.forEach(vals::add);
        return vals.stream();

    }
}
