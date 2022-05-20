package io.github.xmljim.json.factory.parser.event;

import io.github.xmljim.json.factory.parser.Statistics;
import io.github.xmljim.json.model.NodeType;

import java.math.BigDecimal;
import java.util.function.Supplier;

public interface Assembler<T> extends Configurable {

    /**
     * The document start event
     *
     * @param type the document type, will either be {@linkplain NodeType#OBJECT} or
     *             {@link NodeType#ARRAY}
     */
    void documentStart(NodeType type);

    /**
     * The document end event
     */
    void documentEnd();

    /**
     * Start of new JSONArray.
     *
     * @param key the parent key that is associated with the array
     */
    void jsonArrayStart(String key);

    /**
     * End of a JSONArray
     *
     * @param key the parent key associated with the array
     */
    void jsonArrayEnd(String key);

    /**
     * Start of a new JSONObject
     *
     * @param key the parent key associated with the object
     */
    void jsonObjectStart(String key);

    /**
     * End of a JSONObject
     *
     * @param key the parent key associated with this object
     */
    void jsonObjectEnd(String key);

    /**
     * String value
     *
     * @param key   the key associated with this value
     * @param value the value
     */
    void valueString(String key, String value);

    /**
     * Long value
     *
     * @param key   the key associated with this value
     * @param value the value
     */
    void valueLong(String key, Long value);

    /**
     * Integer value
     *
     * @param key   the key associated with this value
     * @param value the value
     */
    void valueInt(String key, Integer value);

    /**
     * BigDecimal value
     *
     * @param key   the key associated with this value
     * @param value the value
     */
    void valueBigDecimal(String key, BigDecimal value);

    /**
     * Double value value
     *
     * @param key   the key associated with this value
     * @param value the value
     */
    void valueDouble(String key, Double value);

    /**
     * Float value
     *
     * @param key   the key associated with this value
     * @param value the value
     */
    void valueFloat(String key, Float value);

    void valueNumber(String key, Number value);

    /**
     * Boolean value
     *
     * @param key   the key associated with this value
     * @param value the value
     */
    void valueBoolean(String key, boolean value);

    /**
     * null value
     *
     * @param key the key associated with this value
     */
    void valueNull(String key);

    void newKey(String key);

    Supplier<T> getResult();

    Statistics getStatistics();
}
