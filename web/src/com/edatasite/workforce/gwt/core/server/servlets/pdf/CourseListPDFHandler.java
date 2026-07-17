package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/24/12
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class CourseListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private TCService tcService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
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
        ListResult<CourseItem> courseList = tcService.getCourseList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CourseItem> courseItems = courseList.getList();
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.courseListFileName) + "_" + dateFormat(new Date()));
        pdfData.setTableName(commonLocalizer.localizeWithParam(PdfLocalizationName.courseListTableName, user.getFullName()));

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(CourseItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.courseName), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.SUBJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.subjectOnly), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.ALIAS, new CellData(commonLocalizer.localize(PdfLocalizationName.aliasName), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.courseType), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.VALIDITY, new CellData(commonLocalizer.localize(PdfLocalizationName.validity), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.DURATION, new CellData(commonLocalizer.localize(PdfLocalizationName.duration), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.PRICEPERSTUDENT, new CellData(commonLocalizer.localize(PdfLocalizationName.pricePerStudent), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.PREREQUISITE, new CellData(commonLocalizer.localize(PdfLocalizationName.preRequisite), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.account), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.EXAMREQUIRED, new CellData(commonLocalizer.localize(PdfLocalizationName.examRequired), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.OPITO, new CellData(commonLocalizer.localize(PdfLocalizationName.opito), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.MEDCLEARANCE, new CellData(commonLocalizer.localize(PdfLocalizationName.medClearance), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.COURSE_REQUIREMENTS, new CellData(commonLocalizer.localize(PdfLocalizationName.courseRequirements), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseItem.INSTRUCTOR, new CellData(commonLocalizer.localize(PdfLocalizationName.instructors), Element.ALIGN_LEFT));
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (courseItems != null) {
            for (CourseItem course : courseItems) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(CourseItem.NUMBER)) {
                    columnMap.put(CourseItem.NUMBER, course.getNumberData() != null ? new CellData(escapeHtml(course.getNumberData().getNumberString()), Element.ALIGN_LEFT) : new CellData("—"));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.NAME)) {
                    columnMap.put(CourseItem.NAME, new CellData(escapeHtml(course.getCourseName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.SUBJECT)) {
                    columnMap.put(CourseItem.SUBJECT, course.getSubject() != null ? new CellData(escapeHtml(course.getSubject().getName()), Element.ALIGN_LEFT) : new CellData("—"));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.ALIAS)) {
                    columnMap.put(CourseItem.ALIAS, new CellData(escapeHtml(course.getAliasName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.TYPE)) {
                    columnMap.put(CourseItem.TYPE, course.getCourseType() != null ? new CellData(escapeHtml(course.getCourseType().getName()), Element.ALIGN_LEFT) : new CellData("—"));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.VALIDITY)) {
                    columnMap.put(CourseItem.VALIDITY, course.getValidity() != null ? new CellData(escapeHtml(course.getValidity().toString()), Element.ALIGN_LEFT) : new CellData("—"));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.DURATION)) {
                    columnMap.put(CourseItem.DURATION, course.getDuration() != null ? new CellData(escapeHtml(course.getDuration().toString())) : new CellData("—"));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.PRICEPERSTUDENT)) {
                    columnMap.put(CourseItem.PRICEPERSTUDENT, course.getPricePerStudent() != null ? new CellData(escapeHtml(course.getPricePerStudent().toString())) : new CellData("—"));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.PREREQUISITE)) {
                    StringBuilder buffer = new StringBuilder();
                    if (course.getPreRequisite() != null) {
                        for (SelectItem item : course.getPreRequisite()) {
                            buffer.append(item.getName()).append(",");
                        }
                    }
                    columnMap.put(CourseItem.PREREQUISITE, buffer.length() > 0 ? new CellData(buffer.deleteCharAt(buffer.length() - 1).toString()) : new CellData("—"));
                }
//				if (panelTools.getColumnCodeName().contains(CourseItem.ACCOUNT)) {
//					columnMap.put(CourseItem.ACCOUNT, course.getAccountItem() != null ? course.getAccountItem().getName() : "");
//				}
                if (panelTools.getColumnCodeName().contains(CourseItem.EXAMREQUIRED)) {
                    columnMap.put(CourseItem.EXAMREQUIRED, course.isExamRequired() ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.no), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.OPITO)) {
                    columnMap.put(CourseItem.OPITO, course.isOpito() ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes)) : new CellData(commonLocalizer.localize(PdfLocalizationName.no)));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.MEDCLEARANCE)) {
                    columnMap.put(CourseItem.MEDCLEARANCE, course.isMedClearance() ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes)) : new CellData(commonLocalizer.localize(PdfLocalizationName.no)));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.COURSE_REQUIREMENTS)) {
                    columnMap.put(CourseItem.COURSE_REQUIREMENTS, new CellData(getResultOrLongDash(course.getCourseRequirementsAsString())));
                }
                if (panelTools.getColumnCodeName().contains(CourseItem.INSTRUCTOR)) {
                    columnMap.put(CourseItem.INSTRUCTOR, new CellData(getResultOrLongDash(course.getInstructorsAsString())));
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
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.courseListFileName) + "_" + dateFormat(new Date()));
    }
}
