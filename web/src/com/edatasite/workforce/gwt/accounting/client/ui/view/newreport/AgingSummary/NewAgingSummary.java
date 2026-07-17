package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.AgingSummary;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.paging.PagingWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 18.09.2014.
 */
public class NewAgingSummary extends Composite implements Constants, AccountingConstants {
    interface AgingSummaryUiBinder extends UiBinder<HTMLPanel, NewAgingSummary> {
    }

    private static final AgingSummaryUiBinder ourUiBinder = GWT.create(AgingSummaryUiBinder.class);

    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final Integer DEFAULT_INTERVAL = 30;
    private static final Integer DEFAULT_INTERVAL_LIMIT = 90;

    private String type;
    private CrmAccountLookUp crmAccountLookUp;
    private HashMap<Integer, BigDecimal> balanceByColumn;
    private List<AgingSummaryItem> items;
    private Integer columnCount;
    private Integer interval;
    private Integer intervalLimit;
    private KpiModal filterDialog;
    private KpiCheckBox detailView;
    private KpiCheckBox excludePrepayments;
    private DatePicker dateValue;
    private TextBox intervalTextBox;
    private TextBox intervalLimitTextBox;
    private GBoxItem pdfTemplateBoxItem;
    private DataListBox pdfTemplates;
    private WfmButton2 filterButton;
    private WfmButton2 updateButton;
    private PagingWidget pagingWidget;

    @UiField
    HTMLPanel topPanel;
    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    Element tableHead;
    @UiField
    Element tableBody;
    @UiField
    Element tableSection;
    @UiField
    HTMLPanel noResultMessage;

    private CurrencyItem currencyItem;

    public NewAgingSummary(String type_) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        AccountingService.App.get().getCompanyBaseCurrency(new AsyncCallback<CurrencyItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CurrencyItem currencyItem) {
                NewAgingSummary.this.currencyItem = currencyItem;
                NewAgingSummary.this.type = type_;
                onInitialize();
            }
        });
    }

    public static native void frameAffix() /*-{
        $wnd.table__frame_affix_init();
    }-*/;

    private void onInitialize() {
        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        dateValue = new DatePicker();
        dateValue.setDate(new Date());
        dateValue.ensureDebugId("agedReceivables-date");
        if (type.equals(PAYABLE) || !Utils.hasRole(CLIENT)) {
            headerPanel.addGroupBoxItem(wfmStrings.date(), dateValue);
        }
        intervalTextBox = new TextBox();
        Validation.addNumericKeyboardListener(intervalTextBox, 0);
        intervalTextBox.setText(DEFAULT_INTERVAL.toString());
        interval = DEFAULT_INTERVAL;
        intervalTextBox.ensureDebugId("agedReceivables-interval");
        if (type.equals(PAYABLE) || !Utils.hasRole(CLIENT)) {
            headerPanel.addGroupBoxItem(wfmStrings.interval(), intervalTextBox);
        }
        intervalLimitTextBox = new TextBox();
        Validation.addNumericKeyboardListener(intervalLimitTextBox, 0);
        intervalLimitTextBox.setText(DEFAULT_INTERVAL_LIMIT.toString());
        intervalLimit = DEFAULT_INTERVAL_LIMIT;
        intervalLimitTextBox.ensureDebugId("agedPayables-through-daysPastDue");
        if (type.equals(PAYABLE) || !Utils.hasRole(CLIENT)) {
            headerPanel.addGroupBoxItem(accountingStrings.limitDays(), intervalLimitTextBox);
        }
        initFilterPopup();
        filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        filterButton.addClickHandler(event -> filterDialog.open());
        if (type.equals(PAYABLE) || !Utils.hasRole(CLIENT)) {
            GBoxItem filterItem = headerPanel.addGroupBoxItem(null, filterButton);
            filterItem.setStyleSplitRight(true);
        }
        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> onUpdate(false));

        if (type.equals(PAYABLE) || !Utils.hasRole(CLIENT)) {
            GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
            updateItem.setStyleSplitRight(true);
            updateItem.getComponent().getElement().addClassName("group-box__item-content--no-border");
        }
        if (type.equals(PAYABLE) || !Utils.hasRole(CLIENT)) {
            headerPanel.addGroupBoxItem(exportSection());
        }
        pdfTemplates = new DataListBox();
        if (type.equals(PAYABLE) || !Utils.hasRole(CLIENT)) {
            pdfTemplateBoxItem = headerPanel.addGroupBoxItem(wfmStrings.pleaseSelectPdfTemplate(), pdfTemplates);
            pdfTemplateBoxItem.setStyleSplitRight(true);
        }
        noResultMessage.getElement().getStyle().setDisplay(Style.Display.NONE);

        pagingWidget = new PagingWidget();
        pagingWidget.setLimit(50);
        pagingWidget.setPaging(new PagingWidget.Paging() {

            @Override
            public void loadData(int start, int limit) {
                getReportData(start - 1, limit); // start begins from 1
            }

        });
        GBoxItem pageItem = new GBoxItem(pagingWidget);
        pageItem.addStyleToComponent("paging-group__wrapper");
        pageItem.addStyleName("ml-auto group-box__item--split-right");
        headerPanel.addGroupBoxItem(pageItem);
        onUpdate(true);
    }

    private ListingFilterParameter getFilter() {
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setInterval(getInterval());
        filter.setIntervalLimit(getIntervalLimit());
        filter.setAccountType(type);
        filter.setShowBudget(detailView.getValue());
        filter.setClientId(crmAccountLookUp.getSelectedItemID());
        filter.setExcludePrePayments(excludePrepayments.getValue());
        filter.setStartDateNC(Utils.getStartDateNCForFilter(dateValue.getDate()));
        return filter;
    }

    private void getReportData(int start, int limit) {
        LoadingPanel.loading(true);
        ListingFilterParameter filter = getFilter();
        filter.setLimit(limit);
        filter.setStart(start);
        filter.setFromListing(true);
        InvoiceService.App.get().getOverdueInvoiceByCrmAccount(filter, new AsyncCallback<ListResult<AgingSummaryItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ListResult<AgingSummaryItem> result) {
                LoadingPanel.loading(false);
                items = result.getList();
                pagingWidget.setTotalCount(result.getTotal());
                drawHeader(interval, intervalLimit);
                if (detailView.getValue()) {
                    setItemsForDetailView();
                } else {
                    setItems();
                }
                frameAffix();
            }
        });
    }

    private Integer getInterval() {
        return Integer.parseInt(intervalTextBox.getValue());
    }

    private Integer getIntervalLimit() {
        return Integer.parseInt(intervalLimitTextBox.getValue());
    }

    private void showHidePdfTemplates() {
        SelectItem selectedItem = crmAccountLookUp.getSelectedItem();
        pdfTemplateBoxItem.setVisible(selectedItem != null);
    }

    private void onUpdate(boolean isFirstClick) {
        noResultMessage.getElement().setInnerHTML(" ");
        noResultMessage.getElement().getStyle().setDisplay(Style.Display.NONE);
        //clearElementChild(tableBody);
        clearElementChild(tableHead);
        if (isFirstClick) {
            initFinancialSettings();
        }
        interval = Integer.parseInt(intervalTextBox.getText());
        intervalLimit = Integer.parseInt(intervalLimitTextBox.getText());
        drawHeader(interval, intervalLimit);

        initInternal();
        showHidePdfTemplates();
    }

    private void initInternal() {
        if (!detailView.getValue()) {
            InvoiceService.App.get().getCompanyPdfTemplates(type.equals(RECEIVABLE) ? AGED_RECEIVABLE : AGED_PAYABLE, new AsyncCallback<PdfTemplateItemList>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(PdfTemplateItemList result) {

                    pdfTemplates.setItems(result.getItems());
                }
            });
        }
        pagingWidget.resetAndReload();
    }

    private void drawHeader(final Integer days, final Integer limit) {
        clearElementChild(tableBody);
        clearElementChild(tableHead);

        if (limit % days == 0) {
            columnCount = limit / days + 4;
        } else {
            columnCount = limit / days + 5;
        }
        Element theadTR = DOM.createTR();
        tableHead.appendChild(theadTR);
        if (detailView.getValue()) {
            createTHeadTH(theadTR, wfmStrings.invoiceDate(), 100, TEXT_LEFT);
            createTHeadTH(theadTR, wfmStrings.dueDate(), 100, TEXT_LEFT);
//            createTHeadTH(theadTR, wfmStrings.number(), 150, TEXT_LEFT);
            createTHeadTH(theadTR, wfmStrings.reference(), 150, TEXT_LEFT);
        } else {
            if (RECEIVABLE.equals(type)) {
                createTHeadTH(theadTR, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), 300, TEXT_LEFT);
            } else {
                createTHeadTH(theadTR, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), 300, TEXT_LEFT);
            }
        }
        createTHeadTH(theadTR, wfmStrings.current(), 80, TEXT_RIGHT);
        int i, j, startLimit, endLimit;

        for (i = 1, j = 0; i < columnCount - 2; i++, j++) {
            startLimit = j * days + 1;
            endLimit = (j + 1) * days;

            if (startLimit > limit) {
                createTHeadTH(theadTR, " > " + limit, 80, TEXT_RIGHT);
            } else {
                if (endLimit >= limit) {
                    createTHeadTH(theadTR, startLimit + " - " + limit, 80, TEXT_RIGHT);
                } else {
                    createTHeadTH(theadTR, startLimit + " - " + endLimit, 80, TEXT_RIGHT);
                }
            }
        }

        createTHeadTH(theadTR, accountingMessages.total("(" + currencyItem.getName() + ")"), 80, TEXT_RIGHT);

        detailView.setEnabled(true);
        updateButton.setEnabled(true);
    }

    private void setItemsForDetailView() {
        if (items != null && items.size() > 0) {

            for (AgingSummaryItem item : items) {
                BigDecimal total = ZERO;

                Element tr = DOM.createTR();
                tr.addClassName("row-head");
                tableBody.appendChild(tr);
                Element td = DOM.createTD();
                td.appendChild(getAsLink(item.getCustomerOrSupplier(), (type.equals(RECEIVABLE) ? "client|summary/" : "suppliersummary|summary/") + item.getCustomerOrSupplierObjectId(), item.getCustomerOrSupplier()));
                td.setAttribute("colspan", String.valueOf(columnCount + 2));
                tr.appendChild(td);

                for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
                    Integer start = j * getInterval();
                    Integer in = (j + 1) * getInterval();

                    if (item.getInvoiceList() != null && !item.getInvoiceList().isEmpty())
                        for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                            if (start > getIntervalLimit()) {
                                start = getIntervalLimit();
                            }
                            if (in > getIntervalLimit()) {
                                in = getIntervalLimit();
                            }
                            if ((inv.getAging() > j * getInterval() && inv.getAging() <= in) || (inv.getAging() > getIntervalLimit() && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                                Element tr2 = DOM.createTR();
                                tableBody.appendChild(tr2);

                                Element tdInvoiceDate = DOM.createTD();
                                tdInvoiceDate.setInnerHTML(DateUtils.format(inv.getInvoiceDate().getNonConvertedDate()));
                                tr2.appendChild(tdInvoiceDate);

                                Element tdDueDate = DOM.createTD();
                                tdDueDate.setInnerHTML(DateUtils.format(inv.getDueDate().getNonConvertedDate()));
                                tr2.appendChild(tdDueDate);

                                Element tdInvoiceNumber = DOM.createTD();
                                if (inv != null && inv.getTypeName().equals("Bank Receipt")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "spendreceivemoney|summary/" + inv.getObjectID() + "/" + "RECEIVE_MONEY", inv.getInvoiceNumber()));
                                } else if (inv != null && inv.getTypeName().equals("Bank Payment")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "spendreceivemoney|summary/" + inv.getObjectID() + "/" + "SPEND_MONEY", inv.getInvoiceNumber()));
                                } else if (inv != null && inv.getTypeName().equals("Cash Payment")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "spendreceivemoney|summary/" + inv.getObjectID() + "/" + "CASH_PAYMENT", inv.getInvoiceNumber()));
                                } else if (inv != null && inv.getTypeName().equals("Cash Receipt")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "spendreceivemoney|summary/" + inv.getObjectID() + "/" + "CASH_RECEIPT", inv.getInvoiceNumber()));
                                } else if (inv != null && inv.getTypeName().equals("Pre Payment")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "invoicepayment|paymentView/" + inv.getObjectID(), inv.getInvoiceNumber()));
                                } else if (inv != null && inv.getTypeName().equals("Manual Journal")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "manual|summary/" + inv.getObjectID(), inv.getInvoiceNumber()));
                                } else if (inv != null && inv.getTypeName().equals("Expense")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "expenseReports|previewReport/" + inv.getObjectID() + "/EXPENSE_VIEW/ACCOUNTING", inv.getInvoiceNumber()));
                                } else if (inv != null && inv.getTypeName().equals("Check Transaction")) {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), "check|summary/" + inv.getObjectID(), inv.getInvoiceNumber()));
                                } else if (inv != null && "EXCHANGEGAINANDLOSS".equals(inv.getTypeName())) {
                                    KpiModal modal = new KpiModal();
                                    Integer[] journalIds = inv.getJournalId() != null
                                            ? Arrays.stream(inv.getJournalId().replaceAll("[{}]", "").split(",")).map(String::trim).map(Integer::parseInt).toArray(Integer[]::new)
                                            : new Integer[0];
                                    BigDecimal[] exchangeRates = inv.getExchangeRates() != null
                                            ? Arrays.stream(inv.getExchangeRates().replaceAll("[{}]", "").split(",")).map(String::trim).map(BigDecimal::new).toArray(BigDecimal[]::new)
                                            : new BigDecimal[0];
                                    BigDecimal[] currencyDifferences = inv.getCurrencyDifference() != null
                                            ? Arrays.stream(inv.getCurrencyDifference().replaceAll("[{}]", "").split(",")).map(String::trim).map(BigDecimal::new).toArray(BigDecimal[]::new)
                                            : new BigDecimal[0];
                                    String currencyName = inv.getCurrencyName();
                                    FlexTable flexTable = new FlexTable();
                                    flexTable.setStyleName(DEFAULT_WIDTH);
                                    flexTable.setWidget(0, 0, new Span(wfmStrings.journalReport()));
                                    flexTable.setWidget(0, 1, new Span(wfmStrings.exchangeRate()));
                                    flexTable.setWidget(0, 2, new Span(wfmStrings.currency()));
                                    flexTable.setWidget(0, 3, new Span(wfmStrings.difference()+" "+wfmStrings.amount()));

                                    for (int l = 0; l < journalIds.length; l++) {
                                        Element journalId = DOM.createTD();
                                        journalId.appendChild(getAsLink(journalIds[l].toString(), "clickedreport|journalReport/" + journalIds[l], journalIds[l].toString()));
                                        flexTable.setWidget(l+1, 0, new Span(journalId));
                                        flexTable.setWidget(l+1, 1, new Span(exchangeRates[l] != null ? exchangeRates[l].toString() : ""));
                                        flexTable.setWidget(l+1, 2, new Span(currencyName));
                                        flexTable.setWidget(l+1, 3, new Span(currencyDifferences[l] != null ? currencyDifferences[l].toString() : ""));
                                    }
                                    flexTable.setWidth("100%");
                                    modal.add(flexTable);
                                    modal.setWidth(1000);
                                    Element link = DOM.createAnchor();
                                    link.setInnerHTML(wfmStrings.currencyDifference());
                                    DOM.sinkEvents(link.cast(), Event.ONCLICK);
                                    DOM.setEventListener(link.cast(), event -> {
                                       modal.open();
                                    });
                                    tdInvoiceNumber.appendChild(link);
                                } else {
                                    tdInvoiceNumber.appendChild(getAsLink(inv.getInvoiceNumber(), (type.equals(RECEIVABLE) ? "saleinvoice|summary/" : "purchaseinvoice|summary/") + inv.getObjectID(), inv.getInvoiceNumber()));
                                }
                                tr2.appendChild(tdInvoiceNumber);
//                                Element tdReference = DOM.createTD();
//                                tdReference.setInnerHTML(inv.getReference() != null ? inv.getReference() : "");
//                                tr2.appendChild(tdReference);

                                for (int x = 0; x < i - 1; x++) {
                                    Element tdTemp = DOM.createTD();
                                    tr2.appendChild(tdTemp);
                                }

                                Element tdAmount = DOM.createTD();
                                tdAmount.addClassName(TEXT_RIGHT);
                                tdAmount.setTitle(inv.getAmount().setScale(5, RoundingMode.HALF_UP).toString());
                                tdAmount.setInnerHTML(getValueAsString(inv.getAmount()));
                                tr2.appendChild(tdAmount);

                                for (int x = i; x < columnCount - 1; x++) {
                                    Element tdTemp = DOM.createTD();
                                    tr2.appendChild(tdTemp);
                                }

                                total = total.add(inv.getAmount());
                            }
                        }
                }

                Element tr3 = DOM.createTR();
                tr3.addClassName("total_row");
                tableBody.appendChild(tr3);

                Element td4 = DOM.createTD();
                td4.setInnerHTML(wfmStrings.total());
                tr3.appendChild(td4);

                Element td5 = DOM.createTD();
                td5.addClassName(TEXT_RIGHT);
                td5.setTitle(total.setScale(5, RoundingMode.HALF_UP).toString());
                td5.appendChild(getAsLink(getValueAsString(total), (type.equals(RECEIVABLE) ? "customerBalance|customerBalance/" + item.getCustomerOrSupplierObjectId().toString() + "/" + CrmAccountItem.CUSTOMER : "supplierBalance|supplierBalance/" + item.getCustomerOrSupplierObjectId().toString() + "/" + CrmAccountItem.SUPPLIER)));
                td5.setAttribute("colspan", String.valueOf(columnCount + 1));
                tr3.appendChild(td5);

                //Empty row
                Element tr6 = DOM.createTR();
                tr6.addClassName("row-spacing");
                Element td6 = DOM.createTD();
                td6.setAttribute("colspan", String.valueOf(columnCount + 2));
                tr6.appendChild(td6);
                tableBody.appendChild(tr6);
            }
        } else {
            noResultMessage.getElement().setInnerHTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
            noResultMessage.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        }
    }

    public void setItems() {
        if (items != null && !items.isEmpty()) {
            Element tr = DOM.createTR();
            tableBody.appendChild(tr);
            Element td = DOM.createTD();
            td.getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
            td.setInnerHTML(type.equals(RECEIVABLE) ? wfmStrings.receivables().toUpperCase(Locale.ROOT) : wfmStrings.payable().toUpperCase(Locale.ROOT));
            td.setAttribute("colspan", String.valueOf(columnCount));
            tr.appendChild(td);
            List<AgingSummaryItem> systemCalculateGainAndLossItems = new ArrayList<>();
            for (AgingSummaryItem item : items) {
                if (item.getAccountType().equals("ACCOUNT")) {
                    systemCalculateGainAndLossItems.add(item);
                } else {
                    generateItemsTable(item);
                }
            }
            if (!systemCalculateGainAndLossItems.isEmpty()) {
                Element trGAL = DOM.createTR();
                tableBody.appendChild(trGAL);
                Element tdGAL = DOM.createTD();
                tdGAL.getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
                tdGAL.setInnerHTML(wfmStrings.gainAndLossAmountsInRealTime() + "           <a target=\"blanck\" href=\"https://www.kpi.com/wiki/gain-loss-for-unpaid-invoices\">... " + wfmStrings.more().toLowerCase(Locale.ROOT) + "</a>");
                tdGAL.setAttribute("colspan", String.valueOf(columnCount));
                tdGAL.setAttribute("title", "Gain/Loss=SUM(Invoice Due Amount × (Current Exchange Rate - Invoice Exchange Rate)) ");
                tdGAL.setAttribute("style", "background-color : #f5f7f9;font-weight: 500; height: 3.25rem; font-size: 1.35rem;");
                trGAL.appendChild(tdGAL);
                for (AgingSummaryItem item : systemCalculateGainAndLossItems) {
                    generateItemsTable(item);
                }
            }
            initTotal();
        } else {
            noResultMessage.getElement().setInnerHTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
            noResultMessage.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        }
    }

    private void generateItemsTable(AgingSummaryItem item) {
        Element tr;
        Element td;
        tr = DOM.createTR();
        tableBody.appendChild(tr);
        BigDecimal total = ZERO;
        td = DOM.createTD();
        td.getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
        tr.appendChild(td);

        if (item.getAccountType().equals("ACCOUNT")) {
            td.appendChild(getAsLink(item.getCustomerOrSupplier(), ("clickedreport|transactionsByPeriod/") + item.getCustomerOrSupplierObjectId(), item.getCustomerOrSupplier()));
        } else if (item.getCustomerOrSupplierObjectId() != null && item.getCustomerOrSupplierObjectId() != 0) {
            td.appendChild(getAsLink(item.getCustomerOrSupplier(), (type.equals(RECEIVABLE) ? "client|summary/" : "suppliersummary|summary/") + item.getCustomerOrSupplierObjectId(), item.getCustomerOrSupplier()));
        } else {
            td.appendChild(new Span(item.getCustomerOrSupplier()).getElement());
        }

        for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
            BigDecimal balance = ZERO;
            Integer start = j * getInterval();
            Integer in = (j + 1) * getInterval();

            if (item.getInvoiceList() != null && !item.getInvoiceList().isEmpty())
                for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                    if (start > getIntervalLimit()) {
                        start = getIntervalLimit();
                    }
                    if (in > getIntervalLimit()) {
                        in = getIntervalLimit();
                    }
                    if ((inv.getAging() > j * getInterval() && inv.getAging() <= in) || (inv.getAging() > getIntervalLimit() && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                        balance = balance.add(inv.getAmount());
                    }
                }
            Element td2 = DOM.createTD();
            td2.addClassName(TEXT_RIGHT);
            tr.appendChild(td2);
            td2.setTitle(balance.setScale(5, RoundingMode.HALF_UP).toString());
            td2.setInnerHTML(getValueAsString(balance));
            total = total.add(balance);
        }

        if (total.compareTo(ZERO) != 0) {
            Element td3 = DOM.createTD();
            td3.addClassName(TEXT_RIGHT);
            td3.setTitle(total.setScale(5, RoundingMode.HALF_UP).toString());
            tr.appendChild(td3);
            if (item.getAccountType().equals("ACCOUNT")) {
                td3.appendChild(getAsLink(getValueAsString(total), ("clickedreport|transactionsByPeriod/" + item.getCustomerOrSupplierObjectId().toString())));
            } else {
                td3.appendChild(getAsLink(getValueAsString(total), (type.equals(RECEIVABLE) ? "customerBalance|customerBalance/" + item.getCustomerOrSupplierObjectId().toString() + "/" + CrmAccountItem.CUSTOMER : "supplierBalance|supplierBalance/" + item.getCustomerOrSupplierObjectId().toString() + "/" + CrmAccountItem.SUPPLIER)));
            }
        }
    }

    private void initTotal() {
        InvoiceService.App.get().getOverdueInvoiceByCrmAccount(getFilter(), new AsyncCallback<ListResult<AgingSummaryItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ListResult<AgingSummaryItem> result) {
                LoadingPanel.loading(false);

                if (detailView.getValue()) {
                    setTotalForDetailView(result.getList());
                } else {
                    setTotal(result.getList());
                }
                frameAffix();
            }
        });
    }

    private void setTotalForDetailView(List<AgingSummaryItem> list) {
        if (list != null && list.size() > 0) {
            balanceByColumn = new HashMap<>();
            for (AgingSummaryItem item : list) {
                BigDecimal total = ZERO;
                for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
                    BigDecimal balance = ZERO;
                    Integer start = j * getInterval();
                    Integer in = (j + 1) * getInterval();
                    if (item.getInvoiceList() != null && !item.getInvoiceList().isEmpty())
                        for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                            if (start > getIntervalLimit()) {
                                start = getIntervalLimit();
                            }
                            if (in > getIntervalLimit()) {
                                in = getIntervalLimit();
                            }
                            if ((inv.getAging() > j * getInterval() && inv.getAging() <= in) || (inv.getAging() > getIntervalLimit() && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                                total = total.add(inv.getAmount());
                                balance = balance.add(inv.getAmount());
                            }
                        }
                    balanceByColumn.put(i, getColumnBalance(i).add(balance));
                }
                balanceByColumn.put(columnCount - 1, getColumnBalance(columnCount - 1).add(total));
            }

            Element bottomPercentTr = DOM.createTR();
            Element bottomTotalTr = DOM.createTR();
            tableBody.appendChild(bottomPercentTr);
            if (!Utils.hasRole(CLIENT)) {
                tableBody.appendChild(bottomTotalTr);
            }
            bottomPercentTr.addClassName("total_row");
            bottomTotalTr.addClassName("total_row double");

            Element percentTd = DOM.createTD();
            bottomPercentTr.appendChild(percentTd);
            Element totalTd = DOM.createTD();
            bottomTotalTr.appendChild(totalTd);

            percentTd.setInnerHTML(accountingStrings.agingPercentage());
            percentTd.setAttribute("colspan", "3");
            totalTd.setInnerHTML(type.equals(RECEIVABLE) ? accountingStrings.totalReceivables() : wfmStrings.totalPayables());
            totalTd.setAttribute("colspan", "3");//for invoice date,due date, reference

            for (int i = 1; i < columnCount; i++) {
                Element percentages = DOM.createTD();
                percentages.addClassName(TEXT_RIGHT);
                bottomPercentTr.appendChild(percentages);
                if (balanceByColumn.get(columnCount - 1).compareTo(BigDecimal.ZERO) == 0) {
                    percentages.setInnerHTML("0.00 %");
                } else {
                    percentages.setInnerHTML(getValueAsString(balanceByColumn.get(i).divide(balanceByColumn.get(columnCount - 1), RoundingMode.HALF_UP).multiply(new BigDecimal(100))) + " %");
                }

                Element totals = DOM.createTD();
                totals.addClassName(TEXT_RIGHT);
                bottomTotalTr.appendChild(totals);
                totals.setTitle(balanceByColumn.get(i).setScale(5, RoundingMode.HALF_UP).toString());
                totals.setInnerHTML(getValueAsString(balanceByColumn.get(i)));
            }

            Element lastTr = DOM.createTR();
            Element lastTd = DOM.createTD();
            lastTd.setAttribute("colspan", String.valueOf(columnCount + 2));
            lastTr.appendChild(lastTd);
            tableBody.appendChild(lastTr);
        } else {
            noResultMessage.getElement().setInnerHTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
            noResultMessage.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        }
    }

    public void setTotal(List<AgingSummaryItem> list) {
        if (list != null && list.size() > 0) {
            balanceByColumn = new HashMap<>();

            for (AgingSummaryItem item : list) {
                BigDecimal total = ZERO;
                for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
                    BigDecimal balance = ZERO;
                    Integer start = j * getInterval();
                    Integer in = (j + 1) * getInterval();

                    if (item.getInvoiceList() != null && !item.getInvoiceList().isEmpty())
                        for (AgingSummaryInvoiceItem inv : item.getInvoiceList()) {
                            if (start > getIntervalLimit()) {
                                start = getIntervalLimit();
                            }
                            if (in > getIntervalLimit()) {
                                in = getIntervalLimit();
                            }
                            if ((inv.getAging() > j * getInterval() && inv.getAging() <= in) || (inv.getAging() > getIntervalLimit() && i == columnCount - 2) || (i == 1 && inv.getAging() == 0)) {
                                balance = balance.add(inv.getAmount());
                            }
                        }
                    balanceByColumn.put(i, getColumnBalance(i).add(balance));
                    total = total.add(balance);
                }
                if (total.compareTo(ZERO) != 0) {
                    balanceByColumn.put(columnCount - 1, getColumnBalance(columnCount - 1).add(total));
                }
            }

            Element bottomPercentTr = DOM.createTR();
            Element bottomTotalTr = DOM.createTR();
            tableBody.appendChild(bottomPercentTr);
            if (!Utils.hasRole(CLIENT)) {
                tableBody.appendChild(bottomTotalTr);
            }
            bottomPercentTr.addClassName("total_row");
            bottomTotalTr.addClassName("total_row total_row_last");

            Element percentTd = DOM.createTD();
            bottomPercentTr.appendChild(percentTd);
            Element totalTd = DOM.createTD();
            bottomTotalTr.appendChild(totalTd);

            percentTd.setInnerHTML(accountingStrings.agingPercentage());
            totalTd.setInnerHTML(type.equals(RECEIVABLE) ? accountingStrings.totalReceivables() : wfmStrings.totalPayables());

            for (int i = 1; i < columnCount; i++) {
                Element percentages = DOM.createTD();
                percentages.addClassName(TEXT_RIGHT);
                bottomPercentTr.appendChild(percentages);
                BigDecimal divisor = balanceByColumn.get(columnCount - 1);
                BigDecimal value = balanceByColumn.get(i);
                if (value != null) {
                    if (divisor != null && divisor.compareTo(BigDecimal.ZERO) == 0) {
                        percentages.setInnerHTML("0.00 %");
                    } else {
                        if (divisor != null) {
                            percentages.setInnerHTML(getValueAsString(value.divide(divisor, RoundingMode.HALF_UP).multiply(new BigDecimal(100))) + " %");
                        } else {
                            percentages.setInnerHTML("0.00 %");
                        }
                    }
                    Element totals = DOM.createTD();
                    totals.addClassName(TEXT_RIGHT);
                    bottomTotalTr.appendChild(totals);
                    totals.setTitle(value.setScale(5, RoundingMode.HALF_UP).toString());
                    totals.setInnerHTML(getValueAsString(value));
                }
            }

            Element lastTr = DOM.createTR();
            Element lastTd = DOM.createTD();
            lastTd.setAttribute("colspan", String.valueOf(columnCount));
            lastTr.appendChild(lastTd);
            tableBody.appendChild(lastTr);
        } else {
            noResultMessage.getElement().setInnerHTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
            noResultMessage.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        }
    }

    private void createTHeadTH(Element element, String name, int width, String customStyle) {
        Element th = DOM.createTH();
        th.setClassName("stickerCell");
        if (!customStyle.isEmpty()) {
            th.addClassName(customStyle);
        }
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.getStyle().setProperty("minWidth", width + "px");
        divElement.setInnerText(name);

        th.appendChild(divElement);
        element.appendChild(th);
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private BigDecimal getColumnBalance(Integer columnIndex) {
        BigDecimal balance;
        if (balanceByColumn.get(columnIndex) == null) {
            balance = ZERO;
        } else {
            balance = balanceByColumn.get(columnIndex);
        }
        return balance;
    }

    private Element getAsLink(String text, final String linkHref, String... title) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(text);
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        DOM.setEventListener(link.cast(), event -> {
            if (title.length > 0) {
                SinksContainerFactory.entryPoint.onHistoryChanged(linkHref, title[0]);
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged(linkHref);
            }
        });

        return link;
    }

    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.setWidth(450);
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        String lookUpText = RECEIVABLE.equals(type) ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) + ":" : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()) + ":";
        crmAccountLookUp = new CrmAccountLookUp((RECEIVABLE.equals(type) ? CrmAccountLookUp.CUSTOMER : CrmAccountLookUp.SUPPLIER), true);
        crmAccountLookUp.ensureDebugId("agedReceivables-client-LookUp");
        crmAccountLookUp.getSuggestBox().addSelectionHandler(event -> showHidePdfTemplates());
        if (Utils.hasRole(CLIENT) && type.equals(RECEIVABLE)) {
            crmAccountLookUp.setSelected(Utils.getUserID());
        }
        contentPanel.add(new FormGroup(lookUpText, crmAccountLookUp));

        detailView = new KpiCheckBox(wfmStrings.detail());
        detailView.setEnabled(false);
        detailView.ensureDebugId("detail-checkBox");
        contentPanel.add(new FormGroup(detailView));

        excludePrepayments = new KpiCheckBox(accountingStrings.excludePrepayments());
        excludePrepayments.ensureDebugId("exclude_prepayments-checkBox");
        contentPanel.add(new FormGroup(excludePrepayments));

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_SECONDARY, clickEvent -> {
            if (crmAccountLookUp != null) {
                crmAccountLookUp.clear();
            }
            detailView.setValue(Boolean.FALSE);
            excludePrepayments.setValue(Boolean.FALSE);
        });
        WfmButton2 applyFilterButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS, clickEvent -> {
            filterDialog.close();
            onUpdate(false);
        });

        filterDialog.add(contentPanel);
        filterDialog.addButton(resetButton);
        filterDialog.addButton(applyFilterButton);
    }

    private GBoxItem exportSection() {
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
        showMenuContainer.add(this::getPdfVersion);
        showMenuContainer.add(this::createExcelLink);
        showLink.add(showMenuContainer);

        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, div);
        exportItem.addStyleName("export-item");
        return exportItem;
    }

    private MaterialLink createExcelLink() {
        MaterialLink exportLink = new MaterialLink();
        exportLink.addStyleName("hasicon--left");
        Icon icon = new Icon();
        icon.setClass("ficon--file-excel");
        exportLink.add(icon);
        exportLink.setText(wfmStrings.excel());
        exportLink.addClickHandler(clickEvent -> {
            ListingFilterParameter filterParameter = getFilter();
            String url = (CommandConstants.COMMON_URL + "/agingSummaryExcelHandler");
            filterParameter.setPropertyCode("");
            HashMap<String, String> requestParametrs = filterParameter.getRequestParams();
            Utils.sendPDFOrExcelRequest(topPanel, url, requestParametrs, "_blank");
        });
        return exportLink;
    }

    public Div getPdfVersion() {
        Div wrapper = new Div("java-wrap");

        MaterialLink pdfLink = new MaterialLink();
        MaterialIcon pdfIcon = new MaterialIcon();
        pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
        pdfLink.add(pdfIcon);
        pdfLink.setText(wfmStrings.pdf());

        MaterialDropDown mdp = new MaterialDropDown(pdfLink);
        mdp.setHover(true);
        mdp.setHoverable(true);
        MaterialLink portrait = new MaterialLink();
        portrait.setText(wfmStrings.portrait());
        MaterialLink landscape = new MaterialLink();
        landscape.setText(wfmStrings.landscape());
        mdp.add(portrait);
        mdp.add(landscape);
        portrait.addClickHandler((event) -> {
            ListingFilterParameter filterParameter = getFilter();
            String url = (CommandConstants.PDF_URL + "/agingSummaryPdfHandler");
            HashMap<String, String> requestParametrs = filterParameter.getRequestParams();
            Utils.sendPDFOrExcelRequest(topPanel, url, requestParametrs, "_blank");

        });
        landscape.addClickHandler((event) -> {
            ListingFilterParameter filterParameter = getFilter();
            filterParameter.setLandscape(true);
            String url = (CommandConstants.PDF_URL + "/agingSummaryPdfHandler");
            HashMap<String, String> requestParametrs = filterParameter.getRequestParams();
            Utils.sendPDFOrExcelRequest(topPanel, url, requestParametrs, "_blank");
        });
        wrapper.add(pdfLink);
        wrapper.add(mdp);

        return wrapper;
    }


    private void initFinancialSettings() {
        AccountingService.App.get().getCompanyFinancialSettings(new AsyncCallback<FinancialSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(FinancialSettingsItem financialSettingsItem) {
                if (financialSettingsItem.getAgedPastDueDays() != null) {
                    intervalLimitTextBox.setText(financialSettingsItem.getAgedPastDueDays());
                    intervalLimit = Integer.parseInt(financialSettingsItem.getAgedPastDueDays());
                }
                if (financialSettingsItem.getAgedFilterInterval() != null) {
                    intervalTextBox.setText(financialSettingsItem.getAgedFilterInterval());
                    interval = Integer.parseInt(financialSettingsItem.getAgedFilterInterval());
                }
            }
        });
    }
}
