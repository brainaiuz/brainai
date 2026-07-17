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
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdditionalPaymentExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(AdditionalPaymentExcelHandler.class);
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    @Autowired
    PayrollService payrollService;
    @Autowired
    protected PropertManager propertManager;
    WfmResourceBundleMessageSource pdfWfmMessageSource;
    String sheetName;

    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = userManager.getUser();
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());
         sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.additionalPaymentList);
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        filterParameters.setLimit(filterParameters.getLimit() < (LIMIT_EXCEL_ROW) ? filterParameters.getLimit() : LIMIT_EXCEL_ROW);
        ListResult<AdditionalPayment> aPayments = payrollService.getAdditionalPaymentList(filterParameters);
        ExcelData[] cellDatas;
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        HashMap<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);

            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnData.put(AdditionalPayment.PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.period), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(AdditionalPayment.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(AdditionalPayment.CATEGORY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.category), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(AdditionalPayment.REFERENCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(AdditionalPayment.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(AdditionalPayment.TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(AdditionalPayment.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " holatiga ko'ra", workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), "  " + commonLocalizer.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
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

            for (AdditionalPayment item : aPayments.getList()) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(AdditionalPayment.PERIOD)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(AdditionalPayment.PERIOD, new ExcelData(item.getPeriod() != null ? ServerUtils.convertToUzbDateFormat(item.getPeriod()) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(AdditionalPayment.PERIOD, new ExcelData(item.getPeriod() != null ? item.getPeriod() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }


                }
                if (panelTools.getColumnCodeName().contains(AdditionalPayment.APPROVER)) {
                    mapColumn.put(AdditionalPayment.APPROVER, new ExcelData(item.getApprover() != null ? item.getApprover().getName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(AdditionalPayment.CATEGORY)) {
                    mapColumn.put(AdditionalPayment.CATEGORY, new ExcelData(item.getCategory().getCode() != null ? item.getCategory().getCode() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(AdditionalPayment.REFERENCE)) {
                    mapColumn.put(AdditionalPayment.REFERENCE, new ExcelData(item.getReference() != null ? item.getReference() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(AdditionalPayment.STATUS)) {
                    mapColumn.put(AdditionalPayment.STATUS, new ExcelData(item.getStatus() != null ? item.getStatus() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(AdditionalPayment.TOTAL)) {
                    mapColumn.put(AdditionalPayment.TOTAL, new ExcelData(item.getTotal() != null ? item.getTotal() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(AdditionalPayment.CREATOR)) {
                    mapColumn.put(AdditionalPayment.CREATOR, new ExcelData(item.getCreator().getName() != null ? item.getCreator().getName() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate additional payment list excel report, exception: " + e);
        }
        return null;

    }


    @Override
    protected void setFileName() {
        this.filename = "AdditionalPaymentsList";
    }
}
