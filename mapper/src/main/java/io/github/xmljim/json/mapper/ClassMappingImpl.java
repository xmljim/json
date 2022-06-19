package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.*;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.mapper.exception.JsonMapperException;
import io.github.xmljim.json.model.JsonObject;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

class ClassMappingImpl implements ClassMapping {


    private final ClassConfig classConfig;

    private boolean initialized;
    private final Set<String> ignoredKeys = new HashSet<>();

    private Class<?> sourceClass;
    private Class<?> targetClass;
    private boolean isMemberClass;

    private boolean isRecord;
    private boolean isPublic;

    private final List<String> constructorKeys = new ArrayList<>();

    private final MapperFactory mapperFactory;
    private final Mapping mapping;

    private final List<MemberMapping> memberMappings = new ArrayList<>();

    public ClassMappingImpl(Mapping mapping, ClassConfig config) {
        this.mapping = mapping;
        this.classConfig = config;
        this.mapperFactory = mapping.getMapperFactory();
        this.sourceClass = config.getSourceClass();
        this.targetClass = config.getTargetClass();
        initialize();
    }


    @Override
    public ClassConfig getClassConfig() {
        return classConfig;
    }

    @Override
    public Mapping getMapping() {
        return this.mapping;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getSourceClass() {
        return (Class<T>) sourceClass;
    }

    protected <T> void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getTargetClass() {
        return (Class<T>) targetClass;
    }

    @Override
    public boolean isPublic() {
        return this.isPublic;
    }


    @Override
    public boolean isRecord() {
        return this.isRecord;
    }

    @Override
    public List<String> getConstructorKeys() {
        return constructorKeys;
    }

    @Override
    public MemberMapping getMemberMapping(Field field) {
        return getMemberMappings().filter(memberMapping -> memberMapping.getFieldName().equals(field.getName()))
            .findFirst().orElse(null);
    }

    @Override
    public MemberMapping getMemberMapping(String jsonKey) {
        return getMemberMappings().filter(memberMapping -> memberMapping.getJsonKey().equals(jsonKey))
            .findFirst()
            .orElse(null);
    }

    @Override
    public Set<String> getIgnoredKeys() {
        return ignoredKeys;
    }

    @Override
    public void appendMemberMapping(MemberMapping memberMapping) {
        memberMappings.add(memberMapping);
    }

    private void initialize() {
        if (initialized) {
            return;
        }

        if (getClassConfig().getSourceClass() == null) {
            throw new JsonMapperException("Invalid ClassMapping: Source Class is required");
        }

        if (getClassConfig().getTargetClass().isInterface() || Modifier.isAbstract(getTargetClass().getModifiers())) {
            throw new JsonMapperException("Invalid target class [" + getTargetClass().getName() + "]. Target cannot be"
                + " an interface or abstract class");
        }

        if (getClassConfig().getSourceClass() != null) {
            //class must be same or inheritable from source
            if (!getClassConfig().getSourceClass().isAssignableFrom(getTargetClass())) {
                throw new JsonMapperException("Invalid target class [" + getTargetClass().getName() + "]. Target class "
                    + "must be assignable from source class [" + getTargetClass().getName() + "]");
            }
        }

        this.isMemberClass = getTargetClass().isMemberClass();
        this.isRecord = getTargetClass().isRecord();
        this.isPublic = Modifier.isPublic(getTargetClass().getModifiers());


        scanClass(this.targetClass);

        if (isRecord()) {
            getMemberMappings().forEach(memberMapping -> this.constructorKeys.add(memberMapping.getJsonKey()));
        }

        initialized = true;
    }

    /**
     * Create a new JSONObject from a class source
     *
     * @param source         the class instance
     * @param elementFactory The element factory that will be used to create the source
     * @param mapperFactory  the mapper factory
     * @param mapper         the mapper
     * @param <T>            the class type
     * @return a new JsonObject
     */
    public <T> JsonObject apply(T source, ElementFactory elementFactory, MapperFactory mapperFactory, Mapper mapper) {
        JsonObject newObject = elementFactory.newObject();
        getMemberMappings().forEach(memberMapping -> memberMapping.applyToJson(source, newObject));
        return newObject;
    }

    public <T> T apply(JsonObject source, ElementFactory elementFactory, MapperFactory mapperFactory, Mapper mapper) {
        return null;
    }

    private <T> void scanClass(Class<T> classToScan) {
        Arrays.stream(classToScan.getDeclaredFields()).forEach(field -> addEntry(classToScan, field));
        getSuperclass(classToScan).ifPresent(this::scanClass);
    }

    private <T, S> Optional<Class<S>> getSuperclass(Class<T> clazz) {
        @SuppressWarnings("unchecked") final Class<S> superclass = (Class<S>) clazz.getSuperclass();
        final Class<S> willSend = superclass != Object.class ? superclass : null;
        return Optional.ofNullable(willSend);
    }

    private <T> void addEntry(Class<T> owningClass, Field field) {
        if (!hasMemberMapping(owningClass, field)) {

            MemberConfig config = MemberConfig.with()
                .containerClass(owningClass)
                .field(field)
                .build();

            MemberMapping memberMapping = mapperFactory.newMemberMapping(this, config);

            //MemberMappingImpl memberMapping = new MemberMappingImpl(this, owningClass, field);
            memberMappings.add(memberMapping);
            if (memberMapping.isIgnored()) {
                ignoredKeys.add(memberMapping.getJsonKey());
            }
        }
    }

    private <T> boolean hasMemberMapping(Class<T> owningClass, Field field) {
        return getMemberMappings().anyMatch(memberMapping -> memberMapping.getContainerClass().equals(owningClass)
            && memberMapping.getFieldName().equals(field.getName()));
    }

    @Override
    public Stream<MemberMapping> getMemberMappings() {
        return memberMappings.stream();//return memberMappings.stream().filter(memberMapping -> !memberMapping.isIgnored());
    }


    @SuppressWarnings("unchecked")
    public <T> Constructor<T> getConstructor() {
        Class<?>[] argTypes = getConstructorKeys().stream()
            .map(this::getMemberMapping)
            .map(memberMapping -> getRawType(memberMapping.getFieldType())).toList()
            .toArray(new Class[]{});


        try {
            return (Constructor<T>) targetClass.getDeclaredConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            StringBuilder builder = new StringBuilder();
            Arrays.stream(argTypes).forEach(c -> builder.append(c.toString()).append(", "));
            throw new JsonMapperException("No constructor found with args: [" + builder.toString().substring(0, builder.toString().length() - 2) + "]");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getRawType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        }
        return (Class<T>) type;
    }

    @Override
    public <T> T toClass(JsonObject jsonObject) {
        T classInstance = newInstance(jsonObject);
        List<MemberMapping> assignable = getAssignableMembers().toList();
        getAssignableMembers().forEach(memberMapping -> memberMapping.applyToClass(jsonObject, classInstance));
        return classInstance;

    }

    private Stream<MemberMapping> getAssignableMembers() {
        return memberMappings.stream()
            .filter(memberMapping -> !memberMapping.isIgnored())
            .filter(memberMapping -> !getIgnoredKeys().contains(memberMapping.getJsonKey()))
            .filter(memberMapping -> !getConstructorKeys().contains(memberMapping.getJsonKey()));
    }

    public <T> T newInstance(JsonObject jsonObject) {
        Object[] args = getConstructorArgs(jsonObject);
        try {
            Constructor<T> constructor = getConstructor();
            return constructor.newInstance(args);
        } catch (SecurityException | InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new JsonMapperException("Error creating class: " + targetClass + ": " + e.getMessage(), e);
        }
    }

    private Object[] getConstructorArgs(JsonObject jsonObject) {
        return getConstructorKeys().stream()
            .map(this::getMemberMapping)
            .map(memberMapping -> memberMapping.getValue(jsonObject))
            .toList()
            .toArray(new Object[]{});
    }

}
