package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductKitItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.itemBatches.ItemBatchServiceLocal;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.commons.MastercardPaymentHandler;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.*;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.QRBarcodeEncoder;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.QRCodeGenerator;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.tag.*;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.*;
import com.edatasite.workforce.gwt.core.server.utils.*;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.*;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Jun-2010
 * Time: 15:39:30
 */
public abstract class BaseInvoicePdfHandler extends AbstractITextPostPdfHandler implements AccountingConstants, PDFConstants, IPostPDFHandler {

    protected static String SALES_RECEIPT = "SALES_RECEIPT";

    protected static String SO_FILE_NAME = "Sales_Order";
    protected static String SQ_FILE_NAME = "Sales_Quote";
    protected static String SI_FILE_NAME = "Sales_Invoice";
    protected static String PS_FILE_NAME = "Packing Slip";
    protected static String SR_FILE_NAME = "Sales_Receipt";
    protected static String PBI_FILE_NAME = "Project_Based_Invoice";
    protected static String PI_FILE_NAME = "Purchase_Invoice";
    protected static String PO_FILE_NAME = "Purchase_Order";
    protected static String DN_FILE_NAME = "Debit_Note";
    protected static String CN_FILE_NAME = "Credit_Note";

    protected static String APPROVED_STAMP_URL = "/pdfimages/approved.png";
    protected static String PAID_STAMP_URL = "/pdfimages/paid.png";
    protected static String PARTIALLY_PAID_STAMP_URL = "/pdfimages/partial-paid.png";
    protected static String OVERDUE_STAMP_URL = "/pdfimages/overdue.png";
    protected static String RECEIVED_STAMP_URL = "/pdfimages/received.png";
    protected static List<String> EU_MEMBERS = Arrays.asList("AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "GB");
    private final DecimalFormat numberFormat = new DecimalFormat("###.##");
    private static final String NOT_AVAILABLE = "N/A";
    @Autowired
    public GenericSettingsManager genericSettingsManager;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    protected InvoiceManager invoiceManager;
    protected QuoteManager quoteManager;
    protected ClientManager clientManager;
    protected InvoicingSettingsManager invoicingSettingsManager;
    protected VatManager vatManager;
    protected CrmAccountManager crmAccountManager;
    protected AddressManager addressManager;
    protected WarehouseManager warehouseManager;
    @Autowired
    protected InvoiceTermsManager invoiceTermsManager;
    @Autowired
    protected CompanyManager companyManager;
    @Autowired
    protected ItemCFManager itemCFManager;
    @Autowired
    protected ProductSerialManager productSerialManager;
    @Autowired
    protected TransactionManager transactionManager;
    @Autowired
    protected ItemStockManager itemStockManager;
    @Autowired
    protected PickListManager pickListManager;
    @Autowired
    protected InvoicePaymentManager invoicePaymentManager;
    @Autowired
    @Qualifier("commonService")
    protected CommonServiceLocal commonServiceLocal;
    @Autowired
    protected AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    protected ItemTableSettingService itemTableSettingService;
    @Autowired
    EntityTypeManager entityTypeManager;
    @Autowired
    CurrencyService currencyService;
    private EmployeeManager employeeManager;
    private CurrencyManager currencyManager;
    private ClientContactManager clientContactManager;
    private CrmContactManager crmContactManager;
    private BankAccountManager bankAccountManager;
    private PaymentMethodManager paymentMethodManager;
    private ShippingMethodManager shippingMethodManager;
    private ItemManager itemManager;
    private ProjectManager projectManager;
    @Autowired
    private ProductPictureManager productPictureManager;
    @Autowired
    private PriceLevelPPManager priceLevelPPManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private ItemBatchManager itemBatchManager;
    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private ItemBatchServiceLocal itemBatchService;
    @Autowired
    private PriceLevelService priceLevelService;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private CustomCrmAccountManager customCrmAccountManager;
    @Autowired
    private TaxillaPDFProvider taxillaPDFProvider;

    static Integer generateHashKey(NewInvoiceItem item, List<QIGroupingField> groupingFields) {
        StringBuilder keyBuilder = new StringBuilder();
        if (groupingFields.contains(QIGroupingField.ITEM)) {
            keyBuilder.append(QIGroupingField.ITEM.name() + ":")
                    .append(item.getItemID() != null ? item.getItemID().toString() : "")
                    .append(item.getItemName());
        }

        if (groupingFields.contains(QIGroupingField.PRICE)) {
            keyBuilder.append("|").append(QIGroupingField.PRICE.name() + ":").append(item.getUnitPrice());
        }

        if (groupingFields.contains(QIGroupingField.ACCOUNT) && item.getAccountItem() != null) {
            keyBuilder.append("|").append(QIGroupingField.ACCOUNT.name() + ":").append(item.getAccountItem().getId());
        }

        if (groupingFields.contains(QIGroupingField.TAX) && item.getTaxItem() != null) {
            keyBuilder.append("|").append(QIGroupingField.TAX.name() + ":").append(item.getTaxItem().getId());
        }

        if (groupingFields.contains(QIGroupingField.DEPARTMENT) && item.getDepartmentItem() != null) {
            keyBuilder.append("|").append(QIGroupingField.DEPARTMENT.name() + ":").append(item.getDepartmentItem().getId());
        }

        return keyBuilder.toString().hashCode();
    }

    public static long daysBetween(Date startDate, Date endDate) {
        return (setTimeToMidnight(endDate).getTime() - setTimeToMidnight(startDate).getTime()) / (24 * 60 * 60 * 1000);
    }

    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public void setInvoiceManager(InvoiceManager invoiceManager) {
        this.invoiceManager = invoiceManager;
    }

    public void setQuoteManager(QuoteManager quoteManager) {
        this.quoteManager = quoteManager;
    }

    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void setCurrencyManager(CurrencyManager currencyManager) {
        this.currencyManager = currencyManager;
    }

    public void setClientContactManager(ClientContactManager clientContactManager) {
        this.clientContactManager = clientContactManager;
    }

    public void setCrmContactManager(CrmContactManager crmContactManager) {
        this.crmContactManager = crmContactManager;
    }

    public void setBankAccountManager(BankAccountManager bankAccountManager) {
        this.bankAccountManager = bankAccountManager;
    }

    public void setPaymentMethodManager(PaymentMethodManager paymentMethodManager) {
        this.paymentMethodManager = paymentMethodManager;
    }

    public void setInvoicingSettingsManager(InvoicingSettingsManager invoicingSettingsManager) {
        this.invoicingSettingsManager = invoicingSettingsManager;
    }

    public void setVatManager(VatManager vatManager) {
        this.vatManager = vatManager;
    }

    public void setShippingMethodManager(ShippingMethodManager shippingMethodManager) {
        this.shippingMethodManager = shippingMethodManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void setCrmAccountManager(CrmAccountManager crmAccountManager) {
        this.crmAccountManager = crmAccountManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setInvoiceCircularResolver(InvoiceCircularResolver invoiceCircularResolver) {
        this.invoiceCircularResolver = invoiceCircularResolver;
    }

    public void setAddressManager(AddressManager addressManager) {
        this.addressManager = addressManager;
    }

    public void setWarehouseManager(WarehouseManager warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    public boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        NewInvoice invoiceData = (NewInvoice) dataClass;
        EdsUser edsUser;
        if (invoiceData.getUserID() != null) {
            edsUser = userManager.get(invoiceData.getUserID());
        } else {
            edsUser = invoiceManager.getUser();
        }

        EdsCrmAccount client = null;
        EdsCrmContact clientContact = null;
        EdsCrmAccount supplier = null;

        if (isClient()) {
            client = clientManager.get(invoiceData.getClientID());
            if (invoiceData.getClientContactID() != null) {
                clientContact = crmContactManager.get(invoiceData.getClientContactID());
            } else {
                clientContact = clientContactManager.getPrimaryClientContact(invoiceData.getClientID());
            }
            if (clientContact != null && !ServerUtils.equalsEdsObject(client, clientContact.getCrmAccount())) {
                clientContact = clientContactManager.getPrimaryClientContact(invoiceData.getClientID());
            }
        } else {
            supplier = crmAccountManager.get(invoiceData.getClientID());
            if (invoiceData.getClientContactID() != null) {
                clientContact = crmContactManager.get(invoiceData.getClientContactID());
            } else {
                clientContact = clientContactManager.getPrimarySupplierContact(invoiceData.getClientID());
            }
            if (clientContact != null && !ServerUtils.equalsEdsObject(supplier, clientContact.getCrmAccount())) {
                clientContact = clientContactManager.getPrimarySupplierContact(invoiceData.getClientID());
            }
        }

        EdsCurrency edsCurrency = currencyManager.getCurrency(invoiceData.getCurrencyID());

        return getInvoiceData(invoiceData, edsUser, edsCurrency, isClient() ? client : supplier, clientContact);
    }

    public HashMap<String, CustomisedITextTable> getCustomData(NewInvoice invoiceData) {
        Integer clientId = invoiceData.getClientID();
        CustomisedITextTable customTable = new CustomisedITextTable();
        EdsUser user = userManager.getUser();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Date currentDate = new Date();
        String date = ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(currentDate, user.getCompany()), user);
        int totalCount = vatManager.getTaxRatesListCount();
        customTable.addRow(CURRENT_DATE, dateFormat.format(currentDate));
        customTable.addRow(CURRENT_DATE_BY_COMPANY_FORMAT, date);
        customTable.addRow(CURRENT_YEAR, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        customTable.addRow(CURRENT_TIME, timeFormat.format(userManager.getUser().getUserDate()));
        customTable.addRow(ITEM_TAX_RATE_COUNT, String.valueOf(totalCount));
        HashMap<String, CustomisedITextTable> result = new HashMap<>();
        result.put(CLIENT_ATTACHMENTS, getClientAttachments(clientId));
        result.put("CLIENT_ADDRESS_DATA", getClientAddress(clientId));
        result.put("CLIENT_PRICE_LEVELS", getClientPriceLevels(clientId));
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT)) {
            result.put("CUSTOM_CRM_ACCOUNT_DATA", getCustomCrmAccountData(invoiceData));
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CRYPTOGRAPHIC_STAMP_FROM_TAXILLA)) {
            result.put("TAXILLA_QR_CODE", getQRcodeFromZatca(invoiceData));
        }
        result.put(CUSTOM_DATA, customTable);
        return result;
    }

    private CustomisedITextTable getClientPriceLevels(Integer clientId) {
        CustomisedITextTable pricelevelTable = new CustomisedITextTable();
        pricelevelTable.addColumn("PRICE_LEVEL", "");

        EdsCrmAccount client = crmAccountManager.get(clientId);
        if (client == null) {
            return pricelevelTable;
        }

        List<EdsPriceLevel> priceLevels = client.getPriceLevels();
        if (priceLevels != null && !priceLevels.isEmpty()) {
            for (EdsPriceLevel priceLevel : priceLevels) {
                pricelevelTable.addRow(escapeHtml(priceLevel.getName()));
            }
        }

        return pricelevelTable;
    }


    private CustomisedITextTable getClientAddress(Integer clientId) {
        CustomisedITextTable clientTable = new CustomisedITextTable();
        clientTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(clientId);
        if (edsCrmAccount == null) {
            return clientTable;
        }
        EdsAddress billAddress = edsCrmAccount.getBillingAddress();
        if (billAddress != null) {
            clientTable.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
            clientTable.addRowWithCode(BILL_ADDRESS, "", escapeHtml(billAddress.getAddress()));
            clientTable.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(billAddress.getAddressb()));
            clientTable.addRowWithCode(BILL_CITY, "", escapeHtml(billAddress.getCity()));
            clientTable.addRowWithCode(BILL_COUNTRY, "", escapeHtml(billAddress.getCountryName()));
            clientTable.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(billAddress.getZipCode()));
        }
        EdsAddress mailAddress = edsCrmAccount.getMailingAddress();
        if (mailAddress != null) {
            clientTable.addRowWithCode(MAIL_ADDRESS_NAME, "", escapeHtml(mailAddress.getName()));
            clientTable.addRowWithCode(MAIL_ADDRESS, "", escapeHtml(mailAddress.getAddress()));
            clientTable.addRowWithCode(MAIL_ADDRESS2, "", escapeHtml(mailAddress.getAddressb()));
            clientTable.addRowWithCode(MAIL_CITY, "", escapeHtml(mailAddress.getCity()));
            clientTable.addRowWithCode(MAIL_COUNTRY, "", escapeHtml(mailAddress.getCountryName()));
            clientTable.addRowWithCode(MAIL_ZIPCODE, "", escapeHtml(mailAddress.getZipCode()));
        }
        return clientTable;
    }

    private CustomisedITextTable getCustomCrmAccountData(NewInvoice invoice) {
        CustomisedITextTable clientTable = new CustomisedITextTable();
        clientTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        EdsCustomCrmAccount customCrmAccount = customCrmAccountManager.get(invoice.getCustomCrmAccountId());
        if (customCrmAccount == null) {
            clientTable.addRowWithCode("IS_EMPTY", "", "YES");
            return clientTable;
        }
        clientTable.addRowWithCode("IS_EMPTY", "", "NO");
        clientTable.addRowWithCode(NAME, "", escapeHtml(customCrmAccount.getClientName()));
        clientTable.addRowWithCode(CLIENT_VAT_NUMBER, "", escapeHtml(customCrmAccount.getVatNumber()));
        clientTable.addRowWithCode("CLIENT_TRN_NUMBER", "", escapeHtml(customCrmAccount.getTrnNumber()));
        clientTable.addRowWithCode(PDFConstants.CLIENT_CODE, "", escapeHtml(customCrmAccount.getClientNumber()));

        clientTable.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(customCrmAccount.getBillingAddressName()));
        clientTable.addRowWithCode(BILL_ADDRESS, "", escapeHtml(customCrmAccount.getBillingAddress()));
        clientTable.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(customCrmAccount.getBillingAddressb()));
        clientTable.addRowWithCode(BILL_CITY, "", escapeHtml(customCrmAccount.getBillingCity()));
        clientTable.addRowWithCode(BILL_COUNTRY, "", escapeHtml(customCrmAccount.getBillingCountryName()));
        clientTable.addRowWithCode(BILL_STATE, "", escapeHtml(customCrmAccount.getBillingStateName()));
        clientTable.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(customCrmAccount.getBillingZipCode()));

        clientTable.addRowWithCode(MAIL_ADDRESS_NAME, "", escapeHtml(customCrmAccount.getMailingAddressName()));
        clientTable.addRowWithCode(MAIL_ADDRESS, "", escapeHtml(customCrmAccount.getMailingAddress()));
        clientTable.addRowWithCode(MAIL_ADDRESS2, "", escapeHtml(customCrmAccount.getMailingAddressb()));
        clientTable.addRowWithCode(MAIL_CITY, "", escapeHtml(customCrmAccount.getMailingCity()));
        clientTable.addRowWithCode(MAIL_COUNTRY, "", escapeHtml(customCrmAccount.getMailingCountryName()));
        clientTable.addRowWithCode(MAIL_STATE, "", escapeHtml(customCrmAccount.getMailingStateName()));
        clientTable.addRowWithCode(MAIL_ZIPCODE, "", escapeHtml(customCrmAccount.getMailingZipCode()));
        return clientTable;
    }

    private CustomisedITextTable getQRcodeFromZatca(NewInvoice invoice) {
        CustomisedITextTable clientTable = new CustomisedITextTable();
        clientTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        String qrFromTaxilla = taxillaPDFProvider.getQRFromTaxilla(
                invoice.getInvoiceNumber(),
                invoice.isCreditNote() && RECEIVABLE.equalsIgnoreCase(invoice.getType()) ? "381" : invoice.isCreditNote() && PAYABLE.equalsIgnoreCase(invoice.getType()) ? "383" : "388",
                new SimpleDateFormat("yyyy").format(invoice.getInvoiceDate().getNonConvertedDate()));

        clientTable.addRowWithCode("QR_CODE", "QR_CODE", qrFromTaxilla != null ? qrFromTaxilla : "");
        return clientTable;
    }

    private CustomisedITextTable getClientAttachments(Integer clientId) {
        CustomisedITextTable attachmentTable = new CustomisedITextTable();
        attachmentTable.addColumnOrder("FILE_NAME", "DOC_ID", "ISSUED_DATE", "DOCUMENT_DESCRIPTION", "TYPE", "CREATED_DATE", "EXPIRY_DATE", "FILE_URL");
        ArrayList<FileResource> resource = documentsService.getFileResources(Constants.F_CRM_ACCOUNT, clientId, clientId);
        if (resource != null && !resource.isEmpty()) {
            for (FileResource aResource : resource) {
                attachmentTable.addRow(aResource.getFileName() != null ? aResource.getFileName() : "", aResource.getDocID() != null ? aResource.getDocID() : "",
                        aResource.getIssuedDate() != null ? dateFormat.format(aResource.getIssuedDate().getNonConvertedDate()) : "", aResource.getDescription() != null ? aResource.getDescription() : "",
                        aResource.getType() != null ? aResource.getType() : "", aResource.getCreationDate() != null ? dateFormat.format(aResource.getCreationDate()) : "",
                        aResource.getExpireDate() != null ? dateFormat.format(aResource.getExpireDate().getNonConvertedDate()) : "", aResource.getDownloadUrl() != null ? aResource.getDownloadUrl() : "");
            }
        }
        return attachmentTable;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        NewInvoice invoiceData = (NewInvoice) dataClass;
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(company, null);
        EdsUser edsUser;
        if (invoiceData.getUserID() != null) {
            edsUser = userManager.get(invoiceData.getUserID());
        } else {
            edsUser = invoiceManager.getUser();
        }

        //bu add viewda ham itemCustomField chiqishi uchun qilindi
        if (invoiceData.getCustomItemColumns() == null) {
            invoiceData.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_INVOICE_ITEM));
        }

        initFooterParams(edsUser.getCompany());
        EdsCrmAccount client = null;
        EdsCrmContact clientContact = null;
        EdsCrmAccount supplier = null;

        if (isClient()) {
            client = clientManager.get(invoiceData.getClientID());
            if (invoiceData.getClientContactID() != null) {
                clientContact = crmContactManager.get(invoiceData.getClientContactID());
            } else {
                clientContact = clientContactManager.getPrimaryClientContact(invoiceData.getClientID());
            }
            if (clientContact != null && !ServerUtils.equalsEdsObject(client, clientContact.getCrmAccount())) {
                clientContact = clientContactManager.getPrimaryClientContact(invoiceData.getClientID());
            }
        } else {
            supplier = crmAccountManager.get(invoiceData.getClientID());
            if (invoiceData.getClientContactID() != null) {
                clientContact = crmContactManager.get(invoiceData.getClientContactID());
            } else {
                clientContact = clientContactManager.getPrimarySupplierContact(invoiceData.getClientID());
            }
            if (clientContact != null && !ServerUtils.equalsEdsObject(supplier, clientContact.getCrmAccount())) {
                clientContact = clientContactManager.getPrimarySupplierContact(invoiceData.getClientID());
            }
        }

        EdsCurrency edsCurrency = currencyManager.getCurrency(invoiceData.getCurrencyID());

        ITextGenericPdfData pdfData = getInvoiceDataCustomise(invoiceData, edsUser, edsCurrency, isClient() ? client : supplier, clientContact);
        pdfData.setCustomData(getCustomData(invoiceData));
        pdfData.setCurrentDate(ServerUtils.shortDateFormat(edsUser.getUserDate(new Date()), edsUser));
        pdfData.setPriceFormat(priceScaleFormat);
        pdfData.setUserId(edsUser.getObjectID().toString());

        return pdfData;
    }

    @Override
    protected PdfParams getParams(Object dataClass) {
        PdfParams params = new PdfParams();
        params.setHeaderHeight("300px");
        params.setFooterHeight("240px");
        return params;
    }

    protected <T extends EdsCrmAccount> ITextGenericPdfData getInvoiceDataCustomise(NewInvoice invoiceData,
                                                                                    EdsUser edsUser,
                                                                                    EdsCurrency edsCurrency,
                                                                                    T clientOrSupplier,
                                                                                    EdsCrmContact clientContact) {
        return null;
    }

    protected <ClientOrSupplier extends EdsCrmAccount> EdsCrmAccount getSupplier(ClientOrSupplier clientOrSupplier) {
        if (clientOrSupplier instanceof EdsCrmAccount) {
            return clientOrSupplier;
        }
        return null;
    }

    protected <ClientOrSupplier extends EdsCrmAccount> EdsCrmAccount getClient(ClientOrSupplier clientOrSupplier) {
        if (clientOrSupplier instanceof EdsCrmAccount) {
            return clientOrSupplier;
        }
        return null;
    }

    protected boolean isClient() {
        return true;
    }

    /**
     * @param request
     * @return NewInvoice
     */
    public NewInvoice getInvoiceData(HttpServletRequest request) {
        NewInvoice newInvoiceData = new NewInvoice();
        EdsUser user = uploadManager.getUser();
        boolean isProjectLineItemEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        //Temporary shit. Something wrong on server. Trying to find where is NullPointer.

        if (user == null) {
            throw new NullPointerException("User is null");
        }
        if (user.getCompany() == null) {
            throw new NullPointerException("Company is null");
        }
        if (user.getCompany().getCountryZone() == null) {
            throw new NullPointerException("Countryzone is null");
        }
        if (user.getCompany().getCountryZone().getZone() == null) {
            throw new NullPointerException("Zone is null");
        }
        if (user.getCompany().getCountryZone().getZone().getZoneID() == null) {
            throw new NullPointerException("ZoneID is null");
        }
        if (TimeZone.getTimeZone(user.getCompany().getCountryZone().getZone().getZoneID()) == null) {
            throw new NullPointerException("TimeZone.getTimeZone() is null");
        }

        NewInvoiceItem[] items = new NewInvoiceItem[Integer.parseInt(request.getParameter(PDFTransferObject.LENGTH))];

        for (int i = 0; i < items.length; i++) {
            items[i] = new NewInvoiceItem();
            String itemID = request.getParameter(PDFTransferObject.ITEM_ID + i);
            if (itemID != null) {
                items[i].setItemID(Integer.parseInt(itemID));
            }
            items[i].setItemName(URLDecoder.decode(request.getParameter(PDFTransferObject.ITEM_NAME + i), StandardCharsets.UTF_8));
            items[i].setProductBrand(URLDecoder.decode(request.getParameter(PDFTransferObject.BRAND_NAME + i), StandardCharsets.UTF_8));
            items[i].setDescription(URLDecoder.decode(request.getParameter(PDFTransferObject.DESCRIPTION + i), StandardCharsets.UTF_8));
            if (!"".equals(request.getParameter(PDFTransferObject.ITEM_TYPE + i))) {
                items[i].setProductType(Integer.valueOf(request.getParameter(PDFTransferObject.ITEM_TYPE + i)));
            }
            items[i].setQuantity(request.getParameter(PDFTransferObject.QTY + i).equals("null") ? null : unformat(request.getParameter(PDFTransferObject.QTY + i)));
            items[i].setMeasurement(new SelectItem(null, request.getParameter(PDFTransferObject.UNIT_MEASUREMENT + i), request.getParameter(PDFTransferObject.UNIT_MEASUREMENT_DESCRIPTION + i)));
            items[i].setUnitPrice(request.getParameter(PDFTransferObject.UNIT_PRICE + i).equals("null") ? null : unformat(request
                    .getParameter(PDFTransferObject.UNIT_PRICE + i)));
            items[i].setItemOriginalPrice(request.getParameter(PDFTransferObject.ORIGINAL_PRICE + i).equals("null") ? null : unformat(request
                    .getParameter(PDFTransferObject.ORIGINAL_PRICE + i)));
            items[i].setDiscountPercent(request.getParameter(PDFTransferObject.PRODUCT_DISCOUNT + i).equals("null") ? null : unformat(request
                    .getParameter(PDFTransferObject.PRODUCT_DISCOUNT + i)));
            items[i].setDiscountAmount(request.getParameter(PDFTransferObject.PRODUCT_DISCOUNT_FIXED + i).equals("null") ? null : unformat(request
                    .getParameter(PDFTransferObject.PRODUCT_DISCOUNT_FIXED + i)));
            items[i].setDoubleDiscountPercent(request.getParameter(PDFTransferObject.PRODUCT_DOUBLE_DISCOUNT + i).equals("null") ? null : unformat(request
                    .getParameter(PDFTransferObject.PRODUCT_DOUBLE_DISCOUNT + i)));
            items[i].setDoubleDiscountAmount(request.getParameter(PDFTransferObject.PRODUCT_DOUBLE_DISCOUNT_FIXED + i).equals("null") ? null : unformat(request
                    .getParameter(PDFTransferObject.PRODUCT_DOUBLE_DISCOUNT_FIXED + i)));
            items[i].setNet(unformat(request.getParameter(PDFTransferObject.NET + i)));
            Integer taxID = request.getParameter(PDFTransferObject.VAT_ID + i).equals("null") ? null : Integer.parseInt(request.getParameter(PDFTransferObject.VAT_ID + i));
            if (taxID != null) {
                EdsVat vat = vatManager.get(taxID);
                if (vat != null) {
                    items[i].setTaxItem(vat.createTaxItem());
                }
            }
            items[i].setAccountName(request.getParameter(PDFTransferObject.ACCOUNT_NAME + i));
            items[i].setTaxAmount(unformat(request.getParameter(PDFTransferObject.TAX_AMOUNT + i)));
            items[i].setTotalAmount(unformat(request.getParameter(PDFTransferObject.TOTAL_AMOUNT + i)));
            if (request.getParameter(PDFTransferObject.QUOTE_ITEM_ID + i) != null && !request.getParameter(PDFTransferObject.QUOTE_ITEM_ID + i).equals("null")) {
                String quoteItemId = request.getParameter(PDFTransferObject.QUOTE_ITEM_ID + i);
                items[i].setQuoteItemId(quoteItemId != null && !("").equals(quoteItemId) ? Integer.valueOf(quoteItemId) : null);
            }
            if (isProjectLineItemEnable) {
                items[i].setProject(new SelectItem(null, URLDecoder.decode(request.getParameter(PDFTransferObject.PROJECT_NAME + i), StandardCharsets.UTF_8)));
            }
            if (itemID != null && !itemID.isEmpty()) {
                items[i].setBatchTrackingEnabled(itemManager.get(Integer.parseInt(itemID)).getBatchTrackingEnabled());
            }
            String parameter = request.getParameter(PDFTransferObject.ATTACHMENT + i);
            if (StringUtils.isNotEmpty(parameter)) {
                FileItem fileItem = new FileItem();
                fileItem.setAmazonLink(parameter);
                fileItem.setUploadType(EdsContextParams.getUploadType());
                items[i].getAttachments().add(fileItem);
            }
            int count = 0;
            if (request.getParameter(PDFTransferObject.ITEM_CUSTOM_FIELDS_SIZE) != null) {
                count = Integer.parseInt(request.getParameter(PDFTransferObject.ITEM_CUSTOM_FIELDS_SIZE));
            }
            List<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                CompanyCustomFieldItem item = new CompanyCustomFieldItem();
                item.setDataType(request.getParameter(PDFTransferObject.ITEM_FIELD_DATA_TYPE + i + j));
                item.setFieldName(request.getParameter(PDFTransferObject.ITEM_FIELD_NAME + i + j));
                item.setFieldStringValue(request.getParameter(PDFTransferObject.ITEM_FIELD_STRING_VALUE + i + j));
                if (request.getParameter(PDFTransferObject.ITEM_FIELD_DATA_VALUE + i + j) != null) {
                    Date parsedDate = parseFilterParameterDate(request.getParameter(PDFTransferObject.ITEM_FIELD_DATA_VALUE + i + j));
                    item.setFieldDateNonConvertedValue(new DateNonConvertable(parsedDate));
                    if (parsedDate != null) {
                        item.setFieldDateNonConvertedValue(new DateNonConvertable(parsedDate));
                    }
                }
                customFieldItems.add(j, item);
            }
            items[i].setCustomFieldItems(Lists.newArrayList(customFieldItems));
        }
        newInvoiceData.setItems(items);


        if (request.getParameter(PDFTransferObject.EXPENSE_LENGTH) != null) {
            BillableExpenseItem[] expenseItems = new BillableExpenseItem[Integer.parseInt(request.getParameter(PDFTransferObject.EXPENSE_LENGTH))];
            //for Expense
            for (int i = 0; i < expenseItems.length; i++) {
                expenseItems[i] = new BillableExpenseItem();

                String expenseTotal = request.getParameter(PDFTransferObject.ITEM_EXPENSE_TOTAL_AMOUNT + i);
                String expenseTotalWithMarkup = request.getParameter(PDFTransferObject.ITEM_EXPENSE_TOTAL_WITH_MARKUP + i);
                String expenseMarkupAmount = request.getParameter(PDFTransferObject.ITEM_EXPENSE_MARKUP_AMOUNT + i);
                String expenseMarkupTaxAmount = request.getParameter(PDFTransferObject.ITEM_EXPENSE_MARKUP_TAX_AMOUNT + i);
                String numberExpression = "^[-+]?\\d+(\\.\\d+)?";
                expenseTotal = expenseTotal != null && !expenseTotal.isEmpty() && expenseTotal.replace(",", "").matches(numberExpression) ? expenseTotal : "0.00";
                expenseTotalWithMarkup = expenseTotalWithMarkup != null && !expenseTotalWithMarkup.isEmpty() && expenseTotalWithMarkup.replace(",", "").matches(numberExpression) ? expenseTotalWithMarkup : "0.00";
                expenseMarkupAmount = expenseMarkupAmount != null && !expenseMarkupAmount.isEmpty() && expenseMarkupAmount.replace(",", "").matches(numberExpression) ? expenseMarkupAmount : "0.00";
                expenseMarkupTaxAmount = expenseMarkupTaxAmount != null && !expenseMarkupTaxAmount.isEmpty() && expenseMarkupTaxAmount.replace(",", "").matches(numberExpression) ? expenseMarkupTaxAmount : "0.00";

                expenseItems[i].setAccount(new SelectItem(null, URLDecoder.decode(request.getParameter(PDFTransferObject.ITEM_EXPENSE_CATEGORY + i), StandardCharsets.UTF_8)));
                expenseItems[i].setDescription(URLDecoder.decode(request.getParameter(PDFTransferObject.ITEM_EXPENSE_DESCRIPTION + i), StandardCharsets.UTF_8));
                expenseItems[i].setAmountInCurrency(unformat(expenseTotal));
                //expenseItems[i].setBaseSubtotal(unformat(expenseTotalWithMarkup));
                expenseItems[i].setMarkupAmount(unformat(expenseMarkupAmount));
                expenseItems[i].setMarkupTaxAmount(unformat(expenseMarkupTaxAmount));

            }
            newInvoiceData.setExpenses(Lists.newArrayList(expenseItems));
        }

        if (request.getParameter(PDFTransferObject.NOTES_LENGTH) != null) {
            HistoryListItem[] notes = new HistoryListItem[Integer.parseInt(request.getParameter(PDFTransferObject.NOTES_LENGTH))];
            for (int i = 0; i < notes.length; i++) {
                notes[i] = new HistoryListItem();
                notes[i].setComment(URLDecoder.decode(request.getParameter(PDFTransferObject.NOTES + i), StandardCharsets.UTF_8));
                notes[i].setEmployee(request.getParameter(PDFTransferObject.NOTES_EMPLOYEE + i));
                String noteDate = request.getParameter(PDFTransferObject.NOTES_DATE + i);
                if (noteDate != null && !"".equals(noteDate)) {
                    notes[i].setEventDate(parseFilterParameterDate(noteDate));
                }
            }
            newInvoiceData.setHistoryList(notes);
        }


        TotalTaxItem[] totalTaxItems = new TotalTaxItem[Integer.parseInt(request.getParameter(PDFTransferObject.TOTALTAX_LENGHT))];
        for (int i = 0; i < totalTaxItems.length; i++) {
            totalTaxItems[i] = new TotalTaxItem();
            totalTaxItems[i].setTaxAmount(unformat(request.getParameter(PDFTransferObject.TOTALTAX_AMOUNT + i)));
            TaxItem item = new TaxItem(Integer.parseInt(request.getParameter(PDFTransferObject.TOTALTAX_ID + i)),
                    request.getParameter(PDFTransferObject.TOTALTAX_NAME + i), BigDecimal.valueOf(Double.parseDouble(request.getParameter(PDFTransferObject.TOTALTAX_PERCENT + i))));
            totalTaxItems[i].setTaxItem(item);
        }
        newInvoiceData.setTotalTaxItems(totalTaxItems);

        String pdfTemplateID = request.getParameter(PDFTransferObject.PDF_TEMPLATE_ID);
        newInvoiceData.setPdfTemplateID((pdfTemplateID != null && !"".equals(pdfTemplateID)) ? Integer.valueOf(pdfTemplateID) : null);

        String orderIds = request.getParameter(PDFTransferObject.ORDER_BASEINVOICE_ORDER_IDS);
        newInvoiceData.setOrderBaseinvoiceOrderIds(!ServerUtils.isNullOrEmpty(orderIds) ? orderIds : null);

        String invoiceID = request.getParameter(PDFTransferObject.INVOICE_ID);
        String convertedItemID = request.getParameter(PDFTransferObject.CONVERTED_ITEM_ID);
        if (invoiceID != null && !"".equals(invoiceID)) {
            newInvoiceData.setID(Integer.valueOf(invoiceID));
        }
        if (convertedItemID != null && !"".equals(convertedItemID)) {
            newInvoiceData.setConvertedItemID(Integer.valueOf(convertedItemID));
        }
        if (request.getParameter(PDFTransferObject.CONVERTION_PERCENT) != null) {
            newInvoiceData.setConvertedPercent(unformat(request.getParameter(PDFTransferObject.CONVERTION_PERCENT)));
        }
        if (request.getParameter(PDFTransferObject.PROGRESS_INVOICING) != null) {
            newInvoiceData.setProgressInvoicing("true".equals(request.getParameter(PDFTransferObject.PROGRESS_INVOICING)));
        }

        ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
        int count = 0;
        if (request.getParameter(PDFTransferObject.CUSTOM_FIELDS_SIZE) != null) {
            count = Integer.parseInt(request.getParameter(PDFTransferObject.CUSTOM_FIELDS_SIZE));
        }

        for (int i = 0; i < count; i++) {
            CompanyCustomFieldItem item = new CompanyCustomFieldItem();
            item.setDataType(request.getParameter(PDFTransferObject.FIELD_DATA_TYPE + i));
            item.setFieldName(request.getParameter(PDFTransferObject.FIELD_NAME + i));
            item.setFieldStringValue(request.getParameter(PDFTransferObject.FIELD_STRING_VALUE + i));
            if (request.getParameter(PDFTransferObject.FIELD_SELECTED_ID + i) != null && !"null".equals(request.getParameter(PDFTransferObject.FIELD_SELECTED_ID + i))) {
                Integer selectId = Integer.parseInt(request.getParameter(PDFTransferObject.FIELD_SELECTED_ID + i));
                item.setSelectedId(selectId);
            }
            item.setUiType(request.getParameter(PDFTransferObject.FIELD_UI_TYPE + i));
            if (request.getParameter(PDFTransferObject.FIELD_DATA_VALUE + i) != null) {
                Date parsedDate = parseFilterParameterDate(request.getParameter(PDFTransferObject.FIELD_DATA_VALUE + i));
                item.setFieldDateNonConvertedValue(new DateNonConvertable(parsedDate));
                if (parsedDate != null) {
                    item.setFieldDateNonConvertedValue(new DateNonConvertable(parsedDate));
                }
            }
            customFieldItems.add(i, item);
        }

        newInvoiceData.setCustomFieldItems(Lists.newArrayList(customFieldItems));
        newInvoiceData.setProjectBasedInvoice("true".equals(request.getParameter(PDFTransferObject.IS_PROJECT_BASED_INVOICE)));
        newInvoiceData.setIntroduction(URLDecoder.decode(request.getParameter(PDFTransferObject.INTRODUCTION), StandardCharsets.UTF_8));
        newInvoiceData.setInvoiceNumber(request.getParameter(PDFTransferObject.INVOICE_NUMBER));
        newInvoiceData.setQuoteNumber(request.getParameter(PDFTransferObject.QUOTE_NUMBER));
        newInvoiceData.setReference(URLDecoder.decode(request.getParameter(PDFTransferObject.REFERENCE), StandardCharsets.UTF_8));
        String periodStart = request.getParameter(PDFTransferObject.PERIOD_START);
        String periodEnd = request.getParameter(PDFTransferObject.PERIOD_END);
        if (periodStart != null && !"".equals(periodStart.trim())) {
            newInvoiceData.setPeriodStart(new DateNonConvertable(parseFilterParameterDate(periodStart)));
        }
        if (periodEnd != null && !"".equals(periodEnd.trim())) {
            newInvoiceData.setPeriodEnd(new DateNonConvertable(parseFilterParameterDate(periodEnd)));
        }
        newInvoiceData.setClientID(Integer.parseInt(request.getParameter(PDFTransferObject.CLIENT_ID)));

        if (request.getParameter(PDFTransferObject.INVOICE_TYPE) != null) {
            newInvoiceData.setInvoiceType(Integer.parseInt(request.getParameter(PDFTransferObject.INVOICE_TYPE)));
        }

        if (request.getParameter(PDFTransferObject.BILL_ADDRESS_ID) != null) {
            newInvoiceData.setBillAddressID(Integer.parseInt(request.getParameter(PDFTransferObject.BILL_ADDRESS_ID)));
        }
        if (request.getParameter(PDFTransferObject.MAIL_ADDRESS_ID) != null) {
            newInvoiceData.setMailAddressID(Integer.parseInt(request.getParameter(PDFTransferObject.MAIL_ADDRESS_ID)));
        }

        if (request.getParameter(PDFTransferObject.CLIENT_ID_PURCHASE) != null) {
            TypeItem clientItem = new TypeItem(Integer.parseInt(request.getParameter(PDFTransferObject.CLIENT_ID_PURCHASE)), null, null);
            if (request.getParameter(PDFTransferObject.PUR_CL_BILL_ADDR_ID) != null) {
                clientItem.setBillAddressID(Integer.parseInt(request.getParameter(PDFTransferObject.PUR_CL_BILL_ADDR_ID)));
            }
            if (request.getParameter(PDFTransferObject.PUR_CL_MAIL_ADDR_ID) != null) {
                clientItem.setMailAddressID(Integer.parseInt(request.getParameter(PDFTransferObject.PUR_CL_MAIL_ADDR_ID)));
            }
            newInvoiceData.setClientItem(clientItem);
        }

        if (getFromInvoice() != null && getFromInvoice().equals(PURCHASE_ORDER)) {
            newInvoiceData.setPoNumber(request.getParameter(PDFTransferObject.INVOICE_NUMBER));
        } else {
            newInvoiceData.setPoNumber(request.getParameter(PDFTransferObject.PO_NUMBER));
        }
        newInvoiceData.setInvoiceDate(new DateNonConvertable(parseFilterParameterDate(request.getParameter(PDFTransferObject.INVOICE_DATE))));
        newInvoiceData.setDueDate(new DateNonConvertable(parseFilterParameterDate(request.getParameter(PDFTransferObject.DUE_DATE))));

        newInvoiceData.setExchageRate(unformat(request.getParameter(PDFTransferObject.EXCHANGE_RATE)));
        newInvoiceData.setCurrencyID(Integer.parseInt(request.getParameter(PDFTransferObject.CURRENCY)));
        newInvoiceData.setPaymentInstruction(URLDecoder.decode(request.getParameter(PDFTransferObject.PAYMENT_INSTRUCTION), StandardCharsets.UTF_8));
        if (request.getParameter(PDFTransferObject.INVOICE_DUE_TERMS_ID) != null) {
            EdsInvoiceTerms edsInvoiceTerms = invoiceTermsManager.get(Integer.parseInt(request.getParameter(PDFTransferObject.INVOICE_DUE_TERMS_ID)));
            if (edsInvoiceTerms != null) {
                newInvoiceData.setInvoiceTermsItem(edsInvoiceTerms.getAsRPC());
            }
        }
        if (request.getParameter(PDFTransferObject.PROJECT_ID) != null) {
            newInvoiceData.setRelatedProjectID(Integer.parseInt(request.getParameter(PDFTransferObject.PROJECT_ID)));
        }
        if (request.getParameter(PDFTransferObject.CURRENT_APPROVER_ID) != null) {
            EdsEmployee employee = employeeManager.get(Integer.parseInt(request.getParameter(PDFTransferObject.CURRENT_APPROVER_ID)));
            if (employee != null) {
                SelectItem approver = new SelectItem();
                approver.setId(employee.getObjectID());
                approver.setName(employee.getFullName());
                newInvoiceData.setCurrentApproverSelectItem(approver);
            }
        }
        if (request.getParameter(PDFTransferObject.CLIENT_CONTACT_ID) != null) {
            newInvoiceData.setClientContactID(Integer.parseInt(request.getParameter(PDFTransferObject.CLIENT_CONTACT_ID)));
        }
        if (request.getParameter(PDFTransferObject.SHIPPING_METHOD_ID) != null) {
            newInvoiceData.setShippingMethodID(Integer.parseInt(request.getParameter(PDFTransferObject.SHIPPING_METHOD_ID)));
        }

        if (request.getParameter(PDFTransferObject.PREVIOUS_BALANCE) != null) {
            newInvoiceData.setPreviosBalance(unformat(request.getParameter(PDFTransferObject.PREVIOUS_BALANCE)));
        }
        if (request.getParameter(PDFTransferObject.PAYMENTS_RECEIVED) != null) {
            newInvoiceData.setPaymentsReceived(unformat(request.getParameter(PDFTransferObject.PAYMENTS_RECEIVED)));
        }
        if (request.getParameter(PDFTransferObject.CANCEL_DATE) != null && !"".equals(request.getParameter(PDFTransferObject.CANCEL_DATE).trim())) {
            newInvoiceData.setCancelDate(new DateNonConvertable(parseFilterParameterDate(request.getParameter(PDFTransferObject.CANCEL_DATE))));
        }

        if (request.getParameter(PDFTransferObject.PAYMENT_TYPE) != null && !"null".equals(request.getParameter(PDFTransferObject.PAYMENT_TYPE))) {
            newInvoiceData.setPaymentMethodID(Integer.valueOf(request.getParameter(PDFTransferObject.PAYMENT_TYPE)));
        }
        newInvoiceData.setPaymentTerms(URLDecoder.decode(request.getParameter(PDFTransferObject.PAYMENT_TERMS), StandardCharsets.UTF_8));
        newInvoiceData.setShippingTerms(URLDecoder.decode(request.getParameter(PDFTransferObject.SHIPPING_TERMS), StandardCharsets.UTF_8));
        if (request.getParameter(PDFTransferObject.REQUESTIONED_BY) != null && !"null".equals(request.getParameter(PDFTransferObject.REQUESTIONED_BY))) {
            newInvoiceData.setRequisitionedBy(new SelectItem(Integer.valueOf(request.getParameter(PDFTransferObject.REQUESTIONED_BY))));
        }
        newInvoiceData.setStatus(request.getParameter(PDFTransferObject.INVOICE_STATUS));
        newInvoiceData.setStatusCode(request.getParameter(PDFTransferObject.INVOICE_STATUS));
        newInvoiceData.setTotal(unformat(request.getParameter(PDFTransferObject.TOTAL)));
        String totalDiscount = request.getParameter(PDFTransferObject.TOTAL_DISCOUNT);
        newInvoiceData.setTotalDiscount(totalDiscount != null ? unformat(totalDiscount) : null);
        String totalInInvoiceCurrency = request.getParameter(PDFTransferObject.TOTAL_IN_INVOICE_CURRENCY);
        newInvoiceData.setTotalInInvoiceCurrency(totalInInvoiceCurrency != null ? unformat(totalInInvoiceCurrency) : null);
        String bankAccountID = request.getParameter(PDFTransferObject.BANK_ACCOUNT_ID);
        newInvoiceData.setBankAccount(new SelectItem(((bankAccountID != null && !"".equals(bankAccountID)) ? Integer.valueOf(bankAccountID) : null), ""));
        newInvoiceData.setTotalTaxes(unformat(request.getParameter(PDFTransferObject.TOTAL_TAXES)));
        newInvoiceData.setSubtotal(unformat(request.getParameter(PDFTransferObject.SUBTOTAL)));
        newInvoiceData.setNetAmount(unformat(request.getParameter(PDFTransferObject.NET_AMOUNT)));
        newInvoiceData.setBillableExpenseAmount(unformat(request.getParameter(PDFTransferObject.BILL_EXP_TOTAL)));
        newInvoiceData.setBillableExpenseTaxAmount(unformat(request.getParameter(PDFTransferObject.BILL_EXP_TAX_TOTAL)));

        return newInvoiceData;
    }

    private BigDecimal unformat(String text) {
        if (text != null) {
            return new BigDecimal(text.replace(",", ""));
        }
        return ZERO;
    }

    private boolean isValid(String value) {
        return value != null && value.length() > 0;
    }

    @Override
    protected String getPdfLogoUrl(EdsCompany edsCompany, boolean hasPhantom) throws IOException {
        String url = super.getPdfAccountingLogoUrl(edsCompany);
        if (StringUtils.isNotEmpty(url)) {
            return url;
        }
        return super.getPdfLogoUrl(edsCompany, hasPhantom);
    }

    protected Object getDataClass(HttpServletRequest request) {
        return getInvoiceData(request);
    }

    @Override
    protected void setFileName(EdsUser edsUser, Object dataClass) {
        Integer invoiceID = null;
        if (edsUser != null) {
            EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
            if (dataClass instanceof NewInvoice newInvoice) {
                invoiceID = newInvoice.getID();
            } else if (dataClass instanceof InvoiceQuoteRequestObject quoteRequestObject) {
                invoiceID = quoteRequestObject.getObjectID();
            } else if (dataClass instanceof RequestObject requestObject) {
                invoiceID = requestObject.getObjectID();
            }
            if (invoicingSettings.getPdfNamingFormat() != null && !"".equals(invoicingSettings.getPdfNamingFormat())) {
                Map<String, String> params = new HashMap<>();
                if (dataClass instanceof NewInvoice data) {
                    EdsCrmAccount clientBase = isClient() ? clientManager.get(data.getClientID()) : crmAccountManager.get(data.getClientID());

                    params.put(PDF_CLIENT, clientBase.getName());
                    params.put(PDF_CLIENT_CODE, clientBase.getNumber());
                    params.put(PDF_NUMBER, data.getInvoiceNumber());
                } else if (dataClass instanceof InvoiceQuoteRequestObject) {
                    params = getFileNameParams(invoiceID);
                } else if (dataClass instanceof RequestObject) {
                    params = getFileNameParams(invoiceID);
                }
                if (edsUser.getCompany().getObjectID().equals(22440)) {
                    if (SI_FILE_NAME.equals(getFileName())) {
                        params.put(PDF_TYPE, "Rechnung");
                    } else {
                        params.put(PDF_TYPE, getFileName());
                    }
                } else {
                    params.put(PDF_TYPE, getFileName());
                }
                String pdfPrefix = invoicingSettings.getPdfNamingPrefix();
                if (pdfPrefix != null && !"".equals(pdfPrefix)) {
                    params.put(PDF_PREFIX, pdfPrefix);
                }
                params.put(PDF_COMPANY_NAME, edsUser.getCompany().getName());
                params.put(PDF_GENERATED_DATE, dateFormat.format(edsUser.getUserDate()));
                params.put(PDF_USER_NAME, edsUser.getName());

                String[] format = invoicingSettings.getPdfNamingFormat().split("_");
                StringBuilder fileName = new StringBuilder();
                if (params != null && params.size() > 0) {
                    for (String aFormat : format) {
                        String value = params.get(aFormat);
                        if (value != null && !"".equals(value.trim())) {
                            fileName.append(fileName.length() > 0 ? ("-" + value) : value);
                        }
                    }
                }
                if (fileName.length() > 0) {
                    setFileName(fileName.toString());
                } else {
                    setFileName(getFileName() + "-" + edsUser.getCompany().getName() + "-" + dateFormat.format(edsUser.getUserDate()));
                }
            } else {
                String fileName = getFileName();
                if (edsUser.getCompany().getObjectID().equals(22440) && SI_FILE_NAME.equals(fileName)) {
                    fileName = "Rechnung";
                }
                setFileName(fileName + "-" + edsUser.getCompany().getName() + "-" + dateFormat.format(edsUser.getUserDate()));
            }
        } else {
            setFileName(getFileName() + "-" + dateFormat.format(new Date()));
        }
    }

    /**
     * @return
     */
    protected String getFromInvoice() {
        return null;
    }

    protected CustomisedITextTable getCustomAddressTable(EdsCrmAccount clientSupplier, EdsCrmContact crmContact, NewInvoice data, EdsUser user) {
        Map<String, String> values = getBillToAddressMap(clientSupplier, crmContact, data, true);
        CustomisedITextTable addressTable = new CustomisedITextTable();
        addressTable.setName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billTo));
        addressTable.addColumnOrder(COLUMN_VALUE);
        addressTable.addRowWithCode(NAME, escapeHtml(values.get(NAME)));        // Client Name
        addressTable.addRowWithCode(ACCOUNT_OWNER, escapeHtml(values.get(ACCOUNT_OWNER)));        // Client Owner
        addressTable.addRowWithCode(CLIENT_PHONE, escapeHtml(values.get(CLIENT_PHONE)));        // Client Phone
        addressTable.addRowWithCode(CLIENT_EMAIL, escapeHtml(values.get(CLIENT_EMAIL)));        // Client Email
        addressTable.addRowWithCode(CLIENT_FAX, escapeHtml(values.get(CLIENT_FAX)));        // Client Fax
        addressTable.addRowWithCode(CLIENT_CONTACT, escapeHtml(values.get(CLIENT_CONTACT)));  // Contact Name
        if (values.get(PURCHASE_CLIENT_NAME) != null) {
            addressTable.addRowWithCode(PURCHASE_CLIENT_NAME, escapeHtml(values.get(PURCHASE_CLIENT_NAME)));
        }
        if (values.get(PURCHASE_CLIENT_CONTACT_NAME) != null) {
            addressTable.addRowWithCode(PURCHASE_CLIENT_CONTACT_NAME, escapeHtml(values.get(PURCHASE_CLIENT_CONTACT_NAME)));
        }
        if (values.get(PURCHASE_CLIENT_EMAIL) != null) {
            addressTable.addRowWithCode(PURCHASE_CLIENT_EMAIL, escapeHtml(values.get(PURCHASE_CLIENT_EMAIL)));
        }
        if (values.get(PURCHASE_CLIENT_PHONE) != null) {
            addressTable.addRowWithCode(PURCHASE_CLIENT_PHONE, escapeHtml(values.get(PURCHASE_CLIENT_PHONE)));
        }
        if (values.get(BUILDING_NUMBER) != null) {
            addressTable.addRowWithCode(BUILDING_NUMBER, escapeHtml(values.get(BUILDING_NUMBER)));
        }
        if (values.get(PLOT_IDENTIFICATION) != null) {
            addressTable.addRowWithCode(PLOT_IDENTIFICATION, escapeHtml(values.get(PLOT_IDENTIFICATION)));
        }
        if (values.get(CITY_SUBDIVISION_NAME) != null) {
            addressTable.addRowWithCode(CITY_SUBDIVISION_NAME, escapeHtml(values.get(CITY_SUBDIVISION_NAME)));
        }
        //Client/Supplier Bill Address
        if (values.get(BILL_ADDRESS_NAME) != null) {
            addressTable.addRowWithCode(BILL_ADDRESS_NAME, escapeHtml(values.get(BILL_ADDRESS_NAME)));      // Address Name
        }
        if (values.get(BILL_ADDRESS) != null) {
            addressTable.addRowWithCode(BILL_ADDRESS, escapeHtml(values.get(BILL_ADDRESS)));      // Address
        }
        if (values.get(BILL_ADDRESS2) != null) {
            addressTable.addRowWithCode(BILL_ADDRESS2, escapeHtml(values.get(BILL_ADDRESS2)));    // Address 2
        }
        if (values.get(BILL_CITY) != null) {
            addressTable.addRowWithCode(BILL_CITY, escapeHtml(values.get(BILL_CITY)));
        }
        if (values.get(BILL_STATE) != null) {
            addressTable.addRowWithCode(BILL_STATE, escapeHtml(values.get(BILL_STATE)));
        }
        if (values.get(BILL_ZIPCODE) != null) {
            addressTable.addRowWithCode(BILL_ZIPCODE, escapeHtml(values.get(BILL_ZIPCODE)));
        }
        if (values.get(BILL_COUNTRY) != null) {
            addressTable.addRowWithCode(BILL_COUNTRY, escapeHtml(values.get(BILL_COUNTRY)));
        }
        if (values.get(BILL_COUNTRY_EU_MEMBER) != null) {
            addressTable.addRowWithCode(BILL_COUNTRY_EU_MEMBER, escapeHtml(values.get(BILL_COUNTRY_EU_MEMBER)));
        }
        //Client/Supplier Mail Address
        if (values.get(MAIL_ADDRESS_NAME) != null) {
            addressTable.addRowWithCode(MAIL_ADDRESS_NAME, escapeHtml(values.get(MAIL_ADDRESS_NAME)));      // Address Name
        }
        if (values.get(MAIL_ADDRESS) != null) {
            addressTable.addRowWithCode(MAIL_ADDRESS, escapeHtml(values.get(MAIL_ADDRESS)));      // Address
        }
        if (values.get(MAIL_ADDRESS2) != null) {
            addressTable.addRowWithCode(MAIL_ADDRESS2, escapeHtml(values.get(MAIL_ADDRESS2)));    // Address 2
        }
        if (values.get(MAIL_CITY) != null) {
            addressTable.addRowWithCode(MAIL_CITY, escapeHtml(values.get(MAIL_CITY)));
        }
        if (values.get(MAIL_STATE) != null) {
            addressTable.addRowWithCode(MAIL_STATE, escapeHtml(values.get(MAIL_STATE)));
        }
        if (values.get(MAIL_ZIPCODE) != null) {
            addressTable.addRowWithCode(MAIL_ZIPCODE, escapeHtml(values.get(MAIL_ZIPCODE)));
        }
        if (values.get(MAIL_COUNTRY) != null) {
            addressTable.addRowWithCode(MAIL_COUNTRY, escapeHtml(values.get(MAIL_COUNTRY)));
        }
        if (values.get(MAIL_COUNTRY_EU_MEMBER) != null) {
            addressTable.addRowWithCode(MAIL_COUNTRY_EU_MEMBER, escapeHtml(values.get(MAIL_COUNTRY_EU_MEMBER)));
        }

        //Company/Client Bill Address
        if (values.get(COMP_BILL_ADDRESS_NAME) != null) {
            addressTable.addRowWithCode(COMP_BILL_ADDRESS_NAME, escapeHtml(values.get(COMP_BILL_ADDRESS_NAME)));      // Address Name
        }
        if (values.get(COMP_BILL_ADDRESS) != null) {
            addressTable.addRowWithCode(COMP_BILL_ADDRESS, escapeHtml(values.get(COMP_BILL_ADDRESS)));      // Address
        }
        if (values.get(COMP_BILL_ADDRESS2) != null) {
            addressTable.addRowWithCode(COMP_BILL_ADDRESS2, escapeHtml(values.get(COMP_BILL_ADDRESS2)));    // Address 2
        }
        if (values.get(COMP_BILL_CITY) != null) {
            addressTable.addRowWithCode(COMP_BILL_CITY, escapeHtml(values.get(COMP_BILL_CITY)));
        }
        if (values.get(COMP_BILL_STATE) != null) {
            addressTable.addRowWithCode(COMP_BILL_STATE, escapeHtml(values.get(COMP_BILL_STATE)));
        }
        if (values.get(COMP_BILL_ZIPCODE) != null) {
            addressTable.addRowWithCode(COMP_BILL_ZIPCODE, escapeHtml(values.get(COMP_BILL_ZIPCODE)));
        }
        if (values.get(COMP_BILL_COUNTRY) != null) {
            addressTable.addRowWithCode(COMP_BILL_COUNTRY, escapeHtml(values.get(COMP_BILL_COUNTRY)));
        }
        //Company/Client Mail Address
        if (values.get(COMP_MAIL_ADDRESS_NAME) != null) {
            addressTable.addRowWithCode(COMP_MAIL_ADDRESS_NAME, escapeHtml(values.get(COMP_MAIL_ADDRESS_NAME)));      // Address Name
        }
        if (values.get(COMP_MAIL_ADDRESS) != null) {
            addressTable.addRowWithCode(COMP_MAIL_ADDRESS, escapeHtml(values.get(COMP_MAIL_ADDRESS)));      // Address
        }
        if (values.get(COMP_MAIL_ADDRESS2) != null) {
            addressTable.addRowWithCode(COMP_MAIL_ADDRESS2, escapeHtml(values.get(COMP_MAIL_ADDRESS2)));    // Address 2
        }
        if (values.get(COMP_MAIL_CITY) != null) {
            addressTable.addRowWithCode(COMP_MAIL_CITY, escapeHtml(values.get(COMP_MAIL_CITY)));
        }
        if (values.get(COMP_MAIL_STATE) != null) {
            addressTable.addRowWithCode(COMP_MAIL_STATE, escapeHtml(values.get(COMP_MAIL_STATE)));
        }
        if (values.get(COMP_MAIL_ZIPCODE) != null) {
            addressTable.addRowWithCode(COMP_MAIL_ZIPCODE, escapeHtml(values.get(COMP_MAIL_ZIPCODE)));
        }
        if (values.get(COMP_MAIL_COUNTRY) != null) {
            addressTable.addRowWithCode(COMP_MAIL_COUNTRY, escapeHtml(values.get(COMP_MAIL_COUNTRY)));
        }

        //WAREHOUSE ADDRESS
        if (getFromInvoice() != null && (PURCHASE_ORDER.equals(getFromInvoice()) || PURCHASE_INVOICE.equals(getFromInvoice()))) {
            EdsWarehouse warehouse = warehouseManager.getDefaultWarehouse();
            if (warehouse != null && warehouse.getName() != null && !"".equals(warehouse.getName())) {
                addressTable.addRowWithCode(WH_NAME, warehouse.getName());
            }
        }

        addressTable.addRowWithCode(PARENT_ACCOUNT, escapeHtml(values.get(PARENT_ACCOUNT)));
        addressTable.addRowWithCode(CLIENT_CURRENCY, escapeHtml(values.get(CLIENT_CURRENCY)));
        addressTable.addRowWithCode(CLIENT_VAT_NUMBER, escapeHtml(values.get(CLIENT_VAT_NUMBER)));
        addressTable.addRowWithCode(CRNUMBER, escapeHtml(values.get(CRNUMBER)));
        addressTable.addRowWithCode(PDFConstants.CLIENT_CODE, escapeHtml(values.get(PDFConstants.CLIENT_CODE)));
        addressTable.addRowWithCode(CLIENT_OWNER, escapeHtml(values.get(CLIENT_OWNER)));
        addressTable.addRowWithCode(CONTACT_FIRST_NAME, escapeHtml(values.get(CONTACT_FIRST_NAME)));
        addressTable.addRowWithCode(CONTACT_LAST_NAME, escapeHtml(values.get(CONTACT_LAST_NAME)));
        addressTable.addRowWithCode(CONTACT_MIDDLE_NAME, escapeHtml(values.get(CONTACT_MIDDLE_NAME)));
        addressTable.addRowWithCode(CONTACT_PHONE, escapeHtml(values.get(CONTACT_PHONE)));
        addressTable.addRowWithCode(CONTACT_EMAIL, escapeHtml(values.get(CONTACT_EMAIL)));
        addressTable.addRowWithCode(CLIENT_EMAIL, escapeHtml(values.get(CLIENT_EMAIL)));
        addressTable.addRowWithCode(CONTACT_JOB_TITLE, escapeHtml(values.get(CONTACT_JOB_TITLE)));
        addressTable.addRowWithCode(PAYMENT_METHOD, escapeHtml(values.get(PAYMENT_METHOD)));
        addressTable.addRowWithCode(CLIENT_WEBSITE, escapeHtml(values.get(CLIENT_WEBSITE)));
        addressTable.addRowWithCode("CLIENT_REGISTRATION_NUMBER", escapeHtml(values.get("CLIENT_REGISTRATION_NUMBER")));
        addressTable.addRowWithCode("CLIENT_BANK_NAME", values.get("CLIENT_BANK_NAME"));
        addressTable.addRowWithCode("CLIENT_BANK_ACCOUNT_NAME", values.get("CLIENT_BANK_ACCOUNT_NAME"));
        addressTable.addRowWithCode("CLIENT_BANK_ACCOUNT_NO", escapeHtml(values.get("CLIENT_BANK_ACCOUNT_NO")));
        addressTable.addRowWithCode("CLIENT_BANK_BRANCH", escapeHtml(values.get("CLIENT_BANK_BRANCH")));
        addressTable.addRowWithCode("CLIENT_BANK_ADDRESS", escapeHtml(values.get("CLIENT_BANK_ADDRESS")));
        addressTable.addRowWithCode("CLIENT_BANK_SWIFT_CODE", escapeHtml(values.get("CLIENT_BANK_SWIFT_CODE")));
        addressTable.addRowWithCode("CLIENT_BANK_SORT_CODE", escapeHtml(values.get("CLIENT_BANK_SORT_CODE")));
        addressTable.addRowWithCode("CLIENT_BANK_IBAN_CODE", escapeHtml(values.get("CLIENT_BANK_IBAN_CODE")));
        addressTable.addRowWithCode("CLIENT_TAX_TREATMENT", escapeHtml(values.get("CLIENT_TAX_TREATMENT")));

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_ZALOG)) {
            addressTable.addRowWithCode("CLIENT_ZALOG", escapeHtml(values.get("CLIENT_ZALOG")));
        }

        addressTable.addRowWithCode("SHIP_TO_LABEL", escapeHtml(values.get("SHIP_TO_LABEL")));
        addressTable.addRowWithCode("SUPPLIER_LABEL", escapeHtml(values.get("SUPPLIER_LABEL")));

        if (values.get(CLIENT_PRE_PAYMENT_BALANCE) != null) {
            addressTable.addRowWithCode(CLIENT_PRE_PAYMENT_BALANCE, escapeHtml(values.get(CLIENT_PRE_PAYMENT_BALANCE)));
        }

        if (user != null) {
            addressTable.addRowWithCode(USERNAME, user.getUserName() != null ? user.getUserName() : "");
            addressTable.addRowWithCode(USER_F_NAME, user.getFirstName() != null ? user.getFirstName().replace("&", "&amp;") : "");
            addressTable.addRowWithCode(USER_L_NAME, user.getLastName() != null ? user.getLastName() : "");
            addressTable.addRowWithCode(USER_M_NAME, user.getMiddleName() != null ? user.getMiddleName() : "");
            addressTable.addRowWithCode(USER_EMAIL, user.getEmail() != null ? user.getEmail() : "");
            if (user instanceof EdsEmployee edsEmployee) {
                EdsCrmContact userCrmContact = edsEmployee.getContact();
                if (userCrmContact != null && userCrmContact.getPrimaryPhone() != null && !"".equals(userCrmContact.getPrimaryPhone().trim())) {
                    addressTable.addRowWithCode(USER_PHONE, escapeHtml(userCrmContact.getPrimaryPhone().replace("|", "")));
                }
            }
        }
        addressTable.setCustomFields(getCustomFields(clientSupplier, data, null));
        return addressTable;
    }

    protected CustomisedITextTable getCustomPrimaryContactAddressTable(EdsCrmContact primaryContact) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        addressTable.addColumnOrder(COLUMN_VALUE);
        if (primaryContact != null) {
            List<EdsAddress> primaryContactAddressList = primaryContact.getAddresses();
            for (EdsAddress address : primaryContactAddressList) {
                if (address.isPrimary()) {
                    addressTable.addRowWithCode(PRIMARY_CONTACT_ADDESS_NAME, escapeHtml(address.getName()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_ADDESS_LINE1, escapeHtml(address.getAddress()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_ADDESS_LINE2, escapeHtml(address.getAddressb()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_COUNTRY, escapeHtml(address.getCountry() != null ? address.getCountry().getName() : address.getCountryName()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_STATE, escapeHtml(address.getState() != null ? address.getState().getName() : ""));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_CITY, escapeHtml(address.getCity()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_ZIPCODE, escapeHtml(address.getZipCode()));
                } else {//return last secondary address
                    addressTable.addRowWithCode(PRIMARY_CONTACT_SECONDARY_ADDESS_NAME, escapeHtml(address.getName()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_SECONDARY_ADDESS_LINE1, escapeHtml(address.getAddress()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_SECONDARY_ADDESS_LINE2, escapeHtml(address.getAddressb()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_SECONDARY_COUNTRY, escapeHtml(address.getCountry() != null ? address.getCountry().getName() : address.getCountryName()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_SECONDARY_STATE, escapeHtml(address.getState() != null ? address.getState().getName() : ""));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_SECONDARY_CITY, escapeHtml(address.getCity()));
                    addressTable.addRowWithCode(PRIMARY_CONTACT_SECONDARY_ZIPCODE, escapeHtml(address.getZipCode()));
                }
            }
        }
        return addressTable;
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsCrmAccount clientSupplier, NewInvoice invoiceData, Map<String, LinkedHashMap<String, Map<String, String>>> customFields) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        if (clientSupplier != null && clientSupplier.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(clientSupplier.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmAccount));
            if (customFieldItems != null && !customFieldItems.isEmpty()) {
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                    if (item.getFieldName().equals("Действителен до")
                            && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_ZALOG) && item.getFieldDateNonConvertedValue() != null) {
                        Date expiryDate = item.getFieldDateNonConvertedValue().getNonConvertedDate();

                        Date dueDateWithoutTimeZone = null;
                        Date invoiceDateWithoutTimeZone = null;
                        Date expiryDateWithoutTimeZone = null;
                        if (invoiceData.getDueDate() != null && invoiceData.getDueDate().getNonConvertedDate() != null) {
                            try {
                                dueDateWithoutTimeZone = shortDateFormat.parse(shortDateFormat.format(invoiceData.getDueDate().getNonConvertedDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (expiryDate != null) {
                            try {
                                expiryDateWithoutTimeZone = shortDateFormat.parse(shortDateFormat.format(expiryDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (invoiceData.getInvoiceDate() != null) {
                            try {
                                invoiceDateWithoutTimeZone = shortDateFormat.parse(shortDateFormat.format(invoiceData.getInvoiceDate().getNonConvertedDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Map<String, String> col = new HashMap<>();
                        col.put(COLUMN_NAME, "VALID_PASSPORT");
                        if (expiryDateWithoutTimeZone != null) {
                            if (expiryDateWithoutTimeZone.after(invoiceDateWithoutTimeZone) && expiryDateWithoutTimeZone.before(dueDateWithoutTimeZone)) {
                                col.put(COLUMN_VALUE, escapeHtml("false"));
                            } else {
                                col.put(COLUMN_VALUE, escapeHtml("true"));
                            }
                        } else {
                            col.put(COLUMN_VALUE, escapeHtml("true"));
                        }
                        itemCusFields.put(escapeHtml("VALID_PASSPORT"), col);
                    }
                    if (item.getFieldName().equals("Дата Рождения")
                            && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_ZALOG)) {
                        Map<String, String> col = new HashMap<>();
                        col.put(COLUMN_NAME, "VALID_AGE");
                        Date currentDate = new Date();
                        Calendar currentDateCalendar = Calendar.getInstance();
                        if (item.getFieldDateNonConvertedValue() != null) {
                            Calendar birthDayCalendar = Calendar.getInstance();
                            birthDayCalendar.setTime(item.getFieldDateNonConvertedValue().getDate());
                            Date birthDay = item.getFieldDateNonConvertedValue().getDate();
                            int age;
                            if (birthDay != null) {
                                if (currentDateCalendar.get(Calendar.MONTH) == birthDayCalendar.get(Calendar.MONTH)) {
                                    if (currentDateCalendar.get(Calendar.DAY_OF_MONTH) < birthDayCalendar.get(Calendar.DAY_OF_MONTH)) {
                                        age = currentDateCalendar.get(Calendar.YEAR) - birthDayCalendar.get(Calendar.YEAR) - 1;
                                    } else {
                                        age = currentDateCalendar.get(Calendar.YEAR) - birthDayCalendar.get(Calendar.YEAR);
                                    }
                                } else if (currentDateCalendar.get(Calendar.DAY_OF_MONTH) < birthDayCalendar.get(Calendar.DAY_OF_MONTH)) {
                                    age = currentDateCalendar.get(Calendar.YEAR) - birthDayCalendar.get(Calendar.YEAR) - 1;
                                } else {
                                    age = currentDateCalendar.get(Calendar.YEAR) - birthDayCalendar.get(Calendar.YEAR);
                                }
                                if (age < 23) {
                                    col.put(COLUMN_VALUE, "false");
                                } else {
                                    col.put(COLUMN_VALUE, "true");
                                }
                                itemCusFields.put(escapeHtml("VALID_AGE"), col);
                            }
                        }
                    }
                }
                customFields.put(ACCOUNT, itemCusFields);
            }
        }

        if (invoiceData != null && clientSupplier == null
                && invoiceData.getCustomFieldItems() != null && !invoiceData.getCustomFieldItems().isEmpty()) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : invoiceData.getCustomFieldItems()) {
                if (item != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                    if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                        String dateValue = "";
                        EdsCompany company = userManager.getUser().getCompany();
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        if (item.getFieldDateNonConvertedValue() != null) {
                            if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                dateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else {
                                dateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            }
                        }
                        cols.put(COLUMN_VALUE, dateValue);
                    } else if (CompanyCustomFieldItem.NUMBER.equals(item.getDataType())) {
                        cols.put(COLUMN_VALUE, item.getFieldStringValue() != null && !"".equals(item.getFieldStringValue()) ? escapeHtml(numberFormat.format(Double.valueOf(item.getFieldStringValue()))) : null);
                    } else {
                        cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                    }
                    if (item.getFieldName() != null) {
                        itemCusFields.put(item.getFieldName(), cols);
                    }
                }
            }
            customFields.put(PDFConstants.INVOICE, itemCusFields);
        }
        return customFields;
    }

    protected CustomisedITextTable getConsignorAndConsigneeTable(Integer consignorID, Integer consigneeID) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(NAME, BILL_ADDRESS, BILL_CITY, BILL_STATE, BILL_ZIPCODE, BILL_COUNTRY);
        if (consignorID != null) {
            EdsCrmAccount consignor = crmAccountManager.get(consignorID);
            if (consignor != null && consignor.getBillingAddress() != null) {
                EdsAddress address = consignor.getBillingAddress();
                table.addRowWithCode(CONSIGNOR, consignor.getName(),
                        (address.getAddress() != null ? address.getAddress() : ""),
                        (address.getCity() != null ? address.getCity() : ""),
                        (address.getState() != null ? address.getState().getName() : ""),
                        (address.getZipCode() != null ? address.getZipCode() : ""),
                        (address.getCountry() != null ? address.getCountry().getName() : ""));
            }
        }
        if (consigneeID != null) {
            EdsCrmAccount consignee = crmAccountManager.get(consigneeID);
            if (consignee != null && consignee.getBillingAddress() != null) {
                EdsAddress address = consignee.getBillingAddress();
                table.addRowWithCode(CONSIGNEE, consignee.getName(),
                        (address.getAddress() != null ? address.getAddress() : ""),
                        (address.getCity() != null ? address.getCity() : ""),
                        (address.getState() != null ? address.getState().getName() : ""),
                        (address.getZipCode() != null ? address.getZipCode() : ""),
                        (address.getCountry() != null ? address.getCountry().getName() : ""));
            }
        }
        return table;
    }

    protected <ClientOrSupplier extends EdsCrmAccount> Map<String, String> getBillToAddressMap(ClientOrSupplier clientOrSupplier, EdsCrmContact crmContact, NewInvoice data, boolean customised) {
        String contactName = "", firstName = null, lastName = null, middleName = null, Email = null, Phone = null, jobTitle = null;
        EdsCompany company = userManager.getUser().getCompany();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(company, null);
        String customerPaymentMethod = "";
        if (clientOrSupplier != null && clientOrSupplier.getPaymentMethod() != null) {
            customerPaymentMethod = clientOrSupplier.getPaymentMethod().getName();
        }
        if (crmContact != null) {
            firstName = crmContact.getFirstName() != null ? crmContact.getFirstName().replace("&", "&amp;") : "";
            lastName = crmContact.getLastName();
            middleName = crmContact.getMiddleName();
            Phone = crmContact.getPrimaryPhone() != null ? crmContact.getPrimaryPhone().replace("|", "") : "";
            Email = crmContact.getPrimaryEmail();
            jobTitle = crmContact.getJobTitles();

            String title = (crmContact != null && crmContact.getTitle() != null && !"".equals(crmContact.getTitle().trim())) ? crmContact.getTitle().trim() : "";
            if (company != null && company.getObjectID().equals(8175)) {//8175 --> Portiva
                if ("Mr.".equals(title)) {
                    title = "de heer";
                } else if ("Mrs.".equals(title)) {
                    title = "mevrouw";
                }
                contactName = ("".equals(title) ? "" : title + " ") + crmContact.getFullName();
            } else {
                contactName = ("".equals(title) ? "" : title + " ") + crmContact.getName();
            }
        }

        EdsAddress billAddress = data.getBillAddressID() != null ? addressManager.get(data.getBillAddressID()) : null;
        EdsAddress mailAddress = data.getMailAddressID() != null ? addressManager.get(data.getMailAddressID()) : null;

        if (billAddress != null && billAddress.isDeleted()) {
            Integer primaryBillableAddressId = clientOrSupplier.getPrimaryBillingAddressId();

            if (primaryBillableAddressId != null) {
                billAddress = addressManager.get(primaryBillableAddressId);
            }
        }

        Map<String, String> values = new HashMap<>();

        boolean isClient = isClient();

        values.put(TYPE, isClient ? RECEIVABLE : PAYABLE);
        values.put(HEADER, isClient ? commonLocalizer.localize(PdfLocalizationName.customer) : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.supplier));
        values.put(BILL_TO_HEADER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billTo));
        values.put(SHIP_TO_HEADER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shipTo));

        values.put(NAME, clientOrSupplier.getName());
        //values.put(ACCOUNT_OWNER, clientOrSupplier.getOwner() != null ? clientOrSupplier.getOwner().getName() : "");
        values.put(ACCOUNT_OWNER, clientOrSupplier.getOwners() != null && !clientOrSupplier.getOwners().isEmpty() ? clientOrSupplier.getOwners().get(0).getName() : "");
        values.put(CLIENT_CONTACT, contactName);
        values.put(CLIENT_PHONE, clientOrSupplier.getPhone() != null ? clientOrSupplier.getPhone().replace("|", "") : "");
        values.put(CLIENT_FAX, clientOrSupplier.getFax() != null ? clientOrSupplier.getFax().replace("|", "") : "");
        if (SALE_INVOICE.equals(getFromInvoice())) {
            values.put(CLIENT_VAT_NUMBER, clientOrSupplier.getVatNumber() != null ? pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.vatNumberLabel) + ": " + clientOrSupplier.getVatNumber() : "");
        }
        //BILL ADDRESS FOR CLIENT (SI,SQ,SO), SUPPLIER (PI,PO)
        putBillAddressValues(values, billAddress, customised, "");
        //MAILING ADDRESS FOR CLIENT(SI,SQ,SO), SUPPLIER (PI, PO)
        if (customised) {
            putMailAddressValues(values, mailAddress, customised, "");
        } else {
            if (SALE_INVOICE.equals(getFromInvoice())) {
                if (data.getInvoiceType() == null || PRODUCT_INVOICE_TYPE.equals(data.getInvoiceType())) {
                    putMailAddressValues(values, mailAddress, customised, "");
                }
            } else {
                putMailAddressValues(values, mailAddress, customised, "");
            }
        }

        if (PdfReferenceCodeNameEnum.PACKING_SLIP.equals(getPdfCodeName(null)) || PdfReferenceCodeNameEnum.SO_PACKING_SLIP.equals(getPdfCodeName(null))) {
            putCompanyAddress(values, company, true, mailAddress);
        }

        if (!isClient) {
            //BILL TO ADDRESS FOR PURCHASE INVOICE/ORDER
            if (data.getClientItem() != null && data.getClientItem().getId() != null) {
                EdsCrmAccount pClient = crmAccountManager.get(data.getClientItem().getId());
                if (pClient != null) {
                    values.put(PURCHASE_CLIENT_HEADER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.customer));
                    values.put(PURCHASE_CLIENT_NAME, pClient.getName());
                    values.put(PURCHASE_CLIENT_EMAIL, pClient.getEmail());
                    values.put(PURCHASE_CLIENT_PHONE, pClient.getPhone() != null ? pClient.getPhone().replace("|", "") : "");
                    if (pClient.getPrimaryContact() != null) {
                        values.put(PURCHASE_CLIENT_CONTACT_NAME, pClient.getPrimaryContact().getName());
                    }
                    if (data.getClientItem().getBillAddressID() != null) {
                        putBillAddressValues(values, addressManager.get(data.getClientItem().getBillAddressID()), customised, "COMP_");
                    }
                    if (data.getClientItem().getMailAddressID() != null) {
                        putMailAddressValues(values, addressManager.get(data.getClientItem().getMailAddressID()), customised, "COMP_");
                    }
                } else {
                    putCompanyAddress(values, company, customised, mailAddress);
                }
            } else {
                putCompanyAddress(values, company, customised, mailAddress);
            }
        }

        if (customised) {
            values.put(PARENT_ACCOUNT, clientOrSupplier.getParent() != null && clientOrSupplier.getParent().getName() != null ? clientOrSupplier.getParent().getName() : "");
            values.put(CLIENT_CURRENCY, clientOrSupplier.getCurrency() != null && clientOrSupplier.getCurrency().getName() != null ? clientOrSupplier.getCurrency().getName() : "");
            values.put(CLIENT_VAT_NUMBER, clientOrSupplier.getVatNumber() != null && StringUtils.isNotBlank(clientOrSupplier.getTrn()) ? escapeHtml(clientOrSupplier.getTrn()) : escapeHtml(clientOrSupplier.getVatNumber()));
            values.put(PDFConstants.CLIENT_CODE, clientOrSupplier.getNumber() != null ? escapeHtml(clientOrSupplier.getNumber()) : "");
            //values.put(CLIENT_OWNER, clientOrSupplier.getOwner() != null ? escapeHtml(clientOrSupplier.getOwner().getName()) : "");
            values.put(CLIENT_OWNER, clientOrSupplier.getOwners() != null && !clientOrSupplier.getOwners().isEmpty() ? escapeHtml(clientOrSupplier.getOwners().get(0).getName()) : "");
            values.put(CONTACT_FIRST_NAME, firstName != null ? firstName : "");
            values.put(CONTACT_LAST_NAME, lastName != null ? lastName : "");
            values.put(CONTACT_MIDDLE_NAME, middleName != null ? middleName : "");
            values.put(CONTACT_PHONE, Phone != null ? Phone : "");
            values.put(CONTACT_EMAIL, Email != null ? Email : "");
            values.put(CONTACT_JOB_TITLE, jobTitle != null ? jobTitle : "");
            values.put(CLIENT_EMAIL, clientOrSupplier.getEmail() != null ? clientOrSupplier.getEmail() : "");
            values.put(PAYMENT_METHOD, escapeHtml(customerPaymentMethod));
            values.put(CLIENT_WEBSITE, escapeHtml(clientOrSupplier.getWebsite()));
            values.put("CLIENT_REGISTRATION_NUMBER", escapeHtml(clientOrSupplier.getRegistrationNumber()));
            values.put("CLIENT_BANK_NAME", escapeHtml(clientOrSupplier.getBankName()));
            values.put("CLIENT_BANK_ACCOUNT_NAME", escapeHtml(clientOrSupplier.getAccountName()));
            values.put("CLIENT_BANK_ACCOUNT_NO", escapeHtml(clientOrSupplier.getAccountNo()));
            values.put("CLIENT_BANK_BRANCH", escapeHtml(clientOrSupplier.getBranch()));
            values.put("CLIENT_BANK_ADDRESS", escapeHtml(clientOrSupplier.getBankAddress()));
            values.put("CLIENT_BANK_SWIFT_CODE", escapeHtml(clientOrSupplier.getSwiftCode()));
            values.put("CLIENT_BANK_SORT_CODE", escapeHtml(clientOrSupplier.getSortCode()));
            values.put("CLIENT_BANK_IBAN_CODE", escapeHtml(clientOrSupplier.getIbanCode()));
            values.put(CRNUMBER, clientOrSupplier.getCrNumber() != null ? escapeHtml(clientOrSupplier.getCrNumber()) : "");
            values.put("CLIENT_TAX_TREATMENT", clientOrSupplier.getTaxTreatment() != null ? escapeHtml(clientOrSupplier.getTaxTreatment().getName()) : "");

            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_ZALOG)) {
                BigDecimal zalog = invoicePaymentManager.getCrmAccountTotalAmount(clientOrSupplier.getObjectID(), data.getID());
                values.put("CLIENT_ZALOG", escapeHtml(priceScaleFormat.format(zalog)));
            }

            values.put("SHIP_TO_LABEL", pdfWfmMessageSource.localize(PdfLocalizationName.shipTo));
            values.put("SUPPLIER_LABEL", pdfWfmMessageSource.localize(PdfLocalizationName.supplier));

            BigDecimal customerPrePaymentBalance = invoiceManager.getCustomerPrePaymentBalance(clientOrSupplier.getObjectID());
            if (customerPrePaymentBalance != null) {
                values.put(CLIENT_PRE_PAYMENT_BALANCE, priceScaleFormat.format(customerPrePaymentBalance));
            }
        }
        return values;
    }

    private void putBillAddressValues(Map<String, String> values, EdsAddress billAddress, boolean customised, String keyPrefix) {
        if (billAddress != null) {
            values.put(keyPrefix + BILL_ADDRESS_NAME, billAddress.getName() != null ? billAddress.getName() : "");
            values.put(keyPrefix + BILL_ADDRESS, billAddress.getAddress() != null ? billAddress.getAddress() : "");
            values.put(keyPrefix + BILL_ADDRESS2, billAddress.getAddressb() != null ? billAddress.getAddressb() : "");
            values.put(keyPrefix + BILL_COUNTRY, billAddress.getCountry() != null ? billAddress.getCountry().getName() : "");
            values.put(keyPrefix + BUILDING_NUMBER, billAddress.getBuildingNumber() != null ? billAddress.getBuildingNumber() : "");
            values.put(keyPrefix + PLOT_IDENTIFICATION, billAddress.getPlotIdentification() != null ? billAddress.getPlotIdentification() : "");
            values.put(keyPrefix + CITY_SUBDIVISION_NAME, billAddress.getCitySubdivisionName() != null ? billAddress.getCitySubdivisionName() : "");
            if (billAddress.getCountry() != null && billAddress.getCountry().getCode() != null && EU_MEMBERS.contains(billAddress.getCountry().getCode())) {
                values.put(keyPrefix + BILL_COUNTRY_EU_MEMBER, "TRUE");
            } else {
                values.put(keyPrefix + BILL_COUNTRY_EU_MEMBER, "FALSE");
            }
            if (customised) {
                values.put(keyPrefix + BILL_CITY, billAddress.getCity() != null ? billAddress.getCity() : "");
                values.put(keyPrefix + BILL_STATE, (billAddress.getState() != null && billAddress.getState().getName() != null) ? billAddress.getState().getName() : "");
                values.put(keyPrefix + BILL_ZIPCODE, billAddress.getZipCode() != null ? billAddress.getZipCode() : "");
            } else {
                values.put(keyPrefix + BILL_CITYSTATEZIP, getCityStateZipAsString(billAddress.getCity(), billAddress.getState(), billAddress.getZipCode()));
            }
        }
    }

    private void putMailAddressValues(Map<String, String> values, EdsAddress mailAddress, boolean customised, String keyPrefix) {
        if (mailAddress != null) {
            values.put(keyPrefix + MAIL_ADDRESS_NAME, mailAddress.getName() != null ? mailAddress.getName() : "");
            values.put(keyPrefix + MAIL_ADDRESS, mailAddress.getAddress() != null ? mailAddress.getAddress() : "");
            values.put(keyPrefix + MAIL_ADDRESS2, mailAddress.getAddressb() != null ? mailAddress.getAddressb() : "");
            values.put(keyPrefix + MAIL_COUNTRY, mailAddress.getCountry() != null ? mailAddress.getCountry().getName() : "");
            if (mailAddress.getCountry() != null && mailAddress.getCountry().getCode() != null && EU_MEMBERS.contains(mailAddress.getCountry().getCode())) {
                values.put(keyPrefix + MAIL_COUNTRY_EU_MEMBER, "TRUE");
            } else {
                values.put(keyPrefix + MAIL_COUNTRY_EU_MEMBER, "FALSE");
            }
            if (customised) {
                values.put(keyPrefix + MAIL_CITY, mailAddress.getCity() != null ? mailAddress.getCity() : "");
                values.put(keyPrefix + MAIL_STATE, (mailAddress.getState() != null && mailAddress.getState().getName() != null) ? mailAddress.getState().getName() : "");
                values.put(keyPrefix + MAIL_ZIPCODE, mailAddress.getZipCode() != null ? mailAddress.getZipCode() : "");
            } else {
                values.put(keyPrefix + MAIL_CITYSTATEZIP, getCityStateZipAsString(mailAddress.getCity(), mailAddress.getState(), mailAddress.getZipCode()));
            }
        }
    }

    private void putCompanyAddress(Map<String, String> values, EdsCompany company, boolean customised, EdsAddress mailAddress) {
        values.put(COMP_BILL_ADDRESS, company.getAddress1() != null ? company.getAddress1() : "");
        values.put(COMP_BILL_ADDRESS2, company.getBillAddress2() != null ? company.getBillAddress2() : "");
        if (company.getCountryZone() != null && company.getCountryZone().getCountry() != null && company.getCountryZone().getCountry().getName() != null) {
            values.put(COMP_BILL_COUNTRY, company.getCountryZone().getCountry().getName());
        }
        if (customised) {
            values.put(COMP_BILL_CITY, company.getCity() != null ? company.getCity() : "");
            values.put(COMP_BILL_STATE, (company.getCountryRegion() != null && company.getCountryRegion().getName() != null) ? company.getCountryRegion().getName() : "");
            values.put(COMP_BILL_ZIPCODE, company.getPostCode() != null ? company.getPostCode() : "");
        } else {
            values.put(COMP_BILL_CITYSTATEZIP, getCityStateZipAsString(company.getCity(), company.getCountryRegion(), company.getPostCode()));
        }
        //SHIP TO ADDRESS FOR PURCHASE INVOICE/ORDER
        if (mailAddress != null && !mailAddress.getAddressDataAsHTML().isEmpty()) {
            values.put(COMP_MAIL_ADDRESS, mailAddress.getAddress() != null ? mailAddress.getAddress() : "");
            values.put(COMP_MAIL_ADDRESS2, mailAddress.getAddressb() != null ? mailAddress.getAddressb() : "");
            if (mailAddress.getCountryName() != null) {
                values.put(COMP_MAIL_COUNTRY, mailAddress.getCountryName());
            }
            if (customised) {
                values.put(COMP_MAIL_CITY, mailAddress.getCity() != null ? mailAddress.getCity() : "");
                values.put(COMP_MAIL_STATE, mailAddress.getStateName() != null ? mailAddress.getStateName() : "");
                values.put(COMP_MAIL_ZIPCODE, mailAddress.getZipCode() != null ? mailAddress.getZipCode() : "");
            } else {
                values.put(COMP_MAIL_CITYSTATEZIP, getCityStateZipAsString(mailAddress.getCity(), mailAddress.getState(), mailAddress.getZipCode()));
            }
        } else {
            values.put(COMP_MAIL_ADDRESS, company.getAddress2() != null ? company.getAddress2() : "");
            values.put(COMP_MAIL_ADDRESS2, company.getMailAddress2() != null ? company.getMailAddress2() : "");
            if (company.getMailingCountryName() != null) {
                values.put(COMP_MAIL_COUNTRY, company.getMailingCountryName());
            }
            if (customised) {
                values.put(COMP_MAIL_CITY, company.getMailingCity() != null ? company.getMailingCity() : "");
                values.put(COMP_MAIL_STATE, (company.getMailingCountryRegion() != null && company.getMailingCountryRegion().getName() != null) ? company.getMailingCountryRegion().getName() : "");
                values.put(COMP_MAIL_ZIPCODE, company.getMailingPostCode() != null ? company.getMailingPostCode() : "");
            } else {
                values.put(COMP_MAIL_CITYSTATEZIP, getCityStateZipAsString(company.getMailingCity(), company.getMailingCountryRegion(), company.getMailingPostCode()));
            }
        }
    }

    private String getCityStateZipAsString(String city, EdsRegion state, String zipCode) {
        StringBuilder cityStateZip = new StringBuilder();
        if (city != null && !"".equals(city.trim())) {
            cityStateZip.append(city.trim());
        }
        if (state != null && state.getName() != null && !"".equals(state.getName().trim())) {
            if (cityStateZip.length() > 0) {
                cityStateZip.append(", ");
            }
            cityStateZip.append(state.getName().trim());
        }
        if (zipCode != null && !"".equals(zipCode.trim())) {
            if (cityStateZip.length() > 0) {
                cityStateZip.append(", ");
            }
            cityStateZip.append(zipCode.trim());
        }
        return cityStateZip.toString();
    }

    /**
     * addRow array elements values equals true sum equals to column count
     * <p/>
     * if (addData[0]) <b> Reciept Number </b>
     * if (addData[1]) <b> Invoice Number </b>
     * if (addData[2]) <b> Quote Number </b>
     * if (addData[3]) <b> Purchase Order Number </b>
     * if (addData[4]) <b> Reference </b>
     * if (addData[5]) <b> Tax Number </b>
     * if (addData[6]) <b> Invoice Date </b>
     * if (addData[7]) <b> Invoice due Date </b>
     * if (addData[8]) <b> Invoice Receipt Date </b>
     * if (addData[9]) <b> Payment Date </b>
     * if (addData[10]) <b> Invoice Discount </b>
     * <p/>
     * Number and Dates Table
     *
     * @param invoiceData invoice data
     * @param edsUser     current user
     * @param rowKeys     rows to show
     * @return
     */
    protected ITextTableList getNumberAndDatesTableData(NewInvoice invoiceData, EdsUser edsUser, Map<String, String> rowKeys) {
        final Map<String, String> values = this.getNumberAndDatesTableMap(invoiceData, edsUser, false);
        final ITextTableList numberAndDates = new ITextTableList(2);

        if (rowKeys.containsKey(RECEIPT_NO)/*addRow[0]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(RECEIPT_NO)/*columnName[k++]*/, values.get(RECEIPT_NO));
        }
        if (rowKeys.containsKey(INV_NUMBER)/*addRow[1]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(INV_NUMBER)/*columnName[k++]*/, values.get(INV_NUMBER));
        }
        if (rowKeys.containsKey(QT_NUMBER)/*addRow[2]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(QT_NUMBER)/*columnName[k++]*/, values.get(QT_NUMBER));
        }
        if (rowKeys.containsKey(PO_NUMBER)/*addRow[3]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(PO_NUMBER)/*columnName[k++]*/, values.get(PO_NUMBER));
        }
        if (rowKeys.containsKey(REFERENCE)/*addRow[4]*/ && !values.get(REFERENCE).equals("") && values.get(REFERENCE) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(REFERENCE)/*columnName[k++]*/, values.get(REFERENCE));
        }
        if (rowKeys.containsKey(COMP_VAT_NUMBER)/*addRow[5]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(COMP_VAT_NUMBER)/*columnName[k++]*/, values.get(COMP_VAT_NUMBER));
        }
        if (rowKeys.containsKey(PROJECT_NAME) && values.get(PROJECT_NAME) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(PROJECT_NAME)/*columnName[k++]*/, values.get(PROJECT_NAME));
        }
        if (rowKeys.containsKey(PROJECT_CODE_ONLY) && values.get(PROJECT_CODE_ONLY) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(PROJECT_CODE_ONLY)/*columnName[k++]*/, values.get(PROJECT_CODE_ONLY));
        }
        if (rowKeys.containsKey(INV_DATE)/*addRow[6]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(INV_DATE)/*columnName[k++]*/, values.get(INV_DATE));
        }
        if (rowKeys.containsKey(INVOICE_DUE_TERMS) && values.get(INVOICE_DUE_TERMS) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(INVOICE_DUE_TERMS), values.get(INVOICE_DUE_TERMS));
        } else if (rowKeys.containsKey(INV_DUE_DATE)/*addRow[7]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(INV_DUE_DATE)/*columnName[k++]*/, values.get(INV_DUE_DATE));
        }
        if (rowKeys.containsKey(RECEIPT_DATE)/*addRow[8]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(RECEIPT_DATE)/*columnName[k++]*/, values.get(RECEIPT_DATE));
        }
        if (rowKeys.containsKey(PAYMENT_DATE)/*addRow[9]*/) {
            numberAndDates.addPdfTableRows(rowKeys.get(PAYMENT_DATE)/*columnName[k++]*/, values.get(PAYMENT_DATE));
        }
        if (values.containsKey(CANCEL_DATE) && values.get(CANCEL_DATE) != null) {
            numberAndDates.addPdfTableRows(accountingLocalizer.localizeAccounting(PdfLocalizationName.cancelDate), values.get(CANCEL_DATE));
        }
        if (values.containsKey(QRCODE) && values.get(QRCODE) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(QRCODE), values.get(QRCODE));
        }
        if (values.containsKey(QRCODE_GOOGLE) && values.get(QRCODE_GOOGLE) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(QRCODE_GOOGLE), values.get(QRCODE_GOOGLE));
        }
        if (values.containsKey(INV_DATE_UNIQUE_FORMAT) && values.get(INV_DATE_UNIQUE_FORMAT) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(INV_DATE_UNIQUE_FORMAT), values.get(INV_DATE_UNIQUE_FORMAT));
        }
        if (values.containsKey(INV_DUE_DATE_UNIQUE_FORMAT) && values.get(INV_DUE_DATE_UNIQUE_FORMAT) != null) {
            numberAndDates.addPdfTableRows(rowKeys.get(INV_DUE_DATE_UNIQUE_FORMAT), values.get(INV_DUE_DATE_UNIQUE_FORMAT));
        }
        return numberAndDates;
    }

    protected CustomisedITextTable getCustomNumberAndDatesTable(NewInvoice invoice, EdsUser edsUser, String[] codes, String[] labels) {
        DecimalFormat priceFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoice.getPdfTemplateID());

        Map<String, String> values = getNumberAndDatesTableMap(invoice, edsUser, true);
        CustomisedITextTable numAndDates = new CustomisedITextTable();
        numAndDates.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        for (int i = 0; i < codes.length; i++) {
            numAndDates.addRowWithCode(codes[i], labels[i], values.get(codes[i]), codes[i]);
        }
        numAndDates.addRowWithCode(INV_TYPE, accountingLocalizer.localizeAccounting(PdfLocalizationName.invoiceType), escapeHtml(String.valueOf(invoice.getInvoiceType())), INV_TYPE);
        String updateDate = longDateFormat(invoice.getLastUpdateDate());
        numAndDates.addRowWithCode(LAST_UPDATED_DATE, commonLocalizer.localizeAccounting(PdfLocalizationName.modifiedDate), escapeHtml(updateDate), LAST_UPDATED_DATE);
        numAndDates.addRowWithCode(LAST_UPDATER, commonLocalizer.localizeAccounting(PdfLocalizationName.modifiedBy), escapeHtml(invoice.getLastUpdater()), LAST_UPDATER);

        EdsFinancialSettings edsFinancial = financialSettingsManager.getFinancialSettings();
        if (edsFinancial != null) {
            numAndDates.addRowWithCode(COMP_VAT_NUMBER, (isValid(edsFinancial.getTaxIdDisplayNumber()) ? escapeHtml(edsFinancial.getTaxIdDisplayNumber()) : "Tax Reg"),
                    escapeHtml(edsFinancial.getTaxIdNumber()), COMP_VAT_NUMBER);
        }

        if (invoice.getRelatedProjectID() != null) {
            EdsProject project = projectManager.get(invoice.getRelatedProjectID());
            if (project != null) {
                if (isValid(project.getName())) {
                    numAndDates.addRowWithCode(PROJECT_NAME, commonLocalizer.localize(PdfLocalizationName.projectName), escapeHtml(project.getName()), PROJECT_NAME);
                }
                if (isValid(project.getNumber())) {
                    numAndDates.addRowWithCode(PROJECT_NUMBER, commonLocalizer.localizeAccounting(PdfLocalizationName.projectNumber), escapeHtml(project.getNumber()), PROJECT_NUMBER);
                }
                if (project.getStartDate() != null) {
                    numAndDates.addRowWithCode(PROJECT_START_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.projectStartDate), dateFormat.format(project.getStartDate()), PROJECT_START_DATE);
                }
                if (project.getDueDate() != null) {
                    numAndDates.addRowWithCode(PROJECT_DUE_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.projectDueDate), dateFormat.format(project.getDueDate()), PROJECT_DUE_DATE);
                }
                if (project.getDescription() != null) {
                    numAndDates.addRowWithCode(PROJECT_DESC, commonLocalizer.localizeAccounting(PdfLocalizationName.projectDescription), escapeHtml(project.getDescription()), PROJECT_DESC);
                }
                if (project.getManager() != null && isValid(project.getManager().getName())) {
                    numAndDates.addRowWithCode(PROJECT_MANAGER, commonLocalizer.localizeAccounting(PdfLocalizationName.projectManager), escapeHtml(project.getManager().getName()), PROJECT_MANAGER);
                }
                StringBuilder bManagers = new StringBuilder();
                String prefix = "";
                if (project.getBackupManagers() != null) {
                    for (EdsEmployee backupManager : project.getBackupManagers()) {
                        bManagers.append(prefix);
                        bManagers.append(backupManager.getFullName());
                        prefix = ", ";
                    }
                    numAndDates.addRowWithCode(PROJECT_BACKUP_MANAGERS, commonLocalizer.localizeAccounting(PdfLocalizationName.backupManager), escapeHtml(bManagers.toString()), PROJECT_BACKUP_MANAGERS);
                }
                StringBuilder accountRelation = new StringBuilder();
                StringBuilder contactRelation = new StringBuilder();
                List<EdsRelation> relations = relationManager.getAllRelations(RelationItem.TYPE_PROJECT, project.getObjectID());
                for (EdsRelation relationItem : relations) {
                    if (RelationItem.TYPE_CONTACT.equals(relationItem.getToType())) {
                        if (contactRelation.length() > 0) {
                            contactRelation.append(",");
                        }
                        contactRelation.append(relationItem.getToName());
                    } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationItem.getToType())) {
                        if (accountRelation.length() > 0) {
                            accountRelation.append(",");
                        }
                        accountRelation.append(relationItem.getToName());
                    }
                }
                numAndDates.addRowWithCode(PROJECT_RELATIONS_CONTACT, "Project Contact Links", escapeHtml(contactRelation.toString()), PROJECT_RELATIONS_CONTACT);
                numAndDates.addRowWithCode(PROJECT_RELATIONS_ACCOUNT, "Project Account Links", escapeHtml(accountRelation.toString()), PROJECT_RELATIONS_ACCOUNT);
                List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), commonService.getCompanyCustomFields(ViewName.Project));
                for (CompanyCustomFieldItem cfi : customFieldItems) {
                    numAndDates.addRowWithCode(cfi.getFieldName(), "Custom Fields", escapeHtml(cfi.getFieldStringValue()), cfi.getFieldName());
                }
            }
        }
        if (invoice.getOpportunityID() != null) {
            EdsOpportunity opportunity = opportunityManager.get(invoice.getOpportunityID());
            if (opportunity != null) {
                if (isValid(opportunity.getNumber())) {
                    numAndDates.addRowWithCode(OPPORTUNITY_NUMBER, crmLocalizer.localize(PdfLocalizationName.opportunityNumber), escapeHtml(opportunity.getNumber()), OPPORTUNITY_NUMBER);
                }
                if (isValid(opportunity.getName())) {
                    numAndDates.addRowWithCode(OPPORTUNITY_NAME, commonLocalizer.localize(PdfLocalizationName.opportunityName), escapeHtml(opportunity.getName()), OPPORTUNITY_NAME);
                }
            }
        }
        if (invoice.getCurrentApproverSelectItem() != null) {
            String quoteApprover = escapeHtml(invoice.getCurrentApproverSelectItem().getName());
            numAndDates.addRowWithCode(SALE_QUOTE_APPROVER, commonLocalizer.localizeAccounting(PdfLocalizationName.approver), quoteApprover, SALE_QUOTE_APPROVER);
        } else if (invoice.getID() != null) {
            EdsSaleQuote saleQuote = quoteManager.getSaleQuote(invoice.getID());
            if (saleQuote != null && saleQuote.getCurrentApprover() != null && saleQuote.getCurrentApprover().getExactEmployee() != null) {
                //quote approver when quote converted to sales order
                String orderApprover = escapeHtml(saleQuote.getCurrentApprover().getExactEmployee().getName());
                numAndDates.addRowWithCode(SALE_QUOTE_APPROVER, commonLocalizer.localizeAccounting(PdfLocalizationName.approver), orderApprover, SALE_QUOTE_APPROVER);
            }
        }
        if (invoice.getID() != null) {
            EdsBaseInvoice baseInvoice = invoiceManager.get(invoice.getID());
            String invoiceApprover = baseInvoice != null && baseInvoice.getCurrentApprover() != null && baseInvoice.getCurrentApprover().getExactEmployee() != null ? escapeHtml(baseInvoice.getCurrentApprover().getExactEmployee().getFullName()) : "";
            numAndDates.addRowWithCode("SALE_INVOICE_APPROVER", commonLocalizer.localizeAccounting(PdfLocalizationName.approver), invoiceApprover, "SALE_INVOICE_APPROVER");
        }
        if (invoice.getPreviosBalance() != null) {
            numAndDates.addRowWithCode(PREVIOUS_BALANCE, accountingLocalizer.localizeAccounting(PdfLocalizationName.previousBalance), priceFormat.format(invoice.getPreviosBalance()), PREVIOUS_BALANCE);
        }
        if (invoice.getPaymentsReceived() != null) {
            numAndDates.addRowWithCode(PAYMENTS_RECEIVED, accountingLocalizer.localizeAccounting(PdfLocalizationName.paymentsReceived), priceFormat.format(invoice.getPaymentsReceived()), PAYMENTS_RECEIVED);
        }
        if (invoice.getPreviosBalance() != null || invoice.getPaymentsReceived() != null) {
            BigDecimal paymentAdjustment = (invoice.getPreviosBalance() != null ? invoice.getPreviosBalance() : ZERO)
                    .subtract(invoice.getPaymentsReceived() != null ? invoice.getPaymentsReceived() : ZERO);
            if (paymentAdjustment.compareTo(ZERO) >= 0) {
                numAndDates.addRowWithCode(PAYMENT_ADJUSTMENT, accountingLocalizer.localizeAccounting(PdfLocalizationName.paymentAdjustment), priceFormat.format(paymentAdjustment), PAYMENT_ADJUSTMENT);
            } else {
                numAndDates.addRowWithCode(PAYMENT_ADJUSTMENT, accountingLocalizer.localizeAccounting(PdfLocalizationName.paymentAdjustment), "(" + priceFormat.format(paymentAdjustment.abs()) + ")", PAYMENT_ADJUSTMENT);
            }
        }
        if ((getFromInvoice() == null || SALE_QUOTE.equals(getFromInvoice())) && invoice.isSalesOrder()) {
            EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(invoice.getID());
            if (pickList != null && pickList.getGrossWeight() != null) {
                numAndDates.addRowWithCode(PICK_GROSS_WEIGHT, "Gross Weight", priceFormat.format(pickList.getGrossWeight()), PICK_GROSS_WEIGHT);
            }
            if (pickList != null) {
                String shipDate = pickList.getShipDate() != null ? dateFormat.format(pickList.getShipDate()) : "";
                String carrierAccountId = escapeHtml(pickList.getCarrierAccountID());
                numAndDates.addRowWithCode(PICK_SHIP_DATE, "Ship Date", shipDate, PICK_SHIP_DATE);
                numAndDates.addRowWithCode(PICK_CARRIER_ACCOUNT_ID, "Carrier Account Id", carrierAccountId, PICK_CARRIER_ACCOUNT_ID);
            }

        } else if ((getFromInvoice() == null || SALE_INVOICE.equals(getFromInvoice())) && !ServerUtils.isNullOrEmpty(invoice.getQuoteNumber())) {
            List<EdsSaleQuote> quoteList = quoteManager.getQuoteByNumber(invoice.getQuoteNumber());
            Integer quoteId = null;
            if (quoteList != null && !quoteList.isEmpty()) {
                quoteId = quoteList.get(0) != null ? quoteList.get(0).getObjectID() : null;
            }
            if (quoteId != null) {
                EdsPickList pickList = pickListManager.getPickListBySaleQuoteID(quoteId);
                if (pickList != null && pickList.getGrossWeight() != null) {
                    numAndDates.addRowWithCode(PICK_GROSS_WEIGHT, "Gross Weight", priceFormat.format(pickList.getGrossWeight()), PICK_GROSS_WEIGHT);
                }
                if (pickList != null) {
                    String shipDate = pickList.getShipDate() != null ? dateFormat.format(pickList.getShipDate()) : "";
                    String carrierAccountId = escapeHtml(pickList.getCarrierAccountID());
                    numAndDates.addRowWithCode(PICK_SHIP_DATE, "Ship Date", shipDate, PICK_SHIP_DATE);
                    numAndDates.addRowWithCode(PICK_CARRIER_ACCOUNT_ID, "Carrier Account Id", carrierAccountId, PICK_CARRIER_ACCOUNT_ID);
                }
            }
        }
        String fromQuoteDate = "";
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());
        if ((getFromInvoice() == null || SALE_INVOICE.equals(getFromInvoice())) && invoice.getConvertedItemID() != null) {
            EdsSaleQuote edsSaleQuote = invoiceManager.getSaleQuote(invoice.getConvertedItemID());
            fromQuoteDate = edsSaleQuote != null && edsSaleQuote.getInvoiceDate() != null ?
                    shortDateFormat.format(edsSaleQuote.getInvoiceDate()) :
                    "";
            String convertedQuoteApprover = edsSaleQuote != null && edsSaleQuote.getCurrentApprover() != null &&
                    edsSaleQuote.getCurrentApprover().getExactEmployee() != null ?
                    escapeHtml(edsSaleQuote.getCurrentApprover().getExactEmployee().getName()) :
                    "";
            numAndDates.addRowWithCode(CONVERTED_QUOTE_APPROVER, "Quote Approver", convertedQuoteApprover, CONVERTED_QUOTE_APPROVER);
        }
        Double balance = 0d;
        if (RECEIVABLE.equals(invoice.getType())) {
            balance = crmAccountManager.getClientBalance(invoice.getClientID()).doubleValue();
        } else {
            EdsCrmAccount clientBase = crmAccountManager.get(invoice.getClientID());
            balance = crmAccountManager.getSupplierBalance(clientBase.getObjectID()).doubleValue();
        }
        numAndDates.addRowWithCode(CUSTOMER_BALANCE, accountingLocalizer.localizeAccounting(PdfLocalizationName.balance), priceFormat.format(balance), CUSTOMER_BALANCE);
        numAndDates.addRowWithCode(FROM_QUOTE_NUMBER, accountingLocalizer.localizeAccounting(PdfLocalizationName.quoteNumber, "Quote Number"), invoice.getFromQuoteNumber() != null ? invoice.getFromQuoteNumber() : "", FROM_QUOTE_NUMBER);
        numAndDates.addRowWithCode(QUOTATION_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.quoteDate, "Quote Date"), invoice.getQuotationDate() != null ? dateFormat.format(invoice.getQuotationDate().getDate()) : fromQuoteDate, QUOTATION_DATE);
        String taxType = "";
        if (invoice != null && invoice.getTaxCalculationType() != null) {
            if (TAX_CALCULATION_INCLUSIVE.equals(invoice.getTaxCalculationType())) {
                taxType = "INCLUSIVE";
            } else if (TAX_CALCULATION_EXCLUSIVE.equals(invoice.getTaxCalculationType())) {
                taxType = "EXCLUSIVE";
            } else {
                taxType = "NO_TAX";
            }
        }
        numAndDates.addRowWithCode(TAX_CODE, "Tax Type", taxType, TAX_CODE);
        String preview = "";
        if (invoice != null && invoice.getID() == null) {
            preview = "Preview";
        }
        numAndDates.addRowWithCode("PREVIEW", "Preview", preview, "PREVIEW");
        if (invoice.isCreditNote() && !ServerUtils.isNullOrEmpty(invoice.getNoteReason())) {
            String noteReason =  NoteInstructionType.valueOf(invoice.getNoteReason()).getValue();
            numAndDates.addRowWithCode("NOTE_REASON", "Note Reason", noteReason, "NOTE_REASON");
        }

        String creationDate = invoice.getCreationDate() != null ? dateFormat.format(invoice.getCreationDate()) : "";
        String creationTime = invoice.getCreationDate() != null ? timeFormat.format(ServerUtils.convertServerDateToUserDate(invoice.getCreationDate(), edsUser.getUserTimezone())) : "";
        numAndDates.addRowWithCode("CREATION_DATE", "Creation Date", creationDate, "CREATION_DATE");
        numAndDates.addRowWithCode("CREATION_TIME", "Creation Time", creationTime, "CREATION_TIME");
        numAndDates.addRowWithCode("START_DATE", commonLocalizer.localizeAccounting(PdfLocalizationName.startDate));
        numAndDates.addRowWithCode("END_DATE", commonLocalizer.localizeAccounting(PdfLocalizationName.endDate));
        numAndDates.addRowWithCode("MEMBER", commonLocalizer.localizeAccounting(PdfLocalizationName.member));
        numAndDates.addRowWithCode("PHONE", commonLocalizer.localizeAccounting(PdfLocalizationName.phone));
        numAndDates.addRowWithCode("EMAIL", commonLocalizer.localizeAccounting(PdfLocalizationName.email));


        return numAndDates;
    }

    private Map<String, String> getNumberAndDatesTableMap(NewInvoice invoiceData, EdsUser edsUser, boolean customised) {
        String recieptNo = "";
        String invoiceNumber = invoiceData.getInvoiceNumber() == null ? NOT_AVAILABLE : invoiceData.getInvoiceNumber();
        EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
        if (getFromInvoice() != null && getFromInvoice().equals(SALES_RECEIPT)
                && invoiceSettings.getInvoiceNumberingFormat() != null) {
            String[] invoiceNumFormating = invoiceSettings.getInvoiceNumberingFormat().split("_");
            if (invoiceNumFormating[0] != null) {
                recieptNo = invoiceNumber.replaceFirst(invoiceNumFormating[0], "");
            }
        }
        String vatNumber = "";
        EdsFinancialSettings edsFinancial = financialSettingsManager.getFinancialSettings();
        if (edsFinancial != null && edsFinancial.getTaxIdNumber() != null) {
            vatNumber = edsFinancial.getTaxIdNumber();
        }
        String quoteNumberValue = invoiceData.getQuoteNumber() != null ? invoiceData.getQuoteNumber().trim() : "";
        String poNumberValue = invoiceData.getPoNumber() != null ? invoiceData.getPoNumber().trim() : "";
        String reference = invoiceData.getReference() != null ? invoiceData.getReference().trim() : "";
        String shippingMethod = "";
        if (invoiceData.getShippingMethodID() != null) {
            EdsShippingMethod method = shippingMethodManager.get(invoiceData.getShippingMethodID());
            shippingMethod = method.getName();
        }

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());
        String invoiceDate = "";
        String invoiceDueDate = "";
        Locale locale = new Locale("en", "EN");
        if (edsUser.getCompany() != null && !ServerUtils.isNullOrEmpty(edsUser.getCompany().getLocale())) {
            EdsLocale edsLocale = localeManager.getLocaleBylanguageCode(edsUser.getCompany().getLocale());
            if (edsLocale != null && edsLocale.getLanguageCode() != null && edsLocale.getCountry() != null) {
                locale = new Locale(edsLocale.getLanguageCode(), edsLocale.getCountry());
            }
        }

        SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), locale);
        invoiceDate = invoiceData.getInvoiceDate() != null ? ruDateFormat.format(invoiceData.getInvoiceDate().getNonConvertedDate()) : "";
        invoiceDueDate = invoiceData.getDueDate() != null ? ruDateFormat.format(invoiceData.getDueDate().getNonConvertedDate()) : "";

        SimpleDateFormat uniqueDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String invoiceDateUniqueFormat = invoiceData.getInvoiceDate() != null ? uniqueDateFormat.format(invoiceData.getInvoiceDate().getNonConvertedDate()) : "";
        String invoiceDueDateUniqueFormat = invoiceData.getDueDate() != null ? uniqueDateFormat.format(invoiceData.getDueDate().getNonConvertedDate()) : "";

        String periodStart = invoiceData.getPeriodStart() == null ? "" : shortDateFormat.format(invoiceData.getPeriodStart().getNonConvertedDate());
        String periodEnd = invoiceData.getPeriodEnd() == null ? "" : shortDateFormat.format(invoiceData.getPeriodEnd().getNonConvertedDate());

        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM");
        String startMonth = invoiceData.getPeriodStart() == null ? "" : monthFormatter.format(invoiceData.getPeriodStart().getNonConvertedDate());
        String endMonth = invoiceData.getPeriodEnd() == null ? "" : monthFormatter.format(invoiceData.getPeriodEnd().getNonConvertedDate());

        Long periodDays = null;
        if (invoiceData.getInvoiceDate() != null && invoiceData.getDueDate() != null) {
            periodDays = daysBetween(invoiceData.getInvoiceDate().getNonConvertedDate(), invoiceData.getDueDate().getNonConvertedDate());
        }
        String receiptDate = shortDateFormat.format(edsUser.getCompany().getCompanyDate());
        String statusCode = invoiceData.getStatusCode();
        String relatedInvoiceNumber = invoiceData.getRelatedInvoiceNumber();
        String relatedInvoiceDate = invoiceData.getRelatedInvoiceDate() != null ? shortDateFormat.format(invoiceData.getRelatedInvoiceDate()) : "";
        String currentApprover = invoiceData.getCurrentApproverSelectItem() != null ? invoiceData.getCurrentApproverSelectItem().getName() : "";
        String fromQuoteNumber = invoiceData.getFromQuoteNumber() != null ? invoiceData.getQuoteNumber() : "";
        String quotationDate = invoiceData.getQuotationDate() != null ? shortDateFormat.format(invoiceData.getQuotationDate().getDate()) : "";
        String paymentDateString = "";
        Date paymentDate = null;
        PaymentItem[] paymentItems = invoiceData.getPaymentItems();
        if (paymentItems != null && paymentItems.length > 0) {
            for (PaymentItem paymentItem : paymentItems) {
                if (paymentItem != null) {
                    if (paymentDate == null) {
                        paymentDate = paymentItem.getDate().getNonConvertedDate();
                    } else if (paymentDate.before(paymentItem.getDate().getNonConvertedDate())) {
                        paymentDate = paymentItem.getDate().getNonConvertedDate();
                    }
                }
            }
            if (paymentDate != null) {
                paymentDateString = shortDateFormat.format(paymentDate);
            }
        }


        Map<String, String> values = new HashMap<>();
        if (customised) {
            String period = (invoiceData.getPeriodStart() != null ? shortDateFormat.format(invoiceData.getPeriodStart().getNonConvertedDate()) : "")
                    + (invoiceData.getPeriodStart() != null || invoiceData.getPeriodEnd() != null ? " > " : "")
                    + (invoiceData.getPeriodEnd() != null ? shortDateFormat.format(invoiceData.getPeriodEnd().getNonConvertedDate()) : "");

            values.put(RECEIPT_NO, escapeHtml(recieptNo));
            values.put(INV_NUMBER, escapeHtml(invoiceNumber));
            values.put(QT_NUMBER, escapeHtml(quoteNumberValue));
            values.put(PO_NUMBER, escapeHtml(poNumberValue));
            values.put(REFERENCE, escapeHtml(reference));
            values.put(SHIPPING_METHOD, escapeHtml(shippingMethod));
            values.put(PAYMENT_TERMS, invoiceData.getPaymentTerms() != null ? escapeHtml(invoiceData.getPaymentTerms()) : "");
            values.put(COMP_VAT_NUMBER, escapeHtml(vatNumber));
            values.put(INV_DATE, escapeHtml(invoiceDate));
            values.put(INV_DUE_DATE, escapeHtml(invoiceDueDate));

            values.put(PERIOD_START_DATE, escapeHtml(periodStart));
            values.put(PERIOD_END_DATE, escapeHtml(periodEnd));
            values.put(START_MONTH, escapeHtml(startMonth));
            values.put(END_MONTH, escapeHtml(endMonth));

            if (invoiceData.getInvoiceTermsItem() != null) {
                values.put(INVOICE_DUE_TERMS, invoiceData.getInvoiceTermsItem().getName());
            }
            values.put(PERIOD, escapeHtml(period));
            values.put(RECEIPT_DATE, escapeHtml(receiptDate));
            values.put(PAYMENT_DATE, escapeHtml(paymentDateString));
            values.put(INVOICE_STATUS, escapeHtml(statusCode));
            values.put(PERIOD_DAYS, escapeHtml(periodDays == null ? null : (periodDays.intValue() == 0 ? "1" : Integer.toString(periodDays.intValue()))));
            values.put(RELATED_INVOICE_NUMBER, escapeHtml(relatedInvoiceNumber));
            values.put(RELATED_INVOICE_DATE, escapeHtml(relatedInvoiceDate));
            values.put(APPROVER, escapeHtml(currentApprover));
        } else {
            values.put(RECEIPT_NO, escapeHtml(recieptNo));
            values.put(INV_NUMBER, escapeHtml(invoiceNumber));
            values.put(QT_NUMBER, escapeHtml(quoteNumberValue));
            values.put(PO_NUMBER, escapeHtml(poNumberValue));
            values.put(REFERENCE, escapeHtml(reference));
            if (invoiceData.getRelatedProjectID() != null) {
                EdsProject project = projectManager.get(invoiceData.getRelatedProjectID());
                if (project != null) {
                    values.put(PROJECT_NAME, (project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() + " - " : "") + project.getName());
                    values.put(PROJECT_CODE_ONLY, project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() : "N/A");
                }
            }
            values.put(COMP_VAT_NUMBER, vatNumber);
            values.put(INV_DATE, invoiceDate);
            values.put(INV_DUE_DATE, invoiceDueDate);
            if (invoiceData.getInvoiceTermsItem() != null) {
                values.put(INVOICE_DUE_TERMS, invoiceData.getInvoiceTermsItem().getName());
            }
            values.put(RECEIPT_DATE, receiptDate);
            values.put(PAYMENT_DATE, paymentDateString);
            if (invoiceData.getCancelDate() != null) {
                values.put(CANCEL_DATE, shortDateFormat.format(invoiceData.getCancelDate().getNonConvertedDate()));
            }
        }
        if (!invoiceData.isProjectBasedInvoice()) {
            SimpleDateFormat qrDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String date = qrDateFormat.format(invoiceData.getInvoiceDate().getNonConvertedDate());
            DecimalFormat format = new DecimalFormat("##0.00");
            BigDecimal exchangeRate = invoiceData.getExchageRate().compareTo(ZERO) != 0 ? invoiceData.getExchageRate() : new BigDecimal("1.00");

            String qrBarcodeHash = QRBarcodeEncoder.encode(
                    new Seller(edsUser.getCompany().getName()),
                    new TaxNumber(!vatNumber.equals("") ? vatNumber : NULL),
                    new InvoiceDate(date),
                    new InvoiceTotalAmount((invoiceData.getTotal() != null ? format.format(invoiceData.getTotal().multiply(exchangeRate)) : NULL)),
                    new InvoiceTaxAmount((invoiceData.getTotalTaxes() != null ? format.format(invoiceData.getTotalTaxes()) : NULL))
            );
            StringBuilder url = new StringBuilder();
            StringBuilder apiQrServer = new StringBuilder();
            apiQrServer.append("https://api.qrserver.com/v1/create-qr-code/?size=350x350&margin=40&data=");
            url.append("https://chart.googleapis.com/chart?chs=350x350&cht=qr&chl=");
            if ("SA".equals(companyManager.get(SecurityContext.getCompanyID()).getCountry().getCode())) {
                EdsInvoice saleInvoice = invoiceManager.get(invoiceData.getID());
                if (saleInvoice != null && !ServerUtils.isNullOrEmpty(saleInvoice.getZatcaQRCode()) && "CLEARED".equals(saleInvoice.getZatcaStatus())) {
                    url.append(saleInvoice.getZatcaQRCode());
                    apiQrServer.append(saleInvoice.getZatcaQRCode());
                } else {
                    url.append(qrBarcodeHash);
                    apiQrServer.append(qrBarcodeHash);
                }
            } else {
                url.append(qrBarcodeHash);
                apiQrServer.append(qrBarcodeHash);
            }
            values.put(QRCODE, apiQrServer.toString());
            values.put(QRCODE_GOOGLE, url.toString());
        }
        values.put(FROM_QUOTE_NUMBER, escapeHtml(fromQuoteNumber));
        values.put(QUOTATION_DATE, escapeHtml(quotationDate));
        values.put(INV_DATE_UNIQUE_FORMAT, invoiceDateUniqueFormat);
        values.put(INV_DUE_DATE_UNIQUE_FORMAT, invoiceDueDateUniqueFormat);
        return values;
    }

    /**
     * addColumn Max Length and Min Length equals <b> 10 </b>
     * <p/>
     * addColumn array elements values equals true sum equals to numCol
     * <p/>
     * if(addColumn[0]) <b> No </b>
     * if(addColumn[1]) <b> Product Name </b>
     * if(addColumn[2]) <b> Description </b>
     * if(addColumn[3]) <b> Quantity Units </b>
     * if(addColumn[4]) <b> Unit Measurement </b>
     * if(addColumn[5]) <b> Unit Price </b>
     * if(addColumn[6]) <b> Product Discount </b>
     * if(addColumn[7]) <b> Net Amount </b>
     * if(addColumn[8]) <b> Tax Amount </b>
     * if(addColumn[9]) <b> Total Price </b>
     * if(addColumn[10]) <b> Vat Amount </b>
     * <p/>
     * Add to table columns
     *
     * @param invoiceData
     * @param edsUser
     * @param edsCurrency
     * @return
     */
    protected ITextTableList getProducTableData(NewInvoice invoiceData,
                                                EdsUser edsUser,
                                                EdsCurrency edsCurrency,
                                                Map<String, String> columns) {
        final List<String> header = new LinkedList<>();

        for (Map.Entry<String, String> entry : columns.entrySet()) {
            header.add(entry.getValue());
        }

        ITextTableList productItemTable = new ITextTableList(header.size());
        productItemTable.addPdfTableHeader(header.toArray(new String[]{}));

        DecimalFormat defaultScaleFormat = new DecimalFormat(",##0.00");
        DecimalFormat unitPriceNumberFormat = getUnitPriceNumberFormat(edsUser.getCompany(), null);
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(edsUser.getCompany(), null);
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(edsUser.getCompany(), null);

        Map<Integer, BigDecimal> estAmountsMap = null, priorAmountsMap = null;
        if (invoiceData.getConvertedItemID() != null && PdfReferenceCodeNameEnum.SALES_INVOICE.equals(getPdfCodeName(null))) {
            estAmountsMap = invoiceManager.getEstAmounts(invoiceData.getConvertedItemID());
            priorAmountsMap = invoiceManager.getPriorAmounts(invoiceData.getConvertedItemID(), invoiceData.getID());
        }

        for (int i = 0; i <= invoiceData.getItems().length - 1; i++) {
            NewInvoiceItem item = invoiceData.getItems()[i];
            BigDecimal netAmount = item.getQuantity().multiply(item.getUnitPrice());
            BigDecimal itemDiscount = ZERO;
            if (item.getDiscountPercent() != null) {
                itemDiscount = netAmount.multiply(item.getDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP);
            } else if (item.getDiscountAmount() != null) {
                itemDiscount = item.getDiscountAmount();
            }
            BigDecimal itemDiscount2 = ZERO;
            if (item.getDoubleDiscountPercent() != null) {
                itemDiscount2 = netAmount.multiply(item.getDoubleDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP);
            } else if (item.getDiscountAmount() != null) {
                itemDiscount2 = item.getDoubleDiscountAmount();
            }
            BigDecimal itemNetAmount = netAmount.subtract(itemDiscount.add(itemDiscount2));

            List<CellData> columnsValue = new ArrayList<>();
            String no = (i + 1) + ".", name;
            EdsItem edsItem = null;
            String number = item.getItemNumber();
            if (item.getItemID() != null && item.getItemID() > 0) {
                edsItem = itemManager.get(item.getItemID());
                name = edsItem.getName();
                number = edsItem.getProductNumber();
            } else if (item.getItemName() != null) {
                name = item.getItemName();
            } else {
                name = "";
            }
            String description = item.getDescription() != null ? item.getDescription() : NOT_AVAILABLE;
            String quantityUnits;
            if (invoiceData.isProjectBasedInvoice() && item.isFromTimesheet()) {
                quantityUnits = item.getQuantity() != null ? ServerUtils.timeSpentToString(item.getQuantity().multiply(new BigDecimal(60)).setScale(0, RoundingMode.HALF_UP).intValue()) : "";
            } else {
                quantityUnits = item.getQuantity() != null ? qtyNumberFormat.format(item.getQuantity()) : "";
            }
            quantityUnits += item.getMeasurement() != null ? " " + escapeHtml(item.getMeasurement().getName()) : "";
            String unitPrice = item.getUnitPrice() != null ? unitPriceNumberFormat.format(item.getUnitPrice()) : "";
            String productDiscount = "";
            if (item.getDiscountPercent() != null) {
                productDiscount = defaultScaleFormat.format(item.getDiscountPercent()) + " %";
            } else if (item.getDiscountAmount() != null) {
                productDiscount = priceScaleFormat.format(item.getDiscountAmount()) + " " + getCurrencySymbol(edsCurrency, true);
            }
            String productDiscount2 = "";
            if (item.getDoubleDiscountPercent() != null) {
                productDiscount2 = defaultScaleFormat.format(item.getDoubleDiscountPercent()) + " %";
            } else if (item.getDoubleDiscountAmount() != null) {
                productDiscount2 = priceScaleFormat.format(item.getDoubleDiscountAmount()) + " " + getCurrencySymbol(edsCurrency, true);
            }
            String netData = itemNetAmount != null ? priceScaleFormat.format(itemNetAmount/*.setScale(2, BigDecimal.ROUND_HALF_UP)*/) + "" : NOT_AVAILABLE;

            BigDecimal taxAmountNum = item.getTaxAmount();
            if (taxAmountNum == null) {
                BigDecimal taxPercent = (item.getTaxItem() != null && item.getTaxItem().getEffectiveTaxPercent() != null) ? item.getTaxItem().getEffectiveTaxPercent() : ZERO;
                if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                    taxAmountNum = itemNetAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, RoundingMode.HALF_UP);
                } else {
                    taxAmountNum = itemNetAmount.multiply(taxPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                }
            }

            String taxAmount = priceScaleFormat.format(taxAmountNum);
            boolean isReverseCharge = getPdfCodeName(null) != null && getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.PURCHASE_ORDER) && invoiceData.getType().equalsIgnoreCase(PAYABLE) && invoiceData.getTypeItem() != null && invoiceData.getTypeItem().isReverseChargeApplicable() && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ACCOUNTING_IS_REVERSE_CHARGE);
            String totalPrice = priceScaleFormat.format(item.getTotalAmount().subtract(isReverseCharge && item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO));
//            String totalPrice = priceScaleFormat.format(item.getTotalAmount());

            BigDecimal taxRate = null;
            EdsVat vat = (item.getTaxItem() != null && item.getTaxItem().getId() != null) ? vatManager.get(item.getTaxItem().getId()) : null;
            if (itemNetAmount != null && vat != null) {
                taxRate = vat.getTaxRateAsBigDecimal();
            }
            String taxRateAsString = taxRate != null ? "" + defaultScaleFormat.format(taxRate) : "0.00";

            for (String key : columns.keySet()) {
                switch (key) {
                    case ITEM_NO -> columnsValue.add(new CellData(no));
                    case ITEM_NAME -> {
                        if (number != null && !"".equals(number)) {
                            columnsValue.add(new CellData(number + "->" + name));
                        } else {
                            columnsValue.add(new CellData(name));
                        }
                    }
                    case ITEM_DESCRIPTION -> columnsValue.add(new CellData(description));
                    case ITEM_EST_AMOUNT -> {
                        BigDecimal estAmount = estAmountsMap != null ? estAmountsMap.get(i) : null;
                        columnsValue.add(new CellData((estAmount != null ? priceScaleFormat.format(estAmount) : ""), Element.ALIGN_RIGHT));
                    }
                    case ITEM_PRIOR_AMOUNT -> {
                        BigDecimal priorAmount = priorAmountsMap != null ? priorAmountsMap.get(i) : null;
                        columnsValue.add(new CellData((priorAmount != null ? priceScaleFormat.format(priorAmount) : ""), Element.ALIGN_RIGHT));
                    }
                    case ITEM_QTY_HRS -> columnsValue.add(new CellData(quantityUnits, Element.ALIGN_RIGHT));
                    case ITEM_UNIT_PRICE -> columnsValue.add(new CellData(unitPrice, Element.ALIGN_RIGHT));
                    case ITEM_COMISSION ->
                            columnsValue.add(new CellData(item.getComission() != null ? unitPriceNumberFormat.format(item.getComission()) : "", Element.ALIGN_RIGHT));
                    case ITEM_DISCOUNT -> columnsValue.add(new CellData(productDiscount, Element.ALIGN_RIGHT));
                    case ITEM_DOUBLE_DISCOUNT -> columnsValue.add(new CellData(productDiscount2, Element.ALIGN_RIGHT));
                    case ITEM_ACCOUNT ->
                            columnsValue.add(new CellData(item.getAccountItem() != null ? item.getAccountItem().getName() : ""));
                    case ITEM_NET_AMOUNT -> columnsValue.add(new CellData(netData, Element.ALIGN_RIGHT));
                    case ITEM_TAX_AMOUNT -> columnsValue.add(new CellData(taxAmount, Element.ALIGN_RIGHT));
                    case ITEM_TOTAL_AMOUNT -> columnsValue.add(new CellData(totalPrice, Element.ALIGN_RIGHT));
                    case ITEM_TAX_RATE -> columnsValue.add(new CellData(taxRateAsString, Element.ALIGN_RIGHT));
                    case PROJECT_NAME ->
                            columnsValue.add(new CellData(item.getProject() != null ? item.getProject().getName() : ""));
                    case PDFConstants.CLIENT ->
                            columnsValue.add(new CellData(item.getClient() != null ? item.getClient().getName() : ""));
                    case ITEM_WAREHOUSE ->
                            columnsValue.add(new CellData(item.getWarehouse() != null ? item.getWarehouse().getName() : ""));
                    default -> {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldByCode(key);
                        if (customFieldItem != null) {
                            columnsValue.add(new CellData(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() : ""));
                        } else {
                            columnsValue.add(new CellData(""));
                        }
                    }
                }
            }

            productItemTable.addPdfTableRows(columnsValue.toArray(new CellData[]{}));
        }
        setDefaultFontToTable(edsUser.getCompany(), productItemTable);
        return productItemTable;
    }

    protected ITextTableList getExpenseTableData(NewInvoice invoiceData, EdsUser edsUser, EdsCurrency edsCurrency, LinkedHashMap<String, String> columns) {

        LinkedList<String> header = new LinkedList<>();
        if (columns.containsKey(ITEM_EXPENSE_CATEGORY)) {
            header.add(columns.get(ITEM_EXPENSE_CATEGORY));
        }
        if (columns.containsKey(ITEM_EXPENSE_DESCRIPTION)) {
            header.add(columns.get(ITEM_EXPENSE_DESCRIPTION));
        }
        if (columns.containsKey(ITEM_EXPENSE_TOTAL_AMOUNT)) {
            header.add(columns.get(ITEM_EXPENSE_TOTAL_AMOUNT));
        }

        ITextTableList expenseTableItems = new ITextTableList(header.size());
        expenseTableItems.addPdfTableHeader(header.toArray(new String[]{}));

        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        if (invoiceData.getExpenses() != null) {
            for (int i = 0; i <= invoiceData.getExpenses().size() - 1; i++) {
                BillableExpenseItem item = invoiceData.getExpenses().get(i);
                List<CellData> columnsValue = new ArrayList<>();
                String category = "", description = "", total = "";

                if (item.getAccount() != null) {
                    category = item.getAccount().getName();
                }
                if (item.getDescription() != null) {
                    description = item.getDescription();
                }

                BigDecimal totalAmount = BigDecimal.ZERO;
                totalAmount = totalAmount.add(invoiceData.getCurrencyID().equals(item.getCurrencyID()) && item.getAmountInCurrency() != null
                        ? item.getAmountInCurrency()
                        : item.getAmountInBase() != null && invoiceData.getExchageRate() != null
                        ? item.getAmountInBase().multiply(invoiceData.getExchageRate())
                        : BigDecimal.ZERO);
                totalAmount = totalAmount.add(invoiceData.getCurrencyID().equals(item.getCurrencyID()) && item.getMarkupAmount() != null
                        ? item.getMarkupAmount()
                        : item.getMarkupAmountInBase() != null && invoiceData.getExchageRate() != null
                        ? item.getMarkupAmountInBase().multiply(invoiceData.getExchageRate())
                        : BigDecimal.ZERO);
                totalAmount = totalAmount.add(invoiceData.getCurrencyID().equals(item.getCurrencyID()) && item.getMarkupTaxAmount() != null
                        ? item.getMarkupTaxAmount()
                        : item.getMarkupTaxAmountInBase() != null && invoiceData.getExchageRate() != null
                        ? item.getMarkupTaxAmountInBase().multiply(invoiceData.getExchageRate())
                        : BigDecimal.ZERO);
                total = priceScaleNumberFormat.format(totalAmount);

                if (columns.containsKey(ITEM_EXPENSE_CATEGORY)) {
                    columnsValue.add(new CellData(category));
                }
                if (columns.containsKey(ITEM_EXPENSE_DESCRIPTION)) {
                    columnsValue.add(new CellData(description));
                }
                if (columns.containsKey(ITEM_EXPENSE_TOTAL_AMOUNT)) {
                    columnsValue.add(new CellData(total, Element.ALIGN_RIGHT));
                }

                expenseTableItems.addPdfTableRows(columnsValue.toArray(new CellData[]{}));
            }
        }
        setDefaultFontToTable(edsUser.getCompany(), expenseTableItems);

        return expenseTableItems;

    }

    protected CustomisedITextTable getExpenseCustomTableData(NewInvoice invoiceData, EdsUser edsUser) {
        CustomisedITextTable expenseTable = new CustomisedITextTable();

        /*1 */
        expenseTable.addColumn(ITEM_EXPENSE_CATEGORY, commonLocalizer.localizeAccounting(PdfLocalizationName.category));
        /*2 */
        expenseTable.addColumn(ITEM_EXPENSE_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        /*3 */
        expenseTable.addColumn(ITEM_EXPENSE_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount));
        /*4 */
        expenseTable.addColumn(ITEM_EXPENSE_TOTAL_WITH_MARKUP, "Total with markup");
        /*5 */
        expenseTable.addColumn(ITEM_EXPENSE_MARKUP_AMOUNT, "Markup Amount");
        /*6 */
        expenseTable.addColumn(ITEM_EXPENSE_MARKUP_TAX_AMOUNT, "Markup Tax Amount");
        /*7 */
        expenseTable.addColumn(ITEM_EXPENSE_BEDRAG_AMOUNT, "Bedrag Amount");
        /*8 */
        expenseTable.addColumn(ITEM_EXPENSE_DATE, "Date");
        /*9 */
        expenseTable.addColumn(ITEM_EXPENSE_CURRENCY, "Currency");
        /*10 */
        expenseTable.addColumn(ITEM_EXPENSE_MARKUP_TAX_RATE, "Markup Tax Rate");


        List<String> columnsValue = new ArrayList<>();

        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        if (invoiceData.getExpenses() != null && !invoiceData.getExpenses().isEmpty()) {
            invoiceData.getExpenses().sort(Comparator.comparing(BillableExpenseItem::getDateSort));
            for (int i = 0; i < invoiceData.getExpenses().size(); i++) {
                columnsValue.clear();

                BillableExpenseItem item = invoiceData.getExpenses().get(i);

                String category = "", description = "", total = "", markup = "", markupTax = "", markupTaxRate = "", totalWithMarkup = "", bedrag = "", date = "", currency = "";
                if (item.getAccount() != null) {
                    category = item.getAccount().getName();
                }
                if (item.getDescription() != null) {
                    description = item.getDescription();
                }
                if (item.getAmountInCurrency() != null) {
                    total = priceScaleNumberFormat.format(invoiceData.getCurrencyID().equals(item.getCurrencyID())
                            ? item.getAmountInCurrency()
                            : item.getAmountInBase() != null && invoiceData.getExchageRate() != null
                            ? item.getAmountInBase().multiply(invoiceData.getExchageRate())
                            : BigDecimal.ZERO);
                }
                if (item.getMarkupAmount() != null) {
                    markup = priceScaleNumberFormat.format(invoiceData.getCurrencyID().equals(item.getCurrencyID())
                            ? item.getMarkupAmount()
                            : item.getMarkupAmountInBase() != null && invoiceData.getExchageRate() != null
                            ? item.getMarkupAmountInBase().multiply(invoiceData.getExchageRate())
                            : BigDecimal.ZERO);
                }
                if (item.getMarkupTaxAmount() != null) {
                    markupTax = priceScaleNumberFormat.format(invoiceData.getCurrencyID().equals(item.getCurrencyID())
                            ? item.getMarkupTaxAmount()
                            : item.getMarkupTaxAmountInBase() != null && invoiceData.getExchageRate() != null
                            ? item.getMarkupTaxAmountInBase().multiply(invoiceData.getExchageRate())
                            : BigDecimal.ZERO);
                }
                if (item.getMarkupTax() != null && item.getMarkupTax().getTaxPercent() != null) {
                    markupTaxRate = priceScaleNumberFormat.format(item.getMarkupTax().getTaxPercent());
                }
                BigDecimal etotal = invoiceData.getCurrencyID().equals(item.getCurrencyID())
                        ? item.getAmountInCurrency()
                        : item.getAmountInBase() != null && invoiceData.getExchageRate() != null
                        ? item.getAmountInBase().multiply(invoiceData.getExchageRate())
                        : BigDecimal.ZERO;
                etotal = etotal.add(item.getCurrencyID() != null && invoiceData.getCurrencyID().equals(item.getCurrencyID())
                        ? item.getMarkupAmount()
                        : item.getMarkupAmountInBase() != null && invoiceData.getExchageRate() != null
                        ? item.getMarkupAmountInBase().multiply(invoiceData.getExchageRate())
                        : BigDecimal.ZERO);
                totalWithMarkup = priceScaleNumberFormat.format(etotal);
                if (item.getMarkupTaxAmount() != null) {
                    bedrag = priceScaleNumberFormat.format(etotal.add(item.getMarkupTaxAmount()));
                } else {
                    bedrag = totalWithMarkup;
                }
                if (item.getDate() != null) {
                    date = dateFormat.format(item.getDate());
                }
                currency = item.getCurrencyName() != null ? item.getCurrencyName() : "";

                if (expenseTable.containsColumn(ITEM_EXPENSE_CATEGORY)) {
                    columnsValue.add(escapeHtml(category));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_DESCRIPTION)) {
                    columnsValue.add(escapeHtml(description));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_TOTAL_AMOUNT)) {
                    columnsValue.add(escapeHtml(total));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_TOTAL_WITH_MARKUP)) {
                    columnsValue.add(escapeHtml(totalWithMarkup));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_MARKUP_AMOUNT)) {
                    columnsValue.add(escapeHtml(markup));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_MARKUP_TAX_AMOUNT)) {
                    columnsValue.add(escapeHtml(markupTax));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_BEDRAG_AMOUNT)) {
                    columnsValue.add(escapeHtml(bedrag));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_DATE)) {
                    columnsValue.add(escapeHtml(date));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_CURRENCY)) {
                    columnsValue.add(escapeHtml(currency));
                }
                if (expenseTable.containsColumn(ITEM_EXPENSE_MARKUP_TAX_RATE)) {
                    columnsValue.add(escapeHtml(markupTaxRate));
                }

                expenseTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }

        return expenseTable;

    }

    //used to Sales Invoice and Purchase Invoice
    protected CustomisedITextTable getConvertedInvoiceCustomTableData(NewInvoice invoiceData, EdsUser edsUser) {
        CustomisedITextTable convertedInvoiceCustomTable = new CustomisedITextTable();

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        /*1*/
        convertedInvoiceCustomTable.addColumn(INV_NUMBER, commonLocalizer.localizeAccounting(PdfLocalizationName.number));
        /*2*/
        convertedInvoiceCustomTable.addColumn(INV_DATE, commonLocalizer.localizeAccounting(PdfLocalizationName.date));
        /*3*/
        convertedInvoiceCustomTable.addColumn(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount));
        /*4*/
        convertedInvoiceCustomTable.addColumn(PAYMENT_TOTAL, "Paid");
        /*5*/
        convertedInvoiceCustomTable.addColumn(DUE_AMOUNT, commonLocalizer.localizeAccounting(PdfLocalizationName.dueAmount));
        /*6*/
        convertedInvoiceCustomTable.addColumn(PDFConstants.TOTAL_AMOUNT, commonLocalizer.localizeAccounting(PdfLocalizationName.dueAmount));
        /*7*/
        convertedInvoiceCustomTable.addColumn(PDFConstants.SUBTOTAL, commonLocalizer.localizeAccounting(PdfLocalizationName.subtotal));

        convertedInvoiceCustomTable.addColumn(PDFConstants.TAX_TOTAL, commonLocalizer.localizeAccounting(PdfLocalizationName.taxTotal));

        convertedInvoiceCustomTable.addColumn(PDFConstants.DISCOUNT_TOTAL, commonLocalizer.localizeAccounting(PdfLocalizationName.discount));

        List<String> columnsValue = new ArrayList<>();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        EdsQuote quote = quoteManager.get(invoiceData.getConvertedItemID());

        List<EdsInvoice> invoicesList = quote.getInvoices();
        BigDecimal exchangeRate = invoiceData.getExchageRate().compareTo(ZERO) != 0 ? invoiceData.getExchageRate() : new BigDecimal("1.00");

        if (!invoicesList.isEmpty()) {
            for (EdsInvoice invoice : invoicesList) {
                if (!invoice.isDeleted()) {
                    columnsValue.clear();
                    String number = "", invoiceDate = "", total = "", subTotal = "", taxTotal = "", paidAmount = "", dueAmount = "", quoteTotal = "", discountTotal = "";

                    //invoice number
                    if (invoice.getNumber() != null) {
                        number = invoice.getNumber();
                    }

                    //invoice date
                    invoiceDate = invoice.getInvoiceDate() == null ? "" : shortDateFormat.format(invoice.getInvoiceDate());

                    //invoice total
                    BigDecimal totalAmount = ZERO;
                    if (invoice.getTotalInInvoiceCurrency() != null) {
                        totalAmount = invoice.getTotalInInvoiceCurrency();
                        total = priceScaleNumberFormat.format(totalAmount);
                    } else {
                        totalAmount = invoice.getTotal().multiply(exchangeRate);
                        total = totalAmount.compareTo(ZERO) != 0 ? priceScaleNumberFormat.format(totalAmount) : "";
                    }

                    subTotal = invoice.getSubtotal() != null && invoice.getSubtotal().compareTo(ZERO) != 0 ? priceScaleNumberFormat.format(invoice.getSubtotal()) : priceScaleNumberFormat.format(BigDecimal.ZERO);
                    taxTotal = invoice.getTotalTaxes() != null && invoice.getTotalTaxes().compareTo(ZERO) != 0 ? priceScaleNumberFormat.format(invoice.getTotalTaxes()) : priceScaleNumberFormat.format(BigDecimal.ZERO);
                    discountTotal = invoice.getTotalDiscount() != null && invoice.getTotalDiscount().compareTo(ZERO) != 0 ? priceScaleNumberFormat.format(invoice.getTotalDiscount()) : priceScaleNumberFormat.format(BigDecimal.ZERO);

                    //invoice due amount
                    if (invoice.getFullPayments().compareTo(ZERO) != 0) {
                        paidAmount = priceScaleNumberFormat.format(invoice.getFullPayments());
                    } else {
                        paidAmount = priceScaleNumberFormat.format(invoice.getFullPaymentsInBase());
                    }
                    dueAmount = priceScaleNumberFormat.format(invoice.getDueAmount());

                    //Invoices quote total
                    BigDecimal quoteAmount = ZERO;
                    if (quote.getTotalInInvoiceCurrency() != null) {
                        quoteAmount = quote.getTotalInInvoiceCurrency();
                        quoteTotal = priceScaleNumberFormat.format(quoteAmount);
                    } else {
                        quoteAmount = quote.getTotal().multiply(exchangeRate);
                        quoteTotal = quoteAmount.compareTo(ZERO) != 0 ? priceScaleNumberFormat.format(quoteAmount) : "";
                    }

                    if (convertedInvoiceCustomTable.containsColumn(INV_NUMBER)) {
                        columnsValue.add(escapeHtml(number));
                    }
                    columnsValue.add(escapeHtml(invoiceDate));

                    if (convertedInvoiceCustomTable.containsColumn(TOTAL)) {
                        columnsValue.add(escapeHtml(total));
                    }
                    if (convertedInvoiceCustomTable.containsColumn(PAYMENT_TOTAL)) {
                        columnsValue.add(escapeHtml(paidAmount));
                    }
                    if (convertedInvoiceCustomTable.containsColumn(DUE_AMOUNT)) {
                        columnsValue.add(escapeHtml(dueAmount));
                    }
                    if (convertedInvoiceCustomTable.containsColumn(PDFConstants.TOTAL_AMOUNT)) {
                        columnsValue.add(escapeHtml(quoteTotal));
                    }
                    if (convertedInvoiceCustomTable.containsColumn(PDFConstants.SUBTOTAL)) {
                        columnsValue.add(escapeHtml(subTotal));
                    }
                    if (convertedInvoiceCustomTable.containsColumn(PDFConstants.TAX_TOTAL)) {
                        columnsValue.add(escapeHtml(taxTotal));
                    }
                    if (convertedInvoiceCustomTable.containsColumn(PDFConstants.DISCOUNT_TOTAL)) {
                        columnsValue.add(escapeHtml(discountTotal));
                    }

                    convertedInvoiceCustomTable.addRow(columnsValue.toArray(new String[]{}));
                }
            }

            if (quote.getQuoteItems() != null && !quote.getQuoteItems().isEmpty()) {
                CustomisedITextTable quoteItemItextTable = new CustomisedITextTable();
                quoteItemItextTable.addColumn(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
                quoteItemItextTable.addColumn(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
                quoteItemItextTable.addColumn(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(invoiceData.isProjectBasedInvoice() ? PdfLocalizationName.hours : PdfLocalizationName.qty));
                quoteItemItextTable.addColumn(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice));
                quoteItemItextTable.addColumn(ITEM_DISCOUNT_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountAmount));
                quoteItemItextTable.addColumn(ITEM_TAX_RATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.vatAmount));
                quoteItemItextTable.addColumn(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.tax));
                quoteItemItextTable.addColumn(ITEM_NET_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.netAmount));
                quoteItemItextTable.addColumn(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.amount));

                List<String> quoteItemColValue = new ArrayList<>();
                for (EdsQuoteItem item : quote.getQuoteItems()) {
                    quoteItemColValue.clear();

                    String quoteItemName = item.getItem() != null ? item.getItem().getName() : "";
                    String quoteItemDescription = item.getDescription();
                    String quoteItemQty = item.getQty() != null ? priceScaleNumberFormat.format(item.getQty()) : "";
                    String quoteItemUnitPrice = priceScaleNumberFormat.format(item.getUnitPrice());
                    EdsVat vat = (item.getVat() != null && item.getVat().getObjectID() != null) ? vatManager.get(item.getVat().getObjectID()) : null;
                    String quoteItemTaxRate = vat != null ? priceScaleNumberFormat.format(vat.getTaxRateAsBigDecimal()) : "0.00";
                    String quoteItemTaxAmount = item.getTaxAmount() != null ? priceScaleNumberFormat.format(item.getTaxAmount()) : "0";
                    // start total
                    BigDecimal netAmount = item.getQty().multiply(item.getUnitPrice());
                    BigDecimal itemDiscount = BigDecimal.ZERO;
                    if (item.getDiscount() != null) {
                        itemDiscount = netAmount.multiply(item.getDiscount()).divide(AccountingConstants.HUNDRED, 4, RoundingMode.HALF_UP);
                    } else if (item.getDiscountAmount() != null) {
                        itemDiscount = item.getDiscountAmount();
                    }
                    String quoteItemDiscount = priceScaleNumberFormat.format(itemDiscount);
                    BigDecimal itemDiscountedNetAmount = netAmount.subtract(itemDiscount);
                    BigDecimal bigDecimalTaxAmount = Optional.ofNullable(item.getTaxAmount()).orElse(BigDecimal.ZERO);
                    BigDecimal totalAmount = itemDiscountedNetAmount.add(bigDecimalTaxAmount);
                    String quoteItemNetAmount = priceScaleNumberFormat.format(itemDiscountedNetAmount);
                    String quoteItemTotalAmount = priceScaleNumberFormat.format(totalAmount);
                    // end total

                    if (quoteItemItextTable.containsColumn(ITEM_NAME)) {
                        quoteItemColValue.add(escapeHtml(quoteItemName));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_DESCRIPTION)) {
                        quoteItemColValue.add(escapeHtml(quoteItemDescription));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_QTY_HRS)) {
                        quoteItemColValue.add(escapeHtml(quoteItemQty));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_UNIT_PRICE)) {
                        quoteItemColValue.add(escapeHtml(quoteItemUnitPrice));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_DISCOUNT_AMOUNT)) {
                        quoteItemColValue.add(escapeHtml(quoteItemDiscount));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_TAX_RATE)) {
                        quoteItemColValue.add(escapeHtml(quoteItemTaxRate));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_TAX_AMOUNT)) {
                        quoteItemColValue.add(escapeHtml(quoteItemTaxAmount));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_NET_AMOUNT)) {
                        quoteItemColValue.add(escapeHtml(quoteItemNetAmount));
                    }
                    if (quoteItemItextTable.containsColumn(ITEM_TOTAL_AMOUNT)) {
                        quoteItemColValue.add(escapeHtml(quoteItemTotalAmount));
                    }
                    quoteItemItextTable.addRow(quoteItemColValue.toArray(new String[]{}));
                }
                convertedInvoiceCustomTable.addChildRows(quoteItemItextTable.getRows());
            }
        }
        return convertedInvoiceCustomTable;
    }

    protected CustomisedITextTable getMultiQuoteAndGdnConverToInvoiceProductTableData(NewInvoice invoiceData, EdsUser user) {
        CustomisedITextTable productItemTable = new CustomisedITextTable();
        EdsInvoice edsInvoice = invoiceManager.get(invoiceData.getID());
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        if (edsInvoice == null) {
            return productItemTable;
        }
        if (edsInvoice.getConvertedShippingData() == null || edsInvoice.getConvertedShippingData().isEmpty()) {
            return productItemTable;
        }

        DecimalFormat qtyNumberFormat = getQtyNumberFormat(user.getCompany(), invoiceData.getPdfTemplateID());
        DecimalFormat unitPriceNumberFormat = getUnitPriceNumberFormat(user.getCompany(), invoiceData.getPdfTemplateID());
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(user.getCompany(), invoiceData.getPdfTemplateID());

        productItemTable.addColumnOrder(ITEM_NO, ITEM_NAME, ITEM_DESCRIPTION, ITEM_QTY_HRS, ITEM_UNIT_PRICE, ITEM_NET_AMOUNT, ITEM_TOTAL_AMOUNT, "ITEM_GDN_NUMBERS", "ITEM_GDN_DATES", "ITEM_QUOTE_NUMBERS");

        List<Object[]> objects = invoiceManager.getMultiQuoteAndGdnConvertedInvoiceData(edsInvoice.getObjectID());
        if (objects != null && !objects.isEmpty()) {
            int count = 0;
            for (Object[] item : objects) {
                count = count + 1;
                String invoiceNumber = (String) item[0];
                Integer itemId = (Integer) item[1];
                String productNumber = (String) item[2];
                String name = (String) item[3];
                String description = (String) item[4];
                BigDecimal qty = (BigDecimal) item[5];
                BigDecimal unitPrice = (BigDecimal) item[6];
                BigDecimal net = (BigDecimal) item[7];
                BigDecimal amount = (BigDecimal) item[8];
                BigDecimal discountPercent = (BigDecimal) item[9];
                BigDecimal discountAmount = (BigDecimal) item[10];
                String gdnNumbers = (String) item[11];
                String gdnDates = (String) item[12];
                String quoteNumbers = (String) item[13];

                String quantityUnits = qty != null ? qtyNumberFormat.format(qty) : "";
                String unitPriceString = unitPrice != null ? unitPriceNumberFormat.format(unitPrice) : "";

                BigDecimal netAmount = qty != null ? qty.multiply(unitPrice) : BigDecimal.ZERO;
                BigDecimal itemDiscount = ZERO;
                if (discountPercent != null) {
                    itemDiscount = netAmount.multiply(discountPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                } else if (discountAmount != null) {
                    itemDiscount = discountAmount;
                }
                BigDecimal itemDiscountedNetAmount = netAmount.subtract(itemDiscount);
                String netPrice = itemDiscountedNetAmount != null ? priceScaleNumberFormat.format(itemDiscountedNetAmount) : "";
                String totalPrice = amount != null ? priceScaleNumberFormat.format(amount) : "";

                productItemTable.addRow(count + ".", name, description, quantityUnits, unitPriceString, netPrice, totalPrice, gdnNumbers, gdnDates, quoteNumbers);
            }
        }

        return productItemTable;
    }

    protected CustomisedITextTable getCustomProducTableData(NewInvoice invoiceData, EdsUser edsUser, EdsCurrency edsCurrency) {
        CustomisedITextTable productItemTable = new CustomisedITextTable();

        productItemTable.addColumn(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
        productItemTable.addColumn(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
        productItemTable.addColumn(ITEM_PRODUCT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
        productItemTable.addColumn(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        productItemTable.addColumn(ITEM_TYPE, commonLocalizer.localizeAccounting(PdfLocalizationName.type));
        productItemTable.addColumn(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(invoiceData.isProjectBasedInvoice() ? PdfLocalizationName.hours : PdfLocalizationName.qty));
        productItemTable.addColumn(ITEM_UNIT_MEASUREMENT, "");
        productItemTable.addColumn(ITEM_UNIT_MEASUREMENT_DESCRIPTION, "");
        productItemTable.addColumn(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice));
        productItemTable.addColumn(ITEM_UNIT_PRICE_IN_BASE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice));
        productItemTable.addColumn(ITEM_UNIT_PRICE_DISCOUNTED, accountingLocalizer.localizeAccounting(PdfLocalizationName.discountedUnitPrice));
        productItemTable.addColumn(ITEM_UNIT_PRICE_AVERAGE, accountingLocalizer.localizeAccounting(PdfLocalizationName.averageUnitPrice));
        productItemTable.addColumn(ITEM_NET_WITHOUT_DISCOUNT, accountingLocalizer.localizeAccounting(PdfLocalizationName.netWithoutDiscount));
        productItemTable.addColumn(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
        productItemTable.addColumn(ITEM_DISCOUNT_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountAmount));
        productItemTable.addColumn(ITEM_DISCOUNT_TYPE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
        productItemTable.addColumn(ITEM_NET_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.netAmount));
        productItemTable.addColumn(ITEM_NET_AMOUNT_IN_BASE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.netAmount));
        productItemTable.addColumn(ITEM_TAX_LABEL, "");
        productItemTable.addColumn(ITEM_TAX_RATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.vatAmount));
        productItemTable.addColumn(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.tax));
        productItemTable.addColumn(ITEM_DOUBLE_TAX_LABEL, "");
        productItemTable.addColumn(ITEM_DOUBLE_TAX_RATE, "");
        productItemTable.addColumn(ITEM_DOUBLE_TAX_AMOUNT, "");
        productItemTable.addColumn(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.amount));
        productItemTable.addColumn(ITEM_CATEGORY, "");
        productItemTable.addColumn(ITEM_MANUFACTURER, "");
        productItemTable.addColumn(ITEM_PART_NUMBER, "");
        productItemTable.addColumn(ITEM_VENDOR, "");
        productItemTable.addColumn(ITEM_PICTURE, "");
        productItemTable.addColumn(ITEM_SKU_NUMBER, "");
        productItemTable.addColumn(ITEM_QUOTE_QUANTITY_ORDERED, "");
        productItemTable.addColumn(ITEM_BACK_ORDERED, "");
        productItemTable.addColumn(ITEM_PROJECT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
        productItemTable.addColumn(ITEM_SUB_PROJECT, "");
        productItemTable.addColumn(ITEM_BRAND_NAME, "");
        productItemTable.addColumn(ITEM_ORIGINAL_PRICE, "");
        productItemTable.addColumn(ITEM_PRODUCT_SERIAL, "");
        productItemTable.addColumn(ITEM_PRODUCT_LOT_NUMBER, "");
        productItemTable.addColumn(ITEM_PRODUCT_LOT_NUMBER_QTY, "");
        productItemTable.addColumn(ITEM_PRODUCT_EXPIRATION_DATE, "");
        productItemTable.addColumn(ITEM_ACCOUNT, "");
        productItemTable.addColumn(ACCOUNT_NUMBER, "");
        productItemTable.addColumn(ITEM_DEPARTMENT, "");
        productItemTable.addColumn(ITEM_QTY_ON_HAND, "");
        productItemTable.addColumn(ITEM_ORDERED_QTY, "");
        productItemTable.addColumn(ITEM_RECIEVE, "");
        productItemTable.addColumn(ITEM_NET_PROFIT, "");
        productItemTable.addColumn(ITEM_TOTAL_SALES_PRICE, "");
        productItemTable.addColumn(ITEM_TOTAL_COST_PRICE, "");
        productItemTable.addColumn(ITEM_ORDERED_PRODUCT_QTY, "");
        productItemTable.addColumn(ITEM_PREV_INVOICES_PRODUCT_QTY, "");
        productItemTable.addColumn(ITEM_PROFIT_IC, "");
        productItemTable.addColumn(ITEM_COST_PRICE, "");
        productItemTable.addColumn(ITEM_BARCODE, "");
        productItemTable.addColumn(ITEM_WAREHOUSE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.warehouse));
        productItemTable.addColumn(PARENT_ACCOUNT, "");
        productItemTable.addColumn(ITEM_IS_PICKABLE, "");
        productItemTable.addColumn(PICK_ITEM_SHIPPED_QTY, "");
        productItemTable.addColumn(PICK_ITEM_REFERENCE, "");
        productItemTable.addColumn(PICK_ITEM_NUMBER_PACKS, "");
        productItemTable.addColumn(PICK_ITEM_QTY_PER_PACK, "");
        productItemTable.addColumn(PICK_ITEM_LAST_SHIPPED_QTY, "");
        productItemTable.addColumn("ITEM_BATCH_SERIAL_NUMBER", "");
        productItemTable.addColumn("ITEM_BATCH_EXPIRE_DATE", "");
        productItemTable.addColumn("ITEM_BATCH_QTY", "");
        productItemTable.addColumn(ITEM_TAX_AMOUNT_IN_BASE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
        productItemTable.addColumn(ITEM_TOTAL_AMOUNT_IN_BASE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount));
        productItemTable.addColumn("ITEM_REVERSED_QTY", "");
        productItemTable.addColumn("ITEM_UNIT_CUSTOM_PRICE", "");
        productItemTable.addColumn("ITEM_QUOTE_NUMBER", "");
        if (SALE_QUOTE.equals(getFromInvoice()) || SALE_ORDER.equals(getFromInvoice())) {
            productItemTable.addColumn(ATTACHMENTS, "Attachment");
        }

        List<String> columnsValue = new ArrayList<>();
        DecimalFormat defaultScaleFormat = getDefaultScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        DecimalFormat unitPriceNumberFormat = getUnitPriceNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        BigDecimal exchangeRate = invoiceData.getExchageRate().compareTo(ZERO) != 0
                ? invoiceData.getExchageRate() : new BigDecimal("1.00");

        if (SALE_INVOICE.equals(getFromInvoice())) {
            productItemTable.addColumn(ITEM_CONVERTED_QUOTE_CUSTOM_FIELDS, "");
        }

        boolean isProductSerial = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED);
        boolean isOrderedProductQty = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ORDERED_PRODUCT_QTY_FOR_PDF);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());

        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new LinkedHashMap<>();
        CustomisedHierarchyProductsTable hierarchy = new CustomisedHierarchyProductsTable();

        EdsSaleQuote cachedConvertedQuote = invoiceData.getConvertedItemID() != null
                ? invoiceManager.getSaleQuote(invoiceData.getConvertedItemID()) : null;
        EdsSaleQuote cachedInvoiceQuote = invoiceData.getConvertedItemID() == null && invoiceData.getID() != null
                ? invoiceManager.getSaleQuote(invoiceData.getID()) : null;
        EdsInvoice cachedEdsInvoice = null;
        EdsTransaction cachedEdsTransaction = null;
        if (SALE_INVOICE.equals(getFromInvoice()) && invoiceData.getID() != null) {
            cachedEdsInvoice = invoiceManager.get(invoiceData.getID());
            if (cachedEdsInvoice != null) {
                cachedEdsTransaction = transactionManager.getTransactionByInvoice(cachedEdsInvoice);
            }
        }

        Map<String, String> lineItemCustomField = Maps.newHashMap();
        Map<String, Boolean> standardFields = Maps.newHashMap();
        standardFields.put(ItemTableConstants.PRODUCT, true);
        standardFields.put(ItemTableConstants.DESCRIPTION, true);
        standardFields.put(ItemTableConstants.QTY, true);
        standardFields.put(ItemTableConstants.MEASUREMENT, true);
        standardFields.put(ItemTableConstants.UNITPRICE, true);
        standardFields.put(ItemTableConstants.DISCOUNT_AMT, true);
        standardFields.put(ItemTableConstants.DEPARTMENT, true);
        standardFields.put(ItemTableConstants.ACCOUNT, true);
        standardFields.put(ItemTableConstants.NET_AMT, true);
        standardFields.put(ItemTableConstants.TAX_LIST, true);
        standardFields.put(ItemTableConstants.DOUBLE_TAX_LIST, true);
        standardFields.put(ItemTableConstants.WAREHOUSE, true);
        standardFields.put(ItemTableConstants.TOTAL_AMT, true);
        standardFields.put(ItemTableConstants.PROJECT, true);
        standardFields.put(ItemTableConstants.DISCOUNT_LIST, true);
        standardFields.put(ATTACHMENTS, true);
        if (invoiceData.getCustomItemColumns() != null) {
            for (ColumnConfigs column : invoiceData.getCustomItemColumns()) {
                if (!standardFields.containsKey(column.getCode())) {
                    lineItemCustomField.put(column.getTitle(), column.getTitle());
                }
            }
        }

        Set<Integer> invoiceItemSet = Arrays.stream(invoiceData.getItems())
                .map(NewInvoiceItem::getID).collect(Collectors.toSet());
        String convertedItemQty = "";
        BigDecimal convertedQty = ZERO;
        if (invoiceData.getConvertedInvoices() != null && !invoiceData.getConvertedInvoices().isEmpty()) {
            for (NewInvoice invoice : invoiceData.getConvertedInvoices()) {
                if (invoice != null && invoice.getItems() != null) {
                    for (NewInvoiceItem invoiceItem : invoice.getItems()) {
                        if (invoiceItem.getID() != null && invoiceItemSet.contains(invoiceItem.getID()))
                            convertedQty = convertedQty.add(invoiceItem.getQuantity() != null ? invoiceItem.getQuantity() : ZERO);
                    }
                }
            }
            convertedItemQty = qtyNumberFormat.format(convertedQty);
        }

        Set<Integer> itemIds = new HashSet<>();
        Set<Integer> discountIds = new HashSet<>();
        Set<Integer> vatIds = new HashSet<>();
        Set<Integer> quoteItemIds = new HashSet<>();
        Set<Integer> invoiceItemIds = new HashSet<>();

        for (NewInvoiceItem item : invoiceData.getItems()) {
            if (item.getItemID() != null && item.getItemID() > 0) itemIds.add(item.getItemID());
            if (item.getItemDiscountID() != null) discountIds.add(item.getItemDiscountID());
            if (item.getItemDoubleDiscountID() != null) discountIds.add(item.getItemDoubleDiscountID());
            if (item.getTaxItem() != null && item.getTaxItem().getId() != null)
                vatIds.add(item.getTaxItem().getId());
            if (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getId() != null)
                vatIds.add(item.getDoubleTaxItem().getId());
            if (item.getQuoteItemId() != null) quoteItemIds.add(item.getQuoteItemId());
            if (item.getID() != null) invoiceItemIds.add(item.getID());
        }

        Map<Integer, EdsItem> itemCache = itemIds.isEmpty() ? Collections.emptyMap() : itemManager.getByIds(itemIds);
        Map<Integer, EdsDiscount> discountCache = discountIds.isEmpty() ? Collections.emptyMap() : discountManager.getByIds(discountIds);
        Map<Integer, EdsVat> vatCache = vatIds.isEmpty() ? Collections.emptyMap() : vatManager.getByIds(vatIds);

        Map<Integer, EdsQuoteItem> quoteItemCache = quoteItemIds.isEmpty()
                ? Collections.emptyMap() : quoteManager.getQuoteItemsByIds(quoteItemIds);

        Map<Integer, String> orderNumberCache = quoteItemIds.isEmpty()
                ? Collections.emptyMap() : invoiceManager.getOrderNumbers(quoteItemIds);

        Map<Integer, List<EdsProductPicture>> pictureCache = itemIds.isEmpty()
                ? Collections.emptyMap() : productPictureManager.getProductPicturesByItemIds(itemIds);

        Map<Integer, HashMap<PriceLevelItem, PriceLevelPPItem>> priceLevelCache = itemIds.isEmpty()
                ? Collections.emptyMap() : priceLevelPPManager.getPriceLevelPPItemsByIds(itemIds);

        Map<Integer, ProductSerialItem> serialCache = Collections.emptyMap();
        Map<Integer, List<ProductSerialItem>> batchSerialCache = Collections.emptyMap();
        if (isProductSerial && !invoiceItemIds.isEmpty()) {
            if (PURCHASE_ORDER.equals(getFromInvoice())) {
                serialCache = productSerialManager.getFirstSerialsByInvoiceItemIds(
                        invoiceItemIds, itemIds, PURCHASE_ORDER);
            } else if (SALE_INVOICE.equals(getFromInvoice())) {
                serialCache = productSerialManager.getFirstSerialsByInvoiceItemIds(
                        invoiceItemIds, itemIds, SALE_INVOICE);
                batchSerialCache = productSerialManager.getBatchSerialsByInvoiceItemIds(
                        invoiceItemIds, itemIds,
                        cachedEdsInvoice != null ? cachedEdsInvoice.getConvertedShippingData() : null);
            }
        }

        Map<Integer, List<ProductTrackBatchItem>> trackBatchCache = Collections.emptyMap();
        if (SALE_INVOICE.equals(getFromInvoice()) && !invoiceItemIds.isEmpty()) {
            String entityType = invoiceData.isDebitNote()
                    ? ItemSerialEntityType.DEBIT_NOTE.name()
                    : ItemSerialEntityType.SALES_INVOICE.name();
            trackBatchCache = itemBatchManager.getBatchItemsByInvoiceItemIds(
                    invoiceItemIds, itemIds, invoiceData.getID(), entityType);
        }

        Map<Integer, BigDecimal> orderedQtyCache = Collections.emptyMap();
        Map<Integer, BigDecimal> prevInvoiceQtyCache = Collections.emptyMap();
        if (isOrderedProductQty && SALE_INVOICE.equals(getFromInvoice())
                && invoiceData.getConvertedItemID() != null && !itemIds.isEmpty()) {
            orderedQtyCache = invoiceManager.getConvertedQuoteItemQuantities(
                    itemIds, invoiceData.getConvertedItemID(),
                    invoiceData.getCreationDate(), quoteItemIds);
            prevInvoiceQtyCache = invoiceManager.getPreviousConvertedSaleInvoiceItemsQuantities(
                    itemIds, invoiceData.getConvertedItemID(), invoiceData.getCreationDate());
        }

        for (int i = 0; i <= invoiceData.getItems().length - 1; i++) {
            NewInvoiceItem item = invoiceData.getItems()[i];

            EdsItem currentItem = item.getItemID() != null ? itemCache.get(item.getItemID()) : null;
            EdsDiscount discount = item.getItemDiscountID() != null ? discountCache.get(item.getItemDiscountID()) : null;
            EdsDiscount discount2 = item.getItemDoubleDiscountID() != null ? discountCache.get(item.getItemDoubleDiscountID()) : null;
            EdsVat vat = (item.getTaxItem() != null && item.getTaxItem().getId() != null) ? vatCache.get(item.getTaxItem().getId()) : null;
            EdsVat doubleVat = (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getId() != null) ? vatCache.get(item.getDoubleTaxItem().getId()) : null;
            EdsQuoteItem quoteItem = item.getQuoteItemId() != null ? quoteItemCache.get(item.getQuoteItemId()) : null;
            String orderNumber = item.getQuoteItemId() != null ? orderNumberCache.getOrDefault(item.getQuoteItemId(), "") : "";

            String quoteQty = "", backOrdered = "", pickReference = "",
                    pickNumberPacks = "", pickQtyPack = "", pickLastShippedQty = "";

            if (invoiceData.getConvertedItemID() != null) {
                if (cachedConvertedQuote != null && cachedConvertedQuote.getQuoteItems() != null) {
                    for (EdsQuoteItem qi : cachedConvertedQuote.getQuoteItems()) {
                        if (qi.getObjectID().equals(item.getQuoteItemId())
                                && qi.getItem() != null
                                && qi.getItem().getObjectID().equals(item.getItemID())) {
                            quoteQty = qi.getQty() != null ? qtyNumberFormat.format(qi.getQty()) : "";
                            backOrdered = qi.getQty() != null
                                    ? String.valueOf(qi.getQty().intValue() - item.getQuantity().intValue()) : "";
                            pickReference = qi.getReference() != null ? qi.getReference() : "";
                            pickNumberPacks = qi.getNumberOfPacks() != null ? qtyNumberFormat.format(qi.getNumberOfPacks()) : "";
                            pickQtyPack = qi.getQtyPerPack() != null ? qtyNumberFormat.format(qi.getQtyPerPack()) : "";
                            pickLastShippedQty = qi.getShip() != null ? qtyNumberFormat.format(qi.getShip()) : "";
                            break;
                        }
                    }
                }
            } else if (quoteItem != null) {
                if (quoteItem.getItem() != null && quoteItem.getItem().getObjectID().equals(item.getItemID())) {
                    pickReference = quoteItem.getReference() != null ? quoteItem.getReference() : "";
                    pickNumberPacks = quoteItem.getNumberOfPacks() != null ? qtyNumberFormat.format(quoteItem.getNumberOfPacks()) : "";
                    pickQtyPack = quoteItem.getQtyPerPack() != null ? qtyNumberFormat.format(quoteItem.getQtyPerPack()) : "";
                    pickLastShippedQty = quoteItem.getShip() != null ? qtyNumberFormat.format(quoteItem.getShip()) : "";
                }
            } else if (invoiceData.getID() != null && cachedInvoiceQuote != null
                    && cachedInvoiceQuote.getQuoteItems() != null) {
                for (EdsQuoteItem qi : cachedInvoiceQuote.getQuoteItems()) {
                    if (qi != null && qi.getItem() != null
                            && qi.getItem().getObjectID().equals(item.getItemID())) {
                        pickReference = qi.getReference() != null ? qi.getReference() : "";
                        pickNumberPacks = qi.getNumberOfPacks() != null ? qtyNumberFormat.format(qi.getNumberOfPacks()) : "";
                        pickQtyPack = qi.getQtyPerPack() != null ? qtyNumberFormat.format(qi.getQtyPerPack()) : "";
                        pickLastShippedQty = qi.getShip() != null ? qtyNumberFormat.format(qi.getShip()) : "";
                        break;
                    }
                }
            }

            productItemTable.addColumn(ITEM_QTY, "Quantity");

            String accountName = item.getAccountName() != null ? item.getAccountName() : "";
            String accountNumber = item.getAccountItem() != null ? item.getAccountItem().getCode() : "";
            String department = (item.getDepartmentItem() != null && item.getDepartmentItem().getName() != null)
                    ? item.getDepartmentItem().getName() : "";
            String qtyOnHand = item.getItemsInStockQty() != null ? qtyNumberFormat.format(item.getItemsInStockQty()) : "";
            String recieve = item.getReceive() != null ? qtyNumberFormat.format(item.getReceive()) : "";

            BigDecimal netAmount = item.getQuantity().multiply(item.getUnitPrice());
            BigDecimal itemDiscount = ZERO;
            if (item.getDiscountPercent() != null) {
                itemDiscount = netAmount.multiply(item.getDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP);
            } else if (item.getDiscountAmount() != null) {
                itemDiscount = item.getDiscountAmount();
            }
            if (item.getDoubleDiscountPercent() != null) {
                itemDiscount = itemDiscount.add(netAmount.multiply(item.getDoubleDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP));
            } else if (item.getDoubleDiscountAmount() != null) {
                itemDiscount = itemDiscount.add(item.getDoubleDiscountAmount());
            }
            BigDecimal itemDiscountedNetAmount = netAmount.subtract(itemDiscount);

            String no = (i + 1) + ".", name, productNumber = "";
            StringBuilder priceLevels = new StringBuilder();

            if (currentItem != null) {
                HashMap<PriceLevelItem, PriceLevelPPItem> ppItems = priceLevelCache.get(item.getItemID());
                if (ppItems != null) {
                    ppItems.forEach((pli, plpi) -> {
                        if (!priceLevels.isEmpty()) priceLevels.append(";");
                        priceLevels.append(pli.getName()).append(":").append(plpi.getCustomPrice());
                    });
                }
                name = currentItem.getName();
                productNumber = currentItem.getProductNumber() != null ? currentItem.getProductNumber() : "";
            } else if (item.getItemName() != null) {
                name = item.getItemName();
            } else {
                name = "";
            }

            String barcode = item.getItemBarcode() != null ? item.getItemBarcode() : "";
            String warehouse = item.getWarehouse() != null ? item.getWarehouse().getName() : "";
            String brandName = item.getProductBrand() != null ? item.getProductBrand() : "";
            String description = item.getDescription() != null ? item.getDescription() : NOT_AVAILABLE;
            String type = item.getProductType() != null ? String.valueOf(item.getProductType()) : NOT_AVAILABLE;
            String quantityUnits;
            if (invoiceData.isProjectBasedInvoice()) {
                quantityUnits = item.getQuantity() != null
                        ? ServerUtils.timeSpentToString(item.getQuantity().multiply(new BigDecimal(60)).setScale(2, RoundingMode.HALF_UP).intValue()) : "";
            } else {
                quantityUnits = item.getQuantity() != null ? qtyNumberFormat.format(item.getQuantity()) : "";
            }
            String reversed_qty = item.getReceivedQty() != null
                    ? qtyNumberFormat.format(item.getReceivedQty()) : qtyNumberFormat.format(ZERO);
            String unitMeasurement = item.getMeasurement() != null ? item.getMeasurement().getName() : "";
            String unitMeasurementDesc = item.getMeasurement() != null ? item.getMeasurement().getDescription() : "";
            String unitPrice = item.getUnitPrice() != null ? unitPriceNumberFormat.format(item.getUnitPrice()) : "";
            String unitPriceInBase = item.getUnitPrice() != null
                    ? unitPriceNumberFormat.format(item.getUnitPrice().divide(exchangeRate, 5, RoundingMode.HALF_UP)) : "";
            String originalItemPrice = item.getItemOriginalPrice() != null ? unitPriceNumberFormat.format(item.getItemOriginalPrice()) : "";

            String productDiscount = "";
            if (item.getDiscountPercent() != null) {
                productDiscount = defaultScaleFormat.format(item.getDiscountPercent()) + " %";
            } else if (item.getDiscountAmount() != null) {
                productDiscount = priceScaleNumberFormat.format(item.getDiscountAmount());
            }

            String discountName = "";
            if (discount != null) {
                discountName = discount.getName();
                discountName += !"".equals(productDiscount) ? (" (" + productDiscount + ")") : "";
            }

            String productDiscount2 = "";
            if (item.getDoubleDiscountPercent() != null) {
                productDiscount2 = defaultScaleFormat.format(item.getDoubleDiscountPercent()) + " %";
            } else if (item.getDoubleDiscountAmount() != null) {
                String currency = getCurrencySymbol(edsCurrency, true);
                if (currency == null || "".equals(currency.trim())) currency = getCurrencyName(edsCurrency);
                productDiscount2 = priceScaleNumberFormat.format(item.getDoubleDiscountAmount()) + " " + currency;
            }

            String discountName2 = "";
            if (discount2 != null) {
                discountName2 = discount2.getName();
                discountName2 += !"".equals(productDiscount2) ? (" (" + productDiscount2 + ")") : "";
            }

            String discountAmount = priceScaleNumberFormat.format(itemDiscount);
            String netWithoutDiscount = priceScaleNumberFormat.format(netAmount);
            String netData = priceScaleNumberFormat.format(itemDiscountedNetAmount);
            String netDataInBase = priceScaleNumberFormat.format(itemDiscountedNetAmount.divide(exchangeRate, 5, RoundingMode.HALF_UP));

            BigDecimal taxAmountNum = item.getTaxAmount();
            if (taxAmountNum == null) {
                BigDecimal taxPercent = (item.getTaxItem() != null && item.getTaxItem().getEffectiveTaxPercent() != null)
                        ? item.getTaxItem().getEffectiveTaxPercent() : ZERO;
                taxAmountNum = TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())
                        ? itemDiscountedNetAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, RoundingMode.HALF_UP)
                        : itemDiscountedNetAmount.multiply(taxPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
            }
            BigDecimal doubleTaxAmountNum = item.getDoubleTaxAmount();
            if (doubleTaxAmountNum == null) {
                BigDecimal taxPercent = (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getEffectiveTaxPercent() != null)
                        ? item.getDoubleTaxItem().getEffectiveTaxPercent() : ZERO;
                doubleTaxAmountNum = TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())
                        ? itemDiscountedNetAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, RoundingMode.HALF_UP)
                        : itemDiscountedNetAmount.multiply(taxPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
            }

            String doubleTaxAmount = priceScaleNumberFormat.format(doubleTaxAmountNum);
            String taxAmount = priceScaleNumberFormat.format(taxAmountNum);
            String taxAmountInBase = priceScaleNumberFormat.format(taxAmountNum.divide(exchangeRate, ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
            String totalPrice = priceScaleNumberFormat.format(item.getTotalAmount() != null ? item.getTotalAmount() : ZERO);
            String totalPriceInBase = priceScaleNumberFormat.format(item.getTotalAmount() != null
                    ? item.getTotalAmount().divide(exchangeRate, ServerUtils.getCalculationScale(), RoundingMode.HALF_UP) : ZERO);
            String discountedUnitPriceAsString = priceScaleNumberFormat.format(itemDiscountedNetAmount.divide(item.getQuantity(), 2, RoundingMode.HALF_UP));
            String averageUnitPriceAsString = priceScaleNumberFormat.format(item.getTotalAmount() != null
                    ? item.getTotalAmount().divide(item.getQuantity(), 2, RoundingMode.HALF_UP) : ZERO);

            BigDecimal vatRate = null, doubleVatRate = null;
            if (itemDiscountedNetAmount != null && vat != null) vatRate = vat.getTaxRateAsBigDecimal();
            String vatLabel = vat != null && vat.getName() != null ? vat.getName() : "";
            String vatRateAsString = vatRate != null ? "" + defaultScaleFormat.format(vatRate) : "0.00";

            if (itemDiscountedNetAmount != null && doubleVat != null)
                doubleVatRate = doubleVat.getTaxRateAsBigDecimal();
            String doubleVatLabel = doubleVat != null && doubleVat.getName() != null ? doubleVat.getName() : "";
            String doubleVatRateAsString = doubleVatRate != null ? "" + defaultScaleFormat.format(doubleVatRate) : "0.00";

            String manufacturer = "", skuNumber = "", partNumber = "", vendor = "", itemCategory = NOT_AVAILABLE;
            if (currentItem != null) {
                if (currentItem.getManufacturer() != null) manufacturer = currentItem.getManufacturer();
                if (currentItem.getInternalSKUNumber() != null) skuNumber = currentItem.getInternalSKUNumber();
                if (currentItem.getPartNumber() != null) partNumber = currentItem.getPartNumber();
                if (currentItem.getVendor() != null) vendor = currentItem.getVendor().getName();
                if (currentItem.getCategory() != null) itemCategory = currentItem.getCategory().getName();
            }

            String pictureUrl = "";
            if (currentItem != null) {
                List<EdsProductPicture> pictures = pictureCache.getOrDefault(item.getItemID(), Collections.emptyList());
                for (EdsProductPicture picture : pictures) {
                    if (picture.isDefaultPicture()) {
                        pictureUrl = uploadManager.getFileURL(picture);
                        break;
                    }
                }
            }

            String project = item.getProject() != null ? item.getProject().getName() : "";
            String subProject = item.getParentProject() != null ? item.getParentProject().getName() : "";

            String itemProfitPercentage = "";
            String itemProfit = "";
            String itemCostPrice = item.getUnitCost() != null ? priceScaleNumberFormat.format(item.getUnitCost()) : "";

            BigDecimal itemCostPriceAmount = item.getUnitCost() != null ? item.getUnitCost().multiply(exchangeRate) : ZERO;
            BigDecimal totalCostPriceAmount = itemCostPriceAmount.multiply(item.getQuantity());
            String totalCostPrice = defaultScaleFormat.format(totalCostPriceAmount);

            BigDecimal itemSalesPriceAmount = item.getItemOriginalPrice() != null ? item.getItemOriginalPrice() : ZERO;
            BigDecimal totalSalesPriceAmount = itemSalesPriceAmount.multiply(item.getQuantity());
            String totalSalesPrice = defaultScaleFormat.format(totalSalesPriceAmount);

            BigDecimal totalNetProfitAmount = netAmount.subtract(totalCostPriceAmount);
            String totalNetProfit = defaultScaleFormat.format(totalNetProfitAmount);

            String parentAccountNumber = item.getAccountItem() != null ? item.getAccountItem().getParentCode() : "";
            String isPickable = item.isPickable() ? "YES" : "NO";
            String shippedQty = item.getShippedQty() != null ? qtyNumberFormat.format(item.getShippedQty()) : "";

            if (SALE_INVOICE.equals(getFromInvoice()) && item.getItemID() != null
                    && cachedEdsInvoice != null && cachedEdsTransaction != null) {
                BigDecimal cogsPrice = itemStockManager.getTransactionValueByTransactionIdAndItemId(
                        cachedEdsTransaction.getObjectID(), item.getItemID());
                if (cogsPrice != null && cogsPrice.compareTo(ZERO) > 0
                        && itemDiscountedNetAmount != null && itemDiscountedNetAmount.compareTo(ZERO) > 0) {
                    BigDecimal costPrice = cogsPrice.divide(item.getQuantity(), 4, RoundingMode.HALF_UP);
                    cogsPrice = cogsPrice.multiply(exchangeRate);
                    BigDecimal itemProfitAmount = itemDiscountedNetAmount.subtract(cogsPrice);
                    BigDecimal itemPercentageAmount = itemProfitAmount.divide(itemDiscountedNetAmount, 4, RoundingMode.HALF_UP).multiply(HUNDRED);
                    itemCostPrice = defaultScaleFormat.format(costPrice);
                    itemProfit = defaultScaleFormat.format(itemProfitAmount);
                    itemProfitPercentage = defaultScaleFormat.format(itemPercentageAmount);
                }
            }

            String productSerialNumber = "", productLotNumber = "", productSerialQTY = "", productExpirationDate = "";
            if (isProductSerial && item.getItemID() != null) {
                if (PURCHASE_ORDER.equals(getFromInvoice())) {
                    ProductSerialItem serialItem = serialCache.get(item.getID());
                    productSerialNumber = serialItem != null ? serialItem.getSerial() : "";
                    productLotNumber = serialItem != null ? serialItem.getLotNumber() : "";
                    productExpirationDate = serialItem != null && serialItem.getExpirationDate() != null
                            ? shortDateFormat.format(ServerUtils.convertServerDateToUserDate(serialItem.getExpirationDate(), edsUser.getUserTimezone())) : "";
                } else if (SALE_INVOICE.equals(getFromInvoice())) {
                    if (item.getBatchTrackingEnabled()) {
                        List<ProductSerialItem> batchSerials = batchSerialCache.getOrDefault(item.getID(), Collections.emptyList());
                        if (!batchSerials.isEmpty()) {
                            productLotNumber = batchSerials.stream().map(ProductSerialItem::getSerial).collect(Collectors.joining(", "));
                            productSerialQTY = batchSerials.stream().map(a -> a.getQty() != null ? qtyNumberFormat.format(a.getQty()) : "").collect(Collectors.joining(", "));
                            productExpirationDate = batchSerials.stream().map(a -> a.getExpirationDate() != null
                                    ? shortDateFormat.format(ServerUtils.convertServerDateToUserDate(a.getExpirationDate(), edsUser.getUserTimezone())) : "").collect(Collectors.joining(", "));
                        }
                    } else {
                        ProductSerialItem serialItem = serialCache.get(item.getID());
                        productSerialNumber = serialItem != null ? serialItem.getSerial() : "";
                        productLotNumber = serialItem != null ? serialItem.getLotNumber() : "";
                        productExpirationDate = serialItem != null && serialItem.getExpirationDate() != null
                                ? shortDateFormat.format(ServerUtils.convertServerDateToUserDate(serialItem.getExpirationDate(), edsUser.getUserTimezone())) : "";
                    }
                }
            }

            StringBuilder convertedQuoteCustomFields = new StringBuilder();
            if (SALE_INVOICE.equals(getFromInvoice()) && item.getQuoteItemId() != null) {
                EdsQuoteItem edsQuoteItem = quoteItemCache.get(item.getQuoteItemId());
                if (edsQuoteItem != null && edsQuoteItem.getQuote() != null) {
                    EdsQuote edsQuote = edsQuoteItem.getQuote();
                    List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(
                            edsQuote.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleQuote));
                    Map<String, String> customFieldMap = new HashMap<>();
                    for (CompanyCustomFieldItem customField : customFieldItems) {
                        if (customField == null) continue;
                        if (CompanyCustomFieldItem.DATE.equals(customField.getDataType())
                                && "Data DDT".equals(customField.getFieldName())
                                && customField.getFieldDateNonConvertedValue() != null) {
                            customFieldMap.put(customField.getFieldName(),
                                    shortDateFormat.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate()));
                        } else if ("DDT Numero".equals(customField.getFieldName())
                                && customField.getFieldStringValue() != null
                                && !"".equals(customField.getFieldStringValue())) {
                            customFieldMap.put(customField.getFieldName(), customField.getFieldStringValue());
                        }
                    }
                    if (customFieldMap.get("DDT Numero") != null)
                        convertedQuoteCustomFields.append("DDT Numero: ").append(customFieldMap.get("DDT Numero")).append("\n");
                    if (customFieldMap.get("Data DDT") != null)
                        convertedQuoteCustomFields.append("Data DDT: ").append(customFieldMap.get("Data DDT")).append("\n");
                }
            }

            String orderedQty = "", prevInvoicesQty = "";
            if (isOrderedProductQty && SALE_INVOICE.equals(getFromInvoice())
                    && item.getItemID() != null && invoiceData.getConvertedItemID() != null) {
                BigDecimal oq = orderedQtyCache.get(item.getItemID());
                BigDecimal pq = prevInvoiceQtyCache.get(item.getItemID());
                orderedQty = qtyNumberFormat.format(oq != null ? oq : ZERO);
                prevInvoicesQty = qtyNumberFormat.format(pq != null ? pq : ZERO);
            }

            StringBuilder batchSerialNumber = new StringBuilder();
            StringBuilder batchExpireDate = new StringBuilder();
            StringBuilder batchQty = new StringBuilder();
            if (item.getTrackBatchesEnabled()) {
                List<ProductTrackBatchItem> batchItems = trackBatchCache.getOrDefault(item.getID(), Collections.emptyList());
                for (ProductTrackBatchItem data : batchItems) {
                    batchSerialNumber.append(data.getSerial()).append("\n");
                    batchQty.append(qtyNumberFormat.format(data.getQty())).append("\n");
                    batchExpireDate.append(data.getExpirationDate() != null
                            ? shortDateFormat.format(ServerUtils.convertServerDateToUserDate(data.getExpirationDate(), edsUser.getUserTimezone())) + "\n" : "");
                }
            }

            columnsValue.clear();
            if (productItemTable.containsColumn(ITEM_NO)) columnsValue.add(escapeHtml(no));
            if (productItemTable.containsColumn(ITEM_NAME)) columnsValue.add(escapeHtml(name));
            if (productItemTable.containsColumn(ITEM_PRODUCT_NAME)) columnsValue.add(escapeHtml(productNumber));
            if (productItemTable.containsColumn(ITEM_DESCRIPTION)) columnsValue.add(escapeHtml(description));
            if (productItemTable.containsColumn(ITEM_TYPE)) columnsValue.add(escapeHtml(type));
            if (productItemTable.containsColumn(ITEM_QTY_HRS)) columnsValue.add(escapeHtml(quantityUnits));
            if (productItemTable.containsColumn(ITEM_UNIT_MEASUREMENT)) columnsValue.add(escapeHtml(unitMeasurement));
            if (productItemTable.containsColumn(ITEM_UNIT_MEASUREMENT_DESCRIPTION))
                columnsValue.add(escapeHtml(unitMeasurementDesc));
            if (productItemTable.containsColumn(ITEM_UNIT_PRICE)) columnsValue.add(escapeHtml(unitPrice));
            if (productItemTable.containsColumn(ITEM_UNIT_PRICE_IN_BASE)) columnsValue.add(escapeHtml(unitPriceInBase));
            if (productItemTable.containsColumn(ITEM_UNIT_PRICE_DISCOUNTED))
                columnsValue.add(escapeHtml(discountedUnitPriceAsString));
            if (productItemTable.containsColumn(ITEM_UNIT_PRICE_AVERAGE))
                columnsValue.add(escapeHtml(averageUnitPriceAsString));
            if (productItemTable.containsColumn(ITEM_NET_WITHOUT_DISCOUNT))
                columnsValue.add(escapeHtml(netWithoutDiscount));
            if (productItemTable.containsColumn(ITEM_DISCOUNT)) columnsValue.add(escapeHtml(productDiscount));
            if (productItemTable.containsColumn(ITEM_DISCOUNT_AMOUNT)) columnsValue.add(escapeHtml(discountAmount));
            if (productItemTable.containsColumn(ITEM_DISCOUNT_TYPE)) columnsValue.add(escapeHtml(discountName));
            if (productItemTable.containsColumn(ITEM_DOUBLE_DISCOUNT)) columnsValue.add(escapeHtml(productDiscount2));
            if (productItemTable.containsColumn(ITEM_DOUBLE_DISCOUNT_TYPE)) columnsValue.add(escapeHtml(discountName2));
            if (productItemTable.containsColumn(ITEM_NET_AMOUNT)) columnsValue.add(escapeHtml(netData));
            if (productItemTable.containsColumn(ITEM_NET_AMOUNT_IN_BASE)) columnsValue.add(escapeHtml(netDataInBase));
            if (productItemTable.containsColumn(ITEM_TAX_LABEL)) columnsValue.add(escapeHtml(vatLabel));
            if (productItemTable.containsColumn(ITEM_TAX_RATE)) columnsValue.add(escapeHtml(vatRateAsString));
            if (productItemTable.containsColumn(ITEM_TAX_AMOUNT)) columnsValue.add(escapeHtml(taxAmount));
            if (productItemTable.containsColumn(ITEM_DOUBLE_TAX_LABEL)) columnsValue.add(escapeHtml(doubleVatLabel));
            if (productItemTable.containsColumn(ITEM_DOUBLE_TAX_RATE))
                columnsValue.add(escapeHtml(doubleVatRateAsString));
            if (productItemTable.containsColumn(ITEM_DOUBLE_TAX_AMOUNT)) columnsValue.add(escapeHtml(doubleTaxAmount));
            if (productItemTable.containsColumn(ITEM_TOTAL_AMOUNT)) columnsValue.add(escapeHtml(totalPrice));
            if (productItemTable.containsColumn(ITEM_CATEGORY)) columnsValue.add(escapeHtml(itemCategory));
            if (productItemTable.containsColumn(ITEM_MANUFACTURER)) columnsValue.add(escapeHtml(manufacturer));
            if (productItemTable.containsColumn(ITEM_PART_NUMBER)) columnsValue.add(escapeHtml(partNumber));
            if (productItemTable.containsColumn(ITEM_VENDOR)) columnsValue.add(escapeHtml(vendor));
            if (productItemTable.containsColumn(ITEM_PICTURE)) columnsValue.add(escapeHtml(pictureUrl));
            if (productItemTable.containsColumn(ITEM_SKU_NUMBER)) columnsValue.add(escapeHtml(skuNumber));
            if (productItemTable.containsColumn(ITEM_QUOTE_QUANTITY_ORDERED)) columnsValue.add(escapeHtml(quoteQty));
            if (productItemTable.containsColumn(ITEM_BACK_ORDERED)) columnsValue.add(escapeHtml(backOrdered));
            if (productItemTable.containsColumn(ITEM_PROJECT)) columnsValue.add(escapeHtml(project));
            if (productItemTable.containsColumn(ITEM_SUB_PROJECT)) columnsValue.add(escapeHtml(subProject));
            if (productItemTable.containsColumn(ITEM_BRAND_NAME)) columnsValue.add(escapeHtml(brandName));
            if (productItemTable.containsColumn(ITEM_ORIGINAL_PRICE)) columnsValue.add(escapeHtml(originalItemPrice));
            if (productItemTable.containsColumn(ITEM_PRODUCT_SERIAL)) columnsValue.add(escapeHtml(productSerialNumber));
            if (productItemTable.containsColumn(ITEM_PRODUCT_LOT_NUMBER))
                columnsValue.add(escapeHtml(productLotNumber));
            if (productItemTable.containsColumn(ITEM_PRODUCT_LOT_NUMBER_QTY))
                columnsValue.add(escapeHtml(productSerialQTY));
            if (productItemTable.containsColumn(ITEM_PRODUCT_EXPIRATION_DATE))
                columnsValue.add(escapeHtml(productExpirationDate));
            if (productItemTable.containsColumn(ITEM_ACCOUNT)) columnsValue.add(escapeHtml(accountName));
            if (productItemTable.containsColumn(ACCOUNT_NUMBER)) columnsValue.add(escapeHtml(accountNumber));
            if (productItemTable.containsColumn(ITEM_DEPARTMENT)) columnsValue.add(escapeHtml(department));
            if (productItemTable.containsColumn(ITEM_QTY_ON_HAND)) columnsValue.add(escapeHtml(qtyOnHand));
            if (productItemTable.containsColumn(ITEM_ORDERED_QTY)) columnsValue.add(escapeHtml(convertedItemQty));
            if (productItemTable.containsColumn(ITEM_RECIEVE)) columnsValue.add(escapeHtml(recieve));
            if (productItemTable.containsColumn(ITEM_NET_PROFIT)) columnsValue.add(escapeHtml(totalNetProfit));
            if (productItemTable.containsColumn(ITEM_TOTAL_SALES_PRICE)) columnsValue.add(escapeHtml(totalSalesPrice));
            if (productItemTable.containsColumn(ITEM_TOTAL_COST_PRICE)) columnsValue.add(escapeHtml(totalCostPrice));
            if (productItemTable.containsColumn(ITEM_ORDERED_PRODUCT_QTY)) columnsValue.add(escapeHtml(orderedQty));
            if (productItemTable.containsColumn(ITEM_PREV_INVOICES_PRODUCT_QTY))
                columnsValue.add(escapeHtml(prevInvoicesQty));
            if (productItemTable.containsColumn(ITEM_PROFIT_PERCENTAGE_IC))
                columnsValue.add(escapeHtml(itemProfitPercentage));
            if (productItemTable.containsColumn(ITEM_PROFIT_IC)) columnsValue.add(escapeHtml(itemProfit));
            if (productItemTable.containsColumn(ITEM_COST_PRICE)) columnsValue.add(escapeHtml(itemCostPrice));
            if (productItemTable.containsColumn(ITEM_BARCODE)) columnsValue.add(escapeHtml(barcode));
            if (productItemTable.containsColumn(ITEM_WAREHOUSE)) columnsValue.add(escapeHtml(warehouse));
            if (productItemTable.containsColumn(PARENT_ACCOUNT)) columnsValue.add(escapeHtml(parentAccountNumber));
            if (productItemTable.containsColumn(ITEM_IS_PICKABLE)) columnsValue.add(escapeHtml(isPickable));
            if (productItemTable.containsColumn(PICK_ITEM_SHIPPED_QTY)) columnsValue.add(escapeHtml(shippedQty));
            if (productItemTable.containsColumn(PICK_ITEM_REFERENCE)) columnsValue.add(escapeHtml(pickReference));
            if (productItemTable.containsColumn(PICK_ITEM_NUMBER_PACKS)) columnsValue.add(escapeHtml(pickNumberPacks));
            if (productItemTable.containsColumn(PICK_ITEM_QTY_PER_PACK)) columnsValue.add(escapeHtml(pickQtyPack));
            if (productItemTable.containsColumn(PICK_ITEM_LAST_SHIPPED_QTY))
                columnsValue.add(escapeHtml(pickLastShippedQty));
            if (productItemTable.containsColumn("ITEM_BATCH_SERIAL_NUMBER"))
                columnsValue.add(escapeHtml(batchSerialNumber.toString()));
            if (productItemTable.containsColumn("ITEM_BATCH_EXPIRE_DATE"))
                columnsValue.add(escapeHtml(batchExpireDate.toString()));
            if (productItemTable.containsColumn("ITEM_BATCH_QTY")) columnsValue.add(escapeHtml(batchQty.toString()));
            if (productItemTable.containsColumn(ITEM_TAX_AMOUNT_IN_BASE)) columnsValue.add(escapeHtml(taxAmountInBase));
            if (productItemTable.containsColumn(ITEM_TOTAL_AMOUNT_IN_BASE))
                columnsValue.add(escapeHtml(totalPriceInBase));
            if (productItemTable.containsColumn("ITEM_REVERSED_QTY")) columnsValue.add(escapeHtml(reversed_qty));
            if (productItemTable.containsColumn("ITEM_UNIT_CUSTOM_PRICE"))
                columnsValue.add(escapeHtml(priceLevels.toString()));
            if (productItemTable.containsColumn("ITEM_QUOTE_NUMBER")) columnsValue.add(escapeHtml(orderNumber));

            if (productItemTable.containsColumn(ATTACHMENTS)
                    && (SALE_QUOTE.equals(getFromInvoice()) || SALE_ORDER.equals(getFromInvoice()))) {
                FileItem attachment = item.getAttachments() != null && !item.getAttachments().isEmpty()
                        ? item.getAttachments().get(0) : null;
                String url = "";
                if (attachment != null) {
                    if ("AMAZON".equals(attachment.getUploadType()))
                        url = attachment.getAmazonLink().replaceAll("[&]", "&amp;");
                    else if ("GOOGLE".equals(attachment.getUploadType()))
                        url = attachment.getGoogleDocumentLink();
                    else if ("OFFICE_365".equals(attachment.getUploadType()))
                        url = attachment.getOfficeDocumentLink();
                }
                columnsValue.add(StringUtils.isNotEmpty(url)
                        ? "<img src=\"" + url + "\" style=\"width:30px; height:30px\"></img>"
                        : "<span>&#160; &#160;</span>");
            }

            if (SALE_INVOICE.equals(getFromInvoice()) && productItemTable.containsColumn(ITEM_CONVERTED_QUOTE_CUSTOM_FIELDS))
                columnsValue.add(escapeHtml(convertedQuoteCustomFields.toString()));

            if (productItemTable.containsColumn(ITEM_QTY))
                columnsValue.add(escapeHtml(item.getQuantity() != null ? String.valueOf(item.getQuantity()) : ""));

            if (item.getCustomFieldItems() != null && !item.getCustomFieldItems().isEmpty()) {
                for (CompanyCustomFieldItem ccfi : item.getCustomFieldItems()) {
                    if (lineItemCustomField.containsKey(ccfi.getFieldName())) {
                        productItemTable.addColumn(lineItemCustomField.get(ccfi.getFieldName()), lineItemCustomField.get(ccfi.getFieldName()));
                        if (CompanyCustomFieldItem.DATE.equals(ccfi.getDataType())) {
                            columnsValue.add(ccfi.getFieldDateNonConvertedValue() != null
                                    ? escapeHtml(shortDateFormat.format(ccfi.getFieldDateNonConvertedValue().getNonConvertedDate())) : "");
                        } else {
                            columnsValue.add(ccfi.getFieldStringValue() != null ? ccfi.getFieldStringValue() : "");
                        }
                    }
                }
            }

            productItemTable.addRow(columnsValue.toArray(new String[]{}));

            if (item.getItemID() != null && item.getItemID() != 0) {
                CustomisedITextTable childItextTable = new CustomisedITextTable();
                childItextTable.addColumn(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
                childItextTable.addColumn(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
                childItextTable.addColumn(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
                childItextTable.addColumn(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(invoiceData.isProjectBasedInvoice() ? PdfLocalizationName.hours : PdfLocalizationName.qty));

                if (currentItem != null && currentItem.getProductKitItems() != null) {
                    List<String> childColValue = new ArrayList<>();
                    short count = 0;
                    for (EdsProductKitItems kitItem : currentItem.getProductKitItems()) {
                        EdsItem childItem = kitItem.getItem();
                        if (childItem != null) {
                            childColValue.clear();
                            if (childItextTable.containsColumn(ITEM_NO))
                                childColValue.add(escapeHtml(String.valueOf(count++)));
                            if (childItextTable.containsColumn(ITEM_NAME))
                                childColValue.add(escapeHtml(childItem.getName()));
                            if (childItextTable.containsColumn(ITEM_DESCRIPTION))
                                childColValue.add(escapeHtml(childItem.getDescription()));
                            if (childItextTable.containsColumn(ITEM_QTY_HRS))
                                childColValue.add(escapeHtml(kitItem.getQuantity() != null ? qtyNumberFormat.format(kitItem.getQuantity()) : ""));
                            childItextTable.addRow(childColValue.toArray(new String[]{}));
                        }
                    }
                    productItemTable.addChildRows(childItextTable.getRows());
                }
            }

            if (item.getItemID() != null && item.getItemID() != 0 && currentItem != null) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                if (currentItem.getItemCustomFields() != null) {
                    List<CompanyCustomFieldItem> cfItems = CustomFieldsUtils.setRPCCustomFieldItems(
                            currentItem.getItemCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductCategory));
                    for (CompanyCustomFieldItem cf : cfItems) {
                        if (cf == null || cf.getFieldName() == null) continue;
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, escapeHtml(cf.getFieldName()));
                        cols.put(COLUMN_VALUE, CompanyCustomFieldItem.DATE.equals(cf.getDataType())
                                ? (cf.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(cf.getFieldDateNonConvertedValue().getNonConvertedDate())) : null)
                                : (cf.getFieldStringValue() != null ? escapeHtml(cf.getFieldStringValue()) : null));
                        itemCusFields.put(escapeHtml(cf.getFieldName()), cols);
                    }
                }
                if (currentItem.getCustomFields() != null) {
                    List<CompanyCustomFieldItem> cfItems = CustomFieldsUtils.setRPCCustomFieldItems(
                            currentItem.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView));
                    for (CompanyCustomFieldItem cf : cfItems) {
                        if (cf == null || cf.getFieldName() == null) continue;
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, escapeHtml(cf.getFieldName()));
                        cols.put(COLUMN_VALUE, CompanyCustomFieldItem.DATE.equals(cf.getDataType())
                                ? (cf.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(cf.getFieldDateNonConvertedValue().getNonConvertedDate())) : null)
                                : (cf.getFieldStringValue() != null ? escapeHtml(cf.getFieldStringValue()) : null));
                        itemCusFields.put(escapeHtml(cf.getFieldName()), cols);
                    }
                }
                customFields.put(no, itemCusFields);
            }

            hierarchy.buildHierarchy(description, productItemTable.getRows().get("" + i));
        }

        productItemTable.setCustomFields(getCustomFields(null, invoiceData, customFields));
        productItemTable.setHierarchyTable(hierarchy);
        return productItemTable;
    }

    protected CustomisedITextTable getGroupItemNameAndUnitPriceTableData(NewInvoice invoiceData, EdsUser edsUser) {
        HashBasedTable<String, BigDecimal, BigDecimal> requestedItems = HashBasedTable.create();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        for (NewInvoiceItem item : invoiceData.getItems()) {
            if (requestedItems.get(item.getItemName(), item.getUnitPrice()) != null) {
                BigDecimal qty = requestedItems.get(item.getItemName(), item.getUnitPrice()).add(item.getQuantity());
                requestedItems.put(item.getItemName(), item.getUnitPrice(), qty);
            } else {
                requestedItems.put(item.getItemName(), item.getUnitPrice(), item.getQuantity());
            }
        }

        CustomisedITextTable groupItemNameTable = new CustomisedITextTable();
        groupItemNameTable.addColumn(ITEM_NAME, "");
        groupItemNameTable.addColumn(QTY, "");
        groupItemNameTable.addColumn(ITEM_UNIT_PRICE, "");
        groupItemNameTable.addColumn(ITEM_TOTAL_AMOUNT, "");

        for (String itemName : requestedItems.rowKeySet()) {
            for (BigDecimal unitPrice : requestedItems.row(itemName).keySet()) {

                String productUnitPrice = priceScaleNumberFormat.format(unitPrice);
                String productQty = priceScaleNumberFormat.format(requestedItems.get(itemName, unitPrice));
                String netAmount = priceScaleNumberFormat.format(unitPrice.multiply(requestedItems.get(itemName, unitPrice)));

                groupItemNameTable.addRow(itemName, productQty, productUnitPrice, netAmount);
            }
        }
        return groupItemNameTable;
    }

    protected CustomisedITextTable getGroupItemNameAndTaxRate(NewInvoice invoiceData, EdsUser edsUser) {
        CustomisedITextTable groupItemNameTable = new CustomisedITextTable();
        Map<Integer, NewInvoiceItem> itemsMap = new HashMap<>();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        List<QIGroupingField> fields = new ArrayList<>();
        fields.add(QIGroupingField.ITEM);
        fields.add(QIGroupingField.TAX);

        for (NewInvoiceItem item : invoiceData.getItems()) {
            Integer key = generateHashKey(item, fields);

            if (itemsMap.get(key) == null) {
                item.setQuoteItemId(null);
                item.setDescription(null);

                if (!fields.contains(QIGroupingField.PRICE)) {
                    item.setUnitPrice(item.getUnitPrice().multiply(item.getQuantity()));
                    item.setQuantity(BigDecimal.ONE);
                }
                if (!fields.contains(QIGroupingField.ACCOUNT)) {
                    item.setAccountItem(null);
                }
                if (!fields.contains(QIGroupingField.TAX)) {
                    item.setTaxItem(null);
                }
                if (!fields.contains(QIGroupingField.DEPARTMENT)) {
                    item.setDepartmentItem(null);
                }
                itemsMap.put(key, item);
            } else {
                NewInvoiceItem value = itemsMap.get(key);
                if (!fields.contains(QIGroupingField.PRICE)) {
                    BigDecimal unitPrice = item.getUnitPrice().multiply(Optional.ofNullable(item.getQuantity()).orElse(BigDecimal.ZERO));
                    value.setUnitPrice(value.getUnitPrice().add(Optional.ofNullable(unitPrice).orElse(BigDecimal.ZERO)));
                    value.setQuantity(value.getQuantity().add(Optional.ofNullable(item.getQuantity()).orElse(BigDecimal.ZERO)));
                    value.setTaxAmount(value.getTaxAmount().add(Optional.ofNullable(item.getTaxAmount()).orElse(BigDecimal.ZERO)));
                    value.setTotalAmount(value.getTotalAmount().add(Optional.ofNullable(item.getTotalAmount()).orElse(BigDecimal.ZERO)));
                    value.setTaxItem(item.getTaxItem());
                }
            }
        }
        List<NewInvoiceItem> items = new ArrayList<>(itemsMap.values());
        items.sort(Comparator.comparing(NewInvoiceItem::getItemName));

        groupItemNameTable.addColumn(ITEM_NAME, "");
        groupItemNameTable.addColumn(QTY, "");
        groupItemNameTable.addColumn(ITEM_TAX_RATE, "");
        groupItemNameTable.addColumn(ITEM_TAX_AMOUNT, "");
        groupItemNameTable.addColumn(ITEM_NET_AMOUNT, "");
        groupItemNameTable.addColumn(ITEM_TOTAL_AMOUNT, "");

        for (NewInvoiceItem item : items) {
            String itemName = item.getItemName();
            String qty = item.getQuantity() != null ? priceScaleNumberFormat.format(item.getQuantity()) : "";
            String taxRate = item.getTaxItem() != null && item.getTaxItem().getTaxPercent() != null ? priceScaleNumberFormat.format(item.getTaxItem().getTaxPercent()) : "";
            String taxAmount = item.getTaxAmount() != null ? priceScaleNumberFormat.format(item.getTaxAmount()) : "";
            String netAmount = item.getUnitPrice() != null ? priceScaleNumberFormat.format(item.getUnitPrice()) : "";
            String totalAmount = item.getTotalAmount() != null ? priceScaleNumberFormat.format(item.getTotalAmount()) : "";

            groupItemNameTable.addRow(itemName, qty, taxRate, taxAmount, netAmount, totalAmount);
        }

        return groupItemNameTable;
    }

    protected CustomisedITextTable getDetailedItemsFromOrderBaseInvoice(NewInvoice invoiceData, EdsUser edsUser, ArrayList<EdsQuoteItem> itemList) {
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        CustomisedITextTable detailedItemsTable = new CustomisedITextTable();
        detailedItemsTable.addColumn(NUMBER, "");
        detailedItemsTable.addColumn(ITEM_NAME, "");
        detailedItemsTable.addColumn(ITEM_DESCRIPTION, "");
        detailedItemsTable.addColumn(REFERENCE, "");
        detailedItemsTable.addColumn(QTY, "");
        detailedItemsTable.addColumn(DATE, "");
        detailedItemsTable.addColumn(ITEM_UNIT_PRICE, "");
        detailedItemsTable.addColumn(ITEM_TAX_RATE, "");
        detailedItemsTable.addColumn(ITEM_TAX_AMOUNT, "");
        detailedItemsTable.addColumn(ITEM_NET_AMOUNT, "");
        detailedItemsTable.addColumn(ITEM_TOTAL_AMOUNT, "");
        detailedItemsTable.addColumn(FROM_DATE, "");
        detailedItemsTable.addColumn(TO_DATE, "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (ServerUtils.isNullOrEmpty(invoiceData.getOrderBaseinvoicedOrderIds())) {
            return detailedItemsTable;
        }

        for (EdsQuoteItem item : itemList) {
            String number = item.getQuote() != null && item.getQuote().getNumber() != null ? item.getQuote().getNumber() : "";
            String itemName = item.getItem() != null && item.getItem().getName() != null ? item.getItem().getName() : "";
            String qty = item.getQty() != null ? priceScaleNumberFormat.format(item.getQty()) : "";
            String reference = item.getQuote().getReference() != null ? item.getQuote().getReference() : "";
            String taxRate = item.getVat() != null ? priceScaleNumberFormat.format(item.getVat().getTaxRate()) : "";
            String taxAmount = item.getTaxAmount() != null ? priceScaleNumberFormat.format(item.getTaxAmount()) : "";
            String unitPrice = item.getUnitPrice() != null ? priceScaleNumberFormat.format(item.getUnitPrice()) : "";
            String description = item.getDescription() != null ? item.getDescription() : "";
            String invDate = item.getQuote() != null && item.getQuote().getInvoiceDate() != null ? simpleDateFormat.format(item.getQuote().getInvoiceDate()) : "null";
            BigDecimal netAmountBigDecimal = item.getQty().multiply(item.getUnitPrice());
            BigDecimal totalAmountBigDecimal = netAmountBigDecimal.add(Optional.ofNullable(item.getTaxAmount()).orElse(BigDecimal.ZERO));
            String netAmount = priceScaleNumberFormat.format(netAmountBigDecimal);
            String totalAmount = priceScaleNumberFormat.format(totalAmountBigDecimal);
            String fromDate = "";
            String toDate = "";
            if (item.getQuote() != null && item.getQuote().getInvoices() != null
                    && item.getQuote().getInvoices().get(0) != null
                    && ((EdsSaleInvoice) item.getQuote().getInvoices().get(0)).getFromDate() != null
                    && ((EdsSaleInvoice) item.getQuote().getInvoices().get(0)).getToDate() != null) {
                fromDate = simpleDateFormat.format(((EdsSaleInvoice) item.getQuote().getInvoices().get(0)).getFromDate());
                toDate = simpleDateFormat.format(((EdsSaleInvoice) item.getQuote().getInvoices().get(0)).getToDate());
            }

            detailedItemsTable.addRow(number, itemName, description, reference, qty, invDate, unitPrice, taxRate, taxAmount, netAmount, totalAmount, fromDate, toDate);
        }

        return detailedItemsTable;
    }

    protected CustomisedITextTable getTotalForDetailedItemsFromOrderBaseInvoice(NewInvoice invoiceData, EdsUser edsUser, List<EdsQuote> quotes) {
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        CustomisedITextTable totalTable = new CustomisedITextTable();

        if (ServerUtils.isNullOrEmpty(invoiceData.getOrderBaseinvoicedOrderIds())) {
            return totalTable;
        }

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal discountTotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (EdsQuote quote : quotes) {
            subTotal = subTotal.add(quote.getSubtotal());
            discountTotal = discountTotal.add(Optional.ofNullable(quote.getDiscountAmount()).orElse(BigDecimal.ZERO));
            taxTotal = taxTotal.add(Optional.ofNullable(quote.getTotalTaxes()).orElse(BigDecimal.ZERO));
            total = total.add(Optional.ofNullable(quote.getTotal()).orElse(BigDecimal.ZERO));
        }

        totalTable.addColumnOrder(PDFConstants.COLUMN_VALUE);
        totalTable.addRowWithCode(SUBTOTAL, priceScaleNumberFormat.format(Optional.ofNullable(subTotal).orElse(BigDecimal.ZERO)));
        totalTable.addRowWithCode(TAX_TOTAL, priceScaleNumberFormat.format(Optional.ofNullable(taxTotal).orElse(BigDecimal.ZERO)));
        totalTable.addRowWithCode(DISCOUNT_TOTAL, priceScaleNumberFormat.format(Optional.ofNullable(discountTotal).orElse(BigDecimal.ZERO)));
        totalTable.addRowWithCode(TOTAL, priceScaleNumberFormat.format(Optional.ofNullable(total).orElse(BigDecimal.ZERO)));

        return totalTable;
    }

    protected CustomisedITextTable getGroupItemName(NewInvoice invoiceData, EdsUser edsUser) {
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        CustomisedITextTable detailedItemsTable = new CustomisedITextTable();
        detailedItemsTable.addColumn(NUMBER, "");
        detailedItemsTable.addColumn(ITEM_NAME, "");
        detailedItemsTable.addColumn(ITEM_DESCRIPTION, "");
        detailedItemsTable.addColumn(REFERENCE, "");
        detailedItemsTable.addColumn(QTY, "");
        detailedItemsTable.addColumn(ITEM_TAX_RATE, "");
        detailedItemsTable.addColumn(ITEM_TAX_AMOUNT, "");
        detailedItemsTable.addColumn(ITEM_NET_AMOUNT, "");
        detailedItemsTable.addColumn(ITEM_TOTAL_AMOUNT, "");
        if (ServerUtils.isNullOrEmpty(invoiceData.getOrderBaseinvoicedOrderIds())) {
            return detailedItemsTable;
        }

        List<Object[]> list = quoteManager.getGroupedItemsByName(Stream.of(invoiceData.getOrderBaseinvoicedOrderIds().split(",")).peek(String::trim).map(Integer::valueOf).toList());
        list.forEach(objs -> {
            String itemName = (String) objs[0];
            String qty = priceScaleNumberFormat.format(objs[1]);
            String netAmount = priceScaleNumberFormat.format(objs[2]);
            String taxAmount = priceScaleNumberFormat.format(objs[3]);
            String totalAmount = priceScaleNumberFormat.format(objs[4]);
            String taxRate = "";
            Integer vatId = (Integer) objs[5];
            EdsVat vat = vatManager.get(vatId);
            if (vat != null) {
                taxRate = priceScaleNumberFormat.format(vat.getTaxRate());
            }
            detailedItemsTable.addRow(itemName, qty, taxRate, taxAmount, netAmount, totalAmount);
        });

        return detailedItemsTable;
    }

    protected CustomisedITextTable getGroupItemNameForProductTableData(NewInvoice invoiceData, EdsUser edsUser) {
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        Map<String, ArrayList<NewInvoiceItem>> itemMap = new LinkedHashMap<>();

        CustomisedITextTable detailedItemsTable = new CustomisedITextTable();
        detailedItemsTable.addColumn(ITEM_NAME, "");
        detailedItemsTable.addColumn(QTY, "");
        detailedItemsTable.addColumn(ITEM_UNIT_PRICE, "");
        detailedItemsTable.addColumn(ITEM_TAX_AMOUNT, "");
        detailedItemsTable.addColumn(ITEM_NET_AMOUNT, "");
        detailedItemsTable.addColumn(ITEM_TOTAL_AMOUNT, "");
        if (invoiceData.getItems().length == 0) {
            return detailedItemsTable;
        }

        for (NewInvoiceItem item : invoiceData.getItems()) {
            if (item.getItemName() != null) {
                if (itemMap.containsKey(item.getItemName())) {
                    itemMap.get(item.getItemName()).add(item);
                } else {
                    itemMap.put(item.getItemName(), new ArrayList<>(Collections.singletonList(item)));
                }
            }
        }

        for (Map.Entry<String, ArrayList<NewInvoiceItem>> entry : itemMap.entrySet()) {
            BigDecimal qtySum = BigDecimal.ZERO;
            BigDecimal unitPriceSum = BigDecimal.ZERO;
            BigDecimal taxAmountSum = BigDecimal.ZERO;
            BigDecimal netAmountSum = BigDecimal.ZERO;
            BigDecimal totalAmountSum = BigDecimal.ZERO;
            for (NewInvoiceItem item : entry.getValue()) {
                qtySum = qtySum.add(Optional.ofNullable(item.getQuantity()).orElse(BigDecimal.ZERO));
                unitPriceSum = unitPriceSum.add(Optional.ofNullable(item.getUnitPrice()).orElse(BigDecimal.ZERO));
                taxAmountSum = taxAmountSum.add(Optional.ofNullable(item.getTaxAmount()).orElse(BigDecimal.ZERO));
                BigDecimal netAmountBigDecimal = item.getQuantity().multiply(item.getUnitPrice());
                netAmountSum = netAmountSum.add(netAmountBigDecimal);
                totalAmountSum = totalAmountSum.add(item.getTotalAmount());
            }
            String itemName = entry.getKey();
            String qty = priceScaleNumberFormat.format(qtySum);
            String unitPrice = priceScaleNumberFormat.format(unitPriceSum);
            String taxAmount = priceScaleNumberFormat.format(taxAmountSum);
            String netAmount = priceScaleNumberFormat.format(netAmountSum);
            String totalAmount = priceScaleNumberFormat.format(totalAmountSum);

            detailedItemsTable.addRow(itemName, qty, unitPrice, taxAmount, netAmount, totalAmount);
        }

        return detailedItemsTable;
    }

    protected List<CustomisedProductCategoriesITextTable> getCustomProducCategoriesTableData(NewInvoice invoiceData, EdsUser edsUser, EdsCurrency edsCurrency) {
        List<CustomisedProductCategoriesITextTable> productCategoriesITextTable = new ArrayList<>();
        Map<String, ArrayList<NewInvoiceItem>> itemMap = new LinkedHashMap<>();
        if (invoiceData.getItems() != null && invoiceData.getItems().length > 0) {
            DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

            for (NewInvoiceItem item : invoiceData.getItems()) {
                String category = "";
                if (item.getItemCategory() != null) {
                    category = item.getItemCategory();
                } else if (item.getItemID() != null) {
                    EdsItem edsItem = itemManager.get(item.getItemID());
                    category = edsItem != null && edsItem.getCategory() != null ? edsItem.getCategory().getName() : "";
                }
                if (item.getItemID() != null && category != null && !"".equals(category)) {
                    if (itemMap.containsKey(category)) {
                        itemMap.get(category).add(item);
                    } else {
                        itemMap.put(category, new ArrayList<>(Collections.singletonList(item)));
                    }
                } else {
                    if (itemMap.containsKey(PA_NOT_AVAILABLE_STRING)) {
                        itemMap.get(PA_NOT_AVAILABLE_STRING).add(item);
                    } else {
                        itemMap.put(PA_NOT_AVAILABLE_STRING, new ArrayList<>(Collections.singletonList(item)));
                    }
                }
            }
            for (Map.Entry<String, ArrayList<NewInvoiceItem>> entry : itemMap.entrySet()) {
                CustomisedProductCategoriesITextTable categoriesTable = new CustomisedProductCategoriesITextTable();
                Map<String, String> rows = new HashMap<>();
                BigDecimal subtotalWithoutDiscount = ZERO;
                BigDecimal subtotal = ZERO;
                for (NewInvoiceItem valueItem : entry.getValue()) {
                    BigDecimal netAmount = valueItem.getQuantity().multiply(valueItem.getUnitPrice());
                    BigDecimal itemDiscount = ZERO;
                    if (valueItem.getDiscountPercent() != null) {
                        itemDiscount = netAmount.multiply(valueItem.getDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                    } else if (valueItem.getDiscountAmount() != null) {
                        itemDiscount = valueItem.getDiscountAmount();
                    }
                    if (valueItem.getDoubleDiscountPercent() != null) {
                        itemDiscount = itemDiscount.add(netAmount.multiply(valueItem.getDoubleDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP));
                    } else if (valueItem.getDoubleDiscountAmount() != null) {
                        itemDiscount = itemDiscount.add(valueItem.getDoubleDiscountAmount());
                    }
                    BigDecimal itemDiscountedNetAmount = netAmount.subtract(itemDiscount);

                    subtotalWithoutDiscount = subtotalWithoutDiscount.add(netAmount != null ? netAmount : ZERO);
                    subtotal = subtotal.add(itemDiscountedNetAmount != null ? itemDiscountedNetAmount : ZERO);
                }

                String subtotalWithoutDiscountString = priceScaleNumberFormat.format(subtotal);
                String subtotalDiscountedString = priceScaleNumberFormat.format(subtotal);
                rows.put(ITEM_CATEGORY, entry.getKey());
                rows.put(SUBTOTAL, subtotalWithoutDiscountString);
                rows.put(DISCOUNTED_SUBTOTAL, subtotalDiscountedString);
                categoriesTable.setRows(rows);

                invoiceData.setItems(entry.getValue().toArray(new NewInvoiceItem[]{}));
                CustomisedITextTable table = getCustomProducTableData(invoiceData, edsUser, edsCurrency);
                categoriesTable.setTable(table);
                productCategoriesITextTable.add(categoriesTable);
            }
        }
        return productCategoriesITextTable;
    }

    protected List<CustomisedProductCategoriesITextTable> getGroupItemWithCustomField(NewInvoice invoiceData, EdsUser edsUser, EdsCurrency edsCurrency) {
        List<CustomisedProductCategoriesITextTable> groupItemCustomFieldITextTable = new ArrayList<>();
        Map<String, ArrayList<NewInvoiceItem>> itemMap = new LinkedHashMap<>();
        if (invoiceData.getItems() != null && invoiceData.getItems().length > 0) {
            DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

            for (NewInvoiceItem item : invoiceData.getItems()) {
                if (item.getCustomFieldItems() != null && !item.getCustomFieldItems().isEmpty()) {
                    for (CompanyCustomFieldItem itemCustomField : item.getCustomFieldItems()) {
                        if (!ServerUtils.isNullOrEmpty(itemCustomField.getFieldStringValue())) {
                            if (itemMap.containsKey(itemCustomField.getFieldStringValue())) {
                                itemMap.get(itemCustomField.getFieldStringValue()).add(item);
                            } else {
                                itemMap.put(itemCustomField.getFieldStringValue(), new ArrayList<>(Collections.singletonList(item)));
                            }
                        } else {
                            if (itemMap.containsKey(PA_NOT_AVAILABLE_STRING)) {
                                itemMap.get(PA_NOT_AVAILABLE_STRING).add(item);
                            } else {
                                itemMap.put(PA_NOT_AVAILABLE_STRING, new ArrayList<>(Collections.singletonList(item)));
                            }
                        }
                    }
                }
            }
            for (Map.Entry<String, ArrayList<NewInvoiceItem>> entry : itemMap.entrySet()) {
                CustomisedProductCategoriesITextTable groupItemCustomFieldTable = new CustomisedProductCategoriesITextTable();
                Map<String, String> rows = new HashMap<>();
                BigDecimal subtotalWithoutDiscount = ZERO;
                BigDecimal subtotal = ZERO;
                for (NewInvoiceItem valueItem : entry.getValue()) {
                    BigDecimal netAmount = valueItem.getQuantity().multiply(valueItem.getUnitPrice());
                    BigDecimal itemDiscount = ZERO;
                    if (valueItem.getDiscountPercent() != null) {
                        itemDiscount = netAmount.multiply(valueItem.getDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                    } else if (valueItem.getDiscountAmount() != null) {
                        itemDiscount = valueItem.getDiscountAmount();
                    }
                    if (valueItem.getDoubleDiscountPercent() != null) {
                        itemDiscount = itemDiscount.add(netAmount.multiply(valueItem.getDoubleDiscountPercent()).divide(HUNDRED, 4, RoundingMode.HALF_UP));
                    } else if (valueItem.getDoubleDiscountAmount() != null) {
                        itemDiscount = itemDiscount.add(valueItem.getDoubleDiscountAmount());
                    }
                    BigDecimal itemDiscountedNetAmount = netAmount.subtract(itemDiscount);

                    subtotalWithoutDiscount = subtotalWithoutDiscount.add(netAmount != null ? netAmount : ZERO);
                    subtotal = subtotal.add(itemDiscountedNetAmount != null ? itemDiscountedNetAmount : ZERO);
                }

                String subtotalWithoutDiscountString = priceScaleNumberFormat.format(subtotal);
                String subtotalDiscountedString = priceScaleNumberFormat.format(subtotal);
                rows.put("ITEM_CUSTOM_FIELD", entry.getKey());
                rows.put(SUBTOTAL, subtotalWithoutDiscountString);
                rows.put(DISCOUNTED_SUBTOTAL, subtotalDiscountedString);
                groupItemCustomFieldTable.setRows(rows);

                invoiceData.setItems(entry.getValue().toArray(new NewInvoiceItem[]{}));
                CustomisedITextTable table = getCustomProducTableData(invoiceData, edsUser, edsCurrency);
                groupItemCustomFieldTable.setTable(table);
                groupItemCustomFieldITextTable.add(groupItemCustomFieldTable);
            }
        }
        return groupItemCustomFieldITextTable;
    }

    protected CustomisedITextTable getCustomPaymentHistory(NewInvoice invoiceData, EdsUser edsUser, EdsCurrency edsCurrency) {
        CustomisedITextTable paynethistory = new CustomisedITextTable();

        /*1 */
        paynethistory.addColumn(ITEM_PAYMENT_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.date));
        /*2 */
        paynethistory.addColumn(ITEM_PAYMENT_USER, accountingLocalizer.localizeAccounting(PdfLocalizationName.user));
        /*3 */
        paynethistory.addColumn(ITEM_PAYMENT_AMOUNT, accountingLocalizer.localizeAccounting(PdfLocalizationName.amount));
        /*4 */
        paynethistory.addColumn(ITEM_PAYMENT_CUSTOMER, commonLocalizer.localizeAccounting(PdfLocalizationName.customer));

        paynethistory.addColumn(ITEM_PAYMENT_REFERENCE, commonLocalizer.localizeAccounting(PdfLocalizationName.reference));

        String paidToLocalize = commonLocalizer.localizeAccounting(PdfLocalizationName.paidTo);
        paynethistory.addColumn(ITEM_PAYMENT_PAID_TO, paidToLocalize);

        paynethistory.addColumn(PO_PAYMENT_TYPE, "");

        paynethistory.addColumn(ITEM_PAYMENT_DETAIL, "");


        List<String> columnsValue = new ArrayList<>();

        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        if (invoiceData.getPaymentItems() != null && invoiceData.getPaymentItems().length > 0) {
            Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new LinkedHashMap<>();
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            for (int i = 0; i <= invoiceData.getPaymentItems().length - 1; i++) {
                LinkedHashMap<String, Map<String, String>> paymentCustomFields = new LinkedHashMap<>();
                PaymentItem item = invoiceData.getPaymentItems()[i];

                String paymentDate = dateFormat.format(item.getDate().getNonConvertedDate());
                String user = item.getUser();
                String amount = priceScaleFormat.format(item.getAmount() != null ? item.getAmount() : ZERO);
                String crmAccount = item.getCrmAccount() != null ? item.getCrmAccount().getName() : "";
                String reference = item.getReference() != null ? item.getReference() : "";
                String paidTo = item.getPaidTo() != null ? item.getPaidTo() : "";
                String type = item.getType() != null ? item.getType() : "";

                String detail = "";
                if (RECEIVABLE.equals(invoiceData.getType())) {
                    detail = accountingLocalizer.localizeWithParam(PAYMENT_RECEIVED_FROM_WITH_DATE, new String[]{crmAccount, paymentDate, amount});
                    detail = detail == null ? "" : detail + " (" + paidToLocalize + ":" + paidTo + ")";
                }

                columnsValue.clear();
                if (paynethistory.containsColumn(ITEM_PAYMENT_DATE)) {
                    columnsValue.add(escapeHtml(paymentDate));
                }
                if (paynethistory.containsColumn(ITEM_PAYMENT_USER)) {
                    columnsValue.add(escapeHtml(user));
                }
                if (paynethistory.containsColumn(ITEM_PAYMENT_AMOUNT)) {
                    columnsValue.add(escapeHtml(amount));
                }
                if (paynethistory.containsColumn(ITEM_PAYMENT_CUSTOMER)) {
                    columnsValue.add(escapeHtml(crmAccount));
                }
                if (paynethistory.containsColumn(ITEM_PAYMENT_REFERENCE)) {
                    columnsValue.add(escapeHtml(reference));
                }
                if (paynethistory.containsColumn(ITEM_PAYMENT_PAID_TO)) {
                    columnsValue.add(escapeHtml(paidTo));
                }
                if (paynethistory.containsColumn(PO_PAYMENT_TYPE)) {
                    columnsValue.add(escapeHtml(type));
                }
                if (paynethistory.containsColumn(ITEM_PAYMENT_DETAIL)) {
                    columnsValue.add(escapeHtml(detail));
                }
                paynethistory.addRow(columnsValue.toArray(new String[]{}));

                EdsInvoice creditNote = null;
                if (item.getCreditNote() != null) {
                    creditNote = invoiceManager.get(item.getCreditNote().getId());
                }
                if (creditNote != null) {
                    if (creditNote.getCustomFields() != null) {
                        List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(creditNote.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleInvoice));
                        for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                            if (customFieldItem != null) {
                                Map<String, String> cols = new HashMap<>();
                                cols.put(COLUMN_NAME, customFieldItem.getFieldName() != null ? escapeHtml(customFieldItem.getFieldName()) : null);
                                if (CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
                                    cols.put(COLUMN_VALUE, customFieldItem.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                                } else {
                                    if (edsUser.getCompany().getObjectID().equals("50734") && customFieldItem.getFieldName().equals("Credit Note Type") && customFieldItem.getFieldStringValue().equals("Deduction")) {
                                        List<EdsInvoiceItem> items = creditNote.getInvoiceItems();
                                        if (!items.isEmpty()) {
                                            String description = items.get(0).getDescription();
                                            cols.put(COLUMN_VALUE, !description.isEmpty() ? escapeHtml(description) : escapeHtml(customFieldItem.getFieldStringValue()));
                                        } else {
                                            cols.put(COLUMN_VALUE, customFieldItem.getFieldStringValue() != null ? escapeHtml(customFieldItem.getFieldStringValue()) : null);
                                        }
                                    } else {
                                        cols.put(COLUMN_VALUE, customFieldItem.getFieldStringValue() != null ? escapeHtml(customFieldItem.getFieldStringValue()) : null);
                                    }
                                }
                                if (customFieldItem.getFieldName() != null) {
                                    paymentCustomFields.put(escapeHtml(customFieldItem.getFieldName()), cols);
                                }
                            }
                        }
                    }
                    String no = String.valueOf((i + 1));
                    customFields.put(no, paymentCustomFields);
                }
            }
            paynethistory.setCustomFields(customFields);
        }
        return paynethistory;
    }

    protected CustomisedITextTable getCustomProductSerialTable(NewInvoice invoiceData, EdsUser edsUser) {
        CustomisedITextTable productSerialTable = new CustomisedITextTable();
        productSerialTable.addColumn(ITEM_SERIAL_NUMBER, "Serial Number");
        productSerialTable.addColumn(ITEM_LOT_NUMBER, "Lot Number");
        productSerialTable.addColumn(ITEM_REF_NUMBER, "Ref Number");
        productSerialTable.addColumn(ITEM_EXPIRATION_DATE, "Expiry Date");

        List<String> columnsValue = new ArrayList<>();

        SimpleDateFormat dateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        if (PURCHASE_INVOICE.equals(getFromInvoice())) {

            EdsPurchaseOrder edsPurchaseOrder = quoteManager.getPurchaseOrderByID(invoiceData.getConvertedItemID());

            if (edsPurchaseOrder != null) {

                Set<Integer> purchaseInvoiceItemIds = new HashSet<>();

                for (NewInvoiceItem invoiceItem : invoiceData.getItems()) {
                    purchaseInvoiceItemIds.add(invoiceItem.getItemID());
                }

                for (EdsQuoteItem edsQuoteItem : edsPurchaseOrder.getQuoteItems()) {

                    if (edsQuoteItem != null && edsQuoteItem.getItem() != null
                            && purchaseInvoiceItemIds.contains(edsQuoteItem.getItem().getObjectID())) {

                        List<ProductSerialItem> productSerialItems = productSerialManager.getProductSerialByIds(edsQuoteItem.getObjectID(),
                                edsQuoteItem.getItem().getObjectID(),
                                PURCHASE_ORDER);

                        if (productSerialItems != null && !productSerialItems.isEmpty()) {

                            for (ProductSerialItem serialItem : productSerialItems) {

                                columnsValue.clear();

                                String serialNumber = "", lotNumber = "", refNumber = "", expirationDate = "";

                                serialNumber = serialItem.getSerial();

                                lotNumber = serialItem.getLotNumber();

                                refNumber = serialItem.getRefNumber();

                                if (serialItem.getExpirationDate() != null) {
                                    expirationDate = dateFormat.format(serialItem.getExpirationDate());
                                }

                                if (productSerialTable.containsColumn(ITEM_SERIAL_NUMBER)) {
                                    columnsValue.add(escapeHtml(serialNumber));
                                }
                                if (productSerialTable.containsColumn(ITEM_LOT_NUMBER)) {
                                    columnsValue.add(escapeHtml(lotNumber));
                                }
                                if (productSerialTable.containsColumn(ITEM_REF_NUMBER)) {
                                    columnsValue.add(escapeHtml(refNumber));
                                }
                                if (productSerialTable.containsColumn(ITEM_EXPIRATION_DATE)) {
                                    columnsValue.add(escapeHtml(expirationDate));
                                }

                                productSerialTable.addRow(columnsValue.toArray(new String[]{}));
                            }
                        }
                    }
                }
            }
        }

        return productSerialTable;
    }

    /**
     * addRow Max Length and Min Length 4
     * <p/>
     * if(addRow[0]) <b> SubTotal </b>
     * if(addRow[1]) <b> Discount Amount </b>
     * if(addRow[2]) <b> Vat Table </b>  Vat Table First Row Name  nor should. Vat Name get in Data Base
     * if(addRow[3]) <b> Shipping </b>
     * if(addRow[4]) <b> Total </b>
     *
     * @param edsUser
     * @param edsCurrency
     * @param invoiceData
     * @param rowsMap
     * @return
     */
    protected ITextTableList getTotalTable(EdsUser edsUser,
                                           EdsCurrency edsCurrency,
                                           NewInvoice invoiceData,
                                           Map<String, String> rowsMap) {
        final Map<String, Object> values = getTotalTableMap(edsUser, edsCurrency, invoiceData, false);
        final ITextTableList invoiceTotalTable = new ITextTableList(2);
        int k = 0;

        if (rowsMap.containsKey(SUBTOTAL)) {
            invoiceTotalTable.addPdfTableRows(rowsMap.get(SUBTOTAL), (String) values.get(SUBTOTAL));
        }
        if (rowsMap.containsKey(DISCOUNT_TOTAL) && values.get(DISCOUNT_TOTAL) != null) {
            invoiceTotalTable.addPdfTableRows(rowsMap.get(DISCOUNT_TOTAL), (String) values.get(DISCOUNT_TOTAL));
        }
        if (rowsMap.containsKey(DISCOUNTED_SUBTOTAL)) {
            invoiceTotalTable.addPdfTableRows(rowsMap.get(DISCOUNTED_SUBTOTAL), (String) values.get(DISCOUNTED_SUBTOTAL));
        }
        if (values.containsKey(GROUP_TAX_)) {
            List<String[]> groupTaxList = (List<String[]>) values.get(GROUP_TAX_);
            for (String[] cols : groupTaxList) {
                invoiceTotalTable.addPdfTableRows(cols[0], cols[1]);
            }
        } else {
            if (values.containsKey(TAX_)) {
                List<String[]> taxList = (List<String[]>) values.get(TAX_);
                for (String[] cols : taxList) {
                    invoiceTotalTable.addPdfTableRows(cols[0], cols[1]);
                }
            }
        }
        if (rowsMap.containsKey(SHIPPING_TOTAL) && values.get(SHIPPING_TOTAL) != null) {
            invoiceTotalTable.addPdfTableRows(rowsMap.get(SHIPPING_TOTAL), (String) values.get(SHIPPING_TOTAL));
            if (values.get(SHIPPING_VAT) != null) {
                invoiceTotalTable.addPdfTableRows((String) values.get(SHIPPING_VAT_NAME), (String) values.get(SHIPPING_VAT));
            }
        }

        if (rowsMap.containsKey(BILL_EXP_TOTAL)) {
            invoiceTotalTable.addPdfTableRows(rowsMap.get(BILL_EXP_TOTAL), (String) values.get(BILL_EXP_TOTAL));
        }

        if (rowsMap.containsKey(BILL_EXP_TAX_TOTAL)) {
            invoiceTotalTable.addPdfTableRows(rowsMap.get(BILL_EXP_TAX_TOTAL), (String) values.get(BILL_EXP_TAX_TOTAL));
        }

        if (rowsMap.containsKey(TOTAL)) {
            invoiceTotalTable.addPdfTableRows(rowsMap.get(TOTAL), (String) values.get(TOTAL));
        }
        List<String[]> paymentList = (List<String[]>) values.get(PAYMENT_);
        if (!paymentList.isEmpty()) {
            for (String[] row : paymentList) {
                invoiceTotalTable.addPdfTableRows(row[0], row[1]);
            }
            invoiceTotalTable.addPdfTableRows("Due Amount", (String) values.get(DUE_AMOUNT));
        }
        return invoiceTotalTable;
    }

    protected CustomisedITextTable getCustomisedTotalTable(EdsUser edsUser, EdsCurrency edsCurrency, NewInvoice invoiceData) {
        Map<String, Object> values = getTotalTableMap(edsUser, edsCurrency, invoiceData, true);

        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        totalTable.addRowWithCode(SUBTOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal),
                (String) values.get(SUBTOTAL), SUBTOTAL);
        totalTable.addRowWithCode("EXCHANGE_RATE", pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.exchangeRate),
                (String) values.get("EXCHANGE_RATE"), "EXCHANGE_RATE");
        totalTable.addRowWithCode("EXCHANGE_RATE_REVERSE", pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.exchangeRate),
                (String) values.get("EXCHANGE_RATE_REVERSE"), "EXCHANGE_RATE_REVERSE");
        totalTable.addRowWithCode("TOTAL_AMOUNT_AED", "Total Amount in AED",
                (String) values.get("TOTAL_AMOUNT_AED"), "TOTAL_AMOUNT_AED");
        totalTable.addRowWithCode("EXCHANGE_RATE_AED", "Exchange Rate in AED",
                (String) values.get("EXCHANGE_RATE_AED"), "EXCHANGE_RATE_AED");
        totalTable.addRowWithCode(SUBTOTAL_IN_BASE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal),
                (String) values.get(SUBTOTAL_IN_BASE), SUBTOTAL_IN_BASE);
        totalTable.addRowWithCode(SUBTOTAL_WORD, accountingLocalizer.localizeAccounting(PdfLocalizationName.subtotalToWord), (String) values.get(SUBTOTAL_WORD), SUBTOTAL_WORD);
        if (values.containsKey(DISCOUNT_TOTAL)) {
            totalTable.addRowWithCode(DISCOUNT_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount),
                    (String) values.get(DISCOUNT_TOTAL), DISCOUNT_TOTAL);
        }
        totalTable.addRowWithCode(DISCOUNTED_SUBTOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountedSubTotal),
                (String) values.get(DISCOUNTED_SUBTOTAL), DISCOUNTED_SUBTOTAL);
        totalTable.addRowWithCode(PAYMENT_TOTAL, commonLocalizer.localize(PdfLocalizationName.paymentTotal), (String) values.get(PAYMENT_TOTAL), PAYMENT_TOTAL);
        totalTable.addRowWithCode(HAS_BILL_EXP_TOTAL, "",
                (String) values.get(HAS_BILL_EXP_TOTAL), HAS_BILL_EXP_TOTAL);
        totalTable.addRowWithCode(BILL_EXP_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billableExpense),
                (String) values.get(BILL_EXP_TOTAL), BILL_EXP_TOTAL);
        totalTable.addRowWithCode(BILL_EXP_TAX_TOTAL, "Billable Expense Tax",
                (String) values.get(BILL_EXP_TAX_TOTAL), BILL_EXP_TAX_TOTAL);

        totalTable.addRowWithCode(BTW_MIN_TOTAL, "BTW Min Total",
                (String) values.get(BTW_MIN_TOTAL), BTW_MIN_TOTAL);
        totalTable.addRowWithCode(BTW_MAX_TOTAL, "BTW Max Total",
                (String) values.get(BTW_MAX_TOTAL), BTW_MAX_TOTAL);
        totalTable.addRowWithCode(BTW_TOTAL, "BTW Total",
                (String) values.get(BTW_TOTAL), BTW_TOTAL);
        totalTable.addRowWithCode(EIND_SUBTOTAL, "Eind Subtotal",
                (String) values.get(EIND_SUBTOTAL), EIND_SUBTOTAL);
        totalTable.addRowWithCode(EINDTOTAL, "Eind Total",
                (String) values.get(EINDTOTAL), EINDTOTAL);
        totalTable.addRowWithCode(INVOICE_QUOTE_UNREC_REVENUE_TOTAL, "Unrecognize Quote Total",
                (String) values.get(INVOICE_QUOTE_UNREC_REVENUE_TOTAL), INVOICE_QUOTE_UNREC_REVENUE_TOTAL);
        totalTable.addRowWithCode(TOTAL_QUONTITY, "Quontity",
                (String) values.get(TOTAL_QUONTITY), TOTAL_QUONTITY);

        int i = 0;
        List<String[]> taxList = (List<String[]>) values.get(TAX_);
        for (String[] cols : taxList) {
            totalTable.addRowWithCode(TAX_ + i, cols[0], cols[1], TAX_);
            i++;
        }
        totalTable.addRowWithCode(TAX_TOTAL, accountingLocalizer.localizeAccounting(PdfLocalizationName.taxTotal), (String) values.get(TAX_TOTAL), TAX_TOTAL);
        totalTable.addRowWithCode(TAX_TOTAL_IN_BASE, accountingLocalizer.localizeAccounting(PdfLocalizationName.taxTotal), (String) values.get(TAX_TOTAL_IN_BASE), TAX_TOTAL_IN_BASE);
        if (values.get(SHIPPING_TOTAL) != null) {
            String shippingMethodName = values.get(SHIPPING_TOTAL_NAME) != null && !"".equals(values.get(SHIPPING_TOTAL_NAME)) ? (String) values.get(SHIPPING_TOTAL_NAME) : null;
            totalTable.addRowWithCode(SHIPPING_TOTAL, shippingMethodName != null ? shippingMethodName : accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingTotal), (String) values.get(SHIPPING_TOTAL), SHIPPING_TOTAL);
            if (values.get(SHIPPING_VAT) != null) {
                String shippingVatName = values.get(SHIPPING_VAT_NAME) != null && !"".equals(values.get(SHIPPING_VAT_NAME)) ? (String) values.get(SHIPPING_VAT_NAME) : null;
                totalTable.addRowWithCode(SHIPPING_VAT, shippingVatName != null ? shippingVatName : accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingVat), (String) values.get(SHIPPING_VAT), SHIPPING_VAT);
            }
        }
        totalTable.addRowWithCode(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total), (String) values.get(TOTAL), TOTAL);
        totalTable.addRowWithCode(TOTAL_IN_BASE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total), (String) values.get(TOTAL_IN_BASE), TOTAL_IN_BASE);
        totalTable.addRowWithCode(TOTAL_WORD, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(TOTAL_WORD), TOTAL_WORD);
        totalTable.addRowWithCode(TOTAL_IN_BASE_WORD, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(TOTAL_IN_BASE_WORD), TOTAL_IN_BASE_WORD);
        totalTable.addRowWithCode(TOTAL_IN_USD_WORD, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(TOTAL_IN_USD_WORD), TOTAL_IN_USD_WORD);
        totalTable.addRowWithCode(TOTAL_WORD_ALL, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(TOTAL_WORD_ALL), TOTAL_WORD_ALL);
        totalTable.addRowWithCode(TOTAL_UZB_WORD_ALL, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(TOTAL_UZB_WORD_ALL), TOTAL_UZB_WORD_ALL);
        totalTable.addRowWithCode(TOTAL_UZB_WORD_ALL_LOTIN, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(TOTAL_UZB_WORD_ALL_LOTIN), TOTAL_UZB_WORD_ALL_LOTIN);
        totalTable.addRowWithCode(SUBTOTAL_WORD_ALL, accountingLocalizer.localizeAccounting(PdfLocalizationName.subtotalToWord), (String) values.get(SUBTOTAL_WORD_ALL), SUBTOTAL_WORD_ALL);
        totalTable.addRowWithCode(DISCOUNT_TOTAL_WORD_ALL, "Discount Word", (String) values.get(DISCOUNT_TOTAL_WORD_ALL), DISCOUNT_TOTAL_WORD_ALL);
        if (invoiceData.isCreditNote()) {
            if (values.get(CREDIT_NOTE_IN_TOTAL) != null && values.get(CREDIT_NOTE_BALANCE) != null) {
                totalTable.addRowWithCode(CREDIT_NOTE_IN_TOTAL, accountingLocalizer.localizeAccounting(PdfLocalizationName.creditNoteInvoiceTotal), (String) values.get(CREDIT_NOTE_IN_TOTAL), CREDIT_NOTE_IN_TOTAL);
                totalTable.addRowWithCode(CREDIT_NOTE_BALANCE, accountingLocalizer.localizeAccounting(PdfLocalizationName.creditNoteBalance), (String) values.get(CREDIT_NOTE_BALANCE), CREDIT_NOTE_BALANCE);
            }
            if (values.get(CREDIT_NOTE_INVOICE_SUB_TOTAL) != null) {
                totalTable.addRowWithCode(CREDIT_NOTE_INVOICE_SUB_TOTAL, "Credit Note Invoice Subtotal", (String) values.get(CREDIT_NOTE_INVOICE_SUB_TOTAL), CREDIT_NOTE_INVOICE_SUB_TOTAL);
            }
            if (values.get(CREDIT_NOTE_INVOICE_REVISED_SUB_TOTAL) != null) {
                totalTable.addRowWithCode(CREDIT_NOTE_INVOICE_REVISED_SUB_TOTAL, "Credit Note Invoice Revised Subtotal", (String) values.get(CREDIT_NOTE_INVOICE_REVISED_SUB_TOTAL), CREDIT_NOTE_INVOICE_REVISED_SUB_TOTAL);
            }
        }

        if (values.get(TOTAL_PROFIT_AMOUNT) != null) {
            totalTable.addRowWithCode(TOTAL_PROFIT_AMOUNT, "Total Profit",
                    (String) values.get(TOTAL_PROFIT_AMOUNT), TOTAL_PROFIT_AMOUNT);
        }
        i = 0;
        List<String[]> paymentList = (List<String[]>) values.get(PAYMENT_);
        for (String[] cols : paymentList) {
            totalTable.addRowWithCode(PAYMENT_ + i, cols[0], cols[1], PAYMENT_);
            i++;
        }
        totalTable.addRowWithCode(DUE_AMOUNT, commonLocalizer.localizeAccounting(PdfLocalizationName.dueAmount), (String) values.get(DUE_AMOUNT), DUE_AMOUNT);
        totalTable.addRowWithCode(DUE_AMOUNT_WORD, "Due Amount Word", (String) values.get(DUE_AMOUNT_WORD), DUE_AMOUNT_WORD);
        totalTable.addRowWithCode(LAST_PAYMENT, "Last Payment", (String) values.get(LAST_PAYMENT), LAST_PAYMENT);
        totalTable.addRowWithCode(TOTAL_ARABIC_WORD_ALL, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(TOTAL_ARABIC_WORD_ALL), TOTAL_ARABIC_WORD_ALL);
        totalTable.addRowWithCode(DUE_AMOUNT_ARABIC_WORD, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), (String) values.get(DUE_AMOUNT_ARABIC_WORD), DUE_AMOUNT_ARABIC_WORD);
        totalTable.addRowWithCode(TOTAL_OVERALL_DISCOUNT, "Overall Discount Total", (String) values.get(TOTAL_OVERALL_DISCOUNT), TOTAL_OVERALL_DISCOUNT);
        if (invoiceData.isProgressInvoicing() && values.get(INVOICED_AMOUNT) != null && values.get(REMAINING_BALANCE) != null) {
            totalTable.addRowWithCode(INVOICED_AMOUNT, commonLocalizer.localize("invoicedAmount"), (String) values.get(INVOICED_AMOUNT), INVOICED_AMOUNT);
            totalTable.addRowWithCode(REMAINING_BALANCE, commonLocalizer.localize("remainingBalance"), (String) values.get(REMAINING_BALANCE), REMAINING_BALANCE);
        }

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PRE_PAYMENT_AMOUNT) && values.get("PRE_PAYMENT") != null && values.get("REMAINING_AMOUNT") != null) {
            i = 0;
            List<String[]> prePaymentList = (List<String[]>) values.get("PRE_PAYMENT");
            for (String[] cols : prePaymentList) {
                totalTable.addRowWithCode("PRE_PAYMENT" + i, "", cols[0], "PRE_PAYMENT");
                i++;
            }
            totalTable.addRowWithCode("REMAINING_AMOUNT", "Remainning Amount", (String) values.get("REMAINING_AMOUNT"), "REMAINING_AMOUNT");
        }

        totalTable.addRowWithCode("TOTAL_WORD_ALL_WITH_CENT", "Total word all with cent", (String) values.get("TOTAL_WORD_ALL_WITH_CENT"), "TOTAL_WORD_ALL_WITH_CENT");

        return totalTable;
    }

    private Map<String, Object> getTotalTableMap(EdsUser edsUser, EdsCurrency edsCurrency, NewInvoice invoiceData, boolean customised) {
        List<InnerVatClass> vatList = new LinkedList<>();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        BigDecimal exchangeRate = invoiceData.getExchageRate().compareTo(ZERO) != 0 ? invoiceData.getExchageRate() : new BigDecimal("1.00");
        BigDecimal totalDiscountAmount = ZERO;
        boolean isGroupTax = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.GROUP_TAX_ENABLED);
        boolean isDoubleTax = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_TAX_ENABLED);
        BigDecimal itemTotalProfitAmount = ZERO;
        Map<Integer, InnerVatClass> taxMap = new HashMap<>();

        //T13495 Company ID lar: 65847, 65988
        Double exchange;
        EdsCurrency currency = currencyManager.getCurrency(CurrencyManager.USD);
        if (Objects.equals(invoiceData.getCurrencyID(), currency.getObjectID())) {
            exchange = invoiceData.getExchageRate().doubleValue();
        } else {
            CurrencyListItem currencyUSD = currencyService.getCurrencyRateByDate(currency.getObjectID(), invoiceData.getInvoiceDate());
            exchange = currencyUSD.getExchangeRate();
        }
        BigDecimal exchangeRateInUsd = BigDecimal.valueOf(exchange).setScale(4, RoundingMode.HALF_UP);
        BigDecimal exchangeRateInUsdReverse = BigDecimal.valueOf(1 / exchange).setScale(4, RoundingMode.HALF_UP);

        String exchangerate = exchangeRateInUsd.toString();
        String exchangeReverse = exchangeRateInUsdReverse.toString();

        Double exchangeAed = 1d;
        BigDecimal exchangeRateInAed = null;
        String exchangeRateAed = "";
        BigDecimal totalAmountInAed = ZERO;
        //T14610 required by 74078 NEW VISION company
        if (invoiceData.getCurrencyName() != null && invoiceData.getCurrencyName().equals("USD")) {
            EdsCurrency currencyAED = currencyManager.getCurrency("AED");
            CurrencyListItem currencyInAED = currencyService.getCurrencyRateByDate(currencyAED.getObjectID(), invoiceData.getInvoiceDate());
            exchangeAed = currencyInAED.getExchangeRate();
            exchangeRateInAed = BigDecimal.valueOf(exchangeAed).setScale(4, RoundingMode.HALF_UP);
            exchangeRateAed = exchangeRateInAed.toString();
            totalAmountInAed = invoiceData.getTotal().multiply(exchangeRateInAed);
        }

        for (int i = 0; i <= invoiceData.getItems().length - 1; i++) {
            NewInvoiceItem item = invoiceData.getItems()[i];
            BigDecimal netAmount = item.getQuantity().multiply(item.getUnitPrice());
            BigDecimal itemDiscount = ZERO;
            if (item.getDiscountPercent() != null) {
                itemDiscount = netAmount.multiply(item.getDiscountPercent() != null ? item.getDiscountPercent() : ZERO).divide(HUNDRED, 4, RoundingMode.HALF_UP);
            } else if (item.getDiscountAmount() != null) {
                itemDiscount = item.getDiscountAmount();
            }
            BigDecimal itemDiscount2 = ZERO;
            if (item.getDiscountPercent() != null) {
                itemDiscount2 = netAmount.multiply(item.getDoubleDiscountPercent() != null ? item.getDoubleDiscountPercent() : ZERO).divide(HUNDRED, 4, RoundingMode.HALF_UP);
            } else if (item.getDoubleDiscountAmount() != null) {
                itemDiscount2 = item.getDoubleDiscountAmount();
            }
            BigDecimal itemNetAmount = netAmount.subtract(itemDiscount.add(itemDiscount2));
            totalDiscountAmount = totalDiscountAmount.add(itemDiscount.add(itemDiscount2));
            if (item.getTaxItem() != null && item.getTaxItem().getId() != null) {
                EdsVat vat = vatManager.get(item.getTaxItem().getId());
                InnerVatClass vatClass = new InnerVatClass();
                vatClass.setVat(vat);
                vatClass.setVatID(vat.getObjectID());

                BigDecimal taxAmount = item.getTaxAmount();
                if (taxAmount == null) {
                    BigDecimal taxPercent = (item.getTaxItem() != null && item.getTaxItem().getEffectiveTaxPercent() != null) ? item.getTaxItem().getEffectiveTaxPercent() : ZERO;
                    if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                        taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, RoundingMode.HALF_UP);
                    } else {
                        taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                    }
                }
                vatClass.setVatAmount(taxAmount);
                vatList.add(vatClass);

                if (isGroupTax && !customised) {
                    if (vat.getGroupTax()) {
                        for (EdsTaxGroupItem edsTaxGroupItem : vat.getGroupItems()) {
                            EdsVat edsVatItem = edsTaxGroupItem.getItem();
                            InnerVatClass vatItemClass = new InnerVatClass();
                            vatItemClass.setVat(edsVatItem);
                            vatItemClass.setVatID(edsVatItem.getObjectID());
                            BigDecimal taxPercent2 = edsVatItem.getEffectiveRateAsBigDecimal() != null ? edsVatItem.getEffectiveRateAsBigDecimal() : ZERO;
                            if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                                taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED.add(taxPercent2), 4, RoundingMode.HALF_UP);
                            } else {
                                taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                            }
                            if (taxMap.containsKey(vatItemClass.getVatID())) {
                                InnerVatClass existVatClass = taxMap.get(vatItemClass.getVatID());
                                taxAmount = existVatClass.getVatAmount().add(taxAmount);
                                existVatClass.setVatAmount(taxAmount);
                                taxMap.put(existVatClass.getVatID(), existVatClass);
                            } else {
                                vatItemClass.setVatAmount(taxAmount);
                                taxMap.put(vatItemClass.getVatID(), vatItemClass);
                            }
                        }
                    } else {
                        if (taxMap.containsKey(vatClass.getVatID())) {
                            InnerVatClass existVatClass = taxMap.get(vatClass.getVatID());
                            taxAmount = existVatClass.getVatAmount().add(vatClass.getVatAmount());
                            existVatClass.setVatAmount(taxAmount);
                            taxMap.put(existVatClass.getVatID(), existVatClass);
                        } else {
                            taxMap.put(vatClass.getVatID(), vatClass);
                        }
                    }
                }
            }
            if (isGroupTax && isDoubleTax && !customised
                    && item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getId() != null) {
                EdsVat vat = vatManager.get(item.getDoubleTaxItem().getId());
                InnerVatClass vatClass = new InnerVatClass();
                vatClass.setVat(vat);
                vatClass.setVatID(vat.getObjectID());

                BigDecimal taxAmount = item.getDoubleTaxAmount();
                if (taxAmount == null) {
                    BigDecimal taxPercent = (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getEffectiveTaxPercent() != null) ? item.getDoubleTaxItem().getEffectiveTaxPercent() : ZERO;
                    if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                        taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, RoundingMode.HALF_UP);
                    } else {
                        taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                    }
                }
                vatClass.setVatAmount(taxAmount);

                if (vat.getGroupTax()) {
                    for (EdsTaxGroupItem edsTaxGroupItem : vat.getGroupItems()) {
                        EdsVat edsVatItem = edsTaxGroupItem.getItem();
                        InnerVatClass vatItemClass = new InnerVatClass();
                        vatItemClass.setVat(edsVatItem);
                        vatItemClass.setVatID(edsVatItem.getObjectID());
                        BigDecimal taxPercent2 = edsVatItem.getEffectiveRateAsBigDecimal() != null ? edsVatItem.getEffectiveRateAsBigDecimal() : ZERO;
                        if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                            taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED.add(taxPercent2), 4, RoundingMode.HALF_UP);
                        } else {
                            taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                        }
                        if (taxMap.containsKey(vatItemClass.getVatID())) {
                            InnerVatClass existVatClass = taxMap.get(vatItemClass.getVatID());
                            taxAmount = existVatClass.getVatAmount().add(taxAmount);
                            existVatClass.setVatAmount(taxAmount);
                            taxMap.put(existVatClass.getVatID(), existVatClass);
                        } else {
                            vatItemClass.setVatAmount(taxAmount);
                            taxMap.put(vatItemClass.getVatID(), vatItemClass);
                        }
                    }
                } else {
                    if (taxMap.containsKey(vatClass.getVatID())) {
                        InnerVatClass existVatClass = taxMap.get(vatClass.getVatID());
                        taxAmount = existVatClass.getVatAmount().add(vatClass.getVatAmount());
                        existVatClass.setVatAmount(taxAmount);
                        taxMap.put(existVatClass.getVatID(), existVatClass);
                    } else {
                        taxMap.put(vatClass.getVatID(), vatClass);
                    }
                }
            }

            if (SALE_INVOICE.equals(getFromInvoice()) && item.getItemID() != null) {
                EdsInvoice edsInvoice = invoiceManager.get(invoiceData.getID());
                if (edsInvoice != null) {
                    EdsTransaction edsTransaction = transactionManager.getTransactionByInvoice(edsInvoice);
                    if (edsTransaction != null) {
                        BigDecimal cogsPrice = itemStockManager.getTransactionValueByTransactionIdAndItemId(edsTransaction.getObjectID(), item.getItemID());
                        if (cogsPrice != null && cogsPrice.compareTo(ZERO) > 0 && itemNetAmount != null && itemNetAmount.compareTo(ZERO) > 0) {
                            cogsPrice = cogsPrice != null ? cogsPrice.multiply(exchangeRate) : ZERO;
                            itemTotalProfitAmount = itemTotalProfitAmount.add(itemNetAmount.subtract(cogsPrice));
                        }
                    }
                }
            }
        }

        DecimalFormat defaultScaleFormat = getDefaultScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());

        String totalQuontity = "";
        BigDecimal quontity = ZERO;
        if (invoiceData.getItems() != null) {
            for (NewInvoiceItem item : invoiceData.getItems()) {
                if (!invoiceData.isProjectBasedInvoice()) {
                    quontity = quontity.add(item.getQuantity() != null ? item.getQuantity() : ZERO);
                }
            }
        }
        if (quontity.compareTo(ZERO) != 0 && !invoiceData.isProjectBasedInvoice()) {
            totalQuontity = qtyNumberFormat.format(quontity);
        }

        String currencyTemplate = "";
        if (!customised) {
            String currencySym = (edsCurrency.getSymbol() != null && !edsCurrency.getSymbol().equals(edsCurrency.getName()) ? "(" + edsCurrency.getSymbol() + ") " : "");
            String currencyName = (edsCurrency.getName() != null ? edsCurrency.getName() : "");
            currencyTemplate = currencyName + " " + currencySym + " ";
        }
        String subTotal = currencyTemplate;
        String disAmount = currencyTemplate;
        String discountedSubtotal = currencyTemplate;
        String taxTotal = currencyTemplate;
        String totalInBase = currencyTemplate;
        String subtotalInBase = currencyTemplate;
        String taxTotalInBase = currencyTemplate;
        String shippingTotal = currencyTemplate;
        String shippingVat = currencyTemplate;
        String total = currencyTemplate;
        String billableExpenseTotal = currencyTemplate;
        String billableExpenseTaxTotal = currencyTemplate;
        String subTotal_word = currencyTemplate;
        String total_word = currencyTemplate;
        String total_in_base_word = currencyTemplate;
        String total_in_usd_word = currencyTemplate;
        String subTotal_word_all = currencyTemplate;
        String total_word_all = currencyTemplate;
        String total_arabic_word_all = currencyTemplate;
        String total_uzb_word_all = currencyTemplate;
        String total_uzb_word_all_lotin = currencyTemplate;
        String discount_total_word_all = currencyTemplate;
        String totalInAED = currencyTemplate;

        BigDecimal totalAmount = ZERO;
        BigDecimal totalAmountInBase = ZERO;
        BigDecimal totalAmountInUsd = ZERO;

        if (getPdfCodeName(null) != null
                && getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.PURCHASE_ORDER)
                && invoiceData.getType() != null
                && invoiceData.getType().equalsIgnoreCase(PAYABLE)
                && invoiceData.getTypeItem() != null
                && invoiceData.getTypeItem().isReverseChargeApplicable()
                && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ACCOUNTING_IS_REVERSE_CHARGE)) {

            if (invoiceData.getTotalInInvoiceCurrency() != null) {
                invoiceData.setTotalInInvoiceCurrency(invoiceData.getTotalInInvoiceCurrency().subtract(invoiceData.getTotalTaxesInInvoiceCurrency() == null ? (invoiceData.getTotalTaxes() == null ? BigDecimal.ZERO : invoiceData.getTotalTaxes()) : invoiceData.getTotalTaxesInInvoiceCurrency()));
            } else {
                invoiceData.setTotal(invoiceData.getTotal().subtract(invoiceData.getTotalTaxes() == null ? BigDecimal.ZERO : invoiceData.getTotalTaxes()).multiply(exchangeRate));
            }
        }
        totalInAED = totalInAED + priceScaleNumberFormat.format(totalAmountInAed);
        if (invoiceData.getTotalInInvoiceCurrency() != null) {
            totalAmount = invoiceData.getTotalInInvoiceCurrency();
            total = total + priceScaleNumberFormat.format(totalAmount);
        } else {
            totalAmount = invoiceData.getTotal().multiply(exchangeRate);
            total = total + (totalAmount.compareTo(ZERO) != 0 ? priceScaleNumberFormat.format(totalAmount) : "");
        }
        if (invoiceData.getTotal() != null) {
            totalInBase = totalInBase + priceScaleNumberFormat.format(invoiceData.getTotal());
            totalAmountInBase = invoiceData.getTotal();
            totalAmountInUsd = invoiceData.getTotal().multiply(exchangeRateInUsd);
        }
        subtotalInBase = subtotalInBase + priceScaleNumberFormat.format(invoiceData.getSubtotal().divide(exchangeRate, 5, RoundingMode.HALF_UP));
        NumberToWord numberToWordConverter, numberToWordConverterUz = null, numberToWordConverterUzLotin = null;
        if (edsUser.getCompany().getLocale() != null && "ru".equals(edsUser.getCompany().getLocale())) {
            numberToWordConverter = new NumberToWord_ru();
            numberToWordConverterUz = new NumberToWord_uz();
            numberToWordConverterUzLotin = new NumberToWord_uz_lotin();
        } else {
            numberToWordConverter = new NumberToWord_en();
        }

        NumberToWord numberToWordConverterArabic = null;
        if (isArabicCompany(edsUser)) {
            numberToWordConverterArabic = new NumberToWord_ar();
        }

        int scale = 2;
        if (fs != null && fs.getCalculationScale() != null) {
            scale = fs.getCalculationScale();
        }
        total_word = total_word + numberToWordConverter.toWord(totalAmount.abs());
        total_in_base_word = total_in_base_word + numberToWordConverter.convert(totalAmountInBase.abs().setScale(scale, RoundingMode.HALF_UP));
        total_in_usd_word = total_in_usd_word + numberToWordConverter.convert(totalAmountInUsd.abs().setScale(scale, RoundingMode.HALF_UP));
        subTotal_word = subTotal_word + numberToWordConverter.toWord(invoiceData.getSubtotal().abs());
        total_word_all = total_word_all + numberToWordConverter.convert(totalAmount.abs().setScale(scale, RoundingMode.HALF_UP));
        subTotal_word_all = subTotal_word_all + numberToWordConverter.convert(invoiceData.getSubtotal().abs().setScale(scale, RoundingMode.HALF_UP));
        subTotal = subTotal + (invoiceData.getSubtotal().compareTo(ZERO) != 0 ? priceScaleNumberFormat.format(invoiceData.getSubtotal()/*.setScale(2, BigDecimal.ROUND_HALF_UP)*/) : "");
        disAmount = disAmount + priceScaleNumberFormat.format(totalDiscountAmount);
        discountedSubtotal = discountedSubtotal + priceScaleNumberFormat.format(invoiceData.getSubtotal().subtract(totalDiscountAmount));
        discount_total_word_all = discount_total_word_all + escapeHtml(numberToWordConverter.convert(totalDiscountAmount.abs().setScale(scale, RoundingMode.HALF_UP)));

        //total uzbek word
        total_uzb_word_all = total_uzb_word_all + (numberToWordConverterUz != null ? numberToWordConverterUz.convert(totalAmount.abs().setScale(scale, RoundingMode.HALF_UP)) : "");
        total_uzb_word_all_lotin = total_uzb_word_all_lotin + (numberToWordConverterUzLotin != null ? numberToWordConverterUzLotin.convert(totalAmount.abs().setScale(scale, RoundingMode.HALF_UP)) : "");
        //total arabic word
        total_arabic_word_all = total_arabic_word_all + (numberToWordConverterArabic != null ? numberToWordConverterArabic.convert(totalAmount.abs().setScale(2, RoundingMode.HALF_UP)) : "");

        String creditNoteInvoiceTotal = "";
        String creditNoteBalance = "";
        String creditNoteInvoiceSubtotal = "";
        String creditNoteInvoiceRevisedSubtotal = "";
        if (invoiceData.isCreditNote()) {
            if (invoiceData.getCreditNoteInvoiceTotal() != null && totalAmount != null) {
                creditNoteInvoiceTotal = priceScaleNumberFormat.format(invoiceData.getCreditNoteInvoiceTotal());
                creditNoteBalance = priceScaleNumberFormat.format(invoiceData.getCreditNoteInvoiceTotal().subtract(totalAmount));
            }
            if (invoiceData.getCreditNoteInvoiceSubTotal() != null) {
                creditNoteInvoiceSubtotal = priceScaleNumberFormat.format(invoiceData.getCreditNoteInvoiceSubTotal());
            }
            if (invoiceData.getCreditNoteInvoiceSubTotal() != null && invoiceData.getSubtotal() != null) {
                creditNoteInvoiceRevisedSubtotal = priceScaleNumberFormat.format(invoiceData.getCreditNoteInvoiceSubTotal().subtract(invoiceData.getSubtotal()));
            }
        }

        BigDecimal totalTaxAmount = ZERO;
        BigDecimal eindSubtotal = ZERO;
        BigDecimal eindTotal = ZERO;
        BigDecimal btwMin = ZERO;
        BigDecimal btwMax = ZERO;
        BigDecimal btwTotal = ZERO;
        List<String[]> taxList = new LinkedList<>();
        List<String[]> groupTaxList = new LinkedList<>();
        if (invoiceData.getTotalTaxItems() != null && invoiceData.getTotalTaxItems().length > 0) {
            for (TotalTaxItem taxItem : invoiceData.getTotalTaxItems()) {
                if (taxItem.getTaxItem() != null) {
                    if (taxItem.getTaxItem().getTaxPercent().compareTo(new BigDecimal("21.00")) == 0) {
                        btwMax = btwMax.add(taxItem.getTaxAmount());
                    } else if (taxItem.getTaxItem().getTaxPercent().compareTo(new BigDecimal("6.00")) == 0) {
                        btwMin = btwMin.add(taxItem.getTaxAmount());
                    }
                }
                EdsVat edsTax = vatManager.get(taxItem.getTaxItem().getId());
                taxList.add(new String[]{edsTax.getTaxNameAndRateAsString(defaultScaleFormat),
                        (taxItem.getTaxAmount() != null ? currencyTemplate + priceScaleNumberFormat.format(taxItem.getTaxAmount()) : "")});
                totalTaxAmount = totalTaxAmount.add(taxItem.getTaxAmount());
            }
        } else {
            List<InnerVatClass> unicalVatList = new LinkedList<>();
            for (InnerVatClass inner : vatList) {
                Boolean contain = false;

                for (InnerVatClass vat : unicalVatList) {
                    if (vat.getVatID() != null && vat.getVatID().equals(inner.getVatID())) {
                        contain = true;
                        vat.setJoinedVatAmount(vat.getJoinedVatAmount().add(inner.getVatAmount()));
                    }
                }
                if (!contain) {
                    inner.setJoinedVatAmount(inner.getVatAmount());
                    unicalVatList.add(inner);
                }
            }

            for (InnerVatClass vat : unicalVatList) {
                if (vat != null && vat.getVat() != null && vat.getVat().getObjectID() != null) {
                    taxList.add(new String[]{(vat.getVat().getName() == null ? "" : (vat.getVat().getName() + "(" + defaultScaleFormat.format(vat.getVat().getTaxRate()) + "%)")),
                            (vat != null && vat.getJoinedVatAmount() != null ? currencyTemplate + priceScaleNumberFormat.format(vat.getJoinedVatAmount()) : "")});
                    totalTaxAmount = totalTaxAmount.add(vat.getJoinedVatAmount());
                }
            }
        }
        if (taxMap != null && taxMap.size() > 0) {
            for (InnerVatClass groupTaxItem : taxMap.values()) {
                groupTaxList.add(new String[]{(groupTaxItem.getVat().getName() == null ? "" : (groupTaxItem.getVat().getName() + "(" + defaultScaleFormat.format(groupTaxItem.getVat().getTaxRate()) + "%)")),
                        (groupTaxItem.getVatAmount() != null ? currencyTemplate + priceScaleNumberFormat.format(groupTaxItem.getVatAmount()) : "")});
            }
        }

        boolean hasExpense = false;
        if (invoiceData.getBillableExpenseAmount() != null) {
            billableExpenseTotal = billableExpenseTotal + priceScaleNumberFormat.format(invoiceData.getBillableExpenseAmount()/*.setScale(2, BigDecimal.ROUND_HALF_UP)*/);
            eindSubtotal = invoiceData.getBillableExpenseAmount().add(invoiceData.getSubtotal());

            if (BigDecimal.ZERO.compareTo(invoiceData.getBillableExpenseAmount()) < 0) {
                hasExpense = true;
            }
        }
        if (invoiceData.getBillableExpenseTaxAmount() != null) {
            billableExpenseTaxTotal = billableExpenseTaxTotal + priceScaleNumberFormat.format(invoiceData.getBillableExpenseTaxAmount());
        }

        if (invoiceData.getExpenses() != null) {
            for (int i = 0; i < invoiceData.getExpenses().size(); i++) {
                BillableExpenseItem item = invoiceData.getExpenses().get(i);
                if (item.getMarkupTax() != null) {
                    if (item.getMarkupTax().getTaxPercent().compareTo(new BigDecimal("21.00")) == 0) {
                        btwMax = btwMax.add(item.getMarkupTaxAmount());
                    } else if (item.getMarkupTax().getTaxPercent().compareTo(new BigDecimal("6.00")) == 0) {
                        btwMin = btwMin.add(item.getMarkupTaxAmount());
                    }
                }
            }
        }
        btwTotal = btwMin.add(btwMax);
        eindTotal = eindSubtotal.add(btwTotal);

        List<String[]> paymentList = new LinkedList<>();
        PaymentItem[] pItems = invoiceData.getPaymentItems();
        BigDecimal paymentTotal = ZERO;
        BigDecimal lastPayment = ZERO;
        boolean isCreditNote = invoiceData.isCreditNote();
        if (pItems != null && pItems.length > 0) {
            int paySize = pItems.length;
            int i = 0;
            for (PaymentItem pi : pItems) {
                paymentTotal = paymentTotal.add(pi.getAmount());
                if (pi.isInvoiceCreditNoteAllocation()) {
                    paymentList.add(new String[]{isCreditNote ? accountingLocalizer.localizeAccounting(PdfLocalizationName.lessCreditToInvoice) : accountingLocalizer.localizeAccounting(PdfLocalizationName.lessCreditNote), currencyTemplate + priceScaleNumberFormat.format(pi.getAmount())});
                } else {
                    SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());
                    String paymentDate = pi.getDate() != null ? shortDateFormat.format(pi.getDate().getNonConvertedDate()) : "";
                    paymentList.add(new String[]{(isCreditNote ? accountingLocalizer.localizeAccounting(PdfLocalizationName.lessCashRefund) : accountingLocalizer.localizeAccounting(PdfLocalizationName.lessPayment) + "\n" + paymentDate), currencyTemplate + priceScaleNumberFormat.format(pi.getAmount())});
                }
                ++i;
                if (paySize == i) {
                    lastPayment = pi.getAmount();
                }
            }
        }

        Map<String, Object> values = new HashMap<>();
        values.put("EXCHANGE_RATE", exchangerate);
        values.put("EXCHANGE_RATE_REVERSE", exchangeReverse);
        values.put("TOTAL_AMOUNT_AED", totalInAED);
        values.put("EXCHANGE_RATE_AED", exchangeRateAed);
        values.put(SUBTOTAL, subTotal);
        values.put(SUBTOTAL_IN_BASE, subtotalInBase);
        if (totalDiscountAmount != null && totalDiscountAmount.compareTo(ZERO) > 0) {
            values.put(DISCOUNT_TOTAL, disAmount);
        }
        values.put(DISCOUNTED_SUBTOTAL, discountedSubtotal);
        values.put(TAX_, taxList);
        if (isGroupTax && !customised) {
            values.put(GROUP_TAX_, groupTaxList);
        }
        values.put(TAX_TOTAL, taxTotal + priceScaleNumberFormat.format(totalTaxAmount));
        //musor customization
        values.put(BTW_MIN_TOTAL, priceScaleNumberFormat.format(btwMin));
        values.put(BTW_MAX_TOTAL, priceScaleNumberFormat.format(btwMax));
        values.put(BTW_TOTAL, priceScaleNumberFormat.format(btwTotal));
        values.put(EIND_SUBTOTAL, priceScaleNumberFormat.format(eindSubtotal));
        values.put(EINDTOTAL, priceScaleNumberFormat.format(eindTotal));

        if (totalTaxAmount.compareTo(ZERO) > 0) {
            values.put(TAX_TOTAL_IN_BASE, taxTotalInBase + priceScaleNumberFormat.format(totalTaxAmount.divide(exchangeRate, 5, RoundingMode.HALF_UP)));
        }
        values.put(HAS_BILL_EXP_TOTAL, String.valueOf(hasExpense));
        values.put(BILL_EXP_TOTAL, billableExpenseTotal);
        values.put(BILL_EXP_TAX_TOTAL, billableExpenseTaxTotal);
        if (invoiceData.getShippingMethodID() != null) {
            EdsShippingMethod method = shippingMethodManager.get(invoiceData.getShippingMethodID());
            BigDecimal shippingPrice = invoiceData.getShippingPrice();
            if (method != null && shippingPrice != null) {
                shippingTotal = shippingTotal + priceScaleNumberFormat.format(shippingPrice);
                values.put(SHIPPING_TOTAL, shippingTotal);
                values.put(SHIPPING_TOTAL_NAME, method.getName());
                if (method.getVat() != null && method.getVat().getTaxRateAsBigDecimal() != null) {
                    BigDecimal vat = shippingPrice.multiply(method.getVat().getTaxRateAsBigDecimal().divide(HUNDRED, 4, RoundingMode.HALF_UP));
                    shippingVat = shippingVat + priceScaleNumberFormat.format(vat);
                    values.put(SHIPPING_VAT, shippingVat);
                    values.put(SHIPPING_VAT_NAME, method.getVat().getTaxNameAndRateAsString(defaultScaleFormat));
                }
            }
        }
        values.put(TOTAL, total);
        values.put(TOTAL_IN_BASE, totalInBase);

        values.put(PAYMENT_, paymentList);
        if (paymentTotal.compareTo(lastPayment) == 0) {
            values.put(LAST_PAYMENT, priceScaleNumberFormat.format(totalAmount));
        } else {
            values.put(LAST_PAYMENT, priceScaleNumberFormat.format(paymentTotal.subtract(lastPayment)));
        }
        values.put(PAYMENT_TOTAL, currencyTemplate + priceScaleNumberFormat.format(paymentTotal));
        values.put(DUE_AMOUNT, currencyTemplate + priceScaleNumberFormat.format(totalAmount.subtract(paymentTotal)));
        String due_amount_word = "";
        String due_amount_word_arabic = "";
        due_amount_word = numberToWordConverter.convert(totalAmount.subtract(paymentTotal).abs().setScale(scale, RoundingMode.HALF_UP));
        due_amount_word_arabic = numberToWordConverterArabic != null ? numberToWordConverterArabic.convert(totalAmount.subtract(paymentTotal).abs().setScale(2, RoundingMode.HALF_UP)) : "";
        if (edsUser.getCompany().getLocale() != null && "ru".equals(edsUser.getCompany().getLocale())) {
            values.put(DUE_AMOUNT_WORD, due_amount_word);
            values.put(SUBTOTAL_WORD, subTotal_word);
            values.put(TOTAL_WORD, total_word);
            values.put(TOTAL_IN_BASE_WORD, total_in_base_word);
            values.put(TOTAL_IN_USD_WORD, total_in_usd_word);
            values.put(TOTAL_WORD_ALL, total_word_all);
            values.put(SUBTOTAL_WORD_ALL, subTotal_word_all);
            values.put(TOTAL_UZB_WORD_ALL, total_uzb_word_all);
            values.put(TOTAL_UZB_WORD_ALL_LOTIN, total_uzb_word_all_lotin);
            values.put(DISCOUNT_TOTAL_WORD_ALL, discount_total_word_all);
        } else {
            values.put(DUE_AMOUNT_WORD, WordUtils.capitalizeFully(due_amount_word));
            values.put(SUBTOTAL_WORD, WordUtils.capitalizeFully(subTotal_word));
            values.put(TOTAL_WORD, WordUtils.capitalizeFully(total_word));
            values.put(TOTAL_IN_BASE_WORD, WordUtils.capitalizeFully(total_in_base_word));
            values.put(TOTAL_IN_USD_WORD, WordUtils.capitalizeFully(total_in_usd_word));
            if (edsUser.getCompany().getObjectID().equals(58835)) {
                Long fraction = totalAmount.abs().setScale(0, RoundingMode.DOWN).longValue();
                Integer cents = totalAmount.abs().setScale(scale, RoundingMode.HALF_UP).remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();
                String fractionWord = WordUtils.capitalize(numberToWordConverter.toWord(fraction).replaceAll("\\d*/\\d*", ""));
                String centsWord = WordUtils.capitalize(numberToWordConverter.toWord(cents).replaceAll("\\d*/\\d*", ""));
                String totalInWords = fractionWord + (cents > 0 ? " and " + centsWord + " " + getCompanyCurrencyFrname(edsCurrency) : "");
                values.put(TOTAL_WORD_ALL, WordUtils.capitalizeFully(totalInWords));
            } else {
                values.put(TOTAL_WORD_ALL, WordUtils.capitalizeFully(total_word_all));
            }
            values.put(SUBTOTAL_WORD_ALL, WordUtils.capitalizeFully(subTotal_word_all));
            values.put(DISCOUNT_TOTAL_WORD_ALL, discount_total_word_all);
        }
        if (isArabicCompany(edsUser)) {
            values.put(TOTAL_ARABIC_WORD_ALL, total_arabic_word_all);
            values.put(DUE_AMOUNT_ARABIC_WORD, due_amount_word_arabic);
        }
        values.put(TOTAL_QUONTITY, totalQuontity != null ? totalQuontity : "");
        if (invoiceData.isCreditNote()) {
            if (!"".equals(creditNoteInvoiceTotal) && !"".equals(creditNoteBalance)) {
                values.put(CREDIT_NOTE_IN_TOTAL, creditNoteInvoiceTotal);
                values.put(CREDIT_NOTE_BALANCE, creditNoteBalance);
            }
            if (!"".equals(creditNoteInvoiceSubtotal)) {
                values.put(CREDIT_NOTE_INVOICE_SUB_TOTAL, creditNoteInvoiceSubtotal);
            }
            if (!"".equals(creditNoteInvoiceRevisedSubtotal)) {
                values.put(CREDIT_NOTE_INVOICE_REVISED_SUB_TOTAL, creditNoteInvoiceRevisedSubtotal);
            }
        }

        if (SALE_INVOICE.equals(getFromInvoice())) {
            if (invoiceData.getBillableExpenseAmount() != null) {
                itemTotalProfitAmount = itemTotalProfitAmount.add(invoiceData.getBillableExpenseAmount());
            }
            values.put(TOTAL_PROFIT_AMOUNT, defaultScaleFormat.format(itemTotalProfitAmount != null ? itemTotalProfitAmount : ZERO));
        }
        String unrecRevCode = genericSettingsManager.getValueByKey(GenericSettingsEnum.UNRECOGNIZED_REVENUE_CODE);

        if (unrecRevCode != null && !unrecRevCode.isEmpty()) {
            BigDecimal unrecRevTotal = invoiceManager.getInvoiceQuoteUnrecTotal(invoiceData.getID(), unrecRevCode);

            if (unrecRevTotal != null && unrecRevTotal.compareTo(BigDecimal.ZERO) > 0) {
                values.put(INVOICE_QUOTE_UNREC_REVENUE_TOTAL, priceScaleNumberFormat.format(unrecRevTotal));
            }
        }
        String total_overall_discount = invoiceData.getTotalDiscount().compareTo(ZERO) != 0 ?
                priceScaleNumberFormat.format(invoiceData.getTotalDiscount()) :
                priceScaleNumberFormat.format(BigDecimal.ZERO);
        values.put(TOTAL_OVERALL_DISCOUNT, total_overall_discount);
        if (invoiceData.isProgressInvoicing()) {
            BigDecimal invoicedAmount = invoiceData.getInvoicedAmount() != null ? invoiceData.getInvoicedAmount() : BigDecimal.ZERO;

            String invoicedAmountString = priceScaleNumberFormat.format(invoicedAmount);
            String remainingBalance = priceScaleNumberFormat.format(totalAmount.subtract(invoicedAmount));

            values.put(INVOICED_AMOUNT, invoicedAmountString);
            values.put(REMAINING_BALANCE, remainingBalance);
        }

        BigDecimal remainingAmount = totalAmount;
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PRE_PAYMENT_AMOUNT)) {

            List<EdsInvoicePayment> edsInvoicePaymentList = invoiceData.getID() != null ? invoicePaymentManager.getOderPrePaymentAmount(invoiceData.getID()) : null;
            List<String[]> customerPrepaymentList = new LinkedList<>();

            for (EdsInvoicePayment payment : edsInvoicePaymentList) {
                BigDecimal orderPrePaymentAmount = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
                remainingAmount = remainingAmount.subtract(orderPrePaymentAmount);

                EdsCompany company = edsUser.getCompany();
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                String paymentDateString = "";
                Date paymentDate = null;
                try {
                    paymentDate = payment.getPaymentDate() != null ? new Date((payment.getPaymentDate()).getTime()) : null;
                } catch (Exception e) {

                }

                if (paymentDate != null) {
                    if ((company.getLocale() != null && "ru".equals(company.getLocale())) || genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PRE_PAYMENT_AMOUNT)) {
                        Locale ruLocale = new Locale("ru", "RU");
                        SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                        paymentDateString = ruDateFormat.format(paymentDate);
                    } else {
                        paymentDateString = shortDateFormat.format(paymentDate);
                    }
                }
                customerPrepaymentList.add(new String[]{currencyTemplate + priceScaleNumberFormat.format(orderPrePaymentAmount) + "\n " + paymentDateString});
            }
            values.put("PRE_PAYMENT", customerPrepaymentList);
            values.put("REMAINING_AMOUNT", priceScaleNumberFormat.format(remainingAmount));
        }

        Long fraction = totalAmount.abs().setScale(0, RoundingMode.DOWN).longValue();
        Integer cents = totalAmount.abs().setScale(scale, RoundingMode.HALF_UP).remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        String fractionWord;
        String centsWord;
        String totalInWordsWithCent;
        NumberToWord numberToWord;
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(edsUser);
        if (userSettings != null && "ru".equals(userSettings.getInternationalization())) {
            numberToWord = new NumberToWord_ru();
            fractionWord = WordUtils.capitalize(numberToWord.toWord(fraction).replaceAll("\\d*/\\d*", ""));
            centsWord = WordUtils.capitalize(numberToWord.toWord(cents).replaceAll("\\d*/\\d*", ""));
            totalInWordsWithCent = fractionWord + (cents > 0 ? " и " + centsWord + " " + getCompanyCurrencyFrname(edsCurrency) : "");
        } else if (userSettings != null && "uz".equals(userSettings.getInternationalization())) {
            numberToWord = new NumberToWord_uz_lotin();
            fractionWord = WordUtils.capitalize(numberToWord.toWord(fraction).replaceAll("\\d*/\\d*", ""));
            centsWord = WordUtils.capitalize(numberToWord.toWord(cents).replaceAll("\\d*/\\d*", ""));
            totalInWordsWithCent = fractionWord + (cents > 0 ? " va " + centsWord + " " + getCompanyCurrencyFrname(edsCurrency) : "");
        } else {
            numberToWord = new NumberToWord_en();
            fractionWord = WordUtils.capitalize(numberToWord.toWord(fraction).replaceAll("\\d*/\\d*", ""));
            centsWord = WordUtils.capitalize(numberToWord.toWord(cents).replaceAll("\\d*/\\d*", ""));
            totalInWordsWithCent = fractionWord + (cents > 0 ? " and " + centsWord + " " + getCompanyCurrencyFrname(edsCurrency) : "");
        }
        values.put("TOTAL_WORD_ALL_WITH_CENT", totalInWordsWithCent);

        return values;
    }

    private String getCompanyCurrencyFrname(EdsCurrency edsCurrency) {
        return edsCurrency != null ? edsCurrency.getFrname() : "";
    }

    protected String getCurrencySymbol(EdsCurrency edsCurrency, boolean customised) {
        if (customised) {
            return (edsCurrency.getSymbol() != null ? edsCurrency.getSymbol() : "");
        } else {
            return (edsCurrency.getSymbol() != null ? "(" + edsCurrency.getSymbol() + ") " : "");
        }
    }

    protected String getCurrencyName(EdsCurrency edsCurrency) {
        return (edsCurrency.getName() != null ? edsCurrency.getName() : "");
    }

    protected String getTaxCalculationType(Integer taxCalcType) {
        return taxCalcType == null ? "" : (taxCalcType.equals(0) ?
                commonLocalizer.localize("noTax", "No Tax") :
                (taxCalcType.equals(1) ? commonLocalizer.localize("taxInclusive", "Tax Inclusive") :
                        commonLocalizer.localize("taxExclusive", "Tax Exclusive")));
    }

    protected String getExchangeRate(NewInvoice invoice) {
        BigDecimal exchangeRate = invoice.getExchageRate().compareTo(ZERO) != 0 ? invoice.getExchageRate() : new BigDecimal("1.00");
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        int scale = 5;
        if (fs != null && fs.getExchangeRateScale() != null) {
            scale = fs.getExchangeRateScale();
        }
        return exchangeRate.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    private String getExchangeRate(PaymentItem paymentItem) {
        if (paymentItem == null || paymentItem.getExchangeRate() == null) {
            return "";
        }
        BigDecimal exchangeRate = paymentItem.getExchangeRate().compareTo(ZERO) != 0 ? paymentItem.getExchangeRate() : new BigDecimal("1.00");
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        int scale = 5;
        if (fs != null && fs.getExchangeRateScale() != null) {
            scale = fs.getExchangeRateScale();
        }
        return exchangeRate.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    protected CustomisedITextTable getDueAmountTable(NewInvoice invoice, EdsUser edsUser) {
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoice.getPdfTemplateID());
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(REFERENCE_COLUMN, AMOUNT_COLUMN, DATE_COLUMN);
        for (NewInvoice inv : invoice.getSameProjectInvoices()) {
            table.addRow(inv.getInvoiceNumber(), priceScaleNumberFormat.format(inv.getDuePayments()), shortDateFormat.format(inv.getInvoiceDate().getNonConvertedDate()));
        }
        return table;
    }

    protected CustomisedITextTable getPrepaymentTable(NewInvoice invoice, EdsUser edsUser) {
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoice.getPdfTemplateID());
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumnOrder(REFERENCE_COLUMN, AMOUNT_COLUMN, DUE_AMOUNT_COLUMN, DATE_COLUMN, EXCHANGE_RATE);
        for (PaymentItem paymentItem : invoice.getProjectPrepayments()) {
            table.addRow(paymentItem.getReference(),
                    priceScaleNumberFormat.format(paymentItem.getAmount()),
                    priceScaleNumberFormat.format(paymentItem.getAmount().subtract(paymentItem.getAppliedPaymentAmount())),
                    shortDateFormat.format(paymentItem.getInvoiceDate()),
                    escapeHtml(getExchangeRate(paymentItem)));
        }
        return table;
    }

    /**
     * First Elment Google link and Secont Elemnt Google Image Url
     *
     * @param invoiceData
     * @return List<String>
     */
    protected List<String> getGoogleLinkAndImgUrl(NewInvoice invoiceData, EdsCurrency edsCurrency, Integer companyID, boolean customised) {

        String googlecheckoutId = invoiceCircularResolver.getInvoiceGoogleCheckoutMerchantId(companyID);

        String invoiceCurrencyName = edsCurrency.getName();
        ArrayList<String> suppurtedCurrencies = new ArrayList<>();
        suppurtedCurrencies.add("USD");
        boolean isValidGoogleLink = googlecheckoutId != null && !"".equals(googlecheckoutId) && suppurtedCurrencies.contains(invoiceCurrencyName);
        if (isValidGoogleLink) {
            StringBuilder linkBuffer = new StringBuilder(EdsContextParams.getFullHost()); // application host url
            linkBuffer.append(COMMON_URL);   //servlet
            linkBuffer.append("/pdfPaymentHandler"); //servlet method pattern
            linkBuffer.append("?paymentType=googleCheckout"); // servlet method identifier
            linkBuffer.append("&invoiceId="); // invoice id
            linkBuffer.append(EncryptionHelper.encryptURL(String.valueOf(invoiceData.getID()))); // invoice id
            linkBuffer.append("&accountDetail="); //account detail
            linkBuffer.append(EncryptionHelper.encryptURL(googlecheckoutId)); // account detail
            linkBuffer.append("&" + SESSION_ID_COOKIE + "="); //sessionid will used SessionFilter
            linkBuffer.append(ServerSecurityContext.getInstance().getSessionId());
            List<String> linkImg = new ArrayList<>();
            linkImg.add(customised ? escapeHtml(linkBuffer.toString()) : linkBuffer.toString());
            try {
                linkImg.add(customised ? escapeHtml(getGoogleImageUrl()) : getGoogleImageUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return linkImg;
        }
        return null;
    }

    protected List<String> getMasterCardPaymentURL(NewInvoice invoiceData, Integer companyID, boolean customised) {
        if (/*false && */invoiceData.getID() != null && invoiceCircularResolver.isMasterCardParametersValid(invoiceData.getID(), companyID)) {
            List<String> mastercardURLParameters = new ArrayList<>();

            String mastercardPaymentURL = EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/mastercardPaymentValidate?" +
                    "user_amount=" + EncryptionHelper.encryptURL(getDueAmount(invoiceData).toString()) +
                    "&user_cid=" + EncryptionHelper.encryptURL(companyID.toString()) +
                    "&user_key=" + EncryptionHelper.encryptURL(invoiceData.getID().toString()) +
                    "&user_type=" + EncryptionHelper.encryptURL(MastercardPaymentHandler.INVOICE);

            mastercardURLParameters.add(mastercardPaymentURL);
            try {
                mastercardURLParameters.add(customised ? escapeHtml(getMasterCardImageUrl()) : getMasterCardImageUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return mastercardURLParameters;
        }
        return null;
    }

    public BigDecimal getDueAmount(NewInvoice invoice) {
        if (invoice.getTotalInInvoiceCurrency() == null) {
            return BigDecimal.ZERO;
        }
        return invoice.getTotalInInvoiceCurrency()
                .subtract(invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount())
                .setScale(invoice.getCalcScale() != null ? invoice.getCalcScale() : ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
    }

    protected List<String> getElavonPaymentURL(NewInvoice invoiceData, Integer companyID, boolean customised) {
        if (invoiceData.getID() != null && invoiceCircularResolver.isElavonParametersValid(invoiceData.getID(), companyID)) {
            List<String> elavonURLParameters = new ArrayList<>();

            StringBuilder elavonPaymentURL = new StringBuilder();

            String requiredParameters = companyID.toString() + "_" + invoiceManager.getUser().getObjectID().toString() + "_" + invoiceData.getID().toString();

            elavonPaymentURL.append(EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/payWithElavon?cmd=gc");
            elavonPaymentURL.append("&rp=" + EncryptionHelper.encryptURL(requiredParameters));

            elavonURLParameters.add(elavonPaymentURL.toString());

            return elavonURLParameters;
        }
        return null;
    }

    /**
     * First Element list PayPall Link And Second Element list Image Url PayPall
     *
     * @return List<String>
     */
    protected List<String> getPayPallLinkAndImgUrl(NewInvoice invoice, Integer companyID, boolean customised, boolean isInvoice) {
        String link = "";
        if (isInvoice) {
            link = invoiceCircularResolver.getInvoicePaymentLink(invoice.getID(), invoice, companyID);
        } else {
            link = invoiceCircularResolver.getOrderPaymentLink(invoice.getID(), invoice, companyID);
        }

        if (link != null) {
            List<String> linkImg = new ArrayList<>();
            linkImg.add(customised ? escapeHtml(link) : link);
            try {
                linkImg.add(customised ? escapeHtml(getPayPalImageUrl()) : getPayPalImageUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return linkImg;
        }
        return null;
    }

    protected Map<String, String> getPayMeData(NewInvoice invoice) {
        HashMap<String, String> data = new HashMap<>();
        String paymentLink = invoiceCircularResolver.getPayMeInvoicePaymentLink(invoice);
        if (paymentLink != null) {
            data.put("paymentLink", paymentLink);
            data.put("qrCode", QRCodeGenerator.generate(paymentLink, 250, 250));
            data.put("image", "https://cdn.paycom.uz/documentation_assets/payme_03.svg");
        }
        return data;
    }

    protected HashMap<String, String> getStripeLinkAndImgUrl(NewInvoice invoice, Integer companyID, boolean customised, boolean isInvoice) {
        String link = "";
        if (isInvoice) {
            link = invoiceCircularResolver.getStripeInvoicePaymentLink(invoice.getID(), invoice, companyID);
        } else {
            link = invoiceCircularResolver.getStripeOrderPaymentLink(invoice.getID(), invoice, companyID, isInvoice);
        }
        // If link is valid, generate additional data
        if (StringUtils.isNotBlank(link)) {
            HashMap<String, String> response = new HashMap<>();
            response.put("imageStripe", "https://apps.kpi.com/mainStyles/images/stripe.png");
            response.put("qrCodeStripe", QRCodeGenerator.generate(link, 250, 250));
            response.put("paymentLinkStripe", customised ? escapeHtml(link) : link);
//          response.put("image", customised ? escapeHtml(getStripeImageUrl()) : getStripeImageUrl());
            return response;
        }
        return null;
    }

    protected Map<String, String> getClickData(NewInvoice invoice) {
        HashMap<String, String> data = new HashMap<>();
        String paymentLink = invoiceCircularResolver.getClickInvoicePaymentLink(invoice);
        if (paymentLink != null) {
            data.put("paymentLink", paymentLink);
            data.put("qrCode", QRCodeGenerator.generate(paymentLink, 250, 250));
            data.put("image", "https://docs.click.uz/wp-content/themes/click_help/assets/images/logo.png");
        }
        return data;
    }

    protected Map<String, String> getRevolutData(NewInvoice invoice) {
        HashMap<String, String> data = new HashMap<>();
        String paymentLink = invoiceCircularResolver.getRevolutInvoicePaymentLink(invoice);
        if (paymentLink != null) {
            data.put("paymentLink", paymentLink);
            data.put("qrCode", QRCodeGenerator.generate(paymentLink, 250, 250));
            data.put("image", "https://upload.wikimedia.org/wikipedia/commons/c/c9/Logo_Revolut.png");
        }
        return data;
    }

    protected String getGoogleImageUrl() throws IOException {
        return getRealPath(GOOGLE_CHECKOUT_LOGO_URL);
    }

    protected String getPayPalImageUrl() throws IOException {
        return getRealPath(PAYPAL_LOGO_URL);
    }

    protected String getMasterCardImageUrl() throws IOException {
        return getRealPath(MASTERCARD_LOGO_URL);
    }

    /**
     * Invoice Client Approvement Url
     *
     * @param invoice
     * @return String
     */
    protected String getInvoiceClientApproveUrl(NewInvoice invoice, Integer companyID, boolean customized) {
        StringBuilder url = new StringBuilder();

        url.append(EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/invoiceClientApproveNotification");
        url.append("?invoice_status=").append(invoice.getStatus());
        url.append("&custom=_").append(EncryptionHelper.encryptURL(companyID + "_" + ServerSecurityContext.getInstance().getDatabase() + "_" + invoice.getID()));

        return customized ? escapeHtml(url.toString()) : url.toString();
    }

    /**
     * Terms and Condation Table
     *
     * @param invoiceData
     * @return
     */
    protected ITextTableList getTermsConditionsTableData(NewInvoice invoiceData, String tableHeaderName) {
        if (invoiceData.getPaymentInstruction() != null && invoiceData.getPaymentInstruction().length() > 1) {
            ITextTableList termsConditions = new ITextTableList(1);
            termsConditions.addPdfTableHeader(tableHeaderName);
            termsConditions.addPdfTableRows(invoiceData.getPaymentInstruction());
            return termsConditions;
        }
        return null;
    }

    protected CustomisedITextTable getCustomTermsConditionsTableData(NewInvoice invoiceData, String tableHeaderName) {
        if (StringUtils.trimToNull(invoiceData.getPaymentInstruction()) != null) {
            CustomisedITextTable termsConditions = new CustomisedITextTable();
            termsConditions.addColumnOrder(COLUMN_VALUE);
            termsConditions.addHeaderColumns(tableHeaderName);
            String[] paymentInstructions;
            if (Utils.isRTL(invoiceData.getPaymentInstruction())) {
                paymentInstructions = invoiceData.getPaymentInstruction().split("\n");
            } else {
                paymentInstructions = invoiceData.getPaymentInstruction().split("\r\n");
            }
            if (paymentInstructions != null) {
                for (String paymentInstruction : paymentInstructions) {
                    termsConditions.addRow(escapeHtml(paymentInstruction));
                }
            }
            return termsConditions;
        }
        return null;
    }

    protected CustomisedITextTable getCustomClientOrSupplierTypeTable(EdsCrmAccount clientSupplier) {
        if (clientSupplier != null && !clientSupplier.getAccountTypes().isEmpty()) {
            CustomisedITextTable table = new CustomisedITextTable();
            table.addColumnOrder(COLUMN_NAME, TYPE);
            for (EdsReference type : clientSupplier.getAccountTypes()) {
                table.addRowWithCode(type.getCode() != null ? type.getCode() : "",
                        type.getName() != null ? type.getName() : "",
                        type.getCode() != null ? type.getCode() : "");

            }
            return table;
        }
        return null;
    }

    protected ITextTableList getIntroductionTableData(String introductionData, String tableHeaderName) {
        if (introductionData != null && !introductionData.isEmpty()) {
            ITextTableList introductions = new ITextTableList(1);
            introductions.addPdfTableHeader(tableHeaderName);
            introductions.addPdfTableRows(introductionData);
            return introductions;
        }
        return null;
    }

    protected CustomisedITextTable getCustomIntroductionTableData(String introductionData, String tableHeaderName) {
        if (introductionData != null && !introductionData.isEmpty()) {
            CustomisedITextTable introductions = new CustomisedITextTable();
            introductions.addColumnOrder(COLUMN_VALUE);
            introductions.addHeaderColumns(tableHeaderName);
            String[] introductionRows = introductionData.split("\r\n");
            if (introductionRows != null) {
                for (String introductionRow : introductionRows) {
                    introductions.addRow(escapeHtml(introductionRow));
                }
            }
            return introductions;
        }
        return null;
    }

    protected ITextTableList getBankTableData(EdsUser edsUser, NewInvoice invoiceData, EdsCrmAccount supplier) {
        if (getFromInvoice() != null && getFromInvoice().equals(PURCHASE_INVOICE)) {
            if (supplier != null) {
                ITextTableList bankAccount = new ITextTableList(2);
                if (isValid(supplier.getBankName())) {
                    bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.bankName) + " ", supplier.getBankName());
                }
                if (isValid(supplier.getBranch())) {
                    bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.branch) + " ", supplier.getBranch());
                }
                if (isValid(supplier.getBankAddress())) {
                    bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.bankAddress) + " ", supplier.getBankAddress());
                }
                return bankAccount;
            }
        } else {
            EdsBankAccount bAcc = null;
            EdsInvoicingSettings invInf = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
            if (invoiceData.getBankAccount() != null && invoiceData.getBankAccount().getId() != null) {
                bAcc = bankAccountManager.get(invoiceData.getBankAccount().getId());
            } else if (invInf != null && invInf.getBankAccountId() != null) {
                bAcc = bankAccountManager.get(invInf.getBankAccountId());
            }
            if (bAcc != null) {
                if (getFromInvoice() != null && (SALE_INVOICE.equals(getFromInvoice()) || SALE_QUOTE.equals(getFromInvoice()))) {
                    ITextTableList bankAccount = new ITextTableList(2);
                    if (isValid(bAcc.getAccount() != null ? bAcc.getAccount().getName() : "")) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bank), bAcc.getAccount().getName());
                    }
                    if (isValid(bAcc.getBankBranch())) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.branch), bAcc.getBankBranch());
                    }
                    if (isValid(bAcc.getPhoneNumber())) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.phone), bAcc.getPhoneNumber());
                    }

                    if (isValid(bAcc.getStreetAddress()) || isValid(bAcc.getCity()) || isValid(bAcc.getCountry() != null ? bAcc.getCountry().getName() : "") || isValid(bAcc.getPostCode())) {
                        String bankAddress = "";
                        if (bAcc.getStreetAddress() != null && !bAcc.getStreetAddress().equals("")) {
                            bankAddress = bankAddress + bAcc.getStreetAddress() + "\n";
                        }

                        bankAddress = bankAddress + (((bAcc.getCity() != null && !bAcc.getCity().equals("")) ? bAcc.getCity() + ", " : "") +
                                ((bAcc.getState() != null && bAcc.getState().getName() != null) ? bAcc.getState().getName() + " " : "") +
                                ((bAcc.getPostCode() != null && !bAcc.getPostCode().equals("")) ? bAcc.getPostCode() : "")) + "\n";

                        if (bAcc.getCountry() != null && bAcc.getCountry().getName() != null) {
                            bankAddress = bankAddress + bAcc.getCountry().getName();
                        }
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bankAddress), bankAddress);
                    }
                    return bankAccount;
                } else {
                    ITextTableList bankAccount = new ITextTableList(2);
                    if (isValid(bAcc.getAccount() != null ? bAcc.getAccount().getName() : "")) {
                        bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.bankName) + " ", bAcc.getAccount().getName());
                    }
                    if (isValid(bAcc.getBankBranch())) {
                        bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.branch) + " ", bAcc.getBankBranch());
                    }
                    if (isValid(bAcc.getPhoneNumber())) {
                        bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.phone), bAcc.getPhoneNumber());
                    }

                    if (isValid(bAcc.getStreetAddress()) || isValid(bAcc.getCity()) || isValid(bAcc.getCountry() != null ? bAcc.getCountry().getName() : "") || isValid(bAcc.getPostCode())) {
                        String bankAddress = "";
                        if (bAcc.getStreetAddress() != null && !bAcc.getStreetAddress().equals("")) {
                            bankAddress = bankAddress + bAcc.getStreetAddress() + "\n";
                        }

                        bankAddress = bankAddress + (((bAcc.getCity() != null && !bAcc.getCity().equals("")) ? bAcc.getCity() + ", " : "") +
                                ((bAcc.getState() != null && bAcc.getState().getName() != null) ? bAcc.getState().getName() + " " : "") +
                                ((bAcc.getPostCode() != null && !bAcc.getPostCode().equals("")) ? bAcc.getPostCode() : "")) + "\n";

                        if (bAcc.getCountry() != null && bAcc.getCountry().getName() != null) {
                            bankAddress = bankAddress + bAcc.getCountry().getName();
                        }
                        bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.bankAddress) + " ", bankAddress);
                    }
                    return bankAccount;
                }
            }
        }
        return null;
    }

    /**
     * @param edsUser
     * @param invoiceData
     * @param supplier
     * @return
     */
    protected ITextTableList getAccountTable(EdsUser edsUser, NewInvoice invoiceData, EdsCrmAccount supplier) {
        if (getFromInvoice() != null && getFromInvoice().equals(PURCHASE_INVOICE)) {
            if (supplier != null) {
                ITextTableList bankAccount = new ITextTableList(2);
                if (isValid(supplier.getAccountName())) {
                    bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.accountName) + " ", supplier.getAccountName());
                }
                if (isValid(supplier.getAccountNo())) {
                    bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.accountNo) + " ", supplier.getAccountNo());
                }
                if (isValid(supplier.getSwiftCode())) {
                    bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.swiftCode) + " ", supplier.getSwiftCode());
                }
                if (isValid(supplier.getIbanCode())) {
                    bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.ibanCode) + " ", supplier.getIbanCode());
                }
                return bankAccount;
            }
        } else {
            EdsBankAccount bAcc = null;
            EdsInvoicingSettings invInf = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
            if (invoiceData.getBankAccount() != null && invoiceData.getBankAccount().getId() != null) {
                bAcc = bankAccountManager.get(invoiceData.getBankAccount().getId());
            } else if (invInf != null && invInf.getBankAccountId() != null) {
                bAcc = bankAccountManager.get(invInf.getBankAccountId());
            }
            if (bAcc != null) {
                if (getFromInvoice() != null && (SALE_INVOICE.equals(getFromInvoice()) || SALE_QUOTE.equals(getFromInvoice()))) {
                    ITextTableList bankAccount = new ITextTableList(2);
                    if (isValid(bAcc.getAccauntName())) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountName), bAcc.getAccauntName());
                    }

                    if (isValid(bAcc.getAccountNumber())) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountNo), bAcc.getAccountNumber());
                    }

                    if (isValid(bAcc.getSwiftCode())) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.swiftBic), bAcc.getSwiftCode());
                    }

                    if (isValid(bAcc.getSortCode())) {
//                        if (edsUser.getCompany().getObjectID().equals(6506)) {
//                            //Company ID : 6506; Company Name: Purple Oranges Pty Ltd; E-Mail:troy@purpleoranges.com;
//                            //Sort code changed to BSB code for this company
//                            bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bsbcode), bAcc.getSortCode());
//                        } else

                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.sortCode), bAcc.getSortCode());

                    }

                    if (isValid(bAcc.getIbanCode())) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.ibanCode), bAcc.getIbanCode());
                    }

                    if (isValid(bAcc.getAbaCode())) {
                        bankAccount.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.abaCode), bAcc.getAbaCode());
                    }
                    return bankAccount;
                } else {
                    ITextTableList bankAccount = new ITextTableList(2);
                    if (isValid(bAcc.getAccauntName())) {
                        bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.accountName) + " ", bAcc.getAccauntName());
                    }

                    if (isValid(bAcc.getAccountNumber())) {
                        bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.accountNo) + " ", bAcc.getAccountNumber());
                    }

                    if (isValid(bAcc.getSwiftCode())) {
                        bankAccount.addPdfTableRows(commonLocalizer.localizeAccounting(PdfLocalizationName.swiftCode) + " ", bAcc.getSwiftCode());
                    }

                    if (isValid(bAcc.getSortCode())) {
//                        if (edsUser.getCompany().getObjectID().equals(6506)) {
//                            //Company ID : 6506; Company Name: Purple Oranges Pty Ltd; E-Mail:troy@purpleoranges.com;
//                            //Sort code changed to BSB code for this company
//                            bankAccount.addPdfTableRows(accountingLocalizer.localizeAccounting(PdfLocalizationName.bsbcode) + " ", bAcc.getSortCode());
//                        }
                        //    else {
                        bankAccount.addPdfTableRows(accountingLocalizer.localizeAccounting(PdfLocalizationName.sortCode) + " ", bAcc.getSortCode());
                        // }
                    }

                    if (isValid(bAcc.getIbanCode())) {
                        bankAccount.addPdfTableRows(accountingLocalizer.localizeAccounting(PdfLocalizationName.ibanCode) + " ", bAcc.getIbanCode());
                    }

                    if (isValid(bAcc.getAbaCode())) {
                        bankAccount.addPdfTableRows(accountingLocalizer.localizeAccounting(PdfLocalizationName.abaCode) + " ", bAcc.getAbaCode());
                    }
                    return bankAccount;
                }
            }
        }
        return null;
    }

    public CustomisedITextTable getCustomisedBankTableData(EdsUser edsUser, NewInvoice invoiceData, EdsCrmAccount supplier) {
        CustomisedITextTable bankAccount = new CustomisedITextTable();
        bankAccount.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        if (getFromInvoice() != null && (getFromInvoice().equals(PURCHASE_INVOICE) || getFromInvoice().equals(PURCHASE_ORDER))) {
            if (supplier != null) {
                //Bank Data
                if (isValid(supplier.getBankName())) {
                    bankAccount.addRowWithCode(BANK_NAME, commonLocalizer.localizeAccounting(PdfLocalizationName.bankName) + " ", escapeHtml(supplier.getBankName()), BANK_NAME);
                }
                if (isValid(supplier.getBranch())) {
                    bankAccount.addRowWithCode(BRANCH, commonLocalizer.localizeAccounting(PdfLocalizationName.branch) + " ", escapeHtml(supplier.getBranch()), BRANCH);
                }
                if (isValid(supplier.getBankAddress())) {
                    bankAccount.addRowWithCode(BILL_ADDRESS, commonLocalizer.localizeAccounting(PdfLocalizationName.bankAddress) + " ", escapeHtml(supplier.getBankAddress()), BILL_ADDRESS);
                }

                EdsInvoicingSettings invInf = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
                if (invInf != null && invInf.getBankAccountId() != null) {
                    EdsBankAccount bAcc = bankAccountManager.get(invInf.getBankAccountId());
                    if (bAcc != null) {
                        if (isValid(bAcc.getAccount() != null ? bAcc.getAccount().getName() : "")) {
                            bankAccount.addRowWithCode(EXT_BANK_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bank),
                                    escapeHtml(bAcc.getAccount().getName()), EXT_BANK_NAME);
                        }
                        if (isValid(bAcc.getBankBranch())) {
                            bankAccount.addRowWithCode(EXT_BRANCH, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.branch),
                                    escapeHtml(bAcc.getBankBranch()), EXT_BRANCH);
                        }
                        if (isValid(bAcc.getPhoneNumber())) {
                            bankAccount.addRowWithCode(EXT_PHONE_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.phone),
                                    escapeHtml(bAcc.getPhoneNumber()), EXT_PHONE_NUMBER);
                        }
                        bankAccount.setCustomFields(getBankCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(bAcc.getCustomFields(), commonService.getCompanyCustomFields(ViewName.BankAccounts))));
                    }
                }
                return bankAccount;
            }
        } else {
            EdsBankAccount bAcc = null;
            EdsInvoicingSettings invInf = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
            if (invoiceData.getBankAccount() != null && invoiceData.getBankAccount().getId() != null) {
                bAcc = bankAccountManager.get(invoiceData.getBankAccount().getId());
            } else if (invInf != null && invInf.getBankAccountId() != null) {
                bAcc = bankAccountManager.get(invInf.getBankAccountId());
            }
            if (bAcc != null) {
                //Bank Data
                if (isValid(bAcc.getAccount() != null ? bAcc.getAccount().getName() : "")) {
                    bankAccount.addRowWithCode(BANK_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bank),
                            escapeHtml(bAcc.getAccount().getName()), BANK_NAME);
                }
                if (isValid(bAcc.getBankBranch())) {
                    bankAccount.addRowWithCode(BRANCH, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.branch),
                            escapeHtml(bAcc.getBankBranch()), BRANCH);
                }
                if (isValid(bAcc.getStreetAddress()) || isValid(bAcc.getCity()) || isValid(bAcc.getCountry() != null ? bAcc.getCountry().getName() : "") || isValid(bAcc.getPostCode())) {
                    String bankAddress = "";
                    if (bAcc.getStreetAddress() != null && !bAcc.getStreetAddress().equals("")) {
                        bankAddress = bankAddress + bAcc.getStreetAddress() + "\n";

                        bankAccount.addRowWithCode(STREET_ADDRESS, accountingLocalizer.localizeAccounting(PdfLocalizationName.streetAddress),
                                escapeHtml(bAcc.getStreetAddress()), STREET_ADDRESS);
                    }

                    bankAddress = bankAddress + (((bAcc.getCity() != null && !bAcc.getCity().equals("")) ? bAcc.getCity() + ", " : "") +
                            ((bAcc.getState() != null && bAcc.getState().getName() != null) ? bAcc.getState().getName() + " " : "") +
                            ((bAcc.getPostCode() != null && !bAcc.getPostCode().equals("")) ? bAcc.getPostCode() : "")) + "\n";

                    if (bAcc.getCity() != null && !"".equals(bAcc.getCity())) {
                        bankAccount.addRowWithCode(BANK_ACCOUNT_CITY, accountingLocalizer.localizeAccounting(PdfLocalizationName.city),
                                escapeHtml(bAcc.getCity()), BANK_ACCOUNT_CITY);
                    }

                    if (bAcc.getState() != null && bAcc.getState().getName() != null && !"".equals(bAcc.getState().getName())) {
                        bankAccount.addRowWithCode(BANK_ACCOUNT_STATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.state),
                                escapeHtml(bAcc.getState().getName()), BANK_ACCOUNT_STATE);
                    }

                    if (bAcc.getPostCode() != null && !"".equals(bAcc.getPostCode())) {
                        bankAccount.addRowWithCode(BANK_ACCOUNT_POSTCODE, accountingLocalizer.localizeAccounting(PdfLocalizationName.postCode),
                                escapeHtml(bAcc.getPostCode()), BANK_ACCOUNT_POSTCODE);
                    }

                    if (bAcc.getCountry() != null && bAcc.getCountry().getName() != null) {
                        bankAddress = bankAddress + bAcc.getCountry().getName();

                        bankAccount.addRowWithCode(BANK_ACCOUNT_COUNTRY, accountingLocalizer.localizeAccounting(PdfLocalizationName.country),
                                escapeHtml(bAcc.getCountry().getName()), BANK_ACCOUNT_COUNTRY);
                    }
                    bankAccount.addRowWithCode(BILL_ADDRESS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bankAddress),
                            escapeHtml(bankAddress), BILL_ADDRESS);
                }
                if (isValid(bAcc.getPhoneNumber())) {
                    bankAccount.addRowWithCode(PHONE_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.phone),
                            escapeHtml(bAcc.getPhoneNumber()), PHONE_NUMBER);
                }
                boolean isMultiCurrency = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);
                if (isMultiCurrency && bAcc.getAccount() != null && bAcc.getAccount().getCurrency() != null) {
                    bankAccount.addRowWithCode(CURRENCY, accountingLocalizer.localizeAccounting(PdfLocalizationName.currency),
                            escapeHtml(bAcc.getAccount().getCurrency().getName()), CURRENCY);
                }

                bankAccount.setCustomFields(getBankCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(bAcc.getCustomFields(), commonService.getCompanyCustomFields(ViewName.BankAccounts))));
            }
        }
        return bankAccount;
    }

    public CustomisedITextTable getCustomisedAccountTableData(EdsUser edsUser, NewInvoice invoiceData, EdsCrmAccount supplier) {
        CustomisedITextTable bankAccount = new CustomisedITextTable();
        bankAccount.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        if (getFromInvoice() != null && (getFromInvoice().equals(PURCHASE_INVOICE) || getFromInvoice().equals(PURCHASE_ORDER))) {
            if (supplier != null) {
                //Account Data
                if (isValid(supplier.getAccountName())) {
                    bankAccount.addRowWithCode(ACCOUNT_NAME, commonLocalizer.localizeAccounting(PdfLocalizationName.accountName) + " ", escapeHtml(supplier.getAccountName()), ACCOUNT_NAME);
                }
                if (isValid(supplier.getAccountNo())) {
                    bankAccount.addRowWithCode(ACCOUNT_NUMBER, commonLocalizer.localizeAccounting(PdfLocalizationName.accountNo) + " ", escapeHtml(supplier.getAccountNo()), ACCOUNT_NUMBER);
                }
                if (isValid(supplier.getSwiftCode())) {
                    bankAccount.addRowWithCode(SWIFT_BIC, commonLocalizer.localizeAccounting(PdfLocalizationName.swiftCode) + " ", escapeHtml(supplier.getSwiftCode()), SWIFT_BIC);
                }
                if (isValid(supplier.getIbanCode())) {
                    bankAccount.addRowWithCode(IBAN_CODE, commonLocalizer.localizeAccounting(PdfLocalizationName.ibanCode) + " ", escapeHtml(supplier.getIbanCode()), IBAN_CODE);
                }
                if (isValid(supplier.getSortCode())) {
                    bankAccount.addRowWithCode(SORT_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.sortCode), escapeHtml(supplier.getSortCode()), SORT_CODE);
                }

                EdsInvoicingSettings invInf = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
                if (invInf != null && invInf.getBankAccountId() != null) {
                    EdsBankAccount bAcc = bankAccountManager.get(invInf.getBankAccountId());
                    if (bAcc != null) {
                        if (isValid(bAcc.getAccauntName())) {
                            bankAccount.addRowWithCode(EXT_ACCOUNT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountName),
                                    escapeHtml(bAcc.getAccauntName()), EXT_ACCOUNT_NAME);
                        }

                        if (isValid(bAcc.getAccountNumber())) {
                            bankAccount.addRowWithCode(EXT_ACCOUNT_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountNo),
                                    escapeHtml(bAcc.getAccountNumber()), EXT_ACCOUNT_NUMBER);
                        }

                        if (isValid(bAcc.getSwiftCode())) {
                            bankAccount.addRowWithCode(EXT_SWIFT_BIC, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.swiftBic),
                                    escapeHtml(bAcc.getSwiftCode()), EXT_SWIFT_BIC);
                        }

                        if (isValid(bAcc.getSortCode())) {
                            bankAccount.addRowWithCode(EXT_SORT_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.sortCode),
                                    escapeHtml(bAcc.getSortCode()), EXT_SORT_CODE);
                        }

                        if (isValid(bAcc.getIbanCode())) {
                            bankAccount.addRowWithCode(EXT_IBAN_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.ibanCode),
                                    escapeHtml(bAcc.getIbanCode()), EXT_IBAN_CODE);
                        }

                        if (isValid(bAcc.getAbaCode())) {
                            bankAccount.addRowWithCode(EXT_ABA_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.abaCode),
                                    escapeHtml(bAcc.getAbaCode()), EXT_ABA_CODE);
                        }
                    }
                }
                return bankAccount;
            }
        } else {
            EdsBankAccount bAcc = null;
            EdsInvoicingSettings invInf = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
            if (invoiceData.getBankAccount() != null && invoiceData.getBankAccount().getId() != null) {
                bAcc = bankAccountManager.get(invoiceData.getBankAccount().getId());
            } else if (invInf != null && invInf.getBankAccountId() != null) {
                bAcc = bankAccountManager.get(invInf.getBankAccountId());
            }
            if (bAcc != null) {
                //Account Data
                if (isValid(bAcc.getAccauntName())) {
                    bankAccount.addRowWithCode(ACCOUNT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountName),
                            escapeHtml(bAcc.getAccauntName()), ACCOUNT_NAME);
                }

                if (isValid(bAcc.getAccountNumber())) {
                    bankAccount.addRowWithCode(ACCOUNT_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountNo),
                            escapeHtml(bAcc.getAccountNumber()), ACCOUNT_NUMBER);
                }

                if (isValid(bAcc.getSwiftCode())) {
                    bankAccount.addRowWithCode(SWIFT_BIC, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.swiftBic),
                            escapeHtml(bAcc.getSwiftCode()), SWIFT_BIC);
                }

                if (isValid(bAcc.getSortCode())) {
                    bankAccount.addRowWithCode(SORT_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.sortCode),
                            escapeHtml(bAcc.getSortCode()), SORT_CODE);
                }

                if (isValid(bAcc.getIbanCode())) {
                    bankAccount.addRowWithCode(IBAN_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.ibanCode),
                            escapeHtml(bAcc.getIbanCode()), IBAN_CODE);
                }

                if (isValid(bAcc.getAbaCode())) {
                    bankAccount.addRowWithCode(ABA_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.abaCode),
                            escapeHtml(bAcc.getAbaCode()), ABA_CODE);
                }

                if (bAcc.getAccount() != null && isValid(bAcc.getAccount().getAccountCode())) {
                    bankAccount.addRowWithCode(ACCOUNT_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountCode),
                            escapeHtml(bAcc.getAccount().getAccountCode()), ACCOUNT_CODE);
                }
            }
        }
        return bankAccount;
    }

    protected ITextTableList getPOTableData(NewInvoice invoiceData, EdsCurrency edsCurrency) {
        EdsCrmContact requestioned = (invoiceData.getRequisitionedBy() != null && invoiceData.getRequisitionedBy().getId() != null) ?
                crmContactManager.get(invoiceData.getRequisitionedBy().getId()) : null;//po
        EdsPaymentMethod paymentMethod = invoiceData.getPaymentMethodID() != null ? paymentMethodManager.get(invoiceData.getPaymentMethodID()) : null;//PO
        String requestionedBy = requestioned != null ? requestioned.getName() : ""/*NOT_AVAILABLE*/;
        String paymentType = paymentMethod != null ? commonLocalizer.localize(paymentMethod.getCode(), paymentMethod.getName()) : ""/*commonLocalizer.localize(NOT_AVAILABLE, NOT_AVAILABLE)*/;
        String paymentTerms = invoiceData.getPaymentTerms() != null ? invoiceData.getPaymentTerms() : ""/*NOT_AVAILABLE*/;
        String shippingTerm = invoiceData.getShippingTerms() != null ? invoiceData.getShippingTerms() : ""/*NOT_AVAILABLE*/;
        String poCurrency = edsCurrency != null ? edsCurrency.getFullName() : ""/*NOT_AVAILABLE*/;

        List<String> header = new LinkedList<>();
        List<String> values = new LinkedList<>();
        if (!"".equals(requestionedBy.trim())) {
            header.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.requisitionedBy));
            values.add(requestionedBy);
        }
        header.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poCurrency));
        header.add(pdfWfmMessageSource.localize(PdfLocalizationName.paymentType));
        header.add(pdfWfmMessageSource.localize(PdfLocalizationName.paymentTerms));
        header.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shippingTerms));
        values.add(poCurrency);
        values.add(paymentType);
        values.add(paymentTerms);
        values.add(shippingTerm);

        ITextTableList poTableData = new ITextTableList(header.size());
        poTableData.addPdfTableHeader(header.toArray(new String[]{}));
        poTableData.addPdfTableRows(values.toArray(new String[]{}));
        return poTableData;
    }

    protected CustomisedITextTable getCustomPOTableData(NewInvoice invoiceData, EdsCurrency edsCurrency) {
        EdsCrmContact requestioned = (invoiceData.getRequisitionedBy() != null && invoiceData.getRequisitionedBy().getId() != null) ?
                crmContactManager.get(invoiceData.getRequisitionedBy().getId()) : null;//po
        EdsPaymentMethod paymentMethod = invoiceData.getPaymentMethodID() != null ? paymentMethodManager.get(invoiceData.getPaymentMethodID()) : null;//PO
        if (paymentMethod == null) {
            EdsQuote edsQuote = quoteManager.get(invoiceData.getID());
            if (edsQuote instanceof EdsPurchaseOrder) {
                EdsPurchaseOrder edsPurchaseOrder = (EdsPurchaseOrder) edsQuote;
                if (edsPurchaseOrder != null && edsPurchaseOrder.getPaymentMethod() != null) {
                    paymentMethod = edsPurchaseOrder.getPaymentMethod();
                }
            }
        }
        String requestionedBy = requestioned != null ? requestioned.getName() : NOT_AVAILABLE;
        String paymentType = paymentMethod != null ? commonLocalizer.localize(paymentMethod.getCode(), paymentMethod.getName()) : commonLocalizer.localize(NOT_AVAILABLE, NOT_AVAILABLE);
        String paymentTerms = invoiceData.getPaymentTerms() != null ? invoiceData.getPaymentTerms() : NOT_AVAILABLE;
        String shippingTerm = invoiceData.getShippingTerms() != null ? invoiceData.getShippingTerms() : NOT_AVAILABLE;
        String poCurrency = edsCurrency != null ? edsCurrency.getFullName() : NOT_AVAILABLE;
        String approver = "", approverPosition = "";
        CustomisedITextTable poTableData = new CustomisedITextTable();
        if (invoiceData.getID() != null) {
            EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(invoiceData.getID());
            if (purchaseOrder != null && purchaseOrder.getCurrentApprover() != null && purchaseOrder.getCurrentApprover().getExactEmployee() != null) {
                approver = escapeHtml(purchaseOrder.getCurrentApprover().getExactEmployee().getFullName());
                EdsEmployee emp = getEmployeeManager().get(purchaseOrder.getCurrentApprover().getExactEmployee().getObjectID());
                if (emp != null && emp.getPosition() != null) {
                    approverPosition = escapeHtml(emp.getPosition().getName());
                }
            }
        }
        poTableData.addColumnOrder(REQUISTIONED_BY, CURRENCY, PDFConstants.PO_PAYMENT_TYPE, PAYMENT_TERMS, SHIPPING_TERMS, PURCHASE_ORDER_APPROVER, PURCHASE_ORDER_APPROVER_POSITION);
        poTableData.addHeaderColumns(accountingLocalizer.localizeAccounting(PdfLocalizationName.requisitionedBy),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.currency),
                commonLocalizer.localize(PdfLocalizationName.paymentType),
                commonLocalizer.localize(PdfLocalizationName.paymentTerms),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingTerms), "");
        poTableData.addRow(escapeHtml(requestionedBy), escapeHtml(poCurrency), escapeHtml(paymentType), escapeHtml(paymentTerms), escapeHtml(shippingTerm), escapeHtml(approver), escapeHtml(approverPosition));
        return poTableData;
    }

    protected CustomisedITextTable getCustomApproverData(NewInvoice invoiceData) {
        CustomisedITextTable approverTable = new CustomisedITextTable();
        if (invoiceData.getID() == null) {
            return approverTable;
        }

        EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(invoiceData.getID());
        EdsQuote edsQuote = quoteManager.get(invoiceData.getID());
        List<EdsApprover> approvers = new ArrayList<>();
        if (purchaseOrder != null) {
            approvers = purchaseOrder.getApprovers();
        } else if (edsQuote != null) {
            approvers = edsQuote.getApprovers();
        }
        if (approvers != null && !approvers.isEmpty()) {
            approverTable.addColumnOrder(PDFConstants.APPROVERS,
                    PDFConstants.APPROVERS_DATES,
                    "QR",
                    "APPROVERS_DEPARTMENT_EN",
                    "APPROVERS_DEPARTMENT_RU",
                    "APPROVERS_DEPARTMENT_UZ",
                    "APPROVERS_DEPARTMENT",
                    "APPROVERS_POSITION_EN",
                    "APPROVERS_POSITION_RU",
                    "APPROVERS_POSITION_UZ",
                    "APPROVERS_POSITION",
                    "APPROVERS_PHONE",
                    "APPROVERS_EMAIL",
                    "STATUS");

            for (EdsApprover approver : approvers) {
                EdsUser approverExactEmployee = approver.getExactEmployee();
                String fullName = "";
                String approveDate = "";
                String departmentEn = "";
                String departmentRu = "";
                String departmentUz = "";
                String department = "";
                String primaryPhone = "";
                String email = "";
                String positionEn = "";
                String positionRu = "";
                String positionUz = "";
                String position = "";
                String status = approver.getStatus() != null ? escapeHtml(approver.getStatus().getCode()) : "";
                if (approverExactEmployee != null) {
                    EdsEmployee employee = approverExactEmployee.getEmployee();
                    fullName = escapeHtml(approverExactEmployee.getFullName());
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", commonLocalizer.initializeUserLocale());
                    approveDate = (approver.getApproverHistory() != null && !approver.getApproverHistory().isEmpty()) ? format.format(employee.getUserDate(approver.getApproverHistory().iterator().next().getApproveDate())) : "";
                    EdsDepartment edsDepartment = employee.getTeam();
                    department = edsDepartment != null ? edsDepartment.getName() : "";
                    if (edsDepartment != null && edsDepartment.getLocale() != null) {
                        departmentEn = edsDepartment.getLocale().getEnglish();
                        departmentRu = edsDepartment.getLocale().getRussian();
                        departmentUz = edsDepartment.getLocale().getUzbek();
                    }

                    EdsPosition edsPosition = employee.getPosition();
                    position = edsPosition != null ? edsPosition.getName() : "";
                    if (edsPosition != null && edsPosition.getLocale() != null) {
                        positionEn = edsPosition.getLocale().getEnglish();
                        positionRu = edsPosition.getLocale().getRussian();
                        positionUz = edsPosition.getLocale().getUzbek();
                    }
                    primaryPhone = employee.getPrimaryPhone() != null ? employee.getPrimaryPhone() : "";
                    email = employee.getEmail() != null ? employee.getEmail() : "";
                }

                StringBuilder data = new StringBuilder(fullName);
                if (!ServerUtils.isNullOrEmpty(primaryPhone)) {
                    data.append("\n" + primaryPhone);
                }
                if (!ServerUtils.isNullOrEmpty(email)) {
                    data.append("\n" + email);
                }
                data.append("\n" + "Automated By zeta.uz");

                approverTable.addRow(
                        fullName,
                        " " + approveDate,
                        QRCodeGenerator.generate(data.toString(), 200, 200),
                        departmentEn,
                        departmentRu,
                        departmentUz,
                        department,
                        positionEn,
                        positionRu,
                        positionUz,
                        position,
                        primaryPhone,
                        email,
                        status
                );
            }
        }

        return approverTable;
    }

    protected ITextUserData getCreatorData(NewInvoice invoiceData) {
        ITextUserData creatorData = new ITextUserData();
        if (invoiceData != null && invoiceData.getID() != null) {
            if (PdfReferenceCodeNameEnum.SALES_INVOICE.equals(getPdfCodeName(null)) ||
                    PdfReferenceCodeNameEnum.PURCHASE_INVOICE.equals(getPdfCodeName(null)) ||
                    PdfReferenceCodeNameEnum.PACKING_SLIP.equals(getPdfCodeName(null))) {
                EdsInvoice invoice = invoiceManager.get(invoiceData.getID());
                if (invoice != null && invoice.getCreator() != null) {
                    EdsUser creator = invoice.getCreator();
                    creatorData.setFullName(creator.getFullName().replace("&", "&amp;"));
                    if (creator.isEmployee()) {
                        EdsEmployee emp = getEmployeeManager().get(creator.getObjectID());
                        creatorData.setPhone(Utils.formatPhoneNumber((emp.getWorkPhoneFirst() != null && !emp.getWorkPhoneFirst().equals("")) ? escapeHtml(emp.getWorkPhoneFirst()) : ""));
                        creatorData.setEmail(creator.getEmail() != null && !creator.getEmail().equals("") ? escapeHtml(creator.getEmail()) : "");
                        if (emp.getProfile() != null && emp.getProfile().getContact() != null) {
                            EdsCrmContact contact = emp.getProfile().getContact();
                            String fax = EdsCrmContactItemParams.getFirstItemParamValue(contact.getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.WORK_FAX, EdsCrmContactItemParams.HOME_FAX);
                            creatorData.setFax(fax);
                        }
                    }
                }
            } else {
                EdsQuote quote = quoteManager.get(invoiceData.getID());
                if (quote != null && quote.getCreator() != null) {
                    EdsUser creator = quote.getCreator();
                    creatorData.setFullName(creator.getFullName().replace("&", "&amp;"));
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", commonLocalizer.initializeUserLocale());
                    creatorData.setCreationDate(format.format(creator.getUserDate(quote.getCreationDate())));
                    if (creator.isEmployee()) {
                        EdsEmployee emp = getEmployeeManager().get(creator.getObjectID());
                        String phone = Utils.formatPhoneNumber((emp.getWorkPhoneFirst() != null && !emp.getWorkPhoneFirst().equals("")) ? escapeHtml(emp.getWorkPhoneFirst()) : "");
                        String email = creator.getEmail() != null && !creator.getEmail().equals("") ? escapeHtml(creator.getEmail()) : "";
                        creatorData.setPhone(phone);
                        creatorData.setMobilePhone(Utils.formatPhoneNumber((emp.getMobilePhoneFirst() != null && !emp.getMobilePhoneFirst().equals("")) ? escapeHtml(emp.getMobilePhoneFirst()) : ""));
                        creatorData.setHomePhone(Utils.formatPhoneNumber((emp.getHomePhoneFirst() != null && !emp.getHomePhoneFirst().equals("")) ? escapeHtml(emp.getHomePhoneFirst()) : ""));
                        creatorData.setPosition(emp.getPosition() != null ? emp.getPosition().getName() : "");
                        creatorData.setEmail(email);
                        if (emp.getProfile() != null && emp.getProfile().getContact() != null) {
                            EdsCrmContact contact = emp.getProfile().getContact();
                            String fax = EdsCrmContactItemParams.getFirstItemParamValue(contact.getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.WORK_FAX, EdsCrmContactItemParams.HOME_FAX);
                            creatorData.setFax(fax);
                        }
                        if (emp.getEmployeeDepartment() != null && emp.getEmployeeDepartment().getTeam() != null) {
                            EdsDepartment edsDepartment = emp.getEmployeeDepartment().getTeam();
                            if (edsDepartment != null && edsDepartment.getLocale() != null) {
                                creatorData.setDepartmentEn(edsDepartment.getLocale().getEnglish());
                                creatorData.setDepartmentRu(edsDepartment.getLocale().getRussian());
                                creatorData.setDepartmentUz(edsDepartment.getLocale().getUzbek());
                            }
                        }

                        EdsPosition edsPosition = emp.getPosition();
                        if (edsPosition != null && edsPosition.getLocale() != null) {
                            creatorData.setPositionEn(edsPosition.getLocale().getEnglish());
                            creatorData.setPositionRu(edsPosition.getLocale().getRussian());
                            creatorData.setPositionUz(edsPosition.getLocale().getUzbek());
                        }

                        if (creator.getFullName() != null) {
                            StringBuilder data = new StringBuilder(creator.getFullName());
                            if (!ServerUtils.isNullOrEmpty(phone)) {
                                data.append("\n" + phone);
                            }
                            if (!ServerUtils.isNullOrEmpty(email)) {
                                data.append("\n" + email);
                            }
                            data.append("\n" + "Automated By zeta.uz");
                            creatorData.setCreatorNameQrCode(escapeHtml(QRCodeGenerator.generate(data.toString(), 200, 200)));
                        }
                    }
                }
            }
        }
        return creatorData;
    }

    /**
     * Abstract Method
     *
     * @param invoice
     * @param clientOrSupplier
     * @param clientContact
     * @return
     */
    public abstract <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact);

    /**
     * Footer top Text
     * return no null value
     */
    protected abstract String getFooterContactText();

    /**
     * Pdf File Name
     *
     * @return
     */
    public abstract String getFileName();

    protected abstract Map<String, String> getFileNameParams(Integer objectID);

    @Override
    protected PdfPTable getPageHeader(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {
        String companyName = edsCompany.getName();
        String address = edsCompany.getAddress1() != null ? edsCompany.getAddress1() : "";
        String city = edsCompany.getCity() != null ? edsCompany.getCity() : "";
        String postCode = (edsCompany.getPostCode() != null && !"".equals(edsCompany.getPostCode())) ? edsCompany.getPostCode() : "";
        String state = (edsCompany.getCountryRegion() != null) ? edsCompany.getCountryRegion().getName() : "";
        String country = (edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null) ? edsCompany.getCountryZone().getCountry().getName() : "";
        String cityPostCode = (!"".equals(city) && !"".equals(postCode) ? (city + "," + postCode) : (!"".equals(city) ? city : postCode));
        String compPhone = (edsCompany.getPhone() != null && edsCompany.getPhone().length() > 1 ? (commonLocalizer.localizeAccounting(PdfLocalizationName.phone) + " " + edsCompany.getPhone()) : "");
        String compFax = (edsCompany.getFaxNumber() != null && edsCompany.getFaxNumber().length() > 1 ? (commonLocalizer.localizeAccounting(PdfLocalizationName.fax) + " " + edsCompany.getFaxNumber()) : "");
        String compEmail = (edsCompany.getEmail() != null && edsCompany.getEmail().length() > 1 ? (commonLocalizer.localizeAccounting(PdfLocalizationName.email) + " " + edsCompany.getEmail()) : "");

        String default_font = fontName != null ? fontName : ITextFontTypeEnum.TIMES_NEW_ROMAN.getName();
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(edsCompany);
        PdfPTable header = new PdfPTable(2);
        header.getDefaultCell().setBorder(0);
        float width = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        header.setWidthPercentage(50);
        header.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        header.setTotalWidth(width);
        PdfPTable leftHeader = new PdfPTable(1);
        leftHeader.getDefaultCell().setBorder(0);
        leftHeader.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        leftHeader.setTotalWidth((document.getPageSize().getWidth() / 2) - document.leftMargin() - 10);

        Color textColor = null;
        String color = edsCompany.getCompanySettings().getPdfStyleColor();
        if (color != null && !"".equals(color) && color.length() == 6) {
            textColor = Utils.hexToRGB(color);
        } else {
            textColor = Utils.hexToRGB(DEFAULT_FONT_COLOR);
        }
        leftHeader.addCell(new Phrase(companyName, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, false, 15, Font.NORMAL, textColor)));
        leftHeader.addCell(new Phrase(address, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        leftHeader.addCell(new Phrase(cityPostCode, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        if (state != null && !"".equals(state)) {
            leftHeader.addCell(new Phrase(state, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        }
        if (country != null && !"".equals(country)) {
            leftHeader.addCell(new Phrase(country, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        }
        if (!"".equals(compPhone)) {
            leftHeader.addCell(new Phrase(compPhone, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        }
        if (!"".equals(compFax)) {
            leftHeader.addCell(new Phrase(compFax, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        }
        if (!"".equals(compEmail)) {
            leftHeader.addCell(new Phrase(compEmail, FontFactory.getFont(default_font, BaseFont.IDENTITY_H, 8)));
        }
        header.addCell(leftHeader);

        String imageUrl = null;
        try {
            imageUrl = getPdfLogoUrl(edsCompany, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PdfPTable rightTable = new PdfPTable(1);
        rightTable.getDefaultCell().setBorder(0);
        rightTable.setTotalWidth(width / 2);
        rightTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        rightTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        if (imageUrl != null) {
            try {
                Image image = Image.getInstance(imageUrl);

                EdsInvoicingSettings is = invoicingSettingsManager.getInvoiceSettings(edsCompany);
                if (is != null && is.getInvoiceLogoWidth() != null && is.getInvoiceLogoHeight() != null) {
                    //COMPANY_ID:9331 ----> The PMO Company width:245;height:74;
                    image.scaleAbsolute(is.getInvoiceLogoWidth(), is.getInvoiceLogoHeight());
                } else {
                    if (image.getWidth() > 240 && image.getHeight() > 60) {
                        float widthScale = image.getWidth() / 240;
                        float heightScale = image.getHeight() / 60;
                        if (widthScale > heightScale) {
                            image.scaleAbsoluteWidth(240);
                            image.scaleAbsoluteHeight(image.getHeight() / widthScale);
                        } else {
                            image.scaleAbsoluteHeight(60);
                            image.scaleAbsoluteWidth(image.getWidth() / heightScale);
                        }
                    } else if (image.getWidth() > 240) {
                        image.scaleAbsoluteWidth(240);
                        image.scaleAbsoluteHeight(image.getHeight() * 240 / image.getWidth());
                    } else if (image.getHeight() > 60) {
                        image.scaleAbsoluteHeight(image.getHeight());
                        image.scaleAbsoluteWidth(image.getWidth() * 60 / image.getHeight());
                    } else {
                        image.scaleAbsolute((int) (image.getWidth() * 0.8), (int) (image.getHeight() * 0.8));
                    }
                }
                Chunk a = new Chunk(image, 0, 0);
                PdfPCell cell = new PdfPCell(new Phrase(a));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingRight(30);
                cell.setBorderWidth(0);
                rightTable.addCell(cell);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        header.addCell(rightTable);

        PdfPTable headerWithUnderLine = new PdfPTable(1);
        headerWithUnderLine.setTotalWidth(width);
        headerWithUnderLine.getDefaultCell().setBorder(Rectangle.BOTTOM);
        headerWithUnderLine.setSpacingAfter(0);
        headerWithUnderLine.addCell(header);

        return headerWithUnderLine;
    }

    @Override
    protected PdfPTable getPageFooter(Object object, EdsCompany edsCompany, PdfWriter pdfWriter, Document document, String fontName) throws DocumentException {
        PdfPTable footer = new PdfPTable(1);
        footer.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        footer.getDefaultCell().setPadding(3);
        footer.getDefaultCell().setBorder(0);
        float width = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        footer.setTotalWidth(width);
        footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        EdsUser edsUser;
        if (getUserId(object) != null) {
            edsUser = userManager.get(getUserId(object));
        } else {
            edsUser = userManager.getUser();
        }

        String default_font = fontName != null ? fontName : ITextFontTypeEnum.TIMES_NEW_ROMAN.getName();

        boolean isSpanish = edsUser.getCompany().getLocale() != null && "ES".equals(edsUser.getCompany().getLocale());
        boolean isCesarCompanyWithSpanishLocal = isSpanish && edsUser.getCompany().getObjectID().equals(4847);
        if (!edsUser.getCompany().getObjectID().equals(4847) || (getFromInvoice() != null && !getFromInvoice().equals(SALE_QUOTE))) {
            if (edsUser.getCompany().getObjectID().equals(9600) &&  /*Evento Solutions. Company ID : 9600*/
                    (getFromInvoice() != null && (getFromInvoice().equals(SALE_INVOICE) || getFromInvoice().equals(PURCHASE_ORDER)))) {
                document.setMargins(20, 20, 150, 170);
                PdfPTable signatureTable = new PdfPTable(2);
                signatureTable.getDefaultCell().setBorder(0);
                signatureTable.getDefaultCell().setPaddingTop(15);
                signatureTable.getDefaultCell().setPaddingBottom(15);
                signatureTable.getDefaultCell().setPaddingLeft(40);
                signatureTable.getDefaultCell().setPaddingRight(40);
                PdfPCell cell = new PdfPCell();
                cell.addElement(new Phrase("For eVento", FontFactory.getFont(BaseFont.TIMES_ROMAN, 9, Font.BOLD)));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                signatureTable.addCell(cell);
                signatureTable.addCell(new Phrase("", FontFactory.getFont(BaseFont.TIMES_ROMAN, 9)));
                signatureTable.addCell(new Phrase("Authorized Signatory", FontFactory.getFont(BaseFont.TIMES_ROMAN, 9)));
                signatureTable.addCell(new Phrase("Receiver's Signature", FontFactory.getFont(BaseFont.TIMES_ROMAN, 9)));
                footer.addCell(signatureTable);

            }
            if (!SALES_RECEIPT.equals(getFromInvoice())) {
                if (isShownEmployeeFooter) {
                    Font font8 = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8);
                    footer.addCell(new Phrase(getFooterContactText(), font8));
                    footer.addCell(new Phrase(getContactText(edsUser), font8));
                    if (!(PURCHASE_ORDER.equals(getFromInvoice()) || PURCHASE_INVOICE.equals(getFromInvoice()))) {
                        footer.addCell(new Phrase(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.thankYuoForYourBusiness), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 10, Font.NORMAL)));
                    }
                }
            } else {
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
            }
        } else {
            if (isCesarCompanyWithSpanishLocal) {
                footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                if (!SALES_RECEIPT.equals(getFromInvoice())) {
                    footer.addCell(new Phrase(pdfWfmMessageSource.localizeAccounting("andradeFooterMessage"), FontFactory.getFont(BaseFont.TIMES_ROMAN, 8)));
                }
                footer.addCell(" ");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
            } else {
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
                footer.addCell("");
            }
        }

        PdfPTable table = super.getPageFooter(object, edsCompany, pdfWriter, document, default_font);
        PdfPCell cell = new PdfPCell(table);
        cell.setBorder(0);
        cell.setPaddingRight(width - 250);
        cell.setPaddingTop(20);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        footer.addCell(cell);
        footer.setSpacingAfter(40);

        return footer;
    }

    @Override
    public void initFooterParams(EdsCompany edsCompany) {
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(edsCompany);
        if (invoicingSettings != null) {
            if (invoicingSettings.isShowPDFPoweredBy() != null) {
                setShownWFTFooter(invoicingSettings.isShowPDFPoweredBy());
            }
            if (invoicingSettings.isShownEmployeeFooter() != null) {
                setShownEmployeeFooter(invoicingSettings.isShownEmployeeFooter());
            }
            if (invoicingSettings.isShowPDFPaging() != null) {
                setShownPaging(invoicingSettings.isShowPDFPaging());
            }
        }
    }

    @Override
    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        audingPdfFooterSignature(pdfReader, pdfStamper, document);
        super.initPagingAndStamper(pdfReader, pdfStamper, document, iTextPdfTemplateEvent, dataClass);
        EdsCompany company;
        if (dataClass instanceof InvoiceQuoteRequestObject && ((InvoiceQuoteRequestObject) dataClass).getUserID() != null) {
            company = userManager.get(((InvoiceQuoteRequestObject) dataClass).getUserID()).getCompany();
        } else {
            company = userManager.getUser().getCompany();
        }
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());
        Boolean enableStamper = companySystemSettings.getEnablePdfStamper() != null ? companySystemSettings.getEnablePdfStamper() : true;
        if (enableStamper) {
            NewInvoice newInvoice;
            if (getPdfCodeName(null) != null) {
                if (dataClass instanceof NewInvoice) {
                    newInvoice = (NewInvoice) dataClass;
                    if (newInvoice.getID() != null) {
                        if (getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.PURCHASE_ORDER)
                                || getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.SALES_QUOTE)
                                || getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.SALES_ORDER)
                        ) {
                            EdsQuote quote = quoteManager.get(newInvoice.getID());
                            newInvoice.setStatusCode(quote.getStatus().getCode());
                        } else {
                            EdsInvoice invoice = invoiceManager.get(newInvoice.getID());
                            newInvoice.setStatusCode(invoice.getStatus().getCode());
                        }
                    }
                } else {
                    if (getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.PURCHASE_ORDER)
                            || getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.SALES_QUOTE)
                            || getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.SALES_ORDER)
                            || getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.SO_PACKING_SLIP)) {
                        RequestObject requestObject = (RequestObject) dataClass;
                        EdsQuote quote = quoteManager.get(requestObject.getObjectID());
                        newInvoice = EdsQuote.getQuoteData(quote);
                    } else {
                        RequestObject requestObject = (RequestObject) dataClass;
                        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
                        newInvoice = EdsInvoice.getInvoiceData(invoice);
                    }
                }
                Image image = null;
                if (((getPdfCodeName(null).equals(PdfReferenceCodeNameEnum.PURCHASE_ORDER) && newInvoice.getStatusCode().equals(CONVERTED))) ||
                        (newInvoice.getStatusCode().equals(RECEIVED) || newInvoice.getStatusCode().equals(OVER_DUE) || newInvoice.getStatusCode().equals(PAID) ||
                                (getFromInvoice() != null && getFromInvoice().equals(SALES_RECEIPT) && (newInvoice.getStatusCode().equals(APPROVED) || newInvoice.getStatusCode().equals(OPEN) || newInvoice.getStatusCode().equals(OVER_DUE))))) {
                    try {
                        if (getFromInvoice() != null && getFromInvoice().equals(SALES_RECEIPT) &&
                                newInvoice.getPaymentItems() != null && newInvoice.getPaymentItems().length > 0 &&
                                (APPROVE.equals(newInvoice.getStatusCode()) || OPEN.equals(newInvoice.getStatusCode()) || OVER_DUE.equals(newInvoice.getStatusCode()))) {
                            image = Image.getInstance(getRealPath(PARTIALLY_PAID_STAMP_URL));
                        } else if (newInvoice.getStatusCode().equals(APPROVE)) {
                            image = Image.getInstance(getRealPath(APPROVED_STAMP_URL));
                        } else if (newInvoice.getStatusCode().equals(RECEIVED)) {
                            image = Image.getInstance(getRealPath(RECEIVED_STAMP_URL));
                        } else if (newInvoice.getStatusCode().equals(OVER_DUE)) {
                            image = Image.getInstance(getRealPath(OVERDUE_STAMP_URL));
                        } else if (newInvoice.getStatusCode().equals(CONVERTED)) {
                            image = Image.getInstance(getRealPath(RECEIVED_STAMP_URL));
                        } else {
                            image = Image.getInstance(getRealPath(PAID_STAMP_URL));
                        }
                    } catch (IOException exp) {

                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    EdsInvoicingSettings is = invoicingSettingsManager.getInvoiceSettings(company);
                    if (is.getInvoiceStampWidth() != null && is.getInvoiceStampHeight() != null) {
                        image.scaleAbsolute(is.getInvoiceStampWidth(), is.getInvoiceStampHeight());
                    } else {
                        image.scaleAbsolute((int) (image.getWidth() * 0.9), (int) (image.getHeight() * 0.9));
                    }
                    image.setAbsolutePosition(200, 400);
                    image.setRotation((float) Math.PI / 6);
                    int n = pdfReader.getNumberOfPages();
                    PdfContentByte content;
                    for (int i = 1; i <= n; i++) {
                        content = pdfStamper.getOverContent(i);
                        content.addImage(image);
                    }
                }
            }
        }
    }

    @Override
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        return new Document(PageSize.A4, 20, 20, 160, 130);
    }

    @Override
    protected Integer getUserId(Object object) {
        if (object instanceof InvoiceQuoteRequestObject) {
            return ((InvoiceQuoteRequestObject) object).getUserID();
        }
        return null;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof NewInvoice) {
            return ((NewInvoice) object).getPdfTemplateID();
        } else if (object instanceof InvoiceQuoteRequestObject) {
            return ((InvoiceQuoteRequestObject) object).getTemplateID();
        }
        return null;
    }

    protected String getContactText(EdsUser edsUser) {
        String infName = edsUser.getFullName() != null ? edsUser.getFullName() : "";
        String infPhone = "", infEmail = "";
        if (edsUser.isEmployee()) {
            EdsEmployee emp = employeeManager.get(edsUser.getObjectID());
            infPhone = Utils.formatPhoneNumber((emp.getPrimaryPhone() != null && !emp.getPrimaryPhone().equals("")) ? emp.getPrimaryPhone() : "");
            infEmail = (edsUser.getEmail() != null && !edsUser.getEmail().equals("") ? edsUser.getEmail() : "");
        }
        String contact = "";
        if (!"".equals(infName.trim())) {
            contact = contact + infName;
        }
        if (!"".equals(infPhone.trim())) {
            if (!"".equals(contact.trim())) {
                contact = contact + ", ";
            }
            contact = contact + infPhone;
        }
        if (!"".equals(infEmail.trim())) {
            if (!"".equals(contact.trim())) {
                contact = contact + " at ";
            }
            contact = contact + infEmail;
        }
        return contact;
    }

    protected ITextTableList getFooterData(EdsUser edsUser) {
        ITextTableList footerTable = new ITextTableList(1);
        String footerContext = getFooterContactText();
        footerTable.addPdfTableRows(footerContext);
        footerTable.addPdfTableRows(getContactText(edsUser));
        footerTable.addPdfTableRows(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.thankYuoForYourBusiness));
        return footerTable;
    }

    protected CustomisedITextTable getCustomFooterData(EdsUser edsUser) {
        CustomisedITextTable footerTable = new CustomisedITextTable();
        if (isShownEmployeeFooter) {
            footerTable.addColumnOrder(COLUMN_VALUE);
            String footerContext = getFooterContactText();
            footerContext = escapeHtml(footerContext).replaceAll("\n\n", "<br/>");
            footerTable.addRow(footerContext);
            footerTable.addRow(escapeHtml(getContactText(edsUser)));
            footerTable.addRow(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.thankYuoForYourBusiness));
        }
        return footerTable;
    }

    protected List<String> getInvoiceQuoteNotes(HistoryListItem[] items) {
        if (items != null && items.length > 0) {
            List<String> notes = new LinkedList<>();
            for (HistoryListItem item : items) {
                String title = item.getEmployee() == null ? "" : (item.getEmployee() + " on ");
                title = title + (item.getEventDate() != null ? dateFormat(item.getEventDate()) : "");
                notes.add(!"".equals(title) ? escapeHtml(title + " : " + item.getComment()) :
                        escapeHtml(item.getComment()));
            }
            return notes;
        }
        return null;
    }

    protected void setDefaultFontToTable(EdsCompany company, ITextTableList... tables) {
        String fontName = getDefaultFont(company);
        if (tables != null) {
            for (ITextTableList table : tables) {
                if (table != null) {
                    table.setFontName(fontName);
                }
            }
        }
    }

    protected CustomisedITextTable getCustomProductAssemblyItemsTable(List<AssemblyItem> assemblyItems, EdsCompany company) {
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(company, null);
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(company, null);
        CustomisedITextTable itemTable = new CustomisedITextTable();

        itemTable.addColumnOrder(ITEM_PRODUCT_NAME, "PRODUCT_TYPE", "ITEM_IN_STOCK", ITEM_DESCRIPTION, "QTY", "COST_PRICE");
        itemTable.addHeaderColumns("Product Name", "Product Type", "Item In Stock", "Description", "Quantity", "Cost Price");

        if (assemblyItems != null && !assemblyItems.isEmpty()) {
            for (AssemblyItem item : assemblyItems) {
                String productName = item.getProduct() != null ? item.getProduct().getName() : "";
                String productType = EdsItem.getProductTypeAsStr(item.getProductType());
                String itemInStock = item.getItemsInStock() != null ? qtyNumberFormat.format(item.getItemsInStock()) : "";
                String description = item.getDescription();
                String qty = qtyNumberFormat.format(item.getQuantity());
                String costPrice = priceScaleFormat.format(item.getCostPrice());
                itemTable.addRow(productName, productType, itemInStock, description, qty, costPrice);
            }
        }

        return itemTable;
    }

    protected CustomisedITextTable getCustomProductKitItemsTable(List<ProductKitItem> productKitItems, EdsCompany company) {
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(company, null);
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(company, null);

        CustomisedITextTable itemTable = new CustomisedITextTable();
        itemTable.addColumnOrder(ITEM_PRODUCT_NAME, "QTY", "PRICE", "COST", "TAX", "SUB_TOTAL");
        itemTable.addHeaderColumns("Product Name", "Quantity", "Price", "Cost", "Tax", "Sub Total");

        if (productKitItems != null && !productKitItems.isEmpty()) {
            for (ProductKitItem item : productKitItems) {
                String productName = item.getProductItem() != null ? item.getProductItem().getName() : "";
                String qty = qtyNumberFormat.format(item.getQuantity());
                itemTable.addRow(productName, qty, item.getPrice(), item.getCost(), item.getTax(), item.getSubtotal());
            }
        }

        return itemTable;
    }

    public List<CustomisedITextTable> getCustomFieldTables(List<CompanyCustomFieldItem> customFieldItems) {
        List<CustomisedITextTable> result = new ArrayList<>();

        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            for (CompanyCustomFieldItem fieldsItem : customFieldItems) {
                if (UI_TYPE_ENTITY_DROPDOWN.equals(fieldsItem.getUiType()) || TYPE_ENTITY_LOOKUP.equals(fieldsItem.getUiType())
                        && fieldsItem.getEntityType() != null && fieldsItem.getEntityType().getId() != null && fieldsItem.getSelectedId() != null) {
                    EdsEntityType et = entityTypeManager.get(fieldsItem.getEntityType().getId());
                    EdsEmployee employee = employeeManager.get(fieldsItem.getSelectedId());
                    if (et != null && LookUpConstants.EMPLOYEE.equals(et.getCode()) && employee != null) {
                        CustomisedITextTable table = new CustomisedITextTable();
                        table.addColumnOrder(COLUMN_VALUE);
                        table.addRowWithCode(NAME, escapeHtml(employee.getFullName()));
                        table.addRowWithCode(EMAIL, escapeHtml(employee.getEmail() != null ? employee.getEmail() : ""));
                        table.addRowWithCode(PHONE, escapeHtml(employee.getPrimaryPhone() != null ? employee.getPrimaryPhone() : ""));
                        result.add(table);
                    }
                }
            }
        }
        return result;
    }

    public CustomisedITextTable getCustomClientSupplierEntityCustomFieldTable(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null || customFieldItems.isEmpty()) {
            return null;
        }
        for (CompanyCustomFieldItem fieldsItem : customFieldItems) {
            if ((UI_TYPE_ENTITY_DROPDOWN.equals(fieldsItem.getUiType())
                    || TYPE_ENTITY_LOOKUP.equals(fieldsItem.getUiType())
                    || UI_TYPE_LOOKUP.equals(fieldsItem.getUiType()))
                    && fieldsItem.getFieldStringValue() != null && !"".equals(fieldsItem.getFieldStringValue())) {
                try {
                    EdsCrmAccount clientSupplier = null;
                    if (fieldsItem.getLookUpTypeEnum() != null && (CustomFieldLookUpTypeEnum.CUSTOMER.equals(fieldsItem.getLookUpTypeEnum())
                            || CustomFieldLookUpTypeEnum.SUPPLIER.equals(fieldsItem.getLookUpTypeEnum()))) {
                        clientSupplier = crmAccountManager.getCrmAccountByName(fieldsItem.getFieldStringValue());
                    }
                    if (clientSupplier == null) {
                        continue;
                    }
                    CustomisedITextTable tableData = new CustomisedITextTable();
                    tableData.addColumnOrder(COLUMN_VALUE);
                    tableData.addRowWithCode(PDFConstants.CLIENT, escapeHtml(clientSupplier.getName()));
                    if (clientSupplier.getParent() != null) {
                        tableData.addRowWithCode(PDFConstants.PARENT_ACCOUNT, escapeHtml(clientSupplier.getParent().getName()));
                    }
                    tableData.addRowWithCode(CLIENT_EMAIL, escapeHtml(clientSupplier.getEmail()));
                    tableData.addRowWithCode(CLIENT_PHONE, escapeHtml(clientSupplier.getPhone()));
                    EdsAddress billAddress = clientSupplier.getBillingAddress();
                    if (billAddress != null) {
                        tableData.addRowWithCode(BILL_ADDRESS_NAME, billAddress.getName() != null ? billAddress.getName() : "");
                        tableData.addRowWithCode(BILL_ADDRESS, billAddress.getAddress() != null ? billAddress.getAddress() : "");
                        tableData.addRowWithCode(BILL_ADDRESS2, billAddress.getAddressb() != null ? billAddress.getAddressb() : "");
                        tableData.addRowWithCode(BILL_COUNTRY, billAddress.getCountry() != null ? billAddress.getCountry().getName() : "");
                        tableData.addRowWithCode(BILL_CITY, billAddress.getCity() != null ? billAddress.getCity() : "");
                        tableData.addRowWithCode(BILL_STATE, (billAddress.getState() != null && billAddress.getState().getName() != null) ? billAddress.getState().getName() : "");
                        tableData.addRowWithCode(BILL_ZIPCODE, billAddress.getZipCode() != null ? billAddress.getZipCode() : "");
                    }
                    if (clientSupplier.getPrimaryContact() != null) {
                        tableData.addRowWithCode(CLIENT_CONTACT, escapeHtml(clientSupplier.getPrimaryContact().getName()));
                        tableData.addRowWithCode(CONTACT_EMAIL, clientSupplier.getPrimaryContact().getPrimaryEmail() != null ? clientSupplier.getPrimaryContact().getPrimaryEmail() : "");
                        tableData.addRowWithCode(CONTACT_PHONE, clientSupplier.getPrimaryContact().getPrimaryPhone() != null ? clientSupplier.getPrimaryContact().getPrimaryPhone().replace("|", "") : "");
                    }
                    tableData.addRowWithCode(BANK_NAME, escapeHtml(clientSupplier.getBankName()));
                    tableData.addRowWithCode(BANK_ACCOUNT_NAME, escapeHtml(clientSupplier.getAccountName()));
                    tableData.addRowWithCode(BANK_ACCOUNT_NUMBER, escapeHtml(clientSupplier.getAccountNo()));
                    tableData.addRowWithCode(SWIFT_BIC, escapeHtml(clientSupplier.getSwiftCode()));
                    tableData.addRowWithCode(SORT_CODE, escapeHtml(clientSupplier.getSortCode()));
                    tableData.addRowWithCode(IBAN_CODE, escapeHtml(clientSupplier.getIbanCode()));
                    tableData.addRowWithCode(BRANCH, escapeHtml(clientSupplier.getBranch()));
                    tableData.addRowWithCode("BANK_ACCOUNT_ADDRESS", escapeHtml(clientSupplier.getBankAddress()));

                    return tableData;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public CustomisedITextTable getCustomEmployeeEntityCustomFieldTable(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null || customFieldItems.isEmpty()) {
            return null;
        }
        for (CompanyCustomFieldItem fieldsItem : customFieldItems) {
            if ((UI_TYPE_ENTITY_DROPDOWN.equals(fieldsItem.getUiType())
                    || TYPE_ENTITY_LOOKUP.equals(fieldsItem.getUiType())
                    || UI_TYPE_LOOKUP.equals(fieldsItem.getUiType()))
                    && fieldsItem.getSelectedId() != null) {
                try {
                    EdsEmployee employee = employeeManager.get(fieldsItem.getSelectedId());
                    if (employee == null) {
                        continue;
                    }
                    CustomisedITextTable tableData = new CustomisedITextTable();
                    tableData.addColumnOrder(COLUMN_VALUE);
                    tableData.addRowWithCode("PRIMARY_PHONE", escapeHtml(employee.getPrimaryPhone()));
                    tableData.addRowWithCode("EMPLOYEE_NAME", escapeHtml(employee.getFullName()));
                    return tableData;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getBankCustomFields(List<CompanyCustomFieldItem> bankCustomFields) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (bankCustomFields != null && !bankCustomFields.isEmpty()) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : bankCustomFields) {
                if (item != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                    if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                        cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                    } else {
                        cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                    }
                    if (item.getFieldName() != null) {
                        itemCusFields.put(item.getFieldName(), cols);
                    }
                }
            }
            customFields.put(PDFConstants.INVOICE, itemCusFields);
        }
        return customFields;
    }

    public boolean isArabicCompany(EdsUser edsUser) {
        if (edsUser.getCompany().getCountryZone() != null && edsUser.getCompany().getCountryZone().getCountry() != null) {
            return ("AE".equals(edsUser.getCompany().getCountryZone().getCountry().getCode())
                    || "SA".equals(edsUser.getCompany().getCountryZone().getCountry().getCode())
                    || "OM".equals(edsUser.getCompany().getCountryZone().getCountry().getCode())
                    || "QA".equals(edsUser.getCompany().getCountryZone().getCountry().getCode()));
        }
        return false;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return null;
    }

    public static class InnerVatClass {

        private EdsVat vat;
        private Integer vatID;
        private BigDecimal joinedVatAmount;
        private BigDecimal vatAmount;

        public BigDecimal getJoinedVatAmount() {
            return joinedVatAmount;
        }

        public void setJoinedVatAmount(BigDecimal joinedVatAmount) {
            this.joinedVatAmount = joinedVatAmount;
        }

        public EdsVat getVat() {
            return vat;
        }

        public void setVat(EdsVat vat) {
            this.vat = vat;
        }

        public BigDecimal getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(BigDecimal vatAmount) {
            this.vatAmount = vatAmount;
        }

        public Integer getVatID() {
            return vatID;
        }

        public void setVatID(Integer vatID) {
            this.vatID = vatID;
        }
    }
}
