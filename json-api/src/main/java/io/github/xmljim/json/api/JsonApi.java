package io.github.xmljim.json.api;

import io.github.xmljim.json.factory.jsonpath.JsonPathBuilder;
import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.factory.jsonpath.ResultType;
import io.github.xmljim.json.factory.mapper.MapperBuilder;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.merge.MergeFactory;
import io.github.xmljim.json.factory.merge.strategy.MergeResultStrategy;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.JsonParserException;
import io.github.xmljim.json.factory.parser.ParserBuilder;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.merger.conflict.ArrayConflictStrategies;
import io.github.xmljim.json.merger.conflict.ObjectConflictStrategies;
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

    public JsonPathApi JsonPath = new JsonPathApi();
    public JsonParserApi JsonParser = new JsonParserApi();

    public ElementApi JsonElement = new ElementApi();

    public JsonMergeApi JsonMerge = new JsonMergeApi();

    public JsonMapperApi JsonMapper = new JsonMapperApi();

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

        public <T extends JsonNode> T parse(String jsonString) {
            return parserFactory.newParser().parse(InputData.of(jsonString));
        }

        public <T extends JsonNode> T parse(Path path) {
            if (path.getFileSystem().isOpen()) {
                if (!Files.exists(path)) {
                    throw new JsonParserException("File does not exist: " + path.getFileName().toString());
                }
            }
            return parserFactory.newParser().parse(InputData.of(path));
        }

        public <T extends JsonNode> T parse(InputStream inputStream) {
            return parserFactory.newParser().parse(InputData.of(inputStream));
        }

        public <T extends JsonNode> T parse(Reader reader) {
            return parserFactory.newParser().parse(InputData.of(reader));
        }

        public ParserBuilder getParserBuilder() {
            return parserFactory.newParserBuilder();
        }
    }

    public static final class ElementApi {
        private final ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);

        private ElementApi() {
            //no-op
        }

        public JsonObject newObject() {
            return elementFactory.newObject();
        }

        public JsonArray newArray() {
            return elementFactory.newArray();
        }

        public <T> JsonValue<T> newValue(T value) {
            return elementFactory.newValue(value);
        }
    }

    public static final class JsonMergeApi {
        private final MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);

        private JsonMergeApi() {
            // no-op
        }

        public <T extends JsonNode> T merge(T primary, T secondary) {
            return mergeFactory.newMergeProcessor().merge(primary, secondary);
        }

        public <T extends JsonNode> T merge(T primary, T secondary, ArrayConflictStrategies arrayConflictStrategies,
                                            ObjectConflictStrategies objectConflictStrategies) {
            return mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(arrayConflictStrategies)
                .setObjectConflictStrategy(objectConflictStrategies)
                .build()
                .merge(primary, secondary);
        }

        public <T extends JsonNode> T merge(T primary, T secondary, ArrayConflictStrategies arrayConflictStrategies,
                                            ObjectConflictStrategies objectConflictStrategies,
                                            MergeResultStrategy mergeResultStrategy,
                                            String mergeAppendKey) {
            return mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(arrayConflictStrategies)
                .setObjectConflictStrategy(objectConflictStrategies)
                .setMergeAppendKey(mergeAppendKey)
                .setMergeResultStrategy(mergeResultStrategy)
                .build()
                .merge(primary, secondary);
        }
    }

    public static final class JsonMapperApi {
        private final MapperFactory mapperFactory = ServiceManager.getProvider(MapperFactory.class);

        private JsonMapperApi() {
            //no-op
        }

        public MapperBuilder getMapperBuilder() {

            return mapperFactory.newBuilder();
        }

        public JsonObject toJsonObject(Object object) {
            return mapperFactory.newMapper().toJson(object);
        }

        public JsonObject toJsonObject(Map<String, Object> objectMap) {
            return mapperFactory.newMapper().toJson(objectMap);
        }

        public JsonArray toJsonArray(Collection<Object> collection) {
            return mapperFactory.newMapper().toJson(collection);
        }

        public <T> T toClass(JsonObject jsonObject, Class<T> targetClass) {
            return mapperFactory.newMapper().toClass(jsonObject, targetClass);
        }

        public Map<String, Object> toMap(JsonObject jsonObject) {
            return mapperFactory.newMapper().toMap(jsonObject);
        }

        public List<?> toList(JsonArray jsonArray) {
            return mapperFactory.newMapper().toList(jsonArray);
        }

        public JsonValue<?> toValue(Object value) {
            return mapperFactory.newMapper().toValue(value);
        }
    }
}
