package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowPush;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Azazello on 10/14/15.
 */
public class WorkflowPushNotification extends KpiModal implements Constants {
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    private WorkflowPush pushItem;
    private final Integer workflowID;
    private final Integer objectID;
    private final String module;
    private TextBox subject;
    private DataListBox recipient;
    private KpiSelect2 roles;
    private final boolean notForStep;

    private MaterialPanel contentPanel;
    private DataListBox fieldCodes;
    private HTML attribute;
    private final String debug_id = "workflow_push_";
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();
    private Localize localize;

    public final static ReferenceItem[] CASE_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.assignee(), CustomFormConstants.ASSIGNEE),
            new ReferenceItem(1, wfmStrings.resolver(), CustomFormConstants.RESOLVER)};
    public final static SelectItem[] LEAD_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.owner(), CustomFormConstants.OWNER),
            new ReferenceItem(1, wfmStrings.assignee(), CustomFormConstants.ASSIGNEE),
            new ReferenceItem(2, wfmStrings.backupAssignee(), CustomFormConstants.BACKUP_ASSIGNEE)};
    public final static SelectItem[] CONTACT_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.owner(), CustomFormConstants.OWNER)};
    public final static SelectItem[] EVENT_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.createdBy(), CustomFormConstants.CREATOR),
            new ReferenceItem(1, wfmStrings.share(), CustomFormConstants.SHARED_WITH)
    };
    public final static SelectItem[] PURCHASE_ORDER_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.purchaseOrderManager(), CustomFormConstants.MANAGER),
            new ReferenceItem(1, wfmStrings.projectManager(), CustomFormConstants.PROJECT_MANAGER),
            new ReferenceItem(2, wfmStrings.projectBackupManagers(), CustomFormConstants.PROJECT_BACKUP_MANAGER)};
    public final static SelectItem[] OPPORTUNITY_RECIPIENTS = {
            new ReferenceItem(1, wfmStrings.assignee(), CustomFormConstants.ASSIGNEE),
            new ReferenceItem(2, wfmStrings.backupAssignee(), CustomFormConstants.BACKUP_ASSIGNEE)};
    public final static SelectItem[] CASH_ADVANCE_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.requester(), CustomFormConstants.REQUESTER),
            new ReferenceItem(1, wfmStrings.prevApprover(), CustomFormConstants.PREV_APPROVER),
            new ReferenceItem(2, wfmStrings.currentApprover(), CustomFormConstants.CURRENT_APPROVER),
            new ReferenceItem(3, wfmStrings.nextApprover(), CustomFormConstants.NEXT_APPROVER)};
    public final static SelectItem[] LEAVE_REQUEST_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.employee(), CustomFormConstants.EMPLOYEE),
            new ReferenceItem(1, wfmStrings.prevApprover(), CustomFormConstants.PREV_APPROVER),
            new ReferenceItem(2, wfmStrings.currentApprover(), CustomFormConstants.CURRENT_APPROVER),
            new ReferenceItem(3, wfmStrings.nextApprover(), CustomFormConstants.NEXT_APPROVER)};
    public final static SelectItem[] EXPENSE_CLAIM_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.employee(), CustomFormConstants.EMPLOYEE),
            new ReferenceItem(1, wfmStrings.createdBy(), CustomFormConstants.CREATOR),
            new ReferenceItem(2, wfmStrings.currentApprover(), CustomFormConstants.CURRENT_APPROVER),
            new ReferenceItem(3, wfmStrings.nextApprover(), CustomFormConstants.NEXT_APPROVER),
            new ReferenceItem(4, wfmStrings.prevApprover(), CustomFormConstants.PREV_APPROVER)};
    public final static SelectItem[] ONBOARDING_STEP_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.employee(), CustomFormConstants.EMPLOYEE),
            new ReferenceItem(1, wfmStrings.createdBy(), CustomFormConstants.CREATOR),
            new ReferenceItem(2, wfmStrings.currentApprover(), CustomFormConstants.CURRENT_APPROVER)};
    public final static SelectItem[] HRMS_EMPLOYEE_RECIPIENTS = {
            new ReferenceItem(0, wfmStrings.allEmployees(), CustomFormConstants.ALL_EMPLOYEE)};


    public WorkflowPushNotification(Integer workflowID, Integer objectID, String module) {
        this.workflowID = workflowID;
        this.objectID = objectID;
        this.module = module;

        notForStep = WorkflowRule._WORKFLOW_MODULE_CASE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_LEAD.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CONTACT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CS_STUDENT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE.equals(module) ||
//                WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(module) ||
//                WorkflowRule._WORKFLOW_MODULE_SALEQUOTE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE.equals(module) ||
//                WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PRODUCT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_GDN.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PICKLIST.equals(module);
//                WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(module);

        setTitle(wfmStrings.pushNotification());
        init();
        fillFields();
        setWidth(600);
        open();
    }

    private void fillFields() {
        LoadingPanel.loading(true);
        profileService.getWorkflowPush(objectID, workflowID, new AbstractAsyncCallback<WorkflowPush>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WorkflowPush result) {
                LoadingPanel.loading(false);
                pushItem = result;
                subject.setText(pushItem.getSubject());
                recipient.setSelectedByCode(pushItem.getRecipient());
                roles.setItems(result.getAllRoles());
                if (result.getSelectedRoles() != null) {
                    roles.setSelectedItems(result.getSelectedRoles());
                }
                initColumns(pushItem.getWorkflow().getModule());
            }
        });

    }

    private void init() {
        LoadingPanel.loading(true);

        subject = new TextBox();
        subject.setMaxLength(200);

        recipient = new DataListBox();
        roles = new KpiSelect2(true);
        setRecipientItems();

        drawContentPanel();

        WfmButton2 btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.ensureDebugId("save_button");
        btnSave.addClickHandler(sender -> save());

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, (e) -> close());
        addButton(cancel);
        addButton(btnSave);
        addWidget(subject, wfmStrings.subject());
        addWidget(contentPanel, wfmStrings.content());
        /*if (!WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(module)) {*/
            addWidget(recipient, wfmStrings.recipient());
        /*}*/
        if (!notForStep) {
            addWidget(roles, wfmStrings.recipientsByRoles());
        }
        LoadingPanel.loading(false);
    }

    private void setRecipientItems() {
        if (WorkflowRule._WORKFLOW_MODULE_CASE.equals(module)) {
            recipient.setItems(CASE_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_LEAD.equals(module)) {
            recipient.setItems(LEAD_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_CONTACT.equals(module)) {
            recipient.setItems(CONTACT_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(module) || WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(module)) {
            recipient.setItems(EVENT_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER.equals(module)) {
            recipient.setItems(PURCHASE_ORDER_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY.equals(module)) {
            recipient.setItems(OPPORTUNITY_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE.equals(module)) {
            recipient.setItems(CASH_ADVANCE_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST.equals(module)) {
            recipient.setItems(LEAVE_REQUEST_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_EXPENSE_CLAIM.equals(module)) {
            recipient.setItems(EXPENSE_CLAIM_RECIPIENTS);
        } else if (WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(module)) {
            recipient.setItems(HRMS_EMPLOYEE_RECIPIENTS);
        } else if (!notForStep) {
            recipient.setItems(ONBOARDING_STEP_RECIPIENTS);
        }
    }

    private void save() {
        if (!validate()) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        pushItem = pushItem == null ? new WorkflowPush() : pushItem;
        pushItem.setSubject(subject.getText());
        pushItem.setRecipient(recipient.getSelectedItem() != null ? recipient.getSelectedItem().getReferenceCode() : null);
        pushItem.setWorkflowID(workflowID);
        pushItem.setSelectedRoles(roles.getSelectedItems());
        LoadingPanel.loading(true);
        profileService.saveWorkflowPush(pushItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                close();
                Info.show(wfmMessages.workflowPushSuccesfully(objectID == null ? wfmStrings.saved() : Property.get(Constants.TASK, wfmStrings.updated(), wfmStrings.task())), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_PUSH_NOTIFICATION_CHANGE, result, WorkflowPushNotification.this);
            }
        });
    }

    private void drawContentPanel() {
        contentPanel = new MaterialPanel();

        MaterialPanel row = new MaterialPanel("grid-row");
        MaterialPanel dropdownPanel = new MaterialPanel("col-7");
        MaterialPanel attributePanel = new MaterialPanel("col-5");
        row.add(dropdownPanel);
        row.add(attributePanel);

        fieldCodes = new DataListBox();
        fieldCodes.ensureDebugId(debug_id + "fieldCodes");
        fieldCodes.addValueChangeHandler(changeEvent -> {
            if (changeEvent.getValue() != null && changeEvent.getValue().getDescription() != null) {
                attribute.setHTML(changeEvent.getValue().getDescription());
            } else {
                attribute.setHTML(wfmStrings.noAttributesSelected());
            }
        });
        dropdownPanel.add(fieldCodes);

        attribute = new HTML(wfmStrings.noAttributesSelected());
        attribute.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        attribute.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        attributePanel.add(attribute);

        contentPanel.add(row);
    }

    private void initColumns(String code) {
        if (code != null) {
            AllInOneService.App.get().getDefaultModelForm(getFormIDOfModule(code), new AsyncCallback<ModelForm>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ModelForm modelForm) {
                    fields.clear();
                    if (modelForm != null && modelForm.getFields().size() > 0) {
                        for (ModelField field : modelForm.getFields()) {
                            if (field.isEntityField()) {
                                fields.put(field.getField_ID(), field);
                            }
                        }
                    }
                    notifyAllFieldRelateds(modelForm.getAttributes());
                }
            });
        }
    }

    private void notifyAllFieldRelateds(SelectItem[] additionalAttributes) {
        SelectItem[] items = getColumnsAsReferenceItems(additionalAttributes);
        fieldCodes.setItems(items);
        fieldCodes.addValueChangeHandler(changeEvent -> {
            attribute.setText("");
            if (fieldCodes.getSelectedItem() != null && fieldCodes.getSelectedItem().getDescription() != null) {
                attribute.setText(fieldCodes.getSelectedItem().getDescription());
            }
        });
    }

    protected Localize getLocalizer() {
        if (localize == null) {
            localize = new Localize();
        }
        return localize;
    }

    private SelectItem[] getColumnsAsReferenceItems(SelectItem[] additionalAttributes) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                String description = entry.getValue().getField_ID() != null ? ("${" + entry.getValue().getField_ID().toLowerCase() + "}") : entry.getValue().getField_ID();
                result.add(new SelectItem(entry.getValue().getObjectID(), name, description));
            }
        }
        if (additionalAttributes != null && additionalAttributes.length > 0) {
            result.addAll(Arrays.asList(additionalAttributes));
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    private String getFormIDOfModule(String code) {
        if (code != null && !"".equals(code)) {
            if (WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(code)) {
                return LayoutRPC.MANUAL_JOURNAL_FORM;
            } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(code)) {
                return LayoutRPC.TASK_MAX_FORM;
            }
            return code.replace("_WORKFLOW_MODULE_", "") + "_FORM";
        }
        return null;
    }

    public boolean validate() {
        int error = 0;
        subject.removeStyleName(ERROR_FORM_STYLE);
        recipient.removeStyleName(ERROR_FORM_STYLE);
        if (notForStep) {
            if (/*!WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(module) && */!Validation.validateListBoxRequired(recipient, new HTML(), wfmStrings.pleaseSelect())) {
                recipient.addStyleName(ERROR_FORM_STYLE);
                error++;
            }
        }
        if (!Validation.validateTextBoxRequired(subject)) {
            subject.addStyleName(ERROR_FORM_STYLE);
            error++;
        }
        return error == 0;
    }
}
