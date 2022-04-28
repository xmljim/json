package io.xmljim.json.parser.event;

import io.xmljim.json.factory.parser.event.Assembler;

public abstract class Assemblers<T> implements Assembler<T> {
    public static Assembler<?> newDefaultAssembler() {
        return new JsonAssemblerImpl();
    }


}
