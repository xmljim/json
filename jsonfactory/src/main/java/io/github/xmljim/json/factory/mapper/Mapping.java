package io.github.xmljim.json.factory.mapper;

import java.util.stream.Stream;

public interface Mapping {

    MapperFactory getMapperFactory();

    Stream<ClassMapping> getClassMappings();

    <T> ClassMapping getClassMapping(Class<T> mappedClass);

    <T> boolean containsClassMapping(Class<T> mappedClass);

    void append(ClassMapping classMapping);

    <T> void append(Class<T> classMapping);
}
