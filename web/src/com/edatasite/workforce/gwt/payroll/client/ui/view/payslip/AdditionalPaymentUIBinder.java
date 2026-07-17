package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AdditionalPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.UL;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentCategoryItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollBatchLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollCategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollDepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollLocationLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollSupervisorLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
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
import com.edatasite.workforce.gwt.payroll.client.ui.view.AddEditAdditionalPaymentView;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Shohruh on 28 Oct 2016.
 */
public class AdditionalPaymentUIBinder implements Constants {
    public Widget getNoteHistoryWidget() {
        return noteHistoryWidget;
    }

    interface IAdditionalPaymentUIBinder extends UiBinder<HTMLPanel, AdditionalPaymentUIBinder> {
    }

    private static final IAdditionalPaymentUIBinder ourUiBinder = GWT.create(IAdditionalPaymentUIBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final CoreMessages coreMessages = CoreMessages.App.get();
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private final DateTimeFormat format_month = DateTimeFormat.getFormat("MMMM");
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");
    public static final BigDecimal HUNDRED = new BigDecimal(100);

    private final SelectItem GROUP_TYPE;
    private final SelectItem EMPLOYEE_TYPE;
    private final SelectItem DEPARTMENT_TYPE;
    private final SelectItem LOCATION_TYPE;
    private final SelectItem SUPERVISOR_TYPE;
    private final SelectItem BY_PAYMENT;

    private final SelectItem FIXED_AMOUNT;
    private final SelectItem BASIC_SALARY;
    private final SelectItem BASIC_SALARY_ALLOWANCE;
//    private SelectItem BASIC_SALARY_ALLOWANCE;

    private final HTMLPanel rootElement;

    //Columns
    private static final String pType = "pType_";
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();

    @UiField
    HTMLPanel panel;
    //    @UiField
    Div categoryPanel;
    @UiField
    Div groupPanel;
    //    @UiField
    Div defaultCategoryPanel;
    @UiField
    Div approverPanel;
    @UiField
    Div monthPanel;
    @UiField
    Div yearPanel;
    //    @UiField
    Div showInPayslipPanel;
    //    @UiField
    Div referencePanel;
    @UiField
    VerticalPanel itemsTable;
    @UiField
    Div totalPanel;
    //    @UiField
    Div defaultDatePanel;
    @UiField
    Div paymentTypePanel;
    @UiField
    Div paymentTypeInputBox;
    @UiField
    Div showAdditionalFields;

    private FormGroup groupBox;
    private TextBox referenceTextBox;
    private ChosenApproversWidget approver;
    private PayrollBatchLookUp payrollBatchLookUp;
    private PayrollEmployeeLookUp employeeLookUp;
    private PayrollDepartmentLookUp departmentLookUp;
    private PayrollLocationLookUp locationLookUp;
    private PayrollSupervisorLookUp supervisorLookUp;
    private DataListBox month, year, groupType, paymentType;
    private PayrollCategoryLookUp categoryForAll;
    private EditableTable paymentsTable;
    private KpiCheckBox showInPayslip;
    private FormGroup showInPayslipField;
    private HTML totalLabel, totalAmount;
    private WfmButton2 draftButton, submitButton, approveButton, declineButton, editButton;
    private Div editDiv, draftDiv, submitDiv, approveDiv, declineDiv, pdfDiv;
    private SplitButton pdfButton;
    private FormGroup paymentTypeBox;
    private FormGroup fixedAmountBox;
    private FormGroup basicAllowanceBox;
    private Anchor basicAllowanceLabel;
    private TextBox fixedAmountInput;
    private FormGroup basicSalaryBox;
    private TextBox percentageInput;
    private KpiModal categoriesDialogBox;
    ArrayList<PaymentDeductionSelectItem> selectedCategories = new ArrayList<>();
    private final ArrayList<HistoryListItem> historyNodes = new ArrayList<>();
//    private PayrollPdfPanel pdfPanel;

    private Command groupChangeEvent, categoryChangeEvent;
    private AdditionalPayment data;
    private List<PaymentDeductionObject> items;
    private final Integer objectId;
    private final Integer employeeId;
    private BigDecimal total = BigDecimal.ZERO;
    private CurrencyItem currency;
    private boolean summaryView = false;
    private boolean isCopyView = false;
    private final String type;
    private DatePicker defaultDateWidget;
    private Boolean isBasicPaymentType = false;
    private Boolean isAllowenceType = false;
    private Div categoriesList;
    private final List<PaymentCategoryItem> allowanceItems = new ArrayList<>();
    private Boolean isFillFromMethod = false;
    private final String categoryType;
    public InvoiceAdvancedOptions advancedOptions;
    private String defaultPaymentType;
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();
    private NoteHistoryWidget noteHistoryWidget;

    private final AddEditAdditionalPaymentView.AdditionalPaymentInterface viewInterface;
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();
    private final HashMap<Integer, BigDecimal> empModeMap = new HashMap<>();

    public AdditionalPaymentUIBinder(AddEditAdditionalPaymentView.AdditionalPaymentInterface viewInterface, Integer objectId, Integer employeeId, boolean summaryView, String type, String categoryType, boolean isCopyView) {
        rootElement = ourUiBinder.createAndBindUi(this);
        rootElement.setStyleName("content-box content-box--white");
        this.viewInterface = viewInterface;
        this.objectId = objectId;
        this.employeeId = employeeId;
        this.summaryView = summaryView;
        this.isCopyView = isCopyView;
        this.type = type;
        this.categoryType = categoryType;

        GROUP_TYPE = new SelectItem(1, wfmStrings.group(), "group");
        EMPLOYEE_TYPE = new SelectItem(2, wfmStrings.employee(), "employee");
        DEPARTMENT_TYPE = new SelectItem(3, wfmStrings.department(), "department");
        LOCATION_TYPE = new SelectItem(4, wfmStrings.location(), "location");
        SUPERVISOR_TYPE = new SelectItem(5, wfmStrings.supervisor(), "supervisor");

        BY_PAYMENT = new SelectItem(1, PayrollConstants.CATEGORY_PAYMENT.equals(type) ? Property.get(ADDITIONAL_PAYMENT_LIST, wfmStrings.additionalPayment()) : payrollStrings.additionalDeduction());

        FIXED_AMOUNT = new SelectItem(1, wfmStrings.fixedAmount(), "FIXED_AMOUNT");
        BASIC_SALARY = new SelectItem(2, wfmStrings.basicSalary(), "BASIC_SALARY");
        BASIC_SALARY_ALLOWANCE = new SelectItem(3, wfmStrings.basicAllowancePay(), "BASIC_SALARY_ALLOWANCE");
    }

    public void init() {
        referenceTextBox = new TextBox();
        referenceTextBox.setEnabled(!summaryView);

        payrollBatchLookUp = new PayrollBatchLookUp();
        payrollBatchLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeGroup());
        if (!summaryView) {
            payrollBatchLookUp.showClearButton();
            payrollBatchLookUp.setClearCommand(() -> {
                paymentsTable.removeAllRows();
            });
        }
        payrollBatchLookUp.setEnabled(!summaryView);

        employeeLookUp = new PayrollEmployeeLookUp(false);
        employeeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeGroup());
        if (!summaryView) {
            employeeLookUp.showClearButton();
            employeeLookUp.setClearCommand(() -> {
                paymentsTable.removeAllRows();
            });
        }
        employeeLookUp.setEnabled(!summaryView);

        departmentLookUp = new PayrollDepartmentLookUp();
        departmentLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeGroup());
        if (!summaryView) {
            departmentLookUp.showClearButton();
            departmentLookUp.setClearCommand(() -> {
                paymentsTable.removeAllRows();
            });
        }
        departmentLookUp.setEnabled(!summaryView);

        locationLookUp = new PayrollLocationLookUp();
        locationLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeGroup());
        if (!summaryView) {
            locationLookUp.showClearButton();
            locationLookUp.setClearCommand(() -> {
                paymentsTable.removeAllRows();
            });
        }
        locationLookUp.setEnabled(!summaryView);

        supervisorLookUp = new PayrollSupervisorLookUp();

        if (SUPERVISOR_TYPE.getDescription().equals(categoryType)) {
            supervisorLookUp.addItem(new SelectItem(Utils.getUserID(), Utils.getUserFullName()));
            supervisorLookUp.setSelected(new SelectItem(Utils.getUserID(), Utils.getUserFullName()));

            if (supervisorLookUp.isSelected()) {
                onChangeGroup();
            }

            if (!summaryView) {
                supervisorLookUp.showClearButton();
                supervisorLookUp.setClearCommand(() -> {
                    paymentsTable.removeAllRows();
                });
            }
        }
        supervisorLookUp.setEnabled(false);

        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
            groupBox = new FormGroup(wfmStrings.group(), payrollBatchLookUp);
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
            groupBox = new FormGroup(wfmStrings.employee(), employeeLookUp);
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD)) {
            groupBox = new FormGroup(wfmStrings.department(), departmentLookUp);
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
            groupBox = new FormGroup(wfmStrings.location(), locationLookUp);
        } else if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD)) {
            groupBox = new FormGroup(wfmStrings.supervisor(), supervisorLookUp);
        } else {
            groupBox = new FormGroup();
        }

        approver = new ChosenApproversWidget(RelationItem.TYPE_ADDITIONAL_PAYMENT, objectId);

        month = new DataListBox();
        month.setEnabled(!summaryView);
        setMonthItems();
        month.setChangeEvent(this::onChangeMonthEndYear);

        year = new DataListBox();
        year.setWithoutNullLabel(true);
        year.setEnabled(!summaryView);
        setYearItems();
        year.setSelected(Integer.valueOf(format_year.format(new Date())));
        year.setChangeEvent(this::onChangeMonthEndYear);

        categoryForAll = new PayrollCategoryLookUp(type);
        categoryForAll.setWidth("100%");
        categoryForAll.getSuggestBox().addSelectionHandler(selectionEvent -> applyDefaultCategory());

        showInPayslip = new KpiCheckBox();
        showInPayslip.setEnabled(!summaryView);
        showInPayslip.setValue(true);
        showInPayslipField = new FormGroup(wfmStrings.showInPayslip(), showInPayslip);
        showInPayslipField.setVisible(false);

        defaultDateWidget = new DatePicker();
        defaultDateWidget.addChangeHandler(event -> applyDefaultDate());

        initDefaultDateFormat();
        initPaymentType();
        initDefaultType(defaultPaymentType);
        initGroupType();
        initCategoryType();
        initTotals();
        initButtonsPanel();
        createNoteHistoryWidget();

        groupPanel.add(groupBox);
//        defaultCategoryPanel.add(new FormGroup(wfmStrings.defaultCategory(), categoryForAll));
        approverPanel.add(new FormGroup(wfmStrings.approver(), approver));
        monthPanel.add(new FormGroup(wfmStrings.period(), month));
        yearPanel.add(new FormGroup(year));
//        showInPayslipPanel.add(new FormGroup(wfmStrings.showInPayslip(), showInPayslipField));
//        showInPayslipPanel.add(showInPayslipField);
//        referencePanel.add(new FormGroup(wfmStrings.reference(), referenceTextBox));

        advancedOptions = createAdvancedOptions();
        MaterialLink showFields = new MaterialLink(wfmStrings.showAdditionalFields());
        showFields.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
        showFields.addStyleName("btn-flat ExpenseAddEditVIew");
        FormGroup showMoreField = new FormGroup(showFields);
        showAdditionalFields.add(showMoreField);

        if (summaryView) {
            basicSalaryBox.setEnabled(false);
            categoryForAll.setEnabled(false);
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, viewInterface.getView(), (sender, args) -> {
            if (summaryView) {
                approver.setEnabled(false);
            }
        });
    }

    private void createNoteHistoryWidget() {
        noteHistoryWidget = new NoteHistoryWidget(callback -> PayrollService.App.get().loadPaymentNotes(objectId, callback));
        noteHistoryWidget.setSaveIntoDatabase(noteHistory -> {
            LoadingPanel.loading(true);
            PayrollService.App.get().createPaymentHistoryNote(objectId, noteHistory, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer hisItemId) {
                    noteHistory.setObjectID(hisItemId);
                    LoadingPanel.loading(false);
                }
            });
        });
    }


    protected InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                result.add(new FormGroup(payrollStrings.defaultCategory(), categoryForAll));
                result.add(new FormGroup(wfmStrings.paymentDate(), defaultDateWidget));
                result.add(new FormGroup(wfmStrings.reference(), referenceTextBox));
                result.add(new FormGroup(showInPayslipField));
                return result;
            }
        }, false);
    }


    private void initDefaultDateFormat() {
//        FormGroup group = new FormGroup(wfmStrings.paymentDate(), defaultDateWidget);
//        defaultDatePanel.add(group);
        if (summaryView) {
//            defaultDatePanel.setVisible(false);
        }
    }

    private void initDefaultType(String def) {
        if (def != null) {
            paymentType.setSelectedByDescription(def);
            if ("FIXED_AMOUNT".equals(def)) {
                isBasicPaymentType = false;
                isAllowenceType = false;
                createFixedAmountWidgets();
            } else if ("BASIC_SALARY".equals(def)) {
                isBasicPaymentType = true;
                isAllowenceType = false;
                createBasicSalaryWidgets();
            } else if ("BASIC_SALARY_ALLOWANCE".equals(def)) {
                isBasicPaymentType = false;
                isAllowenceType = true;
                createBasicSalaryAndAllowanceWidgets();
            } else {
                fixedAmountBox.setVisible(false);
                basicSalaryBox.setVisible(false);
                basicAllowanceLabel.setVisible(false);
            }
        }
    }

    private void initPaymentType() {
        SelectItem PLEASE_SELECT = new SelectItem(-1, "Please Select ", "PLEASE_SELECT");
        SelectItem[] selectItems = new SelectItem[]{PLEASE_SELECT, FIXED_AMOUNT, BASIC_SALARY, BASIC_SALARY_ALLOWANCE};

        paymentType = new DataListBox();
        paymentType.setWithoutNullLabel(true);
        paymentType.setItems(selectItems);
        paymentType.setChangeEvent(this::onChangePaymentType);

        paymentTypeBox = new FormGroup();
        paymentTypeBox.setLabel(wfmStrings.paymentType());
        paymentTypeBox.setContent(paymentType);
        paymentTypePanel.add(paymentTypeBox);

        fixedAmountInput = new TextBox();
        Validation.addNumericKeyboardListener(fixedAmountInput, 2);
        fixedAmountBox = new FormGroup(wfmStrings.fixedAmount(), fixedAmountInput);
        fixedAmountBox.setVisible(false);

        percentageInput = new TextBox();
        Validation.checkToFocusTextBox(percentageInput, NumberFormat.getFormat(",##0").format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(percentageInput, 2);
        percentageInput.addValueChangeHandler(event -> {
            applyPercentage(percentageInput.getText());
        });
        basicSalaryBox = new FormGroup(wfmStrings.percentage() + "(%)", percentageInput);
        basicSalaryBox.setVisible(false);

        basicAllowanceLabel = new Anchor(wfmStrings.basicAllowancePay());
        basicAllowanceLabel.getElement().getStyle().setProperty("color", "#1071e3");
        basicAllowanceLabel.getElement().getStyle().setProperty("cursor", "pointer");
        basicAllowanceLabel.getElement().getStyle().setProperty("paddingLeft", "5px");
        basicAllowanceLabel.addClickHandler(clickEvent -> createBasicAllowanceWidgets());
        paymentTypeBox.getGroupLabel().add(basicAllowanceLabel);

        createBasicAllowancePopUp();
        setPaymentTypeValues();
//        paymentType.setSelectedByDescription(defaultPaymentType);
//        onChangePaymentType();
        paymentTypeInputBox.add(fixedAmountBox);
        paymentTypeInputBox.add(basicSalaryBox);
//        paymentTypeInputBox.add(basicAllowanceLabel);
        if (summaryView) {
            fixedAmountInput.setEnabled(false);
            percentageInput.setEnabled(false);
            basicAllowanceLabel.setVisible(false);
        }
        if (Utils.hasPermission(PermissionConstants.PAYMENT_TYPE_EDIT)) {
            paymentType.setEnabled(!summaryView);
        } else {
            paymentType.setEnabled(false);
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
        if (summaryView) {
            basicAllowanceLabel.setVisible(false);
        }
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
        onChangeGroup();
    }

    private void createBasicAllowanceWidgets() {
        categoriesDialogBox.open();
    }

    private void onChangePaymentType() {
        String selectedItem = paymentType.getSelectedItem() != null ? paymentType.getSelectedItem().getDescription() : "";
        if ("FIXED_AMOUNT".equals(selectedItem)) {
            isBasicPaymentType = false;
            isAllowenceType = false;
            createFixedAmountWidgets();
            changeTable(true);
        } else if ("BASIC_SALARY".equals(selectedItem)) {
            isBasicPaymentType = true;
            isAllowenceType = false;
            createBasicSalaryWidgets();
            changeTable(true);
        } else if ("BASIC_SALARY_ALLOWANCE".equals(selectedItem)) {
            isBasicPaymentType = false;
            isAllowenceType = true;
            createBasicSalaryAndAllowanceWidgets();
            changeTable(true);
        } else {
            fixedAmountBox.setVisible(false);
            basicSalaryBox.setVisible(false);
            basicAllowanceLabel.setVisible(false);
        }
        updateTotal();
    }

    private void changeTable(boolean isNotFillForm) {
        paymentsTable.removeFromParent();
        initTables(isNotFillForm);
        if (isNotFillForm) {
            onChangeGroup();
        }
    }

    private void createBasicSalaryAndAllowanceWidgets() {
        filterParameter.setBasicPlusAllowancePaymentType(true);
        basicSalaryBox.setVisible(true);
        fixedAmountBox.setVisible(false);
        basicAllowanceLabel.setVisible(true);
    }

    private void createBasicSalaryWidgets() {
        filterParameter.setBasicPlusAllowancePaymentType(false);
        basicSalaryBox.setVisible(true);
        fixedAmountBox.setVisible(false);
        basicAllowanceLabel.setVisible(false);
        percentageInput.addChangeHandler(event -> {
            applyPercentage(percentageInput.getText());
        });
    }

    private void applyPercentage(String text) {
        if (text == null || text.isEmpty()) {
            text = "0";
        }
//        double value = PayrollClientUtils.parseToBigDecimal(text).doubleValue();
        EditableTable table = paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            EditableTextBox percentage = (EditableTextBox) table.getColumnById(i, ItemTableConstants.PERCENTAGE);
            EditableTextBox amountTextBox = (EditableTextBox) table.getColumnById(i, ItemTableConstants.AMOUNT);
            EditableTextBox basicOrAllowanceTextBox = isAllowence() ? (EditableTextBox) table.getColumnById(i, ItemTableConstants.ALLOWANCE) : (EditableTextBox) table.getColumnById(i, ItemTableConstants.BASIC_SALARY);
            Integer column = table.getColumnId(ItemTableConstants.PERCENTAGE);
            Integer amountColumnId = table.getColumnId(ItemTableConstants.AMOUNT);
//            double amountValue = Double.parseDouble(basicOrAllowanceTextBox.getText());
//            double finalAmount = amountValue * (value / 100);
            BigDecimal amonBigDecimal = null;
            BigDecimal percentageBigDecimal = PayrollClientUtils.parseToBigDecimal(percentageInput.getText());
            if (basicOrAllowanceTextBox != null) {
                amonBigDecimal = PayrollClientUtils.parseToBigDecimal(basicOrAllowanceTextBox.getText());
                BigDecimal total = amonBigDecimal.multiply(percentageBigDecimal.divide(new BigDecimal(100), 2, RoundingMode.CEILING));
                amountTextBox.setText(PayrollClientUtils.numberFormat(total));
                table.getGrid().getModel().update(i, amountColumnId, amountTextBox);
            }
//            changePerAmount.setText(PayrollClientUtils.format(total));
            percentage.setText(PayrollClientUtils.numberFormat(percentageBigDecimal));
            percentageInput.setText(PayrollClientUtils.numberFormat(percentageBigDecimal));
            table.getGrid().getModel().update(i, column, percentage);
        }

        updateTotal();

    }


    private void applyAmountTextBox(Integer rowIndex) {
        EditableTable table = paymentsTable;
        EditableTextBox percentage = (EditableTextBox) table.getColumnById(rowIndex, ItemTableConstants.PERCENTAGE);
        EditableTextBox amountTextBox = (EditableTextBox) table.getColumnById(rowIndex, ItemTableConstants.AMOUNT);
        EditableTextBox basicOrAllowanceTextBox = isAllowence() ? (EditableTextBox) table.getColumnById(rowIndex, ItemTableConstants.ALLOWANCE) : (EditableTextBox) table.getColumnById(rowIndex, ItemTableConstants.BASIC_SALARY);
        Integer column = table.getColumnId(ItemTableConstants.PERCENTAGE);
        Integer amountColumnId = table.getColumnId(ItemTableConstants.AMOUNT);
        BigDecimal amountBigDecimal = PayrollClientUtils.parseToBigDecimal(amountTextBox.getText());
        BigDecimal amonBigDecimal = PayrollClientUtils.parseToBigDecimal(basicOrAllowanceTextBox != null ? basicOrAllowanceTextBox.getText() : "");
        if (isAllowence() || isBasicPayment()) {
            if (basicOrAllowanceTextBox.getText() != "0,00" && basicOrAllowanceTextBox.getText() != "0.00") {
                amountTextBox.setText(amountTextBox.getText());
                BigDecimal total = amountBigDecimal.multiply(new BigDecimal(100)).divide(amonBigDecimal, 2, RoundingMode.FLOOR);
                percentage.setText(PayrollClientUtils.format(total));
                paymentsTable.refreshCustomCellDisplayValue(rowIndex, ItemTableConstants.PERCENTAGE);
            } else {
                amountTextBox.setText("0,00");
                table.getGrid().getModel().update(rowIndex, amountColumnId, amountTextBox);
                Info.show(payrollStrings.pleaseInitiallyEnterAdditionalPaymentOfBasicOrAllowance(), Info.Type.WARNING);
                table.refreshCustomCellDisplayValue(rowIndex, "amount");
            }
        }
        updateTotal();
    }

    private void createFixedAmountWidgets() {
        filterParameter.setBasicPlusAllowancePaymentType(false);
        fixedAmountBox.setVisible(true);
        basicSalaryBox.setVisible(false);
        basicAllowanceLabel.setVisible(false);
        fixedAmountInput.addChangeHandler(changeEvent -> {
            applyAmount(fixedAmountInput.getValue());
        });
    }

    private void setDefaultPaymentType() {
        String selectedItem = paymentType.getSelectedItem() != null ? paymentType.getSelectedItem().getDescription() : "";
        if ("FIXED_AMOUNT".equals(selectedItem)) {
            applyAmount(fixedAmountInput.getText());
        } else if ("BASIC_SALARY".equals(selectedItem)) {
            applyPercentage(percentageInput.getText());
        } else if ("BASIC_SALARY_ALLOWANCE".equals(selectedItem)) {
            applyPercentage(percentageInput.getText());
        }
    }

    private void setPaymentTypeValues() {
        if (data != null && data.getPaymentType() != null) {
            paymentType.setSelectedByDescription(data.getPaymentType());
            if ("FIXED_AMOUNT".equals(data.getPaymentType())) {
                isBasicPaymentType = false;
                isAllowenceType = false;
                fixedAmountInput.setText(String.valueOf(data.getFixedAmount()));
                createFixedAmountWidgets();
            } else if ("BASIC_SALARY".equals(data.getPaymentType())) {
                isBasicPaymentType = true;
                isAllowenceType = false;
                percentageInput.setText(PayrollClientUtils.format(data.getPercentage().setScale(2, RoundingMode.HALF_UP)));
                createBasicSalaryWidgets();
            } else if ("BASIC_SALARY_ALLOWANCE".equals(data.getPaymentType())) {
                isBasicPaymentType = false;
                isAllowenceType = true;
                selectedCategories = data.getAllowancePaymentCategories();
                percentageInput.setText(PayrollClientUtils.format(data.getPercentage().setScale(2, RoundingMode.HALF_UP)));
                categoriesList.clear();
                categoriesDialogBox.clear();
                createBasicSalaryAndAllowanceWidgets();
                createBasicAllowancePopUp();
            }
            updateTotal();
        }
    }

    private void initCategoryType() {
        categoryChangeEvent = () -> {
            empModeMap.clear();
            paymentsTable.removeAllRows();
            paymentsTable.setVisible(false);
            EditableTable table = paymentsTable;
            table.setVisible(true);
            if (items != null && items.size() > 0) {
                showInPayslipField.setVisible(true);
                int i = 0;
                for (PaymentDeductionObject item : items) {
                    if (item.getEmployee() != null && item.getEmployee().getId() != null && empModeMap.get(item.getEmployee().getId()) == null) {
                        empModeMap.put(item.getEmployee().getId(), item.getEmpMode());
                    }
                    table.addRow(getWidgets(item, i));
                    table.getGridPanel().getGrid().getWidget(i, 1).addStyleName("uploadLinkStyle2");
                    if (item.getCountIncident() > 0) {
                        table.getGridPanel().getGrid().getWidget(i, 1).setTitle(item.getCountIncident() > 1 ? wfmStrings.incidents() : wfmStrings.incident());
                    }
                    i++;
                }
                applyDefaultCategory();
                applyDefaultDate();
                if (!isFillFromMethod) setDefaultPaymentType();
                isFillFromMethod = false;
            } else {
                showInPayslipField.setVisible(false);
            }
            updateTotal();
        };
    }


    public void showAdvancedOptions(String title, Widget advancedOptions) {
        KpiSideNavBox popUp = new KpiSideNavBox();

        Heading h1 = new Heading(HeadingSize.H1);
        h1.setClass("hasicon--left");
        h1.add(new Span(title));

        popUp.addHeader(h1);
        popUp.addBody(advancedOptions);
        popUp.getContentFooter().removeFromParent();
        popUp.show();
    }

    private void initGroupType() {
//        groupType = new DataListBox();
//        groupType.setWithoutNullLabel(true);
//        List<SelectItem> groupItems = new ArrayList<>();
//        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD)) {
//            groupItems.add(GROUP_TYPE);
//        }
//        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD)) {
//            groupItems.add(EMPLOYEE_TYPE);
//        }
//        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD) || Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT)) {
//            groupItems.add(DEPARTMENT_TYPE);
//        }
//        if (Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD)) {
//            groupItems.add(LOCATION_TYPE);
//        }

//        groupType.setItems(groupItems.toArray(new SelectItem[]{}));
        setValueToGroupBox(data);
        groupChangeEvent = () -> {
            setValueToGroupBox(data);
        };
//            groupType.setChangeEvent(groupChangeEvent);
//            paymentsTable.removeAllRows();
//        };


//        groupType.setSelected(GROUP_TYPE);
//        groupType.setEnabled(!summaryView);
    }

    private void setValueToGroupBox(AdditionalPayment data) {
        if (GROUP_TYPE.getDescription().equals(categoryType) || (data != null && data.getCategory() != null)) {
            groupBox.setLabel(wfmStrings.group());
            groupBox.setContent(payrollBatchLookUp);
            employeeLookUp.clear();
            departmentLookUp.clear();
            locationLookUp.clear();
            supervisorLookUp.clear();
        } else if (EMPLOYEE_TYPE.getDescription().equals(categoryType) || (data != null && data.getEmployee() != null)) {
            groupBox.setLabel(wfmStrings.employee());
            groupBox.setContent(employeeLookUp);
            payrollBatchLookUp.clear();
            departmentLookUp.clear();
            locationLookUp.clear();
            supervisorLookUp.clear();
        } else if (LOCATION_TYPE.getDescription().equals(categoryType) || (data != null && data.getLocation() != null)) {
            groupBox.setLabel(wfmStrings.location());
            groupBox.setContent(locationLookUp);
            payrollBatchLookUp.clear();
            employeeLookUp.clear();
            departmentLookUp.clear();
            supervisorLookUp.clear();
        } else if (SUPERVISOR_TYPE.getDescription().equals(categoryType) || (data != null && data.getSupervisor() != null)) {
            groupBox.setLabel(wfmStrings.supervisor());
            groupBox.setContent(supervisorLookUp);
            payrollBatchLookUp.clear();
            employeeLookUp.clear();
            departmentLookUp.clear();
            locationLookUp.clear();
        } else {
            groupBox.setLabel(wfmStrings.department());
            groupBox.setContent(departmentLookUp);
            payrollBatchLookUp.clear();
            employeeLookUp.clear();
            locationLookUp.clear();
            supervisorLookUp.clear();
        }
    }

    private void initButtonsPanel() {
        draftDiv = new Div();
        draftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        draftButton.addClickHandler(clickEvent -> save(PAYMENT_STATUS_DRAFT));
        draftDiv.add(draftButton);

        submitDiv = new Div();
        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> save(PAYMENT_STATUS_SUBMITTED));
        submitDiv.add(submitButton);

        approveDiv = new Div();
        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> save(PAYMENT_STATUS_APPROVED));
        approveDiv.add(approveButton);

        declineDiv = new Div();
        declineButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
        declineButton.addClickHandler(clickEvent -> save(PAYMENT_STATUS_REJECTED));
        declineDiv.add(declineButton);

        editDiv = new Div();
        editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE);
        editButton.addClickHandler(clickEvent -> {
            viewInterface.getView().closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged((PayrollConstants.CATEGORY_PAYMENT.equals(type) ? "additionalPayment|edit/" : "additionalDeduction|edit/") + objectId, data.getReference());
        });
        editDiv.add(editButton);

        pdfDiv = new Div();
        pdfButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        pdfDiv.add(pdfButton);

        draftDiv.setVisible(false);
        submitDiv.setVisible(false);
        approveDiv.setVisible(false);
        declineDiv.setVisible(false);
        editDiv.setVisible(false);
        pdfDiv.setVisible(false);

        renderButtons();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, viewInterface.getView(), (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    String status = data != null && data.getOverallStatus() != null ? data.getOverallStatus().getCode() : null;
                    if (status == null || PAYMENT_STATUS_DRAFT.equals(status) || PAYMENT_STATUS_REJECTED.equals(status)) {
                        if (Utils.adminOrDirector() || item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                            approveDiv.setVisible(true);
                            submitDiv.setVisible(false);
                        } else {
                            approveDiv.setVisible(false);
                            submitDiv.setVisible(true);
                        }
                    }
                });
            }
        });
    }

    private void initTotals() {
        totalLabel = new HTML(wfmStrings.total());
        totalAmount = new HTML(PayrollClientUtils.format(total));

        ReceiptTable totalTable = new ReceiptTable();
        totalTable.clear();
        totalTable.removeShippingBody();
        totalTable.addGrossItem(totalLabel, totalAmount);
        totalPanel.add(totalTable);
    }

    public void initTables(boolean isNotFillForm) {
        paymentsTable = new EditableTable(getColumns(), (Utils.hasPermission(PermissionConstants.ADDITIONAL_PAYMENT_LINE_ITEM_DELETE)));
        paymentsTable.setVisible(isPayment());
        paymentsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {

            }

            @Override
            public void removeRow() {
                updateTotal();
            }
        });
        itemsTable.add(paymentsTable);
        if (!isNotFillForm) {
            categoryChangeEvent.execute();
        }
    }

    private boolean isPayment() {
        return true;
    }

    private boolean isBasicPayment() {
        return isBasicPaymentType;
    }

    private boolean isAllowence() {
        return isAllowenceType;
    }

    private ColumnConfig[] getColumns() {
        if (isBasicPayment()) {
            columnsMap.remove(ItemTableConstants.ALLOWANCE);
        } else if (isAllowence()) {
            columnsMap.remove(ItemTableConstants.BASIC_SALARY);
        } else {
            columnsMap.remove(ItemTableConstants.PERCENTAGE);
            columnsMap.remove(ItemTableConstants.BASIC_SALARY);
            columnsMap.remove(ItemTableConstants.ALLOWANCE);

        }
        ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
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
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.BASIC_SALARY:
                    if (isBasicPayment()) {
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.BASIC_SALARY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.basicSalary(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[i++] = columnConfig;
                    }
                    break;
                case ItemTableConstants.ALLOWANCE:
                    if (isAllowence()) {
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.ALLOWANCE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.allowance(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[i++] = columnConfig;
                    }
                    break;
                case ItemTableConstants.PERCENTAGE:
                    if (isBasicPayment() || isAllowence()) {
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PERCENTAGE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.percentage() + "(%)", Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[i++] = columnConfig;
                    }
                    break;
                case ItemTableConstants.AMOUNT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.AMOUNT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.amount(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.CATEGORY:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CATEGORY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.categories(), Utils.getColumnWidth(columnConfigs.getWidth(), 130), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.PAYMENT_DATE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PAYMENT_DATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.paymentDate(), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                default:
                    columnConfig = new ColumnConfig(CustomCell.class, columnConfigs.getCode(), columnConfigs.getTitle(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), isPixel);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;

                    if (Constants.UI_TYPE_TEXTAREA.equalsIgnoreCase(columnConfigs.getUiType())) {
                        columnConfig.setCustomStyleName("product-description-cell");
                    }
                    break;
            }
        }
        return columns;
    }

    private Widget[] getWidgets(PaymentDeductionObject item, int index) {
        int count = 0;
        final Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.EMPLOYEE.equals(columnCode)) {
                EmployeeBox employeeBox = new EmployeeBox(item.getId(), item.getEmployee(), item.getCountIncident());
                employeeBox.setEnabled(true);
                employeeBox.setReadOnly(true);
                employeeBox.addStyleName(DEFAULT_WIDTH);
                employeeBox.setStyleName("file--AdditionalPaymentUIBinder");
                widgets[count] = employeeBox;
            } else if (ItemTableConstants.AMOUNT.equals(columnCode)) {
                final EditableTextBox amountTextBox = new EditableTextBox();
                amountTextBox.addStyleName(DEFAULT_WIDTH);
                amountTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                amountTextBox.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                Validation.checkToFocusTextBox(amountTextBox, NumberFormat.getFormat(",##0").format(BigDecimal.ZERO));
                Validation.addNumericKeyboardListener(amountTextBox, 2);
                amountTextBox.setText(PayrollClientUtils.format(item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO));
                amountTextBox.setEnabled(!summaryView && isPayment());
                amountTextBox.addChangeHandler(changeEvent -> applyAmountTextBox(index));
                addHandlersForTextBox(amountTextBox, index, true);
                widgets[count] = amountTextBox;
            } else if (ItemTableConstants.CATEGORY.equals(columnCode)) {
                final PayrollCategoryLookUp categoryLookUp = new PayrollCategoryLookUp(type);
                categoryLookUp.setWidth(Constants.NORMAL_WIDTH);
                categoryLookUp.getSuggestBox().getElement().getStyle().setWidth(100, Style.Unit.PCT);
                categoryLookUp.getSuggestBox().getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                if (item.getCategoryItem() != null) {
                    categoryLookUp.addCategoryItem(item.getCategoryItem());
                }
                categoryLookUp.setEnabled(!summaryView);
                if (objectId == null) {
                    categoryLookUp.getSuggestBox().addSelectionHandler(sh -> {
                        PayrollService.App.get().getPredefinedValueOfCategory(item.getEmployee().getId(), categoryLookUp.getSelectedItemID(), new AsyncCallback<BigDecimal>() {
                            @Override
                            public void onFailure(Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(BigDecimal value) {
                                if (value != null) {

                                    EditableTextBox txtAmount = (EditableTextBox) paymentsTable.getColumnById(paymentsTable.getGrid().getCurrentRow(), wfmStrings.amount());
                                    txtAmount.setText(PayrollClientUtils.format(value));
                                    updateTotal();
                                    ((CustomCell) paymentsTable.getColumnCellWidgetById(paymentsTable.getGrid().getCurrentRow(), wfmStrings.amount())).InActive();
                                }
                            }
                        });
                    });
                }
                widgets[count] = categoryLookUp;
            } else if (ItemTableConstants.BASIC_SALARY.equals(columnCode) || ItemTableConstants.ALLOWANCE.equals(columnCode)) {
                BigDecimal amount = isAllowence() ? item.getBasicPlusAllowance() : item.getEmployeeBasicSalary();
                amount = amount != null ? amount : BigDecimal.ZERO;
                //By Commission fields
                final EditableTextBox basicSalaryTextBox = new EditableTextBox(PayrollClientUtils.format(BigDecimal.ZERO), true);
                basicSalaryTextBox.addStyleName(DEFAULT_WIDTH);
                basicSalaryTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                basicSalaryTextBox.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                Validation.checkToFocusTextBox(basicSalaryTextBox, NumberFormat.getFormat(",##0").format(BigDecimal.ZERO));
                Validation.addNumericKeyboardListener(basicSalaryTextBox, 2);
                basicSalaryTextBox.setText(PayrollClientUtils.format(amount));
                addHandlersForTextBox(basicSalaryTextBox, index, false);
                if (Utils.hasPermission(PermissionConstants.BASIC_SALARY_EDIT)) {
                    basicSalaryTextBox.setEnabled(!summaryView);
                } else {
                    basicSalaryTextBox.setEnabled(false);
                }
                widgets[count] = basicSalaryTextBox;
            } else if (ItemTableConstants.PERCENTAGE.equals(columnCode) && (isBasicPayment() || isAllowence())) {
                BigDecimal percentage = item.getPercentage() != null ? item.getPercentage() : BigDecimal.ZERO;
                final EditableTextBox percentageTextBox = new EditableTextBox(PayrollClientUtils.format(BigDecimal.ZERO), true);
                percentageTextBox.addStyleName(DEFAULT_WIDTH);
                percentageTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                percentageTextBox.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                Validation.checkToFocusTextBox(percentageTextBox, NumberFormat.getFormat(",##0").format(BigDecimal.ZERO));
                Validation.addNumericKeyboardListener(percentageTextBox, 2);
                percentageTextBox.setText(PayrollClientUtils.format(percentage));
                percentageTextBox.setEnabled(!summaryView);

                addHandlersForTextBox(percentageTextBox, index, false);
                widgets[count] = percentageTextBox;
            } else if (ItemTableConstants.PAYMENT_DATE.equals(columnCode)) {
                ExtendedDatePicker paymentDate = new ExtendedDatePicker();
                paymentDate.addStyleName(DEFAULT_WIDTH);
                paymentDate.setWidth(NORMAL_WIDTH);
                paymentDate.setDate(item.getAdditionalPaymentDate().getNonConvertedDate());
                paymentDate.setEnabled(!summaryView);
                widgets[count] = paymentDate;
            } else if (itemCFs.containsKey(columnCode)) {

                CompanyCustomFieldItem cfItem = itemCFs.get(columnCode);
                CompanyCustomFieldItem companyCustomFieldItem = setCustomFieldValue(item.getItemCustomFields(), cfItem);

                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextBoxField cf = new CustomTextBoxField(companyCustomFieldItem);
                    cf.setEnabled(!summaryView);
                    widgets[count] = cf;
                    cf.setMaxLength(1000);
                } else if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextAreaField customTextAreaField = new CustomTextAreaField(companyCustomFieldItem);
                    customTextAreaField.hideCharacterLimitPanel();
                    customTextAreaField.setEnabled(!summaryView);
                    Validation.addAutoResizeListenerToTextArea(customTextAreaField.getTextArea());
                    widgets[count] = customTextAreaField;
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                    CustomPercentageField cf = new CustomPercentageField(companyCustomFieldItem);
                    cf.setEnabled(!summaryView);
                    widgets[count] = cf;
                } else if (Constants.UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                    CustomDropDownField cf = new CustomDropDownField(companyCustomFieldItem);
                    cf.setEnabled(!summaryView);
                    widgets[count] = cf;
                } else if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                    CustomDatePicker cf = new CustomDatePicker(companyCustomFieldItem);
                    cf.setEnabled(!summaryView);
                    widgets[count] = cf;
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                    CustomDateTime cf = new CustomDateTime(companyCustomFieldItem);
                    cf.setEnabled(!summaryView);
                    widgets[count] = cf;
                } else if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    CustomFieldLookUpField cf = new CustomFieldLookUpField(companyCustomFieldItem);
                    cf.setEnabled(!summaryView);
                    widgets[count] = cf;
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    CustomFieldMultiLookUpField cf = new CustomFieldMultiLookUpField(companyCustomFieldItem);
                    cf.setEnabled(!summaryView);
                    widgets[count] = cf;
                }

                if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                    CompanyCustomFieldItem fitem = companyCustomFieldItem;
                    if (fitem != null) {
                        ((CustomFieldInterface) widgets[count]).setFieldItem(fitem);
                    }
                }
            }
            count++;
        }
        return widgets;
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

    private void addHandlersForTextBox(EditableTextBox textBox, int index, boolean isAmountTextBox) {
        textBox.addValueChangeHandler(event -> {
            textBox.setText(PayrollClientUtils.format(new BigDecimal(textBox.getText())));
        });

        textBox.addFocusHandler(event -> {
            textBox.selectAll();
        });

        textBox.addKeyboardListener(new KeyboardListener() {
            @Override
            public void onKeyDown(Widget widget, char c, int i) {
                if (isAmountTextBox) applyAmountTextBox(index);
                else changePerColumn(index);
            }

            @Override
            public void onKeyPress(Widget widget, char c, int i) {
                if (isAmountTextBox) applyAmountTextBox(index);
                else changePerColumn(index);
            }

            @Override
            public void onKeyUp(Widget widget, char c, int i) {
                if (isAmountTextBox) applyAmountTextBox(index);
                else changePerColumn(index);
            }
        });

    }

    private void changePerColumn(int index) {
        EditableTextBox changePerAmount = (EditableTextBox) paymentsTable.getColumnById(index, ItemTableConstants.AMOUNT);
        EditableTextBox percentageTextBox = (EditableTextBox) paymentsTable.getColumnById(index, ItemTableConstants.PERCENTAGE);

        String key = isAllowence() ? ItemTableConstants.ALLOWANCE : ItemTableConstants.BASIC_SALARY;
        EditableTextBox basicSalaryTextBox = (EditableTextBox) paymentsTable.getColumnById(index, key);

        BigDecimal percentageBigDecimal = PayrollClientUtils.parseToBigDecimal(percentageTextBox.getText() != null ? percentageTextBox.getText() : "0");
        BigDecimal amonBigDecimal = PayrollClientUtils.parseToBigDecimal(basicSalaryTextBox.getText() != null ? basicSalaryTextBox.getText() : "0");

        BigDecimal total = amonBigDecimal.multiply(percentageBigDecimal.divide(new BigDecimal(100), 2, RoundingMode.CEILING));
        changePerAmount.setText(PayrollClientUtils.format(total));
        paymentsTable.refreshCustomCellDisplayValue(index, ItemTableConstants.AMOUNT);
        paymentsTable.refreshCustomCellDisplayValue(index, key);
        paymentsTable.refreshCustomCellDisplayValue(index, ItemTableConstants.PERCENTAGE);

        updateTotal();
    }

    public void itemTableColumnConfig(AdditionalPayment data) {
        if (data.getItemCustomFields() != null) {
            itemCFs.clear();
            for (CompanyCustomFieldItem item : data.getItemCustomFields()) {
                itemCFs.put(item.getColumnCode(), item);
            }
        }

        if (data.getColumnConfigs() != null) {
            columnsMap.clear();
            for (ColumnConfigs cc : data.getColumnConfigs()) {
                if (cc.isSelected()) {
                    columnsMap.put(cc.getCode(), cc);
                }
            }
        }
    }

    public void fillFormData(AdditionalPayment data) {
        this.data = data;
        isFillFromMethod = true;
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
        SplitButtonItem pdfVersion = new SplitButtonItem("PDF_VERSION", wfmStrings.pdfVersion(), () -> generatePDF(finalDefaultTemplateId), true);
        pdfItems.add(pdfVersion);
        pdfButton.addItemList(pdfItems);

        if (data.getDefaultDate() != null) {
            defaultDateWidget.setDate(data.getDefaultDate().getNonConvertedDate());
        }
        if (data.getEmployee() != null) {
            employeeLookUp.addItem(data.getEmployee());
            employeeLookUp.setSelected(data.getEmployee());
            groupChangeEvent.execute();
        } else if (data.getDepartment() != null) {
            departmentLookUp.addItem(data.getDepartment());
            departmentLookUp.setSelected(data.getDepartment());
            groupChangeEvent.execute();
        } else if (data.getLocation() != null) {
            locationLookUp.addItem(data.getLocation());
            locationLookUp.setSelected(data.getLocation());
            groupChangeEvent.execute();
        } else if (data.getSupervisor() != null) {
            supervisorLookUp.addItem(data.getSupervisor());
            supervisorLookUp.setSelected(data.getSupervisor());
            groupChangeEvent.execute();
        } else {
            payrollBatchLookUp.setSelected(data.getPayrollBatch());
            groupChangeEvent.execute();
        }
        groupBox.setEnabled(false);
        if (PAYMENT_STATUS_APPROVED.equals(data.getStatusCode())) {
            defaultDateWidget.setEnabled(false);
        }
        currency = data.getCurrency();
        referenceTextBox.setText(data.getReference());
        month.setSelected(data.getMonthID());
        year.setSelected(data.getYear());
        setPaymentTypeValues();

        items = data.getItems();
        changeTable(false);
        showInPayslip.setValue(data.isShowInPayslip());

        if (data.getDefaultCategory() != null) {
            categoryForAll.refreshLookUp(data.getDefaultPayrollCategoryId());
        }
        updateTotal();
        renderButtons();
    }

    public void initDefaults(AdditionalPayment data) {
        categoryForAll.addCategoryItem(data.getDefaultCategory());
        if (data.getDefaultDate() != null) {
            defaultDateWidget.setDate(data.getDefaultDate().getNonConvertedDate());
        } else {
            defaultDateWidget.setDefaultFormatText();
        }
        if (data.getDepartmentList() != null && data.getDepartmentList().length == 1 && DEPARTMENT_TYPE.getDescription().equals(categoryType)) {
            departmentLookUp.addItem(data.getDepartmentList()[0]);
            departmentLookUp.setSelected(data.getDepartmentList()[0].getId());
            onChangeGroup();
        }
        applyDefaultCategory();
        applyDefaultDate();
    }

    public void renderButtons() {
        Integer currentApproverID = data != null && data.getCurrentApproverAsSelectItem() != null ? data.getCurrentApproverAsSelectItem().getId() : null;
        String statusCode = data != null && data.getOverallStatus() != null ? data.getOverallStatus().getCode() : null;
        boolean canApprove = Utils.adminOrDirector() || Utils.getUserID().equals(currentApproverID);

        pdfDiv.setVisible((employeeId != null || objectId != null) && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_PDF));
        if (objectId != null && !isCopyView) {//for edit/view
            draftDiv.setVisible(false);
            submitDiv.setVisible(false);
            if (statusCode != null && !PAYMENT_STATUS_APPROVED.equals(statusCode)) {
                if (canApprove && !PAYMENT_STATUS_REJECTED.equals(statusCode)) {
                    approveDiv.setVisible(true);
                    declineDiv.setVisible(summaryView);
                } else if (PAYMENT_STATUS_REJECTED.equals(statusCode)) {
                    submitButton.setText(wfmStrings.resubmit());
                } else {
                    submitDiv.setVisible(true);
                }
                editDiv.setVisible(summaryView && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT) && data != null && !PAYMENT_STATUS_APPROVED.equals(data.getStatusCode()));
                draftDiv.setVisible(!summaryView);
            }
        } else {//for add
            if (Utils.adminOrDirector()) {
                draftDiv.setVisible(true);
                submitDiv.setVisible(false);
                approveDiv.setVisible(true);
            } else {
                draftDiv.setVisible(!summaryView);
                submitDiv.setVisible(!summaryView);
            }
        }
    }

    public AdditionalPayment getFormData(String status) {
        AdditionalPayment payment = new AdditionalPayment();
        if (!isCopyView) {
            payment.setObjectID(objectId);
        }
        payment.setReference(referenceTextBox.getText());
        payment.setMonth(month.getSelectedItem().getName());
        payment.setMonthID(month.getSelectedId());
        payment.setYear(year.getSelectedId());
        payment.setPayrollBatch(payrollBatchLookUp.getSelectedItem());
        payment.setCurrency(currency);
        payment.setType(Constants.ADDITIONAL_PAYMENT_TYPE);
        payment.setApprovers(approver.getChosenApprovers());
        payment.setItems(getItems());
        payment.setShowInPayslip(showInPayslip.getValue());
        payment.setStatusCode(status);
        payment.setTotal(total);
        payment.setCategoryType(type);
        payment.setEmployee(employeeLookUp.getSelectedItem());
        payment.setDefaultDate(new DateNonConvertable(defaultDateWidget.getDate()));
        payment.setDepartment(departmentLookUp.getSelectedItem());
        payment.setLocation(locationLookUp.getSelectedItem());
        payment.setSupervisor(supervisorLookUp.getSelectedItem());
        payment.setPaymentType(paymentType.getSelectedIndex() == -1 ? null : paymentType.getSelectedItem(true).getDescription());
        payment.setFixedAmount(fixedAmountInput.getText() != null && !fixedAmountInput.getText().isEmpty() ? new BigDecimal(fixedAmountInput.getText()) : BigDecimal.ZERO);
        payment.setPercentage(PayrollClientUtils.parseToBigDecimal(percentageInput.getText()).setScale(2, RoundingMode.HALF_UP));
        payment.setBasicPlusAllowance(isAllowence());
        payment.setFromCopy(isCopyView);
        payment.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        if (isAllowence()) {
            payment.setAllowancePaymentCategories(selectedCategories);
        }
        if (categoryForAll.getSelectedItemID() != null) {
            payment.setDefaultPayrollCategoryId(categoryForAll.getSelectedItemID());
        }
        return payment;
    }

    private List<PaymentDeductionObject> getItems() {
        List<PaymentDeductionObject> items = new ArrayList<>();
        EditableTable table = paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            PaymentDeductionObject item = new PaymentDeductionObject();
            EmployeeBox employeeCell = (EmployeeBox) table.getColumnById(i, ItemTableConstants.EMPLOYEE);
            TextBox amountTextBox = (TextBox) table.getColumnById(i, ItemTableConstants.AMOUNT);
            PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(i, ItemTableConstants.CATEGORY);
            DatePicker paymentDatePicker = (DatePicker) table.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
            String key = isAllowence() ? ItemTableConstants.ALLOWANCE : ItemTableConstants.BASIC_SALARY;
            EditableTextBox basicOrBasicAllowance = (EditableTextBox) table.getColumnById(i, key);
            EditableTextBox percentage = (EditableTextBox) table.getColumnById(i, ItemTableConstants.PERCENTAGE);

            if (employeeCell != null) {
                item.setId(employeeCell.getDeductionId());
                item.setEmployee(employeeCell.getEmployee());
            }
            if (amountTextBox != null) {
                item.setPaymentAmount(PayrollClientUtils.parseToBigDecimal(amountTextBox.getText()));
            }
            if (categoryLookUp != null) {
                item.setCategoryItem(categoryLookUp.getSelectedData());
            } else if (categoryLookUp == null && categoryForAll.getSelectedData() != null) {
                item.setCategoryItem(categoryForAll.getSelectedData());
            }
            if (paymentDatePicker != null) {
                item.setAdditionalPaymentDate(new DateNonConvertable(paymentDatePicker.getDate()));
            } else if (paymentDatePicker == null && defaultDateWidget.getDate() != null) {
                item.setAdditionalPaymentDate(new DateNonConvertable(defaultDateWidget.getDate()));
            }

            BigDecimal value = PayrollClientUtils.parseToBigDecimal(basicOrBasicAllowance != null ? basicOrBasicAllowance.getText() : "");
//
//            BigDecimal perValue = ;
//            perValue.setScale(2,RoundingMode.UNNECESSARY);
            item.setPercentage(PayrollClientUtils.parseToBigDecimal(percentage != null ? percentage.getText() : "").setScale(2, RoundingMode.HALF_UP));
            if (isAllowence()) {
                item.setBasicPlusAllowance(value);
            } else if (isBasicPayment()) {
                item.setEmployeeBasicSalary(value);
            } else {
                item.setBasicPlusAllowance(BigDecimal.ZERO);
                item.setEmployeeBasicSalary(BigDecimal.ZERO);
                item.setPercentage(BigDecimal.ZERO);
            }

            //set fromDate/endDate for advance payment
            Integer currentYear = year.getSelectedId();
            Integer monthDayCount = CalendarUtil.getMonthDaysCount(month.getSelectedId(), currentYear);
            item.setStarttDate(new DateNonConvertable(new Date(currentYear - 1900, month.getSelectedId(), 1)));
            item.setEnddDate(new DateNonConvertable(new Date(currentYear - 1900, month.getSelectedId(), monthDayCount)));

            if (itemCFs != null && !itemCFs.isEmpty()) {
                ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                for (String keyCF : itemCFs.keySet()) {
                    CustomFieldInterface customField = (CustomFieldInterface) paymentsTable.getColumnById(i, keyCF);

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
                    } else if (itemCFs.size() > 0 && itemCFs.get(keyCF) != null && itemCFs.get(keyCF).getUiType() != null) {
                        fieldItems.add(itemCFs.get(keyCF));
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

    private void updateTotal() {
        total = BigDecimal.ZERO;
        EditableTable table = paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            TextBox amount = (TextBox) table.getColumnById(i, ItemTableConstants.AMOUNT);
            total = total.add(PayrollClientUtils.parseToBigDecimal(amount.getText()));
        }
        if (currency != null) {
            totalLabel.setText(wfmMessages.total(currency.getName()));
        }
        totalAmount.setText(PayrollClientUtils.format(total));
    }

    private void save(String status) {
        if (!validate(status)) {
            disableButtons(true);
            return;
        }
        disableButtons(false);
        AdditionalPayment saveItemData = getFormData(status);
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setMonthId(saveItemData.getMonthID());
        filterParametrs.setYear(saveItemData.getYear());
        String categoryName = "";
        if (saveItemData.getEmployee() != null) {
            filterParametrs.setEmployeeIDs(String.valueOf(saveItemData.getEmployee().getId()));
            categoryName = saveItemData.getEmployee().getName();
        }
        if (saveItemData.getDepartment() != null) {
            filterParametrs.setDepartmentId(saveItemData.getDepartment().getId());
            categoryName = saveItemData.getDepartment().getName();
        }
        if (saveItemData.getLocation() != null) {
            filterParametrs.setLocationId(saveItemData.getLocation().getId());
            categoryName = saveItemData.getLocation().getName();
        }
        if (saveItemData.getPayrollBatch() != null) {
            filterParametrs.setPayrollBatchID(saveItemData.getPayrollBatch().getId());
            categoryName = saveItemData.getPayrollBatch().getName();
        }
        if (saveItemData.getSupervisor() != null) {
            filterParametrs.setSupervisorId(saveItemData.getSupervisor().getId());
            categoryName = saveItemData.getSupervisor().getName();
        }

        String[] checkCategoryName = categoryName.split(">");
        String finalCategoryName;
        if (checkCategoryName.length > 1) finalCategoryName = checkCategoryName[1];
        else finalCategoryName = checkCategoryName[0];

        if (!isCopyView && objectId != null && month.getSelectedId().equals(data.getMonthID()) && data.getYear().equals(year.getSelectedId()) && (departmentLookUp != null && data.getDepartment() != null && Objects.equals(data.getDepartment().getId(), departmentLookUp.getSelectedItemID()))) {
            saveItem(saveItemData);
        } else if (!isCopyView && objectId != null && month.getSelectedId().equals(data.getMonthID()) && data.getYear().equals(year.getSelectedId())) {
            saveItem(saveItemData);
        } else {
            PayrollService.App.get().isExistSuchAdditionalPaymentByCategory(filterParametrs, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    disableButtons(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        disableButtons(true);
                        if (viewInterface.getView() != null) {
                            KpiModal modal = new KpiModal();
                            modal.setTitle(wfmStrings.warning());
                            FlexTable flexTable = new FlexTable();
                            Label label1 = new Label();
                            Label label2 = new Label();
//                            label1.setText(coreMessages.valueAlreadyExists(finalCategoryName, saveItemData.getMonth() + "-" + saveItemData.getYear()));
                            label2.setText(wfmMessages.areYouSureWantToAddThisAdditionalPayment());
                            flexTable.setWidget(0, 0, label1);
                            flexTable.setWidget(1, 0, label2);
                            modal.add(flexTable);

                            WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
                            save.addClickHandler(click -> {
                                modal.close();
                                saveItem(saveItemData);
                            });
                            WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);
                            cancel.addClickHandler(click -> modal.close());
                            modal.addButton(cancel);
                            modal.addButton(save);
                            modal.open();
                        }
                    } else {
                        saveItem(saveItemData);
                    }
                }
            });
        }


    }

    private void saveItem(AdditionalPayment data) {
        LoadingPanel.loading(true);
        disableButtons(false);
        PayrollService.App.get().saveAdditionalPayment(data, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                disableButtons(true);
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.errorOccurredSavingChanges());
            }

            @Override
            public void onSuccess(Void aVoid) {
                disableButtons(true);
                LoadingPanel.loading(false);
                Info.show(Property.get(ADDITIONAL_PAYMENT_LIST, wfmStrings.messSuccessfullySaved(), PayrollConstants.CATEGORY_PAYMENT.equals(type) ? wfmStrings.additionalPayment() : payrollStrings.additionalDeduction()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADDITIONAL_PAYMENT_ADD, null, null);
                if (viewInterface.getView() != null) {
                    viewInterface.getView().closeTab();
                }
            }
        });
    }

    private void disableButtons(boolean isEnable) {
        draftButton.setEnabled(isEnable);
        submitButton.setEnabled(isEnable);
        approveButton.setEnabled(isEnable);
    }


    private boolean validate(String status) {
        int errors = 0;
        if (!PAYMENT_STATUS_DRAFT.equals(status) && !Validation.validateTextBoxRequired(referenceTextBox)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(month, new HTML(), "")) {
            errors++;
        }
//        if (GROUP_TYPE.equals(groupType.getSelectedItem())) {
//            if (!Validation.validateLookUpRequired(payrollBatchLookUp)) {
//                errors++;
//            }
//        } else if (EMPLOYEE_TYPE.equals(groupType.getSelectedItem())) {
//            if (!Validation.validateLookUpRequired(employeeLookUp)) {
//                errors++;
//            }
//        } else if (DEPARTMENT_TYPE.equals(groupType.getSelectedItem())) {
//            if (!Validation.validateLookUpRequired(departmentLookUp)) {
//                errors++;
//            }
//        } else if (LOCATION_TYPE.equals(groupType.getSelectedItem())) {
//            if (!Validation.validateLookUpRequired(locationLookUp)) {
//                errors++;
//            }
//        }
        if (!approver.isValid()) {
            errors++;
        }

        if (!PAYMENT_STATUS_DRAFT.equals(status)) {
            EditableTable table = paymentsTable;

            paymentsTable.setValidRows(0);
            ArrayList<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();
            for (String columnCode : columnsMap.keySet()) {
                if (itemCFs.containsKey(columnCode) && (itemCFs.get(columnCode).isRequired() || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(itemCFs.containsKey(columnCode)) || Constants.UI_TYPE_URL.equals(itemCFs.containsKey(columnCode)) || Constants.UI_TYPE_PERCENTAGE.equals(itemCFs.containsKey(columnCode)))) {
                    requiredAndEmailCFs.add(itemCFs.get(columnCode));
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
                paymentsTable.resetValidation(i);
                rowError = validateRequiredItems(i, requiredAndEmailCFs, requiredColumnCodes)[0];
                if (rowError == 0) {
                    paymentsTable.setItemValid(i, true);
                    paymentsTable.incValidRow();
                } else if (rowError == requiredRow + requiredAndEmailCFs.size() - validateRequiredItems(i, requiredAndEmailCFs, requiredColumnCodes)[1]) {

                    if (!areOtherRowsAffected(i)) {
                        paymentsTable.setItemValid(i, false); // exclude
                    } else {
                        colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                        errorFound = true;
                    }
                } else {
                    colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                    errorFound = true;
                }
            }
            if (paymentsTable.getValidRows() == 0) {
                colorizeErrorField(0, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }
            if (errorFound) {
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                return false;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        if (!PAYMENT_STATUS_DRAFT.equals(status) && total != null && total.compareTo(BigDecimal.ZERO) < 0) {
            Info.show(wfmStrings.totalAmountCantLessThanZero(), Info.Type.WARNING);
            return false;
        }
        if (itemCFs != null && itemCFs.values().size() > 0) {
            return Validation.itemTableNumericCFMinValueValidate(paymentsTable, itemCFs.values());
        } else {
            return true;
        }
    }

    private void colorizeErrorField(int rowID, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        TextBox amountTextBox = (TextBox) paymentsTable.getColumnById(rowID, ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) paymentsTable.getColumnById(rowID, ItemTableConstants.CATEGORY);
        DatePicker paymentDatePicker = (DatePicker) paymentsTable.getColumnById(rowID, ItemTableConstants.PAYMENT_DATE);

        if (requiredColumnCodes.contains(ItemTableConstants.CATEGORY)) {
            if (categoryLookUp.getSelectedItem() == null || (categoryLookUp.getText() == null || categoryLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(categoryLookUp.getText()))) {
                paymentsTable.notValid(rowID, ItemTableConstants.CATEGORY);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.AMOUNT)) {
            if (!Validation.validateTextAreaRequired(amountTextBox)) {
                paymentsTable.notValid(rowID, ItemTableConstants.AMOUNT);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.PAYMENT_DATE)) {
            if (!Validation.validateDate(paymentDatePicker)) {
                paymentsTable.notValid(rowID, ItemTableConstants.PAYMENT_DATE);
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            }
            if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateEmailRequired(t)) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateUrl(t, null)) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        paymentsTable.notValid(rowID, fieldItem.getColumnCode());

                    }
                }
            } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(Constants.ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    paymentsTable.notValid(rowID, fieldItem.getColumnCode());
                }
            }
        }
    }


    private boolean areOtherRowsAffected(int rowID) {
        boolean result = false;
        TextBox amountTextBox = (TextBox) paymentsTable.getColumnById(rowID, ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) paymentsTable.getColumnById(rowID, ItemTableConstants.CATEGORY);
        DatePicker paymentDatePicker = (DatePicker) paymentsTable.getColumnById(rowID, ItemTableConstants.PAYMENT_DATE);
        result |= amountTextBox != null && (amountTextBox.getText() != null && !"".equals(amountTextBox.getText().trim()));
        result |= categoryLookUp != null && (categoryLookUp.getSelectedItem() != null && categoryLookUp.getSelectedItem().getId() != null);
        result |= paymentDatePicker != null && paymentDatePicker.getDate() != null;
        return result;
    }

    private int[] validateRequiredItems(int rowID, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        int nonRequired = 0;
        int[] error = new int[2];

        TextBox amountTextBox = (TextBox) paymentsTable.getColumnById(rowID, ItemTableConstants.AMOUNT);
        PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) paymentsTable.getColumnById(rowID, ItemTableConstants.CATEGORY);
        DatePicker paymentDatePicker = (DatePicker) paymentsTable.getColumnById(rowID, ItemTableConstants.PAYMENT_DATE);
        if (!Validation.validateTextBoxRequired(amountTextBox)) {
            paymentsTable.notValid(rowID, ItemTableConstants.AMOUNT);
            errors++;
        }
        if (categoryLookUp != null && !Validation.validateLookUpRequired(categoryLookUp)) {
            paymentsTable.notValid(rowID, ItemTableConstants.CATEGORY);
            errors++;
        }
        if (paymentDatePicker != null && !Validation.validateDate(paymentDatePicker)) {
            paymentsTable.notValid(rowID, ItemTableConstants.PAYMENT_DATE);
            errors++;
        }

        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    paymentsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        paymentsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        paymentsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    } else {
                        nonRequired++;
                    }
                }
            } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateEmailRequired(t)) {
                        paymentsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText() != null && t.getText().length() > 0) {
                        if (!Validation.validateEmailRequired(t)) {
                            paymentsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateUrl(t, null)) {
                        paymentsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText() != null && t.getText().length() > 0) {
                        if (!Validation.validateUrl(t, null)) {
                            paymentsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedId() == null) {
                    paymentsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    paymentsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    paymentsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    paymentsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    paymentsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) paymentsTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(Constants.ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    paymentsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            }
        }

        error[0] = errors;
        error[1] = nonRequired;
        return error;
    }

    private void onChangeGroup() {
        filterParameter.setObjectId(payrollBatchLookUp.getSelectedItemID());
        filterParameter.setEmployeeId(employeeLookUp.getSelectedItemID());
        filterParameter.setDepartmentId(departmentLookUp.getSelectedItemID());
        filterParameter.setLocationId(locationLookUp.getSelectedItemID());
        filterParameter.setPaymentCategories(selectedCategories);
        filterParameter.setBasicPlusAllowancePaymentType(isAllowence());
        filterParameter.setResignedEmployeesIncluded(false);
        filterParameter.setSupervisorId(supervisorLookUp.getSelectedItemID());
        filterParameter.setMonthId(month.getSelectedId());
        filterParameter.setYear(year.getSelectedId());
        LoadingPanel.loading(true, panel);
        PayrollService.App.get().getEmployeesForAdditionalPayment(filterParameter, null, new AsyncCallback<AdditionalPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(AdditionalPayment result) {
                LoadingPanel.loading(false);
                items = result.getItems();
                currency = result.getCurrency();
                categoryChangeEvent.execute();
                LoadingPanel.loading(false, panel);
            }
        });

        changeReferenceValue();
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
        month.setSelectedNullLabel();
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

    private void applyDefaultCategory() {
        if (categoryForAll.getSelectedItem() != null) {
            EditableTable table = paymentsTable;
            for (int i = 0; i < table.getRowCount(); i++) {
                PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) table.getColumnById(i, ItemTableConstants.CATEGORY);
                Integer column = table.getColumnId(ItemTableConstants.CATEGORY);
                if (categoryLookUp != null) {
                    categoryLookUp.addCategoryItem(categoryForAll.getSelectedData());
                    table.getGrid().getModel().update(i, column, categoryLookUp);
                }
            }
        }
    }

    private void applyAmount(String value) {
        EditableTable table = paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            EditableTextBox amountTextBox = (EditableTextBox) table.getColumnById(i, ItemTableConstants.AMOUNT);
            EmployeeBox employeeCell = (EmployeeBox) table.getColumnById(i, ItemTableConstants.EMPLOYEE);

            Integer column = table.getColumnId(ItemTableConstants.AMOUNT);
            if (amountTextBox != null && employeeCell != null && employeeCell.getEmployee() != null) {
                BigDecimal empMode = empModeMap.get(employeeCell.getEmployee().getId()) != null ? empModeMap.get(employeeCell.getEmployee().getId()) : BigDecimal.ONE;
                BigDecimal amount = value != null && !value.isEmpty() ? PayrollClientUtils.parseToBigDecimal(value) : BigDecimal.ZERO;
                amountTextBox.setItemValue(amount.multiply(empMode).setScale(2, RoundingMode.HALF_UP).toString());
                table.getGrid().getModel().update(i, column, amountTextBox);
            }
        }
        updateTotal();
    }

    private void applyDefaultDate() {
        if (defaultDateWidget.getDate() != null) {
            EditableTable table = paymentsTable;
            for (int i = 0; i < table.getRowCount(); i++) {
                ExtendedDatePicker defaultSelectedDate = (ExtendedDatePicker) table.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
                Integer column = table.getColumnId(ItemTableConstants.PAYMENT_DATE);
                if (defaultSelectedDate != null) {
                    defaultSelectedDate.setItemValue(defaultDateWidget.getDate());
                    table.getGrid().getModel().update(i, column, defaultSelectedDate);
                }
            }
        }
    }

    private boolean isSelectedGroupBox() {
        return supervisorLookUp.isSelected() || locationLookUp.isSelected() || employeeLookUp.isSelected() || payrollBatchLookUp.isSelected() || departmentLookUp.isSelected();
    }

    private void onChangeMonthEndYear() {
        if (isSelectedGroupBox()) {
            Integer currentYear = year.getSelectedId();
            Integer monthDayCount = CalendarUtil.getMonthDaysCount(month.getSelectedId(), currentYear);
            Date monthEndDate = new Date(currentYear - 1900, month.getSelectedId(), monthDayCount);

            if (defaultDateWidget != null) {
                defaultDateWidget.setDate(monthEndDate);
                applyDefaultDate();
            }
            changeReferenceValue();
            onChangeGroup();
        } else {
            Info.show(wfmMessages.notSelected(groupBox.getGroupLabel().getElement().getInnerHTML()), Info.Type.WARNING);
        }
    }

    private void changeReferenceValue() {
        String departmentText = departmentLookUp != null && departmentLookUp.getSelectedItem() != null ? departmentLookUp.getSelectedItem().getName() : "";
        String supervisorText = supervisorLookUp != null && supervisorLookUp.getSelectedItem() != null ? supervisorLookUp.getSelectedItem().getName() : "";
        String locationText = locationLookUp != null && locationLookUp.getSelectedItem() != null ? locationLookUp.getSelectedItem().getName() : "";
        String employeeText = employeeLookUp != null && employeeLookUp.getSelectedItem() != null ? employeeLookUp.getSelectedItem().getName() : "";
        String payrollBatchText = payrollBatchLookUp != null && payrollBatchLookUp.getSelectedItem() != null ? payrollBatchLookUp.getSelectedItem().getName() : "";
        String monthText = month != null && month.getSelectedItem() != null ? month.getSelectedItem().getName() : "";
        String yearText = year != null && year.getSelectedItem() != null ? year.getSelectedItem().getName() : "";
        String defaultReferenceText = monthText + " " + yearText + " " + departmentText + supervisorText + locationText + employeeText + payrollBatchText;
        referenceTextBox.setText(defaultReferenceText);
    }


    private static class EmployeeBox extends Div implements CustomCellInterface {
        Integer deductionId;
        SelectItem employee;
        Integer incidentCount;
        Span span = new Span();
        EditableTextBox textBox = new EditableTextBox();

        public EmployeeBox(Integer deductionId, SelectItem employee, Integer countIncident) {
            super();
            this.deductionId = deductionId;
            this.employee = employee;
            this.incidentCount = countIncident;

            if (employee.getDescription() != null && !"".equals(employee.getDescription())) {
                textBox.setText(employee.getDescription() + " -> " + employee.getName());
            } else {
                textBox.setText(employee.getName());
            }

            if (incidentCount > 0) {
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
            if (Utils.hasGenericAccess(GenericSettingsEnum.SHOW_INCIDENT_COUNT_ADDITIONAL_PAYMENT)) {
                if (incidentCount > 0) {
                    return textBox.getText() + " <span class='tab-label'>" + incidentCount + "</span>";
                }
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
    }

    private void generatePDF(Integer pdfId) {
        AdditionalPaymentRequestObject requestObject = new AdditionalPaymentRequestObject(data.getObjectID(), employeeId);
        requestObject.setPdfTemplateID(pdfId);
        String pdfURL = CommandConstants.PDF_URL + (employeeId != null ? "/additionalPaymentItemPdfHandler" : "/additionalPaymentPdfHandler");
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(getRootElement(), pdfURL, parametrs, "_blank");
    }

    private void generatePDF(boolean isLandscape) {
        String URL = CommandConstants.PDF_URL + "/additionalPaymentPdfHandler";
        RequestObject requestObject = new RequestObject(data.getObjectID());
        requestObject.setIS_LANDSCAPE(isLandscape);
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(getRootElement(), URL, parametrs, "_blank");
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }

    public Div getEditDiv() {
        return editDiv;
    }

    public Div getDraftDiv() {
        return draftDiv;
    }

    public Div getSubmitDiv() {
        return submitDiv;
    }

    public Div getApproveDiv() {
        return approveDiv;
    }

    public Div getDeclineDiv() {
        return declineDiv;
    }

    public Div getPdfDiv() {
        return pdfDiv;
    }

    public List<PaymentDeductionSelectItem> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(ArrayList<PaymentDeductionSelectItem> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public String getDefaultPaymentType() {
        return defaultPaymentType;
    }

    public void setDefaultPaymentType(String defaultPaymentType) {
        this.defaultPaymentType = defaultPaymentType;
    }

    private BigDecimal getBigDecimalValue(String value) {
        if (value == null || "0".equals(value) || "0.0".equals(value) || "0.00".equals(value)) {
            return BigDecimal.ZERO;
        }
        return PayrollClientUtils.parseToBigDecimal(value);
    }

    public List<HistoryListItem> getHistoryNodes() {
        return historyNodes;
    }
}
