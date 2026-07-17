package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RentalOrderViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private RentalOrderService rentalOrderService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private RentalOrderManager rentalOrderManager;
    @Autowired
    private ItemManager itemManager;

    final DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
    DecimalFormat priceScaleFormat;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        RentalOrderData rentalOrderData = rentalOrderService.getRentalOrderData(requestObject.getObjectID(), true);
        priceScaleFormat = getPriceScaleNumberFormat(financialSettingsManager.getFinancialSettings());
        baseInvoice.setCustomProductTable(getProductTable(rentalOrderData, priceScaleFormat));
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(rentalOrderData, requestObject.getObjectID()));
        baseInvoice.setCustomTotalTable(getTotalTable(rentalOrderData));
        customData.put("CREATION_DATA", getCreationData(rentalOrderData));
        customData.put("CUSTOM_FIELD", getCustomField(rentalOrderData));
        pdfData.setCustomData(customData);
        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    private CustomisedITextTable getCustomNumberAndDatesTable(RentalOrderData rentalOrderData, Integer objectId) {
        CustomisedITextTable viewTable = new CustomisedITextTable();
        EdsRentalOrder rentalOrder = rentalOrderManager.get(objectId);
        viewTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        if (rentalOrder.getCustomer() != null) {
            EdsCrmAccount customer = crmAccountManager.get(rentalOrder.getCustomer().getObjectID());
            if (customer != null) {
                viewTable.addRowWithCode("CUSTOMER", commonLocalizer.localize(PdfLocalizationName.customer), escapeHtml(customer != null ? customer.getName() : ""));
                viewTable.addRowWithCode("CUSTOMER_PHONE", commonLocalizer.localize(PdfLocalizationName.phone), escapeHtml(customer != null ? customer.getPhone() : ""));
                viewTable.addRowWithCode("CUSTOMER_EMAIL", commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(customer != null ? customer.getEmail() : ""));
            }
        }
        viewTable.addRowWithCode("NUMBER", commonLocalizer.localize(PdfLocalizationName.number), escapeHtml(rentalOrderData.getNumberData() != null ? rentalOrderData.getNumberData().getNumberString() : ""));
        viewTable.addRowWithCode("CLIENT_INVOICE_TERM", commonLocalizer.localize(PdfLocalizationName.paymentTerms), escapeHtml(rentalOrderData.getPaymentTerms() != null ? rentalOrderData.getPaymentTerms().getName() : ""));
        viewTable.addRowWithCode("START_DATE", commonLocalizer.localize(PdfLocalizationName.startDate), escapeHtml(rentalOrderData.getStartDate() != null ? rentalOrderData.getStartDate().toString() : ""));
        viewTable.addRowWithCode("DATE", commonLocalizer.localize(PdfLocalizationName.expirationDate), escapeHtml(rentalOrderData.getExpirationDate() != null ? rentalOrderData.getExpirationDate().toString() : ""));
        viewTable.addRowWithCode("TAX_CALC_TYPE", commonLocalizer.localize(PdfLocalizationName.amount), escapeHtml(rentalOrderData.getTaxCalculationType() != null ? rentalOrderData.getTaxCalculationType().toString() : "Tax Exclusive"));
        viewTable.addRowWithCode("STATUS", commonLocalizer.localize(PdfLocalizationName.status), escapeHtml(rentalOrderData.getStatus() != null ? rentalOrderData.getStatus().getCode() : ""));
        viewTable.addRowWithCode("ITEMS", commonLocalizer.localize(PdfLocalizationName.itemName), escapeHtml(rentalOrderData.getItemColumns() != null ? rentalOrderData.getItemColumns().toString() : ""));
        viewTable.addRowWithCode("ADDITIONAL_INFORMATION", pdfWfmMessageSource.localize("additionalInformation"), "");
        return viewTable;
    }

    private CustomisedITextTable getProductTable(RentalOrderData rentalOrderData, DecimalFormat priceScaleFormat) {
        CustomisedITextTable productTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> itemCusFields = new HashMap<>();
        productTable.addColumn("PRODUCT_FOR_RENT", pdfWfmMessageSource.localize(PdfLocalizationName.itemName));
        productTable.addColumn("PRODUCT", pdfWfmMessageSource.localize(PdfLocalizationName.itemName));
        productTable.addColumn(DESCRIPTION, pdfWfmMessageSource.localize(PdfLocalizationName.description));
        productTable.addColumn(QTY, pdfWfmMessageSource.localize(PdfLocalizationName.qty));
        productTable.addColumn(ITEM_UNIT_PRICE, pdfWfmMessageSource.localize(PdfLocalizationName.unitPrice));
        productTable.addColumn(ITEM_TAX_RATE, pdfWfmMessageSource.localize(PdfLocalizationName.taxRate));
        productTable.addColumn(ITEM_NET_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.netAmount));
        productTable.addColumn(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.totalAmount));
        productTable.addColumn(ITEM_BRAND_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.brand));
        productTable.addColumn(DURATION, pdfWfmMessageSource.localize(PdfLocalizationName.duration));

        int counter = 0;
        boolean isColumnsAdded = false;
        for (RentalOrderItem item : rentalOrderData.getRentalOrderItems()) {
            Integer rentalItemId = Optional.ofNullable(item.getRentalItem())
                    .map(ProductSelectItem::getId)
                    .orElse(null);
            EdsItem rentalItem = itemManager.get(rentalItemId);
            if (rentalItem == null) continue;
            List<String> values = new ArrayList<>(Arrays.asList(
                    escapeHtml(Optional.ofNullable(item.getRentalItem()).map(ProductSelectItem::getName).orElse("")),
                    escapeHtml(Optional.ofNullable(item.getProductItem()).map(ProductSelectItem::getName).orElse("")),
                    escapeHtml(Optional.ofNullable(item.getDescription()).orElse("")),
                    item.getQty() != null ? escapeHtml(priceScaleFormat.format(item.getQty())) : "",
                    item.getPrice() != null ? escapeHtml(priceScaleFormat.format(item.getPrice())) : "",
                    escapeHtml(Optional.ofNullable(item.getTaxItem()).map(TaxItem::getName).orElse("")),
                    item.getNetAmount() != null ? escapeHtml(priceScaleFormat.format(item.getNetAmount())) : "",
                    item.getSubTotal() != null ? escapeHtml(priceScaleFormat.format(item.getSubTotal())) : "",
                    rentalItem != null && rentalItem.getBrand() != null ? escapeHtml(rentalItem.getBrand().getName()) : "",
                    item.getDescription() != null && !item.getDescription().isEmpty() ? getDurationDays(item.getDescription()) : ""
            ));

            if (rentalItem.getCustomFields() != null) {
                ArrayList<CompanyCustomFieldItem> customFieldTemplates = commonService.getCompanyCustomFields(ViewName.RentalProductsView);
                ArrayList<CompanyCustomFieldItem> filledFields = CustomFieldsUtils.setRPCCustomFieldItems(rentalItem.getCustomFields(), customFieldTemplates);
                if (filledFields != null && !filledFields.isEmpty()) {
                    SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                    for (CompanyCustomFieldItem field : filledFields) {
                        if (field == null || field.getAliasName() == null) continue;
                        if (!isColumnsAdded) {
                            productTable.addColumn(field.getAliasName(), field.getAliasName());
                        }
                        String value = "";
                        if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                            if (field.getFieldDateNonConvertedValue() != null) {
                                value = escapeHtml(shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()));
                            }
                        } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                            if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                                value = escapeHtml(decimalFormat.format(Double.parseDouble(field.getFieldStringValue())));
                            }
                        } else {
                            if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                                value = escapeHtml(field.getFieldStringValue());
                            }
                        }
                        values.add(value);
                    }
                    isColumnsAdded = true;
                }
            }
            productTable.addRow(values.toArray(new String[0]));

            if (item.getProductItem() != null && itemManager.get(item.getProductItem().getId()) != null) {
                itemCusFields.put(String.valueOf(counter), getProductCustomFields(itemManager.get(item.getProductItem().getId()).getCustomFields()));
            }
            counter++;
        }

        productTable.setCustomFields(itemCusFields);
        return productTable;
    }

    public LinkedHashMap<String, Map<String, String>> getProductCustomFields(EdsItemCustomFields itemCustomFields) {
        ArrayList<CompanyCustomFieldItem> customFieldTemplates = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
        ArrayList<CompanyCustomFieldItem> filledFields = CustomFieldsUtils.setRPCCustomFieldItems(itemCustomFields, customFieldTemplates);
        LinkedHashMap<String, Map<String, String>> customFieldsMap = new LinkedHashMap<>();
        if (filledFields != null && !filledFields.isEmpty()) {
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            for (CompanyCustomFieldItem field : filledFields) {
                if (field == null || field.getAliasName() == null) continue;
                Map<String, String> fieldData = new HashMap<>();
                fieldData.put(COLUMN_NAME, escapeHtml(field.getAliasName()));
                String value = "";
                if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                    if (field.getFieldDateNonConvertedValue() != null) {
                        value = escapeHtml(shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()));
                    }
                } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                    if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                        value = escapeHtml(decimalFormat.format(Double.parseDouble(field.getFieldStringValue())));
                    }
                } else {
                    if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                        value = escapeHtml(field.getFieldStringValue());
                    }
                }
                fieldData.put(COLUMN_VALUE, value);
                customFieldsMap.put(field.getAliasName(), fieldData);
            }
        }
        return customFieldsMap;
    }

    private CustomisedITextTable getTotalTable(RentalOrderData rentalOrderData) {
        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        totalTable.addRowWithCode(SUBTOTAL, commonLocalizer.localize(PdfLocalizationName.subtotal), escapeHtml(rentalOrderData.getSubTotal() != null ? priceScaleFormat.format(rentalOrderData.getSubTotal()) : ""));
        totalTable.addRowWithCode(TOTAL, commonLocalizer.localize(PdfLocalizationName.total), escapeHtml(rentalOrderData.getTotal() != null ? priceScaleFormat.format(rentalOrderData.getTotal()) : ""));
        totalTable.addRowWithCode(TAX_TOTAL, commonLocalizer.localize(PdfLocalizationName.taxTotal), escapeHtml(rentalOrderData.getTaxAmount() != null ? priceScaleFormat.format(rentalOrderData.getTaxAmount()) : ""));
        totalTable.addRowWithCode(ITEM_BASE_TOTAL, commonLocalizer.localize(PdfLocalizationName.baseTotal));
        totalTable.addRowWithCode("NET_TOTAL", commonLocalizer.localize(PdfLocalizationName.netAmount));
        return totalTable;
    }

    private CustomisedITextTable getCustomField(RentalOrderData customFielditem) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (customFielditem.getCustomFieldItems() != null && customFielditem.getCustomFieldItems().size() > 0) {
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : customFielditem.getCustomFieldItems()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(field.getAliasName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, field.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate())) : "—");
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("RENTAL_ORDER", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }
        return customFieldTable;
    }

    private CustomisedITextTable getCreationData(RentalOrderData rentalOrderData) {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (rentalOrderData == null) {
            return customTable;
        }

        String creationDate = rentalOrderData.getCreatedDate() != null ? shortDateFormat.format(rentalOrderData.getCreatedDate()) : "";
        String modifiedDate = rentalOrderData.getUpdatedDate() != null ? shortDateFormat.format(rentalOrderData.getUpdatedDate()) : "";
        customTable.addRowWithCode("CREATED_DATE", "", creationDate);
        customTable.addRowWithCode("MODIFIED_DATE", "", modifiedDate);
        return customTable;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        if (StringUtils.isNotBlank(request.getParameter("userID"))) {
            requestObject.setUserID(Integer.valueOf(request.getParameter("userID")));
        }
        if (StringUtils.isNotBlank(request.getParameter("templateID"))) {
            requestObject.setTemplateID(Integer.valueOf(request.getParameter("templateID")));
        }
        if (StringUtils.isNotBlank(request.getParameter("IS_LANDSCAPE"))) {
            requestObject.setIS_LANDSCAPE(Boolean.valueOf(request.getParameter("IS_LANDSCAPE")));
        }
        return requestObject;
    }

    private String getDurationDays(String date) {
        String[] dates = date.split("->");
        if (dates.length == 2) {
            String fromDateStr = dates[0].trim();
            String toDateStr = dates[1].trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, formatter);
            LocalDateTime toDate = LocalDateTime.parse(toDateStr, formatter);
            return String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate));
        }
        return "";
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((InvoiceQuoteRequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "rentalOrder";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.RENTAL_ORDER;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object != null && object instanceof InvoiceQuoteRequestObject) {
            return ((InvoiceQuoteRequestObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("rentalOrder");
    }
}
