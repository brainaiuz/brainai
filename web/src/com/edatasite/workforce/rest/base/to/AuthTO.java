package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek.
 */
public class AuthTO implements IsSerializable {
    String username;
    String password;

    UserTO user;
    String session;
    Integer companyId;

    public AuthTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}
