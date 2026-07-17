package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SaleQuotesListExcelHandler extends BaseExcelHandler {
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
    private static final Logger log = LoggerFactory.getLogger(SaleQuotesListExcelHandler.class);
    @Override
    protected void setFileName() {
        filename = "Sales Quotes";
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

        InvoiceList invoiceList = quoteService.getSaleQuoteData(filterParameters);
        List<NewInvoice> quotes = invoiceList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();

        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.salesQuote);
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();

            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.Qnumber, "Number"), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.date), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.validDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.MANAGER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.manager), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CURRENCY, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.STATUS, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.SUB_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.subtotal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.BASE_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.total) + "(" + currency + ")", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.taxTotal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.PO_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.poNumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.REFERENCE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.OPPORTUNITY_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.opportunity, "Opportunity"), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMapWithoutBorder(panelTools.getListViewCustomFields(), mapColumnHeader);

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            Integer calculationScale = getCalculationScale(fs);

            for (NewInvoice item : quotes) {
                String number = "", client = "", status = "";
                String creator = item.getCreator() != null ? item.getCreator().getName() : "";
                String manager = item.getCurrentApproverSelectItem() != null ? item.getCurrentApproverSelectItem().getName() : "";
                if (item.getInvoiceNumber() != null) {
                    number = item.getInvoiceNumber();
                }
                if (item.getClientName() != null) {
                    client = item.getClientName();
                }

                BigDecimal amount = item.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP);
                BigDecimal subTotal = item.getSubtotal().setScale(calculationScale, RoundingMode.HALF_UP);
                BigDecimal taxTotal = item.getTotalTaxes().setScale(calculationScale, RoundingMode.HALF_UP);

                if (Constants.APPROVE.equals(item.getStatusCode())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.approvedByManager);
                } else if (Constants.MANAGER_REJECT.equals(item.getStatusCode())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.rejectedByManager);
                } else if (Constants.CLIENT_APPROVE.equals(item.getStatusCode())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.approvedByClient);
                } else if (Constants.REJECT.equals(item.getStatusCode())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.rejectedByClient);
                } else if (Constants.SUBMITTED_TO_MANAGER.equals(item.getStatusCode())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.waitingForManagerApprove);
                } else {
                    status = item.getStatus();
                }

                ExcelData[] cellData;
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_NUMBER)) {
                    mapColumn.put(InvoiceList.INVOICE_NUMBER, new ExcelData(number, ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_DATE)) {
                    String invoiceDate = ServerUtils.dateFormat(item.getInvoiceDate() != null ? item.getInvoiceDate().getNonConvertedDate() : null, shortDateFormat);
                    mapColumn.put(InvoiceList.INVOICE_DATE, new ExcelData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(invoiceDate) : invoiceDate,
                            ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_DATE)) {
                    String dueDate = ServerUtils.dateFormat(item.getDueDate() != null ? item.getDueDate().getNonConvertedDate() : null, shortDateFormat);
                    mapColumn.put(InvoiceList.DUE_DATE, new ExcelData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dueDate) : dueDate,
                            ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.RELATED_PROJECT)) {
                    mapColumn.put(InvoiceList.RELATED_PROJECT, new ExcelData(item.getRelatedProjectName() != null ? item.getRelatedProjectName() : "N/A",
                            ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT)) {
                    mapColumn.put(InvoiceList.CLIENT, new ExcelData(client, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR)) {
                    mapColumn.put(InvoiceList.CREATOR, new ExcelData(creator, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.MANAGER)) {
                    mapColumn.put(InvoiceList.MANAGER, new ExcelData(manager, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CURRENCY)) {
                    mapColumn.put(InvoiceList.CURRENCY, new ExcelData(item.getCurrencyName(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_AMOUNT)) {
                    mapColumn.put(InvoiceList.DUE_AMOUNT, new ExcelData(amount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.STATUS)) {
                    mapColumn.put(InvoiceList.STATUS, new ExcelData(status, ExcelData.STRING, 22, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.SUB_TOTAL)) {
                    mapColumn.put(InvoiceList.SUB_TOTAL, new ExcelData(subTotal, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.BASE_TOTAL)) {
                    mapColumn.put(InvoiceList.BASE_TOTAL, new ExcelData(item.getTotal().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.TAX_TOTAL)) {
                    mapColumn.put(InvoiceList.TAX_TOTAL, new ExcelData(taxTotal, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PO_NUMBER)) {
                    mapColumn.put(InvoiceList.PO_NUMBER, new ExcelData(item.getPoNumber(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.REFERENCE)) {
                    mapColumn.put(InvoiceList.REFERENCE, new ExcelData(item.getReference(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY_NUMBER)) {
                    mapColumn.put(InvoiceList.OPPORTUNITY_NUMBER, new ExcelData(item.getOpportunityNumber() != null ? item.getOpportunityNumber() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, company);
                excelDataList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellData = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellData);

                list.add(cellData);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate sales quote list excel report, exception: " + e.getMessage());
        }
        return null;
    }


}
