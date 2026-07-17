package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 30.06.14
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public class HostBasedModuleSettingsItem implements Serializable {

    private String code;
    private String host;

    public HostBasedModuleSettingsItem() {
    }

    public HostBasedModuleSettingsItem(String code) {
        this.code = code;
    }

    public HostBasedModuleSettingsItem(String code, String host) {
        this.code = code;
        this.host = host;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
