package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.KeyNameCase;
import io.github.xmljim.json.factory.mapper.MapperConfig;
import io.github.xmljim.json.factory.mapper.ValueConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class MapperConfigImpl implements MapperConfig {
    private Class<?> targetClass;
    private ValueConverter<?> valueConverter;
    private KeyNameCase keyNameCase = KeyNameCase.DEFAULT;
    private final Set<String> ignoreKeys = new HashSet<>();

    @Override
    public Optional<Class<?>> getTargetClass() {
        return Optional.ofNullable(targetClass);
    }

    protected void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public Optional<ValueConverter<?>> getValueConverter() {
        return Optional.ofNullable(valueConverter);
    }

    protected void setValueConverter(ValueConverter<?> valueConverter) {
        this.valueConverter = valueConverter;
    }

    @Override
    public KeyNameCase getKeyNameCase() {
        return keyNameCase;
    }

    protected void setKeyNameCase(KeyNameCase keyNameCase) {
        this.keyNameCase = keyNameCase;
    }

    @Override
    public Set<String> getIgnoreKeys() {
        return ignoreKeys;
    }

    protected void setIgnoreKeys(Collection<String> ignoreKeys) {
        this.ignoreKeys.addAll(ignoreKeys);
    }
}
