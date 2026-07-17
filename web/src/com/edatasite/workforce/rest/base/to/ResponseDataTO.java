package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov on 3/30/15.
 */
public class ResponseDataTO implements IsSerializable {

    private Object result;
    private String messageType;
    private String message;
    private Integer status;


    public ResponseDataTO() {
    }

    public ResponseDataTO(Object result, String messageType, String message, Integer status) {
        this.result = result;
        this.messageType = messageType;
        this.message = message;
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
