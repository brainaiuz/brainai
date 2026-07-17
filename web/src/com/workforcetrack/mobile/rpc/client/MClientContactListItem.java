package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.core.client.rpc.ClientContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 21.06.11
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MClientContactListItem {

    private Integer objectID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean primaryContact;
    private String position;
    private Boolean active;
    private Integer clientID;

    public MClientContactListItem() {
    }

    public MClientContactListItem(ContactListItem ccli) {
        if (ccli != null) {
            this.objectID = ccli.getObjectId();
            this.firstName = ccli.getFirstName();
            this.lastName = ccli.getLastName();
            this.email = ccli.getPrimaryEmail();
            this.phone = ccli.getPrimaryPhone();
            this.primaryContact = ccli.isPrimaryContact();
            this.position = ccli.getJobTitle();
            this.active = ccli.isAccessEnabled();
        }

    }

    public MClientContactListItem(ClientContact ccli) {
        if (ccli != null) {
            this.objectID = ccli.getObjectID();
            this.firstName = ccli.getFirstName();
            this.lastName = ccli.getLastName();
            this.email = ccli.getEmail();
            this.phone = ccli.getPhone();
            this.primaryContact = ccli.getPrimaryContact();
            this.position = ccli.getPosition();
            //this.active = ccli.isAccessEnabled();
        }

    }

    public Boolean getActive() {
        return active;
    }

    public ClientContact convertToContactListItem(ClientContact clientContact) {
        if (clientContact == null) {
            clientContact = new ClientContact();
        }
        clientContact.setObjectID(this.objectID);
        clientContact.setFirstName(this.firstName);
        clientContact.setLastName(this.lastName);
        clientContact.setEmail(this.email);
        clientContact.setPhone(this.phone);
        clientContact.setActive(this.active);
        clientContact.setClientId(this.clientID);
        clientContact.setPrimaryContact(this.primaryContact != null ? this.primaryContact : false);

        return clientContact;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }
}
