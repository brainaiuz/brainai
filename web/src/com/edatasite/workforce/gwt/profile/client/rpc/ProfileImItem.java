package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProfileImItem implements IsSerializable {
    private Integer objectId;
    private String profile;
    private String im;
    private String account;

    public Integer getObjectId() {
        return this.objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getIm() {
        return this.im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String accout) {
        this.account = accout;
    }
}
