package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 07-Apr-2018
 * Time: 17:39:36
 */
public class AddSolutionView extends CustomForm2 implements Constants, Colapse {
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected Integer objectId;
    protected SolutionItem item;
    private TextBox title;
    private DataListBox assignee;
    private DataListBox status;
    private KpiEditor questionHTML;
    private KpiEditor answerHTML;
    protected GeneralFileUpload attachment = null;
    private final String nickDebugId = "add_solution_view_";
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public AddSolutionView(Integer objectId) {
        super("addsolution", crmStrings.editSolution());
        this.objectId = objectId;
    }

    public AddSolutionView(String name, String description) {
        super(name, description);
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Solution, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                initialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    public void initialize() {
        title = new TextBox();
        title.ensureDebugId(this.nickDebugId + "title");

        assignee = new DataListBox();
        assignee.ensureDebugId(this.nickDebugId + "assignee");

        status = new DataListBox();
        status.ensureDebugId(this.nickDebugId + "status");

        questionHTML = new KpiEditor(true);
        questionHTML.ensureDebugId(this.nickDebugId + "questionHTML");
        questionHTML.getElement().setId("add_solution_view_question");

        answerHTML = new KpiEditor(true);
        answerHTML.ensureDebugId(this.nickDebugId + "answerHTML");
        answerHTML.getElement().setId("add_solution_view_answer");

        attachment = new GeneralFileUpload(F_SOLUTION, objectId, objectId);

        addTitleField(CRM_SOLUTION_INFORMATION, wfmStrings.solutionInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TITLE) != null) {
            addField(CustomFormConstants.TITLE, title, getTitle(formPropertyMap.get(CustomFormConstants.TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.TITLE).getTitle() : wfmStrings.title()), formPropertyMap.get(CustomFormConstants.TITLE).isRequired());
            title.setEnabled(!formPropertyMap.get(CustomFormConstants.TITLE).isDisabled());
        } else {
            addField(TITLE, title, getTitle(wfmStrings.title(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null) {
            addField(CustomFormConstants.ASSIGNEE, assignee, getTitle(formPropertyMap.get(ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEE).getTitle() : wfmStrings.assignee()), formPropertyMap.get(CustomFormConstants.ASSIGNEE).isRequired());
            assignee.setEnabled(!formPropertyMap.get(CustomFormConstants.ASSIGNEE).isDisabled());
        } else {
            addField(ASSIGNEE, assignee, getTitle(wfmStrings.assignee()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()), formPropertyMap.get(CustomFormConstants.STATUS).isRequired());
            status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
        } else {
            addField(STATUS, status, getTitle(wfmStrings.status()));
        }

        addTitleField(CRM_SOLUTION_DESCRIPTION, wfmStrings.solutionDescription());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION) != null) {
            addField(CustomFormConstants.CRM_SOLUTION_QUESTION, questionHTML, getTitle(formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION).getTitle() : wfmStrings.question()), formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION).isRequired());
            questionHTML.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION).isDisabled());
        } else {
            addField(CRM_SOLUTION_QUESTION, questionHTML, getTitle(wfmStrings.question(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION) != null) {
            addField(CustomFormConstants.CRM_SOLUTION_ANSWER, answerHTML, getTitle(formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_ANSWER).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_ANSWER).getTitle() : wfmStrings.answer()), formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_ANSWER).isRequired());
            answerHTML.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_ANSWER).isDisabled());
        } else {
            addField(CRM_SOLUTION_ANSWER, answerHTML, getTitle(wfmStrings.answer(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ATTACHMENTS) != null) {
            addField(CustomFormConstants.ATTACHMENTS, attachment, getTitle(formPropertyMap.get(CustomFormConstants.ATTACHMENTS).isChanged() ? formPropertyMap.get(CustomFormConstants.ATTACHMENTS).getTitle() : wfmStrings.attachments()), formPropertyMap.get(ATTACHMENTS).isRequired());
        } else {
            addField(ATTACHMENTS, attachment, wfmStrings.attachments(), true);
        }

        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), BTN_PRIMARY, event -> save());
    }

    @Override
    protected void getDataToFillFields() {
        CRMService.App.get().getSolution(objectId, new AbstractAsyncCallback<SolutionItem>() {
            public void failure(Throwable throwable) {
            }

            public void success(final SolutionItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    item = o;
                    fillFieldsWithData();
                });
            }
        });
    }

    protected void fillFieldsWithData() {
        title.setText(item.getTitle());
        assignee.setItems(item.getAssignees());
        if (item.getAssigneeId() != null) {
            assignee.setSelected(item.getAssigneeId());
        } else {
            assignee.setSelected(Utils.getUserID());
        }
        status.setItems(item.getStatuses());
        if (item.getStatusId() != null) {
            status.setSelected(item.getStatusId());
        }
        questionHTML.setData(item.getQuestion() != null ? item.getQuestion() : "");
        answerHTML.setData(item.getAnswer() != null ? item.getAnswer() : "");
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(title)) {
            errors++;
        }
        if (!Validation.validateEditorRequired(questionHTML)) {
            errors++;
        }
        if (!Validation.validateEditorRequired(answerHTML)) {
            errors++;
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TITLE) != null && formPropertyMap.get(CustomFormConstants.TITLE).isRequired()) {
            errors += markAsError(CustomFormConstants.TITLE, title, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.TITLE).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.TITLE).getTitle() : wfmStrings.title(), title, formPropertyMap.get(CustomFormConstants.TITLE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE).isRequired()) {
            errors += markAsError(CustomFormConstants.ASSIGNEE, assignee, !Validation.validateListBoxRequired(assignee));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, status, !Validation.validateListBoxRequired(status));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION) != null && formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_QUESTION).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_SOLUTION_QUESTION, questionHTML, !Validation.validateMaterialEditorRequired(questionHTML));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_ANSWER) != null && formPropertyMap.get(CustomFormConstants.CRM_SOLUTION_ANSWER).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_SOLUTION_ANSWER, answerHTML, !Validation.validateMaterialEditorRequired(answerHTML));
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected void save() {
        if (!validate()) {
            return;
        }
        item.setTitle(title.getText());
        item.setAssigneeId(assignee.getSelectedId());
        item.setStatusId(status.getSelectedId());
        item.setQuestion(questionHTML.getData());
        item.setAnswer(answerHTML.getData());
        item.setAttachments(attachment.getAttachedFiles());
        enableButton(false);
        LoadingPanel.loading(true);
        CRMService.App.get().saveSolution(item, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(final Void o) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), crmStrings.solution()));
                closeTab(null);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SOLUTION_ADD_EDIT, null, AddSolutionView.this);
            }
        });
    }

    @Override
    protected String getWikiCode() {
        return objectId != null ? PermissionConstants.CRM_SOLUTION_EDIT : PermissionConstants.CRM_SOLUTION_ADD;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SOLUTION_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    public String getIconStyle() {
        return "crm solitions-list";
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