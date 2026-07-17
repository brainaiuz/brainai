package com.edatasite.workforce.gwt.documents.client.gwtupload.file.events;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;

public abstract class ProgressEventBase<H extends EventHandler> extends DomEvent<H> {
    public boolean lengthComputable() {
        return lengthComputable(getNativeEvent());
    }

    private static final native boolean lengthComputable(NativeEvent event) /*-{
        return event.lengthComputable;
    }-*/;

    public int loaded() {
        return loaded(getNativeEvent());
    }

    private static final native int loaded(NativeEvent event) /*-{
        return event.loaded;
    }-*/;

    public int total() {
        return total(getNativeEvent());
    }

    private static final native int total(NativeEvent event) /*-{
        return event.total;
    }-*/;
}

