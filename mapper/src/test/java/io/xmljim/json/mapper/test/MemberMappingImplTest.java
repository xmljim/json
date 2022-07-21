package io.xmljim.json.mapper.test;

import io.github.xmljim.json.factory.mapper.*;
import io.github.xmljim.json.mapper.MapperFactoryImpl;
import io.xmljim.json.mapper.test.testclasses.Person;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MemberMappingImplTest {

    @Test
    void getJsonKey() throws NoSuchFieldException {
        MapperFactory mapperFactory = new MapperFactoryImpl();
        MappingConfig mappingConfig = MappingConfig.with()
            .appendClassConfig(ClassConfig.with().
                sourceClass(Person.class)
                .build())
            .build();

        Mapping mapping = mapperFactory.newMapping(mappingConfig);
        ClassMapping classMapping = mapping.getClassMapping(Person.class);

        Field firstNameField = Person.class.getDeclaredField("firstName");
        MemberMapping memberMapping = classMapping.getMemberMapping(firstNameField);

        assertNotNull(memberMapping);
        assertEquals("firstName", memberMapping.getJsonKey());
    }

    @Test
    void isIgnored() {
    }

    @Test
    void getContainerClass() {
    }

    @Test
    void getSetterMethod() {
    }

    @Test
    void getGetterMethod() {
    }

    @Test
    void getElementTargetClass() {
    }

    @Test
    void getJsonConverter() {
    }

    @Test
    void getFieldConverter() {
    }

    @Test
    void getNodeType() {
    }
}