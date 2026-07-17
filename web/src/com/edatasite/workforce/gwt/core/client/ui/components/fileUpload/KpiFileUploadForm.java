package com.edatasite.workforce.gwt.core.client.ui.components.fileUpload;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUploadDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 12/13/17.
 */
public class KpiFileUploadForm extends Composite implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static FileUploadFormUiBinder ourUiBinder = GWT.create(FileUploadFormUiBinder.class);
    @UiField
    Label label;
    @UiField
    Div filePanel;
    @UiField
    Div attachButton;
    ArrayList<FileResource> uploadedFiles = new ArrayList<>();
    private GWTFileUploadDialog fileUploadDialog;

    private Integer uploadType;

    public KpiFileUploadForm(Integer uploadType) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.uploadType = uploadType;
        initForm();
    }

    private void initForm() {
        label.setText(wfmStrings.attachmentsUpload());

        fileUploadDialog = new GWTFileUploadDialog(uploadType, null, null);
        fileUploadDialog.onLoadCommand(this::refreshFilePanel);
        fileUploadDialog.setActivator(attachButton);
    }

    private void refreshFilePanel() {
        ArrayList<FileResource> fileResources = new ArrayList<>();

        if (fileUploadDialog.getUploadedFiles() != null) {
            fileResources.addAll(fileUploadDialog.getUploadedFiles().values());
        }
        addFiles(fileResources);
    }

    public void addFiles(ArrayList<FileResource> fileResources) {
        filePanel.clear();
        uploadedFiles.addAll(fileResources);

        for (FileResource fr : uploadedFiles) {
            KpiFile file = new KpiFile(fr);
            file.deleteHandler(event -> deleteFile(fr));
            filePanel.add(file);
        }
    }

    public ArrayList<FileResource> getFiles() {
        return uploadedFiles;
    }

    private void deleteFile(FileResource file) {
        uploadedFiles.remove(file);
    }

    public void clearFiles() {
        filePanel.clear();
        uploadedFiles.clear();
    }

    interface FileUploadFormUiBinder extends UiBinder<Widget, KpiFileUploadForm> {
    }
}
