package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.service.JsonService;
import io.github.xmljim.json.service.ServiceManager;

import java.util.List;

public interface MappingConfig {

    List<ClassConfig> getClassConfigurations();

    static Builder with() {
        return ServiceManager.getProvider(MappingConfig.Builder.class);
    }

    static MappingConfig empty() {
        return with().build();
    }

    interface Builder extends JsonService {

        default <T> Builder withClass(Class<T> sourceClass) {
            return appendClassConfig(ClassConfig.with().sourceClass(sourceClass).build());
        }

        Builder appendClassConfig(ClassConfig classConfig);

        MappingConfig build();
    }
}
