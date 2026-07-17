package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.ACCOUNTING.MANAGER;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.DUE_DATE;

/**
 * Created by Omonullo on 5/31/2017.
 */
public class RequestForPurchasePDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Autowired
    private QuoteService quoteService;
    @Autowired
    private RFPManager rfpManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        EdsRFP edsRFP = rfpManager.get(requestObject.getObjectID());
        RFPData rfpData = null;
        if (requestObject.getObjectID() != null) {
            RFPData filter = new RFPData();
            filter.setObjectID(requestObject.getObjectID());
            rfpData = quoteService.getRFPData(filter);
        }
        baseInvoice.setCustomProductTable(getCustomProductTable(rfpData, company));
        baseInvoice.setCustomBillToAddress(getCustomBillToAddressTable(rfpData, company));
        pdfData.setBaseInvoice(baseInvoice);
        customFieldTable.setCustomFields(getCustomFields(edsRFP));
        customData.put("CUSTOM_FIELD", customFieldTable);
        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getCustomProductTable(RFPData rfpData, EdsCompany company) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat numberFormat = getPriceScaleNumberFormat(fs);
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(company, null);

        CustomisedITextTable productsTable = new CustomisedITextTable();
        productsTable.addColumn(ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        productsTable.addColumn(PDFConstants.ITEM_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        productsTable.addColumn(PDFConstants.ITEM_QTY_ON_HAND, commonLocalizer.localize(PdfLocalizationName.onHand));
        productsTable.addColumn(PDFConstants.ITEM_WAREHOUSE, commonLocalizer.localize(PdfLocalizationName.warehouse));
        productsTable.addColumn(PDFConstants.ITEM_QTY, commonLocalizer.localize(PdfLocalizationName.qty));
        productsTable.addColumn(PDFConstants.ITEM_UNIT_MEASUREMENT, commonLocalizer.localize(PdfLocalizationName.measurementUnits));
        productsTable.addColumn(ITEM_NO, pdfWfmMessageSource.localize(PdfLocalizationName.number));
        for (CompanyCustomFieldItem customFieldItem : rfpData.getItemCustomFields()) {
            productsTable.addColumn(customFieldItem.getFieldName(), customFieldItem.getFieldName());
        }

        int counter = 0;
        if (rfpData != null && rfpData.getItems() != null) {
            for (RFPItem item : rfpData.getItems()) {
                ArrayList<String> row = new ArrayList<>();
                ProductSelectItem product = item.getProductItem();
                String name = product != null && product.getName() != null ? product.getName() : "";
                String description = item.getDescription() != null ? item.getDescription() : "";
                String qtyOnHand = item.getQtyOnhand() != null ? numberFormat.format(item.getQtyOnhand()) : "";
                SelectItem warehouse = item.getWareHouse();
                String warehouseName = warehouse != null && warehouse.getName() != null ? warehouse.getName() : "";
                String quantity = item.getQty() != null ? numberFormat.format(item.getQty()) : "";
                SelectItem measurement = item.getMeasurement();
                String measurementName = measurement != null && measurement.getName() != null ? measurement.getName() : "";
                counter = counter + 1;
                row.add(escapeHtml(name));
                row.add(escapeHtml(description));
                row.add(escapeHtml(qtyOnHand));
                row.add(escapeHtml(warehouseName));
                row.add(escapeHtml(quantity));
                row.add(escapeHtml(measurementName));
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

    private CustomisedITextTable getCustomBillToAddressTable(RFPData rfpData, EdsCompany company) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        addressTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        SelectItem creator = rfpData.getCreator();

        String dueDate = "";
        if (company.getLocale() != null && "ru".equals(company.getLocale())) {
            Locale ruLocale = new Locale("ru", "RU");
            SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
            dueDate = rfpData.getDueDate() != null ? ruDateFormat.format(rfpData.getDueDate()) : "";
        } else {
            dueDate = rfpData.getDueDate() != null ? shortDateFormat.format(rfpData.getDueDate()) : "";
        }
        addressTable.addRowWithCode(CREATOR, commonLocalizer.localize(PdfLocalizationName.creator), escapeHtml(creator != null && creator.getName() != null ? creator.getName() : ""));
        addressTable.addRowWithCode(DUE_DATE, commonLocalizer.localize(PdfLocalizationName.dueDate), escapeHtml(dueDate));
        SelectItem manager = rfpData.getCurrentApprover();
        addressTable.addRowWithCode(MANAGER, commonLocalizer.localize(PdfLocalizationName.manager), escapeHtml(manager != null && manager.getName() != null ? manager.getName() : ""));
        SelectItem project = rfpData.getRelatedProject();
        addressTable.addRowWithCode(PROJECT, pdfWfmMessageSource.localize(PdfLocalizationName.project), escapeHtml(project != null && project.getName() != null ? project.getName() : ""));
        String number = rfpData.getNumberData() != null && rfpData.getNumberData().getNumberString() != null ? rfpData.getNumberData().getNumberString() : "";
        addressTable.addRowWithCode(NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.rfp) + " #", escapeHtml(number));
        String customer = rfpData.getCustomer() != null ? escapeHtml(rfpData.getCustomer().getName()) : "";
        addressTable.addRowWithCode(Constants.CUSTOMER, commonLocalizer.localize(PdfLocalizationName.customer), customer);

        return addressTable;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    private String getFileName(Object dataClass) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        String number = requestObject.getRequestParams().get("number") != null && !"".equals(requestObject.getRequestParams().get("number")) ? requestObject.getRequestParams().get("number") : "RFP";
        String date = requestObject.getRequestParams().get("date") != null ? requestObject.getRequestParams().get("date") : "";
        return number + "_" + date;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsRFP rfp) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();

        if (rfp != null && rfp.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(rfp.getCustomFields(), commonService.getCompanyCustomFields(ViewName.RequestForPurchase));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            EdsCompany company = userManager.getUser().getCompany();
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                            String date = "";
                            if (true) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                date = item.getFieldDateNonConvertedValue() != null ? ruDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else {
                                date = item.getFieldDateNonConvertedValue() != null ? shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            }
                            cols.put(COLUMN_VALUE, date);
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put("RFP", itemCusFields);
            }
        }
        return customFields;
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
        return PdfReferenceCodeNameEnum.RFP;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize("requestForPurchase");
    }
}
