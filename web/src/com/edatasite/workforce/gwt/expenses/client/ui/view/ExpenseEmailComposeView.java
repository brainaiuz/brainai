package com.edatasite.workforce.gwt.expenses.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseEmailTemplateData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 2/23/2019.
 */
public class ExpenseEmailComposeView extends FooteredCustomForm implements Constants, Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private KpiEditor editor;
    private DataListBox emailTemplateListBox;
    private Label fromEmail, toEmail;
    private TextBox subjectTextBox;
    private String templateType;
    private WfmButton2 sendButton, closeButton;

    public static ExpenseReportsListItem report;
    private Integer expenseReportID;
    private String employeeName;

    public ExpenseEmailComposeView(Integer expenseReportID, String employeeName) {
        super("expenseemailcomposeadd", wfmStrings.composeMail());
        this.expenseReportID = expenseReportID;
        this.employeeName = employeeName;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        emailTemplateListBox = new DataListBox();
        emailTemplateListBox.setNullLabel(wfmStrings.emailTemplate());
        fromEmail = new Label();
        toEmail = new Label();
        subjectTextBox = new TextBox();
        editor = new KpiEditor(true);

        emailTemplateListBox.addValueChangeHandler(event -> generateMessageText());

        FormGroup fromField = new FormGroup(wfmStrings.from(), fromEmail);
        FormGroup toField = new FormGroup(wfmStrings.to(), toEmail);
        FormGroup subjectField = new FormGroup(wfmStrings.subject(), subjectTextBox);
        FormGroup templateField = new FormGroup("", emailTemplateListBox);

        addField(MESSAGE_CENTER.FROM, fromField);
        addField(MESSAGE_CENTER.TO, toField);
        addField(MESSAGE_CENTER.SUBJECT, subjectField);
        addField(MESSAGE_CENTER.TEMPLATE, templateField);
        addField(MESSAGE_CENTER.EDITOR, editor);

        RootPanel.get().addStyleName("fitted-content");
        show();
    }

    private void generateMessageText() {
        if (emailTemplateListBox.isSomethingSelected()) {
            EntityToEmailTemplate item = new EntityToEmailTemplate();
            item.setEntityId(expenseReportID);
            item.setEntityType(templateType);
            item.setEmailTemplateId(emailTemplateListBox.getSelectedItem().getId());

            EmailTemplateService.App.get().generateExpenseClaimTemplateItem(item, new AbstractAsyncCallback<EmailTemplateItem>() {
                public void failure(Throwable caught) {
                }

                public void success(EmailTemplateItem result) {
                    if (result != null && result.getMessageHTML() != null) {
                        editor.setData(result.getMessageHTML());
                        fromEmail.setText(result.getFromEmail());
                        toEmail.setText(result.getToEmail());
                        subjectTextBox.setText(result.getSubject());
                    }
                }
            });
        }
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        ArrayList<Widget> result = new ArrayList<>();

        sendButton = new WfmButton2(wfmStrings.send(), BTN_PRIMARY);
        closeButton = new WfmButton2(wfmStrings.close(), BTN_DEFAULT_OUTLINE);

        sendButton.addClickHandler(clickEvent -> sendMessage());
        closeButton.addClickHandler(clickEvent -> closeTab());

        Div closeWrapper = new Div();
        closeWrapper.add(closeButton);

        Div sendWrapper = new Div();
        sendWrapper.add(sendButton);

        result.add(closeWrapper);
        result.add(sendWrapper);

        return result;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void getDataToFillFields() {
        ExpenseService.App.get().getEmailTemplateData(expenseReportID, new AbstractAsyncCallback<ExpenseEmailTemplateData>() {
            public void success(ExpenseEmailTemplateData result) {
                templateType = result.getMessageType();
                if (result.getTemplateList() != null && result.getTemplateList().length > 0) {
                    emailTemplateListBox.setItems(result.getTemplateList());
                    if (result.getTemplateList().length == 1) {
                        emailTemplateListBox.setSelected(result.getTemplateList()[0]);
                        generateMessageText();
                    } else {
                        for (SelectItem item : result.getTemplateList()) {
                            if (item.isSelected()) {
                                emailTemplateListBox.setSelected(item);
                                generateMessageText();
                            }
                        }
                    }
                } else {
                    emailTemplateListBox.setWithoutNullLabel(wfmStrings.emailTemplate());
                }
            }
        });
    }

    public void sendMessage() {
        LoadingPanel.loading(true);
        sendButton.setEnabled(false);
        ExpenseService.App.get().sendEmail(expenseReportID, editor.getData(), emailTemplateListBox.getSelectedId(), new AbstractAsyncCallback() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                sendButton.setEnabled(true);
            }

            public void success(Object result) {
                LoadingPanel.loading(false);
                Info.show(accountingStrings.sucSubmittedSavedExpenseReport(), Info.Type.INFO);
                sendButton.setEnabled(true);
                SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + expenseReportID + "/" + EXPENSE_VIEW);
                if (!Utils.isNullOrEmpty(employeeName) && !"null".equals(employeeName)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STEP_EXPENSEREPORT_SAVED, report, ExpenseEmailComposeView.this);
                }
                closeTab();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EXPENSE_EMAIL_COMPOSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
