# Count-If

Returns a count of values based on a predicate condition.

This function is intended to be used within a _Predicate_ expression

## Syntax

```
count-if (list, test)
```

## Arguments

- `list` - [`LIST`] An expression that contains the list of elements to be evaluated for counting
- `test` - [`PREDICATE`] The predicate expression to evaluate the `list` parameters for inclusion into testing
-

## Returns

`[LONG]` An aggregated count of all elements that meet the list conditions' predicate

## Taxonomy

| Category  | Classification |
|:----------|:---------------|
| PREDICATE | AGGREGATE      |

## Examples

The following example shows an array of school assignments by type code and status.
The following examples demonstrates how to use `count-if` in a predicate expression
to select schools that have the count of both `doc_type_id` and `doc_status` meet
specific criteria and also meet a threshold count value.

```json
[
    {
        "id": 1,
        "school_cd": 18712,
        "assignments": [
            {
                "doc_id": 1,
                "doc_type_id": 11,
                "doc_status": "ASSIGNED"
            },
            {
                "doc_id": 2,
                "doc_type_id": 11,
                "doc_status": "PROCESSED"
            },
            {
                "doc_id": 3,
                "doc_type_id": 19,
                "doc_status": "PROCESSED"
            },
            {
                "doc_id": 4,
                "doc_type_id": 19,
                "doc_status": "ASSIGNED"
            },
            {
                "doc_id": 5,
                "doc_type_id": 19,
                "doc_status": "ASSIGNED"
            }
        ]
    },
    {
        "id": 2,
        "school_cd": 18713,
        "assignments": [
            {
                "doc_id": 12,
                "doc_type_id": 11,
                "doc_status": "ASSIGNED"
            },
            {
                "doc_id": 22,
                "doc_type_id": 11,
                "doc_status": "PROCESSED"
            },
            {
                "doc_id": 32,
                "doc_type_id": 19,
                "doc_status": "PROCESSED"
            },
            {
                "doc_id": 42,
                "doc_type_id": 19,
                "doc_status": "ASSIGNED"
            },
            {
                "doc_id": 52,
                "doc_type_id": 19,
                "doc_status": "PROCESSED"
            },
            {
                "doc_id": 62,
                "doc_type_id": 19,
                "doc_status": "ASSIGNED"
            },
            {
                "doc_id": 72,
                "doc_type_id": 19,
                "doc_status": "PROCESSED"
            }
        ]
    }
]

```

**Example 1**: Select schools where the number of documents with `doc_type_id` = 19
_and_ `doc_status` = 'PROCESSED' _and_ the count is greater than or equal to 3.

``` 
$.*[?(count-if(@.assignments.*, @.doc_type_id == 19 && @.doc_status == 'PROCESSED') >= 3)]
```

> **Explanation**: The path selects all schools and applies a predicate filter that
> test each `assignments` element that match `doc_status` and `doc_type_id` criteria
> and evaluate the count against the final threshold (3).

Returns one result (`id`:`2`):

```json
 {
    "id": 2,
    "school_cd": 18713,
    "assignments": [
        {
            "doc_id": 12,
            "doc_type_id": 11,
            "doc_status": "ASSIGNED"
        },
        {
            "doc_id": 22,
            "doc_type_id": 11,
            "doc_status": "PROCESSED"
        },
        {
            "doc_id": 32,
            "doc_type_id": 19,
            "doc_status": "PROCESSED"
        },
        {
            "doc_id": 42,
            "doc_type_id": 19,
            "doc_status": "ASSIGNED"
        },
        {
            "doc_id": 52,
            "doc_type_id": 19,
            "doc_status": "PROCESSED"
        },
        {
            "doc_id": 62,
            "doc_type_id": 19,
            "doc_status": "ASSIGNED"
        },
        {
            "doc_id": 72,
            "doc_type_id": 19,
            "doc_status": "PROCESSED"
        }
    ]
}
```
