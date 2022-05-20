import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.mapper.MapperFactoryImpl;

module io.github.xmljim.json.mapper {
    requires transitive io.github.xmljim.json.factory;
    opens io.github.xmljim.json.mapper;
    exports io.github.xmljim.json.mapper to io.xmljim.json.mapper.test;
    provides MapperFactory with MapperFactoryImpl;
    uses ElementFactory;
}