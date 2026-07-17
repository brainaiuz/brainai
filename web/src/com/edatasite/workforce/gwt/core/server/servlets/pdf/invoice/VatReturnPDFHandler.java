package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnTransferObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 17.01.12
 * Time: 16:28
 * To change this template use File | Settings | File Templates.
 */
public class VatReturnPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private AccountingService accountingService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        Date fromDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date toDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        BigDecimal flatPercent = BigDecimal.valueOf(Double.parseDouble(filterParametrs.getMessageStatus()));

        VatReturnTransferObject vatEFiling = accountingService.getVatReturnReport(new DateNonConvertable(fromDate),
                                             new DateNonConvertable(toDate), flatPercent);
        if (vatEFiling == null) {
            return null;
        }
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        }
        String currencySymbol = fs != null && fs.getCurrency() != null ? escapeHtml(fs.getCurrency().getSymbol()) : "";
        String currencyCode = fs != null && fs.getCurrency() != null ? escapeHtml(fs.getCurrency().getName()) : "";
        String date = commonLocalizer.localize(PdfLocalizationName.from) + " "
                + format.format(fromDate) + " "
                + commonLocalizer.localize(PdfLocalizationName.to) + " "
                + format.format(toDate);

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("DETAIL_TABLE", getDetailTable(vatEFiling, fromDate, toDate, company));
        customData.put("CALCULATION_TABLE", getCalculationTable(vatEFiling, company, fs));
        customData.put("EXCLUDE_VAT_TABLE", getExcludeVATTable(vatEFiling, company, fs));
        customData.put("EC_EXLUDE_VAT_TABLE", getEcExcludeVATTable(vatEFiling, company, fs));
        customData.put("HEADER_TABLE", getHeaderTable(company));
        pdfData.setCurrentDate(date);
        pdfData.setExtraData(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.figuresIn), " ", currencySymbol, "(", currencyCode, ")"));

        pdfData.setCustomData(customData);

        return pdfData;
    }

    private CustomisedITextTable getHeaderTable(EdsCompany company) {
        CustomisedITextTable headerTable = new CustomisedITextTable();

        headerTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        headerTable.addRowWithCode("DETAIL_TITLE", commonLocalizer.localize(PdfLocalizationName.vatReturnDetails), "");
        headerTable.addRowWithCode("CALCULATION_TITLE", commonLocalizer.localize(PdfLocalizationName.vatCalculations), "");
        headerTable.addRowWithCode("EXCLUDE_VAT_TITLE", commonLocalizer.localize(PdfLocalizationName.salesAndPurchasesExcludingVAT), "");

        if (ServerUtils.isArabicCompany(company)) {
            headerTable.addRowWithCode("EC_EXCLUDE_VAT_TITLE", commonLocalizer.localize(PdfLocalizationName.gccSuppliesAndPurchasesExcludingVAT), "");
        } else {
            headerTable.addRowWithCode("EC_EXCLUDE_VAT_TITLE", commonLocalizer.localize(PdfLocalizationName.ecSuppliesAndPurchasesExcludingVAT), "");
        }
        return headerTable;
    }

    //VAT Return Details table
    private CustomisedITextTable getDetailTable(VatReturnTransferObject vatEFiling, Date fromDate, Date toDate, EdsCompany company) {
        CustomisedITextTable detailTable = new CustomisedITextTable();

        SimpleDateFormat format;
        if (company != null && company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        }

        String registrationNo = getResultOrLongDash(vatEFiling.getRegistrationNumber());
        String vatSchema = getResultOrLongDash(vatEFiling.getVatScheme());
        String periodCoveredBy = getVatPeriodType(vatEFiling.getPeriodCovered());
        Date date = vatEFiling.getPaymentDueDate().getDate();

        detailTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        detailTable.addRowWithCode(VAT_REGISTRATION_NUMBER, commonLocalizer.localize(PdfLocalizationName.registrationNumber), registrationNo);
        detailTable.addRowWithCode("VAT_SCHEMA", commonLocalizer.localize(PdfLocalizationName.vatScheme), vatSchema);
        detailTable.addRowWithCode("PRIOD_COVERED_BY", commonLocalizer.localize(PdfLocalizationName.periodCoveredBy), periodCoveredBy);
        detailTable.addRowWithCode(FROM_DATE, commonLocalizer.localize(PdfLocalizationName.from), format.format(fromDate));
        detailTable.addRowWithCode(TO_DATE, commonLocalizer.localize(PdfLocalizationName.to), format.format(toDate));
        if (vatEFiling.getPaymentDueDate() != null) {
            detailTable.addRowWithCode(ACCOUNTING_VAT_RETURN_REPORT, commonLocalizer.localize(PdfLocalizationName.thisReturnAndAnyPayment),
                                      format.format(vatEFiling.getPaymentDueDate().getDate()));
        }

        return detailTable;
    }

    //VAT Calculations table
    private CustomisedITextTable getCalculationTable(VatReturnTransferObject vatEFiling, EdsCompany company, EdsFinancialSettings fs) {
        CustomisedITextTable calCustomTable = new CustomisedITextTable();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        calCustomTable.addColumnOrder(COLUMN_CODE, COLUMN_NAME, COLUMN_VALUE);
        calCustomTable.addRowWithCode("VAT_DUE_THIS_PERIOD", "1", commonLocalizer.localize(PdfLocalizationName.vatDueThisPeriod), priceScaleFormat.format(vatEFiling.getVatOnSalesAndOutputs()));
        String s = "";
        if (vatEFiling.isFlatRate()) {
            if (ServerUtils.isArabicCompany(company)) {
                s = commonLocalizer.localize(PdfLocalizationName.enterAmountForVATOnAnyGoodsGCC);
            } else {
                s = commonLocalizer.localize(PdfLocalizationName.enterAmountForVATOnAnyGoods);
            }
        } else {
            if (ServerUtils.isArabicCompany(company)) {
                s = commonLocalizer.localize(PdfLocalizationName.vatDueInThisPeriodOnAcquisitionsGCC);
            } else {
                s = commonLocalizer.localize(PdfLocalizationName.vatDueInThisPeriodOnAcquisitions);
            }
        }
        calCustomTable.addRowWithCode("GCC", "2", s, priceScaleFormat.format(vatEFiling.getVatFromECMemberStates()));
        calCustomTable.addRowWithCode("TOTAL_VAT_DUE", "3", commonLocalizer.localize(PdfLocalizationName.totalVATDue), priceScaleFormat.format(vatEFiling.getTotalVatDue()));
        String isFlatRate = "";
        if (vatEFiling.isFlatRate()) {
            isFlatRate = commonLocalizer.localize(PdfLocalizationName.vatReclaimedOnCapitalPurchases);
        } else {
            if (ServerUtils.isArabicCompany(company)) {
                isFlatRate = commonLocalizer.localize(PdfLocalizationName.vatReclaimedInThisPeriodGCC);
            } else {
                isFlatRate = commonLocalizer.localize(PdfLocalizationName.vatReclaimedInThisPeriod);
            }

        }
        calCustomTable.addRowWithCode("PERIOD_GCC", "4", isFlatRate, priceScaleFormat.format(vatEFiling.getVatOnPurchaseAndInputs()));
        String VatToReclaim = "";
        if (vatEFiling.getVatToReclaimFromCustoms().compareTo(AccountingConstants.ZERO) >= 0) {
            VatToReclaim = commonLocalizer.localize(PdfLocalizationName.vatToPayToCustoms);
        } else {
            VatToReclaim = commonLocalizer.localize(PdfLocalizationName.vatToReclaimFromCustoms);
        }

        calCustomTable.addRowWithCode("PAY_TO_CUSTOM", "5", VatToReclaim, priceScaleFormat.format(vatEFiling.getVatToReclaimFromCustoms()));

        return calCustomTable;
    }

    //Sales and Purchases Excluding VAT
    private CustomisedITextTable getExcludeVATTable(VatReturnTransferObject vatEFiling, EdsCompany company, EdsFinancialSettings fs) {
        CustomisedITextTable excludeCustomTable = new CustomisedITextTable();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        excludeCustomTable.addColumnOrder(COLUMN_CODE, COLUMN_NAME, COLUMN_VALUE);
        if (ServerUtils.isArabicCompany(company)) {
            excludeCustomTable.addRowWithCode("TOTAL_SALES_AND_ALL_GCC", "6", accountingLocalizer.localizeAccounting(PdfLocalizationName.totalValueOfSalesAndAllGCC), priceScaleFormat.format(vatEFiling.getTotalSalesAndOutputs()));
            excludeCustomTable.addRowWithCode("TOTAL_PURCHASES_AND_ALL_OTHER_GCC", "7", accountingLocalizer.localizeAccounting(PdfLocalizationName.totalValueOfPurchasesAndAllOtherGCC), priceScaleFormat.format(vatEFiling.getTotalPurchasesAndInputs()));
        } else {
            excludeCustomTable.addRowWithCode("TOTAL_SALES_AND_ALL_GCC", "6", accountingLocalizer.localizeAccounting(PdfLocalizationName.totalValueOfSalesAndAll), priceScaleFormat.format(vatEFiling.getTotalSalesAndOutputs()));
            excludeCustomTable.addRowWithCode("TOTAL_PURCHASES_AND_ALL_OTHER_GCC", "7", accountingLocalizer.localizeAccounting(PdfLocalizationName.totalValueOfPurchasesAndAllOther), priceScaleFormat.format(vatEFiling.getTotalPurchasesAndInputs()));
        }
        return excludeCustomTable;
    }

    //EC Supplies and Purchases Excluding VAT
    private CustomisedITextTable getEcExcludeVATTable(VatReturnTransferObject vatEFiling, EdsCompany company, EdsFinancialSettings fs) {
        CustomisedITextTable ecExcludeCustomTable = new CustomisedITextTable();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        ecExcludeCustomTable.addColumnOrder(COLUMN_CODE, COLUMN_NAME, COLUMN_VALUE);
        if (ServerUtils.isArabicCompany(company)) {
            ecExcludeCustomTable.addRowWithCode("TOTAL_SUPPLIES_OF_GOODS_GCC", "8", commonLocalizer.localize(PdfLocalizationName.totalValueOfAllSuppliesOfGoodsGCC), priceScaleFormat.format(vatEFiling.getTotalSupplies()));
            ecExcludeCustomTable.addRowWithCode("TOTAL_ASQUISITIONS_OF_GOODS_GCC", "9", commonLocalizer.localize(PdfLocalizationName.totalValueOfAllAcquisitionsOfGoodsGCC), priceScaleFormat.format(vatEFiling.getTotalAcquisitions()));
        } else {
            ecExcludeCustomTable.addRowWithCode("TOTAL_SUPPLIES_OF_GOODS_GCC", "8", commonLocalizer.localize(PdfLocalizationName.totalValueOfAllSuppliesOfGoods), priceScaleFormat.format(vatEFiling.getTotalSupplies()));
            ecExcludeCustomTable.addRowWithCode("TOTAL_ASQUISITIONS_OF_GOODS_GCC", "9", commonLocalizer.localize(PdfLocalizationName.totalValueOfAllAcquisitionsOfGoods), priceScaleFormat.format(vatEFiling.getTotalAcquisitions()));
        }
        if (vatEFiling.isFlatRate()) {
            ecExcludeCustomTable.addRowWithCode("AMOUNT_OF_VAT_YOU", "10", commonLocalizer.localize(PdfLocalizationName.theAmountOfVATYou), priceScaleFormat.format(vatEFiling.getFlatRateSchemeVatDifference()));
        }
        return ecExcludeCustomTable;
    }

    public String getVatPeriodType(String periodTypeCode) {
        if (AccountingConstants.MONTHLY1.equals(periodTypeCode)) {
            return commonLocalizer.localizeAccounting(PdfLocalizationName.monthly);
        } else if (AccountingConstants.QUARTERLY.equals(periodTypeCode)) {
            return commonLocalizer.localizeAccounting(PdfLocalizationName.quarterly);
        } else if (AccountingConstants.ANNUAL.equals(periodTypeCode)) {
            return commonLocalizer.localizeAccounting(PdfLocalizationName.annual);
        }
        return commonLocalizer.localizeAccounting(PdfLocalizationName.custom);
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.vatReturn);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.VAT_RETURN;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("VatReturn_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }
}
