package io.github.xmljim.json.factory.mapper;


import java.util.Map;

public interface Converter {

    <R, T> R convert(T value);

    Map<String, String> getArguments();
}
