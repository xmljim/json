package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.Mapper;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.mapper.ValueConverter;
import io.github.xmljim.json.factory.mapper.annotation.JsonElement;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class ClassMember {
    private final Field field;
    private final Class<?> containerClass;
    private final boolean isAbstract;
    private final String fieldName;
    private final Type fieldType;
    private final boolean ignored;
    private final boolean accessible;
    private final String jsonKey;
    private final MethodReference setterMethodReference;
    private final MethodReference getterMethodReference;
    private final ValueConverter<?> getterValueConverter;
    private final ValueConverter<?> setterValueConverter;
    private final Class<?> targetClass;
    private final Mapper mapper;
    private final MapperFactory mapperFactory;

    public ClassMember(final Class<?> containerClass, final Field theField, final Mapper mapper, final MapperFactory mapperFactory) {
        this.field = theField;
        this.containerClass = containerClass;
        this.isAbstract = Modifier.isAbstract(containerClass.getModifiers());
        this.fieldName = theField.getName();
        this.fieldType = theField.getGenericType();
        this.jsonKey = AnnotationUtils.findJsonElementKey(theField).orElse(fieldName);
        this.ignored = mapper.getConfig().getIgnoreKeys().contains(jsonKey) || AnnotationUtils.findJsonElementIgnore(theField);
        this.accessible = theField.trySetAccessible();
        this.setterMethodReference = findMethod(containerClass, theField, true);
        this.getterMethodReference = findMethod(containerClass, theField, false);
        this.getterValueConverter = AnnotationUtils.getConvertToJson(theField).orElse(null);
        this.setterValueConverter = AnnotationUtils.getConvertToValue(theField).orElse(null);
        this.targetClass = AnnotationUtils.findConvertClass(theField).orElse(evalTargetClass(theField));
        this.mapper = mapper;
        this.mapperFactory = mapperFactory;
    }

    /**
     * Locate a method to set or get a value. Method names are determined either by explicitly setting the
     * {@link JsonElement#getterMethod()} or {@link JsonElement#setterMethod()} values, or by implying the setter
     * or getter name from the field name, i.e., if a field is named <code>fooBar</code>, the setter name would be
     * <code>setFooBar</code> and the getter name would be <code>getFooBar</code>. Once the method name is determined,
     * it scans all methods matching that name, return type and parameter type signatures appropriate for setters
     * and getters. The returnType for getters and parameter type for setters is mapped to the field's declared type.
     *
     * @param clazz  The current class
     * @param field  the current field within the class
     * @param setter flag to indicate whether to search for setter or getter methods; if set to true, it searches for
     *               setters; false searches for getters
     * @return An Optional of a Method that optionally contains a Method reference.
     */
    private MethodReference findMethod(Class<?> clazz, Field field, boolean setter) {

        final String methodName = setter ? AnnotationUtils.findJsonElementSetter(field).orElse(getMappedMethodName(field.getName(), true))
            : AnnotationUtils.findJsonElementGetter(field).orElse(getMappedMethodName(field.getName(), false));

        final int paramCount = setter ? 1 : 0;
        final Type returnType = setter ? Void.TYPE : field.getType();//field.getGenericType() != null ? (ParameterizedType)field.getGenericType() : field.getType();
        final List<Method> methods = findMethods(clazz, methodName, paramCount, returnType);

        Optional<Method> m;

        if (setter) {
            m = methods.stream().filter(method -> getMethodParameterType(method).equals(getFieldType(field))).findFirst();
        } else if (methods.size() == 0 && (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class))) {
            final String isMethodName = methodName.replaceFirst("get", "is");
            m = findMethods(clazz, isMethodName, paramCount, returnType).stream().findFirst();
        } else {
            m = methods.stream().findFirst();
        }

        if (m.isPresent()) {
            MethodReference.MethodType methodType = setter ? MethodReference.MethodType.SETTER : MethodReference.MethodType.GETTER;
            Type contextType = setter ? getMethodParameterType(m.get()) : m.get().getGenericReturnType();
            return new MethodReference(methodType, m.get().getName(),
                m.get().trySetAccessible(), Modifier.isPublic(m.get().getModifiers()), contextType);

        } else {
            return null;
        }
    }

    private List<Method> findMethods(Class<?> clazz, String methodName, int paramCount, Type returnType) {
        return Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().equals(methodName))
            .filter(method -> method.getParameterCount() == paramCount)
            .filter(method -> method.getReturnType().equals(returnType))
            .collect(Collectors.toList());

    }

    private Type getMethodParameterType(Method m) {
        final Type[] baseParam = m.getParameterTypes();
        final Type[] genericParam = m.getGenericParameterTypes();

        if (genericParam.length == 1) {
            return genericParam[0];
        } else {
            return baseParam[0];
        }
    }

    /**
     * Reflection magic to determine the Field's type
     *
     * @param field the field to evaluate
     * @return the Field type
     */
    private Type getFieldType(Field field) {

        return field.getGenericType();
    }

    /**
     * Some reflection magic to handle Generic types
     *
     * @param field the field
     * @return the underlying field type
     */
    private Class<?> evalTargetClass(Field field) {
        if (field.getGenericType() instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        }
        return field.getType();
    }

    /**
     * Utility to build a setterMethod name from a string
     *
     * @param key the key (typically a json key, but could be a field name)
     * @return a setter method name
     */
    private String getMappedMethodName(String key, boolean setter) {
        final String[] tokens = key.split("-");

        final StringBuilder builder = new StringBuilder();
        if (setter) {
            builder.append("set");
        } else {
            builder.append("get");
        }

        for (final String token : tokens) {
            for (int i = 0; i < token.length(); i++) {
                if (i == 0) {
                    builder.append(Character.toTitleCase(token.charAt(i)));
                } else {
                    builder.append(token.charAt(i));
                }
            }
        }

        return builder.toString();
    }

    public Optional<MemberHandler> getMemberHandler(Object instance) {
        MemberHandler handler = null;

        if (!isIgnored()) {
            handler = new MemberHandler(this, instance, getMapper(), getMapperFactory());
        }

        return Optional.ofNullable(handler);
    }

    public Field getField() {
        return field;
    }

    public Class<?> getContainerClass() {
        return containerClass;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Type getFieldType() {
        return fieldType;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public MethodReference getSetterMethodReference() {
        return setterMethodReference;
    }

    public MethodReference getGetterMethodReference() {
        return getterMethodReference;
    }

    public ValueConverter<?> getGetterValueConverter() {
        return getterValueConverter;
    }

    public ValueConverter<?> getSetterValueConverter() {
        return setterValueConverter;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }
}
