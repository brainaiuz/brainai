package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.11.2008
 * Time: 19:51:11
 * To change this template use File | Settings | File Templates.
 */
public class ExpensesListPDFHandler extends AbstractITextPostPdfHandler implements AccountingConstants {

    @Autowired
    private ExpenseService expenseService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        filterParametrs.setStartDate(parseFilterParameterDate(filterParametrs.getStartDateNC()));
        filterParametrs.setEndDate(parseFilterParameterDate(filterParametrs.getEndDateNC()));

        ListResult<ExpenseReportsListItem> reportList = expenseService.getExpenseReportsDataFromSolr(filterParametrs);

        List<ExpenseReportsListItem> items = reportList.getList();
        List<ExpenseReportsListItem> subitems = ListUtils.getSublist(items, filterParametrs.getStart(), filterParametrs.getLimit());
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(NUMBER_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(TITLE_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.title), Element.ALIGN_LEFT));
        mapColumnHeader.put(PERIOD_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(PROJECT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedProject_exportFiles), Element.ALIGN_LEFT));
        mapColumnHeader.put(REPORTER_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.reporter), Element.ALIGN_LEFT));
        mapColumnHeader.put(APPROVER_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        mapColumnHeader.put(STATUS_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(ORIGINAL_AMOUNT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.originalAmount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(PAID_AMOUNT_COLUMN, new CellData(accountingLocalizer.localize(PdfLocalizationName.paidAmount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(DUE_AMOUNT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(TAX_AMOUNT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.taxAmount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(RELATED_PO, new CellData(accountingLocalizer.localize(PdfLocalizationName.relatedPO), Element.ALIGN_LEFT));
        mapColumnHeader.put(FIXED_ASSET, new CellData(commonLocalizer.localize(PdfLocalizationName.fixedAsset), Element.ALIGN_LEFT));
        mapColumnHeader.put(SUPPLIER, new CellData(accountingLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(CURRENCY_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(TYPE_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        for (ExpenseReportsListItem report : subitems) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(NUMBER_COLUMN)) {
                mapColumns.put(NUMBER_COLUMN, report.getExpenseNumber() != null ? new CellData(getResultOrLongDash(report.getExpenseNumber()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TITLE_COLUMN)) {
                mapColumns.put(TITLE_COLUMN, new CellData(getResultOrLongDash(report.getTitle()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(PERIOD_COLUMN)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    mapColumns.put(PERIOD_COLUMN, report.getStartDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(shortDateFormat.format(report.getStartDate().getNonConvertedDate())), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                } else {
                    mapColumns.put(PERIOD_COLUMN, report.getStartDate() != null ? new CellData(shortDateFormat.format(report.getStartDate().getNonConvertedDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
//                mapColumns.put(PERIOD_COLUMN, report.getStartDate() != null ? new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(shortDateFormat.format(report.getStartDate().getNonConvertedDate())) : shortDateFormat.format(report.getStartDate().getNonConvertedDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(PROJECT_COLUMN)) {
                mapColumns.put(PROJECT_COLUMN, new CellData(getResultOrLongDash(report.getProjectName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(REPORTER_COLUMN)) {
                mapColumns.put(REPORTER_COLUMN, new CellData(escapeHtml(getResultOrLongDash(report.getReporterName())), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(APPROVER_COLUMN)) {
                mapColumns.put(APPROVER_COLUMN, report.getApproverSelectItem() != null ? new CellData(getResultOrLongDash(report.getApproverSelectItem().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(STATUS_COLUMN)) {
                mapColumns.put(STATUS_COLUMN, new CellData(getResultOrLongDash(report.getStatus()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(ORIGINAL_AMOUNT_COLUMN)) {
                mapColumns.put(ORIGINAL_AMOUNT_COLUMN, report.getTotal() != null ? new CellData(priceScaleFormat.format(report.getTotal()), Element.ALIGN_RIGHT) : new CellData(BigDecimal.ZERO.toString(), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(PAID_AMOUNT_COLUMN)) {
                mapColumns.put(PAID_AMOUNT_COLUMN, report.getPaidTotal() != null ? new CellData(priceScaleFormat.format(report.getPaidTotal()), Element.ALIGN_RIGHT) : new CellData(BigDecimal.ZERO.toString(), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(DUE_AMOUNT_COLUMN)) {
                mapColumns.put(DUE_AMOUNT_COLUMN, report.getDueTotal() != null ? new CellData(priceScaleFormat.format(report.getDueTotal()), Element.ALIGN_RIGHT) : new CellData(BigDecimal.ZERO.toString(), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(TAX_AMOUNT_COLUMN)) {
                mapColumns.put(TAX_AMOUNT_COLUMN, report.getTaxTotal() != null ? new CellData(priceScaleFormat.format(report.getTaxTotal()), Element.ALIGN_RIGHT) : new CellData(BigDecimal.ZERO.toString(), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(RELATED_PO)) {
                mapColumns.put(RELATED_PO, new CellData(getResultOrLongDash(report.getPurchaseOrderNumber())));
            }
            if (panelTools.getColumnCodeName().contains(FIXED_ASSET)) {
                mapColumns.put(FIXED_ASSET, report.getFixedAsset() != null ? new CellData(getResultOrLongDash(report.getFixedAsset().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(SUPPLIER)) {
                mapColumns.put(SUPPLIER, report.getSupplier() != null ? new CellData(getResultOrLongDash(report.getSupplier().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CURRENCY_COLUMN)) {
                mapColumns.put(CURRENCY_COLUMN, report.getExpenseCurrency() != null ? new CellData(getResultOrLongDash(report.getExpenseCurrency().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TYPE_COLUMN)) {
                mapColumns.put(TYPE_COLUMN, report.isCompanyExpense() ? new CellData(commonLocalizer.localizeAccounting("companyExpense"), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localizeAccounting("employeeExpense"), Element.ALIGN_LEFT));
            }
            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), report, company);
            
            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
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
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("expenseClaims");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Expense_Claim_Report_" + dateFormat(user.getUserDate()));
    }

}
