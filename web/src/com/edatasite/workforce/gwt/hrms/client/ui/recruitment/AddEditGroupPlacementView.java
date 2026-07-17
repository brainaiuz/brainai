package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementTableItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES;

public class AddEditGroupPlacementView extends CustomForm2 implements Colapse, Constants {
    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Integer objectId;
    private GroupPlacementItem item;
    private DatePicker date;
    private Numbering numbering;
    private WfmButton2 draftButton;
    private FormHasCustomField customFieldUtil;
    private WfmButton2 approveButton;
    private WfmButton2 submitButton;
    private ChosenApproversWidget approvers;
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private EditableTable placementTable;
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();
    private LinkedHashMap<String, FormProperty> formPropertyMap;


    public AddEditGroupPlacementView() {
        super("addGroupPlacement", "addGroupPlacement ");
    }


    public AddEditGroupPlacementView(Integer objectId) {
        super("addGroupPlacement", "addGroupPlacement ");
        this.objectId = objectId;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyAllCustomFields(ViewName.GroupPlacementItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
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

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.GroupPlacementList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditGroupPlacementView.super.onInitialize();
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
        date.setWidth(MIN_DEFAULT_WIDTH);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null) {
            addField(CustomFormConstants.DATE, date, getTitle(formPropertyMap.get(CustomFormConstants.DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(CustomFormConstants.DATE).isRequired()));
        } else {
            addField(CustomFormConstants.DATE, date, getTitle(wfmStrings.date()));
        }


        numbering = new Numbering();
        numbering.setEnabled(false);


        addField(CustomFormConstants.NUMBER, numbering, wfmStrings.number());

        approvers = new ChosenApproversWidget(RelationItem.TYPE_GROUP_PLACEMENT, objectId);
        approvers.setWidth("200px");
        addField("APPROVERS", approvers, getTitle(wfmStrings.approvers()));
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
            Info.warn(wfmStrings.fillRequiredField());
            return;
        }

        GroupPlacementItem placementItem = new GroupPlacementItem();
        placementItem.setId(objectId);
        placementItem.setNumberData(numbering.getNumberData(false));
        placementItem.setDate(DateTimePicker.getDateTime(date.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));

        placementItem.setPlacementTableItems(getTableItems());
        placementItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        placementItem.setStatusCode(status);
        placementItem.setApprovers(approvers.getChosenApprovers());
        HrmsService.App.get().createGroupPlacement(placementItem, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.placement()));
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GROUP_PLACEMENT_ADD, null, AddEditGroupPlacementView.this);

            }
        });

    }


    public GroupPlacementTableItem[] getTableItems() {
        ArrayList<GroupPlacementTableItem> placementItems = new ArrayList<>();
        for (int i = 0; i < placementTable.getGrid().getRowCount(); i++) {
            GroupPlacementTableItem result = new GroupPlacementTableItem();
            Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();
            if (item != null && item.getPlacementTableItems() != null && item.getPlacementTableItems()[i] != null) {
                result.setObjectID(item.getPlacementTableItems()[i].getObjectID());
            }
            LocationLookUpWithCode location = (LocationLookUpWithCode) placementTable.getColumnById(i, ItemTableConstants.LOCATION);
            if (location != null) {
                result.setLocation(location.getSelectedItem());
            }

            DataListBox type = (DataListBox) placementTable.getColumnById(i, ItemTableConstants.TYPE);
            if (type != null && type.getSelectedItem() != null) {
                result.setType(type.getSelectedItem().getId());
            }

            CRMLookUp candidate = (CRMLookUp) placementTable.getColumnById(i, ItemTableConstants.CANDIDATE);
            if (candidate != null) {
                result.setCandidate(candidate.getSelectedItem());
            }

            DepartmentLookUp deparment = (DepartmentLookUp) placementTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
            if (deparment != null) {
                result.setDepartment(deparment.getSelectedItem());
            }

            PositionLookUp position = (PositionLookUp) placementTable.getColumnById(i, ItemTableConstants.POSITION);
            if (position != null) {
                result.setPosition(position.getSelectedItem());
            }

            CustomFieldLookUp vacancy = (CustomFieldLookUp) placementTable.getColumnById(i, ItemTableConstants.VACANCY);
            if (vacancy != null) {
                result.setMatchedVacancy(vacancy.getSelectedItem());
            }

            ExtendedDatePicker date = (ExtendedDatePicker) placementTable.getColumnById(i, ItemTableConstants.FROM_DATE);
            if (date != null) {
                result.setEffectiveDate(DateTimePicker.getDateTime(date.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
            }

            if (itemCFs != null && !itemCFs.isEmpty()) {
                ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                for (String key : itemCFs.keySet()) {
                    CustomFieldInterface customField = (CustomFieldInterface) placementTable.getColumnById(i, key);

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

            placementItems.add(result);

        }
        return placementItems.toArray(new GroupPlacementTableItem[]{});
    }

    @Override
    protected void getDataToFillFields() {
        HrmsService.App.get().getGroupPlacementItem(objectId, false, new AbstractAsyncCallback<GroupPlacementItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }


            @Override
            public void success(GroupPlacementItem result) {
                item = result;
                if (result != null) {
                    objectId = result.getId();
                    numbering.setNumberData(result.getNumberData());
                    date.setDate(result.getDate() != null ? result.getDate() : null);
                    if (result.getId() != null) {
                        getTableInfo(result.getPlacementTableItems());
                    }
                    getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
                }
                initButtonsPanel();
            }
        });

    }


    private void initButtonsPanel() {
        if (item == null) {
            item = new GroupPlacementItem();
        }
        draftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        draftButton.addClickHandler(event -> {
            save(Constants.GROUP_PLACEMENT_DRAFT);
        });
        addRightButton(draftButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> save(Constants.GROUP_PLACEMENT_APPROVED));
        approveButton.setVisible(false);
        addRightButton(approveButton);


        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> save(Constants.GROUP_PLACEMENT_SUBMITTED));
        submitButton.setVisible(false);
        addRightButton(submitButton);

        approvers = new ChosenApproversWidget(RelationItem.TYPE_GROUP_PLACEMENT, item.getApproverEmployee() != null ? objectId : null);

        if (item.getApprover() != null && item.isApprover()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVERS) != null) {
                addField(CustomFormConstants.APPROVERS, approvers, getTitle(formPropertyMap.get(CustomFormConstants.APPROVERS).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVERS).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.APPROVERS).isRequired()));
                approvers.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVERS).isDisabled());
            } else {
                addField(APPROVERS, approvers, getTitle(wfmStrings.approver(), true));
            }
            if (objectId != null) {
                if (Constants.GROUP_PLACEMENT_DRAFT.equals(item.getStatusCode())) {
                    draftButton.setVisible(true);
                } else if (Constants.GROUP_PLACEMENT_SUBMITTED.equals(item.getStatusCode()) ||
                        Constants.GROUP_PLACEMENT_APPROVED.equals(item.getStatusCode())) {
                    draftButton.setVisible(false);
                }
            } else {
                draftButton.setVisible(true);
            }
        } else {
            approveButton.setVisible(true);
            if (Constants.GROUP_PLACEMENT_SUBMITTED.equals(item.getStatusCode()) ||
                    Constants.GROUP_PLACEMENT_APPROVED.equals(item.getStatusCode())) {
                draftButton.setVisible(false);
            }
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddEditGroupPlacementView.this, (sender, args) -> {
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
        ItemTableSettingService.App.get().getColumnConfigs(ItemTableEnum.GROUP_PLACEMENT_ITEM_TABLE, new AbstractAsyncCallback<ColumnConfigs[]>() {
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

                placementTable = new EditableTable(getColumns(columnsMap), true, true);
                placementTable.setDraggable(true);
                placementTable.setWidth("100%");
                placementTable.setListener(new EditableTableListener() {
                    @Override
                    public void addRow() {
                        placementTable.addRow(widgets(new GroupPlacementTableItem()));
                    }

                    @Override
                    public void removeRow() {

                    }
                });


                placementTable.addRow(widgets(new GroupPlacementTableItem()));
                addField(INVOLVED_EMPLOYEES, placementTable, null);
            }
        });

    }

    private void getTableInfo(GroupPlacementTableItem[] placementTableItems) {
        placementTable.removeAllRows();
        for (GroupPlacementTableItem placementTableItem : placementTableItems) {
            placementTable.addRow(widgets(placementTableItem));
        }
    }


    private Widget[] widgets(GroupPlacementTableItem item) {
        int index1 = 0;
        ArrayList<Widget> widgets = new ArrayList<>();
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.LOCATION.equals(columnCode)) {
                LocationLookUpWithCode locationLookUpWithCode = new LocationLookUpWithCode();
                locationLookUpWithCode.getSuggestBox().addSelectionHandler(e -> {
                    DataListBox type = (DataListBox) placementTable.getColumnById(placementTable.getGrid().getCurrentRow(), ItemTableConstants.TYPE);
                    if (type.getSelectedItem() != null && type.getSelectedId().equals(LookUpConstants.EMPLOYEE_ID)) {
                        LocationLookUpWithCode location = (LocationLookUpWithCode) placementTable.getColumnById(placementTable.getGrid().getCurrentRow(), ItemTableConstants.LOCATION);
                        updateItemTableFilter(LookUpConstants.EMPLOYEE_ID, new CRMLookUp(LookUpConstants.HRMS_EMPLOYEE), location.getSelectedItemID());
                    } else if (type.getSelectedItem() != null && type.getSelectedId().equals(LookUpConstants.CANDIDATE_ID)) {
                        LocationLookUpWithCode location = (LocationLookUpWithCode) placementTable.getColumnById(placementTable.getGrid().getCurrentRow(), ItemTableConstants.LOCATION);
                        CRMLookUp candidate = new CRMLookUp(LookUpConstants.CANDIDATE_ID);
                        candidate.getFilterParametrs().setLocationId(location.getSelectedItemID());
                        placementTable.getGrid().getModel().update(placementTable.getGrid().getCurrentRow(), 3, candidate);
                    }
                });
                locationLookUpWithCode.setValueNotEmptyMeansSelected(true);
                locationLookUpWithCode.setWidth("100%");
                locationLookUpWithCode.addStyleName("lookUp-moveRight");
                locationLookUpWithCode.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getLocation() != null) {
                    locationLookUpWithCode.setSelected(item.getLocation());
                }
                widgets.add(locationLookUpWithCode);
            } else if (ItemTableConstants.TYPE.equals(columnCode)) {
                DataListBox type = new DataListBox();
                type.addValueChangeHandler(e -> {
                    CRMLookUp candidate;
                    LocationLookUpWithCode location = (LocationLookUpWithCode) placementTable.getColumnById(placementTable.getGrid().getCurrentRow(), ItemTableConstants.LOCATION);
                    if (type.getSelectedId() == LookUpConstants.CANDIDATE_ID) {
                        candidate = new CRMLookUp(LookUpConstants.CANDIDATE);
                        candidate.getSuggestBox().addSelectionHandler( event -> setCandidateItems(candidate.getSelectedItemID()));
                        candidate.getFilterParametrs().setLocationId(location.getSelectedItem() != null ? location.getSelectedItemID() : null);
                    } else {
                        candidate = new CRMLookUp(LookUpConstants.HRMS_EMPLOYEE);
                        candidate.getSuggestBox().addSelectionHandler(event -> {
                            updateLocationByEmployee(candidate.getSelectedItemID());
                        });
                    }
                    updateItemTableFilter(type.getSelectedId(), candidate, location.getSelectedItemID());

                });
                type.setItems(getTypes(), wfmStrings.type());
                type.setWidth("100%");
                type.setSelected(item.getType());
                type.setTitle(columnCode);
                widgets.add(type);
            } else if (ItemTableConstants.CANDIDATE.equals(columnCode)) {
                CRMLookUp candidateLookUp;
                if (item.getType() != null) {
                    candidateLookUp = new CRMLookUp(item.getType().equals(LookUpConstants.CANDIDATE_ID) ? LookUpConstants.CANDIDATE : LookUpConstants.HRMS_EMPLOYEE);
                } else {
                    candidateLookUp = new CRMLookUp(LookUpConstants.CANDIDATE_ID);
                }
                candidateLookUp.setWidth("100%");
                candidateLookUp.setSelected(item.getCandidate());
                candidateLookUp.setTitle(columnCode);
                widgets.add(candidateLookUp);
            } else if (ItemTableConstants.DEPARTMENT.equals(columnCode)) {
                DepartmentLookUp dep = new DepartmentLookUp();
                dep.setWidth("100%");
                dep.setSelected(item.getDepartment());
                dep.setTitle(columnCode);
                widgets.add(dep);
            } else if (ItemTableConstants.POSITION.equals(columnCode)) {
                PositionLookUp pos = new PositionLookUp();
                pos.setWidth("100%");
                pos.setSelected(item.getPosition());
                pos.setTitle(columnCode);
                widgets.add(pos);

            } else if (ItemTableConstants.VACANCY.equals(columnCode)) {
                CustomFieldLookUp vacancy = new CustomFieldLookUp(CustomFieldLookUpTypeEnum.VACANCY, null);
                vacancy.setWidth("100%");
                vacancy.setSelected(item.getMatchedVacancy());
                vacancy.setTitle(columnCode);
                widgets.add(vacancy);
            } else if (ItemTableConstants.FROM_DATE.equals(columnCode)) {
                ExtendedDatePicker datePicker = new ExtendedDatePicker();
                datePicker.setDate(item.getEffectiveDate() != null ? item.getEffectiveDate() : null);
                datePicker.setTitle(columnCode);
                widgets.add(datePicker);
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
        int i = 0;
        for (String cc : columnsMap.keySet()) {
            ColumnConfigs columnConfigs = columnsMap.get(cc);
            boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            ColumnConfig columnConfig;
            switch (cc) {
                case ItemTableConstants.LOCATION:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.LOCATION, columnConfigs.isChanged() ? columnConfigs.getTitle() : Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.TYPE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.TYPE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.type(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.CANDIDATE:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CANDIDATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : Property.get(Constants.CANDIDATE, wfmStrings.candidate()), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.DEPARTMENT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.POSITION:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.POSITION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.position(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.VACANCY:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.VACANCY, columnConfigs.isChanged() ? columnConfigs.getTitle() : Property.get(Constants.VACANCY, wfmStrings.vacancy()), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;

                case ItemTableConstants.FROM_DATE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.FROM_DATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.effectiveDate(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
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
        for (int i = 0; i < placementTable.getGrid().getRowCount(); i++) {
            CRMLookUp employee = (CRMLookUp) placementTable.getColumnById(i, ItemTableConstants.CANDIDATE);
            DataListBox type = (DataListBox) placementTable.getColumnById(i, ItemTableConstants.TYPE);
            DepartmentLookUp department = (DepartmentLookUp) placementTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
            PositionLookUp position = (PositionLookUp) placementTable.getColumnById(i, ItemTableConstants.POSITION);
            CustomFieldLookUp vacancy = (CustomFieldLookUp) placementTable.getColumnById(i, ItemTableConstants.VACANCY);
            ExtendedDatePicker date = (ExtendedDatePicker) placementTable.getColumnById(i, ItemTableConstants.FROM_DATE);
            LocationLookUpWithCode location = (LocationLookUpWithCode) placementTable.getColumnById(i, ItemTableConstants.LOCATION);
            if (i != 0 && employee.getSelectedItem() == null && type.getSelectedItem() == null && department.getSelectedItem() == null &&
                    position.getSelectedItem() == null && vacancy.getSelectedItem() == null && date.getDate() == null && location.getSelectedItem() == null) {
                placementTable.getGrid().removeRow(i);
                continue;
            }
            if (type.getSelectedItem() == null) {
                placementTable.notValid(i, ItemTableConstants.TYPE);
                error++;
            }
            if (department.getSelectedItem() == null && position.getSelectedItem() == null && vacancy.getSelectedItem() == null) {
                placementTable.notValid(i, ItemTableConstants.VACANCY);
                placementTable.notValid(i, ItemTableConstants.DEPARTMENT);
                placementTable.notValid(i, ItemTableConstants.POSITION);
                error++;
            }
            if (date.getDate() == null) {
                placementTable.notValid(i, ItemTableConstants.FROM_DATE);
                error++;
            }
            if (location.getSelectedItem() == null) {
                placementTable.notValid(i, ItemTableConstants.LOCATION);
                error++;
            }
            if (employee.getSelectedItem() == null) {
                placementTable.notValid(i, ItemTableConstants.EMPLOYEE);
                error++;
            }
            if (this.date.getDate() == null) {
                this.date.addStyleName(ERROR_FORM_STYLE);
                error++;
            }

        }

        return error == 0;
    }

    private void updateItemTableFilter(Integer type, CRMLookUp candidate, Integer locationId) {
        DepartmentLookUp department = new DepartmentLookUp();
        if (type.equals(LookUpConstants.EMPLOYEE_ID)) {
            candidate.getFilterParametrs().setLocationId(locationId);
            department.getFilterParametrs().setLocationId(locationId);
        }
        placementTable.getGrid().getModel().update(placementTable.getGrid().getCurrentRow(), 3, candidate);
        placementTable.getGrid().getModel().update(placementTable.getGrid().getCurrentRow(), 4, department);
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.GROUP_PLACEMENT_FORM;
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

    private SelectItem[] getTypes() {
        return new SelectItem[]{
                new SelectItem(LookUpConstants.CANDIDATE_ID, wfmStrings.candidate()),
                new SelectItem(LookUpConstants.EMPLOYEE_ID, wfmStrings.employee())
        };
    }

    private void updateLocationByEmployee(Integer employeeId) {
        HrmsService.App.get().getLocationByEmployeeId(employeeId, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem result) {
                LocationLookUpWithCode location = new LocationLookUpWithCode();
                if (result != null) {
                    location.setSelected(result);
                }
                updateItemTableFilter(LookUpConstants.EMPLOYEE_ID, (CRMLookUp) placementTable.getColumnById(placementTable.getGrid().getCurrentRow(), ItemTableConstants.CANDIDATE), location.getSelectedItemID());
                placementTable.getGrid().getModel().update(placementTable.getGrid().getCurrentRow(), 1, location);
            }
        });
    }

    private void setCandidateItems(Integer candidateId){
        HrmsService.App.get().getCandidateItems(candidateId, new AsyncCallback<GroupPlacementItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(GroupPlacementItem result) {
            DepartmentLookUp departmentLookUp = new DepartmentLookUp();
            PositionLookUp positionLookUp = new PositionLookUp();
            departmentLookUp.setSelected(result.getDepartment());
            positionLookUp.setSelected(result.getPosition());
            placementTable.getGrid().getModel().update(placementTable.getGrid().getCurrentRow(), 4, departmentLookUp);
            placementTable.getGrid().getModel().update(placementTable.getGrid().getCurrentRow(), 5, positionLookUp);
            }
        });
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
}
