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
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.07.2009
 * Time: 16:15:12
 * To change this template use File | Settings | File Templates.
 */
public class SaleInvoicesListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(SaleInvoicesListExcelHandler.class);

    @Autowired
    private InvoiceCircularResolver invoiceService;
    @Autowired
    protected WfmResourceBundleMessageSource pdfWfmMessageSource;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private PropertManager propertManager;
    private String sheetname;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "Sales Invoices";
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

        InvoiceList invoiceList = invoiceService.getSaleInvoiceData(filterParameters);
        List<NewInvoice> invoices = invoiceList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : pdfWfmMessageSource.localize("salesInvoices");

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.Qnumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.invoiceDate, ""), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.dueDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CURRENCY, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.ORIGINAL_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dueAmount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.PAID_AMOUNT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.paidAmount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.taxTotal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.BASE_TOTAL, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.total) + "(" + currency + ")", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.STATUS, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.PO_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.poNumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.REFERENCE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.QUOTE_NUMBER, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.quoteNo), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CLIENT_VAT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vatNumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.OPPORTUNITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.opportunity), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.SUB_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.subtotal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.MANAGER, new ExcelData(hrmsLocalizer.localize(PdfLocalizationName.manager), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            List<ExcelData> excelDataList = new ArrayList<>();

            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            Integer calculationScale = getCalculationScale(fs);

            list.add(cellDatas);
            for (NewInvoice item : invoices) {
                String number = "", client = "", status = "";

                if (item.getInvoiceNumber() != null) {
                    number = item.getInvoiceNumber();
                }
                if (item.getClientName() != null) {
                    client = item.getClientName();
                }
                if (item.getStatus() != null) {
                    status = item.getStatus();
                }

                BigDecimal paidAmount = (item.getPaidAmount() != null ? item.getPaidAmount() : BigDecimal.ZERO).setScale(calculationScale, RoundingMode.HALF_UP);
                BigDecimal dueAmount = item.getTotalInInvoiceCurrency().subtract(paidAmount).setScale(calculationScale, RoundingMode.HALF_UP);
                BigDecimal originalAmount = item.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP);
                BigDecimal taxTotal = (item.getTotalTaxes() != null ? item.getTotalTaxes().multiply(item.getExchageRate() != null ? item.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO).setScale(calculationScale, RoundingMode.HALF_UP);

                if (item.isCreditNote()) {
                    paidAmount = paidAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(paidAmount) : paidAmount;
                    dueAmount = dueAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(dueAmount) : dueAmount;
                    originalAmount = originalAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(originalAmount) : originalAmount;
                }

                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_NUMBER)) {
                    mapColumn.put(InvoiceList.INVOICE_NUMBER, new ExcelData(number, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
                if (panelTools.getColumnCodeName().contains(InvoiceList.CURRENCY)) {
                    mapColumn.put(InvoiceList.CURRENCY, new ExcelData(item.getCurrencyName(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.ORIGINAL_AMOUNT)) {
                    mapColumn.put(InvoiceList.ORIGINAL_AMOUNT, new ExcelData(originalAmount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_AMOUNT)) {
                    mapColumn.put(InvoiceList.DUE_AMOUNT, new ExcelData(dueAmount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PAID_AMOUNT)) {
                    mapColumn.put(InvoiceList.PAID_AMOUNT, new ExcelData(paidAmount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.TAX_TOTAL)) {
                    mapColumn.put(InvoiceList.TAX_TOTAL, new ExcelData(taxTotal, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.BASE_TOTAL)) {
                    mapColumn.put(InvoiceList.BASE_TOTAL, new ExcelData(item.getTotal().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.STATUS)) {
                    mapColumn.put(InvoiceList.STATUS, new ExcelData(status, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PO_NUMBER)) {
                    mapColumn.put(InvoiceList.PO_NUMBER, new ExcelData(item.getPoNumber(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR)) {
                    mapColumn.put(InvoiceList.CREATOR, new ExcelData(item.getCreatorName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.REFERENCE)) {
                    mapColumn.put(InvoiceList.REFERENCE, new ExcelData(item.getReference(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.QUOTE_NUMBER)) {
                    mapColumn.put(InvoiceList.QUOTE_NUMBER, new ExcelData(item.getQuoteNumber(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY)) {
                    mapColumn.put(InvoiceList.OPPORTUNITY, new ExcelData(item.getOpportunityNumber(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT_VAT_NUMBER)) {
                    String vatNumber = !ServerUtils.isNullOrEmpty(item.getClientTrnNumber()) ? item.getClientTrnNumber() : item.getClientVatNumber();
                    mapColumn.put(InvoiceList.CLIENT_VAT_NUMBER, new ExcelData(vatNumber, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.SUB_TOTAL)) {
                    mapColumn.put(InvoiceList.SUB_TOTAL, new ExcelData(item.getSubtotal(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.MANAGER)) {
                    String manager = (item.getCurrentApproverSelectItem() != null && item.getCurrentApproverSelectItem().getName() != null) ? item.getCurrentApproverSelectItem().getName() : "—";
                    mapColumn.put(InvoiceList.MANAGER, new ExcelData(manager, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            log.error("Cannot generate sales invoice list excel report, exception: " + e.getMessage());
        }
        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
