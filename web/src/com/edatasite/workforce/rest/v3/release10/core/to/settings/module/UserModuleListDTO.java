package com.edatasite.workforce.rest.v3.release10.core.to.settings.module;

import java.util.List;

public class UserModuleListDTO {
    private String title;
    private List<UserModuleDTO> children;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserModuleDTO> getChildren() {
        return children;
    }

    public void setChildren(List<UserModuleDTO> children) {
        this.children = children;
    }
}
