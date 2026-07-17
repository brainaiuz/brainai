package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.InvoicePaymentRequestObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_uz_lotin;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Dilshod Madrahimov on 3/16/15.
 */
public class InvoicePaymentViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants, AccountingConstants {

    private boolean isCashRefund;
    private boolean isPrePayment;
    private boolean isReceivable;
    private boolean isSupplierCredit;
    private boolean isCustomerPrepayment;
    private PaymentItem paymentItem;
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat defaultScaleFormat = new DecimalFormat(",##0.00");

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    CurrencyManager currencyManager;
    @Autowired
    CrmAccountManager crmAccountManager;
    @Autowired
    QuoteManager quoteManager;

    public void setInvoiceService(InvoiceService invoiceServ) {
        invoiceService = invoiceServ;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData genericPdfData = new ITextGenericPdfData();
        CustomisedITextTable paymentTable = new CustomisedITextTable();

        //new table for applied invoices
        CustomisedITextTable appliedTransactionsTable = new CustomisedITextTable();
        appliedTransactionsTable.addColumnOrder("DOCUMENT");
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(company, null);

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        InvoicePaymentRequestObject ro = (InvoicePaymentRequestObject) dataClass;
        PaymentItem ip = invoiceService.getPaymentOrRefund(ro.getObjectID(), isCashRefund);
        paymentTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);

        String project = ip.getProject() != null ? ip.getProject().getName() : "";
        String account = ip.getPaidTo() != null ? ip.getPaidTo() : "";
        String recPay = ip.getReceivablePayable() != null ? ip.getReceivablePayable().getName() : "";
        String note = ip.getNote() != null ? ip.getNote() : "";
        String number = ip.getNumber() != null ? ip.getNumber() : "";
        String date = ip.getDate() != null ? shortDateFormat.format(ip.getDate().getDate()) : "";
        String reference = ip.getReference() != null ? ip.getReference() : "";

        String amount = defaultScaleFormat.format(ip.getAmount() != null ? ip.getAmount() : BigDecimal.ZERO);
        String remBalance = defaultScaleFormat.format(ip.getRemainingBalance() != null ? ip.getRemainingBalance() : BigDecimal.ZERO);
        String refTotal = defaultScaleFormat.format(ip.getRefundPaymentAmount() != null ? ip.getRefundPaymentAmount() : BigDecimal.ZERO);
        String appliedTotal = "";
        if (ip.getAppliedPaymentAmount() != null && ip.getAppliedPaymentAmount().compareTo(BigDecimal.ZERO) != 0) {
            appliedTotal = getMoneyFormat(ip.getAppliedPaymentAmount());
        }
        String currency = ip.getCurrency() != null && ip.getCurrency().getName() != null ? ip.getCurrency().getName() : "";
        NumberToWord_en numberToWord_en = new NumberToWord_en();
        NumberToWord_ru numberToWord_ru = new NumberToWord_ru();
        NumberToWord_uz_lotin numberToWord_uz_lotin = new NumberToWord_uz_lotin();
        String amountInWordEn = "";
        String amountInWordRu = "";
        String amountInWordUz = "";
        if (ip.getAmount() != null) {
            amountInWordEn = WordUtils.capitalizeFully(numberToWord_en.toWord(ip.getAmount().abs()));
            amountInWordRu = WordUtils.capitalizeFully(numberToWord_ru.toWord(ip.getAmount().abs()));
            amountInWordUz = WordUtils.capitalizeFully(numberToWord_uz_lotin.toWord(ip.getAmount().abs()));
        }
        EdsSaleQuote quote = ip.getSaleQuoteItem() != null ? quoteManager.getSaleQuote(ip.getSaleQuoteItem().getId()) : null;

        paymentTable.addRowWithCode("PROJECT", commonLocalizer.localize(PdfLocalizationName.project), escapeHtml(project));
        paymentTable.addRowWithCode("ACCOUNT", commonLocalizer.localize(PdfLocalizationName.bankName), escapeHtml(account));
        paymentTable.addRowWithCode(RECEIVABLE_PAYABLE, RECEIVABLE_PAYABLE, escapeHtml(recPay));
        paymentTable.addRowWithCode("NOTE", commonLocalizer.localize(PdfLocalizationName.note), escapeHtml(note));
        paymentTable.addRowWithCode("NUMBER", commonLocalizer.localize(PdfLocalizationName.number), escapeHtml(number));
        paymentTable.addRowWithCode("DATE", commonLocalizer.localize(PdfLocalizationName.date), escapeHtml(date));
        paymentTable.addRowWithCode("DATE_IN_SIMPLE_FORMAT", commonLocalizer.localize(PdfLocalizationName.date), ip.getDate() != null ? escapeHtml(format.format(ip.getDate().getDate())) : "");
        paymentTable.addRowWithCode(REFERENCE, commonLocalizer.localize(PdfLocalizationName.reference), escapeHtml(reference));
        paymentTable.addRowWithCode("AMOUNT", commonLocalizer.localize(PdfLocalizationName.amount), escapeHtml(amount));
        paymentTable.addRowWithCode("APPLIED_TOTAL", commonLocalizer.localize(PdfLocalizationName.appliedTotal), appliedTotal);
        paymentTable.addRowWithCode(CURRENCY, commonLocalizer.localize(PdfLocalizationName.currency), currency);
        paymentTable.addRowWithCode("AMOUNT_IN_WORD_EN", commonLocalizer.localize(PdfLocalizationName.amountInWords), amountInWordEn);
        paymentTable.addRowWithCode("AMOUNT_IN_WORD_RU", commonLocalizer.localize(PdfLocalizationName.amountInWords), amountInWordRu);
        paymentTable.addRowWithCode("AMOUNT_IN_WORD_UZ", commonLocalizer.localize(PdfLocalizationName.amountInWords), amountInWordUz);
        paymentTable.addRowWithCode(CREATOR, commonLocalizer.localize(PdfLocalizationName.preparedBy), escapeHtml(ip.getUser()));
        paymentTable.addRowWithCode(REMAINING_BALANCE, commonLocalizer.localize(PdfLocalizationName.remainingBalance), escapeHtml(remBalance));
        paymentTable.addRowWithCode("REFUND_TOTAL", commonLocalizer.localize(PdfLocalizationName.refundTotal), escapeHtml(refTotal));

        if (isCustomerPrepayment && ip.getSaleQuoteItem() != null) {
            paymentTable.addRowWithCode("SQ_NUMBER", commonLocalizer.localize(PdfLocalizationName.quoteNo), escapeHtml(ip.getSaleQuoteItem().getName()));
        } else if (isSupplierCredit && ip.getPurchaseOrderItem() != null){
            paymentTable.addRowWithCode("PO_NUMBER", commonLocalizer.localize(PdfLocalizationName.poNumber), escapeHtml(ip.getPurchaseOrderItem().getName()));
        }
        if (quote != null) {
            Date invoiceDate = quote.getInvoiceDate();
            paymentTable.addRowWithCode("SQ_CUSTOMER_NAME", commonLocalizer.localize(PdfLocalizationName.customer), quote.getClient() != null ? quote.getClient().getName() : "");
            paymentTable.addRowWithCode("SQ_DATE", commonLocalizer.localize(PdfLocalizationName.date), quote.getInvoiceDate() != null ?  shortDateFormat.format(invoiceDate): "");
        }

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        if (!CollectionUtils.isEmpty(ip.getAppliedPayments())) {
            ip.getAppliedPayments().stream().filter(p -> p.getInvoice() != null).forEach(p -> {
                appliedTransactionsTable.addRow(escapeHtml((p.getInvoice().getName())));
            });
        }
        customData.put("APPLIED_DOCUMENTS_TABLE", appliedTransactionsTable);

        CustomisedITextTable applyCreditData = new CustomisedITextTable();
        applyCreditData.addColumn("SUPPLIER", commonLocalizer.localize(PdfLocalizationName.supplier));
        applyCreditData.addColumn("INVOICE_NUMBER", commonLocalizer.localize(PdfLocalizationName.invoiceNumber));
        applyCreditData.addColumn("INVOICE_DATE", commonLocalizer.localize(PdfLocalizationName.invoiceDate));
        applyCreditData.addColumn("DUE_DATE", commonLocalizer.localize(PdfLocalizationName.dueDate));
        applyCreditData.addColumn("INVOICE_TOTAL", commonLocalizer.localize(PdfLocalizationName.invoiceTotal));
        applyCreditData.addColumn("APPLIED_AMOUNT", commonLocalizer.localize(PdfLocalizationName.appliedAmount));


        if (ip != null && ip.getAppliedPayments() != null && !ip.getAppliedPayments().isEmpty()) {
            for (PaymentItem item : ip.getAppliedPayments()) {
                String supplier = item.getCrmAccount() != null ? escapeHtml(item.getCrmAccount().getName()) : "";
                String invoiceNumber = item.getInvoice() != null ? escapeHtml(item.getInvoice().getName()) : "";
                String invoiceDate = item.getInvoiceDate() != null ? shortDateFormat.format(item.getInvoiceDate()) : "";
                String dueDate = item.getInvoiceDueDate() != null ? shortDateFormat.format(item.getInvoiceDueDate()) : "";
                String invoiceTotal = qtyNumberFormat.format(Optional.ofNullable(item.getInvoiceTotal()).orElse(BigDecimal.ZERO));
                String appliedCreditTotal = qtyNumberFormat.format(Optional.ofNullable(item.getAmount()).orElse(BigDecimal.ZERO));
                applyCreditData.addRow(supplier, invoiceNumber, invoiceDate, dueDate, invoiceTotal, appliedCreditTotal);
            }
        }


        Map<String, String> localizeLabels = new LinkedHashMap<>();
        localizeLabels.put("SUPPLIER_LABEL", commonLocalizer.localize(PdfLocalizationName.supplier));
        localizeLabels.put("CUSTOMER_LABEL", commonLocalizer.localize(PdfLocalizationName.customer));
        localizeLabels.put("PAID_TO", commonLocalizer.localize(PdfLocalizationName.paidTo));

        customData.put("PAYMENT_TABLE", paymentTable);
        customData.put("APPLY_CREDIT_DATA", applyCreditData);
        customData.put("CUSTOM_FIELD", customFieldData(ip));
        customData.put("QUOTE_ITEMS", getQuoteItemsTable(quote));

        genericPdfData.setCustomData(customData);
        genericPdfData.setCompanyData(getCompanyData(company, true, hasPhantom));

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        baseInvoice.setCustomProductTableList(getRefundInformation(ip));
        baseInvoice.setCustomBillToAddress(getCustomBillToAddressTable(ip));
        baseInvoice.setObjectId(ip.getObjectId());

        genericPdfData.setBaseInvoice(baseInvoice);
        genericPdfData.setLocalizeLabels(localizeLabels);
        genericPdfData.setUserId(userManager.getUser().getObjectID().toString());
        return genericPdfData;
    }

    public List<CustomisedITextTable> getRefundInformation(PaymentItem ip) {
        if (ip == null) {
            return null;
        }
        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(company);

        List<CustomisedITextTable> refInfTables = new ArrayList<>();
        if (ip.getRefundPayments() != null && !ip.getRefundPayments().isEmpty()) {
            for (PaymentItem refundPayment : ip.getRefundPayments()) {
                CustomisedITextTable refInfTable = new CustomisedITextTable();
                refInfTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);

                refInfTable.addRowWithCode("CRM_ACCOUNT", commonLocalizer.localize(PdfLocalizationName.customer), refundPayment.getCrmAccount() != null ?  escapeHtml(refundPayment.getCrmAccount().getName()) : "");
                refInfTable.addRowWithCode("REFUND_NUMBER", commonLocalizer.localize(PdfLocalizationName.refund), refundPayment.getInvoice() != null ? escapeHtml(refundPayment.getInvoice().getName()) : "");
                refInfTable.addRowWithCode("DATE", commonLocalizer.localize(PdfLocalizationName.date), refundPayment.getInvoiceDate() != null ? escapeHtml(dateFormat.format(refundPayment.getInvoiceDate())) : "");
                refInfTable.addRowWithCode("DATE_IN_SIMPLE_FORMAT", commonLocalizer.localize(PdfLocalizationName.date), refundPayment.getInvoiceDate() != null ? escapeHtml(format.format(refundPayment.getInvoiceDate())) : "");
                refInfTable.addRowWithCode("TOTAL_AMOUNT", commonLocalizer.localize(PdfLocalizationName.totalAmount), refundPayment.getInvoiceTotal() != null ? escapeHtml(defaultScaleFormat.format(refundPayment.getInvoiceTotal())) : "");
                refInfTable.addRowWithCode("REFUND_AMOUNT", commonLocalizer.localize(PdfLocalizationName.refundAmount), refundPayment.getAmount() != null ? defaultScaleFormat.format(refundPayment.getAmount()) : "");
                refInfTable.addRowWithCode(CURRENCY, commonLocalizer.localize(PdfLocalizationName.currency), refundPayment.getCurrency() != null ? escapeHtml(refundPayment.getCurrency().getName()) : "");
                refInfTable.addRowWithCode("CLOSE_AMOUNT", commonLocalizer.localize(PdfLocalizationName.closeAmount), refundPayment.getCloseAmount() != null ? defaultScaleFormat.format(refundPayment.getCloseAmount()) : "");
                refInfTable.addRowWithCode("PAID_TO", commonLocalizer.localize(PdfLocalizationName.details), escapeHtml(refundPayment.getPaidTo()));
                refInfTables.add(refInfTable);
            }
        }

        return refInfTables;
    }

    public CustomisedITextTable getQuoteItemsTable(EdsSaleQuote quote) {
        CustomisedITextTable quoteItemsTable = new CustomisedITextTable();
        if (quote == null) {
            return null;
        }

        quoteItemsTable.addColumn("SQ_PRODUCT_NAME", commonLocalizer.localize(PdfLocalizationName.product));
        quoteItemsTable.addColumn("SQ_PRODUCT_NUMBER", commonLocalizer.localize(PdfLocalizationName.number));
        quoteItemsTable.addColumn("SQ_PRODUCT_UNIT_MEASUREMENT", commonLocalizer.localize(PdfLocalizationName.unitMeasurement));
        quoteItemsTable.addColumn("SQ_PRODUCT_QTY", commonLocalizer.localize(PdfLocalizationName.quantity));

        final List<String> quoteItemValues = Lists.newArrayList();
        if (quote != null && !quote.getQuoteItems().isEmpty()) {
            for (EdsQuoteItem quoteItem : quote.getQuoteItems()) {

                String name = quoteItem.getItem() != null ? quoteItem.getItem().getName() != null ? escapeHtml(quoteItem.getItem().getName()) : "" : "";
                String productNumber = quoteItem.getItem() != null ? quoteItem.getItem().getProductNumber() != null ?  escapeHtml(quoteItem.getItem().getProductNumber()) : "" : "";
                String unitMeasurement = quoteItem.getItem() != null ? quoteItem.getItem().getUnitMeasurement() != null ? quoteItem.getItem().getUnitMeasurement().getName() != null ?  escapeHtml(quoteItem.getItem().getUnitMeasurement().getName()) : "" : "" : "";
                BigDecimal qty = quoteItem.getQty() != null ? (quoteItem.getQty()) : BigDecimal.ZERO;

                quoteItemValues.add(name);
                quoteItemValues.add(productNumber);
                quoteItemValues.add(unitMeasurement);
                quoteItemValues.add(qty.toString());
                quoteItemsTable.addRow(quoteItemValues.toArray(new String[]{}));
                quoteItemValues.clear();
            }
        }

        return quoteItemsTable;
    }

    public CustomisedITextTable customFieldData(PaymentItem ip) {

        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(company);
        DecimalFormat numberFormat = getPriceScaleNumberFormat(company, null);
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);

        if (ip != null && ip.getCustomFields() != null && !ip.getCustomFields().isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : ip.getCustomFields()) {
                switch (fieldItem.getDataType()) {
                    case DATA_TYPE_DATE:
                        String dateValue = "";
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                        if (fieldItem.getFieldDateNonConvertedValue() != null) {
                            dateValue = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            if (fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate());
                                calendar.add(Calendar.DAY_OF_MONTH, 14);
                            }
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), dateValue, DATA_TYPE_DATE);
                        break;
                    case DATA_TYPE_NUMBER:
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(fieldItem.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), numberValue, DATA_TYPE_NUMBER);
                        break;
                    case DATA_TYPE_TEXT:
                        String textValue = "";
                        if (TYPE_ENTITY_LOOKUP.equals(fieldItem.getUiType())) {
                            String defaultValue = "";
                            if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                                Integer id = null;
                                try {
                                    id = Integer.valueOf(fieldItem.getFieldStringValue());
                                } catch (final NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (id != null && fieldItem.getQueryItems() != null) {
                                    for (final SelectItem selectItem : fieldItem.getQueryItems()) {
                                        if (selectItem.getId().equals(id)) {
                                            defaultValue = escapeHtml(selectItem.getName());
                                            break;
                                        }
                                    }
                                }
                            }
                            customFieldTable.addRowWithCode(fieldItem.getDefaultName(), fieldItem.getAliasName(), escapeHtml(defaultValue));
                        } else if (UI_TYPE_HTML_TEXTAREA.equals(fieldItem.getUiType())) {
                            String html = fieldItem.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            textValue = doc.body().text();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        } else {
                            textValue = fieldItem.getFieldStringValue();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        }
                        break;
                    case DATA_TYPE_PROFILE_IMAGE:
                        String uploadImageId = "";
                        if (fieldItem.getProfielImageId() != null) {
                            uploadImageId = commonService.getImageUrl(fieldItem.getProfielImageId());
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), uploadImageId, UI_TYPE_PROFILE_IMAGE_WIDGET);
                        break;
                    default:
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(fieldItem.getFieldStringValue()), DATA_TYPE_TEXT);
                        break;

                }

            }
        }
        return customFieldTable;
    }

    private CustomisedITextTable getCustomBillToAddressTable(PaymentItem ip) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        SelectItem accountItem = ip.getCrmAccount();
        if (accountItem == null || accountItem.getId() == null) {
            return addressTable;
        }
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(accountItem.getId());
        final EdsUser user = crmAccountManager.getUser();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        if (edsCrmAccount == null) {
            return addressTable;
        }
        addressTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        String crmAccountName = ip.getCrmAccount() != null ? ip.getCrmAccount().getName() : "";

        EdsAddress customerPrepaymentAddress = null;
        if (edsCrmAccount.getBillingAddress() != null && edsCrmAccount.getBillingAddress().getAddress() != null) {
            customerPrepaymentAddress = edsCrmAccount.getBillingAddress();
        } else if (edsCrmAccount.getMailingAddress() != null && edsCrmAccount.getMailingAddress().getAddress() != null){
            customerPrepaymentAddress = edsCrmAccount.getMailingAddress();
        }


        String address = "";
        String address2 = "";
        String city = "";
        String country = "";
        String zipCode = "";
        if (customerPrepaymentAddress != null) {
            address = customerPrepaymentAddress.getAddress() != null ? escapeHtml(customerPrepaymentAddress.getAddress()) : "";
            address2 = customerPrepaymentAddress.getAddressb() != null ? escapeHtml(customerPrepaymentAddress.getAddressb()) : "";
            city = customerPrepaymentAddress.getCity() != null ? escapeHtml(customerPrepaymentAddress.getCity()) : "";
            country = customerPrepaymentAddress.getCountryName() != null ? escapeHtml(customerPrepaymentAddress.getCountryName()) : "";
            zipCode = customerPrepaymentAddress.getZipCode() != null ? escapeHtml(customerPrepaymentAddress.getZipCode()) : "";
        }
        String phone = edsCrmAccount.getPhone() != null ? escapeHtml(edsCrmAccount.getPhone()) : "";
        String email = edsCrmAccount.getEmail() != null ? escapeHtml(edsCrmAccount.getEmail()) : "";
        String date = format.format(ServerUtils.getCompanyDate(new Date(), user.getCompany()));

        addressTable.addRowWithCode("CRM_ACCOUNT", "CRM_ACCOUNT", escapeHtml(crmAccountName));
        addressTable.addRowWithCode(ADDRESS1, commonLocalizer.localize(PdfLocalizationName.address), escapeHtml(address));
        addressTable.addRowWithCode(ADDRESS2, commonLocalizer.localize(PdfLocalizationName.address), escapeHtml(address2));
        addressTable.addRowWithCode(BILL_CITY, commonLocalizer.localize(PdfLocalizationName.city), escapeHtml(city));
        addressTable.addRowWithCode(COUNTRY_NAME, commonLocalizer.localize(PdfLocalizationName.country), escapeHtml(country));
        addressTable.addRowWithCode(BILL_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), escapeHtml(zipCode));
        addressTable.addRowWithCode(PHONE, commonLocalizer.localize(PdfLocalizationName.phone), escapeHtml(phone));
        addressTable.addRowWithCode(EMAIL, commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(email));
        addressTable.addRowWithCode(CURRENT_DATE_BY_COMPANY_FORMAT, "", escapeHtml(date));
        addressTable.addRowWithCode(CURRENT_TIME, "", escapeHtml(simpleDateFormat.format(user.getUserDate())));
        return addressTable;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof InvoicePaymentRequestObject) {
            return ((InvoicePaymentRequestObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        isCashRefund = Boolean.valueOf(request.getParameter("isCashRefund"));
        isPrePayment = Boolean.valueOf(request.getParameter("isPrePayment"));
        isReceivable = Boolean.valueOf(request.getParameter("isReceivable"));
        isSupplierCredit = Boolean.valueOf(request.getParameter("isSupplierCredit"));
        isCustomerPrepayment = Boolean.valueOf(request.getParameter("isCustomerPrepayment"));
        return new InvoicePaymentRequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("PaymentView_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        audingPdfFooterSignature(pdfReader, pdfStamper, document);
        super.initPagingAndStamper(pdfReader, pdfStamper, document, iTextPdfTemplateEvent, dataClass);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        if (isReceivable) {
            return PdfReferenceCodeNameEnum.PREPAYMENT;
        } else {
            return PdfReferenceCodeNameEnum.SUPPLIER_CREDIT;
        }
    }

    @Override
    protected String getTableName(Object dataClass) {
        String tableName = "Payment View";
        if (isCashRefund) {
            tableName = "Refund View";
        } else if (isCustomerPrepayment) {
            tableName = commonLocalizer.localize(PdfLocalizationName.customerPrepayment);
        } else if (isSupplierCredit) {
            tableName = commonLocalizer.localize(PdfLocalizationName.supplierPrepayment).replaceFirst("[%][s]", commonLocalizer.localize(PdfLocalizationName.supplier));
        }
        return tableName;
    }
}
