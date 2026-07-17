package com.edatasite.workforce.rest.base.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 4/20/15 9:27 PM
 */
public enum ApiActionEnum implements IsSerializable {

    COPY("COPY"),
    MOVE("MOVE"),
    DELETE("DELETE"),
    START("START"),
    STOP("STOP"),
    RESET("RESET"),
    APPLY("APPLY"),
    OPEN("OPEN"),
    DOWNLOAD("DOWNLOAD"),
    APPROVE("APPROVE"),
    DRAFT("DRAFT"),
    SAVE("SAVE"),
    SUBMIT("SUBMIT"),
    DECLINE("DECLINE"),
    EDIT("EDIT"),
    IN("IN"),
    OUT("OUT"),
    REJECT("REJECT"),
    SEND("SEND"),
    CREATED("CREATED"),
    MOVED("MOVED"),
    UPDATED("UPDATED"),
    EDITED("EDITED"),
    DELETED("DELETED");

    public String code;

    ApiActionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
