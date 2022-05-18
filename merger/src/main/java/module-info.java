import io.xmljim.json.factory.merge.MergeFactory;
import io.xmljim.json.merger.MergeFactoryImpl;

module io.xmljim.json.merger {
    requires transitive io.xmljim.jsonfactory;
    opens io.xmljim.json.merger to io.xmljim.jsonfactory;
    provides MergeFactory with MergeFactoryImpl;
}