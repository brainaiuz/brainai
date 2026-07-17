package com.edatasite.workforce.gwt.core.client.ui.components.fileUpload;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.event.dom.client.ClickHandler;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by Anvar Akramov on 12/14/17.
 */
public class KpiFile extends MaterialPanel {

    private FileResource fileResource;

    Div container;
    Span title = new Span();
    Span size = new Span();
    Italic close = new Italic();

    public KpiFile(FileResource fileResource) {
        super();
        setClass("attach-item");

        container = new Div("attach-item__text");
        add(container);

        if (fileResource != null) {
            setFile(fileResource);
        }
        title.setClass("btn-uploaded__title");
        title.getElement().getStyle().setProperty("cursor", "pointer");
        size.setClass("attach-item__size");
        close.setClass("close material-icons");
        close.getElement().getStyle().setProperty("cursor", "pointer");

        close.addClickHandler(event -> removeFromParent());

        container.add(title);
        container.add(size);
        container.add(close);
    }

    public FileResource getFile() {
        return fileResource;
    }

    public void setFile(FileResource fileResource) {
        this.fileResource = fileResource;
        title.setText(fileResource.getFileName());
        size.setText(fileResource.getFileSizeAsString());
    }

    public void deleteHandler(ClickHandler clickHandler) {
        close.addClickHandler(clickHandler);
    }
}
