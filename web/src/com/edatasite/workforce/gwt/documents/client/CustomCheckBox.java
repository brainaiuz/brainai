package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import gwt.material.design.client.ui.html.Div;

public class CustomCheckBox extends Div {
    private Integer fileID;
    private KpiCheckBox checkBox;
    private FileResource fileResource;
    public CustomCheckBox(Integer fileID, String name) {
        super("kpi-upload__checkbox-item");
        this.fileID = fileID;
        checkBox = new KpiCheckBox(name);
        add(checkBox);
    }

    public FileResource getFileResource() {
        return fileResource;
    }

    public void setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
    }

    public void addValueChangeHandler(ValueChangeHandler handler) {
        checkBox.addValueChangeHandler(handler);
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public Boolean getValue() {
        return checkBox.getValue();
    }

}
