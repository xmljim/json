package io.xmljim.json.mapper.test.testclasses;

import java.util.Collection;

public record TestRecord(String name, int number, boolean isSet, Collection<String> values) {

}
