package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Label;

/**
 * Author: Azazello
 * Date: 4/13/2018
 * Time: 8:48 PM
 */
public class SolutionQuickAddForm extends CrmQuickAddForm {
    interface SolutionQuickAddFormUiBinder extends UiBinder<HTMLPanel, SolutionQuickAddForm> {
    }

    private static final SolutionQuickAddFormUiBinder ourUiBinder = GWT.create(SolutionQuickAddFormUiBinder.class);

    @UiField
    HTMLPanel panel;
    @UiField
    Label titleLabel;
    @UiField
    TextBox title;
    @UiField
    Label assigneeLabel;
    @UiField
    DataListBox assignee;
    @UiField
    Label statusLabel;
    @UiField
    DataListBox status;
    @UiField
    Label questionLabel;
    @UiField
    TextArea question;
    @UiField
    Label answerLabel;
    @UiField
    TextArea answer;

    private final String debug_id = "solution_quick_add_";
    private SolutionItem item;

    public SolutionQuickAddForm(RelationItem... relationItems) {
        initWidget(ourUiBinder.createAndBindUi(this));
        setRelationItems(relationItems);
        initialize();
    }

    protected void initialize() {
        titleLabel.setText(wfmStrings.title());
        assigneeLabel.setText(wfmStrings.assignee());
        statusLabel.setText(wfmStrings.status());
        questionLabel.setText(wfmStrings.question());
        answerLabel.setText(wfmStrings.answer());
        assigneeLabel.setText(wfmStrings.assignee());
        question.setHeight("100px");
        answer.setHeight("100px");

        title.ensureDebugId(this.debug_id + "title");
        assignee.ensureDebugId(this.debug_id + "assignee");
        status.ensureDebugId(this.debug_id + "status");
        question.ensureDebugId(this.debug_id + "question");
        answer.ensureDebugId(this.debug_id + "answer");
    }

    public void getQuickData() {
        CRMService.App.get().getSolution(null, new AbstractAsyncCallback<SolutionItem>() {
            public void failure(Throwable throwable) {
            }

            public void success(final SolutionItem o) {
                item = o;
                assignee.setItems(item.getAssignees());
                assignee.setSelected(Utils.getUserID());
                status.setItems(item.getStatuses());
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(title)) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(question)) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(answer)) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        item.setTitle(title.getText());
        item.setAssigneeId(assignee.getSelectedId());
        item.setStatusId(status.getSelectedId());
        item.setQuestion(question.getText());
        item.setAnswer(answer.getText());
        LoadingPanel.loading(true, panel);
        CRMService.App.get().saveSolution(item, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            public void success(final Void result) {
                LoadingPanel.loading(false, panel);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), crmStrings.solution()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SOLUTION_ADD_EDIT, result, SolutionQuickAddForm.this);
                if (command != null) {
                    command.execute(0);
                }
            }
        });
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    @Override
    protected String getRelationType() {
        return null;
    }

    @Override
    protected String getRelationName() {
        return null;
    }
}