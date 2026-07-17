package com.edatasite.workforce.gwt.core.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

public class RunWebHookRequestItem implements IsSerializable {
    private String formId;
    private String relationType;
    private Integer relationId;
    private HashMap<String, Object> keyValues;
    private ItemTableEnum itemTableType;
    private String uuid;

    public RunWebHookRequestItem() {
    }

    public RunWebHookRequestItem(String formId, String relationType, Integer relationId, HashMap<String, Object> keyValues, ItemTableEnum itemTableType, String uuid) {
        this.formId = formId;
        this.relationType = relationType;
        this.relationId = relationId;
        this.keyValues = keyValues;
        this.itemTableType = itemTableType;
        this.uuid = uuid;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public HashMap<String, Object> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(HashMap<String, Object> keyValues) {
        this.keyValues = keyValues;
    }

    public ItemTableEnum getItemTableType() {
        return itemTableType;
    }

    public void setItemTableType(ItemTableEnum itemTableType) {
        this.itemTableType = itemTableType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
