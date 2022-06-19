package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.factory.mapper.parser.MappingParser;
import io.github.xmljim.json.service.JsonService;

/**
 * Factory/Service for Mapping Json to Java Classes
 */
public interface MapperFactory extends JsonService {


    /**
     * Create a new Mapper with default configurations, i.e., no {@link Mapping} data
     *
     * @return a new Mapper with default configurations
     */
    Mapper newMapper();

    Mapper newMapper(Mapping mapping);

    default Mapper newMapper(MappingConfig mappingConfig) {
        return newMapper(newMapping(mappingConfig));
    }

    ClassMapping newClassMapping(Mapping mapping, ClassConfig classConfig);

    MemberMapping newMemberMapping(ClassMapping classMapping, MemberConfig memberConfig);

    Mapping newMapping(MappingConfig mappingConfig);

    MappingParser newMappingParser(MappingParserConfig mappingParserConfig);

    default MappingParser newMappingParser() {
        return newMappingParser(MappingParserConfig.withDefaults());
    }

}
