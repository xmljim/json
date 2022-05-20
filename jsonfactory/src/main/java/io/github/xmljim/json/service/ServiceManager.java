package io.github.xmljim.json.service;

import io.github.xmljim.json.service.exception.JsonPluginException;
import io.github.xmljim.json.service.exception.JsonServiceProviderUnavailableException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public final class ServiceManager {

    private static ServiceManager instance;
    private final boolean isLoaded;
    private final Map<Class<? extends JsonService>, Set<ServiceProvider>> serviceProviderMap = new HashMap<>();
    private static final VersionExpression defaultVersion = new VersionExpression("~1.0.0");

    private ServiceManager() {
        isLoaded = loadServices();
    }

    private boolean loadServices() {
        ClasspathScanner scanner = new ClasspathScanner();
        Set<ClasspathScanner.AjpServiceClass> classes = scanner.scan();

        classes.forEach(service -> {
            service.serviceClasses().forEach(clazz -> {
                ServiceProvider serviceProvider = new ServiceProvider(service.serviceProvider(), clazz, service.getClass().getModule().getName());
                serviceProviderMap.computeIfAbsent(serviceProvider.serviceClass(), V -> new HashSet<>()).add(serviceProvider);
            });
        });

        return true;
    }

    private static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }

        return instance;
    }

    public boolean isLoaded() {
        return isLoaded;
    }


    public static <T extends JsonService> boolean isServiceAvailable(Class<T> service) {
        Optional<Set<ServiceProvider>> serviceProviders = getInstance().getServiceProviders(service);
        return serviceProviders.filter(providers -> getInstance().pickProvider(providers, defaultVersion).isPresent()).isPresent();
    }

    public static <T extends JsonService> boolean isServiceAvailable(Class<T> service, String versionExpression) {
        VersionExpression ve = new VersionExpression(versionExpression);
        Optional<Set<ServiceProvider>> serviceProviders = getInstance().getServiceProviders(service);
        return serviceProviders.filter(providers -> getInstance().pickProvider(providers, ve).isPresent()).isPresent();
    }

    @SuppressWarnings("unchecked")
    public static <T extends JsonService> Set<T> listServices() {
        return getInstance().serviceProviderMap.values().stream()
            .flatMap(Collection::stream)
            .map(value -> getInstance().newInstance((Class<T>) value.providerClass))
            .collect(Collectors.toSet());

    }

    @SuppressWarnings("unchecked")
    public static <T extends JsonService> Set<T> listProviders(Class<T> service) {
        return getInstance().serviceProviderMap.getOrDefault(service, Collections.emptySet()).stream()
            .map(provider -> getInstance().newInstance((Class<T>) provider.providerClass()))
            .collect(Collectors.toSet());
    }


    public static <T extends JsonService> T getProvider(Class<T> serviceClass) {
        return getInstance().loadProvider(serviceClass, defaultVersion);
    }

    public static <T extends JsonService> T getProvider(Class<T> serviceClass, String versionExpression) {
        return getInstance().loadProvider(serviceClass, new VersionExpression(versionExpression));
    }

    @SuppressWarnings("unchecked")
    private <T extends JsonService> T loadProvider(Class<T> serviceClass, VersionExpression versionExpression) {
        Optional<Set<ServiceProvider>> serviceProviders = getServiceProviders(serviceClass);

        if (serviceProviders.isPresent()) {
            Optional<ServiceProvider> provider = pickProvider(serviceProviders.get(), versionExpression);
            if (provider.isPresent()) {
                return newInstance((Class<T>) provider.get().providerClass);
            } else {
                throw new JsonServiceProviderUnavailableException("No provider available for service: " + serviceClass.getSimpleName() +
                    " with Version qualifier: " + versionExpression.toString());
            }
        }

        throw new JsonServiceProviderUnavailableException("No provider available for service: " + serviceClass.getSimpleName());
    }

    private <T extends JsonService> T newInstance(Class<T> providerClass) {
        try {
            Constructor<T> ctor = providerClass.getConstructor();
            return ctor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new JsonPluginException(e.getMessage(), e);
        }
    }

    private <T extends JsonService> Optional<Set<ServiceProvider>> getServiceProviders(Class<T> service) {
        return Optional.ofNullable(serviceProviderMap.getOrDefault(service, null));
    }

    private Optional<ServiceProvider> pickProvider(Set<ServiceProvider> providers, VersionExpression versionExpression) {
        if (providers.isEmpty()) {
            return Optional.ofNullable(null);
        } else if (providers.size() == 1) {
            return Optional.of(providers.iterator().next());
        } else {
            Set<ServiceProvider> filteredVersions = providers.stream()
                .filter(sp -> sp.version.evaluateVersion(versionExpression))
                .collect(Collectors.toSet());

            List<ServiceProvider> sorted =
                filteredVersions.stream()
                    .sorted(Comparator.comparing(ServiceProvider::priority)
                        .thenComparing(ServiceProvider::version)
                        .thenComparing(ServiceProvider::isNative)
                        .reversed())
                    .toList();

            return Optional.ofNullable(sorted.iterator().next());
        }
    }

    private record ServiceProvider(Class<? extends JsonService> serviceClass, Class<?> providerClass,
                                   Version version, int priority, boolean isNative, String module) {

        public ServiceProvider(Class<?> providerClass, String module) {
            this(providerClass.getAnnotation(JsonServiceProvider.class), providerClass, module);
        }

        public ServiceProvider(JsonServiceProvider serviceProvider, Class<?> providerClass, String module) {
            this(serviceProvider.service(), providerClass, new Version(serviceProvider.version()),
                serviceProvider.priority(), serviceProvider.isNative(), module);
        }
    }
}
