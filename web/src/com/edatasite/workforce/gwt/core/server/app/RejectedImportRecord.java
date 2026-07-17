package com.edatasite.workforce.gwt.core.server.app;

/**
 * Created by Shohruh on 27-Jun-16.
 */
public class RejectedImportRecord {
    private String data;
    private String comment;

    public RejectedImportRecord(String data) {
        this.data = data != null ? data.trim() : "";
    }

    public String getData() {
        return data != null ? data : "";
    }

    public void setData(String data) {
        this.data = data != null ? data.trim() : "";
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setErrorComment(String comment) {
        this.comment = comment;
    }

    public void appendErrorComment(String comment) {
        this.comment = (this.comment != null ? this.comment + " " : "") + comment;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
