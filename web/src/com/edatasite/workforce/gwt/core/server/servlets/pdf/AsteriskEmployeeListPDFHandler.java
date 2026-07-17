package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
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

public class AsteriskEmployeeListPDFHandler extends AbstractITextPostPdfHandler {
    private ProfileService profileService;

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }



    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "Asterisk employee list" + dateFormat(new Date()));
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest httpRequest) {
        return false;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.taskListTableName);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter listingFilterParameter = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        listingFilterParameter.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
//        Integer asteriskSettingsId = 2;
        ListResult<EmployeeListItem> list = profileService.getAsteriskEmployeeList(listingFilterParameter.getRelationID(), listingFilterParameter);

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(EmployeeListItem.EMPLOYEE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.USERNAME, new CellData(commonLocalizer.localize(PdfLocalizationName.internalNumber), Element.ALIGN_LEFT));

        ListPanelToolRpc listPanelToolRpc = listingFilterParameter.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(listPanelToolRpc.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = listPanelToolRpc.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (EmployeeListItem item: list.getList()){
            Map<String, CellData> columnMap = new HashMap<>();
            if (listPanelToolRpc.getColumnCodeName().contains(EmployeeListItem.EMPLOYEE_NAME)) {
                columnMap.put(EmployeeListItem.EMPLOYEE_NAME, new CellData(getResultOrLongDash(item.getFullName()),Element.ALIGN_LEFT));
            }
            if (listPanelToolRpc.getColumnCodeName().contains(EmployeeListItem.STATUS)) {
                columnMap.put(EmployeeListItem.STATUS, new CellData(getResultOrLongDash(item.getStatus()), Element.ALIGN_LEFT));
            }
            if (listPanelToolRpc.getColumnCodeName().contains(EmployeeListItem.USERNAME)) {
                columnMap.put(EmployeeListItem.USERNAME, new CellData(getResultOrLongDash(item.getAsteriskUsername()), Element.ALIGN_LEFT));
            }
            CustomFieldsUtils.setCustomFieldsPdfTableRows(listPanelToolRpc.getListViewCustomFields(), columnMap, listPanelToolRpc.getColumnCodeName(), item, company);
            List<CellData> columns = listPanelToolRpc.getColumnCodeName().stream()
                    .filter(columnCode -> columnMap.containsKey(columnCode))
                    .map(columnCode -> columnMap.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[] {}));
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return  pdfData;
    }
}
