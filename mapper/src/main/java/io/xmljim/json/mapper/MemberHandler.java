package io.xmljim.json.mapper;

import io.xmljim.json.factory.mapper.Mapper;
import io.xmljim.json.factory.mapper.MapperBuilder;
import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.mapper.exception.JsonMapperException;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.model.JsonValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

class MemberHandler {
    private final ClassMember classMember;
    private final Object instance;
    private final Mapper mapper;
    private final MapperFactory factory;

    public MemberHandler(ClassMember classMember, Object instance, Mapper mapper, MapperFactory mapperFactory) {
        this.classMember = classMember;
        this.instance = instance;
        this.mapper = mapper;
        this.factory = mapperFactory;
    }

    public ClassMember getClassMember() {
        return classMember;
    }

    private Optional<Object> handleJSONValue(JsonValue<?> value) {
        Object returnValue = null;

        if (getClassMember().isIgnored()) {
            return Optional.empty();
        }

        if (getClassMember().getSetterValueConverter() != null) {
            returnValue = getClassMember().getSetterValueConverter().convert(value.get());
        } else if (value.type().isPrimitive()) {
            returnValue = value.get();
        } else {
            final Class<?> targetClass = getClassMember().getTargetClass();
            if (value.type().isArray()) {
                returnValue = buildTargetMapper().toList((JsonArray) value.get());  //.toList((JsonArray) value.value(), buildMapperConfig());
            } else {
                returnValue = getMapper().toClass((JsonObject) value.get(), targetClass);  //getMapper().convertToClass((JsonObject) value.value(), targetClass);
            }
        }

        return Optional.ofNullable(returnValue);
    }

    private MapperFactory getFactory() {
        return factory;
    }

    private MapperBuilder getMapperBuilder() {
        return getFactory().newBuilder();
    }

    private Mapper getMapper() {
        return mapper;
    }

    private Mapper buildTargetMapper() {
        return getFactory().newBuilder()
            .merge(getMapper())
            .setValueConverter(null)
            .setTargetClass(getClassMember().getTargetClass())
            .build();
    }

    public void setMemberValue(JsonValue<?> value) {
        try {
            if (getClassMember().getContainerClass().equals(instance.getClass())) {
                final Field field = instance.getClass().getDeclaredField(getClassMember().getFieldName());
                setFieldValue(field, value);
            } else {
                Optional<Method> method = getSetterMethod();
                method.ifPresent(method1 -> setMethodValue(method1, value));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void setFieldValue(Field field, JsonValue<?> value) {
        final Optional<Object> valueToSet = handleJSONValue(value);

        if (valueToSet.isPresent()) {
            try {
                field.setAccessible(true);
                field.set(instance, valueToSet.get());
            } catch (final Exception e) {
                throw new JsonMapperException(e);
            }
        }
    }

    private void setMethodValue(Method method, JsonValue<?> value) {
        final Optional<Object> valueToSet = handleJSONValue(value);

        if (valueToSet.isPresent()) {
            try {
                method.setAccessible(true);
                method.invoke(instance, valueToSet.get());
            } catch (final Exception e) {
                throw new JsonMapperException(e);
            }
        }
    }

    public <V> V getMemberValue() {
        try {
            if (getClassMember().getContainerClass().equals(instance.getClass())) {
                final Field field = instance.getClass().getDeclaredField(getClassMember().getFieldName());
                return getFieldMemberValue(field);
            } else {
                Optional<Method> method = getGetterMethod();
                return method.<V>map(this::getMethodMemberValue).orElse(null);
            }
        } catch (Exception e) {
            throw new JsonMapperException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <V> V getFieldMemberValue(Field field) {
        try {
            field.setAccessible(true);
            final Object rawValue = field.get(instance);

            if (getClassMember().getGetterValueConverter() != null) {
                return (V) getClassMember().getGetterValueConverter().convert(rawValue);
            } else {
                return (V) rawValue;
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new JsonMapperException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <V> V getMethodMemberValue(Method method) {
        try {
            final Object rawValue = method.invoke(instance);

            if (getClassMember().getGetterValueConverter() != null) {
                return (V) getClassMember().getGetterValueConverter().convert(rawValue);
            } else {
                return (V) rawValue;
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new JsonMapperException(e.getMessage(), e);
        }
    }

    public Optional<Method> getSetterMethod() {
        Method method = null;

        if (getClassMember().getSetterMethodReference() != null) {
            final MethodReference ref = getClassMember().getSetterMethodReference();

            try {
                assert ref.getParameterType() != null;
                method = getClassMember().getContainerClass().getMethod(ref.getName(), ref.getParameterType().getClass());
            } catch (NoSuchMethodException | SecurityException e) {
                throw new JsonMapperException("Error fetching setter method " + ref.getName(), e);
            }
        }

        return Optional.ofNullable(method);
    }

    private Optional<Method> getGetterMethod() {
        Method method = null;

        if (getClassMember().getGetterMethodReference() != null) {
            String refName = getClassMember().getGetterMethodReference().getName();

            try {
                method = getClassMember().getContainerClass().getMethod(refName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new JsonMapperException("Error fetching setter method " + refName, e);
            }
        }

        return Optional.ofNullable(method);
    }
}
