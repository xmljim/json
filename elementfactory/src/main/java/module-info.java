import io.github.xmljim.json.elementfactory.ElementFactoryImpl;
import io.github.xmljim.json.factory.model.ElementFactory;

module io.github.xmljim.json.elementfactory {
    requires transitive io.github.xmljim.json.factory;
    opens io.github.xmljim.json.elementfactory;
    provides ElementFactory with ElementFactoryImpl;
}