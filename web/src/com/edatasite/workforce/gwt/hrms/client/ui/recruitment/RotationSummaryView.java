package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
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
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationTableItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES;

public class RotationSummaryView extends CustomForm2 implements Colapse, Constants {
    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final ListingFilterParameter filterParameters = new ListingFilterParameter();
    private String type;
    private Integer objectId;
    private RotationItem item;
    private DatePicker date;
    private NumberData numberData;
    private Numbering numbering;
    private HTML rotationCode;
    private WfmButton2 draftButton;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private HTML approvers;
    private String statusCode;
    private WfmButton2 submitButton, approveButton, declineButton, editButton;
    private FormHasCustomField customFieldUtil;
    private SplitButton printPdfSplitButton;
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private EditableTable rotationTable;
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();


    public RotationSummaryView(Integer objectId) {
        super("summaryRotation", "rotationSummary");
        this.objectId = objectId;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.RotationList, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                    RotationSummaryView.super.onInitialize();
                }
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

    @Override
    protected void registerFields() {

        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());

        date = new DatePicker();
        date.setWidth("200px");
        date.setDateTimeFormat(DateTimeFormat.getFormat(Utils.getShortDateFormat()));
        addField(CustomFormConstants.DATE, date, getTitle(wfmStrings.date()));

        numbering = new Numbering();
        addField(CustomFormConstants.NUMBER, numbering, wfmStrings.number());

        approvers = initHTML();
        addField("APPROVERS", approvers, wfmStrings.approvers());

        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        drawItemTable();
        show();
    }

    private void drawItemTable() {
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

                rotationTable = new EditableTable(getColumns(columnsMap), true, false);
                rotationTable.setDraggable(true);
                rotationTable.ensureDebugId("Rotation_item_table");
                rotationTable.setWidth("100%");
                addField(INVOLVED_EMPLOYEES, rotationTable, null, false);
            }
        });

    }

    private ColumnConfig[] getColumns(Map<String, ColumnConfigs> columnsMap) {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        int i = 0;
        for (String cc : columnsMap.keySet()) {
            ColumnConfigs columnConfigs = columnsMap.get(cc);
            boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            ColumnConfig columnConfig;

            switch (cc) {
                case ItemTableConstants.EMPLOYEE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.EMPLOYEE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.employee(), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
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
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.NEW_LOCATION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.newLocation(), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
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

    protected void enableFalse() {
        numbering.setEnabled(false);
        date.setEnabled(false);
    }

    private void getTableInfo(RotationTableItem[] rotationTableItems) {
        for (RotationTableItem rotationTableItem : rotationTableItems) {
            rotationTable.addRow(widgets(rotationTableItem));
        }
    }


    private Widget[] widgets(RotationTableItem item) {
        int index = 0;
        ArrayList<Widget> widgets = new ArrayList<>();
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.EMPLOYEE.equals(columnCode)) {
                CustomCellLabel label = new CustomCellLabel(item.getEmployee().getName());
                label.setStyleName("uploadLinkStyle2");
                if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + item.getEmployee().getId(), item.getEmployee().getName(), item.getEmployee().getName()));
                }

                final EmployeeLookUp employeeLookUp = new EmployeeLookUp(true, false);
                employeeLookUp.setValueNotEmptyMeansSelected(true);
                employeeLookUp.setWidth("100%");
                employeeLookUp.setEnabled(false);
                employeeLookUp.addStyleName("lookUp-moveRight");
                if (item.getEmployee() != null) {
                    employeeLookUp.setSelected(item.getEmployee());
                }
                employeeLookUp.setStyleName("uploadLinkStyle2");
                employeeLookUp.addValueChangeHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("employee|add/summary/" + item.getEmployeeId()));

                widgets.add(label);
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
                newLoc.setEnabled(false);
                newLoc.setSelected(item.getNewLocation());
                newLoc.setTitle(columnCode);
                widgets.add(newLoc);
            } else if (ItemTableConstants.NEW_DEPARTMENT.equals(columnCode)) {
                DepartmentLookUp newDep = new DepartmentLookUp();
                newDep.setWidth("100%");
                newDep.setEnabled(false);
                newDep.setSelected(item.getNewDepartment());
                newDep.setTitle(columnCode);
                widgets.add(newDep);
            } else if (ItemTableConstants.NEW_POSITION.equals(columnCode)) {
                PositionLookUp newPos = new PositionLookUp();
                newPos.setWidth("100%");
                newPos.setEnabled(false);
                newPos.setSelected(item.getNewPosition());
                newPos.setTitle(columnCode);
                widgets.add(newPos);
            } else if (itemCFs.containsKey(columnCode)) {

                CompanyCustomFieldItem cfItem = itemCFs.get(columnCode);
                CompanyCustomFieldItem companyCustomFieldItem = setCustomFieldValue(item.getItemCustomFields(), cfItem);

                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextBoxField customTextBoxField = new CustomTextBoxField(companyCustomFieldItem);
                    customTextBoxField.setEnabled(false);
                    widgets.add(customTextBoxField);
                } else if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextAreaField customTextAreaField = new CustomTextAreaField(companyCustomFieldItem);
                    customTextAreaField.hideCharacterLimitPanel();
                    customTextAreaField.setEnabled(false);
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
                    CustomFieldLookUpField customFieldLookUpField = new CustomFieldLookUpField(companyCustomFieldItem);
                    customFieldLookUpField.setEnabled(false);
                    widgets.add(customFieldLookUpField);
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomFieldMultiLookUpField(companyCustomFieldItem));
                }

                if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                    CompanyCustomFieldItem fitem = companyCustomFieldItem;
                    if (fitem != null) {
                        ((CustomFieldInterface) widgets.get(index)).setFieldItem(fitem);
                    }
                }
            }
            index++;


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

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {
        declineButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> save(Constants.ROTATION_REJECTED));
        declineButton.setVisible(false);

        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, clickEvent -> save(ROTATION_APPROVED));
        approveButton.setVisible(false);


        submitButton = addButton(Constants.ROTATION_REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), Constants.BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.ROTATION_SUBMITTED);
        });
        submitButton.setVisible(false);


        editButton = addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/edit/" + item.getId());
        });
        editButton.setVisible(false);

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.HRMS_ROTATION_PDF)) {
            addRightButton(printPdfSplitButton);
        }
    }

    private void save(String statusCode) {
        item.setStatusCode(statusCode);
        LoadingPanel.loading(true);
        HrmsService.App.get().updateRotationApprove(item, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void objectId) {
                closeTab();
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        HrmsService.App.get().getRotationItem(objectId, true, new AbstractAsyncCallback<RotationItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(RotationItem result) {
                item = result;
                objectId = result.getId();
                numbering.setNumberData(result.getNumberData());
                date.setDate(result.getDate().getNonConvertedDate());
                enableFalse();
                if (result.getApproverEmployee() != null) {
                    approvers.setHTML(result.getApproverEmployee().getName());
                }
                if (result.getOverallStatus() != null) {
                    statusCode = result.getOverallStatus().getCode();
                }
                getTableInfo(result.getRotationTableItems());
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems(), true);
                initButtons();
                pdfTool(result);
            }
        });
    }

    public void pdfTool(RotationItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/rotationViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private void initButtons() {
        if (item.isApprover()) {
            Integer currentApproverId = item.getApproverEmployee() != null ? item.getApproverEmployee().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (Constants.ROTATION_SUBMITTED.equals(statusCode) && currentUserId.equals(currentApproverId)) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            }

            editButton.setVisible(
                    Utils.hasPermission(PermissionConstants.HRMS_ROTATION_EDIT)
                            && !(Constants.ROTATION_APPROVED.equals(statusCode)) && (currentUserId.equals(item.getCreator().getId())));


            if (Constants.ROTATION_REJECTED.equals(statusCode) && item.getCreator() != null && currentUserId.equals(item.getCreator().getId())) {
                submitButton.setVisible(true);

            }
        } else {
            editButton.setVisible(Utils.hasPermission(PermissionConstants.HRMS_ROTATION_EDIT)
                    && !(Constants.ROTATION_APPROVED.equals(statusCode)));
        }
    }


    private boolean validation() {
        int error = 0;

        return false;
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


}
