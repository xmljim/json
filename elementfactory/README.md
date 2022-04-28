# ElementFactory

This is a default reference implementation of the `io.xmljim.json.factory.model.ElementFactory` service. There is only
one public class, `io.xmljim.json.elementfactory.ElementFactoryImpl`, which implements the service, and provides methods
and a default implementation of the JSON Model.

This module includes transitive dependencies on `jsonfactory` and `model` modules. As a result, once loaded, you can
access the `ElementFactory` service through the `ServiceManager`:

```java
ElementFactory elementFactory=ServiceManager.getProvider(ElementFactory.class);

//create a new JsonObject:
    JsonObject newObject=elementFactory.newObject();
//add a property - internally uses ElementFactory to create a JsonValue
    newObject.put("key","value");

//create a new JsonArray:
    JsonArray newArray=elementFactory.newArray();
//add a value (boolean) - internally uses ElementFactory to create a JsonValue
    newArray.add(true);

//create a new JsonValue that can be applied to a node:
    JsonValue<String> stringValue=elementFactory.newValue("Another string");

//set the value
    newObject.put("another_key",stringValue);

    newArray.add(stringValue);
```