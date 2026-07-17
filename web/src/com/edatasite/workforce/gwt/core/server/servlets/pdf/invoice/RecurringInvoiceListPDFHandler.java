package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 28.05.2010
 * Time: 18:46:31
 * To change this template use File | Settings | File Templates.
 */
public class RecurringInvoiceListPDFHandler extends AbstractITextPostPdfHandler {

    private InvoiceCircularResolver invoiceCircularResolver;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Autowired
    @Qualifier("invoiceCircularResolver")
    public void setInvoiceCircularResolver(InvoiceCircularResolver invoiceCircularResolver) {
        this.invoiceCircularResolver = invoiceCircularResolver;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("recurringInvoices");
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        //ITextTableList tableList = new ITextTableList(7);
        //pdfData.setListTable(tableList);
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }
        EdsUser user = userManager.getUser();
        EdsFinancialSettings fs=  financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();

        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParameters.setLimit(MAX_PDF_OR_EXCEL_LIMIT);
        }

        ListResult<RecurringInvoiceListItem> invoiceList = invoiceCircularResolver.getRecurringInvoiceData(filterParameters);
        List<RecurringInvoiceListItem> invoiceData = invoiceList.getList();
        List<RecurringInvoiceListItem> invoiceDatas = ListUtils.getSublist(invoiceData, filterParameters.getStart(), filterParameters.getLimit());

//        DecimalFormat numberFormat = new DecimalFormat("#,##0.00");
//        pdfData.setTableName(getTitle());
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(RecurringInvoiceListItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(RecurringInvoiceListItem.AMOUNT, new CellData(accountingLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.BASE_AMOUNT, new CellData(accountingLocalizer.localize(PdfLocalizationName.amount) + "(" + currency + ")", Element.ALIGN_RIGHT));
        mapColumnHeader.put(RecurringInvoiceListItem.CLIENT, new CellData(accountingLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.REPEATS, new CellData(commonLocalizer.localize(PdfLocalizationName.repeats), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.NEXT_IVOICE_DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.nextInvoiceDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.endDateField), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.RECURRENCE_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.recurrenceStatus), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.REFERENCE, new CellData(accountingLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_RIGHT));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[0]));
        String status = "";
        for (RecurringInvoiceListItem recurring : invoiceDatas) {
            LinkedList<CellData> rowList = new LinkedList<>();
            Integer[] tempAligment = new Integer[header.size()];
            String[] temp = new String[header.size()];
            for (int j = 0; j < header.size(); j++) {
                tempAligment[j] = Element.ALIGN_LEFT;
                if (RecurringInvoiceListItem.AMOUNT.equals(header.get(j))) {
                    temp[j] = recurring.getAmountInInvoiceCurrency() == null ? "—" : getMoneyFormat(recurring.getAmountInInvoiceCurrency());
                    rowList.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }else if (RecurringInvoiceListItem.BASE_AMOUNT.equals(header.get(j))) {
                    temp[j] = recurring.getAmount() == null ? "—" : getMoneyFormat(recurring.getAmount());
                    rowList.add(new CellData(temp[j], Element.ALIGN_RIGHT));
                }else if (RecurringInvoiceListItem.REPEATS.equals(header.get(j))) {
                    temp[j] = recurring.getRepeats() == null ? "—" : recurring.getRepeats();
                    rowList.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }  else if (RecurringInvoiceListItem.CLIENT.equals(header.get(j))) {
                    temp[j] = recurring.getClient() == null ? "—" : recurring.getClient();
                    rowList.add(new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (RecurringInvoiceListItem.NEXT_IVOICE_DATE.equals(header.get(j))) {
                    temp[j] = recurring.getNextInvoiceDate() == null ? "—" : recurring.getNextInvoiceDate().toString();
                    rowList.add(new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (RecurringInvoiceListItem.END_DATE.equals(header.get(j))) {
                    temp[j] = recurring.getEndDate() == null ? "—" : dateFormat(recurring.getEndDate());
                    rowList.add(new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (RecurringInvoiceListItem.STATUS.equals(header.get(j))) {
                    if (DRAFT.equals(recurring.getStatusCode())) {
                        status = commonLocalizer.localize(PdfLocalizationName.draft);
                    } else if (APPROVE.equals(recurring.getStatusCode())) {
                        status = commonLocalizer.localize(PdfLocalizationName.approved);
                    } else if (OPEN.equals(recurring.getStatusCode())) {
                        status = commonLocalizer.localize(PdfLocalizationName.sent);
                    }
                    temp[j] = accountingLocalizer.localizeWithParam(PdfLocalizationName.invoiceWillBe, status);
                    rowList.add(new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (RecurringInvoiceListItem.RECURRENCE_STATUS.equals(header.get(j))) {
                    temp[j] = recurring.getRecurrenceStatus() == null ? "—" : recurring.getRecurrenceStatus();
                    rowList.add(new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (RecurringInvoiceListItem.REFERENCE.equals(header.get(j))) {
                    temp[j] = recurring.getReference() == null ? "-" : recurring.getReference();
                    rowList.add(new CellData(temp[j], Element.ALIGN_RIGHT));
                }
            }
            tableList.addPdfTableRows(rowList.toArray(new CellData[]{}));
        }
        pdfData.setListTable(tableList);

        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(getFileName() + "_" + dateFormat(user.getUserDate()));
    }

    public String getFileName() {
        return "Recurring_Invoices_List";
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    protected String getTitle() {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.recurringInvoicesList);
    }
}
