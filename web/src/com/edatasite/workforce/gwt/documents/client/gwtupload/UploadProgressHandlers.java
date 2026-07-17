package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.google.gwt.json.client.JSONValue;

public interface UploadProgressHandlers {
    void onProgress(String id, String filename, int loaded, int total);

    void onComplete(String id, String filename, JSONValue response);

    void onCancel(String id, String filename);

    void onFailure(String id, String filename);

    int onSubmit(String id, String filename);
}
