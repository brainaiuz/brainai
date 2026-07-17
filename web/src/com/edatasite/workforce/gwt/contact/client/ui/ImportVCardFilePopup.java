package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by Sherali on 3/4/2016.
 * Project web
 */
public class ImportVCardFilePopup extends KpiModal implements CommandConstants {

    public ImportVCardFilePopup() {
        super();
        setTitle(wfmStrings.importingVCardFromAppsLikeAppleAddressBook());
        setWidth(350);
        build();
        open();
    }

    public void build() {
        HTML label = new HTML(wfmStrings.messSelectFile());
        final FileUpload upload = new FileUpload();
        upload.setName(ATTACHMENT_PARAM_BASE + 0);
        TextArea description = new TextArea();
        description.setName(DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");
        TextBox uploadType = new TextBox();
        uploadType.setName(UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        final WfmFormPanel form = new WfmFormPanel("/CreateAttachment");
        form.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                if (form.getErrorString() == null) {
                    if (form.getObjectID() != null) {
                        close();
                        importFile(form.getObjectID());
                    }
                    Info.show(wfmMessages.importingMessage(Property.getPluralWithObjectCode(Constants.Contacts, wfmStrings.contacts())), Info.Type.INFO);
                } else {
                    Info.show(wfmStrings.messParseErrorCompareFile(), Info.Type.WARNING);
                }
            }
        });

        HorizontalPanel hp = new HorizontalPanel();
        hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hp.add(upload);
        hp.add(description);
        hp.add(uploadType);

        final VerticalPanel vp = new VerticalPanel();
        vp.add(label);
        vp.add(hp);
        vp.add(new Label(wfmStrings.pleaseProvideVcfFileOnly()));

        form.setWidget(vp);
        add(vp);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        addButton(new WfmButton2(wfmStrings.importString(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (!Utils.isNullOrEmpty(upload.getFilename())) {
                if (".vcf".equals(upload.getFilename().substring(upload.getFilename().lastIndexOf(".")))) {
                    form.submit();
                } else {
                    Info.show("Please provide .vcf file only", Info.Type.WARNING);
                }
            }
        }));
    }

    private void importFile(Integer objectID) {
        ImportFile importFile = new ImportFile();
        importFile.setFileID(objectID);
        importFile.setType(ImportTypeEnum.VCARD_CONTACT);
        importFile.setDefaultSeparator(',');
        LoadingPanel.loading(true);
        ImportFileService.App.get().addImportToQueue(importFile, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                showFailureMessage(wfmStrings.errorOccurredUpdate());
            }

            @Override
            public void success(String result) {
                LoadingPanel.loading(false);
                if (!Utils.isNullOrEmpty(result)) {
                    String errorMessage = result + " " + wfmStrings.importIsAlreadyInProgress();
                    showFailureMessage(errorMessage);
                }
            }
        });
    }

    public void showFailureMessage(final String errorMessage) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(errorMessage);
        messageBox.open();
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
            }
        });
    }
}