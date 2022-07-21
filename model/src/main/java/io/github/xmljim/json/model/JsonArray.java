package io.github.xmljim.json.model;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A container for a list of values
 */
public non-sealed interface JsonArray extends JsonNode {

    default NodeType type() {
        return NodeType.ARRAY;
    }

    /**
     * Add a value to the array. Internally it should convert the value to a
     * {@link JsonValue} type
     *
     * @param value the value
     * @param <V>   the value type
     * @return the value
     */
    <V> boolean add(V value);

    /**
     * A a collection of values
     *
     * @param collection the value collection
     * @return true if the collection of values was added
     */
    boolean addAll(Collection<?> collection);

    /**
     * A a collection of values from a stream
     *
     * @param stream the value stream
     * @return true if the values were added
     */
    boolean addAll(Stream<?> stream);

    /**
     * Insert a value a specified index
     *
     * @param index the index position. Must be < the length -1
     * @param value the value to insert
     * @param <V>   the value type
     * @return true if the value was inserted
     */
    <V> boolean insert(int index, V value);

    /**
     * Insert a collection of values, starting at a given index position
     *
     * @param index      the index position
     * @param collection the collect to add
     * @return true if the collection of values were added
     */
    boolean insertAll(int index, Collection<?> collection);

    /**
     * Insert a collection stream of values, starting at a given index position
     *
     * @param index  the index position
     * @param stream the collection stream
     * @return true if the values were inserted
     */
    boolean insertAll(int index, Stream<?> stream);

    /**
     * Set/Update a value at a given index position
     *
     * @param index the index position
     * @param value the value
     * @param <V>   the value type
     * @return the value, converted to a {@link JsonValue} instance
     */
    <V> JsonValue<V> set(int index, V value);

    /**
     * Return an optional JsonValue from a given index
     *
     * @param index the index position
     * @param <V>   the value type
     * @return returns an Optional of the value, which may be empty if
     * no value exists at the given index
     */
    <V> Optional<JsonValue<V>> value(int index);

    /**
     * Return an optional (raw) value from a given index
     *
     * @param index the index position
     * @param <V>   the value type
     * @return an Optional of the value, which may be empty if the
     * value was null, or no value exists at a given index
     */
    @SuppressWarnings("unchecked")
    default <V> Optional<V> getOptional(int index) {
        return (Optional<V>) value(index).map(JsonValue::get);
    }

    /**
     * Get the JsonValue at a given index
     *
     * @param index the index position
     * @param <V>   the value type
     * @return the JsonValue or null if no value exists at a given index
     */
    @SuppressWarnings("unchecked")
    default <V> JsonValue<V> getValue(int index) {
        return (JsonValue<V>) value(index).orElse(null);
    }

    /**
     * Get the (raw) value at a given index
     *
     * @param index the index position
     * @param <V>   the value type
     * @return the value or null if the value is either null or
     * does not exist at a given index
     */
    @SuppressWarnings("unchecked")
    default <V> V get(int index) {
        return (V) getOptional(index).orElse(null);
    }

    /**
     * Evaluates the list of data to see if it contains the raw value
     *
     * @param value the value
     * @param <V>   the value type
     * @return true if the list contains the value; false otherwise
     */
    <V> boolean contains(V value);

    /**
     * Evaluates the list of JsonValues to see if it contains a JsonValue
     * with this value
     *
     * @param value the JsonValue
     * @return true if the contains the JsonValue; false otherwise
     */
    boolean containsValue(JsonValue<?> value);

    /**
     * Remove a value at a given index
     *
     * @param index the index position
     * @param <V>   the value type
     * @return the removed JsonValue
     */
    <V> JsonValue<V> remove(int index);

    /**
     * Splice the array to return values from a given start position
     *
     * @param start the start position
     * @return all values beginning with the start position until the end
     * of the array
     */
    Stream<JsonValue<?>> splice(int start);

    /**
     * Splice the array to return values from a given starting position
     * containing up to the specified length.
     *
     * @param start  the start position
     * @param length the length
     * @return an array of values beginning with the start index and up
     * to the length of values subsequently.
     */
    Stream<JsonValue<?>> splice(int start, int length);

    /**
     * Returns a stream of JsonValues in the array
     *
     * @return a stream of JsonValues
     */
    Stream<JsonValue<?>> jsonValues();

    /**
     * Returns a stream of raw values in the array
     *
     * @return a stream of raw values in the array
     */
    Stream<?> values();

}
