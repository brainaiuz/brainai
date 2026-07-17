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
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 06.08.2010
 * Time: 21:24:32
 * To change this template use File | Settings | File Templates.
 */
public class RecurringInvoiceListExcelHandler extends BaseExcelHandler implements Constants {

    private static final Logger log = LoggerFactory.getLogger(RecurringInvoiceListExcelHandler.class);
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
        filename = "Recurring_Invoices";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        String shortDateFormat = "MMM dd, yyyy";
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
        EdsCompany edsCompany = user.getCompany();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();

        ListResult<RecurringInvoiceListItem> invoiceList = invoiceService.getRecurringInvoiceData(filterParameters);
        List<RecurringInvoiceListItem> invoices = invoiceList.getList();
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(RecurringInvoiceListItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(RecurringInvoiceListItem.AMOUNT, accountingLocalizer.localize(PdfLocalizationName.amount));
        mapColumnHeader.put(RecurringInvoiceListItem.BASE_AMOUNT, accountingLocalizer.localize(PdfLocalizationName.amount) + "(" + currency + ")");
        mapColumnHeader.put(RecurringInvoiceListItem.CLIENT, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(RecurringInvoiceListItem.REPEATS, commonLocalizer.localize(PdfLocalizationName.repeats));
        mapColumnHeader.put(RecurringInvoiceListItem.NEXT_IVOICE_DATE, accountingLocalizer.localize(PdfLocalizationName.nextInvoiceDate));
        mapColumnHeader.put(RecurringInvoiceListItem.END_DATE, commonLocalizer.localize(PdfLocalizationName.endDateField));
        mapColumnHeader.put(RecurringInvoiceListItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(RecurringInvoiceListItem.RECURRENCE_STATUS, commonLocalizer.localize(PdfLocalizationName.recurrenceStatus));
        mapColumnHeader.put(RecurringInvoiceListItem.REFERENCE, commonLocalizer.localize(PdfLocalizationName.reference));
        ExcelData[] cellDatas;
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.recurringInvoice);


            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(RecurringInvoiceListItem.AMOUNT) || header.get(i).equals(RecurringInvoiceListItem.CLIENT) ? 50 : 20, false, header.get(i).equals(RecurringInvoiceListItem.REPEATS) || header.get(i).equals(RecurringInvoiceListItem.STATUS) || header.get(i).equals(RecurringInvoiceListItem.END_DATE), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);

            Integer calculationScale = getCalculationScale(fs);

            for (RecurringInvoiceListItem recurring : invoices) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (RecurringInvoiceListItem.AMOUNT.equals(header.get(j))) {
                        BigDecimal amount = (recurring.getAmountInInvoiceCurrency() == null ? BigDecimal.ZERO : recurring.getAmountInInvoiceCurrency()).setScale(calculationScale, RoundingMode.HALF_UP);
                        cellDatas[j] = new ExcelData(amount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (RecurringInvoiceListItem.BASE_AMOUNT.equals(header.get(j))) {
                        BigDecimal amount = (recurring.getAmount() == null ? BigDecimal.ZERO : recurring.getAmount()).setScale(calculationScale, RoundingMode.HALF_UP);
                        cellDatas[j] = new ExcelData(amount, ExcelData.CURRENCY, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (RecurringInvoiceListItem.REPEATS.equals(header.get(j))) {
                        temp = recurring.getRepeats() == null ? "" : recurring.getRepeats();
                    }
                    if (RecurringInvoiceListItem.CLIENT.equals(header.get(j))) {
                        temp = recurring.getClient() == null ? "" : recurring.getClient();
                    }
                    if (RecurringInvoiceListItem.NEXT_IVOICE_DATE.equals(header.get(j))) {
                        temp = recurring.getNextInvoiceDate() != null ? ServerUtils.dateFormat(recurring.getNextInvoiceDate(), shortDateFormat) : "";
                    }
                    if (RecurringInvoiceListItem.END_DATE.equals(header.get(j))) {
                        temp = recurring.getEndDate() != null ? ServerUtils.dateFormat(recurring.getEndDate(), shortDateFormat) : "";
                    }
                    if (RecurringInvoiceListItem.REFERENCE.equals(header.get(j))) {
                        temp = recurring.getReference() != null ? recurring.getReference() : "";
                    }
                    if (RecurringInvoiceListItem.STATUS.equals(header.get(j))) {
                        String status = "";
                        if (recurring.getStatus() != null) {
                            //if (recurring.getEndDate() != null && recurring.getEndDate().before(new Date())){
                            //    temp = accountingLocalizer.localizeAccounting(PdfLocalizationName.ended);
                            //}else{
                            if (DRAFT.equals(recurring.getStatusCode())) {
                                status = commonLocalizer.localize(PdfLocalizationName.draft);
                            } else if (APPROVE.equals(recurring.getStatusCode())) {
                                status = commonLocalizer.localize(PdfLocalizationName.approved);
                                } else if (OPEN.equals(recurring.getStatusCode())) {
                                    status = commonLocalizer.localize(PdfLocalizationName.sent);
                                }
                                temp = accountingLocalizer.localizeWithParam(PdfLocalizationName.invoiceWillBe,status);
                            //}
                        }
                    }
                    if (RecurringInvoiceListItem.RECURRENCE_STATUS.equals(header.get(j))) {
                        temp = recurring.getRecurrenceStatus() != null ? recurring.getRecurrenceStatus() : "";
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(RecurringInvoiceListItem.AMOUNT) || header.get(j).equals(RecurringInvoiceListItem.AMOUNT) ? 15 : 20, false, !header.get(j).equals(RecurringInvoiceListItem.CLIENT), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);

            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate recurring invoice list excel report, exception: " + e);
        }
        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
