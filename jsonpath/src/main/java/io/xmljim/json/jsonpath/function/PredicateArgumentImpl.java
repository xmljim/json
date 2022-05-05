package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.context.Context;

import java.util.function.Predicate;

class PredicateArgumentImpl extends AbstractArgument<Predicate<Context>, Boolean> implements PredicateArgument {
    public PredicateArgumentImpl(String name, Predicate<Context> element) {
        super(name, element);
    }

    @Override
    public Boolean apply(Context context) {
        return element().test(context);
    }
}
