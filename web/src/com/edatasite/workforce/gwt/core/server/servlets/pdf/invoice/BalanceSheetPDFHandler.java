package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet.BalancesheetSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/27/11
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class BalanceSheetPDFHandler extends AbstractITextPostPdfHandler {

    private AccountingService accountingService;
    private CurrencyManager currencyManager;

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    public void setCurrencyManager(CurrencyManager currencyManager) {
        this.currencyManager = currencyManager;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        String format = ServerUtils.getShortDateFormat(userManager.getUser());
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        Integer currencyId = filterParametrs.getCurrencyID();
        EdsCurrency currency = currencyManager.getCurrency(currencyId);

        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());

        if (filterParametrs.isShowBudget()) {
            BalanceSheetSummary sheetSummary = accountingService.getBalanceSheetSummary(new DateNonConvertable(startDate), new DateNonConvertable(endDate), filterParametrs.isActualDue(), filterParametrs.getDepartmentId(), filterParametrs.getProjectId(), currency.getObjectID());
            pdfData.setCustomData(getBalanceSheetSummaryData(sheetSummary, filterParametrs.isZeroAvoided()));
        } else {
            BalanceSheet sheet = accountingService.getBalanceSheet(new DateNonConvertable(startDate), new DateNonConvertable(endDate), filterParametrs.isActualDue(), filterParametrs.getDepartmentId(), filterParametrs.getProjectId(), currency.getObjectID());
            pdfData.setCustomListData(getBalanceSheetCustomListData(sheet, filterParametrs.isZeroAvoided()));
            pdfData.setCustomData(getBalanceSheetCustomData(sheet));
        }

        pdfData.setExtraData(commonLocalizer.localize(PdfLocalizationName.figuresIn) + " (" + currency.getName() + ")");
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            pdfData.setCurrentDate(commonLocalizer.localize(PdfLocalizationName.asAt) + " " + ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(endDate, format)));
        } else {
            pdfData.setCurrentDate(commonLocalizer.localize(PdfLocalizationName.asAt) + " " + ServerUtils.dateFormat(endDate, format));
        }
//        pdfData.setCurrentDate(commonLocalizer.localize(PdfLocalizationName.asAt) + " " + ServerUtils.dateFormat(endDate, format));
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }

    private HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> getBalanceSheetCustomListData(BalanceSheet sheet, boolean zeroAvoided) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        DecimalFormat format = getPriceScaleNumberFormat(financialSettings);

        HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customListData = new LinkedHashMap<>();
        for (BalancesheetSettingsItem setting : sheet.getSettings().getSettings()) {
            LinkedList<HashMap<String, CustomisedITextTable>> rows = new LinkedList<>();
            for (BalancesheetSettingsItem item : setting.getItems()) {
                if (sheet.getItemByKey(item.getCode()) != null && sheet.getItemByKey(item.getCode()).getItems().length > 0) {
                    HashMap<String, CustomisedITextTable> row = new LinkedHashMap<>();
                    row.put(item.getCode().replace("&", "and"), createRows(sheet.getItemByKey(item.getCode()), zeroAvoided, format));
                    rows.add(row);
                }
            }
            customListData.put(Constants.ASSETS.equals(setting.getCode()) ? "ASSETS" : "LIABILITY", rows);
        }
        return customListData;
    }

    private HashMap<String, CustomisedITextTable> getBalanceSheetCustomData(BalanceSheet sheet) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        DecimalFormat format = getPriceScaleNumberFormat(financialSettings);

        HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();
        for (BalancesheetSettingsItem setting : sheet.getSettings().getSettings()) {
            CustomisedITextTable table = new CustomisedITextTable();
            if (Constants.ASSETS.equals(setting.getCode())) {
                table.setName(setting.getTitle());

                Map<String, String> headerMap = new HashMap<>();
                headerMap.put(PDFConstants.COLUMN_NAME, sheet.getTotalAsset().getName());
                headerMap.put(PDFConstants.COLUMN_VALUE, getValueAsString(sheet.getTotalAsset().getValue(), format));
                table.setHeader(headerMap);

                customData.put("ASSETS", table);
            } else {
                table.setName(setting.getTitle());

                Map<String, String> headerMap = new HashMap<>();
                headerMap.put(PDFConstants.COLUMN_NAME, sheet.getTotalLiability().getName());
                headerMap.put(PDFConstants.COLUMN_VALUE, getValueAsString(sheet.getTotalLiability().getValue(), format));
                table.setHeader(headerMap);

                customData.put("LIABILITY", table);
            }
        }
        return customData;
    }

    private CustomisedITextTable createRows(BalanceSheetItem parent, boolean zeroAvoided, DecimalFormat format) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(parent.getName());

        HashMap<String, String> footer = new LinkedHashMap<>();
        footer.put(PDFConstants.COLUMN_NAME, parent.getTotal().getName().replace("&", "and"));
        footer.put(PDFConstants.COLUMN_VALUE, getValueAsString(parent.getTotal().getValue(), format));
        table.setHeader(footer);

        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        for (BalanceSheetInnerItem item : parent.getItems()) {
            if (zeroAvoided) {
                if (item.getValue() != null && BigDecimal.ZERO.compareTo(item.getValue()) == 0) {
                    continue;
                }
            }
            table.addRow(item.getName().replace("&", "and"), getValueAsString(item.getValue(), format));
        }
        return table;
    }

    private HashMap<String, CustomisedITextTable> getBalanceSheetSummaryData(BalanceSheetSummary sheet, boolean zeroAvoided) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        DecimalFormat format = getPriceScaleNumberFormat(financialSettings);

        HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();

        CustomisedITextTable assetsTable = new CustomisedITextTable();
        assetsTable.setName(commonLocalizer.localize(PdfLocalizationName.assets));

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(PDFConstants.COLUMN_NAME, sheet.getAssets().getTotal().getName());
        headerMap.put(PDFConstants.COLUMN_VALUE, getValueAsString(sheet.getAssets().getTotal().getValue(), format));
        assetsTable.setHeader(headerMap);

        assetsTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        if (sheet.getAssets() != null && sheet.getAssets().getItems().length > 0) {
            for (BalanceSheetInnerItem item : sheet.getAssets().getItems()) {
                if (zeroAvoided) {
                    if (item.getValue() != null && BigDecimal.ZERO.compareTo(item.getValue()) == 0) {
                        continue;
                    }
                }
                assetsTable.addRow(item.getName(), getValueAsString(item.getValue(), format));
            }
        }
        customData.put("ASSETS", assetsTable);

        CustomisedITextTable liabTable = new CustomisedITextTable();
        liabTable.setName(accountingLocalizer.localize(PdfLocalizationName.equityLiabilities));

        Map<String, String> header2Map = new HashMap<>();
        header2Map.put(PDFConstants.COLUMN_NAME, sheet.getLiabilities().getTotal().getName());
        header2Map.put(PDFConstants.COLUMN_VALUE, getValueAsString(sheet.getLiabilities().getTotal().getValue(), format));
        liabTable.setHeader(header2Map);

        liabTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        if (sheet.getLiabilities() != null && sheet.getLiabilities().getItems().length > 0) {
            for (BalanceSheetInnerItem item : sheet.getLiabilities().getItems()) {
                if (zeroAvoided) {
                    if (item.getValue() != null && BigDecimal.ZERO.compareTo(item.getValue()) == 0) {
                        continue;
                    }
                }
                liabTable.addRow(item.getName(), getValueAsString(item.getValue(), format));
            }
        }
        customData.put("LIABILITY", liabTable);

        return customData;
    }

    private String getValueAsString(BigDecimal value, DecimalFormat numberFormat) {
        return value.compareTo(AccountingConstants.ZERO) >= 0 ? numberFormat.format(value) : "(" + numberFormat.format(value.abs()) + ")";
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof ListingFilterParameter) {
            return ((ListingFilterParameter) object).getTemplateID();
        } else {
            return null;
        }
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.BALANCE_SHEET;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Balance_Sheet_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.balanceSheet);
    }
}
