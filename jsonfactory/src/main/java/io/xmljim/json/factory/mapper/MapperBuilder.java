package io.xmljim.json.factory.mapper;

import java.util.Collection;

public interface MapperBuilder {

    Mapper build();

    MapperBuilder setTargetClass(Class<?> targetClass);

    MapperBuilder setValueConverter(ValueConverter<?> valueConverter);

    MapperBuilder setKeyNameCase(KeyNameCase keyNameCase);

    MapperBuilder setIgnoreKeys(Collection<String> ignoreKeys);

    @SuppressWarnings("unused")
    MapperBuilder setIgnoreKeys(String... ignoreKeys);

    default MapperBuilder merge(Mapper mapper) {
        return setTargetClass(mapper.getConfig().getTargetClass().orElse(null))
            .setValueConverter(mapper.getConfig().getValueConverter().orElse(null))
            .setKeyNameCase(mapper.getConfig().getKeyNameCase())
            .setIgnoreKeys(mapper.getConfig().getIgnoreKeys());

    }
}
