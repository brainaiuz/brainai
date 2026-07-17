package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Anvarbek
 * Date: 07.01.2008
 * Time: 14:47:09
 */
public class ContractListItem implements IsSerializable, ListingCustomFields {

    public static final String NUMBER = "number";
    public static final String CLIENT = "client";
    public static final String PROJECT = "project";
    public static final String OBJECT_ID = "objectId";
    public static final String LAST_NOTE_COMMENT = "lastNoteComment";
    public static final String CONTRACT_REGISTRATION_DATE = "contractRegistrationDate";
    public static final String CONTRACT_START_DATE = "startDate";
    public static final String CONTRACT_END_DATE = "dueDate";

    private String client;
    private String number;
    private Integer objectId;
    private String allowanceByClient;
    private String project;
    private String lastNoteComment;
    private DateNonConvertable contractBeginDate;
    private DateNonConvertable contractEndDate;
    private DateNonConvertable creationTime;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getAllowanceByClient() {
        return allowanceByClient;
    }

    public void setAllowanceByClient(String allowanceByClient) {
        this.allowanceByClient = allowanceByClient;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProject() {
        return project;
    }

    public String getLastNoteComment() {
        return lastNoteComment;
    }

    public void setLastNoteComment(String lastNoteComment) {
        this.lastNoteComment = lastNoteComment;
    }

    public void setContractBeginDate(DateNonConvertable contractBeginDate) {
        this.contractBeginDate = contractBeginDate;
    }

    public DateNonConvertable getContractBeginDate() {
        return contractBeginDate;
    }

    public void setContractEndDate(DateNonConvertable contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public DateNonConvertable getContractEndDate() {
        return contractEndDate;
    }

    public DateNonConvertable getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateNonConvertable creationTime) {
        this.creationTime = creationTime;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
        if (customFields != null && customFields.size() > 0) {
            for (CompanyCustomFieldItem customField : customFields) {
                if ((customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue())) || customField.getFieldDateNonConvertedValue() != null) {
                    if (customField.getColumnCode() != null) {
                        Object value = null;
                        if ((customField.getDataType().equals(CompanyCustomFieldItem.TEXT) || customField.getDataType().equals(CompanyCustomFieldItem.NUMBER)) && customField.getFieldStringValue() != null) {
                            try {
                                value = customField.getDataType().equals(CompanyCustomFieldItem.TEXT) ? customField.getFieldStringValue() : Double.valueOf(customField.getFieldStringValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (customField.getDataType().equals(CompanyCustomFieldItem.DATE) && customField.getFieldDateNonConvertedValue() != null) {
                                value = customField.getFieldDateNonConvertedValue().getNonConvertedDate();
                            }
                        }
                        if (value != null) {
                            getCustomFieldsMap().put(customField.getColumnCode(), value);
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }
}
