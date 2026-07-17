package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 10.07.2009
 * Time: 16:53:49
 * To change this template use File | Settings | File Templates.
 */
public class ClientsByAmmount implements IsSerializable {

    private Integer clientID;
    private String clientName;
    private Double ammount;

    public ClientsByAmmount() {
    }

    public ClientsByAmmount(Integer clientID, String clientName, Double ammount) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.ammount = ammount;
    }

    public ClientsByAmmount(Integer clientID, String clientName, BigDecimal ammount) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.ammount = ammount != null ? ammount.doubleValue() : 0;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }
}
