package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationTableItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.datamodel.Editable;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES;

public class AddEditRotationView extends CustomForm2 implements Colapse, Constants {

    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer objectId;
    private RotationItem item;
    private DatePicker date;
    private Integer emplooyeeId;

    private Numbering numbering;
    private WfmButton2 draftButton;
    private FormHasCustomField customFieldUtil;
    private WfmButton2 approveButton;
    private WfmButton2 submitButton;
    private ChosenApproversWidget approvers;
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private EditableTable rotationTable;
    private Integer prefilledEmployeeId;
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();

    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public AddEditRotationView() {
        super("addRotation", "addRotation ");
    }


    public AddEditRotationView(Integer objectId) {
        super("addRotation", "addRotation ");
        this.objectId = objectId;
    }
    public AddEditRotationView(Integer objectId, Integer prefilledEmployeeId) {
        super("addRotation", "addRotation ");
        this.objectId = objectId;
        this.prefilledEmployeeId = prefilledEmployeeId;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyAllCustomFields(ViewName.RotationItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    for (CompanyCustomFieldItem item : result) {
                        itemCFs.put(item.getColumnCode(), item);
                    }
                }
            }
        });

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.RotationList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditRotationView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void registerFields() {
        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());

        date = new DatePicker();
        date.addStyleName(DEFAULT_WIDTH);
        date.setDateTimeFormat(DateTimeFormat.getFormat(Utils.getShortDateFormat()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null) {
            GWT.log("date");
            addField(CustomFormConstants.DATE, date, getTitle(formPropertyMap.get(CustomFormConstants.DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(CustomFormConstants.DATE).isRequired()));
        } else {
            addField(CustomFormConstants.DATE, date, getTitle(wfmStrings.date()));
        }


        numbering = new Numbering();
        numbering.setEnabled(false);
        addField(CustomFormConstants.NUMBER, numbering, wfmStrings.number());

        approvers = new ChosenApproversWidget(RelationItem.TYPE_ROTATION, objectId);
        if (approvers.getApproversSize() > 0) {
            addField(APPROVERS, approvers, getTitle(wfmStrings.approvers()));
        }
        approvers.addStyleName(MAX_DEFAULT_WIDTH);
        getCustomFieldUtil().drawCustomFields(this, objectId, false);

        drawItemTable();
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {

    }

    private void save(String status) {
        if (!validation(status)) {
            enableButton(true);
            Info.warn(wfmStrings.fillRequiredField());
            return;
        }

        RotationItem rotationItem = new RotationItem();
        rotationItem.setId(objectId);
        rotationItem.setNumberData(numbering.getNumberData(false));
        rotationItem.setDate(new DateNonConvertable(date.getDate()));
        rotationItem.setRotationTableItems(getTableItems());
        rotationItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        rotationItem.setStatusCode(status);
        rotationItem.setApprovers(approvers.getChosenApprovers());
        hireDateValidation(rotationItem);
    }
    private void hireDateValidation(RotationItem rotationItem) {
        HrmsService.App.get().hireDateValidation(rotationItem, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                enableButton(true);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(String result) {
                enableButton(true);

                if (result == null || result.isEmpty()) {
                    checkPositionAvailability(rotationItem);
                    return;
                }

                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK);
                messageBox.setWidth(520);
                messageBox.setTitle(wfmStrings.hireDateValidationFailed());
                messageBox.setMessage(result);
                messageBox.open();


            }

        });
    }



    public RotationTableItem[] getTableItems() {
        ArrayList<RotationTableItem> rotationsItems = new ArrayList<>();
        for (int i = 0; i < rotationTable.getGrid().getRowCount(); i++) {
            EmployeeLookUp employeeLookUp = null;
            RotationTableItem result = new RotationTableItem();
            Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();
            if (item != null && item.getRotationTableItems() != null && item.getRotationTableItems()[i] != null) {
                result.setItemId(item.getRotationTableItems()[i].getItemId());
            }

            LocationLookUpWithCode curLocation = (LocationLookUpWithCode) rotationTable.getColumnById(i, ItemTableConstants.CURRENT_LOCATION);
            if (curLocation != null) {
                result.setCurrentLocation(curLocation.getSelectedItem());
            }

            LocationLookUpWithCode newLocation = (LocationLookUpWithCode) rotationTable.getColumnById(i, ItemTableConstants.NEW_LOCATION);
            if (newLocation != null) {
                result.setNewLocation(newLocation.getSelectedItem());
            }

            DepartmentLookUp currentDepartmet = (DepartmentLookUp) rotationTable.getColumnById(i, ItemTableConstants.CURRENT_DEPARTMENT);
            if (currentDepartmet != null) {
                result.setCurrentDepartment(currentDepartmet.getSelectedItem());
            }

            DepartmentLookUp newDepartmet = (DepartmentLookUp) rotationTable.getColumnById(i, ItemTableConstants.NEW_DEPARTMENT);
            if (newDepartmet != null) {
                result.setNewDepartment(newDepartmet.getSelectedItem());
            }

            PositionLookUp currentPosition = (PositionLookUp) rotationTable.getColumnById(i, ItemTableConstants.CURRENT_POSIITON);
            if (currentPosition != null) {
                result.setCurrentPosition(currentPosition.getSelectedItem());
            }

            PositionLookUp newPosition = (PositionLookUp) rotationTable.getColumnById(i, ItemTableConstants.NEW_POSITION);
            if (newPosition != null) {
                result.setNewPosition(newPosition.getSelectedItem());
            }


            employeeLookUp = (EmployeeLookUp) rotationTable.getColumnById(i, ItemTableConstants.EMPLOYEE);
            if (employeeLookUp.getSelectedItemID() != null && employeeLookUp.getSelectedItem().getName().equals(employeeLookUp.getSuggestBox().getText())) {

                if (itemCFs != null && !itemCFs.isEmpty()) {
                    ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                    for (String key : itemCFs.keySet()) {
                        CustomFieldInterface customField = (CustomFieldInterface) rotationTable.getColumnById(i, key);

                        if (customField != null) {
                            final CompanyCustomFieldItem companyCustomFieldItem = customField.getFieldItem();
                            final CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                            resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                            resultItem.setDataType(companyCustomFieldItem.getDataType());
                            resultItem.setUiType(companyCustomFieldItem.getUiType());
                            resultItem.setColumnCode(key);
                            resultItem.setFieldName(companyCustomFieldItem.getFieldName());
                            resultItem.setAliasName(companyCustomFieldItem.getAliasName());
                            resultItem.setFileUploadFieldId(companyCustomFieldItem.getFileUploadFieldId());
                            resultItem.setUiType(companyCustomFieldItem.getUiType());
                            resultItem.setLookUpTypeEnum(companyCustomFieldItem.getLookUpTypeEnum());
                            resultItem.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            resultItem.setSelectedId(companyCustomFieldItem.getSelectedId());
                            resultItem.setReferenceItem(customField.getFieldItem().getReferenceItem());
                            resultItem.setFieldDateNonConvertedValue(customField.getFieldItem().getFieldDateNonConvertedValue());

                            fieldItems.add(resultItem);
                        } else if (itemCFsValues.size() > 0 && itemCFsValues.get(key) != null && itemCFsValues.get(key).getUiType() != null) {
                            fieldItems.add(itemCFsValues.get(key));
                        }
                    }
                    if (!fieldItems.isEmpty()) {
                        result.setItemCustomFields(fieldItems);
                    }
                }
                result.setEmployee(employeeLookUp.getSelectedItem());
                rotationsItems.add(result);
            }
        }

        return rotationsItems.toArray(new RotationTableItem[]{});
    }

    @Override
    protected void getDataToFillFields() {
        HrmsService.App.get().getRotationItem(objectId, false, new AbstractAsyncCallback<RotationItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }


            @Override
            public void success(RotationItem result) {
                item = result;
                if (result != null) {
                    objectId = result.getId();
                    numbering.setNumberData(result.getNumberData());
                    date.setDate(result.getDate() != null ? result.getDate().getNonConvertedDate() : null);
                    if (result.getId() != null) {
                        getTableInfo(result.getRotationTableItems());
                    }
                    getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
                }
                initButtonsPanel();
            }
        });

    }

    private void checkPositionAvailability(RotationItem rotationItem) {
        List<Integer> positionIds = Arrays.stream(getTableItems())
                .map(RotationTableItem::getNewPosition)
                .filter(Objects::nonNull)
                .map(SelectItem::getId)
                .collect(Collectors.toList());
        rotationItem.setDate(date.getDateAsNonConvertable());
        if (positionIds.isEmpty() || objectId != null) {
            createRotation(rotationItem);
            return;
        }
        HrmsService.App.get().checkPositionAvailability((ArrayList<Integer>) positionIds, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                enableButton(true);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    createRotation(rotationItem);
                } else {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    Info.warn(wfmStrings.position() + " " + wfmStrings.isAlreadySelected() + "");
                }
            }
        });
    }


    private void initButtonsPanel() {
        if (item == null) {
            item = new RotationItem();
        }
        draftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        draftButton.addClickHandler(event -> {
            enableButton(false);
            save(Constants.ROTATION_DRAFT);
        });
        addRightButton(draftButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> {
            enableButton(false);
            save(Constants.ROTATION_APPROVED);
        });
        approveButton.setVisible(false);
        addRightButton(approveButton);


        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> {
            enableButton(false);
            save(Constants.ROTATION_SUBMITTED);
        });
        submitButton.setVisible(false);
        addRightButton(submitButton);

        approvers = new ChosenApproversWidget(RelationItem.TYPE_ROTATION, item.getApproverEmployee() != null ? objectId : null, emplooyeeId != null ? emplooyeeId : null);

        if (item.getApprover() != null && item.isApprover()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVERS) != null) {
                addField(CustomFormConstants.APPROVERS, approvers, getTitle(formPropertyMap.get(CustomFormConstants.APPROVERS).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVERS).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.APPROVERS).isRequired()));
                approvers.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVERS).isDisabled());
            } else {
                addField(APPROVERS, approvers, getTitle(wfmStrings.approver(), true));
            }
            if (objectId != null) {
                if (Constants.ROTATION_DRAFT.equals(item.getStatusCode())) {
                    draftButton.setVisible(true);
                } else if (Constants.ROTATION_SUBMITTED.equals(item.getStatusCode()) ||
                        Constants.ROTATION_APPROVED.equals(item.getStatusCode())) {
                    draftButton.setVisible(false);
                }
            } else {
                draftButton.setVisible(true);
            }
        } else {
            submitButton.setVisible(true);
            if (Constants.ROTATION_SUBMITTED.equals(item.getStatusCode()) ||
                    Constants.ROTATION_APPROVED.equals(item.getStatusCode())) {
                draftButton.setVisible(false);
            }
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddEditRotationView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    if (itemId != null && Utils.getUserID().equals(itemId)) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        approveButton.setVisible(false);
                    }
                });
                if (approveButton != null && submitButton != null && approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
        });
    }

    private void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(ItemTableEnum.ROTATION_ITEM_TABLE, new AbstractAsyncCallback<ColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ColumnConfigs[] result) {


                if (result != null) {
                    for (ColumnConfigs cc : result) {
                        if (cc.isSelected()) {
                            columnsMap.put(cc.getCode(), cc);
                        }
                    }
                }

                rotationTable = new EditableTable(getColumns(columnsMap), true, true);
                rotationTable.setDraggable(true);
                rotationTable.ensureDebugId("Rotation_item_table");
                rotationTable.setWidth("100%");
                rotationTable.setListener(new EditableTableListener() {
                    @Override
                    public void addRow() {
                        rotationTable.addRow(widgets(new RotationTableItem()));
                    }

                    @Override
                    public void removeRow() {

                    }
                });


                if (prefilledEmployeeId != null) {
                    fetchEmployeeInfoForPrefill(prefilledEmployeeId);
                } else {
                    for (int i = 0; i < 3; i++) {
                        rotationTable.addRow(widgets(new RotationTableItem()));
                    }
                }

                addField(INVOLVED_EMPLOYEES, rotationTable, null);
            }
        });

    }


    private void fetchEmployeeInfoForPrefill(Integer employeeId) {
        LoadingPanel.loading(true);

        HrmsService.App.get().getEmployeeDataForRotation(employeeId, new AbstractAsyncCallback<RotationTableItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
                for (int i = 0; i < 3; i++) {
                    rotationTable.addRow(widgets(new RotationTableItem()));
                }
            }

            @Override
            public void success(RotationTableItem result) {
                LoadingPanel.loading(false);
                rotationTable.removeAllRows();

                if (result != null) {
                    rotationTable.addRow(widgets(result));

                    for (int i = 0; i < 2; i++) {
                        rotationTable.addRow(widgets(new RotationTableItem()));
                    }
                } else {
                    for (int i = 0; i < 3; i++) {
                        rotationTable.addRow(widgets(new RotationTableItem()));
                    }
                }
            }
        });
    }

    private void getTableInfo(RotationTableItem[] rotationTableItems) {
        rotationTable.removeAllRows();
        for (RotationTableItem rotationTableItem : rotationTableItems) {
            rotationTable.addRow(widgets(rotationTableItem));
        }
    }


    private Widget[] widgets(RotationTableItem item) {
        int index1 = 0;
        ArrayList<Widget> widgets = new ArrayList<>();
        for (String columnCode : columnsMap.keySet()) {
            ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
            if (ItemTableConstants.EMPLOYEE.equals(columnCode)) {
                final EmployeeLookUp employeeLookUp = new EmployeeLookUp(true, false);
                employeeLookUp.setValueNotEmptyMeansSelected(true);
                employeeLookUp.setWidth("100%");
                employeeLookUp.addStyleName("lookUp-moveRight");
                employeeLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getEmployee() != null) {
                    employeeLookUp.setSelected(item.getEmployee());
                    emplooyeeId = item.getEmployee().getId();
                }
                employeeLookUp.getSuggestBox().addSelectionHandler(event -> {
                    int currentRow = rotationTable.getGrid().getCurrentRow();
                    Editable model = rotationTable.getGrid().getModel();
                    if (currentRow == 0) {
                        approvers.reloadApproverWidgets(RelationItem.TYPE_ROTATION, objectId, employeeLookUp.getSelectedItemID());
                    }

                    HrmsService.App.get().getDepartmentAndPositionForRotation(employeeLookUp.getSelectedItemID(), new AbstractAsyncCallback<RotationItem>() {
                        public void success(RotationItem result) {

                            LocationLookUpWithCode locationLookUp = (LocationLookUpWithCode) rotationTable.getColumnById(currentRow, ItemTableConstants.CURRENT_LOCATION);
                            DepartmentLookUp departmentLookUp = (DepartmentLookUp) rotationTable.getColumnById(currentRow, ItemTableConstants.CURRENT_DEPARTMENT);
                            PositionLookUp positionLookUp = (PositionLookUp) rotationTable.getColumnById(currentRow, ItemTableConstants.CURRENT_POSIITON);
                            LocationLookUpWithCode newlocationLookUp = (LocationLookUpWithCode) rotationTable.getColumnById(currentRow, ItemTableConstants.NEW_LOCATION);

                            departmentLookUp.setSelected(result.getCurDepartment());
                            positionLookUp.setSelected(result.getCurPosition());
                            locationLookUp.setSelected(result.getCurLocation());
                            newlocationLookUp.setSelected(result.getCurLocation());

                            DepartmentLookUp newDepartment = new DepartmentLookUp();
                            if (result.getNewLocation() != null) {
                                newDepartment = new DepartmentLookUp(result.getNewLocation().getId());
                                model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.NEW_DEPARTMENT), newDepartment);
                            } else if (result.getCurLocation() != null) {
                                newDepartment = new DepartmentLookUp(result.getCurLocation().getId());
                                model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.NEW_DEPARTMENT), newDepartment);
                            }
                            LocationLookUpWithCode newLoc = newlocationLookUp;
                            newlocationLookUp.getSuggestBox().addSelectionHandler(event -> {
                                DepartmentLookUp newDep = new DepartmentLookUp(newLoc.getSelectedItemID());
                                model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.NEW_DEPARTMENT), newDep);

                                listingFilterParameter.setLocationId(newLoc.getSelectedItemID());
                                PositionLookUp newPosition = new PositionLookUp();
                                newPosition.setFilterParametrs(listingFilterParameter);
                                model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.NEW_POSITION), newPosition);
                            });

                            if (newlocationLookUp != null && newlocationLookUp.getSelectedItemID() != null) {
                                listingFilterParameter.setLocationId(newlocationLookUp.getSelectedItemID());
                            }
                            DepartmentLookUp newDep = newDepartment;
                            newDepartment.getSuggestBox().addSelectionHandler(event -> {
                                listingFilterParameter.setDepartmentId(newDep.getSelectedItemID());
                                PositionLookUp newPosition = new PositionLookUp();
                                newPosition.setFilterParametrs(listingFilterParameter);
                                model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.NEW_POSITION), newPosition);
                            });

                            PositionLookUp newPosition = new PositionLookUp();
                            newPosition.setFilterParametrs(listingFilterParameter);

                            model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.CURRENT_DEPARTMENT), departmentLookUp);
                            model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.CURRENT_POSIITON), positionLookUp);
                            model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.CURRENT_LOCATION), locationLookUp);
                            model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.NEW_LOCATION), newlocationLookUp);
                            model.update(currentRow, rotationTable.getColumnId(ItemTableConstants.NEW_POSITION), newPosition);

                            for (String key : itemCFs.keySet()) {
                                if (result.getEmployeeCustomFields().containsKey(itemCFs.get(key).getAliasName())) {
                                    CompanyCustomFieldItem cfItem = itemCFs.get(key);
                                    CompanyCustomFieldItem companyCustomFieldItem = setCustomFieldValue(result.getCustomFieldItems(), cfItem);
                                    CustomFieldInterface wid = (CustomFieldInterface) rotationTable.getColumnById(rotationTable.getGrid().getCurrentRow(), key);
                                    wid.setFieldItem(companyCustomFieldItem);
                                    model.update(rotationTable.getGrid().getCurrentRow(), rotationTable.getColumnId(key), wid);
                                }

                            }
                        }
                    });
                });
                widgets.add(employeeLookUp);
            } else if (ItemTableConstants.CURRENT_LOCATION.equals(columnCode)) {
                LocationLookUpWithCode curentLoc = new LocationLookUpWithCode();
                curentLoc.setWidth("100%");
                curentLoc.setEnabled(false);
                curentLoc.setSelected(item.getCurrentLocation());
                curentLoc.setTitle(columnCode);
                widgets.add(curentLoc);
            } else if (ItemTableConstants.CURRENT_DEPARTMENT.equals(columnCode)) {
                DepartmentLookUp curentDep = new DepartmentLookUp();
                curentDep.setWidth("100%");
                curentDep.setEnabled(false);
                curentDep.setSelected(item.getCurrentDepartment());
                curentDep.setTitle(columnCode);
                widgets.add(curentDep);
            } else if (ItemTableConstants.CURRENT_POSIITON.equals(columnCode)) {
                PositionLookUp curentPos = new PositionLookUp();
                curentPos.setWidth("100%");
                curentPos.setEnabled(false);
                curentPos.setSelected(item.getCurrentPosition());
                curentPos.setTitle(columnCode);
                widgets.add(curentPos);
            } else if (ItemTableConstants.NEW_LOCATION.equals(columnCode)) {
                LocationLookUpWithCode newLoc = new LocationLookUpWithCode();
                newLoc.setWidth("100%");
                newLoc.setSelected(item.getNewLocation());
                newLoc.setTitle(columnCode);
                widgets.add(newLoc);
            } else if (ItemTableConstants.NEW_DEPARTMENT.equals(columnCode)) {
                DepartmentLookUp newDep = new DepartmentLookUp();
                newDep.setWidth("100%");
                newDep.setSelected(item.getNewDepartment());
                newDep.setTitle(columnCode);
                widgets.add(newDep);
            } else if (ItemTableConstants.NEW_POSITION.equals(columnCode)) {
                PositionLookUp newPos = new PositionLookUp();
                newPos.setWidth("100%");
                newPos.setSelected(item.getNewPosition());
                newPos.setTitle(columnCode);
                widgets.add(newPos);
            } else if (itemCFs.containsKey(columnCode)) {

                CompanyCustomFieldItem cfItem = itemCFs.get(columnCode);
                CompanyCustomFieldItem companyCustomFieldItem = setCustomFieldValue(item.getItemCustomFields(), cfItem);

                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomTextBoxField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextAreaField customTextAreaField = new CustomTextAreaField(companyCustomFieldItem);
                    customTextAreaField.hideCharacterLimitPanel();
                    Validation.addAutoResizeListenerToTextArea(customTextAreaField.getTextArea());
                    widgets.add(customTextAreaField);
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomPercentageField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomDropDownField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomDatePicker(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomDateTime(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomFieldLookUpField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomFieldMultiLookUpField(companyCustomFieldItem));
                }

                if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                    CompanyCustomFieldItem fitem = companyCustomFieldItem;
                    if (fitem != null) {
                        ((CustomFieldInterface) widgets.get(index1)).setFieldItem(fitem);
                    }
                }
            }
            index1++;


        }
        return widgets.toArray(new Widget[]{});
    }


    private CompanyCustomFieldItem setCustomFieldValue(ArrayList<CompanyCustomFieldItem> itemCustomFields, CompanyCustomFieldItem cfItem) {
        if (itemCustomFields != null && itemCustomFields.size() > 0) {
            for (CompanyCustomFieldItem customFieldItem : itemCustomFields) {
                if (customFieldItem.getDataType().equals(cfItem.getDataType()) && customFieldItem.getUiType().equals(cfItem.getUiType()) && customFieldItem.getAliasName().equals(cfItem.getAliasName())) {
                    return customFieldItem;
                }
            }
        }
        return cfItem;
    }


    private ColumnConfig[] getColumns(Map<String, ColumnConfigs> columnsMap) {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        for (String cc : columnsMap.keySet()) {
            ColumnConfigs columnConfigs = columnsMap.get(cc);
            boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            ColumnConfig columnConfig;
            switch (cc) {
                case ItemTableConstants.EMPLOYEE:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.EMPLOYEE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.employee(), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.CURRENT_LOCATION:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CURRENT_LOCATION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.location(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.CURRENT_DEPARTMENT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CURRENT_DEPARTMENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.department(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.CURRENT_POSIITON:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CURRENT_POSIITON, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.position(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.NEW_LOCATION:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.NEW_LOCATION, columnConfigs.isChanged() ? columnConfigs.getTitle() : "new Location", Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.NEW_DEPARTMENT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.NEW_DEPARTMENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : hrmsStrings.newDepartment(), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.NEW_POSITION:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.NEW_POSITION, columnConfigs.isChanged() ? columnConfigs.getTitle() : Utils.textFormat(wfmStrings.addNew(), wfmStrings.position()), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                default:
                    columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), 100, columnsMap.get(cc).isRequired(), true);
                    if (columnsMap.get(cc).getWidth() != null && columnsMap.get(cc).getWidth() > 0) {
                        columnConfig.setWidth(columnsMap.get(cc).getWidth());
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                    }
                    if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(columnsMap.get(cc).getUiType())) {
                        columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                        columns.add(columnConfig);

                        columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode() + "_DESCRIPTION", wfmStrings.description(), columnsMap.get(cc).getWidth() * 40 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                        columns.add(columnConfig);
                    } else if (UI_TYPE_LOOKUP.equals(columnsMap.get(cc).getUiType())) {
                        columnConfig = new ColumnConfig(LookUpCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                        columns.add(columnConfig);
                    } else {
                        columns.add(columnConfig);
                    }
                    break;
            }
        }
        return columns.toArray(new ColumnConfig[]{});
    }

    private boolean validation(String status) {
        int error = 0;
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null && formPropertyMap.get(CustomFormConstants.DATE).isRequired()) {
            error += markAsError(date, !Validation.validateDate(date));
        }

        error += getCustomFieldUtil().validateCustomFields();

        EditableGrid grid = rotationTable.getGrid();
        ArrayList<Integer> selectedGroup = new ArrayList<>();
        selectedGroup.clear();
        for (int i = 0; i < grid.getRowCount(); i++) {
            EmployeeLookUp employeeLookUp = (EmployeeLookUp) rotationTable.getColumnById(i, ItemTableConstants.EMPLOYEE);
            PositionLookUp positionLookUp = (PositionLookUp) rotationTable.getColumnById(i, ItemTableConstants.NEW_POSITION);
            DepartmentLookUp departmentLookUp = (DepartmentLookUp) rotationTable.getColumnById(i, ItemTableConstants.NEW_DEPARTMENT);
            if (selectedGroup.contains(employeeLookUp.getSelectedItemID())) {
                Info.warn(employeeLookUp.getSelectedItem().getName() + " " + wfmStrings.isAlreadySelected());
                employeeLookUp.addStyleName(ERROR_FORM_STYLE);
                error++;
            } else if (employeeLookUp.getSelectedItem() == null && i == 0) {
                error++;
                rotationTable.addStyleName(ERROR_FORM_STYLE);
            } else if (employeeLookUp.getSelectedItem() == null) {
                grid.removeRow(i);
            } else {
                selectedGroup.add(employeeLookUp.getSelectedItemID());
            }

        }

        return error == 0;
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.ROTATION_FORM;
    }

    @Override
    protected String getFormType() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private void createRotation(RotationItem rotationItem) {
        HrmsService.App.get().createRotation(rotationItem, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                enableButton(true);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), hrmsStrings.rotations()));
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ROTATION_ADD, null, AddEditRotationView.this);

            }
        });
    }

}
