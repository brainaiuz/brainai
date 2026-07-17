package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.*;

/**
 * Created by Azazello on 7/25/15.
 */
public class EmployeeStepPDFHandler extends AbstractITextPostPdfHandler {
    private HrmsService hrmsService;

    public void setHrmsService(HrmsService hrmsService) {
        this.hrmsService = hrmsService;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(true);
        filterParametrs.setForExportOnly(true);
        EdsUser user = uploadManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_PDF_ROWS);
        }
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools.getColumnCodeName().contains(EmployeeStepItem.ACTION)) {
            panelTools.getColumnCodeName().remove(EmployeeStepItem.ACTION);
        } else {
            panelTools.getColumnCodeName().remove("Action");
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListResult<EmployeeStepItem> stepList = hrmsService.getEmployeeStepList(filterParametrs);

        HashMap<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(EmployeeStepItem.EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.EMPLOYEE_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.employeeCode), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.CANDIDATE_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.candidateCode), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.EMPLOYEE_LOCATION, new CellData(commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.ASSIGN_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.approverStatus), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.CREATION_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(EmployeeStepItem.UPDATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }
        List<CellData> header = new ArrayList<>();
        for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
            if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                header.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
            }
        }

        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        pdfData.setTableName(filterParametrs.getRelationName());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (EmployeeStepItem item : stepList.getList()) {
            Map<String, CellData> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.EMPLOYEE)) {
                columnMap.put(EmployeeStepItem.EMPLOYEE, new CellData(getResultOrLongDash(item.getEmployeeName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.EMPLOYEE_CODE)) {
                columnMap.put(EmployeeStepItem.EMPLOYEE_CODE, new CellData(getResultOrLongDash(item.getEmployeeCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.CANDIDATE_CODE)) {
                columnMap.put(EmployeeStepItem.CANDIDATE_CODE, new CellData(getResultOrLongDash(item.getCandidateCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.TYPE)) {
                columnMap.put(EmployeeStepItem.TYPE, new CellData(getResultOrLongDash(item.getTypeName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.EMPLOYEE_LOCATION)) {
                columnMap.put(EmployeeStepItem.EMPLOYEE_LOCATION, new CellData(getResultOrLongDash(item.getLocation()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.STATUS)) {
                columnMap.put(EmployeeStepItem.STATUS, new CellData(getResultOrLongDash(item.getStatusName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.ASSIGN_STATUS)) {
                columnMap.put(EmployeeStepItem.ASSIGN_STATUS, new CellData(getResultOrLongDash(item.getAssignStatues()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.CREATION_DATE)) {
                columnMap.put(EmployeeStepItem.CREATION_DATE, item.getCreationDate() != null ? new CellData(dateFormat(item.getCreationDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(EmployeeStepItem.UPDATED_DATE)) {
                columnMap.put(EmployeeStepItem.UPDATED_DATE, item.getUpdatedDate() != null ? new CellData(dateFormat(item.getUpdatedDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.isCustomFieldsShown()) {
                CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), item, edsCompany);
            }
            List<CellData> column = new ArrayList<>();
            for (String columnCode : panelTools.getColumnCodeName()) {
                if (columnMap.containsKey(columnCode)) {
                    column.add(columnMap.get(columnCode));
                }
            }
            tableList.addPdfTableRows(column.toArray(new CellData[0]));
        }
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        setFileName(filterParametrs.getRelationName() + " " + dateFormat(new Date()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.getRelationName();
    }
}
