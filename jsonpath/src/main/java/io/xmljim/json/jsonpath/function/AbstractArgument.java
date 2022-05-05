package io.xmljim.json.jsonpath.function;

abstract class AbstractArgument<E, R> implements Argument<E, R> {
    private final String name;
    private final E element;

    public AbstractArgument(String name, E element) {
        this.name = name;
        this.element = element;
    }

    public String name() {
        return name;
    }

    public E element() {
        return element;
    }
}
