package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public abstract class UploadHandlerAbstract {
    protected Options options;
    private List<String> queue;
    protected UploadProgressHandlers progressHandlers;
    protected static final WfmMessages messages = WfmMessages.App.get();

    protected abstract String add(Object o);

    public void upload(String id) {
        this.queue.add(id);
        uploadFile(id);
    }

    public void cancel(String id) {
        cancelUploadFile(id);
        deQueueFiles(id);
    }

    public void cancelAll() {
        List<String> queueCopy = new ArrayList<String>(queue);
        for (String s : queueCopy) {
            cancelUploadFile(s);
        }
        for (String s : queueCopy) {
            deQueueFiles(s);
        }
    }

    protected abstract String getName(String id);

    protected abstract int getSize(String id);

    @Deprecated
    protected List<String> getQueue() {
        return queue;
    }

    protected abstract String uploadFile(String id);

    protected abstract void cancelUploadFile(String id);

    protected void deQueueFiles(String id) {
        final int i = queue.indexOf(id);
        queue.remove(i);

        if (queue.size()>i) {
            String nextId = queue.get(i);
            this.uploadFile(nextId);
        }
    }

    protected UploadHandlerAbstract(UploadProgressHandlers progressHandlers, Options options) {
        this.progressHandlers = progressHandlers;
        this.options = options;
        this.queue = new ArrayList<>();
    }

    protected void log(String s) {
        GWT.log(s);
    }

    protected void showError(String message) {
        Window.alert(message);
    }
}
