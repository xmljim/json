---
layout: page
title: "JSON Data Model"
permalink: /api/model
---
# JSON Data Model

**Module Name:** `io.github.xmljim.json.model`

The JSON Data Model are a set of interfaces that represent a JSON data instance.  At a high level, a JSON instance consists of three primary 
`JsonElement` types:


- `JsonObject`: Represents an associative array of key-value pairs, where each key is a String, a value is any valid Json value type. From a Java perspective,
  this an analog to a `Map<String,JsonValue<?>`.  Key order is not guaranteed.
- `JsonArray`: Represents an seqential list of values, where a value is any valid Json value type. The Java equivalent is `List<JsonValue<?>>`. The
  order of values must be guaranteed.
- `JsonValue<?>`: Represents a Json value. Values can be primitive (Number, Boolean, String), `JsonObject`, `JsonArray` or `null`.  A number value can be 
  a `long`, `int`, `double`, or `float`.
  
  
## `JsonElement` Interface

The `JsonElement` interface is the topmost interface for the Json Data Model, and contains the following methods:

- `NodeType type()`: Returns the value type for the element
- `toJsonString()`: Returns a formatted Json string for that element
- `prettyPrint()`: Returns a formatted, pretty-print indented Json string
- `asString()`: Attempts to cast the element to a String value. Throws a `ClassCastException` if the value is not a `NodeType.STRING`
- 

