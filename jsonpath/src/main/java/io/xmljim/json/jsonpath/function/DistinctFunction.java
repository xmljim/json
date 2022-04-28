package io.xmljim.json.jsonpath.function;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.service.ServiceManager;

import java.util.Collections;
import java.util.List;

public class DistinctFunction implements ContextFunction {
    @Override
    public Context apply(Context context) {
        ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);
        JsonArray distinctList = elementFactory.newArray();

        context.stream().map(Context::get).distinct().forEach(distinctList::add);
        return Context.create(distinctList);
    }


    @Override
    public List<PredicateExpression> args() {
        return Collections.emptyList();
    }

    @Override
    public String name() {
        return "distinct";
    }
}
