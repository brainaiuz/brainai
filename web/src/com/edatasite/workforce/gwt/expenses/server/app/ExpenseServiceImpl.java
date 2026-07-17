package com.edatasite.workforce.gwt.expenses.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseCategory;
import com.edatasite.workforce.core.domain.EdsExpenseHistory;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.core.domain.accounting.EdsExpensePaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsExpenseTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSharedNumber;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.customfields.EdsExpenseItemCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.solr.component.ExpenseReportClaimsSolrComponent;
import com.edatasite.workforce.core.solr.document.ExpenseReportClaimsSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateUtils;
import com.edatasite.workforce.gwt.core.server.app.PathFinder;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseManager;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExchangeRateHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExpenseItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SharedNumberManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.InvoiceCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.ExpenseReportEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ProjectBudgetCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.utils.CurrencyConverter;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseCategory;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseEmailTemplateData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpensePaymentData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportViewParameters;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseRequestObject;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseProjectItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentAndPrePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.ItemTableSettingsServiceLocal;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

//import com.edatasite.workforce.rest.aspects.CheckPermission;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.10.2008
 * Time: 20:57:44
 */
@Transactional
@Service("expenseService")
public class ExpenseServiceImpl implements ExpenseService, ExpenseServiceLocal, Constants, AccountingConstants {

    private static final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);
    @Autowired
    protected LayoutManager layoutManager;
    @Autowired
    private ExpenseManager expenseManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ExpenseCategoryManager categoryManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ExpenseHistoryManager historyManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ExpensePaymentManager expensePaymentManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    @Qualifier("expensesViewPDFHandler")
    private IPostPDFHandler expensesViewPDFHandler;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    @Qualifier("accountingService")
    private AccountingServiceLocal accountingService;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private ExchangeRateHistoryManager exchangeRateHistoryManager;
    @Autowired
    private FixedAssetManager fixedAssetManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private ItemTableSettingsServiceLocal itemTableSettingsServiceLocal;
    @Autowired
    private ExpenseItemCFManager expenseItemCFManager;
    @Autowired
    private InvoiceCFManager invoiceCFManager;
    @Autowired
    private BatchPaymentManager batchPaymentManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private SharedNumberManager sharedNumberManager;
    @Autowired
    private ExpenseReportClaimsSolrComponent expenseReportClaimsSolrComponent;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportData getReportData(ExpenseReportViewParameters parameters) {
        ExpenseReportsListItem report = new ExpenseReportsListItem();
        if (parameters == null) {
            parameters = new ExpenseReportViewParameters();
        }
        if (parameters.getObjectID() == null) {
            report.setTaxCalculationType(allInOneServiceLocal.getTaxCalcTypeForInvoice());
        }
        ReportData reportData = new ReportData();
        reportData.setCurrencies(currencyService.getCurrencies(true));
        reportData.setCategories(getCategories());
        reportData.setTaxTreatments(ServerUtils.getAsSelectItem(filterTaxTreatment(referenceManager.listReferences(Constants._TAX_TREATMENT)), ServerUtils.REFERENCE));

        if (parameters.getObjectID() != null) {
            report = getReport(parameters.getObjectID());
        } else if (parameters.getExternalObjectID() != null) {
            report = getReport(parameters.getExternalObjectID(), false);
            report.setExpenseNumberData(generateExpenseReportNumber());
            report.setExpenseNumber(report.getExpenseNumberData().getTransferNumber());
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.ExpenceReportView);
            report.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems));
        } else {
            //report.setNumber(generateExpenseReportNumber());
            report.setExpenseNumberData(generateExpenseReportNumber());
            report.setExpenseNumber(report.getExpenseNumberData().getTransferNumber());
            String dynamicTypeField = genericSettingsManager.getValueByKey(GenericSettingsEnum.DYNAMIC_CUSTOM_FIELD);
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.ExpenceReportView);

            if (dynamicTypeField != null && !dynamicTypeField.isEmpty()) {
                for (CompanyCustomFieldItem item : customFieldsItems) {
                    if (dynamicTypeField.equals(item.getColumnCode())) {
                        if (FROM_DISBURSEMENT.equals(parameters.getExternalFormID())) {
                            item.setFieldStringValue("Disbursement");
                        } else if (FROM_INTERNAL_INVOICE.equals(parameters.getExternalFormID())) {
                            item.setFieldStringValue("Internal Invoice");
                        } else {
                            item.setFieldStringValue("Expenses");
                        }
                        break;
                    }
                }
            }
            report.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems));
            report.setReporterId(expenseManager.getUser().getObjectID());
            report.setReporterName(employeeManager.getUser().getName());
            reportData.setCurrencies(currencyService.getEmployeeCurrencies(expenseManager.getUser().getObjectID(), true));
            report.setExpenseCurrency(reportData.getCurrencies().length > 0 ? (CurrencyItem) reportData.getCurrencies()[0] : null);

            if (parameters.getPurchaseOrderID() != null) {
                EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(parameters.getPurchaseOrderID());
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE)) {
                    ExpenseListItem[] expenseItems = new ExpenseListItem[1];
                    expenseItems[0] = new ExpenseListItem();
                    expenseItems[0].setPurchaseOrder(new SelectItem(purchaseOrder.getObjectID(), purchaseOrder.getNumber()));
                    report.setItems(expenseItems);
                } else {
                    report.setPurchaseOrder(new SelectItem(purchaseOrder.getObjectID(), purchaseOrder.getNumber()));
                }
            }
        }


        if (parameters.getProjectID() != null) {
            EdsProject project = projectManager.get(parameters.getProjectID());
            report.setProject(project.getAsSelectItem());
            if (project.getClient() != null) {
                report.setClientId(project.getClient().getObjectID());
                report.setClientName(project.getClient().getName());
            }
        }
        if (parameters.getOpportunityID() != null) {
            EdsOpportunity opportunity = opportunityManager.get(parameters.getOpportunityID());
            report.setOpportunity(opportunity.getAsSelectItem());
        }
        if (parameters.getSaleOrderId() != null) {
            EdsSaleQuote saleQuote = quoteManager.getSaleQuote(parameters.getSaleOrderId());
            if (saleQuote != null && saleQuote.getClient() != null) {
                report.setSaleOrderClient(new SelectItem(saleQuote.getClient().getObjectID(), saleQuote.getClient().getName()));
                report.setTitle(saleQuote.getNumber());
            }
        }

        if (financialSettingsManager.getFinancialSettings().isVatRegistered()) {

        }
        //Expense report item custom fields
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.ExpenseReportItem);
        report.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        report.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.EXPENSE_CLAIM_ITEM));
        report.setSystemCustomFields(commonServiceLocal.getCompanyCustomFields(ViewName.ExpenceReportViewSystem));

        report.setPdfTemplateList(invoiceServiceLocal.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.EXPENSE_REPORT.name()));
        report.setJoinOpportunityToExpenseClaim(expenseManager.getUser().getCompany().getCompanySettings().getJoinOpportunityToExpenseClaim());
        report.setApproveProcessEnabled(approverManager.isExistApproverByEntityType(RelationItem.TYPE_EXPENSE_CLAIM));
        reportData.setReport(report);
        reportData.setAccounts(accountingService.getAccountsForExpense(new ListingFilterParameter()));
        reportData.setBaseCurrency(getBaseCurrency());

        reportData.setLayoutHTML(PathFinder.getLayoutHTML(EXPENSE_REPORT));
        reportData.setDoubleTaxEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_TAX_ENABLED));

        return reportData;
    }

    @Override
    public ReportData getReportSummaryData(Integer reportId) {
        ReportData reportData = new ReportData();
        reportData.setLayoutHTML(PathFinder.getLayoutHTML(EXPENSE_REPORT));

        reportData.setOnlyLinksShow(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EXPENSE_IMAGE_LINK_ENABLED));
        reportData.setDoubleTaxEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_TAX_ENABLED));
        reportData.setReport(getReport(reportId));
        reportData.getReport().setPdfTemplateList(invoiceServiceLocal.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.EXPENSE_REPORT.name()));

        //Expense report item custom fields
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.ExpenseReportItem);
        reportData.getReport().setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        reportData.getReport().setCustomItemColumns(itemTableSettingsServiceLocal.getColumnConfigs(ItemTableEnum.EXPENSE_CLAIM_ITEM, false, true));
        return reportData;
    }

    @Override
    public ExpensePaymentData[] getExpensePayments(Integer reportID) {
        EdsExpenseReport expenseReport = expenseReportManager.get(reportID);
        List<EdsExpensePayment> payments = expensePaymentManager.getPayments(expenseReport);
        ArrayList<ExpensePaymentData> paymentItems = new ArrayList<>();
        int i = 0;
        for (EdsExpensePayment ep : payments) {
            paymentItems.add(ep.getExpensePaymentAsRPC());
        }
        List<EdsInvoicePayment> invPayments = invoicePaymentManager.getExpensePaymentItems(expenseReport.getObjectID());
        if (!CollectionUtils.isEmpty(invPayments)) {
            for (EdsInvoicePayment edsInvoicePayment : invPayments) {
                ExpensePaymentData data = new ExpensePaymentData();
                data.setApplyCredit(true);
                data.setObjectID(edsInvoicePayment.getObjectID());
                data.setReportId(expenseReport.getObjectID());
                data.setDate(new DateNonConvertable(edsInvoicePayment.getPaymentDate()));
                data.setPaymentAmount(edsInvoicePayment.getAmountInInvoiceCurrency() != null ? edsInvoicePayment.getAmountInInvoiceCurrency() : edsInvoicePayment.getAmount());
                data.setExchangeRate(edsInvoicePayment.getExchangeRate());
                if (edsInvoicePayment.getCrmAccount() != null) {
                    data.setSupplier(edsInvoicePayment.getCrmAccount().getAsSelectItem());
                }
                paymentItems.add(data);
            }
        }

        return paymentItems.toArray(new ExpensePaymentData[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCategories() {
        EdsUser user = employeeManager.getUser();
        List<EdsExpenseCategory> categories = categoryManager.getCategoriesByCompany(user.getCompany());
        SelectItem[] items = new SelectItem[categories.size()];
        int i = 0;
        for (EdsExpenseCategory category : categories) {
            items[i++] = new SelectItem(category.getObjectID(), category.getName());
        }
        return items;
    }


    public Integer addExpenseCategory(ExpenseCategory category) {
        EdsExpenseCategory expenseCategory = categoryManager.isUnicalCategoryName(category.getName());
        if (expenseCategory == null) {
            expenseCategory = new EdsExpenseCategory();
            expenseCategory.setName(category.getName());
            categoryManager.create(expenseCategory);
        }

        return expenseCategory.getObjectID();
    }

    public ExpenseListItem[] getExpenses(Integer reportId) {
        return getExpenses(reportId, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ExpenseListItem[] getExpenses(Integer reportId, boolean loadAttachments) {
        List<EdsExpense> expenseList = expenseReportManager.getExpenseReport(reportId).getExpenses();
        ExpenseListItem[] result = new ExpenseListItem[expenseList.size()];
        List<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.ExpenseReportItem);

        int i = 0;
        for (EdsExpense expense : expenseList) {
            result[i] = expense.createExpenseListItem();

            if (loadAttachments) {
                result[i].setAttachments(getFileResources(expense.getObjectID()));
            }
            if (expense.getCustomFields() != null) {
                result[i].setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(expense.getCustomFields(), CustomFieldsUtils.cloneItemCustomFields(itemCustomFields)));
            }
            result[i].setProjectBasedEntryIds(expenseManager.getRelatedTimesheetsByExpense(expense.getObjectID()));
            i++;
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsExpense.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(reportId);
        ServerUtils.kpiLog(log, kpiLog, "View expense claim");
        return result;
    }


    public ExpenseReportsListItem getReport(Integer objectId) {
        return getReport(objectId, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ExpenseReportsListItem getReport(Integer objectId, boolean loadAttachments) {
        //NumberData numberData = new NumberData();
        ExpenseReportsListItem result;
        EdsExpenseReport report = expenseReportManager.getExpenseReport(objectId);
        if (report == null) {
            return new ExpenseReportsListItem();
        }
        result = report.createExpenseReportListItem();

        EdsExpenseTransaction edsExpenseTransaction = transactionManager.getTransactionByExpense(report);
        if (edsExpenseTransaction != null && !edsExpenseTransaction.isDeleted()) {
            result.setJournalId(edsExpenseTransaction.getJournalId());
        }

        result.setExpenseNumber(report.getNumber());
        result.setExpenseNumberData(parseExpenseNumberData(report));

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            result.setReporterName(null);
        }
        if (result.getBaseCurrency() == null) {
            result.setBaseCurrency(getBaseCurrency());
        }
        if (result.getExpenseCurrency() == null) {
            result.setExpenseCurrency(result.getBaseCurrency());
        }

        int categoryCount = 0;
        result.setItems(getExpenses(objectId, loadAttachments));
        for (ExpenseListItem ei : result.getItems()) {
            if (ei.getCurrencyId() != null) {
                result.setOldExpense(true);
            }
            if (ei.getAccountId() != null) {
                categoryCount++;
            }
        }
        if (categoryCount == result.getItems().length) {
            result.setCategoriesSelected(true);
        }

        if (report.getCurrentApprover() != null && report.getCurrentApprover().getExactEmployee() != null) {
            result.setApprover(expenseReportManager.getUser().getObjectID().equals(report.getCurrentApprover().getExactEmployee().getObjectID()));
        }
        if (report.getSaleOrder() != null) {
            result.setSaleOrder(new SelectItem(report.getSaleOrder().getObjectID(), report.getSaleOrder().getNumber()));
        }
        if (report.getPurchaseOrder() != null) {
            result.setPurchaseOrder(new SelectItem(report.getPurchaseOrder().getObjectID(), report.getPurchaseOrder().getNumber()));
            BigDecimal quoteTotalReceivedAllocation = BigDecimal.ZERO;
            for (EdsQuoteItem quoteItem : report.getPurchaseOrder().getQuoteItems()) {
                quoteTotalReceivedAllocation = Optional.ofNullable(quoteItem.getReceivedAllocation()).orElse(BigDecimal.ZERO);
            }
            result.setTotalAllocated(quoteTotalReceivedAllocation);
        }
        if (report.getSupplier() != null) {
            result.setSupplier(report.getSupplier().getAsSelectItem());
        }
        result.setFixedAsset(report.getFixedAsset() != null ? report.getFixedAsset().getAsSelectItem() : null);
        result.setTaxCalculationType(report.getTaxCalculationType());
        result.setPurpose(report.getPurpose());
        result.setPlace(report.getPlace());
        result.setTaxTotal(report.getTaxTotal());
        result.setTotal(report.getTotal());
        result.setBaseTotal(report.getBaseTotal());
        result.setPaidTotal(getPaidTotal(report));
        result.setDueTotal(report.getTotal() != null ? report.getTotal().subtract(result.getPaidTotal()) : BigDecimal.ZERO);
        result.setJoinOpportunityToExpenseClaim(expenseManager.getUser().getCompany().getCompanySettings().getJoinOpportunityToExpenseClaim());
        result.setCreatedDate(new DateNonConvertable(report.getCreationDate()));
        result.setUpdatedDate(new DateNonConvertable(report.getLastUpdateTime()));
        FileItem[] attachments = getAttachments(report.getObjectID());
        if (attachments != null && attachments.length > 0) {
            result.setAttachments(attachments);
        } else {
            result.setAttachments(new FileItem[0]);
        }

        result.setNoteItems(getReportsHistory(objectId));
        /*if (report.getIntNumber() != null && !"".equals(report.getIntNumber()) && !"".equals(report.getNumber()) && report.getNumber() != null) {
            numberData.setNumberString(report.getNumber());
            numberData.setIntNumber(report.getIntNumber());
            numberData.setNumberFormat(generateExpenseReportNumber().getNumberFormat());
            result.setNumber(numberData);
        } else {
            result.setNumber(generateExpenseReportNumber());
        }*/
        //init invoice custom fields
        EdsInvoiceCustomFields customFields = report.getCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.ExpenceReportView);
        result.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));
        result.setSystemCustomFields(commonServiceLocal.getCompanyCustomFields(ViewName.ExpenceReportViewSystem));

        if (report.getTaxTreatment() != null) {
            result.setTaxTreatment(report.getTaxTreatment().getAsSelectItem());
            result.getTaxTreatment().setCode(report.getTaxTreatment().getCode());
        }
        if (report.getPlaceOfSupply() != null) {
            result.setPlaceOfSupply(report.getPlaceOfSupply().getAsSelectItem());
            result.getPlaceOfSupply().setCode(report.getPlaceOfSupply().getCode());
            result.getPlaceOfSupply().setCategory(PLACEOFSUPPLY_CATEGORY.REGION);
        } else if (report.getPlaceOfSupplyGCCCountry() != null) {
            result.setPlaceOfSupply(report.getPlaceOfSupplyGCCCountry().getAsSelectItem());
            result.getPlaceOfSupply().setCode(report.getPlaceOfSupplyGCCCountry().getCode());
            result.getPlaceOfSupply().setCategory(PLACEOFSUPPLY_CATEGORY.COUNTRY);
        }
        result.setReversechargeApplicable(report.isReverseChargeApplicable());

        result.setPaymentItems(getExpensePayments(objectId));
        result.setReporterId(report.getReporter() != null ? report.getReporter().getObjectID() : report.getCandidate() != null ? report.getCandidate().getObjectID() : null);
        result.setReporterName(report.getReporter() != null ? report.getReporter().getName() : report.getCandidate() != null ? report.getCandidate().getName() : null);
        result.setPdfTemplateId(report.getPdfTemplate() != null ? report.getPdfTemplate().getObjectID() : null);
        result.setApproveProcessEnabled(approverManager.isExistApproverByEntityType(RelationItem.TYPE_EXPENSE_CLAIM));

        return result;
    }

    private BankTransferNumberData parseExpenseNumberData(EdsExpenseReport report) {
        BankTransferNumberData numberData = getExpenseNumberData();

        numberData.setFourDigitNumber(report.getIntNumber() != null ? new DecimalFormat("0000").format(report.getIntNumber()) : "");

        String dateString = ServerUtils.getBankTransferDateNumber(report.getStartDate());
        numberData.setWithDate(report.getNumber() != null && report.getNumber().contains(dateString));
        numberData.setDate(numberData.isWithDate() ? dateString : "");

        return numberData;
    }


    private BankTransferNumberData getExpenseNumberData() {
        BankTransferNumberData numberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber;
        String numberFormat = null;

        if (settings != null) {
            numberFormat = settings.getExpenseNumberingFormat();
        }
        if (numberFormat == null) {
            numberFormat = EdsNumberingSettings.DEF_EX_PREFIX;
        }

        fourDigitNumber = expenseReportManager.getLastIntNumber();

        parseNumber(numberFormat, numberData, fourDigitNumber);

        return numberData;
    }

    private void parseNumber(String numberFormat, BankTransferNumberData numberData, Integer fourDigitNumber) {
        String lastFourNumber = null;
        String[] partNumbers = numberFormat.split("_");
        numberData.setPrefix(partNumbers[0]);
        if (partNumbers.length == 1) {
            lastFourNumber = "0";
        }
        if (partNumbers.length == 2 && !numberFormat.contains("date")) {
            lastFourNumber = partNumbers[1];
        }
        if (numberFormat.contains("date")) {
            numberData.setWithDate(true);
            numberData.setDate(ServerUtils.getBankTransferDateNumber(new Date()));
            lastFourNumber = numberFormat.substring(numberFormat.length() - 9, numberFormat.length() - 5);
        }

        Integer intLastFourNumber = 1;
        try {
            intLastFourNumber = Integer.parseInt(lastFourNumber);
        } catch (NumberFormatException e) {
        }
        DecimalFormat format = new DecimalFormat("0000");
        numberData.setFourDigitNumber((fourDigitNumber != null && fourDigitNumber.compareTo(intLastFourNumber) >= 0) ? format.format(fourDigitNumber + 1) : lastFourNumber);
    }

    //@CheckPermission(permissions = {PermissionConstants.HRMS_EXPENCE_REPORT})
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ExpenseReportsListItem> getEmployeesReportList(ListingFilterParameter filter) {
        //below is incorrect one which caused issue with pagination on webui
//        return getSortedList(expenseReportManager.getEmployeeReports(filter, false));
        Integer employee_id = null;
        if (filter.getEmployeeId() == null || filter.getEmployeeId() == 0) {
            employee_id = employeeManager.getUser().getObjectID();
        } else {
            employee_id = filter.getEmployeeId();
        }
        SelectItem[] employeeId = {new SelectItem(employee_id, "")};

        ListingFilterParameter filterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ExpenceReportListPanel);
        if (StringUtils.isNotBlank(filter.getSortField())) {
            filterParameter.setSortField(filter.getSortField());
            filterParameter.setAscending(filter.isAscending());
        } else {
            filterParameter.setSortField(AccountingConstants.PERIOD_COLUMN);
            filterParameter.setAscending(filter.isAscending());
        }
        if (filter.getEmployeeId() != null) {
            filterParameter.setEmployeeId(filter.getEmployeeId());
        }
        filterParameter.setAccessEnabled(false);
        filterParameter.setStatusID(filter.getStatusID());
        filterParameter.setStart(filter.getStart());
        filterParameter.setLimit(filter.getLimit());
        filterParameter.setCurrentPage(filter.getCurrentPage());
        filterParameter.setSearchKey(filter.getSearchKey());
        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_EXPENSES_SEE_ALL)) {
            filterParameter.getFacetFilter().getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[1]).setFacetItems(employeeId);
        }
        filterParameter.setHRMS(filter.isHRMS());
        return getExpenseReportsDataFromSolr(filterParameter);
    }

    public ListResult<ExpenseReportsListItem> getCompanyReports(String status, ListingFilterParameter fp) {
        return getSortedList(expenseReportManager.getCompanyReports(status, fp));
    }

    private ListResult<ExpenseReportsListItem> getSortedList(List<EdsExpenseReport> reportList) {
        ArrayList<ExpenseReportsListItem> listItem = new ArrayList<>();
        EdsUser user = expenseReportManager.getUser();
        int totalCount = reportList.size();
        for (EdsExpenseReport report : reportList) {
            ExpenseReportsListItem li = report.createExpenseReportListItem();
            if (isOk(report.getCurrentApprover()) && isOk(report.getCurrentApprover().getExactEmployee())) {
                li.setApprover(user.getObjectID().equals(report.getCurrentApprover().getExactEmployee().getObjectID()));
            }
            li.setStatus(referenceWfmMessageSource.localize(li.getStatusCode(), li.getOverallStatusName()));
            listItem.add(li);
        }
        return new ListResult<>(listItem, totalCount);
    }

    public ListResult<ExpenseReportsListItem> getExpenseReportsDataFromSolr(ListingFilterParameter filterParametrs) {
        FacetFilterRpc reportsFacetFilter = filterParametrs.getFacetFilter();
        if (reportsFacetFilter != null && !reportsFacetFilter.isFilterChanges()) {
            reportsFacetFilter = commonServiceLocal.getUserFacetFilter(reportsFacetFilter);
        }

        if (filterParametrs.getStartDateNC() != null) {
            filterParametrs.setStartDate(ServerUtils.parseFilterParameterDate(filterParametrs.getStartDateNC()));
        }
        if (filterParametrs.getEndDateNC() != null) {
            filterParametrs.setEndDate(ServerUtils.parseFilterParameterDate(filterParametrs.getEndDateNC()));
        }

        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        String solrQuery = getExpenseReportsCoreSolrQuery(filterParametrs, edsUser) +
                SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(
                        reportsFacetFilter,
                        FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                        reportsFacetFilter,
                        edsCompany,
                        SolrExpenseReportRepresenter.FIELD_START_DATE,
                        SolrExpenseReportRepresenter.FIELD_START_DATE,
                        FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]
                );
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsExpense.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get expense claim list");
        return getExpenseReportsResponse(filterParametrs, edsUser, solrQuery);
    }


    public String getExpenseReportsCoreSolrQuery(ListingFilterParameter filterParameter, EdsUser user) {

        boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.EXPENSE_SEE_OWN);
        if (filterParameter.getClientId() != null) {
            EdsCrmAccount crmAccount = crmAccountManager.get(filterParameter.getClientId());
            ownerAccess = ownerAccess && crmAccount.getOwners().contains(user);
        }

        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrExpenseReportRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            solrQuery.append(" AND ").append(SolrExpenseReportRepresenter.FIELD_COMPOSITE).append(":( ").append(QueryBuilderForSolr.normalaizeKeyword(filterParameter.getSearchKey()));
            if (!filterParameter.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(solrQuery, QueryBuilderForSolr.getDynSearchFields(), filterParameter.getSearchKey());
            }
            solrQuery.append(")");
        }
        if (filterParameter.getProjectId() != null) {
            solrQuery.append(" AND (").append(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID).append(":").append(filterParameter.getProjectId())
                    .append(" OR ").append(SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_ID).append(":").append(filterParameter.getProjectId()).append(") ");
        }
        if (StringUtils.isNotBlank(filterParameter.getStatusCode())) {
            solrQuery.append(" AND (").append(SolrExpenseReportRepresenter.FIELD_STATUS_CODE).append(":").append(filterParameter.getStatusCode()).append(") ");
        }
        if (filterParameter.isFromMobile() && !(ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM) || ServerUtils.hasPermission(PermissionConstants.HRMS_CAN_APPROVE_EXPENSE_CLAIM))) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_APPROVER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" )");
        } else if (!ServerUtils.hasPermission(filterParameter.isHRMS() ? PermissionConstants.HRMS_EXPENSES_SEE_ALL : PermissionConstants.ACCOUNTING_EXPENSE_FULL_LIST_ACCESS)) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_SUPPLIER_OWNER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" OR ").append(SolrExpenseReportRepresenter.FIELD_APPROVER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" OR ").append(SolrExpenseReportRepresenter.FIELD_APPROVER2_ID).append(":").append(user.getObjectID());
            solrQuery.append(" OR ").append(SolrExpenseReportRepresenter.FIELD_REPORTER_ID).append(":").append(user.getObjectID());
            solrQuery.append(" )");
        }
        if (filterParameter.isHRMS() ? ServerUtils.hasPermission(PermissionConstants.HRMS_EXPENCE_REPORT) && ServerUtils.hasPermission(PermissionConstants.HRMS_COMPANY_EXPENSE_LIST) :
                ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_LIST) && ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_LIST)) {
            //it's ok do not nothing
        } else if (filterParameter.isHRMS() ? ServerUtils.hasPermission(PermissionConstants.HRMS_EXPENCE_REPORT) : ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_LIST)) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE).append(":").append(Boolean.FALSE);
            solrQuery.append(" )");
        } else if (filterParameter.isHRMS() ? ServerUtils.hasPermission(PermissionConstants.HRMS_COMPANY_EXPENSE_LIST) : ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_LIST)) {
            solrQuery.append(" AND ");
            solrQuery.append("( ");
            solrQuery.append(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE).append(":").append(Boolean.TRUE);
            solrQuery.append(" )");
        }
        if (filterParameter.getObjectsIds() != null) {
            solrQuery.append(" AND (").append(SolrExpenseReportRepresenter.FIELD_REPORT_ID).append(":").append(filterParameter.getObjectsIds().replace(",", " OR " + SolrExpenseReportRepresenter.FIELD_REPORT_ID + ":")).append(")");
        }
        if (filterParameter.getEmployeeId() != null) {
            solrQuery.append(" AND ((").append(SolrExpenseReportRepresenter.FIELD_REPORTER_ID).append(":").append(filterParameter.getEmployeeId()).append(")");
            solrQuery.append(" OR   (").append(SolrExpenseReportRepresenter.FIELD_APPROVER_ID).append(":").append(filterParameter.getEmployeeId()).append("))");
        }
        return solrQuery.toString();
    }


    private ListResult<ExpenseReportsListItem> getExpenseReportsResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery) {
        Page<ExpenseReportClaimsSolrDoc> resp = expenseReportClaimsSolrComponent.getList(filterParameter, solrQuery);
        return getExpenseReportsFromSolrResult(resp, edsUser, filterParameter);
    }

    private ListResult<ExpenseReportsListItem> getExpenseReportsFromSolrResult(Page<ExpenseReportClaimsSolrDoc> resp, EdsUser currentUser, ListingFilterParameter filterParameter) {
        boolean isProjectInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        boolean isPurchaseOrderInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE);
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        int totalNumber = (int) resp.getTotalElements();
        ArrayList<ExpenseReportsListItem> reportsItems = new ArrayList<>();

        List<String> customFieldColumns = (filterParameter.getListPanelTool() != null
                && filterParameter.getListPanelTool().getColumnCodeName() != null
                && !filterParameter.getListPanelTool().getColumnCodeName().isEmpty()) ? filterParameter.getListPanelTool().getColumnCodeName() : null;
        boolean isApproveProcessEnabled = approverManager.isExistApproverByEntityType(RelationItem.TYPE_EXPENSE_CLAIM);

        if (resp.getContent() != null) {
            for (ExpenseReportClaimsSolrDoc doc : resp.getContent()) {
                if (doc != null) {

                    ExpenseReportsListItem reportItem = expenseWrapSolrDocumentToRPC(doc, isProjectInLine);
                    reportItem.setApproveProcessEnabled(isApproveProcessEnabled);

                    if (!filterParameter.isShortList()) {

                        EdsExpenseReport expenseReport = expenseReportManager.getExpenseReport(reportItem.getId());
                        if (expenseReport != null) {
                            if (expenseReport.getCustomFields() != null && customFieldColumns != null) {
                                reportItem.setCustomFields(CustomFieldsUtils.getRPCCustomFields(expenseReport.getCustomFields(), panelTools.getColumnCodeName()));
                            }
                            List<EdsExpense> expenses = expenseReport.getExpenses();
                            if (expenses != null && !expenses.isEmpty()) {
                                int categoryCount = 0;
                                for (EdsExpense expense : expenses) {
                                    if (expense.getAccount() != null) {
                                        categoryCount++;
                                    }
                                }
                                reportItem.setCategoriesSelected(categoryCount == expenses.size());
                            }
                            if (isPurchaseOrderInLine) {
                                if (expenses != null && !expenses.isEmpty()) {
                                    StringBuilder sb = new StringBuilder();
                                    for (EdsExpense expense : expenses) {
                                        if (expense.isAllocatedToPO()) {
                                            sb.append(expense.getPurchaseOrder().getNumber());
                                            sb.append(",");
                                        }
                                    }
                                    reportItem.setPurchaseOrderNumber(!sb.isEmpty() ? sb.substring(0, sb.toString().length() - 1) : "");
                                }
                            } else if (expenseReport.getPurchaseOrder() != null) {
                                reportItem.setPurchaseOrder(new SelectItem(expenseReport.getPurchaseOrder().getObjectID(), expenseReport.getPurchaseOrder().getNumber()));
                                reportItem.setPurchaseOrderNumber(expenseReport.getPurchaseOrder().getNumber());

                                if (expenses != null && !expenses.isEmpty() && expenses.get(0) != null) {
                                    EdsQuote mainQuote = quoteManager.get(reportItem.getPurchaseOrder().getId());
                                    BigDecimal quoteTotalReceivedAllocation = BigDecimal.ZERO;
                                    for (EdsQuoteItem quoteItem : mainQuote.getQuoteItems()) {
                                        quoteTotalReceivedAllocation = Optional.ofNullable(quoteItem.getReceivedAllocation()).orElse(BigDecimal.ZERO);
                                    }
                                    reportItem.setAllocatedToPO(quoteTotalReceivedAllocation.compareTo(BigDecimal.ZERO) != 0);
                                }
                            }
                            if (expenseReport.getProject() != null && expenseReport.getProject().getStatus() != null) {
                                reportItem.setProjectStatusCode(expenseReport.getProject().getStatus().getCode());
                            }
                        }
                    }
                    reportsItems.add(reportItem);
                }
            }
        }
        return new ListResult<>(reportsItems, totalNumber);
    }

    private ExpenseReportsListItem expenseWrapSolrDocumentToRPC(ExpenseReportClaimsSolrDoc doc, boolean isProjectInLine) {
        ExpenseReportsListItem item = new ExpenseReportsListItem();
        item.setId(doc.getReportId());
        item.setTitle(doc.getTitle());
        Date startDate = doc.getStartDate();
        item.setStartDate(startDate != null ? new DateNonConvertable(startDate) : null);
        item.setProjectName(getProjectName(isProjectInLine, doc));
        item.setReporterId(doc.getReporterId());

        //NumberData numberData = new NumberData();
        //numberData.setNumberString(SolrUtils.asString(doc, SolrExpenseReportRepresenter.FIELD_NUMBERING));
        item.setExpenseNumber(doc.getNumbering());

        item.setReporterName(doc.getReporterName());
        item.setTaxTotal(BigDecimal.valueOf(doc.getTaxAmount()));
        item.setApproverSelectItem(new SelectItem(doc.getCurrentApproverExactEmployeeId(), doc.getCurrentApproverExactEmployeeName()));
        item.setStatusCode(doc.getStatusCode());
        item.setStatus(referenceWfmMessageSource.localize(item.getStatusCode(), item.getOverallStatusName()));
        item.setTotal(BigDecimal.valueOf(doc.getOrginalAmount()));
        item.setPaidTotal(BigDecimal.valueOf(doc.getPaidAmount()));
        item.setDueTotal(BigDecimal.valueOf(doc.getDueAmount()));
        item.setFixedAsset(new SelectItem(doc.getFixedAssetId(), doc.getFixedAssetName()));
        item.setSupplier(new SelectItem(doc.getSupplierId(), doc.getSupplierName()));
        item.setCompanyExpense(doc.getCompanyExpense());
        Integer currencyID = doc.getCurrencyId();
        if (currencyID != null) {
            item.setExpenseCurrency(new CurrencyItem(currencyID, doc.getCurrencyName()));
        }
        ArrayList<ApproverItemMini> approvers = new ArrayList<>();
        ArrayList<Integer> approverIdList, approverStatusIdList, exactEmployeeIdList;
        List<String> exactEmployeeNameList, approverStatusCodeList;
//        for (int i = 1; i <= 10; i++) {
//            approverIdList = SolrUtils.asListInteger(doc, SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_ID + i);
//            if (approverIdList != null && approverIdList.size() > 0) {
//
//                ApproverItemMini approverItemMini = new ApproverItemMini();
//                approverItemMini.setObjectID(approverIdList.get(0));
//                approverItemMini.setApproverOrder(i);
//
//                approverStatusIdList = SolrUtils.asListInteger(doc, SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_STATUS_ID + i);
//                if (approverStatusIdList != null) {
//                    approverStatusCodeList = SolrUtils.asListString(doc, SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_STATUS_CODE + i);
//                    ReferenceItem status = new ReferenceItem();
//                    status.setId(approverStatusIdList.get(0));
//                    status.setCode(approverStatusCodeList != null ? approverStatusCodeList.get(0) : null);
//                    approverItemMini.setStatus(status);
//                }
//
//                exactEmployeeIdList = SolrUtils.asListInteger(doc, SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_ID + i);
//                if (exactEmployeeIdList != null) {
//                    exactEmployeeNameList = SolrUtils.asListString(doc, SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_NAME + i);
//                    approverItemMini.setExactEmployee(new SelectItem(exactEmployeeIdList.get(0), exactEmployeeNameList != null ? exactEmployeeNameList.get(0) : null));
//                }
//
//                approvers.add(approverItemMini);
//            }
//        }
//        item.setApprovers(approvers);

        Integer prevApproverID = doc.getPreviousApproverId();
        if (prevApproverID != null) {
            ApproverItemMini prevApprover = new ApproverItemMini();
            prevApprover.setObjectID(prevApproverID);
            prevApprover.setExactEmployee(new SelectItem(doc.getPreviousApproverExactEmployeeId(), doc.getPreviousApproverExactEmployeeName()));
            ReferenceItem status = new ReferenceItem();
            status.setId(doc.getPreviousApproverStatusId());
            status.setCode(doc.getPreviousApproverStatusCode());
            prevApprover.setStatus(status);

            item.setPrevApprover(prevApprover);
        }

        Integer currentApproverID = doc.getCurrentApproverId();
        if (currentApproverID != null) {
            ApproverItemMini currentApprover = new ApproverItemMini();
            currentApprover.setObjectID(currentApproverID);
            currentApprover.setExactEmployee(new SelectItem(doc.getCurrentApproverExactEmployeeId(), doc.getCurrentApproverExactEmployeeName()));
            ReferenceItem status = new ReferenceItem();
            status.setId(doc.getCurrentApproverStatusId());
            status.setCode(doc.getCurrentApproverStatusCode());
            currentApprover.setStatus(status);

            item.setCurrentApprover(currentApprover);
        }

        Integer overallStatusID = doc.getOverallStatusId();
        if (overallStatusID != null) {
            ReferenceItem overallStatus = new ReferenceItem();
            overallStatus.setId(doc.getOverallStatusId());
            overallStatus.setCode(doc.getOverallStatusCode());
            overallStatus.setName(overallStatus.getCode() != null ? referenceWfmMessageSource.localize(overallStatus.getCode()) : doc.getOverallStatusName());
            item.setOverallStatus(overallStatus);
        }

        return item;
    }

    public SolrQuery getExpenseReportsSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(filterParameter.getLimit()));

        if (!filterParameter.isSearchButton()) {
            if (StringUtils.isNotBlank(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                switch (filterParameter.getSortField()) {
                    case AccountingConstants.TITLE_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.SORTABLE_TITLE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.NUMBER_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.FIELD_NUMBERING, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.PROJECT_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.SORTABLE_RELATED_PROJECT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.REPORTER_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.SORTABLE_REPORTER_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.APPROVER_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.SORTABLE_APPROVER_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.STATUS_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.SORTABLE_STATUS_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.ORIGINAL_AMOUNT_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.PAID_AMOUNT_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.FIELD_PAID_AMOUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.DUE_AMOUNT_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.FIELD_DUE_AMOUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.PERIOD_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.FIELD_START_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.TAX_AMOUNT_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.FIELD_TAX_AMOUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case AccountingConstants.CURRENCY_COLUMN ->
                            query.setSort(SolrExpenseReportRepresenter.FIELD_CURRENCY_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                }
            } else {
                query.setSort(SolrExpenseReportRepresenter.FIELD_REPORT_ID, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    private String getProjectName(Boolean isProjectInLine, ExpenseReportClaimsSolrDoc solrDoc) {
        if (isProjectInLine) {
            return ServerUtils.asListToString(solrDoc.getMultiProjectNumberName());
        }
        return solrDoc.getRelatedProjectNumberName();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRelatedProjects() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(DR);
        return invoiceCircularResolver.getRelatedProjects(fp);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getApprover(Integer projectID, boolean firstApprover) {
        if (firstApprover) {
            List<EdsEmployee> accountants = employeeManager.getEmployeesForAccounting(null, new String[]{EdsRole.ACCOUNTANT_CODE});
            SelectItem[] result = new SelectItem[accountants.size()];
            int i = 0;
            for (EdsEmployee acc : accountants) {
                result[i++] = new SelectItem(acc.getObjectID(), acc.getName());
            }
            return result;
        } else {
            EdsUser user = employeeManager.getUser();
            EdsEmployee teamLeader = user.getEmployee().getTeam().getLeader();
            List<EdsEmployee> approverList = employeeManager.getDirectors();
            if (teamLeader != null) {
                approverList.add(teamLeader);
            }
            Set<EdsEmployee> uniqApprovers = new HashSet<>();
            for (EdsEmployee approver : approverList) {
                if (!approver.getDeleted()) {
                    uniqApprovers.add(approver);
                }
            }
            //If project selected. If not only directors would be shown.
            if (projectID != null) {
                EdsProject project = projectManager.get(projectID);
                EdsEmployee manager = project.getManager();
                if (!manager.getDeleted()) {
                    uniqApprovers.add(manager);
                }
            }

            SelectItem[] result = new SelectItem[uniqApprovers.size()];
            int i = 0;
            for (EdsEmployee employee : uniqApprovers) {
                result[i++] = new SelectItem(employee.getObjectID(), employee.getFullName());
            }

            return result;
        }
    }

    public SelectItem[] getApproversForLookUp(ListingFilterParameter parametrs) {
        String formType = parametrs.getInvoiceType();
        List<EdsEmployee> employees = null;
        parametrs.setResignedEmployeesIncluded(true);
        if (EXPENSE_REPORT.equals(formType)) {
            List<String> roles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM);
            boolean isFirstApprover = parametrs.isNewType();
            boolean isDoubleApproverEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EXPENSE_DOUBLE_APPROVER_ENABLED);
            if (isDoubleApproverEnabled && isFirstApprover) {
                List<String> _roles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM_DOUBLE_APPROVE);
                employees = employeeManager.getEmployeesForAccounting(parametrs, _roles.toArray(new String[]{}));
            } else {
                employees = employeeManager.getEmployeesForAccounting(parametrs, roles.toArray(new String[]{}));
            }
        } else if (SALE_QUOTE.equals(formType)) {
            List<String> roles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_CAN_APPROVE_SALES_QUOTE);
            employees = employeeManager.getEmployeesForAccounting(parametrs, roles.toArray(new String[]{}));
        } else if (PURCHASE_ORDER.equals(formType)) {
            List<String> roles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_CAN_APPROVE_PURCHASE_ORDER);
            employees = employeeManager.getEmployeesForAccounting(parametrs, roles.toArray(new String[]{}));
        } else {
            employees = new LinkedList<>();
        }
        SelectItem[] result = new SelectItem[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            result[i++] = new SelectItem(employee.getObjectID(), employee.getFullName());
        }
        return result;
    }

    @Deprecated
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem getBaseCurrency() {
        return invoiceCircularResolver.getBaseCurrency();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem[] getReportsHistory(Integer reportId) {
        List<EdsExpenseHistory> historyList = historyManager.getReportsHistory(reportId);
        if (historyList == null) {
            historyList = new LinkedList<>();
        }

        List<HistoryListItem> noteItemsList = new LinkedList<>();
        for (EdsExpenseHistory item : historyList) {
            if (StringUtils.isNotBlank(item.getComment())) {
                noteItemsList.add(item.getHistoryItem());
            }
        }

        return noteItemsList.toArray(new HistoryListItem[0]);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Double getExchRateForExpenseReport(String from, String to) {
        if (from.equals(to)) {
            return 1d;
        }

        CurrencyConverter converter = CurrencyConverter.getInstance();
        try {
            return converter.getExchangeRateDouble(from, to);
        } catch (IllegalArgumentException ex) {
            return 1d;
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            return 1d;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Double getExchRate(String to) {
        CurrencyItem currencyItem = invoiceCircularResolver.getBaseCurrency();
        return getExchRateForExpenseReport(currencyItem.getName(), to);
    }

    @Transactional
    public String changeExpenseStatus(Integer reportId, String status, String note, Boolean isApproveForAll, ArrayList<ExpenseListItem> lineItems) {
        EdsExpenseReport report = saveStatus(reportId, status, isApproveForAll);
        if (status.equals(EXPENSE_DECLINED)) {
            report.setRejectionNote(note);
        }
        if (StringUtils.isNotBlank(note)) {
            HistoryListItem historyListItem = new HistoryListItem();
            historyListItem.setEmployee(expenseReportManager.getUser().getName());
            historyListItem.setEventDate(new Date());
            historyListItem.setComment(note);
            createNote(report, historyListItem);
        }
        if (EXPENSE_SUBMITTED.equals(status)) {
            sendEmail(reportId);
        }

        expenseReportManager.update(report);
        EdsUser user = expenseReportManager.getUser();

        if (report.getProject() != null || genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            EdsBusinessEvent event = null;
            if (status.equals(EXPENSE_DECLINED)) {
                event = baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.EXPENSE_REPORT_DECLINE, report, user);
                event.setCustomStringField(report.getProject() != null ? report.getProject().getObjectID().toString() : "");
            } else if (status.equals(EXPENSE_SUBMITTED) || status.equals(EXPENSE_APPROVED)) {
                event = baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.EXPENSE_REPORT_SUBMIT, report, user);
                event.setCustomStringField(report.getProject() != null ? report.getProject().getObjectID().toString() : "");
            }

        }

        if (lineItems != null && !lineItems.isEmpty()) {
            for (ExpenseListItem listItem : lineItems) {
                EdsExpense expense = expenseManager.get(listItem.getId());
                expense.setAccount(accountingManager.get(listItem.getAccountId()));
                expenseManager.update(expense);
            }
        }
        addExpenseReportToSolr(report);
        allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_EXPENSE_CLAIM, report.getObjectID(), null);


        //EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum.ON_APPROVE_REJECT.name(), report, user);
        //workflowEvent.setEntityType(RelationItem.TYPE_EXPENSE_CLAIM);

        if (EXPENSE_APPROVED.equals(status) || EXPENSE_DECLINED.equals(status)) {
            baseEventPostProcessor.registerEvent(ExpenseReportEventListenerImpl.TYPE, EXPENSE_APPROVED.equals(status) ? ExpenseReportEventListenerImpl.EVENT_EXPENSE_REPORT_APPROVE : ExpenseReportEventListenerImpl.EVENT_EXPENSE_REPORT_DECLINE, report, user);
        }
        return referenceWfmMessageSource.localizeRef(report.getStatus());
    }

    private EdsExpenseReport saveStatus(Integer reportId, String status, Boolean isApproveForAll) {
        if (reportId == null) { //expense claim status add qilindi deb qo'ysa bo'ladi.
            throw new IllegalArgumentException("Status can not be changed without report.");
        }
        EdsExpenseReport expenseReport = expenseReportManager.getExpenseReport(reportId);
        if (expenseReport == null) {
            throw new IllegalArgumentException("Expense with such objectId doesn't exist. ObjectId = " + reportId);
        }
        EdsReference reference = referenceManager.findReference(Constants.EXPENSE_STATUS, status);
        if (reference == null) {
            throw new IllegalArgumentException("There is no such status for Expense. Status name = " + status);
        }
        if (isApproveForAll != null && isApproveForAll) {
            expenseReport.setOverallStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_APPROVED));
        } else if (!EdsExpenseReport.EXPENSE_APPROVED.equals(reference.getCode())) {
            expenseReport.setOverallStatus(reference);

        } else if (EdsExpenseReport.EXPENSE_APPROVED.equals(reference.getCode())
                && expenseReport.getOverallStatus() != null
                && Constants.EXPENSE_DRAFT.equals(expenseReport.getOverallStatus().getCode())) {

            expenseReport.setOverallStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));
        }
        expenseReport.updateStatus(reference);

        return expenseReport;
    }

    @Transactional
    public Integer saveReport(ExpenseReportsListItem report) {
        if (StringUtils.isBlank(report.getExpenseNumber()) || report.getIntNumber() == null) {
            BankTransferNumberData numberData = generateExpenseReportNumber();
            report.setExpenseNumberData(numberData);
            report.setExpenseNumber(numberData.getTransferNumber());
            report.setIntNumber(Integer.valueOf(numberData.getFourDigitNumber()));
        }
        if (expenseReportManager.isExpenseNumberExists(report.getExpenseNumber(), report.getId(), (report.getStartDate() != null ? report.getStartDate().getNonConvertedDate() : null))) {
            return -1;
        }

        EdsUser user;
        boolean isEdit = true;
        String expenseOldStatus = null;
        if (!report.isCandidate() && report.getEmployeeId() != null && report.getEmployeeId() != 0) {
            user = employeeManager.get(report.getEmployeeId());
        } else {
            user = employeeManager.getUser();
        }

        EdsExpenseReport expenseReport = report.getId() != null ? expenseReportManager.getExpenseReport(report.getId()) : null;
        if (expenseReport == null) {
            expenseReport = new EdsExpenseReport();
            expenseReport.setReporter(user.getEmployee());
            Integer intNumber = report.getIntNumber() != null ? report.getIntNumber() : Integer.valueOf(getExpenseNumberData().getFourDigitNumber());
            expenseReport.setIntNumber(intNumber);
            expenseReportManager.create(expenseReport);
            isEdit = false;

            if (report.getExchangeRate() != null && report.getExpenseCurrency() != null && report.getExpenseCurrency().getId() != null) {
                exchangeRateHistoryManager.registerExpenseReportExRateHistory(currencyManager.get(report.getExpenseCurrency().getId()), report.getExchangeRate());
            }
        } else {
            expenseOldStatus = expenseReport.getStatus().getCode();
            if (report.getIntNumber() != null) {
                expenseReport.setIntNumber(report.getIntNumber());
            }
            if (Constants.EXPENSE_PAID.equals(expenseOldStatus) || (Constants.PARTIALLY_PAID.equals(expenseOldStatus) && report.getTotal().compareTo(getPaidTotal(expenseReport)) == 0)) {
                report.setStatusCode(Constants.EXPENSE_PAID);
            }
            if (Constants.EXPENSE_PAID.equals(expenseOldStatus) && report.getTotal().compareTo(getPaidTotal(expenseReport)) > 0) {
                report.setStatusCode(Constants.PARTIALLY_PAID);
            }
        }

        if (report.getReporterId() != null) {
            if (report.isCandidate()) {
                expenseReport.setCandidate(crmContactManager.get(report.getReporterId()));
            } else {
                expenseReport.setReporter(employeeManager.get(report.getReporterId()));
            }
        }

        if (report.getSaleOrder() != null && report.getSaleOrder().getId() != null) {
            expenseReport.setSaleOrder(quoteManager.getSaleQuote(report.getSaleOrder().getId()));
        }
        expenseReport.setTitle(report.getTitle());
        expenseReport.setDescription(report.getDescription());
        if (report.getStartDate() != null) {
            expenseReport.setStartDate(report.getStartDate().getNonConvertedDate());
        }
        expenseReport.setTaxCalculationType(report.getTaxCalculationType());
        expenseReport.setPeriodStartDate(report.getPeriodStartDate() != null ? report.getPeriodStartDate().getNonConvertedDate() : null);
        expenseReport.setPeriodEndDate(report.getPeriodEndDate() != null ? report.getPeriodEndDate().getNonConvertedDate() : null);
        if (report.getPdfTemplateId() != null) {
            expenseReport.setPdfTemplate(companyPdfTemplateManager.get(report.getPdfTemplateId()));
        }
        expenseReport.setCompanyExpense(report.isCompanyExpense());
        /*if (report.getIntNumber() != null) {
            expenseReport.setIntNumber(report.getIntNumber());
        }*/
        if (StringUtils.isNotBlank(report.getExpenseNumber())) {
            expenseReport.setNumber(report.getExpenseNumber());
        }

        if (report.getFixedAsset() != null && report.getFixedAsset().getId() != null) {
            expenseReport.setFixedAsset(fixedAssetManager.get(report.getFixedAsset().getId()));
        }

        if (report.getProject() != null && report.getProject().getId() != null) {
            expenseReport.setProject(projectManager.get(report.getProject().getId()));
        }
        if (report.getOpportunity() != null && report.getOpportunity().getId() != null) {
            expenseReport.setOpportunity(opportunityManager.get(report.getOpportunity().getId()));
        }

        if (report.getSupplier() != null && report.getSupplier().getId() != null) {
            expenseReport.setSupplier(crmAccountManager.get(report.getSupplier().getId()));
        }
        if (report.getTaxTreatment() != null) {
            expenseReport.setTaxtreatmentId(report.getTaxTreatment().getId());
            expenseReport.setTaxTreatment(referenceManager.get(report.getTaxTreatment().getId()));
        }
        if (report.getPayableAccount() != null && report.getPayableAccount().getId() != null) {
            expenseReport.setPayableAccount(accountingManager.get(report.getPayableAccount().getId()));
        }
        if (report.getPlaceOfSupply() != null && report.getPlaceOfSupply().getId() != null) {

            if (PLACEOFSUPPLY_CATEGORY.COUNTRY.equals(report.getPlaceOfSupply().getCategory())) {
                expenseReport.setPlaceofsupplyGCCCountryId(report.getPlaceOfSupply().getId());
                expenseReport.setPlaceOfSupplyGCCCountry(countryManager.get(report.getPlaceOfSupply().getId()));

                expenseReport.setPlaceofsupplyId(null);
            } else {
                expenseReport.setPlaceofsupplyId(report.getPlaceOfSupply().getId());
                expenseReport.setPlaceOfSupply(regionManager.get(report.getPlaceOfSupply().getId()));

                expenseReport.setPlaceofsupplyGCCCountryId(null);
            }
        } else {
            expenseReport.setPlaceofsupplyId(null);
            expenseReport.setPlaceofsupplyGCCCountryId(null);
        }
        expenseReport.setReverseChargeApplicable(report.isReversechargeApplicable());

        if (report.getApproverSelectItem() != null && report.isFromOldMobile()) {
            ApproverItemMini approverItemMini = new ApproverItemMini();
            approverItemMini.setObjectID(report.getApproverSelectItem().getId());
            approverItemMini.setApproverOrder(1);
            report.getApprovers().add(approverItemMini);
        }

        if (isOk(report.getApprovers())) {
            if (Constants.EXPENSE_DRAFT.equals(expenseOldStatus)) {
                // it was difficult to merge expense approvers, so just deleting old records
                approverManager.deletedAprovers(RelationItem.TYPE_EXPENSE_CLAIM, report.getId());
                //delete prev/current approvers
                report.setCurrentApprover(null);
                report.setPrevApprover(null);
                expenseReport.setCurrentApprover(null);
                expenseReport.setPrevApprover(null);
            }

            report.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : report.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null && !_edsApprover.getDeleted()) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (Constants.EXPENSE_SUBMITTED.equals(report.getStatusCode()) && isFirstApprover) {
                        expenseReport.setPrevApprover(null);
                        expenseReport.setCurrentApprover(_edsApprover);
                        expenseReport.getCurrentApprover().setStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, report.getStatusCode()));
                        expenseReport.setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));
                        isFirstApprover = false;
                    } else if (expenseReport.getCurrentApprover() != null && report.getStatusCode() != null && isFirstApprover) {
                        expenseReport.getCurrentApprover().setStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, report.getStatusCode()));
                        expenseReport.setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));

                        isFirstApprover = false;
                    } else if (expenseReport.getCurrentApprover() != null && report.getStatusCode() != null) {
                        expenseReport.getCurrentApprover().setStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));
                    }
                    if (report.getStatusCode() != null && !EXPENSE_APPROVED.equals(report.getStatusCode())) {
                        expenseReport.setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, report.getStatusCode()));
                    }
                    if (expenseReport.isCurrentApproverRejected()) {
                        expenseReport.setEntityStatus(expenseReport.getCurrentApprover().getStatus());
                    }
                } else/* if (!isEdit)*/ {
                    EdsApprover edsApprover = _edsApprover.cloneShallow();
                    edsApprover.setObjectID(null);
                    edsApprover.setApproverHistory(new HashSet<>());
                    edsApprover.setEntityID(expenseReport.getObjectID());
                    edsApprover.setIs_default(false);
                    edsApprover.setDeleted(false);
                    if (report.getStatusCode() != null && isFirstApprover) {
                        edsApprover.setStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, report.getStatusCode()));
                        if (Constants.EXPENSE_DRAFT.equals(report.getStatusCode())) {
                            expenseReport.setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, report.getStatusCode()));
                        } else {
                            expenseReport.setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));
                        }
                        isFirstApprover = false;
                    } else if (report.getStatusCode() != null) {
                        edsApprover.setStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));
                    }
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        edsApprover.setExactEmployee(user_);
                    }
                    edsApprover.setApproverRoles(new HashSet<>());
                    edsApprover.setApproverEmployees(new HashSet<>());
                    edsApprover.setDynamicQueries(new HashSet<>());
                    approverManager.createOrUpdate(edsApprover);

                    for (EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                        edsApprover.getApproverRoles().add(roleapp);
                    }

                    for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                        edsApprover.getApproverEmployees().add(ucerapp);
                    }

                    if (expenseReport.getCurrentApprover() == null) {
                        expenseReport.setCurrentApprover(edsApprover);
                    }
                    expenseReport.getApprovers().add(edsApprover);
                }
            }
            //update after new approvers set
            expenseReportManager.update(expenseReport);
        } else {
            if (Constants.EXPENSE_DRAFT.equals(report.getStatusCode())) {
                expenseReport.setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, report.getStatusCode()));
            } else {
                expenseReport.setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));
            }
        }

        if (report.getPurchaseOrder() != null && report.getPurchaseOrder().getId() != null) {
            expenseReport.setPurchaseOrder(quoteManager.getPurchaseOrderByID(report.getPurchaseOrder().getId()));
        } else {
            expenseReport.setPurchaseOrder(null);
        }
        expenseReport.setPurpose(report.getPurpose());
        expenseReport.setPlace(report.getPlace());
        if (report.getBaseCurrency() != null && report.getBaseCurrency().getId() != null) {
            expenseReport.setBaseCurrency(currencyManager.getCurrency(report.getBaseCurrency().getId()));
        }
        if (report.getExpenseCurrency() != null) {
            expenseReport.setCurrency(currencyManager.get(report.getExpenseCurrency().getId()));
        }
        expenseReport.setExchangeRate(report.getExchangeRate());

        if (report.getItems() != null && report.getItems().length > 0) {
            List<EdsExpense> expenseSet = new LinkedList<>();
            for (ExpenseListItem item : report.getItems()) {
                EdsExpense expense = saveExpenseReportItem(expenseReport.getObjectID(), item);
                expenseSet.add(expense);
            }
            expenseReport.setExpenses(expenseSet);
        }

        expenseReport.setTaxTotal(report.getTaxTotal());
        expenseReport.setTotal(report.getTotal());
        expenseReport.setBaseTotal(report.getBaseTotal());

        if (report.getCustomFieldItems() != null && !report.getCustomFieldItems().isEmpty()) {
            expenseReport.setCustomFields(invoiceServiceLocal.createInvoiceCustomFields(report.getCustomFieldItems()));
        }

        expenseReportManager.update(expenseReport);
        if (report.getNoteItems() != null && report.getNoteItems().length > 0) {
            updateNotes(expenseReport, report.getNoteItems());
        }

        if (expenseReport.getObjectID() != null && report.getAttachments() != null && report.getAttachments().length > 0 && report.getAttachments()[0].getId() != null) {
            attachmentUtilsManager.saveAttachments(F_EXP_DOC, expenseReport.getObjectID(), expenseReport.getObjectID(), report.getAttachments());
        }

        if (report.isReSubmit()) {
            sendExpenseReportMail(expenseReport);
        }

        updateProjectsExpenseDate(report);

        //Register event in MyUpdate
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        if (isEdit) {
            baseEventPostProcessor.registerEvent(ExpenseReportEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, expenseReport, user);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, expenseReport, user);
            workflowEvent.setEntityType(RelationItem.TYPE_EXPENSE_CLAIM);


            kpiLog.setEntityName(EdsExpense.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(expenseReport.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update expense claim");
        } else {
            baseEventPostProcessor.registerEvent(ExpenseReportEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, expenseReport, user);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, expenseReport, user);
            workflowEvent.setEntityType(RelationItem.TYPE_EXPENSE_CLAIM);
            kpiLog.setEntityName(EdsExpense.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(expenseReport.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Add expense claim");
        }

        if (expenseReport.getOverallStatus() != null
                && (EXPENSE_SUBMITTED.equals(expenseReport.getOverallStatus().getCode()) || EXPENSE_APPROVED.equals(expenseReport.getOverallStatus().getCode()))
                && (expenseReport.getProject() != null && expenseReport.getProject().getObjectID() != null)
                || genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {

            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.EXPENSE_REPORT_SUBMIT, expenseReport, expenseReportManager.getUser());
            event.setCustomStringField(expenseReport.getProject() != null ? expenseReport.getProject().getObjectID().toString() : "");
        }
        /* Create Push Notification */
        if (report.getStatusCode() != null && (EXPENSE_APPROVED.equals(report.getStatusCode()) || EXPENSE_DECLINED.equals(report.getStatusCode()))) {
            baseEventPostProcessor.registerEvent(ExpenseReportEventListenerImpl.TYPE,
                    EXPENSE_APPROVED.equals(report.getStatusCode()) ? ExpenseReportEventListenerImpl.EVENT_EXPENSE_REPORT_APPROVE
                            : ExpenseReportEventListenerImpl.EVENT_EXPENSE_REPORT_DECLINE,
                    expenseReport, user);
        }
        addExpenseReportToSolr(expenseReport);


        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent2 = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), expenseReport, user);
        workflowEvent2.setEntityType(RelationItem.TYPE_EXPENSE_CLAIM);

        return expenseReport.getObjectID();
    }

    private void addExpenseReportToSolr(EdsExpenseReport expenseReport) {
        try {
            expenseReportClaimsSolrComponent.index(expenseReport);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void updateNotes(EdsExpenseReport expenseReport, HistoryListItem[] noteItems) {
        List<Integer> existingIDList = new LinkedList<>();
        for (HistoryListItem ni : noteItems) {
            if (ni.getObjectID() == null) {
                EdsExpenseHistory eNote = createNote(expenseReport, ni);
                existingIDList.add(eNote.getObjectID());
            }
            if (ni.getObjectID() != null) {
                EdsExpenseHistory expenseHistory = historyManager.get(ni.getObjectID());
                if (expenseHistory != null) {
                    expenseHistory.setComment(ni.getComment());
                    expenseHistory.setSuperUser(ServerUtils.isSuperUser());
                    historyManager.update(expenseHistory);
                }
                existingIDList.add(ni.getObjectID());
            }
        }

        List<EdsExpenseHistory> noteList = historyManager.getReportsHistory(expenseReport.getObjectID());
        for (EdsExpenseHistory note : noteList) {
            if (!existingIDList.contains(note.getObjectID())) {
                historyManager.delete(note);
            }
        }
    }

    @Transactional
    public EdsExpenseHistory createNote(EdsExpenseReport expenseReport, HistoryListItem noteItem) {
        EdsUser user = employeeManager.getUser();
        EdsExpenseHistory reportHistory = new EdsExpenseHistory();
        String statusName = referenceWfmMessageSource.localizeRef(expenseReport.getStatus()).toLowerCase();
        String status = expenseReport.getStatus().getCode().equals(Constants.EXPENSE_DRAFT) ?
                statusName + "ed" : statusName.toLowerCase();
        String reportEventDescription = user.getFullName() + " " + status + " the expense claim.";

        reportHistory.setEvent(expenseReport.getStatus());
        reportHistory.setEventDescription(reportEventDescription);
        reportHistory.setExpenseReport(expenseReport);
        if (user instanceof EdsEmployee) {
            reportHistory.setEmployee((EdsEmployee) user);
        } else {
            reportHistory.setEmployee(employeeManager.get(user.getObjectID()));
        }
        reportHistory.setEventDate(getCompanyDate());
        reportHistory.setComment(noteItem.getComment());
        reportHistory.setSuperUser(ServerUtils.isSuperUser());

        historyManager.create(reportHistory);

        return reportHistory;
    }

    public Integer createExpenseClaimHistory(Integer expenseReportId, HistoryListItem hisItem) {
        if (expenseReportId != null && hisItem != null) {
            EdsExpenseHistory expenseHistory = new EdsExpenseHistory();
            expenseHistory.setExpenseReport(expenseReportManager.get(expenseReportId));
            expenseHistory.setEmployee(employeeManager.getUser().getEmployee());
            expenseHistory.setSuperUser(ServerUtils.isSuperUser());
            expenseHistory.setComment(hisItem.getComment());

            expenseHistory.setEventDate((new DateNonConvertable(new Date())).getNonConvertedDate());

            historyManager.create(expenseHistory);
            return expenseHistory.getObjectID();
        }
        return null;
    }

    public Boolean deleteExpenseHistory(Integer expenseHistoryId) {
        if (expenseHistoryId != null) {
            historyManager.delete(historyManager.get(expenseHistoryId));
        }
        return false;
    }

    @Override
    public void deleteSelectedExpenseReports(ArrayList<Integer> ids) {
        for (Integer id : ids) {
            deleteExpenseReport(id);
        }
    }

    public Boolean deleteExpenseReport(Integer objectID) {
        EdsExpenseReport expenseReport = expenseReportManager.get(objectID);
        if (expenseReport != null) {
            if (!canDeleteExpenseReport(expenseReport)) {
                return false;
            }

            if (expenseReport.getExpenses() != null && expenseReport.getExpenses().size() > 0) {
                for (EdsExpense expense : expenseReport.getExpenses()) {
                    if (expenseReportManager.isUsedForInvoices(expense.getObjectID())) {
                        return false;
                    }
                }
            }

            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(ExpenseReportEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, expenseReport, expenseReportManager.getUser());
            event.setCustomStringField(expenseReport.getTitle());

            transactionManager.deleteExpenseReportTransaction(expenseReport);

            approverManager.deletedAprovers(RelationItem.TYPE_EXPENSE_CLAIM, expenseReport.getObjectID());

            expenseReport.setDeleted(true);
            expenseReportManager.update(expenseReport);

            List<EdsExpensePayment> expensePayments = expenseReport.getPayments();
            if (expensePayments != null && expensePayments.size() > 0) {
                for (EdsExpensePayment ep : expensePayments) {
                    transactionManager.deleteExpensePaymentTransaction(ep);
                    ep.setDeleted(true);
                    expensePaymentManager.update(ep);
                }
            }

            for (EdsExpense expense : expenseReport.getExpenses()) {
                expenseManager.removeRelatedTimesheetsFromExpense(expense.getObjectID());
            }
            try {
                solrManager.removeExpenseReportByIds(expenseReport.getObjectID());
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }

            //If you removing expense report then you should clean its project budgets
            if (expenseReport.getProject() != null || genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                EdsReference sts = expenseReport.getStatus();
                if (EXPENSE_SUBMITTED.equals(sts.getCode()) || EXPENSE_APPROVED.equals(sts.getCode()) || EXPENSE_PAID.equals(sts.getCode()) || PARTIALLY_PAID.equals(sts.getCode())) {
                    EdsBusinessEvent e = baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.EXPENSE_REPORT_DECLINE, expenseReport, expenseReportManager.getUser());
                    e.setCustomStringField(expenseReport.getProject() != null ? expenseReport.getProject().getObjectID().toString() : "");
                }
            }

        } else {
            try {
                solrManager.removeExpenseReportByIds(objectID);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        return true;
    }

    private boolean canDeleteExpenseReport(EdsExpenseReport expenseReport) {
        if (expenseReport == null) {
            return false;
        }

        if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS)) {
            return true;
        }

        if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_DELETE)) {
            return false;
        }

        EdsUser currentUser = expenseReportManager.getUser();
        return currentUser != null
                && currentUser.isEmployee()
                && expenseReport.getReporter() != null
                && expenseReport.getReporter().getObjectID() != null
                && expenseReport.getReporter().getObjectID().equals(currentUser.getEmployee().getObjectID());
    }

    public PaymentAndPrePaymentData getExpenseReportPaymentsHistory(Integer objectID) {
        EdsExpenseReport expenseReport = expenseReportManager.get(objectID);
        PaymentAndPrePaymentData result = new PaymentAndPrePaymentData();
        if (expenseReport != null) {
            ArrayList<PaymentItem> payments = new ArrayList<>();
            List<EdsExpensePayment> expensePayments = expensePaymentManager.getPayments(expenseReport);
            if (expensePayments != null && expensePayments.size() > 0) {
                for (EdsExpensePayment expensePayment : expensePayments) {
                    PaymentItem paymentItem = new PaymentItem();
                    paymentItem.setDate(new DateNonConvertable(expensePayment.getPaymentDate()));
                    paymentItem.setUser(expensePayment.getUser() != null ? expensePayment.getUser().getFullName() : null);
                    paymentItem.setAmount(expensePayment.getAmount());
                    paymentItem.setReference(expensePayment.getReference());
                    paymentItem.setPaidTo(expensePayment.getAccount() != null ? expensePayment.getAccount().getName() : null);
                    paymentItem.setCrmAccount(expensePayment.getSupplier() != null ? expensePayment.getSupplier().getAsSelectItem() : null);
                    paymentItem.setStatusText(expensePayment.getStatus() != null ? expensePayment.getStatus().getName() : null);
                    payments.add(paymentItem);
                }
            }
            result.setPayments(payments.toArray(new PaymentItem[0]));
            return result;
        }
        return null;
    }

    private EdsExpense saveExpenseReportItem(Integer reportId, ExpenseListItem expenseItem) {
        EdsExpenseReport report = expenseReportManager.get(reportId);
        EdsExpense expense = expenseManager.getExpense(expenseItem.getId());

        if (expense == null) {
            expense = new EdsExpense();
            expenseManager.create(expense);
        }

        expense.setReport(report);
        expense.setAccount(accountingManager.get(expenseItem.getAccountId()));
        expense.setDescription(expenseItem.getDescription());
        expense.setUnits(expenseItem.getUnits());
        expense.setCostPerUnit(expenseItem.getCostPerUnit());
        if (expenseItem.getCurrencyId() != null) {
            expense.setCurrency(currencyManager.getCurrency(expenseItem.getCurrencyId()));
        }
        if (expenseItem.getExchageRate() != null) {
            expense.setExchageRate(expenseItem.getExchageRate());
        } else if (report.getExchangeRate() != null) {
            expense.setExchageRate(report.getExchangeRate());
        }
        expense.setTax((expenseItem.getTax() != null && expenseItem.getTax().getId() != null) ? vatManager.get(expenseItem.getTax().getId()) : null);
        expense.setDoubleTax((expenseItem.getDoubleTax() != null && expenseItem.getDoubleTax().getId() != null) ? vatManager.get(expenseItem.getDoubleTax().getId()) : null);
        expense.setClient(expenseItem.getClientId() != null ? crmAccountManager.get(expenseItem.getClientId()) : null);
        expense.setDepartment((expenseItem.getDepartment() != null && expenseItem.getDepartment().getId() != null) ? departmentManager.get(expenseItem.getDepartment().getId()) : null);
        expense.setProject(expenseItem.getProject() != null && expenseItem.getProject().getId() != null ? projectManager.get(expenseItem.getProject().getId()) : null);
        expense.setPurchaseOrder(expenseItem.getPurchaseOrder() != null && expenseItem.getPurchaseOrder().getId() != null ? quoteManager.getPurchaseOrderByID(expenseItem.getPurchaseOrder().getId()) : null);

        if (expenseItem.getDoubleTaxAmountInBase() != null) {
            expense.setDoubleTaxAmount(expenseItem.getDoubleTaxAmountInBase().multiply(report.getExchangeRate()));
        }
        if (expenseItem.getTaxAmountInTc() != null) {
            expense.setTaxAmount(expenseItem.getTaxAmountInTc());
        }
        expense.setDate(expenseItem.getIncurredDate());
        expense.setGLCode(expenseItem.getGlCode());
        expense.setSubtotal(expenseItem.getSubtotal());
        expense.setBaseSubtotal(expenseItem.getBaseSubtotal());
        expense.setMarkupAmount(expenseItem.getMarkupAmount());
        expense.setCashOrCardType(expenseItem.getCashOrCardType());
        expense.setProjectBase(expenseItem.isProjectBase());

        FileResource[] attachments = expenseItem.getAttachments();
        if (attachments != null && attachments.length > 0) {
            FileItem[] fItems = new FileItem[attachments.length];
            for (int i = 0; i < attachments.length; i++) {
                fItems[i] = new FileItem();
                fItems[i].setId(attachments[i].getObjectId());
                fItems[i].setFileName(attachments[i].getEncodedName());
            }

            attachmentUtilsManager.saveAttachments(F_EXP, expense.getObjectID(), expense.getObjectID(), fItems);
        }
        expense.setCustomFields(createExpenseItemCustomFields(expenseItem.getCustomFieldItems()));
        expenseManager.update(expense);

        if (expenseItem.getProjectBasedEntryIds() != null) {
            for (int i = 0; i < expenseItem.getProjectBasedEntryIds().length; i++) {
                if (expenseItem.getProjectBasedEntryIds()[i] != null) {
                    timeSheetManager.get(expenseItem.getProjectBasedEntryIds()[i]).setUsedInExpense(true);
                    timeSheetManager.get(expenseItem.getProjectBasedEntryIds()[i]).setExpenseID(expense.getObjectID());
                }
            }
        }

        return expense;
    }

    public EdsExpenseItemCustomFields createExpenseItemCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsExpenseItemCustomFields expenseItemCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                expenseItemCustomFields = expenseItemCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                expenseItemCustomFields = new EdsExpenseItemCustomFields();
                expenseItemCFManager.create(expenseItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(expenseItemCustomFields, customFieldItems);
            return expenseItemCustomFields;
        }
        return null;
    }

    private Date getCompanyDate() {
        EdsUser user = employeeManager.getUser();
        Calendar companyTime = new GregorianCalendar(TimeZone.getTimeZone(user.getCompany().getCountryZone().getZone().getZoneID()));
        return companyTime.getTime();
    }

    public void sendEmail(Integer reportId) {
        EdsExpenseReport expenseReport = expenseReportManager.getExpenseReport(reportId);
        sendExpenseReportMail(expenseReport);
    }

    private void sendExpenseReportMail(EdsExpenseReport expenseReport) {
        ByteArrayOutputStream pdfStream = expensesViewPDFHandler.getPDFStream(new ExpenseRequestObject(expenseReport.getObjectID(), genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EXPENSE_IMAGE_LINK_ENABLED)));

        String statusCode = getPreferredStatus(expenseReport);
        Date companyDate = expenseReportManager.getUser().getUserDate();
        if (statusCode.equals(Constants.EXPENSE_SUBMITTED)) {
            //if report has been declined earlier then send resubmit email
            EdsExpenseHistory declinedHistory = getEventLastHistoryRecord(Constants.EXPENSE_DECLINED, expenseReport.getObjectID());
            if (declinedHistory != null) {
                messageManager.reportResubmittedToApprover(expenseReport, companyDate/*resubmittedDate*/, declinedHistory.getEventDate(), pdfStream);
            } else {
                messageManager.reportSubmittedToEmployee(expenseReport, companyDate, pdfStream);
                messageManager.reportSubmittedToApprover(expenseReport, companyDate, pdfStream);
            }
            baseEventPostProcessor.registerEvent(ExpenseReportEventListenerImpl.TYPE, ExpenseReportEventListenerImpl.EVENT_EXPENSE_REPORT_SEND_TO_APPROVER, expenseReport, employeeManager.getUser());
        }

        try {
            pdfStream.flush();
            pdfStream.close();
        } catch (IOException ex) {
            log.error("Unable to close PDF Stream.", ex);
        }
    }

    public void sendEmail(Integer reportId, String message, Integer emailTemplateID) {
        EdsExpenseReport expenseReport = expenseReportManager.getExpenseReport(reportId);
        expenseReport.setEmailTemplateID(emailTemplateID);
        expenseReportManager.update(expenseReport);

        Map<String, Object> values = new TreeMap<>();
        EdsUser approver = null;
        if (expenseReport.getCurrentApprover() != null && expenseReport.getCurrentApprover().getExactEmployee() != null) {
            approver = expenseReport.getCurrentApprover().getExactEmployee();
        }
        if (approver == null) {
            approver = userManager.getUser().getEmployee();
        }
        String link = EncryptionHelper.encryptURL("expenseReports|previewReport/" + expenseReport.getObjectID() + "/" + EXPENSE_VIEW + "/" + "ACCOUNTING");
        String userUrl = EncryptionHelper.encryptURL(approver.getObjectID().toString());

        String host = EdsContextParams.getHost(approver.getCompany().getObjectID());
        String shortLink = host + "/Accounting.html?link=" + link + "&uid=" + userUrl + "&cid=" + EncryptionHelper.encryptURL(approver.getCompany().getObjectID().toString());

        values.put(EmailTemplateUtils.ET_EXPENSE_HOST, host);
        values.put(EmailTemplateUtils.ET_EXPENSE_SHORT_LINK, shortLink);
        values.put(EmailTemplateUtils.ET_LINK, link);
        values.put(EmailTemplateUtils.ET_EXPENSE_USERURL, userUrl);
        try {
            message = EdsTemplates.evaluateTemplate(values, message);//generate message
        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream pdfStream = expensesViewPDFHandler.getPDFStream(new ExpenseRequestObject(reportId, genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EXPENSE_IMAGE_LINK_ENABLED)));

        String statusCode = getPreferredStatus(expenseReport);
        Date companyDate = expenseReportManager.getUser().getUserDate();
        switch (statusCode) {
            case Constants.EXPENSE_SUBMITTED -> {
                //if report has been declined earlier then send resubmit email
                EdsExpenseHistory declinedHistory = getEventLastHistoryRecord(Constants.EXPENSE_DECLINED, reportId);
                if (declinedHistory != null) {
                    messageManager.reportResubmittedToApprover(expenseReport, message, companyDate, pdfStream);
                }
                //else send submit email
                else {
                    messageManager.reportSubmittedToEmployee(expenseReport, companyDate, pdfStream);
                    messageManager.reportSubmittedToApprover(expenseReport, message, companyDate, pdfStream);
                }
            }
            case Constants.EXPENSE_APPROVED ->
                    messageManager.reportApprovedToEmployee(expenseReport, companyDate, pdfStream);
            case Constants.EXPENSE_DECLINED -> messageManager.reportDeclinedToEmployee(expenseReport, companyDate);
        }

        try {
            pdfStream.flush();
            pdfStream.close();
        } catch (IOException ex) {
            log.error("Unable to close PDF Stream.", ex);
        }
    }

    private String getPreferredStatus(EdsExpenseReport report) {
        return report.getStatus() != null ? report.getStatus().getCode() : "";
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String reportSubmitOrResubmitReport(Integer reportID) {
        String submitedReport = Constants.EXPENSE_CLAIM_CATEGORY_SUBMIT;
        EdsExpenseHistory declinedHistory = getEventLastHistoryRecord(Constants.EXPENSE_DECLINED, reportID);
        if (declinedHistory != null) {
            submitedReport = Constants.EXPENSE_CLAIM_CATEGORY_RESUBMIT;
        }
        return submitedReport;
    }


    public Integer savePayment(ExpensePaymentData epd) {
        if (epd.isValidateReference() && accountingManager.isDuplicateReference(epd.getReferenceNumber(), null)) {
            return ExpensePaymentData.REFERENCE_EXIST;
        }

        EdsExpensePayment expensePayment = new EdsExpensePayment();
        EdsExpenseReport ep = expenseReportManager.get(epd.getReportId());
        expensePayment.setObjectID(epd.getObjectID());
        expensePayment.setBatchPayment(batchPaymentManager.get(epd.getBatchPaymentID()));
        expensePayment.setExpenseReport(ep);
        expensePayment.setSupplier(ep.getSupplier());
        expensePayment.setAmount(epd.getPaymentAmount());
        expensePayment.setPaymentDate(epd.getDate().getNonConvertedDate());

        if (epd.getPaymentAccount() != null && epd.getPaymentAccount().getId() != null) {
            expensePayment.setAccount(accountingManager.get(epd.getPaymentAccount().getId()));
        }
        expensePayment.setReference(epd.getReferenceNumber());
        expensePayment.setUser(expenseManager.getUser());
        expensePayment.setExchangeRate(epd.getExchangeRate());
        expensePayment.setAmountInEntityCurrency(epd.getPaymentAmountInExpenseCurrency());
        if (epd.getCurrency() != null) {
            expensePayment.setCurrencyID(currencyManager.get(epd.getCurrency().getId()).getObjectID());
        } else {
            expensePayment.setCurrencyID(ep.getCurrency().getObjectID());
        }

        expensePaymentManager.createOrUpdate(expensePayment);
        accountingService.createTransactionForExpencePayment(expensePayment);
        setExpensePaymentStatus(expensePayment, ep);
        addExpenseReportToSolr(ep);

        FileResource[] attachments = epd.getAttachments();
        if (attachments != null && attachments.length > 0) {

            commonServiceLocal.createExpensePaymentFolder(expensePayment.getObjectID());

            FileItem[] fItems = new FileItem[attachments.length];
            for (int i = 0; i < attachments.length; i++) {
                fItems[i] = new FileItem();
                fItems[i].setId(attachments[i].getObjectId());
                fItems[i].setFileName(attachments[i].getEncodedName());
            }

            attachmentUtilsManager.saveAttachments(F_EXP_PAYMENT, expensePayment.getObjectID(), expensePayment.getObjectID(), fItems);
        }

        if (epd.getOldPaymentAmount() != null && epd.getBatchPaymentID() != null) {
            EdsBatchPayment edsBatchPayment = batchPaymentManager.get(epd.getBatchPaymentID());
            if (edsBatchPayment != null) {
                BigDecimal batchTotal = edsBatchPayment.getTotalAmount();
                batchTotal = batchTotal.add(epd.getPaymentAmount()).subtract(epd.getOldPaymentAmount());
                edsBatchPayment.setTotalAmount(batchTotal);
                batchPaymentManager.update(edsBatchPayment);
            }
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsExpense.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(expensePayment.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, "Add Expense Payment");

        return expensePayment.getObjectID();
    }

    private void setExpensePaymentStatus(EdsExpensePayment expensePayment, EdsExpenseReport ep) {
        BigDecimal paidTotal = getPaidTotal(ep);
        Double dueAmount = ep.getTotal().doubleValue() - paidTotal.doubleValue();
        if (BigDecimal.valueOf(dueAmount).setScale(5, RoundingMode.HALF_UP).doubleValue() <= 0.01) {
//        if (ep.getTotal().compareTo(paidTotal) <= 0.01) {
            EdsReference paid = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_PAID);
            expensePayment.setStatus(paid);
            ep.setEntityStatus(paid);
        } else {
            EdsReference paid = referenceManager.findReference(EXPENSE_STATUS, PARTIALLY_PAID);
            expensePayment.setStatus(paid);
            ep.setEntityStatus(paid);
        }
    }

    public BigDecimal getPaidTotal(EdsExpenseReport ep) {
        List<EdsExpensePayment> payments = expensePaymentManager.getPayments(ep);
        BigDecimal totalPaid = ZERO;
        for (EdsExpensePayment p : payments) {
            totalPaid = totalPaid.add(p.getAmountInEntityCurrency() != null ? p.getAmountInEntityCurrency() : p.getAmount());
        }

        List<EdsInvoicePayment> invPayments = invoicePaymentManager.getExpensePaymentItems(ep.getObjectID());
        if (!CollectionUtils.isEmpty(invPayments)) {
            for (EdsInvoicePayment edsInvoicePayment : invPayments) {
                totalPaid = totalPaid.add(edsInvoicePayment.getAmountInInvoiceCurrency() != null ? edsInvoicePayment.getAmountInInvoiceCurrency() : edsInvoicePayment.getAmount());
            }
        }
        return totalPaid.setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ExpenseEmailTemplateData getEmailTemplateData(Integer expenseReportID) {
        String templateType = reportSubmitOrResubmitReport(expenseReportID);
        return new ExpenseEmailTemplateData(templateType, getEmailTemplates(templateType));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplates(String templateCategory) {
        List<EdsEmailTemplate> emailTemplates = emailTemplateManager.getEmailTemplatesByCategory(templateCategory);
        EdsEmailTemplate defaultEmailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(templateCategory);

        if (defaultEmailTemplate == null) {
            defaultEmailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(templateCategory);
        }

        ArrayList<SelectItem> itemsList = new ArrayList<>();

        if (defaultEmailTemplate != null) {
            itemsList.add(new SelectItem(defaultEmailTemplate.getObjectID(), defaultEmailTemplate.getName()));
        }

        for (EdsEmailTemplate emailTemplate : emailTemplates) {
            if (defaultEmailTemplate != null && !defaultEmailTemplate.equals(emailTemplate)) {
                itemsList.add(new SelectItem(emailTemplate.getObjectID(), emailTemplate.getName()));
            }
        }

        return itemsList.toArray(new SelectItem[]{});
    }

    @Override
    public ExpenseListItem[] getExpenseItemsForPOAllocation(Integer purchaseOrderID) {
        List<EdsExpense> expenses = expenseReportManager.getPurchaseOrderRelatedExpenseItems(purchaseOrderID);
        List<ExpenseListItem> items = new LinkedList<>();
        for (EdsExpense e : expenses) {
            ExpenseListItem item = new ExpenseListItem();
            item.setId(e.getObjectID());
            item.setReportId(e.getReport().getObjectID());
            item.setExpenseReportNumber(e.getReport().getNumber());
            item.setDate(e.getReport().getStartDate());
            if (e.getReport().getReporter() != null) {
                item.setReportReporter(new SelectItem(e.getReport().getReporter().getObjectID(), e.getReport().getReporter().getFullName()));
            }
            item.setCategoryId(e.getAccount().getObjectID());
            item.setCategoryName(e.getAccount().getName());
            if (e.getReport() != null && e.getReport().getTaxCalculationType() != null && e.getReport().getTaxCalculationType() == TAX_CALCULATION_INCLUSIVE) {
                if (e.getTax() != null) {
                    BigDecimal taxShareIncremented = e.getTax().getEffectiveRateAsBigDecimal().divide(HUNDRED, 4, RoundingMode.HALF_UP);
                    item.setBaseSubtotal(e.getBaseSubtotal().divide(ONE.add(taxShareIncremented), 2, RoundingMode.HALF_UP));
                } else {
                    item.setBaseSubtotal(e.getBaseSubtotal());
                }
            } else {
                item.setBaseSubtotal(e.getBaseSubtotal());
            }
            item.setAllocatedToPO(e.isAllocatedToPO());
            items.add(item);
        }
        return items.toArray(new ExpenseListItem[]{});
    }

    public BankTransferNumberData generateExpenseReportNumber() {
        BankTransferNumberData numberData = getExpenseNumberData();
        return expenseReportManager.generateNewNumber(numberData);
    }

    /*public NumberData reGenerateExpenseReportNumber() {
        NumberData numberData;
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = accountingManager.getExpenseLastIntNumber();
        numberData = resetNumberDataSettings(settings, intNumber);
        while (accountingManager.isExpenseNumberExists(numberData.getNumberString(), null)) {
            numberData = resetNumberDataSettings(settings, intNumber++);
        }
        return numberData;

    }*/

    /*private NumberData resetNumberDataSettings(EdsNumberingSettings settings, Integer intNumber) {
        NumberData numberData;
        if (settings != null && settings.getExpenseNumberingFormat() != null) {
            numberData = settings.parseNumberData(intNumber, settings.getExpenseNumberingFormat());
        } else {
            numberData = EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_EX_PREFIX);
        }
        return numberData;
    }*/

    /*public NumberData generateExpenseReportNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = accountingManager.getExpenseLastIntNumber();
        if (settings != null && settings.getExpenseNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getExpenseNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_EX_PREFIX);
        }
    }*/

    public ExpensePaymentData getPaymentData(Integer paymentID) {
        ExpensePaymentData paymentData = new ExpensePaymentData();

        //payment that was done on clicking PAY
        EdsExpensePayment payment = expensePaymentManager.get(paymentID);

        //Expense Claims related to that payment
        if (payment != null) {
            EdsExpenseReport expenseReport = payment.getExpenseReport();
            List<EdsExpensePayment> expensePayments = expensePaymentManager.getPayments(expenseReport);
            BigDecimal paymentTotalForEdit = BigDecimal.ZERO;
            for (EdsExpensePayment expensePayment : expensePayments) {
                if (!expensePayment.getObjectID().equals(paymentID)) {
                    paymentTotalForEdit = paymentTotalForEdit.add(expensePayment.getAmount());
                }
            }
            paymentData.setStatus(expenseReport.getOverallStatus() != null ? expenseReport.getOverallStatus().getCode() : null);
            paymentData.setPaymentAmount(payment.getAmount());
            paymentData.setPaymentAmountInExpenseCurrency(payment.getAmountInEntityCurrency());
            paymentData.setTotalPaymentAmountForEdit(paymentTotalForEdit);
            paymentData.setObjectID(payment.getObjectID());
            paymentData.setReportId(expenseReport.getObjectID());
            paymentData.setTitle(expenseReport.getTitle());
            paymentData.setDate(new DateNonConvertable(payment.getPaymentDate()));
            paymentData.setExpenseDate(expenseReport.getStartDate());
            paymentData.setNumberData(expenseReport.getNumber());
            paymentData.setTotalExpenseAmount(expenseReport.getTotal());
            paymentData.setTotalExpenseAmountinBase(expenseReport.getBaseTotal());
            paymentData.setReferenceNumber(payment.getReference());
            paymentData.setExchangeRate(payment.getExchangeRate());
            if (expenseReport.getCurrency() != null) {
                paymentData.setExpenseCurrency(expenseReport.getCurrency().createCurrencyItem());
            }
            if (payment.getCurrencyID() != null) {
                paymentData.setCurrency(currencyService.getCurrency(payment.getCurrencyID()));
            }
            paymentData.setBaseCurrency(currencyService.getBaseCurrency());
            if (expenseReport.getSupplier() != null) {
                paymentData.setSupplier(expenseReport.getSupplier().getAsSelectItem());
            }
            if (payment.getAccount() != null) {
                paymentData.setPaymentAccount(payment.getAccount().getAsSelectItem());
            }
            paymentData.setLayoutHTML(PathFinder.getLayoutHTML(EXPENSE_PAYMENT_FORM));

            EdsExpensePaymentTransaction expensePaymentTransaction = transactionManager.getTransactionByExpensePayment(payment);
            if (expensePaymentTransaction != null) {
                paymentData.setJournalId(expensePaymentTransaction.getJournalId());
            }
            if (payment.getBatchPayment() != null) {
                paymentData.setBatchPaymentID(payment.getBatchPayment().getObjectID());
            }
        }
        return paymentData;
    }

    @Override
    public void deleteExpensePayment(Integer objectId) {
        EdsExpensePayment payment = expensePaymentManager.get(objectId);

        EdsReference approvedStatus = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_APPROVED);
        EdsReference partiallyPaid = referenceManager.findReference(EXPENSE_STATUS, PARTIALLY_PAID);

        EdsExpenseReport expenseReport = payment.getExpenseReport();
        if (expenseReport.getPayments().size() > 1) {
            expenseReport.setEntityStatus(partiallyPaid);
        } else {
            expenseReport.setEntityStatus(approvedStatus);
        }

        if (payment.getBatchPayment() != null && payment.getBatchPayment().getObjectID() != null) {
            BigDecimal total = expensePaymentManager.getBatchPaymentItems(payment.getBatchPayment().getObjectID(), payment.getObjectID(), true);

            EdsBatchPayment batchPayment = payment.getBatchPayment();

            if (total.compareTo(BigDecimal.ZERO) > 0) {
                batchPayment.setTotalAmount(total);
            } else {
                batchPayment.setDeleted(true);
                deleteBatchPaymentNumberData(batchPayment);
            }
        }
        expenseReportManager.update(expenseReport);
        transactionManager.deleteExpensePaymentTransaction(payment);
        payment.setDeleted(true);
        expensePaymentManager.update(payment);
        addExpenseReportToSolr(expenseReport);
    }

    private void deleteBatchPaymentNumberData(EdsBatchPayment edsBatchPayment) {
        String entityCode = Constants.RECEIVABLE.equals(edsBatchPayment.getType()) ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT;
        EdsSharedNumber sharedNumber = sharedNumberManager.getByEntityID(edsBatchPayment.getObjectID(), entityCode);
        if (sharedNumber != null) {
            sharedNumber.setDeleted(true);
            sharedNumberManager.update(sharedNumber);
        }
    }

    @Override
    public void changeRelatedPurchaseOrder(Integer purchaseOrderID, Integer expenseID) {
        EdsPurchaseOrder purchaseOrder = purchaseOrderID != null ? quoteManager.getPurchaseOrderByID(purchaseOrderID) : null;
        EdsExpenseReport expenseReport = expenseReportManager.get(expenseID);
        if (expenseReport != null) {
            expenseReport.setPurchaseOrder(purchaseOrder);
            addExpenseReportToSolr(expenseReport);
            EdsExpenseTransaction transaction = transactionManager.getTransactionByExpense(expenseReport);
            if (transaction != null) {
                accountingService.createOrUpdateTransactionForExpense(expenseReport);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<String, BigDecimal> getExpenseChartData(ListingFilterParameter fp) {
        return expenseReportManager.getAllExpenseReportsChartData(fp);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<String, BigDecimal> getExpensesByOldNewEmployeesChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        fp.setType(1);//New Employees
        LinkedHashMap<String, BigDecimal> res = expenseReportManager.getAllExpenseReportsChartData(fp);

        for (BigDecimal total : res.values()) {
            if (resultMap.containsKey("New Employees")) {
                resultMap.put("New Employees", resultMap.get("New Employees").add(total));
            } else {
                resultMap.put("New Employees", total);
            }
        }

        fp.setType(2);//Old Employees
        res = expenseReportManager.getAllExpenseReportsChartData(fp);

        for (BigDecimal total : res.values()) {
            if (resultMap.containsKey("Old Employees")) {
                resultMap.put("Old Employees", resultMap.get("Old Employees").add(total));
            } else {
                resultMap.put("Old Employees", total);
            }
        }
        return resultMap;
    }

    @Override
    public ArrayList<ExpenseProjectItem> getExpenseProjects(DateNonConvertable startPeriod, DateNonConvertable endPeriod, ListingFilterParameter fp) {
        List<EdsProject> projects = expenseReportManager.getExpenseProjects(startPeriod, endPeriod, fp);

        ArrayList<ExpenseProjectItem> items = new ArrayList<>();
        for (EdsProject project : projects) {
            ExpenseProjectItem item = new ExpenseProjectItem();
            item.setId(project.getObjectID());
            item.setName(project.getName());
            item.setLastExpenseReportedDate(project.getLastExpenseClaimedDate());

            if (project.getClient() != null) {
                item.setCustomer(project.getClient().getAsSelectItem());
            }
            items.add(item);
        }

        return items;
    }

    @Override
    public SelectItem[] getEmployeeClients(Integer employeeId) {
        List<EdsCrmAccount> clients = expenseReportManager.getEmployeeClients(employeeId);
        ArrayList<SelectItem> items = new ArrayList<>();

        if (clients != null && !clients.isEmpty()) {
            for (EdsCrmAccount client : clients) {
                items.add(client.getAsSelectItem());
            }

            return items.toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    @Override
    public String getProjectBaseExpenseFormLayout() {
        return layoutManager.getLayoutHTML(LayoutRPC.PROJECT_BASE_EXPENSE_FORM);
    }

    private void updateProjectsExpenseDate(ExpenseReportsListItem report) {
        if (report.getProjectIds() != null) {
            for (
                    int i = 0;
                    i < report.getProjectIds().length;
                    i++) {
                if (report.getProjectIds()[i] != null) {
                    projectManager.get(report.getProjectIds()[i]).setLastExpenseClaimedDate(report.getStartDate().getNonConvertedDate());
                }
            }
        }
    }

    @Override
    public SelectItem getDefaultAccountForProjectBaseExpense() {
        String defaultAccountCode = genericSettingsManager.getValueByKey(GenericSettingsEnum.DEFAULT_ACCOUNT_FOR_PROJECT_BASE_EXPENSE);

        if (defaultAccountCode != null && !defaultAccountCode.isEmpty()) {
            EdsAccount account = accountingManager.getAccountByCode(defaultAccountCode);

            if (account != null) {
                return account.getAsSelectItem();
            }
        }
        return null;
    }

    @Override
    public Integer getEmployeesReportListCount(ListingFilterParameter filterParameter) {
        try {
            return expenseReportManager.getEmployeeReports(filterParameter, true).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public ApprovalListResult getExpenseApprovers(Integer userId, boolean fromSettings) {
        return allInOneService.getApprovers("EXPENSE_CLAIM", null, true, userId, fromSettings);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FileItem[] getAttachments(Integer expenseId) {
        List<FileResource> expAttachments = attachmentUtilsManager.getAttachments(F_EXP, expenseId, expenseId);
        FileItem[] fileItems = new FileItem[expAttachments.size()];
        for (int i = 0; i < expAttachments.size(); i++) {
            FileResource fileResource = expAttachments.get(i);
            FileItem fileItem = new FileItem();
            fileItem.setId(fileResource.getObjectId());
            fileItem.setAttachmentId(fileResource.getBodyId());
            fileItem.setFileName(fileResource.getEncodedName());
            fileItem.setDescription(fileResource.getDescription());
            fileItem.setSize(fileResource.getContentLength());
            fileItem.setUploadType(fileResource.getUploadType());
            fileItem.setDate(fileResource.getCreationDate());
            switch (fileResource.getUploadType()) {
                case GOOGLE -> fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
                case OFFICE_365, OFFICE_365_SHARE_POINT -> {
                    fileItem.setDocumentID(fileResource.getDocumentID());
                    fileItem.setDocumentOpenID(fileResource.getDocumentOpenID());
                    fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                }
                default -> fileItem.setAmazonLink(fileResource.getAmazonLink());
            }
            fileItems[i] = fileItem;
        }
        return fileItems;
    }

    @Override
    public FileResource[] getFileResources(Integer expenseID) {
        List<FileResource> expAttachments = attachmentUtilsManager.getAttachments(F_EXP, expenseID, expenseID);
        return expAttachments.toArray(new FileResource[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public EdsExpenseHistory getEventLastHistoryRecord(String statusCode, Integer reportId) {
        EdsReference reference = referenceManager.findReference(Constants.EXPENSE_STATUS, statusCode);
        return historyManager.getLastEventRecord(reference, reportId);
    }

    @Override
    public boolean saveExpenseCellValue(ExpenseReportsListItem rowValue, String columnCodeName) {
        EdsExpenseReport expenseReport = expenseReportManager.getExpenseReport(rowValue.getId());
        try {
            EdsInvoiceCustomFields expenseCF = expenseReport.getCustomFields();
            if (expenseCF == null) {
                expenseCF = new EdsInvoiceCustomFields();
                invoiceCFManager.create(expenseCF);
                expenseReport.setCustomFields(expenseCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(expenseCF, rowValue.getCustomFields(), columnCodeName);
            return true;
        } catch (Exception e) {
            log.error("Purchase Order List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public List<HistoryNote> loadExpenseNoteHistory(Integer objectId) {
        List<MyUpdateItem> historyItems = invoiceServiceLocal.getAllHistory(objectId, EXPENSE_REPORT);
        List<HistoryNote> result = new ArrayList<>(historyItems);
        HistoryListItem[] reportsHistory = getReportsHistory(objectId);
        if (reportsHistory != null) {
            result.addAll(Arrays.asList(reportsHistory));
        }

        return result;
    }

    public List<EdsExpenseReport> getPayslipRelatedExpenseClaims(Integer payslipId) {
        return expenseReportManager.getPayslipRelatedExpenseClaims(payslipId);
    }

    private List<EdsReference> filterTaxTreatment(List<EdsReference> list) {
        List<EdsReference> filteredItems = new ArrayList<>();
        boolean isReverseCharge = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ACCOUNTING_IS_REVERSE_CHARGE);
        String countryCode = userManager.getUser().getCompany().getCountry().getCode();

        for (EdsReference t : list) {

            if (Constants.GCC_VAT_REGISTERED.equals(t.getCode()) || Constants.GCC_NON_VAT_REGISTERED.equals(t.getCode()) || Constants.NON_GCC.equals(t.getCode())) {

                if (isReverseCharge) {
                    filteredItems.add(t);
                }
            } else if (Constants.VAT_REGISTERED_DESIGNATED_ZONE.equals(t.getCode()) || Constants.NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(t.getCode())) {

                if (Constants.AE.equals(countryCode)) {
                    filteredItems.add(t);
                }
            } else {
                filteredItems.add(t);
            }
        }

        return filteredItems;
    }

}

