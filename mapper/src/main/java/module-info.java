import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.mapper.MapperFactoryImpl;

module io.xmljim.json.mapper {
    requires transitive io.xmljim.jsonfactory;
    opens io.xmljim.json.mapper;
    exports io.xmljim.json.mapper to io.xmljim.json.mapper.test;
    provides MapperFactory with MapperFactoryImpl;
    uses ElementFactory;
}