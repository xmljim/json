## JSON Library

There are pleny of Java-based JSON libraries.  All of the do their job very, very well.  However, I originally developed this library back in 2007/2008 to support some functionality I needed that the JSON libraries at the time didn't.  Originally developed and compiled with Java 6, it's slowly evolved as newer Java versions were released.  Up until recently, my JEP library was compiled with Java 8.

It's time to upgrade again.  This time, all the way to Java 17, the latest LTS release.  It was also time for a significant refactor of the architecture.  Instead of a single monolithic Jar file, this library takes advantage of the Java module system, with each of the constituent pieces designed in a modular fashion.

The benefit of this approach is that it makes easier to build a pluggable architecture where someone can pull in the pieces they need, rather than one giant "uber-jar" of everything.

### Components

There are only two core component modules that are common to each of the "pluggable" components:

- Json Model:  This contains the interfaces that represent the Json Data model, e.g., `JsonArray`, `JsonObject`, and `JsonValue<?>`
- Json Factory:  This has a direct dependency on the Json Model, and provides the interfaces that provide the base API for each of the components.  These are tied together with a `ServiceManager` that allows for the indirect instantiation of each component as a service.


