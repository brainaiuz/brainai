package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AdditionalPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.AdditionalPaymentLeaveRequestWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.UL;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentCategoryItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollBatchLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollCategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollDepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollLocationLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment.AdditionalPaymentItemModal;
import com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment.PaymentCalculationSideNavBox;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AdditionalPaymentAddEditViewV2 extends CustomForm2 implements Colapse, Constants, FormHasCustomFieldInterface {


    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private final HashMap<Integer, BigDecimal> empModeMap = new HashMap<>();
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();
    ArrayList<PaymentDeductionSelectItem> selectedCategories = new ArrayList<>();
    private Div categoriesList;
    private final List<PaymentCategoryItem> allowanceItems = new ArrayList<>();

    private final String GROUP_TYPE = "group";
    private final String EMPLOYEE_TYPE = "employee";
    private final String DEPARTMENT_TYPE = "department";
    private final String LOCATION_TYPE = "location";
    private final String SUPERVISOR_TYPE = "supervisor";

    private final SelectItem FIXED_AMOUNT = new SelectItem(1, wfmStrings.fixedAmount(), "FIXED_AMOUNT");
    private final SelectItem BASIC_SALARY = new SelectItem(2, wfmStrings.basicSalary(), "BASIC_SALARY");
    private final SelectItem BASIC_SALARY_ALLOWANCE = new SelectItem(3, wfmStrings.basicAllowancePay(), "BASIC_SALARY_ALLOWANCE");

    private final String type;
    private String entityType;
    private CurrencyItem currency;
    private KpiCheckBox showInPayslip;
    private KpiCheckBox calculateByLastMonth;

    private Integer objectId;
    private boolean isCopyView = false;
    private AdditionalPayment data;
    private WfmButton2 draftButton, submitButton, approveButton;
    private SplitButton pdfButton;
    private MaterialLink exportExl;

    private TextBox referenceTextBox;
    private ChosenApproversWidget approver;
    private LookUp lookUp;
    private DataListBox month, year, paymentType;
    private DatePicker defaultDateWidget;
    private Anchor basicAllowanceLabel;
    private KpiModal categoriesDialogBox;
    private TextBox fixedAmount;
    private FormGroup fixedAmountFormGroup;
    private PayrollCategoryLookUp categoryForAll;

    private EditableTable paymentsTable;
    private EditableGrid paymentsTableGrid;
    private EditableTable paymentsTableAllowance;
    private EditableGrid paymentsTableAllowanceGrid;
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private NoteHistoryWidget noteHistoryWidget;
    private AdditionalPaymentItemModal additionalPaymentItemModal;
    private MultiSelectEmployeeLookUp multiSelectLookUpForEmployeeType;
    private PaymentCalculationSideNavBox calculationSideNavBox;
    private FormHasCustomField customFieldUtil;

    private DataListBox tableLimitListBox;
    private TextBox tableCurrentBox;
    private Integer totalTableItems = 0;
    private Integer tableCurrent = 0;
    Integer tableStart = 0;
    private MaterialLink tablePagingResult;
    private TextBox tableSearchBox;
    private final HashMap<String, PaymentDeductionObject> changedItemMap = new HashMap<>();
    private final HashMap<String, Boolean> deletedItemMap = new HashMap<>();
    private FooterUploadPanel footerUploadPanel;
    private final HashMap<Integer, PaymentDeductionObject> existingItems = new HashMap<>();

    public AdditionalPaymentAddEditViewV2(String type) {
        super("addAdditionalPayment");
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        this.type = type;
    }

    public AdditionalPaymentAddEditViewV2(String type, String entityType) {
        super("addAdditionalPayment");
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        this.type = type;
        this.entityType = entityType;
    }

    public AdditionalPaymentAddEditViewV2(Integer objectId, String type, String entityType, boolean isCopyView) {
        super("editAdditionalPayment");
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        this.objectId = objectId;
        this.type = type;
        this.entityType = entityType;
        this.isCopyView = isCopyView;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.AdditionalPayment, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        initSearchPanel();
        initPaginationWidgets();
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {
        noteHistoryWidget = new NoteHistoryWidget(callback -> PayrollService.App.get().loadPaymentNotes(objectId, callback));

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");

        footer.addToLeftSide(informer);
        footerUploadPanel = new FooterUploadPanel(Constants.F_ADDITIONAL_PAYMENT, objectId, true);
        footerUploadPanel.setWidth("10%");
        if (Utils.hasPermission(PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT)) {
            footer.addToLeftSide(footerUploadPanel);
        }
    }

    @Override
    protected void getDataToFillFields() {
        drawForm();
        LoadingPanel.loading(true);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
        LoadingPanel.loading(true);
        fp.setStart(Optional.ofNullable(tableStart).orElse(0));
        fp.setLimit(Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20));
        fp.setSearchKey(tableSearchBox.getText());
        PayrollService.App.get().getAdditionalPaymentData(fp, new AsyncCallback<AdditionalPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AdditionalPayment result) {
                LoadingPanel.loading(false);
                data = result;
                data.setOldStatusCode(data.getStatusCode());
                initButtonsPanel();
                drawTableSection();

                if (objectId == null) {
                    paymentType.setSelected(FIXED_AMOUNT);
                    onChangePaymentType(true, false);

                    setDefaultValues();
                    setDefaultValuesByFormProperty();

                    if (data.getDepartmentList() != null && data.getDepartmentList().length == 1 && DEPARTMENT_TYPE.equals(entityType)) {
                        lookUp.addItem(data.getDepartmentList()[0]);
                        lookUp.setSelected(data.getDepartmentList()[0].getId());
                        loadTableData(true);
                    }
                    pdfButton.addItemList(Collections.singletonList(getExcelPrintSplitButton()));
                } else {
                    categoryForAll.addCategoryItem(data.getDefaultCategory());
                    List<SplitButtonItem> pdfItems = new ArrayList<>();

                    Integer defaultTemplateId = null;
                    if (data.getPdfTemplateList() != null && data.getPdfTemplateList().getItems() != null && data.getPdfTemplateList().getItems().length > 0) {
                        for (SelectItem pdfItem : data.getPdfTemplateList().getItems()) {

                            if (pdfItem.isDefaultSelected()) {
                                defaultTemplateId = pdfItem.getId();
                            }
                            pdfItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(pdfItem.getId())));
                        }
                    } else {
                        pdfItems.add(new SplitButtonItem("PDF_TEMPLATE_LANDSCAPE", wfmStrings.landscape(), () -> generatePDF(true), true));
                    }
                    Integer finalDefaultTemplateId = defaultTemplateId;
                    SplitButtonItem pdfVersion = new SplitButtonItem("PDF_VERSION", wfmStrings.print(), () -> generatePDF(finalDefaultTemplateId), true);
                    pdfItems.add(getExcelPrintSplitButton());
                    pdfItems.add(pdfVersion);
                    pdfButton.addItemList(pdfItems);

                    if (data.getDefaultDate() != null) {
                        defaultDateWidget.setDate(data.getDefaultDate().getNonConvertedDate());
                    }
                    if (data.getEmployee() != null || EMPLOYEE_TYPE.equals(entityType)) {

                        List<SelectItem> selectItems = new ArrayList<>();
                        if (result.getItems() != null && result.getItems().size() > 0) {
                            for (PaymentDeductionObject object : result.getItems()) {
                                selectItems.add(object.getEmployee());
                            }
                        }
                        multiSelectLookUpForEmployeeType.setSelectedItems(selectItems);
                    } else if (data.getDepartment() != null) {
                        lookUp.setSelected(data.getDepartment());
                    } else if (data.getLocation() != null) {
                        lookUp.setSelected(data.getLocation());
                    } else if (data.getSupervisor() != null) {
                        lookUp.setSelected(data.getSupervisor());
                    } else {
                        lookUp.setSelected(data.getPayrollBatch());
                    }
                    if (PAYMENT_STATUS_DRAFT.equals(data.getStatusCode())) {
                        if (multiSelectLookUpForEmployeeType != null) {
                            multiSelectLookUpForEmployeeType.setEnabled(true);
                        }
                        if (lookUp != null) {
                            lookUp.setEnabled(false);
                        }
                    }

                    currency = data.getCurrency();
                    referenceTextBox.setText(data.getReference());
                    month.setSelected(data.getMonthID());
                    year.setSelected(data.getYear());
                    showInPayslip.setValue(data.isShowInPayslip());

                    onChangePaymentType(false, true);
                    setPaginationData(data.getTotalItems());
                    if (data.getItems() != null && data.getItems().size() > 0) {
                        data.getItems().stream().filter(i -> i.getEmployee() != null).forEach(i -> existingItems.put(i.getEmployee().getId(), i));
                        for (PaymentDeductionObject item : data.getItems()) {
                            if (isAdvancedType()) {
                                paymentsTableAllowance.addRow(getWidgets(item));
                            } else {
                                paymentsTable.addRow(getWidgets(item));
                            }
                        }
                    }
                    if (result.getLeaveRequestId() != null) {
                        disableAllFields();
                    }

                    if (data.getCalculationDetails() != null && data.getCalculationDetails().size() > 0) {
                        calculationSideNavBox = new PaymentCalculationSideNavBox(data.getCalculationDetails(), data.getEmployeeDataDetail());
                        FooterInformer calculationHistory = new FooterInformer(SvgEnum.messageSquare, wfmStrings.calculationDetails());
                        calculationHistory.setWidth("15%");
                        calculationHistory.setInitialClasses("informer-item history-notes-container");
                        calculationHistory.addClickHandler(click -> calculationSideNavBox.show());

                        footer.addToLeftSide(calculationHistory);
                    }
                }
                if (result.getLeaveRequestId() != null || (result.getFromId() != null && result.getFromType() != null)) {
                    FooterInformer link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
                    link.setBadgeCount(1);

                    link.addClickHandler(event -> new AdditionalPaymentLeaveRequestWidget(result.getFromObject(), result.getFromType(), result.getLeaveRequestId() != null).show());
                    footer.addToLeftSide(link);
                }
                if (data.getCustomFields() != null && data.getCustomFields().size() > 0) {
                    getCustomFieldUtil().fillCustomFieldsWithData(data.getCustomFields());
                }

            }
        });
    }

    private SplitButtonItem getExcelPrintSplitButton() {
        exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.xlsCaption());
        exportExl.addClickHandler((event) -> {
            excelVersion(exportPanel, true);
        });
        SplitButtonItem splitButtonItem = new SplitButtonItem("XLS_VERSION", wfmStrings.excel(), () -> excelVersion(exportPanel, false), true);
        return splitButtonItem;
    }

    private void excelVersion(MaterialPanel hp, boolean fromSummary) {
        ListingFilterParameter filter = new ListingFilterParameter();

        filter.setObjectId(objectId);
        filter.setViewType(fromSummary ? Constants.VIEW : Constants.ADD);
        String excelURL = CommandConstants.COMMON_URL + "/additionalPaymentAddEditExcelHandler";
        Utils.sendPDFOrExcelRequest(hp, excelURL, getExcelDataAsMap(), "_blank");
    }

    private HashMap<String, String> getExcelDataAsMap() {
        AdditionalPayment payment = getFormData(PAYMENT_STATUS_DRAFT);
        payment.setObjectID(objectId);
        payment.setCreator(new SelectItem(1, Utils.getUserFullName()));
        if (approver.getFirstApproverLookUp() != null && approver.getFirstApproverLookUp().getSelectedItem() != null && payment.getCurrentApproverAsSelectItem() == null) {
            payment.setApprover(approver.getFirstApproverLookUp().getSelectedItem());
        }
        payment.setEntityType(entityType);
        HashMap<String, String> map = payment.getValueMapForExcel();

        if (GROUP_TYPE.equals(entityType)) {
            map.put(AdditionalPayment.GROUP_TYPE_ID, String.valueOf(lookUp.getSelectedItemID()));
        }
//        List<PaymentDeductionObject> paymentDeductionObjectList = getItems();
        JSONArray jsonArray = new JSONArray();
        AtomicInteger i = new AtomicInteger(0);
        changedItemMap.values().forEach(item -> {
            JSONObject jsonObject = new JSONObject();
            item.getExcelValuesAsMap().forEach((key, val) -> {
                jsonObject.put(key, new JSONString(val));
            });
            jsonArray.set(i.getAndIncrement(), jsonObject);
        });
        StringBuilder deletedItemsID = new StringBuilder();
        if (deletedItemMap != null && deletedItemMap.size() > 0) {
            deletedItemMap.forEach((k, v) -> {
                if (v) {
                    deletedItemsID.append(k).append(",");
                }
            });
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put(AdditionalPayment.UPDATED_TABLE_ITEMS, jsonArray);
        resultJson.put(AdditionalPayment.DELETED_TABLE_ITEMS, new JSONString(deletedItemsID.toString()));
        map.put(AdditionalPayment.ADDITIONAL_PAYMENT_ITEMTABLE_DATA, resultJson.toString());
        if (defaultDateWidget.getDate() != null) {
            map.put(AdditionalPayment.DEFAULT_DATE, DateUtils.format(defaultDateWidget.getDate(), DateUtils.dateFormatShort1));
        }
        map.put(AdditionalPayment.IS_BASIC_SALARY, String.valueOf(isBasicSalaryType()));

        return map;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void disableAllFields() {
        if (multiSelectLookUpForEmployeeType != null) {
            multiSelectLookUpForEmployeeType.setEnabled(false);
        }
        if (lookUp != null) {
            lookUp.setEnabled(false);
        }
        if (defaultDateWidget != null) {
            defaultDateWidget.setEnabled(true);
        }
        if (month != null) {
            month.setEnabled(true);
        }
        if (year != null) {
            year.setEnabled(true);
        }
        if (paymentType != null) {
            paymentType.setEnabled(false);
        }
        if (categoryForAll != null) {
            categoryForAll.setEnabled(false);
        }
        if (referenceTextBox != null) {
            referenceTextBox.setEnabled(false);
        }
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE) != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE).getDefaultValue() != null) {
            if (EMPLOYEE_TYPE.equals(entityType)) {
                multiSelectLookUpForEmployeeType.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.EMPLOYEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.EMPLOYEE).getDefaultValue()));
            } else {
                lookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.EMPLOYEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.EMPLOYEE).getDefaultValue()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE) != null && formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                defaultDateWidget.setDate(currentDate);
            } else {
                try {
                    defaultDateWidget.setDate(DateUtils.parse(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(PERIOD) != null && formPropertyMap.get(PERIOD).getDefaultValue() != null) {
            month.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PERIOD).getSelectedId(), formPropertyMap.get(CustomFormConstants.PERIOD).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE) != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).getSelectedId() != null) {
            Integer paymentTypeId = formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).getSelectedId();
            paymentType.setSelected(paymentTypeId == 1 ? FIXED_AMOUNT : paymentTypeId == 2 ? BASIC_SALARY : paymentTypeId == 3 ? BASIC_SALARY_ALLOWANCE : null);
            onChangePaymentType(true, false);
        }

        if (formPropertyMap != null && formPropertyMap.get(AMOUNT) != null && formPropertyMap.get(AMOUNT).getDefaultValue() != null) {
            fixedAmount.setText(formPropertyMap.get(AMOUNT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).getSelectedId() != null && formPropertyMap.get(CATEGORY).getDefaultValue() != null) {
            PaymentDeductionSelectItem selectItem = new PaymentDeductionSelectItem();
            selectItem.setId(formPropertyMap.get(CATEGORY).getSelectedId());
            selectItem.setName(formPropertyMap.get(CATEGORY).getDefaultValue());
            categoryForAll.addCategoryItem(selectItem);
        }

        if (formPropertyMap != null && formPropertyMap.get(REFERENCE) != null && formPropertyMap.get(REFERENCE).getDefaultValue() != null) {
            referenceTextBox.setText(formPropertyMap.get(REFERENCE).getDefaultValue());
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ADDITIONAL_PAYMENT_FORM;
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

    private void drawForm() {
        drawMainSection();
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId);
    }

    private void drawMainSection() {

        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());

        String name = "";

        if (EMPLOYEE_TYPE.equals(entityType)) {
            multiSelectLookUpForEmployeeType = new MultiSelectEmployeeLookUp();
            multiSelectLookUpForEmployeeType.getFilterParametrs().setHRMS(true);
            name = formPropertyMap != null && formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.employee();
        } else if (DEPARTMENT_TYPE.equals(entityType)) {
            lookUp = new PayrollDepartmentLookUp();
            name = formPropertyMap != null && formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.department();
        } else if (LOCATION_TYPE.equals(entityType)) {
            lookUp = new PayrollLocationLookUp();
            name = formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (GROUP_TYPE.equals(entityType)) {
            lookUp = new PayrollBatchLookUp();
            name = formPropertyMap != null && formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.group();
        } else if (SUPERVISOR_TYPE.equals(entityType)) {
            lookUp = new PayrollBatchLookUp();
            name = formPropertyMap != null && formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.supervisor();
        }
        if (EMPLOYEE_TYPE.equals(entityType)) {
            multiSelectLookUpForEmployeeType.setRemoveActionCommand(() -> {
                tableStart = 0;
                tableCurrent = 0;
                totalTableItems = 0;
                changedItemMap.clear();
                deletedItemMap.clear();
                loadTableData(false);
            });
            multiSelectLookUpForEmployeeType.getSuggestBox().addSelectionHandler(selectionEvent -> {
                tableStart = 0;
                tableCurrent = 0;
                totalTableItems = 0;
                changedItemMap.clear();
                deletedItemMap.clear();
                loadTableData(false);
            });
        } else if (lookUp != null) {
            lookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                tableStart = 0;
                tableCurrent = 0;
                totalTableItems = 0;
                changedItemMap.clear();
                deletedItemMap.clear();
                loadTableData(true);
            });
            lookUp.showClearButton();
            lookUp.setClearCommand(() -> {
                //            paymentsTable.removeAllRows();
            });
        }

        if (formPropertyMap != null && formPropertyMap.get(EMPLOYEE) != null) {
            if (EMPLOYEE_TYPE.equals(entityType)) {
                addField(EMPLOYEE, multiSelectLookUpForEmployeeType, getTitle(name, formPropertyMap.get(EMPLOYEE).isRequired()));
                multiSelectLookUpForEmployeeType.setEnabled(!formPropertyMap.get(EMPLOYEE).isDisabled());
            } else if (lookUp != null) {
                addField(EMPLOYEE, lookUp, getTitle(name, formPropertyMap.get(EMPLOYEE).isRequired()));
                lookUp.setEnabled(!formPropertyMap.get(EMPLOYEE).isDisabled());
            }
        } else {
            if (EMPLOYEE_TYPE.equals(entityType)) {
                addField(EMPLOYEE, multiSelectLookUpForEmployeeType, getTitle(name, true));
            } else {
                addField(EMPLOYEE, lookUp, getTitle(name, true));
            }
        }

        defaultDateWidget = new DatePicker();
        defaultDateWidget.addChangeHandler(event -> applyDefaultDate());

        if (formPropertyMap != null && formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE) != null) {
            addField(AdditionalPaymentImport.PAYMENT_DATE, defaultDateWidget, getTitle(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).isChanged() ? formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getTitle() : wfmStrings.paymentDate(), formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).isRequired()));
            defaultDateWidget.setEnabled(!formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).isDisabled());
        } else {
            addField(AdditionalPaymentImport.PAYMENT_DATE, defaultDateWidget, getTitle(wfmStrings.paymentDate()));
        }

        month = new DataListBox();
        setMonthItems();
        month.setChangeEvent(this::onChangeMonthEndYear);

        year = new DataListBox();
        year.setWithoutNullLabel(true);
        setYearItems();
        year.setSelected(Integer.valueOf(format_year.format(new Date())));
        year.setChangeEvent(this::onChangeMonthEndYear);

        if (formPropertyMap != null && formPropertyMap.get(PERIOD) != null) {
            addField(PERIOD, new InputGroup(month, year), getTitle(formPropertyMap.get(PERIOD).isChanged() ? formPropertyMap.get(PERIOD).getTitle() : wfmStrings.period(), formPropertyMap.get(PERIOD).isRequired()));
            month.setEnabled(!formPropertyMap.get(PERIOD).isDisabled());
            year.setEnabled(!formPropertyMap.get(PERIOD).isDisabled());
        } else {
            addField(PERIOD, new InputGroup(month, year), getTitle(wfmStrings.period(), true));
        }

        SelectItem PLEASE_SELECT = new SelectItem(-1, "Please Select ", "PLEASE_SELECT");
        SelectItem[] selectItems = new SelectItem[]{PLEASE_SELECT, FIXED_AMOUNT, BASIC_SALARY, BASIC_SALARY_ALLOWANCE};

        paymentType = new DataListBox();
        paymentType.setWithoutNullLabel(true);
        paymentType.setItems(selectItems);
        paymentType.setChangeEvent(() -> {
            tableStart = 0;
            tableCurrent = 0;
            totalTableItems = 0;
            changedItemMap.clear();
            deletedItemMap.clear();
            onChangePaymentType(true, false);
        });

        FormGroup amountTypeBox = new FormGroup();
        amountTypeBox.setLabel(formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE) != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).getTitle() : wfmStrings.paymentType(), formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE) != null && formPropertyMap.get(ACCOUNTING.PAYMENT_TYPE).isRequired());
        amountTypeBox.setContent(paymentType);

        basicAllowanceLabel = new Anchor(wfmStrings.basicAllowancePay());
        basicAllowanceLabel.setVisible(false);
        basicAllowanceLabel.getElement().getStyle().setProperty("color", "#1071e3");
        basicAllowanceLabel.getElement().getStyle().setProperty("cursor", "pointer");
        basicAllowanceLabel.getElement().getStyle().setProperty("paddingLeft", "5px");
        basicAllowanceLabel.addClickHandler(clickEvent -> {
            categoriesDialogBox.open();
        });
        amountTypeBox.getGroupLabel().add(basicAllowanceLabel);
        createBasicAllowancePopUp();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE) != null) {
            addField(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE, amountTypeBox, null);
            paymentType.setEnabled(!formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE, amountTypeBox, null);
        }

        fixedAmount = new TextBox();
        Validation.addNumericKeyboardListener(fixedAmount, 2, false);
        fixedAmount.addValueChangeHandler(changeEvent -> applyPercentage());
        fixedAmountFormGroup = new FormGroup();
        fixedAmountFormGroup.setVisible(false);
        fixedAmountFormGroup.setContent(fixedAmount);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AMOUNT) != null) {
            addField(CustomFormConstants.AMOUNT, fixedAmountFormGroup, null);
            fixedAmount.setEnabled(!formPropertyMap.get(CustomFormConstants.AMOUNT).isDisabled());
        } else {
            addField(CustomFormConstants.AMOUNT, fixedAmountFormGroup, null);
        }

        categoryForAll = new PayrollCategoryLookUp(type);
        categoryForAll.setWidth("100%");
        categoryForAll.getSuggestBox().addSelectionHandler(selectionEvent -> applyDefaultCategory());

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, categoryForAll, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CATEGORY).isRequired()));
            categoryForAll.setEnabled(!formPropertyMap.get(CATEGORY).isDisabled());
        } else {
            addField(CATEGORY, categoryForAll, getTitle(wfmStrings.category()));
        }

        referenceTextBox = new TextBox();

        if (formPropertyMap != null && formPropertyMap.get(REFERENCE) != null) {
            addField(REFERENCE, referenceTextBox, getTitle(formPropertyMap.get(REFERENCE).isChanged() ? formPropertyMap.get(REFERENCE).getTitle() : wfmStrings.reference(), formPropertyMap.get(REFERENCE).isRequired()));
            referenceTextBox.setEnabled(!formPropertyMap.get(REFERENCE).isDisabled());
        } else {
            addField(REFERENCE, referenceTextBox, getTitle(wfmStrings.reference()));
        }

        showInPayslip = new KpiCheckBox(formPropertyMap != null && formPropertyMap.get("SHOW_PAYSLIP") != null && formPropertyMap.get("SHOW_PAYSLIP").isChanged() ? formPropertyMap.get("SHOW_PAYSLIP").getTitle() : wfmStrings.showInPayslip());
        showInPayslip.setValue(false);
        showInPayslip.setEnabled(PayrollConstants.CATEGORY_PAYMENT.equals(type));
        showInPayslip.addValueChangeHandler(valueChangeEvent -> {
            EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
            for (int i = 0; i < table.getRowCount(); i++) {
                calculationTaxAndemployerContribution(i);
            }
        });

        calculateByLastMonth = new KpiCheckBox(payrollStrings.calculateByLastMonth());
        calculateByLastMonth.setVisible(false);
        calculateByLastMonth.addValueChangeHandler(changeEvent -> onChangePaymentType(true, false));

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_6, showInPayslip));
        row.add(new GColumn(GColumnEnum.COL_6, calculateByLastMonth));

        addField("SHOW_PAYSLIP", row, "&nbsp;", true);

    }

    private void setMonthItems() {
        month.addListItem(new SelectItem(0, wfmStrings.january()));
        month.addListItem(new SelectItem(1, wfmStrings.february()));
        month.addListItem(new SelectItem(2, wfmStrings.march()));
        month.addListItem(new SelectItem(3, wfmStrings.april()));
        month.addListItem(new SelectItem(4, wfmStrings.may()));
        month.addListItem(new SelectItem(5, wfmStrings.june()));
        month.addListItem(new SelectItem(6, wfmStrings.july()));
        month.addListItem(new SelectItem(7, wfmStrings.august()));
        month.addListItem(new SelectItem(8, wfmStrings.september()));
        month.addListItem(new SelectItem(9, wfmStrings.october()));
        month.addListItem(new SelectItem(10, wfmStrings.november()));
        month.addListItem(new SelectItem(11, wfmStrings.december()));
        month.setSelected(new Date().getMonth());
    }

    private void setYearItems() {
        SelectItem[] yearItem = new SelectItem[5];
        Date date = new Date();
        int currentYear = Integer.valueOf(format_year.format(date));

        for (int i = 2, j = 0; j < 2; i--, j++) {
            yearItem[j] = new SelectItem(currentYear - i, String.valueOf(currentYear - i));
        }

        yearItem[2] = new SelectItem(currentYear, String.valueOf(currentYear));

        for (int i = 1, j = 3; i <= 2; i++, j++) {
            yearItem[j] = new SelectItem(currentYear + i, String.valueOf(currentYear + i));
        }
        year.setItems(yearItem);
    }

    private void onChangeMonthEndYear() {
        if ((lookUp != null && lookUp.isSelected()) || (EMPLOYEE_TYPE.equals(entityType) && !multiSelectLookUpForEmployeeType.getSelectedItemIds().isEmpty())) {
            Integer currentYear = year.getSelectedId();
            Integer monthDayCount = CalendarUtil.getMonthDaysCount(month.getSelectedId(), currentYear);
            Date monthEndDate = new Date(currentYear - 1900, month.getSelectedId(), monthDayCount);

            if (defaultDateWidget != null) {
                defaultDateWidget.setDate(monthEndDate);
                applyDefaultDate();
            }
            changeReferenceValue();
            tableStart = 0;
            tableCurrent = 0;
            totalTableItems = 0;
            changedItemMap.clear();
            deletedItemMap.clear();
            loadTableData(true);
        }
    }

    private void changeReferenceValue() {
        String name;
        if (EMPLOYEE_TYPE.equals(entityType)) {
            name = multiSelectLookUpForEmployeeType != null ? multiSelectLookUpForEmployeeType.getSelectedItemsAsString() : "";
        } else {
            name = lookUp != null && lookUp.getSelectedItem() != null ? lookUp.getSelectedItem().getName() : "";
        }
        String category = categoryForAll != null && categoryForAll.getSelectedItem() != null ? categoryForAll.getSelectedItem().getName() : "";

        String defaultReferenceText = name + " " + category;
        referenceTextBox.setText(defaultReferenceText);
    }

    private void onChangePaymentType(boolean isNotFillForm, boolean edit) {
        fixedAmount.setText("");

        String paymentTypeName = edit && data.getPaymentType() != null ? data.getPaymentType() : paymentType.getSelectedItem() != null ? paymentType.getSelectedItem().getDescription() : "";
        if (edit && paymentTypeName != null) {
            paymentType.setSelectedByDescription(paymentTypeName);
        }
        if (FIXED_AMOUNT.getDescription().equals(paymentTypeName)) {
            fixedAmountFormGroup.setLabel(formPropertyMap.get(CustomFormConstants.AMOUNT) != null && formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : wfmStrings.fixedAmount());
            fixedAmountFormGroup.setVisible(true);
            changeTable(isNotFillForm);
            basicAllowanceLabel.setVisible(false);
        } else if (BASIC_SALARY.getDescription().equals(paymentTypeName)) {
            fixedAmountFormGroup.setLabel(formPropertyMap.get(CustomFormConstants.AMOUNT) != null && formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : wfmStrings.percentage() + "(%)");
            if (edit && data.getPercentage() != null) {
                fixedAmount.setText(PayrollClientUtils.format(data.getPercentage().setScale(2, RoundingMode.HALF_UP)));
            }
            fixedAmountFormGroup.setVisible(true);
            changeTable(isNotFillForm);
            basicAllowanceLabel.setVisible(false);
        } else if (BASIC_SALARY_ALLOWANCE.getDescription().equals(paymentTypeName)) {
            fixedAmountFormGroup.setLabel(formPropertyMap.get(CustomFormConstants.AMOUNT) != null && formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : wfmStrings.percentage() + "(%)");
            if (edit) {
                selectedCategories = data.getAllowancePaymentCategories();
                categoriesList.clear();
                categoriesDialogBox.clear();
                createBasicAllowancePopUp();
                if (data.getPercentage() != null) {
                    fixedAmount.setText(PayrollClientUtils.format(data.getPercentage().setScale(2, RoundingMode.HALF_UP)));
                }
            }
            fixedAmountFormGroup.setVisible(true);
            changeTable(isNotFillForm);
            basicAllowanceLabel.setVisible(true);
        } else {
            fixedAmountFormGroup.setVisible(false);
            basicAllowanceLabel.setVisible(false);
        }
    }

    private void applyDefaultDate() {
        if (defaultDateWidget.getDate() != null) {
            EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
            for (int i = 0; i < table.getRowCount(); i++) {
                EmployeeBox employeeBox = (EmployeeBox) table.getColumnById(i, ItemTableConstants.EMPLOYEE);
                ExtendedDatePicker defaultSelectedDate = (ExtendedDatePicker) table.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
                Integer column = table.getColumnId(ItemTableConstants.PAYMENT_DATE);
                Date selectedDate = defaultDateWidget.getDate();
                if (employeeBox != null && employeeBox.getEmployee() != null && changedItemMap.get(employeeBox.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + employeeBox.getDeductionId() : "")) != null
                        && changedItemMap.get(employeeBox.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + employeeBox.getDeductionId() : "")).getAdditionalPaymentDate() != null) {
                    selectedDate = changedItemMap.get(employeeBox.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + employeeBox.getDeductionId() : "")).getAdditionalPaymentDate().getNonConvertedDate();
                }
                if (defaultSelectedDate != null) {
                    defaultSelectedDate.setItemValue(selectedDate);
                    table.getGrid().getModel().update(i, column, defaultSelectedDate);
                }
                calculationTaxAndemployerContribution(i);
            }
        }
    }

    private void deleteTableItem(Integer rowId) {
        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        EmployeeBox employee = (EmployeeBox) table.getColumnById(rowId, ItemTableConstants.EMPLOYEE);
        Integer employeeId = employee != null && employee.getEmployee() != null ? employee.getEmployee().getId() : null;
        String key = employeeId + (data != null && data.getOldStatusCode() != null ? "_" + employee.getDeductionId() : "");
        if (changedItemMap.get(key) != null) {
            changedItemMap.remove(key);
        }
        deletedItemMap.put(key, true);
    }

    private void changedItemsMap() {
        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        int i = isAdvancedType() ? paymentsTableAllowanceGrid.getCurrentRow() : paymentsTableGrid.getCurrentRow();
        Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();
        EmployeeBox employeeCell = (EmployeeBox) table.getColumnById(i, ItemTableConstants.EMPLOYEE);
        CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(i, ItemTableConstants.CATEGORY);
        ExtendedDatePicker paymentDatePicker = (ExtendedDatePicker) table.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
        CustomCellLabel total = (CustomCellLabel) table.getColumnById(i, PayrollContants.TOTAL_SALARY);
        LinkCellWidget employerContribution = (LinkCellWidget) table.getColumnById(i, PayrollContants.EMPLOYER_CONTRIBUTION);
        LinkCellWidget customDeduction = (LinkCellWidget) table.getColumnById(i, PayrollContants.DEDUCTION);
        LinkCellWidget tax = (LinkCellWidget) table.getColumnById(i, PayrollContants.TAX);
        BigDecimal taxTotal = BigDecimal.ZERO;

        if (employeeCell != null && employeeCell.getEmployee() != null) {
            String key = employeeCell.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + employeeCell.getDeductionId() : "");
            PaymentDeductionObject item = changedItemMap.get(key) != null ? changedItemMap.get(key) : new PaymentDeductionObject();

            item.setId(employeeCell.getDeductionId());
            item.setEmployee(employeeCell.getEmployee());
            item.setBasicSalaryPartAmount(employeeCell.getBasicSalaryPartAmount());
            if (employeeCell.getDeductionId() != null) {
                itemCFsValues.putAll(setHideValues(item, employeeCell.getDeductionId()));
            }

            if (amountTextBox != null) {
                item.setPaymentAmount(PayrollClientUtils.parseToBigDecimal(amountTextBox.getText()));
            }
            if (categoryLookUp != null && categoryLookUp.getSelectedData() != null) {
                item.setCategoryItem(categoryLookUp.getSelectedData());
            } else if (categoryLookUp != null && categoryLookUp.getSelectedItemID() != null) {
                PaymentDeductionSelectItem selectItem = new PaymentDeductionSelectItem();
                selectItem.setId(categoryLookUp.getSelectedItem().getId());
                selectItem.setName(categoryLookUp.getSelectedItem().getName());
                item.setCategoryItem(selectItem);
            } else if (categoryLookUp == null && categoryForAll.getSelectedData() != null) {
                item.setCategoryItem(categoryForAll.getSelectedData());
            }
            if (paymentDatePicker != null) {
                item.setAdditionalPaymentDate(new DateNonConvertable(paymentDatePicker.getDate()));
            } else if (paymentDatePicker == null && defaultDateWidget.getDate() != null) {
                item.setAdditionalPaymentDate(new DateNonConvertable(defaultDateWidget.getDate()));
            }

            if (isAdvancedType()) {
                CustomCellTextBox basicOrBasicAllowance = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.BASIC_SALARY);
                CustomCellTextBox percentage = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.PERCENTAGE);

                BigDecimal value = PayrollClientUtils.parseToBigDecimal(basicOrBasicAllowance != null ? basicOrBasicAllowance.getText() : "");
                item.setPercentage(PayrollClientUtils.parseToBigDecimal(percentage != null ? percentage.getText() : "").setScale(2, RoundingMode.HALF_UP));

                if (isAllowenceType()) {
                    item.setBasicPlusAllowance(value);
                } else if (isBasicSalaryType()) {
                    item.setEmployeeBasicSalary(value);
                }
            } else {
                item.setBasicPlusAllowance(BigDecimal.ZERO);
                item.setEmployeeBasicSalary(BigDecimal.ZERO);
                item.setPercentage(BigDecimal.ZERO);
            }
            if (total != null) {
                item.setTotalAmount(PayrollClientUtils.parseToBigDecimal(total.getText()));
            }
            if (tax != null) {
                item.setTax(PayrollClientUtils.parseToBigDecimal(tax.getText()));
                if (!showInPayslip.getValue() && employeeCell.getTaxCategories() != null && employeeCell.getTaxCategories().size() > 0) {
                    List<PaymentDeductionObject> taxCategories = new ArrayList<>();
                    for (PaymentDeductionObject taxCategory : employeeCell.getTaxCategories()) {
                        BigDecimal payAmount = item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO;
                        taxCategory.setAmount(payAmount.multiply(taxCategory.getPercentage()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                        taxCategories.add(taxCategory);
                        taxTotal = taxTotal.add(taxCategory.getAmount());
                    }
                    item.setTaxCategories(taxCategories);
                }
            }
            if (employerContribution != null) {
                item.setEmployerContribution(PayrollClientUtils.parseToBigDecimal(employerContribution.getText()));

                if (!showInPayslip.getValue() && employeeCell.getEmployerContributionCategories() != null && employeeCell.getEmployerContributionCategories().size() > 0) {
                    List<PaymentDeductionObject> employeeContributions = new ArrayList<>();
                    for (PaymentDeductionObject employeeContribution : employeeCell.getEmployerContributionCategories()) {
                        BigDecimal payAmount = item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO;
                        employeeContribution.setAmount(payAmount.multiply(employeeContribution.getPercentage()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                        employeeContributions.add(employeeContribution);
                    }
                    item.setEmployerContributionCategories(employeeContributions);
                }
            }
            if (customDeduction != null) {
                item.setDeduction(PayrollClientUtils.parseToBigDecimal(customDeduction.getText()));

                boolean isSuitableCategory = item.getCategoryItem() != null && PayrollConstants.CATEGORY_PAYMENT.equals(item.getCategoryItem().getType()) && !item.getCategoryItem().isExcludeInCustomDeductions();
                if (!showInPayslip.getValue() && isSuitableCategory && employeeCell.getDeductionCategories() != null && employeeCell.getDeductionCategories().size() > 0) {
                    List<PaymentDeductionObject> deductionCategories = new ArrayList<>();
                    for (PaymentDeductionObject deductionCategory : employeeCell.getDeductionCategories()) {
                        BigDecimal payAmount = item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO;
                        if (Integer.valueOf(4).equals(deductionCategory.getType()) && !item.getCategoryItem().isExcludeInCustomDeductions()) {
                            payAmount = payAmount.subtract(taxTotal);
                        }
                        deductionCategory.setAmount(payAmount.multiply(deductionCategory.getPercentage()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                        deductionCategories.add(deductionCategory);
                    }
                    item.setDeductionCategories(deductionCategories);
                }
            }

            Integer currentYear = year.getSelectedId();
            Integer monthDayCount = CalendarUtil.getMonthDaysCount(month.getSelectedId(), currentYear);
            item.setStarttDate(new DateNonConvertable(new Date(currentYear - 1900, month.getSelectedId(), 1)));
            item.setEnddDate(new DateNonConvertable(new Date(currentYear - 1900, month.getSelectedId(), monthDayCount)));

            if (itemCFs != null && !itemCFs.isEmpty()) {
                ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                for (String keyCF : itemCFs.keySet()) {
                    CustomFieldInterface customField = (CustomFieldInterface) table.getColumnById(i, keyCF);

                    if (customField != null) {
                        final CompanyCustomFieldItem companyCustomFieldItem = customField.getFieldItem();
                        final CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                        resultItem.setDataType(companyCustomFieldItem.getDataType());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());
                        resultItem.setColumnCode(keyCF);
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
                    } else if (itemCFsValues.size() > 0 && itemCFsValues.get(keyCF) != null && itemCFsValues.get(keyCF).getUiType() != null) {
                        fieldItems.add(itemCFsValues.get(keyCF));
                    }
                }
                if (!fieldItems.isEmpty()) {
                    item.setItemCustomFields(fieldItems);
                }
            }
            changedItemMap.put(key, item);
        }
    }

    private Widget[] getWidgets(PaymentDeductionObject item) {
        int count = 0;
        List<Widget> widgets = new ArrayList<>();
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.EMPLOYEE.equals(columnCode)) {
                EmployeeBox employeeBox = new EmployeeBox(item);
                employeeBox.setEnabled(true);
                employeeBox.setReadOnly(true);
                employeeBox.addStyleName(DEFAULT_WIDTH);
                employeeBox.setStyleName("file--AdditionalPaymentUIBinder");
                widgets.add(employeeBox);
                count++;
            } else if (ItemTableConstants.AMOUNT.equals(columnCode)) {
                CustomCellTextBox amountTextBox = new CustomCellTextBox();
                amountTextBox.addStyleName(DEFAULT_WIDTH);
                amountTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                amountTextBox.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                Validation.checkToFocusTextBox(amountTextBox, NumberFormat.getFormat(",###.##").format(BigDecimal.ZERO));
                Validation.addNumericKeyboardListener(amountTextBox, 3);
                amountTextBox.setText(PayrollClientUtils.format(item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO));
                amountTextBox.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                amountTextBox.addChangeHandler(changeEvent -> {
                    EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
                    if (isAdvancedType()) {
                        CustomCellTextBox basicSalaryTextBox = (CustomCellTextBox) paymentsTableAllowance.getColumnById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.BASIC_SALARY);
                        CustomCellTextBox percentageTextBox = (CustomCellTextBox) paymentsTableAllowance.getColumnById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.PERCENTAGE);
                        BigDecimal amount = PayrollClientUtils.parseToBigDecimal(amountTextBox.getText());
                        BigDecimal basicSalary = PayrollClientUtils.parseToBigDecimal(basicSalaryTextBox.getText());
                        BigDecimal percentage = (amount.divide(basicSalary, 14, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100));
                        Integer totalColumn = table.getColumnId(ItemTableConstants.PERCENTAGE);
                        percentageTextBox.setText(NumberFormat.getFormat(",###.##").format(percentage));
                        CustomCell amountCell = (CustomCell) paymentsTableAllowance.getColumnCellWidgetById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.PERCENTAGE);
                        amountCell.InActive();
                    }
                    calculationTaxAndemployerContribution(table.getGrid().getCurrentRow());
                    changedItemsMap();
                });
                widgets.add(amountTextBox);
                count++;
            } else if (ItemTableConstants.CATEGORY.equals(columnCode)) {
                PayrollCategoryLookUp categoryLookUp = new PayrollCategoryLookUp(type);
                categoryLookUp.setWidth(Constants.NORMAL_WIDTH);
                categoryLookUp.getSuggestBox().getElement().getStyle().setWidth(100, Style.Unit.PCT);
                categoryLookUp.getSuggestBox().getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                if (item.getCategoryItem() != null) {
                    categoryLookUp.addCategoryItem(item.getCategoryItem());
                }
                categoryLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (objectId == null) {
                    categoryLookUp.getSuggestBox().addSelectionHandler(sh -> {
                        PayrollService.App.get().getPredefinedValueOfCategory(item.getEmployee().getId(), categoryLookUp.getSelectedItemID(), new AsyncCallback<BigDecimal>() {
                            @Override
                            public void onFailure(Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(BigDecimal value) {
                                EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
                                if (value != null) {
                                    CustomCellTextBox txtAmount = (CustomCellTextBox) table.getColumnById(table.getGrid().getCurrentRow(), wfmStrings.amount());
                                    txtAmount.setText(PayrollClientUtils.format(value));
                                    ((CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), wfmStrings.amount())).InActive();
                                }
                                calculationTaxAndemployerContribution(table.getGrid().getCurrentRow());
                                changedItemsMap();
                            }
                        });
                    });
                } else {
                    categoryLookUp.getSuggestBox().addSelectionHandler(sh -> {
                        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
                        calculationTaxAndemployerContribution(table.getGrid().getCurrentRow());
                        changedItemsMap();
                    });
                }
                widgets.add(categoryLookUp);
                count++;
            } else if (ItemTableConstants.BASIC_SALARY.equals(columnCode)) {
                if ((isAdvancedType())) {
                    BigDecimal amount = isAllowenceType() ? item.getBasicPlusAllowance() : item.getEmployeeBasicSalary();
                    amount = amount != null ? amount : BigDecimal.ZERO;
                    //By Commission fields
                    CustomCellTextBox basicSalaryTextBox = new CustomCellTextBox(true);
                    basicSalaryTextBox.addStyleName(DEFAULT_WIDTH);
                    basicSalaryTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    basicSalaryTextBox.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                    Validation.checkToFocusTextBox(basicSalaryTextBox, NumberFormat.getFormat(",##0").format(BigDecimal.ZERO));
                    Validation.addNumericKeyboardListener(basicSalaryTextBox, 2);
                    basicSalaryTextBox.setText(PayrollClientUtils.format(amount));
                    if (!Utils.hasPermission(PermissionConstants.BASIC_SALARY_EDIT)) {
                        basicSalaryTextBox.setEnabled(false);
                    }
                    basicSalaryTextBox.addChangeHandler(e -> {
                        CustomCellTextBox amountTextBox = (CustomCellTextBox) paymentsTableAllowance.getColumnById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.AMOUNT);
                        CustomCellTextBox percentage = (CustomCellTextBox) paymentsTableAllowance.getColumnById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.PERCENTAGE);


                        BigDecimal amountBigDecimal = PayrollClientUtils.parseToBigDecimal(basicSalaryTextBox.getText());
                        BigDecimal percentageBigDecimal = PayrollClientUtils.parseToBigDecimal(percentage.getText());
                        amountTextBox.setText(PayrollClientUtils.format(amountBigDecimal.multiply(percentageBigDecimal).divide(BigDecimal.valueOf(100), RoundingMode.CEILING).setScale(2, RoundingMode.HALF_UP)));

                        CustomCell amountCell = (CustomCell) paymentsTableAllowance.getColumnCellWidgetById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.AMOUNT);
                        amountCell.InActive();
                        calculationTaxAndemployerContribution(paymentsTableAllowance.getGrid().getCurrentRow());
                        changedItemsMap();
                    });
                    widgets.add(basicSalaryTextBox);
                    count++;
                }

            } else if (ItemTableConstants.PERCENTAGE.equals(columnCode)) {
                if ((isAdvancedType())) {
                    BigDecimal percentage = item.getPercentage() != null ? item.getPercentage() : BigDecimal.ZERO;
                    CustomCellTextBox percentageTextBox = new CustomCellTextBox(true);
                    percentageTextBox.addStyleName(DEFAULT_WIDTH);
                    percentageTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    percentageTextBox.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                    Validation.checkToFocusTextBox(percentageTextBox, NumberFormat.getFormat(",###.##").format(BigDecimal.ZERO));
                    Validation.addNumericKeyboardListener(percentageTextBox, 3);
                    percentageTextBox.setText(NumberFormat.getFormat(",###.##").format(percentage));
                    percentageTextBox.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                    percentageTextBox.addChangeHandler(e -> {
                        CustomCellTextBox amountTextBox = (CustomCellTextBox) paymentsTableAllowance.getColumnById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.AMOUNT);
                        CustomCellTextBox basicSalaryTextBox = (CustomCellTextBox) paymentsTableAllowance.getColumnById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.BASIC_SALARY);


                        BigDecimal amountBigDecimal = PayrollClientUtils.parseToBigDecimal(basicSalaryTextBox.getText());
                        BigDecimal percentageBigDecimal = PayrollClientUtils.parseToBigDecimal(percentageTextBox.getText());
                        amountTextBox.setText(PayrollClientUtils.format(amountBigDecimal.multiply(percentageBigDecimal).divide(BigDecimal.valueOf(100), RoundingMode.CEILING).setScale(2, RoundingMode.HALF_UP)));
                        CustomCell amountCell = (CustomCell) paymentsTableAllowance.getColumnCellWidgetById(paymentsTableAllowance.getGrid().getCurrentRow(), ItemTableConstants.AMOUNT);
                        amountCell.InActive();
                        calculationTaxAndemployerContribution(paymentsTableAllowance.getGrid().getCurrentRow());
                        changedItemsMap();
                    });

                    widgets.add(percentageTextBox);
                    count++;
                }
            } else if (ItemTableConstants.PAYMENT_DATE.equals(columnCode)) {
                ExtendedDatePicker paymentDate = new ExtendedDatePicker();
                paymentDate.addStyleName(DEFAULT_WIDTH);
                paymentDate.setWidth(NORMAL_WIDTH);
                if (item.getAdditionalPaymentDate() != null) {
                    paymentDate.setDate(item.getAdditionalPaymentDate().getNonConvertedDate());
                }
                paymentDate.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                widgets.add(paymentDate);
                paymentDate.addChangeHandler(changeEvent -> {
                    EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
                    calculationTaxAndemployerContribution(table.getGrid().getCurrentRow());
                    changedItemsMap();
                });
                count++;
            } else if (PayrollContants.DEDUCTION.equals(columnCode)) {
                LinkCellWidget deduction = new LinkCellWidget(PayrollClientUtils.format(BigDecimal.ZERO), () -> {
                    EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;

                    int currentRow = table.getGrid().getCurrentRow();
                    EmployeeBox employee = (EmployeeBox) table.getColumnById(currentRow, ItemTableConstants.EMPLOYEE);
                    PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(currentRow, ItemTableConstants.CATEGORY);
                    if (showInPayslip.getValue() || categoryLookUp.getSelectedItemID() == null) {
                        if (showInPayslip.getValue()) {
                            Info.warn("The Show in payslip field should be unselectable");
                        } else {
                            Info.warn("Category field should be selectable");
                        }
                    } else {
                        showTaxModal(item, employee.getTaxCategories(), employee.getEmployerContributionCategories(), employee.getDeductionCategories(),
                                employee != null && employee.getDeductionId() != null && (data.getLeaveRequestId() == null || (data.getLeaveRequestId() != null && !PAYMENT_STATUS_DRAFT.equals(data.getStatusCode()))));
                    }
                });
                if (item != null && item.getDeduction() != null) {
                    deduction.setText(PayrollClientUtils.format(item.getDeduction()));
                }
                widgets.add(deduction);
                count++;
            } else if (PayrollContants.EMPLOYER_CONTRIBUTION.equals(columnCode)) {
                LinkCellWidget employerContribution = new LinkCellWidget(PayrollClientUtils.format(BigDecimal.ZERO), () -> {
                    EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;

                    int currentRow = table.getGrid().getCurrentRow();
                    EmployeeBox employee = (EmployeeBox) table.getColumnById(currentRow, ItemTableConstants.EMPLOYEE);
                    PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(currentRow, ItemTableConstants.CATEGORY);
                    if (showInPayslip.getValue() || categoryLookUp.getSelectedItemID() == null) {
                        if (showInPayslip.getValue()) {
                            Info.warn("The Show in payslip field should be unselectable");
                        } else {
                            Info.warn("Category field should be selectable");
                        }
                    } else {
                        showTaxModal(item, employee.getTaxCategories(), employee.getEmployerContributionCategories(), employee.getDeductionCategories(),
                                employee != null && employee.getDeductionId() != null && (data.getLeaveRequestId() == null || (data.getLeaveRequestId() != null && !PAYMENT_STATUS_DRAFT.equals(data.getStatusCode()))));
                    }
                });
                if (item != null && item.getEmployerContribution() != null) {
                    employerContribution.setText(PayrollClientUtils.format(item.getEmployerContribution()));
                }
                widgets.add(employerContribution);
                count++;
            } else if (PayrollContants.TAX.equals(columnCode)) {

                LinkCellWidget taxCell = new LinkCellWidget(PayrollClientUtils.format(BigDecimal.ZERO), () -> {
                    EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
                    int currentRow = table.getGrid().getCurrentRow();
                    EmployeeBox employee = (EmployeeBox) table.getColumnById(currentRow, ItemTableConstants.EMPLOYEE);
                    PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(currentRow, ItemTableConstants.CATEGORY);
                    if (showInPayslip.getValue() || categoryLookUp.getSelectedItemID() == null) {
                        if (showInPayslip.getValue()) {
                            Info.warn("The Show in payslip field should be unselectable");
                        } else {
                            Info.warn("Category field should be selectable");
                        }
                    } else {
                        showTaxModal(item, employee.getTaxCategories(), employee.getEmployerContributionCategories(), employee.getDeductionCategories(),
                                employee != null && employee.getDeductionId() != null && (data.getLeaveRequestId() == null || (data.getLeaveRequestId() != null && !PAYMENT_STATUS_DRAFT.equals(data.getStatusCode()))));
                    }
                });
                if (item != null && item.getTax() != null) {
                    taxCell.setText(PayrollClientUtils.format(item.getTax()));
                }
                widgets.add(taxCell);
                count++;
            } else if (PayrollContants.TOTAL_SALARY.equals(columnCode)) {

                CustomCellLabel totalSalary = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
                totalSalary.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                if (item != null && item.getTotalAmount() != null) {
                    totalSalary.setText(PayrollClientUtils.format(item.getTotalAmount()));
                }
                widgets.add(totalSalary);
                count++;
            } else if (itemCFs.containsKey(columnCode)) {

                CompanyCustomFieldItem cfItem = itemCFs.get(columnCode);
                CompanyCustomFieldItem companyCustomFieldItem = setCustomFieldValue(item.getItemCustomFields(), cfItem);

                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextBoxField cf = new CustomTextBoxField(companyCustomFieldItem);
                    cf.setMaxLength(1000);
                    widgets.add(cf);
                    cf.addValueChangeHandler(event -> {
                        if (cf.getText() != null && !"".equals(cf.getText())) {
                            changedItemsMap();
                        }
                    });
                } else if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextAreaField customTextAreaField = new CustomTextAreaField(companyCustomFieldItem);
                    customTextAreaField.hideCharacterLimitPanel();
                    Validation.addAutoResizeListenerToTextArea(customTextAreaField.getTextArea());
                    widgets.add(customTextAreaField);
                    customTextAreaField.addKeyPressHandler(event -> {
                        if (customTextAreaField.getText() != null && !"".equals(customTextAreaField.getText())) {
                            changedItemsMap();
                        }
                    });
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                    CustomPercentageField cf = new CustomPercentageField(companyCustomFieldItem);
                    widgets.add(cf);
                    cf.addValueChangeHandler(event -> {
                        if (cf.getText() != null && !"".equals(cf.getText())) {
                            changedItemsMap();
                        }
                    });
                } else if (Constants.UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                    CustomDropDownField cf = new CustomDropDownField(companyCustomFieldItem);
                    widgets.add(cf);
                    cf.addValueChangeHandler(event -> {
                        if (cf.getSelectedItem() != null) {
                            changedItemsMap();
                        }
                    });
                } else if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                    CustomDatePicker cf = new CustomDatePicker(companyCustomFieldItem);
                    widgets.add(cf);
                    cf.addChangeHandler(event -> {
                        if (cf.getDate() != null) {
                            changedItemsMap();
                        }
                    });
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                    CustomDateTime cf = new CustomDateTime(companyCustomFieldItem);
                    widgets.add(cf);
                } else if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    CustomFieldLookUpField cf = new CustomFieldLookUpField(companyCustomFieldItem);
                    widgets.add(cf);
                    cf.addValueChangeHandler(event -> {
                        if (cf.getSelectedItem() != null) {
                            changedItemsMap();
                        }
                    });
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    CustomFieldMultiLookUpField cf = new CustomFieldMultiLookUpField(companyCustomFieldItem);
                    widgets.add(cf);
                    cf.addValueChangeHandler(event -> {
                        if (cf.getSelectedItems() != null) {
                            changedItemsMap();
                        }
                    });
                }

                if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                    CompanyCustomFieldItem fitem = companyCustomFieldItem;
                    if (fitem != null) {
                        ((CustomFieldInterface) widgets.get(count)).setFieldItem(fitem);
                    }
                }
                count++;
            }
        }
        return widgets.toArray(new Widget[]{});
    }

    private void calculationTaxAndemployerContribution(int currentRow) {
        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        EmployeeBox employee = (EmployeeBox) table.getColumnById(currentRow, ItemTableConstants.EMPLOYEE);
        CustomCellTextBox amount = (CustomCellTextBox) table.getColumnById(currentRow, ItemTableConstants.AMOUNT);
        CustomCellLabel total = (CustomCellLabel) table.getColumnById(currentRow, PayrollContants.TOTAL_SALARY);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(currentRow, ItemTableConstants.CATEGORY);
        ExtendedDatePicker paymentDatePicker = (ExtendedDatePicker) table.getColumnById(currentRow, ItemTableConstants.PAYMENT_DATE);
        LinkCellWidget employerContribution = (LinkCellWidget) table.getColumnById(currentRow, PayrollContants.EMPLOYER_CONTRIBUTION);
        LinkCellWidget tax = (LinkCellWidget) table.getColumnById(currentRow, PayrollContants.TAX);
        LinkCellWidget deduction = (LinkCellWidget) table.getColumnById(currentRow, PayrollContants.DEDUCTION);
        BigDecimal reqAmount = PayrollClientUtils.parseToBigDecimal(amount.getText());
        BigDecimal taxableAmount = reqAmount;
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal employerContributionTotal = BigDecimal.ZERO;
        BigDecimal deductionTotal = BigDecimal.ZERO;
        List<PaymentDeductionObject> taxCategories = new ArrayList<>();
        List<PaymentDeductionObject> employeeContributionCategories = new ArrayList<>();
        List<PaymentDeductionObject> deductionCategories = new ArrayList<>();
        if (!showInPayslip.getValue() && employee != null && employee.getEmployee() != null && categoryLookUp != null && categoryLookUp.getSelectedItemID() != null) {
            if (categoryLookUp.getSelectedItemID() != null) {
                PaymentDeductionSelectItem categoryItem = categoryLookUp.getSelectedData();
                if (PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS.equals(categoryItem.getSystemCode()) ||
                        PayrollConstants.MATERIAL_AID_TYPE_FUNERAL.equals(categoryItem.getSystemCode()) ||
                        PayrollConstants.MATERIAL_AID_TYPE_GIFT.equals(categoryItem.getSystemCode())) {
                    BigDecimal balance = employee.getLgotaBalanceMap().getOrDefault(categoryItem.getSystemCode(), BigDecimal.ZERO);
                    taxableAmount = taxableAmount.subtract(balance);
                    taxableAmount = taxableAmount.compareTo(BigDecimal.ZERO) > 0 ? taxableAmount : BigDecimal.ZERO;
                }
            }
            if (employee.getAllTaxCategories() != null && employee.getAllTaxCategories().size() > 0 && taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                for (PaymentDeductionObject taxCategory : employee.getAllTaxCategories()) {
                    boolean findCategory = false;
                    if (taxCategory.isFromAllAllowances()) {
                        findCategory = true;
                    } else if (taxCategory != null && !taxCategory.isSalaryObject() && taxCategory.getType() != null && taxCategory.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE) && taxCategory.getLinkedCategories() != null && taxCategory.getLinkedCategories().size() > 0) {
                        for (PaymentDeductionObject taxAllowanceCategory : taxCategory.getLinkedCategories()) {
                            if (taxAllowanceCategory != null && taxAllowanceCategory.getCategoryItem() != null && categoryLookUp.getSelectedItem().getId().equals(taxAllowanceCategory.getCategoryItem().getId())) {
                                findCategory = true;
                                break;
                            }
                        }
                    }
                    if (findCategory) {
                        taxCategories.add(taxCategory);
                        BigDecimal taxAmount = taxableAmount.multiply(taxCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        taxTotal = taxTotal.add(taxAmount);
                    }
                }
            }
            if (employee.getAllEmployerContributionCategories() != null && employee.getAllEmployerContributionCategories().size() > 0 && taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                for (PaymentDeductionObject empContCategory : employee.getAllEmployerContributionCategories()) {
                    boolean findCategory = false;
                    if (empContCategory.isFromAllAllowances()) {
                        findCategory = true;
                    } else if (empContCategory != null && !empContCategory.isSalaryObject() && empContCategory.getType() != null && empContCategory.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE) && empContCategory.getLinkedCategories() != null && empContCategory.getLinkedCategories().size() > 0) {
                        for (PaymentDeductionObject empContAllowanceCategory : empContCategory.getLinkedCategories()) {
                            if (empContAllowanceCategory != null && empContAllowanceCategory.getCategoryItem() != null && categoryLookUp.getSelectedItemID().equals(empContAllowanceCategory.getCategoryItem().getId())) {
                                findCategory = true;
                            }
                        }
                    }
                    if (findCategory) {
                        employeeContributionCategories.add(empContCategory);
                        BigDecimal empContrAmount = taxableAmount.multiply(empContCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        employerContributionTotal = employerContributionTotal.add(empContrAmount);
                    }
                }
            }
            PaymentDeductionSelectItem categoryItem = categoryLookUp.getSelectedData();
            if (employee.getAllDeductionCategories() != null && employee.getAllDeductionCategories().size() > 0 && taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                DateNonConvertable paymentDate = null;
                if (paymentDatePicker != null) {
                    paymentDate = new DateNonConvertable(paymentDatePicker.getDate());
                } else if (paymentDatePicker == null && defaultDateWidget.getDate() != null) {
                    paymentDate = new DateNonConvertable(defaultDateWidget.getDate());
                }
                for (PaymentDeductionObject deductionCategory : employee.getAllDeductionCategories()) {
                    if (paymentDate != null &&
                            (deductionCategory.getStarttDate() == null || !paymentDate.getNonConvertedDate().before(deductionCategory.getStarttDate().getNonConvertedDate())) &&
                            (deductionCategory.getEnddDate() == null || !paymentDate.getNonConvertedDate().after(deductionCategory.getEnddDate().getNonConvertedDate()))
                    ) {
                        boolean findCategory = false;
                        BigDecimal deductableAmount = taxableAmount;
                        if (Integer.valueOf(4).equals(deductionCategory.getType()) && !categoryItem.isExcludeInCustomDeductions()) {
                            deductableAmount = taxableAmount.subtract(taxTotal);
                            findCategory = true;
                        } else if (Integer.valueOf(2).equals(deductionCategory.getType()) && deductionCategory.getLinkedCategories() != null && deductionCategory.getLinkedCategories().size() > 0) {
                            for (PaymentDeductionObject deductionAllowanceCategory : deductionCategory.getLinkedCategories()) {
                                if (deductionAllowanceCategory != null && deductionAllowanceCategory.getCategoryItem() != null && categoryLookUp.getSelectedItemID().equals(deductionAllowanceCategory.getCategoryItem().getId())) {
                                    findCategory = true;
                                }
                            }
                        } else if (deductionCategory.isFromAllAllowances()) {
                            findCategory = true;
                        }

                        if (findCategory) {
                            deductionCategories.add(deductionCategory);
                            BigDecimal deductionAmount = deductableAmount.multiply(deductionCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            deductionTotal = deductionTotal.add(deductionAmount);
                        }
                    }
                }
            }
        }

        if (tax != null) {
            tax.setText(PayrollClientUtils.format(taxTotal));
            Integer taxColumn = table.getColumnId(PayrollContants.TAX);
            table.getGrid().getModel().update(currentRow, taxColumn, tax);
        }

        if (employerContribution != null) {
            employerContribution.setText(PayrollClientUtils.format(employerContributionTotal));
            Integer empContrColumn = table.getColumnId(PayrollContants.EMPLOYER_CONTRIBUTION);
            table.getGrid().getModel().update(currentRow, empContrColumn, employerContribution);
        }

        if (deduction != null) {
            deduction.setText(PayrollClientUtils.format(deductionTotal));
            Integer deductionColumn = table.getColumnId(PayrollContants.DEDUCTION);
            table.getGrid().getModel().update(currentRow, deductionColumn, deduction);
        }

        if (total != null) {
            BigDecimal totalValue = reqAmount.subtract(taxTotal).subtract(deductionTotal).setScale(2, RoundingMode.HALF_UP);
            total.setText(PayrollClientUtils.format(totalValue));
            Integer totalColumn = table.getColumnId(PayrollContants.TOTAL_SALARY);
            table.getGrid().getModel().update(currentRow, totalColumn, total);
        }

        employee.setTaxCategories(taxCategories);
        employee.setEmployerContributionCategories(employeeContributionCategories);
        employee.setDeductionCategories(deductionCategories);
    }

    private void showTaxModal(PaymentDeductionObject item, List<PaymentDeductionObject> taxCategories, List<PaymentDeductionObject> employerContributionCategories, List<PaymentDeductionObject> deductionCategories, boolean isEdit) {
        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(table.getGrid().getCurrentRow(), ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(table.getGrid().getCurrentRow(), ItemTableConstants.CATEGORY);

        if (additionalPaymentItemModal != null) {
            additionalPaymentItemModal.close();
        }
        additionalPaymentItemModal = new AdditionalPaymentItemModal(item, columnsMap, PayrollClientUtils.parseToBigDecimal(amountTextBox.getText()), categoryLookUp.getSelectedData(), isEdit, taxCategories, employerContributionCategories, deductionCategories);
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

    private void applyDefaultCategory() {
        if (categoryForAll.getSelectedItem() != null) {
            EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;

            for (int i = 0; i < table.getRowCount(); i++) {
                PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(i, ItemTableConstants.CATEGORY);
                if (categoryLookUp != null) {
                    Integer column = table.getColumnId(ItemTableConstants.CATEGORY);
                    categoryLookUp.addCategoryItem(categoryForAll.getSelectedData());
                    table.getGrid().getModel().update(i, column, categoryLookUp);
                    calculationTaxAndemployerContribution(i);
                }
            }
            changeReferenceValue();
        }
    }

    private void createBasicAllowancePopUp() {
        categoriesDialogBox = new KpiModal();
        categoriesDialogBox.setTitle(payrollStrings.allowanceDetails());
        categoriesDialogBox.getElement().getStyle().setProperty("minWidth", "400px");
        categoriesDialogBox.getElement().getStyle().setProperty("margin", "30px auto");
        categoriesDialogBox.addStyleName("deductionDetailsModal");
        categoriesList = new Div();
        categoriesList.setWidth("100%");
        categoriesList.setHeight("150px");

        createCtegoryList();
        categoriesDialogBox.add(categoriesList);
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.getElement().getStyle().setProperty("maxHeight", "300px");
        categoriesDialogBox.add(scrollPanel);
    }

    private void createCtegoryList() {

        ListingFilterParameter filterParameters = new ListingFilterParameter();
        filterParameters.setAccountType(type);
        filterParameters.setActive(false);
        filterParameters.setCorporate(Utils.isArabicCompany());
        filterParameters.setPayment(true);
        UL ul = new UL();
        PaymentCategoryItem categoryItemSelectAll = new PaymentCategoryItem(new PaymentDeductionSelectItem(-1, wfmStrings.selectAll(), "SELECT_ALL", null), () -> changedItemMap.clear());
        categoryItemSelectAll.getCheckBox().addValueChangeHandler(event -> {
            for (PaymentCategoryItem item : allowanceItems) {
                if (categoryItemSelectAll.getCheckBox().getValue()) {
                    item.chooseAsSelected();
                } else {
                    item.chooseAsUnSelected();
                }
            }
        });
        ul.add(categoryItemSelectAll.getWidget());
        LoadingPanel.loading(true);
        AllInOneService.App.get().getCategoriesForLookUp(filterParameters, new AsyncCallback<PaymentDeductionSelectItem[]>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(PaymentDeductionSelectItem[] paymentDeductionSelectItems) {
                LoadingPanel.loading(false);
                filterParameter.setPayment(false);

                List<Integer> selectedIds = selectedCategories.stream().map(PaymentDeductionSelectItem::getId).collect(Collectors.toList());
                for (PaymentDeductionSelectItem item : paymentDeductionSelectItems) {
                    PaymentCategoryItem categoryItem = new PaymentCategoryItem(item, () -> changedItemMap.clear());
                    if (selectedIds.contains(item.getId())) {
                        categoryItem.chooseAsSelected();
                    }
                    ul.add(categoryItem.getWidget());
                    allowanceItems.add(categoryItem);
                }
                categoriesList.add(ul);
                categoriesList.setVisible(true);
            }
        });
        WfmButton2 apply = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        apply.addClickHandler(clickEvent -> {
            calculateBasicAndAllowancebySelectedCategory();
            categoriesDialogBox.close();
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(event -> {
            for (PaymentCategoryItem item : allowanceItems) {
                item.chooseAsUnSelected();
            }
            calculateBasicAndAllowancebySelectedCategory();
            categoriesDialogBox.close();
        });
        categoriesDialogBox.addButton(apply);
        categoriesDialogBox.addButton(cancel);
    }

    private void calculateBasicAndAllowancebySelectedCategory() {
        selectedCategories = new ArrayList<>();
        for (PaymentCategoryItem item : allowanceItems) {
            if (item.isSelected()) {
                selectedCategories.add(item.getItem());
            }
        }
        loadTableData(true);
    }

    private void changeTable(boolean isNotFillForm) {
        if (isAdvancedType()) {
            paymentsTableAllowance.setVisible(true);
            paymentsTable.setVisible(false);
            calculateByLastMonth.setVisible(true);
        } else {
            paymentsTableAllowance.setVisible(false);
            paymentsTable.setVisible(true);
            calculateByLastMonth.setVisible(false);
        }
        if (isNotFillForm) {
            loadTableData(true);
        }
    }

    private void drawTableSection() {
        if (data.getItemCustomFields() != null) {
            itemCFs.clear();
            for (CompanyCustomFieldItem item : data.getItemCustomFields()) {
                itemCFs.put(item.getColumnCode(), item);
            }
        }

        if (data.getColumnConfigs() != null) {
            columnsMap.clear();
            for (ColumnConfigs cc : data.getColumnConfigs()) {
                GWT.log(cc.getCode());
                if (cc.isSelected()) {
                    columnsMap.put(cc.getCode(), cc);
                }
            }
        }

        initTables();

        Div itemsTableContainer = new Div();
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, paymentsTable)));
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, paymentsTableAllowance)));
        if (isAdvancedType()) {
            paymentsTableAllowance.setVisible(true);
            paymentsTable.setVisible(false);
        } else {
            paymentsTableAllowance.setVisible(false);
            paymentsTable.setVisible(true);
        }

        addField(CustomFormConstants.ITEMS, itemsTableContainer, null, true);
    }

    public void initTables() {
        paymentsTableAllowance = new EditableTable(getColumns(true), Utils.hasPermission(PermissionConstants.ADDITIONAL_PAYMENT_LINE_ITEM_DELETE));
        paymentsTableAllowanceGrid = paymentsTableAllowance.getGrid();
        paymentsTableAllowance.setRemoveRowListener(() -> {
            if (paymentsTableAllowanceGrid.getRowCount() >= 2) {
                deleteTableItem(paymentsTableAllowanceGrid.getCurrentRow());
                paymentsTableAllowanceGrid.getModel().removeRow(paymentsTableAllowanceGrid.getCurrentRow());
            } else {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            }
        });

        paymentsTable = new EditableTable(getColumns(false), Utils.hasPermission(PermissionConstants.ADDITIONAL_PAYMENT_LINE_ITEM_DELETE));
        paymentsTableGrid = paymentsTable.getGrid();

        paymentsTable.setRemoveRowListener(() -> {
            if (paymentsTableGrid.getRowCount() >= 2) {
                deleteTableItem(paymentsTableGrid.getCurrentRow());
                paymentsTableGrid.getModel().removeRow(paymentsTableGrid.getCurrentRow());
            } else {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            }
        });
    }

    private ColumnConfig[] getColumns(boolean isAllowence) {
        List<ColumnConfig> columns = new ArrayList<>();
        int i = 0;
        for (String cc : columnsMap.keySet()) {
            ColumnConfigs columnConfigs = columnsMap.get(cc);
            boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            ColumnConfig columnConfig;

            switch (cc) {
                case ItemTableConstants.EMPLOYEE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.EMPLOYEE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.employee(), Utils.getColumnWidth(columnConfigs.getWidth() != null && !isAllowence ? columnConfigs.getWidth() + 5 : columnConfigs.getWidth(), 250), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.BASIC_SALARY:
                    if (isAllowence) {
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.BASIC_SALARY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.basicSalary(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns.add(columnConfig);
                    }
                    break;
                case ItemTableConstants.PERCENTAGE:
                    if (isAllowence) {
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PERCENTAGE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.percentage() + "(%)", Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns.add(columnConfig);
                    }
                    break;
                case ItemTableConstants.AMOUNT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.AMOUNT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.amount(), Utils.getColumnWidth(columnConfigs.getWidth() != null && !isAllowence ? columnConfigs.getWidth() + 8 : columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.CATEGORY:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CATEGORY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.categories(), Utils.getColumnWidth(columnConfigs.getWidth() != null && !isAllowence ? columnConfigs.getWidth() + 7 : columnConfigs.getWidth(), 130), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.PAYMENT_DATE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PAYMENT_DATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.paymentDate(), Utils.getColumnWidth(columnConfigs.getWidth() != null && !isAllowence ? columnConfigs.getWidth() + 5 : columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.DEDUCTION:
                    columnConfig = new ColumnConfig(LinkableCell.class, PayrollContants.DEDUCTION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.deduction(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.EMPLOYER_CONTRIBUTION:
                    columnConfig = new ColumnConfig(LinkableCell.class, PayrollContants.EMPLOYER_CONTRIBUTION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.employerContribution(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.TAX:
                    columnConfig = new ColumnConfig(LinkableCell.class, PayrollContants.TAX, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.tax(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.TOTAL_SALARY:
                    columnConfig = new ColumnConfig(CustomCell.class, PayrollContants.TOTAL_SALARY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.total(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                default:
                    columnConfig = new ColumnConfig(CustomCell.class, columnConfigs.getCode(), columnConfigs.getTitle(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), isPixel);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);

                    if (Constants.UI_TYPE_TEXTAREA.equalsIgnoreCase(columnConfigs.getUiType())) {
                        columnConfig.setCustomStyleName("product-description-cell");
                    }
                    break;
            }
        }
        return columns.toArray(new ColumnConfig[]{});
    }

    private void applyPercentage() {

        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            EmployeeBox employeeBox = (EmployeeBox) table.getColumnById(i, ItemTableConstants.EMPLOYEE);
            CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.AMOUNT);
            if (employeeBox != null && employeeBox.getEmployee() != null) {
                BigDecimal empMode = empModeMap.get(employeeBox.getEmployee().getId()) != null ? empModeMap.get(employeeBox.getEmployee().getId()) : BigDecimal.ONE;
                if (isBasicSalaryType() || isAllowenceType()) {
                    CustomCellTextBox percentageBox = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.PERCENTAGE);
                    CustomCellTextBox basicOrAllowanceTextBox = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.BASIC_SALARY);

                    Integer percentageColumnId = table.getColumnId(ItemTableConstants.PERCENTAGE);
                    Integer amountColumnId = table.getColumnId(ItemTableConstants.AMOUNT);

                    BigDecimal amountBigDecimal = PayrollClientUtils.parseToBigDecimal(basicOrAllowanceTextBox.getText());
                    BigDecimal percentageBigDecimal = getItemTableBigDecimalColumnValue(fixedAmount.getText(), employeeBox, ItemTableConstants.PERCENTAGE, employeeBox.getDeductionId());
                    if (changedItemMap.get(employeeBox.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + employeeBox.getDeductionId() : "")) != null) {
                        percentageBox.setText(NumberFormat.getFormat(",###.##").format(changedItemMap.get(employeeBox.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + employeeBox.getDeductionId() : "")).getPercentage()));
                        amountTextBox.setText(PayrollClientUtils.format(changedItemMap.get(employeeBox.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + employeeBox.getDeductionId() : "")).getPaymentAmount()));
                    } else {
                        percentageBox.setText(PayrollClientUtils.format(percentageBigDecimal));
                        amountTextBox.setText(PayrollClientUtils.format(amountBigDecimal.multiply(percentageBigDecimal).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                    }
                    table.getGrid().getModel().update(i, percentageColumnId, percentageBox);
                    table.getGrid().getModel().update(i, amountColumnId, amountTextBox);
                } else {
                    BigDecimal fixedAmountValue = getItemTableBigDecimalColumnValue(fixedAmount.getText(), employeeBox, ItemTableConstants.AMOUNT, employeeBox.getDeductionId());

                    Integer amountColumnId = table.getColumnId(ItemTableConstants.AMOUNT);
                    amountTextBox.setValue(PayrollClientUtils.format(fixedAmountValue.multiply(empMode).setScale(2, RoundingMode.HALF_UP)));
                    table.getGrid().getModel().update(i, amountColumnId, amountTextBox);
                }
                calculationTaxAndemployerContribution(i);
            }
        }
    }

    private BigDecimal getItemTableBigDecimalColumnValue(String firstVal, EmployeeBox employeeBox, String columnCode, Integer deductionId) {
        BigDecimal percentageBigDecimal = PayrollClientUtils.parseToBigDecimal(fixedAmount.getText());
        PaymentDeductionObject object = changedItemMap.get(employeeBox.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + deductionId : ""));
        if (employeeBox.getEmployee().getId() != null && object != null) {
            if (ItemTableConstants.PERCENTAGE.equals(columnCode)) {
                return object.getPercentage();
            } else if (ItemTableConstants.AMOUNT.equals(columnCode)) {
                return object.getPaymentAmount();
            } else if (ItemTableConstants.BASIC_SALARY.equals(columnCode)) {
                return isAllowenceType() ? object.getBasicPlusAllowance() : object.getEmployeeBasicSalary();
            } else {
                return percentageBigDecimal;
            }
        }
        return percentageBigDecimal;
    }

    private void initButtonsPanel() {

        Div filterDiv = new Div("frame__info-paging");
        filterDiv.add(drawPaginationPanel());
        footer.addToLeftSide(filterDiv);

        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_PDF)) {
            pdfButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
            addRightButton(pdfButton);
        }


        draftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        draftButton.addClickHandler(clickEvent -> save(PAYMENT_STATUS_DRAFT));
        draftButton.setVisible(false);
        addRightButton(draftButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> save(PAYMENT_STATUS_APPROVED));
        approveButton.setVisible(false);
        addRightButton(approveButton);

        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> save(PAYMENT_STATUS_SUBMITTED));
        submitButton.setVisible(false);
        addRightButton(submitButton);

        String statusCode = !isCopyView && data != null && data.getOverallStatus() != null ? data.getOverallStatus().getCode() : null;

        approver = new ChosenApproversWidget(RelationItem.TYPE_ADDITIONAL_PAYMENT, data.getApprover() != null && !isCopyView ? objectId : null);

        if (data.isApprover()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVERS) != null) {
                addField(CustomFormConstants.APPROVERS, approver, getTitle(formPropertyMap.get(CustomFormConstants.APPROVERS).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVERS).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.APPROVERS).isRequired()));
                approver.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVERS).isDisabled());
            } else {
                addField(APPROVERS, approver, getTitle(wfmStrings.approver(), true));
            }

            if (objectId != null) {
                if (Constants.PAYMENT_STATUS_DRAFT.equals(statusCode) || isCopyView) {
                    draftButton.setVisible(true);
                }
                if (Constants.PAYMENT_STATUS_SUBMITTED.equals(statusCode) ||
                        Constants.PAYMENT_STATUS_APPROVED.equals(statusCode)) {
                    draftButton.setVisible(false);
                }
            } else {
                draftButton.setVisible(true);
            }
        } else {
            approveButton.setVisible(true);

            if (Constants.PAYMENT_STATUS_SUBMITTED.equals(statusCode) ||
                    Constants.PAYMENT_STATUS_APPROVED.equals(statusCode)) {
                draftButton.setVisible(false);
            }
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AdditionalPaymentAddEditViewV2.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    if (itemId != null && Utils.getUserID().equals(itemId)) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        approveButton.setVisible(false);
                    }
                });
                if (approveButton != null && submitButton != null && approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
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

    private void enableButtons(boolean isEnable) {
        draftButton.setEnabled(isEnable);
        submitButton.setEnabled(isEnable);
        approveButton.setEnabled(isEnable);
    }

    private boolean validate(String status) {
        int errors = 0;

        if (formPropertyMap != null && formPropertyMap.get(PERIOD) != null && formPropertyMap.get(PERIOD).isRequired()) {
            errors += markAsError(month, !Validation.validateListBoxRequired(month));
        }

        if (EMPLOYEE_TYPE.equals(entityType)) {
            errors += markAsError(multiSelectLookUpForEmployeeType, (multiSelectLookUpForEmployeeType.getSelectedItemIds() != null && multiSelectLookUpForEmployeeType.getSelectedItemIds().isEmpty()));
        } else {
            errors += markAsError(lookUp, !Validation.validateLookUpRequired(lookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(PAYMENT_TYPE) != null && formPropertyMap.get(PAYMENT_TYPE).isRequired()) {
            errors += markAsError(paymentType, !Validation.validateListBoxRequired(paymentType));
        }

//        errors += markAsError(defaultDateWidget, !Validation.validateDate(defaultDateWidget));

//        errors += markAsError(fixedAmount, !Validation.validateTextBoxRequired(fixedAmount));

        errors += markAsError(categoryForAll, !Validation.validateLookUpRequired(categoryForAll));

        if (formPropertyMap != null && formPropertyMap.get(REFERENCE) != null && formPropertyMap.get(REFERENCE).isRequired() && !PAYMENT_STATUS_DRAFT.equals(status)) {
            errors += markAsError(referenceTextBox, !Validation.validateTextBoxRequired(referenceTextBox));
        }

        if (formPropertyMap != null && formPropertyMap.get(APPROVER) != null && formPropertyMap.get(APPROVER).isRequired() && data != null && data.isApprover() && !approver.isValid()) {
            errors++;
        }

        if (!PAYMENT_STATUS_DRAFT.equals(status)) {
            EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;

            table.setValidRows(0);
            ArrayList<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();
            if (itemCFs != null && columnsMap != null) {
                for (String columnCode : columnsMap.keySet()) {
                    if (itemCFs.containsKey(columnCode) && (itemCFs.get(columnCode).isRequired() || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(itemCFs.containsKey(columnCode)) || Constants.UI_TYPE_URL.equals(itemCFs.containsKey(columnCode)) || Constants.UI_TYPE_PERCENTAGE.equals(itemCFs.containsKey(columnCode)))) {
                        requiredAndEmailCFs.add(itemCFs.get(columnCode));
                    }
                }
            }
            boolean errorFound = false;

            ArrayList<String> requiredColumnCodes = new ArrayList<>();
            int requiredRow = 0;

            if (columnsMap != null && columnsMap.values().size() > 0) {
                for (ColumnConfigs columnConfigs : columnsMap.values()) {
                    if (columnConfigs != null && columnConfigs.isRequired() && columnConfigs.getCompanyCustomFieldID() == null) {
                        requiredRow++;
                        requiredColumnCodes.add(columnConfigs.getCode());
                    }
                }
            }

            for (int i = 0; i < table.getRowCount(); i++) {
                int rowError;
                table.resetValidation(i);
                rowError = validateRequiredItems(i, requiredAndEmailCFs, requiredColumnCodes)[0];
                if (rowError == 0) {
                    table.setItemValid(i, true);
                    table.incValidRow();
                } else if (rowError == requiredRow + requiredAndEmailCFs.size() - validateRequiredItems(i, requiredAndEmailCFs, requiredColumnCodes)[1]) {

                    if (!areOtherRowsAffected(i)) {
                        table.setItemValid(i, false); // exclude
                    } else {
                        colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                        errorFound = true;
                    }
                } else {
                    colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                    errorFound = true;
                }
            }
            if (table.getValidRows() == 0) {
                colorizeErrorField(0, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }
            if (errorFound) {
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                return false;
            }
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        if (itemCFs != null && itemCFs.values().size() > 0) {
            EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
            boolean error = Validation.itemTableNumericCFMinValueValidate(table, itemCFs.values());
            return error;
        }

        if (Utils.isAdditionalPaymentsLocked() && DateUtils.getTransactionLockDate().after(defaultDateWidget.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.additionalPayment(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private int[] validateRequiredItems(int rowID, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        int nonRequired = 0;
        int[] error = new int[2];

        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;

        CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(rowID, ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(rowID, ItemTableConstants.CATEGORY);
        ExtendedDatePicker paymentDatePicker = (ExtendedDatePicker) table.getColumnById(rowID, ItemTableConstants.PAYMENT_DATE);
        if (amountTextBox == null || !Validation.validateTextBoxRequired(amountTextBox)) {
            table.notValid(rowID, ItemTableConstants.AMOUNT);
            errors++;
        } else {
            if (requiredColumnCodes.contains(ItemTableConstants.AMOUNT)) {
                BigDecimal amount = PayrollClientUtils.parseToBigDecimal(amountTextBox.getText());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    table.notValid(rowID, ItemTableConstants.AMOUNT);
                    errors++;
                }
            }
        }
        if (categoryLookUp != null && !Validation.validateLookUpRequired(categoryLookUp)) {
            table.notValid(rowID, ItemTableConstants.CATEGORY);
            errors++;
        }

        if (paymentDatePicker != null && paymentDatePicker.getDate() == null) {
            paymentDatePicker.addStyleName(ERROR_FORM_STYLE);
            table.notValid(rowID, ItemTableConstants.PAYMENT_DATE);
            errors++;
        }
        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    table.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        table.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        table.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    } else {
                        nonRequired++;
                    }
                }
            } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateEmailRequired(t)) {
                        table.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText() != null && t.getText().length() > 0) {
                        if (!Validation.validateEmailRequired(t)) {
                            table.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateUrl(t, null)) {
                        table.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText() != null && t.getText().length() > 0) {
                        if (!Validation.validateUrl(t, null)) {
                            table.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedId() == null) {
                    table.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    table.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    table.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    table.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    table.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(Constants.ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    table.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            }
        }

        error[0] = errors;
        error[1] = nonRequired;
        return error;
    }

    private void colorizeErrorField(int rowID, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(rowID, ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(rowID, ItemTableConstants.CATEGORY);
        ExtendedDatePicker paymentDatePicker = (ExtendedDatePicker) table.getColumnById(rowID, ItemTableConstants.PAYMENT_DATE);

        if (requiredColumnCodes.contains(ItemTableConstants.CATEGORY)) {
            if (categoryLookUp == null || categoryLookUp.getSelectedItem() == null || (categoryLookUp.getText() == null || categoryLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(categoryLookUp.getText()))) {
                table.notValid(rowID, ItemTableConstants.CATEGORY);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.AMOUNT)) {
            if (!Validation.validateTextAreaRequired(amountTextBox)) {
                table.notValid(rowID, ItemTableConstants.AMOUNT);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.PAYMENT_DATE)) {
            if (!Validation.validateDate(paymentDatePicker)) {
                table.notValid(rowID, ItemTableConstants.PAYMENT_DATE);
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            }
            if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateEmailRequired(t)) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateUrl(t, null)) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        table.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        table.notValid(rowID, fieldItem.getColumnCode());

                    }
                }
            } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) table.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(Constants.ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    table.notValid(rowID, fieldItem.getColumnCode());
                }
            }
        }
    }


    private boolean areOtherRowsAffected(int rowID) {
        boolean result = false;
        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(rowID, ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(rowID, ItemTableConstants.CATEGORY);
        ExtendedDatePicker paymentDatePicker = (ExtendedDatePicker) table.getColumnById(rowID, ItemTableConstants.PAYMENT_DATE);
        result |= amountTextBox != null && (amountTextBox.getText() != null && !"".equals(amountTextBox.getText().trim()));
        result |= categoryLookUp != null && (categoryLookUp.getSelectedItem() != null && categoryLookUp.getSelectedItem().getId() != null);
        result |= paymentDatePicker != null && paymentDatePicker.getDate() != null;
        return result;
    }

    public AdditionalPayment getFormData(String status) {
        AdditionalPayment payment = new AdditionalPayment();
        payment.setFromView(false);
        if (!isCopyView) {
            payment.setObjectID(objectId);
        }
        payment.setReference(referenceTextBox.getText());
        payment.setMonth(month.getSelectedItem().getName());
        payment.setMonthID(month.getSelectedId());
        payment.setYear(year.getSelectedId());
        payment.setCurrency(currency);
        payment.setType(Constants.ADDITIONAL_PAYMENT_TYPE);
        payment.setApprovers(approver.getChosenApprovers());
        payment.setShowInPayslip(showInPayslip.getValue());
        payment.setStatusCode(status);
        payment.setAttachments(footerUploadPanel.getAttachedFiles());
        payment.setEntityType(entityType);

        if (EMPLOYEE_TYPE.equals(entityType)) {
            filterParameter.setEmployeeIDs(multiSelectLookUpForEmployeeType.getSelectedItemsIdsAsString());
        }
        if (DEPARTMENT_TYPE.equals(entityType)) {
            filterParameter.setDepartmentId(lookUp.getSelectedItemID());
        }
        if (LOCATION_TYPE.equals(entityType)) {
            filterParameter.setLocationId(lookUp.getSelectedItemID());
        }
        if (GROUP_TYPE.equals(entityType)) {
            filterParameter.setObjectId(lookUp.getSelectedItemID());
        }
        if (SUPERVISOR_TYPE.equals(entityType)) {
            filterParameter.setSupervisorId(lookUp.getSelectedItemID());
        }
        filterParameter.setPaymentCategories(selectedCategories);
        filterParameter.setBasicPlusAllowancePaymentType(isAllowenceType());
        filterParameter.setResignedEmployeesIncluded(false);
        filterParameter.setMonthId(month.getSelectedId());
        filterParameter.setYear(year.getSelectedId());
        filterParameter.setCalculateByLastMonth(calculateByLastMonth.getValue());
        payment.setFilterParameter(filterParameter);
        payment.setOldStatusCode(data != null && !isCopyView ? data.getOldStatusCode() : null);
        payment.setCategoryType(type);
        payment.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        if (EMPLOYEE_TYPE.equals(entityType)) {
            payment.setEmployeeIds(multiSelectLookUpForEmployeeType.getSelectedItemsIdsAsString());
        } else if (DEPARTMENT_TYPE.equals(entityType)) {
            payment.setDepartment(lookUp.getSelectedItem());
        } else if (LOCATION_TYPE.equals(entityType)) {
            payment.setLocation(lookUp.getSelectedItem());
        } else if (GROUP_TYPE.equals(entityType)) {
            payment.setPayrollBatch(lookUp.getSelectedItem());
        } else if (SUPERVISOR_TYPE.equals(entityType)) {
            payment.setSupervisor(lookUp.getSelectedItem());
        }

        payment.setDefaultDate(new DateNonConvertable(defaultDateWidget.getDate()));
        payment.setPaymentType(paymentType.getSelectedIndex() == -1 ? null : paymentType.getSelectedItem(true).getDescription());
        if (isAdvancedType()) {
            payment.setPercentage(PayrollClientUtils.parseToBigDecimal(fixedAmount.getText()).setScale(2, RoundingMode.HALF_UP));
        } else {
            payment.setFixedAmount(fixedAmount.getText() != null && !fixedAmount.getText().isEmpty() ? BigDecimal.valueOf(Utils.universalParse(Utils.getNumberFormat(), fixedAmount.getText())) : BigDecimal.ZERO);
        }
        payment.setBasicPlusAllowance(isAllowenceType());
        payment.setFromCopy(isCopyView);
        payment.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        if (isAllowenceType()) {
            payment.setAllowancePaymentCategories(selectedCategories);
        }
//        GWT.log(selectedCategories + " " + (selectedCategories != null && selectedCategories.size() > 0));
        if (categoryForAll.getSelectedItemID() != null) {
            payment.setDefaultPayrollCategoryId(categoryForAll.getSelectedItemID());
        }
        if (data.getCalculationDetails() != null) {
            payment.setCalculationDetails(data.getCalculationDetails());
        }
        payment.setDeletedItems(deletedItemMap);
        payment.setChangedItems(changedItemMap);
        return payment;
    }

    private List<PaymentDeductionObject> getItems() {
        List<PaymentDeductionObject> items = new ArrayList<>();
        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
        Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            PaymentDeductionObject item = new PaymentDeductionObject();
            EmployeeBox employeeCell = (EmployeeBox) table.getColumnById(i, ItemTableConstants.EMPLOYEE);
            CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.AMOUNT);
            PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(i, ItemTableConstants.CATEGORY);
            ExtendedDatePicker paymentDatePicker = (ExtendedDatePicker) table.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
            CustomCellLabel total = (CustomCellLabel) table.getColumnById(i, PayrollContants.TOTAL_SALARY);
            LinkCellWidget employerContribution = (LinkCellWidget) table.getColumnById(i, PayrollContants.EMPLOYER_CONTRIBUTION);
            LinkCellWidget deduction = (LinkCellWidget) table.getColumnById(i, PayrollContants.DEDUCTION);
            LinkCellWidget tax = (LinkCellWidget) table.getColumnById(i, PayrollContants.TAX);
            BigDecimal taxTotal = BigDecimal.ZERO;

            if (employeeCell != null) {
                item.setId(employeeCell.getDeductionId());
                item.setEmployee(employeeCell.getEmployee());
                item.setBasicSalaryPartAmount(employeeCell.getBasicSalaryPartAmount());
                if (employeeCell.getDeductionId() != null) {
                    itemCFsValues.putAll(setHideValues(item, employeeCell.getDeductionId()));
                }
            }
            if (amountTextBox != null) {
                item.setPaymentAmount(PayrollClientUtils.parseToBigDecimal(amountTextBox.getText()));
            }
            if (categoryLookUp != null && categoryLookUp.getSelectedData() != null) {
                item.setCategoryItem(categoryLookUp.getSelectedData());
            } else if (categoryLookUp != null && categoryLookUp.getSelectedItemID() != null) {
                PaymentDeductionSelectItem selectItem = new PaymentDeductionSelectItem();
                selectItem.setId(categoryLookUp.getSelectedItem().getId());
                selectItem.setName(categoryLookUp.getSelectedItem().getName());
                item.setCategoryItem(selectItem);
            } else if (categoryLookUp == null && categoryForAll.getSelectedData() != null) {
                item.setCategoryItem(categoryForAll.getSelectedData());
            }
            if (paymentDatePicker != null) {
                item.setAdditionalPaymentDate(new DateNonConvertable(paymentDatePicker.getDate()));
            } else if (paymentDatePicker == null && defaultDateWidget.getDate() != null) {
                item.setAdditionalPaymentDate(new DateNonConvertable(defaultDateWidget.getDate()));
            }

            if (isAdvancedType()) {
                CustomCellTextBox basicOrBasicAllowance = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.BASIC_SALARY);
                CustomCellTextBox percentage = (CustomCellTextBox) table.getColumnById(i, ItemTableConstants.PERCENTAGE);

                BigDecimal value = PayrollClientUtils.parseToBigDecimal(basicOrBasicAllowance != null ? basicOrBasicAllowance.getText() : "");
                item.setPercentage(PayrollClientUtils.parseToBigDecimal(percentage != null ? percentage.getText() : "").setScale(2, RoundingMode.HALF_UP));

                if (isAllowenceType()) {
                    item.setBasicPlusAllowance(value);
                } else if (isBasicSalaryType()) {
                    item.setEmployeeBasicSalary(value);
                }
            } else {
                item.setBasicPlusAllowance(BigDecimal.ZERO);
                item.setEmployeeBasicSalary(BigDecimal.ZERO);
                item.setPercentage(BigDecimal.ZERO);
            }
            if (total != null) {
                item.setTotalAmount(PayrollClientUtils.parseToBigDecimal(total.getText()));
            }
            if (tax != null) {
                item.setTax(PayrollClientUtils.parseToBigDecimal(tax.getText()));
                if (!showInPayslip.getValue() && employeeCell.getTaxCategories() != null && employeeCell.getTaxCategories().size() > 0) {
                    List<PaymentDeductionObject> taxCategories = new ArrayList<>();
                    for (PaymentDeductionObject taxCategory : employeeCell.getTaxCategories()) {
                        BigDecimal payAmount = item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO;
                        taxCategory.setAmount(payAmount.multiply(taxCategory.getPercentage()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                        taxCategories.add(taxCategory);
                        taxTotal = taxTotal.add(taxCategory.getAmount());
                    }
                    item.setTaxCategories(employeeCell.getTaxCategories());
                }
            }
            if (employerContribution != null) {
                item.setEmployerContribution(PayrollClientUtils.parseToBigDecimal(employerContribution.getText()));

                if (!showInPayslip.getValue() && employeeCell.getEmployerContributionCategories() != null && employeeCell.getEmployerContributionCategories().size() > 0) {
                    List<PaymentDeductionObject> employeeContributions = new ArrayList<>();
                    for (PaymentDeductionObject employeeContribution : employeeCell.getEmployerContributionCategories()) {
                        BigDecimal payAmount = item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO;
                        employeeContribution.setAmount(payAmount.multiply(employeeContribution.getPercentage()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                        employeeContributions.add(employeeContribution);
                    }
                    item.setEmployerContributionCategories(employeeContributions);
                }
            }
            if (deduction != null) {
                item.setDeduction(PayrollClientUtils.parseToBigDecimal(deduction.getText()));

                if (!showInPayslip.getValue() && employeeCell.getDeductionCategories() != null && employeeCell.getDeductionCategories().size() > 0) {
                    List<PaymentDeductionObject> customDeductions = new ArrayList<>();
                    for (PaymentDeductionObject customDeduction : employeeCell.getDeductionCategories()) {
                        BigDecimal payAmount = item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO;
                        customDeduction.setAmount(payAmount.subtract(taxTotal).multiply(customDeduction.getPercentage()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                        customDeductions.add(customDeduction);
                    }
                    item.setDeductionCategories(customDeductions);
                }
            }

            Integer currentYear = year.getSelectedId();
            Integer monthDayCount = CalendarUtil.getMonthDaysCount(month.getSelectedId(), currentYear);
            item.setStarttDate(new DateNonConvertable(new Date(currentYear - 1900, month.getSelectedId(), 1)));
            item.setEnddDate(new DateNonConvertable(new Date(currentYear - 1900, month.getSelectedId(), monthDayCount)));

            if (itemCFs != null && !itemCFs.isEmpty()) {
                ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                for (String keyCF : itemCFs.keySet()) {
                    CustomFieldInterface customField = (CustomFieldInterface) table.getColumnById(i, keyCF);

                    if (customField != null) {
                        final CompanyCustomFieldItem companyCustomFieldItem = customField.getFieldItem();
                        final CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                        resultItem.setDataType(companyCustomFieldItem.getDataType());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());
                        resultItem.setColumnCode(keyCF);
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
                    } else if (itemCFsValues.size() > 0 && itemCFsValues.get(keyCF) != null && itemCFsValues.get(keyCF).getUiType() != null) {
                        fieldItems.add(itemCFsValues.get(keyCF));
                    }
                }
                if (!fieldItems.isEmpty()) {
                    item.setItemCustomFields(fieldItems);
                }
            }

            items.add(item);
        }
        return items;
    }


    private Map<String, CompanyCustomFieldItem> setHideValues(PaymentDeductionObject result, Integer lineItemId) {
        Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();
        if (data.getItems() != null && data.getItems().size() > 0) {
            for (PaymentDeductionObject dedductionItem : data.getItems()) {
                if (lineItemId.equals(dedductionItem.getId())) {
                    if (dedductionItem.getPaymentAmount() != null) {
                        result.setPaymentAmount(dedductionItem.getPaymentAmount());
                    }
                    if (dedductionItem.getCategoryItem() != null) {
                        result.setCategoryItem(dedductionItem.getCategoryItem());
                    }
                    if (dedductionItem.getAdditionalPaymentDate() != null) {
                        result.setAdditionalPaymentDate(dedductionItem.getAdditionalPaymentDate());
                    }
                    if (dedductionItem.getEmployeeBasicSalary() != null) {
                        result.setEmployeeBasicSalary(dedductionItem.getEmployeeBasicSalary());
                    }
                    if (dedductionItem.getBasicPlusAllowance() != null) {
                        result.setBasicPlusAllowance(dedductionItem.getBasicPlusAllowance());
                    }
                    if (dedductionItem.getPercentage() != null) {
                        result.setPercentage(dedductionItem.getPercentage());
                    }

                    if (itemCFs != null && dedductionItem.getCustomFieldValuesAsMap() != null) {
                        for (String columnCode : itemCFs.keySet()) {
                            if (dedductionItem.getCustomFieldValuesAsMap().get(columnCode) != null && dedductionItem.getCustomFieldValuesAsMap().get(columnCode).getUiType() != null) {
                                itemCFsValues.put(columnCode, dedductionItem.getCustomFieldValuesAsMap().get(columnCode));
                            }
                        }
                    }
                    break;
                }
            }
        }
        return itemCFsValues;
    }

    private void save(String status) {
        enableButtons(false);

        if (!validate(status)) {
            enableButtons(true);
            return;
        }
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setMonthId(month.getSelectedId());
        filterParametrs.setYear(year.getSelectedId());
        if (EMPLOYEE_TYPE.equals(entityType)) {
            filterParametrs.setEmployeeIDs(multiSelectLookUpForEmployeeType.getSelectedItemsIdsAsString());
        } else if (DEPARTMENT_TYPE.equals(entityType)) {
            filterParametrs.setDepartmentId(lookUp.getSelectedItemID());
        } else if (LOCATION_TYPE.equals(entityType)) {
            filterParametrs.setLocationId(lookUp.getSelectedItemID());
        } else if (GROUP_TYPE.equals(entityType)) {
            filterParametrs.setPayrollBatchID(lookUp.getSelectedItemID());
        } else if (SUPERVISOR_TYPE.equals(entityType)) {
            filterParametrs.setSupervisorId(lookUp.getSelectedItemID());
        }

        if (!isCopyView && objectId != null && month.getSelectedId().equals(data.getMonthID()) && data.getYear().equals(year.getSelectedId()) && (DEPARTMENT_TYPE.equals(entityType) && data.getDepartment() != null && Objects.equals(data.getDepartment().getId(), lookUp.getSelectedItemID()))) {
            saveItem(status);
        } else if (!isCopyView && objectId != null && month.getSelectedId().equals(data.getMonthID()) && data.getYear().equals(year.getSelectedId())) {
            saveItem(status);
        } else {
            PayrollService.App.get().isExistSuchAdditionalPaymentByCategory(filterParametrs, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    enableButtons(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        enableButtons(true);
                        KpiModal modal = new KpiModal();
                        modal.setTitle(wfmStrings.warning());
                        FlexTable flexTable = new FlexTable();
                        Label label1 = new Label();
                        Label label2 = new Label();
                        label2.setText(wfmMessages.areYouSureWantToAddThisAdditionalPayment());
                        flexTable.setWidget(0, 0, label1);
                        flexTable.setWidget(1, 0, label2);
                        modal.add(flexTable);

                        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
                        save.addClickHandler(click -> {
                            modal.close();
                            saveItem(status);
                        });
                        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);
                        cancel.addClickHandler(click -> modal.close());
                        modal.addButton(cancel);
                        modal.addButton(save);
                        modal.open();
                    } else {
                        saveItem(status);
                    }
                }
            });
        }


    }

    private void saveItem(String status) {
        LoadingPanel.loading(true);
        enableButtons(false);
        PayrollService.App.get().saveAdditionalPayment(getFormData(status), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                enableButtons(true);
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.errorOccurredSavingChanges());
            }

            @Override
            public void onSuccess(Void aVoid) {
                closeTab();
                LoadingPanel.loading(false);
                Info.show(Property.get(ADDITIONAL_PAYMENT_LIST, wfmStrings.messSuccessfullySaved(), PayrollConstants.CATEGORY_PAYMENT.equals(type) ? wfmStrings.additionalPayment() : payrollStrings.additionalDeduction()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADDITIONAL_PAYMENT_ADD, null, null);

            }
        });
    }

    private Widget drawPaginationPanel() {
        this.initPaginationWidgets();
        GBoxItem limitField = new GBoxItem(tableLimitListBox);
        limitField.setWidth("100px");

        Icon prevIcon = new Icon();
        prevIcon.setClass("ficon--chevron-left");
        MaterialLink prevLink = new MaterialLink();
        prevLink.setStyleName("btn btn--white btn--icon");
        prevLink.add(prevIcon);

        GBoxItem currentItem = new GBoxItem(tableCurrentBox);
        currentItem.addStyleToComponent("paging__currentpage");

        Icon nextIcon = new Icon();
        nextIcon.setClass("ficon--chevron-right");
        MaterialLink nextLink = new MaterialLink();
        nextLink.setStyleName("btn btn--white btn--icon");
        nextLink.add(nextIcon);

        GBoxRow row = new GBoxRow();
        row.add(new GBoxItem(tablePagingResult));
        row.add(limitField);
        row.add(new GBoxItem(prevLink));
        row.add(currentItem);
        row.add(new GBoxItem(nextLink));

        prevLink.addClickHandler((event) -> {
            Integer totalPagesSize = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
            tableStart -= totalPagesSize;
            if (tableStart <= 0) {
                tableStart = 0;
            }
            loadTableData(true);
        });

        nextLink.addClickHandler((event) -> {
            Integer selectedLimit = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
            int totalPages = totalTableItems / selectedLimit + (totalTableItems % selectedLimit > 0 ? 1 : 0);
            int currentPage = tableStart / selectedLimit + 1;

            if (currentPage >= totalPages) {
                return;
            }
            tableStart += selectedLimit;
            loadTableData(true);
        });

        tableLimitListBox.addValueChangeHandler(event -> {
            tableStart = 0;
            tableCurrent = 0;
            totalTableItems = 0;
            loadTableData(true);
        });
        return row;
    }

    private void initPaginationWidgets() {
        if (tableLimitListBox == null) {
            tableLimitListBox = new DataListBox();
            tableLimitListBox.setWithoutNullLabel(true);
            tableLimitListBox.setItems(new SelectItem[]{
                    new SelectItem(10, "10"),
                    new SelectItem(20, "20"),
                    new SelectItem(30, "30"),
                    new SelectItem(50, "50"),
            });
            tableLimitListBox.setSelected(new SelectItem(20, "20"));
        }
        if (tableCurrentBox == null) {
            tableCurrentBox = new TextBox();
            tableCurrentBox.setStyleName("currLoc form-control");
            tableCurrentBox.setValue(tableCurrent.toString());
        }
        if (tablePagingResult == null) {
            tablePagingResult = new MaterialLink();
            tablePagingResult.setHref("javascript:void(0)");
            tablePagingResult.setClass("btn btn--white");
            tablePagingResult.setText("0 - 0 of 0");
        }
    }

    private void initSearchPanel() {
        tableSearchBox = new TextBox();
        tableSearchBox.addStyleName("gwt-SuggestBox");
        tableSearchBox.setPlaceHolder(wfmStrings.searchEmployee());
        tableSearchBox.addKeyDownHandler((event) -> {
            if (event.getNativeKeyCode() == 13) {
                String searchtext = tableSearchBox.getText();

                if ((searchtext == null || searchtext.trim().isEmpty()) && filterParameter != null && filterParameter.getSqlSearchKey() == null) {
                    return;
                }
                loadTableData(true);
            }
        });
        Span btnSearch = new Span();
        btnSearch.setStyleName("caret");
        btnSearch.ensureDebugId("searchForm__btn");
        btnSearch.addClickHandler((event) -> {
            String searchtext = tableSearchBox.getText();

            if (searchtext == null || searchtext.trim().isEmpty()) {
                return;
            }
            loadTableData(true);
        });
        Div searchDiv = new Div("simpleGwt-ComboBox");
        searchDiv.add(tableSearchBox);
        searchDiv.add(btnSearch);
        addField(SEARCH, searchDiv, null);
    }

    void setPaginationData(Integer total) {
        if (total == null) {
            total = 0;
        }
        totalTableItems = total;

        int pageSize = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
        int position = Optional.ofNullable(tableStart).orElse(0);

        tableCurrent = position / pageSize + 1;

        tableCurrentBox.setValue(tableCurrent.toString());
        tablePagingResult.setText((position + 1) + " - " + ((position + pageSize) < totalTableItems ? (position + pageSize) : totalTableItems) + " " + wfmStrings.of() + " " + totalTableItems);
    }

    private void loadTableData(boolean recalculate) {
        if (objectId != null) {
            LoadingPanel.loading(true);
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setObjectId(objectId);
            fp.setStart(Optional.ofNullable(tableStart).orElse(0));
            fp.setLimit(Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20));
            fp.setSearchKey(tableSearchBox.getText());
            fp.setMonthId(month.getSelectedId());
            fp.setYear(year.getSelectedId());
            fp.setCalculateByLastMonth(calculateByLastMonth.getValue());
            fp.setShowSummaryView(false);
            PayrollService.App.get().getAdditionalPaymentItemsData(fp, new AsyncCallback<AdditionalPayment>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(AdditionalPayment result) {
                    LoadingPanel.loading(false);
                    data = result;
                    setPaginationData(result.getTotalItems());
                    paymentsTableAllowance.removeAllRows();
                    paymentsTable.removeAllRows();
                    if (result.getItems() != null && result.getItems().size() > 0) {
                        for (PaymentDeductionObject item : result.getItems()) {
                            PaymentDeductionObject pdObject = item;
                            String key = item.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + item.getId() : "");
                            if (item.getEmployee() != null && changedItemMap.get(key) != null) {
                                pdObject = changedItemMap.get(key);
                                pdObject.setEmployerContributionCategories(item.getEmployerContributionCategories());
                                pdObject.setAllEmployerContributionCategories(item.getAllEmployerContributionCategories());
                                pdObject.setDeductionCategories(item.getDeductionCategories());
                                pdObject.setAllDeductionCategories(item.getAllDeductionCategories());
                                pdObject.setLgotaBalanceMap(item.getLgotaBalanceMap());
                                pdObject.setTaxCategories(item.getTaxCategories());
                                pdObject.setAllTaxCategories(item.getAllTaxCategories());
                            }
                            if (isAdvancedType()) {
                                paymentsTableAllowance.addRow(getWidgets(pdObject));
                            } else {
                                paymentsTable.addRow(getWidgets(pdObject));
                            }
                        }

                        if (data.getLeaveRequestId() == null) {
                            applyDefaultCategory();
                        }
                        applyDefaultDate();
                        if (fixedAmount != null && fixedAmount.getText() != null && !"".equals(fixedAmount.getText())) {
                            applyPercentage();
                        }
                    }
                }
            });
        } else {
            if ((lookUp != null && lookUp.getSelectedItemID() != null) || EMPLOYEE_TYPE.equals(entityType)) {
                if (EMPLOYEE_TYPE.equals(entityType)) {
                    filterParameter.setEmployeeIDs(multiSelectLookUpForEmployeeType.getSelectedItemsIdsAsString());
                }
                if (DEPARTMENT_TYPE.equals(entityType)) {
                    filterParameter.setDepartmentId(lookUp.getSelectedItemID());
                }
                if (LOCATION_TYPE.equals(entityType)) {
                    filterParameter.setLocationId(lookUp.getSelectedItemID());
                }
                if (GROUP_TYPE.equals(entityType)) {
                    filterParameter.setObjectId(lookUp.getSelectedItemID());
                }
                if (SUPERVISOR_TYPE.equals(entityType)) {
                    filterParameter.setSupervisorId(lookUp.getSelectedItemID());
                }

                filterParameter.setStart(Optional.ofNullable(tableStart).orElse(0));
                filterParameter.setLimit(Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20));
                filterParameter.setSearchKey(tableSearchBox.getText());

                filterParameter.setPaymentCategories(selectedCategories);
                filterParameter.setBasicPlusAllowancePaymentType(isAllowenceType());
                filterParameter.setResignedEmployeesIncluded(false);
                filterParameter.setMonthId(month.getSelectedId());
                filterParameter.setYear(year.getSelectedId());
                filterParameter.setCalculateByLastMonth(calculateByLastMonth.getValue());
                LoadingPanel.loading(true, panel);
                PayrollService.App.get().getEmployeesForAdditionalPayment(filterParameter, recalculate ? null : existingItems, new AsyncCallback<AdditionalPayment>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false, panel);
                    }

                    @Override
                    public void onSuccess(AdditionalPayment result) {
                        LoadingPanel.loading(false);
                        data = result;
                        currency = result.getCurrency();
                        empModeMap.clear();
                        EditableTable table = isAdvancedType() ? paymentsTableAllowance : paymentsTable;
                        table.removeAllRows();
                        setPaginationData(result.getTotalItems());
                        if (result.getItems() != null && result.getItems().size() > 0) {
                            int i = 0;
                            for (PaymentDeductionObject item : result.getItems()) {
                                PaymentDeductionObject pdObject = item;
                                String key = item.getEmployee().getId() + (data != null && data.getOldStatusCode() != null ? "_" + item.getId() : "");
                                if (item.getEmployee() != null && changedItemMap.get(key) != null) {
                                    pdObject = changedItemMap.get(key);
                                    pdObject.setTaxCategories(item.getTaxCategories());
                                    pdObject.setEmployerContributionCategories(item.getEmployerContributionCategories());
                                    pdObject.setDeductionCategories(item.getDeductionCategories());
                                    pdObject.setLgotaBalanceMap(item.getLgotaBalanceMap());
                                    pdObject.setAllTaxCategories(item.getAllTaxCategories());
                                    pdObject.setAllEmployerContributionCategories(item.getAllEmployerContributionCategories());
                                    pdObject.setAllDeductionCategories(item.getAllDeductionCategories());
                                }
                                if (item.getEmployee() != null && item.getEmployee().getId() != null && empModeMap.get(item.getEmployee().getId()) == null) {
                                    empModeMap.put(item.getEmployee().getId(), item.getEmpMode());
                                }
                                table.addRow(getWidgets(pdObject));
                                table.getGridPanel().getGrid().getWidget(i, 1).addStyleName("uploadLinkStyle2");
                                i++;
                            }
                            applyDefaultCategory();
                            applyDefaultDate();
                            if (fixedAmount != null && fixedAmount.getText() != null && recalculate) {
                                applyPercentage();
                            }
                        }
                        LoadingPanel.loading(false, panel);
                    }
                });

                changeReferenceValue();
            }
        }
    }

    private boolean isAdvancedType() {
        return paymentType.isSomethingSelected() && !FIXED_AMOUNT.equals(paymentType.getSelectedItem());
    }

    private boolean isAllowenceType() {
        return BASIC_SALARY_ALLOWANCE.equals(paymentType.getSelectedItem());
    }

    private boolean isBasicSalaryType() {
        return BASIC_SALARY.equals(paymentType.getSelectedItem());
    }

    private static class EmployeeBox extends Div implements CustomCellInterface {
        Integer deductionId;
        SelectItem employee;
        Integer incidentCount;
        BigDecimal basicSalaryPartAmount;
        List<PaymentDeductionObject> allTaxCategories;
        List<PaymentDeductionObject> taxCategories;
        List<PaymentDeductionObject> employerContributionCategories;
        List<PaymentDeductionObject> allEmployerContributionCategories;

        List<PaymentDeductionObject> allDeductionCategories;
        List<PaymentDeductionObject> deductionCategories;
        HashMap<String, BigDecimal> lgotaBalanceMap;
        Span span = new Span();
        CustomCellTextBox textBox = new CustomCellTextBox();

        public EmployeeBox(PaymentDeductionObject item) {
            super();
            this.deductionId = item.getId();
            this.employee = item.getEmployee();
            this.incidentCount = item.getCountIncident();
            this.allTaxCategories = item.getAllTaxCategories();
            this.taxCategories = item.getTaxCategories();
            this.allEmployerContributionCategories = item.getAllEmployerContributionCategories();
            this.employerContributionCategories = item.getEmployerContributionCategories();
            this.allDeductionCategories = item.getAllDeductionCategories();
            this.deductionCategories = item.getDeductionCategories();
            this.basicSalaryPartAmount = item.getBasicSalaryPartAmount();
            this.lgotaBalanceMap = item.getLgotaBalanceMap();

            if (employee.getDescription() != null && !"".equals(employee.getDescription())) {
                textBox.setText(employee.getDescription() + " -> " + employee.getName());
            } else {
                textBox.setText(employee.getName());
            }

            if (incidentCount != null && incidentCount > 0) {
                span.setStyleName("tab-label");
                span.setText(String.valueOf(incidentCount));
                add(span);

                textBox.addFocusHandler(focusEvent -> {
                    if (Utils.hasPermission("HRMS_INCIDENT_LIST")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("starter|incidentList/" + employee.getId() + "/fromEmployeeList/view/");
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT);
                    }
                });
            } else {
                textBox.addFocusHandler(focusEvent -> {
                    if (Utils.hasPermission("HRMS_EMPLOYEE_PROFILE_SUMMARY")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + employee.getId() + "/fromEmployeeList/view/");
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT);
                    }
                });
            }
            add(textBox);
        }

        public void setReadOnly(boolean readOnly) {
            textBox.setReadOnly(readOnly);
        }


        @Override
        public String getDisplayValue() {
            if (Utils.hasGenericAccess(GenericSettingsEnum.SHOW_INCIDENT_COUNT_ADDITIONAL_PAYMENT) && incidentCount != null && incidentCount > 0) {
                return textBox.getText() + " <span class='tab-label'>" + incidentCount + "</span>";
            }
            return textBox.getDisplayValue();
        }

        @Override
        public void setItemValue(Object value) {
            textBox.setItemValue(value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            textBox.setItemFocus(focused);
        }

        public Integer getDeductionId() {
            return deductionId;
        }

        public SelectItem getEmployee() {
            return employee;
        }

        public List<PaymentDeductionObject> getTaxCategories() {
            return this.taxCategories;
        }

        public List<PaymentDeductionObject> getEmployerContributionCategories() {
            return this.employerContributionCategories;
        }

        public List<PaymentDeductionObject> getAllTaxCategories() {
            return this.allTaxCategories;
        }

        public List<PaymentDeductionObject> getAllEmployerContributionCategories() {
            return this.allEmployerContributionCategories;
        }

        public HashMap<String, BigDecimal> getLgotaBalanceMap() {
            return lgotaBalanceMap;
        }

        public BigDecimal getBasicSalaryPartAmount() {
            return this.basicSalaryPartAmount;
        }

        public void setTaxCategories(final List<PaymentDeductionObject> taxCategories) {
            this.taxCategories = taxCategories;
        }

        public void setEmployerContributionCategories(final List<PaymentDeductionObject> employerContributionCategories) {
            this.employerContributionCategories = employerContributionCategories;
        }

        public List<PaymentDeductionObject> getAllDeductionCategories() {
            return allDeductionCategories;
        }

        public void setAllDeductionCategories(List<PaymentDeductionObject> allDeductionCategories) {
            this.allDeductionCategories = allDeductionCategories;
        }

        public List<PaymentDeductionObject> getDeductionCategories() {
            return deductionCategories;
        }

        public void setDeductionCategories(List<PaymentDeductionObject> deductionCategories) {
            this.deductionCategories = deductionCategories;
        }
    }

    private void generatePDF(Integer pdfId) {
        AdditionalPaymentRequestObject requestObject = new AdditionalPaymentRequestObject(data.getObjectID(), null);
        requestObject.setPdfTemplateID(pdfId);
        String pdfURL = CommandConstants.PDF_URL + "/additionalPaymentPdfHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    private void generatePDF(boolean isLandscape) {
        String URL = CommandConstants.PDF_URL + "/additionalPaymentPdfHandler";
        RequestObject requestObject = new RequestObject(data.getObjectID());
        requestObject.setIS_LANDSCAPE(isLandscape);
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, URL, parametrs, "_blank");
    }
}
