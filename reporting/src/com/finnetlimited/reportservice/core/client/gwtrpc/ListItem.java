package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by Virus on 6/9/2014.
 */
public class ListItem extends SelectItem {

    private String folderName;
    private String viewCode;
    private Integer xmlTemplateId;
    private Integer categoryId;
    private Boolean library;
    private Boolean asStarred;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getXmlTemplateId() {
        return xmlTemplateId;
    }

    public void setXmlTemplateId(Integer xmlTemplateId) {
        this.xmlTemplateId = xmlTemplateId;
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public Boolean isLibrary() {
        return Boolean.TRUE.equals(library);
    }

    public void setLibrary(Boolean isLibrary) {
        this.library = isLibrary;
    }

    public Boolean getAsStarred() {
        return Boolean.TRUE.equals(asStarred);
    }

    public void setAsStarred(Boolean asStarred) {
        this.asStarred = asStarred;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }
}
