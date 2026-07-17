package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/30/12
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */

public class ScheduledCourseListExcelHandler extends BaseExcelHandler {

    @Autowired
    private TCService tcService;

    @Autowired
    private PropertManager propertManager;

    @Override
    protected void setFileName() {
        filename = "ScheduledCourseList";
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;

        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        String shortDateFormat = "MMM dd, yyyy";
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        ListResult<ScheduledCourseItem> courseList = tcService.getCourseScheduleFromSolr(filterParametrs);
        List<ScheduledCourseItem> courseItems = new ArrayList<>(courseList.getList());
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(ScheduledCourseItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, false, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.COURSE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.courseField), ExcelData.STRING, 35, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.LANGUAGE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.language), ExcelData.STRING, 15, false, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.LOCATION, new ExcelData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.DURATION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.duration), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.COUNT_OF_SETS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.numberOfSeats), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.AVAILABLE_SET, new ExcelData(commonLocalizer.localize(PdfLocalizationName.availableSeats), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.INSTRUCTOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.instructor), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.ASSESSOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.assessor), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.COUNT_OF_STUDENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.countOfStudent), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.countOfConfirmedStudent), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
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

            for (ScheduledCourseItem course : courseItems) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.NUMBER)) {
                    mapColumn.put(ScheduledCourseItem.NUMBER, new ExcelData(course.getNumber() != null ? course.getNumber() : "", ExcelData.STRING, 10, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COURSE)) {
                    mapColumn.put(ScheduledCourseItem.COURSE, new ExcelData(course.getCourseName() != null ? course.getCourseName() : "", ExcelData.STRING, 35, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.LANGUAGE)) {
                    mapColumn.put(ScheduledCourseItem.LANGUAGE, new ExcelData(course.getLanguageName() != null ? course.getLanguageName() : "", ExcelData.STRING, 15, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.LOCATION)) {
                    mapColumn.put(ScheduledCourseItem.LOCATION, new ExcelData(course.getLocationName() != null ? course.getLocationName() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.START_DATE)) {
                    mapColumn.put(ScheduledCourseItem.START_DATE, new ExcelData(course.getStartDate() != null ? user.getUserDate(course.getStartDate()) : "", ExcelData.DATE, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.DURATION)) {
                    mapColumn.put(ScheduledCourseItem.DURATION, new ExcelData(course.getDuration() != null ? course.getDuration() + "hours(s)" : "", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COUNT_OF_SETS)) {
                    mapColumn.put(ScheduledCourseItem.COUNT_OF_SETS, new ExcelData(course.getNumberOfSeats() != null ? course.getNumberOfSeats() : 0, ExcelData.INTEGER, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.AVAILABLE_SET)) {
                    mapColumn.put(ScheduledCourseItem.AVAILABLE_SET, new ExcelData(course.getAvailableSets() != null ? course.getAvailableSets() : 0, ExcelData.INTEGER, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.INSTRUCTOR)) {
                    mapColumn.put(ScheduledCourseItem.INSTRUCTOR, new ExcelData(course.getInstructorName() != null ? course.getInstructorName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.ASSESSOR)) {
                    mapColumn.put(ScheduledCourseItem.ASSESSOR, new ExcelData(course.getAssessorName() != null ? course.getAssessorName() : "", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COUNT_OF_STUDENT)) {
                    mapColumn.put(ScheduledCourseItem.COUNT_OF_STUDENT, new ExcelData(course.getCountOfStudent() != null ? course.getCountOfStudent() : 0, ExcelData.INTEGER, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT)) {
                    mapColumn.put(ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT, new ExcelData(course.getCountOfConfirmedStudent() != null ? course.getCountOfConfirmedStudent() : 0, ExcelData.INTEGER, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
