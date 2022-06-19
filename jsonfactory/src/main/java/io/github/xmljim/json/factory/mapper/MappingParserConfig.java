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

public interface MappingParserConfig extends ParserSettings, MappingConfig {

    static Builder with() {
        return ServiceManager.getProvider(MappingParserConfig.Builder.class);
    }

    static MappingParserConfig withDefaults() {
        return with().build();
    }

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
