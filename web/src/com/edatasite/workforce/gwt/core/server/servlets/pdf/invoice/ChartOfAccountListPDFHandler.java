package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Abror Abdukadirov
 * Date: 10.11.2017 18:57
 */
public class ChartOfAccountListPDFHandler extends AbstractITextPostPdfHandler implements AccountingConstants {

    @Autowired
    private AccountingService accountingService;

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ChartOfAccountList_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        String type1 = "";
        if (filterParametrs.getAccountType() != null) {
            String type = "";
            if (filterParametrs.getAccountType().equals(AccountingConstants.ASSETS)) {
                type = "Assets";
            } else if (filterParametrs.getAccountType().equals(LIABILITIES)) {
                type = "Liabilitiyes";
            } else if (filterParametrs.getAccountType().equals(AccountingConstants.EQUITY)) {
                type = "Equity";
            } else if (filterParametrs.getAccountType().equals(EXPENSES)) {
                type = "Expenses";
            } else if (filterParametrs.getAccountType().equals(REVENUE)) {
                type = "Revenue";
            }

            type1 = type;
        }
        String params = (!"".equals(type1) ? " (" + type1 + ")" : "");
        return (commonLocalizer.localizeWithParam(PdfLocalizationName.chartOfAccountsList,
                uploadManager.getUser().getFullName()) + params);
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = uploadManager.getUser().getCompany().getCompanySettings();

        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(MAX_PDF_OR_EXCEL_LIMIT);
        }
        filterParametrs.setFromExcelPDF(true);
        List<AccountListItem> accountItems = accountingService.getAccountList(filterParametrs).getList();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(AccountListItem.CODE, new CellData(accountingLocalizer.localizeWithParam(PdfLocalizationName.code), Element.ALIGN_LEFT));
        mapColumnHeader.put(AccountListItem.NAME, new CellData(accountingLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(AccountListItem.PARENT, new CellData(commonLocalizer.localize(PdfLocalizationName.parent), Element.ALIGN_LEFT));
        mapColumnHeader.put(AccountListItem.TYPE, new CellData(accountingLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(AccountListItem.CURRENCY, new CellData(accountingLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(AccountListItem.BALANCE, new CellData(accountingLocalizer.localize(PdfLocalizationName.balance), Element.ALIGN_RIGHT));
        mapColumnHeader.put(AccountListItem.LAST_UPDATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = new ArrayList<>();
        for (String columnCodeName : panelTools.getColumnCodeName()) {
            if (mapColumnHeader.containsKey(columnCodeName)) {
                header.add(mapColumnHeader.get(columnCodeName));
            }
        }
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (accountItems != null && accountItems.size() > 0) {
            for (AccountListItem item : accountItems) {
                Map<String, CellData> mapColumns = new HashMap<>();

                String balance = String.valueOf(0d);
                if (item.getBalance() != null) {
                    if (item.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                        balance = "(" + (new BigDecimal(-1)).multiply(item.getBalance().setScale(2, RoundingMode.HALF_UP)) + ")";
                    } else {
                        balance = String.valueOf(item.getBalance().setScale(2, RoundingMode.HALF_UP));
                    }
                }
                if (panelTools.getColumnCodeName().contains(AccountListItem.CODE)) {
                    mapColumns.put(AccountListItem.CODE, new CellData(getResultOrLongDash(item.getCode()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(AccountListItem.NAME)) {
                    mapColumns.put(AccountListItem.NAME, new CellData(getResultOrLongDash(item.getName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(AccountListItem.PARENT)) {
                    mapColumns.put(AccountListItem.PARENT, new CellData(getResultOrLongDash(item.getParentName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(AccountListItem.TYPE)) {
                    mapColumns.put(AccountListItem.TYPE, new CellData(getResultOrLongDash(item.getAccountType()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(AccountListItem.CURRENCY)) {
                    mapColumns.put(AccountListItem.CURRENCY, new CellData(getResultOrLongDash(item.getCurrency()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(AccountListItem.BALANCE)) {
                    mapColumns.put(AccountListItem.BALANCE, new CellData(balance, Element.ALIGN_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(AccountListItem.LAST_UPDATED_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumns.put(AccountListItem.LAST_UPDATED_DATE, item.getLastUpdatedDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(item.getLastUpdatedDate())), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        mapColumns.put(AccountListItem.LAST_UPDATED_DATE, item.getLastUpdatedDate() != null ? new CellData(dateFormat(item.getLastUpdatedDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
//                    mapColumns.put(AccountListItem.LAST_UPDATED_DATE, item.getLastUpdatedDate() != null ? new CellData(dateFormat(item.getLastUpdatedDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                List<CellData> columns = new ArrayList<>();
                for (String columnCodeName : panelTools.getColumnCodeName()) {
                    if (mapColumns.containsKey(columnCodeName)) {
                        columns.add(mapColumns.get(columnCodeName));
                    }
                }
                CellData[] columnStrings = new CellData[columns.size()];
                columns.toArray(columnStrings);
                tableList.addPdfTableRows(columnStrings);
            }
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }
}
