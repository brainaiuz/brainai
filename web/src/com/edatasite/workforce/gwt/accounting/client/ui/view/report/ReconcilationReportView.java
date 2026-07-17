package com.edatasite.workforce.gwt.accounting.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankReconcilationReportData;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankReconcilationReportItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal.JournalTable;
import com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal.JournalTableColumn;
import com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal.JournalTableItem;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.07.2010
 * Time: 15:07:07
 * To change this template use File | Settings | File Templates.
 */

public class ReconcilationReportView extends View implements AccountingConstants, Colapse {

    private DatePicker toDate;
    private DatePicker fromDate;
    private WfmButton2 update;
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final DateTimeFormat givenDateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
    private final Integer bankAccountID;
    private MaterialPanel tableWrapper;
    private String bankName = "";
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private GBoxRow groupBoxRow;

    public ReconcilationReportView(Integer bankAccountID, String[] params) {
        super("report", accountingStrings.reconcilationReport());
        if ((params.length >= 2 && params[1] != null && !"".equals(params[1]))) {
            bankName = params[1];
        }
        this.bankAccountID = bankAccountID;
    }

    @Override
    protected Widget onInitialize() {

        MaterialPanel reportBody = new MaterialPanel("inputs_sm reconcilation_report");
        Div tabContent = new Div("tab-content workarea_head_tabs_content textPanel");
        tableWrapper = new MaterialPanel("table-wrapper table-wrapper--has-affix");
        tabContent.add(tableWrapper);
        reportBody.add(tabContent);

        MaterialPanel panel = new MaterialPanel("pageContent_reporting");
        panel.getElement().setAttribute("style", "background: transparent;");

        HorizontalPanel header = new HorizontalPanel();
        header.setWidth("100%");
        MaterialPanel fixedContent = new MaterialPanel("fixed-content reportFiltersPanel");
        MaterialPanel filterPanel = new MaterialPanel("section-box reportFiltersPanel__content");

        GBox headerBoxPanel = new GBox();
        headerBoxPanel.addStyleName("group-box--width-free");
        groupBoxRow = new GBoxRow();

        toDate = new DatePicker();
        toDate.setDate(new Date());

        fromDate = new DatePicker();
        fromDate.setDate(new Date());

        update = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        update.addClickHandler(event -> getAndSetReconcilationReport());

        GBoxItem bank = new GBoxItem(wfmStrings.bankName(), new HTML(bankName));
        bank.setStyleNoBorder(true);
        groupBoxRow.add(bank);

        GBoxItem dataItemFrom = new GBoxItem(wfmStrings.from(), fromDate);
        groupBoxRow.add(dataItemFrom);

        GBoxItem dateItemTo = new GBoxItem(wfmStrings.to(), toDate);
        groupBoxRow.add(dateItemTo);

        GBoxItem updateButtonItem = new GBoxItem(update);
        groupBoxRow.add(updateButtonItem);

        MaterialLink ieLink = new MaterialLink();//import/export button link for listing top panel
        new MaterialTooltip(ieLink, wfmStrings.importExport()).setPosition(Position.TOP);
        ieLink.setHref("#");
        ieLink.setClass("btn btn--icon");

        ieLink.add(new SvgIcon(SvgEnum.downloadCloud));
        ieLink.ensureDebugId("import_export_button_id");

        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        menuContainer.setClass("dropdown-content--2");
        menuContainer.setBelowOrigin(true);

        MaterialLink pdfVersion = new MaterialLink();
        MaterialIcon pdfIcon = new MaterialIcon();
        pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
        pdfVersion.add(pdfIcon);
        pdfVersion.setText(wfmStrings.pdf());
        pdfVersion.ensureDebugId("pdf_button");

        Div wrapper = new Div("java-wrap");
        wrapper.add(pdfVersion);


        MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
        mdp.setHover(true);
        mdp.setHoverable(true);
        mdp.add(ReconcilationReportView.this::getPortraitLink);
        mdp.add(ReconcilationReportView.this::getLandscapeLink);
        wrapper.add(mdp);

        setPDFListener();

        menuContainer.add(wrapper);

        MaterialLink xlsVersion = new MaterialLink();
        xlsVersion = new MaterialLink();
        MaterialIcon xlsIcon = new MaterialIcon();
        xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
        xlsVersion.add(xlsIcon);
        xlsVersion.setText(wfmStrings.excel());
        xlsVersion.ensureDebugId("excel_button");
        xlsVersion.addClickHandler(event -> {
            String URL = (CommandConstants.COMMON_URL + "/reconcilationReportExcelHandler");
            final ListingFilterParameter filter = new ListingFilterParameter();
            filter.setStartDate(toDate.getDate());
            filter.setEndDate(fromDate.getDate());
            filter.setObjectId(bankAccountID);
            Utils.sendPDFOrExcelRequest(groupBoxRow, URL, filter.getRequestParams(), "_blank");

        });
        menuContainer.add(xlsVersion);


        ieLink.add(menuContainer);
//        GBoxItem exportimport = new GBoxItem(ieLink);
        MaterialMenuBar importExportMenu = new MaterialMenuBar();
        importExportMenu.setClass("btn-group dropdown-kit--arrow--below");
        importExportMenu.add(ieLink);
        importExportMenu.setMarginTop(20);
        importExportMenu.addStyleName("file--ReconcilationReportVIew");
        groupBoxRow.add(importExportMenu);

        headerBoxPanel.add(groupBoxRow);
        filterPanel.add(headerBoxPanel);

        fixedContent.add(filterPanel);
        header.add(fixedContent);
        panel.add(header);
        panel.add(reportBody);
        add(panel);

        getAndSetReconcilationReport();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, ReconcilationReportView.this, (sender, args) -> getAndSetReconcilationReport());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, ReconcilationReportView.this, (sender, args) -> getAndSetReconcilationReport());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, ReconcilationReportView.this, (sender, args) -> getAndSetReconcilationReport());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, ReconcilationReportView.this, (sender, args) -> getAndSetReconcilationReport());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RECONCILESTATUS_CHANGED, ReconcilationReportView.this, (sender, args) -> getAndSetReconcilationReport());

        RootPanel.get().addStyleName("fitted-content");
        RootPanel.get().addStyleName("remove-has-tabs-className");
        return null;
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
        String URL = (CommandConstants.PDF_URL + "/reconcilationReportPdfHandler");
        final ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLandscape(landscape);
        filter.setStartDateNC(givenDateFormat.format(toDate.getDate()));
        filter.setEndDateNC(givenDateFormat.format(fromDate.getDate()));
        filter.setObjectId(bankAccountID);
        Utils.sendPDFOrExcelRequest(groupBoxRow, URL, filter.getRequestParams(), "_blank");

    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private void getAndSetReconcilationReport() {
        LoadingPanel.loading(true);
        tableWrapper.clear();
        AccountingService.App.get().getBankAccountReconcilationReport(givenDateFormat.format(toDate.getDate()), givenDateFormat.format(fromDate.getDate()), bankAccountID, new AbstractAsyncCallback<BankReconcilationReportData>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(BankReconcilationReportData result) {
                LoadingPanel.loading(false);
                //VAT Return Details table
                JournalTableColumn[] detailColumns = new JournalTableColumn[5];

                detailColumns[0] = new JournalTableColumn("date", wfmStrings.date());
                detailColumns[0].setWidthPercentage(18);

                detailColumns[1] = new JournalTableColumn("empty", "");
                detailColumns[1].setWidthPercentage(18);

                detailColumns[2] = new JournalTableColumn("description", wfmStrings.description());
                detailColumns[2].setWidthPercentage(39);

                detailColumns[3] = new JournalTableColumn("reference", wfmStrings.reference());
                detailColumns[3].setWidthPercentage(26);

                detailColumns[4] = new JournalTableColumn("amount", wfmStrings.amount());
                detailColumns[4].setWidthPercentage(17);
                detailColumns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

                JournalTable reconcilationReport = new JournalTable(detailColumns);
                reconcilationReport.setWidth("100%");

                //Bank Account Balance In WorkforceTrack
                reconcilationReport.add(getBoldTableItem(fromDate.getDate(), Property.get(Constants.BANKACCOUNT, accountingStrings.bankAccountBalanceIn(), wfmStrings.bankAccount()) + " " + Utils.getProductName(), result.getBankAccountBalanceInWFT()));
                //Reconcilation Balance
                reconcilationReport.add(getTotalTableItem(accountingStrings.reconciledBalance(), result.getReconcileBalance()));
                //Outstanding Payments
                reconcilationReport.add(getHeaderTableItem(accountingStrings.plusOutstandingPayments()));
                reconcilationReport.addItems(getJournalTableItems(result.getOutstandingPayments()));
                reconcilationReport.add(getTotalTableItem(accountingStrings.totalOutstandingPayments(), result.getTotalOutstandingPayments()));
                //Outstanding Receipts
                reconcilationReport.add(getHeaderTableItem(accountingStrings.lessOutstandingReceipts()));
                reconcilationReport.addItems(getJournalTableItems(result.getOutstandingReceipts()));
                reconcilationReport.add(getTotalTableItem(accountingStrings.totalOutstandingReceipts(), result.getTotalOutstandingReceipts()));
                //UnReconciled Bank Statement Lines
                reconcilationReport.add(getHeaderTableItem(accountingStrings.plusUnRecBankStatLines()));
                reconcilationReport.addItems(getJournalTableItems(result.getUnReconciledBankStatementLines()));
                reconcilationReport.add(getTotalTableItem(accountingStrings.totalUnRecStatLines(), result.getTotalUnReconciledBankStatementLines()));
                //Balance At Bank
                reconcilationReport.add(getBoldTableItem(toDate.getDate(), accountingStrings.balanceAtBank(), result.getBalanceAtBank()));
                reconcilationReport.getTable().setWidth("100%");
                tableWrapper.add(reconcilationReport);
            }
        });
    }

    private JournalTableItem[] getJournalTableItems(BankReconcilationReportItem[] items) {
        JournalTableItem[] tableItems = new JournalTableItem[items.length];
        for (int i = 0; i < items.length; i++) {
            Object[] values = new Object[5];
            values[0] = new HTML("<span style='display:block;margin-left:10px'>" + DateUtils.format(items[i].getDate()) + "</span>");
            values[1] = new Label(" ");
            Label label = new Label(items[i].getDescription() != null ? items[i].getDescription() : " ");
            label.addStyleName("center");
            values[2] = label;
            values[3] = new Label(items[i].getReference() != null ? items[i].getReference() : " ");
            if (items[i].getAmount() != null) {
                if (items[i].getAmount().compareTo(ZERO) < 0) {
                    values[4] = new Label("(" + AccountingUtils.get().formatPrice(items[i].getAmount().abs()) + ")");
                } else {
                    values[4] = new Label(AccountingUtils.get().formatPrice(items[i].getAmount()));
                }
            } else {
                values[4] = new Label(AccountingUtils.getZero());
            }
            tableItems[i] = new JournalTableItem(values);
        }
        return tableItems;
    }

    private JournalTableItem getHeaderTableItem(String description) {
        Object[] values = new Object[1];
        values[0] = new HTML("<b>" + description + "</b>");

        Integer[] colspans = new Integer[1];
        colspans[0] = 5;
        JournalTableItem tabItem = new JournalTableItem(values);
        tabItem.setColspans(colspans);
        return tabItem;
    }

    private JournalTableItem getTotalTableItem(String description, BigDecimal amount) {
        Object[] values = new Object[4];
        values[0] = new HTML("<b style='e\text-alig:center;display:block;margin-left:10px'>" + description + "</b>");
        values[1] = new HTML("");
        if (amount != null) {
            if (amount.compareTo(ZERO) < 0) {
                values[3] = new HTML("<b>(" + AccountingUtils.get().formatPrice(amount.abs()) + ")</b>");
            } else {
                values[3] = new HTML("<b>" + AccountingUtils.get().formatPrice(amount) + "</b>");
            }
        } else {
            values[3] = new HTML("<b>" + AccountingUtils.getZero() + "</b>");
        }

        Integer[] colspans = new Integer[4];
        colspans[0] = 3;
        JournalTableItem tabItem = new JournalTableItem(values);
        tabItem.setColspans(colspans);
        return tabItem;
    }

    private JournalTableItem getBoldTableItem(Date toDates, String description, BigDecimal amount) {
        Object[] values = new Object[5];
        values[0] = toDate != null ? new HTML("<b>" + DateUtils.format(toDates) + "</b>") : " ";
        values[1] = new HTML("");
        HTML descr = new HTML("<b>" + description + "</b>");
        descr.addStyleName("center");
        values[2] = descr;
        values[3] = " ";
        if (amount != null) {
            if (amount.compareTo(ZERO) < 0) {
                values[4] = new HTML("<b>(" + AccountingUtils.get().formatPrice(amount.abs()) + ")</b>");
            } else {
                values[4] = new HTML("<b>" + AccountingUtils.get().formatPrice(amount) + "</b>");
            }
        } else {
            values[4] = new HTML("<b>" + AccountingUtils.getZero() + "</b>");
        }
        return new JournalTableItem(values);
    }

    @Override
    public String getIconStyle() {
        return "accountMark manual-journals";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        LoadingPanel.loading(true);
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
                LoadingPanel.loading(false);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
                LoadingPanel.loading(false);
            }
        });
    }

}

