package io.github.xmljim.json.mapper.config;

import io.github.xmljim.json.factory.config.AbstractConfiguration;
import io.github.xmljim.json.factory.mapper.Converter;
import io.github.xmljim.json.factory.mapper.MemberConfig;
import io.github.xmljim.json.model.NodeType;
import io.github.xmljim.json.service.JsonServiceProvider;

import java.lang.reflect.Field;

public class MemberConfigImpl extends AbstractConfiguration implements MemberConfig {
    private MemberConfigImpl() {
        //no-arg constructor;
    }

    @Override
    public Field getField() {
        return get(MemberFields.FIELD);
    }

    @Override
    public String getJsonKey() {
        return get(MemberFields.JSON_KEY);
    }

    @Override
    public NodeType getNodeType() {
        return get(MemberFields.NODE_TYPE);
    }

    @Override
    public <T> Class<T> getContainerClass() {
        return get(MemberFields.CONTAINER_CLASS);
    }

    @Override
    public <T> Class<T> getElementTargetClass() {
        return get(MemberFields.ELEMENT_TARGET_CLASS);
    }

    @Override
    public Converter getFieldConverter() {
        return get(MemberFields.FIELD_CONVERTER);
    }

    @Override
    public Converter getJsonConverter() {
        return get(MemberFields.JSON_CONVERTER);
    }

    @Override
    public String getGetterMethodName() {
        return get(MemberFields.GETTER_METHOD_NAME);
    }

    @Override
    public String getSetterMethodName() {
        return get(MemberFields.SETTER_METHOD_NAME);
    }

    @Override
    public boolean isIgnored() {
        if (containsKey(MemberFields.IGNORED)) {
            return get(MemberFields.IGNORED);
        }
        return false;
    }

    @JsonServiceProvider(service = MemberConfig.Builder.class, version = "1.0.1")
    public static class MemberConfigBuilder implements MemberConfig.Builder {
        MemberConfigImpl memberConfig = new MemberConfigImpl();

        @Override
        public Builder field(Field field) {
            memberConfig.put(MemberFields.FIELD, field);
            return this;
        }

        @Override
        public Builder jsonKey(String jsonKey) {
            memberConfig.put(MemberFields.JSON_KEY, jsonKey);
            return this;
        }

        @Override
        public Builder nodeType(NodeType nodeType) {
            memberConfig.put(MemberFields.NODE_TYPE, nodeType);
            return this;
        }

        @Override
        public <T> Builder containerClass(Class<T> containerClass) {
            memberConfig.put(MemberFields.CONTAINER_CLASS, containerClass);
            return this;
        }

        @Override
        public <T> Builder elementTargetClass(Class<T> elementTargetClass) {
            memberConfig.put(MemberFields.ELEMENT_TARGET_CLASS, elementTargetClass);
            return this;
        }

        @Override
        public Builder fieldConverter(Converter fieldConverter) {
            memberConfig.put(MemberFields.FIELD_CONVERTER, fieldConverter);
            return this;
        }

        @Override
        public Builder jsonConverter(Converter jsonConverter) {
            memberConfig.put(MemberFields.JSON_CONVERTER, jsonConverter);
            return this;
        }

        @Override
        public Builder getterMethodName(String getterMethodName) {
            memberConfig.put(MemberFields.GETTER_METHOD_NAME, getterMethodName);
            return this;
        }

        @Override
        public Builder setterMethodName(String setterMethodName) {
            memberConfig.put(MemberFields.SETTER_METHOD_NAME, setterMethodName);
            return this;
        }

        @Override
        public Builder ignored(boolean ignored) {
            memberConfig.put(MemberFields.IGNORED, ignored);
            return this;
        }

        @Override
        public MemberConfig build() {
            //TODO: Add some validations?
            return memberConfig;
        }
    }

    private enum MemberFields {
        FIELD,
        JSON_KEY,
        NODE_TYPE,
        CONTAINER_CLASS,
        ELEMENT_TARGET_CLASS,
        FIELD_CONVERTER,
        JSON_CONVERTER,
        SETTER_METHOD_NAME,
        GETTER_METHOD_NAME,
        IGNORED
    }
}
