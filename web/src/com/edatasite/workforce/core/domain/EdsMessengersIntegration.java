package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.WhatsappCredentialsItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA,name = "messengers_integration")
public class EdsMessengersIntegration extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "whatsapp_token",length = 1000)
    private String whatsappToken;

    @Column(name = "whatsapp_number")
    private String whatsappNumber;

    @Column(name = "wabaid")
    private String whatsappBussinnessId;

    @Column(name = "phoneId")
    private String phoneNumberId;


    public WhatsappCredentialsItem toRpc() {
        WhatsappCredentialsItem item = new WhatsappCredentialsItem();
        item.setPhoneNumber(getWhatsappNumber());
        item.setAccessToken(getWhatsappToken());
        return item;
    }
    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getWhatsappToken() {
        return whatsappToken;
    }

    public void setWhatsappToken(String whatsappToken) {
        this.whatsappToken = whatsappToken;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getWhatsappBussinnessId() {
        return whatsappBussinnessId;
    }

    public void setWhatsappBussinnessId(String whatsappBussinnessId) {
        this.whatsappBussinnessId = whatsappBussinnessId;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(String phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }
}
