package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.variables.Global;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

abstract class CachedExpression extends AbstractExpression {
    private final ConcurrentHashMap<String, List<Context>> concurrentHashMap = new ConcurrentHashMap<>();

    public CachedExpression(String expression, Global global) {
        super(expression, global);
    }

    @Override
    public List<Context> values(Context inputContext) {
        String key = getExpression() + "_" + inputContext.toString();
        if (!concurrentHashMap.containsKey(key)) {
            applyContext(inputContext);
        }
        //System.out.println("Get Cache [" + key + "]");
        return concurrentHashMap.getOrDefault(key, Collections.emptyList());
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        return index < size(inputContext) ? Optional.of(values(inputContext).get(index)) : Optional.empty();
    }

    @Override
    public int size(Context inputContext) {
        //System.out.println("size [" + getExpression() + "]: " + inputContext);
        return values(inputContext).size();
    }

    public void cache(Context inputContext, List<Context> values) {
        String key = getExpression() + "_" + inputContext.toString();
        concurrentHashMap.putIfAbsent(key, values);
        //System.out.println("Cached [" + key + "]: " + values);
    }

    public boolean isCached(Context inputContext) {
        String key = getExpression() + "_" + inputContext.toString();
        return concurrentHashMap.containsKey(key);
    }


    public abstract void applyContext(Context inputContext);

}
