package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.model.JsonObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface ClassMapping {
    ClassConfig getClassConfig();

    Mapping getMapping();

    <T> Class<T> getSourceClass();

    <T> Class<T> getTargetClass();

    boolean isPublic();

    boolean isRecord();

    List<String> getConstructorKeys();

    Stream<MemberMapping> getMemberMappings();

    MemberMapping getMemberMapping(Field var1);

    MemberMapping getMemberMapping(String var1);

    Set<String> getIgnoredKeys();

    void appendMemberMapping(MemberMapping var1);


    <T> Constructor<T> getConstructor();

    <T> T toClass(JsonObject jsonObject);
}