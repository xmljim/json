package io.xmljim.json.parser.event;

import io.xmljim.json.factory.parser.JsonEventParserException;
import io.xmljim.json.factory.parser.JsonParserException;
import io.xmljim.json.factory.parser.ParserSettings;
import io.xmljim.json.factory.parser.event.JsonEvent;

import java.util.concurrent.Flow.Subscription;

abstract class BaseEventHandler extends EventHandlers {
    private boolean complete = false;
    private Subscription subscription;

    public BaseEventHandler() {
        super();
    }

    public BaseEventHandler(final ParserSettings settings) {
        super(settings);
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public String getDataValue(final JsonEvent event) {
        String dataVal = null;
        if (event.getData() != null) {
            try {
                dataVal = new String(event.getData().array(), this.getSettings().getCharacterSet());
            } catch (final Exception e) {
                throw new JsonEventParserException(e);
            }
        }

        return dataVal;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(getSettings().getRequestNextLength());

    }

    @Override
    public void onError(Throwable throwable) {
        throw new JsonParserException(throwable);
    }

    @Override
    public void onComplete() {
        complete = true;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    public void requestNext() {
        subscription.request(getSettings().getRequestNextLength());
    }

}
