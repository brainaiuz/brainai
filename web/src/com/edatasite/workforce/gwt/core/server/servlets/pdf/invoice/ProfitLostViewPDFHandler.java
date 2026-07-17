package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountItemWithBudgetDate;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountItemsByAccountType;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ActBudVar;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetInDate;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetManagerItems;
import com.edatasite.workforce.gwt.accounting.client.rpc.PnLFilter;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.CompareCategoryEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.CompareWithEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTreeObject;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 03-Aug-2018
 * Time: 03:55:47
 * To change this template use File | Settings | File Templates.
 */
public class ProfitLostViewPDFHandler extends AbstractITextPostPdfHandler {
    private final String REVENUE = "REVENUE";
    private final String SALE = "SALE";
    private final String OTHER_INCOME = "OTHER_INCOME";
    private final String GROSS_PROFIT = "GROSS_PROFIT";

    private final String EXPENSE = "EXPENSE";
    private final String DIRECT_COST = "DIRECT_COST";
    private final String DEPRECIATION = "DEPRECIATION";
    private final String OVERHEAD = "OVERHEAD";
    private final String NET_PROFIT = "NET_PROFIT";
    private final BigDecimal ZERO = new BigDecimal("0.00");
    private Integer tableColsCount;
    private boolean isShowBadget = false;
    private DecimalFormat priceScaleNumberFormat;
    private final HashMap<String, CustomisedITextTable> pnlMap = new LinkedHashMap<>();
    private final ArrayList<String> shownObjects = new ArrayList<>();

    @Autowired
    private CurrencyManager currencyManager;
    private AccountingService accountingService;
    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private DepartmentManager departmentManager;

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        if (filterParametrs.getShownObjects() != null) {
            shownObjects.clear();
            shownObjects.addAll(Arrays.asList(filterParametrs.getShownObjects().split(",")));
        }

        FromToDate main = new FromToDate(new DateNonConvertable(startDate), new DateNonConvertable(endDate));
        FromToDate[] compareDates = getCompareDates(startDate, Integer.valueOf(filterParametrs.getDataType()));
        tableColsCount = compareDates.length + 1;

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = currencyManager.getCurrency(filterParametrs.getCurrencyID());
        String currencySymbol = currency.getSymbol() != null ? currency.getSymbol() : "";
        String currencyCode = currency.getName();

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        }
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            pdfData.setCurrentDate(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.from),
                    " ", ServerUtils.convertToUzbDateFormat(format.format(startDate)), " ",
                    commonLocalizer.localize(PdfLocalizationName.to),
                    " ", ServerUtils.convertToUzbDateFormat(format.format(endDate))));
        } else {
            pdfData.setCurrentDate(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.from),
                    " ", format.format(startDate), " ",
                    commonLocalizer.localize(PdfLocalizationName.to),
                    " ", format.format(endDate)));
        }
//        pdfData.setCurrentDate(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.from),
//                " ", format.format(startDate), " ",
//                commonLocalizer.localize(PdfLocalizationName.to),
//                " ", format.format(endDate)));

        String projectNumberName = "";
        if (filterParametrs.getProjectId() != null) {
            EdsProject project = projectManager.get(filterParametrs.getProjectId());
            projectNumberName = project.getNumber() + " -> " + project.getName();
        }
        String departmentName = "";
        if (filterParametrs.getDepartmentId() != null){
            EdsDepartment department = departmentManager.get(filterParametrs.getDepartmentId());
            departmentName = department.getName();
        }
        pdfData.setExtraData(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.figuresIn), " ", currencySymbol, "(", currencyCode, ")") + (departmentName.length() != 0 ? "<br/>" + departmentName : "") + "<br/>" + projectNumberName);

        isShowBadget = filterParametrs.isShowBudget();
        PnLFilter filter = new PnLFilter();
        filter.setMain(main);
        filter.setCompareTo(compareDates);
        filter.setSortField(filterParametrs.getSortField() != null ? filterParametrs.getSortField() : Constants.ACC_NAME);
        filter.setSortDirection(filterParametrs.getSortDir() == 1 ? ASC_STR : DESC_STR);
        filter.setCosolidation(filterParametrs.isActualDue());
        filter.setDepartmentID(filterParametrs.getDepartmentId());
        filter.setProjectID(filterParametrs.getProjectId());
        filter.setCurrencyId(filterParametrs.getCurrencyID());
        filter.setShowBudget(isShowBadget);
        BudgetManagerItems profLoss = accountingService.getProfitAndLoss(filter);
        Integer calculationScale = getCalculationScale();
        if (profLoss != null) {
            priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
            ITextTableList table = new ITextTableList(compareDates.length + 2);
            if (isShowBadget) {
                table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.code));
                table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.account));
                table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.actual));
                table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.budget));
                table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.variance));

                pdfData.setListTable(table);
                // Gross Items
                CustomisedITextTable revenue = getBudgetData(profLoss.getRevenue());
                if (revenue != null) {
                    pnlMap.put(REVENUE, revenue);
                }
                CustomisedITextTable sale = getBudgetData(profLoss.getSale());
                if (sale != null) {
                    pnlMap.put(SALE, sale);
                }
                CustomisedITextTable expense = getBudgetData(profLoss.getExpense());
                if (expense != null) {
                    pnlMap.put(EXPENSE, expense);
                }
                pnlMap.put(GROSS_PROFIT, getTotal(profLoss.getGrossVariance(), "Gross Profit", calculationScale));

                CustomisedITextTable directCost = getBudgetData(profLoss.getDirectCosts());
                if (directCost != null) {
                    pnlMap.put(DIRECT_COST, directCost);
                }
                CustomisedITextTable depreciation = getBudgetData(profLoss.getDepreciation());
                if (depreciation != null) {
                    pnlMap.put(DEPRECIATION, depreciation);
                }
                CustomisedITextTable overhead = getBudgetData(profLoss.getOverhead());
                if (overhead != null) {
                    pnlMap.put(OVERHEAD, overhead);
                }
                CustomisedITextTable otherIncome = getBudgetData(profLoss.getOtherIncome());
                if (otherIncome != null) {
                    pnlMap.put(OTHER_INCOME, otherIncome);
                }
                pnlMap.put(NET_PROFIT, getTotal(profLoss.getNetVariance(), "Net Profit", calculationScale));
                pdfData.setCustomData(pnlMap);
            } else {
                table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.accountName));
                for (int i = compareDates.length - 1; i >= 0; i--) {
                    table.addPdfTableHeader(format.format(compareDates[i].getTo().getDate()));
                }
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    table.addPdfTableHeader(ServerUtils.convertToUzbDateFormat(format.format(endDate)));
                } else {
                    table.addPdfTableHeader(format.format(endDate));
                }
//                table.addPdfTableHeader(format.format(endDate));
                pdfData.setListTable(table);

                HashMap<String, CustomisedITextTable> pnlMap = new LinkedHashMap<>();
                // Gross Items
                CustomisedITextTable revenue = collectPnlData(profLoss.getRevenue(), priceScaleNumberFormat, filterParametrs.isZeroAvoided(), false, filterParametrs.isSummaryView());
                if (revenue != null) {
                    pnlMap.put(REVENUE, revenue);
                }
                CustomisedITextTable sale = collectPnlData(profLoss.getSale(), priceScaleNumberFormat, filterParametrs.isZeroAvoided(), false, filterParametrs.isSummaryView());
                if (sale != null) {
                    pnlMap.put(SALE, sale);
                }
                CustomisedITextTable otherIncome = collectPnlData(profLoss.getOtherIncome(), priceScaleNumberFormat, filterParametrs.isZeroAvoided(), false, filterParametrs.isSummaryView());
                if (otherIncome != null) {
                    pnlMap.put(OTHER_INCOME, otherIncome);
                }
                pnlMap.put(GROSS_PROFIT, collectGrandTotal(profLoss.getGrossProfit(), priceScaleNumberFormat, false, true));

                // Net items
                CustomisedITextTable expense = collectPnlData(profLoss.getExpense(), priceScaleNumberFormat, filterParametrs.isZeroAvoided(), true, filterParametrs.isSummaryView());
                if (expense != null) {
                    pnlMap.put(EXPENSE, expense);
                }
                CustomisedITextTable directCost = collectPnlData(profLoss.getDirectCosts(), priceScaleNumberFormat, filterParametrs.isZeroAvoided(), true, filterParametrs.isSummaryView());
                if (directCost != null) {
                    pnlMap.put(DIRECT_COST, directCost);
                }
                CustomisedITextTable depreciation = collectPnlData(profLoss.getDepreciation(), priceScaleNumberFormat, filterParametrs.isZeroAvoided(), true, filterParametrs.isSummaryView());
                if (depreciation != null) {
                    pnlMap.put(DEPRECIATION, depreciation);
                }
                CustomisedITextTable overhead = collectPnlData(profLoss.getOverhead(), priceScaleNumberFormat, filterParametrs.isZeroAvoided(), true, filterParametrs.isSummaryView());
                if (overhead != null) {
                    pnlMap.put(OVERHEAD, overhead);
                }
                pnlMap.put(NET_PROFIT, collectGrandTotal(profLoss.getNetProfit(), priceScaleNumberFormat, false, false));
                pdfData.setCustomData(pnlMap);
            }
        }
        return pdfData;
    }


    private CustomisedITextTable getBudgetData(AccountItemsByAccountType item) {
        if (item != null) {
            AccountItemWithBudgetDate[] accItems = item.getAccountItems();
            if (accItems != null && accItems.length > 0) {
                CustomisedITextTable table = new CustomisedITextTable();
                table.setName(item.getGroupName());
                LinkedList<String> list = new LinkedList<>();
                for (AccountItemWithBudgetDate accItem : accItems) {
                    if (accItem != null) {
                        ActBudVar var = accItem.getVariance();
                        list = new LinkedList<>();
                        list.add(accItem.getName());
                        list.add(getValueAsString(var != null && var.getActual() != null ? var.getActual().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
                        list.add(getValueAsString(var != null && var.getBudget() != null ? var.getBudget().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
                        list.add(getValueAsString(var != null && var.getVariance() != null ? var.getVariance().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
                    }
                    table.addTreeRows(accItem.getCode(), null, list.toArray(new String[]{}));
                }
                list = new LinkedList<>();
                list.add("");
                list.add(getValueAsString(item.getTotalWithVariance() != null && item.getTotalWithVariance().getActual() != null ? item.getTotalWithVariance().getActual().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
                list.add(getValueAsString(item.getTotalWithVariance() != null && item.getTotalWithVariance().getBudget() != null ? item.getTotalWithVariance().getBudget().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
                list.add(getValueAsString(item.getTotalWithVariance() != null && item.getTotalWithVariance().getVariance() != null ? item.getTotalWithVariance().getVariance().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
                table.addTotalRow("Total " + item.getGroupName(), list);
                return table;
            }
        }
        return null;
    }

    private CustomisedITextTable collectPnlData(AccountItemsByAccountType item, DecimalFormat format, boolean excludeZero, boolean isNegative, boolean isSummary) {
        if (item != null) {
            AccountItemWithBudgetDate[] accItems = item.getAccountItems();
            if (accItems != null && accItems.length > 0) {

                Map<String, AccountItemWithBudgetDate> map1 = new HashMap<>(accItems.length);
                ArrayList<AccountItemWithBudgetDate> map2 = new ArrayList<>();

                Arrays.stream(accItems).forEach(acc -> map1.put(acc.getCode(), acc));
                Arrays.stream(accItems).forEach(acc -> {
                    if (StringUtils.isNotBlank(acc.getParentCode())) {
                        if (map1.get(acc.getParentCode()) == null) {
                            AccountItem accountCodeUnique = accountingService.getAccountCodeUnique(acc.getParentCode(), null);
                            if (accountCodeUnique != null) {
                                AccountItemWithBudgetDate budgetDate = new AccountItemWithBudgetDate(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName());
                                budgetDate.getChilds().add(acc);
                                map1.put(acc.getParentCode(), budgetDate);
                                map2.add(budgetDate);
                            } else {
                                map2.add(acc);
                            }
                        } else {
                            map1.get(acc.getParentCode()).getChilds().add(acc);
                        }
                    } else {
                        map2.add(acc);
                    }
                });

                CustomisedITextTable table = new CustomisedITextTable();
                table.setName(item.getGroupName());

                if (!shownObjects.contains(item.getGroupName())) {
                    table.setName("");
                    table.addTotalRow(item.getGroupName(), collectTotal(item.getActualTotal(), format, isNegative));
                } else {
                    map2.forEach(key -> {
                        ArrayList<BigDecimal> childTotal = new ArrayList<>(tableColsCount - 1);
                        for (int i = 0; i < tableColsCount; i++) {
                            childTotal.add(i, BigDecimal.ZERO);
                        }
                        if (!shownObjects.contains(key.getCode())) {
                            generateSummaryViewTreeTable(key, childTotal);
                            table.addTreeRows(key.getName().concat("<small>").concat("(").concat(key.getCode()).concat(")").concat("</small>"),
                                    null, collectCildTotal(childTotal, format, isNegative).toArray(new String[]{}));
                        } else {
                            generateTreeTableRows(null, key, format, excludeZero, isNegative, table, childTotal);
                        }
                    });
                    table.addTotalRow(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.total), " ",
                            item.getGroupName()), collectTotal(item.getActualTotal(), format, isNegative));
                }
                return table;
            }
        }
        return null;
    }

    private void generateSummaryViewTreeTable(AccountItemWithBudgetDate key, ArrayList<BigDecimal> childTotal) {
        key.getChilds().forEach(child -> {
            generateSummaryViewTreeTable(child, childTotal);
            List<BudgetInDate> budgetInOneAccount = child.getRowCells();
            if (budgetInOneAccount != null && !child.isCalculated()) {
                child.setCalculated(true);
                calculate(childTotal, budgetInOneAccount);
            }
        });
        if (key.getRowCells() != null) {
            List<BudgetInDate> budgetInOneAccount = key.getRowCells();
            if (budgetInOneAccount != null && !key.isCalculated()) {
                key.setCalculated(true);
                calculate(childTotal, budgetInOneAccount);
            }
        }
    }

    private void generateTreeTableRows(ITextTreeObject to, AccountItemWithBudgetDate accItem1,
                                       DecimalFormat format, boolean excludeZero, boolean isNegative,
                                       CustomisedITextTable table, ArrayList<BigDecimal> childTotal) {
        boolean addRow = !excludeZero;
        to = addItemToList(to, accItem1, format, excludeZero, isNegative, table, addRow, !accItem1.getChilds().isEmpty());
        if (!accItem1.getChilds().isEmpty()) {
            ITextTreeObject finalTo = to;
            accItem1.getChilds().forEach(child -> {
                if (shownObjects.contains(accItem1.getCode())) {
                    generateTreeTableRows(finalTo, child, format, excludeZero, isNegative, table, childTotal);
                }
                List<BudgetInDate> budgetInOneAccount = child.getRowCells();
                if (budgetInOneAccount != null && !child.isCalculated()) {
                    child.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            });
            if (accItem1.getRowCells() != null) {
                List<BudgetInDate> budgetInOneAccount = accItem1.getRowCells();
                if (budgetInOneAccount != null && !accItem1.isCalculated()) {
                    accItem1.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            }
            boolean used = false;
            if (!accItem1.getChilds().isEmpty() && accItem1.getRowCells() != null && !accItem1.getRowCells().isEmpty()) {
                List<BudgetInDate> budgetInDates = accItem1.getRowCells();
                Collections.reverse(budgetInDates);
                for (BudgetInDate bItem : budgetInDates) {
                    if (bItem.getValue() != null) {
                        used = true;
                    }
                }
            }

            if (accItem1.getName() != null && !used) {
                List<BigDecimal> total = new ArrayList<>(childTotal);
                table.addTreeRows(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.total), " ", accItem1.getName()),
                        null, collectCildTotal(total, format, isNegative).toArray(new String[]{}));
            } else {
                ArrayList<BigDecimal> accountAndChildTotal = new ArrayList<>(tableColsCount - 1);
                for (int i = 0; i < tableColsCount; i++) {
                    accountAndChildTotal.add(i, BigDecimal.ZERO);
                }
                calculate(accountAndChildTotal, accItem1.getRowCells());
                for (AccountItemWithBudgetDate child : accItem1.getChilds()) {
                    List<BudgetInDate> budgetInOneAccount = child.getRowCells();
                    Collections.reverse(budgetInOneAccount);
                    if (budgetInOneAccount != null) {
                        calculate(accountAndChildTotal, budgetInOneAccount);
                    }
                }

                List<BigDecimal> total = new ArrayList<>(accountAndChildTotal);
                table.addTreeRows(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.total), " ", accItem1.getName()),
                        null, collectCildTotal(total, format, isNegative).toArray(new String[]{}));
            }
        }
    }

    private void calculate(ArrayList<BigDecimal> childTotal, List<BudgetInDate> budgetInOneAccount) {
        int i = 0;
        for (BigDecimal cellTotal : childTotal) {
            BigDecimal amount = budgetInOneAccount.get(i).getValue() != null
                    ? budgetInOneAccount.get(i).getValue()
                    : BigDecimal.ZERO;
            if (cellTotal == null) {
                childTotal.set(i, amount);
            } else {
                childTotal.set(i, cellTotal.add(amount));
            }
            i++;
        }
    }

    private ITextTreeObject addItemToList(ITextTreeObject to, AccountItemWithBudgetDate accItem1, DecimalFormat format,
                                          boolean excludeZero, boolean isNegative, CustomisedITextTable table, boolean addRow, boolean hasChilds) {
        List<String> values = new ArrayList<>();
        if (accItem1.getRowCells() != null) {
            Collections.reverse(accItem1.getRowCells());
            for (BudgetInDate bItem : accItem1.getRowCells()) {
                if (bItem.getValue() != null) {
                    if (excludeZero) {
                        if (bItem.getValue().compareTo(BigDecimal.ZERO) != 0) {
                            values.add(getValueAsString(bItem.getValue(), format, isNegative));
                            addRow = true;
                        } else {
                            values.add("");
                        }
                    } else {
                        values.add(getValueAsString(bItem.getValue(), format, isNegative));
                        addRow = true;
                    }
                } else {
                    values.add("");
                }
            }
        }
        if (addRow || hasChilds) {
            to = table.addTreeRows(accItem1.getName().concat("<small>").concat("(").concat(accItem1.getCode()).concat(")").concat("</small>"),
                    to, values.toArray(new String[]{}));
        }
        return to;
    }

    private LinkedList<String> collectCildTotal(List<BigDecimal> chilDTotals, DecimalFormat format, boolean isNegative) {
        LinkedList<String> total = new LinkedList<>();
        Collections.reverse(chilDTotals);
        for (BigDecimal bItem : chilDTotals) {
            if (bItem != null) {
                total.add(getValueAsString(bItem, format, isNegative));
            } else {
                total.add("");
            }
        }
        return total;
    }

    private LinkedList<String> collectTotal(ArrayList<BudgetInDate> actualTotal, DecimalFormat format, boolean isNegative) {
        LinkedList<String> total = new LinkedList<>();
        Collections.reverse(actualTotal);
        for (BudgetInDate bItem : actualTotal) {
            if (bItem.getValue() != null) {
                total.add(getValueAsString(bItem.getValue(), format, isNegative));
            } else {
                total.add("");
            }
        }
        return total;
    }

    private CustomisedITextTable getTotal(ActBudVar variance, String totalHeader, Integer calculationScale) {
        LinkedList<String> list = new LinkedList<>();
        list.add("");
        list.add(getValueAsString(variance != null && variance.getActual() != null ? variance.getActual().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
        list.add(getValueAsString(variance != null && variance.getBudget() != null ? variance.getBudget().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
        list.add(getValueAsString(variance != null && variance.getVariance() != null ? variance.getVariance().setScale(getCalculationScale(), BigDecimal.ROUND_HALF_UP) : ZERO, priceScaleNumberFormat, false));
        CustomisedITextTable table = new CustomisedITextTable();
        table.addTotalRow(totalHeader, list);
        return table;
    }

    private CustomisedITextTable collectGrandTotal(ArrayList<BudgetInDate> actualTotal, DecimalFormat format, boolean isNegative, boolean isGross) {
        CustomisedITextTable table = new CustomisedITextTable();
        if (actualTotal != null) {
            Collections.reverse(actualTotal);
            LinkedList<String> values = new LinkedList<>();
            for (BudgetInDate bItem : actualTotal) {
                if (bItem.getValue() != null) {
                    values.add(getValueAsString(bItem.getValue(), format, isNegative));
                } else {
                    values.add("");
                }
            }
            table.addTotalRow(commonLocalizer.localize(isGross ? PdfLocalizationName.grossProfit : PdfLocalizationName.netProfit), values);
            return table;
        }
        return null;
    }

    private String getValueAsString(BigDecimal value, DecimalFormat format, boolean isNegative) {
        if (isNegative) {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return "(" + format.format(value) + ")";
            } else {
                return format.format(value.abs());
            }
        } else {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return format.format(value);
            } else {
                return "(" + format.format(value.abs()) + ")";
            }
        }
    }

    private FromToDate[] getCompareDates(Date from, Integer compareWithId) {
        CompareWithEnum compareWithEnum = CompareWithEnum.getEnumyById(compareWithId);
        FromToDate[] compareDates = new FromToDate[compareWithEnum.getLength()];
        if (CompareCategoryEnum.Day.equals(compareWithEnum.getCompareCategoryEnum())) {
            Calendar fromCalendar = new GregorianCalendar();
            fromCalendar.setTime(from);
            fromCalendar.add(Calendar.DATE, -1);

            Calendar toCalendar = new GregorianCalendar();
            toCalendar.setTime(from);
            toCalendar.add(Calendar.DATE, -1);

            ServerUtils.setBeginningOfTheDay(fromCalendar);
            ServerUtils.setEndOfTheDay(toCalendar);

            compareDates[0] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
        } else if (CompareCategoryEnum.Week.equals(compareWithEnum.getCompareCategoryEnum())) {
            Calendar fromCalendar = new GregorianCalendar();
            fromCalendar.setTime(from);
            fromCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            fromCalendar.add(Calendar.DATE, -7);

            Calendar toCalendar = new GregorianCalendar();
            toCalendar.setTime(from);
            toCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            toCalendar.add(Calendar.DATE, -7);

            ServerUtils.setBeginningOfTheDay(fromCalendar);
            ServerUtils.setEndOfTheDay(toCalendar);

            compareDates[0] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
        } else if (CompareCategoryEnum.Month.equals(compareWithEnum.getCompareCategoryEnum())) {
            for (int i = 0; i < compareWithEnum.getLength(); i++) {
                Calendar fromCalendar = new GregorianCalendar();
                fromCalendar.setTime(from);
                fromCalendar.add(Calendar.MONTH, -(i + 1));

                Calendar toCalendar = new GregorianCalendar();
                toCalendar.setTime(from);
                toCalendar.add(Calendar.MONTH, -(i + 1));
                toCalendar.set(Calendar.DAY_OF_MONTH, toCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                ServerUtils.setBeginningOfTheDay(fromCalendar);
                ServerUtils.setEndOfTheDay(toCalendar);

                compareDates[i] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
            }
        } else if (CompareCategoryEnum.Year.equals(compareWithEnum.getCompareCategoryEnum())) {
            for (int i = 0; i < compareWithEnum.getLength(); i++) {
                Calendar fromCalendar = new GregorianCalendar();
                fromCalendar.setTime(from);
                fromCalendar.add(Calendar.YEAR, -(i + 1));

                Calendar toCalendar = new GregorianCalendar();
                toCalendar.setTime(fromCalendar.getTime());
                toCalendar.add(Calendar.YEAR, 1);
                toCalendar.add(Calendar.DATE, -1);

                ServerUtils.setBeginningOfTheDay(fromCalendar);
                ServerUtils.setEndOfTheDay(toCalendar);

                compareDates[i] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
            }
        }

        return compareDates;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Profit&Loss_" + dateFormat(user.getUserDate()));
    }


    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PROFIT_AND_LOSS;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.profitAndLoss);
    }
}
