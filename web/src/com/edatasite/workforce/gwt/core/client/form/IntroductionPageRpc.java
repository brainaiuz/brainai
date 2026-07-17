package com.edatasite.workforce.gwt.core.client.form;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IntroductionPageRpc implements IsSerializable {
    private Integer id;
    private String parentFormId;
    private String formId;
    private Boolean isActive=true;
    private String editorValue;
    private String okButtonName;
    private String cancelButtonName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentFormId() {
        return parentFormId;
    }

    public void setParentFormId(String parentFormId) {
        this.parentFormId = parentFormId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getEditorValue() {
        return editorValue;
    }

    public void setEditorValue(String editorValue) {
        this.editorValue = editorValue;
    }

    public String getOkButtonName() {
        return okButtonName;
    }

    public void setOkButtonName(String okButtonName) {
        this.okButtonName = okButtonName;
    }

    public String getCancelButtonName() {
        return cancelButtonName;
    }

    public void setCancelButtonName(String cancelButtonName) {
        this.cancelButtonName = cancelButtonName;
    }
}
