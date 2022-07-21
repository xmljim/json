package io.github.xmljim.json.mapper.config;

import io.github.xmljim.json.factory.config.AbstractConfiguration;
import io.github.xmljim.json.factory.mapper.ClassConfig;
import io.github.xmljim.json.factory.mapper.MemberConfig;
import io.github.xmljim.json.service.JsonServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ClassConfigImpl extends AbstractConfiguration implements ClassConfig {
    private final List<MemberConfig> memberConfigs = new ArrayList<>();

    private ClassConfigImpl() {
        //no-args constructor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getTargetClass() {
        return (Class<T>) get(ClassFields.TARGET_CLASS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getSourceClass() {
        return (Class<T>) get(ClassFields.SOURCE_CLASS);
    }

    @Override
    public Set<String> getIgnoredKeys() {
        return get(ClassFields.IGNORE_KEYS);
    }

    @Override
    public List<String> getConstructorKeys() {
        return get(ClassFields.CONSTRUCTOR_KEYS);
    }

    @Override
    public Stream<MemberConfig> getMemberConfigurations() {
        return memberConfigs.stream();
    }

    private void appendMemberConfig(MemberConfig memberConfig) {
        memberConfigs.add(memberConfig);
    }

    @JsonServiceProvider(service = ClassConfig.Builder.class, version = "1.0.1", isNative = true)
    public static class ClassConfigBuilder implements ClassConfig.Builder {
        private final ClassConfigImpl classConfig = new ClassConfigImpl();

        @Override
        public <T> Builder targetClass(Class<T> targetClass) {
            classConfig.put(ClassFields.TARGET_CLASS, targetClass);
            return this;
        }

        @Override
        public <T> Builder sourceClass(Class<T> sourceClass) {
            classConfig.put(ClassFields.SOURCE_CLASS, sourceClass);
            return this;
        }

        @Override
        public Builder ignoreKeys(Collection<String> keys) {
            classConfig.put(ClassFields.IGNORE_KEYS, keys);
            return this;
        }

        @Override
        public Builder constructorKeys(List<String> keys) {
            classConfig.put(ClassFields.CONSTRUCTOR_KEYS, keys);
            return this;
        }

        @Override
        public Builder appendMemberConfig(MemberConfig memberConfig) {
            classConfig.appendMemberConfig(memberConfig);
            return this;
        }

        @Override
        public ClassConfig build() {
            //TODO: validate?
            if (classConfig.getTargetClass() == null) {
                classConfig.put(ClassFields.TARGET_CLASS, classConfig.get(ClassFields.SOURCE_CLASS));
            }

            return classConfig;
        }
    }

    private enum ClassFields {
        TARGET_CLASS,
        SOURCE_CLASS,
        IGNORE_KEYS,
        CONSTRUCTOR_KEYS
    }
}
