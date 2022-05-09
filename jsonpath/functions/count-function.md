# Count

Returns a count of all values in context

## Syntax

```js
count()
```

## Arguments

None

## Returns

`[LONG]` An aggregated count of all elements in context

## Taxonomy

| Category | Classification |
|:---------|:---------------|
| PATH     | AGGREGATE      |

## Example

Using the "Books" example, return a count of all books:

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
$..book.*.count()
```

**Returns**

```json
[
    4
]
```

[[Functions](../Functions.md)]