package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowInvoice;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azazello on 10/6/16.
 */
public class AddEditWorkflowInvoice extends CustomForm implements Constants, CustomFormConstants {
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final ProfileMessages profileMessages = ProfileMessages.App.get();
    private static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    private String test_code_ID_name = "add_edit_workflow_invoice_view_";
    protected Integer objectID;
    protected Integer workflowID;
    protected WorkflowInvoice item;
    private WorkflowInvoiceWidget totalAmount;
    //CF
    public List<CompanyCustomFieldItem> companyCustomFieldItems;
    public DataListBox[] tbValues;

    public AddEditWorkflowInvoice(Integer objectID, Integer workflowID) {
        super("addWorkflowInvoice", objectID == null ? settingsStrings.addWorkflowInvoice() : settingsStrings.editWorkflowInvoice());
        this.objectID = objectID;
        this.workflowID = workflowID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        loadPage();
        return null;
    }

    private void loadPage() {
        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyCustomFields(ViewName.SaleInvoice, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            public void failure(Throwable throwable) {
            }

            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    companyCustomFieldItems = result;
                    initialize();
                }
            }
        });
    }

    protected void initialize() {
        totalAmount = new WorkflowInvoiceWidget(null);
        drawForm();
    }

    protected void drawForm() {
        addTitleField(WORKFLOW_INVOICE.WORKFLOW_INVOICE_INFORMATION, getTitle(wfmStrings.invoiceInformation()));
        addField(WORKFLOW_INVOICE.TOTAL_AMOUNT, totalAmount, getTitle(wfmStrings.totalAmount()));
//        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
//            addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
//            tbValues = new DataListBox[companyCustomFieldItems.size()];
//            for (int i = 0; i < companyCustomFieldItems.size(); i++) {
//                tbValues[i] = new DataListBox();
//                tbValues[i].setLayoutData(companyCustomFieldItems.get(i).getColumnCode());
//                tbValues[i].addStyleName(DEFAULT_WIDTH);
//                addField("string_value" + (i + 1), tbValues[i], companyCustomFieldItems.get(i).getFieldName());
//            }
//        }
        show();
    }

    @Override
    protected void addButtons() {
        addButton(objectID == null ? wfmStrings.save() : wfmStrings.update(), null, (test_code_ID_name + "save_and_close_button"), (ClickHandler) event -> save());
    }

    private void save() {
        getValues();
        LoadingPanel.loading(true);
        profileService.saveWorkflowInvoice(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                closeTab();
                Info.show(profileMessages.workflowInvoiceSavSuc());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_INVOICE_MODIFICATION, item, AddEditWorkflowInvoice.this);
            }
        });
    }

    private void getValues() {
        item = item == null ? new WorkflowInvoice() : item;
        item.getInvoiceFields().clear();
        item.getInvoiceFields().add(totalAmount.getInvoiceField(TOTAL_AMOUNT));
//        if (tbValues != null && tbValues.length > 0) {
//            for (DataListBox box : tbValues) {
//                if(box.getSelectedItem() != null){
//                }
//            }
//        }
    }

    @Override
    protected void getDataToFillFields() {
        profileService.getWorkflowInvoice(objectID, workflowID, new AbstractAsyncCallback<WorkflowInvoice>() {
            public void failure(Throwable d) {
                LoadingPanel.loading(false);
                closeTab();
            }

            public void success(final WorkflowInvoice result) {
                DeferredCommand.addCommand(() -> {
                    LoadingPanel.loading(false);
                    item = result;
                    setValues();
                });
            }
        });
    }

    private SelectItem[] getColumnsAsReferenceItem(ArrayList<ModelField> fields) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields.size() > 0) {
            for (ModelField entry : fields) {
                String localized = getLocalizer().localizeByFieldID(entry.getForm_ID(), entry.getField_ID());
                String name = localized != null ? localized : (entry.getField_ID().contains("string_value") || entry.getField_ID().contains("double_value") || entry.getField_ID().contains("date_value") ? entry.getLabel() : entry.getField_ID());
                String description = entry.getField_ID();
                result.add(new SelectItem(entry.getObjectID(), name, description));
            }
        }
        result.sort((item1, item2) -> item1.getName().compareTo(item2.getName()));
        return result.toArray(new SelectItem[]{});
    }

    private void setItems(SelectItem[] fields) {
        if (tbValues != null && tbValues.length > 0) {
            int i = 0;
            for (DataListBox box : tbValues) {
                String title = companyCustomFieldItems.size() > i && companyCustomFieldItems.get(i) != null ? companyCustomFieldItems.get(i).getFieldName() : null;
                box.setItems(fields, objectID == null ? title : wfmStrings.pleaseSelect());
                i++;
            }
        }
    }

    private void setValues() {
        totalAmount.setField(item.getFieldsMap().get(TOTAL_AMOUNT));
        if (item.getInvoiceFields().size() > 0) {
            totalAmount.fill(item.getInvoiceFields().get(0));
        }
//        if (tbValues != null && tbValues.length > 0) {
//            for (DataListBox box : tbValues){
//            }
//        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKFLOW_INVOICE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
