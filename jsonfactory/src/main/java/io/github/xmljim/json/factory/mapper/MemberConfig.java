package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.factory.config.Configuration;
import io.github.xmljim.json.model.NodeType;
import io.github.xmljim.json.service.JsonService;
import io.github.xmljim.json.service.ServiceManager;

import java.lang.reflect.Field;

public interface MemberConfig extends Configuration {

    Field getField();

    String getJsonKey();

    NodeType getNodeType();

    <T> Class<T> getContainerClass();

    <T> Class<T> getElementTargetClass();

    Converter getFieldConverter();

    Converter getJsonConverter();

    String getGetterMethodName();

    String getSetterMethodName();

    boolean isIgnored();

    public static Builder with() {
        return ServiceManager.getProvider(MemberConfig.Builder.class);
    }

    interface Builder extends JsonService {

        Builder field(Field field);

        Builder jsonKey(String jsonKey);

        Builder nodeType(NodeType nodeType);

        <T> Builder containerClass(Class<T> containerClass);

        <T> Builder elementTargetClass(Class<T> elementTargetClass);

        Builder fieldConverter(Converter fieldConverter);

        Builder jsonConverter(Converter jsonConverter);

        Builder getterMethodName(String getterMethodName);

        Builder setterMethodName(String setterMethodName);

        Builder ignored(boolean ignored);

        MemberConfig build();
    }
}
