package com.edatasite.workforce.gwt.crm.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 09-Jul-2009
 * Time: 18:22:40
 * To change this template use File | Settings | File Templates.
 */
public class MessageTrackListItem implements IsSerializable {

    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private Integer clickCount;
    private Integer openedCount;
    private Integer contactType;
    private Integer crmAccountID;
    private Integer entityID;
    private String link;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getOpenedCount() {
        return openedCount;
    }

    public void setOpenedCount(Integer openedCount) {
        this.openedCount = openedCount;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getCrmAccountID() {
        return crmAccountID;
    }

    public void setCrmAccountID(Integer crmAccountID) {
        this.crmAccountID = crmAccountID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}