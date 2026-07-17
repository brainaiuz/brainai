package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Omonullo Abdullaev on 10/28/2016.
 */
public class GroupPayrunListExcelHandler extends BaseExcelHandler {
    private WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    PayrollService payrollService;
    @Autowired
    private PropertManager propertManager;

    private String fileHeaderName;
    private static final Logger log = LoggerFactory.getLogger(GroupPayrunListExcelHandler.class);
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = userManager.getUser();
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());
        String sheetname = fileHeaderName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.groupPayrunsList);
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        filterParameters.setLimit(filterParameters.getLimit() < (LIMIT_EXCEL_ROW) ? filterParameters.getLimit() : LIMIT_EXCEL_ROW);
        ListResult<GroupPayrunData> gPayruns = payrollService.getPayslipTableList(filterParameters);
        ExcelData[] cellDatas;
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        HashMap<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(fileHeaderName);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnData.put(GroupPayrunData.PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.period), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.PREPARER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.BATCH, new ExcelData(commonLocalizer.localize(PdfLocalizationName.payrollGroup), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.TOTAL_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.totalAmount), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.TOTAL_IN_BASE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.totalInBase), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.CURRENCY_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.PAYMENT_METHOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.paymentMethod), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.PROCESS_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.processDate), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.BASIC_SALARY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.basicSalary), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.ALLOWANCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.allowance), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.PENSION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.pension), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.DEDUCTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.deduction), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(GroupPayrunData.EXPENSE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.expense), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(),sheetname, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " holatiga ko'ra", workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }
            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnData.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnData.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);
            for (GroupPayrunData item : gPayruns.getList()) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.PERIOD)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(GroupPayrunData.PERIOD, new ExcelData(item.getMonth() != null ? ServerUtils.convertToUzbDateFormat(item.getMonth()) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(GroupPayrunData.PERIOD, new ExcelData(item.getMonth() != null ? item.getMonth() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
//                    mapColumn.put(GroupPayrunData.PERIOD, new ExcelData(item.getMonth() != null ? item.getMonth() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.APPROVER)) {
                    mapColumn.put(GroupPayrunData.APPROVER, new ExcelData(item.getApprover() != null ? item.getApprover().getName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.PREPARER)) {
                    mapColumn.put(GroupPayrunData.PREPARER, new ExcelData(item.getCreator() != null ? item.getCreator().getName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.STATUS)) {
                    mapColumn.put(GroupPayrunData.STATUS, new ExcelData(item.getStatus() != null ? item.getStatus() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.BATCH)) {
                    mapColumn.put(GroupPayrunData.BATCH, new ExcelData(item.getPayrollBatchItem() != null && item.getPayrollBatchItem().getName() != null ? item.getPayrollBatchItem().getName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.TOTAL_AMOUNT)) {
                    mapColumn.put(GroupPayrunData.TOTAL_AMOUNT, new ExcelData(item.getTotalAmount() != null ? item.getTotalAmount().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : "0.00", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(GroupPayrunData.BASIC_SALARY)) {
                    mapColumn.put(GroupPayrunData.BASIC_SALARY, new ExcelData(item.getBasicSalary() != null ? item.getBasicSalary().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : "0.00", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.ALLOWANCE)) {
                    mapColumn.put(GroupPayrunData.ALLOWANCE, new ExcelData(item.getAllowance() != null ? item.getAllowance().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : "0.00", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.PENSION)) {
                    mapColumn.put(GroupPayrunData.PENSION, new ExcelData(item.getPension() != null ? item.getPension().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : "0.00", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.DEDUCTION)) {
                    mapColumn.put(GroupPayrunData.DEDUCTION, new ExcelData(item.getDeduction() != null ? item.getDeduction().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : "0.00", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.EXPENSE)) {
                    mapColumn.put(GroupPayrunData.EXPENSE, new ExcelData(item.getExpense() != null ? item.getExpense().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : "0.00", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }


                if (panelTools.getColumnCodeName().contains(GroupPayrunData.TOTAL_IN_BASE)) {
                    mapColumn.put(GroupPayrunData.TOTAL_IN_BASE, new ExcelData(item.getTotalInBase() != null ? item.getTotalInBase().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : "0.00", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.CURRENCY_NAME)) {
                    mapColumn.put(GroupPayrunData.CURRENCY_NAME, new ExcelData(item.getCurrencyName() != null ? item.getCurrencyName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.CURRENCY_NAME)) {
                    mapColumn.put(GroupPayrunData.PAYMENT_METHOD, new ExcelData(item.getPayMethodName() != null ? item.getPayMethodName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPayrunData.PROCESS_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(GroupPayrunData.PROCESS_DATE, new ExcelData(item.getProcessDate() != null ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(item.getProcessDate().getNonConvertedDate(), userManager.getUser())) : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(GroupPayrunData.PROCESS_DATE, new ExcelData(item.getProcessDate() != null ? ServerUtils.shortDateFormat(item.getProcessDate().getNonConvertedDate(), userManager.getUser()) : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                    }
                }
                excelDataList = new ArrayList<>();
                for (int j = 0; j < panelTools.getColumnCodeName().size(); j++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(j))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(j)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }

            workBook.setList(list);
            return workBook.getWorkBook(fileHeaderName, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate group payrun list excel report, exception: " + e);
        }
        return null;
    }
    @Override
    protected void setFileName() {
        this.filename = fileHeaderName;
    }
}
