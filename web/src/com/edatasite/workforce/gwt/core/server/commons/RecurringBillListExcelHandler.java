package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla Nigmatjonov
 * Date: 22.12.2015
 * Time: 20:34:14
 * To change this template use File | Settings | File Templates.
 */
public class RecurringBillListExcelHandler extends BaseExcelHandler implements Constants, AccountingConstants {

    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private InvoiceService invoiceService;
    private static final Logger log = LoggerFactory.getLogger(RecurringBillListExcelHandler.class);
    @Autowired
    private PropertManager propertManager;
    private String sheetname;
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;


    @Override
    protected void setFileName() {
        filename = "Recurring Bills";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();
        Integer calculationScale = getCalculationScale(fs);

        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        String shortDateFormat = "MMM dd, yyyy";
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit().trim()) && !"null".equals(companySettings.getExcelLimit().trim())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        filterParametrs.setStartDate(parseFilterParameterDate(filterParametrs.getStartDateNC()));
        filterParametrs.setEndDate(parseFilterParameterDate(filterParametrs.getEndDateNC()));

        ListResult<RecurringInvoiceListItem> resultList = invoiceService.getRecurringBillData(filterParametrs);

        ExcelData[] cellExcelDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(RecurringInvoiceListItem.ACTION);

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(RecurringInvoiceListItem.CLIENT, accountingLocalizer.localize(PdfLocalizationName.supplier));
        mapColumnHeader.put(RecurringInvoiceListItem.AMOUNT, accountingLocalizer.localize(PdfLocalizationName.amount));
        mapColumnHeader.put(RecurringInvoiceListItem.BASE_AMOUNT, accountingLocalizer.localize(PdfLocalizationName.amount) + "(" + currency + ")");
        mapColumnHeader.put(RecurringInvoiceListItem.REPEATS, commonLocalizer.localize(PdfLocalizationName.repeats));
        mapColumnHeader.put(RecurringInvoiceListItem.NEXT_IVOICE_DATE, accountingLocalizer.localize(PdfLocalizationName.nextInvoiceDate));
        mapColumnHeader.put(RecurringInvoiceListItem.END_DATE, commonLocalizer.localize(PdfLocalizationName.endDate));
        mapColumnHeader.put(RecurringInvoiceListItem.STATUS, accountingLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(RecurringInvoiceListItem.RECURRENCE_STATUS, commonLocalizer.localize(PdfLocalizationName.recurrenceStatus));

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : accountingLocalizer.localize(PdfLocalizationName.recurringBills);

            List<ExcelData[]> list = new LinkedList<>();
            cellExcelDatas = new ExcelData[header.size()];


            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, commonLocalizer.localize(PdfLocalizationName.asOF, " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelDatas);

            for (RecurringInvoiceListItem item : resultList.getList()) {
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    if (RecurringInvoiceListItem.CLIENT.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getClient() != null ? item.getClient() : "", ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (RecurringInvoiceListItem.AMOUNT.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getAmountInInvoiceCurrency() != null ? item.getAmountInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP) : "", ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (RecurringInvoiceListItem.BASE_AMOUNT.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getAmount() != null ? item.getAmount().setScale(calculationScale, RoundingMode.HALF_UP) : "", ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (RecurringInvoiceListItem.REPEATS.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getRepeats() != null ? item.getRepeats() : "", ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (RecurringInvoiceListItem.NEXT_IVOICE_DATE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(ServerUtils.dateFormat(item.getNextInvoiceDate(), shortDateFormat), ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (RecurringInvoiceListItem.END_DATE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(ServerUtils.dateFormat(item.getEndDate(), shortDateFormat), ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (RecurringInvoiceListItem.STATUS.equals(header.get(j))) {
                        String status = "";
                        if (DRAFT.equals(item.getStatusCode())) {
                            status = commonLocalizer.localize(PdfLocalizationName.draft);
                        } else if (APPROVE.equals(item.getStatusCode())) {
                            status = commonLocalizer.localize(PdfLocalizationName.approved);
                        } else if (OPEN.equals(item.getStatusCode())) {
                            status = commonLocalizer.localize(PdfLocalizationName.sent);
                        }
                        cellExcelDatas[j] = new ExcelData(status, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (RecurringInvoiceListItem.RECURRENCE_STATUS.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getRecurrenceStatus() != null ? item.getRecurrenceStatus() : "", ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate "+filename+" excel report, exception: " + e);
        }


        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
