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
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupPlacementListViewPDFHandler extends AbstractITextPostPdfHandler {
    @Autowired
    private HrmsService hrmsService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        ListResult<GroupPlacementItem> placementListResult = hrmsService.getGroupPlacementList(filterParametrs);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(GroupPlacementItem.NUMBER, new CellData(commonLocalizer.localize("number"), Element.ALIGN_LEFT));
        mapColumnHeader.put(GroupPlacementItem.STATUS, new CellData(commonLocalizer.localize("status"), Element.ALIGN_LEFT));
        mapColumnHeader.put(GroupPlacementItem.APPROVER, new CellData(commonLocalizer.localize("approver"), Element.ALIGN_LEFT));
        mapColumnHeader.put(GroupPlacementItem.DATE, new CellData(commonLocalizer.localize("date"), Element.ALIGN_LEFT));
        mapColumnHeader.put(GroupPlacementItem.CREATOR, new CellData(commonLocalizer.localize("creator"), Element.ALIGN_LEFT));
        mapColumnHeader.put(GroupPlacementItem.CREATED_DATE, new CellData(commonLocalizer.localize("createdDate"), Element.ALIGN_LEFT));
        mapColumnHeader.put(GroupPlacementItem.UPDATER, new CellData(commonLocalizer.localize("modifiedBy"), Element.ALIGN_LEFT));
        mapColumnHeader.put(GroupPlacementItem.UPDATED_DATE, new CellData(commonLocalizer.localize("modifiedDate"), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (GroupPlacementItem item : placementListResult.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.NUMBER)) {
                mapColumns.put(GroupPlacementItem.NUMBER, new CellData(getResultOrLongDash(item.getPlacementCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.STATUS)) {
                String status = " ";
                if (item.getOverallStatus() != null && item.getOverallStatus().getCode() != null) {
                    switch (item.getOverallStatus().getCode()) {
                        case GROUP_PLACEMENT_APPROVED -> status = "approved";
                        case GROUP_PLACEMENT_REJECTED -> status = "rejected";
                        case GROUP_PLACEMENT_SUBMITTED -> status = "waitingForApproval";
                        case GROUP_PLACEMENT_DRAFT -> status = "draft";
                    }
                }
                mapColumns.put(GroupPlacementItem.STATUS, new CellData(getResultOrLongDash(commonLocalizer.localize(status)), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.APPROVER)) {
                mapColumns.put(GroupPlacementItem.APPROVER, new CellData(getResultOrLongDash(item.getApproverEmployee() != null ? item.getApproverEmployee().getName() : "N/A"), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.DATE)) {
                String format = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(item.getDate()), user))
                        : ServerUtils.shortDateFormat(user.getUserDate(item.getDate()), user);
                mapColumns.put(GroupPlacementItem.DATE, new CellData(getResultOrLongDash(format), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.CREATOR)) {
                mapColumns.put(GroupPlacementItem.CREATOR, new CellData(getResultOrLongDash(item.getCreator() != null ? item.getCreator().getName() : "N/A"), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.CREATED_DATE)) {
                String format = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(item.getCreatedDate().getDate()), user))
                        : ServerUtils.shortDateFormat(user.getUserDate(item.getCreatedDate().getDate()), user);
                mapColumns.put(GroupPlacementItem.CREATED_DATE, new CellData(getResultOrLongDash(format), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.UPDATER)) {
                mapColumns.put(GroupPlacementItem.UPDATER, new CellData(getResultOrLongDash(item.getUpdater() != null ? item.getUpdater().getName() : "N/A"), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(GroupPlacementItem.UPDATED_DATE)) {
                String format = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(item.getUpdatedDate().getDate()), user))
                        : ServerUtils.shortDateFormat(user.getUserDate(item.getUpdatedDate().getDate()), user);
                mapColumns.put(GroupPlacementItem.UPDATED_DATE, new CellData(getResultOrLongDash(format), Element.ALIGN_LEFT));
            }
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
        return commonLocalizer.localize(PdfLocalizationName.placement);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName((user.getFirstName() + "_" + user.getLastName() + "_GroupPlacementList_" + dateFormat(user.getUserDate())).replace("/", "_"));
    }

}
