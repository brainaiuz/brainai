package com.edatasite.workforce.gwt.core.server.servlets.pdf;
//for PDF package

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
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
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//PDF Logic
public class VacancyListPDFHandler extends AbstractITextPostPdfHandler {
    private RecruitmentService recruitmentService;
    public void setRecruitmentService(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }
    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }
    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }
    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        ListResult<VacancyItem> vacancyListResult = recruitmentService.getVacancyList(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(VacancyItem.VACANCY_ID, new CellData(commonLocalizer.localize("number"), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_APPROVAL_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.approvalStatus), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_CONTRACT_FROM, new CellData(commonLocalizer.localize(PdfLocalizationName.contractStart), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_CONTRACT_TO, new CellData(commonLocalizer.localize(PdfLocalizationName.contractEnd), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.endDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_GENDER, new CellData(commonLocalizer.localize(PdfLocalizationName.sexDesire), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_JOB_TITLE, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_JOB_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.vacancyJobType), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_MANAGER, new CellData(commonLocalizer.localize(PdfLocalizationName.orderedBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_POSITION, new CellData(hrmsLocalizer.localize(PdfLocalizationName.vacancyPosition), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_PROPOSED_SALARY, new CellData(commonLocalizer.localize("proposedSalary"), Element.ALIGN_RIGHT));
        mapColumnHeader.put(VacancyItem.VACANCY_REQUIRED_DEGREE, new CellData(commonLocalizer.localize(PdfLocalizationName.vacancyRequiredDegree), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDateField), Element.ALIGN_LEFT));
    //    mapColumnHeader.put(VacancyItem.VACANCY_STATUS, new CellData(commonLocalizer.localize("status"), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.vacancyType), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_CREATED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_MODIFIED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(VacancyItem.VACANCY_MODIFIED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader); //Custom Fields
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(mapColumnHeader::containsKey)
                .map(mapColumnHeader::get)
                .collect(Collectors.toList());

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (VacancyItem item : vacancyListResult.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_ID)) {
                mapColumns.put(VacancyItem.VACANCY_ID, item.getNumberData() != null ? new CellData(getResultOrLongDash(item.getNumberData().getNumberString()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_JOB_TITLE)) {
                mapColumns.put(VacancyItem.VACANCY_JOB_TITLE, new CellData(getResultOrLongDash(item.getJobTitle()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_APPROVER)) {
                mapColumns.put(VacancyItem.VACANCY_APPROVER, new CellData(getResultOrLongDash(item.getCurrentApproverEmployeeName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_APPROVAL_STATUS)) {
                mapColumns.put(VacancyItem.VACANCY_APPROVAL_STATUS, new CellData(item.getOverallStatus() != null ? item.getOverallStatus().getCode() : "", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_START_DATE)) {
                mapColumns.put(VacancyItem.VACANCY_START_DATE, item.getStartDate() != null ? new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getStartDate())) : dateFormat(item.getStartDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_END_DATE)) {
                mapColumns.put(VacancyItem.VACANCY_END_DATE, item.getEndDate() != null ? new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getEndDate())) : dateFormat(item.getEndDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
//            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_STATUS)) {
//                mapColumns.put(VacancyItem.VACANCY_STATUS, item.getStatus()
//                        != null ? new CellData(getResultOrLongDash(referenceWfmMessageSource.localize(item.getStatus().getCode(), item.getStatus().getName())), Element.ALIGN_LEFT)
//                        : new CellData("—", Element.ALIGN_LEFT));
//            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_MANAGER)) {
                mapColumns.put(VacancyItem.VACANCY_MANAGER, item.getManager() != null ? new CellData(getResultOrLongDash(item.getManager().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }

            if (panelTools.getColumnCodeName().contains(VacancyItem.PROJECT)) {
                mapColumns.put(VacancyItem.PROJECT, new CellData(getResultOrLongDash(item.getProjectName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_GENDER)) {
                mapColumns.put(VacancyItem.VACANCY_GENDER, new CellData(item.getGender() != null ? commonLocalizer.localize(item.getGender().toLowerCase()) : "", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_PROPOSED_SALARY)) {
                mapColumns.put(VacancyItem.VACANCY_PROPOSED_SALARY, new CellData(getResultOrLongDash(item.getProposedSalary()), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_JOB_REQUIREMENT)) {
                mapColumns.put(VacancyItem.VACANCY_JOB_REQUIREMENT, new CellData(getResultOrLongDash(item.getJobRequirements()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CONTRACT_FROM)) {
                mapColumns.put(VacancyItem.VACANCY_CONTRACT_FROM, item.getContractFrom() != null ? new CellData(dateFormat(item.getContractFrom()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CONTRACT_TO)) {
                mapColumns.put(VacancyItem.VACANCY_CONTRACT_TO, item.getContractTo() != null ? new CellData(dateFormat(item.getContractTo()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_TYPE)) {
                mapColumns.put(VacancyItem.VACANCY_TYPE, new CellData(getResultOrLongDash(item.getVacancyTypeName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_POSITION)) {
                mapColumns.put(VacancyItem.VACANCY_POSITION, item.getPositionItem() != null ? new CellData(getResultOrLongDash(item.getPositionItem().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_LOCATION)) {
                mapColumns.put(VacancyItem.VACANCY_LOCATION, item.getLocationItem() != null ? new CellData(getResultOrLongDash(item.getLocationItem().getName()).replace(",null", ""), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_JOB_TYPE)) {
                mapColumns.put(VacancyItem.VACANCY_JOB_TYPE, item.getJobType() != null ? new CellData(getResultOrLongDash(item.getJobType().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_REQUIRED_DEGREE)) {
                mapColumns.put(VacancyItem.VACANCY_REQUIRED_DEGREE, item.getRequiredDegree() != null ? new CellData(getResultOrLongDash(item.getRequiredDegree().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_DEPARTMENT)) {
                mapColumns.put(VacancyItem.VACANCY_DEPARTMENT, item.getDepartment() != null ? new CellData(getResultOrLongDash(item.getDepartment().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CURRENCY)) {
                mapColumns.put(VacancyItem.VACANCY_CURRENCY, item.getCurrency() != null ? new CellData(getResultOrLongDash(item.getCurrency().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CREATED_DATE)) {
                mapColumns.put(VacancyItem.VACANCY_CREATED_DATE, item.getCreatedDate() != null ? new CellData(longDateFormat(item.getCreatedDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CREATED_BY)) {
                mapColumns.put(VacancyItem.VACANCY_CREATED_BY, item.getCreatedBy() != null ? new CellData(item.getCreatedBy(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_MODIFIED_BY)) {
                mapColumns.put(VacancyItem.VACANCY_MODIFIED_BY, item.getModifiedBy() != null ? new CellData(item.getModifiedBy(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_MODIFIED_DATE)) {
                mapColumns.put(VacancyItem.VACANCY_MODIFIED_DATE, item.getModifiedDate() != null ? new CellData(longDateFormat((item.getModifiedDate())), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }

            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);
            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(mapColumns::containsKey)
                    .map(mapColumns::get)
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }
    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParameter.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("vacancies");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("VacancyListPDF_" + dateFormat(new Date()));
    }
}
