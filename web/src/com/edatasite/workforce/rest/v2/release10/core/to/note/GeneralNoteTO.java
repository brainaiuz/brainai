package com.edatasite.workforce.rest.v2.release10.core.to.note;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 02/16/2018.
 */
public class GeneralNoteTO extends ResponseData {

    private Integer id;
    private Integer owner_id;
    private String owner_name;
    private String owner_avatar;
    private String date;
    private String note_content;
    private String type;

    public GeneralNoteTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Integer owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_avatar() {
        return owner_avatar;
    }

    public void setOwner_avatar(String owner_avatar) {
        this.owner_avatar = owner_avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
