package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.factory.parser.NumericValueType;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserSettings;
import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;
import io.github.xmljim.json.factory.parser.event.Processor;
import io.github.xmljim.json.service.JsonService;
import io.github.xmljim.json.service.ServiceManager;

import java.nio.charset.Charset;

/**
 * Configurations for Mapping Parser that extends both
 * {@link ParserSettings} and {@link MappingConfig}
 */
public interface MappingParserConfig extends ParserSettings, MappingConfig {

    /**
     * Get the Builder for creating configuration settings for Mapping Parser. Requires a
     * {@link JsonService} service provider implementation to be present on the module path
     * @return a new Builder implementation. If no provider is present, it will throw
     * a {@link io.github.xmljim.json.service.exception.JsonServiceProviderUnavailableException}.
     */
    static Builder with() {
        return ServiceManager.getProvider(MappingParserConfig.Builder.class);
    }

    /**
     * Create a new Configuration from default settings
     * @return a new configuration
     */
    static MappingParserConfig withDefaults() {
        return with().build();
    }

    /**
     * Builder interface for creating the Mapper Parser settings. This is defined as a service
     * so that a service provider can register with the ServiceManager.
     */
    interface Builder extends MappingConfig.Builder, JsonService {

        Builder assembler(Assembler<?> assembler);

        Builder blockCount(int blockCount);

        Builder characterSet(Charset charset);

        Builder eventHandler(EventHandler eventHandler);

        Builder enableStatistics(boolean enableStatistics);

        Builder fixedNumberStrategy(NumericValueType fixedNumberStrategy);

        Builder floatingNumberStrategy(NumericValueType floatingNumberStrategy);

        Builder maxEventBufferCapacity(int maxEventBufferCapacity);

        Builder useStrict(boolean useStrict);

        Builder parser(Parser parser);

        Builder processor(Processor processor);

        Builder requestNextLength(int requestNextLength);

        MappingParserConfig build();
    }
}
