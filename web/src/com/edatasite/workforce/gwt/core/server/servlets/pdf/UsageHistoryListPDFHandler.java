package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Rinat
 * Date: 15.09.2011
 * Time: 20:11:02
 * To change this template use File | Settings | File Templates.
 */

public class UsageHistoryListPDFHandler extends AbstractITextPostPdfHandler {

    private MyAccountService usageService;

    public void setUsageService(MyAccountService usageService) {
        this.usageService = usageService;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        ListResult<UsagePlanItem> usagePlanList = usageService.getUsagePlans(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
//        mapColumnHeader.put(UsagePlanItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(UsagePlanItem.MODULES, new CellData(pdfWfmMessageSource.localize("modules"), Element.ALIGN_LEFT));
        mapColumnHeader.put(UsagePlanItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(UsagePlanItem.END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.endDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(UsagePlanItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(UsagePlanItem.USERS, new CellData(pdfWfmMessageSource.localize("users"), Element.ALIGN_LEFT));
        mapColumnHeader.put(UsagePlanItem.ESS_USERS, new CellData("ESS Users", Element.ALIGN_LEFT));
        mapColumnHeader.put(UsagePlanItem.NOACCESS_USERS, new CellData("Non Users", Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (UsagePlanItem item : usagePlanList.getList()) {
            Map<String, String> mapColumns = new HashMap<>();
            /*if (panelTools.getColumnCodeName().contains(UsagePlanItem.NAME)){
                mapColumns.put(UsagePlanItem.NAME, getResultOrLongDash(usagePlans.getCompName()));
            }*/
            if (panelTools.getColumnCodeName().contains(UsagePlanItem.MODULES)){
                final StringBuilder columnValue = new StringBuilder();
                if (item.isAccountsModule()) {
                    columnValue.append("Accounts");
                }
                if (item.isSalesModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append("Sales");
                }
                if (item.isHumansModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append("Humans");
                }
                if (item.isProjectModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append("Projects");
                }

                if (item.isPayrollModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append("Payroll");
                }
                mapColumns.put(UsagePlanItem.MODULES, getResultOrLongDash(columnValue.toString()));
            }
            if (panelTools.getColumnCodeName().contains(UsagePlanItem.START_DATE)){
                mapColumns.put(UsagePlanItem.START_DATE, dateFormat(item.getStartDate()));
            }
            if (panelTools.getColumnCodeName().contains(UsagePlanItem.END_DATE)){
                mapColumns.put(UsagePlanItem.END_DATE, dateFormat(item.getEndDate()));
            }
            if (panelTools.getColumnCodeName().contains(UsagePlanItem.STATUS)){
                StringBuilder status = new StringBuilder(item.getStatus() != null ? item.getStatus() : "");
                status.append(" ("+item.getPeriodType()+")");
                mapColumns.put(UsagePlanItem.STATUS, getResultOrLongDash(status.toString()));
            }
            if (panelTools.getColumnCodeName().contains(UsagePlanItem.USERS)){
                mapColumns.put(UsagePlanItem.USERS, item.getUserCount() != null ? String.valueOf(item.getUserCount()) : "—");
            }
            if (panelTools.getColumnCodeName().contains(UsagePlanItem.ESS_USERS)){
                mapColumns.put(UsagePlanItem.ESS_USERS, item.getEssUserCount() != null ? String.valueOf(item.getEssUserCount()) : "—");
            }
            if (panelTools.getColumnCodeName().contains(UsagePlanItem.NOACCESS_USERS)){
                mapColumns.put(UsagePlanItem.NOACCESS_USERS, item.getNonAccessUserCount() != null ? String.valueOf(item.getNonAccessUserCount()) : "—");
            }
            List<String> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new String[]{}));
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
        return pdfWfmMessageSource.localize("subscriptionHistoryList");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_SubscriptionHistory_" + dateFormat(new Date()));
    }
}
