package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/3/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeaturesMoreList implements IsSerializable {
    /*Table dagi column namelarini saqlovchi Param lar*/
    public static final String FEATURE_SUBJECT = "message_subject";
    public static final String FEATURE_DESCRIPTION = "message";
    public static final String FEATURE_LANGUAGE = "locale";


    private Integer messageID;
    private String message_code;
    private String message_subject;
    private String message;
    private String locale;

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    public String getMessage_code() {
        return message_code;
    }

    public void setMessage_code(String message_code) {
        this.message_code = message_code;
    }

    public String getMessage_subject() {
        return message_subject;
    }

    public void setMessage_subject(String message_subject) {
        this.message_subject = message_subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
