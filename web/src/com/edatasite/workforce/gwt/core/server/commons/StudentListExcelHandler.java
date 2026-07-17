package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/30/12
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */

public class StudentListExcelHandler extends BaseExcelHandler {

    @Autowired
    private TCService tcService;

    @Override
    protected void setFileName() {
        filename = "StudentList";
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;

        EdsCompany edsCompany = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        int start = -200;
        int limit = 200;
        int totalLength = 1;
        List<StudentItem> studentItems = new ArrayList<>();
        while (totalLength > (start += limit)) {
            filterParametrs.setStart(start);
            filterParametrs.setLimit(200);
            ListResult<StudentItem> courseList = tcService.getStudentList(filterParametrs);
            totalLength = courseList.getTotal();
            studentItems.addAll(courseList.getList());
        }
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(StudentItem.STUDENT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_RESIDENCE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.residenceNumber), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.companyEmployeeNumber), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_DEPARTMENT_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.departmentCode), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_REFERENCE_IND_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.refIndNumber), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnHeader.put(StudentItem.STUDENT_FIRST_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.firstName), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_LAST_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.lastName), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_CUSTOMER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_PHONE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.phone), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_E_MAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(StudentItem.STUDENT_LAST_UPDATE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
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

            for (StudentItem course : studentItems) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_NUMBER)) {
                    mapColumn.put(StudentItem.STUDENT_NUMBER, new ExcelData(course.getNumber() != null ? course.getNumber() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_RESIDENCE_NUMBER)) {
                    mapColumn.put(StudentItem.STUDENT_RESIDENCE_NUMBER, new ExcelData(course.getSafetyPPNumber() != null ? course.getSafetyPPNumber() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER)) {
                    mapColumn.put(StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER, new ExcelData(course.getCompEmpNum() != null ? course.getCompEmpNum() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_DEPARTMENT_CODE)) {
                    mapColumn.put(StudentItem.STUDENT_DEPARTMENT_CODE, new ExcelData(course.getDepartmentCode() != null ? course.getDepartmentCode() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_REFERENCE_IND_NUMBER)) {
                    mapColumn.put(StudentItem.STUDENT_REFERENCE_IND_NUMBER, new ExcelData(course.getRefIndNumber() != null ? course.getRefIndNumber() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_FIRST_NAME)) {
                    mapColumn.put(StudentItem.STUDENT_FIRST_NAME, new ExcelData(course.getFirstName() != null ? course.getFirstName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_LAST_NAME)) {
                    mapColumn.put(StudentItem.STUDENT_LAST_NAME, new ExcelData(course.getLastName() != null ? course.getLastName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_CUSTOMER)) {
                    mapColumn.put(StudentItem.STUDENT_CUSTOMER, new ExcelData(course.getCustomerName() != null ? course.getCustomerName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_PHONE_NUMBER)) {
                    mapColumn.put(StudentItem.STUDENT_PHONE_NUMBER, new ExcelData(ServerUtils.refactorPhone(course.getPrimaryPhone()), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_E_MAIL)) {
                    mapColumn.put(StudentItem.STUDENT_E_MAIL, new ExcelData(course.getPrimaryEmail() != null ? course.getPrimaryEmail() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_STATUS)) {
                    mapColumn.put(StudentItem.STUDENT_STATUS, new ExcelData(course.isActive() ? commonLocalizer.localize(PdfLocalizationName.active) : commonLocalizer.localize(PdfLocalizationName.pending), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_LAST_UPDATE_DATE)) {
                    mapColumn.put(StudentItem.STUDENT_LAST_UPDATE_DATE, new ExcelData(course.getUpdatedDate() != null ? dateFormat(course.getUpdatedDate()) : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);

            return workBook.getWorkBook(filename, 0, 0, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
