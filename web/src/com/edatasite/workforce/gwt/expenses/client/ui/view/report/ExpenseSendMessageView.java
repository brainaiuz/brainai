package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseEmailTemplateData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/21/12
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseSendMessageView {
    private static final WfmStrings strings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer expenseReportID;
    private final Command viewCloseListener;

    private KpiEditor invoiceMessage;
    private DataListBox emailTemplateBox;
    private Label fromEmail, toEmail;
    private TextBox subjectBox;
    private String templateType;

    public ExpenseSendMessageView(Integer expenseReportID, Command viewCloseListener) {
        this.expenseReportID = expenseReportID;
        this.viewCloseListener = viewCloseListener;
        initialize();
    }

    private void initialize() {
        emailTemplateBox = new DataListBox();
        fromEmail = new Label();
        toEmail = new Label();
        subjectBox = new TextBox();

        fromEmail.setStyleName("totalBold");
        toEmail.setStyleName("totalBold");

        emailTemplateBox.addValueChangeHandler(event -> {
            if (emailTemplateBox.getSelectedItem() != null) {
                generateMessageText();
            }
        });

        initMessageDialogBox();

        ExpenseService.App.get().getEmailTemplateData(expenseReportID, new AbstractAsyncCallback<ExpenseEmailTemplateData>() {
            public void success(ExpenseEmailTemplateData result) {
                templateType = result.getMessageType();
                if (result.getTemplateList() != null && result.getTemplateList().length > 0) {
                    emailTemplateBox.setItems(result.getTemplateList());
                    emailTemplateBox.setSelected(result.getTemplateList()[0].getId());
                    generateMessageText();
                }
            }
        });
    }

    private void initMessageDialogBox() {
        final KpiModal messageDialogBox = new KpiModal();
        messageDialogBox.setTitle(accountingStrings.submitExpense());
        messageDialogBox.setWidth(600);

        Label infoForSender = new Label(accountingStrings.messYourManager());
        infoForSender.setStyleName("totalBold");

        FlexTable fromToPanel = new FlexTable();
        fromToPanel.getElement().getStyle().setProperty("borderCollapse", "separate");
        fromToPanel.getElement().getStyle().setProperty("borderSpacing", "0 10px");

        int row = 0;

        fromToPanel.setWidget(row, 0, new HTML("<span>Choose Template: </span>"));
        fromToPanel.setWidget(row++, 1, emailTemplateBox);

        fromToPanel.setWidget(row, 0, new HTML("<span>" + strings.from() + ": </span>"));
        fromToPanel.setWidget(row++, 1, fromEmail);

        fromToPanel.setWidget(row, 0, new HTML("<span>" + strings.to() + ": </span>"));
        fromToPanel.setWidget(row++, 1, toEmail);

        fromToPanel.setWidget(row, 0, new HTML("<span>Subject: </span>"));
        fromToPanel.setWidget(row, 1, subjectBox);

        fromToPanel.setHeight("50px");
        fromToPanel.setCellSpacing(10);

        WfmButton2 messageSendButton = new WfmButton2(wfmStrings.send(), WfmButton2.BTN_PRIMARY);
        WfmButton2 closeButton = new WfmButton2("Close", WfmButton2.BTN_DEFAULT);
        messageSendButton.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            ExpenseService.App.get().sendEmail(expenseReportID, invoiceMessage.getData(), emailTemplateBox.getSelectedId(), new AbstractAsyncCallback() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(strings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(Object result) {
                    LoadingPanel.loading(false);
                    Info.show(accountingStrings.sucSubmittedSavedExpenseReport(), Info.Type.INFO);
                    viewCloseListener.execute();
                    SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + expenseReportID + "/" + Constants.EXPENSE_VIEW);
                    messageDialogBox.close();
                }
            });
        });
        closeButton.addClickHandler(clickEvent -> messageDialogBox.close());

        VerticalPanel messageForm = new VerticalPanel();
        messageForm.setHeight("260px");
        messageForm.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        messageForm.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

        invoiceMessage = new KpiEditor(true);
        invoiceMessage.setWidth("545px");

        messageForm.add(infoForSender);
        messageForm.add(fromToPanel);
        messageForm.add(invoiceMessage);
        messageForm.setCellHorizontalAlignment(fromToPanel, HasHorizontalAlignment.ALIGN_LEFT);

        messageDialogBox.add(messageForm);
        messageDialogBox.addButton(closeButton);
        messageDialogBox.addButton(messageSendButton);
        messageDialogBox.open();
    }

    private void generateMessageText() {
        if (emailTemplateBox.getSelectedItem() != null) {
            EntityToEmailTemplate item = new EntityToEmailTemplate();
            item.setEntityId(expenseReportID);
            item.setEntityType(templateType);
            item.setEmailTemplateId(emailTemplateBox.getSelectedItem().getId());

            EmailTemplateService.App.get().generateExpenseClaimTemplateItem(item, new AbstractAsyncCallback<EmailTemplateItem>() {
                public void failure(Throwable caught) {
                }

                public void success(EmailTemplateItem result) {
                    if (result != null && result.getMessageHTML() != null) {
                        invoiceMessage.setData(result.getMessageHTML());
                        fromEmail.setText(result.getFromEmail());
                        toEmail.setText(result.getToEmail());
                        subjectBox.setText(result.getSubject());
                    }
                }
            });
        }
    }
}
