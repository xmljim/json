package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.service.ServiceManager;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.KEYS)
public class KeysFunction extends AbstractJsonPathFunction {

    public KeysFunction() {
        super(BuiltIns.KEYS.functionName(), Collections.emptyList());
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
