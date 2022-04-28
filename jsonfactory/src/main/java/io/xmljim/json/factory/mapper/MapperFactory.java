package io.xmljim.json.factory.mapper;

import io.xmljim.json.service.JsonService;

public interface MapperFactory extends JsonService {

    MapperBuilder newBuilder();

    default Mapper newMapper() {
        return newBuilder().build();
    }
}
