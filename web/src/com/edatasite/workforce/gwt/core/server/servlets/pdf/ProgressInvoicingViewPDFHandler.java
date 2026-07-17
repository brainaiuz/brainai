package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ar;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PDFProgressInvoiceTransferObject;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Azam on 02/20/20.
 */
public class ProgressInvoicingViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private ProjectManager projectManager;

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        try {
            return getInvoiceData(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        int scale = 2;
        if (fs != null && fs.getCalculationScale() != null) {
            scale = fs.getCalculationScale();
        }

        NewInvoice invoiceData = (NewInvoice) dataClass;

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable clientTable = new CustomisedITextTable();
        clientTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);

        EdsCrmAccount client = clientManager.get(invoiceData.getClientID());
        EdsCrmContact clientContact = null;
        if (invoiceData.getClientContactID() != null) {
            clientContact = crmContactManager.get(invoiceData.getClientContactID());
        } else {
            clientContact = clientContactManager.getPrimaryClientContact(invoiceData.getClientID());
        }
        if (clientContact != null && !ServerUtils.equalsEdsObject(client, clientContact.getCrmAccount())) {
            clientContact = clientContactManager.getPrimaryClientContact(invoiceData.getClientID());
        }
        EdsAddress billAddress = invoiceData.getBillAddressID() != null ? addressManager.get(invoiceData.getBillAddressID()) : null;
        EdsAddress mailAddress = invoiceData.getMailAddressID() != null ? addressManager.get(invoiceData.getMailAddressID()) : null;

        String clientName = client != null ? escapeHtml(client.getName()) : "";
        String contactName = clientContact != null ? escapeHtml(clientContact.getFullName()) : "";
        String billAdresses = billAddress != null ? escapeHtml(billAddress.getAddressDataAsHTML()) : "";
        String mailAdresses = mailAddress != null ? escapeHtml(mailAddress.getAddressDataAsHTML()) : "";
        String progressInvoicingType = escapeHtml(invoiceData.getType());

        clientTable.addRowWithCode("CLIENT_NAME", commonLocalizer.localize(PdfLocalizationName.clientName), clientName);
        clientTable.addRowWithCode("CONTACT_NAME", commonLocalizer.localize(PdfLocalizationName.contact), contactName);
        clientTable.addRowWithCode("BILL_ADDRESSES", commonLocalizer.localize(PdfLocalizationName.address), billAdresses);
        clientTable.addRowWithCode("MAIL_ADDRESSES", commonLocalizer.localize(PdfLocalizationName.address), mailAdresses);
        clientTable.addRowWithCode("INVOICE_TYPE", "Invoice Type", progressInvoicingType);
        clientTable.addRowWithCode("INSTALLMENTS", commonLocalizer.localize("installments"), "");
        clientTable.addRowWithCode("AMOUNT", commonLocalizer.localize("amount"), "");

        customData.put("CLIENT_TABLE", clientTable);
        customData.put("PROJECT_TABLE", getProjectData(invoiceData.getRelatedProjectID()));

        CustomisedITextTable productTableData = new CustomisedITextTable();
        productTableData.addColumnOrder(ITEM_NO, INV_DATE, INV_DUE_DATE, ITEM_NAME, ITEM_DESCRIPTION, PERCENTAGE, SUBTOTAL, TOTAL, "TOTAL_INVOICE_CURRENCY", PROJECT_NAME, ITEM_QTY, ITEM_UNIT_PRICE);
        productTableData.addHeaderColumns(
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number),
                commonLocalizer.localize(PdfLocalizationName.startDate),
                commonLocalizer.localize(PdfLocalizationName.endDate),
                commonLocalizer.localize("item"),
                commonLocalizer.localize(PdfLocalizationName.description),
                commonLocalizer.localize("percentage"),
                commonLocalizer.localize(PdfLocalizationName.subtotal),
                commonLocalizer.localize(PdfLocalizationName.total),
                "Total Invoice Currency",
                commonLocalizer.localize(PdfLocalizationName.project),
                "Quote Quantity",
                "Quote Unit Price"
        );

        BigDecimal totalSum = BigDecimal.ZERO;
        BigDecimal subTotalSum = BigDecimal.ZERO;
        BigDecimal totalInvCurSum = BigDecimal.ZERO;
        BigDecimal totalQuoteQuantity = BigDecimal.ZERO;

        for (NewInvoice item : invoiceData.getInvoicedItems()) {
            totalSum = totalSum.add(item.getTotal());
            subTotalSum = subTotalSum.add(item.getSubtotal());
            totalInvCurSum = totalInvCurSum.add(item.getTotalInInvoiceCurrency());
            totalQuoteQuantity = totalQuoteQuantity.add(item.getQuantity());
        }

        int count = 0;
        for (NewInvoice item : invoiceData.getInvoicedItems()) {
            count = count + 1;
            String invoiceDate = item.getInvoiceDateAsString();
            String dueDate = item.getDueDateAsString();
            String itemName = item.getName();
            String description = item.getDescription();
            String percentage = getMoneyFormat(item.getPercentage(), scale);
            String subtotal = getMoneyFormat(item.getSubtotal(), scale);
            String total = getMoneyFormat(item.getTotal(), scale);
            String totalInvoiceCurrency = getMoneyFormat(item.getTotalInInvoiceCurrency(), scale);
            String projectName = item.getProjectName();
            String quoteUnitPrice = getMoneyFormat(item.getUnitPrice(), scale);

            productTableData.addRow(count + "", invoiceDate, dueDate, itemName, description, percentage, subtotal, total, totalInvoiceCurrency, projectName, getMoneyFormat(totalQuoteQuantity), quoteUnitPrice);
        }

        customData.put("PRODUCT_TABLE", productTableData);

        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        NumberToWord numberToWordConverter;
        if (company.getLocale() != null && "ru".equals(company.getLocale())) {
            numberToWordConverter = new NumberToWord_ru();
        } else {
            numberToWordConverter = new NumberToWord_en();
        }

        NumberToWord numberToWordConverterArabic = null;
        if (isArabicCompany(company)) {
            numberToWordConverterArabic = new NumberToWord_ar();
            totalTable.addRowWithCode("TOTAL_ARABIC","Total arabic", numberToWordConverterArabic.convert(invoiceData.getNetAmountTotal().abs().setScale(getCalculationScale(),BigDecimal.ROUND_HALF_UP)));
        }

        totalTable.addRowWithCode("TOTAL", "Total", getMoneyFormat(totalSum, scale));
        totalTable.addRowWithCode("SUB_TOTAL", "Sub Total", getMoneyFormat(subTotalSum, scale));
        totalTable.addRowWithCode("TOTAL_INVOICE_CURRENCY", "Total Inv Cur", getMoneyFormat(totalInvCurSum, scale));
        totalTable.addRowWithCode("QUOTE_TOTAL", "Total Net Amount", getMoneyFormat(invoiceData.getNetAmountTotal()));
        totalTable.addRowWithCode("TOTAL_IN_WORD","Total words", numberToWordConverter.convert(invoiceData.getNetAmountTotal().abs().setScale(getCalculationScale(),BigDecimal.ROUND_HALF_UP)));

        customData.put("TOTAL_TABLE", totalTable);

        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getProjectData(Integer projectId) {
        CustomisedITextTable projectTable = new CustomisedITextTable();
        if (projectId == null) {
            return null;
        }
        EdsProject project = projectManager.get(projectId);
        if (project == null) {
            return null;
        }
        String projectName = escapeHtml(project.getName());
        String projectNumber = escapeHtml(project.getNumber());

        projectTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        projectTable.addRowWithCode(PROJECT_NAME, commonLocalizer.localize(PdfLocalizationName.projectName), projectName);
        projectTable.addRowWithCode(PROJECT_NUMBER, commonLocalizer.localize(PdfLocalizationName.projectNumber), projectNumber);
        projectTable.setCustomFields(getProjectCustomFields(project));

        return projectTable;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getProjectCustomFields(EdsProject project) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();

        if (project.getProjectCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), commonService.getCompanyCustomFields(ViewName.Project));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : "");
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            String dateValue = "";
                            EdsCompany company = userManager.getUser().getCompany();
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                            if (item.getFieldDateNonConvertedValue() != null) {
                                if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                    Locale ruLocale = new Locale("ru", "RU");
                                    SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                    dateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                                } else {
                                    dateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                                }
                                cols.put(COLUMN_VALUE, dateValue);
                            }
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "");
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put("PROJECT", itemCusFields);
            }
        }
        return customFields;
    }

    public NewInvoice getInvoiceData(HttpServletRequest request) throws UnsupportedEncodingException {
        NewInvoice newInvoiceData = new NewInvoice();

        NewInvoice[] invoiceDataItems = new NewInvoice[Integer.parseInt(request.getParameter(PDFProgressInvoiceTransferObject.LENGTH))];
        for (int i = 0; i < invoiceDataItems.length; i++) {
            invoiceDataItems[i] = new NewInvoice();
            String invoiceDate = request.getParameter(PDFProgressInvoiceTransferObject.INVOICE_DATE + i);
            String dueDate = request.getParameter(PDFProgressInvoiceTransferObject.DUE_DATE + i);
            String itemName = request.getParameter(PDFProgressInvoiceTransferObject.ITEM_NAME + i);
            String description = request.getParameter(PDFProgressInvoiceTransferObject.DESCRIPTION + i);
            String qty = request.getParameter(PDFProgressInvoiceTransferObject.QTY + i);
            String unitPrice = request.getParameter(PDFProgressInvoiceTransferObject.UNIT_PRICE + i);
            String percentage = request.getParameter(PDFProgressInvoiceTransferObject.PERCENTAGE + i);
            String subtotal = request.getParameter(PDFProgressInvoiceTransferObject.SUBTOTAL + i);
            String projectName = request.getParameter(PDFProgressInvoiceTransferObject.PROJECT_NAME + i);
            String total = request.getParameter(PDFProgressInvoiceTransferObject.TOTAL + i);
            String totalInvoiceCurrency = request.getParameter(PDFProgressInvoiceTransferObject.TOTAL_AMOUNT + i);

            invoiceDataItems[i].setInvoiceDateAsString(invoiceDate);
            invoiceDataItems[i].setDueDateAsString(dueDate);
            invoiceDataItems[i].setName(itemName);
            invoiceDataItems[i].setDescription(description);
            invoiceDataItems[i].setQuantity(new BigDecimal(qty));
            invoiceDataItems[i].setUnitPrice(new BigDecimal(unitPrice));
            invoiceDataItems[i].setPercentage(new BigDecimal(percentage));
            invoiceDataItems[i].setSubtotal(new BigDecimal(subtotal));
            invoiceDataItems[i].setProjectName(projectName);
            invoiceDataItems[i].setTotal(new BigDecimal(total));
            invoiceDataItems[i].setTotalInInvoiceCurrency(new BigDecimal(totalInvoiceCurrency));
        }

        newInvoiceData.setInvoicedItems(invoiceDataItems);
        if (request.getParameter(PDFProgressInvoiceTransferObject.BILL_ADDRESS_ID) != null) {
            newInvoiceData.setBillAddressID(Integer.parseInt(request.getParameter(PDFProgressInvoiceTransferObject.BILL_ADDRESS_ID)));
        }
        if (request.getParameter(PDFProgressInvoiceTransferObject.MAIL_ADDRESS_ID) != null) {
            newInvoiceData.setMailAddressID(Integer.parseInt(request.getParameter(PDFProgressInvoiceTransferObject.MAIL_ADDRESS_ID)));
        }
        if (request.getParameter(PDFProgressInvoiceTransferObject.CLIENT_ID) != null) {
            newInvoiceData.setClientID(Integer.parseInt(request.getParameter(PDFProgressInvoiceTransferObject.CLIENT_ID)));
        }
        if (request.getParameter(PDFProgressInvoiceTransferObject.CLIENT_CONTACT_ID) != null) {
            newInvoiceData.setClientContactID(Integer.parseInt(request.getParameter(PDFProgressInvoiceTransferObject.CLIENT_CONTACT_ID)));
        }
        if (request.getParameter(PDFProgressInvoiceTransferObject.PROGRESS_INVOICING_TYPE) != null) {
            newInvoiceData.setType(request.getParameter(PDFProgressInvoiceTransferObject.PROGRESS_INVOICING_TYPE));
        }
        if (request.getParameter(PDFProgressInvoiceTransferObject.QOUTE_TOTAL) != null) {
            newInvoiceData.setNetAmountTotal(new BigDecimal(request.getParameter(PDFProgressInvoiceTransferObject.QOUTE_TOTAL)));
        }
        if (request.getParameter(PDFProgressInvoiceTransferObject.QUOTE_NUMBER) != null) {
            newInvoiceData.setQuoteNumber(request.getParameter(PDFProgressInvoiceTransferObject.QUOTE_NUMBER));
        }
        if (!ServerUtils.isNullOrEmpty(request.getParameter(PDFProgressInvoiceTransferObject.PDF_TEMPLATE_ID))) {
            newInvoiceData.setPdfTemplateID(Integer.valueOf(request.getParameter(PDFProgressInvoiceTransferObject.PDF_TEMPLATE_ID)));
        }
        if (request.getParameter(PDFProgressInvoiceTransferObject.PROJECT_ID) != null) {
            newInvoiceData.setRelatedProjectID(Integer.parseInt(request.getParameter(PDFProgressInvoiceTransferObject.PROJECT_ID)));
        }

        return newInvoiceData;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PROGRESS_INVOICING_VIEW;
    }

    @Override
    protected String getTableName(Object dataClass) {
        NewInvoice invoiceData = (NewInvoice) dataClass;

        return commonLocalizer.localize("progressInvoicing") + " - " + escapeHtml(invoiceData.getQuoteNumber());
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        NewInvoice invoiceData = (NewInvoice) dataClass;

        setFileName(commonLocalizer.localize("progressInvoicing") + " - " + escapeHtml(invoiceData.getQuoteNumber()));
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof NewInvoice) {
            return ((NewInvoice) object).getPdfTemplateID();
        }
        return null;
    }
    public boolean isArabicCompany(EdsCompany company) {
        if (company.getCountryZone() != null && company.getCountryZone().getCountry() != null) {
            return ("AE".equals(company.getCountryZone().getCountry().getCode())
                    || "SA".equals(company.getCountryZone().getCountry().getCode())
                    || "OM".equals(company.getCountryZone().getCountry().getCode())
                    || "QA".equals(company.getCountryZone().getCountry().getCode()));
        }
        return false;
    }
}
