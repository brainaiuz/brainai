package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.google.gwt.core.client.GWT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.JQuery;

public class FolderWrap extends Div {
    private FolderSelection selectionHandler;
    private FolderOpen folderOpen;
    private FolderWidget selectedFolder;

    public FolderWrap(FolderSelection selectionHandler, FolderOpen folderOpen) {
        super();
        this.selectionHandler = selectionHandler;
        JQuery.$(this.getElement()).on("folderSelection", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object folderWidget) {
                if (selectionHandler != null && folderWidget != null) {
                    selectedFolder.deselect();
                    selectionHandler.execute((FolderWidget) folderWidget);
                    selectedFolder = (FolderWidget) folderWidget;
                    selectedFolder.select();
                } else {
                    GWT.log("folder widget is null");
                }
                return null;
            }
        });
        JQuery.$(this.getElement()).on("folderOpen", new Functions.EventFunc1() {
            @Override
            public Object call(Event e, Object folderParent) {
                if (folderOpen != null) {
                    folderOpen.execute((FolderWidget) folderParent);
                }
                return null;
            }
        });
    }

    public void add(FolderWidget folderWidget) {
        if (selectedFolder == null) {
            selectedFolder = folderWidget;
        }
        super.add(folderWidget);
    }

    public void setSelectedItem(FolderResource folder) {
        //TODO
    }

    public FolderWidget getSelectedItem() {
        return selectedFolder;
    }

}
