package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.MapperBuilder;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.service.JsonServiceProvider;

@JsonServiceProvider(service = MapperFactory.class, isNative = true, version = "1.0.1")
public class MapperFactoryImpl implements MapperFactory {

    @Override
    public MapperBuilder newBuilder() {
        return new MapperBuilderImpl(this);
    }
}
