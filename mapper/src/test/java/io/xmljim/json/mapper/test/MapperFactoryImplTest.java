package io.xmljim.json.mapper.test;

import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.mapper.MapperFactoryImpl;
import io.github.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MapperFactory Tests")
class MapperFactoryImplTest {

    @DisplayName("Can instantiate a new MapperFactory from Service Manager")
    @Test
    void canInstantiateFactory() {
        System.out.println(ServiceManager.listServices());

        assertTrue(ServiceManager.isServiceAvailable(MapperFactory.class));
        MapperFactory factory = ServiceManager.getProvider(MapperFactory.class);
        assertNotNull(factory);

        assertTrue(factory instanceof MapperFactoryImpl);
    }
}