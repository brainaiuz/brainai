package com.edatasite.workforce.gwt.project.client.ui.view.projectImport;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Normurod on 9/19/15.
 */
public class ImportProjectView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    private DataListBox dwProjectNumber;
    private DataListBox dwProjectName;
    private DataListBox dwStartDate;
    private DataListBox dwDueDate;
    private DataListBox assignee;
    private DataListBox manager;
    private DataListBox dwClient;
    private DataListBox dwDescription;
    private DataListBox dwStatus;

    private String importProjectView = "import_products_view_";

    public ImportProjectView(Integer objectId) {
        super("importprojectadd", "Import Project");
        this.objectId = objectId;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        dwProjectNumber = new DataListBox();
        dwProjectNumber.ensureDebugId(importProjectView + "projectNumber");
        dwProjectNumber.addStyleName(DEFAULT_WIDTH);

        dwProjectName = new DataListBox();
        dwProjectName.ensureDebugId(importProjectView + "projectName");
        dwProjectName.addStyleName(DEFAULT_WIDTH);

        dwDescription = new DataListBox();
        dwDescription.ensureDebugId(importProjectView + "description");
        dwDescription.addStyleName(DEFAULT_WIDTH);

        dwStartDate = new DataListBox();
        dwStartDate.ensureDebugId(importProjectView + "startDate");
        dwStartDate.addStyleName(DEFAULT_WIDTH);

        dwDueDate = new DataListBox();
        dwDueDate.ensureDebugId(importProjectView + "dueDate");
        dwDueDate.addStyleName(DEFAULT_WIDTH);

        dwClient = new DataListBox();
        dwClient.ensureDebugId(importProjectView + "client");
        dwClient.addStyleName(DEFAULT_WIDTH);

        manager = new DataListBox();
        manager.ensureDebugId(importProjectView + "manager");
        manager.addStyleName(DEFAULT_WIDTH);

        dwStatus = new DataListBox();
        dwStatus.ensureDebugId(importProjectView + "status");
        dwStatus.addStyleName(DEFAULT_WIDTH);

        assignee = new DataListBox();
        assignee.ensureDebugId(importProjectView + "assignee");
        assignee.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addTitleField(BASIC_DETAILS, wfmStrings.basicDetails());
        addField(CustomFormConstants.NUMBER, dwProjectNumber, getTitle(wfmStrings.number(), false));
        addField(CustomFormConstants.NAME, dwProjectName, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DESCRIPTION, dwDescription, getTitle(wfmStrings.description(), false));
        addField(CustomFormConstants.START_DATE, dwStartDate, getTitle(wfmStrings.startDate(), true));
        addField(CustomFormConstants.DUE_DATE, dwDueDate, getTitle(wfmStrings.dueDate(), true));
        addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(wfmStrings.manager(), true));
        addField(CustomFormConstants.PROJECT.CLIENT, dwClient, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), false));
        addField(CustomFormConstants.STATUS, dwStatus, getTitle(wfmStrings.status(), false));
        addField(CustomFormConstants.ASSIGNEES, assignee, getTitle(wfmStrings.assignee(), true));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Project;
    }

    @Override
    public void setItems(SelectItem[] items) {
        dwProjectNumber.setItems(items, wfmStrings.number());
        dwProjectName.setItems(items, wfmStrings.name());
        dwDescription.setItems(items, wfmStrings.description());
        dwStartDate.setItems(items, wfmStrings.startDate());
        dwDueDate.setItems(items, wfmStrings.dueDate());
        manager.setItems(items, wfmStrings.manager());
        dwClient.setItems(items, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        dwStatus.setItems(items, wfmStrings.status());
        assignee.setItems(items, wfmStrings.assignees());

        if (tbValues != null && tbValues.length > 0) {
            for (DataListBox dataListBox : tbValues) {
                if (dataListBox != null) {
                    dataListBox.setItems(items);
                }
            }
        }
        LoadingPanel.loading(false);
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    private ImportFile createColumns(ProjectSingleItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.ProjectFields.FIELD_NUMBER, item.getProjectNumberID() != null ? item.getProjectNumberID() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_NAME, item.getNameID() != null ? item.getNameID() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_DESCRIPTION, item.getDescriptionID() != null ? item.getDescriptionID() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_START_DATE, item.getStartDateID() != null ? item.getStartDateID() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_DUE_DATE, item.getDueDateID() != null ? item.getDueDateID() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_MANAGER, item.getManagerId() != null ? item.getManagerId() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_CLIENT, item.getClientId() != null ? item.getClientId() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_STATUS, item.getStatusID() != null ? item.getStatusID() : -1);
        importFile.addColumn(ImportField.ProjectFields.FIELD_ASSIGNEE, item.getAssigneeID() != null ? item.getAssigneeID() : -1);

        if (item.getCustomFields() != null && !item.getCustomFields().isEmpty()) {
            int s = ImportField.ProjectFields.FIELD_CUSTOM_FIELD_START_NUMBER;
            for (CompanyCustomFieldItem customField : item.getCustomFields()) {

                if (customField != null && customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue()) && customField.getFieldStringValue().matches(Constants.REGEX_INTEGER)) {
                    Integer columnID = Integer.parseInt(customField.getFieldStringValue());
                    importFile.addExtraColumn(false,
                            s++,
                            columnID,
                            customField.getDataType(),
                            customField.getColumnCode(),
                            customField.getCustomFieldSettingID() != null ? customField.getCustomFieldSettingID().toString() : "-1",
                            customField.getUiType(),
                            customField.getPredefinedValues() != null ? String.join("-:-", customField.getPredefinedValues()) : null);
                } else {
                    importFile.addExtraColumn(false, s++, null);
                }
            }
        }
        return importFile;
    }

    private ProjectSingleItem getRPC() {
        ProjectSingleItem item = new ProjectSingleItem();
        item.setObjectID(objectId);
        item.setProjectNumberID(getSelectedItem(dwProjectNumber));
        item.setNameID(getSelectedItem(dwProjectName));
        item.setDescriptionID(getSelectedItem(dwDescription));
        item.setStartDateID(getSelectedItem(dwStartDate));
        item.setDueDateID(getSelectedItem(dwDueDate));
        item.setManagerId(getSelectedItem(manager));
        item.setClientId(getSelectedItem(dwClient));
        item.setStatusID(getSelectedItem(dwStatus));
        item.setAssigneeID(getSelectedItem(assignee));

        if (tbValues != null && tbValues.length > 0) {
            ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
            for (int i = 0; i < tbValues.length; i++) {
                CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                resultItem.setObjectId(companyCustomFieldItems.get(i).getObjectId());
                resultItem.setDataType(companyCustomFieldItems.get(i).getDataType());
                resultItem.setUiType(companyCustomFieldItems.get(i).getUiType());
                resultItem.setColumnCode(companyCustomFieldItems.get(i).getColumnCode());
                resultItem.setCustomFieldSettingID(companyCustomFieldItems.get(i).getCustomFieldSettingID());
                resultItem.setPredefinedValues(companyCustomFieldItems.get(i).getPredefinedValues());

                if (tbValues[i].getSelectedItem() != null) {
                    resultItem.setFieldStringValue(tbValues[i].getSelectedItem().getId().toString());
                }
                resultItemList.add(resultItem);
            }
            item.setCustomFields(resultItemList);
        }
        return item;
    }

    public boolean validate() {
        int error = 0;

        if (!Validation.validateListBoxRequired(dwProjectName, new HTML(), "")) {
            error++;
        }
        if (!Validation.validateListBoxRequired(assignee, new HTML(), "")) {
            error++;
        }
        if (!Validation.validateListBoxRequired(manager, new HTML(), "")) {
            error++;
        }
        if (!Validation.validateListBoxRequired(dwStartDate, new HTML(), "")) {
            error++;
        }
        if (!Validation.validateListBoxRequired(dwDueDate, new HTML(), "")) {
            error++;
        }
        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_PROJECT_FORM;
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.PROJECT;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
