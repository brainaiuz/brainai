package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Azazello on 7/25/15.
 */
public class EmployeeStepsListExcelHandler extends BaseExcelHandler {

    @Autowired
    private HrmsService hrmsService;
    @Autowired
    protected UploadManager uploadManager;

    protected String dateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.shortDateFormat(date, uploadManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setAllByFilter(true);
        filterParametrs.setForExportOnly(true);
        EdsCompany edsCompany = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        ListResult<EmployeeStepItem> stepListResult = hrmsService.getEmployeeStepList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(EmployeeStepItem.EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.EMPLOYEE_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employeeCode), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.CANDIDATE_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.candidateCode), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.EMPLOYEE_LOCATION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.ASSIGN_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approverStatus), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.CREATION_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.creationDate), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeStepItem.UPDATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            if (panelTools.isCustomFieldsShown()) {
                CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            }

            // Set excell header
            List<ExcelData> excellDatasList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excellDatasList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
                }
            }
            cellDatas = new ExcelData[excellDatasList.size()];
            excellDatasList.toArray(cellDatas);
            list.add(cellDatas);

            for (EmployeeStepItem item : stepListResult.getList()) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.EMPLOYEE)) {
                    mapColumn.put(EmployeeStepItem.EMPLOYEE, new ExcelData(item.getEmployeeName(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.STATUS)) {
                    mapColumn.put(EmployeeStepItem.STATUS, new ExcelData(item.getStatusName() != null ? item.getStatusName() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.EMPLOYEE_CODE)) {
                    mapColumn.put(EmployeeStepItem.EMPLOYEE_CODE, new ExcelData(item.getEmployeeCode(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.CANDIDATE_CODE)) {
                    mapColumn.put(EmployeeStepItem.CANDIDATE_CODE, new ExcelData(item.getCandidateCode() != null ? item.getCandidateCode() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.TYPE)) {
                    mapColumn.put(EmployeeStepItem.TYPE, new ExcelData(item.getTypeName(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.EMPLOYEE_LOCATION)) {
                    mapColumn.put(EmployeeStepItem.EMPLOYEE_LOCATION, new ExcelData(item.getLocation() != null ? item.getLocation() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.ASSIGN_STATUS)) {
                    mapColumn.put(EmployeeStepItem.ASSIGN_STATUS, new ExcelData(item.getAssignStatues(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.CREATION_DATE)) {
                    mapColumn.put(EmployeeStepItem.CREATION_DATE, new ExcelData(item.getCreationDate() != null ? dateFormat(item.getCreationDate()) : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeStepItem.UPDATED_DATE)) {
                    mapColumn.put(EmployeeStepItem.UPDATED_DATE, new ExcelData(item.getUpdatedDate() != null ? dateFormat(item.getUpdatedDate()) : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, edsCompany);

                if (panelTools.isCustomFieldsShown()) {
                    for (String key : item.getCustomFieldsMap().keySet()) {
                        if (item.getCustomFieldsMap().get(key) != null) {
                            if (item.getCustomFieldsMap().get(key) instanceof Date) {
                                mapColumn.put(key, new ExcelData(dateFormat((Date) item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else if (item.getCustomFieldsMap().get(key) instanceof Double) {
                                mapColumn.put(key, new ExcelData(NumberFormat.getNumberInstance().format(item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                mapColumn.put(key, new ExcelData(item.getCustomFieldsMap().get(key).toString(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        } else {
                            mapColumn.put(key, new ExcelData("", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                        }
                    }
                }

                excellDatasList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excellDatasList.add(getExcelRows(mapColumn.get(panelTools.getColumnCodeName().get(i))));
                    }
                }
                cellDatas = new ExcelData[excellDatasList.size()];
                excellDatasList.toArray(cellDatas);
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Employee Steps";
    }
}
