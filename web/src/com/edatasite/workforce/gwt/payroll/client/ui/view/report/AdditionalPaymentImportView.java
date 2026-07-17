package com.edatasite.workforce.gwt.payroll.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ReferenceInsertionTable;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.AdditionalPaymentImportItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollBatchLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollCategoryLookUp;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADDITIONAL_PAYMENT_LIST;

/**
 * Created by Shohruh on 07 Nov 2016.
 */
public class AdditionalPaymentImportView extends CustomForm2 {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final WfmStrings wfmStrings = GWT.create(WfmStrings.class);

    private static final Integer PAYMENT = 1;
    private static final Integer COMMISSION = 2;
    private static final Integer DEDUCTION = 3;

    private WfmForm table;
    private KpiCheckBox csvHeaderBox;
    private TextBox referenceBox;
    private DataListBox paymentDate;
    private DataListBox monthBox;
    private DataListBox yearBox;
    private ChosenApproversWidget approver;
    private PayrollBatchLookUp payrollBatchLookUp;
    private DataListBox typeList;
    private KpiCheckBox showInPayslip;

    private DataListBox employeeCodeBox;
    private DataListBox amountBox;
    private PayrollCategoryLookUp categoryLookUp;
    private ReferenceInsertionTable categoryInsertionTable;

    private Integer objectID;
    private String additionalPayment = "additionalPayment";

    private SelectItem[] items;
    private char defaultSeparator = ',';

    private final DateTimeFormat format_month = DateTimeFormat.getFormat("MMMM");
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");

    private CurrencyItem currency;

    public AdditionalPaymentImportView(Integer objectID) {
        super("add", "Import Additional Payment");
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {

        table = new WfmForm(new String[]{"7%", "100%", "25%"});
        table.setLabelSize("150px");
        table.setLabelAlignment(WfmForm.ALIGN_RIGHT);

        csvHeaderBox = new KpiCheckBox();
        csvHeaderBox.setValue(Boolean.TRUE);
        csvHeaderBox.setText(wfmStrings.myCSVFileHasHeaders());
        csvHeaderBox.ensureDebugId(additionalPayment + "csvHeaderBox");

        referenceBox = new TextBox();
//        referenceBox.addStyleName(DEFAULT_WIDTH);
        referenceBox.ensureDebugId(additionalPayment + "referenceBox");

        paymentDate = new DataListBox();
        paymentDate.ensureDebugId(additionalPayment + "dayBox");

        monthBox = new DataListBox();
        monthBox.ensureDebugId(additionalPayment + "monthBox");
        setMonthItems();

        yearBox = new DataListBox();
        yearBox.setWithoutNullLabel(true);
        yearBox.ensureDebugId(additionalPayment + "yearBox");
        setYearItems();
        yearBox.setSelected(Integer.valueOf(format_year.format(new Date())));

        payrollBatchLookUp = new PayrollBatchLookUp();
        payrollBatchLookUp.ensureDebugId(additionalPayment + "payrollBatchLookUp");
        payrollBatchLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeGroup());

        approver = new ChosenApproversWidget(RelationItem.TYPE_ADDITIONAL_PAYMENT, null);
        approver.ensureDebugId(additionalPayment + "approver");

        typeList = new DataListBox();
        setTypeItems();

        showInPayslip = new KpiCheckBox(wfmStrings.showInPayslip());
        showInPayslip.ensureDebugId(additionalPayment + "showInPayslip");

        //table data
        employeeCodeBox = new DataListBox();
        employeeCodeBox.ensureDebugId(additionalPayment + "employeeCodeBox");

        amountBox = new DataListBox();
        amountBox.ensureDebugId(additionalPayment + "amountBox");

        categoryLookUp = new PayrollCategoryLookUp("Payment");
        categoryLookUp.ensureDebugId(additionalPayment + "categoryLookUp");

        categoryInsertionTable = new ReferenceInsertionTable(wfmStrings.category(), categoryLookUp, true, table);

        InputGroup periodPanel = new InputGroup();
        periodPanel.add(monthBox, true);
        periodPanel.add(yearBox, true);

        addTitleField(AdditionalPaymentImport.BASIC_INFORMATION, getTitle("Basic Information"));
        addField(AdditionalPaymentImport.REFERENCE, referenceBox, getTitle(wfmStrings.reference()));
        addField(AdditionalPaymentImport.MONTH, periodPanel, getTitle(wfmStrings.period()));
        addField(AdditionalPaymentImport.PAYROLL_BATCH, payrollBatchLookUp, getTitle(wfmStrings.group()));
        addField(AdditionalPaymentImport.APPROVERS, approver, getTitle(wfmStrings.approver()));
        addField(AdditionalPaymentImport.TYPE, typeList, getTitle(wfmStrings.type()));
        addField(AdditionalPaymentImport.SHOW_IN_PAYSLIP, showInPayslip, "", true);

        addTitleField(AdditionalPaymentImport.MAIN_PANEL, getTitle("Main Panel"));
        addField(AdditionalPaymentImport.HAS_CSV_HEADER, csvHeaderBox, "", true);
        addField(AdditionalPaymentImport.EMPLOYEE_NUMBER, employeeCodeBox, getTitle(wfmStrings.employeeCode()));
        addField(AdditionalPaymentImport.AMOUNT, amountBox, getTitle(wfmStrings.amount()));
        addField(AdditionalPaymentImport.CATEGORY, categoryInsertionTable.getTable(), getTitle(wfmStrings.category()));
        addField(AdditionalPaymentImport.PAYMENT_DATE, paymentDate, getTitle(wfmStrings.paymentDate()));

        show();
    }

    private void changeCategoryLookUp(String type) {
        categoryLookUp.setCategoryType(type);
        categoryLookUp.clearOracleItems();
        categoryLookUp.clearAndClearItems();
        categoryLookUp.clearLaters();
        categoryLookUp.clear();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), "import_save_button", "import_save_and_close", (ClickHandler) clickEvent -> save());
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        CommonService.App.get().getCSVColumns(objectID, new AsyncCallback<HashMap<String, SelectItem[]>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(HashMap<String, SelectItem[]> map) {
                LoadingPanel.loading(false);

                for (Map.Entry<String, SelectItem[]> entry : map.entrySet()) {
                    String key = entry.getKey();
                    items = entry.getValue();

                    if (!key.equals(new String(new char[]{defaultSeparator}))) {
                        defaultSeparator = key.charAt(0);
                    }

                    setItems(items, employeeCodeBox, amountBox, categoryInsertionTable.getCsvDataListBox(), paymentDate);
                }
            }
        });

        PayrollService.App.get().getPayrollBatchCurrency(new ListingFilterParameter(), new AsyncCallback<CurrencyItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CurrencyItem result) {
                currency = result;
            }
        });
    }

    private void setItems(SelectItem[] items, final DataListBox... dataListBoxes) {
        for (DataListBox dataListBox : dataListBoxes) {
            if (dataListBox != null) {
                dataListBox.setItems(items);
            }
        }
        employeeCodeBox.setSelectedByValue(wfmStrings.employeeCode());
        amountBox.setSelectedByValue(wfmStrings.amount());
        categoryInsertionTable.getCsvDataListBox().setSelectedByValue(wfmStrings.category());
        paymentDate.setSelectedByValue(wfmStrings.paymentDate());
    }

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(referenceBox)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(monthBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(yearBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(payrollBatchLookUp)) {
            errors++;
        }
        if (currency == null || currency.getId() == null || currency.getId() <= 0) {
            errors++;
            Info.show(wfmStrings.noDefaultCurrencyForGroup(), Info.Type.WARNING);
        }
        if (!approver.isValid()) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(employeeCodeBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(amountBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(paymentDate, new HTML(), "")) {
            errors++;
        }
        if (!categoryInsertionTable.validate()) {
            errors++;
        }
        if (errors != 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }

        if (errors > 0) {
            return false;
        }

        if (Utils.isAdditionalPaymentsLocked()) {
            Integer currentYear = yearBox.getSelectedId();
            Integer currentMonth = monthBox.getSelectedId();
            int monthDayCount = CalendarUtil.getMonthDaysCount(currentMonth, currentYear);
            DateNonConvertable toDate = new DateNonConvertable(new Date(currentYear - 1900, currentMonth, monthDayCount));

            if (DateUtils.getTransactionLockDate().after(toDate.getNonConvertedDate())) {
                Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.additionalPayment(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                return false;
            }
        }

        return true;
    }

    private void save() {
        if (!validate()) {
            return;
        }

        AdditionalPayment data = new AdditionalPayment();
        data.setReference(referenceBox.getText());
        data.setMonth(monthBox.getSelectedItem().getName());
        data.setMonthID(monthBox.getSelectedId());
        data.setYear(yearBox.getSelectedId());
        data.setPayrollBatch(payrollBatchLookUp.getSelectedItem());
        data.setApprovers(approver.getChosenApprovers());
        data.setShowInPayslip(showInPayslip.getValue());
        data.setStatusCode(Constants.PAYMENT_STATUS_DRAFT);
        data.setCurrency(currency);
        if (DEDUCTION.equals(typeList.getSelectedId())) {
            data.setCategoryType(PayrollConstants.CATEGORY_DEDUCTION);
        }

        AdditionalPaymentImportItem item = new AdditionalPaymentImportItem();
        item.setEmployeeCode(employeeCodeBox.getSelectedId());
        item.setAmount(amountBox.getSelectedId());
        item.setCategory(categoryInsertionTable.getData());
        item.setAdditionalPaymentDate(paymentDate.getSelectedId());

        boolean hasHeader_ = csvHeaderBox.getValue();
        ImportFile importFile = item.getImportFile();
        importFile.setFileID(objectID);
        importFile.setType(ImportTypeEnum.ADDITIONAL_PAYMENT);
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader_);

        LoadingPanel.loading(true);
        PayrollService.App.get().addImportToQueue(data, importFile, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show("Error occured. Please check back later.", Info.Type.WARNING);
            }

            @Override
            public void onSuccess(String result) {
                LoadingPanel.loading(false);
                if (result != null && !"".equals(result)) {
                    String errorMessage = result + " " + wfmStrings.importIsAlreadyInProgress();
                    Info.show(errorMessage, Info.Type.WARNING);
                } else {
                    Info.show("Payments are importing...", Info.Type.INFO);
                    closeTab();
                }
            }
        });
    }

    private void onChangeGroup() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(payrollBatchLookUp.getSelectedItemID());
        LoadingPanel.loading(true);
        PayrollService.App.get().getPayrollBatchCurrency(filterParameter, new AsyncCallback<CurrencyItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CurrencyItem result) {
                LoadingPanel.loading(false);
                currency = result;
            }
        });
    }

    private void setTypeItems() {
        typeList.setWithoutNullLabel(true);

        typeList.addListItem(new SelectItem(PAYMENT, Property.get(ADDITIONAL_PAYMENT_LIST, wfmStrings.additionalPayment())));
        typeList.addListItem(new SelectItem(COMMISSION, wfmStrings.byCommission()));
        typeList.addListItem(new SelectItem(DEDUCTION, payrollStrings.additionalDeduction()));

        typeList.setSelected(PAYMENT);

        typeList.addValueChangeHandler(c -> {
            if (DEDUCTION.equals(typeList.getSelectedId())) {
                changeCategoryLookUp("Deduction");
            } else {
                changeCategoryLookUp("Payment");
            }
        });
    }

    private void setMonthItems() {
        SelectItem[] monthItems = new SelectItem[12];
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 0; i < 12; i++) {
            monthItems[i] = new SelectItem(i, format_month.format(date));
            date = DateUtil.addMonths(date, 1);
        }
        monthBox.setItems(monthItems);
        monthBox.setSelectedNullLabel();
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
        yearBox.setItems(yearItem);
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_ADDITIONAL_PAYMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
