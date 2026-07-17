package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BalanceSheet;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet.BalancesheetSettingsItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
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
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 17.09.2014.
 */
public class NewBalanceSheet extends Composite implements AccountingConstants {
    interface BalanceSheetUiBinder extends UiBinder<HTMLPanel, NewBalanceSheet> {
    }

    private static final BalanceSheetUiBinder ourUiBinder = GWT.create(BalanceSheetUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();
    private final boolean isProject_To_Head_Enabled = Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && Utils.hasGenericAccess(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED);

    private Date startDate;
    private Date financialStartDate;
    private Date currentDate;

    private Integer baseCurrencyId;
    private KpiModal filterDialog;
    private DepartmentLookUp departmentLookUp;
    private CRMLookUp projectLookUp;
    private KpiCheckBox consolidation;
    private KpiCheckBox excludeZero;
    private KpiCheckBox summaryView;
    private final DatePicker daysValues;
    private final GBoxItem currencyBox;
    private final DataListBox currencyListBox;
    private final Span currencyListBoxText;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private boolean isCash = false;
    private boolean isBank = false;
    private BigDecimal totalCashBank = BigDecimal.ZERO;

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    Element assetsHeader;
    @UiField
    Element assetsFooter;
    @UiField
    Element assetsBody;
    @UiField
    Element liabilityHeader;
    @UiField
    Element liabilityFooter;
    @UiField
    Element liabilityBody;
    @UiField
    Element dateLabel;
    @UiField
    Element dateCaption;

    public NewBalanceSheet() {
        initWidget(ourUiBinder.createAndBindUi(this));
        currencyListBoxText = new Span();

        GBox gBox = headerPanel.drawNewGroupBox();
        gBox.setStyleUnited(true);
        gBox.setStyleWidthFree(true);

        currencyListBox = new DataListBox();
        currencyListBox.setMaxWidth("8.46rem");
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.ensureDebugId("balance-currencyListBox");
        currencyListBox.addValueChangeHandler(changeEvent -> onCurrencyChange());

        currentDate = getLastDateOfSelectedMonth();

        daysValues = new DatePicker();
        daysValues.setDate(currentDate);
        daysValues.ensureDebugId("balance-dayDatePicker");
        daysValues.addChangeHandler(changeEvent -> onCurrencyChange());
        headerPanel.addGroupBoxItem(accountingStrings.balanceDate(), daysValues);
        currencyBox = headerPanel.addGroupBoxItem(wfmStrings.currency(), currencyListBox);

        initFilterPopup();

        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable caught) {
                onUpdate();
            }

            @Override
            public void onSuccess(TrialBalanceFilterData result) {
                startDate = result.getConversationDate();
                financialStartDate = result.getFinancialYearEnd().getNonConvertedDate();
                financialStartDate = DateUtil.addDays(financialStartDate, 1);
                financialStartDate.setYear(currentDate.getYear());
                setCurrency(result.getCurrencies());
                if (currencyListBox.getSelectedId() == null && result.getBaseCurrency() != null) {
                    currencyListBox.setSelected(result.getBaseCurrency());
                }
                onUpdate();
            }
        });

        WfmButton2 filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        filterButton.addClickHandler(clickEvent -> filterDialog.open());
        GBoxItem filterItem = headerPanel.addGroupBoxItem(null, filterButton);
        filterItem.setStyleSplitRight(true);
        filterItem.setStyleWidthFree(true);


        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> onUpdate());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);

        exportSection();

        GBoxItem currencyTextItem = headerPanel.addGroupBoxItem(null, currencyListBoxText);
        currencyTextItem.setStyleSplitRight(true);
        currencyTextItem.setStyleNoBorder(true);
        currencyTextItem.setStyleWidthFree(true);
        currencyTextItem.getElement().setAttribute("style", "margin-left: auto;");

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_VOID, NewBalanceSheet.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MONEY_TRANSFER, NewBalanceSheet.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, NewBalanceSheet.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, NewBalanceSheet.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, NewBalanceSheet.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, NewBalanceSheet.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FIXED_ASSET_SAVED, NewBalanceSheet.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXCHANGE_RATE_ADDED, NewBalanceSheet.this, (sender, args) -> onCurrencyChange());

        dateLabel.setInnerHTML(wfmStrings.asOF());

    }

    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.setWidth(400);
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        if (isDepartmentRelationEnabled) {
            departmentLookUp = new DepartmentLookUp();
            departmentLookUp.ensureDebugId("balanceSheet-department-LookUp");
            contentPanel.add(new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentLookUp));
        }
        if (isProject_To_Head_Enabled) {
            projectLookUp = new CRMLookUp(LookUpConstants.PROJECT);
            projectLookUp.setFullSearch(true);
            projectLookUp.ensureDebugId("balanceSheet-project-LookUp");
            contentPanel.add(new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp));
        }

        consolidation = new KpiCheckBox(accountingStrings.consolidation());
        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP)) {
            consolidation.ensureDebugId("balance-consolidation-checkBox");
            consolidation.addClickHandler(event -> {
                if (consolidation.getValue()) {
                    currencyListBox.setSelected(baseCurrencyId);
                    currencyListBox.setEnabled(false);
                    currencyListBoxText.setText("");
                } else {
                    currencyListBox.setEnabled(true);
                }
            });
            contentPanel.add(new FormGroup(consolidation));
        }

        summaryView = new KpiCheckBox(wfmStrings.summaryView());
        summaryView.setEnabled(false);
        summaryView.ensureDebugId("summary-checkBox");
        contentPanel.add(new FormGroup(summaryView));

        excludeZero = new KpiCheckBox(accountingStrings.excludeZeroValues());
        excludeZero.ensureDebugId("exludeZero-checkBox");
        contentPanel.add(new FormGroup(excludeZero));

        filterDialog.add(contentPanel);
        filterDialog.addButton(new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            if (departmentLookUp != null) {
                departmentLookUp.clear();
            }
            if (projectLookUp != null) {
                projectLookUp.clear();
            }
            currencyListBox.setEnabled(true);

            consolidation.setValue(false);
            summaryView.setValue(false);
            excludeZero.setValue(false);
        }));

        filterDialog.addButton(new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS, clickEvent -> {
            filterDialog.close();
            onUpdate();
        }));
    }

    private void onUpdate() {
        if (summaryView.getValue()) {
            initInternalSummary();
        } else {
            initInternal();
        }
    }

    private Date getLastDateOfSelectedMonth() {
        Date result;
        try {
            result = DateUtil.getMonthLastDate(new Date());
        } catch (Exception e) {
            return new Date();
        }
        return result;
    }

    private void initInternal() {
        LoadingPanel.loading(true);
        currentDate = daysValues.getDate();
        if (LocaleInfo.getCurrentLocale().getLocaleName().equals("uz")) {
            dateCaption.setInnerHTML(DateUtils.format(currentDate) + " " + wfmStrings.asAt());
            dateLabel.setInnerHTML("");
        } else {
            dateCaption.setInnerHTML(DateUtils.format(currentDate));
        }
        Integer departmentID = isDepartmentRelationEnabled && departmentLookUp != null ? departmentLookUp.getSelectedItemID() : null;
        Integer projectID = isProject_To_Head_Enabled && projectLookUp != null ? projectLookUp.getSelectedItemID() : null;
        AccountingService.App.get().getBalanceSheet(Utils.getStartDateNC(startDate), Utils.getEndDateNC(currentDate), consolidation.getValue(), departmentID, projectID, currencyListBox.getSelectedId(), new AbstractAsyncCallback<BalanceSheet>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(BalanceSheet sheet) {
                summaryView.setEnabled(true);

                clearElementChild(assetsHeader);
                clearElementChild(assetsBody);
                clearElementChild(assetsFooter);

                clearElementChild(liabilityHeader);
                clearElementChild(liabilityBody);
                clearElementChild(liabilityFooter);

                for (BalancesheetSettingsItem setting : sheet.getSettings().getSettings()) {
                    if (Constants.ASSETS.equals(setting.getCode())) {
                        createGroupHeader(assetsHeader, wfmStrings.assets());
                        for (BalancesheetSettingsItem item : setting.getItems()) {
                            createBalanceSheet(assetsBody, sheet.getItemByKey(item.getCode()), item.getCode());
                        }
                        createTotal(assetsFooter, sheet.getTotalAsset());
                    } else {
                        createGroupHeader(liabilityHeader, setting.getTitle());
                        for (BalancesheetSettingsItem item : setting.getItems()) {
                            createBalanceSheet(liabilityBody, sheet.getItemByKey(item.getCode()), null);
                        }
                        createTotal(liabilityFooter, sheet.getTotalLiability());
                    }
                }
                LoadingPanel.loading(false);
                Utils.table__frame_affix_init();
            }
        });
    }

    private void initInternalSummary() {
        LoadingPanel.loading(true);
        currentDate = daysValues.getDate();
        if (LocaleInfo.getCurrentLocale().getLocaleName().equals("uz")) {
            dateCaption.setInnerHTML(DateUtils.format(currentDate) + " " + wfmStrings.asAt());
            dateLabel.setInnerHTML("");
        } else {
            dateCaption.setInnerHTML(DateUtils.format(currentDate));
        }
        Integer departmentID = isDepartmentRelationEnabled && departmentLookUp != null ? departmentLookUp.getSelectedItemID() : null;
        Integer projectID = isProject_To_Head_Enabled && projectLookUp != null ? projectLookUp.getSelectedItemID() : null;
        AccountingService.App.get().getBalanceSheetSummary(Utils.getStartDateNC(startDate), Utils.getEndDateNC(currentDate), consolidation.getValue(), departmentID, projectID, currencyListBox.getSelectedId(), new AbstractAsyncCallback<BalanceSheetSummary>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BalanceSheetSummary sheet) {
                clearElementChild(assetsHeader);
                clearElementChild(assetsBody);
                clearElementChild(assetsFooter);

                clearElementChild(liabilityHeader);
                clearElementChild(liabilityBody);
                clearElementChild(liabilityFooter);

                if (sheet.getAssets().getItems().length > 0) {
                    createGroupHeader(assetsHeader, wfmStrings.assets());
                    createBalanceSheetSummary(assetsBody, sheet.getAssets());
                    createTotal(assetsFooter, sheet.getAssets().getTotal());
                }
                if (sheet.getLiabilities().getItems().length > 0) {
                    createGroupHeader(liabilityHeader, wfmStrings.liabilities());
                    createBalanceSheetSummary(liabilityBody, sheet.getLiabilities());
                    createTotal(liabilityFooter, sheet.getLiabilities().getTotal());
                }
                LoadingPanel.loading(false);
                Utils.table__frame_affix_init();
            }
        });
    }

    private void setCurrency(CurrencyItem[] currencies) {
        currencyListBox.setItems(currencies);
        currencyBox.setVisible(currencies.length > 1);
        for (CurrencyItem currency : currencies) {
            if (currency.isCompanyCurrency()) {
                baseCurrencyId = currency.getId();
                currencyListBox.setSelected(currency.getId());
            }
        }
    }

    private void onCurrencyChange() {
        currentDate = daysValues.getDate();
        if (currencyListBox.getSelectedId().equals(baseCurrencyId)) {
            currencyListBoxText.setText("");
        } else {
            CurrencyService.App.get().getCurrencyRateByDate(currencyListBox.getSelectedId(), new DateNonConvertable(currentDate), new AbstractAsyncCallback<CurrencyListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    currencyListBoxText.setText("");
                }

                @Override
                public void onSuccess(CurrencyListItem result) {
                    String text = "1 " + result.getBaseCurrency().getName() + " = ";
                    text = text.concat(AccountingUtils.get().formatExRate(result.getExchangeRate()) + " ");
                    text = text.concat(result.getCurrency().getName());
                    text = text.concat("(" + DateUtils.getDateAndTimeFormatShort1(currentDate) + ")");
                    currencyListBoxText.setText(text);
                }
            });
        }
    }

    private void createGroupHeader(Element element, String name) {
        Element tr = DOM.createTR();
        Element th = DOM.createTH();
        th.setInnerHTML(name);
        tr.appendChild(th);
        Element td1 = DOM.createTD();
        td1.setInnerHTML("&nbsp;");
        tr.appendChild(td1);
        element.appendChild(tr);
    }

    private void createTotal(Element element, BalanceSheetInnerItem item) {
        Element tr = DOM.createTR();
        Element th = DOM.createTH();
        th.setInnerHTML(item.getName());
        tr.appendChild(th);
        Element td1 = DOM.createTD();
        td1.addClassName(Constants.RIGHT_ALIGN_CELL);
        td1.setInnerHTML(getValueAsString(item.getValue()));
//        td1.appendChild(getDOMLink(item.getValue(), item.getEntityID()));
        tr.appendChild(td1);
        element.appendChild(tr);
    }

    private void createBalanceSheet(Element element, BalanceSheetItem itemGroup, String code) {
        if (itemGroup != null && itemGroup.getItems().length > 0) {
            Element table = DOM.createTable();
            if (AccountingConstants.CASH.equals(code) && itemGroup.getTotal() != null) {
                isCash = true;
                totalCashBank = totalCashBank.add(itemGroup.getTotal().getValue());
            } else if (AccountingConstants.BANK.equals(code)) {
                isBank = true;
                totalCashBank = totalCashBank.add(itemGroup.getTotal().getValue());
            }

            Element tHead = DOM.createTHead();
            createGroupHeader(tHead, itemGroup.getName());
            table.appendChild(tHead);

            Element tFoot = DOM.createTFoot();
            createTotal(tFoot, itemGroup.getTotal());
            table.appendChild(tFoot);

            Element tBody = DOM.createTBody();
            for (BalanceSheetInnerItem item : itemGroup.getItems()) {
                if (excludeZero.getValue()) {
                    if (item.getValue() != null && AccountingUtils.get().formatPrice(item.getValue().abs()).equals(AccountingUtils.get().formatPrice(BigDecimal.ZERO))) {
                        continue;
                    }
                }
                Element tr = DOM.createTR();
                Element td = DOM.createTD();
                td.setInnerHTML(item.getName());
                tr.appendChild(td);
                Element td1 = DOM.createTD();
                td1.addClassName(Constants.RIGHT_ALIGN_CELL);
//                td1.setInnerHTML(getValueAsString(item.getValue()));
                td1.appendChild(getDOMLink(item.getValue(), item.getAccountID()));
                tr.appendChild(td1);
                tBody.appendChild(tr);
            }

            table.appendChild(tBody);
            element.appendChild(table);

            if (isCash && isBank) {
                Element cashBank = DOM.createTFoot();
                BalanceSheetInnerItem cashBankTotal = new BalanceSheetInnerItem();
                cashBankTotal.setValue(totalCashBank);
                cashBankTotal.setName(accountingStrings.cashAndCashEquivalents());
                createTotal(cashBank, cashBankTotal);
                Element tableCashBank = DOM.createTable();
                tableCashBank.appendChild(cashBank);
                element.appendChild(tableCashBank);
                totalCashBank = BigDecimal.ZERO;
                isCash = false;
                isBank = false;
//                table.appendChild(cashBank);
            }
        }
    }

    private void createBalanceSheetSummary(Element element, BalanceSheetItem itemGroup) {
        if (itemGroup != null && itemGroup.getItems().length > 0) {
            Element table = DOM.createTable();

            Element tBody = DOM.createTBody();
            for (BalanceSheetInnerItem item : itemGroup.getItems()) {
                if (excludeZero.getValue()) {
                    if (item.getValue() != null && AccountingUtils.get().formatPrice(item.getValue().abs()).equals(AccountingUtils.get().formatPrice(BigDecimal.ZERO))) {
                        continue;
                    }
                }
                Element tr = DOM.createTR();
                Element td = DOM.createTD();
                td.setInnerHTML(item.getName());
                tr.appendChild(td);
                Element td1 = DOM.createTD();
                td1.addClassName(Constants.RIGHT_ALIGN_CELL);
//                td1.setInnerHTML(getValueAsString(item.getValue()));
                td1.appendChild(getDOMLink(item.getValue(), item.getAccountID()));
                tr.appendChild(td1);
                tBody.appendChild(tr);
            }

            table.appendChild(tBody);
            element.appendChild(table);
        }
    }

    private Element getDOMLink(BigDecimal value, final Integer accountId) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(getValueAsString(value));
        link.setAttribute("style", "cursor: pointer;");


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

        if (accountId != null && accountId > 0) {
            DOM.sinkEvents(link.cast(), Event.ONCLICK);
            DOM.setEventListener(link.cast(), event -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|transactionsByPeriod/" + accountId + "/balanceSheet" +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(startDate) +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(currentDate) +
                    "/" + finalDepartmentid +
                    "/" + finalDepartmentName +
                    "/" + finalProjectId +
                    "/" + finalProjectName));
        } else {
            DOM.sinkEvents(link.cast(), Event.ONCLICK);
            DOM.setEventListener(link.cast(), event -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|accountTransaction/-1/balanceSheet" +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(financialStartDate) +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(currentDate) +
                    "/" + finalDepartmentid +
                    "/" + finalDepartmentName +
                    "/" + finalProjectId +
                    "/" + finalProjectName));
        }

        return link;
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
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

        mdp.add(NewBalanceSheet.this::getPortraitLink);
        mdp.add(NewBalanceSheet.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();


        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(clickEvent -> {
            String URL = (CommandConstants.COMMON_URL + "/balanceSheetExcelHandler");
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setPropertyCode("balanceSheet");
            filter.setStartDateNC(Utils.getStartDateNCForFilter(startDate));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(currentDate));
            filter.setActualDue(consolidation.getValue());
            filter.setShowBudget(summaryView.getValue());
            filter.setCurrencyID(currencyListBox.getSelectedId());
            filter.setAvoidZero(excludeZero.getValue());
            if (isDepartmentRelationEnabled && departmentLookUp != null) {
                filter.setDepartmentId(departmentLookUp.getSelectedItemID());
            }
            if (isProject_To_Head_Enabled && projectLookUp != null) {
                filter.setProjectId(projectLookUp.getSelectedItemID());
            }
            Utils.sendPDFOrExcelRequest(exportPanel, URL, filter.getRequestParams(), "_blank");
        });

        showMenuContainer.add(exportExl);

        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
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
        String URL = (CommandConstants.PDF_URL + "/balanceSheetPDFHandler");
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLandscape(landscape);
        filter.setPropertyCode("balanceSheet");
        filter.setStartDateNC(Utils.getStartDateNCForFilter(startDate));
        filter.setEndDateNC(Utils.getEndDateNCForFilter(currentDate));
        filter.setActualDue(consolidation.getValue());
        filter.setShowBudget(summaryView.getValue());
        filter.setCurrencyID(currencyListBox.getSelectedId());
        filter.setAvoidZero(excludeZero.getValue());
        if (isDepartmentRelationEnabled && departmentLookUp != null) {
            filter.setDepartmentId(departmentLookUp.getSelectedItemID());
        }
        if (isProject_To_Head_Enabled && projectLookUp != null) {
            filter.setProjectId(projectLookUp.getSelectedItemID());
        }
        Utils.sendPDFOrExcelRequest(exportPanel, URL, filter.getRequestParams(), "_blank");
    }
}
