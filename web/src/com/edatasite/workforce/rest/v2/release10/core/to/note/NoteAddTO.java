package com.edatasite.workforce.rest.v2.release10.core.to.note;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d Madrahimov on 12/27/2017.
 */
public class NoteAddTO extends ResponseData {

    private String request_type;
    private Integer request_id;
    private String note;


    public NoteAddTO() {
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public Integer getRequest_id() {
        return request_id;
    }

    public void setRequest_id(Integer request_id) {
        this.request_id = request_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
