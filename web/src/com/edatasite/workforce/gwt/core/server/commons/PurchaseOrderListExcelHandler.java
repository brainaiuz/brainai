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
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PurchaseOrderListExcelHandler extends BaseExcelHandler {

    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private PropertManager propertManager;
    private String sheetname;

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderListExcelHandler.class);

    @Override
    protected void setFileName() {
        filename = "Purchase Orders";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = userManager.getUser();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();

        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        String shortDateFormat = Optional.ofNullable(companySettings.getShortDateFormat()).orElse("MMM dd, yyyy");

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

        InvoiceList invoiceList = quoteServiceLocal.getPurchaseOrderData(filterParameters);
        List<NewInvoice> quotes = invoiceList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.purchaseOrder);
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new ExcelData(commonLocalizer.localizeWithParam(PdfLocalizationName.number), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.typeDate, ""), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.validDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.supplier), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CURRENCY, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.STATUS, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.QUOTE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.quoteNumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.MANAGER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.manager), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.SUB_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.subtotal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.taxTotal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.OPPORTUNITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.opportunity), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.BASE_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.total) + "(" + currency + ")", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.REFERENCE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetname, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + "  Xolatiga ko'ra", workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            Integer calculationScale = getCalculationScale(fs);

            for (NewInvoice item : quotes) {
                String number = "", client = "", status = "", quote = "";
                if (item.getInvoiceNumber() != null) {
                    number = item.getInvoiceNumber();
                }
                if (item.getClientName() != null) {
                    client = String.valueOf(item.getClientName());
                }
                if (item.getStatus() != null) {
                    status = item.getStatus();
                }
                if (item.getQuoteNumber() != null) {
                    quote = item.getQuoteNumber();
                }
                BigDecimal taxTotal = item.getTotalTaxes() != null ? item.getTotalTaxes().multiply(item.getExchageRate() != null ? item.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO;

                BigDecimal amount = item.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP);

                Map<String, ExcelData> mapColumn = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_NUMBER)) {
                    mapColumn.put(InvoiceList.INVOICE_NUMBER, new ExcelData(number, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_DATE)) {
                    mapColumn.put(InvoiceList.INVOICE_DATE, new ExcelData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getInvoiceDate() != null ? item.getInvoiceDate().getNonConvertedDate() : null, shortDateFormat)) : ServerUtils.dateFormat(item.getInvoiceDate() != null ? item.getInvoiceDate().getNonConvertedDate() : null, shortDateFormat), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_DATE))
                    mapColumn.put(InvoiceList.DUE_DATE, new ExcelData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getDueDate() != null ? item.getDueDate().getNonConvertedDate() : null, shortDateFormat)) : ServerUtils.dateFormat(item.getDueDate() != null ? item.getDueDate().getNonConvertedDate() : null, shortDateFormat), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));

                if (panelTools.getColumnCodeName().contains(InvoiceList.RELATED_PROJECT)) {
                    mapColumn.put(InvoiceList.RELATED_PROJECT, new ExcelData(item.getRelatedProjectName() != null ? item.getRelatedProjectName() : "N/A",
                            ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT)) {
                    mapColumn.put(InvoiceList.CLIENT, new ExcelData(client, ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY)) {
                    mapColumn.put(InvoiceList.OPPORTUNITY, new ExcelData(item.getOpportunityNumber(), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CURRENCY)) {
                    mapColumn.put(InvoiceList.CURRENCY, new ExcelData(item.getCurrencyName(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_AMOUNT)) {
                    mapColumn.put(InvoiceList.DUE_AMOUNT, new ExcelData(amount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.STATUS)) {
                    mapColumn.put(InvoiceList.STATUS, new ExcelData(status, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.QUOTE_NUMBER)) {
                    mapColumn.put(InvoiceList.QUOTE_NUMBER, new ExcelData(quote, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.MANAGER)) {
                    mapColumn.put(InvoiceList.MANAGER, new ExcelData(item.getCurrentApproverSelectItem() != null ? String.valueOf(item.getCurrentApproverSelectItem().getName()) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR)) {
                    mapColumn.put(InvoiceList.CREATOR, new ExcelData(item.getCreator() != null && item.getCreator().getName() != null ? item.getCreator().getName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.SUB_TOTAL)) {
                    mapColumn.put(InvoiceList.SUB_TOTAL, new ExcelData(item.getSubtotal() != null ? item.getSubtotal() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.TAX_TOTAL)) {
                    mapColumn.put(InvoiceList.TAX_TOTAL, new ExcelData(taxTotal.setScale(calculationScale, RoundingMode.HALF_UP).toString(), ExcelData.CURRENCY, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.BASE_TOTAL)) {
                    mapColumn.put(InvoiceList.BASE_TOTAL, new ExcelData(item.getTotal().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.REFERENCE)) {
                    mapColumn.put(InvoiceList.REFERENCE, new ExcelData(item.getReference(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, company);
                excelDataList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);

                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate purchase order list excel report, exception: " + e.getMessage());
        }
        return null;
    }
}
