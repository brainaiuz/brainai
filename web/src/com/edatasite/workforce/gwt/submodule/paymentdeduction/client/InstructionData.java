package com.edatasite.workforce.gwt.submodule.paymentdeduction.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/6/11
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstructionData implements IsSerializable{
    private Integer objectID;
    private String text;

    public InstructionData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
