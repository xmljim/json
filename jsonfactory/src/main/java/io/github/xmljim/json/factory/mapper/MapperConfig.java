package io.github.xmljim.json.factory.mapper;

import java.util.Optional;
import java.util.Set;

public interface MapperConfig {

    Optional<Class<?>> getTargetClass();

    Optional<ValueConverter<?>> getValueConverter();

    KeyNameCase getKeyNameCase();

    Set<String> getIgnoreKeys();

}
