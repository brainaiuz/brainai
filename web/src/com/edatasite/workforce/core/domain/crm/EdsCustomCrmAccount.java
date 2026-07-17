package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "customCrmAccount")
public class EdsCustomCrmAccount extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "entity_id") // PI/SI etc id
    private Integer entityId;

    @Column(name = "entity_type") // SALES_INVOICE/SALES_QUOTE
    private String entityType;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "clientId")
    private Integer clientId;

    @Column(name = "clientName")
    private String clientName;

    @Column(name = "clientNumber")
    private String clientNumber;

    @Column(name = "vatNumber")
    private String vatNumber;

    @Column(name = "trnNumber")
    private String trnNumber;

    private String billingAddressName;
    private String billingAddress;
    private String billingAddressb;
    private String billingCity;
    private String billingCountryName;
    private String billingStateName;
    private String billingZipCode;

    private String mailingAddressName;
    private String mailingAddress;
    private String mailingAddressb;
    private String mailingCity;
    private String mailingCountryName;
    private String mailingStateName;
    private String mailingZipCode;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getTrnNumber() {
        return trnNumber;
    }

    public void setTrnNumber(String trnNumber) {
        this.trnNumber = trnNumber;
    }

    public String getBillingAddressName() {
        return billingAddressName;
    }

    public void setBillingAddressName(String billingAddressName) {
        this.billingAddressName = billingAddressName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingAddressb() {
        return billingAddressb;
    }

    public void setBillingAddressb(String billingAddressb) {
        this.billingAddressb = billingAddressb;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getBillingCountryName() {
        return billingCountryName;
    }

    public void setBillingCountryName(String billingCountryName) {
        this.billingCountryName = billingCountryName;
    }

    public String getBillingStateName() {
        return billingStateName;
    }

    public void setBillingStateName(String billingStateName) {
        this.billingStateName = billingStateName;
    }

    public String getBillingZipCode() {
        return billingZipCode;
    }

    public void setBillingZipCode(String billingZipCode) {
        this.billingZipCode = billingZipCode;
    }

    public String getMailingAddressName() {
        return mailingAddressName;
    }

    public void setMailingAddressName(String mailingAddressName) {
        this.mailingAddressName = mailingAddressName;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getMailingAddressb() {
        return mailingAddressb;
    }

    public void setMailingAddressb(String mailingAddressb) {
        this.mailingAddressb = mailingAddressb;
    }

    public String getMailingCity() {
        return mailingCity;
    }

    public void setMailingCity(String mailingCity) {
        this.mailingCity = mailingCity;
    }

    public String getMailingCountryName() {
        return mailingCountryName;
    }

    public void setMailingCountryName(String mailingCountryName) {
        this.mailingCountryName = mailingCountryName;
    }

    public String getMailingStateName() {
        return mailingStateName;
    }

    public void setMailingStateName(String mailingStateName) {
        this.mailingStateName = mailingStateName;
    }

    public String getMailingZipCode() {
        return mailingZipCode;
    }

    public void setMailingZipCode(String mailingZipCode) {
        this.mailingZipCode = mailingZipCode;
    }
}
