package com.edatasite.workforce.gwt.core.client.ui.components.fileUpload.image;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;

/**
 * Created by Anvar Akramov on 12/14/17.
 */
public class KpiImageFile extends MaterialPanel {

    private FileResource productPicture;
    Italic iconClose = new Italic("");
    Div progressIndicator = new Div("btn-upload__indicator active");
    private Image image;
    private boolean done = false;

    public KpiImageFile(boolean dynamicWidth) {
        this(null, dynamicWidth);
    }

    public KpiImageFile(FileResource fileResource, boolean dynamicWidth) {
        super();
        setClass("btn-uploaded-img btn-uploaded--has-control");
        Div uControl = new Div("btn-uploaded__control");
        Div radioDiv = new Div("control control--radio");
        image = new Image();
        if (!dynamicWidth) {
            image.setWidth("200px");
        }
        image.setHeight("220px");
        uControl.add(radioDiv);
        add(progressIndicator);
        if (fileResource != null) {
            setFile(fileResource);
        } else {
            progressIndicator.setVisible(true);
        }
        iconClose.setClass("close");
        add(uControl);
        add(iconClose);
    }

    public FileResource getFile() {
        return productPicture;
    }

    public void setFile(FileResource file) {
        this.productPicture = file;
        image.setUrl(file.getUrlFromSolr());
        add(image);
        progressIndicator.removeFromParent();
    }

    public void addCloseHandler(ClickHandler clickHandler) {
        iconClose.addClickHandler(clickHandler);
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
}
