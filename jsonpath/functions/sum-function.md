# Sum

Returns a sum of all numeric context elements

## Syntax

```js
sum()
```

## Input Type

[`NUMBER`] Will only evaluate numeric contexts. All other types will be ignored.

## Arguments

None

## Returns

`[DOUBLE]` An aggregated sum of all numeric elements in context. Non-numeric elements are
ignored.

## Taxonomy

| Category | Classification |
|:---------|:---------------|
| PATH     | AGGREGATE      |

## Example

Using the "Books" example, return a sum of all book prices:

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

```
$..book.*.price.sum()
```

**Returns**

```json
[
    53.92
]
```

[[Functions](../Functions.md)]