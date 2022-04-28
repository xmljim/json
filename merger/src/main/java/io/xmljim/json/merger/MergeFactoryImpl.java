package io.xmljim.json.merger;

import io.xmljim.json.factory.merge.MergeBuilder;
import io.xmljim.json.factory.merge.MergeFactory;
import io.xmljim.json.service.JsonServiceProvider;

@JsonServiceProvider(service = MergeFactory.class, isNative = true, version = "1.0.1")
public class MergeFactoryImpl implements MergeFactory {
    @Override
    public MergeBuilder newMergeBuilder() {
        return new MergeBuilderImpl();
    }


}
