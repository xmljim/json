package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.*;
import io.github.xmljim.json.factory.mapper.parser.MappingParser;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserBuilder;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.mapper.parser.MappingParserImpl;
import io.github.xmljim.json.service.JsonServiceProvider;
import io.github.xmljim.json.service.ServiceManager;

@JsonServiceProvider(service = MapperFactory.class, isNative = true, version = "1.0.1")
public class MapperFactoryImpl implements MapperFactory {

    @Override
    public Mapper newMapper() {
        return new MapperImpl(this, newMapping(MappingConfig.empty()));
    }

    public Mapper newMapper(Mapping mapping) {
        return new MapperImpl(this, mapping);
    }

    @Override
    public ClassMapping newClassMapping(Mapping mapping, ClassConfig classConfig) {
        return new ClassMappingImpl(mapping, classConfig);
    }

    @Override
    public MemberMapping newMemberMapping(ClassMapping classMapping, MemberConfig memberConfig) {
        return new MemberMappingImpl(classMapping, memberConfig);
    }

    @Override
    public Mapping newMapping(MappingConfig mappingConfig) {
        return new MappingImpl(this, mappingConfig);
    }

    @Override
    public MappingParser newMappingParser(MappingParserConfig mappingParserConfig) {
        ParserFactory parserFactory = ServiceManager.getProvider(ParserFactory.class);
        ParserBuilder parserBuilder = parserFactory.newParserBuilder();
        parserBuilder.mergeSettings(mappingParserConfig);
        Parser parser = parserBuilder.build();

        Mapper mapper = newMapper(mappingParserConfig);

        return new MappingParserImpl(mapper, parser);
    }
}
