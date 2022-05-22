package io.github.xmljim.json.merger.test;

import io.github.xmljim.json.factory.merge.MergeFactory;
import io.github.xmljim.json.factory.merge.MergeProcessor;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Merger Tests")
public class MergerTests {

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
}
