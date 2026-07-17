package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.contact.client.rpc.SipuniSettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "sipuni_settings")
public class EdsSipuniSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "operator_number")
    private String operatorNumber;

    @Column(name = "sip_number")
    private String sipNumber;

    @Column(name = "secret_key")
    private String secretKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private EdsUser operator;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    private Integer companyId;

    public SipuniSettings getRPC(){
        SipuniSettings settings = new SipuniSettings();
        settings.setObjectID(getObjectID());
        settings.setOperator(getOperator() != null ? new SelectItem(getOperator().getObjectID(),getOperator().getName()) : null);
        settings.setSecretKey(getSecretKey());
        settings.setSipNumber(getSipNumber());
        settings.setOperatorNumber(getOperatorNumber());
        return settings;
    }



    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(String operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public String getSipNumber() {
        return sipNumber;
    }

    public void setSipNumber(String sipNumber) {
        this.sipNumber = sipNumber;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public EdsUser getOperator() {
        return operator;
    }

    public void setOperator(EdsUser operator) {
        this.operator = operator;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
