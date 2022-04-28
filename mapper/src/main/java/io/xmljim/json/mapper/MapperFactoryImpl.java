package io.xmljim.json.mapper;

import io.xmljim.json.factory.mapper.MapperBuilder;
import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.service.JsonServiceProvider;

@JsonServiceProvider(service = MapperFactory.class, isNative = true, version = "1.0.1")
public class MapperFactoryImpl implements MapperFactory {

    @Override
    public MapperBuilder newBuilder() {
        return new MapperBuilderImpl(this);
    }
}
