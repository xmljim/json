import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.mapper.MapperFactoryImpl;

module io.xmljim.json.mapper {
    requires transitive io.xmljim.json.elementfactory;

    exports io.xmljim.json.mapper;
    provides MapperFactory with MapperFactoryImpl;
}