package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.ClassMapping;
import io.github.xmljim.json.factory.mapper.Converter;
import io.github.xmljim.json.factory.mapper.MemberConfig;
import io.github.xmljim.json.factory.mapper.MemberMapping;
import io.github.xmljim.json.mapper.exception.JsonMapperException;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.model.NodeType;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

class MemberMappingImpl implements MemberMapping {
    private String jsonKey;
    private NodeType jsonNodeType;
    private Class<?> containerClass;
    private String setterMethodName;
    private String getterMethodName;
    private Method getterMethod;
    private Method setterMethod;
    private Converter jsonConverter;
    private Converter fieldConverter;
    private Class<?> elementTargetClass;
    private boolean ignored;
    private Type fieldType;

    private String fieldName;

    private Field field;

    private final ClassMapping classMapping;

    private final MemberConfig memberConfig;

    protected MemberMappingImpl(ClassMapping classMapping, MemberConfig memberConfig) {
        this.classMapping = classMapping;
        this.memberConfig = memberConfig;
        initialize();

    }

    private Method findMethod(Field field, boolean setter) {

        //short-circuit if the class is a record and we're looking for a setter (there are no setters on Records, sparky)
        if (classMapping.isRecord() && setter) {
            return null;
        }

        final int paramCount = setter ? 1 : 0;
        final Type returnType = setter ? Void.TYPE : field.getType();//field.getGenericType() != null ? (ParameterizedType)field.getGenericType() : field.getType();
        boolean isBoolean = returnType.equals(Boolean.class) || returnType.equals(boolean.class);

        final String methodName = setter ?
            (memberConfig.getSetterMethodName() == null ?
                AnnotationUtils.findJsonElementSetter(field).orElse(getMappedMethodName(field.getName(), true, isBoolean)) :
                memberConfig.getSetterMethodName()) :
            (this.getterMethodName == null ?
                AnnotationUtils.findJsonElementGetter(field).orElse(getMappedMethodName(field.getName(), false, isBoolean)) :
                this.getterMethodName);


        Stream<Method> methodStream = Arrays.stream(memberConfig.getContainerClass().getDeclaredMethods())
            .filter(method -> method.getName().equals(methodName))
            .filter(method -> method.getParameterCount() == paramCount)
            .filter(method -> method.getReturnType().equals(returnType));

        Optional<Method> methodRef = setter ?
            methodStream.filter(method -> fieldAndMethodParameterTypesMatch(field, method)).findFirst() :
            methodStream.findFirst();

        return methodRef.orElse(null);
    }

    private Method findMethodByKey(boolean setter) {
        //short-circuit if the class is a record and we're looking for a setter (there are no setters on Records, sparky)
        if (this.getClassMapping().isRecord() && setter) {
            return null;
        }
        final int paramCount = setter ? 1 : 0;

        final String methodName = setter ?
            (memberConfig.getSetterMethodName() == null ?
                getMappedMethodName(getJsonKey(), true, getNodeType() == NodeType.BOOLEAN) :
                this.getSetterMethodName()) :
            (memberConfig.getGetterMethodName() == null ?
                getMappedMethodName(getJsonKey(), false, getNodeType() == NodeType.BOOLEAN) :
                this.getGetterMethodName());

        return Arrays.stream(getContainerClass().getMethods())
            .filter(method -> method.getName().equals(methodName))
            .filter(method -> method.getParameterCount() == paramCount)
            .filter(method -> isCompatibleReturnType(method, getNodeType(), setter, getElementTargetClass()))
            .findFirst()
            .orElseThrow(() -> new JsonMapperException("No " + (setter ? "setter" : "getter") + " found for key: " + getJsonKey()));

    }

    private boolean isCompatibleReturnType(Method method, NodeType type, boolean setter, Class<?> elementTargetClass) {
        if (setter) {
            return method.getReturnType().equals(Void.TYPE);
        } else {
            return switch (type) {
                case STRING -> method.getReturnType().isAssignableFrom(String.class);
                case BOOLEAN ->
                    method.getReturnType().equals(Boolean.class) || method.getReturnType().equals(boolean.class);
                case LONG -> method.getReturnType().equals(Long.class) || method.getReturnType().equals(long.class);
                case INTEGER ->
                    method.getReturnType().equals(Integer.class) || method.getReturnType().equals(int.class);
                case DOUBLE ->
                    method.getReturnType().equals(Double.class) || method.getReturnType().equals(double.class);
                case ARRAY -> Collection.class.isAssignableFrom(method.getReturnType()) &&
                    method.getGenericReturnType().equals(elementTargetClass);
                case OBJECT -> elementTargetClass.isAssignableFrom(method.getReturnType());
                default -> false;
            };
        }
    }


    private Optional<Field> findField(String jsonKey) {
        return Arrays.stream(this.getContainerClass().getDeclaredFields())
            .filter(field -> field.getName().equals(getMappedNamed(jsonKey)))
            .findFirst();
    }


    private boolean fieldAndMethodParameterTypesMatch(Field field, Method method) {
        return getMethodParameterType(method).equals(getFieldType(field));
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
     * Utility to build a setterMethod name from a string
     *
     * @param key       the key (typically a json key, but could be a field name)
     * @param setter    flag indicating method name is a setter (true) or getter (false)
     * @param isBoolean flag indicating the method return or parameter type is a boolean value
     * @return a bean-compliant method name
     */
    private String getMappedMethodName(String key, boolean setter, boolean isBoolean) {

        final StringBuilder builder = new StringBuilder();
        if (!classMapping.isRecord()) {  //only append get/set/is to "classes" that are not record types
            if (setter) {
                builder.append("set");
            } else {
                builder.append(isBoolean ? "is" : "get");
            }
        }

        builder.append(this.classMapping.isRecord() ? key : getMappedNamed(key));
        return builder.toString();
    }

    private String getMappedNamed(String jsonKey) {
        final StringBuilder builder = new StringBuilder();
        for (final String token : jsonKey.split("[_-]")) {
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

    @Override
    public String getJsonKey() {
        return jsonKey;
    }

    @Override
    public boolean isIgnored() {
        if (this.field != null) {
            return memberConfig.isIgnored() ? memberConfig.isIgnored() : AnnotationUtils.findJsonElementIgnore(field);
        }

        return memberConfig.isIgnored();
    }

    @Override
    public boolean isAccessible() {
        return false;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getContainerClass() {
        return (Class<T>) containerClass;
    }


    @Override
    public String getSetterMethodName() {
        return setterMethodName;
    }


    @Override
    public String getGetterMethodName() {
        return getterMethodName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getElementTargetClass() {
        return (Class<T>) elementTargetClass;
    }

    @Override
    public Converter getJsonConverter() {
        return jsonConverter;
    }

    @Override
    public Converter getFieldConverter() {
        return fieldConverter;
    }

    @Override
    public NodeType getNodeType() {
        return jsonNodeType;
    }

    @Override
    public String getFieldName() {
        return this.fieldName;
    }


    @Override
    public Type getFieldType() {
        return this.fieldType;
    }

    private void initialize() {
        if (memberConfig.getContainerClass() == null) {
            throw new JsonMapperException("Invalid MemberMapping: Container Class is required");
        }

        if (memberConfig.getField() == null && memberConfig.getJsonKey() == null) {
            throw new JsonMapperException("Invalid MemberMapping: Require either field or jsonKey (or both)");
        }

        if (memberConfig.getField() == null) {
            Optional<Field> optionalField = findField(memberConfig.getJsonKey());
            if (optionalField.isPresent()) {
                this.field = optionalField.get();
                this.setterMethod = findMethod(field, true);
                this.getterMethod = findMethod(field, false);
            } else {

                this.setterMethod = findMethodByKey(true);
                this.getterMethod = findMethodByKey(false);
            }
        } else {
            this.field = memberConfig.getField();
            this.setterMethod = findMethod(memberConfig.getField(), true);
            this.getterMethod = findMethod(memberConfig.getField(), false);
        }

        this.setterMethodName = memberConfig.getSetterMethodName() == null ? (setterMethod != null ? setterMethod.getName() : null) : memberConfig.getSetterMethodName();
        this.getterMethodName = memberConfig.getGetterMethodName() == null ? (getterMethod != null ? getterMethod.getName() : null) : memberConfig.getGetterMethodName();
        this.jsonConverter = memberConfig.getJsonConverter() != null ? memberConfig.getJsonConverter() : new PassThroughConverter();
        this.fieldConverter = memberConfig.getFieldConverter() != null ? memberConfig.getFieldConverter() : new PassThroughConverter();
        this.jsonKey = memberConfig.getJsonKey() == null ? AnnotationUtils.findJsonElementKey(field).orElse(field.getName()) : jsonKey;
        this.jsonNodeType = jsonNodeType != null ? jsonNodeType : NodeType.fromClassType(field.getType());
        this.fieldName = field != null ? field.getName() : null;
        this.fieldType = field != null ? field.getGenericType() : (getterMethod != null ? getterMethod.getGenericReturnType() : null);
        this.elementTargetClass = elementTargetClass != null ? elementTargetClass :
            AnnotationUtils.findConvertClass(field).orElse(evalTargetClass(field));
        this.ignored = memberConfig.isIgnored() ? memberConfig.isIgnored() :
            (field != null && AnnotationUtils.findJsonElementIgnore(field));
        this.containerClass = memberConfig.getContainerClass();
    }

    @Override
    public <T> void applyToClass(JsonObject jsonObject, T instance) {
        try {
            Object value = convertValue(jsonObject.getValue(getJsonKey()));
            setterMethod.invoke(instance, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new JsonMapperException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T convertValue(JsonValue<?> value) {
        if (value.type().isPrimitive()) {
            return getJsonConverter().convert(value.get());
        } else if (value.type().isArray()) {
            if (List.class.isAssignableFrom(getRawType(getFieldType()))) {
                return (T) createList(getElementTargetClass(), value.asJsonArray());
            } else {
                return (T) createSet(getElementTargetClass(), value.asJsonArray());
            }
        } else if (value.type().isObject()) {
            return getClassMapping().getMapping().getClassMapping(getElementTargetClass()).toClass(value.asJsonObject());
        }

        return null;
    }

    private <T> List<T> createList(Class<T> type, JsonArray json) {
        List<T> list = new ArrayList<>();
        json.jsonValues().forEach(jsonValue -> list.add(convertValue(jsonValue)));
        return list;
    }

    private <T> Set<T> createSet(Class<T> type, JsonArray json) {
        Set<T> set = new HashSet<>();
        json.jsonValues().forEach(jsonValue -> set.add(convertValue(jsonValue)));
        return set;
    }


    @Override
    public <T> void applyToJson(T instance, JsonObject jsonObject) {
        Object value = getValue(instance);
        jsonObject.put(getJsonKey(), value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(JsonObject jsonObject) {
        return (T) getJsonConverter().convert(jsonObject.get(getJsonKey()));
    }

    @Override
    public ClassMapping getClassMapping() {
        return this.classMapping;
    }

    public <V, T> V getValue(T instance) {
        try {
            return getFieldConverter().convert(getterMethod.invoke(instance));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getRawType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        }
        return (Class<T>) type;
    }
}
