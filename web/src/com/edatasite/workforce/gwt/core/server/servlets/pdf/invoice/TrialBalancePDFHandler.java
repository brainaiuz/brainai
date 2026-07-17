package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalance;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.TrialBalanceFilter;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 29-May-2009
 * Time: 11:00:49
 * To change this template use File | Settings | File Templates.
 */
public class TrialBalancePDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private CurrencyManager currencyManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        EdsUser user = uploadManager.getUser();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer currencyId = filterParametrs.getCurrencyID();
        EdsCurrency currency = currencyManager.getCurrency(currencyId);
        String currencySymbol = currency.getSymbol();
        String currencyCode = currency.getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        Integer showAccounts = filterParametrs.getType();
        ITextTableList table = new ITextTableList(5);
        pdfData.setListTable(table);
        TrialBalanceFilter tbf = new TrialBalanceFilter();
        tbf.setStartDate(startDate != null ? new DateNonConvertable(startDate) : null);
        tbf.setToDate((endDate != null ? new DateNonConvertable(endDate) : null));
        tbf.setSortField(filterParametrs.getSortField());
        tbf.setConsolidation(filterParametrs.isActualDue());
        tbf.setShowValues(showAccounts);
        tbf.setDepartmentID(filterParametrs.getDepartmentId());
        tbf.setSortDirection(filterParametrs.isAscending() ? "ASC" : "DESC");
        tbf.setCurrencyId(currencyId);
        tbf.setSummary(filterParametrs.isShowBudget());
        TrialBalance trialBalance = accountingService.getTrialBalance(tbf);

        String date = commonLocalizer.localize(PdfLocalizationName.asAt) + format.format(endDate);
        StringBuilder nameLabel = new StringBuilder();
        nameLabel.append(commonLocalizer.localize(PdfLocalizationName.trialBalance)).append("\n")
                .append(user.getCompany().getName()).append("\n")
                .append(date).append("\n")
                .append(commonLocalizer.localize(PdfLocalizationName.figuresIn)).append(" ").append(currencySymbol).append("(").append(currencyCode).append(")" + "\n");

        pdfData.setTableName(nameLabel.toString());

        table.addPdfTableHeader(
                drawHeader(commonLocalizer.localize(PdfLocalizationName.account), Element.ALIGN_LEFT),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.beginningBalance), Element.ALIGN_RIGHT),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.debit), Element.ALIGN_RIGHT),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.credit), Element.ALIGN_RIGHT),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.endingBalance), Element.ALIGN_RIGHT));


        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        setData(trialBalance.getAssets(), commonLocalizer.localize(PdfLocalizationName.assets), table, priceScaleNumberFormat, true);
        setData(trialBalance.getLiabilities(), commonLocalizer.localize(PdfLocalizationName.liabilities), table, priceScaleNumberFormat, false);
        setData(trialBalance.getEquity(), commonLocalizer.localize(PdfLocalizationName.equity), table, priceScaleNumberFormat, false);
        setData(trialBalance.getRevenue(), commonLocalizer.localize(PdfLocalizationName.revenue), table, priceScaleNumberFormat, false);
        setData(trialBalance.getExpenses(), commonLocalizer.localize(PdfLocalizationName.expenses), table, priceScaleNumberFormat, true);

        table.addTableWidthPercentage(5f, 1.25f, 1.25f, 1.25f, 1.25f);

        CellData totalNameLabel = new CellData(commonLocalizer.localize(PdfLocalizationName.total));
        totalNameLabel.setFont(createFont(9, true));
        table.addPdfTableRows(totalNameLabel,
                createTotalCell(trialBalance.getTotalBeginningBalance(), priceScaleNumberFormat),
                createTotalCell(trialBalance.getTotalDebit(), priceScaleNumberFormat),
                createTotalCell(trialBalance.getTotalCredit(), priceScaleNumberFormat),
                createTotalCell(trialBalance.getTotalEndingBalance(), priceScaleNumberFormat));
        return pdfData;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = uploadManager.getUser();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        }

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        // Company Data
        pdf.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        String date;
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            date = commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(startDate)) + " "
                    + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(endDate));
        } else {
            date = commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + format.format(startDate) + " "
                    + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + format.format(endDate);
        }
        Integer currencyId = filterParametrs.getCurrencyID();
        EdsCurrency currency = currencyManager.getCurrency(currencyId);
        String currencySymbol = currency != null ? escapeHtml(currency.getSymbol()) : "";
        String currencyCode = currency != null ? escapeHtml(currency.getName()) : "";
        Integer showAccounts = filterParametrs.getType();
        TrialBalanceFilter tbf = new TrialBalanceFilter();
        tbf.setStartDate(startDate != null ? new DateNonConvertable(startDate) : null);
        tbf.setToDate((endDate != null ? new DateNonConvertable(endDate) : null));
        tbf.setSortField(filterParametrs.getSortField());
        tbf.setConsolidation(filterParametrs.isActualDue());
        tbf.setShowValues(showAccounts);
        tbf.setDepartmentID(filterParametrs.getDepartmentId());
        tbf.setSortDirection(filterParametrs.isAscending() ? "ASC" : "DESC");
        tbf.setCurrencyId(currencyId);
        tbf.setSummary(filterParametrs.isShowBudget());
        TrialBalance trialBalance = accountingService.getTrialBalance(tbf);

        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        String collapsed = filterParametrs.getCollapsed();

        HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();
        customData.put(EdsAccountType.ASSETS, getTrialItemsByAccountType(getTreeItems(trialBalance.getAssets()), priceScaleNumberFormat, commonLocalizer.localize(PdfLocalizationName.assets), true, !collapsed.contains("assetsGroup")));
        customData.put(EdsAccountType.LIABILITIES, getTrialItemsByAccountType(getTreeItems(trialBalance.getLiabilities()), priceScaleNumberFormat, commonLocalizer.localize(PdfLocalizationName.liabilities), false, !collapsed.contains("liabilitiesGroup")));
        customData.put(EdsAccountType.EQUITY, getTrialItemsByAccountType(getTreeItems(trialBalance.getEquity()), priceScaleNumberFormat, commonLocalizer.localize(PdfLocalizationName.equity), false, !collapsed.contains("equityGroup")));
        customData.put(EdsAccountType.EXPENSES, getTrialItemsByAccountType(getTreeItems(trialBalance.getExpenses()), priceScaleNumberFormat, commonLocalizer.localize(PdfLocalizationName.expenses), true, !collapsed.contains("expensesGroup")));
        customData.put(EdsAccountType.REVENUE, getTrialItemsByAccountType(getTreeItems(trialBalance.getRevenue()), priceScaleNumberFormat, commonLocalizer.localize(PdfLocalizationName.revenue), false, !collapsed.contains("revenueGroup")));

        Map<Integer, ArrayList<TrialBalanceItem>> map = accountingService.getPRAccountClientSupplierBalance(startDate, endDate);

        if (map != null && !map.isEmpty()) {
            for (Integer key : map.keySet()) {
                customData.put(key.toString(), getTrialBalanceAsCusSupp(map.get(key), priceScaleNumberFormat));
            }
        }
        customData.put("TOTAL", getTrialBalanceTotal(trialBalance, priceScaleNumberFormat));
        customData.put("EMPTY_DATA", getEmptyTable());
        pdf.setCustomData(customData);
        pdf.setExtraData(commonLocalizer.localize(PdfLocalizationName.figuresIn) + " " + "(" + currencyCode + ")");
        pdf.setCurrentDate(date);
        pdf.setTableName(commonLocalizer.localize(PdfLocalizationName.trialBalance));


        pdf.setCustomData(customData);
        Map<String, String> localizeLabels = new LinkedHashMap<>();
        localizeLabels.put("ACCOUNT_LABEL", commonLocalizer.localize(PdfLocalizationName.account));
        localizeLabels.put("BEGINNING_BALANCE_LABEL", commonLocalizer.localize(PdfLocalizationName.beginningBalance));
        localizeLabels.put("DEBIT_LABEL", commonLocalizer.localize(PdfLocalizationName.debit));
        localizeLabels.put("CREDIT_LABEL", commonLocalizer.localize(PdfLocalizationName.credit));
        localizeLabels.put("ENDING_BALANCE_LABEL", commonLocalizer.localize(PdfLocalizationName.endingBalance));
        pdf.setLocalizeLabels(localizeLabels);
        return pdf;
    }

    private ArrayList<TrialBalanceItem> getTreeItems(TrialBalanceItem[] items) {
        Map<String, TrialBalanceItem> map1 = new HashMap<>(items.length);
        ArrayList<TrialBalanceItem> itemsTreeList = new ArrayList<>();
        Arrays.stream(items).forEach(acc -> map1.put(acc.getCode(), acc));
        Arrays.stream(items).forEach(acc -> {
            if (acc.getParentCode() != null) {
                if (map1.get(acc.getParentCode()) == null) {
                    AccountItem accountCodeUnique = new AccountItem(acc.getParentId(), acc.getParentCode(), acc.getParentName());
                    TrialBalanceItem balanceItem = new TrialBalanceItem(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName(),
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                    balanceItem.getChilds().add(acc);
                    map1.put(acc.getParentCode(), balanceItem);
                    itemsTreeList.add(balanceItem);
                } else {
                    map1.get(acc.getParentCode()).getChilds().add(acc);
                }
            } else {
                itemsTreeList.add(acc);
            }
        });
        return itemsTreeList;
    }

    private CustomisedITextTable getTrialItemsByAccountType(ArrayList<TrialBalanceItem> items, DecimalFormat numberFormat, String tableName, boolean isDebitBalance,
                                                            boolean isNotShowChilds) {
        CustomisedITextTable itemTable = new CustomisedITextTable();
        itemTable.setName(tableName);
        itemTable.addColumnOrder(ACCOUNT_CODE, ACCOUNT_NAME, BEGINNET_DEBIT,
                BEGINNET_CREDIT, DEBIT, CREDIT, ENDING_DEBIT, ENDING_CREDIT,
                CATEGORY_CODE, PARENT_CODE, PARENT_NAME, "ACCOUNT_ID", BEGINNET_BALANCE, ENDING_BALANCE, HAS_CHILD);
        TrialBalanceItem itemsTotal = new TrialBalanceItem();
        itemsTotal.setBeginningBalance(BigDecimal.ZERO);
        itemsTotal.setDebit(BigDecimal.ZERO);
        itemsTotal.setCredit(BigDecimal.ZERO);
        itemsTotal.setEndingBalance(BigDecimal.ZERO);
        for (TrialBalanceItem item : items) {
            TrialBalanceItem childTotal = new TrialBalanceItem();
            childTotal.setBeginningBalance(BigDecimal.ZERO);
            childTotal.setDebit(BigDecimal.ZERO);
            childTotal.setCredit(BigDecimal.ZERO);
            childTotal.setEndingBalance(BigDecimal.ZERO);
            collectRows(numberFormat, tableName, isDebitBalance, itemTable, item, 1, "NO", childTotal, itemsTotal, isNotShowChilds);
        }
        drawGroupTotal(numberFormat, isDebitBalance, itemTable, tableName, itemsTotal, "");

        return itemTable;
    }

    private String getTabString(int level) {
        StringBuilder intent = new StringBuilder("");
        if (level > 0) {
            for (int i = 1; i <= level; i++) {
                intent.append("&#160; &#160;");
            }
        }
        return intent.toString();
    }

    private void collectRows(DecimalFormat numberFormat, String tableName, boolean isDebitBalance, CustomisedITextTable itemTable, TrialBalanceItem item,
                             int level, String hasChild, TrialBalanceItem childTotal, TrialBalanceItem itemsTotal, boolean isNotShowChilds) {
        String intent = getTabString(level);

        String code = (item.getCode() != null) ? escapeHtml(item.getCode()) : "";
        String name = (item.getName() != null) ? intent + escapeHtml(item.getName()) : "";
        String categoryCode = item.getCategoryCode() != null ? item.getCategoryCode() : "";
        String beginDebit = getValueAsString(item.getBeginningDebit(), numberFormat);
        String beginCredit = getValueAsString(item.getBeginningCredit(), numberFormat);
        String debit = getValueAsString(item.getDebit(), numberFormat);
        String credit = getValueAsString(item.getCredit(), numberFormat);
        String endDebit = getValueAsString(item.getEndingDebit(), numberFormat);
        String endCredit = getValueAsString(item.getEndingCredit(), numberFormat);
        String parentCode = item.getParentCode() != null ? escapeHtml(item.getParentCode()) : "";
        String parentName = item.getParentName() != null ? escapeHtml(item.getParentName()) : "";
        String accountID = item.getAccountId().toString();
        BigDecimal begBalanceVal = isDebitBalance ? item.getBeginningBalance() : item.getBeginningBalance() == null ? BigDecimal.ZERO : item.getBeginningBalance().multiply(new BigDecimal(-1));
        BigDecimal endBalanceVal = isDebitBalance ? item.getEndingBalance() : item.getEndingBalance() == null ? BigDecimal.ZERO : item.getEndingBalance().multiply(new BigDecimal(-1));
        String begBalanceString = getValueAsString(begBalanceVal, numberFormat);
        String endBalanceString = getValueAsString(endBalanceVal, numberFormat);

        itemsTotal.setBeginningBalance(itemsTotal.getBeginningBalance().add(item.getBeginningBalance() != null ? item.getBeginningBalance() : BigDecimal.ZERO));
        itemsTotal.setDebit(itemsTotal.getDebit().add(item.getDebit() != null ? item.getDebit() : BigDecimal.ZERO));
        itemsTotal.setCredit(itemsTotal.getCredit().add(item.getCredit() != null ? item.getCredit() : BigDecimal.ZERO));
        itemsTotal.setEndingBalance(itemsTotal.getEndingBalance().add(item.getEndingBalance() != null ? item.getEndingBalance() : BigDecimal.ZERO));

        hasChild = item.getChilds().isEmpty() ? "NO" : "YES";
        if (isNotShowChilds) {
            itemTable.addRow(code, name, beginDebit, beginCredit, debit, credit, endDebit, endCredit, categoryCode, parentCode,
                    parentName, accountID, begBalanceString, endBalanceString, hasChild);
        }
        if (!item.getChilds().isEmpty()) {
            for (TrialBalanceItem child : item.getChilds()) {
                collectRows(numberFormat, tableName, isDebitBalance, itemTable, child, level + 1, hasChild, childTotal, itemsTotal, isNotShowChilds);
                if (!child.isCalculated()) {
                    childTotal.setBeginningBalance(childTotal.getBeginningBalance().add(child.getBeginningBalance()));
                    childTotal.setDebit(childTotal.getDebit().add(child.getDebit()));
                    childTotal.setCredit(childTotal.getCredit().add(child.getCredit()));
                    childTotal.setEndingBalance(childTotal.getEndingBalance().add(child.getEndingBalance()));
                    child.setCalculated(true);
                }
            }
            if (!item.isCalculated()) {
                childTotal.setBeginningBalance(childTotal.getBeginningBalance().add(item.getBeginningBalance() != null ? item.getBeginningBalance() : BigDecimal.ZERO));
                childTotal.setDebit(childTotal.getDebit().add(item.getDebit() != null ? item.getDebit() : BigDecimal.ZERO));
                childTotal.setCredit(childTotal.getCredit().add(item.getCredit() != null ? item.getCredit() : BigDecimal.ZERO));
                childTotal.setEndingBalance(childTotal.getEndingBalance().add(item.getEndingBalance() != null ? item.getEndingBalance() : BigDecimal.ZERO));

                item.setCalculated(true);
            }
            boolean used = false;
            if (!item.getChilds().isEmpty() &&
                    item.getBeginningBalance() != null &&
                    item.getDebit() != null &&
                    item.getCredit() != null &&
                    item.getEndingBalance() != null) {
                used = true;
            }
            if (item.getName() != null && !used && isNotShowChilds) {
                drawGroupTotal(numberFormat, isDebitBalance, itemTable, item.getName(), childTotal, intent);
            } else {
                TrialBalanceItem accountAndChildTotal = new TrialBalanceItem();
                accountAndChildTotal.setBeginningBalance(item.getBeginningBalance() != null ? item.getBeginningBalance() : BigDecimal.ZERO);
                accountAndChildTotal.setDebit(item.getDebit() != null ? item.getDebit() : BigDecimal.ZERO);
                accountAndChildTotal.setCredit(item.getCredit() != null ? item.getCredit() : BigDecimal.ZERO);
                accountAndChildTotal.setEndingBalance(item.getEndingBalance() != null ? item.getEndingBalance() : BigDecimal.ZERO);

                Set<TrialBalanceItem> visited = new HashSet<>();

                // Calculate totals for all nested children
                calculatTotal(accountAndChildTotal,item,visited);

                if (isNotShowChilds) {
                    drawGroupTotal(numberFormat, isDebitBalance, itemTable, item.getName(), accountAndChildTotal, intent);
                }
            }
        }

    }

    // Recursive method to calculate totals for all nested children
    private void calculatTotal(TrialBalanceItem accountAndChildTotal, TrialBalanceItem item,Set<TrialBalanceItem> visited) {

        if (visited.contains(item)) {
            return;
        }
        visited.add(item);

        for (TrialBalanceItem child : item.getChilds()) {
            // Recursively calculate totals for sub-children
            if (!child.getChilds().isEmpty()) {
                calculatTotal(accountAndChildTotal, child,visited);
            }
            accountAndChildTotal.setBeginningBalance(accountAndChildTotal.getBeginningBalance().add(child.getBeginningBalance() != null
                    ? child.getBeginningBalance()
                    : BigDecimal.ZERO));
            accountAndChildTotal.setDebit(accountAndChildTotal.getDebit().add(child.getDebit() != null
                    ? child.getDebit()
                    : BigDecimal.ZERO));
            accountAndChildTotal.setCredit(accountAndChildTotal.getCredit().add(child.getCredit() != null
                    ? child.getCredit()
                    : BigDecimal.ZERO));
            accountAndChildTotal.setEndingBalance(accountAndChildTotal.getEndingBalance().add(child.getEndingBalance() != null
                    ? child.getEndingBalance()
                    : BigDecimal.ZERO));
        }
    }
    private void drawGroupTotal(DecimalFormat numberFormat, boolean isDebitBalance, CustomisedITextTable itemTable, String groupName, TrialBalanceItem childTotal, String intent) {
        String name = (groupName != null) ? intent + commonLocalizer.localize(PdfLocalizationName.total) + "&#160;" + escapeHtml(groupName) : "";
        String debit = getValueAsString(childTotal.getDebit(), numberFormat);
        String credit = getValueAsString(childTotal.getCredit(), numberFormat);
        BigDecimal begBalanceVal1 = isDebitBalance ? childTotal.getBeginningBalance() : childTotal.getBeginningBalance() == null ? BigDecimal.ZERO : childTotal.getBeginningBalance().multiply(new BigDecimal(-1));
        BigDecimal endBalanceVal1 = isDebitBalance ? childTotal.getEndingBalance() : childTotal.getEndingBalance() == null ? BigDecimal.ZERO : childTotal.getEndingBalance().multiply(new BigDecimal(-1));
        String begBalanceString1 = getValueAsString(begBalanceVal1, numberFormat);
        String endBalanceString1 = getValueAsString(endBalanceVal1, numberFormat);

        itemTable.addRow("", name, "", "", debit, credit, "", "", "", "",
                "", "", begBalanceString1, endBalanceString1, "YES");
    }

    private CustomisedITextTable getTrialBalanceTotal(TrialBalance trialBalance, DecimalFormat numberFormat) {
        CustomisedITextTable itemTable = new CustomisedITextTable();
        itemTable.setName(commonLocalizer.localize(PdfLocalizationName.total));
        itemTable.addColumnOrder(ACCOUNT_CODE, ACCOUNT_NAME, BEGINNET_DEBIT, BEGINNET_CREDIT, DEBIT, CREDIT, ENDING_DEBIT, ENDING_CREDIT, BEGINNET_BALANCE, ENDING_BALANCE);

        String code = "Total";
        String name = "";
        String totalBeginDebit = getValueAsString(trialBalance.getTotalBeginningDebit(), numberFormat);
        String totalBeginCredit = getValueAsString(trialBalance.getTotalBeginningCredit(), numberFormat);
        String totalDebit = getValueAsString(trialBalance.getTotalDebit(), numberFormat);
        String totalCredit = getValueAsString(trialBalance.getTotalCredit(), numberFormat);
        String totalEndDebit = getValueAsString(trialBalance.getTotalEndingDebit(), numberFormat);
        String totalEndCredit = getValueAsString(trialBalance.getTotalEndingCredit(), numberFormat);
        String totalBeginningBalance = getValueAsString(trialBalance.getTotalBeginningBalance(), numberFormat);
        String totalEndingBalance = getValueAsString(trialBalance.getTotalEndingBalance(), numberFormat);

        itemTable.addRow(code, name, totalBeginDebit, totalBeginCredit, totalDebit, totalCredit, totalEndDebit, totalEndCredit, totalBeginningBalance, totalEndingBalance);

        return itemTable;
    }

    private CustomisedITextTable getTrialBalanceAsCusSupp(ArrayList<TrialBalanceItem> items, DecimalFormat numberFormat) {
        CustomisedITextTable itemTable = new CustomisedITextTable();
        itemTable.addColumnOrder(ACCOUNT_CODE, ACCOUNT_NAME, BEGINNET_DEBIT, BEGINNET_CREDIT, DEBIT, CREDIT, ENDING_DEBIT, ENDING_CREDIT);

        for (TrialBalanceItem item : items) {
            String code = (item.getCode() != null) ? escapeHtml(item.getCode()) : "";
            String name = (item.getName() != null) ? escapeHtml(item.getName()) : "";
            String beginDebit = getValueAsString(item.getBeginningDebit(), numberFormat);
            String beginCredit = getValueAsString(item.getBeginningCredit(), numberFormat);
            String debit = getValueAsString(item.getDebit(), numberFormat);
            String credit = getValueAsString(item.getCredit(), numberFormat);
            String endDebit = getValueAsString(item.getEndingDebit(), numberFormat);
            String endCredit = getValueAsString(item.getEndingCredit(), numberFormat);


            itemTable.addRow(code, name, beginDebit, beginCredit, debit, credit, endDebit, endCredit);
        }

        return itemTable;
    }

    private CustomisedITextTable getEmptyTable() {
        CustomisedITextTable itemTable = new CustomisedITextTable();
        itemTable.addColumnOrder(ACCOUNT_CODE, ACCOUNT_NAME, BEGINNET_DEBIT, BEGINNET_CREDIT, DEBIT, CREDIT, ENDING_DEBIT, ENDING_CREDIT);
        return itemTable;
    }

    private void setData(TrialBalanceItem[] items, String header, ITextTableList table, DecimalFormat numberFormat, boolean isDebitBalance) {
        if (items != null && items.length > 0) {
            drawBookMark(header, table);
            for (TrialBalanceItem item : items) {
                CellData nameCell = new CellData(item.getName().concat(" (").concat(item.getCode()).concat(")"));

                nameCell.setPadding(20, null, null, null);


                BigDecimal begBalanceVal = isDebitBalance ? item.getBeginningBalance() : item.getBeginningBalance() == null ? BigDecimal.ZERO : item.getBeginningBalance().multiply(new BigDecimal(-1));
                BigDecimal endBalanceVal = isDebitBalance ? item.getEndingBalance() : item.getEndingBalance() == null ? BigDecimal.ZERO : item.getEndingBalance().multiply(new BigDecimal(-1));

                table.addPdfTableRows(nameCell, createCell(begBalanceVal, numberFormat), createCell(item.getDebit(), numberFormat),
                        createCell(item.getCredit(), numberFormat), createCell(endBalanceVal, numberFormat));
            }
        }
    }

    private void drawBookMark(String name, ITextTableList table) {
        CellData nameCell = new CellData(name);
        nameCell.setFont(createFont(9, true));
        table.addPdfTableRows(nameCell, new CellData(""), new CellData(""), new CellData(""), new CellData(""));
    }

    private CellData drawHeader(String name, Integer alignment) {
        CellData nameCell = new CellData(name, alignment);
        nameCell.setFont(createFont(9, true));
        return nameCell;
    }

    private Font createFont(Integer fontSize, boolean bold) {
        return FontFactory.getFont(ITextFontTypeEnum.TIMES_NEW_ROMAN.getName(), BaseFont.IDENTITY_H, fontSize, bold ? Font.BOLD : Font.NORMAL);
    }

    private CellData createTotalCell(BigDecimal value, DecimalFormat numberFormat) {
        CellData cell = createCell(value, numberFormat);
        cell.setFont(createFont(9, true));
        return cell;
    }

    private CellData createCell(BigDecimal value, DecimalFormat numberFormat) {
        return new CellData(getValueAsString(value, numberFormat), Element.ALIGN_RIGHT);
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
        setFileName("Trial_Balance_" + dateFormat(user.getUserDate()));
    }

    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.TRIAL_BALANCE;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.trialBalance);
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }
}
