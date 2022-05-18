# Type

Returns each context element's node type

## Syntax

```
type()
```

## Input Type

**[`ANY`]** - Evaluates each context node's type value to determine if it's an array

## Arguments

None

## Returns

`[STRING]` The element's node type string. The following table shows the return values:

| If the node type is a ... | Returns     |
|---------------------------|-------------|
| `JsonObject`              | `"object"`  |
| `JsonArray`               | `"array"`   |
| `Integer`                 | `"integer"` |
| `Long`                    | `"long"`    |
| `Double`                  | `"double"`  |
| `Boolean`                 | `"boolean"` |
| `String`                  | `"string"`  |
| `Null`                    | `"null"`    |

## See Also

[`is-array()`](is-array-function.md)

[`is-boolean()`](is-boolean-function.md)

## Taxonomy

| Category | Classification |
|:---------|:---------------|
| PATH     | NODE           |

## Examples

Using the "Books" example, evaluate the book element type:

```json 
{
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": 10
}
```

**JsonPath**

1: Evaluate the book element

```
$..book.type()
```

Returns:

```json
[
    "array"
]
```

2: Evaluate the book element's children

```
$..book.*.type()
```

Returns:

```json
[
    "object",
    "object",
    "object",
    "object"
]
```

3: Evaluate each book elements' types

```
$..book.*.*.type()
```

Returns

```json
[
    [
        "string",
        "string",
        "string",
        "double"
    ],
    [
        "string",
        "string",
        "string",
        "double"
    ],
    [
        "string",
        "string",
        "string",
        "string",
        "double"
    ],
    [
        "string",
        "string",
        "string",
        "string",
        "double"
    ]
]
```

[[Functions](../Functions.md)]