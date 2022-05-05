# Functions

## Node Functions

### `type()`

Returns the node type for each element in context.

**Input**

`Context<?>`: Context of any type

**Arguments**

None

**Returns**

*[String, Non-contextual]*: The node type value for each context element

**Examples**

Example 1: Return the book object types:

```javascript
$..book[:2].type()
```

returns:

```json
[
    "object",
    "object"
]
```

Example 2: Return the type from all author names:

```javascript
$['store'][..]['author'].type()  
```

returns

```json
[
    "string",
    "string",
    "string",
    "string"
]
```

### `is-primitive()`

Returns `true` if the context element is a primitive value (`string`, `<number-type>`, `boolean`)

### `is-numeric()`

Returns `true` if the context element is a numeric value (`long`, `integer`, `double`)

### `is-string()`

Returns `true` if the context element is a string value

### `is-boolean()`

Returns `true` if the context element is a boolean value

### `is-null()`

Returns `true` if the context element is a null value

### `is-array()`

Returns `true` if the context element is an array value

### `is-object()`

Returns `true` if the context element is an object value

### `length()`

### `keys()`

Returns an array of keys for a given context element.

**Returns**

[Array, Non-contextual]: If the node type is `object`, it returns the keys for that object. For `array` contexts, it
returns the index values. For all others (value types), it returns an empty array;

### `values()`

Returns an array of values for each context element

**Returns**

[Array, Non-contextual]: If the node type is `object`, it returns the keys for that object. For `array` contexts, it
returns the index values. For all others (value types), it returns an empty array;

### `distinct-values()`

Returns an array of distinct values for each context element

**Returns**

[Array, Non-contextual]: If the node type is `object` and `array` contexts, it returns an array of distinct values of
its contents; for primitive values, it returns an array of itself.

### `concat(expr [, expr [...]])`

### `value-list(expr [, expr [...]])`

### `value-map([item_paths],[key_names])`

### `substring()`

### `lowercase()`

### `uppercase()`

### `trim()`

### `replace()`

## Date Functions

### `date(formatExpression)`

## Aggregate Functions

Aggregate functions return a single value from an evaluation of every element in context.

### `sum()`

Returns the sum of all numeric values in context

### `average()`

### `count()`

### `flatten()`

### `distinct()`

## Predicate Expression Functions

Predicate expression functions are treated as first-class expressions for Predicates and Function arguments.

### `value-of(expr [, expr [...]])`

### `concat-of(expr [, expr [...]])`

**See Also**

[concat()](#concatexpr--expr-)

### `type-of(expr)`

**See Also**

[type()](#type)

### `distinct-of(expr)`

### `sum-of(expr)`

### `average-of(expr)`

## Logical Functions

Logical functions take a predicate to evaluate a condition before performing an operation

### `if(predicate, then-expression, else-expression)`

### `sum-if(predicate, expr)`

### `average-if(predicate, expr)`