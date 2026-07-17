package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11.03.13
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class SignatureItem implements IsSerializable {
    private Integer objectID;
    private Integer userID;
    private String userName;
    private String signature;
    private boolean showSignatureOnTop;

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public boolean isShowSignatureOnTop() {
        return showSignatureOnTop;
    }

    public void setShowSignatureOnTop(boolean showSignatureOnTop) {
        this.showSignatureOnTop = showSignatureOnTop;
    }
}
