package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation;

import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.ATTACHMENT_PARAM_BASE;

public class UploadWidget extends Composite {

    interface FileUploadViewUiBinder extends UiBinder<Widget, UploadWidget> {}
    private static final FileUploadViewUiBinder uiBinder = GWT.create(FileUploadViewUiBinder.class);

    @UiField
    FileUpload fileUpload;

    @UiField
    FlexTable files;

    @UiField
    WfmFormPanel form;


    public UploadWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        initwidget1();
    }

    public void initwidget1() {
        fileUpload.setName(ATTACHMENT_PARAM_BASE + 0);
//        fileUpload.addChangeHandler(changeEvent -> {
//            $(form.getElement()).trigger("submit", null);
//        });

//        form.setParameter(SESSION_ID_PARAM_NAME, Cookies.getCookie(SESSION_ID_COOKIE));
//        form.setParameter(CommandConstants.DESCRIPTION_PARAM_NAME, "");
//        form.setParameter(CommandConstants.UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());
//        fileUpload.setWidth("350px");
//        fileUpload.setName(ATTACHMENT_PARAM_BASE + 0);
//        fileUpload.addChangeHandler(changeEvent -> {
//            $(form.getElement()).trigger("submit", null);
//        });
    }

    public FileUpload getFileInput() {
        return fileUpload;
    }

    public FlexTable getFiles() {
        return files;
    }

    public void setFiles(FlexTable files) {
        this.files = files;
    }

    public WfmFormPanel getForm() {
        return form;
    }
}
