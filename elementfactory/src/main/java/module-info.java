import io.xmljim.json.elementfactory.ElementFactoryImpl;
import io.xmljim.json.factory.model.ElementFactory;

module io.xmljim.json.elementfactory {
    requires transitive io.xmljim.jsonfactory;
    opens io.xmljim.json.elementfactory;
    provides ElementFactory with ElementFactoryImpl;
}