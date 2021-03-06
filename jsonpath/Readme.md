# JsonPath

## Contents

- [Overview](#overview)
- [Path Expressions](#path-expressions)
    - [Filters](#filters)
        - [Root Filter](#the-root-filter-)
        - [Current Element Filter](#the-current-element-filter-)
        - [Wildcard Filter](#the-wildcard-filter-)
        - [Recursion Filter](#the-recursion-filter-)
        - [Dot Key Accessor Child Filter](#the-dot-key-accessor-child-filter-)
        - [Bracketed Accessor Child Filter](#the-bracketed-accessor-child-filter-)
        - [Union Filter](#the-union-filter-)
        - [Slice Filter](#the-slice-filter-)
        - [Predicate Filter](#the-predicate-filter-)
        - [Function Filter](#function-filter)
    - [Predicates](#predicates)
        - [Expression Types](#expression-types)
        - [Predicate Operators](#predicate-operators)
        - [Notes](#notes)
    - [Functions](#functions)
        - [Path Functions](#path-functions)
        - [Axis (Navigation) Functions](#axis-navigation-functions)
        - [Document Functions](#document-functions)
    - [Variables](#settings)
        - [Examples](#examples)
- [JsonPath Examples](#jsonpath-examples)
    - [Books Example](#books-example)
- [JsonPath API](#jsonpath-api)

## Overview

This JsonPath implementation is heavily inspired by concepts from
both [Stefan Goessner's](https://goessner.net/articles/JsonPath/)
original ideas as well as from the [JayWay](https://github.com/json-path/JsonPath) JsonPath Java library. In addition,
it extends these with functionality familiar to XPath, including some primitive axis navigation, global settings, and
some additional functions. The rationale for a separate implementation is to leverage the _opinionated_ model used in
this library without requiring a separate, third-party library. That said, there's nothing to prevent this Json library
from integrating the Jayway SPI API as a separate implementation (which is very, very good).

In part, this is also an exercise of newer Java features including streaming (Java 8) and other features that make
coding easier. Plus, it's just an interesting problem to solve :). Take it for what it's worth. If you like it, use it;
if you don't, don't hesitate to provide feedback. In general, I'm not trying to compete with other implementations.
Rather, it's a bit of brain candy for me that might (or might not) be useful to someone. As always, as an open-source,
academic work, you're free to use what you like, just give a tip of the hat my way when you do use it (i.e.,
attribution).

## Path Expressions

At its core, JsonPath uses the notion of _filters_ to traverse the JsonNode tree for selection. The following filters
are provided:

### Filters

| Filter                                       | Description                                                                       |
|:---------------------------------------------|:----------------------------------------------------------------------------------|
| `$`                                          | The root (document) node. Must start all queries                                  |
| `@`                                          | The current node in a predicate expression                                        |
| `*`                                          | Wildcard accessor. Used to select any key or index from the current context       |
| `..`                                         | Descendant traversal. Selects all descendants of the current context              |
| `.<key>`                                     | Selects value from a JsonObject with the named key (or empty if it doesn't exist) |
| `['<key>']`                                  | Equivalent to `.<key>`, just in bracket form. Must use sinqle quotes around key   |
| `[<number>]`                                 | Selects value from an array index                                                 |
| `[<accessor>, <accessor> (,<accessor> ...)]` | A union of values from the current context. Can be either a key or index          |
| `[<start>:<end>]`                            | Slices array of values from starting position (inclusive) to end (exclusive)      | 
| `[?(<predicateExpression>)]`                 | A predicate filter. Operates on each element in context and returns if true       |
| `.<function>([args])`                        | A function filter. Uses the `.` operator to start                                 |

#### The Root Filter (`$`)

The root filter returns the "root" of the JSON document, either a JsonObject or JsonArray instance. The root filter must
be present at the beginning of the JsonPath expression, with the only exception being the use of the
[wildcard](#the-wildcard-filter-). It should not be quoted or bracketed.

#### The Current Element Filter (`@`)

The current element filter returns the current element applied to a [Predicate Expression](#predicates). It cannot be
used in place of the root filter at the beginning of a JsonPath expression.

#### The Wildcard Filter (`*`)

The wildcard filter returns all immediate children of the context element. If the context element is a JsonArray, it
will return the entire set of items in that array. For a JsonObject, it returns each element with a key; It can be used
as the initial filter in a JsonPath expression.

#### The Recursion Filter (`..`)

The recursion filter returns all children and descendant elements from a given context element. Be aware that this could
return a significant number of elements which may cause performance issues for your queries if your JSON document is
very large.

#### The Dot Key Accessor Child Filter (`.`)

The dot path accessor is a path delimiter, and if followed by a key name, is the equivalent of the bracketed
accessor `['key']`. Unlike the bracketed version, key names are not quoted and cannot contain spaces. It cannot be used
with JsonArray indexed values, i.e., the following is a syntax error: `$.1`. Instead, JsonArray index accessors must be
bracketed.

Note that if the value to be accessed does not exist, the filter returns an empty set.

#### The Bracketed Accessor Child Filter (`[]`)

Bracketed Child Filters access single values from a context JsonObject or JsonArray element. To access values from
JsonObject contexts, the filter must be the object key surround in quotes (`['key']`). For JsonArray contexts, use an
index of the value you wish to return, i.e., `[0]` returns the first element in the array. You can also use negative
numbers to access the array from the tail, i.e., `[-1]` returns the last item in the array; `[-2]` returns the
second-to-last item in the array.

Note that if the value to be accessed does not exist, the filter returns an empty set.

#### The Union Filter (`[,,]`)

Returns a set of values that correspond to the specific index values. For example, if we have an array containing the
values `['a', 'b', 'c', 'd', 'e']` and we want the second, fourth and fifth element of the array, we would
use `[1, 3, 4]` (element indexes are 0-based). Note that spaces between indexes is optional (but no more than one space)
, it's allowed to make your expression more readable. A union must include 2 or more indexes.

> **NOTE**: You can also use the same filter for objects to access specific values by key, e.g., `['a', 'b', 'd']`

#### The Slice Filter (`[:]`)

Returns a subset of an array by slicing from a start index (inclusive) to and end index inclusive. The syntax is very
loose in that you can omit either a start or end index (and even both, which is the equivalent of `*` for an array).

The following are some examples of how the slice filter can be used:

| Filter    | Result                                                                     |
|:----------|:---------------------------------------------------------------------------|
| `[0:4]`   | Returns all elements from index 0 to index 3 (up to and excluding index 4) |
| `[:3]`    | Returns all elements from index 0 to index 2 (3 elements total)            |
| `[-2:]`   | Returns the last two elements from the array                               |
| `[2:]`    | Return all elements starting at index 2 to the end of the array            |
| `[:]`     | Return everything (equivalent to `*`)                                      |
| `[0:-2]`  | Return everthing from index 0 to the second to last item in the array      |

#### The Predicate Filter (`[?()]`)

Predicate Filters evaluate each item in the context set and only return the elements that meet the predicate conditions.
See [Predicates](#predicates) for more information.

#### Function Filter

Function filters operate on each item in context and return a value specified by that function.
See [Functions](#functions) for more information.

### Predicates

Predicates are used to evaluate each item in context against an expression and returns `true` if that item meets the
expression criteria. When `false` the item is removed from the selection tree. Predicates consist of three parts

`[?(contextExpression operator testExpression)]`

_Context_ and _Test_ expressions can be either a static value - either a JSON primitive (string, number, boolean, null),
or a _JsonNode_ (a JsonObject or JsonArray), a _path_ expression, or a _variable_.

#### Expression Types

Different predicate expressions expect different value types. Typically these will be provided through either a path
expression, a literal, or a [variable](#settings). The table below describes the supported expression types

| Type     | Description                                                                                                                                                           | Example                                              |
|:---------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------|
| String   | A literal string value. Must be contained in single quotes                                                                                                            | <pre lang="javascript">'foo'</pre>                   |
| Number   | A literal number value. Can be of any type (int, long, double). <br/> depending on the predicate, will evaluate as double                                             | <pre lang="javascript">100.01</pre>                  |
| Boolean  | A boolean value `true` or `false`                                                                                                                                     | <pre lang="javascript">true</pre>                    |
| Null     | A null value `null`                                                                                                                                                   | <pre lang="javascript">null</pre>                    |
| RegExp   | A Regular Expression. The expression must be contained in `/` followed by any support options (see [Notes](#notes))                                                   | <pre lang="javascript">/cat.*/im</pre>               |
| Path     | A Path Expression. Returns a value (or set of values) to be evaluated. <br/>Type is dynamic and predicate will only return `true` if the expected types are evaluated | <pre lang="javascript">@['item'] == 10               |
| List     | A list of literal values (String, Number, Boolean, Null)                                                                                                              | <pre lang="javascript">['a', 24, false, null] </pre> |
| Variable | A variable. Variable values resolved at compile time and return one of the above expression types <br/>(see [Variables](#settings) for more information)             | <pre language="javascript">{var#$.name}</pre>        |

#### Predicate Operators

The following _operators_ are supported (see [Notes](#notes) section for additional information):

| Operator     | Description                                                                                                     | Example                                                |
|:-------------|:----------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------|
| `==`         | Equals operator. Value types must be equivalent, i.e., `'1'` is not equal to `1`                                | <pre lang="javascript">@.item == 'foo'</pre>           |
| `!=`         | Not equals operator.                                                                                            | <pre lang="javascript">@.item != 'foo'</pre>           |
| `<`          | Less than operator. Context and Test expressions must both be numeric, otherwise `false`                        | <pre lang="javascript">@[1] < $.price</pre>            |
| `<=`         | Less than or equal operator. Expressions must both be numeric                                                   | <pre lang="javascript">{var} <= 10</pre>               |
| `>`          | Greater than operator. Expressions must both be numeric                                                         | <pre lang="javascript">@.item > {var#$.item}</pre>     | 
| `>=`         | Greater than or equal operator. Expressions must both be numeric                                                | <pre lang="javascript">@.item.length() >= 0</pre>      |
| `=~`         | Regular expression. Returns `true` for a match. The Test expression must be a regex pattern, e.g., `/cat.*/i`   | <pre lang="javascript">@.item =~ /cat.*/i</pre>        |
| `contains`   | String contains. Context and Test expressions must be Strings                                                   | <pre lang="javascript">@.item contains 'foo'</pre>     |
| `startswith` | String starts with value. Context and Test expressions must be strings                                          | <pre lang="javascript">@.name starts-with 'Jim'</pre>  |
| `endswith`   | String ends with value. Context and Test expressions must be strings                                            | <pre lang="javascript">@.name ends-with 'Earley'</pre> |
| `in`         | Context expression value is contained in the Test expression                                                    | <pre lang="javascript">@.item in ['a', 'b', 'c]</pre>  |
| `nin`        | Context expression value is not contained in the Test expression                                                | <pre lang="javascript">@.item nin ['a', 'b']</pre>     |
| `empty`      | Context expression (array or string) is (or is not) empty. Test expression must be a boolean [`true`, `false`]  | <pre lang="javascript">@.item empty true</pre>         |

#### Notes

1. **Negation**: Predicate expressions can be _negated_ by applying the not (`!`) operator to either the Context or Test
   expression:

    ```
    [?(!@.foo contains 'bar')] || [?(@.foo contains !'bar')]
    ```
2. **Regular Expression Flags**: The following flags can be applied to regular expressions. A regular expression can
   have zero or more flags:

    - `i`: case-insensitive. Maps to Java `Pattern.CASE_INSENSTIVE`
    - `x`: comments. Maps to Java `Pattern.COMMENTS`
    - `m`: multiline. Maps to Java `Pattern.MULTILINE`
    - `s`: Dot All. Matches newline characters as well. Maps to Java `Pattern.DOTALL`
    - `u`: Unicode. Maps to Java `Pattern.UNICODE_CASE`
    - `d`: Unix Lines. Maps to Java `Pattern.UNIX_LINES`

3. **Logical Operators**: The logical operators `&&` and `||` are supported to logically join predicates together.
   Parentheses `()` to logically group predicates together for evaluation. For example:

    - `$.count == 10 && @.price < 4.95`: Returns `true` if the `count` value equals `10` _and_ the current `price` is
      less than `4.95`
    - `$.count == 10 || @.price < 4.95`: Returns `true` if the `count` value equals `10` _or_ the current `price` is
      less than `4.95`
    - `$.manufacturer == 'Acme Corp.' && ($.count == 10 && @.price < 4.95)`: Returns `true` if the `manufacturer` is
      `'Acme Corp.'` _and both_ the `count` value equals `10` _and_ the current `price` is less than `4.95`

### Functions

> **Note**: Functions are an extension feature. While there are similarities to functions in other implementations,
> there is no guarantee that they operate exactly the same way, so results may vary for functions with the same name
> and argument signature.

There are two types of functions: _contextual_ and _non-contextual_. _Contextual_ functions operate by using data from
the current context stream; _Non-contextual_ functions operate independently from any context and typically require one
or more arguments to specify elements that will be used in the function.

#### Path Functions

The following functions can be applied to Path expressions

| Function                     | Description                                                                                                                     |
|:-----------------------------|:--------------------------------------------------------------------------------------------------------------------------------|
| `distinct()`                 | Return a distinct set of values                                                                                                 |
| `sort([order])`              | Sort a set of values. The `order` parameter is optional and defaults to `asc`, use `desc` for reverse order                     |
| `length()`                   | Returns the length of a string or node. For a JsonNode, returns an array's size or an object's key length                       |
| `last()`                     | Syntactic sugar for `[-1]`                                                                                                      |
| `first()`                    | Syntactic sugar for `[0]`                                                                                                       |
| `concat(expr[, expr...])`    | Concatenate expressions into a string value                                                                                     |
| `substring(start [,length])` | Return a substring from the `start` character (0-based) to include `length` characters (or to end if `length` param is omitted) |
| `uppercase()`                | Returns an uppercase string                                                                                                     |
| `lowercase()`                | Returns an lowercase string                                                                                                     |
| `sum()`                      | Sum values in context. Values must be numeric, other values are ignored. Returns a double                                       |

#### Axis (Navigation) Functions

Axis functions provide a means for navigating from a context node to relative nodes. The context must originate from
the _JsonNode_ being evaluated. What this means is that some functions and static predicate expressions **do not have
context** and, will not return relatives.

| Function                                   | Description                                  |
|:-------------------------------------------|:---------------------------------------------|
| `parent()`                                 | The parent of the current context.           |
| `siblings()`                               | The siblings of the current context.         |
| `preceding-sibling([predicateExpression])` | The preceding sibling of the current context |

#### Document Functions

Document functions are non-contextual and are used for loading and parsing JSON data from external sources that can be
used for evaluating data in-context. For example, the `document()` function could be used to load JSON data that
contains values that can applied in a predicate expression.

| Function               | Description                                                    |
|:-----------------------|:---------------------------------------------------------------|
| `document('path')`     | Load an external JSON document from a specified path (string). |
| `parse('jsonString')`  | Parse a JSON string into a JsonNode instance                   | 

### Variables

> **NOTE**: Variables are an extension feature, and not part of the current JsonPath specification. Using
> this expression syntax in other JsonPath implementations may result in errors, or different behavior than
> what is defined here.

Variables are globally scoped expressions that can be applied to [predicate expressions](#predicates). Variables are
assigned during the instantiation of a `JsonPath` instance using the `JsonPathFactory` and `JsonPathBuilder`. Variables
are key-value pairs consisting of a unique variable name and an expression. Valid expressions include JSON primitives
(string, number, boolean, null) and Json nodes (JsonObject or JsonArray). Variable values are immutable and are assigned
on a "first in wins" strategy. Variable resolution occurs at compile time.

Variables can be used either as a Context or Test expression within a Predicate, and use the following syntax:

```
{variable_name[#path_expression]}
```

Where:

- `variable_name` is the unique name for this variable to be retrieved
- `path_expression` is a JsonPath expression to navigate to a value within the variable node, and should only be used
  with JsonNode values. The path expression must start with `#` to indicate that the expression value is contained
  within the value structure.

#### Examples

Assuming that settings are set:

```json5
{
    "a": "string",
    "b": 1,
    "c": true,
    "d": [
        1,
        2,
        3,
        4
    ],
    "e": {
        "foo": "bar"
    }
}
```

| Variable Expression | Example                                                        | Description                                                                                   |
|:--------------------|:---------------------------------------------------------------|:----------------------------------------------------------------------------------------------|
| `{a}`               | <pre lang="javascript">[?(@.item == {a})]</pre>                | Predicate that evaluates the `item` value from the current context and must equal 'string'    |
| `{b}`               | <pre lang="javascript">[?(@.item > {b})]</pre>                 | Predicate that evaluates the `item` value from the current context and must be greater than 1 |
| `{c}`               | <pre lang="javascript">[?(@.item empty {c})]</pre>             | Predicate that evaluates the `item` value is empty (c = `true`)                               |
| `{d}`               | <pre lang="javascript">[?(@.item in {d})]</pre>                | Predicate that evaluates if the `item` value is contained within the set `[1,2,3,4]`          | 
| `{d#$.last()}`      | <pre lang="javascript">[?(@.item == {d#$.last()})]</pre>       | Predicate that evaluates if the `item` value is equal to 4 (the last value in 'd')            | 
| `{e#$.foo}`         | <pre lang="javascript">[?(@.item starts-with {e#$.foo})]</pre> | Predicate that evaluates if the `item` value starts with `'bar'`                              |

## JsonPath Examples

### Books Example

This is the 'classic' books.json example:

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

| Path Expression                                                    | Description                                                 | Returns                                                                                                                                                                                                                                                                                                                                                                   |
|:-------------------------------------------------------------------|:------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <pre lang="javascript">$['store']['book'][*]['author'] </pre>      | Return all book authors                                     | <pre>[<br/>  "Nigel Rees",<br/>  "Evelyn Waugh",<br/>  "Herman Melville",<br/>  "J. R. R. Tolkien"<br/>] </pre>                                                                                                                                                                                                                                                           |
| <pre lang="javascript">$.store.book.*.author </pre>                | Return all book authors (same as above, just dot notation)  | <pre>[<br/>  "Nigel Rees",<br/>  "Evelyn Waugh",<br/>  "Herman Melville",<br/>  "J. R. R. Tolkien"<br/>] </pre>                                                                                                                                                                                                                                                           |
| <pre lang="javascript">$..author</pre>                             | Return all book authors (recursive descent)                 | <pre>[<br/>  "Nigel Rees",<br/>  "Evelyn Waugh",<br/>  "Herman Melville",<br/>  "J. R. R. Tolkien"<br/>] </pre>                                                                                                                                                                                                                                                           |
| <pre lang="javascript">$['store'][..]['author']</pre>              | Return all books (recursive descent, bracketed)             | <pre>[<br/>  "Nigel Rees",<br/>  "Evelyn Waugh",<br/>  "Herman Melville",<br/>  "J. R. R. Tolkien"<br/>] </pre>                                                                                                                                                                                                                                                           |
| <pre lang="javascript">$.store..price</pre>                        | Return all prices in the store                              | <pre>[<br/>  8.95,<br/>  12.99,<br/>  8.99,<br/>  22.99,<br/>  19.95<br/>]</pre>                                                                                                                                                                                                                                                                                          |
| <pre lang="javascript">$..book[2]</pre>                            | Get the third book                                          | <pre>[<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  }<br/>]</pre>                                                                                                                                                                                    |
| <pre lang="javascript">$..book[-2]</pre>                           | Get the second to last book                                 | <pre>[<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  }<br/>]</pre>                                                                                                                                                                                    |
| <pre lang="javascript">$..book[0,1]</pre>                          | Get the first two books                                     | <pre>[<br/>  {<br/>    "category":"reference",<br/>    "title":"Sayings of the Century",<br/>    "price":8.95,<br/>    "author":"Nigel Rees"<br/>  },<br/>  {<br/>    "price":12.99,<br/>    "author":"Evelyn Waugh",<br/>    "category":"fiction",<br/>    "title":"Sword of Honour"<br/>  }<br/>]</pre>                                                                 |
| <pre lang="javascript">$..book[:2]</pre>                           | Get the first two books (slice operator)                    | <pre>[<br/>  {<br/>    "category":"reference",<br/>    "title":"Sayings of the Century",<br/>    "price":8.95,<br/>    "author":"Nigel Rees"<br/>  },<br/>  {<br/>    "price":12.99,<br/>    "author":"Evelyn Waugh",<br/>    "category":"fiction",<br/>    "title":"Sword of Honour"<br/>  }<br/>]</pre>                                                                 |
| <pre lang="javascript">$..book[1:3]</pre>                          | Get the middle two books (index 1 inclusive to 3 exclusive) | <pre>[<br/>  {<br/>    "price":12.99,<br/>    "author":"Evelyn Waugh",<br/>    "category":"fiction",<br/>    "title":"Sword of Honour"<br/>  },<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  }<br/>]</pre>                                           |
| <pre lang="javascript">$..book[2:]</pre>                           | Get the last two books                                      | <pre>[<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  },<br/>  {<br/>    "title":"The Lord of the Rings",<br/>    "author":"J. R. R. Tolkien",<br/>    "price":22.99,<br/>    "isbn":"0-395-19395-8",<br/>    "category":"fiction"<br/>  }<br/>]</pre> |
| <pre lang="javascript">$..book[?(@.isbn)]</pre>                    | Get all books that have an ISBN number                      | <pre>[<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  },<br/>  {<br/>    "title":"The Lord of the Rings",<br/>    "author":"J. R. R. Tolkien",<br/>    "price":22.99,<br/>    "isbn":"0-395-19395-8",<br/>    "category":"fiction"<br/>  }<br/>]</pre> |
| <pre lang="javascript">$..book[?(!@.isbn)]</pre>                   | Get all books that do not have an ISBN number               | <pre>[<br/>  {<br/>    "category":"reference",<br/>    "title":"Sayings of the Century",<br/>    "price":8.95,<br/>    "author":"Nigel Rees"<br/>  },<br/>  {<br/>    "price":12.99,<br/>    "author":"Evelyn Waugh",<br/>    "category":"fiction",<br/>    "title":"Sword of Honour"<br/>  }<br/>]</pre>                                                                 |
| <pre lang="javascript">$..book[?(@.price < 10)]                    | Get all books with price less than 10                       | <pre>[<br/>  {<br/>    "category":"reference",<br/>    "title":"Sayings of the Century",<br/>    "price":8.95,<br/>    "author":"Nigel Rees"<br/>  },<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  }<br/>]</pre>                                     |
| <pre lang="javascript">$..book[?(@.price <= $['expensive'])]</pre> | Get all books that are less than 'expensive' (10)           | <pre>[<br/>  {<br/>    "category":"reference",<br/>    "title":"Sayings of the Century",<br/>    "price":8.95,<br/>    "author":"Nigel Rees"<br/>  },<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  }<br/>]</pre>                                     |
| <pre lang="javascript">$..book[?(@.price <= {expensive})]</pre>    | Get all books that are not expensive (global variable = 10) | <pre>[<br/>  {<br/>    "category":"reference",<br/>    "title":"Sayings of the Century",<br/>    "price":8.95,<br/>    "author":"Nigel Rees"<br/>  },<br/>  {<br/>    "price":8.99,<br/>    "title":"Moby Dick",<br/>    "category":"fiction",<br/>    "author":"Herman Melville",<br/>    "isbn":"0-553-21311-3"<br/>  }<br/>]</pre>                                     |
| <pre lang="javascript">$..book[?(@.author =~ /.*REES/i)]</pre>     | Get all books by author, using regular expression           | <pre>[<br/>  {<br/>    "category":"reference",<br/>    "title":"Sayings of the Century",<br/>    "price":8.95,<br/>    "author":"Nigel Rees"<br/>  }<br/>]</pre>                                                                                                                                                                                                          |

## JsonPath API

To access the `JsonPath` interface, use the `ServiceManager` to create an instance of the `JsonPathFactory` interface.
With the `JsonPathFactory`, you can create a `JsonPath` instance with default configuration:

<pre lang="java">
JsonPathFactory factory=ServiceManager.getProvider(JsonPathFactory.class);
JsonPath jsonPath = factory.newJsonPath();
JsonArray results = jsonPath.select(...);
</pre>



