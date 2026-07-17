package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

public class FolderWidget extends Div {
    private boolean selected;
    private FolderResource folder;
    private String folderType;
    private Div folders;
    private Widget selectIt;
    private String selectionClass;
    public FolderWidget(FolderResource folder, boolean hasChildren) {
        this(folder, null, hasChildren);
    }
    public FolderWidget(FolderResource folder, String folderType, boolean hasChildren) {
        super();
        this.folder = folder;
        this.folderType = folderType;
        if (hasChildren) {
            setInitialClasses("kpi-upload__folder");
            String id = HTMLPanel.createUniqueId();
            Div parent = new Div("kpi-upload__folder-parent kpi-upload__folder-parent--expanded kpi-upload__folder-depth-1 collapsed");
            this.selectIt = parent;
            this.selectionClass = "kpi-upload__folder-parent--selected";
            parent.setDataAttribute("toggle", "collapse");
            parent.setDataAttribute("target", "#" + id);
            Span parentName = new Span(folder.getName());
            folders = new Div("kpi-upload__folder-child collapse");
            folders.setId(id);
            parent.add(parentName);
            add(parent);
            add(folders);

            FolderWidget.this.addClickHandler((event) -> {
                JQuery.$(FolderWidget.this.getElement()).trigger("folderOpen", FolderWidget.this);
                JQuery.$(this.getElement()).trigger("folderSelection", FolderWidget.this);
            });


        } else {
            setInitialClasses("kpi-upload__upload-file");
            Div wrapper = new Div("kpi-upload__content-wrapper");
            Div status = new Div("kpi-upload__upload-file-status");
            Div row = new Div("kpi-upload__upload-file-row");
            this.selectionClass = "kpi-upload__upload-file--selected";
            this.selectIt = this;
            row.getElement().setInnerText(folder.getName());
            wrapper.add(status);
            wrapper.add(row);
            FolderWidget.this.addClickHandler((event) -> {
                event.stopPropagation();
                JQuery.$(this.getElement()).trigger("folderSelection", FolderWidget.this);
            });
            add(wrapper);
        }
    }

    public void select() {
        selectIt.addStyleName(selectionClass);
        selected = true;
    }

    public void deselect() {
        selectIt.removeStyleName(selectionClass);
        selected = false;
    }

    public FolderResource getFolder() {
        return folder;
    }

    public void setFolder(FolderResource folder) {
        this.folder = folder;
    }

    public String getFolderType() {
        return folderType;
    }


    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void addFolder(FolderWidget folderItem) {
        folders.add(folderItem);
    }

}
