package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * User: Abror Abdukadirov
 * Date: 04.05.2017 14:52
 */
public class SendSmsToClientView extends KpiModal implements Constants, CommandConstants {
    private final CRMServiceAsync crmService = CRMService.App.get();
    private Button sendButton;
    private Button closeButton;
    private TextArea2 messageArea;
    private CrmAccountItem crmAccount;
    private String clientPhone;
    private TextBox sentToNumber;
    private DataListBox smsProvider;
    private DataListBox templateItems;
    private WfmMessageBox messageBox;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();

    public SendSmsToClientView() {
        super();
        this.setTitle(WfmStrings.App.get().sendSms());
    }

    public SendSmsToClientView(String clientPhone, CrmAccountItem crmAccount) {
        this();
        this.clientPhone = new PhoneNumber(clientPhone).toString();
        this.crmAccount = crmAccount;
        try {
            if (Utils.isNullOrEmpty(clientPhone) || !clientPhone.matches(Constants.REGEX_PHONE) || crmAccount.getPrimaryContact() == null) {
                messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.incorrectlyPHoneFormat());
                messageBox.open();
                close();
                return;
            }
        } catch (Exception exp) {
            exp.getMessage();
            messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.incorrectlyPHoneFormat());
            messageBox.open();
            close();
            return;
        }
        getData();
    }

    public boolean validate() {
        int errors = 0;
        smsProvider.removeStyleName(ERROR_FORM_STYLE);
        messageArea.removeStyleName(ERROR_FORM_STYLE);
        sentToNumber.removeStyleName(ERROR_FORM_STYLE);
        if (Utils.isNullOrEmpty(messageArea.getText())) {
            messageArea.setStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (smsProvider.getSelectedItem() == null) {
            smsProvider.setStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (Utils.isNullOrEmpty(sentToNumber.getText())) {
            sentToNumber.setStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private String normalize(String numb) {
        numb = numb.trim().replace("+", "").replace("-", "").replace(",", "").replace("(", "").replace(")", "").replace(".", "").replace(" ", "").replace("" + (char) 160, "");
        return numb;
    }

    private void send() {
        if (!validate()) {
            return;
        }
        SmsSendItem smsSendItem = new SmsSendItem();
        smsSendItem.setToNumber(normalize(sentToNumber.getText()));
        smsSendItem.setMessageText(messageArea.getText());
        smsSendItem.setSettingID(smsProvider.getSelectedId());
        if (crmAccount != null && crmAccount.getPrimaryContact() != null) {
            smsSendItem.setEntityID(crmAccount.getPrimaryContact().getObjectId());
        }
        LoadingPanel.loading(true);
        enableButtons(false);
        crmService.smsSendTo(smsSendItem, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButtons(true);
            }

            @Override
            public void success(Boolean s) {
                LoadingPanel.loading(false);
                enableButtons(true);
                if (s != null && s) {
                    Info.show(wfmStrings.messageHasBeenSent(), Info.Type.INFO);
                    close();
                } else {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }
        });
    }

    private void drawShell() {
        messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK);
        messageArea = new TextArea2(160);
        messageArea.setSize("530px", "150px");

        smsProvider = new DataListBox();
        smsProvider.addStyleName(DEFAULT_WIDTH);

        templateItems = new DataListBox();
        templateItems.setWidth("200px");
        templateItems.addValueChangeHandler(event -> {
                if (templateItems.getSelectedItem() == null) {
                    messageArea.setText("");
                    return;
                }
                crmService.generateCrmAccountSMSTemplate(templateItems.getSelectedId(), crmAccount, new AbstractAsyncCallback<String>() {
                    @Override
                    public void failure(Throwable throwable) {
                        messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, wfmStrings.error());
                        messageBox.open();
                    }

                    @Override
                    public void success(String content) {
                        messageArea.setText(content != null ? content : "");
                    }
                });
        });
        sentToNumber = new TextBox();
        sentToNumber.setWidth("200px");
        sentToNumber.setText(clientPhone);
        sentToNumber.getElement().getStyle().setBackgroundColor("White");

        FlexTable table = new FlexTable();
        table.setWidget(0, 0, new HTML("<b class='customTitle'>" + wfmStrings.chooseSmsProvider() + ": </b>"));
        table.setWidget(0, 1, smsProvider);
        table.setWidget(1, 0, new HTML("<br/>"));
        table.setWidget(2, 0, new HTML("<b class='customTitle'>" + wfmStrings.chooseTemplate() + ": </b>"));
        table.setWidget(2, 1, templateItems);
        table.setWidget(2, 2, getLink());
        table.getWidget(2, 2).getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        table.setWidget(3, 0, new HTML("<br/>"));
        table.setHTML(4, 0, "<b class='customTitle'>" + wfmStrings.sendTo() + ": </b>");
        table.setWidget(4, 1, sentToNumber);
        table.setWidget(4, 2, new HTML("<i>  (+) XX XXX XXX-XXXX</i>"));
        table.getWidget(0, 0).setWidth("150px");
        table.getWidget(0, 1).setWidth("250px");
        table.getWidget(2, 0).setWidth("150px");
        table.getWidget(2, 1).setWidth("200px");
        table.getWidget(2, 2).setWidth("50px");

        VerticalPanel messageForm = new VerticalPanel();
        messageForm.setSpacing(2);
        messageForm.setHeight("300px");
        messageForm.setWidth("100%");
        messageForm.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        messageForm.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

        messageForm.add(table);
        messageForm.add(messageArea);
        sendButton = new Button(wfmStrings.send());
        sendButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                send();
            }
        });
        closeButton = new Button(wfmStrings.close());
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                close();
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(7);
        buttonPanel.setStyleName("workforce");
        buttonPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        buttonPanel.add(sendButton);
        buttonPanel.add(closeButton);
        buttonPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        messageForm.add(buttonPanel);

        messageForm.setCellHorizontalAlignment(messageArea, HasHorizontalAlignment.ALIGN_CENTER);

        ScrollPanel generalPanel = new ScrollPanel();
        generalPanel.setHeight("100%");
        generalPanel.setWidth("100%");
        generalPanel.setWidget(messageForm);

        setSize("550px", "320px");
        setScrollable(true);
        add(generalPanel);
        this.open();
    }

    private void getData() {
        LoadingPanel.loading(true);
        String moduleType = crmAccount.hasCustomerType() ? SMS_TEMPLATE_CUSTOMER_BALANSE : SMS_TEMPLATE_SUPPLIER_BALANSE;
        crmService.getSMSItem(moduleType, new AbstractAsyncCallback<SmsSendItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SmsSendItem result) {
                LoadingPanel.loading(false);
                drawShell();
                smsProvider.setItems(result.getProviders());
                templateItems.setItems(result.getTemplates());
            }
        });
    }

    private Anchor getLink() {
        Anchor anchor = new Anchor("   " + wfmStrings.smsTemplates());
        anchor.setWordWrap(false);
        anchor.setStyleName("pointer");
        anchor.addStyleName("on-mouse-over");
        anchor.setTarget("_blank");
        anchor.setHref(GWT.getHostPageBaseURL() + UiSettings.getInstance().SETTINGS + "#emailSettingsHome|smsTemplateList/");
        return anchor;
    }

    private void enableButtons(boolean enable) {
        sendButton.setEnabled(enable);
        closeButton.setEnabled(enable);
    }
}
