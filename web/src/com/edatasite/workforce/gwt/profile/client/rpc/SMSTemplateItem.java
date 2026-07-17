package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Azazello on 4/20/15.
 */
public class SMSTemplateItem implements IsSerializable {
    public static final String NAME = "NAME";
    public static final String MODULE = "MODULE";
    public static final String PROVIDER = "PROVIDER";
    public static final String DEFAULT = "DEFAULT";
    private Integer objectID;
    private String name;
    private Integer moduleID;
    private String moduleName;
    private String moduleCode;
    private String content;
    private boolean isDefault;
    private SelectItem[] modules;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModuleID() {
        return moduleID;
    }

    public void setModuleID(Integer moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public SelectItem[] getModules() {
        return modules;
    }

    public void setModules(SelectItem[] modules) {
        this.modules = modules;
    }
}
