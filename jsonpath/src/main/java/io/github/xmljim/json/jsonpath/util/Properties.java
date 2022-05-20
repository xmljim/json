package io.github.xmljim.json.jsonpath.util;

public interface Properties {
    static final String KEY_DATE_FORMAT = "jsonpath.date.format";
    static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    static final String KEY_DATE_PARSE_FROM_LONG = "jsonpath.date.parseFromLong";
    static final boolean DEFAULT_DATE_PARSE_FROM_LONG = false;

    static final String KEY_DATE_TIME_PARSE_FROM_LONG = "jsonpath.datetime.parseFromLong";
    static final boolean DEFAULT_DATE_TIME_PARSE_FROM_LONG = false;

    static final String KEY_DATE_TIME_FORMAT = "jsonpath.datetime.format";
    static final String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + "'T'HH:mm:ss'Z'";

}
