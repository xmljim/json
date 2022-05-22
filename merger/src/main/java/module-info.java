import io.github.xmljim.json.factory.merge.MergeFactory;
import io.github.xmljim.json.merger.MergeFactoryImpl;

module io.github.xmljim.json.merger {
    requires transitive io.github.xmljim.json.factory;
    opens io.github.xmljim.json.merger to io.github.xmljim.json.factory;
    exports io.github.xmljim.json.merger to io.github.xmljim.json.merger.test;

    provides MergeFactory with MergeFactoryImpl;
}