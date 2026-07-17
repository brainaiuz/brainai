package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.JournalReport;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ListingResult;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.TransactionJournalLookUp;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.paging.PagingWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
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
import java.util.Date;
import java.util.HashMap;



/**
 * Created by admin on 16.09.2014.
 */
public class NewJournalReport extends Composite implements Constants {
    interface NewJournalReportUiBinder extends UiBinder<HTMLPanel, NewJournalReport> {
    }

    private static final NewJournalReportUiBinder ourUiBinder = GWT.create(NewJournalReportUiBinder.class);

    private static final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel topPanel;
    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    DivElement journalReportSection;

    private KpiModal filterDialog;
    private DataListBox orderByValues;
    private DatePicker fromValue;
    private DatePicker toValue;
    private DepartmentLookUp departmentLookUp;
    private AccountsLookUp accountLookUp;
    private TransactionJournalLookUp transactionJournalLookUp;
    private WfmButton2 filterButton;
    private WfmButton2 updateButton;
    private PagingWidget pagingWidget;
    private final Integer objectId;
    private final boolean multiJournal;
    private final String formType;
    private Integer start;
    private Integer limit;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;

    public NewJournalReport(Integer objectId, boolean multiJournal, String formType) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.objectId = objectId;
        this.multiJournal = multiJournal;
        this.formType = formType;
        onInitialize();
    }

    public void onInitialize() {
        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        orderByValues = new DataListBox();
        orderByValues.setWithoutNullLabel(true);
        orderByValues.setItems(getOrderByItems());
        orderByValues.ensureDebugId("journalReport-orderByList");
        headerPanel.addGroupBoxItem(wfmStrings.orderBy(), orderByValues);

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        Date date = new Date();
        fromValue = new DatePicker();
        fromValue.setDate(DateUtil.getMonthFirstDay(date));
        fromValue.ensureDebugId("journalReport-fromDate");
        fromValue.addChangeHandler(changeEvent -> fromValue.removeStyleName(Constants.ERROR_FORM_STYLE));
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);
        toValue = new DatePicker();
        toValue.setDate(DateUtil.getMonthLastDate(date));
        toValue.ensureDebugId("journalReport-toDate");
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        datePeriodItem.setStyleSplitRight(true);
        headerPanel.addGroupBoxItem(datePeriodItem);

        transactionJournalLookUp = new TransactionJournalLookUp();
        transactionJournalLookUp.setWidth("100%");
        transactionJournalLookUp.ensureDebugId("journalReport-journalIdList");

        if (objectId != null) {
            transactionJournalLookUp.setSelected(new SelectItem(objectId, objectId + ""));
        }

        orderByValues.setSelected(1);

        if (!multiJournal) {
            datePeriodItem.setStyleSplitRight(false);
            initFilterPopup();

            filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
            filterButton.removeHasiconLeftStyle();
            filterButton.addClickHandler(event -> filterDialog.open());
            GBoxItem filterItem = headerPanel.addGroupBoxItem(0, null, filterButton);
            filterItem.setStyleSplitRight(true);
            filterItem.setStyleWidthFree(true);
        }

        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> {
            if (validate()) {
                pagingWidget.resetAndReload();
            }
        });
        GBoxItem updateItem = headerPanel.addGroupBoxItem(0, null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);
        headerPanel.addGroupBoxItem(exportSection());

        pagingWidget = new PagingWidget();

        pagingWidget.setLimit(50);
        pagingWidget.setPaging(getPagingLoader());

//        Div pagingWrapper = new Div("group-box__item group-box__items ml-auto");
        GBoxItem paging = headerPanel.addGroupBoxItem(null, pagingWidget);
        paging.addStyleToComponent("paging-group__wrapper");
        paging.setStyleSplitRight(true);
        paging.addStyleName("ml-auto");
//        pagingWrapper.add(paging);
//        pagingWrapper.add(exportSection());
//        headerPanel.addGroupBoxItem(paging);
//        GBox gb = (GBox) headerPanel.getContentPanel().getWidget(0);
//        GBoxRow groupBoxRow = (GBoxRow) gb.getWidget(0);
//        groupBoxRow.add(pagingWrapper);
//        currencyText.setInnerHTML(accountingStrings.figuresIn() + " " + AccountingUtils.getBaseCurrencySymbol() + " (" + AccountingUtils.getBaseCurrencyCode() + ")");

//        if (objectId != null) {
            pagingWidget.resetAndReload();
//        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEORDER_RECEIVED, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_VOID, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MONEY_TRANSFER, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FIXED_ASSET_SAVED, NewJournalReport.this, (sender, args) -> pagingWidget.resetAndReload());
    }

    private boolean validate() {
        int errors = 0;
        fromValue.removeStyleName(ERROR_FORM_STYLE);
        toValue.removeStyleName(ERROR_FORM_STYLE);

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

    private PagingWidget.Paging getPagingLoader() {
        return (start, limit) -> {
            fromValue.removeStyleName(Constants.ERROR_FORM_STYLE);
            LoadingPanel.loading(true);
            String order;

            if (orderByValues.getSelectedItem().getId() == 1) {
                order = JOURNAL_ID;
            } else {
                order = JOURNAL_DATE;
            }
            Integer departmentID = null;

            if (isDepartmentRelationEnabled && departmentLookUp != null) {
                departmentID = departmentLookUp.getSelectedItemID();
            }

            DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(fromValue.getDate()));
            DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(toValue.getDate()));

            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setDepartmentId(departmentID);
            if (!multiJournal) {
                filter.setAccountID(accountLookUp.getSelectedItemID());
            }
            filter.setStart(start - 1);
            filter.setLimit(limit);
            if (multiJournal) {
                filter.setObjectId(objectId);
                filter.setFormType(formType);
            }
            this.start = start - 1;
            this.limit = limit;
            AccountingService.App.get().getJournalReportWithPaging(fromDate, toDate, order, transactionJournalLookUp.getSelectedItemID(), filter, new AbstractAsyncCallback<ListingResult<Transaction>>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(ListingResult<Transaction> transactions) {
                    LoadingPanel.loading(false);
                    clearElementChild(journalReportSection);
                    pagingWidget.setTotalCount(transactions.getTotal());
                    if (transactions == null || transactions.getList() == null || transactions.getList().length == 0) {
                        Element div = DOM.createDiv();
                        div.setInnerHTML("<div style=\"text-align: center;\">" + wfmStrings.noResultsFoundForTheProvidedSearchCriteria() + "</div>");
                        journalReportSection.appendChild(div);
                    } else {
                        for (Transaction transaction : transactions.getList()) {
                            Element div = DOM.createDiv();
                            div.addClassName("table-responsive baseMargin_bottom_double");
                            Element table = DOM.createTable();
                            table.addClassName("table table--overflow-off table--small-cells table-condensed table-bordered table_striped_cols table-hover table_report table_report_sections valign_middle table_leftIndex");
                            table.setAttribute("cellspacing", "0");
                            table.setAttribute("cellpadding", "0");
                            Element colgroup = DOM.createColGroup();
                            Element col = DOM.createCol();
                            col.setAttribute("width", "");
                            Element col1 = DOM.createCol();
                            col1.setAttribute("width", "18%");
                            Element col2 = DOM.createCol();
                            col2.setAttribute("width", "18%");
                            colgroup.appendChild(col);
                            colgroup.appendChild(col1);
                            colgroup.appendChild(col2);
                            table.appendChild(colgroup);
                            div.appendChild(table);
                            Element tbody = DOM.createTBody();
                            tbody.addClassName("category_set");
                            createHeader(tbody, transaction);
                            createFirstTR(tbody);
                            table.appendChild(tbody);
                            TransactionItem[] items = transaction.getTransactionItems();
                            for (TransactionItem item : items) {
                                Element tr = DOM.createTR();
                                Element td = DOM.createTD();
                                Element link = DOM.createAnchor();
                                td.setInnerHTML(item.getAccountName().concat("<small>").concat("(").concat(item.getAccountCode()).concat(")").concat("</small>"));
                                //td.appendChild(link);
                                DOM.sinkEvents(link.cast(), Event.ONCLICK);
                                DOM.setEventListener(link.cast(), event -> {

                                });
                                tr.appendChild(td);

                                if (isDepartmentRelationEnabled &&  departmentLookUp != null && departmentLookUp.getSelectedItem() == null) {
                                    Element td0 = DOM.createTD();
                                    td0.setInnerHTML(item.getDepartment() != null ? item.getDepartment() : "");
                                    tr.appendChild(td0);
                                }

                                Element td1 = DOM.createTD();
                                td1.setInnerHTML(getValueAsString(item.getDebit()));
                                td1.addClassName(RIGHT_ALIGN_CELL);
                                tr.appendChild(td1);
                                Element td2 = DOM.createTD();
                                td2.setInnerHTML(getValueAsString(item.getCredit()));
                                td2.addClassName(RIGHT_ALIGN_CELL);
                                tr.appendChild(td2);
                                tr.setClassName("set_unit_row");
                                tbody.appendChild(tr);
                            }
                            createTotalTR(tbody, transaction);
                            createLastTR(tbody);
                            journalReportSection.appendChild(div);
                        }
                    }
                }
            });
        };
    }

    private void createFirstTR(Element element) {
        Element tr = DOM.createTR();
        Element td = DOM.createTD();
        td.setInnerHTML(wfmStrings.account());
        tr.appendChild(td);
        if (isDepartmentRelationEnabled && departmentLookUp != null && departmentLookUp.getSelectedItem() == null) {
            Element td0 = DOM.createTD();
            td0.setInnerHTML(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
            tr.appendChild(td0);
        }

        Element td1 = DOM.createTD();
        td1.setInnerHTML(wfmStrings.debit());
        td1.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(td1);
        Element td2 = DOM.createTD();
        td2.setInnerHTML(wfmStrings.credit());
        td2.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(td2);
        tr.setClassName("thead");
        element.appendChild(tr);
    }

    private void createTotalTR(Element element, Transaction transaction) {
        Element tr = DOM.createTR();
        tr.addClassName("total_row double");
        tr.setId("journalReport_total");
        Element td = DOM.createTD();
        td.setInnerHTML(wfmStrings.total());
        td.setId("total");
        tr.appendChild(td);
        if (isDepartmentRelationEnabled && departmentLookUp != null && departmentLookUp.getSelectedItem() == null) {
            Element td0 = DOM.createTD();
            tr.appendChild(td0);
        }
        Element td1 = DOM.createTD();
        td1.setInnerHTML(getValueAsString(transaction.getTotalDebit()));
        td1.addClassName(RIGHT_ALIGN_CELL);
        td1.setId("debit_value");
        tr.appendChild(td1);
        Element td2 = DOM.createTD();
        td2.setInnerHTML(getValueAsString(transaction.getTotalCredit()));
        td2.addClassName(RIGHT_ALIGN_CELL);
        td2.setId("credit_value");
        tr.appendChild(td2);
        element.appendChild(tr);
    }

    private void createLastTR(Element element) {
        Element tr = DOM.createTR();
        tr.addClassName("total_row-double-margin");
        tr.getStyle().setHeight(5, Style.Unit.PX);
        Element td = DOM.createTD();
        td.setAttribute("colspan", isDepartmentRelationEnabled && departmentLookUp != null && departmentLookUp.getSelectedItem() == null ? "4" : "3");
        tr.appendChild(td);
        element.appendChild(tr);
    }

    private void createHeader(Element element, Transaction transaction) {
        Element tr = DOM.createTR();
        Element td = DOM.createTD();
        td.setAttribute("colspan", isDepartmentRelationEnabled && departmentLookUp != null && departmentLookUp.getSelectedItem() == null ? "3" : "2");
        element.appendChild(tr);
        tr.appendChild(td);
        String idData = accountingMessages.wfmJournalIDdata(" " + transaction.getJournalId());
        String formatedDate = transaction.getPostedDate() != null ? DateUtils.format(transaction.getPostedDate()) : "";
        String postedBy = accountingMessages.postedBy(Constants.MANUAL_TRANSACTION.equals(transaction.getTransactionType()) ? "Manual Journal: " : "",
                !Utils.isNullOrEmpty(transaction.getPostedBy()) ? transaction.getPostedBy() : "System", formatedDate);
        String journalDate = DateUtils.format(transaction.getJournalDate());

        Element journalLink;
        if (transaction.getTransactionLink() != null && !"".equals(transaction.getTransactionLink())) {
            journalLink = DOM.createAnchor();
            DOM.sinkEvents(journalLink.cast(), Event.ONCLICK);
            EventListener journalEventListener = event -> {
                if (transaction.isBlank()) {
                    Window.open(transaction.getTransactionLink(), "_blank", "");
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged(transaction.getTransactionLink(), transaction.getNumber());
                }
            };
            DOM.setEventListener(journalLink.cast(), journalEventListener);
        } else {
            journalLink = DOM.createSpan();
        }
        journalLink.setInnerHTML(idData + " ");
        journalLink.setTitle(idData);

        Element journalSpan = DOM.createSpan();
        journalSpan.setInnerHTML((transaction.getReversedJournalId() != null ? accountingStrings.reversed() : "") + " "
                + (INVOICEPAYMENT_TRANSACTION.equals(transaction.getTransactionType()) ? transaction.getJournalName().replaceAll("\\bnull\\b", accountingStrings.vatRefund()) : transaction.getJournalName()) + " "
                + "<small>" + postedBy + "</small>" + " " + (transaction.getReversedJournalId() != null ? accountingMessages.reversalOf("" + transaction.getReversedJournalId()) : ""));

        td.addClassName("text-left");
        if (Constants.MANUAL_TRANSACTION.equals(transaction.getTransactionType()) && transaction.getReversedJournalId() != null) {
            Element span = DOM.createSpan();
            span.setInnerHTML(idData.concat(" "));
            span.setTitle(idData);
            td.appendChild(span);
        } else {
            td.appendChild(journalLink);
        }
        td.appendChild(journalSpan);
        Element th2 = DOM.createTD();
        tr.appendChild(th2);
        tr.setClassName("pre_thead");
        th2.addClassName("text-right");
        th2.setInnerHTML(journalDate);
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
    }

    private SelectItem[] getOrderByItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(1, accountingStrings.journalID());
        items[1] = new SelectItem(2, accountingStrings.journalDate());
        return items;
    }

    private String getValueAsString(BigDecimal value) {
        if (value == null) {
            return "";
        }
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private GBoxItem exportSection() {
        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink();
        showLink.addStyleName("btn btn--white btn--icon");

        Icon ieIcon = new Icon();
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

        mdp.add(NewJournalReport.this::getPortraitLink);
        mdp.add(NewJournalReport.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();

        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(ch -> {
            String order = JOURNAL_ID;
            if (orderByValues.getSelectedItem().getId() == 2) {
                order = JOURNAL_DATE;
            }
            String URL = (CommandConstants.COMMON_URL + "/journalReportExcelHandler");
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setPropertyCode("journalReport");
            filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
            if (multiJournal) {
                filter.setObjectId(objectId);
            } else {
                filter.setJournalID(transactionJournalLookUp.getSelectedItemID());
            }
            filter.setJournalID(transactionJournalLookUp.getSelectedItemID());
            filter.setSortField(order);
            filter.setStart(start);
            filter.setLimit(limit);
            if (isDepartmentRelationEnabled && departmentLookUp != null) {
                filter.setDepartmentId(departmentLookUp.getSelectedItemID());
            }
            filter.setAccountID(accountLookUp.getSelectedItemID());
            HashMap<String, String> parametrs = filter.getRequestParams();
            Utils.sendPDFOrExcelRequest(topPanel, URL, parametrs, "_blank");
        });
        showMenuContainer.add(exportExl);

        MaterialLink exportCsv = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon csvIcon = new Icon();
        csvIcon.setClass("ficon--file-csv");
        exportCsv.add(csvIcon);
        exportCsv.setText(wfmStrings.csv());
        exportCsv.addClickHandler(ch -> {
            String order = JOURNAL_ID;
            if (orderByValues.getSelectedItem().getId() == 2) {
                order = JOURNAL_DATE;
            }
            String URL = (CommandConstants.COMMON_URL + "/journalReportCSVHandler");
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setPropertyCode("journalReport");
            filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
            if (multiJournal) {
                filter.setObjectId(objectId);
            } else {
                filter.setJournalID(transactionJournalLookUp.getSelectedItemID());
            }
            filter.setSortField(order);
            filter.setStart(start);
            filter.setLimit(limit);
            if (isDepartmentRelationEnabled && departmentLookUp != null) {
                filter.setDepartmentId(departmentLookUp.getSelectedItemID());
            }
            filter.setAccountID(accountLookUp.getSelectedItemID());
            HashMap<String, String> parametrs = filter.getRequestParams();
            Utils.sendPDFOrExcelRequest(topPanel, URL, parametrs, "_blank");
        });
        showMenuContainer.add(exportCsv);
        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, div);
        exportItem.addStyleName("export-item");
        return exportItem;
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
        String order = JOURNAL_ID;
        if (orderByValues.getSelectedItem().getId() == 2) {
            order = JOURNAL_DATE;
        }
        String URL = (CommandConstants.PDF_URL + "/journalReportPDFHandler");
        final ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLandscape(landscape);
        filter.setPropertyCode("journalReport");
        filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
        filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
        if (multiJournal) {
            filter.setObjectId(objectId);
        } else {
            filter.setJournalID(transactionJournalLookUp.getSelectedItemID());
        }
        filter.setSortField(order);
        filter.setStart(start);
        filter.setLimit(limit);
        if (isDepartmentRelationEnabled && departmentLookUp != null) {
            filter.setDepartmentId(departmentLookUp.getSelectedItemID());
        }
        filter.setAccountID(accountLookUp.getSelectedItemID());
        HashMap<String, String> parametrs = filter.getRequestParams();
        Utils.sendPDFOrExcelRequest(topPanel, URL, parametrs, "_blank");
    }


    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.setWidth(400);
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        if (isDepartmentRelationEnabled) {
            departmentLookUp = new DepartmentLookUp();
            departmentLookUp.setWidth("100%");
            contentPanel.add(new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentLookUp));
        }

        contentPanel.add(new FormGroup(accountingStrings.journalID(), transactionJournalLookUp));

        accountLookUp = new AccountsLookUp();
        accountLookUp.setWidth("100%");
        contentPanel.add(new FormGroup(wfmStrings.account(), accountLookUp));

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
        resetButton.addClickHandler(clickEvent -> {
            if (departmentLookUp != null) {
                departmentLookUp.clear();
            }
            transactionJournalLookUp.clear();
            accountLookUp.clear();
        });

        WfmButton2 applyFilterButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS);
        applyFilterButton.addClickHandler(clickEvent -> {
            filterDialog.close();
            pagingWidget.resetAndReload();
        });

        filterDialog.add(contentPanel);
        filterDialog.addButton(resetButton);
        filterDialog.addButton(applyFilterButton);
    }
}
