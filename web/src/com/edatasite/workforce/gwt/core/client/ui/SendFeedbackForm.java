package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.MaterialFileUploadForm;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.html.Label;

/**
 * Created by Anvar Akramov on 12/13/17.
 */
public class SendFeedbackForm extends Composite implements Constants {
    interface SendFeedbackFormUiBinder extends UiBinder<Widget, SendFeedbackForm> {}
    private static SendFeedbackFormUiBinder ourUiBinder = GWT.create(SendFeedbackFormUiBinder.class);

    @UiField
    DataListBox dwFeedBackType;
    @UiField
    Label feedbackTypeLabel;
    @UiField
    MaterialInput subject;
    @UiField
    Label subjectLabel;
    @UiField
    TextArea askYourQuestionHere;
    @UiField
    Label askYourQuestionHereLabel;
    @UiField
    MaterialFileUploadForm attachments;
    @UiField
    MaterialCheckBox anonymousFeedbackCheckBox;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static Integer questionid = 1;
    private static Integer issueid = 2;
    private static Integer suggestionid = 3;
    private static Integer otherid = 4;
    private String currentUserViewSummary;

    public static final SelectItem[] feedback_types_message = new SelectItem[]{
            new SelectItem(questionid, wfmStrings.question(), true),
            new SelectItem(issueid, wfmStrings.issue(), true),
            new SelectItem(suggestionid, wfmStrings.suggestion(), true),
            new SelectItem(otherid, wfmStrings.other(), true)
    };


    public SendFeedbackForm() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initForm();
    }

    private void initForm() {
        feedbackTypeLabel.setText(wfmStrings.type());
        subjectLabel.setText(wfmStrings.subject());
        askYourQuestionHereLabel.setText(wfmStrings.askYourQuestionHere());
        anonymousFeedbackCheckBox.setText(wfmStrings.sendAnonymously());
        anonymousFeedbackCheckBox.setValue(false);
    }

    public void clearForm() {
        dwFeedBackType.setSelectedNullLabel();
        subject.setText("");
        askYourQuestionHere.setText("");
        attachments.clearFiles();
        anonymousFeedbackCheckBox.setValue(false);
    }


    public boolean validate() {
        if (subject.getText().trim().isEmpty() && askYourQuestionHere.getText().trim().isEmpty()) {
            return false;
        } else {
            return attachments.isFinished();
        }
    }

    public BugReportItem getBugReportItem() {
        boolean isSystemRefrences = dwFeedBackType.getSelectedItem() != null && !dwFeedBackType.getSelectedItem().isNewItem();
        String message = "";
        if (!isSystemRefrences) {
            if (dwFeedBackType.getSelectedId() == issueid) {
                message = "<b>" + wfmStrings.bugUpperCase() + "</b>: " + askYourQuestionHere.getText();
            } else if (dwFeedBackType.getSelectedId() == suggestionid) {
                message = "<b>" + wfmStrings.feedBackSuggestionUpperCase() + "</b>: " + askYourQuestionHere.getText();
            } else if (dwFeedBackType.getSelectedId() == otherid) {
                message = "<b>" + wfmStrings.feedBackOtherUpperCase() + "</b>: " + askYourQuestionHere.getText();
            } else {
                message = "<b>" + wfmStrings.feedBackQuestionUpperCase() + "</b>: " + askYourQuestionHere.getText();
            }
        } else {
            message = askYourQuestionHere.getText();
        }

        final BugReportItem bugReportItem = new BugReportItem();
        bugReportItem.setReportText(message);
        bugReportItem.setSubjectText(subject.getValue());
        bugReportItem.setReportSection(currentUserViewSummary);
        bugReportItem.setUserAgent(Utils.getUserAgent());
        bugReportItem.setAnonim(anonymousFeedbackCheckBox.isChecked());
        if (isSystemRefrences) {
            bugReportItem.setTypeId(dwFeedBackType.getSelectedId());
        }

        final FileItem[] fileItems = new FileItem[attachments.getUploadedFiles().size()];
        for (int i = 0; i < attachments.getUploadedFiles().size(); i++) {
            fileItems[i] = attachments.getUploadedFiles().get(i).getFileItem();
        }
        bugReportItem.setAttachments(fileItems);

        return bugReportItem;
    }

    public void setCurrentUserViewSummary(String currentUserViewSummary) {
        this.currentUserViewSummary = currentUserViewSummary;
    }

    public void setFrom(String from) {
        if (Constants.SUPERVISOR_STRUCTURE.equals(from)) {
            dwFeedBackType.setSelected(new SelectItem(questionid, wfmStrings.question()));
            subject.setText("Supervisor Structure");
            anonymousFeedbackCheckBox.setValue(false);
        } else {
            clearForm();
        }
    }

    public MaterialCheckBox getAnonymousFeedbackCheckBox() {
        return anonymousFeedbackCheckBox;
    }

    public MaterialFileUploadForm getAttachments() {
        return attachments;
    }

    public void getTypes() {
        CRMService.App.get().getCaseTypes(new AbstractAsyncCallback<CaseItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(CaseItem result) {
                if (result.getTypes() != null && result.getTypes().length > 0) {
                    dwFeedBackType.setItems(result.getTypes());
                } else {
                    dwFeedBackType.setItems(feedback_types_message);
                }

            }
        });
    }
}
