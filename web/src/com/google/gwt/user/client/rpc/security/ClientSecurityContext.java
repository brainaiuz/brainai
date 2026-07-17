package com.google.gwt.user.client.rpc.security;

/**
 * Created by Normurod on 1/30/2017.
 */
public class ClientSecurityContext {
    private static ClientSecurityContext instance = new ClientSecurityContext();

    public static ClientSecurityContext get() {
        return instance;
    }

    private ClientSecurityContext() {

    }

    private String sessionId = null;

    public boolean isLoggedIn(){
        return sessionId != null;
    }

    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }

    public String getSessionId(){
        return sessionId;
    }

    public void logOut(){
        sessionId = null;
    }
}
