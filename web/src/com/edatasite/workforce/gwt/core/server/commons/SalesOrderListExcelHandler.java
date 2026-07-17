package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 29.09.2010
 * Time: 19:56:56
 * To change this template use File | Settings | File Templates.
 */

public class SalesOrderListExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(SaleQuotesListExcelHandler.class);
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PropertManager propertManager;
    private String sheetname;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    private static final DecimalFormat numberFormat = new DecimalFormat("#,##0.00");
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    /*  protected Object getDataClass(HttpServletRequest request) {
            return new ListingFilterParameter();
        }
    */
    @Override
    protected void setFileName() {
        filename = "Sale Orders";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        String shortDateFormat = "MMM dd, yyyy";
        EdsUser user = userManager.getUser();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();

        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }

        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParameters.setLimit(LIMIT_EXCEL_ROW);
        }

        InvoiceList invoiceList = quoteService.getSaleOrderData(filterParameters);
        List<NewInvoice> quotes = invoiceList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.salesOrders);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.Qnumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.typeDate, ""), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.validDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CURRENCY, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.STATUS, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.PO_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.poNumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.SUB_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.subtotal), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.BASE_TOTAL, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.total) + "(" + currency + ")", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.taxTotal), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.REFERENCE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.OPPORTUNITY_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.opportunity), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.REMAINING_BALANCE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.remainingBalance), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.NET_AMOUNT_TOTAL, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.netAmount), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            List<ExcelData> excellDatasList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excellDatasList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excellDatasList.size()];
            excellDatasList.toArray(cellDatas);
            list.add(cellDatas);

            Integer calculationScale = getCalculationScale(fs);

            for (NewInvoice item : quotes) {
                String number = "";
                String client = "";
                String status = "";
                if (item.getInvoiceNumber() != null) {
                    number = item.getInvoiceNumber();
                }
                if (item.getClientName() != null) {
                    client = item.getClientName();
                }
                BigDecimal amount = item.getTotalInInvoiceCurrency().setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
                if (item.getStatus() != null) {
                    status = item.getStatus();
                }
                ExcelData[] cellData;
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_NUMBER)) {
                    mapColumn.put(InvoiceList.INVOICE_NUMBER, new ExcelData(number, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_DATE)) {
                    mapColumn.put(InvoiceList.INVOICE_DATE, new ExcelData(ServerUtils.dateFormat(item.getInvoiceDate() != null ? item.getInvoiceDate().getNonConvertedDate() : null, shortDateFormat),
                            ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_DATE))
                    mapColumn.put(InvoiceList.DUE_DATE, new ExcelData(ServerUtils.dateFormat(item.getDueDate() != null ? item.getDueDate().getNonConvertedDate() : null, shortDateFormat),
                            ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                if (panelTools.getColumnCodeName().contains(InvoiceList.RELATED_PROJECT)) {
                    mapColumn.put(InvoiceList.RELATED_PROJECT, new ExcelData(item.getRelatedProjectName() != null ? item.getRelatedProjectName() : "N/A",
                            ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT)) {
                    mapColumn.put(InvoiceList.CLIENT, new ExcelData(client, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CURRENCY)) {
                    mapColumn.put(InvoiceList.CURRENCY, new ExcelData(item.getCurrencyName() != null ? item.getCurrencyName() : "", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_AMOUNT)) {
                    mapColumn.put(InvoiceList.DUE_AMOUNT, new ExcelData(amount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.STATUS)) {
                    mapColumn.put(InvoiceList.STATUS, new ExcelData(status, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PO_NUMBER)) {
                    mapColumn.put(InvoiceList.PO_NUMBER, new ExcelData(item.getPoNumber() != null ? item.getPoNumber() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR)) {
                    mapColumn.put(InvoiceList.CREATOR, new ExcelData(item.getCreatorName() != null ? item.getCreatorName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.SUB_TOTAL)) {
                    mapColumn.put(InvoiceList.SUB_TOTAL, new ExcelData(item.getSubtotal(), ExcelData.CURRENCY, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.BASE_TOTAL)) {
                    mapColumn.put(InvoiceList.BASE_TOTAL, new ExcelData(item.getTotal(), ExcelData.CURRENCY, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.TAX_TOTAL)) {
                    mapColumn.put(InvoiceList.TAX_TOTAL, new ExcelData(item.getTotalTaxes() != null ? item.getTotalTaxes() : BigDecimal.ZERO, ExcelData.CURRENCY, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.REFERENCE)) {
                    mapColumn.put(InvoiceList.REFERENCE, new ExcelData(item.getReference() != null ? item.getReference() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY_NUMBER)) {
                    mapColumn.put(InvoiceList.OPPORTUNITY_NUMBER, new ExcelData(item.getOpportunityNumber() != null ? item.getOpportunityNumber() : item.getOpportunity(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.REMAINING_BALANCE)) {
                    mapColumn.put(InvoiceList.REMAINING_BALANCE, new ExcelData(item.isProgressInvoicing() && item.getInvoicedAmount() != null ? numberFormat.format(item.getTotalInInvoiceCurrency().subtract(item.getInvoicedAmount())) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.NET_AMOUNT_TOTAL)) {
                    mapColumn.put(InvoiceList.NET_AMOUNT_TOTAL, new ExcelData(item.getNetAmountTotal(), ExcelData.CURRENCY, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, company);
                excellDatasList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excellDatasList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellData = new ExcelData[excellDatasList.size()];
                excellDatasList.toArray(cellData);


                list.add(cellData);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate sales order list excel report, exception: " + e);
        }
        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
