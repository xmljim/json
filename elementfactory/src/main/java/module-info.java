import io.xmljim.json.elementfactory.ElementFactoryImpl;
import io.xmljim.json.factory.model.ElementFactory;

module io.xmljim.json.elementfactory {
    requires transitive io.xmljim.jsonfactory;
    exports io.xmljim.json.elementfactory;

    provides ElementFactory with ElementFactoryImpl;
}