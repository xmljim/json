# JsonFactory

The `JsonFactory` module acts as the orchestration layer between various modules by exposing interfaces and services
that are provided by different modules.

All services typically follow a *Factory* design pattern and all extend the `JsonService`. For
example, the `ElementFactory` interface extends `JsonService` and provides methods for creating concrete instances of
the JSON Model.

## ServiceManager

The `ServiceManager` class provides static methods for instantiating *Service Providers*. Service
Providers are implementations for each `JsonService` sub-interface.

The primary methods for creating Service Provider instances are:

- `static <T extends JsonService> T getProvider(Class<T> serviceClass)`: Creates an instance of the Service Provider
  designated by the `serviceClass` parameter, provided that there is at least one provider implementation for that
  class. For example:

  ```java
     ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);
  ```

  If no Service Provider implementation class is not found, `JsonServiceProviderUnavailableException` will be thrown.

- `static <T extends JsonService> T getProvider(Class<T> serviceClass, String versionExpression)`: Creates a Service
  Provider for the specified `serviceClass` and matches the specified `versionExpression`, which is a valid Semantic
  Version. Service Provider instances will include a `JsonServiceProvider` annotation that includes a
  `version` property that will be used to evaluate whether the provider version matches the specified criteria.

There are other useful methods that can be used for accessing Service Providers:

- `static <T extends JsonService> boolean isServiceAvailable(Class<T> service)`: returns `true` if one or more Service
  Providers are available for a given `service` class.

- `static <T extends JsonService> boolean isServiceAvailable(Class<T> service, String versionExpression)`: returns
  `true` if one or more Service Providers are available for a given `service` class _and_ match the version criteria.

- `static <T extends JsonService> Set<T> listServices()`: Provides a set of all available services

- `static <T extends JsonService> Set<T> listProviders(Class<T> service)`: Provides a set of all Service Providers for a
  given `service` class.

## Exposing Service Providers

All Service Providers must implement a specific `JsonService` subinterface. Additionally, the implementation class must
be decorated with the `JsonServiceProvider` annotation, which includes properties that are read by the `ServiceManager`
for evaluating implementations for the desired instance. For example let's assume you have a new Service Provider for
the `ElementFactory`, which is used to instantiate concrete JSON Model classes and values. The following shows what this
class might look like:

```java
import ElementFactory;
import JsonServiceProvider;

@JsonServiceProvider(service = ElementFactory.class, version = "2.0.1")
public class MyElementFactory implements ElementFactory {
// implementation code here...
}
```

The `MyElementFactory` class directly implements the designated Service. Additionally, it's decorated with
a `@JsonServiceProvider` annotation that specifies the `service` class this implementation is providing services for,
and a `version` that indicates the implementation version.

### Exposing the Provider in `module-info`

The last step is to expose the Service Provider in your module's `module-info` file:

```java
import ElementFactory;

module myjson.elementfactory {
    requires transitive io.github.xmljim.json.factory; //note this will also pull in the JSON Model
    //export your package(s)
    exports your.package.name.a;
    exports your.package.name.b;

    //Now specify the service provider with your concrete provider class
    provides ElementFactory with MyElementFactory;

}
```

Once this is loaded, you can now use your implementation:

```java
ElementFactory elementFactory=ServiceManager.getProvider(ElementFactory.class,"2.0.1");
```

## Services

The following Services are defined in this module:

- `ElementFactory`: Interface that creates JSON Model instances.
- `ParserFactory`: Interface for creating a `Parser` instance for loading JSON files.
- `MapperFactory`: Interface for marshalling and unmarshalling JSON and Java classes.
- `MergeFactory`: Interface for merging two JSON instances into a single JSON instance.