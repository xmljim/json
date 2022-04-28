package io.xmljim.json.mapper;

import io.xmljim.json.factory.mapper.Mapper;
import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.model.JsonObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

class ClassScanner {
    private final Map<String, ClassMember> classMemberMap = new HashMap<>();
    private final Class<?> classToProcess;
    private final ElementFactory elementFactory;
    private final Mapper mapper;
    private final MapperFactory factory;

    private ClassScanner(Class<?> classToProcess, ElementFactory elementFactory, Mapper mapper, MapperFactory factory) {
        this.classToProcess = classToProcess;
        this.elementFactory = elementFactory;
        this.mapper = mapper;
        this.factory = factory;
        scanClass(classToProcess);
    }

    public Class<?> getTargetClass() {
        return classToProcess;
    }

    public Mapper getMapper() {
        return mapper;
    }

    private ElementFactory getElementFactory() {
        return elementFactory;
    }

    private MapperFactory getMapperFactory() {
        return factory;
    }

    private Stream<ClassMember> classMembers() {
        return classMemberMap.values().stream();
    }

    public static <T> JsonObject toJsonObject(T source, ElementFactory elementFactory, Mapper mapper, MapperFactory factory) {
        ClassScanner scanner = new ClassScanner(source.getClass(), elementFactory, mapper, factory);
        JsonObject jsonObject = scanner.getElementFactory().newObject();

        scanner.classMembers().forEach(classMember -> {
            classMember.getMemberHandler(source).ifPresent(mh -> jsonObject.put(classMember.getJsonKey(), mh.getMemberValue()));
        });

        return jsonObject;
    }

    public static <T> T toClass(Class<T> classType, JsonObject source, ElementFactory elementFactory, Mapper mapper, MapperFactory factory) {
        ClassScanner scanner = new ClassScanner(classType, elementFactory, mapper, factory);
        T newObject = ClassUtils.createInstance(classType);

        scanner.classMembers().forEach(classMember -> {
            classMember.getMemberHandler(newObject).ifPresent(mh -> {
                mh.setMemberValue(source.value(classMember.getJsonKey()).orElse(null));
            });
        });

        return newObject;
    }

    private void scanClass(Class<?> currentClass) {
        Arrays.stream(currentClass.getDeclaredFields()).forEach(field -> addEntry(currentClass, field));
        final Optional<Class<?>> optionalSuperclass = getSuperclass(currentClass);
        optionalSuperclass.ifPresent(this::scanClass);
    }

    private void addEntry(Class<?> thisClass, Field field) {
        ClassMember classMember = new ClassMember(thisClass, field, getMapper(), getMapperFactory());
        classMemberMap.put(classMember.getJsonKey(), classMember);
    }

    private Optional<Class<?>> getSuperclass(Class<?> clazz) {
        final Class<?> superclass = clazz.getSuperclass();
        final Class<?> willSend = superclass != Object.class ? superclass : null;
        return Optional.ofNullable(willSend);
    }

}
