package com.edatasite.workforce.gwt.core.server.servlets.pdf;
//download PDF logic

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
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
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PositionListPDFHandler extends AbstractITextPostPdfHandler {
    private HrmsService hrmsService;
    public void setHrmsService(HrmsService hrmsService) {
        this.hrmsService = hrmsService;
    }
    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        filterParametrs.setFromExcelPDF(true);
        ListResult<PositionItem> positionList = hrmsService.getPositionList(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        //Mapping
        mapColumnHeader.put(PositionItem.POSITION_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.POSITION_TITLE, new CellData(commonLocalizer.localize(PdfLocalizationName.position), Element.ALIGN_LEFT)); //Position name
        mapColumnHeader.put(PositionItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT)); //Status
        mapColumnHeader.put(PositionItem.DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.EMPLOYEE_COUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.headCount), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.MODIFIED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.MODIFIED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.CREATED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(PositionItem.POSITION_COUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.vacancyCaunt), Element.ALIGN_LEFT)); //need to develop
        mapColumnHeader.put(PositionItem.TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));

        //CustomFields
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader); //Custom Fields

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(mapColumnHeader::containsKey)
                .map(mapColumnHeader::get)
                .collect(Collectors.toList());

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (PositionItem positions : positionList.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();

            if (panelTools.getColumnCodeName().contains(PositionItem.POSITION_CODE)) {
                mapColumns.put(PositionItem.POSITION_CODE, new CellData(positions.getNumberData() == null ? "" : positions.getNumberData().getNumberString()));
            }
            if (panelTools.getColumnCodeName().contains(PositionItem.POSITION_TITLE)) {
                mapColumns.put(PositionItem.POSITION_TITLE, new CellData(positions.getName() == null ? "" : positions.getName()));
            }
            if (panelTools.getColumnCodeName().contains(PositionItem.DEPARTMENT)) {
                mapColumns.put(PositionItem.DEPARTMENT, new CellData(positions.getDepartment() == null ? "" : positions.getDepartment().getName()));
            }
            if (panelTools.getColumnCodeName().contains(PositionItem.STATUS)) {
                mapColumns.put(PositionItem.STATUS, new CellData(positions.getStatus() == null || positions.getStatus().getName() == null ? "" : positions.getStatus().getName()));
            }
            if (panelTools.getColumnCodeName().contains(PositionItem.LOCATION)) {
                mapColumns.put(PositionItem.LOCATION, new CellData(positions.getLocation() == null || positions.getLocation().getName() == null ? "" : positions.getLocation().getName()));
            }
            //Created Date
            if (panelTools.getColumnCodeName().contains(PositionItem.CREATED_DATE)) {
                if ("uz".equalsIgnoreCase(ServerUtils.getUserLocale().getLanguage())) {
                    mapColumns.put(PositionItem.CREATED_DATE, new CellData(positions.getCreatedDate() == null ? "—" : ServerUtils.convertToUzbDateFormat(longDateFormat(positions.getCreatedDate()))));
                } else {
                    mapColumns.put(PositionItem.CREATED_DATE, new CellData(positions.getCreatedDate() == null ? "—" : longDateFormat(positions.getCreatedDate())));
                }
            }
            //Created by
            if (panelTools.getColumnCodeName().contains(PositionItem.CREATED_BY)) {
                mapColumns.put(PositionItem.CREATED_BY, new CellData(positions.getCreatedBy() == null ? "—" : positions.getCreatedBy()));
            }
            //Modified Date
            if (panelTools.getColumnCodeName().contains(PositionItem.MODIFIED_DATE)) {
                if ("uz".equalsIgnoreCase(ServerUtils.getUserLocale().getLanguage())) {
                    mapColumns.put(PositionItem.MODIFIED_DATE, new CellData(positions.getModifiedDate() == null ? "—" : ServerUtils.convertToUzbDateFormat(longDateFormat(positions.getModifiedDate()))));
                } else {
                    mapColumns.put(PositionItem.MODIFIED_DATE, new CellData(positions.getModifiedDate() == null ? "—" : longDateFormat(positions.getModifiedDate())));
                }
            }
            //Modified by
            if (panelTools.getColumnCodeName().contains(PositionItem.MODIFIED_BY)) {
                mapColumns.put(PositionItem.MODIFIED_BY, new CellData(positions.getModifiedBy() == null ? "—" : positions.getModifiedBy()));
            }
            // Vacant count
            if (panelTools.getColumnCodeName().contains(PositionItem.POSITION_COUNT)) {
                mapColumns.put(PositionItem.POSITION_COUNT, new CellData(positions.getEmployeeCount() != null ? String.valueOf(positions.getEmployeeCount()) : "—"));
            }
            // Head Count
            if (panelTools.getColumnCodeName().contains(PositionItem.EMPLOYEE_COUNT)) {
                mapColumns.put(PositionItem.EMPLOYEE_COUNT, new CellData(positions.getHeadCount() != null ? String.valueOf(positions.getHeadCount()) : "—"));
            }
            // Type
            if (panelTools.getColumnCodeName().contains(PositionItem.TYPE)) {
                mapColumns.put(PositionItem.TYPE, new CellData(positions.getType() == null ? "—" : positions.getType().getName()));
            }

            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), positions, company);
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
        return commonLocalizer.localize(PdfLocalizationName.positionListTableName);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Position List");
    }
}
