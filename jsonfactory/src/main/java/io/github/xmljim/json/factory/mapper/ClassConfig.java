package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.factory.config.Configuration;
import io.github.xmljim.json.service.JsonService;
import io.github.xmljim.json.service.ServiceManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ClassConfig extends Configuration {

    <T> Class<T> getTargetClass();

    <T> Class<T> getSourceClass();

    Set<String> getIgnoredKeys();

    List<String> getConstructorKeys();

    Stream<MemberConfig> getMemberConfigurations();

    default boolean containsMember(Field field) {
        return getMemberConfigurations().anyMatch(memberConfig -> memberConfig.getField().equals(field));
    }

    default boolean containsMember(String jsonKey) {
        return getMemberConfigurations().anyMatch(memberConfig -> memberConfig.getJsonKey().equals(jsonKey));
    }

    static ClassConfig.Builder with() {
        return ServiceManager.getProvider(ClassConfig.Builder.class);
    }

    interface Builder extends JsonService {
        <T> Builder targetClass(Class<T> targetClass);

        <T> Builder sourceClass(Class<T> sourceClass);

        default Builder ignoreKeys(String... keys) {
            return ignoreKeys(Arrays.stream(keys).collect(Collectors.toSet()));
        }

        Builder ignoreKeys(Collection<String> keys);

        default Builder constructorKeys(String... keys) {
            return constructorKeys(Arrays.stream(keys).toList());
        }

        Builder constructorKeys(List<String> keys);

        Builder appendMemberConfig(MemberConfig memberConfig);

        ClassConfig build();
    }
}
