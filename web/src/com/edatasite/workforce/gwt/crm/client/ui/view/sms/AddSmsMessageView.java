package com.edatasite.workforce.gwt.crm.client.ui.view.sms;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailServiceAsync;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddMessageView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Aziz
 * Date: 10/16/12
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddSmsMessageView extends AddMessageView implements Colapse {
    private TextArea2 message;
    private DataListBox sender;
    private InputGroup dateWidwet;

    private static final MassMailServiceAsync massMailService = MassMailService.App.get();
    private VerticalPanel personalAttrsPanel;

    public AddSmsMessageView(Integer objectID, Integer campaignID, String campaignName, boolean clone) {
        super("smsmessage", crmStrings.addSMSMessage());
        this.objectID = objectID;
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        this.clone = clone;
        this.isSMS = true;
    }

    @Override
    protected void initialize() {
        //sender
        sender = new DataListBox();
        sender.ensureDebugId("add_scheduled_messages-sender");
        sender.addStyleName(Constants.DEFAULT_WIDTH);
        //subscritption list
        mailListTable = new CheckboxMailingListDataGrid(objectID, true, null);
        mailListTable.ensureDebugId(debug_id + "mail_lists");
        mailListTable.setSize("250px", "200px");
//        campaignSource = new CRMLookUp(CrmConstants.CRM_CAMPAIGN_ID);
//        campaignSource.ensureDebugId(debug_id + "campaign");
//        campaignSource.addStyleName(DEFAULT_WIDTH);
//        if (campaignID != null && campaignName != null) {
//            campaignSource.setSelected(campaignID, campaignName);
//        }
        //Date
        date = new DatePicker();
        date.ensureDebugId(debug_id + "date");
        date.addStyleName(DEFAULT_WIDTH);
        date.setDate(new Date());
        //Time
        time = new KpiTimePicker(true);
        time.setWidth("100px");
        time.addStyleName("form-control");
        time.ensureDebugId(debug_id + "time");
        time.setValue(KpiTimePicker.getHoursAndMinutes(new Date()));
        //message
        message = new TextArea2(true, 3000);
        message.setHeight(250 + "px");
        message.ensureDebugId("add_scheduled_message-message");
        //term_of_use
        notSpammer = new KpiCheckBox(wfmStrings.iHaveRead() + " " + "<a href='" + antiSpamURL + "' target=\"_blank\"> " + wfmStrings.termsOfService() + "</a>," + wfmStrings.andAgreeNotToSpam(), true);
        notSpammer.ensureDebugId(debug_id + "not_spammer");
        if (!clone && objectID != null) {
            notSpammer.setValue(true);
        }
        //attributes
        personalAttrsPanel = new VerticalPanel();
        personalAttrsPanel.setSpacing(3);
        personalAttrsPanel.ensureDebugId(debug_id + "personal_attributes");
    }

    @Override
    protected void drawForm() {
        GRow gRow = new GRow();//Grow is Div
        gRow.add(new GColumn(GColumnEnum.COL_8, new FormGroup(message)));
        gRow.add(new GColumn(GColumnEnum.COL_4, new FormGroup(personalAttrsPanel)));
        addField(CustomFormConstants.SENDER, sender);
        addField(CustomFormConstants.CRM_MESSAGE_CONTENT, personalAttrsPanel);
        addField(CustomFormConstants.CRM_MESSAGE_SUBSCRIPTION_LISTS, mailListTable);
        addField(CustomFormConstants.CRM_MESSAGE_DATETABLE, new InputGroup(date, time), wfmStrings.date());
        addField(CustomFormConstants.CRM_MESSAGE_ANTI_SPAN, notSpammer);
        addField(CustomFormConstants.DESCRIPTION, gRow);
        show();
    }

    @Override
    protected boolean validate(boolean preview) {
        sender.removeStyleName(ERROR_FORM_STYLE);
        mailListTable.removeStyleName(ERROR_FORM_STYLE);
        if (!notSpammer.getValue()) {
            Info.show(crmStrings.agreeTermsService(), Info.Type.WARNING);
            return false;
        }
        int errors = 0;
        if (sender.getSelectedItem() == null) {
            sender.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (mailListTable.getSelectItemsList().size() == 0) {
            mailListTable.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (!Validation.validateTextAreaRequired(message)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected void setItemValues() {
        item.setObjectID(clone ? null : objectID);
        item.setStatus(MessageStatusEnum.PENDING);
        item.setSenderID(sender.getSelectedId());
        item.setCampaignId(campaignSource.getSelectedItemID());
        item.setContent(message.getText());
        item.setScheduled(new Date(date.getDate().getYear(), date.getDate().getMonth(), date.getDate().getDate(), time.getValue()[0], time.getValue()[1]));
        item.setSmsMessage(true);
    }

    @Override
    protected void getDataToFillFields() {
        massMailService.getMailMessage(objectID, isSMS, isView, new AbstractAsyncCallback<MailMessageItem>() {
            public void failure(Throwable throwable) {
            }

            public void success(final MailMessageItem result) {
                Scheduler.get().scheduleDeferred(() -> {
                    item = result;
                    fillFields();
                });
            }
        });
    }

    @Override
    protected void fillFields() {
        sender.setItems(item.getSenders());
        if (item.getSenderID() != null) {
            sender.setSelected(item.getSenderID());
        }
        if (item.getCampaignId() != null && item.getCampaignName() != null) {
            campaignSource.setSelected(item.getCampaignId(), item.getCampaignName());
        }
        if (item.getPersonalAttributes().size() > 0) {
            personalAttrsPanel.add(new HTML("<b class=customTitle>" + wfmStrings.personalizationAttributes() + ":</b>"));
            item.getPersonalAttributes().forEach(attr -> personalAttrsPanel.add(new HTML(attr)));
        }
        date.setDate(item.getScheduled());
        time.setValue(KpiTimePicker.getHoursAndMinutes(item.getScheduled()));

        message.setText(item.getContent());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SMS_MESSAGE_FORM;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
