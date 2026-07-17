package com.edatasite.workforce.gwt.documents.client.footerFileUpload;

import com.edatasite.workforce.gwt.core.client.ui.enums.FileUploadType;
import com.edatasite.workforce.gwt.documents.client.FileUploadDialog;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUploadDialog;

public class FooterGWTFileUploadDialog extends GWTFileUploadDialog {
    private InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider;

    public FooterGWTFileUploadDialog(int folderType, Integer folderID, Integer entityID, InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider) {
        super(folderType, folderID, entityID);
        this.inDatabaseFileWidgetProvider = inDatabaseFileWidgetProvider;
    }

    @Override
    public FileUploadDialog createFileUploadDIalog(FolderResource folderResource, FileUploadType fileUploadType, Integer maxFileSize, boolean isFromDocumentSection) {
        return new FileUploadDialog(folderResource, fileUploadType, maxFileSize, isFromDocumentSection, inDatabaseFileWidgetProvider);
    }
}
