package com.edatasite.workforce.gwt.documents.client.gwtupload.file.events;

import com.google.gwt.event.shared.EventHandler;

public interface ProgressHandler extends EventHandler {
    void onProgress(ProgressEvent event);
}
