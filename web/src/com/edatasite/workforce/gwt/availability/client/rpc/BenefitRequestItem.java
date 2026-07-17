package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Djuraev on 8/6/15.
 */
public class BenefitRequestItem implements IsSerializable {

    public static final String REQUESTER = "REQUESTER";
    public static final String APPROVER = "APPROVER";
    public static final String BENEFIT_TYPE = "BENEFIT_TYPE";
    public static final String REQUESTED_QUANTITY = "REQUESTED_QUANTITY";
    public static final String DATE = "DATE";
    public static final String STATUS = "STATUS";

    private Integer objectID;
    private Integer requesterID;
    private String requester;
    private Integer approverID;
    private String approver;
    private DateNonConvertable date;
    private String description;
    private String rejectionReason;
    private double requestedQuantity;
    private Integer year;
    private SelectItem status;

    private SelectItem[] benefitItem;
    private Integer benefitID;
    private String benefitName;
    private SelectItem quantityType;
    private SelectItem benefitCurrency;

    private String user;
    private Integer userID;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldValues;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public Integer getApproverID() {
        return approverID;
    }

    public void setApproverID(Integer approverID) {
        this.approverID = approverID;
    }

    public Integer getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(Integer requesterID) {
        this.requesterID = requesterID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem[] getBenefitItem() {
        return benefitItem;
    }

    public void setBenefitItem(SelectItem[] benefitItem) {
        this.benefitItem = benefitItem;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public double getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(double requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public Integer getBenefitID() {
        return benefitID;
    }

    public void setBenefitID(Integer benefitID) {
        this.benefitID = benefitID;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public SelectItem getStatus() {
        return status == null ? new SelectItem() : status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(SelectItem quantityType) {
        this.quantityType = quantityType;
    }

    public SelectItem getBenefitCurrency() {
        return benefitCurrency;
    }

    public void setBenefitCurrency(SelectItem benefitCurrency) {
        this.benefitCurrency = benefitCurrency;
    }
    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public HashMap<String, Object> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }
}
