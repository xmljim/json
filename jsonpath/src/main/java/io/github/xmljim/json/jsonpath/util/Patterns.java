package io.github.xmljim.json.jsonpath.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Patterns {
    PATH(PatternConstants.PATH_PATTERN),
    CHILD_FILTER(PatternConstants.CHILD_FILTER),
    WILDCARD_FILTER(PatternConstants.WILDCARD_FILTER),
    RECURSION_FILTER(PatternConstants.RECURSION_FILTER),
    UNION_FILTER(PatternConstants.UNION_FILTER),
    SLICE_FILTER(PatternConstants.SLICE_FILTER),
    ;

    private final String pattern;

    Patterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean matches(String element) {
        return element.matches(getPattern());
    }

    public static Optional<Patterns> findFirstMatchingPattern(String element) {
        return Arrays.stream(values())
                .filter(patterns -> patterns.matches(element))
                .findFirst();
    }

    public static List<Patterns> findAllMatchingPatterns(String element) {
        return Arrays.stream(values())
                .filter(patterns -> patterns.matches(element))
                .toList();
    }

    public String toString() {
        return "[" + name() + "]=" + getPattern();
    }

}
