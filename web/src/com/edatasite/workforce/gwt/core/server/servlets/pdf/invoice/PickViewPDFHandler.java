package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.server.app.itemBatches.ItemBatchServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: 01-Jun-2010
 * Time: 18:06:19
 * To change this template use File | Settings | File Templates.
 */
public class PickViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants  {

    @Autowired
    QuoteService quoteService;
    @Autowired
    private PickListManager pickListManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ItemBatchServiceLocal itemBatchService;

    @Override
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        boolean isAlmadarSerials = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
        return new Document(isAlmadarSerials ? PageSize.A4.rotate() : PageSize.A4, 20, 20, 120, 50);
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        RequestObject requestObject = (RequestObject) dataClass;
        EdsUser user = pickListManager.getUser();

        PickList pickList = quoteService.getPickList(requestObject.getObjectID());
        EdsQuote quote = pickList.getQuoteID() != null ? quoteManager.get(pickList.getQuoteID()) : null;

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        baseInvoice.setCustomNumberAndDatesTable(getNumberDatesTable(pickList, user, quote));
        pdfData.setBaseInvoice(baseInvoice);

        customData.put("PRODUCT_TABLE", getProductData(pickList, user));
        customData.put("QUOTE_PRODUCT_TABLE", getQuoteProductData(quote, user));
        customData.put("QUOTE_PRODUCT_BATCH_TABLE", getProductBatchItems(quote, user));
        customData.put("QUOTE_TOTAL_TABLE", getQuoteTotalData(quote));
        customData.put("QUOTE_BANK_ACCOUNT_TABLE", getQuoteBankAccountData(quote));

        CustomisedITextTable quoteCustomFieldTable = new CustomisedITextTable();
        quoteCustomFieldTable.setCustomFields(getQuoteCustomFieldsData(quote));
        customData.put("QUOTE_CUSTOM_FIELD", quoteCustomFieldTable);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

        pdfData.setCurrentDate(dateFormat.format(currentDate));
        pdfData.setCustomData(customData);

        return pdfData;
    }

    private CustomisedITextTable getNumberDatesTable(PickList pickList, EdsUser user, EdsQuote quote) {
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(user.getCompany(), null);

        String clientName = escapeHtml(pickList.getClientName());
        String orderDate = pickList.getSaleOrderDate() != null ? shortDateFormat.format(pickList.getSaleOrderDate().getNonConvertedDate()) : "";
        String dueDate = pickList.getDueDate() != null ? shortDateFormat.format(pickList.getDueDate().getNonConvertedDate()) : "";
        String shipDate = pickList.getShipDate() != null ? shortDateFormat.format(pickList.getShipDate().getNonConvertedDate()) : "";
        String expectedDate = pickList.getExpectedDate() != null ? shortDateFormat.format(pickList.getExpectedDate().getNonConvertedDate()) : "";
        String pickDate = pickList.getPickDate() != null ? shortDateFormat.format(pickList.getPickDate().getNonConvertedDate()) : "";
        String packDate = pickList.getPackDate() != null ? shortDateFormat.format(pickList.getPackDate().getNonConvertedDate()) : "";
        String gdnNumber = "";
        if (pickList.getGdnNumberData() != null && pickList.getGdnNumberData().getPrefix() != null && pickList.getGdnNumberData().getFourDigitNumber() != null) {
            gdnNumber = pickList.getGdnNumberData().getPrefix() + pickList.getGdnNumberData().getFourDigitNumber();
        }
        String orderNumber = escapeHtml(pickList.getOrderNumber());
        String grossWeight = pickList.getGrossWeight() != null ? qtyNumberFormat.format(pickList.getGrossWeight()) : BigDecimal.ZERO.toString();
        String carrierAccountId = escapeHtml(pickList.getCarrierAccountID());
        String shippingLabel = escapeHtml(pickList.getShippingLabel());
        String poNumber = escapeHtml(pickList.getPoNumber());
        String paymentInstruction = escapeHtml(pickList.getPaymentInstruction());
        String currencyName = quote != null && quote.getCurrency() != null ? escapeHtml(quote.getCurrency().getName()) : "";
        String statusCode = quote != null && quote.getStatus() != null ? escapeHtml(quote.getStatus().getCode()) : "";
        String orderApprover = "";
        if (quote != null && quote.getCurrentApprover() != null && quote.getCurrentApprover().getExactEmployee() != null) {
            orderApprover = escapeHtml(quote.getCurrentApprover().getExactEmployee().getName());
        }
        String dueTerms = "";
        if (quote instanceof EdsSaleQuote) {
            if (((EdsSaleQuote) quote).getInvoiceTerms() != null) {
                dueTerms = ((EdsSaleQuote) quote).getInvoiceTerms().getName();
            }
        }
        String creator = quote != null && quote.getCreator() != null ? escapeHtml(quote.getCreator().getFullName()) : "";
        String quoteTermsCondition = quote != null && quote.getPaymentInstruction() != null ? escapeHtml(quote.getPaymentInstruction()) : "";

        CustomisedITextTable numberDatesTable = new CustomisedITextTable();
        numberDatesTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        numberDatesTable.addRowWithCode(CLIENT_NAME, commonLocalizer.localize(PdfLocalizationName.customer), clientName);
        numberDatesTable.addRowWithCode(INV_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.orderDate), orderDate);
        numberDatesTable.addRowWithCode(INV_DUE_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.dueDate), dueDate);
        numberDatesTable.addRowWithCode(SHIPPING_DATE, commonLocalizer.localizeAccounting(PdfLocalizationName.shipDate), shipDate);
        numberDatesTable.addRowWithCode("EXPECTED_DATE", accountingLocalizer.localizeAccounting(PdfLocalizationName.expectedDate), expectedDate);
        numberDatesTable.addRowWithCode("PICK_DATE", accountingLocalizer.localizeAccounting("pickDate"), pickDate);
        numberDatesTable.addRowWithCode("PACK_DATE", accountingLocalizer.localizeAccounting("packDate"), packDate);
        numberDatesTable.addRowWithCode(GDN_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.gdnNumber), gdnNumber);
        numberDatesTable.addRowWithCode("SO_NUMBER", pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderNumber), orderNumber);
        numberDatesTable.addRowWithCode(PICK_GROSS_WEIGHT, accountingLocalizer.localizeAccounting(PdfLocalizationName.grossWeight), grossWeight);
        numberDatesTable.addRowWithCode(PICK_CARRIER_ACCOUNT_ID, accountingLocalizer.localizeAccounting(PdfLocalizationName.carrierAccountID), carrierAccountId);
        numberDatesTable.addRowWithCode(SHIPPING_LABEL, accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingLabel), shippingLabel);
        numberDatesTable.addRowWithCode(PO_NUMBER, accountingLocalizer.localizeAccounting(PdfLocalizationName.poNumber), poNumber);
        numberDatesTable.addRowWithCode("PAYMENT_INSTRUCTION", "Payment Instructions", paymentInstruction);
        numberDatesTable.addRowWithCode("QUOTE_PAYMENT_INSTRUCTION", "Payment Instructions", quoteTermsCondition);
        numberDatesTable.addRowWithCode("CURRENCY", "", currencyName);
        numberDatesTable.addRowWithCode("INVOICE_STATUS", "", statusCode);
        numberDatesTable.addRowWithCode("SALE_QUOTE_APPROVER", "", orderApprover);
        numberDatesTable.addRowWithCode("INVOICE_DUE_TERMS", "", dueTerms);
        numberDatesTable.addRowWithCode("CREATOR", "", creator);

        EdsCrmAccount customer = pickList.getClientID() != null ? crmAccountManager.get(pickList.getClientID()) : null;
        EdsAddress billAddress = customer != null && customer.getBillingAddress() != null ? customer.getBillingAddress() : null;
        if (billAddress != null) {
            numberDatesTable.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
            numberDatesTable.addRowWithCode(BILL_ADDRESS, "", escapeHtml(billAddress.getAddress()));
            numberDatesTable.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(billAddress.getAddressb()));
            numberDatesTable.addRowWithCode(BILL_CITY, "", escapeHtml(billAddress.getCity()));
            numberDatesTable.addRowWithCode(BILL_COUNTRY, "", escapeHtml(billAddress.getCountryName()));
            numberDatesTable.addRowWithCode(BILL_STATE, "", escapeHtml(billAddress.getStateName()));
            numberDatesTable.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(billAddress.getZipCode()));
        }
        if (customer != null) {
            numberDatesTable.addRowWithCode(CLIENT_PHONE, "", escapeHtml(customer.getPhone()));
            if (customer.getPrimaryContact() != null) {
                numberDatesTable.addRowWithCode("PRIMARY_CONTACT_NAME", "", escapeHtml(customer.getPrimaryContact().getName()));
                numberDatesTable.addRowWithCode("PRIMARY_CONTACT_EMAIL", "", escapeHtml(customer.getPrimaryContact().getPrimaryEmail()));
            }
        }

        return numberDatesTable;
    }

    private CustomisedITextTable getProductData(PickList pickList, EdsUser user) {
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(user.getCompany(), null);
        boolean isAlmadarSerials = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);

        CustomisedITextTable productTable = new CustomisedITextTable();
        productTable.addColumn(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
        productTable.addColumn("ITEM_NUMBER", "");
        productTable.addColumn(ITEM_NAME, commonLocalizer.localizeAccounting(PdfLocalizationName.product));
        productTable.addColumn(DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        productTable.addColumn(PICK_ITEM_REFERENCE, accountingLocalizer.localizeAccounting(PdfLocalizationName.reference));
        productTable.addColumn(ITEM_WAREHOUSE, accountingLocalizer.localizeAccounting(PdfLocalizationName.warehouse));
        productTable.addColumn(ITEM_QTY, accountingLocalizer.localizeAccounting(PdfLocalizationName.qty));
        productTable.addColumn(PICKED, accountingLocalizer.localizeAccounting(PdfLocalizationName.picked));
        productTable.addColumn(PACKED, accountingLocalizer.localizeAccounting(PdfLocalizationName.packed));
        productTable.addColumn("NUMBER_OF_PACKS", accountingLocalizer.localizeAccounting(PdfLocalizationName.numberOfPacks));
        productTable.addColumn("QTY_PER_PACK", accountingLocalizer.localizeAccounting(PdfLocalizationName.qtyPerPack));
        if (isAlmadarSerials) {
            productTable.addColumn("ARTICLE", "Article #");
            productTable.addColumn("READY_TO_SHIP", accountingLocalizer.localizeAccounting(PdfLocalizationName.radyToShip));
        }
        productTable.addColumn(SHIPPED, accountingLocalizer.localizeAccounting(PdfLocalizationName.shipped));
        productTable.addColumn(ITEM_PART_NUMBER, "Item Part Number");
        productTable.addColumn(ITEM_UNIT_MEASUREMENT, "UOM");
        productTable.addColumn(PICK_ITEM_BOOKED_QTY, "Reserved booked qty");

        if (pickList.getItems() == null) {
            return productTable;
        }
        List<String> values = Lists.newArrayList();
        int counter = 0;
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        for (PickListItem item : pickList.getItems()) {
            counter = counter + 1;
            values.add(counter + ".");
            values.add(escapeHtml(item.getItemNumber()));
            values.add(escapeHtml(item.getItemName()));
            values.add(escapeHtml(item.getDescription()));
            values.add(escapeHtml(item.getReference()));
            values.add(item.getWarehouse() != null ? escapeHtml(item.getWarehouse().getName()) : "");
            values.add(item.getQty() != null ? qtyNumberFormat.format(item.getQty()) : "0.00");
            values.add(item.getPicked() != null ? qtyNumberFormat.format(item.getPicked()) : "0.00");
            values.add(item.getPacked() != null ? qtyNumberFormat.format(item.getPacked()) : "0.00");
            values.add(item.getNumberOfPacks() != null ? qtyNumberFormat.format(item.getNumberOfPacks()) : "0.00");
            values.add(item.getQtyPerPack() != null ? qtyNumberFormat.format(item.getQtyPerPack()) : "0.00");
            if (isAlmadarSerials) {
                values.add(item.getArticleNumberCF() != null ? escapeHtml(item.getArticleNumberCF().getFieldStringValue()) : "");
                values.add(item.getReadyToShip() != null ? qtyNumberFormat.format(item.getReadyToShip()) : "0.00");
            }
            values.add(item.getShipped() != null ? qtyNumberFormat.format(item.getShipped()) : "0.00");

            String itemPartNumber = "";
            String measurement = "";
            if (item.getItemID() != null && item.getItemID() > 0) {
                EdsItem edsItem = itemManager.get(item.getItemID());
                if (edsItem != null) {
                    itemPartNumber = escapeHtml(edsItem.getPartNumber());
                    measurement = edsItem.getUnitMeasurement() != null ? escapeHtml(edsItem.getUnitMeasurement().getName()) : "";

                    //product custom fields
                    LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                    if (edsItem.getCustomFields() != null) {
                        List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView));
                        for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                            if (customFieldItem != null) {
                                Map<String, String> cols = new HashMap<>();
                                cols.put(COLUMN_NAME, customFieldItem.getFieldName() != null ? escapeHtml(customFieldItem.getFieldName()) : null);
                                if (CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
                                    cols.put(COLUMN_VALUE, customFieldItem.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                                } else {
                                    cols.put(COLUMN_VALUE, customFieldItem.getFieldStringValue() != null ? escapeHtml(customFieldItem.getFieldStringValue()) : null);
                                }
                                if (customFieldItem.getFieldName() != null) {
                                    itemCusFields.put(escapeHtml(customFieldItem.getFieldName()), cols);
                                }
                            }
                        }
                    }
                    customFields.put(counter + ".", itemCusFields);
                }
            }
            values.add(itemPartNumber);
            values.add(measurement);
            values.add(item.getBookReserve() != null ? qtyNumberFormat.format(item.getBookReserve()) : "0.00");

            productTable.addRow(values.toArray(new String[]{}));
            values.clear();
        }
        productTable.setCustomFields(customFields);

        return productTable;
    }

    private CustomisedITextTable getQuoteBankAccountData(EdsQuote quote) {
        CustomisedITextTable quoteBankAccountTable = new CustomisedITextTable();
        if (quote == null) {
            return quoteBankAccountTable;
        }

        EdsBankAccount bankAccount = quote.getBankAccount();

        if (bankAccount == null) {
            return quoteBankAccountTable;
        }
        String bankName = bankAccount.getAccount() != null ? escapeHtml(bankAccount.getAccount().getName()) : "";
        String ibanCode = escapeHtml(bankAccount.getIbanCode());
        String accountName = escapeHtml(bankAccount.getAccauntName());
        String swiftBic = escapeHtml(bankAccount.getSwiftCode());
        String accountNumber = escapeHtml(bankAccount.getAccountNumber());
        String bankBranch = escapeHtml(bankAccount.getBankBranch());
        String bankAddress = escapeHtml(bankAccount.getStreetAddress());
        String bankCity = escapeHtml(bankAccount.getCity());
        String bankCountry = bankAccount.getCountry() != null ? escapeHtml(bankAccount.getCountry().getName()) : "";

        quoteBankAccountTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        quoteBankAccountTable.addRowWithCode(BANK_NAME, "", bankName);
        quoteBankAccountTable.addRowWithCode(IBAN_CODE, "", ibanCode);
        quoteBankAccountTable.addRowWithCode(ACCOUNT_NAME, "", accountName);
        quoteBankAccountTable.addRowWithCode(SWIFT_BIC, "", swiftBic);
        quoteBankAccountTable.addRowWithCode(ACCOUNT_NUMBER, "", accountNumber);
        quoteBankAccountTable.addRowWithCode(BRANCH, "", bankBranch);
        quoteBankAccountTable.addRowWithCode(BILL_ADDRESS, "", bankAddress);
        quoteBankAccountTable.addRowWithCode(BILL_CITY, "", bankCity);
        quoteBankAccountTable.addRowWithCode(BILL_COUNTRY, "", bankCountry);

        return quoteBankAccountTable;
    }

    private CustomisedITextTable getQuoteTotalData(EdsQuote quote) {
        CustomisedITextTable quoteTotalTable = new CustomisedITextTable();
        if (quote == null) {
            return quoteTotalTable;
        }

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat calculationNumberFormat = getPriceScaleNumberFormat(fs);
        NumberToWord numberToWordConverter = new NumberToWord_en();
        int scale = 2;
        if (fs != null && fs.getCalculationScale() != null) {
            scale = fs.getCalculationScale();
        }

        String subTotal = quote.getSubtotal() != null ? calculationNumberFormat.format(quote.getSubtotal()) : BigDecimal.ZERO.toString();
        String discountTotal = quote.getDiscount() != null ? calculationNumberFormat.format(quote.getDiscount()) : BigDecimal.ZERO.toString();
        String discountAmountTotal = quote.getDiscountAmount() != null ? calculationNumberFormat.format(quote.getDiscountAmount()) : BigDecimal.ZERO.toString();
        String taxTotal = quote.getTotalTaxes() != null ? calculationNumberFormat.format(quote.getTotalTaxes()) : BigDecimal.ZERO.toString();
        BigDecimal quoteTotal = quote.getTotal() != null ? quote.getTotal() : BigDecimal.ZERO;
        String total = quoteTotal != null ? calculationNumberFormat.format(quoteTotal) : BigDecimal.ZERO.toString();
        String totalWordAll = numberToWordConverter.convert(quoteTotal.abs().setScale(scale, RoundingMode.HALF_UP));

        quoteTotalTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        quoteTotalTable.addRowWithCode(SUBTOTAL, "", subTotal);
        quoteTotalTable.addRowWithCode(DISCOUNT_TOTAL, "", discountTotal);
        quoteTotalTable.addRowWithCode(TAX_TOTAL, "", taxTotal);
        quoteTotalTable.addRowWithCode(TOTAL, "", total);
        quoteTotalTable.addRowWithCode(TOTAL_IN_WORDS, "", WordUtils.capitalizeFully(totalWordAll));

        return quoteTotalTable;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getQuoteCustomFieldsData(EdsQuote quote) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (quote == null) {
            return customFields;
        }

        if (quote.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(quote.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleQuote));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : "");
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : "");
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "");
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put("QUOTE", itemCusFields);
            }
        }
        return customFields;
    }

    private CustomisedITextTable getQuoteProductData(EdsQuote quote, EdsUser user) {
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(user.getCompany(), null);
        CustomisedITextTable quoteProductTable = new CustomisedITextTable();

        quoteProductTable.addColumn(ITEM_NO, "");
        quoteProductTable.addColumn("ITEM_PRODUCT_NUMBER", "");
        quoteProductTable.addColumn(ITEM_NAME, "");
        quoteProductTable.addColumn(DESCRIPTION, "");
        quoteProductTable.addColumn(QTY, "");
        quoteProductTable.addColumn(ITEM_UNIT_MEASUREMENT, "");
        quoteProductTable.addColumn(ITEM_UNIT_PRICE, "");
        quoteProductTable.addColumn(ITEM_TAX_RATE, "");
        quoteProductTable.addColumn(ITEM_TAX_AMOUNT, "");
        quoteProductTable.addColumn(ITEM_NET_AMOUNT, "");
        quoteProductTable.addColumn(ITEM_TOTAL_AMOUNT, "");

        if (quote == null) {
            return quoteProductTable;
        }

        int count = 0;
        for (EdsQuoteItem item : quote.getQuoteItems()) {
            count = count + 1;
            String no = count + ".";
            String itemNumber = item.getItem() != null ? item.getItem().getProductNumber() : "";
            String itemName = item.getItem() != null ? escapeHtml(item.getItem().getName()) : "";
            String description = escapeHtml(item.getDescription());
            String qty = item.getQty() != null ? qtyNumberFormat.format(item.getQty()) : "";
            String uom = item.getUnitMeasurement() != null ? escapeHtml(item.getUnitMeasurement().getName()) : "";
            String taxRate = item.getVat() != null ? qtyNumberFormat.format(item.getVat().getTaxRate()) : "";

            BigDecimal taxAmountBigDecimal = item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO;
            BigDecimal unitPriceBigDecimal = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;

            String taxAmount = qtyNumberFormat.format(taxAmountBigDecimal);
            String unitPrice = qtyNumberFormat.format(unitPriceBigDecimal);

            BigDecimal netAmountBigDecimal = item.getQty().multiply(unitPriceBigDecimal);
            BigDecimal totalAmountBigDecimal = netAmountBigDecimal.add(taxAmountBigDecimal);

            String netAmount = qtyNumberFormat.format(netAmountBigDecimal);
            String totalAmount = qtyNumberFormat.format(totalAmountBigDecimal);

            quoteProductTable.addRow(no, itemNumber, itemName, description, qty, uom, unitPrice, taxRate, taxAmount, netAmount, totalAmount);
        }

        return quoteProductTable;
    }

    private CustomisedITextTable getProductBatchItems(EdsQuote quote, EdsUser user) {
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(user.getCompany(), null);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        CustomisedITextTable batchItemsTable = new CustomisedITextTable();

        batchItemsTable.addColumn("ITEM_NUMBER", "");
        batchItemsTable.addColumn(ITEM_NAME, "");
        batchItemsTable.addColumn(ITEM_SERIAL_NUMBER, "");
        batchItemsTable.addColumn(ITEM_EXPIRATION_DATE, "");
        batchItemsTable.addColumn(QTY, "");

        if (quote == null) {
            return batchItemsTable;
        }

        for (EdsQuoteItem item : quote.getQuoteItems()) {
            List<ProductTrackBatchItem> batchItems = itemBatchService.getBatchItemsOfGrnOrGdn(item.getObjectID(), item.getItem().getObjectID(), quote.getObjectID(), ItemSerialEntityType.GOODS_DELIVERED.name());
            for (ProductTrackBatchItem batchItem : batchItems) {
                String itemNumber = item.getItem() != null ? item.getItem().getProductNumber() : "";
                String itemName = item.getItem() != null ? escapeHtml(item.getItem().getName()) : "";
                String serialNumber = batchItem.getSerial();
                String expDate = batchItem.getExpirationDate() != null ? shortDateFormat.format(batchItem.getExpirationDate()) : "";
                String qty = batchItem.getQty() != null ? qtyNumberFormat.format(batchItem.getQty()) : "";

                batchItemsTable.addRow(itemNumber, itemName, serialNumber, expDate, qty);
            }
        }

        return batchItemsTable;
    }

    @Override
    protected String getTableName(Object dataClass) {
        String tableName = "";
        if ("67497".equalsIgnoreCase(SecurityContext.getInstance().getCompanyId())) {
            tableName = "Delivery Note";
        } else {
            tableName = "Pick List";
        }
        return tableName;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof InvoiceQuoteRequestObject) {
            return ((InvoiceQuoteRequestObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PICK_LIST_VIEW;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Pick List" + "_" + dateFormat(new Date()));
    }
}
