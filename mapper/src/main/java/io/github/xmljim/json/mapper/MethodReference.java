package io.github.xmljim.json.mapper;

import io.github.xmljim.json.mapper.exception.JsonMapperException;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class MethodReference {
    private final MethodType methodType;
    private final String name;
    private final boolean accessible;
    private final boolean isPublic;
    private final Type contextType;

    public MethodReference(MethodType methodType, String name, boolean accessible, boolean isPublic, Type contextType) {
        this.methodType = methodType;
        this.name = name;
        this.accessible = accessible;
        this.isPublic = isPublic;
        this.contextType = contextType;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public String getName() {
        return name;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Type getParameterType() {
        if (getMethodType() == MethodType.SETTER) {
            return contextType;
        } else {
            return null;
        }
    }

    public Type getReturnType() {
        if (getMethodType() == MethodType.GETTER) {
            return contextType;
        } else {
            return Void.TYPE;
        }
    }

    public <T> Method getMethodFromInstance(T instance) {
        try {
            if (getMethodType() == MethodType.SETTER) {
                return instance.getClass().getMethod(getName(), contextType.getClass());
            } else {
                return instance.getClass().getMethod(getName());
            }
        } catch (NoSuchMethodException nsme) {
            throw new JsonMapperException(nsme);
        }
    }

    public enum MethodType {
        GETTER,
        SETTER
    }
}
