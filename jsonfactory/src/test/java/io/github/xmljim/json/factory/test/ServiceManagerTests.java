package io.github.xmljim.json.factory.test;

import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.service.JsonService;
import io.github.xmljim.json.service.ServiceManager;
import io.github.xmljim.json.service.exception.JsonServiceProviderUnavailableException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceManagerTests {

    @Test
    void testGetProviderFails() {

        assertFalse(ServiceManager.isServiceAvailable(ElementFactory.class));
        assertThrows(JsonServiceProviderUnavailableException.class,
            () -> ServiceManager.getProvider(MapperFactory.class));
    }

    @Test
    void testGetProviderSuccess() {
        ParserFactory parserFactory = ServiceManager.getProvider(ParserFactory.class);
        assertTrue(parserFactory instanceof TestServiceClass);
    }

    @Test
    void testListServices() {
        Set<JsonService> serviceSet = ServiceManager.listServices();

        assertEquals(1, serviceSet.size());
        assertTrue(serviceSet.iterator().next() instanceof TestServiceClass);
    }

    @Test
    void testListServiceProviders() {
        assertTrue(ServiceManager.listProviders(ElementFactory.class).isEmpty());
        assertTrue(ServiceManager.listProviders(JsonPathFactory.class).isEmpty());

        Set<ParserFactory> serviceProviders = ServiceManager.listProviders(ParserFactory.class);
        assertEquals(1, serviceProviders.size());
        assertTrue(serviceProviders.iterator().next() instanceof TestServiceClass);
    }

}
