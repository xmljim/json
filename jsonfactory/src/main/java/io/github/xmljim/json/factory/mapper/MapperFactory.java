package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.service.JsonService;

public interface MapperFactory extends JsonService {

    MapperBuilder newBuilder();

    default Mapper newMapper() {
        return newBuilder().build();
    }
}
