package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.ProfitAndLoss;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetManagerItems;
import com.edatasite.workforce.gwt.accounting.client.rpc.PnLFilter;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.CompareCategoryEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.CompareWithEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.ProfitAndLoss.Tabs.ProfitTab1;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCustomToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by admin on 05.09.2014.
 */
public class NewProfitAndLoss extends Composite {

    interface NewProfitAndLossUiBinder extends UiBinder<HTMLPanel, NewProfitAndLoss> {
    }

    private static final NewProfitAndLossUiBinder ourUiBinder = GWT.create(NewProfitAndLossUiBinder.class);

    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();



    private Date financialYearStart;
    private Date currentDate;
    private Integer baseCurrencyId;
    private final DateTimeFormat urlDateFormat = DateTimeFormat.getFormat("dd_MM_yyyy");
    private DateTimeFormat format;
    private int columnCount = 0;

    private Boolean isCheckedApplyIncomeTax = false;

    private KpiModal filterDialog;
    private DataListBox compareWithValues;
    private DepartmentLookUp departmentLookUp;
    private ProjectLookUp projectLookUp;
    private KpiCheckBox consolidation;
    private KpiCheckBox applyIncomeTax;
    private KpiCheckBox excludeZero;
    private KpiCheckBox showBudget;
    private KpiCheckBox summaryView;

    private DatePicker fromValue;
    private DatePicker toValue;
    private final DataListBox currencyListBox;

    private DataListBox sortTypeListBox;
    private DataListBox sortByListBox;

    private final KpiCustomToolTip currencyToolTip;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private String[] params;

    private final SelectItem previous6MonthsItem = new SelectItem(CompareWithEnum.Previous6Months.getId(), wfmStrings.previous() + " " + 6 + " " + wfmStrings.months());
    private final SelectItem noneItem = new SelectItem(CompareWithEnum.None.getId(), wfmStrings.none());
    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    ProfitTab1 tab1;
    @UiField
    HTMLPanel exportPanel;

    public NewProfitAndLoss() {
        //Currency Tooltip
        currencyToolTip = new KpiCustomToolTip("");
        currencyToolTip.setVisible(false);

        currencyListBox = new DataListBox();
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.ensureDebugId("profitAndLoss-currencyListBox");
        currencyListBox.addValueChangeHandler(changeEvent -> onCurrencyChange());
        currencyListBox.setMaxWidth("8.46rem");

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        initValues();
    }

    public NewProfitAndLoss(String[] params) {
        this();
        this.params = params;
    }

    private void initValues() {
        format = DateTimeFormat.getFormat("MMMM d, yyyy");
        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable caught) {
                onInitialize();
            }

            @Override
            public void onSuccess(TrialBalanceFilterData result) {
                financialYearStart = DateUtil.addDays(result.getFinancialYearEnd().getNonConvertedDate(), 1);
                currentDate = new Date();
                financialYearStart.setYear(currentDate.getYear());

                while (financialYearStart.after(currentDate)) {
                    financialYearStart.setYear(financialYearStart.getYear() - 1);
                }
                setCurrency(result.getCurrencies());

                if (currencyListBox.getSelectedId() == null && result.getBaseCurrency() != null) {
                    currencyListBox.setSelected(result.getBaseCurrency());
                }

                onInitialize();
                initInternal();
            }
        });
    }


    private void initFormParameters(String[] params) {
        if (params != null && params.length >= 4 && ("balanceSheet".equals(params[1]) || "trialBalance".equals(params[1]) || "accountTransaction".equals(params[1]))) {
            if (params[2] != null) {
                Date externalStartDate = urlDateFormat.parse(params[2]);
                if (fromValue != null) {
                    fromValue.setDate(externalStartDate);
                }
            }
            if (params[3] != null) {
                Date externalEndDate = urlDateFormat.parse(params[3]);
                if (toValue != null) {
                    toValue.setDate(externalEndDate);
                }
            }
        }
    }

    public static native void frameAffix() /*-{
        $wnd.table__frame_affix_init();
    }-*/;

    private void onInitialize() {
        GBox gBox = headerPanel.drawNewGroupBox();
        gBox.setStyleUnited(true);
        gBox.addStyleName("group-box--width-free");

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        fromValue = new DatePicker();
        fromValue.setDate(DateUtil.getMonthFirstDay(new Date()));//when report filtered by previous 6 months, YDT begins this month
        fromValue.ensureDebugId("profitAndLoss-startDate");
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);

        toValue = new DatePicker();
        toValue.setDate(DateUtil.getMonthLastDate(new Date()));
        toValue.ensureDebugId("profitsAndLoss-endDate  ");
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);

        headerPanel.addGroupBoxItem(0, datePeriodItem);
        headerPanel.addGroupBoxItem(0, wfmStrings.currency(), currencyToolTip, currencyListBox);

        fromValue.addChangeHandler(changeEvent -> onCurrencyChange());

        //Filter
        initFilterPopup();

        WfmButton2 filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        new KpiToolTip(filterButton, wfmStrings.filter());
        filterButton.addClickHandler(event -> filterDialog.open());
        GBoxItem filterItem = headerPanel.addGroupBoxItem(0, null, filterButton);
        filterItem.setStyleSplitRight(true);
        filterItem.setStyleWidthFree(true);

        //Sorting
        sortByListBox = new DataListBox();
        sortByListBox.setWithoutNullLabel(true);
        sortByListBox.setMaxWidth("9.46rem");
        sortByListBox.setItems(getSortByItems());
        sortByListBox.setSelected(getSortByItems()[0]);
        sortByListBox.addValueChangeHandler(event -> onUpdate());


        sortTypeListBox = new DataListBox();
        sortTypeListBox.setWithoutNullLabel(true);
        sortTypeListBox.setMaxWidth("5.46rem");
        sortTypeListBox.setItems(getSortItems());
        sortTypeListBox.setSelected(getSortItems()[0]);
        sortTypeListBox.addValueChangeHandler(event -> onUpdate());

        headerPanel.addGroupBoxItem(wfmStrings.sortBy(), sortByListBox);

        GBoxItem sortItem = headerPanel.addGroupBoxItem("", sortTypeListBox);
        sortItem.setStyleSplitRight(true);
        sortItem.setStyleWidthFree(true);

        //Update
        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(clickEvent -> onUpdate());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(0, null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);
        updateItem.getComponent().getElement().addClassName("group-box__item-content--no-border");

        exportSection();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXCHANGE_RATE_ADDED, NewProfitAndLoss.this, (sender, args) -> onCurrencyChange());
        initFormParameters(params);
        tab1.onInitialize(this);
    }

    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.addStyleName("pnl-filterModal");
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        compareWithValues = new DataListBox();
        compareWithValues.setWithoutNullLabel(true);
        compareWithValues.ensureDebugId("profilAndLoss-compareWith-listBox");
        compareWithValues.setItems(getCompareItems());
        compareWithValues.setSelected(noneItem);

        FormGroup compareFormGroup = new FormGroup(accountingStrings.compareWith(), compareWithValues);
        contentPanel.add(compareFormGroup);

        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
            departmentLookUp = new DepartmentLookUp();
            FormGroup departmentFormGroup = new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentLookUp);
            contentPanel.add(departmentFormGroup);
        }
        projectLookUp = new ProjectLookUp(null);
        projectLookUp.ensureDebugId("profilAndLoss-project-LookUp");
        FormGroup projectFormGroup = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp);

        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            contentPanel.add(projectFormGroup);
        }

        consolidation = new KpiCheckBox();
        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP)) {
            consolidation.setText(accountingStrings.consolidation());
            consolidation.ensureDebugId("profitAndlost-consolidation-checkBox");
            consolidation.addClickHandler(event -> {
                applyIncomeTax.setVisible(!consolidation.getValue());
                isCheckedApplyIncomeTax = !consolidation.getValue();
                if (consolidation.getValue()) {
                    currencyListBox.setSelected(baseCurrencyId);
                    currencyListBox.setEnabled(false);
                    clearCurrencyRateHistory();
                } else {
                    currencyListBox.setEnabled(true);
                }
            });
            FormGroup consolidationFormGroup = new FormGroup(consolidation);
            contentPanel.add(consolidationFormGroup);
        }

        applyIncomeTax = new KpiCheckBox();
        if (!Utils.hasGenericAccess(GenericSettingsEnum.ACCOUNTING_APPLY_INCOME_TAX_ENABLED)) {
            applyIncomeTax.setText(accountingStrings.applyIncomeTax());
            applyIncomeTax.addClickHandler(clickEvent -> isCheckedApplyIncomeTax = applyIncomeTax.getValue());

            FormGroup applyIncomeFormGroup = new FormGroup(applyIncomeTax);
            applyIncomeFormGroup.getGroupLabel().removeFromParent();
            contentPanel.add(applyIncomeFormGroup);
        }

        excludeZero = new KpiCheckBox();
        excludeZero.setText(accountingStrings.excludeZeroValues());
        excludeZero.ensureDebugId("exludeZero-checkBox");
        excludeZero.addClickHandler(clickEvent -> {
            if (excludeZero.getValue()) {
                summaryView.setValue(false);
            }
        });

        FormGroup excludeZeroFormGroup = new FormGroup(excludeZero);
        excludeZeroFormGroup.getGroupLabel().removeFromParent();
        contentPanel.add(excludeZeroFormGroup);

        showBudget = new KpiCheckBox();
        showBudget.setText(accountingStrings.showBudget());
        showBudget.ensureDebugId("showBudget-checkBox");
        showBudget.addClickHandler(sender -> {
            if (showBudget.getValue()) {
                compareFormGroup.setVisible(false);
                summaryView.setValue(false);
            } else {
                compareFormGroup.setVisible(true);
            }
        });

        FormGroup showBudgetFormGroup = new FormGroup(showBudget);
        showBudgetFormGroup.getGroupLabel().removeFromParent();
        contentPanel.add(showBudgetFormGroup);

        summaryView = new KpiCheckBox();
        summaryView.setText(wfmStrings.summaryView());
        summaryView.ensureDebugId("summary-checkBox");
        summaryView.addClickHandler(clickEvent -> {
            if (summaryView.getValue()) {
                if (showBudget.getValue()) {
                    showBudget.setValue(false);
                    compareFormGroup.setVisible(true);
                }
                excludeZero.setValue(false);
            }
        });
        FormGroup summaryFormGroup = new FormGroup(summaryView);
        summaryFormGroup.getGroupLabel().removeFromParent();
        contentPanel.add(summaryFormGroup);

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);

        resetButton.addClickHandler(clickEvent -> {
            compareWithValues.clearSelected();
            if (departmentLookUp != null) {
                departmentLookUp.clear();
            }
            projectLookUp.clear();
            currencyListBox.setEnabled(true);

            consolidation.setValue(false);

            applyIncomeTax.setVisible(true);
            applyIncomeTax.setValue(false);
            isCheckedApplyIncomeTax = false;

            compareWithValues.setVisible(true);
            compareFormGroup.setVisible(true);

            excludeZero.setValue(false);
            showBudget.setValue(false);
        });

        WfmButton2 applyFilterButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS);
        applyFilterButton.addClickHandler(clickEvent -> {
            filterDialog.close();
            onUpdate();
        });

        filterDialog.add(contentPanel);
        filterDialog.addButton(resetButton);
        filterDialog.addButton(applyFilterButton);
    }

    private void onUpdate() {
        if (!validate()) {
            return;
        }
        if (isCheckedApplyIncomeTax) {
            updateIncomeTaxTransaction();
        } else {
            initInternal();
        }
    }

    private void updateIncomeTaxTransaction() {
        LoadingPanel.loading(true);
        AccountingService.App.get().updateIncomeTaxData(Utils.getStartDateNC(new DateNonConvertable(DateUtil.resetTime(fromValue.getDate())).getDate()), Utils.getEndDateNC(new DateNonConvertable(DateUtil.getDayLastTime(toValue.getDate())).getDate()), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer integer) {
                initInternal();
            }
        });
    }

    public void initInternal() {
        LoadingPanel.loading(true);
        columnCount = CompareWithEnum.getEnumyById(compareWithValues.getSelectedItem().getId()).getLength() + 1;
        AccountingService.App.get().getProfitAndLoss(getFilter(), new AbstractAsyncCallback<BudgetManagerItems>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(BudgetManagerItems profLoss) {
                LoadingPanel.loading(false);
                tab1.update(profLoss, fromValue.getDate(), toValue.getDate(), excludeZero.getValue(), showBudget.getValue());
                frameAffix();
            }
        });
    }

    private PnLFilter getFilter() {
        PnLFilter result = new PnLFilter();
        DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(fromValue.getDate()));
        DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(toValue.getDate()));
        final FromToDate main = new FromToDate(fromDate, toDate);
        final FromToDate[] compareDates = getCompareDates(fromDate != null ? fromDate.getDate() : null, compareWithValues.getSelectedItem().getId());
        columnCount = CompareWithEnum.getEnumyById(compareWithValues.getSelectedItem().getId()).getLength() + 1;
        result.setMain(main);
        result.setCompareTo(compareDates);
        result.setSortField(sortByListBox.getSelectedItem().getDescription());
        result.setSortDirection(sortTypeListBox.getSelectedItem().getDescription());
        result.setCosolidation(consolidation.getValue());
        result.setDepartmentID(departmentLookUp != null ? departmentLookUp.getSelectedItemID() : null);
        result.setProjectID(projectLookUp.getSelectedItemID());
        result.setCurrencyId(currencyListBox.getSelectedId());
        result.setShowBudget(!consolidation.getValue() && this.showBudget.getValue());
        return result;
    }

    public DatePicker getStartDatePicker() {
        return fromValue;
    }

    public DatePicker getEndDatePicker() {
        return toValue;
    }

    public DataListBox getCompareWithValues() {
        return compareWithValues;
    }

    public FromToDate[] getCompareDates(Date from, Integer compareWithId) {
        CompareWithEnum compareWithEnum = CompareWithEnum.getEnumyById(compareWithId);
        FromToDate[] compareDates = new FromToDate[compareWithEnum.getLength()];
        Date cFrom;
        Date cTo;

        if (CompareCategoryEnum.Day.equals(compareWithEnum.getCompareCategoryEnum())) {
            cFrom = DateUtil.addDays(from, -1);
            cTo = DateUtil.getDayLastTime(DateUtil.addDays(from, -1));
            compareDates[0] = new FromToDate(new DateNonConvertable(cFrom), new DateNonConvertable(cTo));
        } else if (CompareCategoryEnum.Week.equals(compareWithEnum.getCompareCategoryEnum())) {
            cFrom = DateUtil.addDays(DateUtil.getWeekFirstDay(DateUtil.addDays(from, -7)), 1);
            cTo = DateUtil.getDayLastTime(DateUtil.addDays(DateUtil.getWeekLastDay(DateUtil.addDays(from, -7)), 1));
            compareDates[0] = new FromToDate(new DateNonConvertable(cFrom), new DateNonConvertable(cTo));
        } else if (CompareCategoryEnum.Month.equals(compareWithEnum.getCompareCategoryEnum())) {
            for (int i = 0; i < compareWithEnum.getLength(); i++) {
                cFrom = DateUtil.getMonthFirstDay(DateUtil.addMonths(from, -(i + 1)));
                cTo = DateUtil.getDayLastTime(DateUtil.getMonthLastDate(DateUtil.addMonths(from, -(i + 1))));
                compareDates[i] = new FromToDate(new DateNonConvertable(cFrom), new DateNonConvertable(cTo));
            }
        } else if (CompareCategoryEnum.Year.equals(compareWithEnum.getCompareCategoryEnum())) {
            for (int i = 0; i < compareWithEnum.getLength(); i++) {
                cFrom = DateUtil.addYears(from, -(i + 1));
                cTo = DateUtil.getDayLastTime(DateUtil.addDays(DateUtil.addYears(cFrom, 1), -1));
                compareDates[i] = new FromToDate(new DateNonConvertable(cFrom), new DateNonConvertable(cTo));
            }
        }

        return compareDates;
    }

    public Element getDOMLink(BigDecimal value, boolean isNegative, final Integer int_objectID, final Date fromDate, final Date toDate) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(getValueAsString(value, isNegative));
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        String departmentid = "";
        String departmentName = "";
        if (departmentLookUp != null && departmentLookUp.getSelectedItemID() != null) {
            departmentid = departmentLookUp.getSelectedItemID().toString();
            departmentName = departmentLookUp.getSelectedItem().getName();
        }
        String projectId = "";
        String projectName = "";
        if (projectLookUp != null && projectLookUp.getSelectedItemID() != null) {
            projectId = projectLookUp.getSelectedItemID().toString();
            projectName = projectLookUp.getSelectedItem().getName();
        }
        String finalDepartmentid = departmentid;
        String finalDepartmentName = departmentName;
        String finalProjectId = projectId;
        String finalProjectName = projectName;
        DOM.setEventListener(link.cast(), event -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|transactionsByPeriod/" + int_objectID + "/profitAndLoss" +
                "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(fromDate) +
                "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(toDate) +
                "/" + finalDepartmentid +
                "/" + finalDepartmentName +
                "/" + finalProjectId +
                "/" + finalProjectName));

        return link;
    }

    public String getValueAsString(BigDecimal value, boolean isNegative) {
        if (isNegative) {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return "(" + AccountingUtils.get().formatPrice(value) + ")";
            } else {
                return AccountingUtils.get().formatPrice(value.abs());
            }
        } else {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return AccountingUtils.get().formatPrice(value);
            } else {
                return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
            }
        }
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    private SelectItem[] getCompareItems() {
        ArrayList<SelectItem> compareListItems = new ArrayList<>();
        compareListItems.add(noneItem);
        compareListItems.add(new SelectItem(CompareWithEnum.PreviousDay.getId(), wfmStrings.previousDay()));
        compareListItems.add(new SelectItem(CompareWithEnum.PreviousWeek.getId(), wfmStrings.previousWeek()));
        // compare months
        compareListItems.add(new SelectItem(CompareWithEnum.PreviousMonth.getId(), wfmStrings.previousMonth()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous2Months.getId(), wfmStrings.previous() + " " + 2 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous3Months.getId(), wfmStrings.previous() + " " + 3 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous4Months.getId(), wfmStrings.previous() + " " + 4 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous5Months.getId(), wfmStrings.previous() + " " + 5 + " " + wfmStrings.months()));
        compareListItems.add(previous6MonthsItem);
        compareListItems.add(new SelectItem(CompareWithEnum.Previous7Months.getId(), wfmStrings.previous() + " " + 7 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous8Months.getId(), wfmStrings.previous() + " " + 8 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous9Months.getId(), wfmStrings.previous() + " " + 9 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous10Months.getId(), wfmStrings.previous() + " " + 10 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous11Months.getId(), wfmStrings.previous() + " " + 11 + " " + wfmStrings.months()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous12Months.getId(), wfmStrings.previous() + " " + 12 + " " + wfmStrings.months()));

        // compare years
        compareListItems.add(new SelectItem(CompareWithEnum.PreviousYear.getId(), wfmStrings.previous() + " " + " " + wfmStrings.year()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous2Years.getId(), wfmStrings.previous() + " " + 2 + " " + wfmStrings.years()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous3Years.getId(), wfmStrings.previous() + " " + 3 + " " + wfmStrings.years()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous4Years.getId(), wfmStrings.previous() + " " + 4 + " " + wfmStrings.years()));
        compareListItems.add(new SelectItem(CompareWithEnum.Previous5Years.getId(), wfmStrings.previous() + " " + 5 + " " + wfmStrings.years()));

        return compareListItems.toArray(new SelectItem[compareListItems.size()]);
    }

    private void exportSection() {
        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink();
        showLink.addStyleName("btn btn--white btn--icon");

        Icon ieIcon = new Icon();//import/export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        showLink.add(ieIcon);

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2 dropdown-content--export");
        showMenuContainer.setBelowOrigin(true);

        showLink.add(showMenuContainer);


        pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");

        Div wrapper = new Div("java-wrap");
        showMenuContainer.add(wrapper);

        MaterialLink pdfVersion = getPdfVersion();
        wrapper.add(pdfVersion);

        MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
        mdp.setHover(true);
        mdp.setHoverable(true);

        mdp.add(NewProfitAndLoss.this::getPortraitLink);
        mdp.add(NewProfitAndLoss.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();


        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(clickEvent -> {
            String URL = (CommandConstants.COMMON_URL + "/profitLostViewExcelHandler");
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setPropertyCode("newprofitLoss");
            filter.setStartDateNC(Utils.getStartDateNCForFilter(new DateNonConvertable(DateUtil.resetTime(fromValue.getDate()))));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(new DateNonConvertable(DateUtil.getDayLastTime(toValue.getDate()))));
            filter.setSortDir(compareWithValues.getSelectedId());
            filter.setActualDue(consolidation.getValue());
            filter.setDataType("" + compareWithValues.getSelectedItem().getId());
            if (departmentLookUp != null) {
                filter.setDepartmentId(departmentLookUp.getSelectedItemID());
            }
            filter.setProjectId(projectLookUp.getSelectedItemID());
            filter.setSortField(sortByListBox.getSelectedItem().getDescription());
            filter.setSortDir(Constants.ASC_STR.equals(sortTypeListBox.getSelectedItem().getDescription()) ? 1 : 2);
            filter.setCurrencyID(currencyListBox.getSelectedId());
            filter.setAvoidZero(excludeZero.getValue());
            filter.setShowBudget(showBudget.getValue());
            filter.setShowSummaryView(summaryView.getValue());
            filter.setShownObjects(getOnlyStrings(tab1.getShownObjects()));

            HashMap<String, String> parametrs = filter.getRequestParams();
            Utils.sendPDFOrExcelRequest(exportPanel, URL, parametrs, "_blank");
        });

        showMenuContainer.add(exportExl);

        showMenuBar.add(showLink);

        Div div = new Div();
        div.add(showMenuBar);
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, div);
        exportItem.setStyleSplitRight(true);
        exportItem.setStyleWidthFree(true);
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> {
            sendPdfRequest(false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            sendPdfRequest(true);
        });
    }

    private void sendPdfRequest(boolean landscape) {
        String URL = (CommandConstants.PDF_URL + "/profitLostViewPDFHandler");
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLandscape(landscape);
        filter.setPropertyCode("newprofitLoss");
        filter.setStartDateNC(Utils.getStartDateNCForFilter(new DateNonConvertable(DateUtil.resetTime(fromValue.getDate()))));
        filter.setEndDateNC(Utils.getEndDateNCForFilter(new DateNonConvertable(DateUtil.getDayLastTime(toValue.getDate()))));
        filter.setSortDir(compareWithValues.getSelectedId());
        filter.setActualDue(consolidation.getValue());
        filter.setDataType("" + compareWithValues.getSelectedItem().getId());
        if (departmentLookUp != null) {
            filter.setDepartmentId(departmentLookUp.getSelectedItemID());
        }
        filter.setProjectId(projectLookUp.getSelectedItemID());
        filter.setSortField(sortByListBox.getSelectedItem().getDescription());
        filter.setSortDir(Constants.ASC_STR.equals(sortTypeListBox.getSelectedItem().getDescription()) ? 1 : 2);
        filter.setCurrencyID(currencyListBox.getSelectedId());
        filter.setAvoidZero(excludeZero.getValue());
        filter.setShowBudget(showBudget.getValue());
        filter.setShowSummaryView(summaryView.getValue());
        filter.setShownObjects(getOnlyStrings(tab1.getShownObjects()));

        HashMap<String, String> parametrs = filter.getRequestParams();
        Utils.sendPDFOrExcelRequest(exportPanel, URL, parametrs, "_blank");
    }

    private String getOnlyStrings(HashSet<String> shownObjects) {
        StringBuilder stringBuilder = null;
        for (String object : shownObjects) {
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder(object);
            } else {
                stringBuilder.append("," + object);
            }
        }
        return stringBuilder != null ? stringBuilder.toString() : "";
    }

    private void setCurrency(CurrencyItem[] currencies) {
        currencyListBox.setItems(currencies);
        for (CurrencyItem currency : currencies) {
            if (currency.isCompanyCurrency()) {
                baseCurrencyId = currency.getId();
                currencyListBox.setSelected(currency);
            }
        }
    }

    private void onCurrencyChange() {
        currentDate = toValue.getDate();

        if (currencyListBox.getSelectedId() == null) {
            clearCurrencyRateHistory();
            return;
        }
        if (currencyListBox.getSelectedId().equals(baseCurrencyId)) {
            CurrencyItem item = (CurrencyItem) currencyListBox.getSelectedItem();
            clearCurrencyRateHistory();
        } else {
            CurrencyService.App.get().getCurrencyRateByDate(currencyListBox.getSelectedId(), new DateNonConvertable(currentDate), new AbstractAsyncCallback<CurrencyListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    clearCurrencyRateHistory();
                }

                @Override
                public void onSuccess(CurrencyListItem result) {
                    String text = "1 " + result.getBaseCurrency().getName() + " = ";
                    text = text.concat(AccountingUtils.get().formatExRate(result.getExchangeRate()) + " ");
                    text = text.concat(result.getCurrency().getName());
                    text = text.concat(" (" + DateUtils.getDateAndTimeFormatShort1(currentDate) + ")");

                    currencyToolTip.setMessage(text);
                    currencyToolTip.setVisible(true);
                }
            });
        }
    }

    private boolean validate() {
        int errors = 0;
        fromValue.removeStyleName(Constants.ERROR_FORM_STYLE);
        toValue.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!Validation.validateDate(fromValue)) {
            errors++;
        }
        if (!Validation.validateDate(toValue)) {
            errors++;
        }
        if (!Validation.validateDateOrder(fromValue, toValue)) {
            return false;
        }
        return errors == 0;
    }

    private void clearCurrencyRateHistory() {
        currencyToolTip.setMessage("");
        currencyToolTip.setVisible(false);
    }

    private SelectItem[] getSortByItems() {
        SelectItem[] items = new SelectItem[2];

        items[0] = new SelectItem(0, wfmStrings.byAccount(), Constants.ACC_NAME, true);
        items[1] = new SelectItem(1, wfmStrings.byCode(), Constants.ACC_CODE);

        return items;
    }

    private SelectItem[] getSortItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, "A-Z", Constants.ASC_STR, true);
        items[1] = new SelectItem(1, "Z-A", Constants.DESC_STR);
        return items;
    }

    public Date getFinancialYearStart() {
        return financialYearStart;
    }

    public void setFinancialYearStart(Date financialYearStart) {
        this.financialYearStart = financialYearStart;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public DateTimeFormat getFormat() {
        return format;
    }

    public void setFormat(DateTimeFormat format) {
        this.format = format;
    }
}
