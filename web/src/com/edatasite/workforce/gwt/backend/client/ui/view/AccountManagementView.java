package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Label;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: June 26, 2018
 * Time: 8:08:14 PM
 */
public class AccountManagementView extends KpiSideNavBox {
    interface AccountManagementViewUiBinder extends UiBinder<HTMLPanel, AccountManagementView> {
    }

    private static final AccountManagementViewUiBinder ourUiBinder = GWT.create(AccountManagementViewUiBinder.class);
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private final Command command;
    private final AccountManagementListItem item;

    @UiField
    HTMLPanel panel;
    @UiField
    Label accountNameLabel;
    @UiField
    HTML accountName;
    @UiField
    Label roleLabel;
    @UiField
    HTML role;
    @UiField
    Label registrationDateLabel;
    @UiField
    HTML registrationDate;
    @UiField
    Label phoneLabel;
    @UiField
    HTML phone;
    @UiField
    Label emailLabel;
    @UiField
    HTML email;
    @UiField
    Label loginLabel;
    @UiField
    HTML login;
    @UiField
    Label passwordLabel;
    @UiField
    HTML password;
    @UiField
    HTML note;
    @UiField
    HTMLPanel accountNamePanel;
    @UiField
    HTMLPanel rolePanel;
    @UiField
    HTMLPanel registrationDatePanel;
    @UiField
    HTMLPanel phonePanel;
    @UiField
    HTMLPanel emailPanel;
    @UiField
    HTMLPanel loginPanel;
    @UiField
    HTMLPanel passwordPanel;
    @UiField
    HTMLPanel changePasswordButtonPanel;
    @UiField
    HTMLPanel changeUserNameButtonPanel;
    @UiField
    HTMLPanel sendEmailButtonPanel;
    @UiField
    HTMLPanel killSessionButtonPanel;

    AccountManagementView(AccountManagementListItem item, Command command) {
        super(true);
        setStyleName(getElement(), "quick-add", true);

        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.basicInfo());
        addHeader(header);

        this.item = item;
        this.command = command;
        ourUiBinder.createAndBindUi(this);
        init();
    }

    private void init() {
        accountNameLabel.setText(wfmStrings.accountName());
        accountName.setHTML(item.getName());
        accountNamePanel.setVisible(!Utils.isNullOrEmpty(item.getName()));

        roleLabel.setText(wfmStrings.role());
        role.setHTML(item.getRoles());
        rolePanel.setVisible(!Utils.isNullOrEmpty(item.getRoles()));

        registrationDateLabel.setText(wfmStrings.registrationDate());
        registrationDate.setHTML(item.getSignUpDate());
        registrationDatePanel.setVisible(!Utils.isNullOrEmpty(item.getSignUpDate()));

        phoneLabel.setText(wfmStrings.phone());
        phone.setHTML(item.getPhone());
        phonePanel.setVisible(!Utils.isNullOrEmpty(item.getPhone()));

        emailLabel.setText(wfmStrings.email());
        email.setHTML(item.getEmail());
        emailPanel.setVisible(!Utils.isNullOrEmpty(item.getEmail()));

        loginLabel.setText(wfmStrings.login());
        login.setHTML(item.getLogin());
        loginPanel.setVisible(!Utils.isNullOrEmpty(item.getLogin()));

        passwordLabel.setText(wfmStrings.password());
        password.setHTML(item.getPassword());
        passwordPanel.setVisible(false);

        note.setHTML("<b>" + backendStrings.noteBeforeActivateingAccountCopyDetails() + "</b>");

        WfmButton2 changePassword = new WfmButton2(wfmStrings.changePassword(), WfmButton2.BTN_SECONDARY, clickEvent -> showChangeModal(true));
        changePassword.setWidth("100%");
        changePasswordButtonPanel.add(changePassword);

        WfmButton2 changeUserName = new WfmButton2(backendStrings.changeUserName(), WfmButton2.BTN_SECONDARY, clickEvent -> showChangeModal(false));
        changeUserName.setWidth("100%");
        changeUserNameButtonPanel.add(changeUserName);

        final boolean active = !(Constants.EMPLOYEE_STATUS_INACTIVE.equals(item.getEmployeeStatus()) || Constants.EMPLOYEE_STATUS_PENDING.equals(item.getEmployeeStatus()));

        if (!active) {
            WfmButton2 sendEmail = new WfmButton2(wfmStrings.sendEmail(), WfmButton2.BTN_SUCCESS, clickEvent -> {
                remove();
                //new ComposeView(item.getEmail());
                SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getEmail());
            });
            sendEmail.setWidth("100%");
            sendEmailButtonPanel.add(sendEmail);
        }

        WfmButton2 killSessions = new WfmButton2(backendStrings.killUserSessions(),WfmButton2.BTN_REJECT, clickEvent -> {
            LoadingPanel.loading(true, panel);
            BackendService.App.get().killUserSessions(item.getCompanyID(), item.getUserId(), new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false, panel);
                    Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                }

                public void success(Void result) {
                    LoadingPanel.loading(false, panel);
                    if (command != null) {
                        command.execute();
                    }
                    Info.show(backendStrings.userSessionsKilled(), Info.Type.INFO);
                }
            });
        });
        killSessions.setWidth("100%");
        killSessionButtonPanel.add(killSessions);
        setWidth(600);
        addBody(panel);

        addFooter(new WfmButton2(active ? backendStrings.inactivateAccount() : backendStrings.activateAccount(), WfmButton2.BTN_PRIMARY + " btn-block", clickEvent -> {
            LoadingPanel.loading(true, panel);
            BackendService.App.get().changeAccountStatus(item.getCompanyID(), item.getUserId(), active, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false, panel);
                    Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                }

                public void success(Void result) {
                    LoadingPanel.loading(false, panel);
                    remove();
                    if (command != null) {
                        command.execute();
                    }
                    Info.show(backendStrings.accountStatusChanged(), Info.Type.INFO);
                }
            });
        }));

        show();
    }

    private void showChangeModal(boolean isPassword) {
        KpiModal modal = new KpiModal();
        modal.setWidth(350);
        modal.setCloseButton(true);
        modal.setTitle(isPassword ? wfmStrings.changePassword() : backendStrings.changeUserName());

        TextBox textBox = new TextBox();
        modal.add(textBox);
        textBox.addBlurHandler(event12 -> {
            textBox.removeStyleName("x-form-invalid");
            textBox.setTitle("");
        });

        modal.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (!Validation.validateTextBoxRequired(textBox)) {
                Info.warn(wfmStrings.sureEnteredAllData());
                return;
            }
            if (isPassword) {
                LoadingPanel.loading(true);
                BackendService.App.get().changeAccountPassword(item, textBox.getText(), new AbstractAsyncCallback<Void>() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        modal.close();
                    }

                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        modal.close();
                        if (command != null) {
                            command.execute();
                        }
                    }
                });
            } else {
                LoadingPanel.loading(true);
                BackendService.App.get().changeAccountUserName(item.getUserId(), item.getCompanyID(), item.getUserCompanyId(), item.getLogin(), textBox.getText(), new AbstractAsyncCallback<String>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        modal.close();
                    }

                    @Override
                    public void success(String result) {
                        LoadingPanel.loading(false);
                        if (result == null || result.trim().length() == 0) {
                            modal.close();
                            if (command != null) {
                                command.execute();
                            }
                        } else {
                            textBox.setTitle(result);
                            textBox.addStyleName("x-form-invalid");
                        }
                    }
                });
            }
        }));
        modal.open();
    }
}
