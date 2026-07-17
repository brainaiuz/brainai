package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 10/11/15 8:18 PM
 */
public class DashboardWidgetItem implements IsSerializable {
    private String name;
    private String code;
    private String section;
    private String view;
    private boolean fromCompany;
    private boolean fromUser;
    private boolean isAllow;
    private Integer userID;
    private Integer order;

    public DashboardWidgetItem() {

    }

    public DashboardWidgetItem(String name, String code, String section,String view) {
        this.name = name;
        this.code = code;
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(boolean fromCompany) {
        this.fromCompany = fromCompany;
    }

    public boolean isFromUser() {
        return fromUser;
    }

    public void setFromUser(boolean fromUser) {
        this.fromUser = fromUser;
    }

    public boolean isAllow() {
        return isAllow;
    }

    public void setAllow(boolean isAllow) {
        this.isAllow = isAllow;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
