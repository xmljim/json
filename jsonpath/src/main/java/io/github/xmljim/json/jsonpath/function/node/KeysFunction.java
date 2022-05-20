package io.github.xmljim.json.jsonpath.function.node;

import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.service.ServiceManager;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.KEYS)
public class KeysFunction extends AbstractJsonPathFunction {

    public KeysFunction(Global global) {
        super(BuiltIns.KEYS.functionName(), Collections.emptyList(), global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> {
            JsonArray array = ServiceManager.getProvider(ElementFactory.class).newArray();
            if (context.type().isObject()) {
                array.addAll(context.object().keys());
            }
            return Context.createSimpleContext(array);
        });
    }
}
