package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextBasePdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Dilsh0d Madrahimov
 * Date: Feb 21, 2017
 * Time: 16:10:35 PM
 */
public class CashAdvanceListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CashAdvanceListExcelHandler.class);
    AbstractITextBasePdfHandler excelReferenceMessageSource;
    private String fileHeaderName;
    @Autowired
    private PayrollService payrollService;
    @Autowired
    private PropertManager propertManager;


    @Override
    protected void setFileName() {
        filename = "CashAdvancesList";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null && StringUtils.isNotBlank(companySettings.getExcelLimit())) {
            fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            fp.setLimit(LIMIT_EXCEL_ROW);
        }
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());

        String sheetname = fileHeaderName = property != null ? property.getPlural() : commonLocalizer.localize("cashAdvanceList");
        ListResult<CashAdvanceItem> cashAdvanceList = payrollService.getCashAdvanceList(fp);

        ExcelData[] cellDatas;
        HashMap<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            ArrayList<ExcelData[]> list = new ArrayList<>();

            mapColumnHeader.put(CashAdvanceItem.EMPLOYEE_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employeeCode), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
            mapColumnHeader.put(CashAdvanceItem.EMPLOYEE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
            mapColumnHeader.put(CashAdvanceItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
            mapColumnHeader.put(CashAdvanceItem.DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.date), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
            mapColumnHeader.put(CashAdvanceItem.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
            mapColumnHeader.put(CashAdvanceItem.AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(CashAdvanceItem.REMAINING_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dueAmount), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(CashAdvanceItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));

            ListPanelToolRpc panelTools = fp.getListPanelTool();
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + "  Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            ArrayList<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (CashAdvanceItem item : cashAdvanceList.getList()) {
                HashMap<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.EMPLOYEE_CODE)) {
                    mapColumn.put(CashAdvanceItem.EMPLOYEE_CODE, new ExcelData(item.getEmployeeCode() != null ? item.getEmployeeCode() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.EMPLOYEE_NAME)) {
                    mapColumn.put(CashAdvanceItem.EMPLOYEE_NAME, new ExcelData(item.getEmployeeName() != null ? item.getEmployeeName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.NUMBER)) {
                    mapColumn.put(CashAdvanceItem.NUMBER, new ExcelData(item.getNumber() != null ? item.getNumber() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.DATE)) {
                    mapColumn.put(CashAdvanceItem.DATE, new ExcelData(item.getDate() != null ? ServerUtils.shortDateFormat(item.getDate().getNonConvertedDate(), user) : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.APPROVER)) {
                    mapColumn.put(CashAdvanceItem.APPROVER, new ExcelData(item.getApprover() != null ? item.getApprover().getName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.AMOUNT)) {
                    mapColumn.put(CashAdvanceItem.AMOUNT, new ExcelData(getMoneyFormat(item.getTotalAmount(), calculationScale), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.REMAINING_AMOUNT)) {
                    mapColumn.put(CashAdvanceItem.REMAINING_AMOUNT, new ExcelData(getMoneyFormat(item.getRemainingAmount(), calculationScale), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(CashAdvanceItem.STATUS)) {
                    mapColumn.put(CashAdvanceItem.STATUS, new ExcelData(item.getStatus() != null ? item.getStatus().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
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
            return workBook.getWorkBook(filename, 0, 0, 0, 3);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate cash advance list excel report, exception: " + ex);
        }
        return null;
    }
}