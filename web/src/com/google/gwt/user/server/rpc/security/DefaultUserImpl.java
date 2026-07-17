package com.google.gwt.user.server.rpc.security;

/**
 * Created by Normurod on 1/30/2017.
 */
public class DefaultUserImpl {
    private String userName;

    public DefaultUserImpl(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }
}
