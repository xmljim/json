package io.github.xmljim.json.jsonpath.test.util;

import io.github.xmljim.json.jsonpath.util.Patterns;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatternsTest {

    private static final String failureMessage = "Path pattern failed %s [%s]";

    @BeforeAll
    static void printAllPatterns() {
        Arrays.stream(Patterns.values()).forEach(System.out::println);
    }

    @Test
    @DisplayName("Test [PATH]: Bracket Key Selectors")
    void testValidJsonPathKeyPatternWithBrackets() {
        String path = "$['foo']['bar']";
        testPathWithPattern(path, Patterns.PATH);
    }

    @Test
    @DisplayName("Test [PATH]: Dot Key Selectors")
    void testValidJsonPathKeyPatternsWithDots() {
        String path = "$.foo.bar";
        testPathWithPattern(path, Patterns.PATH);
    }

    @Test
    @DisplayName("Test [PATH]: Dot and Bracket Key Selectors")
    void testValidJsonPathKeyPatternsWithDotsAndBrackets() {
        String path1 = "$['foo'].bar";
        String path2 = "$.foo['bar']";

        testPathWithPattern(path1, Patterns.PATH);
        testPathWithPattern(path2, Patterns.PATH);
    }

    @Test
    @DisplayName("Test [PATH] Index Selectors")
    void testValidJsonPathIndexPatterns() {
        String path1 = "$[0]";
        String path2 = "$[-1]";
        String path3 = "@[0][-2][1]";

        testPathWithPattern(path1, Patterns.PATH);
        testPathWithPattern(path2, Patterns.PATH);
        testPathWithPattern(path3, Patterns.PATH);
    }

    @Test
    @DisplayName("Test [CHILD_FILTER] Patterns")
    void testChildFilterPatterns() {
        String path1 = "[0]";
        String path2 = "['foo']";
        String path3 = ".bar_z";
        String path4 = "[-1]";

        testPathWithPattern(path1, Patterns.CHILD_FILTER);
        testPathWithPattern(path2, Patterns.CHILD_FILTER);
        testPathWithPattern(path3, Patterns.CHILD_FILTER);
        testPathWithPattern(path4, Patterns.CHILD_FILTER);
    }

    @Test
    @DisplayName("Test [WILDCARD_FILTER] Patterns")
    void testWildCardFilterPatterns() {
        String path1 = ".*";
        String path2 = "[*]";

        testPathWithPattern(path1, Patterns.WILDCARD_FILTER);
        testPathWithPattern(path2, Patterns.WILDCARD_FILTER);
    }

    @Test
    @DisplayName("Test [PATH] With [WILDCARD_FILTER] Patterns")
    void testPathExpressionWithWildcardFilters() {
        String path1 = "$[*]";
        String path2 = "$.*";

        testPathWithPattern(path1, Patterns.PATH);
        testPathWithPattern(path2, Patterns.PATH);
    }

    @Test
    @DisplayName("Test [RECURSION_FILTER] Patterns")
    void testRecursionFilterPatterns() {
        String path1 = "..";
        String path2 = "[..]";

        testPathWithPattern(path1, Patterns.RECURSION_FILTER);
        testPathWithPattern(path2, Patterns.RECURSION_FILTER);
    }

    @Test
    @DisplayName("Test [PATH] With [RECURSION_FILTER] Patterns")
    void testPathExpressionWithRecursionPatterns() {
        String path1 = "$..";
        String path2 = "@[..]";

        testPathWithPattern(path1, Patterns.PATH);
        testPathWithPattern(path2, Patterns.PATH);
    }

    @Test
    @DisplayName("Test [UNION_FILTER] Patterns")
    void testUnionFilterPatterns() {
        String path1 = "[0,1]";
        String path2 = "['foo', 'bar']";

        testPathWithPattern(path1, Patterns.UNION_FILTER);
        testPathWithPattern(path2, Patterns.UNION_FILTER);
    }

    @Test
    @DisplayName("Test [PATH] With [UNION_FILTER] Patterns")
    void testPathExpressionWithUnionFilterPatterns() {
        String path1 = "$.foo[0,1]";
        String path2 = "@[1]['foo', 'bar']";

        testPathWithPattern(path1, Patterns.PATH);
        testPathWithPattern(path2, Patterns.PATH);
    }

    @Test
    @DisplayName("Test [SLICE_FILTER] Patterns")
    void testSliceFilterPatterns() {
        String path1 = "[-1: ]";
        String path2 = "[1:]";
        String path3 = "[:2]";
        String path4 = "[:-2]";
        String path5 = "[0: 2]";
        String path6 = "[-1:-2]";

        testPathWithPattern(path1, Patterns.SLICE_FILTER);
        testPathWithPattern(path2, Patterns.SLICE_FILTER);
        testPathWithPattern(path3, Patterns.SLICE_FILTER);
        testPathWithPattern(path4, Patterns.SLICE_FILTER);
        testPathWithPattern(path5, Patterns.SLICE_FILTER);
        testPathWithPattern(path6, Patterns.SLICE_FILTER);
    }

    @Test
    @DisplayName("Test [PATH] with [SLICE_FILTER] Patterns")
    void testPathExpressionWithSliceFilters() {
        String path1 = "$['foo'][-1: ]";
        String path2 = "$.*[1:]";
        String path3 = "$..[:2]";
        String path4 = "$[0,1][:-2]";
        String path5 = "@[0: 2]";
        String path6 = "@[0][-1:-2]";

        testPathWithPattern(path1, Patterns.PATH);
        testPathWithPattern(path2, Patterns.PATH);
        testPathWithPattern(path3, Patterns.PATH);
        testPathWithPattern(path4, Patterns.PATH);
        testPathWithPattern(path5, Patterns.PATH);
        testPathWithPattern(path6, Patterns.PATH);
    }

    private void testPathWithPattern(String path, Patterns pattern) {
        assertTrue(pattern.matches(path), getErrorMessage(path, pattern));
        assertEquals(Optional.of(pattern), Patterns.findFirstMatchingPattern(path));
        assertEquals(1, Patterns.findAllMatchingPatterns(path).size());
    }

    private String getErrorMessage(String path, Patterns patterns) {
        return String.format(failureMessage, path, patterns);
    }
}
