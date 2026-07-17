package com.edatasite.workforce.gwt.documents.client.footerFileUpload;

import com.edatasite.workforce.gwt.documents.client.gwtupload.FileWidget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;

public class InDatabaseFileWidgetProvider {
    private ArrayList<FileWidget> fileItems = new ArrayList<>();
    private Div filesPanel;

    public ArrayList<FileWidget> getFileItems() {
        return fileItems;
    }

    public void setFileItems(ArrayList<FileWidget> fileItems) {
        if (fileItems == null) {
            this.fileItems.clear();
            return;
        }
        removeWidgetsFromPanel();
        this.fileItems = fileItems;
        attachFileItems();
    }

    private void removeWidgetsFromPanel() {
        for (FileWidget fileWidget : this.fileItems) {
            filesPanel.remove(fileWidget);
        }
    }

    public void attachFileItems() {
        if (filesPanel == null) {
            return;
        }
        for (FileWidget fileItem : fileItems) {
            filesPanel.add(fileItem);
        }
    }

    public void setFilesPanel(Div filesPanel) {
        this.filesPanel = filesPanel;
        attachFileItems();
    }

    public void clear(){
        if (filesPanel != null) {
            filesPanel.clear();
        }
        fileItems.clear();
    }
}
