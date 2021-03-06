package io.github.xmljim.json.service;

import io.github.xmljim.json.service.exception.JsonPluginException;

import java.lang.module.ModuleDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class ClasspathScanner {
    private final Predicate<Class<?>> classFilter;


    public ClasspathScanner() {
        this.classFilter = clazz -> clazz.isAnnotationPresent(JsonServiceProvider.class);
    }

    public Set<AjpServiceClass> scan() {
        Set<AjpServiceClass> serviceClasses = new HashSet<>();

        ModuleLayer layer = ModuleLayer.boot();
        layer.modules().forEach(module -> {
            ModuleDescriptor descriptor = module.getDescriptor();

            Set<ModuleDescriptor.Provides> jsonServiceProviders = descriptor.provides().stream()
                .filter(provides -> isJsonService(provides.service()))
                .collect(Collectors.toSet());

            @SuppressWarnings("unchecked")
            Set<AjpServiceClass> providerClasses = jsonServiceProviders.stream()
                .flatMap(provides -> provides.providers().stream())
                .map(this::loadClass)
                .map(clazz -> new AjpServiceClass((Class<? extends JsonService>) clazz, Collections.singleton(clazz), classFilter))
                .collect(Collectors.toSet());

            serviceClasses.addAll(providerClasses);
        });

        return serviceClasses;
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonPluginException(e);
        }
    }


    private boolean isJsonService(String className) {
        Class<?> theClass = loadClass(className);
        return Arrays.stream(theClass.getInterfaces()).anyMatch(aClass -> aClass.isAssignableFrom(JsonService.class));
    }

    public record AjpServiceClass(JsonServiceProvider serviceProvider, Set<Class<?>> serviceClasses) {

        public AjpServiceClass(Class<? extends JsonService> service, Set<Class<?>> allClasses, Predicate<Class<?>> filter) {
            this(service.getDeclaredAnnotation(JsonServiceProvider.class), allClasses.stream().filter(filter).collect(Collectors.toSet()));
        }
    }
}
