package io.xmljim.json.mappertest.testclasses;

import io.xmljim.json.factory.mapper.annotation.JsonElement;

import java.util.List;

public class BasicTestClass {
    private String message;
    private boolean read;
    @JsonElement(setterMethod = "setFoo", key = "length")
    private Long count;
    private List<String> valueSet;
    //@JSONTargetClass(TestSubclassImpl.class)
    private TestSubclassImpl subclass;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
    }


    public void setFoo(Long value) {
        count = value;
    }

    /**
     * @return the valueSet
     */
    public List<String> getValueSet() {
        return valueSet;
    }

    /**
     * @param valueSet the valueSet to set
     */
    public void setValueSet(List<String> valueSet) {
        this.valueSet = valueSet;
    }

    /**
     * @return the subclass
     */
    public TestSubclassImpl getSubclass() {
        return subclass;
    }

    /**
     * @param subclass the subclass to set
     */
    public void setSubclass(TestSubclassImpl subclass) {
        this.subclass = subclass;
    }
}
