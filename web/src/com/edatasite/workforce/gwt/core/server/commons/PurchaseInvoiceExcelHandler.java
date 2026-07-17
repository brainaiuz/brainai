package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
 * Time: 16:29:48
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseInvoiceExcelHandler extends BaseExcelHandler implements Constants {

    private static final Logger log = LoggerFactory.getLogger(PurchaseInvoiceExcelHandler.class);
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserManager userManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetname;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Purchase Invoices";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        String shortDateFormat = "MMM dd, yyyy";
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();

        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }


        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParameters.setLimit(LIMIT_EXCEL_ROW);
        }
        ListResult<NewInvoice> purchaseList = invoiceService.getPurchaseInvoiceDataFromSolr(filterParameters);
        List<NewInvoice> invoices = purchaseList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.purchaseInvoice);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.number, accountingLocalizer.localize(PdfLocalizationName.number)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.typeDate, ""), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.dueDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CLIENT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.supplier), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CURRENCY, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.ORIGINAL_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dueAmount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.PAID_AMOUNT, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.paidAmount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.STATUS, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.taxTotal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.BASE_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.total) + "(" + currency + ")", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.REFERENCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.PO_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.poNumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.CLIENT_VAT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vatNumber), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.MANAGER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.manager), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(InvoiceList.OPPORTUNITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.opportunity), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
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
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.asOF) + "   " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }
//            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " " + commonLocalizer.localize(PdfLocalizationName.asOF), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            Integer calculationScale = getCalculationScale(fs);
            DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);
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
                BigDecimal amount = item.getTotalInInvoiceCurrency().subtract(paidAmount).setScale(calculationScale, RoundingMode.HALF_UP);
                BigDecimal originalAmount = item.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP);
                String manager = (item.getPurchaseOrderManager() != null && item.getPurchaseOrderManager().getName() != null) ? item.getPurchaseOrderManager().getName() : "—";

                if (item.isCreditNote()) {
                    paidAmount = paidAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(paidAmount) : paidAmount;
                    amount = amount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(amount) : amount;
                    originalAmount = originalAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(originalAmount) : originalAmount;
                }

                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_NUMBER)) {
                    mapColumn.put(InvoiceList.INVOICE_NUMBER, new ExcelData(number, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(InvoiceList.INVOICE_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getInvoiceDate() != null ? item.getInvoiceDate().getNonConvertedDate() : null, shortDateFormat)), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(InvoiceList.INVOICE_DATE, new ExcelData(ServerUtils.dateFormat(item.getInvoiceDate() != null ? item.getInvoiceDate().getNonConvertedDate() : null, shortDateFormat),
                                ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_DATE))
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(InvoiceList.DUE_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getDueDate() != null ? item.getDueDate().getNonConvertedDate() : null, shortDateFormat)), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(InvoiceList.DUE_DATE, new ExcelData(ServerUtils.dateFormat(item.getDueDate() != null ? item.getDueDate().getNonConvertedDate() : null, shortDateFormat),
                                ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                if (panelTools.getColumnCodeName().contains(InvoiceList.RELATED_PROJECT)) {
                    mapColumn.put(InvoiceList.RELATED_PROJECT, new ExcelData(item.getRelatedProjectName() != null ? item.getRelatedProjectName() : "N/A",
                            ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT)) {
                    mapColumn.put(InvoiceList.CLIENT, new ExcelData(client, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.MANAGER)) {
                    mapColumn.put(InvoiceList.CLIENT, new ExcelData(manager, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY)) {
                    mapColumn.put(InvoiceList.OPPORTUNITY, new ExcelData(item.getOpportunity(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.SUPPLIER)) {
                    mapColumn.put(InvoiceList.SUPPLIER, new ExcelData(client, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CURRENCY)) {
                    mapColumn.put(InvoiceList.CURRENCY, new ExcelData(item.getCurrencyName(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.ORIGINAL_AMOUNT)) {
                    mapColumn.put(InvoiceList.ORIGINAL_AMOUNT, new ExcelData(originalAmount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_AMOUNT)) {
                    mapColumn.put(InvoiceList.DUE_AMOUNT, new ExcelData(amount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PAID_AMOUNT)) {
                    mapColumn.put(InvoiceList.PAID_AMOUNT, new ExcelData(paidAmount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.STATUS)) {
                    mapColumn.put(InvoiceList.STATUS, new ExcelData(status, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR)) {
                    mapColumn.put(InvoiceList.CREATOR, new ExcelData(item.getCreatorName() != null ? item.getCreatorName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                String taxTotal = priceScaleFormat.format(item.getTotalTaxes() != null ? item.getTotalTaxes().multiply(item.getExchageRate() != null ? item.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO);
                if (panelTools.getColumnCodeName().contains(InvoiceList.TAX_TOTAL)) {
                    mapColumn.put(InvoiceList.TAX_TOTAL, new ExcelData(taxTotal, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.BASE_TOTAL)) {
                    mapColumn.put(InvoiceList.BASE_TOTAL, new ExcelData(item.getTotal().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.REFERENCE)) {
                    mapColumn.put(InvoiceList.REFERENCE, new ExcelData(item.getReference(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PO_NUMBER)) {
                    mapColumn.put(InvoiceList.PO_NUMBER, new ExcelData(item.getPoNumber(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT_VAT_NUMBER)) {
                    String vatNumber = !ServerUtils.isNullOrEmpty(item.getClientTrnNumber()) ? item.getClientTrnNumber() : item.getClientVatNumber();
                    mapColumn.put(InvoiceList.CLIENT_VAT_NUMBER, new ExcelData(vatNumber, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate products/services list excel report, exception: " + ex);
        }

        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
