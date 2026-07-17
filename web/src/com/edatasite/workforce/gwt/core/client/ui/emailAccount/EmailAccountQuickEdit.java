package com.edatasite.workforce.gwt.core.client.ui.emailAccount;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailAccountShareModal;
import com.edatasite.workforce.gwt.messagecenter.client.view.ManageFoldersModal;
import com.edatasite.workforce.gwt.messagecenter.client.view.MessageCenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;

public class EmailAccountQuickEdit extends EmailAccountQuickAdd {
    interface EmailAccountQuickEditUiBinder extends UiBinder<HTMLPanel, EmailAccountQuickEdit> {
    }

    private static EmailAccountQuickEditUiBinder ourUiBinder = GWT.create(EmailAccountQuickEditUiBinder.class);

    @UiField
    KpiSwitcher defaultBox;
    @UiField
    KpiSwitcher corporate;
    @UiField
    KpiSwitcher active;

    private Integer objectID;

    public EmailAccountQuickEdit(Integer objectID) {
        super();
        this.objectID = objectID;

        getQuickData(objectID);
    }

    protected void bind() {
        ourUiBinder.createAndBindUi(this);
    }

    protected void initialize() {
        super.initialize();
        //header
        header.setText(wfmStrings.editEmailAccount());

        email.setEnabled(false);
        defaultBox.setOnLabel(wfmStrings.defaultEmailAccount());
        corporate.setOnLabel(wfmStrings.corporateEmail());
        active.setOnLabel(wfmStrings.active());
    }

    protected void initEvents() {}

    protected void initButtons() {
        super.initButtons();
        //Share
        WfmButton2 shareButton = new WfmButton2(wfmStrings.share(), WfmButton2.BTN_DEFAULT);
        shareButton.addClickHandler(clickEvent -> new EmailAccountShareModal(objectID));
        addFooter(shareButton);
        //Manage Folders
        WfmButton2 manageFoldersButton = new WfmButton2(wfmStrings.manageFolders(), WfmButton2.BTN_DEFAULT);
        manageFoldersButton.addClickHandler(clickEvent -> new ManageFoldersModal(objectID));
        addFooter(manageFoldersButton);
        //Delete
        WfmButton2 deleteButton = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_RESET);
        deleteButton.addClickHandler(clickEvent -> deleteAccount());
        addFooter(deleteButton);
    }

    public void getQuickData(Integer objectID) {
        LoadingPanel.loading(true, panel);
        service.getEmailAccount(objectID, new AbstractAsyncCallback<EmailAccountItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(EmailAccountItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                fillFieldsWithData();
            }
        });
    }

    private void deleteAccount() {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        message.setTitle(wfmStrings.warning());
        message.setMessage(wfmMessages.sureYouWantToDelete(MessageCenter.emailAccount.getEmail(), wfmStrings.account()));
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                service.deleteEmailAccount(objectID, new AsyncCallback<Boolean>() {
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    public void onSuccess(Boolean result) {
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.account()), Info.Type.INFO);
                        Utils.openURLCurrentTab(Utils.getHostURL() + (result ? "MessageCenter.html" : "Crm.html"));
                    }
                });
            }
        });
        message.open();
    }

    protected void fillFieldsWithData() {
        super.fillFieldsWithData();
        if (Constants.GOOGLE.equals(providerType)) {
            gmailPanel.setVisible(true);
        } else if (Constants.OFFICE_365.equals(providerType)) {
            outlookPanel.setVisible(true);
        } else {
            defaultDetailPanel.setVisible(true);
        }
        corporate.setValue(item.isCompanyEmail());
        defaultBox.setValue(item.isDefaultEmail());
        active.setValue(item.isActive());
    }

    protected void setValues() {
        super.setValues();
        item.setObjectID(objectID);
        item.setCompanyEmail(corporate.getValue());
        item.setDefaultEmail(defaultBox.getValue());
        item.setActive(active.getValue());
    }
}
