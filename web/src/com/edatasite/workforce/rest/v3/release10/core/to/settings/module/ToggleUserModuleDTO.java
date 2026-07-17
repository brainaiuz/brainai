package com.edatasite.workforce.rest.v3.release10.core.to.settings.module;

public class ToggleUserModuleDTO {
    private String moduleCode;
    private Integer order;

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
