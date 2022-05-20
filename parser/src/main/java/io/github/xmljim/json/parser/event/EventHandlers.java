package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.ParserSettings;
import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;

public abstract class EventHandlers implements EventHandler {
    private ParserSettings settings;
    private Assembler<?> assembler;

    public EventHandlers() {

    }

    public EventHandlers(ParserSettings settings) {
        setSettings(settings);
    }

    @Override
    public void setSettings(ParserSettings settings) {
        this.settings = settings;
        this.assembler = settings.getAssembler();
    }

    @Override
    public ParserSettings getSettings() {
        return settings;
    }

    @Override
    public Assembler<?> getAssembler() {
        return assembler;
    }

    public static EventHandler defaultEventHandler(ParserSettings settings) {
        return new StackEventHandler(settings);
    }


}
