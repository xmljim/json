package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.NodeType;

import java.lang.reflect.Type;

public interface MemberMapping {
    String getJsonKey();

    NodeType getNodeType();

    String getFieldName();

    Type getFieldType();

    boolean isIgnored();

    boolean isAccessible();

    <T> Class<T> getContainerClass();

    String getSetterMethodName();

    String getGetterMethodName();

    <T> Class<T> getElementTargetClass();

    Converter getJsonConverter();

    Converter getFieldConverter();

    <T> void applyToClass(JsonObject var1, T var2);

    <T> void applyToJson(T var1, JsonObject var2);

    <T> T getValue(JsonObject var1);

    ClassMapping getClassMapping();
}
