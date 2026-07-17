package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * User: Ilhombek
 * Date: 31.08.2010
 * Time: 22:31:28
 */
public class ImportEMLTemplatePopup extends Composite implements CommandConstants {

    private String url;
    private DialogBox dialogBox;
    private Button importButton;
    private Button cancelButton;
    private Command submitCommand;
    private Integer uploadPanelId;
    private HTML errorMessage;
    private FileUpload fileUpload;

    public ImportEMLTemplatePopup(String url) {
        super();
        this.url = url;
        showPopup();
    }

    public DialogBox getDialogBox() {
        return dialogBox;
    }

    public void setSubmitCommand(Command submitCommand) {
        this.submitCommand = submitCommand;
    }

    public Integer getUploadPanelId() {
        return uploadPanelId;
    }

    public void showPopup() {
        dialogBox = new DialogBox();
        dialogBox.setAnimationEnabled(true);
        dialogBox.setGlassEnabled(true);
        dialogBox.setHTML("<b class=customTitle>Import Email Template</b>");
        errorMessage = new HTML();
        FlexTable table = new FlexTable();
        table.setStyleName("workforce");
        table.setCellSpacing(10);
        table.setSize("300px", "100px");
        table.setHTML(0, 0, "<b>Select the file to import</b>");
        table.getFlexCellFormatter().setColSpan(0, 0, 2);
        fileUpload = new FileUpload();
        fileUpload.setWidth("200px");
        fileUpload.setName(ATTACHMENT_PARAM_BASE + 0);

        TextArea description = new TextArea();
        description.setName(DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");

        TextBox textBox = new TextBox();
        textBox.setName(UPLOAD_TYPE_PARAM_NAME);
        textBox.setVisible(false);
        textBox.setText(Utils.getUploadTypeParam());

        final WfmFormPanel uploadPanel = new WfmFormPanel(url);
        final HTML messageHtml = new HTML();

        uploadPanel.addSubmitCompleteHandler(event -> {
            messageHtml.setHTML(uploadPanel.getErrorString() != null ?
                    "Unexpected error occured, please try again later" : "Your file has successfully been uploaded");
            if (uploadPanel.getErrorString() == null) {
                uploadPanelId = uploadPanel.getObjectID();

                if ((fileUpload.getFilename().toLowerCase().lastIndexOf(".zip") != -1) /*||
            (fileUpfileUploadload.getFilename().toLowerCase().lastIndexOf(".rar") != -1)*/) {
                    CommonService.App.get().findEMLFileInputZip(uploadPanelId, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            errorMessage.setHTML("<font style='font-weight:bold;color:red;'>Zip file does not contain .eml files</font>");
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            LoadingPanel.loading(false);
                            if (result) {
                                dialogBox.hide();
                                if (submitCommand != null) {
                                    submitCommand.execute();
                                }
                            } else {
                                errorMessage.setHTML("<font style='font-weight:bold;color:red;'>Zip file does not contain .eml files</font>");
                            }
                        }
                    });
                } else {
                    LoadingPanel.loading(false);
                    dialogBox.hide();
                    if (submitCommand != null) {
                        submitCommand.execute();
                    }
                }
            }
            Info.show(messageHtml.getHTML(), Info.Type.INFO);
        });

        HorizontalPanel horz = new HorizontalPanel();
        horz.add(fileUpload);
        horz.add(description);
        horz.add(textBox);
        uploadPanel.setWidget(horz);

        table.setWidget(1, 0, uploadPanel);
        table.getFlexCellFormatter().setColSpan(1, 0, 2);
        table.setWidget(2, 0, errorMessage);
        table.getFlexCellFormatter().setColSpan(2, 0, 2);

        table.setHTML(3, 0, /*"[Only *.eml files are supported.]"*/"[Only single *.eml or zip file containing multiple .eml files are supported.]");
        table.getFlexCellFormatter().setColSpan(3, 0, 2);

        importButton = new Button("Import", (ClickHandler) event -> {
            if (fileUpload.getFilename() != null && !"".equals(fileUpload.getFilename())) {
                if ((fileUpload.getFilename().toLowerCase().lastIndexOf(".eml") != -1) ||
                        (fileUpload.getFilename().toLowerCase().lastIndexOf(".zip") != -1)/* ||
                        (fileUpload.getFilename().toLowerCase().lastIndexOf(".rar") != -1)*/) {
                    errorMessage.setHTML("");
                    saveAttachment(uploadPanel);
                } else {                                                            //Please provide *.eml zip file only
                    errorMessage.setHTML("<font style='font-weight:bold;color:red;'>Please provide *.eml or zip file containing multiple .eml files</font>");
                }
            } else {                                                            //Please select *.eml file
                errorMessage.setHTML("<font style='font-weight:bold;color:red;'>Please select a file to import</font>");
            }

        });
        cancelButton = new Button("Cancel", (ClickHandler) event -> {
            errorMessage.setHTML("");
            dialogBox.hide();
        });
        table.setWidget(4, 0, importButton);
        table.getFlexCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        table.setWidget(4, 1, cancelButton);
        table.getFlexCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);
        dialogBox.setWidget(table);
        dialogBox.show();
    }

    private void saveAttachment(final WfmFormPanel uploadPanel) {
        uploadPanel.submit();
        LoadingPanel.loading(true);
    }
}
