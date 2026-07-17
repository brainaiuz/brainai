package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.documents.client.gwtupload.file.File;
import com.edatasite.workforce.gwt.documents.client.gwtupload.xhr.XMLHttpRequestAdvanced;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.xhr.client.XMLHttpRequest;

import java.util.HashMap;
import java.util.Map;


public class UploadHandlerXhr extends UploadHandlerAbstract {
    private Map<String, File> files;
    private Map<String, XMLHttpRequest> xhrs;
    private Map<String, Integer> loaded;

    public UploadHandlerXhr(UploadProgressHandlers handlers, Options options) {
        super(handlers, options);
        files = new HashMap<>();
        xhrs = new HashMap<>();
        loaded = new HashMap<>();
    }

    @Override
    protected String add(Object o) {
        final String id = UUID.uuid();
        files.put(id, (File) o);
        return id;
    }

    @Override
    protected String getName(String id) {
        return files.get(id).getName();
    }

    @Override
    protected int getSize(String id) {
        return files.get(id).getSize();
    }

    @Override
    protected String uploadFile(final String id) {
        final File file = files.get(id);
        if (file == null) {
            return null;
        }
        final String name = getName(id).replace("\\+", "");

        loaded.put(id, 0);

        XMLHttpRequestAdvanced xhr = XMLHttpRequestAdvanced.create();
        xhrs.put(id, xhr);

        xhr.setOnUploadProgress(e -> {
            if (e.isLengthComputable()) {
                loaded.put(id, e.getLoaded());
                UploadHandlerXhr.super.progressHandlers.onProgress(id, name, e.getLoaded(), e.getTotal());
            }
        });

        xhr.setOnReadyStateChange(xhr1 -> {
            if (xhr1.getReadyState() == XMLHttpRequest.DONE) {
                onComplete(id, xhr1);
            }
        });

        xhr.open("POST", options.getAction());
        String storage = options.getStorage();
        xhr.setRequestHeader(CommandConstants.UPLOAD_TYPE_PARAM_NAME, storage);
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.setRequestHeader("X-File-Name", URL.encode(id + "_upld_" + name));
        xhr.setRequestHeader("Content-Type", "application/octet-stream");
        xhr.send(file);
        log("File with id `" + id + "` was just sent to server");
        return null;
    }

    @Override
    protected void cancelUploadFile(String id) {
        progressHandlers.onCancel(id, files.get(id).getName());
        files.remove(id);
        final XMLHttpRequest xhr = xhrs.get(id);
        if (xhr != null) {
            xhr.abort();
            xhr.clearOnReadyStateChange();
            xhrs.remove(id);
        }
    }

    private void onComplete(String id, XMLHttpRequest xhr) {
        log("File with id `" + id + "` has been successfully uploaded");

        if (files.get(id) == null) {
            return;
        }

        String name = getName(id);
        int size = getSize(id);

        progressHandlers.onProgress(id, name, size, size);

        JSONValue response = new JSONString("");
        if (xhr.getStatus() == 200) {
            response = JSONParser.parseStrict(xhr.getResponseText());
            progressHandlers.onComplete(id, name, response);
        } else {
            progressHandlers.onFailure(id, name);
//            showError("Error: " + messages.errorUploadingFile(name));
        }

        files.remove(id);
        xhr.clearOnReadyStateChange();
        xhrs.remove(id);
    }

    public native static boolean isSupported() /*-{
        var input = document.createElement('input');
        input.type = 'file';

        return (
                'multiple' in input &&
                        typeof File != "undefined" &&
                        typeof (new XMLHttpRequest()).upload != "undefined" );
    }-*/;
}
