package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

public class ShadowAccount implements Serializable {
    private String login;
    private String random;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }
}
