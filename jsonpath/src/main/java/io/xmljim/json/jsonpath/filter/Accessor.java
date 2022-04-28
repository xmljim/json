package io.xmljim.json.jsonpath.filter;

import java.util.Objects;

public final class Accessor {
    private final Object value;

    private Accessor(Object value) {
        assert Objects.nonNull(value);
        this.value = value;
    }

    public boolean isString() {
        return value instanceof String;
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {

        if (isString() && value != "$") {
            return "'" + value + "'";
        }

        return value.toString();
    }

    public static Accessor root() {
        return Accessor.of("$");
    }

    public static Accessor of(String value) {
        return new Accessor(value);
    }

    public static Accessor of(int value) {
        return new Accessor(value);
    }

    public static Accessor parse(String expression) {
        String stripBrackets = expression.replace("[", "").replace("]", "");
        if (isNumeric(stripBrackets)) {
            return of(Integer.parseInt(stripBrackets));
        } else {
            if (stripBrackets.startsWith("'") && stripBrackets.endsWith("'")) {
                return of(stripBrackets.substring(1, stripBrackets.length() - 1));
            }

            return of(expression);
        }
    }

    private static boolean isNumeric(String expression) {
        return expression.matches("-?[0-9]+");
    }


}
