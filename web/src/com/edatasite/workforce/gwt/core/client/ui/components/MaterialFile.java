package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.google.gwt.event.dom.client.ClickHandler;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;

/**
 * Created by Anvar Akramov on 12/14/17.
 */
public class MaterialFile extends MaterialPanel {

    private FileItem fileItem;
    Italic icon = new Italic("");
    Italic iconClose = new Italic("");
    Div progressIndicator = new Div("btn-upload__indicator active");
    private boolean done = false;

    public MaterialFile(FileItem fileItem/*, ClickHandler onCloseEvent*/) {
        super();
        setClass("btn-uploaded");
        //if fileItem not null then show icon
        if(fileItem!=null) {
            setFileItem(fileItem);
        } else {
            //else show display progress
            progressIndicator.setVisible(true);
        }
        iconClose.setClass("close");

        add(icon);
        add(iconClose);
        add(progressIndicator);
    }


    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
        if(fileItem!=null && fileItem.getFileName()!=null && fileItem.getFileName().indexOf(".")>-1) {
            String fileExtension = fileItem.getFileName().substring(fileItem.getFileName().lastIndexOf(".")+1);
            String iconClass = MaterialFileUtils.getFileIconByExtension(fileExtension);
            if(iconClass!=null) {
                icon.setClass(iconClass);
            } else {
                icon.setClass("ficon--attachment");
            }

            progressIndicator.removeFromParent();
        } else {
            icon.setClass("ficon--attachment");
        }
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
