package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.ChooseCRMItemAndSearch;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Label;

import java.util.Map;

/**
 * Created by: Azazello
 * Date: 1/16/2018
 * Time: 2:55 PM
 */
public class CaseQuickAddForm extends CrmQuickAddForm {
    interface CaseQuickAddFormUiBinder extends UiBinder<HTMLPanel, CaseQuickAddForm> {
    }

    private static CaseQuickAddFormUiBinder ourUiBinder = GWT.create(CaseQuickAddFormUiBinder.class);

    @UiField
    HTMLPanel panel;
    @UiField
    Label subjectLabel;
    @UiField
    TextBox subject;
    @UiField
    Label descriptionLabel;
    @UiField
    TextArea description;
    @UiField
    Label reporterLabel;
    @UiField
    HTMLPanel reporter;
    @UiField
    Label assigneeLabel;
    @UiField
    HTMLPanel assignee;
    @UiField
    HTMLPanel statusContainer;
    @UiField
    Label statusLabel;
    @UiField
    DataListBox status;
    @UiField
    Label typeLabel;
    @UiField
    DataListBox type;
//    @UiField
//    Label priorityLabel;
//    @UiField
//    DataListBox priority;

    private String debug_id = "case_quick_add_";
    private CaseItem item;
    private ChooseCRMItemAndSearch reporterWidget;
    private EmployeeLookUp assigneeWidget;
    private Integer statusID;

    public CaseQuickAddForm(Integer statusID, RelationItem... relationItems) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.statusID = statusID;
        setRelationItems(relationItems);
        initialize();
    }

    protected void initialize() {
        subjectLabel.setText(wfmStrings.subject());
        descriptionLabel.setText(wfmStrings.description());
        reporterLabel.setText(wfmStrings.reporter());
        typeLabel.setText(wfmStrings.type());
//        priorityLabel.setText(wfmStrings.priority());
        assigneeLabel.setText(wfmStrings.assignee());
        statusLabel.setText(wfmStrings.status());
        description.setHeight("100px");
        reporterWidget = new ChooseCRMItemAndSearch(null);
        if (relationItems != null) {
            for (RelationItem relationItem : relationItems) {
                if (relationItem != null && (RelationItem.TYPE_CRM_ACCOUNT.equals(relationItem.getToType()) || RelationItem.TYPE_CONTACT.equals(relationItem.getToType()) || RelationItem.TYPE_LEAD.equals(relationItem.getToType()))) {
                    reporterWidget.setValues(relationItem.getToType(), new SelectItem(relationItem.getToID(), relationItem.getToName()));
                    break;
                }
            }
        }
        reporter.add(reporterWidget);
        reporter.add(reporterWidget.getOtherFields());
        assigneeWidget = new EmployeeLookUp(true, true, false);
        assignee.add(assigneeWidget);
        subject.ensureDebugId(this.debug_id + "subject");
        description.ensureDebugId(this.debug_id + "description");
        reporterWidget.ensureDebugId(this.debug_id + "reporterWidget");
        type.ensureDebugId(this.debug_id + "type");
//        priority.ensureDebugId(this.debug_id + "priority");
        assigneeWidget.ensureDebugId(this.debug_id + "assigneeWidget");
        status.ensureDebugId(this.debug_id + "status");
    }

    public void getQuickData() {
        CRMService.App.get().getCaseQuickData(new AbstractAsyncCallback<CaseItem>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(CaseItem result) {
                item = result;
                type.setItems(result.getTypes());
//                priority.setItems(result.getPriorities());
                if (item.getCaseAssigneeId() != null) {
                    String assigneeName = item.getCaseAssigneeName();
                    if (Utils.getUserID().equals(item.getCaseAssigneeId())) {
                        assigneeName += "(Myself)";
                    }
                    assigneeWidget.setSelected(item.getCaseAssigneeId(), assigneeName);
                }
                status.setItems(result.getStatusItems());
                if (statusID != null && statusID > 0) {
                    status.setSelected(statusID);
                    statusContainer.setVisible(false);
                }
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(subject)) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(description)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(assigneeWidget)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(status)) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        setValuesToRPC();
        LoadingPanel.loading(true, panel);
        CRMService.App.get().saveCase(item, false, new AbstractAsyncCallback<SelectItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(final SelectItem result) {
                LoadingPanel.loading(false, panel);
                Info.show(Property.get(CASE_LIST, wfmStrings.messSuccessfullyAdded(), wfmStrings.caseID()), Info.Type.INFO);
                if (Utils.isWebForm()) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.WEB_FORM_SAVED, result, CaseQuickAddForm.this);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_ADD, result, CaseQuickAddForm.this);
                if (command != null) {
                    command.execute(result.getId());
                }
            }
        });
    }

    private void setValuesToRPC() {
        item.setSubject(subject.getText());
        item.setDescription(description.getText());
        item.setAccountId(null);
        item.setCrmContactID(null);
        item.setLeadId(null);
        if (reporterWidget.isLeadChecked() || reporterWidget.isAccountChecked() || reporterWidget.isContactChecked()) {
            SelectItem reportBy = reporterWidget.getReporter();
            if (reportBy != null) {
                if (reporterWidget.isLeadChecked()) {
                    item.setLeadId(reportBy.getId());
                    item.setLead(reportBy.getName());
                } else {
                    item.setLead(null);
                    item.setLeadId(null);
                }
                if (reporterWidget.isAccountChecked()) {
                    item.setAccountId(reportBy.getId());
                    item.setAccountName(reportBy.getName());
                } else {
                    item.setAccountId(null);
                    item.setAccountName(null);
                }
                if (reporterWidget.isContactChecked()) {
                    item.setCrmContactID(reportBy.getId());
                    item.setCrmContact(reportBy.getName());
                } else {
                    item.setCrmContact(null);
                    item.setCrmContactID(null);
                }
            }
        } else {
            if (reporterWidget.isOtherChecked()) {
                Map<String, String> newReporter = reporterWidget.getOtherReporterInformation();
                if (newReporter != null && newReporter.size() > 0) {
                    item.setFirstName(newReporter.get("firstName"));
                    item.setLastName(newReporter.get("lastName"));
                    item.setCompany(newReporter.get("company"));
                    item.setEmail(newReporter.get("email"));
                    item.setPhone(newReporter.get("phone"));
                    item.setFax(newReporter.get("fax"));
                }
            }
        }
        item.setTypeId(type.getSelectedId());
//        if (priority.getSelectedItem() != null) {
//            item.setPriorityId(priority.getSelectedItem().getId());
//        }
        item.setStatus(status.getSelectedItem());
        if (assigneeWidget.getSelectedItem() != null) {
            if (assigneeWidget.getSelectedItem().getName().contains("(Department)")) {
                item.setDepartmentID(assigneeWidget.getSelectedItemID());
                item.setDepartment(assigneeWidget.getSelectedItem().getName());
            } else {
                item.setCaseAssigneeId(assigneeWidget.getSelectedItemID());
                item.setCaseAssigneeName(assigneeWidget.getSelectedItem().getName());
            }
        }
        item.setRelations(getRelations());
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_CASE;
    }

    @Override
    protected String getRelationName() {
        return subject.getText();
    }
}