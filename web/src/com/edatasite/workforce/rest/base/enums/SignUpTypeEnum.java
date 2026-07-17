package com.edatasite.workforce.rest.base.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 4/20/15 9:27 PM
 */
public enum SignUpTypeEnum implements IsSerializable {

    WEB_API("WEB_API"),
    IPHONE("IPHONE"),
    ANDROID("ANDROID");

    public String code;

    SignUpTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
