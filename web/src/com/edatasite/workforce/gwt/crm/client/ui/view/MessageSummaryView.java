package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

/**
 * User: dilshod madrahimov
 * Date: 5/25/12
 * Time: 3:00 PM
 */
public class MessageSummaryView extends AddMessageView implements Constants, Colapse {
    private HTML campaign, subject, preheader, fromEmail, fromName, replyTo, messageText, scheduled, mailListTable, status, entitiesCount, sentCount, unsubscribeCount,
            deliveryCount, deliveryRate, bounceCount, bounceRate, viewCount, viewRate, clickCount, clickRate;
    private final boolean isSent;

    public MessageSummaryView(Integer objectID, boolean isSMS, boolean isSent) {
        super("viewmailmessage", crmStrings.messageView());
        this.objectID = objectID;
        this.isSMS = isSMS;
        this.isSent = isSent;
        this.isView = true;
    }

    public void initialize() {
        fromEmail = initHTML();
        fromName = initHTML();
        status = initHTML();
        scheduled = initHTML();
        mailListTable = initHTML();
        messageText = initHTML();
        campaign = initHTML();
        if (!isSMS) {
            subject = initHTML();
            preheader = initHTML();
            replyTo = initHTML();
            if (isSent) {
                entitiesCount = initHTML();
                sentCount = initHTML();
                unsubscribeCount = initHTML();

                deliveryCount = initHTML();
                deliveryRate = initHTML();

                bounceCount = initHTML();
                bounceRate = initHTML();

                viewCount = initHTML();
                viewRate = initHTML();

                clickCount = initHTML();
                clickRate = initHTML();
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.MASSMAILING_ATTACHMENT_ENABLED)) {
                fileUpload = new GeneralFileUpload(Constants.F_MASS_MAILING, objectID, objectID);
            }
        }
    }


    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void drawForm() {
        addTitleField(CRM_MESSAGE_DETAILS, wfmStrings.messageDetails());
        addField(CRM_MESSAGE_FROM, fromEmail, getTitle(!isSMS ? wfmStrings.fromEmail() : wfmStrings.sender()));
        addField(CRM_MESSAGE_SOURCE, campaign, getTitle(wfmStrings.campaign()));
        addField(CRM_MESSAGE_STATUS, status, getTitle(wfmStrings.status()));
        addField(CRM_MESSAGE_DATETABLE, scheduled, getTitle(isSent ? crmStrings.sentDate() : crmStrings.scheduledDate()));
        addField(CRM_MESSAGE_SUBSCRIPTION_LISTS, mailListTable, getTitle(wfmStrings.subscriptionLists()));
        addTitleField(CRM_MESSAGE_CONTENT, wfmStrings.emailContent());
        addField(CRM_MESSAGE_FIELD, messageText, getTitle(wfmStrings.messageText()));
        if (!isSMS) {
            addField(CRM_MESSAGE_SUBJECT, subject, getTitle(wfmStrings.subject()));
            addField(CRM_MESSAGE_PREHEADER, preheader, getTitle(wfmStrings.preheader()));
            addField(CRM_MESSAGE_REPLYTO, replyTo, getTitle(wfmStrings.replyToOnly()));
            addField(CRM_MESSAGE_FULLNAME, fromName, getTitle(wfmStrings.fromName()));
            if (isSent) {
                addTitleField(CRM_MESSAGE_STATISTICS, getTitle(crmStrings.messageStatistics()));
                addField(CRM_MESSAGE_ENTITIES_COUNT, entitiesCount, getTitle(crmStrings.numberOfContacts()));
                addField(CRM_MESSAGE_SENT_COUNT, sentCount, getTitle(crmStrings.totalSent()));
                addField(CRM_MESSAGE_UNSUBSCRIBES_COUNT, unsubscribeCount, getTitle(crmStrings.unsubscribedUsers()));

                addField(CRM_MESSAGE_DELIVERY_COUNT, deliveryCount, getTitle(crmStrings.delivered()));
                addField(CRM_MESSAGE_DELIVERY_RATE, deliveryRate, getTitle(crmStrings.deliveryRate()));

                addField(CRM_MESSAGE_BOUNCED_COUNT, bounceCount, getTitle(crmStrings.bounced()));
                addField(CRM_MESSAGE_BOUNCED_RATE, bounceRate, getTitle(crmStrings.bouncedRate()));

                addField(CRM_MESSAGE_VIEW_COUNT, viewCount, getTitle(crmStrings.viewCount()));
                addField(CRM_MESSAGE_VIEW_RATE, viewRate, getTitle(crmStrings.viewRate()));

                addField(CRM_MESSAGE_CLICK_COUNT, clickCount, getTitle(crmStrings.clickCount()));
                addField(CRM_MESSAGE_CLICK_RATE, clickRate, getTitle(crmStrings.clickRate()));
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.MASSMAILING_ATTACHMENT_ENABLED)) {
                addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);
            }
        }
        show();
    }

    @Override
    protected void fillFields() {
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_EDIT) && !MessageStatusEnum.IN_PROGRESS.equals(item.getStatus())) {
            addEditButton().addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("message|add/add/" + objectID + "/" + isSMS + "/" + (isSent ? "copy" : "")));
        }
        setInnerHTML(fromEmail, isSMS ? item.getSenderName() : item.getFrom());
        setInnerHTML(status, item.getStatus().getCode());
        setInnerHTML(campaign, item.getCampaignName());
        setInnerHTML(mailListTable, item.getSubscribedLists());
        setInnerHTML(scheduled, DateUtils.formatInternalShort1(item.getScheduled()));
        setInnerHTML(messageText, item.getContent());
        if (!isSMS) {
            setInnerHTML(subject, item.getSubject());
            setInnerHTML(preheader, item.getPreheader());
            setInnerHTML(replyTo, item.getReplyTo());
            setInnerHTML(fromName, item.getFullName());
            if (isSent) {
                setInnerHTML(entitiesCount, item.getEntitiesCount().toString());
                setInnerHTML(sentCount, item.getSentCount().toString());
                setInnerHTML(unsubscribeCount, item.getUnsubscribedCount().toString());

                setInnerHTML(deliveryCount, item.getDeliveryCount().toString());
                setInnerHTML(deliveryRate, item.getDeliveryRate() + " %");

                setInnerHTML(bounceCount, item.getBouncedCount().toString());
                setInnerHTML(bounceRate, item.getBouncedRate() + " %");

                setInnerHTML(viewCount, item.getViewCount().toString());
                setInnerHTML(viewRate, item.getViewRate() + " %");

                setInnerHTML(clickCount, item.getClickCount().toString());
                setInnerHTML(clickRate, item.getClickRate() + " %");
            }
        }
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_REMOVE)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(clickEvent -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        MassMailService.App.get().deleteMailMessage(objectID, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable caught) {
                                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                            }

                            @Override
                            public void success(Void result) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.message()), Info.Type.INFO);
                            }
                        });
                    }
                });
                messageBox.open();
            });
            options.add(delete);
        }
    }

    public String getIconStyle() {
        return "crm message-summary";
    }

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
