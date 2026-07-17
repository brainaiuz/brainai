package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov on 09/04/2018.
 */
public class DocumentSectionPermissionTO extends ResponseData {

    private String title;
    private String code;
    private boolean has_access;

    public DocumentSectionPermissionTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHas_access() {
        return has_access;
    }

    public void setHas_access(boolean has_access) {
        this.has_access = has_access;
    }
}
