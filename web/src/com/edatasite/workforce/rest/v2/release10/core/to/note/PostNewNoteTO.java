package com.edatasite.workforce.rest.v2.release10.core.to.note;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 02/20/2018.
 */
public class PostNewNoteTO extends ResponseData {

    private String visibility;
    private String note;


    public PostNewNoteTO() {
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
