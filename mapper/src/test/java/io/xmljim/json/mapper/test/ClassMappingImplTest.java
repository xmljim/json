package io.xmljim.json.mapper.test;

import io.github.xmljim.json.factory.mapper.*;
import io.github.xmljim.json.mapper.MapperFactoryImpl;
import io.xmljim.json.mapper.test.testclasses.Person;
import io.xmljim.json.mapper.test.testclasses.TestRecord;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ClassMappingImplTest {

    @Test
    void testBasicClassMapping() {
        MapperFactory mapperFactory = new MapperFactoryImpl();
        MappingConfig mappingConfig = MappingConfig.with()
            .build();
        Mapping mapping = mapperFactory.newMapping(mappingConfig);

        ClassConfig classConfig = ClassConfig.with().sourceClass(Person.class).build();
        ClassMapping classMapping = mapperFactory.newClassMapping(mapping, classConfig);
        assertNotNull(classMapping);
        assertEquals(2L, classMapping.getMemberMappings().count());
    }

    @Test
    void testRenamedJsonKeysBasicMapping() throws NoSuchFieldException {


        Field lastNameField = Person.class.getDeclaredField("lastName");
        Field firstNameField = Person.class.getDeclaredField("firstName");

    }

    @Test
    void testWithRecord() {
        MapperFactory factory = new MapperFactoryImpl();
        Mapper mapper = factory.newMapper(MappingConfig.with()
            .appendClassConfig(ClassConfig.with()
                .sourceClass(TestRecord.class)
                .build())
            .build()
        );

        ClassMapping classMapping = mapper.getMapping().getClassMapping(TestRecord.class);
        assertNotNull(classMapping);
        assertTrue(classMapping.isRecord());
        assertEquals(4, classMapping.getMemberMappings().count());

        classMapping.getConstructorKeys().forEach(key -> {
            MemberMapping memberMapping = classMapping.getMemberMapping(key);
            assertNotNull(memberMapping);
        });

        Constructor<TestRecord> constructor = classMapping.getConstructor();
        assertNotNull(constructor);

    }
}