package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlow;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlowItem;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sherzod on 1/14/2016.
 */
public class CashFlowPDFHandler extends AbstractITextPostPdfHandler {

    private final String OPERATING_ACTIVITIES = "OPERATING_ACTIVITIES";
    private final String NET_PROFIT = "NET_PROFIT";
    private final String CURRENT_ASSET = "CURRENT_ASSET";
    private final String PREPAYMENT = "PREPAYMENT";
    private final String CURRENT_LIABILITY = "CURRENT_LIABILITY";
    private final String TOTAL_OPERATING_ACTIVITIES = "TOTAL_OPERATING_ACTIVITIES";

    private final String INVESTING_ACTIVITIES = "INVESTING_ACTIVITIES";
    private final String ACCUMULATED_DEPRECIATION = "ACCUMULATED_DEPRECIATION";
    private final String FIXED_ASSET = "FIXED_ASSET";
    private final String LIABILITIES = "LIABILITIES";
    private final String NON_CURRENT_ASSET = "NON_CURRENT_ASSET";
    private final String TOTAL_INVESTING_ACTIVITIES = "TOTAL_INVESTING_ACTIVITIES";

    private final String FINANCING_ACTIVITIES = "FINANCING_ACTIVITIES";
    private final String LONG_TERM_LIABILITY = "LONG_TERM_LIABILITY";
    private final String EQUITY = "EQUITY";
    private final String TOTAL_FINANCING_ACTIVITIES = "TOTAL_FINANCING_ACTIVITIES";

    private final String INCREASE_DECREASE_FOR_PERIOD = "INCREASE_DECREASE_FOR_PERIOD";
    private final String BEGINNING_CASH = "BEGINNING_CASH";
    private final String ENDING_CASH = "ENDING_CASH";

    @Autowired
    private AccountingServiceLocal accountingService;

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        }
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            String date = commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(startDate)) + " "
                    + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(endDate));
            pdfData.setCurrentDate(date);
        } else {
            String date = commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + format.format(startDate) + " "
                    + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + format.format(endDate);
            pdfData.setCurrentDate(date);
        }

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";
        pdfData.setExtraData(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.figuresIn), " ", currencySymbol, "(", currencyCode, ")"));

        ITextTableList table = new ITextTableList(2);
        table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.accountName), commonLocalizer.localize(PdfLocalizationName.balance));
        pdfData.setListTable(table);

        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        CashFlow cashFlow = accountingService.getCashFlow(filterParametrs);

        SimpleDateFormat fpDateFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
        filterParametrs.setStartDateNC(fpDateFormat.format(ServerUtils.getDayStartTime(new Date(0))));
        filterParametrs.setEndDateNC(fpDateFormat.format(ServerUtils.getDayEndTime(ServerUtils.addDays(startDate, -1))));
        CashFlow beginningBalance = accountingService.getCashFlow(filterParametrs);

        cashFlow.setCashAtTheBeginningOfPeriod(beginningBalance.getNetIncreaseDecreaseForPeriod());
        cashFlow.setCashAtTheEndOfPeriod(cashFlow.getCashAtTheBeginningOfPeriod().add(cashFlow.getNetIncreaseDecreaseForPeriod()));

        HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();

        customData.put(OPERATING_ACTIVITIES, createGroupHeader(commonLocalizer.localize(PdfLocalizationName.operatingActivities)));
        customData.put(NET_PROFIT, createGroupNetTotal(commonLocalizer.localize(PdfLocalizationName.netProfit), cashFlow.getNetProfit(), priceScaleNumberFormat));
        if (CollectionUtils.isNotEmpty(cashFlow.getCurrentAssets())) {
            customData.put(CURRENT_ASSET, createInnerGroup(accountingLocalizer.localize(PdfLocalizationName.currentAsset), cashFlow.getCurrentAssets(), priceScaleNumberFormat));
        }
        if (CollectionUtils.isNotEmpty(cashFlow.getPrepayments())) {
            customData.put(PREPAYMENT, createInnerGroup(commonLocalizer.localize(PdfLocalizationName.prepayments), cashFlow.getPrepayments(), priceScaleNumberFormat));
        }
        if (CollectionUtils.isNotEmpty(cashFlow.getCurrentLiabilities())) {
            customData.put(CURRENT_LIABILITY, createInnerGroup(commonLocalizer.localize(PdfLocalizationName.currentLiability), cashFlow.getCurrentLiabilities(), priceScaleNumberFormat));
        }
        customData.put(TOTAL_OPERATING_ACTIVITIES, createGroupNetTotal(commonLocalizer.localize(PdfLocalizationName.netCashFromOperatingActivities), cashFlow.getNetOperatingActivities(), priceScaleNumberFormat));

        customData.put(INVESTING_ACTIVITIES, createGroupHeader(commonLocalizer.localize(PdfLocalizationName.investingActivities)));
        customData.put(ACCUMULATED_DEPRECIATION, createInnerGroup(commonLocalizer.localize(PdfLocalizationName.accumulatedDepreciation), cashFlow.getAccumulatedDepreciations(), priceScaleNumberFormat));
        customData.put(FIXED_ASSET, createInnerGroup(commonLocalizer.localize(PdfLocalizationName.fixedAssetAccounts), cashFlow.getFixedAssets(), priceScaleNumberFormat));
        customData.put(LIABILITIES, createInnerGroup(commonLocalizer.localize(PdfLocalizationName.liabilities), cashFlow.getLiabilities(), priceScaleNumberFormat));
        customData.put(NON_CURRENT_ASSET, createInnerGroup(accountingLocalizer.localize(PdfLocalizationName.nonCurrentAssets), cashFlow.getNonCurrentAssets(), priceScaleNumberFormat));
        customData.put(TOTAL_INVESTING_ACTIVITIES, createGroupNetTotal(commonLocalizer.localize(PdfLocalizationName.netCashFromInvestingActivities), cashFlow.getNetInvestingActivities(), priceScaleNumberFormat));

        customData.put(FINANCING_ACTIVITIES, createGroupHeader(commonLocalizer.localize(PdfLocalizationName.financingActivities)));
        customData.put(LONG_TERM_LIABILITY, createInnerGroup(commonLocalizer.localize(PdfLocalizationName.longTermLiabilityAccounts), cashFlow.getLongTermLiabilities(), priceScaleNumberFormat));
        customData.put(EQUITY, createInnerGroup(commonLocalizer.localize(PdfLocalizationName.equityAccounts), cashFlow.getEquities(), priceScaleNumberFormat));
        customData.put(TOTAL_FINANCING_ACTIVITIES, createGroupNetTotal(commonLocalizer.localize(PdfLocalizationName.netCashFromFinancingActivities), cashFlow.getNetFinancingActivities(), priceScaleNumberFormat));

        customData.put(INCREASE_DECREASE_FOR_PERIOD, createGroupNetTotal(commonLocalizer.localize(PdfLocalizationName.netIncreaseDecreaseForPeriod), cashFlow.getNetIncreaseDecreaseForPeriod(), priceScaleNumberFormat));
        customData.put(BEGINNING_CASH, createGroupNetTotal(commonLocalizer.localize(PdfLocalizationName.cashAtTheBeginningOfPeriod), cashFlow.getCashAtTheBeginningOfPeriod(), priceScaleNumberFormat));
        customData.put(ENDING_CASH, createGroupNetTotal(commonLocalizer.localize(PdfLocalizationName.cashAtTheEndOfPeriod), cashFlow.getCashAtTheEndOfPeriod(), priceScaleNumberFormat));

        pdfData.setCustomData(customData);
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    private CustomisedITextTable createGroupHeader(String name) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(name);
        return table;
    }

    private CustomisedITextTable createGroupNetTotal(String name, BigDecimal amount, DecimalFormat numberFormat) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(name);
        table.addColumn("AMOUNT", getValueAsString(amount, numberFormat));
        return table;
    }

    private CustomisedITextTable createInnerGroup(String name, List<CashFlowItem> items, DecimalFormat format) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(name);

        BigDecimal totalInnerGroupBalance = BigDecimal.ZERO;
        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE, PDFConstants.HAS_CHILD);

        Map<String, CashFlowItem> map1 = new HashMap<>(items.size());
        ArrayList<CashFlowItem> map2 = new ArrayList<>();
        for (CashFlowItem item : items) {
            if (item.getBalance() != null) {
                totalInnerGroupBalance = totalInnerGroupBalance.add(item.getBalance());
            }
            map1.put(item.getCode(), item);
        }
        for (CashFlowItem item : items) {
            if (item.getParentCode() != null) {
                if (map1.get(item.getParentCode()) == null) {
                    AccountItem accountCodeUnique = new AccountItem(item.getParentId(), item.getParentCode(), item.getParentName());
                    CashFlowItem cashFlowItem = new CashFlowItem(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName(), BigDecimal.ZERO);
                    cashFlowItem.getChilds().add(item);
                    map1.put(item.getParentCode(), cashFlowItem);
                    map2.add(cashFlowItem);
                } else {
                    map1.get(item.getParentCode()).getChilds().add(item);
                }
            } else {
                map2.add(item);
            }
        }

        map2.forEach(key -> {
            BigDecimal childTotal = new BigDecimal(0);
            addItem(table, format, key, childTotal, 2, "NO");
        });

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(PDFConstants.COLUMN_NAME, StringUtils.join(commonLocalizer.localize(PdfLocalizationName.total), " ", name));
        headerMap.put(PDFConstants.COLUMN_VALUE, getValueAsString(totalInnerGroupBalance, format));
        table.setHeader(headerMap);

        return table;
    }

    private void addItem(CustomisedITextTable table, DecimalFormat format, CashFlowItem item, BigDecimal childTotal, int level, String hasChild) {
        String intent = getTabString(level);

        hasChild = item.getChilds().isEmpty() ? "NO" : "YES";
        table.addRow(intent + item.getAccount().getName() + " (" + item.getCode() + ")", getValueAsString(item.getBalance(), format), hasChild);
        if (!item.getChilds().isEmpty()) {
            for (CashFlowItem child : item.getChilds()) {
                addItem(table, format, child, childTotal, level + 1, hasChild);
                if (!child.isCalculated()) {
                    childTotal = childTotal.add(child.getBalance());
                    child.setCalculated(true);
                }
            }
            if (!item.isCalculated()) {
                childTotal = childTotal.add(item.getBalance());
                item.setCalculated(true);
            }
            if (item.getAccount() != null) {
                drawTolatsRow(table, format, intent, childTotal,
                        commonLocalizer.localize(PdfLocalizationName.total) + " " + item.getAccount().getName() + " (" + item.getCode() + ")");
            }
        }
    }

    private void drawTolatsRow(CustomisedITextTable table, DecimalFormat format, String intent, BigDecimal childTotal, String groupName) {
        table.addRow(intent + groupName, getValueAsString(childTotal, format), "YES");
    }

    private String getTabString(int level) {
        StringBuilder intent = new StringBuilder();
        if (level > 0) {
            for (int i = 1; i<=level; i++) {
                intent.append("&nbsp; &nbsp;");
            }
        }
        return intent.toString();
    }

    private String getValueAsString(BigDecimal value, DecimalFormat numberFormat) {
        if (value != null) {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return " " + numberFormat.format(value);
            } else {
                return "(" + numberFormat.format(value.abs()) + ")";
            }
        }
        return " ";
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Cash_Flow_" + dateFormat(user.getUserDate()));
    }

    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.cashFlowStatement);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CASH_FLOW;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }
}
