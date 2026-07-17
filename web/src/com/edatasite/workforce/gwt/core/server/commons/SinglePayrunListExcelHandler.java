package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Dilsh0d Madrahimov
 * Date: Feb 21, 2017
 * Time: 16:10:35 PM
 */
public class SinglePayrunListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(SinglePayrunListExcelHandler.class);

    @Autowired
    private PayrollService payrollService;
    @Autowired
    private PropertManager propertManager;
    private WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Single Payruns";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        String sheetname = property != null ? property.getPlural() : commonLocalizer.localize("singlePayrunList");

        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            fp.setLimit(LIMIT_EXCEL_ROW);
        }
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);

        ListResult<SinglePayrunItem> singlePayrunList = payrollService.getSinglePayrunList(fp);
        ListPanelToolRpc panelTools = fp.getListPanelTool();

        ExcelData[] cellDatas;
        HashMap<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            ArrayList<ExcelData[]> list = new ArrayList<>();

            mapColumnHeader.put(SinglePayrunItem.EMPLOYEE_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employeeCode), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.PROJECTS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.projects), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.PREPARER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.period), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.CURRENCY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.DRIVER_ID, new ExcelData("Driver ID", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.PROCESS_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.processDate), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SinglePayrunItem.PAYMENT_METHOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.paymentMethod), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(),sheetname, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra", workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }
            ArrayList<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (SinglePayrunItem item : singlePayrunList.getList()) {
                HashMap<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.EMPLOYEE_CODE)) {
                    mapColumn.put(SinglePayrunItem.EMPLOYEE_CODE, new ExcelData(item.getEmployeeCode() != null ? item.getEmployeeCode() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.EMPLOYEE)) {
                    mapColumn.put(SinglePayrunItem.EMPLOYEE, new ExcelData(item.getEmployee(), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PROJECTS)) {
                    StringBuilder projectNames = new StringBuilder();
                    if (!item.getProjects().isEmpty()) {
                        for (SelectItem project : item.getProjects()) {
                            if (projectNames.toString().isEmpty()) {
                                projectNames.append(project.getName());
                            } else {
                                projectNames.append(",").append(" ").append(project.getName());
                            }
                        }
                    }
                    mapColumn.put(SinglePayrunItem.PROJECTS, new ExcelData(projectNames.toString(), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PERIOD)) {
                    StringBuilder period = new StringBuilder();
                    period.append(item.getMonth() != null ? item.getMonth() : "")
                            .append(period.length() > 0 && item.getYear() != null ? "," : "")
                            .append(item.getYear() != null ? item.getYear() : "");
//                    mapColumn.put(SinglePayrunItem.PERIOD, new ExcelData(period.toString(), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(SinglePayrunItem.PERIOD, new ExcelData(ServerUtils.convertToUzbDateFormat(period.toString()), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(SinglePayrunItem.PERIOD, new ExcelData(period.toString(), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.APPROVER)) {
                    mapColumn.put(SinglePayrunItem.APPROVER, new ExcelData(item.getApprover() != null ? item.getApprover().getName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PREPARER)) {
                    mapColumn.put(SinglePayrunItem.PREPARER, new ExcelData(item.getCreator() != null ? item.getCreator().getName() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.TOTAL)) {
                    mapColumn.put(SinglePayrunItem.TOTAL, new ExcelData(item.getTotal().setScale(calculationScale, BigDecimal.ROUND_HALF_UP), ExcelData.BIG_DECIMAL, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.STATUS)) {
                    mapColumn.put(SinglePayrunItem.STATUS, new ExcelData(item.getStatus() != null ? item.getStatus() : "", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.CURRENCY)) {
                    mapColumn.put(SinglePayrunItem.CURRENCY, new ExcelData((item.getCurrency() != null && item.getCurrency().getName() != null) ? item.getCurrency().getName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.DRIVER_ID)) {
                    mapColumn.put(SinglePayrunItem.DRIVER_ID, new ExcelData(item.getDriverID() != null ? item.getDriverID() + "" : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PROCESS_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(SinglePayrunItem.PROCESS_DATE, new ExcelData(item.getProcessDate() != null ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(item.getProcessDate().getNonConvertedDate(), user)) : "N/A", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(SinglePayrunItem.PROCESS_DATE, new ExcelData(item.getProcessDate() != null ? ServerUtils.shortDateFormat(item.getProcessDate().getNonConvertedDate(), user) : "N/A", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                    }
                }

                if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PAYMENT_METHOD)) {
                    mapColumn.put(SinglePayrunItem.PAYMENT_METHOD, new ExcelData(item.getPayMethodName() != null ? item.getPayMethodName() : "N/A", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, user.getCompany());
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
            log.error("Cannot generate single payrun list excel report, exception: " + ex);
        }
        return null;
    }
}