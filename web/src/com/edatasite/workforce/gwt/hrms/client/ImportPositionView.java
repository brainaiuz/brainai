package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT;
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT_POSITION_FORM;

public class ImportPositionView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    private DataListBox number;
    private DataListBox position;
    private DataListBox location;
    private DataListBox department;
    private final String type;
    private DataListBox placeCount;
    private DataListBox types;
    private DataListBox coeffitcient;


    public ImportPositionView(Integer ObjectId, String type) {
        super("importpositionadd", "Import Position");
        this.objectId = ObjectId;
        this.type = type;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        String importpositionview = "import_position_view_";

        number = new DataListBox();
        number.ensureDebugId(importpositionview + "number");
        number.addStyleName(DEFAULT_WIDTH);

        position = new DataListBox();
        position.ensureDebugId(importpositionview + "position");
        position.addStyleName(DEFAULT_WIDTH);

        location = new DataListBox();
        location.ensureDebugId(importpositionview + "location");
        location.addStyleName(DEFAULT_WIDTH);

        department = new DataListBox();
        department.ensureDebugId(importpositionview + "department");
        department.addStyleName(DEFAULT_WIDTH);

        placeCount = new DataListBox();
        placeCount.ensureDebugId(importpositionview + "placeCount");
        placeCount.addStyleName(DEFAULT_WIDTH);

        types = new DataListBox();
        types.ensureDebugId(importpositionview + "placeCount");
        types.addStyleName(DEFAULT_WIDTH);

        coeffitcient = new DataListBox();
        coeffitcient.ensureDebugId(importpositionview + "placeCount");
        coeffitcient.addStyleName(DEFAULT_WIDTH);

    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        addField(POSITION, position, getTitle(wfmStrings.position()));
        addField(CustomFormConstants.PROJECT.LOCATION, location, getTitle(wfmStrings.location()));
        addField(CustomFormConstants.DEPARTMENT, department, getTitle(wfmStrings.department()));
        addField(CustomFormConstants.COUNT, placeCount, getTitle(wfmStrings.vacantPlaceCount()));
        addField(CustomFormConstants.TYPE, types, getTitle(wfmStrings.type()));
        addField(CustomFormConstants.POSITIONS.COEFFICENT, coeffitcient, getTitle(wfmStrings.coefficent()));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Positions;
    }

    @Override
    public void setItems(SelectItem[] items) {
        number.setItems(items, wfmStrings.number());
        position.setItems(items, wfmStrings.position());
        location.setItems(items, wfmStrings.location());
        department.setItems(items, wfmStrings.department());
        placeCount.setItems(items, wfmStrings.vacantPlaceCount());
        types.setItems(items, wfmStrings.type());
        coeffitcient.setItems(items, wfmStrings.coefficent());
    }

    private ImportFile createColumns(PositionItem item) {
        ImportFile importFile = new ImportFile();
        if (item != null) {
            importFile.addColumn(ImportField.PositionFields.FIELD_NUMBER, item.getCodeId() != null ? item.getCodeId() : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_POSITION, item.getPositionId() != null ? item.getPositionId() : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_POSITION_AR, item.getPositionId() != null ? item.getPositionId() + 1 : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_POSITION_RU, item.getPositionId() != null ? item.getPositionId() + 2 : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_POSITION_UZ, item.getPositionId() != null ? item.getPositionId() + 3 : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_LOCATION, item.getLocationId() != null ? item.getLocationId() : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_DEPARTMENT, item.getDepartmentId() != null ? item.getDepartmentId() : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_PLACE_COUNT, item.getVacantCountId() != null ? item.getVacantCountId() : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_TYPE, item.getTypeId() != null ? item.getTypeId() : -1);
            importFile.addColumn(ImportField.PositionFields.FIELD_COEFFICENT, item.getCoefficentId() != null ? item.getCoefficentId() : -1);

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

    private PositionItem getRPC() {
        PositionItem positionItem = new PositionItem();
        positionItem.setId(objectId);
        positionItem.setCodeId(getSelectedItem(number));
        positionItem.setPositionId(getSelectedItem(position));
        positionItem.setLocationId(getSelectedItem(location));
        positionItem.setDepartmentId(getSelectedItem(department));
        positionItem.setVacantCountId(getSelectedItem(placeCount));
        positionItem.setTypeId(getSelectedItem(types));
        positionItem.setCoefficentId(getSelectedItem(coeffitcient));

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
            positionItem.setCustomFieldItems(resultItemList);
        }
        return positionItem;
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.POSITION;
    }

    @Override
    protected String getFormID() {
        return IMPORT_POSITION_FORM;
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
