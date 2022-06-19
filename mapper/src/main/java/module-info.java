import io.github.xmljim.json.factory.mapper.*;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.mapper.MapperFactoryImpl;
import io.github.xmljim.json.mapper.config.ClassConfigImpl;
import io.github.xmljim.json.mapper.config.MappingConfigImpl;
import io.github.xmljim.json.mapper.config.MappingParserConfigImpl;
import io.github.xmljim.json.mapper.config.MemberConfigImpl;


module io.github.xmljim.json.mapper {
    //dependencies
    requires transitive io.github.xmljim.json.factory;

    //expose packages for reflection and service creation
    opens io.github.xmljim.json.mapper;
    opens io.github.xmljim.json.mapper.config;
    opens io.github.xmljim.json.mapper.parser;

    //targeted exports (only make them available to specific modules)
    exports io.github.xmljim.json.mapper to io.xmljim.json.mapper.test;
    exports io.github.xmljim.json.mapper.config to io.xmljim.json.mapper.test;

    //Service providers
    provides MapperFactory with MapperFactoryImpl;
    provides MappingConfig.Builder with MappingConfigImpl.MappingConfigBuilder;
    provides ClassConfig.Builder with ClassConfigImpl.ClassConfigBuilder;
    provides MemberConfig.Builder with MemberConfigImpl.MemberConfigBuilder;
    provides MappingParserConfig.Builder with MappingParserConfigImpl.MappingParserConfigBuilder;

    //Services consumed
    uses ElementFactory;
}