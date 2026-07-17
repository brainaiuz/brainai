package com.edatasite.workforce.rest.base.enums;


import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 3/31/15.
 */
public enum NoteEnum implements IsSerializable {

    PUBLIC("PUBLIC", "Public"),
    PRIVATE("PRIVATE", "Private"),
    INTERNAL("INTERNAL", "Internal");

    private String code;
    private String name;

    NoteEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
