package com.edatasite.workforce.gwt.core.client.rpc.solr;

/**
 * Created by IntelliJ IDEA.
 * User: HaveANiceDay
 * Date: 25.10.11
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */

public class SolrPurchaseInvoiceRepresenter extends SolrDocumentRepresenter {
    public static final String SPLIT = "@";
    public static final String ARROW = " -> ";

    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";

    public static final String FIELD_PURCHASEINVOICE_RELATED_PROJECT_STATUS_CODE = "purchaseInvoiceRelatedProjectStatusCode";
    public static final String FIELD_PURCHASEINVOICE_SUPPLIER_VAT_NUMBER = "purchaseInvoiceSupplierVatNumber";
    public static final String FIELD_PURCHASEINVOICE_SUPPLIER_TRN = "purchaseInvoiceSupplierTrn";
    public static final String FIELD_PURCHASEINVOICE_TOTAL_TAXES = "purchaseInvoiceTotalTaxes";
    public static final String FIELD_PURCHASEINVOICE_EXCHANGE_RATE = "purchaseInvoiceExchangeRate";
    public static final String FIELD_PURCHASEINVOICE_TAX_CALCULATION_TYPE = "purchaseInvoiceTaxCalculationType";
    public static final String FIELD_PURCHASEINVOICE_ID = "purchaseInvoiceId";
    public static final String FIELD_PURCHASEINVOICE_NUMBER = "purchaseInvoiceNumber";
    public static final String FIELD_INVOICE_TYPE = "invoiceType";
    public static final String FIELD_INVOICE_DATE = "invoiceDate";
    public static final String FIELD_DUE_DATE = "dueDate";
    public static final String FIELD_RELATED_PROJECT_ID = "relatedProjectId";
    public static final String FIELD_RELATED_PROJECT_NAME = "relatedProjectName";
    public static final String FIELD_RELATED_PROJECT_NUMBER = "relatedProjectNumber";
    public static final String FIELD_RELATED_PROJECT_ID_NAME = "relatedProjectIdName";

    public static final String FIELD_MULTI_PROJECT_NAME = "multiProjectName";
    public static final String FIELD_MULTI_PROJECT_NUMBER = "multiProjectNumber";
    public static final String FIELD_MULTI_PROJECT_ID = "multiProjectId";
    public static final String FIELD_MULTI_PROJECT_ID_NAME = "multiProjectIdName";
    public static final String FIELD_MULTI_PROJECT_NUMBER_NAME = "multiProjectNumberName";

    public static final String FIELD_CLIENT_ID = "clientId";
    public static final String FIELD_CLIENT_NAME = "clientName";
    public static final String FIELD_CLIENT_ID_NAME = "clientIdName";
    public static final String FIELD_CLIENT_OWNER_ID = "clientOwnerId";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_CURRENCY_NAME = "currencyName";
    public static final String FIELD_CURRENCY_ID_NAME = "currencyIdName";
    public static final String FIELD_DUE_AMOUNT = "dueAmount";
    public static final String FIELD_PAID_AMOUNT = "paidAmount";
    public static final String FIELD_TAX_AMOUNT = "taxAmount";
    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_STATUS_SORDER = "statusSorder";
    public static final String FIELD_IS_CREDIT_NOTE = "isCreditNote";
    public static final String FIELD_TOTAL_IN_INVOICE_CURRENCY = "totalInInvoiceCurrency";
    public static final String FIELD_TOTAL_INVOICE_BASE = "totalInvoiceBase";
    public static final String FIELD_PO_NUMBER = "poNumber";
    public static final String FIELD_REFERENCE = "reference";
    public static final String HAS_PAYMENT = "hasPayment";
    public static final String FIELD_CURRENT_APPROVER_ID = "currentApproverId";
    public static final String FIELD_CURRENT_APPROVER_NAME = "currentApproverName";
    public static final String FIELD_CURRENT_APPROVER_ID_NAME = "currentApproverIdName";

    public static final String SORTABLE_PURCHASEINVOICE_NUMBER = "sortablePurchaseInvoiceNumber";
    public static final String SORTABLE_RELATED_PROJECT_NAME = "sortableRelatedProjectName";
    public static final String SORTABLE_CLIENT_NAME = "sortableClientName";
    public static final String SORTABLE_CURRENCY_NAME = "sortableCurrencyName";
    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String PURCHASE_INVOICE_SOLR_DOC = "PurchaseInvoiceSolrInputDocument";

    public static final String FIELD_ITEM_ID = "itemId";
}
