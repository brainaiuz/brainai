package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 4/30/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingDBUrlListItem implements IsSerializable {

    private Integer id;
    private String dbUrl;
    private String userName;
    private String password;
    private ArrayList<SelectItem> company= new ArrayList<>();
    private Boolean encrypt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<SelectItem> getCompany() {
        return company;
    }

    public void setEncrypt(Boolean encrypt) {
        this.encrypt = encrypt;
    }

    public Boolean getEncrypt() {
        return encrypt;
    }
}
