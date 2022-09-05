package io.github.xmljim.json.factory.serializer;

import java.util.stream.Stream;

public interface SerializerConfig {

    Stream<ElementContext> includes();

    Stream<ElementContext> excludes();
}
