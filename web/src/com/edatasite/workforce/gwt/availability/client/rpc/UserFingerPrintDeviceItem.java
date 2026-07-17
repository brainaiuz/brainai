package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Muhammad on 23.04.2016.
 */
public class UserFingerPrintDeviceItem implements IsSerializable {

    private Integer userId;
    private String userName;
    private String deviceId;
    private String fingerPrintId;
    private SelectItem usersList;
    private ArrayList<SelectItem> deviceList;
    private HashMap<String, String> userDeviceFingerPrint;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFingerPrintId() {
        return fingerPrintId;
    }

    public void setFingerPrintId(String fingerPrintId) {
        this.fingerPrintId = fingerPrintId;
    }

    public SelectItem getUsersList() {
        return usersList;
    }

    public void setUsersList(SelectItem usersList) {
        this.usersList = usersList;
    }

    public ArrayList<SelectItem> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(ArrayList<SelectItem> deviceList) {
        this.deviceList = deviceList;
    }

    public HashMap<String, String> getUserDeviceFingerPrint() {
        return userDeviceFingerPrint;
    }

    public void setUserDeviceFingerPrint(HashMap<String, String> userDeviceFingerPrint) {
        this.userDeviceFingerPrint = userDeviceFingerPrint;
    }
}
