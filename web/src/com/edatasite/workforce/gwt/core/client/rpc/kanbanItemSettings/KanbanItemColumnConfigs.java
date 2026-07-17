package com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KanbanItemColumnConfigs implements IsSerializable {
    private Integer objectId;
    private String code;
    private String title;
    private Boolean selected;
    private String relatedFieldCode;
    private Boolean mandatory;
    private Boolean changeable;
    private String localizationCode;
    private String localizationName;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRelatedFieldCode() {
        return relatedFieldCode;
    }

    public void setRelatedFieldCode(String relatedFieldCode) {
        this.relatedFieldCode = relatedFieldCode;
    }

    public Boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean isChangeable() {
        return changeable;
    }

    public void setChangeable(Boolean changeable) {
        this.changeable = changeable;
    }

    public String getLocalizationCode() {
        return localizationCode;
    }

    public void setLocalizationCode(String localizationCode) {
        this.localizationCode = localizationCode;
    }

    public String getLocalizationName() {
        return localizationName;
    }

    public void setLocalizationName(String localizationName) {
        this.localizationName = localizationName;
    }
}
