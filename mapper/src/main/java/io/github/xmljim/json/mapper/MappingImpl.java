package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

class MappingImpl implements Mapping {

    private final MapperFactory mapperFactory;

    private final Set<ClassMapping> classMappingSet = new HashSet<>();

    public MappingImpl(MapperFactory mapperFactory, MappingConfig config) {
        this.mapperFactory = mapperFactory;
        config.getClassConfigurations().forEach(classConfig -> append(mapperFactory.newClassMapping(this, classConfig)));
    }

    @Override
    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    @Override
    public Stream<ClassMapping> getClassMappings() {
        return classMappingSet.stream();
    }

    @Override
    public <T> ClassMapping getClassMapping(Class<T> mappedClass) {
        return
            classMappingSet.stream()
                .filter(containsMapping(mappedClass))
                .findFirst()
                .orElseGet(() -> createClassMapping(mappedClass));
    }

    @Override
    public <T> boolean containsClassMapping(Class<T> mappedClass) {
        return classMappingSet.stream().anyMatch(containsMapping(mappedClass));

    }


    private <T> Predicate<ClassMapping> containsMapping(Class<T> mappedClass) {
        return containsSourceMapping(mappedClass).or(containsTargetMapping(mappedClass));
    }

    private <T> Predicate<ClassMapping> containsSourceMapping(Class<T> mappedClass) {

        return classMapping -> {
            System.out.println(classMapping.getSourceClass().getName().equals(mappedClass.getName()));
            return classMapping.getSourceClass().getName().equals(mappedClass.getName());
        };
    }

    private <T> Predicate<ClassMapping> containsTargetMapping(Class<T> mappedClass) {
        return classMapping -> classMapping.getTargetClass().getName().equals(mappedClass.getName());
    }

    private <T> ClassMapping createClassMapping(Class<T> mappedClass) {
        ClassConfig config = ClassConfig.with().sourceClass(mappedClass).build();
        ClassMapping classMapping = getMapperFactory().newClassMapping(this, config);
        append(classMapping);
        return classMapping;
    }

    @Override
    public void append(ClassMapping classMapping) {
        classMappingSet.add(classMapping);
    }

    @Override
    public <T> void append(Class<T> classReference) {
        append(createClassMapping(classReference));
    }
}
