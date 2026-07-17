package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 10-Aug-2010
 * Time: 04:34:59
 */
public class SolrSaleInvoiceRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String ARROW = " -> ";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_SALEINVOICE_ID = "saleInvoiceId";
    public static final String FIELD_SHIPPING_DATA_ID = "shippingDataId";
    public static final String FIELD_SHIPPING_DATA_NUMBER = "shippingDataNumber";
    public static final String FIELD_OPPORTUNITY_ID = "opportunityId";
    public static final String FIELD_CLIENT_ID = "clientId";
    public static final String FIELD_CLIENT_NAME = "clientName";
    public static final String FIELD_PRODUCT_NAME = "productName";
    public static final String FIELD_CLIENT_ID_NAME = "clientIdName";
    public static final String FIELD_CUSTOM_CLIENT_ID = "customClientId";
    public static final String FIELD_CUSTOM_CLIENT_NAME = "customClientName";
    public static final String FIELD_CUSTOM_CLIENT_ID_NAME = "customClientIdName";
    public static final String FIELD_CLIENT_CONTACT_ID = "clientContactId";
    public static final String FIELD_CLIENT_CONTACT_EMAIL = "clientContactEmail";
    public static final String FIELD_CLIENT_CONTACT_ID_EMAIL = "clientContactIdEmail";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_CURRENCY_NAME = "currencyName";
    public static final String FIELD_CURRENCY_ID_NAME = "currencyIdName";
    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";
    public static final String FIELD_STATUS_SORDER = "statusSorder";
    public static final String FIELD_SHIPPING_DATA_STATUS_NAME = "shippingDataStatusName";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_SHIPPING_DATE = "shippingDate";
    public static final String FIELD_GDN_IS_SALES_ORDER = "gdnIsSalesOrder";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_NAME = "supplierName";
    public static final String FIELD_SUPPLIER_ID_NAME = "supplierIdName";
    public static final String FIELD_DUE_AMOUNT = "dueAmount";
    public static final String FIELD_IS_SALES_ORDER = "isSalesOrder";
    public static final String FIELD_IS_GDN = "isGdn";
    public static final String FIELD_PAID_AMOUNT = "paidAmount";
    public static final String FIELD_SORTABLE_PAID_AMOUNT = "sortablePaidAmount";
    public static final String FIELD_SHPPINGMETHOD_ID = "shppingMethodId";
    public static final String FIELD_SHPPINGMETHOD_NAME = "shppingMethodName";
    public static final String FIELD_SHPPINGMETHOD_ID_NAME = "shppingMethodIdName";
    public static final String FIELD_INVOICE_NUMBER = "invoiceNumber";
    public static final String FIELD_INVOICE_DATE = "invoiceDate";
    public static final String FIELD_RFQ_DATE = "rfqDate"; //new
    public static final String FIELD_DUE_DATE = "dueDate";
    public static final String FIELD_PROJECT_ID = "projectId";
    public static final String FIELD_CREATOR_ID = "creatorId";
    public static final String FIELD_INVOICE_FROM_QUOTE_CREATOR_ID = "invoiceFromQuoteCreatorId";
    public static final String FIELD_CREATOR_NAME = "creatorName";
    public static final String FIELD_CREATOR_LOCATION_ID = "creatorLocationId";
    public static final String FIELD_CREATOR_ID_NAME = "creatorIdName";
    public static final String FIELD_MANAGER_ID = "managerId";
    public static final String FIELD_MANAGER_NAME = "managerName";
    public static final String FIELD_MANAGER_ID_NAME = "managerIdName";
    public static final String FIELD_IS_CREDITNODE = "isCreditNode";
    public static final String FIELD_TOTAL_TAXES = "totalTaxes";
    public static final String FIELD_TAX_CALCULATION_TYPE = "taxCalculationType";
    public static final String FIELD_EXCHARGE_RATE = "exchargeRate";
    public static final String FIELD_TOTAL_INVOICE_CURRENCY = "totalInvoiceCurrency";
    public static final String FIELD_TOTAL_INVOICE_BASE = "totalInvoiceBase";
    public static final String FIELD_RELATED_PROJECT_NAME = "relatedProjectName";
    public static final String FIELD_RELATED_PROJECT_CODE = "relatedProjectCode";
    public static final String FIELD_RELATED_PROJECT_NUMBER = "relatedProjectNumber";
    public static final String FIELD_RELATED_PROJECT_ID = "relatedProjectId";
    public static final String FIELD_RELATED_PROJECT_ID_NAME = "relatedProjectIdName";
    public static final String FIELD_IN_TARGET = "inTarget";
    public static final String FIELD_CURRENT_APPROVER_ID = "currentApproverId";
    public static final String FIELD_CURRENT_APPROVER_NAME = "currentApproverName";
    public static final String FIELD_CURRENT_APPROVER_ID_NAME = "currentApproverIdName";
    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_COUNTRY_NAME = "countryName";
    public static final String FIELD_COUNTRY_ID_NAME = "countryIdName";
    public static final String FIELD_PDF_TEMPLATE_ID = "pdfTemplateId";
    public static final String FIELD_INTRODUCTION = "introduction";
    public static final String FIELD_QUOTE_PERCENT = "quotePercent";
    public static final String FIELD_PROJECT_BASED = "projectBased";

    public static final String FIELD_MULTI_PROJECT_NAME = "multiProjectName";
    public static final String FIELD_MULTI_PROJECT_NUMBER = "multiProjectNumber";
    public static final String FIELD_MULTI_PROJECT_ID = "multiProjectId";
    public static final String FIELD_MULTI_PROJECT_ID_NAME = "multiProjectIdName";
    public static final String FIELD_MULTI_PROJECT_NUMBER_NAME = "multiProjectNumberName";

    public static final String FIELD_PICKLIST_ID = "picklistId";
    public static final String FIELD_OPPORTUNITY_NUMBER = "opportunityNumber";
    public static final String FIELD_QUOTE_NUMBER = "quoteNumber";
    public static final String FIELD_RFQ_ID = "rfqId";
    public static final String FIELD_RFQ_NUMBER = "rfqNumber";
    public static final String FIELD_PO_NUMBER = "poNumber";
    public static final String FIELD_SUB_TOTAL = "subTotal";
    public static final String FIELD_NET_AMOUNT_TOTAL = "netAmountTotal";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_UPDATED_DATE = "updatedDate";
    // Solr sortable fields
    public static final String SORTABLE_INVOICE_NUMBER = "sortableInvoiceNumber";
    public static final String SORTABLE_SHIPPING_DATA_NUMBER = "sortableShippingDataNumber";
    public static final String SORTABLE_CLIENT_NAME = "sortableClientName";
    public static final String SORTABLE_SUPPLIER_NAME = "sortableSupplierName";
    public static final String SORTABLE_CURRENCY_NAME = "sortableCurrencyName";
    public static final String SORTABLE_RELATED_PROJECT_NAME = "sortableRelatedProjectName";
    public static final String SORTABLE_SORTABLE_CURRENT_APPROVER_NAME = "sortableCurrentApproverName";

    public static final String IS_PROGRESS_INVOICING = "isProgressInvoicing";
    //    public static final String MANAGER_ID = "MANAGER_ID";
//    public static final String MANAGER_NAME = "MANAGER_NAME";
    public static final String INTRODUCTION = "introduction";
    public static final String REFERENCE = "reference";
    public static final String HAS_PAYMENT = "hasPayment";

    public static final String FIELD_ITEM_ID = "itemId";
    public static final String FIELD_CREATOR_FULL_NAME = "createrFullName";

    public static final String FIELD_CLIENT_VAT = "clientVat";
    public static final String FIELD_CLIENT_TRN = "clientTrn";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_ZATCA_STATUS = "zatcaStatus";
    public static final String FIELD_CUSTOMER_OWNER_ID = "customerOwnerId";
    public static final String FIELD_RELATED_PROJECT_MANAGER_ID = "projectManagerId";
    public static final String FIELD_PRODUCT_ID_INVOICE= "productIdsFromInvoice";


}
