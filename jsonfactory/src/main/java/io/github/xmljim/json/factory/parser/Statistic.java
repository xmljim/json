package io.github.xmljim.json.factory.parser;

public interface Statistic<T extends Number> {

    String getComponent();

    String getName();

    T getValue();
}
