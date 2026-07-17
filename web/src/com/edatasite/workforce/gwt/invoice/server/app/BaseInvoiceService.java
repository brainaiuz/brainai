/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/7 9:7:46                                                                                              *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsTaxComponent;
import com.edatasite.workforce.core.domain.EdsTaxGroupItem;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentInstruction;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.solr.component.PurchaseOrderSolrComponent;
import com.edatasite.workforce.core.solr.component.RequestForQuoteSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleQuoteSolrComponent;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxComponentData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.FixedAssetServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailNotificationSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceQuoteNoteManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.TaxGroupItemManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.TrashBinManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BillOfEntryItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BillOfEntryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExchangeRateHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemBatchManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PaymentInstructionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductSerialManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ShippingMethodManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileBodyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.RestHookManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfMerger;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.zatca.ZatcaService;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductsAccountsTaxes;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.InstructionData;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.google.gwt.user.server.rpc.security.ServerSecurityContext.getInstance;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.04.2009
 * Time: 15:45:34
 * To change this template use File | Settings | File Templates.
 */

@Transactional
@Service("baseInvoiceService")
public class BaseInvoiceService implements Constants, AccountingConstants {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

    private static final Logger log = LoggerFactory.getLogger(BaseInvoiceService.class);

    @Autowired
    protected VatManager vatManager;
    @Autowired
    protected TaxGroupItemManager taxGroupItemManager;
    @Autowired
    protected CompanyManager companyManager;
    @Autowired
    protected ClientManager clientManager;
    @Autowired
    protected ClientContactManager clientContactManager;
    @Autowired
    protected CrmContactManager crmContactManager;
    @Autowired
    protected CountryManager countryManager;
    @Autowired
    protected CurrencyManager currencyManager;
    @Autowired
    protected InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    protected RegionManager regionManager;
    @Autowired
    protected RoleManager roleManager;
    @Autowired
    protected ItemManager itemManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    protected WfmMessageSource wfmMessageSource;
    @Autowired
    protected UploadManager uploadManager;
    @Autowired
    protected MessageManager messageManager;
    @Autowired
    protected MessageCenterServiceLocal messageCenterServiceLocal;
    @Autowired
    protected InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    protected ExpenseService expenseService;
    @Autowired
    protected ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    protected ExpenseReportManager expenseReportManager;
    @Autowired
    protected ExpenseManager expenseManager;
    @Autowired
    protected AccountingManager accountingManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    @Qualifier("accountingService")
    protected AccountingServiceLocal accountingServiceLocal;
    @Autowired
    protected BankAccountManager bankAccountManager;
    @Autowired
    protected RelationManager relationManager;
    @Autowired
    protected FormPropertyManager formPropertyManager;
    @Autowired
    protected CustomFormItemManager customFormItemManager;
    @Autowired
    protected AttachmentManager attachmentManager;
    @Autowired
    protected TimeSheetManager timeSheetManager;
    @Autowired
    protected ProjectManager projectManager;
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    protected WarehouseManager warehouseManager;
    @Autowired
    protected ModuleManager moduleManager;
    @Autowired
    protected ShippingMethodManager shippingMethodManager;
    @Autowired
    protected InvoiceQuoteNoteManager invoiceQuoteNoteManager;
    @Autowired
    protected CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    protected CrmAccountManager crmAccountManager;
    @Autowired
    protected InvoiceTermsManager invoiceTermsManager;
    @Autowired
    protected PaymentInstructionManager paymentInstructionManager;
    @Autowired
    protected PickListManager pickListManager;
    @Autowired
    protected SolrManager solrManager;
    @Autowired
    protected UnitMeasurementManager unitMeasurementManager;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    protected LayoutManager layoutManager;
    @Autowired
    protected AddressManager addressManager;
    @Autowired
    protected FixedAssetManager fixedAssetManager;
    @Autowired
    protected ProductSerialManager productSerialManager;
    @Autowired
    protected ItemBatchManager itemBatchManager;
    @Autowired
    protected TransactionManager transactionManager;
    @Autowired
    protected TrashBinManager trashBinManager;
    @Autowired
    protected DepartmentManager departmentManager;
    @Autowired
    protected CommonServiceLocal commonServiceLocal;
    @Autowired
    protected FixedAssetServiceLocal fixedAssetService;
    @Autowired
    protected FileHeaderManager fileHeaderManager;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private ExchangeRateHistoryManager exchangeRateHistoryManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;
    @Autowired
    private FileBodyManager fileBodyManager;
    @Autowired
    private DocumentsServiceLocal documentsService;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    public ProductService productService;
    @Autowired
    protected BillOfEntryManager billOfEntryManager;
    @Autowired
    protected BillOfEntryItemManager billOfEntryItemManager;
    @Autowired
    protected RestHookManager restHookManager;
    @Autowired
    protected ApproverManager approverManager;
    @Autowired
    protected ZatcaService zatcaService;
    @Autowired
    protected SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private PurchaseOrderSolrComponent purchaseOrderSolrComponent;
    @Autowired
    private RequestForQuoteSolrComponent rfqSolrComponent;
    @Autowired
    protected StockValidationService stockValidationService;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void initInvoiceData(EdsBaseInvoice invoice, NewInvoice data) {
        EdsCurrency currency = null;
        if (data.getCurrencyID() != null) {
            currency = currencyManager.get(data.getCurrencyID());
        }
        EdsUser user = (EdsUser) getInstance().getUser();
        invoice.setBillAddressID(data.getBillAddressID());
        invoice.setMailAddressID(data.getMailAddressID());
        invoice.setCurrency(currency);
        invoice.setExchangeRate(data.getExchageRate());
        invoice.setObjectKey(data.getObjectKey());
        invoice.setNumber(data.getInvoiceNumber());
        invoice.setPoNumber(data.getPoNumber());
        invoice.setQuoteNumberCN(data.getQuoteNumberCN());
        invoice.setInvoiceDate(data.getInvoiceDate().getNonConvertedDate());
        invoice.setDueDate(data.getDueDate().getNonConvertedDate());
        invoice.setReference(data.getReference());

        if (data.getAccountsReceivablePayable() != null) {
            invoice.setReceivablePayable(accountingManager.get(data.getAccountsReceivablePayable().getId()));
        } else {
            invoice.setReceivablePayable(null);
        }
        invoice.setIntroduction(data.getIntroduction());
        invoice.setType(data.getType());
        invoice.setSubtotal(data.getSubtotal());
        invoice.setTotal(data.getTotal());
        invoice.setTotalInInvoiceCurrency(data.getTotalInInvoiceCurrency());
        invoice.setComissionAmount(data.getComissionAmount());
        invoice.setTotalTaxes(data.getTotalTaxes());
        invoice.setPaymentInstruction(data.getPaymentInstruction());


        // This is for Progressive Sales Invoice
        Integer quoteId = data.getProgressiveInvoiceQuoteId();
        if (data.isProgressInvoicing() && quoteId != null) {
            EdsQuote quote = quoteManager.get(quoteId);
            BigDecimal convertedInvoicesAmount = invoiceManager.getConvertedInvoiceAmount(quoteId, invoice.getObjectID());
            convertedInvoicesAmount = convertedInvoicesAmount.add(invoice.getTotalInInvoiceCurrency());
            BigDecimal convertedBaseAmount = invoiceManager.getConvertedBaseAmount(quoteId, invoice.getObjectID());
            convertedBaseAmount = convertedBaseAmount.add(invoice.getTotal());
            BigDecimal totalInInvoiceCurrency = convertedInvoicesAmount;
            BigDecimal totalInBaseCurrency = convertedBaseAmount;
            if (invoice.getObjectID()==null) {
                // Total with current + previous invoices amount
                totalInInvoiceCurrency = convertedInvoicesAmount.add(data.getTotalInInvoiceCurrency());
                totalInBaseCurrency= convertedBaseAmount.add(data.getTotal());
            }

            // If the totals are exactly equal, skip adjustment logic.
            if (quote.getTotalInInvoiceCurrency().compareTo(totalInInvoiceCurrency) == 0) {
                // Totals match exactly—skip adjustment.
            } else {
                // Calculate the difference between the quote total and the computed total.
                BigDecimal differenceInvoiceAmount = quote.getTotalInInvoiceCurrency().subtract(totalInInvoiceCurrency);
                BigDecimal differenceBaseAmount = quote.getTotal().subtract(totalInBaseCurrency);

                // When the computed total is less than the quote (difference is positive),
                // we need to add the remaining small amount to the new invoice.
                if (differenceInvoiceAmount.compareTo(BigDecimal.ZERO) > 0 && differenceInvoiceAmount.compareTo(new BigDecimal("0.05")) < 0) {
                    adjustProgressiveInvoiceingRemainings(invoice, data, differenceInvoiceAmount, differenceBaseAmount,true);
                }
                // When the computed total is greater than the quote (difference is negative),
                // we need to subtract the tiny amount from the new invoice.
                else if (differenceInvoiceAmount.compareTo(BigDecimal.ZERO) < 0 && differenceInvoiceAmount.abs().compareTo(new BigDecimal("0.05")) < 0) {
                    // Pass the absolute value of the difference so that we subtract a positive amount.
                    adjustProgressiveInvoiceingRemainings(invoice, data, differenceInvoiceAmount.abs(), differenceBaseAmount.abs(), false);
                }
            }
        }

        if (!DRAFT.equals(data.getStatusCode()) && (invoice instanceof EdsSaleInvoice || invoice instanceof EdsPurchaseInvoice) && invoice.getDueDate().before(new Date())) {
            invoice.setStatus(getInvoiceStatus(OVER_DUE));
        } else {
            invoice.setStatus(getInvoiceStatus(data.getStatusCode()));
        }
        invoice.setSaasuGUID(data.getSaasuGUID());
        invoice.setSasuuLastUpdatedTime(data.getSaasuLastUpdateDate());
        invoice.setSaasuLastUpdatedUid(data.getSaasuLastUpdatedUid());
        invoice.setDiscountType(data.getDiscountType());
        invoice.setDiscountAmount(data.getDiscountAmount());

        if (data.getPlaceOfSupply() != null
                && data.getPlaceOfSupply().getId() != null
                && invoice.getClientOrSupplier().getTaxTreatment() != null) {
            SelectItem placeOfSupply = data.getPlaceOfSupply();
            invoice.setPlaceOfSupplyId(placeOfSupply.getId());
            invoice.setPlaceOfSupplyCategory(StringUtils.isNotBlank(placeOfSupply.getCategory()) ? placeOfSupply.getCategory() : PLACEOFSUPPLY_CATEGORY.REGION);
        }
        if (data.getPdfTemplateID() != null) {
            EdsCompanyPdfTemplate pdfTemplate = companyPdfTemplateManager.get(data.getPdfTemplateID());
            invoice.setPdfTemplate(pdfTemplate);
        }
        if (user == null && data.getUserID() != null) {
            user = userManager.get(data.getUserID());
        }
        if (data.isRegisteredInterCompanyTransaction() != null) {
            invoice.setRegisteredInterCompanyTransaction(data.isRegisteredInterCompanyTransaction());
        }

        if (invoice.getCreator() == null) {
            if (data.isInterCompanySales()) {
                EdsEmployee employee = userManager.getAdmin(user.getCompany().getObjectID());
                if (employee != null) {
                    invoice.setCreator(employee);
                } else {
                    invoice.setCreator(user);
                }
            } else {
                invoice.setCreator(user);
            }
        }

        if (invoice.getCreator() == null) {
            invoice.setCreator(user);
        }
        invoice.setUpdater(user);

        if (invoice.getCreationDate() == null) {
            invoice.setCreationDate(new Date());
        }

        if (user != null && data.getCurrencyID() != null && (data.getID() == null || data.getID() == 0)) {
            exchangeRateHistoryManager.registerExchangeRateHistory(data.getExchageRate(), currency);
        }
    }

    private void adjustProgressiveInvoiceingRemainings(EdsBaseInvoice invoice, NewInvoice data, BigDecimal invoiceDiffirence, BigDecimal baseDiffirence, boolean positive) {
        log.info("Adjusting progressive invoicing remainings");
        if (positive) {
            log.info("Adjusting positive progressive invoicing remainings");
            invoice.setTotalInInvoiceCurrency(data.getTotalInInvoiceCurrency().add(invoiceDiffirence));
            invoice.setTotal(data.getTotal().add(baseDiffirence));
        } else {
            log.info("Adjusting negative invoicing remainings");
            invoice.setTotalInInvoiceCurrency(data.getTotalInInvoiceCurrency().subtract(invoiceDiffirence));
            invoice.setTotal(data.getTotal().subtract(baseDiffirence));
        }
    }

    public void initInvoiceItemData(EdsBaseInvoiceItem invoiceItem, NewInvoiceItem newInvoiceItem) {
        EdsItem item = null;
        if (newInvoiceItem.getItemID() != null && newInvoiceItem.getItemID() != 0) {

            item = itemManager.get(newInvoiceItem.getItemID());

            invoiceItem.setItem(item);
            invoiceItem.setItemName(null);
            if (item != null && item.getCategory() != null) {
                invoiceItem.setCategoryName(item.getCategory().getName());
            }
        } else if (newInvoiceItem.getItemName() != null) {
            // This fixes the "oneOfItem" issue during quote to invoice conversion when itemID becomes null
            item = tryRecoverItemFromName(newInvoiceItem.getItemName());

            if (item != null) {
                invoiceItem.setItem(item);
                invoiceItem.setItemName(null);
                if (item.getCategory() != null) {
                    invoiceItem.setCategoryName(item.getCategory().getName());
                }
            } else {
                invoiceItem.setItemName(newInvoiceItem.getItemName());
                invoiceItem.setItem(null);
            }
        } else if (newInvoiceItem.getProjectBasedInvoiceDesc() != null) {
            ((EdsInvoiceItem) invoiceItem).setProjectBasedInvoiceDescription(newInvoiceItem.getProjectBasedInvoiceDesc());
        }

        invoiceItem.setDescription(newInvoiceItem.getDescription());
        invoiceItem.setQty(newInvoiceItem.getQuantity());
        if (newInvoiceItem.getUuid() != null && !newInvoiceItem.getUuid().isEmpty()) {
            invoiceItem.setUuid(newInvoiceItem.getUuid());
        }
        if (newInvoiceItem.getMeasurement() != null && newInvoiceItem.getMeasurement().getId() != null) {
            invoiceItem.setUnitMeasurement(unitMeasurementManager.get(newInvoiceItem.getMeasurement().getId()));
        }
        invoiceItem.setUnitPrice(newInvoiceItem.getUnitPrice());
        invoiceItem.setPriceLevelAmount(newInvoiceItem.getPriceLevelAmount());
        invoiceItem.setComission(newInvoiceItem.getComission());
        invoiceItem.setDiscount(newInvoiceItem.getDiscountPercent());
        invoiceItem.setDiscountAmount(newInvoiceItem.getDiscountAmount());
        if (newInvoiceItem.getItemDiscountID() != null) {
            invoiceItem.setItemDiscount(discountManager.get(newInvoiceItem.getItemDiscountID()));
            invoiceItem.setDiscountItemStaticType(null);
        } else if (newInvoiceItem.getDiscountItemStaticType() != null) {
            invoiceItem.setDiscountItemStaticType(newInvoiceItem.getDiscountItemStaticType());
            invoiceItem.setItemDiscount(null);
        }
        invoiceItem.setDoubleDiscount(newInvoiceItem.getDoubleDiscountPercent());
        invoiceItem.setDoubleDiscountAmount(newInvoiceItem.getDoubleDiscountAmount());
        if (newInvoiceItem.getItemDoubleDiscountID() != null) {
            invoiceItem.setItemDoubleDiscount(discountManager.get(newInvoiceItem.getItemDoubleDiscountID()));
        }

        invoiceItem.setDepartment((newInvoiceItem.getDepartmentItem() != null && newInvoiceItem.getDepartmentItem().getId() != null) ? departmentManager.get(newInvoiceItem.getDepartmentItem().getId()) : null);

        if (newInvoiceItem.getAccountID() != null && newInvoiceItem.getAccountID() != 0) {
            invoiceItem.setAccount(accountingManager.get(newInvoiceItem.getAccountID()));
        } else if (item != null && item.getAccount() != null) {
            invoiceItem.setAccount(item.getAccount());
        }
        invoiceItem.setVat(newInvoiceItem.getTaxItem() != null && newInvoiceItem.getTaxItem().getId() != null ? vatManager.get(newInvoiceItem.getTaxItem().getId()) : null);
        if (newInvoiceItem.getDoubleTaxItem() != null && newInvoiceItem.getDoubleTaxItem().getId() != null) {
            invoiceItem.setDoubleVat(vatManager.get(newInvoiceItem.getDoubleTaxItem().getId()));
        }
        invoiceItem.setNet(newInvoiceItem.getNet());
        invoiceItem.setAmmount(newInvoiceItem.getTotalAmount());
        invoiceItem.setTaxAmount(newInvoiceItem.getTaxAmount());
        invoiceItem.setDoubleTaxAmount(newInvoiceItem.getDoubleTaxAmount());

        if (newInvoiceItem.getWarehouse() != null && newInvoiceItem.getWarehouse().getId() != null) {
            invoiceItem.setWarehouse(warehouseManager.get(newInvoiceItem.getWarehouse().getId()));
        } else if (item != null && item.getDefaultWarehouse() != null) {
            invoiceItem.setWarehouse(item.getDefaultWarehouse());
        }

        if (newInvoiceItem.getProject() != null && newInvoiceItem.getProject().getId() != null) {
            invoiceItem.setProject(projectManager.get(newInvoiceItem.getProject().getId()));
        }

        invoiceItem.setReceiveType(newInvoiceItem.getReceiveType());

        if (newInvoiceItem.getReceive() != null && newInvoiceItem.getReceive().compareTo(BigDecimal.ZERO) > 0) {
            if (invoiceItem.getReceiveType() == null) {
                invoiceItem.setReceiveType(ReceiveTypeEnum.RECEIVE_BY_QTY);
            }

            if (ReceiveTypeEnum.RECEIVE_BY_QTY == invoiceItem.getReceiveType()) {
                invoiceItem.setReceivedQty(newInvoiceItem.getReceivedQty().add(newInvoiceItem.getReceive()));

                if (invoiceItem.getReceivedQty().compareTo(invoiceItem.getQty()) >= 0) {
                    invoiceItem.setQty(invoiceItem.getReceivedQty());
                }
            } else {
                invoiceItem.setReceivedAmount(newInvoiceItem.getReceivedAmount().add(newInvoiceItem.getReceive()));
            }

            invoiceItem.setReceive(newInvoiceItem.getReceive());
        } else {
            invoiceItem.setReceive(BigDecimal.ZERO);
        }

        if (newInvoiceItem.getConvertedQty() != null) {
            invoiceItem.setConvertedQty(newInvoiceItem.getConvertedQty());
        } else {
            invoiceItem.setConvertedQty(BigDecimal.ZERO);
        }

        if (newInvoiceItem.getConvertedAmount() != null) {
            invoiceItem.setConvertedAmount(newInvoiceItem.getConvertedAmount());
        } else {
            invoiceItem.setConvertedAmount(BigDecimal.ZERO);
        }

    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsItem tryRecoverItemFromName(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return null;
        }

        if (itemName.contains(" -> ")) {
            String[] parts = itemName.split(" -> ", 2);
            if (parts.length >= 2) {
                String productNumber = parts[0].trim();
                String productName = parts[1].trim();

                // Try to find the item by product number using the ItemManager method
                try {
                    EdsItem item = itemManager.getItemByNumber(productNumber, false);
                    if (item != null) {
                        // Verify name matches (or is close enough) to avoid false positives
                        if (item.getName() != null &&
                            (item.getName().equals(productName) ||
                             item.getName().equalsIgnoreCase(productName) ||
                             item.getName().contains(productName) ||
                             productName.contains(item.getName()))) {
                            log.info("Recovered item ID {} from itemName '{}' using product_number '{}' and name match",
                                    item.getObjectID(), itemName, productNumber);
                            return item;
                        }
                        // If only one item found with this product number, use it even if name doesn't match exactly
                        // (name might have been corrupted with special characters)
                        log.info("Recovered item ID {} from itemName '{}' using product_number '{}' (name verification skipped)",
                                item.getObjectID(), itemName, productNumber);
                        return item;
                    }
                } catch (Exception e) {
                    log.warn("Error trying to recover item from itemName '{}': {}", itemName, e.getMessage());
                }
            }
        }

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("itemName", itemName);
            String jpql = "SELECT i FROM EdsItem i WHERE (i.deleted is null or i.deleted<>true) AND i.name = :itemName";
            List<EdsItem> items = itemManager.findByNamedParams(jpql, params);

            if (items != null && !items.isEmpty()) {
                if (items.size() == 1) {
                    EdsItem item = items.get(0);
                    log.info("Recovered item ID {} from itemName '{}' using exact name match",
                            item.getObjectID(), itemName);
                    return item;
                } else {
                    log.warn("Multiple items found with name '{}', using first match", itemName);
                    return items.get(0);
                }
            }
        } catch (Exception e) {
            log.warn("Error trying to find item by name '{}': {}", itemName, e.getMessage());
        }

        log.debug("Could not recover item from itemName '{}'", itemName);
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsReference getInvoiceStatus(String invoiceStatus) {
        return referenceManager.findReference(INVOICE_STATUS, invoiceStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaxList getCompanyTaxList() {
        List<EdsVat> vatList = accountingServiceLocal.companyVatList(null, null);

        return createCompanyTaxList(vatList);
    }

    protected TaxList createCompanyTaxList(List<EdsVat> vatList) {
        return accountingServiceLocal.createCompanyTaxList(vatList);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaxData getTax(Integer objectId) {
        TaxData data = new TaxData();
        data.setVatReturnEnabled(isVatReturnEnabled());
        List<EdsReference> faiVatReferences = referenceManager.listReferences(EdsVat.FAI_VAT);
        data.setFaiVats(faiVatReferences.stream().map(EdsReference::getRPC).toArray(ReferenceItem[]::new));
        data.setFaiCategoryOptions(referenceManager.listReferences(EdsVat.FAI_CATEGORY).stream().map(EdsReference::getAsSelectItem).toArray(SelectItem[]::new));

        List<EdsReference> faiPurchaseVatReferences = referenceManager.listReferences(EdsVat.FAI_PURCHASE_VAT);
        data.setFaiPurchaseVats(faiPurchaseVatReferences.stream().map(EdsReference::getRPC).toArray(ReferenceItem[]::new));
        data.setFaiPurchaseCategoryOptions(referenceManager.listReferences(EdsVat.FAI_PURCHASE_CATEGORY).stream().map(EdsReference::getAsSelectItem).toArray(SelectItem[]::new));

        if (objectId != null) {
            EdsVat vat = vatManager.get(objectId);
            data.setObjectId(vat.getObjectID());
            data.setTaxName(vat.getName());
            data.setTaxRate(vat.getTaxRateAsBigDecimal());
            data.setTaxTypeId(vat.getTaxType());
            data.setGroupTax(vat.getGroupTax());
            data.setKey(vat.getKey());
            data.setPermissionType(vat.getPermissionType());
            data.setSelectedByDefault(vat.isSelectedByTaxDefault());
            data.setFaiId(vat.getFaiId());
            data.setFaiPurchaseId(vat.getFaiPurchaseId());
            data.setStatus(vat.getStatus());
            data.setFaiCategoryIds(vat.getFaiCategorieIds());
            data.setFaiPurchaseCategoryIds(vat.getFaiPurchaseCategoryIds());

            if (data.isGroupTax()) {
                LinkedList<TaxItem> groupItemList = new LinkedList<>();
                List<EdsTaxGroupItem> edsTaxGroupItemList = taxGroupItemManager.getGroupItems(objectId);
                for (EdsTaxGroupItem edsTaxGroupItem : edsTaxGroupItemList) {
                    groupItemList.add(edsTaxGroupItem.getItem().createTaxItem());
                }
                data.setGroupItems(groupItemList);
            } else {
                List<EdsTaxComponent> components = vatManager.getTaxComponents(objectId);
                if (components != null && !components.isEmpty()) {
                    TaxComponentData[] comps = new TaxComponentData[components.size()];
                    int i = 0;
                    for (EdsTaxComponent tc : components) {

                        comps[i] = new TaxComponentData();
                        comps[i].setName(tc.getName());
                        comps[i].setCompound(tc.getCompound() != null ? tc.getCompound() : false);
                        comps[i].setRate(tc.getRate());
                        if (tc.getAccount() != null) {
                            comps[i].setAccount(tc.getAccount().getAsSelectItem());
                        }
                        i++;
                    }
                    data.setComponents(comps);
                }
            }
        } else {
            data.setStatus(true);
            return data;
        }
        return data;
    }

    protected boolean isVatReturnEnabled() {
        return genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.VAT_RETURN_ENABLE);
    }

    public boolean deleteTax(Integer objectId) {
        EdsVat vat = vatManager.get(objectId);
        boolean used = false;
        if (vat != null) {
            List<Integer> invoiceIds = invoiceManager.getInvoicesByVat(vat.getObjectID());
            if (invoiceIds != null && !invoiceIds.isEmpty()) {
                used = true;
            }
            List<Integer> quoteIds = quoteManager.getQuotesByVat(vat.getObjectID());
            if (quoteIds != null && !quoteIds.isEmpty()) {
                used = true;
            }
            List<Integer> expenseIds = quoteManager.getExpensesByVat(vat.getObjectID());
            if (expenseIds != null && !expenseIds.isEmpty()) {
                used = true;
            }

            List<Integer> bankTransafersIds = quoteManager.getBankTransafersByVat(vat.getObjectID());
            if (bankTransafersIds != null && !bankTransafersIds.isEmpty()) {
                used = true;
            }
        }
        if (!used) {
            vat.setOutdated(true);
            vatManager.update(vat);
        }
        return !used;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TypeItem[] getClients(String searchKey) {
        List<EdsCrmAccount> clients = clientManager.getClientsForInvoice(searchKey);
        clients = ListUtils.getSublist(clients, 0, DEFAULT_LIMIT);
        TypeItem[] result = new TypeItem[clients.size()];

        Integer baseCurrencyID = currencyService.getBaseCurrency().getId();
        int i = 0;
        for (EdsCrmAccount client : clients) {
            Integer currencyID = client.getCurrency() != null ? client.getCurrency().getObjectID() : baseCurrencyID;
            result[i] = new TypeItem(client.getObjectID(), client.getName(), client.getNumber(), currencyID);
            if (client.getPaymentMethod() != null) {
                result[i].setPaymentType(commonLocalizer.localize(client.getPaymentMethod().getCode(), client.getPaymentMethod().getName()));
            }
            i++;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TypeItem[] getSuppliers(String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setAccountType(EdsCrmAccount.SUPPLIER);
        filterParametrs.setSearchKey(searchKey);
        filterParametrs.setLookUp(true);
        List<EdsCrmAccount> suppliers = crmAccountManager.getList(filterParametrs, null);
        suppliers = ListUtils.getSublist(suppliers, 0, DEFAULT_LIMIT);

        List<TypeItem> items = new ArrayList<>();

        Integer baseCurrencyID = currencyService.getBaseCurrency().getId();
        for (EdsCrmAccount supplier : suppliers) {
            Integer currencyID = supplier.getCurrency() != null ? supplier.getCurrency().getObjectID() : baseCurrencyID;
            if (supplier.getPaymentMethod() != null) {
                TypeItem item = new TypeItem(supplier.getObjectID(), supplier.getName(), supplier.getNumber(), currencyID, supplier.getPaymentMethod().getObjectID());
                item.setPaymentType(commonLocalizer.localize(supplier.getPaymentMethod().getCode(), supplier.getPaymentMethod().getName()));
                items.add(item);
            } else {
                items.add(new TypeItem(supplier.getObjectID(), supplier.getName(), supplier.getNumber(), currencyID));
            }
        }

        items.sort(Comparator.comparing(SelectItem::getName));

        return items.toArray(new TypeItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        return commonService.getCountries();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAllStates() {
        List<EdsRegion> stateList = regionManager.list();
        SelectItem[] result = new SelectItem[stateList.size()];
        int i = 0;
        for (EdsRegion state : stateList) {
            result[i] = new SelectItem(state.getObjectID(), state.getName(), state.getCountry().getObjectID().toString());
            i++;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SettingsData getInvoiceSettings() {
        EdsCompany company = invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        SettingsData result = new SettingsData();
        result.setCountry(getCountries());
        result.setState(getAllStates());
        result.setCompanyID(company.getObjectID());
        if (invSettings != null) {
            result.setConvertInvoiceBtnShow(invSettings.isConvertInvoiceBtnShow());
            result.setIsShowPurchaseInvoiceAndPICreditNoteNumbering(invSettings.getIsPurchaseInvoiceNumberingShow() == null ? false : invSettings.getIsPurchaseInvoiceNumberingShow());
            result.setObjectID(invSettings.getObjectID());
            result.setPaymentDue(invSettings.getPaymentDue());
            result.setSalesQuoteDue(invSettings.getSalesQuoteDue());
            result.setLocaleID(company.getLocale() != null && localeManager.getLocaleBylanguageCode(company.getLocale()) != null ? localeManager.getLocaleBylanguageCode(company.getLocale()).getObjectID() : null);
            result.setBankID(invSettings.getBankAccountId());
            if (invSettings.getDefaultPaymentAccountId() != null) {
                EdsAccount account = accountingManager.get(invSettings.getDefaultPaymentAccountId());
                result.setDefaultPaymentAccount(account != null ? account.getAsSelectItem() : null);
            }
            result.setTaxCalculationType(invSettings.getTaxCalculationType());
            result.setSalesQuoteProgressInvoicing(invSettings.isSalesQuoteProgressInvoicing());
            result.setExpandProductGroup(invSettings.isExpandProductGroup());
            result.setSalesInvoiceInvoiceType(invSettings.getInvoiceType());
            result.setEnableInvoiceCustomTypes(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.INVOICE_CUSTOM_TYPE_ENABLED));
            result.setQuoteConvertToInvoiceCustomType(invSettings.getQuoteConvertToInvoiceCustomType());
            result.setDueDateType(invSettings.getDueDateType());
            result.setConvertPurchaseInvoiceDateType(invSettings.getConvertPurchaseInvoiceDateType());

            result.setInvoiceNumberingFormat(invSettings.getInvoiceNumberingFormat());
            result.setSalesQuoteNumberingFormat(invSettings.getSalesQuoteNumberingFormat());
            result.setSalesOrderNumberingFormat(invSettings.getSalesOrderNumberingFormat());
            result.setPurchaseOrderNumberingFormat(invSettings.getPurchaseOrderNumberingFormat());
            result.setPiNumberingFormat(invSettings.getPiNumberingFormat());
            result.setCnNumberingFormat(invSettings.getCnNumberingFormat());
            result.setNumberingRestartEnabled(invSettings.isNumberingRestartEnabled());
            result.setNumberingRestartDate(invSettings.getNumberingRestartDate());
            result.setNumberingRestartMonth(invSettings.getNumberingRestartMonth());

            result.setPdfNamingFormat(invSettings.getPdfNamingFormat());
            result.setPdfNamingPrefix(invSettings.getPdfNamingPrefix());
            result.setSalesReceiptPdfNamingFormat(invSettings.getSalesReceiptPdfNamingFormat());
            result.setSalesReceiptPdfNamingPrefix(invSettings.getSalesReceiptPdfNamingPrefix());
            result.setSalesOrderPdfNamingFormat(invSettings.getSalesOrderPdfNamingFormat());
            result.setSalesOrderPdfNamingPrefix(invSettings.getSalesOrderPdfNamingPrefix());
            result.setPurchaseOrderPdfNamingFormat(invSettings.getPurchaseOrderPdfNamingFormat());
            result.setPurchaseOrderPdfNamingPrefix(invSettings.getPurchaseOrderPdfNamingPrefix());
            result.setCustomerPdfNamingFormat(invSettings.getCustomerPdfNamingFormat());
            result.setCustomerPdfNamingPrefix(invSettings.getCustomerPdfNamingPrefix());
            result.setSupplierPdfNamingFormat(invSettings.getSupplierPdfNamingFormat());
            result.setSupplierPdfNamingPrefix(invSettings.getSupplierPdfNamingPrefix());

            result.setInstructions(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.SALES_INVOICE_PAYMENT_INSTRUCTION));
            result.setQuoteTermsConditions(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.SALES_QUOTE_TERMS_CONDITIONS));
            result.setSaleOrderTermsConditions(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.SALES_ORDER_PAYMENT_INSTRUCTION));
            result.setPurchaseOrderTermsAndConditions(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.PURCHASE_ORDER_TERMS_CONDITIONS));
            result.setPurchaseInvoicePaymentInstructions(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.PURCHASE_INVOICE_PAYMENT_INSTRUCTION));
            result.setRfqInstructions(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.REQUEST_FOR_QUOTE_INSTRUCTION));
            result.setSaleInvoiceIntroduction(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.SALES_INVOICE_INTRODUCTION));
            result.setSaleQuoteIntroduction(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.SALES_QUOTE_INTRODUCTION));
            result.setSaleOrderIntroduction(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.SALES_ORDER_INTRODUCTION));
            result.setRequestForQuoteIntroduction(paymentInstructionManager.getInstructionsRPC(EdsPaymentInstruction.REQUEST_FOR_QUOTE_INTRODUCTION));
        }
        if (result.getSalesQuoteDue() == null) {
            result.setSalesQuoteDue(30);
        }

        result.setBankAccounts(accountingServiceLocal.getBankAccountItemsForReference());
        List<EdsLocale> edsLocaleList = localeManager.list();
        SelectItem[] companyPdfLocaleItems = new SelectItem[edsLocaleList.size()];
        int i = 0;
        for (EdsLocale edsLocale : edsLocaleList) {
            companyPdfLocaleItems[i++] = new SelectItem(edsLocale.getId(), edsLocale.getCountry());
        }
        result.setCompanyPdfLocaleItems(companyPdfLocaleItems);
        result.setInvoiceCustomTypes(commonServiceLocal.convertReference2SelectItem(INVOICE_CUSTOM_TYPE, false, null));
        result.setSalesQuoteTermCopyToSalesInvoice(invSettings.isSalesQuoteTermCopyToSalesInvoice());
        result.setSalesQuoteTermCopyToSalesOrder(invSettings.isSalesQuoteTermCopyToSalesOrder());
        result.setSalesOrderTermCopyToSalesInvoice(invSettings.isSalesOrderTermCopyToSalesInvoice());
        result.setCopySQIntroduction(invSettings.getCopySQIntroduction());
        result.setCopySOIntroduction(invSettings.getCopySOIntroduction());
        return result;
    }

    public Integer saveCompanyInvoiceSettings(SettingsData data) {
        EdsCompany company = invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invoiceSettings = null;
        if (data.getObjectID() != null) {
            invoiceSettings = invoicingSettingsManager.get(data.getObjectID());
        } else if (invoicingSettingsManager.getInvoiceSettings(company) != null) {
            invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
        }

        if (invoiceSettings == null) {
            invoiceSettings = new EdsInvoicingSettings();
        }
//        invoiceSettings.setCompany(company);
        invoiceSettings.setPaymentDue(data.getPaymentDue());
        invoiceSettings.setSalesQuoteDue(data.getSalesQuoteDue());
        invoiceSettings.setPurchaseOrderDue(data.getPurchaseOrderDue());
        invoiceSettings.setConvertInvoiceBtnShow(data.isConvertInvoiceBtnShow());

        if (data.getIsShowPurchaseInvoiceAndPICreditNoteNumbering() != null) {
            invoiceSettings.setIsPurchaseInvoiceNumberingShow(data.getIsShowPurchaseInvoiceAndPICreditNoteNumbering());
        }
        invoiceSettings.setCustomerPdfNamingFormat(data.getCustomerPdfNamingFormat());
        invoiceSettings.setCustomerPdfNamingPrefix(data.getCustomerPdfNamingPrefix());
        invoiceSettings.setSupplierPdfNamingFormat(data.getSupplierPdfNamingFormat());
        invoiceSettings.setSupplierPdfNamingPrefix(data.getSupplierPdfNamingPrefix());

        //Progress Invoicing in add Sales Quote
        invoiceSettings.setSalesQuoteProgressInvoicing(data.getSalesQuoteProgressInvoicing());
        invoiceSettings.setExpandProductGroup(data.isExpandProductGroup());

        //InvoiceType in add Sales Invoice
        invoiceSettings.setInvoiceType(data.getSalesInvoiceInvoiceType());
        invoiceSettings.setDueDateType(data.getDueDateType());
        invoiceSettings.setConvertPurchaseInvoiceDateType(data.getConvertPurchaseInvoiceDateType());

        //Company Locale
        if (data.getLocaleID() != null && localeManager.get(data.getLocaleID()) != null) {
            company.setLocale(localeManager.get(data.getLocaleID()).getLanguageCode());
            companyManager.update(company);
        } else {
            company.setLocale(null);
            companyManager.update(company);
        }

        //Tax Calculation Type
        invoiceSettings.setTaxCalculationType(data.getTaxCalculationType());

        //Bank Info
        invoiceSettings.setBankAccountId(data.getBankID());
        if (data.getDefaultPaymentAccount() != null) {
            invoiceSettings.setDefaultPaymentAccountId(data.getDefaultPaymentAccount().getId());
        }

        //if invoice custom field enable set quote converting to invoice custom type
        invoiceSettings.setQuoteConvertToInvoiceCustomType(data.getQuoteConvertToInvoiceCustomType());

        invoiceSettings.setPdfNamingFormat(data.getPdfNamingFormat());
        invoiceSettings.setPdfNamingPrefix(data.getPdfNamingPrefix());

        invoiceSettings.setSalesReceiptPdfNamingFormat(data.getSalesReceiptPdfNamingFormat());
        invoiceSettings.setSalesReceiptPdfNamingPrefix(data.getSalesReceiptPdfNamingPrefix());

        invoiceSettings.setSalesOrderPdfNamingFormat(data.getSalesOrderPdfNamingFormat());
        invoiceSettings.setSalesOrderPdfNamingPrefix(data.getSalesOrderPdfNamingPrefix());

        invoiceSettings.setPurchaseOrderPdfNamingFormat(data.getPurchaseOrderPdfNamingFormat());
        invoiceSettings.setPurchaseOrderPdfNamingPrefix(data.getPurchaseOrderPdfNamingPrefix());

        updateInstructionsAndTermsConditions(data.getInstructions(), EdsPaymentInstruction.SALES_INVOICE_PAYMENT_INSTRUCTION);
        updateInstructionsAndTermsConditions(data.getQuoteTermsConditions(), EdsPaymentInstruction.SALES_QUOTE_TERMS_CONDITIONS);
        updateInstructionsAndTermsConditions(data.getSaleOrderTermsConditions(), EdsPaymentInstruction.SALES_ORDER_PAYMENT_INSTRUCTION);
        updateInstructionsAndTermsConditions(data.getPurchaseOrderTermsAndConditions(), EdsPaymentInstruction.PURCHASE_ORDER_TERMS_CONDITIONS);
        updateInstructionsAndTermsConditions(data.getPurchaseInvoicePaymentInstructions(), EdsPaymentInstruction.PURCHASE_INVOICE_PAYMENT_INSTRUCTION);
        updateInstructionsAndTermsConditions(data.getRfqInstructions(), EdsPaymentInstruction.REQUEST_FOR_QUOTE_INSTRUCTION);
        updateInstructionsAndTermsConditions(data.getSaleInvoiceIntroduction(), EdsPaymentInstruction.SALES_INVOICE_INTRODUCTION);
        updateInstructionsAndTermsConditions(data.getSaleQuoteIntroduction(), EdsPaymentInstruction.SALES_QUOTE_INTRODUCTION);
        updateInstructionsAndTermsConditions(data.getSaleOrderIntroduction(), EdsPaymentInstruction.SALES_ORDER_INTRODUCTION);
        updateInstructionsAndTermsConditions(data.getRequestForQuoteIntroduction(), EdsPaymentInstruction.REQUEST_FOR_QUOTE_INTRODUCTION);
        invoiceSettings.setSalesQuoteTermCopyToSalesInvoice(data.isSalesQuoteTermCopyToSalesInvoice());
        invoiceSettings.setSalesQuoteTermCopyToSalesOrder(data.isSalesQuoteTermCopyToSalesOrder());
        invoiceSettings.setSalesOrderTermCopyToSalesInvoice(data.isSalesOrderTermCopyToSalesInvoice());
        invoiceSettings.setCopySQIntroduction(data.isCopySQIntroduction());
        invoiceSettings.setCopySOIntroduction(data.isCopySOIntroduction());
        if (data.getObjectID() != null) {
            invoicingSettingsManager.update(invoiceSettings);
        } else {
            invoicingSettingsManager.create(invoiceSettings);
        }
        return invoiceSettings.getObjectID();
    }

    private void updateInstructionsAndTermsConditions(InstructionData[] instructions, Integer type) {
        List<EdsPaymentInstruction> instructionList = paymentInstructionManager.getInstructions(type);
        for (EdsPaymentInstruction pi : instructionList) {
            boolean contains = false;
            for (InstructionData data : instructions) {
                if (data.getObjectID() != null && data.getObjectID().equals(pi.getObjectID())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                pi.setDeleted(true);
                paymentInstructionManager.update(pi);
            }
        }

        if (instructions.length > 0)
            if (instructions != null) {
                for (InstructionData instruction : instructions) {
                    EdsPaymentInstruction pi;
                    if (instruction.getObjectID() != null) {
                        pi = paymentInstructionManager.get(instruction.getObjectID());
                    } else {
                        pi = new EdsPaymentInstruction();
                    }
                    pi.setType(type);
                    pi.setText(instruction.getText());
                    paymentInstructionManager.createOrUpdate(pi);
                }
            }
    }

    public String getInvoiceLogoUrl() {
        return invoiceCircularResolver.getInvoiceLogoUrl(null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewInvoiceItem getItem(Integer itemID) {
        EdsItem item = itemManager.getItem(itemID);
        return item == null ? new NewInvoiceItem() : item.getTransferObject();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem[] getCurrencies(ListingFilterParameter fp) {
        return currencyService.getCurrencies(true, fp != null && fp.isFiltirize());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BigDecimal getExchangeRate(String to) {
        return BigDecimal.valueOf(expenseService.getExchRate(to));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem getBaseCurrency() {
        return currencyService.getBaseCurrency();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date getDueDate(EdsUser user) {
        if (user.getCompany().getPaymentDue() == null) {
            return changeDate(Calendar.DAY_OF_MONTH, 30);
        } else {
            return changeDate(Calendar.DAY_OF_MONTH, user.getCompany().getPaymentDue());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date changeDate(int field, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(invoiceCircularResolver.getCompanyDate());
        calendar.add(field, days);

        return calendar.getTime();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getDefaultCurrencySymbol() {
        EdsUser user = (EdsUser) getInstance().getUser();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        EdsCurrency companyDefaultCurrency = fs.getCurrency();
        EdsCurrency countryCurrency = user.getCompany().getCountryZone().getCountry().getCurrency();

        if (companyDefaultCurrency != null) {
            return companyDefaultCurrency.getSymbol();
        } else {
            companyDefaultCurrency = countryCurrency;
            if (companyDefaultCurrency != null) {
                return companyDefaultCurrency.getSymbol();
            }
        }

        return currencyManager.getCurrency(CurrencyManager.USD).getSymbol();
    }

    protected Integer initDataForSending(MessageItem messageItem, EdsBaseInvoice baseInvoice, NewInvoice data, ByteArrayOutputStream baos, String fileType, EdsCrmAccount clientSupplier, String type) {
        EdsUser user = (messageItem.getSenderID() != null ? userManager.get(messageItem.getSenderID()) : clientContactManager.getUser());

        Email email = new Email(messageItem.getToEmails(), messageItem.getSubject(), messageItem.getMailContent());
        email.setFromEmail(messageItem.getFromEmail());
        email.setFromName(messageItem.getReplyTo());
        email.setCc(messageItem.getCc());
        email.setBcc(messageItem.getBcc());
        email.setIsInvisibleTrackerInSubject(true);
        email.setAttachments(messageItem.getFileResources());

        String contactName = "";
        EdsClientContact clientContact = null;
        EdsCrmContact crmContact = null;
        EdsUser manager = null;

        if (PURCHASE_ORDER_MANAGER_CATEGORY.equals(messageItem.getType()) || SALES_QUOTE_MANAGER_CATEGORY.equals(messageItem.getType()) && messageItem.getContactId() != null) {
            manager = userManager.get(messageItem.getContactId());
            contactName = manager != null ? manager.getName() : "";
        } else if (messageItem.getContactId() != null) {
            crmContact = crmContactManager.get(messageItem.getContactId());
            clientContact = clientContactManager.getClientContactByCrmContact(messageItem.getContactId());
            contactName = crmContact.getCrmAccount() != null ? crmContact.getCrmAccount().getName() : null;
            baseInvoice.setClientContact(crmContact);
            baseInvoice.setMailSender(user);
        }

        String link = null;
        if (SALES_QUOTE_CATEGORY.equals(messageItem.getType())) {
            if (clientContact != null && crmContact != null && messageItem.isAccess()) {
                link = EdsContextParams.getFullHost() + "Accounting.html?link=" + EncryptionHelper.encryptURL("salequote|summary/" + baseInvoice.getObjectID())
                        + "&uid=" + EncryptionHelper.encryptURL(clientContact.getObjectID().toString())
                        + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            }
        } else if (SALES_QUOTE_MANAGER_CATEGORY.equals(messageItem.getType())) {
            if (manager != null) {
                link = EdsContextParams.getFullHost() + "Accounting.html?link=" + EncryptionHelper.encryptURL("salequote|summary/" + baseInvoice.getObjectID())
                        + "&uid=" + EncryptionHelper.encryptURL(manager.getObjectID().toString())
                        + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            }
        } else if (PURCHASE_ORDER_MANAGER_CATEGORY.equals(messageItem.getType())) {
            if (manager != null) {
                link = EdsContextParams.getFullHost() + "Accounting.html?link=" + EncryptionHelper.encryptURL("purchaseorder|summary/" + baseInvoice.getObjectID())
                        + "&uid=" + EncryptionHelper.encryptURL(manager.getObjectID().toString())
                        + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            }
        } else if (SALES_ORDER_CATEGORY.equals(messageItem.getType()) && crmContact != null) {
            link = EdsContextParams.getFullHost() + "Accounting.html?link=" + EncryptionHelper.encryptURL("saleorder|summary/" + baseInvoice.getObjectID())
                    + "&uid=" + EncryptionHelper.encryptURL(crmContact.getObjectID().toString())
                    + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Integer trackerID = null;
        try {

            List<Integer> fileIds = new ArrayList<>();
            /*List<FileResource> fileResourceList = new ArrayList<>();
            if (SALES_INVOICE_CATEGORY.equals(messageItem.getType()) || PROJECT_BASE_INVOICE_CATEGORY.equals(messageItem.getType())) {
                fileIds = getAttachments(data.getID(), F_SALE_INV, user, fileResourceList);
            } else if (PURCHASE_ORDER_CATEGORY.equals(messageItem.getType())) {
                fileIds = getAttachments(data.getID(), F_PUR_ORDER, user, fileResourceList);
            } else if (SALES_QUOTE_CATEGORY.equals(messageItem.getType())) {
                fileIds = getAttachments(data.getID(), F_SALE_QUOTE, user, fileResourceList);
            } else {
                fileIds = new ArrayList<>();
            }

            if (fileResourceList != null && fileResourceList.size() > 0) {
                email.getAttachments().addAll(fileResourceList);
            }*/

            EdsUpload upload = new EdsUpload();
            upload.setContentType("application/pdf");
            upload.setOriginalName(setFileName(baseInvoice, fileType, RECEIPT_CATEGORY.equals(messageItem.getType())));
            upload.setType(referenceManager.findReference(_UPLOAD_TYPE, EdsContextParams.getUploadType()));
            upload.setInputStream(bais);
            uploadManager.create(upload);
            fileIds.add(upload.getObjectID());

            if (upload.getObjectID() != null) {
                FileResource f = new FileResource();
                f.setBodyId(upload.getObjectID());
                f.setName(upload.getOriginalName());
                f.setContentLength(upload.getSize());
                f.setContentType(upload.getContentType());
                f.setUploadType(upload.getType() != null ? upload.getType().getName() : "");
                email.getAttachments().add(f);
            }
            boolean isMergeProductPdfFiles = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MERGE_PRODUCT_PDF_FILES);
            if (isMergeProductPdfFiles && SALES_QUOTE_CATEGORY.equals(messageItem.getType()) && baseInvoice instanceof EdsSaleQuote) {
                mergeProductCustomFieldPDFs(baseInvoice, clientSupplier, user, fileIds, email);
            }

            /*if (messageItem.getFileResources() != null && messageItem.getFileResources().size() > 0) {
                for (FileResource fileResource : messageItem.getFileResources()) {
                    if (fileResource.getBodyId() != null && !fileIds.contains(fileResource.getBodyId())) {
                        fileIds.add(fileResource.getBodyId());
                    }
                }
            }*/

            boolean purchaseOrderEmailSettings = emailNotificationSettingsManager.hasEmailNotification(user.getObjectID(), EmailNotificationConstants.PURCHASE_ORDER_EMAIL);
            boolean salesQuoteEmailSettings = emailNotificationSettingsManager.hasEmailNotification(user.getObjectID(), EmailNotificationConstants.SALES_QUOTE_EMAIL);
            boolean salesOrderEmailSettings = emailNotificationSettingsManager.hasEmailNotification(user.getObjectID(), EmailNotificationConstants.SALES_ORDER_EMAIL);
            boolean salesInvoiceEmailSettings = emailNotificationSettingsManager.hasEmailNotification(user.getObjectID(), EmailNotificationConstants.SALES_INVOICE_EMAIL);
            if ((PURCHASE_ORDER_CATEGORY.equals(messageItem.getType()) && purchaseOrderEmailSettings) || (SALES_QUOTE_CATEGORY.equals(messageItem.getType()) && salesQuoteEmailSettings)
                    || (SALES_ORDER_CATEGORY.equals(messageItem.getType()) && salesOrderEmailSettings)
                    || ((SALES_INVOICE_CATEGORY.equals(messageItem.getType()) || PROJECT_BASE_INVOICE_CATEGORY.equals(messageItem.getType())) && salesInvoiceEmailSettings)) {

                trackerID = messageCenterServiceLocal.sendMessage(email);

                if (messageItem.isSendCopyToMe() && trackerID != null) {
                    messageManager.sendInvoiceQuoteToManager(messageItem.getFromEmail(), user, null, null, data, contactName, ((fileIds != null && fileIds.size() > 0) ? fileIds : null), type, link, messageItem.getReplyTo());
                }
            } else {
                if (!PURCHASE_ORDER_CATEGORY.equals(messageItem.getType()) && !SALES_QUOTE_CATEGORY.equals(messageItem.getType()) && !SALES_ORDER_CATEGORY.equals(messageItem.getType()) && !SALES_INVOICE_CATEGORY.equals(messageItem.getType())) {

                    trackerID = messageCenterServiceLocal.sendMessage(email);

                    if (messageItem.isSendCopyToMe() && trackerID != null) {
                        messageManager.sendInvoiceQuoteToManager(messageItem.getFromEmail(), user, null, null, data, contactName, ((fileIds != null && fileIds.size() > 0) ? fileIds : null), type, link, messageItem.getReplyTo());
                    }
                }
            }
        } catch (EdsTemplateException | EdsDbException ex) {
            log.error("Error while sending email.", ex);
        }

        try {
            baos.flush();
            baos.close();
            bais.close();
        } catch (IOException ex) {
            log.error("Unable to close stream", ex);
        }

        if (trackerID != null && (SALES_QUOTE_CATEGORY.equals(messageItem.getType())
                || SALES_INVOICE_CATEGORY.equals(messageItem.getType()) || PROJECT_BASE_INVOICE_CATEGORY.equals(messageItem.getType())
                || PURCHASE_ORDER_CATEGORY.equals(messageItem.getType()) || CREDIT_NOTE_CATEGORY.equals(messageItem.getType()))) {

// We have removed Open status
//            if (!PAID.equals(baseInvoice.getStatus().getCode())) {
//                baseInvoice.setStatus(getInvoiceStatus(OPEN));
//            }

            baseInvoice.setSent(true);
        }
        return trackerID;
    }

    @Transactional
    public void mergeProductCustomFieldPDFs(EdsBaseInvoice baseInvoice, EdsCrmAccount clientSupplier, EdsUser user, List<Integer> fileIds, Email email) {
        List<EdsQuoteItem> edsInvoiceItems = ((EdsSaleQuote) baseInvoice).getQuoteItems();
        if (edsInvoiceItems != null && edsInvoiceItems.size() > 0) {
            List<FileResource> fileResourceList = new ArrayList<>();
            for (EdsQuoteItem quoteItem : edsInvoiceItems) {
                if (quoteItem != null && quoteItem.getItem() != null && quoteItem.getItem().getCustomFields() != null) {
                    List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(quoteItem.getItem().getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView));
                    if (customFieldItems != null && customFieldItems.size() > 0) {
                        for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                            if (DATA_TYPE_FILE_UPLOAD.equals(fieldItem.getDataType()) && fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) {
                                fileResourceList.addAll(attachmentUtilsManager.getAttachments(F_CUSTOM_FIELD_ITEM, Double.valueOf(fieldItem.getFieldStringValue()).intValue(), fieldItem.getObjectId()));
                            }
                        }
                    }
                }
            }
            if (fileResourceList.size() > 0) {
                List<InputStream> inputStreams = new ArrayList<>();

                for (FileResource file : fileResourceList) {
                    if (file.getContentType() != null && DOC_PDF.equals(file.getContentType())) {
                        if (Constants.KPI_STORAGE.equals(file.getUploadType())) {
                            InputStream inputStream = uploadManager.getInputStream(fileBodyManager.get(file.getBodyId()));
                            if (inputStream != null) {
                                inputStreams.add(inputStream);
                            }
                        } else {
                            file.setBodyId(file.getBodyId() == null ? file.getObjectId() : file.getBodyId());
                            InputStream inputStream = uploadManager.getInputStream((EdsUpload) uploadManager.get(file.getBodyId()));
                            if (inputStream != null) {
                                inputStreams.add(inputStream);
                            }
                        }
                    }
                }
                if (inputStreams.size() > 0) {
                    ITextPdfMerger pdfMerger = new ITextPdfMerger();
                    ByteArrayOutputStream arrayOutputStream = null;
                    try {
                        arrayOutputStream = pdfMerger.mergePdfFiles(inputStreams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (arrayOutputStream != null) {
                        DocumentItem fileBody = new DocumentItem();
                        fileBody.setInputStream(new ByteArrayInputStream(arrayOutputStream.toByteArray()));
                        fileBody.setContentType("application/pdf");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        if (baseInvoice.getNumber() != null) {
                            fileBody.setName(baseInvoice.getNumber() + "-" + dateFormat.format(user.getCompany().getCompanyDate()) + ".pdf");
                        } else {
                            fileBody.setName("Sales Quote" + "-" + dateFormat.format(user.getCompany().getCompanyDate()) + ".pdf");
                        }
                        EdsFolder folder = folderManager.getPublicFolder(null);
                        Integer folderID = folder != null ? folder.getObjectID() : null;
                        fileBody.setFolderId(folderID);

                        try {
                            arrayOutputStream.flush();
                            arrayOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        FileResource fileResource = null;
                        try {
                            fileResource = documentsService.createFile(fileBody, EdsContextParams.getUploadType(), Constants.F_COMPANY_PUBLIC_ROOT, null);
                        } catch (DuplicateNameException | QuotaExceededException | InsufficientPermissionsException |
                                 ObjectNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (fileResource != null && !StringUtils.isEmpty(fileResource.getAmazonLink())) {
                            email.setContent(email.getContent() + "<br/>" + fileResource.getAmazonLink());
                        }
                    }
                }
            }
        }
    }

    private List<Integer> getAttachments(Integer id, int folderType, EdsUser user, List<FileResource> fileResources) {
        List<FileResource> attachments = attachmentUtilsManager.getAttachments(folderType, id, id, user);
        fileResources.addAll(attachments);
        List<Integer> result = new ArrayList<>();
        for (FileResource fileResource : attachments) {
            result.add(fileResource.getBodyId());
        }
        return result;
    }

    private String setFileName(EdsBaseInvoice inv, String fileType, boolean isReceipt) {
        EdsUser user = userManager.getUser();
        Integer invoiceID = inv.getObjectID();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(user.getCompany());
        Map<String, String> params = new HashMap<>();
        String[] format = new String[]{};

        if (invoicingSettings != null && (!StringUtil.isEmpty(invoicingSettings.getSalesReceiptPdfNamingFormat())
                || !StringUtil.isEmpty(invoicingSettings.getPdfNamingFormat()))) {
            String pdfPrefix = null;
            if (isReceipt && !StringUtil.isEmpty(invoicingSettings.getSalesReceiptPdfNamingPrefix())) {
                pdfPrefix = invoicingSettings.getSalesReceiptPdfNamingPrefix();
            } else if (!isReceipt && !StringUtil.isEmpty(invoicingSettings.getPdfNamingPrefix())) {
                pdfPrefix = invoicingSettings.getPdfNamingPrefix();
            }
            if (pdfPrefix != null) {
                params.put(PDF_PREFIX, pdfPrefix);
            }
            params.put(PDF_CLIENT, inv.getClientOrSupplier().getName());
            params.put(PDF_CLIENT_CODE, inv.getClientOrSupplier().getNumber());
            params.put(PDF_NUMBER, inv.getNumber());
            params.put(PDF_TYPE, fileType);
            params.put(PDF_COMPANY_NAME, user.getCompany().getName());
            params.put(PDF_GENERATED_DATE, dateFormat.format(user.getUserDate()));
            params.put(PDF_USER_NAME, user.getName());

            String pdfFormat = null;
            if (isReceipt && !StringUtil.isEmpty(invoicingSettings.getSalesReceiptPdfNamingFormat())) {
                pdfFormat = invoicingSettings.getSalesReceiptPdfNamingFormat();
            } else if (!isReceipt && !StringUtil.isEmpty(invoicingSettings.getPdfNamingFormat())) {
                pdfFormat = invoicingSettings.getPdfNamingFormat();
            }
            if (pdfFormat != null) {
                format = pdfFormat.split("_");

            }
        }
        StringBuilder fileName = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (String aFormat : format) {
                String value = params.get(aFormat);
                if (!StringUtil.isEmpty(value)) {
                    fileName.append(fileName.length() > 0 ? ("-" + value) : value);
                }
            }
        }
        if (fileName.length() > 0) {
            return fileName + ".pdf";
        } else {
            return fileType + "-" + user.getCompany().getName() + "-" + dateFormat.format(user.getUserDate()) + ".pdf";
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date getInvoiceDate(int day) {
        Calendar invoiceDate = new GregorianCalendar();
        if (financialSettingsManager.getFinancialSettings() != null &&
                financialSettingsManager.getFinancialSettings().getConversionDate() != null)
            invoiceDate.setTime(financialSettingsManager.getFinancialSettings().getConversionDate());
        invoiceDate.set(Calendar.DAY_OF_YEAR, invoiceDate.get(Calendar.DAY_OF_YEAR) + day);

        return invoiceDate.getTime();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProductsAccountsTaxes getProductsAccountsTaxes(String invoiceType) {
        ProductsAccountsTaxes transObject = new ProductsAccountsTaxes();
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setInvoiceType(invoiceType);
        transObject.setProducts(productService.getCompanyProductsByType(filterParametrs));
        transObject.setAccounts(accountingServiceLocal.getAccountsForInvoice());
        transObject.setTaxes(getCompanyTaxList());
        return transObject;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TypeItem getClientOrSupplier(Integer clientSupplierID, String type) {
        EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();

        if (clientSupplierID == null) {
            return new TypeItem();
        }

        EdsCrmAccount clientBase = crmAccountManager.get(clientSupplierID);
        if (clientBase == null) {
            return new TypeItem();
        }

        Integer primaryBillAddressID = null, primaryMailAddressID = null;
        if (clientBase.getBillingAddress() != null) {
            primaryBillAddressID = clientBase.getBillingAddress().getObjectID();
        }
        if (clientBase.getMailingAddress() != null) {
            primaryMailAddressID = clientBase.getMailingAddress().getObjectID();
        }
        Integer currID = clientBase.getCurrency() == null ? null : clientBase.getCurrency().getObjectID();
        String currName = clientBase.getCurrency() == null ? null : clientBase.getCurrency().getName();
        Integer paymentID = clientBase.getPaymentMethod() == null ? null : clientBase.getPaymentMethod().getObjectID();
        TypeItem item = new TypeItem(clientSupplierID, clientBase.getName(), clientBase.getNumber(), currID, paymentID);

        if (clientBase.getPaymentMethod() != null) {
            item.setPaymentType(commonLocalizer.localize(clientBase.getPaymentMethod().getCode(), clientBase.getPaymentMethod().getName()));
        }
        item.setCurrency(currName);
        item.setBillAddressID(primaryBillAddressID);
        item.setMailAddressID(primaryMailAddressID);
        item.setShippingMethodId(clientBase.getShippingMethod() != null ? clientBase.getShippingMethod().getObjectID() : null);
        item.setSubsidiary(clientBase.getSubsidiary() != null);
        item.setPlaceOfSupply(clientBase.getPlaceOfSupply());
        //item.setReverseChargeApplicable(clientBase.isReverseChargeApplicable());

        if (clientBase.getTerms() != null) {
            item.setTermsItem(clientBase.getTerms().getAsRPC());
        }
        if (clientBase.getVat() != null) {
            item.setTaxItem(clientBase.getVat().createTaxItem());
        }
        EdsUser currentUser = userManager.getUser();
        EdsCompany currentCompany = currentUser.getCompany();
        boolean isArabicCompany = ServerUtils.isArabicCompany(currentCompany);
        if (isArabicCompany && clientBase.getVat() != null && item.getTaxItem() != null) {
            TaxItem taxItem = item.getTaxItem();
            if (clientBase.getVat().getFaiCategorieIds() != null) {
                SelectItem[] faiCategories = clientBase.getVat().getFaiCategorieIds().stream()
                        .map(referenceManager::get)
                        .map(r -> new SelectItem(r.getObjectID(), r.getName()))
                        .toArray(SelectItem[]::new);
                taxItem.setFaiCategories(faiCategories);
            }
            if (clientBase.getVat().getFaiPurchaseCategoryIds() != null) {
                SelectItem[] faiPurchaseCategories = clientBase.getVat().getFaiPurchaseCategoryIds().stream()
                        .map(referenceManager::get)
                        .map(r -> new SelectItem(r.getObjectID(), r.getName()))
                        .toArray(SelectItem[]::new);
                taxItem.setFaiPurchaseCategories(faiPurchaseCategories);
            }
            item.setTaxItem(taxItem);
        }
        if (clientBase.getBankAccount() != null) {
            item.setBankAccountID(clientBase.getBankAccount().getObjectID());
        }
        if (Constants.RECEIVABLE.equals(type)) {
            if (clientBase.getReceivable() != null && !clientBase.getReceivable().isDeleted()) {
                item.setAccountsReceivablePayable(clientBase.getReceivable().createAccountItem());
            }
            item.setSupplierCustomerBalance(crmAccountManager.getClientBalance(clientSupplierID, baseCurrency.getObjectID().equals(currID)).doubleValue());
        } else {
            if (clientBase.getPayable() != null && !clientBase.getPayable().isDeleted()) {
                item.setAccountsReceivablePayable(clientBase.getPayable().createAccountItem());
            }
//            if (!clientBase.getBalanceCalculated()) {
            item.setSupplierCustomerBalance(crmAccountManager.getSupplierBalance(clientSupplierID, baseCurrency.getObjectID().equals(currID)).doubleValue());
//            } else {
//                item.setSupplierCustomerBalance(clientBase.getSupplierBalance().doubleValue());
//            }
        }
        if (clientBase.getDepartment() != null) {
            item.setDefaultDepartment(new SelectItem(clientBase.getDepartment().getObjectID(), clientBase.getDepartment().getName()));
        }
        if (clientBase.getWarehouse() != null) {
            item.setDefaultWarehouse(new SelectItem(clientBase.getWarehouse().getObjectID(), clientBase.getWarehouse().getName()));
        }
        if (clientBase.getTaxTreatment() != null) {
            EdsReference taxTreatment = clientBase.getTaxTreatment();
            item.setTaxTreatment(new SelectItem(taxTreatment.getObjectID(), taxTreatment.getName(), taxTreatment.getCode()));
            item.getTaxTreatment().setCode(taxTreatment.getCode());
        }
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AccountItem getDefaultAccountItem(String type, String accountType) {
        EdsCompany company = accountingManager.getUser().getCompany();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
        boolean isDefault = false;
        EdsAccount defaultAccount = null;

        if (invoicingSettings != null && type != null) {
            if (SALE_QUOTE.equals(type)) {
                defaultAccount = invoicingSettings.getDefAccountSQ();
            } else if (SALE_ORDER.equals(type)) {
                defaultAccount = invoicingSettings.getDefAccountSO();
            } else if (SALE_INVOICE.equals(type)) {
                defaultAccount = invoicingSettings.getDefAccountSI();
            } else if (PURCHASE_ORDER.equals(type)) {
                defaultAccount = invoicingSettings.getDefAccountPO();
            } else if (PURCHASE_INVOICE.equals(type)) {
                defaultAccount = invoicingSettings.getDefAccountPI();
            }
        }

        if (defaultAccount == null) {
            if (RECEIVABLE.equals(accountType)) {
                defaultAccount = accountingManager.getAccountTypeWithMinCode(EdsAccountType.SALES);
            }
        } else {
            isDefault = true;
        }
        if (defaultAccount == null) {
            return null;
        }
        AccountItem accountItem = defaultAccount.createAccountItem();
        accountItem.setDefault(isDefault);
        return accountItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DiscountItem getDefaultDiscountItem(String type) {
        EdsCompany company = accountingManager.getUser().getCompany();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
        DiscountItem defaultDiscount = null;

        if (invoicingSettings != null && type != null) {
            if (SALE_QUOTE.equals(type)) {
                defaultDiscount = new DiscountItem(invoicingSettings.getDefDiscountSQ(), getDiscountName(invoicingSettings.getDefDiscountSQ()));
            } else if (SALE_ORDER.equals(type)) {
                defaultDiscount = new DiscountItem(invoicingSettings.getDefDiscountSO(), getDiscountName(invoicingSettings.getDefDiscountSO()));
            } else if (SALE_INVOICE.equals(type)) {
                defaultDiscount = new DiscountItem(invoicingSettings.getDefDiscountSI(), getDiscountName(invoicingSettings.getDefDiscountSI()));
            } else if (PURCHASE_ORDER.equals(type)) {
                defaultDiscount = new DiscountItem(invoicingSettings.getDefDiscountPO(), getDiscountName(invoicingSettings.getDefDiscountPO()));
            } else if (PURCHASE_INVOICE.equals(type)) {
                defaultDiscount = new DiscountItem(invoicingSettings.getDefDiscountPI(), getDiscountName(invoicingSettings.getDefDiscountPI()));
            }
        }

        return defaultDiscount;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaxItem getDefaultTaxItem() {
        EdsVat defaultVat = vatManager.getDefaultVat();
        TaxItem taxItem = null;
        if (defaultVat != null && defaultVat.getFaiId()==null && defaultVat.getFaiPurchaseId()==null) {
            taxItem = new TaxItem(defaultVat.getObjectID(), defaultVat.getName(), defaultVat.getTaxRateAsBigDecimal(), defaultVat.getEffectiveRateAsBigDecimal());
            taxItem.setId(defaultVat.getObjectID());
            taxItem.setName(defaultVat.getTaxNameAndRateAsString());
            taxItem.setTaxPercent(defaultVat.getTaxRateAsBigDecimal());
            taxItem.setEffectiveTaxPercent(defaultVat.getEffectiveRateAsBigDecimal());

            taxItem.setTaxType(defaultVat.getTaxType());
            taxItem.setTaxKey(defaultVat.getKey());
        }
        return taxItem;
    }

    private String getDiscountName(Integer id) {
        String name = null;
        // If nothing selected, by default it gets Percentage
        if (id == null || id == 0) {
            name = "Percentage";
        } else if (id == 1) {
            name = "Fixed Amount";
        } else {
            name = discountManager.get(id).getName();
        }
        return name;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PdfTemplateItemList getCompanyPdfTemplatesByType(String type) {
        return getCompanyPdfTemplatesByType(type, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PdfTemplateItemList getCompanyPdfTemplatesByType(String type, boolean isBrowserVersion) {
        final List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplatesByType(type, isBrowserVersion);
        final SelectItem[] items = new SelectItem[templates.size()];
        Integer defaultTemplateId = null;
        int i = 0;

        for (EdsCompanyPdfTemplate pdfTemplate : templates) {
            items[i++] = new SelectItem(pdfTemplate.getObjectID(), pdfTemplate.getName());

            if (pdfTemplate.isDefaultTemplate()) {
                defaultTemplateId = pdfTemplate.getObjectID();
            }
        }
        return new PdfTemplateItemList(items, defaultTemplateId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getClientPdfTemplatesByType(String type) {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getClientPDFTemplatesByType(type);
        return templates.stream().map(item -> new SelectItem(item.getObjectID(), item.getName()))
                .toList()
                .toArray(new SelectItem[]{});
    }

    protected void addSaleQuoteToSolr(EdsSaleQuote saleQuote) {
        EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(saleQuote.getObjectID());
        try {
            saleQuoteSolrComponent.indexes(Collections.singletonList(saleQuote), (pickList != null) ? Collections.singletonList(pickList) : null);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void addPurchaseOrderToSolr(EdsPurchaseOrder order) {
        try {
            purchaseOrderSolrComponent.index(order);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void addRFQToSolr(EdsRFQ rfq) {
        try {
            rfqSolrComponent.index(rfq);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
