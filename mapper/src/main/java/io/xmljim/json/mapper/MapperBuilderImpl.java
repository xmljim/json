package io.xmljim.json.mapper;

import io.xmljim.json.factory.mapper.KeyNameCase;
import io.xmljim.json.factory.mapper.Mapper;
import io.xmljim.json.factory.mapper.MapperBuilder;
import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.factory.mapper.ValueConverter;
import io.xmljim.json.mapper.exception.JsonMapperConfigurationException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

class MapperBuilderImpl implements MapperBuilder {
    private final MapperConfigImpl mapperConfig = new MapperConfigImpl();
    private final MapperFactory factory;

    public MapperBuilderImpl(MapperFactory factory) {
        this.factory = factory;
    }

    @Override
    public Mapper build() {
        return new DefaultMapper(mapperConfig, factory);
    }

    @Override
    public MapperBuilder setTargetClass(Class<?> targetClass) {
        if (targetClass != null) {
            if (mapperConfig.getValueConverter().isPresent()) {
                throw new JsonMapperConfigurationException("Cannot assign a target class when a ValueConverter is already assigned");
            }
            mapperConfig.setTargetClass(Objects.requireNonNull(targetClass));
        } else {
            setTargetClass(null);
        }

        return this;
    }

    @Override
    public MapperBuilder setValueConverter(ValueConverter<?> valueConverter) {
        if (valueConverter != null) {
            if (mapperConfig.getTargetClass().isPresent()) {
                throw new JsonMapperConfigurationException("Cannot assign a ValueConverter when a Target class is already assigned");
            }
            mapperConfig.setValueConverter(Objects.requireNonNull(valueConverter));
        } else {
            mapperConfig.setValueConverter(null);
        }
        return this;
    }

    @Override
    public MapperBuilder setKeyNameCase(KeyNameCase keyNameCase) {
        mapperConfig.setKeyNameCase(keyNameCase);
        return this;
    }

    @Override
    public MapperBuilder setIgnoreKeys(Collection<String> ignoreKeys) {
        mapperConfig.setIgnoreKeys(ignoreKeys);
        return this;
    }

    @Override
    public MapperBuilder setIgnoreKeys(String... ignoreKeys) {
        return setIgnoreKeys(Arrays.stream(ignoreKeys).collect(Collectors.toSet()));
    }
}
