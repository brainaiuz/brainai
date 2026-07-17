package com.edatasite.workforce.gwt.accounting.client.factory;

import com.edatasite.workforce.gwt.accounting.client.Accounting;
import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.container.WarehouseSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountingDashboardSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountingSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.ExchangeRateSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.ProductionSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.TransactionSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.guide.GuideSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.report.ReportSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.history.ClientImportHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.SupplierImportHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AccountTransactionsHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AccountingHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AddAccountHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AddProductHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AddProductRentalHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AddRentalOrderHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.BankAccountHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.BankStatementHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.BankStatementItemHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.BankStatementItemListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.BuildAssemblyItemHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ChartOfAccountsSummaryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.CheckHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ConsignmentHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.DiscountHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.FixedAssetHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportBankTransferHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportChartOfAccountsHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportManualTransactionHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportProductsHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportStatementsHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportTallyManualTransactionHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ImportTransactionsHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.PaymentMethodListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.PriceLevelHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ProductionHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.ReservationHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.SpendReceiveMoneyHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockAdjustmentHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockOutHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockTransferHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockValuationHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.TaxHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.TransactionItemViewHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.TransferMoneyHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.guide.GuideHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.AddBrandHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.AddProductCategoryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.AddWarehouseLocationListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.BrandsListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.ProductCategoriesListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.ProductCategorySummaryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.UnitMeasurementsListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.WarehouseHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.WarehouseProductListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.ClickedReportHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.CrmAccountBalanceHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.ReconcilationReportHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.ReportHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.TrialBalanceDetailedHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.VATReturnHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.VATReturnTransactionHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.view.SupplierListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.TrashBinListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankAccountingListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CheckListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.FixedAssetRegisterListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.BankPaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.BankReceiptListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.CashPaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.CashReceiptsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.CustomerPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.SupplierPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.consignment.ConsignmentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AssemblyItemListview;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.BuildAssemblyItemListview;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.InventoryItemsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.RentalProductListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.StockTransferListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.WarehousesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.ImportExport.ImportBudgetManagerHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewAccountTransactionView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewAgingSummaryView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewBalanceSheetView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewBudgetSheetView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewCashFlowView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewJournalReportView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewProfitAndLossView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewStockValuationView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewTrialBalanceView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewVatReturnReportView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.OldGccVatReturnReportView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.ManualEntryListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.StockAdjustmentsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.StockOutListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.VatReturnsListView;
import com.edatasite.workforce.gwt.client.client.history.ClientDynamicHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.ClientEditHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.ClientHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.SupplierHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.SupplierSummaryHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.ui.view.NewClientListView;
import com.edatasite.workforce.gwt.core.client.DynamicSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.history.CustomFormItemHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.SearchHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WebhookListHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookEditHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.history.CaseHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactEditHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactImportHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.OpportunityHistoryProcessor;
import com.edatasite.workforce.gwt.expenses.client.history.ExpenseEmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.expenses.client.history.ExpensePaymentViewHistoryProcessor;
import com.edatasite.workforce.gwt.expenses.client.history.ExpenseReportHistoryProcessor;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpenseListView;
import com.edatasite.workforce.gwt.googlecalendar.client.history.EventHistoryProcessor;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.emailcompose.AccountingEmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.expense.ImportCustomExpenseHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.expense.ProjectBaseExpenseHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.manual.HomeCurrencyAdjustmentHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.manual.ManualHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.BatchPaymentHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.CrmAccountProjectBalanceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.InvoicePaymentHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.PaymentRefundHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.PrePaymentHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.PrepaymentListHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.projectbaseinvoice.ProjectBaseInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice.PayableCreditNoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice.PurchaseInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice.RecurringBillsHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseorder.ImportPurchaseOrderHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseorder.PurchaseOrderHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.rfp.RequestForPurchaseHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.rfq.RequestForQuoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.BillableExpenseHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.ImportCustomInvoicesHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.ImportNimbleCommerceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.ReceivableCreditNoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.RecurringInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.SaleInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleorderbaseinvoice.SaleOrderBaseInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.PickListHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.ProgressInvoicingHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.SaleOrderHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.SaleQuoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.shippindData.GoodsDeliveredNotesHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.shippindData.GoodsReceivedNotesHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.ui.view.RecurringBillsListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.RecurringInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.PayInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.ReceivePaymentListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder.RentalOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfp.RequestForPurchaseListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsDeliveredNotesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsReceivedNotesListView;
import com.edatasite.workforce.gwt.issue.client.history.IssueHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.InvoiceSettingsSinksContainer;
import com.edatasite.workforce.gwt.profile.client.history.CustomFieldManagementHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CustomizationSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.TaskHistoryProcessor;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 20.02.2009
 * Time: 18:09:53
 * To change this template use File | Settings | File Templates.
 */
public class AccountingSinksContainerFactory extends SinksContainerFactory implements Constants, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private SinksContainer guide;
    private SinksContainer accounting;
    private SinksContainer selectedContener;
    private boolean isFirstContener = true;

    public AccountingSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("accounting");
    }

    public void initDefaultContainers() {

        if (Utils.hasPermission(ACCOUNTING_GETTING_STARTED_MENU) && !Utils.isAccountingSetup()) {
            guide = new GuideSinksContainer("guide", wfmStrings.gettingStarted());
            guide.setPreparedView("guide");
            setSinksContainer(guide);
        }
        if (Utils.isAccountingSetup()) {
            if (Accounting.dashboards.size() > 0) {
                createDashboardContainer();
            }
            if (Utils.getPropertyListingMap() != null && Utils.getPropertyListingMap().size() > 0) {
                setAccountingPropertyListingsMap(Utils.getPropertyListingMap());
            }

            if (Utils.isAccountingSettingsTabEnabled()) {
                createSettingsSinksContainer();
            }
            if (Utils.hasPermission(ACCOUNTING_SETTINGSE_EXCHANGE_RATE)) {
                createExchangeRateSinksContainer();
            }
            if (Utils.isProductionEnabled()) {
                createProductionContainer();
            }
//            As Munir asked we need to open second container if dashboard has only one view
            if (Accounting.dashboards.size() == 1) {
                openSecondContainer();
            }
        } else if (Utils.hasPermission(ACCOUNTING_GETTING_STARTED_MENU)) {
            setSelection(guide);
        }


//        ((WorkforceEntryPoint) entryPoint).showHelpTools("customisation/" + Utils.getProductName().toLowerCase() + "/" + accountingStrings.docs_accountingQuick_PDF());

        AccountingUtils.instance = AccountingUtils.get();
    }

    //DASHBOARD
    private void createDashboardContainer() {
        AccountingDashboardSinksContainer dashboardContainer = new AccountingDashboardSinksContainer();
        dashboardContainer.setPreparedView("dashboard_" + dashboardContainer.normalizeName(Accounting.defaultDashboardName));
        putContainer(dashboardContainer);
        setDefaultContainer(dashboardContainer.getName());
        setDashboardContainer(dashboardContainer);
    }

    //ACCOUNTING
    private void createAccountingContainer() {
        accounting = new AccountingSinksContainer("accounting", wfmStrings.accounting());
//        accounting.setPreparedView(SALE_INVOICE);
        accounting.setPreparedView("accountingHomepage");
        setSinksContainer(accounting);
    }

    //TRANSACTIONS
    private void createTransactionContainer() {
        TransactionSinksContainer transaction = new TransactionSinksContainer("transaction", accountingStrings.transactions());
        transaction.setPreparedView("cashreceipt");
        setSinksContainer(transaction);
        if (selectedContener == null) {
            selectedContener = transaction;
            setSelection(selectedContener);
        }
    }

    //PRODUCTION
    private void createProductionContainer() {
        ProductionSinksContainer production = new ProductionSinksContainer("production", accountingStrings.production());
        setSinksContainer(production);
        if (selectedContener == null) {
            selectedContener = production;
            setSelection(selectedContener);
        }
    }

    //STATEMENTS
    private void createReportSinksContainer() {
        SinksContainer reports = new ReportSinksContainer("report", accountingStrings.statements());
        reports.setPreparedView("newprofitLoss");
        setSinksContainer(reports);
        if (selectedContener == null) {
            selectedContener = reports;
            setSelection(selectedContener);
        }
    }

    //WAREHOUSE
    private void createWarehousesContainer() {
        WarehouseSinksContainer warehouses = new WarehouseSinksContainer("warehouses", accountingStrings.warehouses());
        warehouses.setPreparedView("warehouseList");
        setSinksContainer(warehouses);
    }

    //SETTINGS
    private void createSettingsSinksContainer() {
        SinksContainer settings = new InvoiceSettingsSinksContainer("settings", wfmStrings.settings());
        settings.setPreparedView("invoiceSettings");
        setSinksContainer(settings);
        if (selectedContener == null) {
            selectedContener = settings;
            setSelection(selectedContener);
        }
    }

    //EXCHANGE RATE
    private void createExchangeRateSinksContainer() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP)) {
            SinksContainer exchangeRate = new ExchangeRateSinksContainer("exchangeRate", wfmStrings.exchangeRate());
            exchangeRate.setPreparedView("exchangeRateView");
            setSinksContainer(exchangeRate);
            if (selectedContener == null) {
                selectedContener = exchangeRate;
                setSelection(selectedContener);
            }
        }
    }


    public void finishGettingStarted() {
        MainLayout.get().clearAllContainers();

        addMenuItems();
        Utils.userSettings.put(ACCOUNTING_IS_SETUP, "true");

        if (Accounting.dashboards.size() > 0) {
            createDashboardContainer();
        }
        createAccountingContainer();
        createTransactionContainer();

        if (Utils.isMultiWarehouseEnabled()) {
            createWarehousesContainer();
        }
        createReportSinksContainer();
        if (Utils.isAccountingSettingsTabEnabled()) {
            createSettingsSinksContainer();
        }
        if (Utils.isProductionEnabled()) {
            createProductionContainer();
        }
        setSelection(accounting);
    }

    public void registerProcessors() {
        registerHistoryProcessor("contact", new ContactHistoryProcessor());
        registerHistoryProcessor("contactedit", new ContactEditHistoryProcessor());
        registerHistoryProcessor(SEARCH, new SearchHistoryProcessor());// History processor for search tab
        registerHistoryProcessor("client", new ClientHistoryProcessor(Constants.ACCOUNTING_SECTION));
        registerHistoryProcessor("clientdynamic", new ClientDynamicHistoryProcessor(Constants.ACCOUNTING_SECTION));
        registerHistoryProcessor("supplier", new SupplierHistoryProcessor());
        registerHistoryProcessor("issue", new IssueHistoryProcessor());
        registerHistoryProcessor("accounting", new AccountingHistoryProcessor());
        registerHistoryProcessor("importclient", new ClientImportHistoryProcessor());
        registerHistoryProcessor("production", new ProductionHistoryProcessor());
        registerHistoryProcessor("importsupplier", new SupplierImportHistoryProcessor());
        registerHistoryProcessor("saleinvoice", new SaleInvoiceHistoryProcessor());
        registerHistoryProcessor("recurringinvoice", new RecurringInvoiceHistoryProcessor());
        registerHistoryProcessor(TASK, new TaskHistoryProcessor());
        registerHistoryProcessor(Constants.PURCHASE_INVOICE, new PurchaseInvoiceHistoryProcessor());
        registerHistoryProcessor("grn", new GoodsReceivedNotesHistoryProcessor());
        registerHistoryProcessor("gdn", new GoodsDeliveredNotesHistoryProcessor());
        registerHistoryProcessor("recurringbill", new RecurringBillsHistoryProcessor());
        registerHistoryProcessor("salequote", new SaleQuoteHistoryProcessor());
        registerHistoryProcessor("requestforquote", new RequestForQuoteHistoryProcessor());
        registerHistoryProcessor("requestforpurchase", new RequestForPurchaseHistoryProcessor());
        registerHistoryProcessor("purchaseorder", new PurchaseOrderHistoryProcessor());
        registerHistoryProcessor("receivablecreditnote", new ReceivableCreditNoteHistoryProcessor());
        registerHistoryProcessor("payablecreditnote", new PayableCreditNoteHistoryProcessor()); // debit note
        registerHistoryProcessor("expenseReports", new ExpenseReportHistoryProcessor());
        registerHistoryProcessor("expenseemailcompose", new ExpenseEmailComposeHistoryProcessor());
        registerHistoryProcessor("account", new AddAccountHistoryProcessor());
        registerHistoryProcessor("tax", new TaxHistoryProcessor());
        registerHistoryProcessor("bank", new BankAccountHistoryProcessor());
        registerHistoryProcessor("invoicepayment", new InvoicePaymentHistoryProcessor());
        registerHistoryProcessor("expensepayment", new ExpensePaymentViewHistoryProcessor());
        registerHistoryProcessor("report", new ReportHistoryProcessor());
        registerHistoryProcessor("clickedreport", new ClickedReportHistoryProcessor());
        registerHistoryProcessor("stockadjustment", new StockAdjustmentHistoryProcessor());
        registerHistoryProcessor("stockout", new StockOutHistoryProcessor());
        registerHistoryProcessor("stockvaluation", new StockValuationHistoryProcessor());
        registerHistoryProcessor("stocktransfer", new StockTransferHistoryProcessor());
        registerHistoryProcessor("warehouse", new WarehouseHistoryProcessor());
        registerHistoryProcessor("warehousesummary", new WarehouseHistoryProcessor());
        registerHistoryProcessor("warehouselocationlist", new AddWarehouseLocationListHistoryProcessor());
        registerHistoryProcessor("warehouseproductlist", new WarehouseProductListHistoryProcessor());
        registerHistoryProcessor("unitmeasurementlist", new UnitMeasurementsListHistoryProcessor());
        registerHistoryProcessor("productcategorylist", new ProductCategoriesListHistoryProcessor());
        registerHistoryProcessor("product", new AddProductHistoryProcessor());
        registerHistoryProcessor("product-rental", new AddProductRentalHistoryProcessor());
        registerHistoryProcessor("rentalorder", new AddRentalOrderHistoryProcessor());
        registerHistoryProcessor("productcategory", new AddProductCategoryHistoryProcessor());
        registerHistoryProcessor("productcategoryview", new ProductCategorySummaryHistoryProcessor());
        registerHistoryProcessor("brandlist", new BrandsListHistoryProcessor());
        registerHistoryProcessor("brand", new AddBrandHistoryProcessor());
        registerHistoryProcessor("picklist", new PickListHistoryProcessor());
        registerHistoryProcessor("manual", new ManualHistoryProcessor());
        registerHistoryProcessor("importmanualtransaction", new ImportManualTransactionHistoryProcessor());
        registerHistoryProcessor("importtallymanualtransaction", new ImportTallyManualTransactionHistoryProcessor());
        registerHistoryProcessor("homecurrencyadjustment", new HomeCurrencyAdjustmentHistoryProcessor());
        registerHistoryProcessor("guide", new GuideHistoryProcessor());
        registerHistoryProcessor("projectBaseInvoice", new ProjectBaseInvoiceHistoryProcessor());
        registerHistoryProcessor("projectBaseExpense", new ProjectBaseExpenseHistoryProcessor());
        registerHistoryProcessor("saleorderBaseInvoice", new SaleOrderBaseInvoiceHistoryProcessor());
        registerHistoryProcessor("transfer", new TransferMoneyHistoryProcessor());
        registerHistoryProcessor("spendreceivemoney", new SpendReceiveMoneyHistoryProcessor());
        registerHistoryProcessor("importbanktransfer", new ImportBankTransferHistoryProcessor());
        registerHistoryProcessor("importbudgetmanager", new ImportBudgetManagerHistoryProcessor());
        registerHistoryProcessor("importbanktransactions", new ImportTransactionsHistoryProcessor());
        registerHistoryProcessor("importbankstatement", new ImportStatementsHistoryProcessor());
        registerHistoryProcessor("clientedit", new ClientEditHistoryProcessor());
        registerHistoryProcessor("suppliersummary", new SupplierSummaryHistoryProcessor());
        registerHistoryProcessor("accountTransactionsList", new AccountTransactionsHistoryProcessor());
        registerHistoryProcessor("saleorder", new SaleOrderHistoryProcessor());
        registerHistoryProcessor("bankStatementList", new BankStatementHistoryProcessor());
        registerHistoryProcessor("bankStatementItemList", new BankStatementItemListHistoryProcessor());
        registerHistoryProcessor("bankStatementItem", new BankStatementItemHistoryProcessor());
        registerHistoryProcessor("reconcilationReport", new ReconcilationReportHistoryProcessor());
        registerHistoryProcessor("importproducts", new ImportProductsHistoryProcessor());
        registerHistoryProcessor("importchartofaccounts", new ImportChartOfAccountsHistoryProcessor());
        registerHistoryProcessor("discount", new DiscountHistoryProcessor());
        registerHistoryProcessor("chartOfAccount", new ChartOfAccountsSummaryHistoryProcessor());
        registerHistoryProcessor("reservation", new ReservationHistoryProcessor());
        registerHistoryProcessor("paymentmethodlist", new PaymentMethodListHistoryProcessor());
        registerHistoryProcessor("priceLevel", new PriceLevelHistoryProcessor());
        registerHistoryProcessor("receivepayment", new BatchPaymentHistoryProcessor());
        registerHistoryProcessor("prepayment", new PrePaymentHistoryProcessor(true));
        registerHistoryProcessor("customerRefund", new PaymentRefundHistoryProcessor(true));
        registerHistoryProcessor("supplierRefund", new PaymentRefundHistoryProcessor(false));
        registerHistoryProcessor("prepaymentList", new PrepaymentListHistoryProcessor(true));
        registerHistoryProcessor("supplierCredit", new PrePaymentHistoryProcessor(false));
        registerHistoryProcessor("supplierCreditList", new PrepaymentListHistoryProcessor(false));
        registerHistoryProcessor("crmAccountProjectBalance", new CrmAccountProjectBalanceHistoryProcessor());
        registerHistoryProcessor("fixedasset", new FixedAssetHistoryProcessor());
        registerHistoryProcessor("customFieldManagement", new CustomFieldManagementHistoryProcessor());
        registerHistoryProcessor("customerBalance", new CrmAccountBalanceHistoryProcessor());
        registerHistoryProcessor("supplierBalance", new CrmAccountBalanceHistoryProcessor());
        registerHistoryProcessor("buildAssembly", new BuildAssemblyItemHistoryProcessor());
        registerHistoryProcessor("check", new CheckHistoryProcessor());
        registerHistoryProcessor("importnimblecommerce", new ImportNimbleCommerceHistoryProcessor());
        registerHistoryProcessor("importcustominvoice", new ImportCustomInvoicesHistoryProcessor());
        registerHistoryProcessor("importcustomexpense", new ImportCustomExpenseHistoryProcessor());
        registerHistoryProcessor("transactionItemView", new TransactionItemViewHistoryProcessor());
        registerHistoryProcessor("opportunity", new OpportunityHistoryProcessor());
        registerHistoryProcessor("case", new CaseHistoryProcessor());
        registerHistoryProcessor("email", new EmailHistoryProcessor());
        registerHistoryProcessor("consignment", new ConsignmentHistoryProcessor());
        registerHistoryProcessor("event", new EventHistoryProcessor());
        registerHistoryProcessor("accountingemailcompose", new AccountingEmailComposeHistoryProcessor());
        registerHistoryProcessor("vatreturn", new VATReturnHistoryProcessor());
        registerHistoryProcessor("vattransaction", new VATReturnTransactionHistoryProcessor());
        registerHistoryProcessor(ITEM_LIST, new CustomFormItemHistoryProcessor());
        registerHistoryProcessor("import", new ContactImportHistoryProcessor());
        registerHistoryProcessor("customizationSettings", new CustomizationSettingsHistoryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
        registerHistoryProcessor("emailcompose", new EmailComposeHistoryProcessor());
        registerHistoryProcessor("progressinvoicing", new ProgressInvoicingHistoryProcessor());
        registerHistoryProcessor("detailedreport", new TrialBalanceDetailedHistoryProcessor());
        registerHistoryProcessor("billableExpense", new BillableExpenseHistoryProcessor());
        registerHistoryProcessor("importPurchaseOrders", new ImportPurchaseOrderHistoryProcessor());
        registerHistoryProcessor("webhook", new WorkflowWebHookHistoryProcessor());
        registerHistoryProcessor("webhookEdit", new WorkflowWebHookEditHistoryProcessor());
        registerHistoryProcessor("webhooklist", new WebhookListHistoryProcessor());
    }

    public void registerMenuItems() {
        if (Utils.isAccountingSetup()) {
            addMenuItems();
        }
    }

    private void addMenuItems() {

        if (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD)) {
            addNewMenuItem(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), "salequote|add/add", 'q');
        }
        if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD)) {
            addNewMenuItem(Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()), "saleinvoice|add/add", 'i');  // i accessKey
        }

        if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_ADD)) {
            addNewMenuItem(Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), "saleorder|add/add");
        }
        if (Utils.hasPermission(PM_MAIN_MENU) && Utils.hasPermission(ACCOUNTING_TIMESHEET_INVOICE_ADD) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
            addNewMenuItem(accountingStrings.timesheetInvoice(), "projectBaseInvoice|add/add");
        }
        if (Utils.hasPermission(ACCOUNTING_BASE_INVOICE_ADD) && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT)) {
            addNewMenuItem(accountingStrings.saleOrderBaseInvoice(), "saleorderBaseInvoice|add/add");
        }
        if (Utils.hasPermission(ACCOUNTING_CUSTOMER_ADD)) {
            addNewMenuItem(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), "client|add/add");
        }
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_ADD)) {
            addNewMenuItem(Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), "purchaseorder|add/add", 'o');
        }
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_ADD)) {
            addNewMenuItem(Property.get(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()), "purchaseinvoice|add/add", 'p');
        }
        if (Utils.hasPermission(ACCOUNTING_SUPPLIER_ADD)) {
            addNewMenuItem(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), "supplier|add/add");
        }
        if (Utils.hasPermission(ACCOUNTING_PRODUCT_ADD)) {
            addNewMenuItem(Property.get(Constants.PRODUCTS_OR_SERVICES, accountingStrings.productOrService()), "product|add/add", 't');
        }
        if (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(EXPENSES_CLAIM, wfmStrings.expenseClaims()), "expenseReports|add/add", 'w');// w accessKey
        }
        if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_ADD)) {
            addNewMenuItem(Property.get(Constants.CUSTOMER_PREPAYMENT, accountingStrings.customerPrepayment(), wfmStrings.customer()), "prepayment|add/add/");
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_SUPPLIER_CREDIT_ADD : ACCOUNTING_SUPPLIER_CREDIT_ADD)) {
            addNewMenuItem(Property.get(Constants.SUPPLIER_LIST, accountingStrings.supplierPrepayment(), wfmStrings.supplier()), "supplierCredit|add/add/");
        }
        if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_ADD)) {
            addNewMenuItem(Property.get(Constants.ASSEMBLY_PRODUCTS, wfmStrings.assemblyItems(), wfmStrings.assemblyItems()), "product|add/add/from_assembly");
        }
        if (Utils.hasPermission(ACCOUNTING_BUILD_ASSEMBLY_ADD)) {
            addNewMenuItem(Property.get(Constants.BUILD_ASSEMBLY_PRODUCTS, accountingStrings.buildAssembly(), accountingStrings.buildAssembly()), "buildAssembly|add/add");
        }
    }

    private void setAccountingPropertyListingsMap(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap) {

        for (SelectItem selectItem : propertyListingsMap.keySet()) {
            LinkedList<View> viewList = new LinkedList<>();
            if (ModuleEnum.ACCOUNTING.getCode().equals(selectItem.getDescription())) {
                LinkedList<PropertyItem> propertyItemList = propertyListingsMap.get(selectItem);
                for (PropertyItem propertyItem : propertyItemList) {
                    if (propertyItem != null) {
                        switch (propertyItem.getObjectName()) {
                            case SALE_QUOTE:
                                viewList.add(new SaleQuoteListView());
                                break;
                            case SALE_ORDER_CODE:
                                viewList.add(new SaleOrderListView());
                                break;
                            case SALE_INVOICE:
                                viewList.add(new SaleInvoiceListView());
                                break;
                            case RECURRING_INVOICE:
                                viewList.add(new RecurringInvoiceListView());
                                break;
                            case REQUEST_FOR_QUOTE:
                                viewList.add(new RequestForQuoteListView());
                                break;
                            case REQUEST_FOR_PURCHASE:
                                viewList.add(new RequestForPurchaseListView());
                                break;
                            case PURCHASE_ORDER:
                                viewList.add(new PurchaseOrderListView());
                                break;
                            case PURCHASE_INVOICE:
                                viewList.add(new PurchaseInvoiceListView());
                                break;
                            case RECURRING_BILL:
                                viewList.add(new RecurringBillsListView());
                                break;
                            case FIXED_ASSETS:
                                viewList.add(new FixedAssetRegisterListView());
                                break;
                            case EXPENSES_CLAIM:
                                viewList.add(new ExpenseListView(ACCOUNTING_CONTEXT));
                                break;
                            case CLIENT_LIST:
                                viewList.add(new NewClientListView(true));
                                break;
                            case SUPPLIER_LIST:
                                viewList.add(new SupplierListView());
                                break;
                            case PRODUCTS_OR_SERVICES:
                                viewList.add(new ProductsServicesListView());
                                break;
                            case INVENTORY_ITEMS:
                                viewList.add(new InventoryItemsListView());
                                break;
                            case BUILD_ASSEMBLY_PRODUCTS:
                                viewList.add(new BuildAssemblyItemListview());
                                break;
                            case ASSEMBLY_PRODUCTS:
                                viewList.add(new AssemblyItemListview());
                                break;
                            case RENTAL_PRODUCTS:
                                viewList.add(new RentalProductListView());
                                break;
                            case RENTAL_ORDERS:
                                viewList.add(new RentalOrderListView());
                                break;
                            case BANKACCOUNT:
                                viewList.add(new BankAccountingListView());
                                break;
                            case TRASH_BIN:
                                viewList.add(new TrashBinListView());
                                break;
                            case "STOCK_ADJUSTMENT":
                                viewList.add(new StockAdjustmentsListView());
                                break;
                            case "STOCK_OUT":
                                viewList.add(new StockOutListView());
                                break;
                            case "consignment":
                                viewList.add(new ConsignmentListView());
                                break;
                            case "CASH_RECEIPT":
                                viewList.add(new CashReceiptsListView());
                                break;
                            case "CASH_PAYMENT":
                                viewList.add(new CashPaymentListView());
                                break;
                            case "RECEIVE_MONEY":
                                viewList.add(new BankReceiptListView());
                                break;
                            case "SPEND_MONEY":
                                viewList.add(new BankPaymentListView());
                                break;
                            case CUSTOMER_PREPAYMENT:
                                viewList.add(new CustomerPrepaymentListView());
                                break;
                            case "supplierPrepayment":
                                viewList.add(new SupplierPrepaymentListView());
                                break;
                            case "checkList":
                                viewList.add(new CheckListView());
                                break;
                            case Constants.MANUAL_TRANSACTIONS:
                                viewList.add(new ManualEntryListView());
                                break;
                            case "BATCH_RECEIVE_PAYMENT":
                                viewList.add(new ReceivePaymentListView());
                                break;
                            case PAYBILLS_LIST:
                                viewList.add(new PayInvoiceListView());
                                break;
                            case "goodsreceivednotes":
                                viewList.add(new GoodsReceivedNotesListView());
                                break;
                            case "goodsdeliverednotes":
                                viewList.add(new GoodsDeliveredNotesListView());
                                break;
                            case "newprofitLoss":
                                viewList.add(new NewProfitAndLossView());
                                break;
                            case "balanceSheet":
                                viewList.add(new NewBalanceSheetView());
                                break;
                            case "trialBalance":
                                viewList.add(new NewTrialBalanceView());
                                break;
                            case "cashFlowStatement":
                                viewList.add(new NewCashFlowView());
                                break;
                            case "arAgingSummary":
                                viewList.add(new NewAgingSummaryView());
                                break;
                            case "apAgingSummary":
                                viewList.add(new NewAgingSummaryView(PAYABLE));
                                break;
                            case "journalReport":
                                viewList.add(new NewJournalReportView());
                                break;
                            case "transactionsByPeriod":
                                viewList.add(new NewAccountTransactionView());
                                break;
                            case "stockValuation":
                                viewList.add(new NewStockValuationView(true, null));
                                break;
                            case "vatReturnsSaudiOrUae":
                                viewList.add(new VatReturnsListView());
                                break;
                            case "oldGccVatReturn":
                                viewList.add(new OldGccVatReturnReportView());
                                break;
                            case "vatReturn":
                                viewList.add(new NewVatReturnReportView());
                                break;
//                            case "vatReturns":
//                                viewList.add(new VatReturnReportsListView());
//                                break;
                            case "budgetsheetView":
                                viewList.add(new NewBudgetSheetView());
                                break;
                            case "warehouseList":
                                viewList.add(new WarehousesListView());
                                break;
                            case "STOCK_TRANSFER":
                                viewList.add(new StockTransferListView());
                                break;
                            default:
                                if (propertyItem.isCustom()) {
                                    if (Constants.PAGE.equals(propertyItem.getType())) {
                                        if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_SUMMARY_" + Utils.getCompanyID())) {
                                            viewList.add(new CustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        } else if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_EDIT_" + Utils.getCompanyID()) || Utils.hasPermission(propertyItem.getFormID() + "_ADD_" + Utils.getCompanyID())) {
                                            viewList.add(new AddCustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        }
                                    } else {
                                        viewList.add(new CustomFormItemListView(propertyItem.getfID(), getLocalizedPlural(propertyItem), propertyItem.getFormID()));
                                    }
                                }
                        }
                    }
                }
            }

            DynamicSinksContainer dynamicSC = new DynamicSinksContainer(selectItem.getCode(), selectItem.getName(), viewList);
            dynamicSC.setPreparedView(selectItem.getCategory());
            if (isFirstContener) {
                selectedContener = dynamicSC;
                setSelection(selectedContener);
                isFirstContener = false;
            }
            setSinksContainer(dynamicSC);
        }
    }

    private String getLocalizedPlural(PropertyItem propertyItem) {
        if (propertyItem.getlPlural() != null) {
            switch (Utils.getUserLanguage()) {
                case "en":
                    return propertyItem.getlPlural().getEnglishName();
                case "ar":
                    return propertyItem.getlPlural().getArabicName();
                case "ru":
                    return propertyItem.getlPlural().getRussianName();
                case "uz":
                    return propertyItem.getlPlural().getUzbekName();
            }
        }
        return propertyItem.getPlural();
    }
}
