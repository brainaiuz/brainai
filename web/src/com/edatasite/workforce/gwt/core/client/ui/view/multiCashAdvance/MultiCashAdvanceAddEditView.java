package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.UL;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentCategoryItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollBatchLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollDepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollLocationLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiCashAdvanceAddEditView extends CustomForm2 implements Colapse, Constants {

    private static final NumberFormat defaultNumberFormat = Utils.getCalculationNumberFormat();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final NumberFormat priceFormat = NumberFormat.getFormat(",##0.00");
    protected final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();
    ArrayList<PaymentDeductionSelectItem> selectedCategories = new ArrayList<>();
    private final String GROUP_TYPE = "group";
    private final String EMPLOYEE_TYPE = "employee";
    private final String DEPARTMENT_TYPE = "department";
    private final String LOCATION_TYPE = "location";
    private final SelectItem FIXED_AMOUNT = new SelectItem(1, wfmStrings.fixedAmount(), "FIXED_AMOUNT");
    private final SelectItem BASIC_SALARY = new SelectItem(2, wfmStrings.basicSalary(), "BASIC_SALARY");
    private final SelectItem BASIC_SALARY_ALLOWANCE = new SelectItem(3, wfmStrings.basicAllowancePay(), "BASIC_SALARY_ALLOWANCE");
    private WfmButton2 submitToManager;
    private WfmButton2 saveAndApprove;
    private WfmButton2 draftButton;
    private final Integer objectID;
    private final String type;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private LookUp lookUp;
    private DatePicker requestedDate;
    private CategoryLookUp categoryLookUp;
    private DataListBox terms, paymentMethod, amountType;
    private Anchor basicAllowanceLabel;
    private TextBox number;
    private KpiModal categoriesDialogBox;
    private Div categoriesList;
    private final List<PaymentCategoryItem> allowanceItems = new ArrayList<>();
    private EditableTable paymentsTable;
    private EditableTable paymentsTableAllowance;
    private Boolean isBasicPaymentType = false;
    private Boolean isAllowenceType = false;
    private TextBox fixedAmount, paymentTerms;
    private FormGroup fixedAmountFormGroup;
    private String statusCode;
    private ChosenApproversWidget approver;
    private MultiCashAdvanceItem multiCashAdvanceItem;
    private HTML totalLabel, totalAmount, totalPaymentLabel, totalPaymentAmount;
    private TotalTable totalsTable;
    private BigDecimal totalReqAmount = BigDecimal.ZERO;
    private BigDecimal totalPayAmount = BigDecimal.ZERO;
    private final HashMap<Integer, BigDecimal> empModeMap = new HashMap<>();
    TextBox tableSearchBox;


    public MultiCashAdvanceAddEditView(String type, Integer objectID) {
        super(MULTI_CASH_ADVANCE_LIST);
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.multiCashAdvance()));
        this.objectID = objectID;
        this.type = type;
    }

    public static int getPriceScale() {
        if (Utils.getAccountingCalculationScale() != null) {
            return Utils.getAccountingCalculationScale();
        }
        return 2;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.MultiCashAdvanceList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                formPropertyMap = result.getFormPropertyMap();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        drawForm();
        LoadingPanel.loading(true);
        AllInOneService.App.get().getMultiCashAdvanceData(objectID, new AbstractAsyncCallback<MultiCashAdvanceItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(MultiCashAdvanceItem result) {
                LoadingPanel.loading(false);

                multiCashAdvanceItem = result;
                if (result.getStatus() != null) {
                    statusCode = result.getStatus().getDescription();
                }
                addButton();
                approver = new ChosenApproversWidget("CASH_ADVANCE", result.getApprover() != null ? objectID : null);
                addField(APPROVERS, approver, getTitle(wfmStrings.approver(), true));

                number.setText(result.getNumber());
                if (result.getPaymentMethods() != null && result.getPaymentMethods().length > 0) {
                    paymentMethod.setItems(result.getPaymentMethods());
                    if (result.getPaymentMethod() != null) {
                        paymentMethod.setSelected(result.getPaymentMethod());
                    }
                }
                if (objectID == null) {

                    amountType.setSelected(FIXED_AMOUNT);
                    onChangePaymentType(true);

                    setDefaultValues();
                    setDefaultValuesByFormProperty();
                } else {
                    if (multiCashAdvanceItem.getEmployee() != null) {
                        lookUp.setSelected(multiCashAdvanceItem.getEmployee());
                    }
                    if (multiCashAdvanceItem.getDate() != null) {
                        requestedDate.setDate(multiCashAdvanceItem.getDate().getNonConvertedDate());
                    }
                    if (multiCashAdvanceItem.getCategoryItem() != null) {
                        categoryLookUp.setSelected(multiCashAdvanceItem.getCategoryItem());
                    }
                    if (multiCashAdvanceItem.getAmountType() != null) {
                        amountType.setSelectedByDescription(multiCashAdvanceItem.getAmountType());
                    }
                    if (multiCashAdvanceItem.getPaymentMethod() != null) {
                        paymentMethod.setSelected(multiCashAdvanceItem.getPaymentMethod());
                    }
                    onChangePaymentType(false);

                    for (CashAdvanceItem item : multiCashAdvanceItem.getCashAdvanceItems()) {
                        if (isBasicPaymentType || isAllowenceType) {
                            paymentsTableAllowance.addRow(getWidgets(null, item));
                        } else {
                            paymentsTable.addRow(getWidgets(null, item));
                        }
                    }
                    updateTotal();
                }
            }
        });
    }

    private void drawForm() {
        drawMainSection();
        drawTableSection();

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
    }

    private void drawMainSection() {

        addTitleField(CustomFormConstants.INFORMATION, wfmStrings.information());

        String name = "";

        if (EMPLOYEE_TYPE.equals(type)) {
            lookUp = new PayrollEmployeeLookUp(false);
            name = Property.get(Constants.EMLOYEE_LIST, wfmStrings.employee());
        } else if (DEPARTMENT_TYPE.equals(type)) {
            lookUp = new PayrollDepartmentLookUp();
            name = Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
        } else if (LOCATION_TYPE.equals(type)) {
            lookUp = new PayrollLocationLookUp();
            name = Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (GROUP_TYPE.equals(type)) {
            lookUp = new PayrollBatchLookUp();
            name = wfmStrings.group();
        }
        lookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeGroup());
        lookUp.showClearButton();
        lookUp.setClearCommand(() -> {
            paymentsTable.removeAllRows();
        });


        if (formPropertyMap != null && formPropertyMap.get(EMPLOYEE) != null) {
            addField(EMPLOYEE, lookUp, getTitle(name, true));
            lookUp.setEnabled(!formPropertyMap.get(EMPLOYEE).isDisabled());
        } else {
            addField(EMPLOYEE, lookUp, getTitle(name, true));
        }


        requestedDate = new DatePicker();
        requestedDate.addStyleName(DEFAULT_WIDTH);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isRequired()));

            requestedDate.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(wfmStrings.date(), true));
        }


        categoryLookUp = new CategoryLookUp(CATEGORY_DEDUCTION, true);
        categoryLookUp.addStyleName(DEFAULT_WIDTH);
        categoryLookUp.getSuggestBox().addSelectionHandler((category) -> applyDefaultCategory());

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, categoryLookUp, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CATEGORY).isRequired()));
            categoryLookUp.setEnabled(!formPropertyMap.get(CATEGORY).isDisabled());
        } else {
            addField(CATEGORY, categoryLookUp, getTitle(wfmStrings.category()));
        }

        paymentMethod = new DataListBox();
        paymentMethod.addStyleName(DEFAULT_WIDTH);

        if (formPropertyMap != null && formPropertyMap.get(PAYMENT_METHOD) != null) {
            addField(PAYMENT_METHOD, paymentMethod, getTitle(formPropertyMap.get(PAYMENT_METHOD).isChanged() ? formPropertyMap.get(PAYMENT_METHOD).getTitle() : wfmStrings.paymentMethod(), formPropertyMap.get(PAYMENT_METHOD).isRequired()));
            paymentMethod.setEnabled(!formPropertyMap.get(PAYMENT_METHOD).isDisabled());
        } else {
            addField(PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        }

        GRow row = new GRow();
        terms = new DataListBox();
        terms.addStyleName(DEFAULT_WIDTH);
        terms.setWithoutNullLabel(true);
        terms.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.fixed() + " " + wfmStrings.amount()),
                new SelectItem(1, wfmStrings.percentage())
        });
        terms.setSelected(new SelectItem(0, wfmStrings.fixed() + " " + wfmStrings.amount()));
        terms.setChangeEvent(() -> {
            applyPercentage();
        });


        paymentTerms = new TextBox();
        Validation.addNumericKeyboardListener(paymentTerms, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);
        paymentTerms.addValueChangeHandler(changeEvent -> {
            applyPercentage();
        });

        row.add(new GColumn(GColumnEnum.COL_6, terms));
        row.add(new GColumn(GColumnEnum.COL_6, paymentTerms));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS, row, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).getTitle() : wfmStrings.paymentTerms(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isRequired()));
            terms.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS, row, getTitle(wfmStrings.paymentTerms()));
        }

        number = new TextBox();
        number.addStyleName(DEFAULT_WIDTH);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()));
            number.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        }

        SelectItem PLEASE_SELECT = new SelectItem(-1, "Please Select ", "PLEASE_SELECT");
        SelectItem[] selectItems = new SelectItem[]{PLEASE_SELECT, FIXED_AMOUNT, BASIC_SALARY, BASIC_SALARY_ALLOWANCE};

        amountType = new DataListBox();
        amountType.setWithoutNullLabel(true);
        amountType.setItems(selectItems);
        amountType.setChangeEvent(() -> {
            onChangePaymentType(true);
        });

        FormGroup amountTypeBox = new FormGroup();
        amountTypeBox.setLabel(formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE) != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).getTitle() : payrollStrings.amountType());
        amountTypeBox.setContent(amountType);

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
            amountType.setEnabled(!formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE, amountTypeBox, null);
        }


        fixedAmount = new TextBox();
        Validation.addNumericKeyboardListener(fixedAmount, 2);
        fixedAmount.addValueChangeHandler(changeEvent -> {
            applyPercentage();
        });
        fixedAmountFormGroup = new FormGroup();
        fixedAmountFormGroup.setVisible(false);
        fixedAmountFormGroup.setContent(fixedAmount);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AMOUNT) != null) {
            addField(CustomFormConstants.AMOUNT, fixedAmountFormGroup, null);
            fixedAmount.setEnabled(!formPropertyMap.get(CustomFormConstants.AMOUNT).isDisabled());
        } else {
            addField(CustomFormConstants.AMOUNT, fixedAmountFormGroup, null);
        }
        initSearchPanel();
    }

    private void drawTableSection() {
        initTables();
        initTotals();

        GColumn cTotalTable = new GColumn(GColumnEnum.COL_3, totalsTable);
        cTotalTable.setOffset(GColumnOffsetEnum.OFFSET_9);

        Div itemsTableContainer = new Div();
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, paymentsTable)));
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, paymentsTableAllowance)));
        if (isBasicPaymentType || isAllowenceType) {
            paymentsTableAllowance.setVisible(true);
            paymentsTable.setVisible(false);
        } else {
            paymentsTableAllowance.setVisible(false);
            paymentsTable.setVisible(true);
        }
        itemsTableContainer.add(new GRow(cTotalTable));
        addField(CustomFormConstants.ITEMS, itemsTableContainer, null, true);
    }

    public void initTables() {

        paymentsTableAllowance = new EditableTable(getColumns(true), true);
        paymentsTableAllowance.setListener(new EditableTableListener() {
            @Override
            public void addRow() {

            }

            @Override
            public void removeRow() {
                updateTotal();
            }
        });

        paymentsTable = new EditableTable(getColumns(false), true);
        paymentsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {

            }

            @Override
            public void removeRow() {
                updateTotal();
            }
        });
    }

    private void initTotals() {
        totalLabel = new HTML("<b>" + wfmStrings.total() + "</b>");
        totalAmount = new HTML("<b>" + format(totalReqAmount) + "</b>");

        totalPaymentLabel = new HTML("<b>" + wfmStrings.paymentAmount() + "</b>");
        totalPaymentAmount = new HTML("<b>" + format(totalPayAmount) + "</b>");

        totalsTable = new TotalTable();
        totalsTable.addItem(totalLabel, totalAmount);
        totalsTable.addItem(totalPaymentLabel, totalPaymentAmount);
    }

    @Override
    protected void addButtons() {

    }

    private void addButton() {
        if (objectID == null || Constants.DRAFT.equals(statusCode)) {
            draftButton = addButton(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> {
                draftButton.setEnabled(false);
                save(Constants.DRAFT);
            });
        }

        submitToManager = addButton(Constants.REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), clickEvent -> {
            submitToManager.setEnabled(false);
            save(Constants.SUBMITTED_TO_MANAGER);
        });

        saveAndApprove = addButton(wfmStrings.approve(), BTN_PRIMARY, clickEvent -> {
            saveAndApprove.setEnabled(false);
            save(Constants.APPROVED);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, MultiCashAdvanceAddEditView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    Integer currentUserId = multiCashAdvanceItem.getCurrentUserId() != null ? multiCashAdvanceItem.getCurrentUserId() : Utils.getUserID();
                    if (itemId != null && currentUserId.equals(itemId)) {
                        saveAndApprove.setVisible(true);
                        submitToManager.setVisible(false);
                    } else {
                        submitToManager.setVisible(true);
                        saveAndApprove.setVisible(false);
                    }
                });
                if (saveAndApprove != null && submitToManager != null && approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApprove.setVisible(true);
                        submitToManager.setVisible(false);
                    } else {
                        saveAndApprove.setVisible(false);
                        submitToManager.setVisible(true);
                    }
                }
            }
        });
    }

    private void onChangePaymentType(boolean isNotFillForm) {
        fixedAmount.setText("");
        String selectedItem = amountType.getSelectedItem() != null ? amountType.getSelectedItem().getDescription() : "";
        if ("FIXED_AMOUNT".equals(selectedItem)) {
            isBasicPaymentType = false;
            isAllowenceType = false;
            fixedAmountFormGroup.setLabel(formPropertyMap.get(CustomFormConstants.AMOUNT) != null && formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : wfmStrings.fixedAmount());
            fixedAmountFormGroup.setVisible(true);
            changeTable(isNotFillForm);
            basicAllowanceLabel.setVisible(false);
        } else if ("BASIC_SALARY".equals(selectedItem)) {
            isBasicPaymentType = true;
            isAllowenceType = false;
            fixedAmountFormGroup.setLabel(formPropertyMap.get(CustomFormConstants.AMOUNT) != null && formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : wfmStrings.percentage() + "(%)");
            fixedAmountFormGroup.setVisible(true);
            changeTable(isNotFillForm);
            basicAllowanceLabel.setVisible(false);
        } else if ("BASIC_SALARY_ALLOWANCE".equals(selectedItem)) {
            isBasicPaymentType = false;
            isAllowenceType = true;
            fixedAmountFormGroup.setLabel(formPropertyMap.get(CustomFormConstants.AMOUNT) != null && formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : wfmStrings.percentage() + "(%)");
            fixedAmountFormGroup.setVisible(true);
            changeTable(isNotFillForm);
            basicAllowanceLabel.setVisible(true);
        } else {
            fixedAmountFormGroup.setVisible(false);
            basicAllowanceLabel.setVisible(false);
        }
        updateTotal();
    }

    private void createBasicAllowancePopUp() {
        categoriesDialogBox = new KpiModal();
        categoriesDialogBox.setTitle("Allowance Details");
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
        PaymentCategoryItem categoryItemSelectAll = new PaymentCategoryItem(new PaymentDeductionSelectItem(-1, wfmStrings.selectAll(), "SELECT_ALL", null));
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
                    PaymentCategoryItem categoryItem = new PaymentCategoryItem(item);
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
    }

    private boolean validation(String status) {
        clearErrorStyle();
        int errors = customValidate();

        if (!Validation.validateLookUpRequired(lookUp)) {
            errors++;
        }
        if (!approver.isValid()) {
            errors++;
        }
        if (!Constants.DRAFT.equals(status) && !Validation.validateDate(requestedDate)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(number)) {
            errors++;
        }
        if (!DRAFT.equals(status)) {
            EditableTable table = isAllowenceType || isBasicPaymentType ? paymentsTableAllowance : paymentsTable;

            for (int i = 0; i < table.getRowCount(); i++) {
                CustomCellTextBox requestAmount = (CustomCellTextBox) table.getColumnById(i, Constants.TOTAL_AMOUNT);
                CustomCellTextBox payAmount = (CustomCellTextBox) table.getColumnById(i, Constants.VARIANCE_AMOUNT);
                CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, Constants.CATEGORY_DEDUCTION);

                if (!Validation.validateTextBoxRequired(requestAmount)) {
                    table.notValid(i, Constants.TOTAL_AMOUNT);
                    errors++;
                } else if (parseToBigDecimal(requestAmount.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                    requestAmount.addStyleName(ERROR_FORM_STYLE);
                    table.notValid(i, Constants.TOTAL_AMOUNT);
                    errors++;
                }


                if (!Validation.validateTextBoxRequired(payAmount)) {
                    table.notValid(i, Constants.VARIANCE_AMOUNT);
                    errors++;
                } else if (parseToBigDecimal(payAmount.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                    payAmount.addStyleName(ERROR_FORM_STYLE);
                    table.notValid(i, Constants.VARIANCE_AMOUNT);
                    errors++;
                }
                if (!Validation.validateLookUpRequired(categoryLookUp)) {
                    table.notValid(i, Constants.CATEGORY_DEDUCTION);
                    errors++;
                }
            }

        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        if (Utils.isCashAdvancesLocked() && DateUtils.getTransactionLockDate().after(requestedDate.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.cashAdvance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void save(String status) {
        enableButtons(false);
        if (!validation(status)) {
            enableButtons(true);
            return;
        }
        MultiCashAdvanceItem multiCashAdvanceItem = new MultiCashAdvanceItem();
        multiCashAdvanceItem.setObjectID(objectID);
        multiCashAdvanceItem.setNumber(number.getValue());
        Integer intNumber = this.multiCashAdvanceItem.getNumberData().parseNumber(number.getText());
        if (intNumber != null) {
            multiCashAdvanceItem.setIntNumber(intNumber);
        }
        if (objectID == null) {
            multiCashAdvanceItem.setCreationDate(new DateNonConvertable(new Date()));
        }
        multiCashAdvanceItem.setType(type);
        multiCashAdvanceItem.setEmployee(lookUp.getSelectedItem());
        multiCashAdvanceItem.setDate(new DateNonConvertable(requestedDate.getDate()));
        multiCashAdvanceItem.setStatus(new SelectItem(status));
        multiCashAdvanceItem.setPaymentTerms(terms.getSelectedItem());
        if (paymentTerms.getText() != null) {
            multiCashAdvanceItem.setPaymentTermsAmount(parseToBigDecimal(paymentTerms.getText()));
        }
        if (Constants.APPROVED.equals(status)) {
            multiCashAdvanceItem.setApprovedDate(new DateNonConvertable(new Date()));
//            multiCashAdvanceItem.setPaidFromAccount(paidFrom.getSelectedItem());
        }
        multiCashAdvanceItem.setCategoryItem(categoryLookUp.getSelectedData());
        multiCashAdvanceItem.setApprovers(approver.getChosenApprovers());
        multiCashAdvanceItem.setPaymentMethod(paymentMethod.getSelectedItem());
        multiCashAdvanceItem.setTotalAmount(totalReqAmount);
        if (amountType.getSelectedItem() != null) {
            multiCashAdvanceItem.setAmountType(amountType.getSelectedItem().getDescription());
            multiCashAdvanceItem.setFixedAmount(parseToBigDecimal(fixedAmount.getText()));
        }

        EditableTable table = isAllowenceType || isBasicPaymentType ? paymentsTableAllowance : paymentsTable;
        List<CashAdvanceItem> cashAdvanceItems = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            EmployeeBox employee = (EmployeeBox) table.getColumnById(i, Constants.EMPLOYEES);
            CustomCellTextBox requestAmount = (CustomCellTextBox) table.getColumnById(i, Constants.TOTAL_AMOUNT);
            CustomCellTextBox payAmount = (CustomCellTextBox) table.getColumnById(i, Constants.VARIANCE_AMOUNT);
            CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, Constants.CATEGORY_DEDUCTION);
            CustomCellTextBox percentage = (CustomCellTextBox) table.getColumnById(i, Constants.PERCENTAGE);
            CustomCellTextBox basicOrAllowanceTextBox = (CustomCellTextBox) table.getColumnById(i, Constants.BASIC_SALARY);

            if (employee != null && employee.getEmployee() != null) {
                CashAdvanceItem cashAdvanceItem = new CashAdvanceItem();
                cashAdvanceItem.setObjectID(employee.getDeductionId());
                if (employee.getDeductionId() == null) {
                    cashAdvanceItem.setCreationDate(new DateNonConvertable(new Date()));
                }
                cashAdvanceItem.setEmployee(employee.getEmployee());
                cashAdvanceItem.setDate(new DateNonConvertable(requestedDate.getDate()));
                cashAdvanceItem.setType("Loan");
                cashAdvanceItem.setPaymentMethod(paymentMethod.getSelectedItem());
                cashAdvanceItem.setTotalAmount(parseToBigDecimal(requestAmount.getText()));
                cashAdvanceItem.setPaymentAmount(parseToBigDecimal(payAmount.getText()));
                cashAdvanceItem.setStatus(new SelectItem(status));
                if (percentage != null) {
                    cashAdvanceItem.setPercentage(parseToBigDecimal(percentage.getText()));
                }
                if (basicOrAllowanceTextBox != null) {
                    cashAdvanceItem.setBasicSalary(parseToBigDecimal(basicOrAllowanceTextBox.getText()));
                }

                if (Constants.APPROVED.equals(status)) {
                    cashAdvanceItem.setApprovedDate(new DateNonConvertable(new Date()));
                }

                cashAdvanceItem.setCategoryItem(categoryLookUp.getSelectedData());
                cashAdvanceItem.setApprovers(approver.getChosenApprovers());
                cashAdvanceItems.add(cashAdvanceItem);
            }
        }
        multiCashAdvanceItem.setCashAdvanceItems(cashAdvanceItems);

        LoadingPanel.loading(true);
        AllInOneService.App.get().saveMultiCashAdvance(multiCashAdvanceItem, false, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    if (CashAdvanceItem.NUMBER_EXISTS.equals(result.getMessage())) {
                        Info.show(wfmStrings.numberAlreadyExist(), Info.Type.WARNING);
                        enableButtons(true);
                    } else if (CashAdvanceItem.NOT_SUFFICIENT_AMOUNT.equalsIgnoreCase(result.getMessage())) {
                        Info.show(wfmStrings.insufficientAmount(), Info.Type.WARNING);
                        enableButtons(true);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, MultiCashAdvanceAddEditView.this);
                        closeTab();
                    }
                }
            }
        });
    }

    private void enableButtons(boolean enable) {
        if (draftButton != null)
            draftButton.setEnabled(enable);
        if (submitToManager != null)
            submitToManager.setEnabled(enable);
        if (saveAndApprove != null)
            saveAndApprove.setEnabled(enable);
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MULTI_CASH_ADVANCE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

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

    @Override
    public String getPropertyCode() {
        return Constants.MULTI_CASH_ADVANCE_LIST;
    }

    private void setDefaultValuesByFormProperty() {
    }

    private void onChangeGroup() {
        if (lookUp.getSelectedItemID() != null) {
            if (EMPLOYEE_TYPE.equals(type)) {
                filterParameter.setEmployeeId(lookUp.getSelectedItemID());
            }
            if (DEPARTMENT_TYPE.equals(type)) {
                filterParameter.setDepartmentId(lookUp.getSelectedItemID());
            }
            if (LOCATION_TYPE.equals(type)) {
                filterParameter.setLocationId(lookUp.getSelectedItemID());
            }
            if (GROUP_TYPE.equals(type)) {
                filterParameter.setObjectId(lookUp.getSelectedItemID());
            }
            filterParameter.setPaymentCategories(selectedCategories);
            filterParameter.setBasicPlusAllowancePaymentType(isAllowenceType);
            filterParameter.setResignedEmployeesIncluded(false);
            filterParameter.setSearchKey(tableSearchBox.getText());
            LoadingPanel.loading(true);
            AllInOneService.App.get().getEmployeesForMultiCashAdvance(filterParameter, new AsyncCallback<ArrayList<PaymentDeductionObject>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ArrayList<PaymentDeductionObject> result) {
                    LoadingPanel.loading(false);
                    empModeMap.clear();
                    paymentsTable.removeAllRows();
                    if (result != null && result.size() > 0) {
                        int i = 0;
                        EditableTable table = isAllowenceType || isBasicPaymentType ? paymentsTableAllowance : paymentsTable;
                        table.removeAllRows();
                        for (PaymentDeductionObject item : result) {
                            if (item.getEmployee() != null && item.getEmployee().getId() != null && empModeMap.get(item.getEmployee().getId()) == null) {
                                empModeMap.put(item.getEmployee().getId(), item.getEmpMode());
                            }
                            table.addRow(getWidgets(item, null));
                            table.getGridPanel().getGrid().getWidget(i, 1).addStyleName("uploadLinkStyle2");
                            i++;
                        }
                        applyDefaultCategory();
                    }
                    updateTotal();
                }
            });
        }
    }

    private void applyDefaultCategory() {
        if (categoryLookUp.getSelectedItem() != null) {
            EditableTable table = isAllowenceType || isBasicPaymentType ? paymentsTableAllowance : paymentsTable;

            for (int i = 0; i < table.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, Constants.CATEGORY_DEDUCTION);
                Integer column = table.getColumnId(Constants.CATEGORY_DEDUCTION);
                categoryLookUp.addCategoryItem(this.categoryLookUp.getSelectedData());
                table.getGrid().getModel().update(i, column, categoryLookUp);
            }
        }
    }

    private void applyPercentage() {

        EditableTable table = isAllowenceType || isBasicPaymentType ? paymentsTableAllowance : paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            EmployeeBox employee = (EmployeeBox) table.getColumnById(i, Constants.EMPLOYEES);
            CustomCellTextBox payAmount = (CustomCellTextBox) table.getColumnById(i, Constants.VARIANCE_AMOUNT);
            CustomCellTextBox amountTextBox = (CustomCellTextBox) table.getColumnById(i, Constants.TOTAL_AMOUNT);
            if (employee != null && employee.getEmployee() != null) {
                BigDecimal empMode = empModeMap.get(employee.getEmployee().getId()) != null ? empModeMap.get(employee.getEmployee().getId()) : BigDecimal.ONE;
                BigDecimal total = null;
                if (isBasicPaymentType || isAllowenceType) {
                    CustomCellTextBox percentage = (CustomCellTextBox) table.getColumnById(i, Constants.PERCENTAGE);

                    CustomCellTextBox basicOrAllowanceTextBox = (CustomCellTextBox) table.getColumnById(i, Constants.BASIC_SALARY);
                    Integer column = table.getColumnId(Constants.PERCENTAGE);
                    Integer amountColumnId = table.getColumnId(Constants.TOTAL_AMOUNT);

                    BigDecimal amonBigDecimal = parseToBigDecimal(basicOrAllowanceTextBox.getText());
                    BigDecimal percentageBigDecimal = parseToBigDecimal(fixedAmount.getText());
                    total = amonBigDecimal.multiply(percentageBigDecimal.divide(BigDecimal.valueOf(100), Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2));
                    percentage.setText(format(percentageBigDecimal));
                    amountTextBox.setText(format(total));
                    table.getGrid().getModel().update(i, column, percentage);
                    table.getGrid().getModel().update(i, amountColumnId, amountTextBox);
                } else {
                    BigDecimal fixedAmountValue = parseToBigDecimal(fixedAmount.getText());

                    Integer amountColumn = table.getColumnId(Constants.TOTAL_AMOUNT);
                    amountTextBox.setValue(formatPrice(fixedAmountValue
                            .multiply(empMode)
                            .setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, RoundingMode.HALF_UP)));
                    table.getGrid().getModel().update(i, amountColumn, amountTextBox);
                }

                if (terms.getSelectedItem().getId() == 1) {
                    total = parseToBigDecimal(amountTextBox.getText());
                    BigDecimal paymentTermsAmount = paymentTerms.getText() != null ? parseToBigDecimal(paymentTerms.getText()) : BigDecimal.ZERO;

                    BigDecimal payAmountValue = total.multiply(paymentTermsAmount
                            .divide(BigDecimal.valueOf(100), Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, RoundingMode.HALF_UP));
                    if (!(isBasicPaymentType || isAllowenceType)) {
                        payAmountValue = payAmountValue.multiply(empMode).setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, RoundingMode.HALF_UP);
                    }
                    payAmount.setText(format(payAmountValue));
                    Integer amountColumnId = table.getColumnId(Constants.VARIANCE_AMOUNT);
                    table.getGrid().getModel().update(i, amountColumnId, payAmount);
                } else {
                    Integer payAmountColumn = table.getColumnId(Constants.VARIANCE_AMOUNT);
                    BigDecimal payAmountValue = parseToBigDecimal(paymentTerms.getText());
                    if (!(isBasicPaymentType || isAllowenceType)) {
                        payAmountValue = payAmountValue.multiply(empMode).setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, RoundingMode.HALF_UP);
                    }
                    payAmount.setValue(formatPrice(payAmountValue));
                    table.getGrid().getModel().update(i, payAmountColumn, payAmount);
                }
            }
        }
        updateTotal();
    }

    private void changeTable(boolean isNotFillForm) {
        if (isBasicPaymentType || isAllowenceType) {
            paymentsTableAllowance.setVisible(true);
            paymentsTable.setVisible(false);
        } else {
            paymentsTableAllowance.setVisible(false);
            paymentsTable.setVisible(true);
        }
        if (isNotFillForm) {
            onChangeGroup();
        }
    }

    private ColumnConfig[] getColumns(boolean isAllowence) {
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();
        columnsList.add(new ColumnConfig(CustomCell.class, Constants.EMPLOYEES, wfmStrings.employee(), 250, false, "left-align-Cell"));

        if (isAllowence) {
            columnsList.add(new ColumnConfig(CustomCell.class, Constants.BASIC_SALARY, wfmStrings.basicSalary(), 90, false, "right-align-Cell"));
            columnsList.add(new ColumnConfig(CustomCell.class, Constants.PERCENTAGE, wfmStrings.percentage() + " (%)", 80, false, "right-align-Cell"));

        }

        columnsList.add(new ColumnConfig(CustomCell.class, Constants.TOTAL_AMOUNT, wfmStrings.requestedAmount(), 90, false, "right-align-Cell"));
        columnsList.add(new ColumnConfig(CustomCell.class, Constants.VARIANCE_AMOUNT
                , wfmStrings.paymentAmount(), 90, false, "right-align-Cell"));
        columnsList.add(new ColumnConfig(LookUpCell.class, Constants.CATEGORY_DEDUCTION, wfmStrings.category(), 130, false, "left-align-Cell"));

        return columnsList.toArray(new ColumnConfig[]{});
    }

    private Object[] getWidgets(PaymentDeductionObject item, CashAdvanceItem cashAdvanceItem) {
        EmployeeBox employeeBox = new EmployeeBox(cashAdvanceItem != null ? cashAdvanceItem.getObjectID() : item.getId(), cashAdvanceItem != null ? cashAdvanceItem.getEmployee() : item.getEmployee());
        employeeBox.setEnabled(true);
        employeeBox.addStyleName(DEFAULT_WIDTH);
        employeeBox.setStyleName("file--AdditionalPaymentUIBinder");

        employeeBox.setReadOnly(true);
        employeeBox.addFocusHandler(focusEvent -> {
            if (cashAdvanceItem != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + cashAdvanceItem.getEmployee().getId() + "/fromEmployeeList/view/");
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + item.getEmployee().getId() + "/fromEmployeeList/view/");
            }
        });
        employeeBox.addMouseOverHandler(mouseOverEvent -> employeeBox.addStyleName("uploadLinkStyle2"));
        employeeBox.addMouseOutHandler(mouseOutEvent -> employeeBox.removeStyleName("uploadLinkStyle2"));

        CustomCellTextBox requestAmountTextBox = new CustomCellTextBox(true);
        requestAmountTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        requestAmountTextBox.setWidth("100%");
        Validation.addNumericKeyboardListener(requestAmountTextBox, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);
        if (cashAdvanceItem != null && cashAdvanceItem.getTotalAmount() != null) {
            requestAmountTextBox.setText(format(cashAdvanceItem.getTotalAmount()));
        }
        requestAmountTextBox.addChangeHandler(changeEvent -> updateTotal());
        requestAmountTextBox.setEnabled(!(Constants.APPROVED.equals(statusCode) || Constants.POSTED.equals(statusCode)));

        CustomCellTextBox paymentAmount = new CustomCellTextBox(true);
        paymentAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        paymentAmount.setWidth("100%");
        Validation.addNumericKeyboardListener(paymentAmount, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);
        paymentAmount.setText(format(cashAdvanceItem != null && cashAdvanceItem.getPaymentAmount() != null ? cashAdvanceItem.getPaymentAmount() : BigDecimal.ZERO));
        paymentAmount.addChangeHandler(changeEvent -> updateTotal());
        paymentAmount.setEnabled(!(Constants.APPROVED.equals(statusCode) || Constants.POSTED.equals(statusCode)));

        CategoryLookUp categoryLookUp = new CategoryLookUp(CATEGORY_DEDUCTION, true);
        categoryLookUp.setWidth(Constants.NORMAL_WIDTH);
        categoryLookUp.getSuggestBox().getElement().getStyle().setWidth(100, Style.Unit.PCT);
        categoryLookUp.getSuggestBox().getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        if (cashAdvanceItem != null && cashAdvanceItem.getCategoryItem() != null) {
            categoryLookUp.addCategoryItem(cashAdvanceItem.getCategoryItem());
        }
        categoryLookUp.setEnabled(!(Constants.APPROVED.equals(statusCode) || Constants.POSTED.equals(statusCode)));


        BigDecimal amount = null;
        if (item != null) {
            amount = isAllowenceType ? item.getBasicPlusAllowance() : item.getEmployeeBasicSalary();
        } else if (cashAdvanceItem != null) {
            amount = cashAdvanceItem.getBasicSalary();
        }
        amount = amount != null ? amount : BigDecimal.ZERO;
        //By Commission fields
        CustomCellTextBox basicSalaryTextBox = new CustomCellTextBox(true);
        basicSalaryTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        basicSalaryTextBox.setWidth("100%");
        Validation.addNumericKeyboardListener(basicSalaryTextBox, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);
        basicSalaryTextBox.setText(format(amount));
        basicSalaryTextBox.setEnabled(!(Constants.APPROVED.equals(statusCode) || Constants.POSTED.equals(statusCode)));

        BigDecimal percentage = BigDecimal.ZERO;
        if (item != null && item.getPercentage() != null) {
            percentage = item.getPercentage();
        } else if (cashAdvanceItem != null && cashAdvanceItem.getPercentage() != null) {
            percentage = cashAdvanceItem.getPercentage().setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, RoundingMode.HALF_UP);
        }
        CustomCellTextBox percentageTextBox = new CustomCellTextBox(true);
        percentageTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        percentageTextBox.setWidth("100%");
        Validation.addNumericKeyboardListener(percentageTextBox, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);
        percentageTextBox.setText(format(percentage));
        percentageTextBox.setEnabled(!(Constants.APPROVED.equals(statusCode) || Constants.POSTED.equals(statusCode)));
        percentageTextBox.addChangeHandler(e -> {
            BigDecimal total = null;
            BigDecimal amonBigDecimal = parseToBigDecimal(basicSalaryTextBox.getText());
            BigDecimal percentageBigDecimal = parseToBigDecimal(percentageTextBox.getText());
            total = amonBigDecimal.multiply(percentageBigDecimal.divide(BigDecimal.valueOf(100), Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2));
            requestAmountTextBox.setText(format(total));
            paymentsTableAllowance.refreshCustomCellDisplayValue(paymentsTableAllowance.getGrid().getCurrentRow(), Constants.TOTAL_AMOUNT);
            updateTotal();
        });
        basicSalaryTextBox.addChangeHandler(e -> {
            BigDecimal total = null;
            BigDecimal amonBigDecimal = parseToBigDecimal(basicSalaryTextBox.getText());
            BigDecimal percentageBigDecimal = parseToBigDecimal(percentageTextBox.getText());
            total = amonBigDecimal.multiply(percentageBigDecimal.divide(BigDecimal.valueOf(100), Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2));
            requestAmountTextBox.setText(format(total));
            paymentsTableAllowance.refreshCustomCellDisplayValue(paymentsTableAllowance.getGrid().getCurrentRow(), Constants.TOTAL_AMOUNT);
            updateTotal();
        });
        if (isBasicPaymentType || isAllowenceType) {
            return new Object[]{employeeBox, basicSalaryTextBox, percentageTextBox, requestAmountTextBox, paymentAmount, categoryLookUp};
        }
        return new Object[]{employeeBox, requestAmountTextBox, paymentAmount, categoryLookUp};
    }

    private void updateTotal() {
        totalReqAmount = BigDecimal.ZERO;
        totalPayAmount = BigDecimal.ZERO;
        EditableTable table = isAllowenceType || isBasicPaymentType ? paymentsTableAllowance : paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            EmployeeBox employee = (EmployeeBox) table.getColumnById(i, Constants.EMPLOYEES);
            CustomCellTextBox requestAmount = (CustomCellTextBox) table.getColumnById(i, Constants.TOTAL_AMOUNT);
            CustomCellTextBox payAmount = (CustomCellTextBox) table.getColumnById(i, Constants.VARIANCE_AMOUNT);

            if (employee != null && employee.getEmployee() != null) {
                BigDecimal reqAmount = parseToBigDecimal(requestAmount.getText());
                totalReqAmount = totalReqAmount.add(reqAmount);
                if (reqAmount.compareTo(BigDecimal.ZERO) > 0) {
                    totalPayAmount = totalPayAmount.add(parseToBigDecimal(payAmount.getText()));
                }
            }
        }
        totalAmount.setHTML(formatPrice(totalReqAmount));
        totalPaymentAmount.setHTML(formatPrice(totalPayAmount));
    }

    public String format(BigDecimal bigDecimal) {
        return defaultNumberFormat.format(bigDecimal.setScale(getPriceScale(), BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            return new BigDecimal(Utils.universalParse(priceFormat, text));
        }
        return BigDecimal.ZERO;
    }

    public String formatPrice(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return priceFormat.format(bigDecimal.setScale(getPriceScale(), BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return "";
    }

    private class EmployeeBox extends EditableTextBox {
        Integer deductionId;
        SelectItem employee;

        public EmployeeBox(Integer deductionId, SelectItem employee) {
            super();
            this.deductionId = deductionId;
            this.employee = employee;
            if (employee.getDescription() != null && !"".equals(employee.getDescription())) {
                setText(employee.getDescription() + " -> " + employee.getName());
            } else {
                setText(employee.getName());
            }
        }

        public Integer getDeductionId() {
            return deductionId;
        }

        public SelectItem getEmployee() {
            return employee;
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
                onChangeGroup();
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
            onChangeGroup();
        });
        Div searchDiv = new Div("simpleGwt-ComboBox");
        searchDiv.add(tableSearchBox);
        searchDiv.add(btnSearch);
        addField(SEARCH, searchDiv, null);
    }
}