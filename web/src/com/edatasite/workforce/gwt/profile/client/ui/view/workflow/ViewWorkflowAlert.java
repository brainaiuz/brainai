package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Azazello
 * Date: 9/29/14
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewWorkflowAlert extends AddWorkflowAlert implements Constants, CustomFormConstants {
    private FlowPanel bccAndCcPanel;
    private HTML recipient, template, subject, content, timeBasedActionDate, fromEmail, fromName, bccLabel, ccLabel, bcc, cc, includePDF;

    private final String test_code_ID_name = "view_workflow_alert_";

    public ViewWorkflowAlert(Integer objectID, Integer workflowID) {
        super("viewworkflowalert", wfmStrings.alert(), objectID, workflowID);
        this.objectID = objectID;
        this.workflowID = workflowID;
    }

    public void registerFields() {
        //recipient
        recipient = new HTML();
        recipient.ensureDebugId(test_code_ID_name + "recipient");
        //template
        template = new HTML();
        template.ensureDebugId(test_code_ID_name + "template");
        //subject
        subject = new HTML();
        subject.ensureDebugId(test_code_ID_name + "subject");
        //subject
        includePDF = new HTML();
        includePDF.ensureDebugId(test_code_ID_name + "subject");
        //subject
        content = new HTML();
        content.ensureDebugId(test_code_ID_name + "content");
        //from email
        fromEmail = new HTML();
        fromEmail.ensureDebugId(test_code_ID_name + "fromEmail");
        //from name
        fromName = new HTML();
        fromName.ensureDebugId(test_code_ID_name + "fromName");
        //bcc
        bcc = new HTML();
        bccLabel = new HTML(wfmStrings.bcc() + ":");
        bcc.setStyleName("field");
        bcc.ensureDebugId(test_code_ID_name + "bcc");
        bccLabel.ensureDebugId(test_code_ID_name + "bccLabel");
        //cc
        cc = new HTML();
        ccLabel = new HTML(wfmStrings.cc() + ":");
        cc.setStyleName("field");
        cc.ensureDebugId(test_code_ID_name + "cc");
        ccLabel.ensureDebugId(test_code_ID_name + "ccLabel");
        //bcc and cc
        bccAndCcPanel = new FlowPanel();
        bccAndCcPanel.ensureDebugId(test_code_ID_name + "bccAndCcPanel");
        //time based action date
        timeBasedActionDate = new HTML();
        timeBasedActionDate.ensureDebugId(test_code_ID_name + "timeBasedActionDate");
        //body
        FlexTable templateMessageContentTable = new FlexTable();
        templateMessageContentTable.setHTML(0, 0, "<b>" + getTitle(wfmStrings.emailContent()) + "</b>");
        templateMessageContentTable.setWidget(0, 1, content);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setWidth(0, 0, "145px");

        addField(WORKFLOW_ALERT_FORM.RECEPIENT, recipient, getTitle(wfmStrings.recipient()));
        addField(WORKFLOW_ALERT_FORM.BCC_CC_PANEL, bccAndCcPanel);
        addField(WORKFLOW_ALERT_FORM.TEMPLATE, template, getTitle(wfmStrings.template()));
        addField(WORKFLOW_ALERT_FORM.SUBJECT, subject, getTitle(wfmStrings.subject()));
        addField(WORKFLOW_ALERT_FORM.CONTENT, content, getTitle(wfmStrings.emailContent()));
        addField(WORKFLOW_ALERT_FORM.FROM_EMAIL, fromEmail, getTitle(wfmStrings.fromEmail()));
        addField(WORKFLOW_ALERT_FORM.FROM_NAME, fromName, getTitle(wfmStrings.fromName()));

        addTitleField(WORKFLOW_TIME_BASED_HEADER, wfmStrings.timeBasedAction());
        addField(WORKFLOW_TIME_BASED, timeBasedActionDate, getTitle(wfmStrings.timeBasedAction()));

        show();
    }

    public void setValuesToWidgets() {
        if (item != null) {
            if (WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(item.getWorkflowRule().getModule())) {
                addField(WORKFLOW_ALERT_FORM.INCLUDE_PDF, includePDF, getTitle(wfmStrings.includePDF()));
            }
            setBccAndCcc();
            setInnerHTML(recipient, item.getRecepient());
            setInnerHTML(template, item.getEmailTemplate() != null ? item.getEmailTemplate().getName() : "");
            setInnerHTML(subject, item.getSubject());
            setInnerHTML(timeBasedActionDate, getTimeBasedActionStrings());
            setInnerHTML(content, item.getContent());
            String fromEmail = item.getFromEmail() != null ? item.getFromEmail() : "";
            if (item.getFromEmail() == null && item.getFromUsers() != null) {
                item.getFromUsers();
                for (SelectItem s : item.getFromUsers()) {
                    if (s.getId() == 0) {
                        fromEmail = s.getName();
                        break;
                    }
                }
            }
            setInnerHTML(this.fromEmail, fromEmail);
            setInnerHTML(fromName, item.getFromName());
            setInnerHTML(includePDF, item.isIncludeAttachment() ? wfmStrings.yes() : wfmStrings.no());
        }
    }

    private void setBccAndCcc() {
        FlowPanel row1 = new FlowPanel();
        FlowPanel row2 = new FlowPanel();
        row1.setStyleName("row");
        row2.setStyleName("row");
        if (!Utils.isNullOrEmpty(item.getToBCC())) {
            row1.add(bccLabel);
            row1.add(bcc);
            setInnerHTML(bcc, item.getToBCC());
            bccAndCcPanel.add(row1);
        }
        if (!Utils.isNullOrEmpty(item.getToCC())) {
            row2.add(ccLabel);
            row2.add(cc);
            setInnerHTML(cc, item.getToCC());
            bccAndCcPanel.add(row2);
        }
    }

    private String getTimeBasedActionStrings() {
        String time = "";
        if (item.getWorkflowActionStartTimeUnit() != null) {
            time += item.getWorkflowActionStartTimeUnit();
        }
        if (item.getWorkflowActionStartTimeGranularity() != null) {
            if (TIME_GRANULARITY.MINUTES.equals(item.getWorkflowActionStartTimeGranularity())) {
                time += (" " + wfmStrings.minutes());
            } else if (TIME_GRANULARITY.HOURS.equals(item.getWorkflowActionStartTimeGranularity())) {
                time += (" " + wfmStrings.hours());
            } else if (TIME_GRANULARITY.DAYS.equals(item.getWorkflowActionStartTimeGranularity())) {
                time += (" " + wfmStrings.days());
            }
            time += " after ";
        }
        if (item.getWorkflowActionStartTime() != null) {
            if (WORKFLOW_START_TIME.TRIGGER_TIME.equals(item.getWorkflowActionStartTime())) {
                time += (" " + wfmStrings.triggerDate());
            }
            if (WORKFLOW_START_TIME.ENTITY_CREATION_TIME.equals(item.getWorkflowActionStartTime())) {
                time += (" " + wfmStrings.createdDate());
            }
            if (WORKFLOW_START_TIME.ENTITY_MODIFICATION_TIME.equals(item.getWorkflowActionStartTime())) {
                time += (" " + wfmStrings.modifiedDate());
            }
        }
        return !"".equals(time) ? time : wfmStrings.notAvailable();
    }

    protected void addButtons() {
        addEditButton().addClickHandler(event -> closeTab("workflowalert|add/add/" + item.getObjectID()));
        addRemoveButton().addClickHandler(event -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmMessages.sureYouWantToDelete("", wfmStrings.alert()));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    AllInOneService.App.get().delete(LayoutRPC.WORKFLOW_ALERT_FORM, Utils.asArrayList(item.getObjectID()), new AbstractAsyncCallback() {
                        @Override
                        public void failure(Throwable caught) {
                        }

                        @Override
                        public void success(Object result) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_ALERT_DELETE, result, ViewWorkflowAlert.this);
                            closeTab();
                        }
                    });
                }
            });
            messageBox.open();
        });
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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
