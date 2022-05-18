# Is-Boolean

Evaluates the context element's node type if it is an array type

## Syntax

```
is-boolean()
```

## Input Type

[`ANY`] Evaluates each context node's type value to determine if it's an array

## Arguments

None

## Returns

`[BOOLEAN]` `true` if the context element's node type is boolean; `false` otherwise

## See Also

[`type()`](type-function.md)

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
                "price": 8.95,
                "available": true
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99,
                "available": true
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99,
                "available": true
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99,
                "available": false
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

1: Evaluate if a book's available property is a boolean type

```
$..book.*.available.is-boolean()
```

2: Evaluate if a book's title property is a boolean type

```
$..book.*.title.is-boolean()
```

**Returns**

1:

```json
[
    true,
    true,
    true,
    true
]
```

2:

```json
[
    false,
    false,
    false,
    false
]
```

[[Functions](../Functions.md)]