package com.edatasite.workforce.rest.v3.release10.core.to.settings.module;


public class UserModuleDTO {
    private String moduleCode;
    private boolean selected = false;
    private Integer order;
    private String title;

    public UserModuleDTO() {
    }

    public UserModuleDTO(String moduleCode, boolean selected, Integer order, String title) {
        this.moduleCode = moduleCode;
        this.selected = selected;
        this.order = order;
        this.title = title;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
