package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.ATTACHMENT_PARAM_BASE;
import static gwt.material.design.client.js.JsMaterialElement.$;

public class MaterialFileUpload extends Composite{

    private Div uploadedFileDiv, uploadDiv;
    private MaterialFile uploadedFile;
    private Italic uploadButton;
    private FileUpload fileUpload;

    public MaterialFileUpload() {
        initWidget();
    }

    private void initWidget() {
        uploadedFileDiv = new Div();
        uploadedFile = new MaterialFile(null);
        uploadedFile.addCloseHandler((event)-> reset());

        Div buttonDiv = new Div("btn-upload__icon");
        uploadButton = new Italic();
        uploadButton.setClass("ficon--attachment");
        buttonDiv.add(uploadButton);
        buttonDiv.addClickHandler(c -> $(fileUpload.getElement()).trigger("click", null));

        fileUpload = new FileUpload();
        fileUpload.setName(ATTACHMENT_PARAM_BASE+0);
        fileUpload.addChangeHandler(e -> {
            FileItem item = new FileItem();
            item.setFileName(fileUpload.getFilename());
            uploadedFile.setFileItem(item);
            uploadedFile.setDone(true);

            uploadedFileDiv.clear();
            uploadedFileDiv.add(uploadedFile);
//            fileUpload.getElement().setPropertyString("value", "");
            uploadDiv.setVisible(false);
        });
        fileUpload.setVisible(false);

        uploadDiv = new Div("btn-upload");
        uploadDiv.add(buttonDiv);
        uploadDiv.add(fileUpload);

        Div div = new Div("form-upload");
        div.add(uploadedFileDiv);
        div.add(uploadDiv);
        initWidget(div);
    }

    public void reset() {
        uploadedFile.setFileItem(null);
        uploadedFile.removeFromParent();
        uploadDiv.setVisible(true);
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public String getFilename() {
        return uploadedFile.getFileItem() != null ? uploadedFile.getFileItem().getFileName() : null;
    }
}
