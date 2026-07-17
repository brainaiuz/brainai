package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/30/12
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */

public class ScheduledCourseListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private TCService tcService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        EdsUser user = uploadManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_PDF_ROWS);
        }
        ListResult<ScheduledCourseItem> courseList = tcService.getCourseScheduleFromSolr(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<ScheduledCourseItem> courseItems = courseList.getList();
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.scheduledCourseListFileName) + "_" + dateFormat(new Date()));
        pdfData.setTableName(commonLocalizer.localizeWithParam(PdfLocalizationName.scheduledCourseListTableName, user.getFullName()));

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(ScheduledCourseItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.COURSE, new CellData(commonLocalizer.localize(PdfLocalizationName.courseField), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.LANGUAGE, new CellData(commonLocalizer.localize(PdfLocalizationName.language), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.DURATION, new CellData(commonLocalizer.localize(PdfLocalizationName.duration), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.COUNT_OF_SETS, new CellData(commonLocalizer.localize(PdfLocalizationName.numberOfSeats), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.AVAILABLE_SET, new CellData(commonLocalizer.localize(PdfLocalizationName.availableSeats), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.INSTRUCTOR, new CellData(commonLocalizer.localize(PdfLocalizationName.instructor), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.ASSESSOR, new CellData(commonLocalizer.localize(PdfLocalizationName.assessor), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.COUNT_OF_STUDENT, new CellData(commonLocalizer.localize(PdfLocalizationName.countOfStudent), Element.ALIGN_LEFT));
        columnHeaderMap.put(ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT, new CellData(commonLocalizer.localize(PdfLocalizationName.countOfConfirmedStudent), Element.ALIGN_LEFT));
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);

        List<CellData> header = new ArrayList<>();

        for (String columnCode : panelTools.getColumnCodeName()) {
            if (columnHeaderMap.containsKey(columnCode)) {
                header.add(columnHeaderMap.get(columnCode));
            }
        }
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (courseItems != null) {
            for (ScheduledCourseItem item : courseItems) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.NUMBER)) {
                    columnMap.put(ScheduledCourseItem.NUMBER, new CellData(escapeHtml(item.getNumber()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COURSE)) {
                    columnMap.put(ScheduledCourseItem.COURSE, new CellData(escapeHtml(item.getCourseName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.LANGUAGE)) {
                    columnMap.put(ScheduledCourseItem.LANGUAGE, new CellData(escapeHtml(item.getLanguageName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.LOCATION)) {
                    columnMap.put(ScheduledCourseItem.LOCATION, new CellData(escapeHtml(item.getLocationName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.START_DATE)) {
                    columnMap.put(ScheduledCourseItem.START_DATE, item.getStartDate() != null ? new CellData(dateFormat(user.getUserDate(item.getStartDate())), Element.ALIGN_LEFT) : new CellData("N/A", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.DURATION)) {
                    columnMap.put(ScheduledCourseItem.DURATION, item.getDuration() != null ? new CellData(item.getDuration() + "Hour(s)", Element.ALIGN_LEFT) : new CellData("0", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COUNT_OF_SETS)) {
                    columnMap.put(ScheduledCourseItem.COUNT_OF_SETS, item.getNumberOfSeats() != null ? new CellData(String.valueOf(item.getNumberOfSeats()), Element.ALIGN_LEFT) : new CellData("0", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.AVAILABLE_SET)) {
                    columnMap.put(ScheduledCourseItem.AVAILABLE_SET, item.getAvailableSets() != null ? new CellData(String.valueOf(item.getAvailableSets()), Element.ALIGN_LEFT) : new CellData("0", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.INSTRUCTOR)) {
                    columnMap.put(ScheduledCourseItem.INSTRUCTOR, (item.getInstructorName() == null) || "0".equals(item.getInstructorName()) ? new CellData("N/A", Element.ALIGN_LEFT) : new CellData(item.getInstructorName(), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.ASSESSOR)) {
                    columnMap.put(ScheduledCourseItem.ASSESSOR, new CellData(escapeHtml(item.getAssessorName())));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COUNT_OF_STUDENT)) {
                    columnMap.put(ScheduledCourseItem.COUNT_OF_STUDENT, item.getCountOfStudent() != null ? new CellData(String.valueOf(item.getCountOfStudent()), Element.ALIGN_LEFT) : new CellData("0", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT)) {
                    columnMap.put(ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT, item.getCountOfConfirmedStudent() != null ? new CellData(String.valueOf(item.getCountOfConfirmedStudent()), Element.ALIGN_LEFT) : new CellData("0", Element.ALIGN_LEFT));
                }
                List<CellData> column = new ArrayList<>();
                for (String columnCode : panelTools.getColumnCodeName()) {
                    if (columnMap.containsKey(columnCode)) {
                        column.add(columnMap.get(columnCode));
                    }
                }
                tableList.addPdfTableRows(column.toArray(new CellData[0]));
            }
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.scheduledCourseListFileName) + "_" + dateFormat(new Date()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("scheduledCourseList");
    }
}
