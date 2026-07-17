package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
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
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */

public class StudentListPDFHandler extends AbstractITextPostPdfHandler {

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
        ListResult<StudentItem> studentList = tcService.getStudentList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<StudentItem> studentItems = studentList.getList();
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.studentListFileName) + "_" + dateFormat(new Date()));
        pdfData.setTableName(commonLocalizer.localizeWithParam(PdfLocalizationName.studentListTableName, user.getFullName()));

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(StudentItem.STUDENT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_RESIDENCE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.residenceNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.companyEmployeeNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_DEPARTMENT_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.departmentCode), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_REFERENCE_IND_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.refIndNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_FIRST_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.firstName), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_LAST_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.lastName), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_CUSTOMER, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_PHONE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.phone), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_E_MAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(StudentItem.STUDENT_LAST_UPDATE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));

        List<CellData> header = new ArrayList<>();
        for (String columnCode : panelTools.getColumnCodeName()) {
            if (columnHeaderMap.containsKey(columnCode)) {
                header.add(columnHeaderMap.get(columnCode));
            }
        }
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[0]));

        if (studentItems != null) {
            for (StudentItem item : studentItems) {
                Map<String, String> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_NUMBER)) {
                    columnMap.put(StudentItem.STUDENT_NUMBER, getResultOrLongDash(item.getNumber()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_RESIDENCE_NUMBER)) {
                    columnMap.put(StudentItem.STUDENT_RESIDENCE_NUMBER, getResultOrLongDash(item.getSafetyPPNumber()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER)) {
                    columnMap.put(StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER, getResultOrLongDash(item.getCompEmpNum()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_DEPARTMENT_CODE)) {
                    columnMap.put(StudentItem.STUDENT_DEPARTMENT_CODE, getResultOrLongDash(item.getDepartmentCode()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_REFERENCE_IND_NUMBER)) {
                    columnMap.put(StudentItem.STUDENT_REFERENCE_IND_NUMBER, getResultOrLongDash(item.getRefIndNumber()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_FIRST_NAME)) {
                    columnMap.put(StudentItem.STUDENT_FIRST_NAME, getResultOrLongDash(item.getFirstName()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_LAST_NAME)) {
                    columnMap.put(StudentItem.STUDENT_LAST_NAME, getResultOrLongDash(item.getLastName()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_CUSTOMER)) {
                    columnMap.put(StudentItem.STUDENT_CUSTOMER, getResultOrLongDash(item.getCustomerName()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_PHONE_NUMBER)) {
                    columnMap.put(StudentItem.STUDENT_PHONE_NUMBER, ServerUtils.refactorPhone(item.getPrimaryPhone()));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_E_MAIL)) {
                    columnMap.put(StudentItem.STUDENT_E_MAIL, getResultOrLongDash(item.getPrimaryEmail()));
                }

                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_STATUS)) {
                    columnMap.put(StudentItem.STUDENT_STATUS, item.isActive() ? commonLocalizer.localize(PdfLocalizationName.active) : commonLocalizer.localize(PdfLocalizationName.pending));
                }
                if (panelTools.getColumnCodeName().contains(StudentItem.STUDENT_LAST_UPDATE_DATE)) {
                    columnMap.put(StudentItem.STUDENT_LAST_UPDATE_DATE, item.getUpdatedDate() != null ? dateFormat(item.getUpdatedDate()) : "—");
                }
                List column = new ArrayList<String>();
                for (String columnCode : panelTools.getColumnCodeName()) {
                    if (columnMap.containsKey(columnCode)) {
                        column.add(columnMap.get(columnCode));
                    }
                }
                String[] colArray = new String[column.size()];
                column.toArray(colArray);
                tableList.addPdfTableRows(colArray);
            }
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.studentListFileName) + "_" + dateFormat(new Date()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("studentList");
    }
}
