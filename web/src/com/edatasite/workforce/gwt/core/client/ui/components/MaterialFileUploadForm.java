package com.edatasite.workforce.gwt.core.client.ui.components;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.ATTACHMENT_PARAM_BASE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.MULTIPLE_FILES;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.SESSION_ID_PARAM_NAME;
import static gwt.material.design.client.js.JsMaterialElement.$;

/**
 * Created by Anvar Akramov on 12/13/17.
 */
public class MaterialFileUploadForm extends Composite implements Constants {

    interface FileUploadFormUiBinder extends UiBinder<Widget, MaterialFileUploadForm> {
    }
    private static FileUploadFormUiBinder ourUiBinder = GWT.create(FileUploadFormUiBinder.class);

    @UiField
    Label attachmmentsUploadLabel;
    @UiField
    Div uploadedFilesContainer;
    @UiField
    Div attachButtonContainer;
    @UiField
    Italic uploadBtnIcon;
    @UiField
    FileUpload uploadFile;
    @UiField
    WfmFormPanel form;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    ArrayList<MaterialFile> uploadedFiles = new ArrayList<>();

    public MaterialFileUploadForm() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initForm();
    }

    public MaterialFileUploadForm(String action) {
        this();
        form.setActionHandler(action);
    }

    public MaterialFileUploadForm(ArrayList<MaterialFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
        initWidget(ourUiBinder.createAndBindUi(this));
        initForm();
        setHasFiles(uploadedFiles!=null && uploadedFiles.size()>0);
        for(MaterialFile materialFile : uploadedFiles) {
            //Display on UI
            uploadedFilesContainer.add(materialFile);
        }
    }

    private void initForm() {
        attachmmentsUploadLabel.setText(wfmStrings.attachmentsUpload());

        setHasFiles(false);

        attachButtonContainer.addClickHandler(clickEvent -> $(uploadFile.getElement()).trigger("click", null));

        form.setParameter(SESSION_ID_PARAM_NAME, Cookies.getCookie(SESSION_ID_COOKIE));
        form.setParameter(MULTIPLE_FILES, "true");
        uploadFile.setName(ATTACHMENT_PARAM_BASE+0);

        form.addSubmitCompleteHandler(event -> {

            Integer id = form.getObjectID();

            if (id != null || form.isSuccess()) {
                FileItem fileItem = new FileItem();
                fileItem.setId(id);
                fileItem.setFileName(uploadFile.getFilename());
                MaterialFile materialFile = uploadedFiles.get(uploadedFiles.size()-1);
                materialFile.setDone(true);
                materialFile.setFileItem(fileItem);

                uploadFile.getElement().setPropertyString("value", "");
            } else {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                messageBox.setTitle(wfmStrings.error());
                if (form.getErrorString() == null) {
                    messageBox.setMessage(wfmStrings.errorOnUploadingDocument());
                } else {
                    messageBox.setMessage(form.getErrorString());
                }
                messageBox.open();
            }
            //SHOW or HIDE divs
            setHasFiles(uploadedFiles.size()>0);
        });
        uploadFile.addChangeHandler(valueChangeEvent -> {
            MaterialFile materialFile = new MaterialFile(null);
            materialFile.addCloseHandler((event)->{

                if(uploadedFilesContainer!=null) {
                    uploadedFilesContainer.remove(materialFile);
                }
                if(uploadedFiles.contains(materialFile)) {
                    uploadedFiles.remove(materialFile);
                }
            });
            //Display on UI
            uploadedFilesContainer.add(materialFile);
            //Keep references
            uploadedFiles.add(materialFile);
            setHasFiles(true);

            $(form.getElement()).trigger("submit", null);
        });

    }

    private void setHasFiles(boolean hasFiles) {
        if(hasFiles) {
            uploadedFilesContainer.setVisible(true);
            attachButtonContainer.setClass("btn-upload btn-upload--plus");
            uploadBtnIcon.setClass("ficon--plus");
        } else {
            uploadedFilesContainer.setVisible(false);
            attachButtonContainer.setClass("btn-upload");
            uploadBtnIcon.setClass("ficon--attachment");
        }
    }

    public void setActionHandler(String action) {
        form.setActionHandler(action);
    }

    public void setParameter(String name, String value) {
        form.setParameter(name, value, false);
    }

    public ArrayList<MaterialFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public boolean isFinished() {
        for (MaterialFile uploadFile : uploadedFiles) {

            if (!uploadFile.isDone()) {
                return false;
            }
        }

        return true;
    }

    public void clearFiles() {
        for (MaterialFile uploadFile : uploadedFiles) {
            try {
                uploadedFilesContainer.remove(uploadFile);
            } catch (Exception e) {
                GWT.log("", e);
            }
        }

        uploadedFiles.clear();
    }
}
