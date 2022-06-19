import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.factory.mapper.*;
import io.github.xmljim.json.factory.merge.MergeFactory;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.ParserFactory;

module io.github.xmljim.json.api {

    requires transitive io.github.xmljim.json.elementfactory;
    requires transitive io.github.xmljim.json.parser;
    requires transitive io.github.xmljim.json.merger;
    requires transitive io.github.xmljim.json.mapper;
    requires transitive io.github.xmljim.json.jsonpath;

    // ** Services Consumed *

    // Element Factory: for creating new model elements
    uses ElementFactory;

    //Mapper Factory: for marshalling and unmarshalling Json and Java objects
    uses MapperFactory;
    //Builder for creating MappingConfig instances used by a Mapper
    uses MappingConfig.Builder;
    //Builder for creating ClassConfig instances used in a MappingConfig
    uses ClassConfig.Builder;
    //Builder for creating MemberConfig instances used in a ClassConfig
    uses MemberConfig.Builder;
    //Builder for creating a MapperParserConfig used by the MapperParser
    uses MappingParserConfig.Builder;
    //Parser Factory: for creating a new JsonParser
    uses ParserFactory;
    //JsonPath Factory: for creating JsonPath instances
    uses JsonPathFactory;
    //Merge Factory: for creating Merger instances
    uses MergeFactory;

    exports io.github.xmljim.json.api;
}