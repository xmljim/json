import io.xmljim.json.factory.merge.MergeFactory;
import io.xmljim.json.merger.MergeFactoryImpl;

module io.xmljim.json.merger {
    requires io.xmljim.json.elementfactory;
    exports io.xmljim.json.merger;
    provides MergeFactory with MergeFactoryImpl;
}