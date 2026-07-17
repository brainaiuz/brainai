package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.addLinkSideNavBox.AddLinkSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadFormPanel;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Dilsh0d Madrahimov on 3/6/2019.
 */
public class EmailSummaryComposeView extends FooteredCustomForm implements Constants, Colapse {
    private static final MessageCenterServiceAsync messageCenterService = MessageCenterService.App.get();

    private DataListBox fromListBox;
    private TextBox toTextBox;
    private TextBox subjectTextBox;
    private TextBox cc;
    private TextBox bcc;
    //private KpiEditor editor;
    private FlowPanel contentPanel;
    private FooterUploadFormPanel footerUploadFormPanel;
    private AddLinkSideNavBox addLinkSideNavBox;
    private FooterInformer linkInformer;
    private String content;

    private final String emailID;
    private Email mailMessage;
    public static Email emailItem;
    AtomicBoolean firstClick = new AtomicBoolean(true);


    public EmailSummaryComposeView(String emailID) {
        super("summary", wfmStrings.mailView());
        this.emailID = emailID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        fromListBox = new DataListBox();
        fromListBox.setWithoutNullLabel(true);
        toTextBox = new TextBox();
        toTextBox.setEnabled(false);

        subjectTextBox = new TextBox();
        subjectTextBox.setEnabled(false);

        cc = new TextBox();
        cc.setEnabled(false);

        bcc = new TextBox();
        bcc.setEnabled(false);

        //editor = new KpiEditor(true, true);
        contentPanel = new FlowPanel();

        FormGroup fromField = new FormGroup(wfmStrings.fromN(), fromListBox);
        FormGroup toField = new FormGroup(wfmStrings.toN(), toTextBox);
        FormGroup subjectField = new FormGroup(wfmStrings.subject(), subjectTextBox);
        FormGroup ccField = new FormGroup(wfmStrings.cc(), cc);
        FormGroup bccField = new FormGroup(wfmStrings.bcc(), bcc);

        toField.getGroupContent().addStyleName("compose__to");

        addField(MESSAGE_CENTER.FROM, fromField);
        addField(MESSAGE_CENTER.TO, toField);
        addField(MESSAGE_CENTER.CC, ccField);
        addField(MESSAGE_CENTER.BCC, bccField);
        addField(MESSAGE_CENTER.SUBJECT, subjectField);
        addField(MESSAGE_CENTER.EDITOR, contentPanel);

        RootPanel.get().addStyleName("fitted-content");
        show();
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        LinkedList<Widget> widgets = new LinkedList<>();

        footerUploadFormPanel = new FooterUploadFormPanel(F_DEFAULT);
        FooterInformer attachment = new FooterInformer(SvgEnum.uploadCloud, wfmStrings.attachments(), null);
        footerUploadFormPanel.setActivator(attachment);

        linkInformer = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        linkInformer.addClickHandler(clickEvent -> {
            drawLinks(mailMessage.getTrackerID(), mailMessage.getSubject(), mailMessage.getRelations());
        });

        widgets.add(attachment);
        widgets.add(linkInformer);

        return widgets;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        LinkedList<Widget> widgets = new LinkedList<>();
        LinkedList<SplitButtonItem> splitConvertButtonItems = new LinkedList<>();
        LinkedList<SplitButtonItem> splitReplyButtonItems = new LinkedList<>();

        SplitButton replySplitButton = new SplitButton(wfmStrings.reply(), 97, BTN_PRIMARY);
        SplitButtonItem replyButton = new SplitButtonItem("REPLY", wfmStrings.reply(), () -> {
            RelationItem.emailItem = mailMessage;
            goTo("emailcompose|add/add/" + null + "/" + Boolean.TRUE + "/" + Boolean.FALSE);
        }, false);

        SplitButtonItem replyToAllButton = new SplitButtonItem("REPLY_ALL", wfmStrings.replyToAll(), () -> {
            RelationItem.emailItem = mailMessage;
            goTo("emailcompose|add/add/" + null + "/" + Boolean.FALSE + "/" + Boolean.FALSE);
        }, false);

        SplitButtonItem forwardButton = new SplitButtonItem("FORWARD", wfmStrings.forward(), () -> {
            RelationItem.emailItem = mailMessage;
            goTo("emailcompose|add/add/" + null + "/" + Boolean.FALSE + "/" + Boolean.TRUE);

        }, false);

        splitReplyButtonItems.add(replyButton);
        splitReplyButtonItems.add(forwardButton);
        splitReplyButtonItems.add(replyToAllButton);
        replySplitButton.addItemList(splitReplyButtonItems);

        Div replyButtonsWrapper = new Div();
        replyButtonsWrapper.add(replySplitButton);

        WfmButton2 deleteButton = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);
        deleteButton.addClickHandler(clickEvent -> deleteMessage());

        Div deleteWrapper = new Div();
        deleteWrapper.add(deleteButton);

        if (Utils.hasPermission(PermissionConstants.CRM_MC_CONVERT_TO_CASE) || Utils.hasPermission(PermissionConstants.CRM_MC_CONVERT_TO_LEAD)) {
            SplitButton convertToSplitButton = new SplitButton(wfmStrings.convertTo(), 97, BTN_PRIMARY);
            if (Utils.hasPermission(PermissionConstants.CRM_MC_CONVERT_TO_CASE)) {
                SplitButtonItem convertToCaseButton = new SplitButtonItem("CASE", wfmStrings.convertTo().concat(" ").concat(Property.get(Constants.CASE_LIST, wfmStrings.crmCase())), () -> convertTo(false), true);
                splitConvertButtonItems.add(convertToCaseButton);
            }
            if (Utils.hasPermission(PermissionConstants.CRM_MC_CONVERT_TO_LEAD)) {
                SplitButtonItem convertToLeadButton = new SplitButtonItem("LEAD", wfmStrings.convertTo().concat(" ").concat(Property.get(Constants.LEADS, wfmStrings.lead())), () -> convertTo(true), false);
                splitConvertButtonItems.add(convertToLeadButton);
            }
            convertToSplitButton.addItemList(splitConvertButtonItems);

            Div convertButtonsWrapper = new Div();
            convertButtonsWrapper.add(convertToSplitButton);

            widgets.add(convertButtonsWrapper);
        }


        widgets.add(replyButtonsWrapper);
        widgets.add(deleteWrapper);

        return widgets;
    }

    private void convertTo(boolean lead) {
        if (lead || mailMessage.getCaseID() == null) {
            LoadingPanel.loading(true);
            service.convertEmailTo(mailMessage.getObjectID(), lead ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CASE, new AbstractAsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(SelectItem item) {
                    LoadingPanel.loading(false);
                    if (item != null) {
                        if (!lead) {
                            Utils.openURL(Utils.getHostURL() + "Crm.html#case|summary/" + item.getId());
                        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(item.getName())) {
                            Utils.openURL(Utils.getHostURL() + "Crm.html#account|summary/" + item.getId());
                        } else if (RelationItem.TYPE_CONTACT.equals(item.getName())) {
                            Utils.openURL(Utils.getHostURL() + "Crm.html#contact|summary/" + item.getId());
                        } else if (item.isDefaultSelected()) {
                            Utils.openURL(Utils.getHostURL() + "Crm.html#leadedit|editlead/" + item.getId());
                        } else {
                            Utils.openURL(Utils.getHostURL() + "Crm.html#lead|summary/" + item.getId());
                        }
                    }
                }
            });
        } else if (mailMessage.getCaseID() != null) {
            Utils.openURL(Utils.getHostURL() + "Crm.html#case|summary/" + mailMessage.getCaseID());
        }
    }

    private void drawLinks(Integer trackerID, String subject, ArrayList<RelationItem> relationItems) {
        if (firstClick.get()) {
            if (addLinkSideNavBox == null) {
                addLinkSideNavBox = new AddLinkSideNavBox(trackerID, RelationItem.TYPE_EMAIL_TRACKER, subject, false);
            }
            addLinkSideNavBox.setSelectedRelations(RelationItem.TYPE_EMAIL_TRACKER, trackerID, relationItems);
            firstClick.set(false);
        } else {
            addLinkSideNavBox.show();
        }
    }

    @Override
    public void reInitialize() {
        drawIFrame();
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
        panel.addStyleName("compose");
        LoadingPanel.loading(true);
        messageCenterService.getEmailWithContent(emailID, new AbstractAsyncCallback<Email>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Email email) {
                LoadingPanel.loading(false);
                if (email == null || email.isDeleted()) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK);
                    messageBox.setMessage("The requested email was not found on email server, it was deleted.");
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            messageBox.close();
                            closeTab();
                        }
                    });
                    messageBox.center();
                } else {
                    if (email.isUnreadStatusChanged()) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, mailMessage, EmailSummaryComposeView.this);
                    }
                    mailMessage = email;
                    emailItem = email;
                    setData();
                }
            }
        });
    }

    private void setData() {
        String fromName = !Utils.isNullOrEmpty(mailMessage.getFromName()) ? mailMessage.getFromName() : "";
        String fromEmail = !Utils.isNullOrEmpty(mailMessage.getFromEmail()) ? mailMessage.getFromEmail() : "";
        fromListBox.setWithoutNullLabel(fromName + " " + fromEmail);
        toTextBox.setText(mailMessage.getToEmails());
        subjectTextBox.setText(mailMessage.getSubject());
        cc.setText(mailMessage.getCc());
        bcc.setText(mailMessage.getBcc());
        //editor.setData(mailMessage.getContent());

        content = mailMessage.getContent().replaceAll("<a\\s*[a-z0-9A-Z_-]*\\s*href=", "<a target=\"_blank\" href=");
        Element iFrame = DOM.createIFrame();
        iFrame.setAttribute("id", "messageCenterEmailSummary" + this.hashCode());
        iFrame.setAttribute("width", "100%");
        iFrame.setAttribute("height", "600px");
        iFrame.setAttribute("scrolling", "yes");
        iFrame.setAttribute("class", "email-summary__iframe");
        contentPanel.getElement().appendChild(iFrame);
        drawIFrame();

        if (mailMessage.getAttachments() != null && !mailMessage.getAttachments().isEmpty()) {
            footerUploadFormPanel.setFilesToPanel(mailMessage.getAttachments());
        }
    }

    private void drawIFrame() {
        if (content != null) {
            content = content.replaceAll("<script[^>]*>(.*?)</script>|<script[^>]*src=[\\\"'][^\\\"']*[\\\"'][^>]*></script>", "");
            Utils.setContentToIFrame("messageCenterEmailSummary" + this.hashCode(), content);
        }
    }

    private void deleteMessage() {
        WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        message.setTitle(wfmStrings.delete() + " " + wfmStrings.message());
        message.setMessage(wfmStrings.sureYouWantToDelete());
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                messageCenterService.setEmailFlags(new ArrayList<>(Collections.singletonList(mailMessage.getObjectID())), mailMessage.getFolderID(), FLAG_DELETED, new AbstractAsyncCallback<Void>() {
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, mailMessage, EmailSummaryComposeView.this);
                        closeTab();
                    }
                });
            }
        });
        message.open();
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.EMAIL_SUMMARY_COMPOSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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
