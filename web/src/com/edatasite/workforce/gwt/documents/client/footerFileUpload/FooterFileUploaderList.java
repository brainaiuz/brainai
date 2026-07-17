package com.edatasite.workforce.gwt.documents.client.footerFileUpload;

import com.edatasite.workforce.gwt.documents.client.gwtupload.FileUploaderList;
import com.edatasite.workforce.gwt.documents.client.gwtupload.Options;
import gwt.material.design.client.ui.html.Div;

public class FooterFileUploaderList extends FileUploaderList {
    private InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider;

    public FooterFileUploaderList(Options options, Div files, InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider) {
        super(options, files);
        this.inDatabaseFileWidgetProvider = inDatabaseFileWidgetProvider;
        this.inDatabaseFileWidgetProvider.setFilesPanel(files);
    }

    @Override
    protected void clearFiles() {
        super.clearFiles();
        inDatabaseFileWidgetProvider.attachFileItems();
    }


}
