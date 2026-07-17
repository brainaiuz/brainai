package com.edatasite.workforce.gwt.core.client.rpc.employee;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by user on 1/12/2016.
 */
public class GoogleMarketPlaceEmployee implements IsSerializable {

    private String fname;
    private String lname;
    private String email;
    private String photoURL;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
