package io.github.xmljim.json.api;

import io.github.xmljim.json.factory.jsonpath.JsonPathBuilder;
import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.factory.jsonpath.ResultType;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.mapper.MappingConfig;
import io.github.xmljim.json.factory.mapper.MappingParserConfig;
import io.github.xmljim.json.factory.mapper.parser.MappingParser;
import io.github.xmljim.json.factory.merge.MergeFactory;
import io.github.xmljim.json.factory.merge.strategy.ArrayConflictStrategy;
import io.github.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.github.xmljim.json.factory.merge.strategy.ObjectConflictStrategy;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.JsonParserException;
import io.github.xmljim.json.factory.parser.ParserBuilder;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonNode;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.service.ServiceManager;

import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class JsonApi {

    /**
     * JsonPath ({@link JsonPathFactory}) API functionality
     */
    public static final JsonPathApi JsonPath = new JsonPathApi();

    /**
     * JsonParser ({@link ParserFactory}) API functionality
     */
    public static final JsonParserApi JsonParser = new JsonParserApi();

    /**
     * JsonElement ({@link ElementFactory}) functionality
     */
    public static final ElementApi JsonElement = new ElementApi();

    /**
     * JsonMerge ({@link MergeFactory}) functionality
     */
    public static final JsonMergeApi JsonMerge = new JsonMergeApi();

    /**
     * JsonMapper ({@link MapperFactory}) functionality
     */
    public static final JsonMapperApi JsonMapper = new JsonMapperApi();

    /**
     * Private constructor, use static fields instead
     */
    private JsonApi() {
        //private constructor
    }

    /**
     * API for JsonPath functionality
     */
    public static final class JsonPathApi {
        private final JsonPathFactory jsonPathFactory = ServiceManager.getProvider(JsonPathFactory.class);

        private JsonPathApi() {
            //no-op
        }

        /**
         * Return the underlying JsonPathBuilder to modify settings
         * for a new {@link io.github.xmljim.json.factory.jsonpath.JsonPath} instance
         *
         * @return a new JsonPathBuilder
         */
        public JsonPathBuilder getBuilder() {
            return jsonPathFactory.newJsonPathBuilder();
        }

        /**
         * Return a JsonArray of values selected from a JsonPath expression.
         * This uses the default settings and properties for a JsonPath instance.
         * If you need to modify settings (e.g., properties, or set variables),
         * use {@link #getBuilder()} to apply these settings and return a
         * {@link io.github.xmljim.json.factory.jsonpath.JsonPath} instance
         *
         * <p>This is the syntactic equivalent of: </p>
         * <pre>
         *     JsonPathFactory jsonPathFactory = ServiceManager.getProvider(JsonPathFactory.class);
         *     JsonPath jsonPath = jsonPathFactory.newJsonPath();
         *     JsonArray result = jsonPath.select(node, pathExpression);
         * </pre>
         *
         * @param node           The context node that will be used to evaluate and select values
         * @param pathExpression the JsonPath expression to query the context node
         * @return a JsonArray of values that represent the select expression
         */
        public JsonArray select(JsonNode node, String pathExpression) {
            return jsonPathFactory.newJsonPath().select(node, pathExpression);
        }

        /**
         * Return a List of values selected from a JsonPath expression.
         * This uses the default settings and properties for a JsonPath instance.
         * If you need to modify settings (e.g., properties, or set variables),
         * use {@link #getBuilder()} to apply these settings and return a
         * {@link io.github.xmljim.json.factory.jsonpath.JsonPath} instance
         *
         * <p>This is the syntactic equivalent of: </p>
         * <pre>
         *     JsonPathFactory jsonPathFactory = ServiceManager.getProvider(JsonPathFactory.class);
         *     JsonPath jsonPath = jsonPathFactory.newJsonPath();
         *     JsonArray result = jsonPath.select(node, pathExpression);
         *
         *     MapperFactory mapperFactory = ServiceManager.getProvider(MapperFactory.class);
         *     Mapper mapper = mapperFactory.newMapper();
         *     return mapper.toList(result);
         * </pre>
         *
         * @param node           The context node that will be used to evaluate and select values
         * @param pathExpression the JsonPath expression to query the context node
         * @return a JsonArray of values that represent the select expression
         */
        @SuppressWarnings("unchecked")
        public List<Object> selectList(JsonNode node, String pathExpression) {
            JsonMapperApi jsonMapperApi = new JsonMapperApi();
            return (List<Object>) jsonMapperApi.toList(select(node, pathExpression));
        }

        /**
         * Return a JsonArray of paths selected from a JsonPath expression.
         * This uses the default settings and properties for a JsonPath instance.
         * If you need to modify settings (e.g., properties, or set variables),
         * use {@link #getBuilder()} to apply these settings and return a
         * {@link io.github.xmljim.json.factory.jsonpath.JsonPath} instance
         *
         * <p>This is the syntactic equivalent of: </p>
         * <pre>
         *     JsonPathFactory jsonPathFactory = ServiceManager.getProvider(JsonPathFactory.class);
         *     JsonPath jsonPath = jsonPathFactory.newJsonPath();
         *     JsonArray result = jsonPath.select(node, pathExpression, ResultType.PATH);
         * </pre>
         *
         * @param node           The context node that will be used to evaluate and select values
         * @param pathExpression the JsonPath expression to query the context node
         * @return a JsonArray of normalized Json paths to each selected item
         */
        public JsonArray selectPath(JsonNode node, String pathExpression) {
            return jsonPathFactory.newJsonPath().select(node, pathExpression, ResultType.PATH);
        }

        @SuppressWarnings("unchecked")
        public List<String> selectPathList(JsonNode node, String pathExpression) {
            JsonMapperApi jsonMapperApi = new JsonMapperApi();
            return (List<String>) jsonMapperApi.toList(select(node, pathExpression));
        }

        public <T> T selectValue(JsonNode node, String pathExpression) {
            return jsonPathFactory.newJsonPath().selectValue(node, pathExpression);
        }
    }

    /**
     * Parser API
     */
    public static final class JsonParserApi {
        private final ParserFactory parserFactory = ServiceManager.getProvider(ParserFactory.class);

        private JsonParserApi() {
            //no-op
        }

        /**
         * Parse a JSON String
         *
         * @param jsonString the JSON String
         * @param <T>        The JsonNode type (either a {@link JsonArray} or {@link JsonObject})
         * @return a new JsonNode type
         */
        public <T extends JsonNode> T parse(String jsonString) {
            return parserFactory.newParser().parse(InputData.of(jsonString));
        }

        /**
         * Parse a JSON String
         *
         * @param path the path to the Json data
         * @param <T>  The JsonNode type (either a {@link JsonArray} or {@link JsonObject})
         * @return a new JsonNode type
         */
        public <T extends JsonNode> T parse(Path path) {
            if (path.getFileSystem().isOpen()) {
                if (!Files.exists(path)) {
                    throw new JsonParserException("File does not exist: " + path.getFileName().toString());
                }
            }
            return parserFactory.newParser().parse(InputData.of(path));
        }

        /**
         * Parse a JSON String
         *
         * @param inputStream the InputStream to the Json data
         * @param <T>         The JsonNode type (either a {@link JsonArray} or {@link JsonObject})
         * @return a new JsonNode type
         */
        public <T extends JsonNode> T parse(InputStream inputStream) {
            return parserFactory.newParser().parse(InputData.of(inputStream));
        }

        /**
         * Parse a JSON String
         *
         * @param reader the Reader to the Json data
         * @param <T>    The JsonNode type (either a {@link JsonArray} or {@link JsonObject})
         * @return a new JsonNode type
         */
        public <T extends JsonNode> T parse(Reader reader) {
            return parserFactory.newParser().parse(InputData.of(reader));
        }

        /**
         * Return the ParserBuilder to configure a new Parser
         *
         * @return the ParserBuilder
         */
        public ParserBuilder getParserBuilder() {
            return parserFactory.newParserBuilder();
        }

        public <T> T parse(InputData inputData, Class<T> targetClass) {
            MappingParser mappingParser = JsonMapper.newMappingParser();
            return mappingParser.parse(inputData, targetClass);
        }

        public <T> T parse(MappingParserConfig mappingParserConfig, InputData inputData, Class<T> targetClass) {
            MappingParser mappingParser = JsonMapper.newMappingParser(mappingParserConfig);
            return mappingParser.parse(inputData, targetClass);
        }
    }

    /**
     * Json Element API
     */
    public static final class ElementApi {
        private final ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);

        private ElementApi() {
            //no-op
        }

        /**
         * Create a new, empty JSONObject instance
         *
         * @return a new empty JSONObject instance
         */
        public JsonObject newObject() {
            return elementFactory.newObject();
        }

        /**
         * Create a new, empty JSONArray instance
         *
         * @return a new empty JSONArray instance
         */
        public JsonArray newArray() {
            return elementFactory.newArray();
        }

        /**
         * Create a new JsonValue instance
         *
         * @param value the raw value
         * @param <T>   the value type
         * @return the new JsonValue
         */
        public <T> JsonValue<T> newValue(T value) {
            return elementFactory.newValue(value);
        }
    }

    /**
     * The JsonMerge API
     */
    public static final class JsonMergeApi {
        private final MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);

        private JsonMergeApi() {
            // no-op
        }

        /**
         * Merge two JsonNodes, using the default configuration (i.e., conflict strategies)
         *
         * @param primary   the primary node
         * @param secondary the secondary node
         * @param <T>       The JsonNode type
         * @return The merged JsonNode
         */
        public <T extends JsonNode> T merge(T primary, T secondary) {
            return mergeFactory.newMergeProcessor().merge(primary, secondary);
        }

        /**
         * Merge two JsonNodes, specifying the ArrayConflictStrategy and ObjectConflict Strategy
         *
         * @param primary                the primary node
         * @param secondary              the secondary node
         * @param arrayConflictStrategy  the conflict strategy to apply for JsonArrays
         * @param objectConflictStrategy The conflict strategy to apply for JsonObjects
         * @param <T>                    the node type
         * @return The merged JsonNode
         */
        public <T extends JsonNode> T merge(T primary, T secondary, ArrayConflictStrategy arrayConflictStrategy,
                                            ObjectConflictStrategy objectConflictStrategy) {
            return mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(arrayConflictStrategy)
                .setObjectConflictStrategy(objectConflictStrategy)
                .build()
                .merge(primary, secondary);
        }

        /**
         * Merge two JsonNodes, specifying the ArrayConflictStrategy and ObjectConflict Strategy
         *
         * @param primary                the primary node
         * @param secondary              the secondary node
         * @param arrayConflictStrategy  the conflict strategy to apply for JsonArrays
         * @param objectConflictStrategy The conflict strategy to apply for JsonObjects
         * @param mergeResultStrategy    the merge result strategy - Here there be dragons. KNOW what you're doing
         * @param mergeAppendKey         the key value to append to a given JsonObject key for Append conflicts
         * @param <T>                    the node type
         * @return The merged JsonNode
         */
        public <T extends JsonNode> T merge(T primary, T secondary, ArrayConflictStrategy arrayConflictStrategy,
                                            ObjectConflictStrategy objectConflictStrategy,
                                            MergeResultStrategy mergeResultStrategy,
                                            String mergeAppendKey) {

            return mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(arrayConflictStrategy)
                .setObjectConflictStrategy(objectConflictStrategy)
                .setMergeAppendKey(mergeAppendKey)
                .setMergeResultStrategy(mergeResultStrategy)
                .build()
                .merge(primary, secondary);
        }
    }

    /**
     * JsonMapper API
     */
    public static final class JsonMapperApi {
        private final MapperFactory mapperFactory = ServiceManager.getProvider(MapperFactory.class);

        private JsonMapperApi() {
            //no-op
        }

        /**
         * Convert an object into a JsonObject
         *
         * @param object the object instance
         * @return the JsonObject
         */
        public JsonObject toJsonObject(Object object) {
            return mapperFactory.newMapper().toJson(object);
        }

        /**
         * Convert a map into a JsonObject
         *
         * @param objectMap the map instance
         * @return a new JsonObject
         */
        public JsonObject toJsonObject(Map<String, Object> objectMap) {
            return mapperFactory.newMapper().toJson(objectMap);
        }

        /**
         * Convert a collection to a JsonArray
         *
         * @param collection the collection
         * @return a new JsonArray
         */
        public JsonArray toJsonArray(Collection<Object> collection) {
            return mapperFactory.newMapper().toJson(collection);
        }

        /**
         * Convert a JsonObject to a class instance
         *
         * @param jsonObject  The JsonObject
         * @param targetClass the target class, should be concrete class
         * @param <T>         the class type or a super type or interface
         * @return the converted class
         */
        public <T> T toClass(JsonObject jsonObject, Class<T> targetClass) {
            return mapperFactory.newMapper().toClass(jsonObject, targetClass);
        }

        public <T> T toClass(MappingConfig mappingConfig, JsonObject jsonObject, Class<T> targetClass) {
            return mapperFactory.newMapper(mappingConfig).toClass(jsonObject, targetClass);
        }

        public <T> JsonObject toJson(T instance) {
            return mapperFactory.newMapper().toJson(instance);
        }

        public <T> JsonObject toJson(MappingConfig mappingConfig, T instance) {
            return mapperFactory.newMapper(mappingConfig).toJson(instance);
        }

        /**
         * Convert a JsonObject to a Map
         *
         * @param jsonObject the JsonObject
         * @return a new Map instance
         */
        public Map<String, Object> toMap(JsonObject jsonObject) {
            return mapperFactory.newMapper().toMap(jsonObject);
        }

        /**
         * Convert a JsonArray to a List
         *
         * @param jsonArray the JsonArray
         * @return the new List instance
         */
        public List<?> toList(JsonArray jsonArray) {
            return mapperFactory.newMapper().toList(jsonArray);
        }

        /**
         * Convert a raw value to a JsonValue
         *
         * @param value the value to convert
         * @return a new JsonValue
         */
        public JsonValue<?> toValue(Object value) {
            return mapperFactory.newMapper().toValue(value);
        }

        MappingParser newMappingParser() {
            return mapperFactory.newMappingParser();
        }

        MappingParser newMappingParser(MappingParserConfig mappingParserConfig) {
            return mapperFactory.newMappingParser(mappingParserConfig);
        }

    }
}
