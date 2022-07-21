package io.github.xmljim.json.merger.test;

import io.github.xmljim.json.factory.merge.MergeFactory;
import io.github.xmljim.json.factory.merge.MergeProcessor;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.merger.conflict.ArrayConflictStrategies;
import io.github.xmljim.json.merger.conflict.ObjectConflictStrategies;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Merger Tests")
class MergerTests {

    @Test
    @DisplayName("Create new MergeFactory")
    void testCreateNewMergeFactory() {
        MergeFactory factory = ServiceManager.getProvider(MergeFactory.class);
        assertNotNull(factory);
    }

    @Test
    @DisplayName("Can create a new MergeProcessor")
    void testCreateNewMergeProcessor() {
        MergeFactory factory = ServiceManager.getProvider(MergeFactory.class);
        MergeProcessor processor = factory.newMergeProcessor();
        assertNotNull(processor);
    }

    @Test
    @DisplayName("Simple Array Default Merge - No Conflicts")
    void whenMergingArraysWithNoConflicts_andDefaultConfig_ShouldReturnAllElements() {
        try (InputStream arrayAStream = getClass().getResourceAsStream("/simple-array-a.json");
             InputStream arrayBStream = getClass().getResourceAsStream("/simple-array-b.json");
             InputStream mergeResultStream = getClass().getResourceAsStream("/simple-default-array-merge-result.json")) {

            ParserFactory parserFactory = ServiceManager.getProvider(ParserFactory.class);


            JsonArray arrayA = parserFactory.newParser().parse(InputData.of(arrayAStream));
            JsonArray arrayB = parserFactory.newParser().parse(InputData.of(arrayBStream));
            JsonArray expectedResult = parserFactory.newParser().parse(InputData.of(mergeResultStream));

            MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);
            MergeProcessor mergeProcessor = mergeFactory.newMergeProcessor();

            JsonArray actualResult = mergeProcessor.merge(arrayA, arrayB);

            assertEquals(expectedResult, actualResult);

        } catch (IOException ioe) {
            fail(ioe);
        }
    }

    @Test
    @DisplayName("Test Array Conflict Strategy: Append")
    void testArrayAppendConflictStrategy() {
        String primaryArray = """
                ["a", "b", {"foo": "bar"}, 1, 1, 3, ["first", "last"]]
                """;

        String secondaryArray = """
                ["z", "b", {"foo": "bar"}, 1, 5, 6, ["first", "last"]]
                """;

        String expectedArray = """
                ["a", "z", "b", {"foo":"bar"}, 1, 1, 5, 3, 6, ["first", "last"]]
                """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonArray primary = factory.newParser().parse(InputData.of(primaryArray));
        JsonArray secondary = factory.newParser().parse(InputData.of(secondaryArray));
        JsonArray expected = factory.newParser().parse(InputData.of(expectedArray));
        MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);


        JsonArray actual = mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(ArrayConflictStrategies.APPEND)
                .build()
                .merge(primary, secondary);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Array Conflict Strategy: Append, Primary bigger than Secondary")
    void testArrayAppendConflictStrategyPrimaryBiggerThanSecondary() {
        String primaryArray = """
                ["a", "b", {"foo": "bar"}, 1, 1, 3, ["first", "last"], 99]
                """;

        String secondaryArray = """
                ["z", "b", {"foo": "bar"}, 1, 5, 6, ["first", "last"]]
                """;

        String expectedArray = """
                ["a", "z", "b", {"foo":"bar"}, 1, 1, 5, 3, 6, ["first", "last"], 99]
                """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonArray primary = factory.newParser().parse(InputData.of(primaryArray));
        JsonArray secondary = factory.newParser().parse(InputData.of(secondaryArray));
        JsonArray expected = factory.newParser().parse(InputData.of(expectedArray));
        MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);


        JsonArray actual = mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(ArrayConflictStrategies.APPEND)
                .build()
                .merge(primary, secondary);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Array Conflict Strategy: Append, Primary bigger than Secondary")
    void testArrayAppendConflictStrategyPrimarySmallerThanSecondary() {
        String primaryArray = """
                ["a", "b", {"foo": "bar"}, 1, 1, 3, ["first", "last"]]
                """;

        String secondaryArray = """
                ["z", "b", {"foo": "bar"}, 1, 5, 6, ["first", "last"], 99]
                """;

        String expectedArray = """
                ["a", "z", "b", {"foo":"bar"}, 1, 1, 5, 3, 6, ["first", "last"], 99]
                """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonArray primary = factory.newParser().parse(InputData.of(primaryArray));
        JsonArray secondary = factory.newParser().parse(InputData.of(secondaryArray));
        JsonArray expected = factory.newParser().parse(InputData.of(expectedArray));
        MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);


        JsonArray actual = mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(ArrayConflictStrategies.APPEND)
                .build()
                .merge(primary, secondary);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Array Conflict Strategy: Deduplicate")
    void testDeduplicateArrayStrategy() {
        String primaryArray = """
                ["a", "b", {"foo": "bar"}, 1, 1, [1,3,5], true]
                """;

        String secondaryArray = """
                ["z", "b", {"foo": "bar"}, 1, 5, [1,3,6], "a"]
                """;

        String expectedArray = """
                ["a", "z", "b", {"foo":"bar"}, 1, 5, [1,3,5,6], true]
                """;
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonArray primary = factory.newParser().parse(InputData.of(primaryArray));
        JsonArray secondary = factory.newParser().parse(InputData.of(secondaryArray));
        JsonArray expected = factory.newParser().parse(InputData.of(expectedArray));

        MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);


        JsonArray actual = mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(ArrayConflictStrategies.DEDUPLICATE)
                .build()
                .merge(primary, secondary);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Array Conflict Strategy: InsertBefore")
    void testArrayConflictStrategyInsertBefore() {
        String primaryArray = """
                ["first", "next-to-last", true, {"simple": "object"}, {"a":true}, [1,2],1,2,3]
                """;

        String secondaryArray = """
                ["second", "last", false, ["simple", "array"], {"b": false}, [3,4], 1,2,4]
                """;

        String expectedArray = """
                ["second", "first",  "last","next-to-last", false, true, ["simple", "array"],{"simple": "object"}, {"a": true, "b": false}, [3,1,4,2] 1,2,4,3]
                """;
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonArray primary = factory.newParser().parse(InputData.of(primaryArray));
        JsonArray secondary = factory.newParser().parse(InputData.of(secondaryArray));
        JsonArray expected = factory.newParser().parse(InputData.of(expectedArray));

        MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);


        JsonArray actual = mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(ArrayConflictStrategies.INSERT_BEFORE)
                .build()
                .merge(primary, secondary);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Array Conflict Strategy: InsertAfter")
    void testArrayConflictStrategyInsertAfter() {
        String primaryArray = """
                ["first", "next-to-last", true, {"simple": "object"}, {"a":true}, [1,2],1,2,3]
                """;

        String secondaryArray = """
                ["second", "last", false, ["simple", "array"],{"b": false}, [3,4], 1,2,4]
                """;

        String expectedArray = """
                ["first", "second", "next-to-last", "last", true, false, {"simple": "object"}, ["simple", "array"],{"a": true, "b": false}, [1,3,2,4], 1,2,3,4]
                """;
        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonArray primary = factory.newParser().parse(InputData.of(primaryArray));
        JsonArray secondary = factory.newParser().parse(InputData.of(secondaryArray));
        JsonArray expected = factory.newParser().parse(InputData.of(expectedArray));

        MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);


        JsonArray actual = mergeFactory.newMergeBuilder()
                .setArrayConflictStrategy(ArrayConflictStrategies.INSERT_AFTER)
                .build()
                .merge(primary, secondary);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Simple Object Merge - Defaults")
    void whenMergingObjectsWithNoConflicts_AndDefaultConfig_ShouldSeeAllElements() {

        String primaryJson = """
                {"foo": "bar", "bar": "baz"}
                """;
        String secondaryJson = """
                {"type": "dog", "color": "black"}
                """;

        String expectedResult = """
                {"foo": "bar", "bar": "baz", "type": "dog", "color": "black"}
                """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonObject primary = factory.newParser().parse(InputData.of(primaryJson));
        JsonObject secondary = factory.newParser().parse(InputData.of(secondaryJson));
        JsonObject expected = factory.newParser().parse(InputData.of(expectedResult));

        MergeFactory mergeFactory = ServiceManager.getProvider(MergeFactory.class);
        MergeProcessor processor = mergeFactory.newMergeProcessor();
        JsonObject actual = processor.merge(primary, secondary);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Object Conflict Strategy: Accept Primary")
    void testObjectConflictStrategyAcceptPrimary() {
        String primaryObject = """
                {"a": true, "b": [1,2], "c": [3,4], "d": {"foo": "bar"}, "e": false}
                """;

        String secondaryObject = """
                {"a": false, "b": [1,2], "c": [5,6], "d": {"bar": "baz"}, "e": null}
                """;

        String expectedObject = """
                {"a": true, "b": [1,2], "c": [3,5,4,6], "d": {"foo": "bar", "bar": "baz"}, "e": false}
                """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonObject primary = factory.newParser().parse(InputData.of(primaryObject));
        JsonObject secondary = factory.newParser().parse(InputData.of(secondaryObject));
        JsonObject expected = factory.newParser().parse(InputData.of(expectedObject));

        JsonObject actual = ServiceManager.getProvider(MergeFactory.class)
                .newMergeBuilder()
                .setObjectConflictStrategy(ObjectConflictStrategies.ACCEPT_PRIMARY)
                .build()
                .merge(primary, secondary);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Object Conflict Strategy: Accept Secondary")
    void testObjectConflictStrategyAcceptSecondary() {
        String primaryObject = """
                {"a": true, "b": [1,2], "c": [3,4], "d": {"foo": "bar"}, "e": false}
                """;

        String secondaryObject = """
                {"a": false, "b": [1,2], "c": [5,6], "d": {"bar": "baz"}, "e": null}
                """;

        String expectedObject = """
                {"a": false, "b": [1,2], "c": [3,5,4,6], "d": {"foo": "bar", "bar": "baz"}, "e": null}
                """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonObject primary = factory.newParser().parse(InputData.of(primaryObject));
        JsonObject secondary = factory.newParser().parse(InputData.of(secondaryObject));
        JsonObject expected = factory.newParser().parse(InputData.of(expectedObject));

        JsonObject actual = ServiceManager.getProvider(MergeFactory.class)
                .newMergeBuilder()
                .setObjectConflictStrategy(ObjectConflictStrategies.ACCEPT_SECONDARY)
                .build()
                .merge(primary, secondary);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Object Conflict Strategy: Append")
    void testObjectConflictStrategyAppend() {
        String primaryObject = """
                {"a": true, "b": [1,2], "c": [3,4], "d": {"foo": "bar"}, "e": false}
                """;

        String secondaryObject = """
                {"a": false, "b": [1,2], "c": [5,6], "d": {"bar": "baz"}, "e": null}
                """;

        String expectedObject = """
                {   "a": true,
                    "a_appended": false,
                    "b": [1,2],
                    "c": [3,5,4,6],
                    "d": {"foo": "bar", "bar": "baz"},
                    "e": false,
                    "e_appended": null
                }
                """;

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);

        JsonObject primary = factory.newParser().parse(InputData.of(primaryObject));
        JsonObject secondary = factory.newParser().parse(InputData.of(secondaryObject));
        JsonObject expected = factory.newParser().parse(InputData.of(expectedObject));

        JsonObject actual = ServiceManager.getProvider(MergeFactory.class)
                .newMergeBuilder()
                .setObjectConflictStrategy(ObjectConflictStrategies.APPEND)
                .setMergeAppendKey("_appended")
                .build()
                .merge(primary, secondary);

        assertEquals(expected, actual);
    }

}
