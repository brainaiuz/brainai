package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
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
 * Date: 7/24/12
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */

public class CourseListExcelHandler extends BaseExcelHandler {

    @Autowired
    private TCService tcService;

    @Override
    protected void setFileName() {
        filename = "CourseList";
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
        List<CourseItem> courseItems = new ArrayList<>();
        while (totalLength > (start += limit)) {
            filterParametrs.setStart(start);
            filterParametrs.setLimit(200);
            ListResult<CourseItem> courseList = tcService.getCourseList(filterParametrs);
            totalLength = courseList.getTotal();
            courseItems.addAll(courseList.getList());
        }
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(CourseItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.courseName), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.SUBJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.subjectOnly), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.ALIAS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.aliasName), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.courseType), ExcelData.STRING, 40, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.VALIDITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.validity), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.DURATION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.duration), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(CourseItem.PRICEPERSTUDENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.pricePerStudent), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(CourseItem.PREREQUISITE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.preRequisite), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(CourseItem.ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.account), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
			mapColumnHeader.put(CourseItem.EXAMREQUIRED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.examRequired), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.OPITO, new ExcelData(commonLocalizer.localize(PdfLocalizationName.opito), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.MEDCLEARANCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.medClearance), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.COURSE_REQUIREMENTS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.courseRequirements), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CourseItem.INSTRUCTOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.instructors), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
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

            for (CourseItem course : courseItems) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(CourseItem.NUMBER)) {
                    mapColumn.put(CourseItem.NUMBER, new ExcelData(course.getNumberData() != null ? course.getNumberData().getNumberString() : "", ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.NAME)) {
                    mapColumn.put(CourseItem.NAME, new ExcelData(course.getCourseName(), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.SUBJECT)) {
                    mapColumn.put(CourseItem.SUBJECT, new ExcelData(course.getSubject() != null && course.getSubject().getName() != null ? course.getSubject().getName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.ALIAS)) {
                    mapColumn.put(CourseItem.ALIAS, new ExcelData(course.getAliasName(), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.DESCRIPTION)) {
                    mapColumn.put(CourseItem.DESCRIPTION, new ExcelData(course.getDescription(), ExcelData.STRING, 40, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.TYPE)) {
                    mapColumn.put(CourseItem.TYPE, new ExcelData(course.getCourseType() != null ? course.getCourseType().getName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.VALIDITY)) {
                    mapColumn.put(CourseItem.VALIDITY, new ExcelData(course.getValidity() != null ? course.getValidity().toString() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.DURATION)) {
                    mapColumn.put(CourseItem.DURATION, new ExcelData(course.getDuration() != null ? course.getDuration().toString() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.PRICEPERSTUDENT)) {
                    mapColumn.put(CourseItem.PRICEPERSTUDENT, new ExcelData(course.getPricePerStudent() != null ? course.getPricePerStudent().toString() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.PREREQUISITE)) {
					StringBuilder buffer = new StringBuilder();
					if (course.getPreRequisite() != null) {
						for (SelectItem item : course.getPreRequisite()) {
							buffer.append(item.getName()).append(",");
						}
					}
                    mapColumn.put(CourseItem.PREREQUISITE, new ExcelData(buffer.length() > 0 ? buffer.deleteCharAt(buffer.length() - 1).toString() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
//                if (panelTools.getColumnCodeName().contains(CourseItem.ACCOUNT)) {
//                    mapColumn.put(CourseItem.ACCOUNT, new ExcelData(course.getAccountItem() != null ? course.getAccountItem().getName() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
//                }
                if (panelTools.getColumnCodeName().contains(CourseItem.EXAMREQUIRED)) {
                    mapColumn.put(CourseItem.EXAMREQUIRED, new ExcelData(course.isExamRequired() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.OPITO)) {
                    mapColumn.put(CourseItem.OPITO, new ExcelData(course.isOpito() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.MEDCLEARANCE)) {
                    mapColumn.put(CourseItem.MEDCLEARANCE, new ExcelData(course.isMedClearance() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.COURSE_REQUIREMENTS)) {
                    mapColumn.put(CourseItem.COURSE_REQUIREMENTS, new ExcelData(course.getCourseRequirementsAsString() != null ? course.getCourseRequirementsAsString() : "", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.INSTRUCTOR)) {
                    mapColumn.put(CourseItem.INSTRUCTOR, new ExcelData(course.getInstructorsAsString() != null ? course.getInstructorsAsString() : "", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
