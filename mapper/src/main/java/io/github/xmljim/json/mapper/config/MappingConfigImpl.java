package io.github.xmljim.json.mapper.config;

import io.github.xmljim.json.factory.config.AbstractConfiguration;
import io.github.xmljim.json.factory.mapper.ClassConfig;
import io.github.xmljim.json.factory.mapper.MappingConfig;
import io.github.xmljim.json.service.JsonServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class MappingConfigImpl extends AbstractConfiguration implements MappingConfig {
    private final List<ClassConfig> classConfigs = new ArrayList<>();

    private MappingConfigImpl() {

    }

    @Override
    public List<ClassConfig> getClassConfigurations() {
        return classConfigs;
    }

    @JsonServiceProvider(service = MappingConfig.Builder.class, version = "1.0.1", isNative = true)
    public static class MappingConfigBuilder implements MappingConfig.Builder {
        private MappingConfigImpl mappingConfig = new MappingConfigImpl();

        @Override
        public Builder appendClassConfig(ClassConfig classConfig) {
            mappingConfig.classConfigs.add(classConfig);
            return this;
        }

        @Override
        public MappingConfig build() {
            return mappingConfig;
        }
    }
}
