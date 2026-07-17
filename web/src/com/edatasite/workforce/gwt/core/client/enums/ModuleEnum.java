package com.edatasite.workforce.gwt.core.client.enums;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public enum ModuleEnum implements Serializable, IsSerializable, Constants {
    PM("pm", PM_URL),
    HRMS("hrms", HRMS_URL),
    ACCOUNTING("accounting", ACCOUNTING_URL),
    CRM("crm", CRM_URL),
    PAYROLL("payroll", PAYROLL_URL),
    DOCUMENTS("documents", DOCUMENTS_URL),
    REPORTING("reportingsystem", REPORTING_URL),
    SETTINGS("settings", SETTINGS_URL),
    BACKEND("backend", BACKEND_URL),
    MYACCOUNT("myaccount", MYACCOUNT_URL),
    MC("messagecenter", MESSAGECENTER_URL),
    MYWORKSPACE("workspace", MYWORKSPACE_URL),
    DEVELOPMENT("development", ""),
    TC("tc", TC_URL),
    TRAINING_CENTER("trainingcenter", TC_URL);
    private String code;
    private String url;

    ModuleEnum(String code, String url) {
        this.code = code;
        this.url = url;
    }

    public static ModuleEnum getModule(String code) {

        for (ModuleEnum module : values()) {
            if (module.getCode().equals(code)) {
                return module;
            }
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;

    }
}
