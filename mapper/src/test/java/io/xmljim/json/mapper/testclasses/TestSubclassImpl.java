package io.xmljim.json.mapper.testclasses;

public class TestSubclassImpl implements TestSubclassInterface {
    private String name;
    private boolean status;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean getStatus() {
        return status;
    }
}
