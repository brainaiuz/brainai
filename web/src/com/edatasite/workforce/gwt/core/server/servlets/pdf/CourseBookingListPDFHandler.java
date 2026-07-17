package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 11/20/12
 * Time: 11:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CourseBookingListPDFHandler extends AbstractITextPostPdfHandler {

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
        ListResult<CourseBookingItem> courseList = tcService.getCourseBookingListFromSolr(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CourseBookingItem> courseItems = courseList.getList();
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.courseBookingListFileName) + "_" + dateFormat(new Date()));
        pdfData.setTableName(commonLocalizer.localizeWithParam(PdfLocalizationName.courseBookingListTableName, user.getFullName()));

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(CourseBookingItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseBookingItem.CUSTOMER, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseBookingItem.CONTACT, new CellData(commonLocalizer.localize(PdfLocalizationName.contact), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseBookingItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseBookingItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseBookingItem.TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        columnHeaderMap.put(CourseBookingItem.CREATIONDATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = new ArrayList<>();
        for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
            if (columnHeaderMap.containsKey(panelTools.getColumnCodeName().get(i))) {
                header.add(columnHeaderMap.get(panelTools.getColumnCodeName().get(i)));
            }
        }
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (courseItems != null) {
            for (CourseBookingItem item : courseItems) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.NUMBER)) {
                    columnMap.put(CourseBookingItem.NUMBER, new CellData(getResultOrLongDash(item.getNumber()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.CUSTOMER)) {
                    columnMap.put(CourseBookingItem.CUSTOMER, item.getCustomer() != null ? new CellData(escapeHtml(item.getCustomer().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.CONTACT)) {
                    columnMap.put(CourseBookingItem.CONTACT, item.getCustomer() != null ? new CellData(escapeHtml(item.getContact().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.LOCATION)) {
                    columnMap.put(CourseBookingItem.LOCATION, item.getLocation() != null ? new CellData(escapeHtml(item.getLocation().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.STATUS)) {
                    columnMap.put(CourseBookingItem.STATUS, new CellData(getResultOrLongDash(item.getStatus().getName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.TYPE)) {
                    columnMap.put(CourseBookingItem.TYPE, new CellData(getResultOrLongDash(item.getType().getName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(CourseBookingItem.CREATIONDATE)) {
                    columnMap.put(CourseBookingItem.CREATIONDATE, item.getCreationDate() != null ? new CellData(dateFormat(item.getCreationDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
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
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.courseBookingListFileName) + "_" + ServerUtils.dateFormat(user.getUserDate(new Date()), "dd_MMM_yyyy_HH:mm"));
    }
}
