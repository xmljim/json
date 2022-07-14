# JSON Library

There are pleny of Java-based JSON libraries. All of the do their job very, very well. However, I originally developed
this library back in 2007/2008 to support some functionality I needed that the JSON libraries at the time didn't have.  
Originally developed and compiled with Java 6, it's slowly evolved as newer Java versions were released. Up until
recently, my JEP library was compiled with Java 8.

It's time to upgrade again. This time, all the way to Java 17, the latest LTS release. It was also time for a
significant refactor of the architecture. Instead of a single monolithic Jar file, this library takes advantage of the
Java module system, with each of the constituent pieces designed in a modular fashion.

The benefit of this approach is that it makes easier to build a pluggable architecture where someone can pull in the
pieces they need, rather than one giant "uber-jar" of everything.  Over time, the goal is to create a "toolbox" of
components (services) that can be assembled together on the module path that provide only what is needed. The idea
is that each service should be plug and play, provided that they adhere to the Service interfaces provided in the 
JsonModel and JsonFactory. Of course, a reference implementation will be provided, but there is no constraint for 
building and using your own.


## Core Components

There are only two core component modules that are common to each of the "pluggable" service components using a Service
Locator design pattern:

- **Json Model** (Module: `io.github.xmljim.json.model`):  This contains the interfaces that represent the Json Data model,
  e.g., `JsonArray`, `JsonObject`,  and `JsonValue<?>`.  
- **Json Factory** (Module: `io.github.xmljim.json.factory`):  This has a direct dependency on the Json Model, and provides the
  interfaces that provide the base API for each service component. These are tied together with a `ServiceManager` provides the
  orchestration for all services by instantiating the concrete implementation of the exposed services.
  
### Json Factory Services

The Json Factory module exposes the following services:

- **ElementFactory** (`io.github.xmljim.json.factory.model.ElementFactory`): This service enables the creation of Json
  elements that implement the Json Data Model interfaces.
  
- **ParserFactory** (`io.github.xmljim.json.factory.parser.ParserFactory`): Service for parsing JSON data.

- **MapperFactory** (`io.github.xmljim.json.factory.mapper.MapperFactory`): Service marshalling and unmarshalling Java 
  objects (classes, records) to and from JSON.
  
- **MergeFactory** (`io.github.xmljim.json.factory.merge.MergeFactory`): Service for merging two JSON instances into a single JSON 
  node.
  
- **JsonPath** (`io.github.xmljim.json.factory.jsonpath.JsonPathFactory`): Service for executing JSONPath expressions on a JSON
  instance.
 

### Service Provider Components

Service components are separate Java modules that provide a "reference" implementation for a given service interface defined 
in the Json Factory module. There is a clear separation of concerns between these interfaces that define discrete behavior. 
Each of the modules here has no explicit knowledge or dependency on any of the other reference modules.  Instead, if a service module requires 
services from another module, it will "consult" the JsonFactory's `ServiceManager` to access a provider for that service. 

- **ElementFactory** (Module: `io.github.xmljim.json.elementfactory`): Implements and provides services to the `ElementFactory`
  interface. Internally it manages the concrete classes to each of the Json Model interfaces.

- **Json Parser** (Module: `io.github.xmljim.json.parser`): Implements and provides services to the `ParserFactory` interface,
  through parsing JSON data into an in-memory JSON data model

- **Json Mapper** (Module: `io.github.xmljim.json.mapper`): Implements and provides object marshalling and unmarshalling service
  to the `MapperFactory` interface.

- **Json Merger** (Module: `io.github.xmljim.json.merge`): Implements and provides services to the `MergerFactory` interface.

- **JsonPath** (Module: `io.github.xmljim.json.jsonpath`): Implements and provides services to the `JsonPathFactory` interface.




