package com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings;

import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;

public enum QuickAddSettingsForm {
    TASK(CustomFieldSection.Task.name(), ViewName.Task);

    private String formId;
    private ViewName viewName;

    QuickAddSettingsForm(String formId, ViewName viewName) {
        this.formId = formId;
        this.viewName = viewName;
    }

    public static QuickAddSettingsForm getByFormId(String formId) {
        for (QuickAddSettingsForm form : QuickAddSettingsForm.values()) {
            if (form.getFormId().equals(formId)) {
                return form;
            }
        }
        return null;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public ViewName getViewName() {
        return viewName;
    }

    public void setViewName(ViewName viewName) {
        this.viewName = viewName;
    }
}
