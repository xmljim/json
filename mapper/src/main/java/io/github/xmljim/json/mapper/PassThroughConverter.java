package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.Converter;

import java.util.Map;

class PassThroughConverter implements Converter {
    @Override
    @SuppressWarnings("unchecked")
    public <R, T> R convert(T value) {
        return (R) value;
    }

    @Override
    public Map<String, String> getArguments() {
        return null;
    }
}
