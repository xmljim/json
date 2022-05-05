package io.xmljim.json.jsonpath.function;


import java.util.List;

public abstract class AbstractJsonPathFunction implements JsonPathFunction {
    private final String name;
    private final List<Argument<?, ?>> args;


    public AbstractJsonPathFunction(String name, List<Argument<?, ?>> arguments) {
        this.name = name;
        this.args = arguments;
    }

    public String name() {
        return name;
    }

    @Override
    public List<Argument<?, ?>> arguments() {
        return args;
    }
}
