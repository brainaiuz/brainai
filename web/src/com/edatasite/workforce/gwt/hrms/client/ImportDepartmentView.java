package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT;
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT_DEPARTMENT_FORM;

public class ImportDepartmentView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {
    private DataListBox number;
    private DataListBox name;
    private DataListBox location;
    private DataListBox parent;
    private DataListBox startDate;
    private DataListBox status;


    public ImportDepartmentView(Integer ObjectId) {
        super("importdepartmentadd", "Import Department");
        this.objectId = ObjectId;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {

        number = new DataListBox();
        number.addStyleName(DEFAULT_WIDTH);

        name = new DataListBox();
        name.addStyleName(DEFAULT_WIDTH);

        location = new DataListBox();
        location.addStyleName(DEFAULT_WIDTH);

        parent = new DataListBox();
        parent.addStyleName(DEFAULT_WIDTH);

        startDate = new DataListBox();
        startDate.addStyleName(DEFAULT_WIDTH);

        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        addField(NAME, name, getTitle(wfmStrings.name()));
        addField(CustomFormConstants.PROJECT.LOCATION, location, getTitle(wfmStrings.location()));
        addField(PARENT, parent, getTitle(wfmStrings.parent()));
        addField(CustomFormConstants.START_DATE, startDate, getTitle(wfmStrings.startDate()));
        addField(CustomFormConstants.STATUS, status, getTitle(wfmStrings.status()));
    }


    private ImportFile createColumns(TeamListItem item) {
        ImportFile importFile = new ImportFile();
        if (item != null) {
            importFile.addColumn(ImportField.DepartmentFields.FIELD_NUMBER, item.getNumberCid() != null ? item.getNumberCid() : -1);
            importFile.addColumn(ImportField.DepartmentFields.FIELD_NAME, item.getTeamCid() != null ? item.getTeamCid() : -1);
            importFile.addColumn(ImportField.DepartmentFields.FILED_LOCATION, item.getLocationCid() != null ? item.getLocationCid() : -1);
            importFile.addColumn(ImportField.DepartmentFields.FILED_PARENT, item.getParentCid() != null ? item.getParentCid() : -1);
            importFile.addColumn(ImportField.DepartmentFields.FIELD_START_DATE, item.getStartDateCid() != null ? item.getStartDateCid() : -1);
            importFile.addColumn(ImportField.DepartmentFields.FIELD_STATUS, item.getStatusCid() != null ? item.getStatusCid() : -1);

            importFile.addColumn(ImportField.DepartmentFields.FIELD_DEPARTMENT_AR, item.getTeamCid() != null ? item.getTeamCid() + 5 : -1);
            importFile.addColumn(ImportField.DepartmentFields.FIELD_DEPARTMENT_RU, item.getTeamCid() != null ? item.getTeamCid() + 6 : -1);
            importFile.addColumn(ImportField.DepartmentFields.FIELD_DEPARTMENT_UZ, item.getTeamCid() != null ? item.getTeamCid() + 7 : -1);
            importFile.addColumn(ImportField.DepartmentFields.FIELD_DEPARTMENT_EN, item.getTeamCid() != null ? item.getTeamCid() + 8 : -1);

            if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                int s = ImportField.PositionFields.FIELD_CUSTOM_FIELD_START_NUMBER;
                for (CompanyCustomFieldItem customField : item.getCustomFieldItems()) {
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

        }
        return importFile;
    }


    private TeamListItem getRPC() {
        TeamListItem departmentItem = new TeamListItem();
        departmentItem.setObjectID(objectId);
        departmentItem.setTeamCid(getSelectedItem(name));
        departmentItem.setLocationCid(getSelectedItem(location));
        departmentItem.setParentCid(getSelectedItem(parent));
        departmentItem.setNumberCid(getSelectedItem(number));
        departmentItem.setStartDateCid(getSelectedItem(startDate));
        departmentItem.setStatusCid(getSelectedItem(status));

        if (tbValues != null && tbValues.length > 0) {
            ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
            for (int i = 0; i < tbValues.length; i++) {
                CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                resultItem.setObjectId(companyCustomFieldItems.get(i).getObjectId());
                resultItem.setDataType(companyCustomFieldItems.get(i).getDataType());
                resultItem.setColumnCode(companyCustomFieldItems.get(i).getColumnCode());
                resultItem.setCustomFieldSettingID(companyCustomFieldItems.get(i).getCustomFieldSettingID());
                resultItem.setPredefinedValues(companyCustomFieldItems.get(i).getPredefinedValues());
                resultItem.setUiType(companyCustomFieldItems.get(i).getUiType());
                if (tbValues[i].getSelectedItem() != null) {
                    resultItem.setFieldStringValue(tbValues[i].getSelectedItem().getId().toString());
                }
                resultItemList.add(resultItem);
            }
            departmentItem.setCustomFieldItems(resultItemList);
        }
        return departmentItem;
    }

    @Override
    public void setItems(SelectItem[] items) {
        number.setItems(items, wfmStrings.number());
        name.setItems(items, wfmStrings.name());
        location.setItems(items, wfmStrings.location());
        parent.setItems(items, wfmStrings.parent());
        startDate.setItems(items, wfmStrings.startDate());
        status.setItems(items, wfmStrings.status());


    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Department;
    }


    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.DEPARTMENT;
    }

    @Override
    protected String getFormID() {
        return IMPORT_DEPARTMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return IMPORT;
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
