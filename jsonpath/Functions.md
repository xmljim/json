# Functions

- [Path Functions](#path-functions)
    - [Aggregate Functions](#aggregate-functions)
    - [Node Functions](#node-functions)
- [Built-in Function Index](#built-in-function-index)

## Path Functions

Path functions can be appended to a JsonPath expression and returns non-contextual value(s)

### Aggregate Functions

Aggregate functions return a single value based on computation of all elements in context

| Function                                   | Description                                          |
|--------------------------------------------|------------------------------------------------------|
| [`average`](functions/average-function.md) | returns the average of all numeric values in context |
| [`count`](functions/count-function.md)     | returns a count of all values in context             |
| [`sum`](functions/sum-function.md)         | returns a sum of all numeric context elements        |

## Node Functions

| Function                                               | Description                                        |
|--------------------------------------------------------|----------------------------------------------------|
| [`is-array`](functions/is-array-function.md)           | returns true if the node type is an array          |
| [`is-boolean()`](functions/is-boolean-function.md)     | returns true if the node type is a boolean value   |
| [`is-null()`](functions/is-null-function.md)           | returns true if the node type is null              |
| [`is-numeric()`](functions/is-numeric-function.md)     | returns true if the node type is numeric           |
| [`is-object()`](functions/is-object-function.md)       | returns true if the node type is an object         |
| [`is-primitive()`](functions/is-primitive-function.md) | returns true if the node type is a primitive value |
| [`is-string()`](functions/is-string-function.md)       | returns true if the node type is string            |

## Built-In Function Index

| Function                                                 | Description                                                                                     |
|----------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| [`average()`](functions/average-function.md)             | returns the average of all numeric values in context                                            |
| concat([expr...])                                        | Concatenates a string from a set of expressions                                                 |
| [`count()`](functions/count-function.md)                 | returns a count of all values in context                                                        |
| [`count-if(list, test)`](functions/count-if-function.md) | returns a count of values based on a predicate                                                  |
| distinct-values()                                        | returns an array of distinct values for each context element                                    |
| ends-with(suffix)                                        | evaluates a string in context for matching the end characters                                   |
| [`is-array()`](functions/is-array-function.md)           | return true if the node type is an array                                                        |
| [`is-boolean()`](functions/is-boolean-function.md)       | returns true if the node type is a boolean value                                                |
| is-null()                                                | returns true if the node type value is null                                                     |
| is-numeric()                                             | Evaluates whether the context element is a numeric value                                        |
| is-object()                                              | returns true if the node type in context is an object                                           |
| is-primitive()                                           | returns if the node type for each context element is a primitive type                           |
| is-string()                                              | returns true if the node argType is a string value                                              |
| keys()                                                   | returns keys for JsonObject instances; all other types return an empty array                    |
| length()                                                 | returns the length/size of an array, object or string node; returns -1 for all other node types |
| lowercase()                                              | converts all string values in context to lowercase                                              |
| replace(match, replace)                                  | replaces string match values with replacement string for all context elements                   |
| split(regex)                                             | splits a string into an array of strings from a match expression                                |
| starts-with(prefix, [offset])                            | evaluates a string in context for matching the start characters                                 |
| substring(start, [end])                                  | returns a substring for string elements in context                                              |
| [`sum()`](functions/sum-function.md)                     | returns the sum of all numeric values in context                                                |
| sum-if(list, test, valueExpr)                            | returns a sum of values based on a predicate                                                    |
| trim()                                                   | removes all surrounding whitespace from all string values in context                            |
| [`type()`](functions/type-function.md)                   | Returns the node argType for each element in context                                            |
| uppercase()                                              | converts all string elements in context to uppercase                                            |
| values()                                                 | returns an array of values for each context element                                             |

