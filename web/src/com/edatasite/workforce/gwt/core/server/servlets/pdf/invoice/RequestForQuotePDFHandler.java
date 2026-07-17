package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.ACCOUNTING.SQ_NUMBER;
import static com.edatasite.workforce.gwt.invoice.client.rpc.PDFTransferObject.INTRODUCTION;
import static com.edatasite.workforce.gwt.invoice.client.rpc.RFQData.REQUEST_FROM;
import static com.edatasite.workforce.gwt.invoice.client.rpc.RFQData.VALID_UNTIL;

/**
 * Created by Shohruh on 05-Feb-16.
 */
public class RequestForQuotePDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {
    private static final Color HEADER_BG_COLOR = new Color(255, 255, 255);
    private static final Color TABLE_HEADER_BG_COLOR = new Color(193, 198, 255);
    private static final Color BORDER_COLOR = new Color(51, 51, 51);
    private static final Integer KG_COMPANY = 31287;
    private static Date processDate;

    @Autowired
    protected QuoteService quoteService;
    @Autowired
    protected InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    protected RFQManager rfqManager;
    @Autowired
    private CrmAccountManager crmAccountManager;

    private DecimalFormat numberFormat;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        final CustomisedITextTable customFieldTable = new CustomisedITextTable();
        RFQData rfqData = null;
        if (requestObject.getObjectID() != null) {
            rfqData = quoteService.getRFQData(requestObject.getObjectID(), null);
        }

        customFieldTable.setCustomFields(getCustomFields(rfqData));
        customData.put("CUSTOM_FIELD", customFieldTable);
        baseInvoice.setCustomProductTable(getCustomProductTable(rfqData, company));
        baseInvoice.setCustomBillToAddress(getCustomBillToAddressTable(rfqData, company));
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getCustomProductTable(RFQData rfqData, EdsCompany company) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        numberFormat = getPriceScaleNumberFormat(fs);
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(company, null);
        CustomisedITextTable productsTable = new CustomisedITextTable();
        productsTable.addColumn(ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        productsTable.addColumn(ITEM_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        productsTable.addColumn(ProductsTable.QTY, commonLocalizer.localize(PdfLocalizationName.qty));
        productsTable.addColumn(ITEM_COMISSION, commonLocalizer.localize(PdfLocalizationName.commission));
        productsTable.addColumn(UNIT, commonLocalizer.localize(PdfLocalizationName.um));
        productsTable.addColumn(ITEM_COST_PRICE, commonLocalizer.localize(PdfLocalizationName.cost));
        productsTable.addColumn(SUPPLIER, commonLocalizer.localize(PdfLocalizationName.supplier));
        productsTable.addColumn(REMARKS, commonLocalizer.localize(PdfLocalizationName.remarks));
        productsTable.addColumn(ITEM_NO, pdfWfmMessageSource.localize(PdfLocalizationName.number));
        for (CompanyCustomFieldItem customFieldItem : rfqData.getItemCustomFields()) {
            productsTable.addColumn(customFieldItem.getFieldName(), customFieldItem.getFieldName());
        }
        int counter = 0;
        if (rfqData != null && rfqData.getItems() != null) {
            for (RFQItem item : rfqData.getItems()) {
                ArrayList<String> row = new ArrayList<>();
                ProductSelectItem product = item.getProduct();
                String name = product != null && product.getName() != null ? product.getName() : "";
                String description = item.getDescription() != null ? item.getDescription() : "";
                String qty = item.getQty() != null ? numberFormat.format(item.getQty()) : "";
                String comission = item.getCommission() != null ? numberFormat.format(item.getCommission()) : "";
                SelectItem measurement = item.getMeasurement();
                String um = measurement != null && measurement.getName() != null ? measurement.getName() : "";
                String cost = item.getUnitCost() != null ? numberFormat.format(item.getUnitCost()) : "";
                SelectItem supplier = item.getSupplier();
                String supplierName = supplier != null && supplier.getName() != null ? supplier.getName() : "";
                String remarks = item.getReMarks() != null ? item.getReMarks() : "";
                counter = counter + 1;
                row.add(escapeHtml(name));
                row.add(escapeHtml(description));
                row.add(escapeHtml(qty));
                row.add(escapeHtml(comission));
                row.add(escapeHtml(um));
                row.add(escapeHtml(cost));
                row.add(escapeHtml(supplierName));
                row.add(escapeHtml(remarks));
                row.add(counter + ".");
                if (item.getItemCustomFields() != null) {
                    for (CompanyCustomFieldItem customFieldItem : item.getItemCustomFields()) {
                        row.add(escapeHtml(customFieldItem.getFieldStringValue()));
                    }
                }
                productsTable.addRow(row.toArray(new String[]{}));
            }
        }
        return productsTable;
    }

    private CustomisedITextTable getCustomBillToAddressTable(RFQData rfqData, EdsCompany company) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        EdsUser user = uploadManager.getUser();
        if (rfqData == null) {
            return addressTable;
        }
        String dateAndTimeFormatShort2 = "MMM dd yyyy, HH:mm";

        addressTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        String requestFrom = commonLocalizer.localize(Constants.COMPANY_SUPPLIERS.equals(rfqData.getRequestFrom()) ? PdfLocalizationName.companySuppliers : PdfLocalizationName.directorySuppliers);
        String date = "";
        if (rfqData.getDate() != null) {
            date = ServerUtils.dateFormat(rfqData.getDate().getNonConvertedDate(), dateAndTimeFormatShort2);
        }
        String validUntil = "";
        if (rfqData.getValidUntil() != null) {
            validUntil = ServerUtils.shortDateFormat(rfqData.getValidUntil().getNonConvertedDate(), company);
        }
        String number = rfqData.getNumberData() != null ? escapeHtml(rfqData.getNumberData().getNumberString()) : "";
        String address = rfqData.getAddressAsString(false);
        String introduction = escapeHtml(rfqData.getIntroduction());
        String projectName = rfqData.getProject() != null ? escapeHtml(rfqData.getProject().getName()) : "";
        String sqNumber = escapeHtml(rfqData.getSqNumber());
        String customerName = rfqData.getCustomer() != null ? escapeHtml(rfqData.getCustomer().getName()) : "";
        String statusName = rfqData.getOverallStatus() != null ? escapeHtml(rfqData.getOverallStatus().getName()) : "";
        String creatorName = user != null ? escapeHtml(user.getFullName()) : "";
        String currentApproverName = rfqData.getCurrentApprover() != null && rfqData.getCurrentApprover().getExactEmployee() != null ? escapeHtml(rfqData.getCurrentApprover().getExactEmployee().getName()) : "";

        EdsCrmAccount customer = rfqData.getCustomer() != null ? crmAccountManager.get(rfqData.getCustomer().getId()) : null;
        String customerContact = customer != null && customer.getPrimaryContact() != null ? escapeHtml(customer.getPrimaryContact().getName()) : "";
        String customerPhone = customer != null ? escapeHtml(customer.getPhone()) : "";
        String customerEmail = customer != null ? escapeHtml(customer.getEmail()) : "";
        String paymentTerms = customer != null && customer.getTerms() != null ? escapeHtml(customer.getTerms().getName()) : "";

        addressTable.addRowWithCode(REQUEST_FROM, commonLocalizer.localize(PdfLocalizationName.requestFrom), escapeHtml(requestFrom));
        addressTable.addRowWithCode(DATE, commonLocalizer.localize("requestDate", "Request Date"), escapeHtml(date));
        addressTable.addRowWithCode(VALID_UNTIL, commonLocalizer.localize(PdfLocalizationName.dueDate), escapeHtml(validUntil));
        addressTable.addRowWithCode(NUMBER, commonLocalizer.localize("requestForQuote") + " #", escapeHtml(number));
        addressTable.addRowWithCode(COMPAN_ADDRESS, commonLocalizer.localize(PdfLocalizationName.addressLine1), escapeHtml(address));
        addressTable.addRowWithCode(INTRODUCTION, pdfWfmMessageSource.localize(PdfLocalizationName.introduction), escapeHtml(introduction));
        addressTable.addRowWithCode(PROJECT, pdfWfmMessageSource.localize(PdfLocalizationName.project), escapeHtml(projectName));
        addressTable.addRowWithCode(SQ_NUMBER, commonLocalizer.localize(PdfLocalizationName.quote) + " #", escapeHtml(sqNumber));
        addressTable.addRowWithCode(Constants.CUSTOMER, "", customerName);
        addressTable.addRowWithCode(STATUS, "", statusName);
        addressTable.addRowWithCode(CREATOR, "", creatorName);
        addressTable.addRowWithCode(CLIENT_CONTACT, "", customerContact);
        addressTable.addRowWithCode(PHONE, "", customerPhone);
        addressTable.addRowWithCode(EMAIL, "", customerEmail);
        addressTable.addRowWithCode(PAYMENT_TERMS, "", paymentTerms);
        addressTable.addRowWithCode(APPROVER, "", currentApproverName);
        EdsAddress billAddress = customer != null ? customer.getBillingAddress() : null;
        if (billAddress != null) {
            addressTable.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
            addressTable.addRowWithCode(BILL_ADDRESS, "", escapeHtml(billAddress.getAddress()));
            addressTable.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(billAddress.getAddressb()));
            addressTable.addRowWithCode(BILL_CITY, "", escapeHtml(billAddress.getCity()));
            addressTable.addRowWithCode(BILL_STATE, "", escapeHtml(billAddress.getStateName()));
            addressTable.addRowWithCode(BILL_COUNTRY, "", escapeHtml(billAddress.getCountryName()));
            addressTable.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(billAddress.getZipCode()));
        }

        return addressTable;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(RFQData rfqData) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();

        if (rfqData != null && rfqData.getCustomFieldList().size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : rfqData.getCustomFieldList()) {
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
                        itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                    }
                }
            }
            customFields.put("RFQ", itemCusFields);
        }
        return customFields;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject();
        String[] ids = !request.getParameter("ids").equals("") ? request.getParameter("ids").split(",") : new String[0];
        requestObject.setObjectID(Integer.valueOf(!request.getParameter("objectID").equals("") ? request.getParameter("objectID") : ids[0]));
        return requestObject;
    }

    private String getFileName(Object dataClass) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        if (requestObject != null && requestObject.getObjectID() != null) {
            EdsRFQ requestForQuote = rfqManager.get(requestObject.getObjectID());
            if (requestForQuote != null && requestForQuote.getNumber() != null) {
                return requestForQuote.getNumber();
            }
        }
        return "RFQ";
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(getFileName(dataClass));
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
        return PdfReferenceCodeNameEnum.RFQ;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localizeAccounting("requestForQuote");
    }
}
