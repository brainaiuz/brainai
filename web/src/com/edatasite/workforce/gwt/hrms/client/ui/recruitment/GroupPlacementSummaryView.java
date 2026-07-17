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
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES;


public class GroupPlacementSummaryView extends CustomForm2 implements Colapse, Constants {

    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final ListingFilterParameter filterParameters = new ListingFilterParameter();
    private Integer objectId;
    private GroupPlacementItem item;
    private DatePicker date;
    private WfmButton2 draftButton;
    private HTML approvers;
    private String statusCode;
    private WfmButton2 submitButton, approveButton, declineButton, editButton;
    private FormHasCustomField customFieldUtil;
    private SplitButton printPdfSplitButton;
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private EditableTable placementTable;
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();


    public GroupPlacementSummaryView(Integer objectId) {
        super("summaryGroupPlacement", "groupPlacementSummary");
        this.objectId = objectId;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.GroupPlacementList, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                    GroupPlacementSummaryView.super.onInitialize();
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
        date.setWidth(MIN_DEFAULT_WIDTH);
        date.addStyleName(DEFAULT_WIDTH);
        addField(CustomFormConstants.DATE, date, getTitle(wfmStrings.date()));

        approvers = initHTML();
        addField("APPROVERS", approvers, wfmStrings.approvers());

        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        drawItemTable();
        show();
    }

    private void drawItemTable() {
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

                placementTable = new EditableTable(getColumns(columnsMap), false);
                placementTable.setWidth("100%");
                addField(INVOLVED_EMPLOYEES, placementTable, null, false);
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
                case ItemTableConstants.LOCATION:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.LOCATION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.location(), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
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
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CANDIDATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.candidate(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.DEPARTMENT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.department(), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
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
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.VACANCY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.vacancy(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
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

    private void getTableInfo(GroupPlacementTableItem[] placementTableItems) {
        for (GroupPlacementTableItem placementTableItem : placementTableItems) {
            placementTable.addRow(widgets(placementTableItem));
        }
    }


    private Widget[] widgets(GroupPlacementTableItem item) {
        int index1 = 0;
        ArrayList<Widget> widgets = new ArrayList<>();
        final Integer[] selectedType = {null};
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.LOCATION.equals(columnCode)) {
                LocationLookUpWithCode locationLookUpWithCode = new LocationLookUpWithCode();
                locationLookUpWithCode.setEnabled(false);
                locationLookUpWithCode.setValueNotEmptyMeansSelected(true);
                locationLookUpWithCode.setWidth("100%");
                locationLookUpWithCode.addStyleName("lookUp-moveRight");
                if (item.getLocation() != null) {
                    locationLookUpWithCode.setSelected(item.getLocation());
                }
                widgets.add(locationLookUpWithCode);
            } else if (ItemTableConstants.TYPE.equals(columnCode)) {
                DataListBox type = new DataListBox();
                type.setEnabled(false);
                type.addValueChangeHandler(e -> {
                    selectedType[0] = type.getSelectedId();
                });
                type.setItems(getTypes(), wfmStrings.type());
                type.setWidth("100%");
                type.setSelected(item.getType());
                type.setTitle(columnCode);
                widgets.add(type);
            } else if (ItemTableConstants.CANDIDATE.equals(columnCode)) {
                CRMLookUp candidate = new CRMLookUp(LookUpConstants.HRMS_EMPLOYEE);
                candidate.setEnabled(false);
                candidate.setWidth("100%");
                candidate.setSelected(item.getCandidate());
                candidate.setTitle(columnCode);
                widgets.add(candidate);
            } else if (ItemTableConstants.DEPARTMENT.equals(columnCode)) {
                DepartmentLookUp dep = new DepartmentLookUp();
                dep.setEnabled(false);
                dep.setWidth("100%");
                dep.setSelected(item.getDepartment());
                dep.setTitle(columnCode);
                widgets.add(dep);
            } else if (ItemTableConstants.POSITION.equals(columnCode)) {
                PositionLookUp pos = new PositionLookUp();
                pos.setEnabled(false);
                pos.setWidth("100%");
                pos.setSelected(item.getPosition());
                pos.setTitle(columnCode);
                widgets.add(pos);

            } else if (ItemTableConstants.VACANCY.equals(columnCode)) {
                CustomFieldLookUp vacancy = new CustomFieldLookUp(CustomFieldLookUpTypeEnum.VACANCY, null);
                vacancy.setEnabled(false);
                vacancy.setWidth("100%");
                vacancy.setSelected(item.getMatchedVacancy());
                vacancy.setTitle(columnCode);
                widgets.add(vacancy);
            } else if (ItemTableConstants.FROM_DATE.equals(columnCode)) {
                ExtendedDatePicker datePicker = new ExtendedDatePicker();
                datePicker.setEnabled(false);
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

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {
        declineButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> save(Constants.GROUP_PLACEMENT_REJECTED));
        declineButton.setVisible(false);

        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, clickEvent -> save(GROUP_PLACEMENT_APPROVED));
        approveButton.setVisible(false);


        submitButton = addButton(Constants.GROUP_PLACEMENT_REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), Constants.BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.GROUP_PLACEMENT_SUBMITTED);
        });
        submitButton.setVisible(false);


        editButton = addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("groupPlacement|add/edit/" + item.getId());
        });
        editButton.setVisible(false);

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.HRMS_GROUP_PLACEMENT_PDF)) {
            addRightButton(printPdfSplitButton);
        }
    }

    private void save(String statusCode) {
        item.setStatusCode(statusCode);
        LoadingPanel.loading(true);
        HrmsService.App.get().updateGroupPlacementApprove(item, new AsyncCallback<Void>() {
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
        HrmsService.App.get().getGroupPlacementItem(objectId, true, new AbstractAsyncCallback<GroupPlacementItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(GroupPlacementItem result) {
                item = result;
                objectId = result.getId();
                date.setDate(result.getDate());
                date.setEnabled(false);
                if (result.getApproverEmployee() != null) {
                    approvers.setHTML(result.getApproverEmployee().getName());
                }
                if (result.getOverallStatus() != null) {
                    statusCode = result.getOverallStatus().getCode();
                }
                getTableInfo(result.getPlacementTableItems());

                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems(), true);
                initButtons();
                pdfTool(result);
            }
        });
    }

    public void pdfTool(GroupPlacementItem result) {
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
        String pdfURL = CommandConstants.PDF_URL + "/groupPlacementViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private void initButtons() {
        if (item.isApprover()) {
            Integer currentApproverId = item.getApproverEmployee() != null ? item.getApproverEmployee().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (Constants.GROUP_PLACEMENT_SUBMITTED.equals(statusCode) && currentUserId.equals(currentApproverId)) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            }

            editButton.setVisible(
                    Utils.hasPermission(PermissionConstants.HRMS_GROUP_PLACEMENT_EDIT)
                            && !(Constants.ROTATION_APPROVED.equals(statusCode)) && (currentUserId.equals(item.getCreator().getId())));


            if (Constants.GROUP_PLACEMENT_REJECTED.equals(statusCode) && item.getCreator() != null && currentUserId.equals(item.getCreator().getId())) {
                submitButton.setVisible(true);

            }
        } else {
            editButton.setVisible(Utils.hasPermission(PermissionConstants.HRMS_GROUP_PLACEMENT_EDIT)
                    && !(Constants.GROUP_PLACEMENT_APPROVED.equals(statusCode)));
        }
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
}
